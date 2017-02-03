package cn.ittiger.im.smack;

import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.constant.MessageType;
import cn.ittiger.im.util.DBHelper;
import cn.ittiger.im.util.LoginHelper;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多人聊天消息监听
 * @author: laohu on 2017/1/24
 * @site: http://ittiger.cn
 */
public class MultiChatMessageListener implements MessageListener {
    private static final String PATTERN = "[a-zA-Z0-9_]+@";
    private String mMeNickName = LoginHelper.getUser().getNickname();
    private String mMeUserName = LoginHelper.getUser().getUsername();

    @Override
    public void processMessage(Message message) {

        //不会收到自己发送过来的消息
        Logger.d(message.toString());
        String from = message.getFrom();//消息发送人，格式:老胡创建的群@conference.121.42.13.79/老胡     --> 老胡发送的
        String to = message.getTo();//消息接收人(当前登陆用户)，格式:zhangsan@121.42.13.79/Smack
        Matcher matcherTo = Pattern.compile(PATTERN).matcher(to);

        if(matcherTo.find()) {
            try {
                String[] fromUsers = from.split("/");
                String friendUserName = fromUsers[0];//老胡创建的群@conference.121.42.13.79
                String friendNickName = fromUsers[1];//发送人的昵称，用于聊天窗口中显示

                JSONObject json = new JSONObject(message.getBody());

                ChatMessage chatMessage = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT.value(), false);
                chatMessage.setFriendUsername(friendUserName);
                chatMessage.setFriendNickname(friendNickName);
                chatMessage.setMeUsername(mMeUserName);
                chatMessage.setMeNickname(mMeNickName);
                chatMessage.setContent(json.optString(ChatMessage.KEY_MESSAGE_CONTENT));

                String sendUser = json.optString(ChatMessage.KEY_MULTI_CHAT_SEND_USER);
                chatMessage.setMeSend(mMeUserName.equals(sendUser));
                chatMessage.setMulti(true);

                DBHelper.getInstance().getSQLiteDB().save(chatMessage);
                EventBus.getDefault().post(chatMessage);
            } catch (Exception e) {
                Logger.e(e, "发送的消息格式不正确");
            }
        } else {
            Logger.e("发送人格式不正确");
        }
    }
}
