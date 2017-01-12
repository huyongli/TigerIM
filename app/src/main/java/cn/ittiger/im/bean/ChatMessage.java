package cn.ittiger.im.bean;

import java.io.Serializable;
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

    public String getUuid() {

        return uuid;
    }

    /**
     * 消息内容
     */
    private String mContent;
    /**
     * 消息类型
     */
    private MessageType mMessageType;
    /**
     * 消息发送人
     */
    private String mSendUserName;
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
     * 文件加载状态,0:加载开始，1：加载成功，-1：加载失败
     */
    private int mLoadState = 0;

    public ChatMessage(MessageType type, String username, String datetime, boolean isSend) {

        super();

        this.mMessageType = type;
        this.mSendUserName = username;
        this.mDatetime = datetime;
        this.mIsSend = isSend;
        this.uuid = UUID.randomUUID().toString();
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

    public void setMessageType(MessageType messageType) {

        mMessageType = messageType;
    }

    public String getSendUserName() {

        return mSendUserName;
    }

    public void setSendUserName(String sendUserName) {

        mSendUserName = sendUserName;
    }

    public String getDatetime() {

        return mDatetime;
    }

    public void setDatetime(String datetime) {

        mDatetime = datetime;
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

    public int getLoadState() {

        return mLoadState;
    }

    public void setLoadState(int loadState) {

        mLoadState = loadState;
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
