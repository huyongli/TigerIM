package cn.ittiger.im.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.ChatAdapter;
import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.bean.MessageType;
import cn.ittiger.im.util.ImageLoaderHelper;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.keyboard.ChatKeyboard;
import cn.ittiger.im.ui.keyboard.ChatKeyboard.ChatKeyboardOperateListener;
import cn.ittiger.im.util.IntentHelper;
import cn.ittiger.util.BitmapUtil;
import cn.ittiger.util.DateUtil;
import cn.ittiger.util.FileUtil;
import cn.ittiger.util.SdCardUtil;
import cn.ittiger.util.ValueUtil;

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
    @BindView(R.id.lv_chat_content)
    ListView mListView;
    /**
     * 聊天输入控件
     */
    @BindView(R.id.ckb_chat_board)
    ChatKeyboard mChatKyboard;
    /**
     * 聊天对象用户Jid
     */
    private String mFriendUser;
    /**
     * 聊天对象昵称
     */
    private String mFriendName;
    /**
     * 聊天窗口对象
     */
    private Chat mChat;
    /**
     * 当前自己昵称
     */
    private String mMeName;
    /**
     * 聊天记录展示适配器
     */
    private ChatAdapter mAdapter;

    /**
     * 文件发送对象
     */
    private String mFileSendJid;
    /**
     * 文件存储目录
     */
    private String mFileDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFriendUser = getIntent().getStringExtra(IntentHelper.KEY_CHAT_USER);
        mFriendName = getIntent().getStringExtra(IntentHelper.KEY_CHAT_NAME);
        mMeName = SmackManager.getInstance().getAccountName();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(mFriendName);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mChatKyboard.setChatKeyboardOperateListener(this);

        String chatJid = SmackManager.getInstance().getChatJidByUser(mFriendUser);
        mFileSendJid = SmackManager.getInstance().getFileTransferJidChatJid(chatJid);
        SmackManager.getInstance().getChatManager().addChatListener(mChatManagerListener);
        mChat = SmackManager.getInstance().createChat(chatJid);

        mFileDir = SdCardUtil.getCacheDir(this);
        addReceiveFileListener();

        List<ChatMessage> list = new ArrayList<>();
        mAdapter = new ChatAdapter(this, ImageLoaderHelper.getChatImageOptions(), list);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 消息接收处理器
     */
    private ChatManagerListener mChatManagerListener = new ChatManagerListener() {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {

            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    //接收到消息Message之后进行消息展示处理，这个地方可以处理所有人的消息
                    ChatMessage msg = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT, mFriendName, DateUtil.formatDatetime(new Date()), false);
                    msg.setContent(message.getBody());
                    handler.obtainMessage(1, msg).sendToTarget();
                }
            });
        }
    };

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
        new Thread() {
            public void run() {

                try {
                    mChat.sendMessage(message);
                    ChatMessage msg = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT, mMeName, DateUtil.formatDatetime(new Date()), true);
                    msg.setContent(message);
                    handler.obtainMessage(1, msg).sendToTarget();
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();

    }

    ;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    mAdapter.add((ChatMessage) msg.obj);
                    break;
                case 2:
                    mAdapter.update((ChatMessage) msg.obj);
                    break;
            }
        }

        ;
    };

    /**
     * 发送文件
     *
     * @param file
     */
    public void sendFile(final File file, MessageType messageType) {

        final OutgoingFileTransfer transfer = SmackManager.getInstance().getSendFileTransfer(mFileSendJid);
        try {
            transfer.sendFile(file, String.valueOf(messageType.value()));
            checkTransferStatus(transfer, file, messageType, true);
        } catch (SmackException e) {
            e.printStackTrace();
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
                    String type = request.getDescription();
                    File file = new File(mFileDir, request.getFileName());
                    transfer.recieveFile(file);
                    checkTransferStatus(transfer, file, MessageType.getMessageType(Integer.parseInt(type)), false);
                } catch (SmackException | IOException e) {
                    e.printStackTrace();
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
     * @param isSend            是否为发送
     */
    public void checkTransferStatus(final FileTransfer transfer, final File file, final MessageType messageType, final boolean isSend) {

        String username = mFriendName;
        if (isSend) {
            username = mMeName;
        }
        final String name = username;
        final ChatMessage msg = new ChatMessage(messageType, name, DateUtil.formatDatetime(new Date()), isSend);
        msg.setFilePath(file.getAbsolutePath());
        msg.setLoadState(0);
        new Thread() {
            public void run() {

                if (transfer.getProgress() < 1) {//传输开始
                    handler.obtainMessage(1, msg).sendToTarget();
                }
                while (!transfer.isDone()) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (FileTransfer.Status.complete.equals(transfer.getStatus())) {//传输完成
                    msg.setLoadState(1);
                    handler.obtainMessage(2, msg).sendToTarget();
                } else if (FileTransfer.Status.cancelled.equals(transfer.getStatus())) {
                    //传输取消
                    msg.setLoadState(-1);
                    handler.obtainMessage(2, msg).sendToTarget();
                } else if (FileTransfer.Status.error.equals(transfer.getStatus())) {
                    //传输错误
                    msg.setLoadState(-1);
                    handler.obtainMessage(2, msg).sendToTarget();
                } else if (FileTransfer.Status.refused.equals(transfer.getStatus())) {
                    //传输拒绝
                    msg.setLoadState(-1);
                    handler.obtainMessage(2, msg).sendToTarget();
                }
            }

            ;
        }.start();
    }

    protected void onDestroy() {

        super.onDestroy();
        SmackManager.getInstance().getChatManager().removeChatListener(mChatManagerListener);
    }

    /**
     * 发送语音消息
     *
     * @param audioFile
     */
    @Override
    public void sendVoice(File audioFile) {

        sendFile(audioFile, MessageType.MESSAGE_TYPE_VOICE);
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

        mPicPath = mFileDir + "/" + DateUtil.formatDatetime(new Date(), "yyyyMMddHHmmss") + ".png";
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
                    sendFile(file, MessageType.MESSAGE_TYPE_IMAGE);
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
        sendFile(new File(mPicPath), MessageType.MESSAGE_TYPE_IMAGE);
    }
}
