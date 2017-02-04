package cn.ittiger.im.activity.base;

import cn.ittiger.base.BaseActivity;
import cn.ittiger.im.R;

import android.os.Build;
import android.os.Bundle;

/**
 * @author: laohu on 2017/1/1
 * @site: http://ittiger.cn
 */
public class IMBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initNavigationBarColor();
    }

    private void initNavigationBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.main_color));
        }
    }

    @Override
    public boolean isLceActivity() {

        return false;
    }
}
