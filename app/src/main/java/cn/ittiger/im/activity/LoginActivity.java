package cn.ittiger.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.im.R;
import cn.ittiger.im.bean.LoginResult;
import cn.ittiger.im.bean.User;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.ClearEditText;
import cn.ittiger.im.util.ActivityUtil;
import cn.ittiger.im.util.LoginHelper;
import cn.ittiger.im.util.UIUtil;
import cn.ittiger.im.util.ValueUtil;

import com.litesuits.android.async.SimpleTask;

/**
 * 登陆openfire服务器
 *
 * @auther: hyl
 * @time: 2015-10-23下午1:36:59
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * 登陆用户
     */
    @BindView(R.id.et_login_username)
    ClearEditText mEditTextUser;
    /**
     * 登陆密码
     */
    @BindView(R.id.et_login_password)
    ClearEditText mEditTextPwd;
    /**
     * 登陆按钮
     */
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    /**
     * 记住密码
     */
    @BindView(R.id.cb_remember_password)
    CheckBox mCbRememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);

        initViews();
        initUserInfo();
    }


    private void initViews() {

        mEditTextUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mEditTextPwd.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initUserInfo() {

        boolean isRemember = LoginHelper.isRememberPassword();
        if (isRemember) {
            User user = LoginHelper.getUser();
            mEditTextUser.setText(user.getUsername());
            mEditTextPwd.setText(user.getPassword());
        }
        mCbRememberPassword.setChecked(isRemember);
    }

    /**
     * 登陆响应
     *
     * @param v
     */
    @OnClick(R.id.btn_login)
    public void onLoginClick(View v) {

        final String username = mEditTextUser.getText().toString();
        final String password = mEditTextPwd.getText().toString();
        if (ValueUtil.isEmpty(username)) {
            UIUtil.showToast(this, getString(R.string.login_error_user));
            return;
        }
        if (ValueUtil.isEmpty(password)) {
            UIUtil.showToast(this, getString(R.string.login_error_password));
            return;
        }

        SimpleTask<LoginResult> task = new SimpleTask<LoginResult>() {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                mBtnLogin.setEnabled(false);
                mBtnLogin.setText(getString(R.string.login_button_login_loading));
            }

            @Override
            protected LoginResult doInBackground() {

                return SmackManager.getInstance().login(username, password);
            }

            @Override
            protected void onPostExecute(LoginResult loginResult) {

                super.onPostExecute(loginResult);
                mBtnLogin.setEnabled(true);
                mBtnLogin.setText(getString(R.string.login_button_unlogin_text));
                LoginHelper.rememberRassword(mCbRememberPassword.isChecked());

                if (loginResult.isSuccess()) {
                    ActivityUtil.skipActivity(LoginActivity.this, FriendListActivity.class);
                    if (mCbRememberPassword.isChecked()) {
                        LoginHelper.saveUser(new User(username, password));
                    }
                } else {
                    UIUtil.showToast(LoginActivity.this, loginResult.getErrorMsg());
                }
            }
        };
        task.execute();
    }

    /**
     * 用户注册
     *
     * @param v
     */
    @OnClick(R.id.tv_login_register)
    public void onRegisterClick(View v) {

        ActivityUtil.startActivity(this, RegisterActivity.class);
    }
}
