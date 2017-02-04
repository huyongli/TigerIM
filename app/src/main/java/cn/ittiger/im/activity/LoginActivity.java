package cn.ittiger.im.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.im.R;
import cn.ittiger.im.activity.base.IMBaseActivity;
import cn.ittiger.im.bean.LoginResult;
import cn.ittiger.im.bean.User;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.ClearEditText;
import cn.ittiger.im.util.LoginHelper;
import cn.ittiger.util.ActivityUtil;
import cn.ittiger.util.UIUtil;
import cn.ittiger.util.ValueUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

/**
 * 登陆openfire服务器
 *
 * @auther: hyl
 * @time: 2015-10-23下午1:36:59
 */
public class LoginActivity extends IMBaseActivity {
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
    AppCompatCheckBox mCbRememberPassword;

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

        mBtnLogin.setEnabled(false);
        mBtnLogin.setText(getString(R.string.login_button_login_loading));
        Observable.just(new User(username, password))
            .subscribeOn(Schedulers.io())//指定下面的flatMap线程
            .flatMap(new Func1<User, Observable<LoginResult>>() {
                @Override
                public Observable<LoginResult> call(User user) {

                    LoginResult loginResult = SmackManager.getInstance().login(username, password);
                    return Observable.just(loginResult);
                }
            })
            .observeOn(AndroidSchedulers.mainThread())//给下面的subscribe设定线程
            .doOnNext(new Action1<LoginResult>() {
                @Override
                public void call(LoginResult loginResult) {

                    LoginHelper.rememberRassword(mCbRememberPassword.isChecked());
                }
            })
            .subscribe(new Action1<LoginResult>() {
                @Override
                public void call(LoginResult loginResult) {

                    if (loginResult.isSuccess()) {
                        if (mCbRememberPassword.isChecked()) {
                            LoginHelper.saveUser(loginResult.getUser());
                        }
                        ActivityUtil.skipActivity(LoginActivity.this, MainActivity.class);
                    } else {
                        mBtnLogin.setEnabled(true);
                        mBtnLogin.setText(getString(R.string.login_button_unlogin_text));
                        UIUtil.showToast(LoginActivity.this, loginResult.getErrorMsg());
                    }
                }
            });
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
