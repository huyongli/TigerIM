package cn.ittiger.im.bean;

import cn.ittiger.database.annotation.Column;
import cn.ittiger.database.annotation.Table;
import cn.ittiger.im.constant.Constant;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.util.DateUtil;

import android.os.Parcel;

/**
 * 聊天记录实体对象
 *
 * @author: laohu on 2017/1/22
 * @site: http://ittiger.cn
 */
@Table(name = "ChatRecord")
public class ChatRecord extends ChatUser {
    /**
     * 最后聊天时间
     */
    @Column(columnName = "chatTime")
    private String mChatTime;
    /**
     * 朋友头像地址
     */
    @Column(columnName = "friendAvatar")
    private String mFriendAvatar;
    /**
     * 最后一条聊天记录
     */
    @Column(columnName = "lastMessage")
    private String mLastMessage;
    /**
     * 未读消息数
     */
    @Column(columnName = "unReadMessageCount")
    private int mUnReadMessageCount;

    public ChatRecord(ChatUser chatUser) {

        //减少服务请求
        setFriendUsername(chatUser.getFriendUsername());
        setFriendNickname(chatUser.getFriendNickname());
        setMeNickname(chatUser.getMeNickname());
        setMeUsername(chatUser.getMeUsername());
        setChatJid(chatUser.getChatJid());
        setFileJid(chatUser.getFileJid());
        setUuid(chatUser.getUuid());
        setChatTime(DateUtil.currentDatetime());
        setMulti(chatUser.isMulti());
    }

    public ChatRecord(ChatMessage chatMessage) {

        setFriendUsername(chatMessage.getFriendUsername());
        setMeUsername(chatMessage.getMeUsername());
        setMeNickname(chatMessage.getMeNickname());

        if(chatMessage.isMulti()) {//群发
            int idx = chatMessage.getFriendUsername().indexOf(Constant.MULTI_CHAT_ADDRESS_SPLIT);
            String friendNickName = chatMessage.getFriendUsername().substring(0, idx);
            setFriendNickname(friendNickName);//群聊记录显示群聊名称
            setChatJid(chatMessage.getFriendUsername());
            setMulti(chatMessage.isMulti());
        } else {
            setFriendNickname(chatMessage.getFriendNickname());
            String chatJid = SmackManager.getInstance().getChatJid(chatMessage.getFriendUsername());
            String fileJid = SmackManager.getInstance().getFileTransferJid(chatJid);
            setChatJid(chatJid);
            setFileJid(fileJid);
        }

        setChatTime(chatMessage.getDatetime());
        setLastMessage(chatMessage.getContent());
        setUuid(chatMessage.getUuid());
        updateUnReadMessageCount();
    }

    public String getFriendAvatar() {

        return mFriendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {

        mFriendAvatar = friendAvatar;
    }

    public String getChatTime() {

        return mChatTime == null ? DateUtil.currentDatetime() : mChatTime;
    }

    public void setChatTime(String chatTime) {

        mChatTime = chatTime;
    }

    public String getLastMessage() {

        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {

        mLastMessage = lastMessage;
    }

    public int getUnReadMessageCount() {

        return mUnReadMessageCount;
    }

    public void updateUnReadMessageCount() {

        mUnReadMessageCount += 1;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }
        if(obj instanceof ChatRecord) {
            return this.getUuid().equals(((ChatRecord) obj).getUuid());
        }
        return false;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeString(this.mChatTime);
        dest.writeString(this.mFriendAvatar);
        dest.writeString(this.mLastMessage);
        dest.writeInt(this.mUnReadMessageCount);
    }

    public ChatRecord() {

    }

    protected ChatRecord(Parcel in) {

        super(in);
        this.mChatTime = in.readString();
        this.mFriendAvatar = in.readString();
        this.mLastMessage = in.readString();
        this.mUnReadMessageCount = in.readInt();
    }

    public static final Creator<ChatRecord> CREATOR = new Creator<ChatRecord>() {
        @Override
        public ChatRecord createFromParcel(Parcel source) {

            return new ChatRecord(source);
        }

        @Override
        public ChatRecord[] newArray(int size) {

            return new ChatRecord[size];
        }
    };
}
