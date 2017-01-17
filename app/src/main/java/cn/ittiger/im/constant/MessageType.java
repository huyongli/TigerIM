package cn.ittiger.im.constant;

/**
 * 消息类型
 * @author: laohu on 2017/1/12
 * @site: http://ittiger.cn
 */
public enum MessageType {
    /**
     * 文本消息类型
     */
    MESSAGE_TYPE_TEXT(0),
    /**
     * 图片消息类型
     */
    MESSAGE_TYPE_IMAGE(1),
    /**
     * 语音消息类型
     */
    MESSAGE_TYPE_VOICE(2);

    int value;
    MessageType(int value) {

        this.value = value;
    }

    public int value() {

        return value;
    }

    public static MessageType getMessageType(int value) {

        if (value == MESSAGE_TYPE_IMAGE.value()) {
            return MESSAGE_TYPE_IMAGE;
        } else if(value == MESSAGE_TYPE_TEXT.value()) {
            return MESSAGE_TYPE_TEXT;
        } else {
            return MESSAGE_TYPE_VOICE;
        }
    }
}
