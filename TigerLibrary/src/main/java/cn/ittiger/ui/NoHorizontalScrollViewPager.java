package cn.ittiger.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能横向滑动的ViewPager
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class NoHorizontalScrollViewPager extends ViewPager {

    public NoHorizontalScrollViewPager(Context context) {
        super(context);
    }

    public NoHorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写拦截事件，返回值设置为false，这时便不会横向滑动了。
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return false;
    }

    /**
     * 重写拦截事件，返回值设置为false，这时便不会横向滑动了。
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return false;
    }
}
