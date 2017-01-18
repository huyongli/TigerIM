package cn.ittiger.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by laohu on 16-12-14.
 */
public class UIUtil {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static Thread getUIThread() {

        return Looper.getMainLooper().getThread();
    }

    public static boolean isOnUIThread() {

        return Thread.currentThread() == getUIThread();
    }

    /**
     * 在UI线程执行
     *
     * @param action
     */
    public static void runOnUIThread(Runnable action) {

        if (!isOnUIThread()) {
            getHandler().post(action);
        } else {
            action.run();
        }
    }

    public static Handler getHandler() {

        return sHandler;
    }

    public static void showToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {

        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
    }
}
