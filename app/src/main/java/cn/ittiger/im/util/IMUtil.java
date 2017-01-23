package cn.ittiger.im.util;

import cn.ittiger.im.activity.ChatActivity;
import cn.ittiger.im.bean.ChatRecord;
import cn.ittiger.im.bean.ChatUser;
import cn.ittiger.util.ActivityUtil;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.roster.RosterEntry;

import android.content.Context;
import android.content.Intent;

/**
 * @author: laohu on 2017/1/22
 * @site: http://ittiger.cn
 */
public final class IMUtil {

    /**
     * 从联系人列表中发起聊天跳转到聊天界面
     *
     * @param context
     * @param rosterEntry
     */
    public static void startChatActivity(Context context, RosterEntry rosterEntry) {

        ChatUser chatUser = DBQueryHelper.queryChatUser(rosterEntry);

        ChatRecord chatRecord = DBQueryHelper.queryChatRecord(chatUser.getUuid());
        if(chatRecord == null) {
            chatRecord = new ChatRecord(chatUser);
        }
        EventBus.getDefault().post(chatRecord);//发起聊天时，发送一个事件到消息列表界面进行处理，如果不存在此聊天记录则创建一个新的

        startChatActivity(context, chatUser);
    }

    public static void startChatActivity(Context context, ChatUser chatUser) {

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IntentHelper.KEY_CHAT_DIALOG, chatUser);
        ActivityUtil.startActivity(context, intent);
    }
}
