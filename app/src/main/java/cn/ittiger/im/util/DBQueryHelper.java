package cn.ittiger.im.util;

import cn.ittiger.im.bean.ChatRecord;
import cn.ittiger.im.bean.ChatUser;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

/**
 * 数据库查询帮助类
 *
 * @author: laohu on 2017/1/20
 * @site: http://ittiger.cn
 */
public class DBQueryHelper {

    /**
     * 根据好友信息查询对应的ChatUser，如果数据库中不存在则创建新的
     *
     * @param friendRoster  好友信息
     * @return
     */
     public static ChatUser queryChatUser(RosterEntry friendRoster) {

         return queryChatUser(friendRoster.getUser(), friendRoster.getName());
     }

    /**
     * 根据好友信息查询对应的ChatUser，如果数据库中不存在则创建新的
     *
     * @param friendUserName
     * @param friendNickName
     * @return
     */
    public static ChatUser queryChatUser(String friendUserName, String friendNickName) {

        String whereClause = "meUserName=? and friendUserName=?";
        String[] whereArgs = {LoginHelper.getUser().getUsername(), friendUserName};
        ChatUser chatUser = DBHelper.getInstance().getSQLiteDB().queryOne(ChatUser.class, whereClause, whereArgs);
        if(chatUser == null) {
            chatUser = new ChatUser(friendUserName, friendNickName);
            DBHelper.getInstance().getSQLiteDB().save(chatUser);
        }
        return chatUser;
    }

    /**
     * 查询登陆用户的所有聊天用户记录
     *
     * @return
     */
    public static List<ChatRecord> queryChatRecord() {

        String whereClause = "meUserName=?";
        String[] whereArgs = {LoginHelper.getUser().getUsername()};
        return DBHelper.getInstance().getSQLiteDB().query(ChatRecord.class, whereClause, whereArgs);
    }

    /**
     * 根据主键查询ChatRecord
     *
     * @param uuid
     * @return
     */
    public static ChatRecord queryChatRecord(String uuid) {

        return DBHelper.getInstance().getSQLiteDB().query(ChatRecord.class, uuid);
    }
}
