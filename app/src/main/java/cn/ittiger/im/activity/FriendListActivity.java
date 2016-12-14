package cn.ittiger.im.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.roster.RosterEntry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.BaseViewAdapter;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.TopTitleBar;
import cn.ittiger.im.ui.TopTitleBar.LeftClickListener;
import cn.ittiger.im.util.ActivityUtil;
import cn.ittiger.im.util.DialogUtil;

/**
 * 好友列表
 *
 * @auther: hyl
 * @time: 2015-10-28上午11:51:22
 */
public class FriendListActivity extends BaseActivity {
    /**
     * 头部
     */
    @BindView(R.id.ttb_friendlist_title)
    TopTitleBar mTitleBar;
    /**
     * 好友列表
     */
    @BindView(R.id.lv_friend_list)
    ListView mListView;
    /**
     * 添加好友
     */
    @BindView(R.id.btn_add_friend)
    Button mBtnAddFriend;
    /**
     *
     */
    private FriendAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist_layout);
        mTitleBar.setTitle("好友列表");
        mTitleBar.setLeftClickListener(new LeftClickListener() {
            @Override
            public void onLeftClick() {

                ActivityUtil.finishActivity(FriendListActivity.this);
            }
        });
        getAllFriends();
    }

    public static boolean isNeedRefresh = false;

    @Override
    protected void onResume() {

        super.onResume();
        if (isNeedRefresh) {
            getAllFriends();
            isNeedRefresh = false;
        }
    }

    public void getAllFriends() {

        AsyncTask<Void, Void, List<RosterEntry>> task = new AsyncTask<Void, Void, List<RosterEntry>>() {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                DialogUtil.showProgressDialog(FriendListActivity.this, "正在查询好友信息，请稍后...");
            }

            @Override
            protected List<RosterEntry> doInBackground(Void... params) {

                Set<RosterEntry> friends = SmackManager.getInstance().getAllFriends();
                List<RosterEntry> list = new ArrayList<>();
                for (RosterEntry friend : friends) {
                    list.add(friend);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<RosterEntry> result) {

                super.onPostExecute(result);
                DialogUtil.hideProgressDialog();
                initListView(result);
            }
        };
        task.execute();
    }

    public void initListView(List<RosterEntry> list) {

        if (mAdapter == null) {
            mAdapter = new FriendAdapter(this, android.R.layout.simple_list_item_1, list);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    final RosterEntry friend = mAdapter.getItem(position);
                    DialogUtil.showDialog(FriendListActivity.this, null, "确定要与[" + friend.getName() + "]聊天吗？", null, null, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startChat(friend);
                        }
                    }, null);
                }
            });
        } else {
            mAdapter.resetData(list);
        }
    }

    /**
     * 开始聊天
     *
     * @param friend
     */
    public void startChat(RosterEntry friend) {

        Bundle bundle = new Bundle();
        bundle.putString("user", friend.getUser());
        bundle.putString("nickname", friend.getName());
        ActivityUtil.startActivity(this, ChatActivity.class, bundle);
    }

    /**
     * 添加好友
     *
     * @param v
     */
    @OnClick(R.id.btn_add_friend)
    public void onAddFriendClick(View v) {

        ActivityUtil.startActivity(this, AddFriendActivity.class);
    }

    public class FriendAdapter extends BaseViewAdapter<RosterEntry> {
        public class FriendViewHolder extends AbsViewHolder {
            public TextView tv;
        }

        public FriendAdapter(Context context, int viewId, List<RosterEntry> list) {

            super(context, viewId, list);
        }

        @Override
        public cn.ittiger.im.adapter.BaseViewAdapter.AbsViewHolder getViewHolder(
                View convertView) {

            FriendViewHolder viewHolder = new FriendViewHolder();
            viewHolder.tv = (TextView) convertView;
            return viewHolder;
        }

        @Override
        protected void initListItemView(int position,
                                        cn.ittiger.im.adapter.BaseViewAdapter.AbsViewHolder absViewHolder,
                                        ViewGroup parent, RosterEntry item) {

            FriendViewHolder viewHolder = (FriendViewHolder) absViewHolder;
            viewHolder.tv.setText(item.getName());
        }
    }
}
