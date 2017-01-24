package cn.ittiger.im.bean;

import cn.ittiger.database.annotation.Column;
import cn.ittiger.database.annotation.PrimaryKey;
import cn.ittiger.database.annotation.Table;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.util.LoginHelper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/**
 * 聊天用户实体对象
 *
 * @author: laohu on 2017/1/19
 * @site: http://ittiger.cn
 */
@Table(name = "ChatDialog")
public class ChatUser implements Parcelable {
    /**
     *
     */
    @PrimaryKey
    private String uuid;
    /**
     * 聊天好友的用户名
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
     * 聊天JID
     */
    @Column(columnName = "chatJid")
    private String mChatJid;
    /**
     * 聊天时文件发送JID
     */
    @Column(columnName = "fileJid")
    private String mFileJid;


    public ChatUser() {

    }

    public ChatUser(String friendUsername, String friendNickname) {

        this();
        this.uuid = UUID.randomUUID().toString();
        mFriendUsername = friendUsername;
        mFriendNickname = friendNickname;

        mChatJid = SmackManager.getInstance().getChatJid(mFriendUsername);
        mFileJid = SmackManager.getInstance().getFileTransferJid(mChatJid);

        User user = LoginHelper.getUser();
        mMeUsername = user.getUsername();
        mMeNickname = user.getNickname();
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
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

    public String getChatJid() {

        return mChatJid;
    }

    public void setChatJid(String chatJid) {

        mChatJid = chatJid;
    }

    public String getFileJid() {

        return mFileJid;
    }

    public void setFileJid(String fileJid) {

        mFileJid = fileJid;
    }


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.uuid);
        dest.writeString(this.mFriendUsername);
        dest.writeString(this.mFriendNickname);
        dest.writeString(this.mMeUsername);
        dest.writeString(this.mMeNickname);
        dest.writeString(this.mChatJid);
        dest.writeString(this.mFileJid);
    }

    protected ChatUser(Parcel in) {

        this.uuid = in.readString();
        this.mFriendUsername = in.readString();
        this.mFriendNickname = in.readString();
        this.mMeUsername = in.readString();
        this.mMeNickname = in.readString();
        this.mChatJid = in.readString();
        this.mFileJid = in.readString();
    }

    public static final Creator<ChatUser> CREATOR = new Creator<ChatUser>() {
        @Override
        public ChatUser createFromParcel(Parcel source) {

            return new ChatUser(source);
        }

        @Override
        public ChatUser[] newArray(int size) {

            return new ChatUser[size];
        }
    };
}
