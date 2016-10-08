package cn.ittiger.im.bean;

import java.io.Serializable;
import java.util.UUID;

/**
 * 聊天发送的消息
 * @auther: hyl
 * @time: 2015-10-28下午5:16:13
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String uuid;
	public String getUuid() {
		return uuid;
	}

	/**
	 * 文本消息类型
	 */
	public static final int MESSAGE_TYPE_TEXT = 0;
	/**
	 * 图片消息类型
	 */
	public static final int MESSAGE_TYPE_IMAGE = 1;
	/**
	 * 语音消息类型
	 */
	public static final int MESSAGE_TYPE_VOICE = 2;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 消息类型，0：文本，1：图片，2：语音
	 */
	private int type;
	/**
	 * 消息发送人
	 */
	private String username;
	/**
	 * 消息发送接收的时间
	 */
	private String datetime;
	/**
	 * 当前消息是否是自己发出的
	 */
	private boolean isSend;
	/**
	 * 接收的图片或语音路径
	 */
	private String filePath;
	/**
	 * 文件加载状态,0:加载开始，1：加载成功，-1：加载失败
	 */
	private int loadState = 0;
	
	public Message(int type, String username, String datetime, boolean isSend) {
		super();
		this.type = type;
		this.username = username;
		this.datetime = datetime;
		this.isSend = isSend;
		
		this.uuid = UUID.randomUUID().toString();
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public boolean isSend() {
		return isSend;
	}

	public void setSend(boolean isSend) {
		this.isSend = isSend;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getLoadState() {
		return loadState;
	}

	public void setLoadState(int loadState) {
		this.loadState = loadState;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(o instanceof Message) {
			return uuid.equals(((Message) o).uuid);
		}
		return false;
	}
}
