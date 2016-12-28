package cn.ittiger.util;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * 系统内安装的应用信息
 * @auther: hyl
 * @time: 2015-10-26上午9:51:32
 */
public class AppInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 应用名称
	 */
	private String mAppLable;
	/**
	 * 应用图标
	 */
	private Drawable mAppIcon;
	/**
	 * 应用包名
	 */
	private String mAppPackage;
	/**
	 * 应用class
	 */
	private String mAppClass;
	
	public String getAppLable() {
		return mAppLable;
	}
	public void setAppLable(String appLable) {
		this.mAppLable = appLable;
	}
	public Drawable getAppIcon() {
		return mAppIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.mAppIcon = appIcon;
	}
	public String getAppPackage() {
		return mAppPackage;
	}
	public void setAppPackage(String appPackage) {
		this.mAppPackage = appPackage;
	}
	public String getAppClass() {
		return mAppClass;
	}
	public void setAppClass(String appClass) {
		this.mAppClass = appClass;
	}
}
