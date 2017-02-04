package cn.ittiger.im.bean;

import cn.ittiger.database.annotation.Column;
import cn.ittiger.database.annotation.PrimaryKey;
import cn.ittiger.database.annotation.Table;
import cn.ittiger.im.constant.FileLoadState;
import cn.ittiger.im.constant.MessageType;
import cn.ittiger.util.DateUtil;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * 聊天发送的消息
 *
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
@Table(name = "ChatMessage")
public class ChatMessage implements Parcelable {
    public static final String KEY_FROM_NICKNAME = "fromNickName";
    public static final String KEY_MESSAGE_CONTENT = "messageContent";
    public static final String KEY_MULTI_CHAT_SEND_USER = "multiChatSendUser";
    /**
     *
     */
    @PrimaryKey
    private String uuid;
    /**
     * 消息内容
     */
    @Column(columnName = "message")
    private String mContent;
    /**
     * 消息类型  {@link MessageType}
     */
    @Column(columnName = "messageType")
    private int mMessageType;
    /**
     * 聊天好友的用户名,群聊时为群聊的jid,格式为：老胡创建的群@conference.121.42.13.79
     */
    @Column(columnName = "friendUserName")
    private String mFriendUsername;
    /**
     * 聊天好友的昵称
     */
    @Column(columnName = "friendNickName")
    private String mFriendNickname;
    /**
     * 自己的用户名
     */
    @Column(columnName = "meUserName")
    private String mMeUsername;
    /**
     * 自己的昵称
     */
    @Column(columnName = "meNickName")
    private String mMeNickname;
    /**
     * 消息发送接收的时间
     */
    @Column(columnName = "dateTime")
    private String mDatetime;
    /**
     * 当前消息是否是自己发出的
     */
    @Column(columnName = "isMeSend")
    private boolean mIsMeSend;
    /**
     * 接收的图片或语音路径
     */
    @Column(columnName = "filePath")
    private String mFilePath;
    /**
     * 文件加载状态 {@link FileLoadState}
     */
    @Column(columnName = "fileLoadState")
    private int mFileLoadState = FileLoadState.STATE_LOAD_START.value();
    /**
     * 是否为群聊记录
     */
    @Column(columnName = "isMulti")
    private boolean mIsMulti = false;

    public ChatMessage() {

    }

    public ChatMessage(int messageType, boolean isMeSend) {

        mMessageType = messageType;
        mIsMeSend = isMeSend;

        this.uuid = UUID.randomUUID().toString();
        this.mDatetime = DateUtil.formatDatetime(new Date());
    }

    public String getContent() {

        return mContent;
    }

    public void setContent(String content) {

        mContent = content;
    }

    public int getMessageType() {

        return mMessageType;
    }

    public void setMessageType(int messageType) {

        mMessageType = messageType;
    }

    public String getFriendUsername() {

        return mFriendUsername;
    }

    public void setFriendUsername(String friendUsername) {

        mFriendUsername = friendUsername;
    }

    public String getFriendNickname() {

        return mFriendNickname;
    }

    public void setFriendNickname(String friendNickname) {

        mFriendNickname = friendNickname;
    }

    public String getMeUsername() {

        return mMeUsername;
    }

    public void setMeUsername(String meUsername) {

        mMeUsername = meUsername;
    }

    public String getMeNickname() {

        return mMeNickname;
    }

    public void setMeNickname(String meNickname) {

        mMeNickname = meNickname;
    }

    public String getDatetime() {

        return mDatetime;
    }

    public void setDatetime(String datetime) {

        mDatetime = datetime;
    }

    public boolean isMeSend() {

        return mIsMeSend;
    }

    public void setMeSend(boolean meSend) {

        mIsMeSend = meSend;
    }

    public String getFilePath() {

        return mFilePath;
    }

    public void setFilePath(String filePath) {

        mFilePath = filePath;
    }

    public int getFileLoadState() {

        return mFileLoadState;
    }

    public void setFileLoadState(int fileLoadState) {

        mFileLoadState = fileLoadState;
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    public boolean isMulti() {

        return mIsMulti;
    }

    public void setMulti(boolean multi) {

        mIsMulti = multi;
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


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.uuid);
        dest.writeString(this.mContent);
        dest.writeInt(this.mMessageType);
        dest.writeString(this.mFriendUsername);
        dest.writeString(this.mFriendNickname);
        dest.writeString(this.mMeUsername);
        dest.writeString(this.mMeNickname);
        dest.writeString(this.mDatetime);
        dest.writeByte(this.mIsMeSend ? (byte) 1 : (byte) 0);
        dest.writeString(this.mFilePath);
        dest.writeInt(this.mFileLoadState);
        dest.writeByte(this.mIsMulti ? (byte) 1 : (byte) 0);
    }

    protected ChatMessage(Parcel in) {

        this.uuid = in.readString();
        this.mContent = in.readString();
        this.mMessageType = in.readInt();
        this.mFriendUsername = in.readString();
        this.mFriendNickname = in.readString();
        this.mMeUsername = in.readString();
        this.mMeNickname = in.readString();
        this.mDatetime = in.readString();
        this.mIsMeSend = in.readByte() != 0;
        this.mFilePath = in.readString();
        this.mFileLoadState = in.readInt();
        this.mIsMulti = in.readByte() != 0;
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel source) {

            return new ChatMessage(source);
        }

        @Override
        public ChatMessage[] newArray(int size) {

            return new ChatMessage[size];
        }
    };
}
