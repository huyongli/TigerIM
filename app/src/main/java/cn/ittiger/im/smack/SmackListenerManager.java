package cn.ittiger.im.smack;

/**
 * Smack全局监听器管理
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class SmackListenerManager {
    private static SmackListenerManager sSmackListenerManager = new SmackListenerManager();
    private TigerChatManagerListener mChatManagerListener;

    public static SmackListenerManager getInstance() {

        return sSmackListenerManager;
    }

    public static void addMessageListener() {

        if(getInstance().mChatManagerListener == null) {
            synchronized (getInstance()) {
                if(getInstance().mChatManagerListener == null) {
                    getInstance().mChatManagerListener = new TigerChatManagerListener();
                }
            }
        }
        SmackManager.getInstance().getChatManager().addChatListener(getInstance().mChatManagerListener);
    }

    public void destroy() {

        SmackManager.getInstance().getChatManager().removeChatListener(mChatManagerListener);
        mChatManagerListener = null;
    }
}
