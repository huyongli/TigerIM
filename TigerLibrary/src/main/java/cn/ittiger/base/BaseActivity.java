package cn.ittiger.base;

import cn.ittiger.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    protected BaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = BaseActivity.this;
    }

    @Override
    public void onBackPressed() {

        if(doubleExitAppEnable()) {
            exitAppDoubleClick();
        } else {
            super.onBackPressed();
        }
    }

    public boolean doubleExitAppEnable() {

        return false;
    }

    /**
     * 双击退出函数变量
     */
    private long exitTime = 0;

    /**
     * 双击退出APP
     */
    private void exitAppDoubleClick() {

        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, R.string.exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            exitApp();
        }
    }

    /**
     * 退出APP
     */
    private void exitApp() {

        super.onBackPressed();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
