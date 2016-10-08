package cn.ittiger.im.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.ittiger.im.R;
import cn.ittiger.im.exception.UnCaughtCrashExceptionHandler;
import cn.ittiger.im.inject.annotation.InjectView;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.im.ui.ClearEditText;
import cn.ittiger.im.util.DialogUtil;
import cn.ittiger.im.util.ValueUtil;

/**
 * 登陆Xmpp服务器
 * 
 * @auther: hyl
 * @time: 2015-10-23下午1:36:59
 */
public class LoginActivity extends BaseActivity {
	/**
	 * 登陆用户
	 */
	@InjectView(id = R.id.et_login_username)
	private ClearEditText mEditTextUser;
	/**
	 * 登陆密码
	 */
	@InjectView(id = R.id.et_login_password)
	private ClearEditText mEditTextPwd;
	/**
	 * 登陆按钮
	 */
	@InjectView(id = R.id.btn_login, onClick = "onLoginClick")
	private Button mBtnLogin;
	/**
	 * 注册按钮
	 */
	@InjectView(id=R.id.tv_login_register, onClick="onRegisterClick")
	private TextView mTvRegister;
	/**
	 * 记住密码
	 */
	@InjectView(id=R.id.cb_remember_password)
	private CheckBox mCbRememberPassword;
	/**
	 * 
	 */
	private SharedPreferences mSharedPreferences;
	
//	@InjectView(id=R.id.tv_login_demo, onClick="onDemoClick")
//	private TextView mTvDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UnCaughtCrashExceptionHandler.getInstance().init(mContext);
		
		setContentView(R.layout.activity_login_layout);
		mSharedPreferences = getPreferences(MODE_PRIVATE);
		boolean isRemember = mSharedPreferences.getBoolean("isRemember", false);
		mCbRememberPassword.setChecked(isRemember);
		if(isRemember) {
			mEditTextUser.setText(mSharedPreferences.getString("username", ""));
			mEditTextPwd.setText(mSharedPreferences.getString("password", ""));
		}
	}

	/**
	 * 登陆响应
	 * 
	 * @param v
	 */
	public void onLoginClick(View v) {
		final String username = mEditTextUser.getText().toString();
		final String password = mEditTextPwd.getText().toString();
		if (ValueUtil.isEmpty(username)) {
			showShortToast("请输入用户名");
			return;
		}
		if (ValueUtil.isEmpty(password)) {
			showShortToast("请输入密码");
			return;
		}
		
		new AsyncTask<String, Integer, JSONObject>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				DialogUtil.showProgressDialog(mContext, "正在登陆，请稍后...");
			}
			
			@Override
			protected JSONObject doInBackground(String... parm) {
				JSONObject json = new JSONObject();
				try {
					boolean flag  = SmackManager.getInstance().login(parm[0], parm[1]);
					json.put("flag", flag);
					if(flag == false) {
						json.put("err", "用户名或密码错误");
					}
				} catch (Exception e) {
					try {
						json.put("flag", false);
						json.put("err", e.getMessage());
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}
				return json;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				DialogUtil.hideProgressDialog();
				try {
					if(!result.getBoolean("flag")) {
						showShortToast(result.get("err").toString());
					} else {
						Editor edit = mSharedPreferences.edit();
						edit.putBoolean("isRemember", mCbRememberPassword.isChecked());
						if(mCbRememberPassword.isChecked()) {
							edit.putString("username", username);
							edit.putString("password", password);
						}
						edit.commit();
						showLongToast("登录成功");
						skipActivity(FriendListActivity.class);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			
		}.execute(username, password);
	}
	
	/**
	 * 用户注册
	 * @param v
	 */
	public void onRegisterClick(View v) {
		showActivity(RegisterActivity.class);
	}
	
	public void onDemoClick(View v) {
		showActivity(ChatActivity.class);
	}
}
