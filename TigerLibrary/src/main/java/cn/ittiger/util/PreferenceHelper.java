package cn.ittiger.util;

import cn.ittiger.app.AppContext;
import cn.ittiger.util.cipher.Cipher;

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

    public static void putInt(String key, int value) {

        sSharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {

        return sSharedPreferences.getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {

        return sSharedPreferences.getInt(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {

        sSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {

        return sSharedPreferences.getBoolean(key, defaultValue);
    }

    public static Object get(String key) {
        return get(key, (Cipher) null);
    }

    public static Object get(String key, Cipher cipher) {
        try {
            String hex = getString(key, null);
            if (hex == null) return null;
            byte[] bytes = HexUtil.decodeHex(hex.toCharArray());
            if (cipher != null) bytes = cipher.decrypt(bytes);
            Object obj = ByteUtil.byteToObject(bytes);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void put(String key, Object ser) {
        put(key, ser, null);
    }

    public static void put(String key, Object ser, Cipher cipher) {
        try {
            if (ser == null) {
                sSharedPreferences.edit().remove(key).commit();
            } else {
                byte[] bytes = ByteUtil.objectToByte(ser);
                if (cipher != null) bytes = cipher.encrypt(bytes);
                putString(key, HexUtil.encodeHexStr(bytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
