package cn.ittiger.im.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.ittiger.im.R;
import cn.ittiger.im.inject.InjectHelper;
import cn.ittiger.im.inject.annotation.InjectView;
import cn.ittiger.im.smack.SmackManager;

/**
 * 账号管理
 * @auther: hyl
 * @time: 2015-10-23下午3:11:11
 */
public class AccountMngActivity extends BaseActivity {
	/**
	 * 注销登陆
	 */
	@InjectView(id=R.id.btn_logout, onClick="onLogoutClick")
	private Button mBtnLogout;
	/**
	 * 用户状态修改
	 */
	@InjectView(id=R.id.rg_user_state)
	private RadioGroup mUserState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_mng_layout);
		InjectHelper.inject(mContext);
		
		mUserState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId) {
					case R.id.rb_user_online://在线
						changeState(0);
						break;
					case R.id.rb_user_busy://忙碌
						changeState(2);
						break;
					case R.id.rb_disconnect://断开连接
						disconnect();
						break;
				}
			}
		});
	}
	
	public void changeState(int code) {
		if(SmackManager.getInstance().updateUserState(code)) {
			showShortToast("修改状态成功");
		} else {
			showShortToast("修改状态失败");
		}
	}
	
	public void disconnect() {
		if(SmackManager.getInstance().disconnect()) {
			finishActivity();
		} else {
			showShortToast("断开连接失败");
		}
	}
	
	/**
	 * 注销登陆
	 * @param v
	 */
	public void onLogoutClick(View v) {
		if(SmackManager.getInstance().logout()) {
			finishActivity();
		} else {
			showShortToast("注销失败");
		}
	}
}
