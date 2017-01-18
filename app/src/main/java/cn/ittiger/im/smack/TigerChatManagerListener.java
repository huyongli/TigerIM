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
                String from = message.getFrom();//格式:laohu@171.17.100.201/Smack
                Matcher matcher = Pattern.compile(PATTERN).matcher(from);

                if(matcher.find()) {
                    String fromUser = matcher.group(0);

                    ChatMessage chatMessage = new ChatMessage(MessageType.MESSAGE_TYPE_TEXT, false);
                    chatMessage.setSendUsername(fromUser);
                    chatMessage.setContent(message.getBody());
                    EventBus.getDefault().post(chatMessage);
                } else {
                    Logger.e("发送人格式不正确");
                }
            }
        });
    }
}
