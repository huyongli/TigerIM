package cn.ittiger.im.util;

import cn.ittiger.im.app.AppContext;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by laohu on 16-12-13.
 */
public class PreferenceHelper {

    private static SharedPreferences sSharedPreferences;

    static {
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(AppContext.getInstance());
    }

    public static void putString(String key, String value) {

        sSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * @param key
     * @return    the default value is ""
     */
    public static String getString(String key) {

        return getString(key, "");
    }

    public static String getString(String key, String defaultValue) {

        return sSharedPreferences.getString(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {

        sSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {

        return sSharedPreferences.getBoolean(key, defaultValue);
    }
}
