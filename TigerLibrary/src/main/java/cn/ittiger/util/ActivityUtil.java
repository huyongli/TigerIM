package cn.ittiger.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by laohu on 16-12-14.
 */
public class ActivityUtil {

    public static void startActivity(Context context, Intent intent) {

        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class claxx) {

        startActivity(context, new Intent(context, claxx));
    }

    public static void startActivity(Context context, Class claxx, Bundle bundle) {

        Intent intent = new Intent(context, claxx);
        intent.putExtras(bundle);
        startActivity(context, intent);
    }

    public static void skipActivity(Context context, Class claxx) {

        startActivity(context, claxx);
        finishActivity((Activity) context);
    }

    public static void finishActivity(Activity activity) {

        activity.finish();
    }
}
