package cn.ittiger.im.smack;

import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.constant.MessageType;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Smack普通消息监听处理
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class TigerChatManagerListener implements ChatManagerListener {
    private static final String PATTERN = "[a-zA-Z0-9_]+@";

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {

        chat.addMessageListener(new ChatMessageListener() {

            @Override
            public void processMessage(Chat chat, Message message) {
                //不会收到自己发送过来的消息
                Logger.d(message.toString());
                String from = message.getFrom();//消息发送人，格式:laohu@171.17.100.201/Smack
                String to = message.getTo();//消息接收人(当前登陆用户)，格式:laohu@171.17.100.201/Smack
                Matcher matcherFrom = Pattern.compile(PATTERN).matcher(from);
                Matcher matcherTo = Pattern.compile(PATTERN).matcher(to);

                if(matcherFrom.find()) {
                    String fromUser = matcherFrom.group(0);
                    String toUser = matcherTo.group(0);

                    ChatMessage chatMessage = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT.value(), false);
                    chatMessage.setFriendUsername(fromUser);
                    chatMessage.setMeUsername(toUser);
                    chatMessage.setContent(message.getBody());
                    EventBus.getDefault().post(chatMessage);
                } else {
                    Logger.e("发送人格式不正确");
                }
            }
        });
    }
}
