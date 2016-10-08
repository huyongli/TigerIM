package cn.ittiger.im.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cn.ittiger.im.R;
import cn.ittiger.im.inject.annotation.InjectView;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.ClearEditText;
import cn.ittiger.im.ui.TopTitleBar;
import cn.ittiger.im.util.ValueUtil;

/**
 * 注册
 * @auther: hyl
 * @time: 2015-10-28上午10:52:49
 */
public class RegisterActivity extends BaseActivity {
	/**
	 * 头部
	 */
	@InjectView(id=R.id.ttb_register_title)
	private TopTitleBar mTitleBar;
	/**
	 * 用户名
	 */
	@InjectView(id=R.id.cet_register_username)
	private ClearEditText mEtUsername;
	/**
	 * 昵称
	 */
	@InjectView(id=R.id.cet_register_nickname)
	private ClearEditText mEtNickname;
	/**
	 * 密码
	 */
	@InjectView(id=R.id.cet_register_password)
	private ClearEditText mEtPassword;
	/**
	 * 重复密码
	 */
	@InjectView(id=R.id.cet_register_repassword)
	private ClearEditText mEtRepassword;
	/**
	 * 注册
	 */
	@InjectView(id=R.id.btn_register_ok, onClick="onRegisterOk")
	private Button mBtnRegisterOk;
	/**
	 * 注册取消
	 */
	@InjectView(id=R.id.btn_register_cancel, onClick="onRegisterCancel")
	private Button mBtnRegisterCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_layout);
		mTitleBar.setLeftClickListener(new TopTitleBar.LeftClickListener() {
			@Override
			public void onLeftClick() {
				finishActivity();
			}
		});
		mTitleBar.setTitle("用户注册");
	}
	
	public void onRegisterOk(View v) {
		final String username = mEtUsername.getText().toString();
		final String nickname = mEtNickname.getText().toString();
		String password = mEtPassword.getText().toString();
		final String repassword = mEtRepassword.getText().toString();
		if(ValueUtil.isEmpty(username)) {
			mEtUsername.setError("用户名不能为空");return;
		}
		if(ValueUtil.isEmpty(nickname)) {
			mEtNickname.setError("昵称不能为空");return;
		}
		if(ValueUtil.isEmpty(password)) {
			mEtPassword.setError("密码不能为空");return;
		}
		if(ValueUtil.isEmpty(repassword)) {
			mEtRepassword.setError("密码确认不能为空");return;
		}
		if(!password.equals(repassword)) {
			mEtRepassword.setError("两次密码不相同，请重新确认");
			mEtRepassword.setText("");
			return;
		}
		new Thread(){
			public void run() {
				register(username, nickname, repassword);
			};
		}.start();
	}
	
	public void register(String username, String nickname, String password) {
		Map<String, String> attributes = new HashMap<>();
		attributes.put("name", nickname);
		final boolean flag = SmackManager.getInstance().registerUser(username, password, attributes);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(flag) {
					showShortToast("注册成功");
					finishActivity();
				} else {
					showShortToast("注册失败");
				}
			}
		});
	}
	
	public void onRegisterCancel(View v) {
		finishActivity();
	}
}
