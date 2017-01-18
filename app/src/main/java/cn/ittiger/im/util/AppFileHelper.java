package cn.ittiger.im.util;

import cn.ittiger.app.AppContext;

/**
 * 应用相关文件帮助类
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class AppFileHelper {

    public static String getAppRoot() {

        return AppContext.getInstance().getExternalCacheDir().getAbsolutePath();
    }

    public static String getAppImageDir() {

        return getAppRoot() + "/image";
    }

    public static String getAppDBDir() {

        return getAppRoot() + "/db";
    }

    public static String getAppCrashDir() {

        return getAppRoot() + "/crash";
    }
}
