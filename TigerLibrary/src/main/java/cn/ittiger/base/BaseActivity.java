package cn.ittiger.base;

import cn.ittiger.R;
import cn.ittiger.util.PageLoadingHelper;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 通用Activity基类
 * @author: laohu
 * @site: http://ittiger.cn
 */
public class BaseActivity extends AppCompatActivity {
    protected BaseActivity mActivity;
    private PageLoadingHelper mPageLoadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = BaseActivity.this;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        if(isLceActivity()) {
            View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
            setContentView(contentView);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {

        if(isLceActivity()) {
            ViewGroup root = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.base_layout, null);
            root.addView(view, 0);
            super.setContentView(root);
            initPageLoading(root);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        if(isLceActivity()) {
            ViewGroup root = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.base_layout, null);
            root.addView(view, 0);
            super.setContentView(root, params);
            initPageLoading(root);
        } else {
            super.setContentView(view, params);
        }
    }

    private void initPageLoading(View root) {

        mPageLoadingHelper = new PageLoadingHelper(root);
        mPageLoadingHelper.setOnLoadingClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clickToRefresh();
            }
        });
        clickToRefresh();
    }

    /**
     * 是否在初始化的时候需要查询数据，并显示查询等待进度条，查询失败显示失败提示
     *
     * @return  if return true, you just to override refreshData() method to loading data
     */
    public boolean isLceActivity() {

        return true;
    }

    /**
     * 点击刷新加载
     */
    private void clickToRefresh() {

        startRefresh();
        refreshData();
    }

    /**
     * 初始化数据
     * 主线程
     */
    public void refreshData() {

    }

    /**
     * 开始加载数据
     */
    public void startRefresh() {

        mPageLoadingHelper.startRefresh();
    }

    /**
     * 加载失败
     */
    public void refreshFailed() {

        mPageLoadingHelper.refreshFailed();
    }

    /**
     * 加载成功
     */
    public void refreshSuccess() {

        mPageLoadingHelper.refreshSuccess();
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
