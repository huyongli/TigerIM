package cn.ittiger.im.smack;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

public class SmackManager {
	private static final String TAG = "SmackManager";
	/**
	 * Xmpp服务器地址
	 */
	public static final String SERVER_IP = "121.42.13.79";
    /**
     * Xmpp 服务器端口
     */
    private static final int PORT = 5222;
    /**
     * 服务器名称
     */
    public static final String SERVER_NAME = "121.42.13.79";
    /**
     * 
     */
    public static final String XMPP_CLIENT = "Smack";
    
    private static volatile SmackManager sSmackManager;
    /**
     * 连接
     */
    private XMPPTCPConnection mConnection;
    
    private SmackManager() {
    	this.mConnection = connect();
	}
    
    /**
     * 获取操作实例
     * @return
     */
    public static SmackManager getInstance() {
    	if(sSmackManager == null) {
    		synchronized (SmackManager.class) {
				if(sSmackManager == null) {
					sSmackManager = new SmackManager();
				}
			}
    	}
    	return sSmackManager;
    }
    
    /**
     * 连接服务器
     * @return
     */
    private XMPPTCPConnection connect() {
    	try {
			SmackConfiguration.setDefaultPacketReplyTimeout(10000);
        	XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
			//是否开启安全模式
			.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
			//服务器名称
			.setServiceName(SERVER_NAME)
			.setHost(SERVER_IP)//服务器IP地址
			//服务器端口
			.setPort(PORT)
        	//是否开启压缩
        	.setCompressionEnabled(false)
        	//开启调试模式
        	.setDebuggerEnabled(true).build();
        	
        	XMPPTCPConnection connection = new XMPPTCPConnection(config);
            connection.connect();
            return connection;
        } catch (Exception e) {
        	return null;
        }
    }
    
    /**
     * 登陆
     * @param user			用户账号
     * @param password		用户密码
     * @return
     * @throws Exception 
     */
    public boolean login(String user, String password) throws Exception {
    	if(!isConnected()) {
			throw new IllegalStateException("服务器断开，请先连接服务器");
    	}
        try {
			mConnection.login(user, password);
            return  true;
        } catch (Exception e) {
			Logger.e(TAG, e, "login failure");
            throw e;
        }
    }

    /**
     * 注销
     * @return
     */
    public boolean logout() {
    	if(!isConnected()) {
    		return false;
    	}
        try {
			mConnection.instantShutdown();
            return  true;
        } catch (Exception e) {
			Logger.e(TAG, e, "logout failure");
            return  false;
        }
    }
    
    /**
     * 删除当前登录的用户信息(从服务器上删除当前用户账号)
     * @return
     */
    public boolean deleteUser() {
    	if(!isConnected()) {
    		return false;
    	}
    	try {
			AccountManager.getInstance(mConnection).deleteAccount();//删除该账号
			return true;
		} catch (NoResponseException | XMPPErrorException
				| NotConnectedException e) {
			return false;
		}
    }
    
    /**
     * 注册用户信息
     * @param username		账号
     * @param password		账号密码
     * @param attributes	账号其他属性，参考AccountManager.getAccountAttributes()的属性介绍
     * @return
     */
    public boolean registerUser(String username, String password, Map<String, String> attributes) {
    	if(!isConnected()) {
			return false;
    	}
    	try {
			AccountManager.getInstance(mConnection).createAccount(username, password, attributes);
			return true;
		} catch (NoResponseException | XMPPErrorException
				| NotConnectedException e) {
			Logger.e(TAG, "register failure", e);
			return false;
		}
    }
    
    /**
     * 修改密码
     * @param newpassword	新密码
     * @return
     */
    public boolean changePassword(String newpassword) {
    	if(!isConnected()) {
    		return false;
    	}
		try {
			AccountManager.getInstance(mConnection).changePassword(newpassword);
			return true;
		} catch (NoResponseException | XMPPErrorException | NotConnectedException e) {
			Logger.e(TAG, "change password failure", e);
			return false;
		}
    }
    
    /**
     * 断开连接，注销
     * @return
     */
    public boolean disconnect() {
    	if(!isConnected()) {
    		return false;
    	}
		mConnection.disconnect();
    	return true;
    }
    
    /**
     * 更新用户状态
     * @param code
     * @return
     */
    public boolean updateUserState(int code) {
    	if(!isConnected()) {
    		throw new NullPointerException("服务器连接失败，请先连接服务器");
    	}
    	try {
	    	Presence presence;  
	        switch (code) {  
	            case 0://设置在线
	                presence = new Presence(Presence.Type.available);
	                presence.setMode(Presence.Mode.available);
					mConnection.sendStanza(presence);
	                break;  
	            case 1://设置Q我吧
	                presence = new Presence(Presence.Type.available);  
	                presence.setMode(Presence.Mode.chat);
					mConnection.sendStanza(presence);
	                break;  
	            case 2://设置忙碌
	                presence = new Presence(Presence.Type.available);  
	                presence.setMode(Presence.Mode.dnd);
					mConnection.sendStanza(presence);
	                break;  
	            case 3://设置离开
	                presence = new Presence(Presence.Type.available);  
	                presence.setMode(Presence.Mode.away);
					mConnection.sendStanza(presence);
	                break;  
	            case 4://设置隐身
	//                Roster roster = connection.getRoster();  
	//                Collection<RosterEntry> entries = roster.getEntries();  
	//                for (RosterEntry entry : entries) {  
	//                    presence = new Presence(Presence.Type.unavailable);  
	//                    presence.setStanzaId(Stanza.ID_NOT_AVAILABLE);  
	//                    presence.setFrom(connection.getUser());  
	//                    presence.setTo(entry.getUser());  
	//                    connection.sendStanza(presence);  
	//                }
	//                // 向同一用户的其他客户端发送隐身状态  
	//                presence = new Presence(Presence.Type.unavailable);  
	//                presence.setStanzaId(Packet.ID_NOT_AVAILABLE);  
	//                presence.setFrom(connection.getUser());  
	//                presence.setTo(StringUtils.parseBareAddress(connection.getUser()));  
	//                connection.sendStanza(presence);
	                break;  
	            case 5://设置离线
	                presence = new Presence(Presence.Type.unavailable);
					mConnection.sendStanza(presence);
	                break;
	            default:  
	                break;  
	            } 
	        return true;
    	} catch (NotConnectedException e) {
    		e.printStackTrace();
    	}  
    	return false;
    }
    
    /**
     * 是否连接成功
     * @return
     */
    private boolean isConnected() {
    	if(mConnection == null) {
    		return false;
    	}
    	if(!mConnection.isConnected()) {
    		try {
				mConnection.connect();
				return true;
			} catch (SmackException | IOException | XMPPException e) {
				return false;
			}
    	}
    	return true;
    }
    
    /**
     * 获取账户昵称
     * @return
     */
    public String getAccountName() {
    	if(isConnected()) {
    		try {
    			return AccountManager.getInstance(mConnection).getAccountAttribute("name");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 获取账户所有属性信息
     * @return
     */
    public Set<String> getAccountAttributes() {
    	if(isConnected()) {
    		try {
    			return AccountManager.getInstance(mConnection).getAccountAttributes();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 创建聊天窗口
     * @param jid   好友的JID
     * @return
     */
    public Chat createChat(String jid) {
    	if(isConnected()) {
    		ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
    		return chatManager.createChat(jid);
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 获取聊天对象管理器
     * @return
     */
    public ChatManager getChatManager() {
    	if(isConnected()) {
    		ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
    		return chatManager;
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 获取当前登录用户的所有好友信息
     * @return
     */
    public Set<RosterEntry> getAllFriends() {
    	if(isConnected()) {
    		return Roster.getInstanceFor(mConnection).getEntries();
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 获取指定账号的好友信息
     * @param user	账号
     * @return
     */
    public RosterEntry getFriend(String user) {
    	if(isConnected()) {
    		return Roster.getInstanceFor(mConnection).getEntry(user);
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 添加好友
     * @param user			用户账号
     * @param nickName		用户昵称
     * @param groupName		所属组名
     * @return
     */
    public boolean addFriend(String user, String nickName, String groupName) {
    	if(isConnected()) {
    		try {
				Roster.getInstanceFor(mConnection).createEntry(user, nickName, new String[]{groupName});
				return true;
			} catch (NotLoggedInException | NoResponseException
					| XMPPErrorException | NotConnectedException e) {
				return false;
			}
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 获取聊天对象的Fully的jid值
     * @param nickname		用户昵称
     * @return
     */
    public String getChatJidByName(String nickname) {
    	RosterEntry friend = SmackManager.getInstance().getFriend(nickname);
    	return getChatJidByUser(friend.getUser());
    }
    
    /**
     * 获取聊天对象的Fully的jid值
     * @param rosterUser	用户账号
     * @return
     */
    public String getChatJidByUser(String rosterUser) {
    	if(!isConnected()) {
    		throw new NullPointerException("服务器连接失败，请先连接服务器");
    	}
    	return rosterUser + "@" + mConnection.getServiceName();
    }
    
    /**
     * 获取文件传输的完全限定Jid
     * The fully qualified jabber ID (i.e. full JID) with resource of the user to send the file to.
     * @param nickname	用户昵称，也就是RosterEntry中的name
     * @return
     */
    public String getFileTransferJid(String nickname) {
    	String chatJid = getChatJidByName(nickname);
    	return getFileTransferJidChatJid(chatJid);
    }
    
    /**
     * 获取文件传输的完全限定Jid
     * The fully qualified jabber ID (i.e. full JID) with resource of the user to send the file to.
     * @param chatJid	与好友聊天的限定JID(如：laohu@192.168.0.108)
     * @return
     */
    public String getFileTransferJidChatJid(String chatJid) {
    	return chatJid + "/" + XMPP_CLIENT;
    }
    
    /**
     * 获取发送文件的发送器
     * @param jid	一个完整的jid(如：laohu@192.168.0.108/Smack，后面的Smack应该客户端类型，不加这个会出错)
     * @return
     */
    public OutgoingFileTransfer getSendFileTransfer(String jid) {
    	if(isConnected()) {
			return FileTransferManager.getInstanceFor(mConnection).createOutgoingFileTransfer(jid);
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 添加文件接收的监听
     * @param fileTransferListener
     */
    public void addFileTransferListener(FileTransferListener fileTransferListener) {
    	if(isConnected()) {
			FileTransferManager.getInstanceFor(mConnection).addFileTransferListener(fileTransferListener);
			return;
    	}
    	throw new NullPointerException("服务器连接失败，请先连接服务器");
    }
    
    /**
     * 创建群聊聊天室
     * @param roomName		聊天室名字
     * @param nickName		创建者在聊天室中的昵称
     * @param password		聊天室密码
     * @return
     */
    public MultiUserChat createChatRoom(String roomName, String nickName, String password) {
    	if(!isConnected()) {
			throw new NullPointerException("服务器连接失败，请先连接服务器");
    	}
    	MultiUserChat muc = null;  
	    try {  
	        // 创建一个MultiUserChat  
	    	muc = MultiUserChatManager.getInstanceFor(mConnection).getMultiUserChat(roomName + "@conference." + mConnection.getServiceName());
	        // 创建聊天室  
	        boolean isCreated = muc.createOrJoin(nickName);
	        if(isCreated) {
	        	// 获得聊天室的配置表单  
	        	Form form = muc.getConfigurationForm();  
	        	// 根据原始表单创建一个要提交的新表单。  
	        	Form submitForm = form.createAnswerForm();
	        	// 向要提交的表单添加默认答复  
	        	List<FormField> fields = form.getFields();
	        	for(int i = 0; fields != null && i < fields.size(); i++) {
	        		if(FormField.Type.hidden != fields.get(i).getType() && fields.get(i).getVariable() != null) {  
	        			// 设置默认值作为答复  
	        			submitForm.setDefaultAnswer(fields.get(i).getVariable());  
	        		}  
	        	}
	        	// 设置聊天室的新拥有者  
	        	List<String> owners = new ArrayList<String>();  
	        	owners.add(mConnection.getUser());// 用户JID
	        	submitForm.setAnswer("muc#roomconfig_roomowners", owners);  
	        	// 设置聊天室是持久聊天室，即将要被保存下来  
	        	submitForm.setAnswer("muc#roomconfig_persistentroom", true);  
	        	// 房间仅对成员开放  
	        	submitForm.setAnswer("muc#roomconfig_membersonly", false);  
	        	// 允许占有者邀请其他人  
	        	submitForm.setAnswer("muc#roomconfig_allowinvites", true);  
	        	if(password != null && password.length() != 0) {  
	        		// 进入是否需要密码  
	        		submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",  true);  
	        		// 设置进入密码  
	        		submitForm.setAnswer("muc#roomconfig_roomsecret", password);  
	        	}  
	        	// 能够发现占有者真实 JID 的角色  
	        	// submitForm.setAnswer("muc#roomconfig_whois", "anyone");  
	        	// 登录房间对话  
	        	submitForm.setAnswer("muc#roomconfig_enablelogging", true);  
	        	// 仅允许注册的昵称登录  
	        	submitForm.setAnswer("x-muc#roomconfig_reservednick", true);  
	        	// 允许使用者修改昵称  
	        	submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);  
	        	// 允许用户注册房间  
	        	submitForm.setAnswer("x-muc#roomconfig_registration", false);  
	        	// 发送已完成的表单（有默认值）到服务器来配置聊天室  
	        	muc.sendConfigurationForm(submitForm);  
	        }
	    } catch (XMPPException | SmackException e) {  
	        e.printStackTrace();  
	        return null;  
	    }  
	    return muc; 
    }
    
    /**
     * 加入一个群聊聊天室
     * @param roomName		聊天室名字
     * @param nickName		用户在聊天室中的昵称
     * @param password		聊天室密码
     * @return
     */
    public MultiUserChat joinChatRoom(String roomName,  String nickName, String password) {  
    	if(!isConnected()) {
			throw new NullPointerException("服务器连接失败，请先连接服务器");
    	}  
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口  
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(mConnection).getMultiUserChat(roomName + "@conference." + mConnection.getServiceName());
            // 聊天室服务将会决定要接受的历史记录数量  
            DiscussionHistory history = new DiscussionHistory();  
            history.setMaxChars(0);  
            // history.setSince(new Date());  
            // 用户加入聊天室  
            muc.join(nickName, password);  
            return muc;  
        } catch (XMPPException | SmackException e) {  
            e.printStackTrace();  
            return null;  
        }  
    } 
}
