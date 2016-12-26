package cn.ittiger.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SDCard帮助类
 * @author: huylee
 * @time:	2015-8-13下午10:13:31
 */
public class SdCardUtil {

    /**
	 * 是否存在SD卡
	 * @return
	 */
	public static boolean isHasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取手机SD卡根目录路径
	 * @return
	 */
	public static String getSDCardDir() {
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/**
	 * 获取手机自身根目录
	 * @return
	 */
	public static String getMobileDir() {
		return Environment.getDataDirectory().getAbsolutePath();
	}
	
	/**
	 * 获取手机数据根目录，有Sdcard则返回Sdcard根目录，没有则返回手机自身根目录
	 * @return
	 */
	public static String getRootPath() {
		return isHasSDCard() ? getSDCardDir() : getMobileDir();
	}
	
	/**
	 * 应用缓存目录，可以通过手机中的清除缓存把数据清除掉
	 * @param context
	 * @return
	 */
	public static String getCacheDir(Context context) {
		return context.getCacheDir().getAbsolutePath();
	}
	
	/**
	 * 获取当前应用的文件目录
	 * @param context
	 * @return
	 */
	public static String getFilesDir(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}

    /**
     * 获取当前路径，可用空间
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableSize(String path) {
        try {
            File base = new File(path);
            StatFs stat = new StatFs(base.getPath());
            return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
