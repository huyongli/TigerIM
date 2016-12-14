package cn.ittiger.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ittiger.im.R;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.ClearEditText;
import cn.ittiger.im.ui.TopTitleBar;
import cn.ittiger.im.util.ActivityUtil;
import cn.ittiger.im.util.UIUtil;
import cn.ittiger.im.util.ValueUtil;

/**
 * 添加好友
 *
 * @auther: hyl
 * @time: 2015-10-28下午2:45:39
 */
public class AddFriendActivity extends BaseActivity {
    @BindView(R.id.ttb_addfriend_title)
    TopTitleBar mTitleBar;
    @BindView(R.id.cet_friend_username)
    ClearEditText mEtUsername;
    @BindView(R.id.cet_friend_nickname)
    ClearEditText mEtNickname;
    @BindView(R.id.btn_add_friend)
    Button mBtnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend_layout);
    }

    @OnClick(R.id.btn_add_friend)
    public void onAddFriendClick(View v) {

        String username = mEtUsername.getText().toString();
        String nickname = mEtNickname.getText().toString();
        if (ValueUtil.isEmpty(username)) {
            mEtUsername.setError("好友用户名不能为空");
            return;
        }
        if (ValueUtil.isEmpty(nickname)) {
            mEtNickname.setError("好友昵称不能为空");
            return;
        }
        boolean flag = SmackManager.getInstance().addFriend(username, nickname, null);
        if (flag) {
            UIUtil.showToast(this, "好友添加成功");
            FriendListActivity.isNeedRefresh = true;
            ActivityUtil.finishActivity(this);
        } else {
            UIUtil.showToast(this, "好友添加失败");
        }
    }
}
