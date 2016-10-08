package cn.ittiger.im.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Toast;
import cn.ittiger.im.activity.interfaces.IShowToastMessage;
import cn.ittiger.im.activity.interfaces.ISkipActivity;
import cn.ittiger.im.inject.InjectHelper;

/**
 * 自定义Activity基类,继承于android.support.v4.app.FragmentActivity
 * @author: huylee
 * @time:	2015-3-2上午11:44:46
 */
public class BaseActivity extends FragmentActivity implements
		ISkipActivity, IShowToastMessage {
	public final String TAG =  getClass().getSimpleName();
	/**
	 * 该Activity是否为主Activity，应用主Activity可以设置为true以退出应用
	 */
	protected boolean mIsMainActivity = false;
	/**
	 * 本身对应的上下文对象
	 */
	@SuppressLint("Registered")
	protected BaseActivity mContext;
	/**
	 * 正在运行的Activity
	 */
	private static List<ComponentName> mRunningActivity = new ArrayList<ComponentName>(); 
	/**
	 * 上一个Activity传过来的Intent对象
	 */
	protected Intent mFromIntent;
	/**
	 * 是否记录当前Activity的生命周期运行情况日志信息，默认不记录
	 */
	protected boolean mIsLogLifeCycle = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		
		mContext = BaseActivity.this;
		mFromIntent = getIntent();
		mRunningActivity.add(getComponentName());
		
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onCreate");
		}
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		InjectHelper.inject(mContext);
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		InjectHelper.inject(mContext);
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		InjectHelper.inject(mContext);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onStart");
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onRestart");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onResume");
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onStop");
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onPause");
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mRunningActivity.remove(getComponentName());
		if(mIsLogLifeCycle) {
			Log.i(TAG, "LifeCycle----onDestroy");
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	/**
	 * 返回
	 * Author: hyl
	 * Time: 2015-7-10上午11:30:43
	 * @param view
	 */
	public void goBack(View view) {
		this.onBackPressed();
	}
	
	/**
	 * 获取正在运行的Activity列表
	 * @author: huylee
	 * @time:	2015-3-2上午11:33:25
	 * @return
	 */
	public static List<ComponentName> getRunningActivity() {
		return mRunningActivity;
	}
	
	/**
	 * 显示一个长时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:31:46
	 * @param message	提示内容
	 */
	@Override
	public void showLongToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}
	

	@Override
	public void showLongToast(int msgId) {
		Toast.makeText(mContext, msgId, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示一个短时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:32:14
	 * @param message	提示内容
	 */
	@Override
	public void showShortToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示一个短时间的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:32:14
	 * @param msgId		提示内容的资源ID
	 */
	@Override
	public void showShortToast(int msgId) {
		Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示一个自定义时长的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:32:14
	 * @param message	提示内容
	 * @param duration	吐司提示显示的时间
	 */
	@Override
	public void showToast(String message, int duration) {
		Toast.makeText(mContext, message, duration).show();
	}

	/**
	 * 显示一个自定义时长的吐司提示
	 * @author: huylee
	 * @time:	2015-1-7下午9:31:46
	 * @param msgId		提示内容的资源ID
	 * @param duration	吐司提示显示的时间
	 */
	@Override
	public void showToast(int msgId, int duration) {
		Toast.makeText(mContext, msgId, duration).show();
	}
	
	/**-------------------- activity跳转相关 --------------------------**/
	@Override
	public void showActivity(Class<?> toActivityCls) {
		Intent intent = new Intent(mContext, toActivityCls);
		startActivity(intent);
		applyActivityAnim();
	}

	@Override
	public void showActivity(Intent intent) {
		startActivity(intent);
		applyActivityAnim();
	}

	@Override
	public void showActivity(Class<?> toActivityCls, Bundle bundle) {
		Intent intent = new Intent(mContext, toActivityCls);
		intent.putExtras(bundle);
		startActivity(intent);
		applyActivityAnim();
	}

	@Override
	public void skipActivity(Class<?> toActivityCls) {
		Intent intent = new Intent(mContext, toActivityCls);
		startActivity(intent);
		finishActivity();
	}

	@Override
	public void skipActivity(Intent intent) {
		startActivity(intent);
		finishActivity();
	}

	@Override
	public void skipActivity(Class<?> toActivityCls, Bundle bundle) {
		Intent intent = new Intent(mContext, toActivityCls);
		intent.putExtras(bundle);
		startActivity(intent);
		finishActivity();
	}
	
	@Override
	public void finishActivity() {
		finish();
		applyActivityAnim();
	}
	
	/**
	 * Activity之间应用调转动画
	 * @author: huylee
	 * @time:	2015-3-12下午3:34:19
	 */
	public void applyActivityAnim() {
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	/**-------------------- 应用退出相关 --------------------------**/
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		applyActivityAnim();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {    
			if(mIsMainActivity) {//调用双击退出函数  
				exitSystemBy2Click();      
			} else {//单击直接退出
				this.onBackPressed();
			}
		}
	    return false; 
	}
	
	/** 
     * 双击退出函数变量
     */  
     private long exitTime = 0;
     /**
     * 双击退出系统
     * @author:hyl
     * @time:2015-2-11下午9:18:34
     */
     protected void exitSystemBy2Click() {  
         if(System.currentTimeMillis() - exitTime > 2000) {
              Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
              exitTime = System.currentTimeMillis();
         } else {
              exitSystem();
         }
     }
     
     /**
 	 * 退出系统
 	 */
 	protected void exitSystem(){
 		mRunningActivity.clear();
 		mRunningActivity = null;
 		android.os.Process.killProcess(android.os.Process.myPid());	
 		finish();
 		applyActivityAnim();
 	}
}
