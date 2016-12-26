package cn.ittiger.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by laohu on 16-12-14.
 */
public class ActivityUtil {

    public static void startActivity(Activity activity, Intent intent) {

        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, Class claxx) {

        startActivity(activity, new Intent(activity, claxx));
    }

    public static void startActivity(Activity activity, Class claxx, Bundle bundle) {

        Intent intent = new Intent(activity, claxx);
        intent.putExtras(bundle);
        startActivity(activity, intent);
    }

    public static void skipActivity(Activity activity, Class claxx) {

        startActivity(activity, claxx);
        finishActivity(activity);
    }

    public static void finishActivity(Activity activity) {

        activity.finish();
    }
}
