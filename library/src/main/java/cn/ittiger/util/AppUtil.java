package cn.ittiger.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 应用相关信息工具函数
 * @auther: hyl
 * @time: 2016-1-18下午3:33:30
 */
public final class AppUtil {
	
	/**
	 * 获取当前应用包名
	 * @param context
	 * @return
	 */
	public static String getAppPackageName(Context context) {
		return context.getPackageName();
	}
	
	/**
	 * 获取当前应用版本号android:versionName
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			String pkName = getAppPackageName(context);
			String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
			return versionName;
		} catch (Exception e) {
		}
		return "1.0";
	}
	
	/**
	 * 获取当前应用的code版本号android:versionCode
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			String pkName = getAppPackageName(context);
			int versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
			return versionCode;
		} catch (Exception e) {
		}
		return 1;
	}
	
	/**
	 * 获取系统所有APP应用
	 * 
	 * @param context
	 */
	public static ArrayList<AppInfo> getAllApp(Context context) {
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		// 将获取到的APP的信息按名字进行排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		for (ResolveInfo info : apps) {
			AppInfo appInfo = new AppInfo();

			appInfo.setAppLable(info.loadLabel(manager) + "");
			appInfo.setAppIcon(info.loadIcon(manager));
			appInfo.setAppPackage(info.activityInfo.packageName);
			appInfo.setAppClass(info.activityInfo.name);
			appList.add(appInfo);
			System.out.println("info.activityInfo.packageName="+info.activityInfo.packageName);
			System.out.println("info.activityInfo.name="+info.activityInfo.name);
		}

		return appList;
	}

	/**
	 * 获取用户安装的APP应用
	 * 
	 * @param context
	 */
	public static ArrayList<AppInfo> getUserApp(Context context) {
		PackageManager manager = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
		// 将获取到的APP的信息按名字进行排序
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		for (ResolveInfo info : apps) {
			AppInfo appInfo = new AppInfo();
			ApplicationInfo ainfo = info.activityInfo.applicationInfo;
			if ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				appInfo.setAppLable(info.loadLabel(manager) + "");
				appInfo.setAppIcon(info.loadIcon(manager));
				appInfo.setAppPackage(info.activityInfo.packageName);
				appInfo.setAppClass(info.activityInfo.name);
				appList.add(appInfo);
			}
		}

		return appList;
	}

	/**
	 * 根据包名和Activity启动类查询应用信息
	 * 
	 * @param cls
	 * @param pkg
	 * @return
	 */
	public static AppInfo getAppByClsPkg(Context context, String pkg, String cls) {
		AppInfo appInfo = new AppInfo();

		PackageManager pm = context.getPackageManager();
		Drawable icon;
		CharSequence label = "";
		ComponentName comp = new ComponentName(pkg, cls);
		try {
			ActivityInfo info = pm.getActivityInfo(comp, 0);
			icon = pm.getApplicationIcon(info.applicationInfo);
			label = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0));
		} catch (NameNotFoundException e) {
			icon = pm.getDefaultActivityIcon();
		}
		appInfo.setAppClass(cls);
		appInfo.setAppIcon(icon);
		appInfo.setAppLable(label + "");
		appInfo.setAppPackage(pkg);

		return appInfo;
	}
}
