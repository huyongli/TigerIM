package cn.ittiger.im.smack;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;

/**
 * Smack全局监听器管理
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class SmackListenerManager {
    private static volatile SmackListenerManager sSmackListenerManager;
    /**
     * 单聊消息监听管理器
     */
    private SmackChatManagerListener mChatManagerListener;
    /**
     * 群聊邀请监听
     */
    private MultiChatInvitationListener mInvitationListener;
    /**
     * 群聊消息监听
     */
    private MultiChatMessageListener mMultiChatMessageListener;
    /**
     * 群聊信息
     */
    private HashMap<String, MultiUserChat> mMultiUserChatHashMap = new HashMap<>();

    private SmackListenerManager() {

        mChatManagerListener = new SmackChatManagerListener();
        mInvitationListener = new MultiChatInvitationListener();
        mMultiChatMessageListener = new MultiChatMessageListener();
    }

    public static SmackListenerManager getInstance() {

        if(sSmackListenerManager == null) {
            synchronized (SmackListenerManager.class) {
                if(sSmackListenerManager == null) {
                    sSmackListenerManager = new SmackListenerManager();
                }
            }
        }
        return sSmackListenerManager;
    }

    public static void addGlobalListener() {

        addMessageListener();
        addInvitationListener();
        addAllMultiChatMessageListener();
    }

    /**
     * 添加单聊消息全局监听
     */
    static void addMessageListener() {

        SmackManager.getInstance().getChatManager().addChatListener(getInstance().mChatManagerListener);
    }

    /**
     * 添加群聊邀请监听
     */
    static void addInvitationListener() {

        SmackManager.getInstance().getMultiUserChatManager().addInvitationListener(getInstance().mInvitationListener);
    }

    /**
     * 为所有已存在的群添加消息监听
     */
    static void addAllMultiChatMessageListener() {

        //因Smack+openfire群聊在用户退出登陆后，群聊无法保存已加入的用户信息，所以手动添加该群中的用户
        SmackMultiChatManager.bindJoinMultiChat();
    }

    /**
     * 为指定群聊添加消息监听
     *
     * @param multiUserChat
     */
    public static void addMultiChatMessageListener(MultiUserChat multiUserChat) {

        if(multiUserChat == null) {
            return;
        }
        getInstance().mMultiUserChatHashMap.put(multiUserChat.getRoom(), multiUserChat);
        multiUserChat.addMessageListener(getInstance().mMultiChatMessageListener);
    }

    public void destroy() {

        SmackManager.getInstance().getChatManager().removeChatListener(mChatManagerListener);
        SmackManager.getInstance().getMultiUserChatManager().removeInvitationListener(mInvitationListener);

        for(MultiUserChat multiUserChat : mMultiUserChatHashMap.values()) {
            multiUserChat.removeMessageListener(mMultiChatMessageListener);
        }

        mChatManagerListener = null;
        mInvitationListener = null;
        mMultiChatMessageListener = null;
        mMultiUserChatHashMap.clear();
    }
}
