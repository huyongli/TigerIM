package cn.ittiger.im.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.activity.base.IMBaseActivity;
import cn.ittiger.im.adapter.CheckableContactAdapter;
import cn.ittiger.im.decoration.ContactItemDecoration;
import cn.ittiger.im.bean.CheckableContactEntity;
import cn.ittiger.im.smack.SmackListenerManager;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.smack.SmackMultiChatManager;
import cn.ittiger.im.util.IMUtil;
import cn.ittiger.im.util.LoginHelper;
import cn.ittiger.indexlist.IndexStickyView;
import cn.ittiger.indexlist.listener.OnItemClickListener;
import cn.ittiger.util.ActivityUtil;
import cn.ittiger.util.UIUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.orhanobut.logger.Logger;

import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 创建群聊界面
 * @author: laohu on 2017/1/24
 * @site: http://ittiger.cn
 */
public class CreateMultiChatActivity extends IMBaseActivity
        implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.indexStickyView)
    IndexStickyView mIndexStickyView;

    private MenuItem mMenuItem;
    private CheckableContactAdapter mAdapter;
    private List<CheckableContactEntity> mCheckList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_multi_chat);
        ButterKnife.bind(this);
        initToolBar();
        mIndexStickyView.addItemDecoration(new ContactItemDecoration());
    }

    private void initToolBar() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(R.string.text_create_multi_chat);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void refreshData() {

        startRefresh();
        Observable.create(new Observable.OnSubscribe<List<CheckableContactEntity>>() {
            @Override
            public void call(Subscriber<? super List<CheckableContactEntity>> subscriber) {

                Set<RosterEntry> friends = SmackManager.getInstance().getAllFriends();
                List<CheckableContactEntity> list = new ArrayList<>();
                for (RosterEntry friend : friends) {
                    list.add(new CheckableContactEntity(friend));
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())//指定上面的Subscriber线程
        .observeOn(AndroidSchedulers.mainThread())//指定下面的回调线程
        .doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

                refreshFailed();
                Logger.e(throwable, "create multi chat query contact list failure");
            }
        })
        .subscribe(new Action1<List<CheckableContactEntity>>() {
            @Override
            public void call(List<CheckableContactEntity> contacts) {
                if(mAdapter == null) {
                    mAdapter = new CheckableContactAdapter(mActivity, contacts);
                    mAdapter.setOnItemClickListener(mContactItemClickListener);
                    mIndexStickyView.setAdapter(mAdapter);
                } else {
                    mAdapter.reset(contacts);
                }
                refreshSuccess();
            }
        });
    }

    OnItemClickListener<CheckableContactEntity> mContactItemClickListener = new OnItemClickListener<CheckableContactEntity>() {

        @Override
        public void onItemClick(View childView, int position, CheckableContactEntity item) {

            item.setChecked(!item.isChecked());
            mAdapter.notifyItemChanged(position);
            if(item.isChecked()) {
                mCheckList.add(item);
            } else {
                mCheckList.remove(item);
            }
            if(mCheckList.size() > 1) {
                mMenuItem.setEnabled(true);
            } else {
                mMenuItem.setEnabled(false);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.create_multi_chat_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        mMenuItem = menu.findItem(R.id.toolbar_create_multi_chat_finish);
        mMenuItem.setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toolbar_create_multi_chat_finish:
                createMultiChat();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 创建多人聊天
     */
    private void createMultiChat() {

        String meNickName = LoginHelper.getUser().getNickname();
        String chatRoomName = String.format(getString(R.string.text_default_multi_chat_nickname), meNickName);
        String reason = String.format(getString(R.string.text_invite_to_multi_chat), meNickName);
        try {
            MultiUserChat multiUserChat = SmackManager.getInstance().createChatRoom(chatRoomName, meNickName, null);
            for(CheckableContactEntity entity : mCheckList) {
                String jid = SmackManager.getInstance().getFullJid(entity.getRosterEntry().getUser());
                multiUserChat.invite(jid, reason);//邀请入群
            }
            SmackListenerManager.addMultiChatMessageListener(multiUserChat);
            SmackMultiChatManager.saveMultiChat(multiUserChat);
            IMUtil.startMultiChatActivity(this, multiUserChat);
            ActivityUtil.finishActivity(this);
        } catch (Exception e) {
            Logger.e(e, "invite friend to chatRoom failure ");
            UIUtil.showToast(mActivity, R.string.text_create_multi_chat_failure);
        }
    }

    @Override
    public boolean isLceActivity() {

        return true;
    }
}
