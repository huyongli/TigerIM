package cn.ittiger.im.smack;

import cn.ittiger.im.bean.MultiChatRoom;
import cn.ittiger.im.util.DBHelper;
import cn.ittiger.im.util.LoginHelper;
import cn.ittiger.util.ValueUtil;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.orhanobut.logger.Logger;

import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;

/**
 * 群聊管理类
 *
 * @author: laohu on 2017/2/3
 * @site: http://ittiger.cn
 */
public class SmackMultiChatManager {


    public static void saveMultiChat(MultiUserChat multiUserChat) {

        DBHelper.getInstance().getSQLiteDB().save(new MultiChatRoom(multiUserChat.getRoom()));
    }

    public static void bindJoinMultiChat() {

        Observable.create(new Observable.OnSubscribe<List<HostedRoom>>() {
            @Override
            public void call(Subscriber<? super List<HostedRoom>> subscriber) {

                try {
                    List<HostedRoom> rooms = SmackManager.getInstance().getHostedRooms();
                    subscriber.onNext(rooms);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

                Logger.e(throwable, "bind join multi chat failure");
            }
        })
        .subscribe(new Action1<List<HostedRoom>>() {
            @Override
            public void call(List<HostedRoom> hostedRooms) {

                if(ValueUtil.isEmpty(hostedRooms)) {
                   return;
                }

                List<MultiChatRoom> multiChats = DBHelper.getInstance().getSQLiteDB().queryAll(MultiChatRoom.class);
                for(HostedRoom room : hostedRooms) {
                    ServiceDiscoveryManager discoManager = SmackManager.getInstance().getServiceDiscoveryManager();
                    // 获得指定XMPP实体的项目
                    // 这个例子获得与在线目录服务相关的项目
                    try {
                        DiscoverItems discoItems = discoManager.discoverItems(room.getJid());
                        // 获得被查询的XMPP实体的要查看的项目
                        List<DiscoverItems.Item> listItems = discoItems.getItems();//获得用户创建的群聊
                        if(listItems.size() > 0) {
                            for(MultiChatRoom chatRoom : multiChats) {
                                int idx = listItems.indexOf(chatRoom);
                                if(idx != -1) {
                                    try {
                                        MultiUserChat multiUserChat = SmackManager.getInstance().getMultiChat(chatRoom.getRoomJid());
                                        multiUserChat.join(LoginHelper.getUser().getNickname());
                                        SmackListenerManager.addMultiChatMessageListener(multiUserChat);
                                    } catch (Exception e) {
                                        Logger.e(e, "join room %s failure", room.getName());
                                    }
                                } else {
                                    DBHelper.getInstance().getSQLiteDB().delete(chatRoom);//服务器上没有此群聊
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
}
