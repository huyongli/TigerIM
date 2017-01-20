package cn.ittiger.im.util;

import cn.ittiger.im.bean.ChatUser;

import org.jivesoftware.smack.roster.RosterEntry;

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

         String whereClause = "meUserName=? and friendUserName=?";
         String[] whereArgs = {LoginHelper.getUser().getUsername(), friendRoster.getUser()};
         ChatUser chatUser = DBHelper.getInstance().getSQLiteDB().queryOne(ChatUser.class, whereClause, whereArgs);
         if(chatUser == null) {
             chatUser = new ChatUser(friendRoster.getUser(), friendRoster.getName());
             DBHelper.getInstance().getSQLiteDB().save(chatUser);
         }
         return chatUser;
     }
}
