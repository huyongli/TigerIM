package cn.ittiger.im.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.im.R;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.util.ActivityUtil;
import cn.ittiger.util.UIUtil;
import cn.ittiger.util.ValueUtil;

/**
 * 添加好友
 *
 * @auther: laohu
 */
public class AddFriendActivity extends IMBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.til_friend_user)
    TextInputLayout mUserTextInput;
    @BindView(R.id.acet_friend_user)
    AppCompatEditText mUserEditText;
    @BindView(R.id.til_friend_nickname)
    TextInputLayout mNickNameTextInput;
    @BindView(R.id.acet_friend_nickname)
    AppCompatEditText mNickNameEditText;
    @BindView(R.id.btn_add_friend)
    Button mBtnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbarTitle.setText(getString(R.string.title_add_friend));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    @OnClick(R.id.btn_add_friend)
    public void onAddFriendClick(View v) {

        String username = mUserEditText.getText().toString();
        if (ValueUtil.isEmpty(username)) {
            mUserTextInput.setError(getString(R.string.error_input_friend_username));
            return;
        }
        String nickname = mNickNameEditText.getText().toString();
        if (ValueUtil.isEmpty(nickname)) {
            mNickNameTextInput.setError(getString(R.string.error_input_friend_username));
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
