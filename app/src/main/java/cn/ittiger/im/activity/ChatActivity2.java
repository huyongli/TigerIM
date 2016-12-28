package cn.ittiger.im.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ittiger.base.BaseActivity;
import cn.ittiger.im.R;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.util.SdCardUtil;
import cn.ittiger.util.UIUtil;
import cn.ittiger.util.ValueUtil;

/**
 * 聊天界面
 *
 * @auther: hyl
 * @time: 2015-10-26上午10:27:12
 */
public class ChatActivity2 extends BaseActivity {
    /**
     * 发送消息展示区
     */
    @BindView(R.id.tv_chat_content)
    TextView mTvMessageContent;
    /**
     * 编辑要发送的消息
     */
    @BindView(R.id.et_chat_msg)
    EditText mEtChatMessage;
    /**
     * 发送消息按钮
     */
    @BindView(R.id.btn_chat_msg_send)
    Button mBtnMsgSend;
    /**
     * 开始录音
     */
    @BindView(R.id.btn_start_record)
    Button mBtnStartRecordVoice;
    /**
     * 停止录音
     */
    @BindView(R.id.btn_stop_record)
    Button mBtnStopRecordVoice;
    /**
     * 发送语音消息
     */
    @BindView(R.id.btn_voice_msg_send)
    Button mBtnVoiceMsgSend;
    /**
     * 当前账户昵称
     */
    private String nickName;
    /**
     * 聊天窗
     */
    private Chat chat;
    /**
     * 当前聊天对象的JID
     */
    private String jid;
    /**
     * 文件发送对象
     */
    private String sendUser;
    private MediaRecorder recorder;//录音对象
    private File audioFile;//录音文件
    private File audioDir;//录音存储目录
    private File receiveDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2_layout);
        nickName = SmackManager.getInstance().getAccountName();

        jid = getFriendJid(nickName) + "@" + SmackManager.SERVER_IP;

        sendUser = jid + "/Smack";

        SmackManager.getInstance().getChatManager().addChatListener(chatManagerListener);
        chat = SmackManager.getInstance().createChat(jid);

        audioDir = new File(SdCardUtil.getRootPath());
        receiveDir = new File(SdCardUtil.getRootPath() + "/receive/");
        if (!receiveDir.exists()) {
            receiveDir.mkdir();
        }
        receiveFile();
    }

    public String getFriendJid(String userName) {

        String friendName = "";
        if ("admin".equals(userName)) {
            friendName = "laohu";
        } else {
            friendName = "admin";
        }
        RosterEntry friend = SmackManager.getInstance().getFriend(friendName);
        if (friend == null) {
            if ("admin".equals(userName)) {
                SmackManager.getInstance().addFriend("laohu", "laohu", null);
            } else {
                SmackManager.getInstance().addFriend("admin", "admin", null);
            }
        } else {
            return friend.getUser();
        }
        return getFriendJid(userName);
    }

    /**
     * 发送消息
     *
     * @param v
     */
    @OnClick(R.id.btn_chat_msg_send)
    public void onMessageSendClick(View v) {

        final String msg = mEtChatMessage.getText().toString();
        if (ValueUtil.isEmpty(msg)) {
            UIUtil.showToast(this, "请输入要发送的消息");
            return;
        }
        updateMsg(msg, nickName);
        new Thread() {
            public void run() {

                try {
                    chat.sendMessage(msg);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    /**
     * 更新展示发送、接收的消息
     *
     * @param msg
     * @param userName
     */
    public void updateMsg(String msg, String userName) {

        String disMsg = mTvMessageContent.getText().toString();
        if (!ValueUtil.isEmpty(disMsg)) {
            disMsg += "\n\n";
        }
        disMsg += userName + ":" + msg;
        mTvMessageContent.setText(disMsg);
        mEtChatMessage.setText("");
    }

    private ChatManagerListener chatManagerListener = new ChatManagerListener() {

        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {

            chat.addMessageListener(new ChatMessageListener() {

                @Override
                public void processMessage(Chat chat, Message message) {

                    final String from = message.getFrom();
                    final String msg = message.getBody();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            updateMsg(msg, from);
                        }
                    });
                }
            });
        }
    };

    protected void onStop() {

        super.onStop();
        SmackManager.getInstance().getChatManager().removeChatListener(chatManagerListener);
    }

    ;

    /**
     * 开始录音
     *
     * @param v
     */
    @OnClick(R.id.btn_start_record)
    public void onStartRecordVoiceClick(View v) {

        mBtnStartRecordVoice.setEnabled(false);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置音频采集原
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 内容输出格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码方式

            audioFile = new File(audioDir, format.format(new Date()) + ".3gp");
            if (!audioFile.exists()) {
                audioFile.createNewFile();
            }
            recorder.setOutputFile(audioFile.getAbsolutePath());

            recorder.prepare(); // 预期准备
            recorder.start();//开始录音

        } catch (IllegalStateException e) {
            recorder = null;
        } catch (IOException e) {
            recorder = null;
        }
    }

    /**
     * 停止录音
     *
     * @param v
     */
    @OnClick(R.id.btn_stop_record)
    public void onStopRecordVoiceClick(View v) {

        mBtnStartRecordVoice.setEnabled(true);
        recorder.stop();// 停止刻录
        recorder.reset();// 重设
        recorder.release();// 刻录完成一定要释放资源
        recorder = null;
    }

    /**
     * 发送语音消息
     *
     * @param v
     */
    @OnClick(R.id.btn_voice_msg_send)
    public void onVoiceMessageSend(View v) {

        sendFile(audioFile);
    }

    /**
     * 发送文件
     *
     * @param file
     */
    public void sendFile(final File file) {

        final OutgoingFileTransfer transfer = SmackManager.getInstance().getSendFileTransfer(sendUser);
        try {
            transfer.sendFile(file, "send file");
            checkTransferStatus(transfer, "发送");
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查发送文件、接收文件的状态
     *
     * @param transfer
     */
    public void checkTransferStatus(final FileTransfer transfer, final String tip) {

        new Thread() {
            public void run() {

                while (!transfer.isDone()) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (FileTransfer.Status.complete.equals(transfer.getStatus())) {
                    handler.obtainMessage(1, "语音" + tip + "成功").sendToTarget();
                    handler.obtainMessage(2, "语音" + tip + "成功").sendToTarget();
                } else if (FileTransfer.Status.cancelled.equals(transfer.getStatus())) {
                    handler.obtainMessage(1, "语音" + tip + "取消").sendToTarget();
                } else if (FileTransfer.Status.error.equals(transfer.getStatus())) {
                    handler.obtainMessage(1, "语音" + tip + "失败").sendToTarget();
                } else if (FileTransfer.Status.refused.equals(transfer.getStatus())) {
                    handler.obtainMessage(1, "语音" + tip + "拒绝").sendToTarget();
                }
            }

            ;
        }.start();
    }

    /**
     * 接收文件
     */
    public void receiveFile() {

        SmackManager.getInstance().addFileTransferListener(new FileTransferListener() {
            @Override
            public void fileTransferRequest(FileTransferRequest request) {
                // Accept it
                IncomingFileTransfer transfer = request.accept();
                try {

                    File file = new File(receiveDir + request.getFileName());

                    transfer.recieveFile(file);
                    checkTransferStatus(transfer, "接收");
                } catch (SmackException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1://状态提示
                    UIUtil.showToast(ChatActivity2.this, msg.obj.toString());
                    break;
                case 2://上传完成
                    updateMsg(msg.obj.toString(), nickName);
                    break;
            }
        }

        ;
    };
}
