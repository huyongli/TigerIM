package cn.ittiger.im.activity;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.base.BaseActivity;
import cn.ittiger.im.R;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.util.UIUtil;
import cn.ittiger.util.ValueUtil;

/**
 * 多人聊天
 *
 * @auther: hyl
 * @time: 2015-10-27上午11:05:33
 */
public class MultiChatActivity extends BaseActivity {
    /**
     * 发送消息展示区
     */
    @BindView(R.id.tv_multichat_content)
    TextView mTvMessageContent;
    /**
     * 编辑要发送的消息
     */
    @BindView(R.id.et_multichat_msg)
    EditText mEtChatMessage;
    /**
     * 发送消息按钮
     */
    @BindView(R.id.btn_multichat_msg_send)
    Button mBtnMsgSend;
    /**
     * 当前账户昵称
     */
    String nickName;
    /**
     * 会议室聊天对象
     */
    private MultiUserChat chatRoom;
    /**
     * 会议室名字
     */
    private static final String ROOM_NAME = "chatroom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multichat_layout);
        ButterKnife.bind(this);

        nickName = SmackManager.getInstance().getAccountName();

        if ("admin".equals(nickName)) {
            chatRoom = SmackManager.getInstance().createChatRoom(ROOM_NAME, nickName, "");
        } else {
            chatRoom = SmackManager.getInstance().joinChatRoom(ROOM_NAME, nickName, "");
        }
        chatRoom.addMessageListener(messageListener);
    }

    private MessageListener messageListener = new MessageListener() {
        @Override
        public void processMessage(Message message) {

            final String from = message.getFrom();
            final String msg = message.getBody();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    updateMsg(msg, from);
                }
            });
        }
    };

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

    /**
     * 发送消息
     *
     * @param v
     */
    @OnClick(R.id.btn_multichat_msg_send)
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
                    chatRoom.sendMessage(msg);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    @Override
    protected void onStop() {

        super.onStop();
        chatRoom.removeMessageListener(messageListener);
    }
}
