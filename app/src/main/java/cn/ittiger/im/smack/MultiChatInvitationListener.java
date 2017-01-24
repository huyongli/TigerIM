package cn.ittiger.im.smack;

import cn.ittiger.im.util.LoginHelper;

import com.orhanobut.logger.Logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * 多人聊天邀请监听
 * @author: laohu on 2017/1/24
 * @site: http://ittiger.cn
 */
public class MultiChatInvitationListener implements InvitationListener {

    @Override
    public void invitationReceived(XMPPConnection conn, MultiUserChat room, String inviter,
                                   String reason, String password, Message message) {

        try {
            room.join(LoginHelper.getUser().getNickname());
        } catch (Exception e) {
            Logger.e(e, "join multiChat failure on invitationReceived");
        }
    }
}
