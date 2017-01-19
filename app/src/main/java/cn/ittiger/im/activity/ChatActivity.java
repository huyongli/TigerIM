package cn.ittiger.im.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.ChatAdapter;
import cn.ittiger.im.bean.ChatDialog;
import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.constant.FileLoadState;
import cn.ittiger.im.constant.MessageType;
import cn.ittiger.im.ui.recyclerview.CommonRecyclerView;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.keyboard.ChatKeyboard;
import cn.ittiger.im.ui.keyboard.ChatKeyboard.ChatKeyboardOperateListener;
import cn.ittiger.im.util.AppFileHelper;
import cn.ittiger.im.util.DBHelper;
import cn.ittiger.im.util.IntentHelper;
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
public class ChatActivity extends IMBaseActivity implements ChatKeyboardOperateListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarTitle)
    TextView mToolbarTitle;
    /**
     * 聊天内容展示列表
     */
    @BindView(R.id.chat_content)
    CommonRecyclerView mChatMessageList;
    /**
     * 聊天输入控件
     */
    @BindView(R.id.ckb_chat_board)
    ChatKeyboard mChatKyboard;
    /**
     * 聊天窗口实体类
     */
    private ChatDialog mChatDialog;
    /**
     * 聊天窗口对象
     */
    private Chat mChat;
    /**
     * 聊天记录展示适配器
     */
    private ChatAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mChatDialog = getIntent().getParcelableExtra(IntentHelper.KEY_CHAT_DIALOG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(mChatDialog.getFriendNickname());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mChatKyboard.setChatKeyboardOperateListener(this);

        mChat = SmackManager.getInstance().createChat(mChatDialog.getChatJid());

        addReceiveFileListener();

        List<ChatMessage> list = new ArrayList<>();
        mAdapter = new ChatAdapter(this, list);
        mLayoutManager = new LinearLayoutManager(this);
        mChatMessageList.setLayoutManager(mLayoutManager);
        mChatMessageList.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {

        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {

        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessageEvent(ChatMessage message) {

        if(mChatDialog.getMeUsername().equals(message.getMeUsername())) {
            message.setFriendNickname(mChatDialog.getFriendNickname());
            message.setMeNickname(mChatDialog.getMeNickname());
            addChatMessageView(message);
            DBHelper.getInstance().getSQLiteDB().save(message);
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
            .subscribeOn(Schedulers.io())
            .map(new Func1<String, ChatMessage>() {
                @Override
                public ChatMessage call(String s) {

                    try {
                        mChat.sendMessage(message);
                        ChatMessage msg = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT.value(), true);
                        msg.setFriendNickname(mChatDialog.getFriendNickname());
                        msg.setFriendUsername(mChatDialog.getFriendUsername());
                        msg.setMeUsername(mChatDialog.getMeUsername());
                        msg.setMeNickname(mChatDialog.getMeNickname());
                        msg.setContent(message);
                        DBHelper.getInstance().getSQLiteDB().save(msg);
                        return msg;
                    } catch (NotConnectedException e) {
                        Logger.e(e, "send message failure");
                        return null;
                    }
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<ChatMessage>() {
                @Override
                public void call(ChatMessage chatMessage) {
                    addChatMessageView(chatMessage);
                }
            });
    }

    /**
     * 发送文件
     *
     * @param file
     */
    public void sendFile(final File file, int messageType) {

        final OutgoingFileTransfer transfer = SmackManager.getInstance().getSendFileTransfer(mChatDialog.getFileJid());
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
        msg.setFriendNickname(mChatDialog.getFriendNickname());
        msg.setFriendUsername(mChatDialog.getFriendUsername());
        msg.setMeUsername(mChatDialog.getMeUsername());
        msg.setMeNickname(mChatDialog.getMeNickname());
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
                } else if (FileTransfer.Status.cancelled.toString().equals(transfer.getStatus())) {
                    //传输取消
                    chatMessage.setFileLoadState(FileLoadState.STATE_LOAD_ERROR.value());
                    mAdapter.update(chatMessage);
                } else if (FileTransfer.Status.error.toString().equals(transfer.getStatus())) {
                    //传输错误
                    chatMessage.setFileLoadState(FileLoadState.STATE_LOAD_ERROR.value());
                    mAdapter.update(chatMessage);
                } else if (FileTransfer.Status.refused.toString().equals(transfer.getStatus())) {
                    //传输拒绝
                    chatMessage.setFileLoadState(FileLoadState.STATE_LOAD_ERROR.value());
                    mAdapter.update(chatMessage);
                }
            }
        });
    }

    private void addChatMessageView(ChatMessage message) {

        mAdapter.add(message);
        mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
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

    @Override
    public void recordStart() {

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
    public void functionClick(int index) {

        switch (index) {
            case 1://选择图片
                selectImage();
                break;
            case 2://拍照
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
