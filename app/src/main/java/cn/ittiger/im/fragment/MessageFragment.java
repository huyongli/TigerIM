package cn.ittiger.im.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.base.BaseFragment;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.ChatRecordAdapter;
import cn.ittiger.im.decoration.CommonItemDecoration;
import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.bean.ChatRecord;
import cn.ittiger.im.ui.recyclerview.CommonRecyclerView;
import cn.ittiger.im.ui.recyclerview.HeaderAndFooterAdapter;
import cn.ittiger.im.util.DBHelper;
import cn.ittiger.im.util.DBQueryHelper;
import cn.ittiger.im.util.IMUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * 聊天消息列表
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public class MessageFragment extends BaseFragment implements CommonRecyclerView.OnItemClickListener {
    @BindView(R.id.recycler_message_record)
    CommonRecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private ChatRecordAdapter mAdapter;
    private HashMap<String, Integer> mMap = new HashMap<>();//聊天用户的用户名与用户聊天记录Position的映射关系

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, null);
        ButterKnife.bind(this, view);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new CommonItemDecoration());
        mRecyclerView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void refreshData() {

        Observable.create(new Observable.OnSubscribe<List<ChatRecord>>() {
            @Override
            public void call(Subscriber<? super List<ChatRecord>> subscriber) {

                List<ChatRecord> list = DBQueryHelper.queryChatRecord();
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

                refreshFailed();
                Logger.e(throwable, "get chat record failure");
            }
        })
        .subscribe(new Action1<List<ChatRecord>>() {
            @Override
            public void call(List<ChatRecord> chatRecords) {

                mAdapter = new ChatRecordAdapter(mContext, chatRecords);
                mRecyclerView.setAdapter(mAdapter);
                refreshSuccess();
            }
        });
    }

    @Override
    public void onItemClick(HeaderAndFooterAdapter adapter, int position, View itemView) {

        IMUtil.startChatActivity(mContext, mAdapter.getItem(position));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mAdapter = null;
        mMap.clear();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatRecordEvent(ChatRecord event) {
        //向其他人发起聊天时接收到的事件
        if(isRemoving() || mAdapter == null) {
            return;
        }
        if(mAdapter.getData().indexOf(event) > -1) {
            return;//已经存在此人的聊天窗口记录
        }
        addChatRecord(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveChatMessageEvent(ChatMessage message) {
        //收到发送的消息时接收到的事件(包括别人发送的和自己发送的消息)
        if(isRemoving() || mAdapter == null) {
            return;
        }
        ChatRecord chatRecord = getChatRecord(message);
        if(chatRecord == null) {//还没有创建此朋友的聊天记录
            chatRecord = new ChatRecord(message);
            addChatRecord(chatRecord);
        } else {
            chatRecord.setChatTime(message.getDatetime());
            chatRecord.setLastMessage(message.getContent());
            chatRecord.updateUnReadMessageCount();
            mAdapter.update(chatRecord);
            DBHelper.getInstance().getSQLiteDB().update(chatRecord);//更新数据库中的记录
        }
    }

    private void addChatRecord(ChatRecord chatRecord) {

        mAdapter.add(chatRecord, 0);
        DBHelper.getInstance().getSQLiteDB().save(chatRecord);
        mLayoutManager.scrollToPosition(0);
        for(String key : mMap.keySet()) {//创建新的聊天记录之后，需要将之前的映射关系进行更新
            mMap.put(key, mMap.get(key) + 1);
        }
    }

    /**
     * 根据消息获取聊天记录窗口对象
     *
     * @param message
     * @return
     */
    private ChatRecord getChatRecord(ChatMessage message) {

        ChatRecord chatRecord = null;
        if(mMap.containsKey(message.getFriendUsername())) {
            chatRecord = mAdapter.getData().get(mMap.get(message.getFriendUsername()));
        } else {
            for(int i = 0; i < mAdapter.getData().size(); i++) {
                chatRecord = mAdapter.getData().get(i);
                if(chatRecord.getMeUsername().equals(message.getMeUsername()) &&
                        chatRecord.getFriendUsername().equals(message.getFriendUsername())) {
                    mMap.put(message.getFriendUsername(), i);
                    break;
                } else {
                    chatRecord = null;
                }
            }
        }
        return chatRecord;
    }

    @Override
    public String getTitle() {

        return getString(R.string.text_message);
    }
}
