package cn.ittiger.im.activity.interfaces;

import android.content.Intent;
import android.os.Bundle;

/**
 * Activity间跳转接口
 * @author: huylee
 * @time:	2014-10-26下午10:31:39
 */
public interface ISkipActivity {
	/**
	 * 从当前activity跳到类toActivityCls指定的Activity，并不调用@param activity's finish()
	 * @author: huylee
	 * @time:	2014-10-26下午10:34:55
	 * @param toActivityCls
	 */
	public void showActivity(Class<?> toActivityCls);

	/**
	 * 从当前activity启动指定Intent，多用于启动Android内部程序(利用Filter启动Activity)，并不调用@param activity's finish()
	 * @author: huylee
	 * @time:	2014-10-26下午10:35:54
	 * @param intent
	 */
	public void showActivity(Intent intent);
	
	/**
	 * 从当前activity跳到类toActivityCls指定的Activity，并传递参数bundle，并不调用@param activity's finish()
	 * @author: huylee
	 * @time:	2014-10-26下午10:38:22
	 * @param toActivityCls
	 * @param bundle
	 */
	public void showActivity(Class<?> toActivityCls, Bundle bundle);

	/**
	 * 从当前activity跳到类toActivityCls指定的Activity，并调用@param activity's finish()
	 * @author: huylee
	 * @time:	2014-10-26下午10:38:41
	 * @param toActivityCls
	 */
	public void skipActivity(Class<?> toActivityCls);
	
	/**
	 *从当前activity启动指定Intent，并调用@param activity's finish()
	 * @author: huylee
	 * @time:	2014-10-26下午10:38:44
	 * @param intent
	 */
	public void skipActivity(Intent intent);
	
	/**
	 * 从当前activity跳到类toActivityCls指定的Activity，并传递参数bundle，并调用@param activity's finish()
	 * @author: huylee
	 * @time:	2014-10-26下午10:38:46
	 * @param toActivityCls
	 * @param bundle
	 */
	public void skipActivity(Class<?> toActivityCls, Bundle bundle);
	
	/**
	 * 关闭该Activity
	 * @author: huylee
	 * @time:	2015-3-12下午3:32:48
	 */
	public void finishActivity();
}
