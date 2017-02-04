package cn.ittiger.im.util;

import cn.ittiger.app.AppContext;
import cn.ittiger.im.activity.ChatActivity;
import cn.ittiger.im.activity.MultiChatActivity;
import cn.ittiger.im.bean.ChatRecord;
import cn.ittiger.im.bean.ChatUser;
import cn.ittiger.util.ActivityUtil;
import cn.ittiger.util.PreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Field;

/**
 * @author: laohu on 2017/1/22
 * @site: http://ittiger.cn
 */
public final class IMUtil {

    private static final String KEY_STORE_KEYBOARD_HEIGHT = "_key_store_keyboard_height";
    private static int sKeyboardHeight = 0;
    private static int sStatusBarHeight = 0;

    public static int getStatusBarHeight() {

        if(sStatusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                sStatusBarHeight = AppContext.getInstance().getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sStatusBarHeight;
    }

    public static boolean isKeyboardHeightStored() {

        return getKeyboardHeight() > 0;
    }

    public static void storeKeyboardHeight(int keyboardHeight) {

        if(!isKeyboardHeightStored()) {
            PreferenceHelper.putInt(KEY_STORE_KEYBOARD_HEIGHT, keyboardHeight);
        }
    }

    public static int getKeyboardHeight() {

        if(sKeyboardHeight == 0) {
            sKeyboardHeight = PreferenceHelper.getInt(KEY_STORE_KEYBOARD_HEIGHT);
        }
        return sKeyboardHeight;
    }

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

        Intent intent;
        if(chatUser.isMulti()) {
            intent = new Intent(context, MultiChatActivity.class);
        } else {
            intent = new Intent(context, ChatActivity.class);
        }
        intent.putExtra(IntentHelper.KEY_CHAT_DIALOG, chatUser);
        ActivityUtil.startActivity(context, intent);
    }

    public static void startMultiChatActivity(Context context, MultiUserChat multiUserChat) {

        ChatUser chatUser = DBQueryHelper.queryChatUser(multiUserChat);
        ChatRecord chatRecord = DBQueryHelper.queryChatRecord(chatUser.getUuid());
        if(chatRecord == null) {
            chatRecord = new ChatRecord(chatUser);
        }
        EventBus.getDefault().post(chatRecord);//发起聊天时，发送一个事件到消息列表界面进行处理，如果不存在此聊天记录则创建一个新的

        startChatActivity(context, chatUser);
    }
}
