package cn.ittiger.im.bean;

import cn.ittiger.im.constant.FileLoadState;
import cn.ittiger.im.constant.MessageType;
import cn.ittiger.util.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 聊天发送的消息
 *
 * @auther: hyl
 * @time: 2015-10-28下午5:16:13
 */
public class ChatMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String uuid;
    /**
     * 消息内容
     */
    private String mContent;
    /**
     * 消息类型
     */
    private MessageType mMessageType;
    /**
     * 消息发送人的用户名
     */
    private String mSendUsername;
    /**
     * 消息发送人的昵称
     */
    private String mSendNickname;
    /**
     * 消息发送接收的时间
     */
    private String mDatetime;
    /**
     * 当前消息是否是自己发出的
     */
    private boolean mIsSend;
    /**
     * 接收的图片或语音路径
     */
    private String mFilePath;
    /**
     * 文件加载状态
     */
    private FileLoadState mFileLoadState = FileLoadState.STATE_LOAD_START;

    public ChatMessage(MessageType messageType, boolean isSend) {

        mMessageType = messageType;
        mIsSend = isSend;

        this.uuid = UUID.randomUUID().toString();
        this.mDatetime = DateUtil.formatDatetime(new Date());
    }

    public String getContent() {

        return mContent;
    }

    public void setContent(String content) {

        mContent = content;
    }

    public MessageType getMessageType() {

        return mMessageType;
    }

    public String getSendUsername() {

        return mSendUsername;
    }

    public void setSendUsername(String sendUsername) {

        mSendUsername = sendUsername;
    }

    public String getSendNickname() {

        return mSendNickname;
    }

    public void setSendNickname(String sendNickname) {

        mSendNickname = sendNickname;
    }

    public String getDatetime() {

        return mDatetime;
    }

    public boolean isSend() {

        return mIsSend;
    }

    public void setSend(boolean send) {

        mIsSend = send;
    }

    public String getFilePath() {

        return mFilePath;
    }

    public void setFilePath(String filePath) {

        mFilePath = filePath;
    }

    public FileLoadState getFileLoadState() {

        return mFileLoadState;
    }

    public void setFileLoadState(FileLoadState fileLoadState) {

        mFileLoadState = fileLoadState;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (o instanceof ChatMessage) {
            return uuid.equals(((ChatMessage) o).uuid);
        }
        return false;
    }
}
