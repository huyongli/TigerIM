package cn.ittiger.base;

import cn.ittiger.R;
import cn.ittiger.util.PageLoadingHelper;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author laohu
 * @site http://ittiger.cn
 */
public abstract class BaseFragment extends Fragment {

    protected AppCompatActivity mContext;
    private PageLoadingHelper mPageLoadingHelper;

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.base_layout, container, false);

        view.addView(getContentView(inflater, savedInstanceState), 0);

        mPageLoadingHelper = new PageLoadingHelper(view);
        mPageLoadingHelper.setOnLoadingClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                clickToRefresh();
            }
        });
        clickToRefresh();

        return view;
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
    public abstract void refreshData();

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

    /**
     * Fragment数据视图
     * @param inflater
     * @param savedInstanceState
     * @return
     */
    public abstract View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState);

    public abstract String getTitle();
}
