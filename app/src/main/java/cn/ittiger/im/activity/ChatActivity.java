package cn.ittiger.im.activity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import cn.ittiger.im.R;
import cn.ittiger.im.activity.base.BaseChatActivity;
import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.constant.FileLoadState;
import cn.ittiger.im.constant.KeyBoardMoreFunType;
import cn.ittiger.im.constant.MessageType;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.util.AppFileHelper;
import cn.ittiger.im.util.DBHelper;
import cn.ittiger.util.BitmapUtil;
import cn.ittiger.util.DateUtil;
import cn.ittiger.util.FileUtil;
import cn.ittiger.util.ValueUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import com.orhanobut.logger.Logger;

/**
 * 单聊窗口
 * @author: laohu on 2017/1/12
 * @site: http://ittiger.cn
 */
public class ChatActivity extends BaseChatActivity {
    /**
     * 聊天窗口对象
     */
    private Chat mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);
        mChat = SmackManager.getInstance().createChat(mChatUser.getChatJid());
        addReceiveFileListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveChatMessageEvent(ChatMessage message) {

        if(mChatUser.getMeUsername().equals(message.getMeUsername()) && !message.isMulti()) {
            addChatMessageView(message);
        }
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void send(final String message) {

        if (ValueUtil.isEmpty(message)) {
            return;
        }
        Observable.just(message)
            .observeOn(Schedulers.io())
            .subscribe(new Action1<String>() {
                @Override
                public void call(String message) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put(ChatMessage.KEY_FROM_NICKNAME, mChatUser.getMeNickname());
                        json.put(ChatMessage.KEY_MESSAGE_CONTENT, message);
                        mChat.sendMessage(json.toString());

                        ChatMessage msg = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT.value(), true);
                        msg.setFriendNickname(mChatUser.getFriendNickname());
                        msg.setFriendUsername(mChatUser.getFriendUsername());
                        msg.setMeUsername(mChatUser.getMeUsername());
                        msg.setMeNickname(mChatUser.getMeNickname());
                        msg.setContent(message);

                        DBHelper.getInstance().getSQLiteDB().save(msg);
                        EventBus.getDefault().post(msg);
                    } catch (Exception e) {
                        Logger.e(e, "send message failure");
                    }
                }
            });
    }

    /**
     * 发送文件
     *
     * @param file
     */
    public void sendFile(final File file, int messageType) {

        final OutgoingFileTransfer transfer = SmackManager.getInstance().getSendFileTransfer(mChatUser.getFileJid());
        try {
            transfer.sendFile(file, String.valueOf(messageType));
            checkTransferStatus(transfer, file, messageType, true);
        } catch (SmackException e) {
            Logger.e(e, "send file failure");
        }
    }

    /**
     * 接收文件
     */
    public void addReceiveFileListener() {

        SmackManager.getInstance().addFileTransferListener(new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest request) {
                // Accept it
                IncomingFileTransfer transfer = request.accept();
                try {
                    int messageType = Integer.parseInt(request.getDescription());

                    File dir = AppFileHelper.getAppChatMessageDir(messageType);
                    File file = new File(dir, request.getFileName());
                    transfer.recieveFile(file);
                    checkTransferStatus(transfer, file, messageType, false);
                } catch (SmackException | IOException e) {
                    Logger.e(e, "receive file failure");
                }
            }
        });
    }

    /**
     * 检查发送文件、接收文件的状态
     *
     * @param transfer
     * @param file              发送或接收的文件
     * @param messageType       文件类型，语音或图片
     * @param isMeSend            是否为发送
     */
    private void checkTransferStatus(final FileTransfer transfer, final File file, final int messageType, final boolean isMeSend) {

        final ChatMessage msg = new ChatMessage(messageType, isMeSend);
        msg.setFriendNickname(mChatUser.getFriendNickname());
        msg.setFriendUsername(mChatUser.getFriendUsername());
        msg.setMeUsername(mChatUser.getMeUsername());
        msg.setMeNickname(mChatUser.getMeNickname());
        msg.setFilePath(file.getAbsolutePath());
        DBHelper.getInstance().getSQLiteDB().save(msg);

        Observable.create(new Observable.OnSubscribe<ChatMessage>(){
            @Override
            public void call(Subscriber<? super ChatMessage> subscriber) {
                addChatMessageView(msg);
                subscriber.onNext(msg);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.io())
        .map(new Func1<ChatMessage, ChatMessage>() {
            @Override
            public ChatMessage call(ChatMessage chatMessage) {

                while (!transfer.isDone()) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return chatMessage;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ChatMessage>() {
            @Override
            public void call(ChatMessage chatMessage) {
                if (FileTransfer.Status.complete.toString().equals(transfer.getStatus())) {//传输完成
                    chatMessage.setFileLoadState(FileLoadState.STATE_LOAD_SUCCESS.value());
                    mAdapter.update(chatMessage);
                } else {
                    chatMessage.setFileLoadState(FileLoadState.STATE_LOAD_ERROR.value());
                    mAdapter.update(chatMessage);
                }
            }
        });
    }

    /**
     * 发送语音消息
     *
     * @param audioFile
     */
    @Override
    public void sendVoice(File audioFile) {

        sendFile(audioFile, MessageType.MESSAGE_TYPE_VOICE.value());
    }

    /**
     * 选择图片
     */
    private static final int REQUEST_CODE_GET_IMAGE = 1;
    /**
     * 拍照
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;

    @Override
    public void functionClick(KeyBoardMoreFunType funType) {

        switch (funType) {
            case FUN_TYPE_IMAGE://选择图片
                selectImage();
                break;
            case FUN_TYPE_TAKE_PHOTO://拍照
                takePhoto();
                break;
        }
    }

    /**
     * 从图库选择图片
     */
    public void selectImage() {

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GET_IMAGE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GET_IMAGE);
        }
    }

    private String mPicPath = "";

    /**
     * 拍照
     */
    public void takePhoto() {

        String dir = AppFileHelper.getAppChatMessageDir(MessageType.MESSAGE_TYPE_IMAGE.value()).getAbsolutePath();
        mPicPath = dir + "/" + DateUtil.formatDatetime(new Date(), "yyyyMMddHHmmss") + ".png";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPicPath)));
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {//拍照成功
                takePhotoSuccess();
            } else if (requestCode == REQUEST_CODE_GET_IMAGE) {//图片选择成功
                Uri dataUri = data.getData();
                if (dataUri != null) {
                    File file = FileUtil.uri2File(this, dataUri);
                    sendFile(file, MessageType.MESSAGE_TYPE_IMAGE.value());
                }
            }
        }
    }

    /**
     * 照片拍摄成功
     */
    public void takePhotoSuccess() {

        Bitmap bitmap = BitmapUtil.createBitmapWithFile(mPicPath, 640);
        BitmapUtil.createPictureWithBitmap(mPicPath, bitmap, 80);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        sendFile(new File(mPicPath), MessageType.MESSAGE_TYPE_IMAGE.value());
    }
}
