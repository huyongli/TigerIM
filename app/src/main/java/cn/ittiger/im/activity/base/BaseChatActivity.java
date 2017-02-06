package cn.ittiger.im.activity.base;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.ChatAdapter;
import cn.ittiger.im.bean.ChatMessage;
import cn.ittiger.im.bean.ChatUser;
import cn.ittiger.im.ui.keyboard.ChatKeyboard;
import cn.ittiger.im.ui.recyclerview.CommonRecyclerView;
import cn.ittiger.im.util.DBQueryHelper;
import cn.ittiger.im.util.IntentHelper;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import org.greenrobot.eventbus.EventBus;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * 单人和多人聊天窗口基类
 *
 * @author: laohu on 2017/2/3
 * @site: http://ittiger.cn
 */
public abstract class BaseChatActivity extends IMBaseActivity implements ChatKeyboard.KeyboardOperateListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarTitle)
    TextView mToolbarTitle;
    /**
     * 聊天内容展示列表
     */
    @BindView(R.id.chat_content)
    CommonRecyclerView mChatMessageRecyclerView;
    /**
     * 聊天输入控件
     */
    @BindView(R.id.ckb_chat_board)
    ChatKeyboard mChatKyboard;
    /**
     * 聊天信息实体类
     */
    protected ChatUser mChatUser;
    /**
     * 聊天记录展示适配器
     */
    protected ChatAdapter mAdapter;
    /**
     * 消息列表布局管理器
     */
    protected LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mChatUser = getIntent().getParcelableExtra(IntentHelper.KEY_CHAT_DIALOG);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(mChatUser.getFriendNickname());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mChatKyboard.setKeyboardOperateListener(this);

        mLayoutManager = new LinearLayoutManager(this);
        mChatMessageRecyclerView.setLayoutManager(mLayoutManager);
        mChatMessageRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mChatKyboard.hideKeyBoardView();
                return false;
            }
        });

        initData();
    }

    private void initData() {

        Observable.create(new Observable.OnSubscribe<List<ChatMessage>>() {
            @Override
            public void call(Subscriber<? super List<ChatMessage>> subscriber) {

                List<ChatMessage> messages = DBQueryHelper.queryChatMessage(mChatUser);
                subscriber.onNext(messages);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<ChatMessage>>() {
            @Override
            public void call(List<ChatMessage> chatMessages) {
                mAdapter = new ChatAdapter(mActivity, chatMessages);
                mChatMessageRecyclerView.setAdapter(mAdapter);
                mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }

    protected void addChatMessageView(ChatMessage message) {

        mAdapter.add(message);
        mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    protected void onStart() {

        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {

        if(mChatKyboard.onInterceptBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
