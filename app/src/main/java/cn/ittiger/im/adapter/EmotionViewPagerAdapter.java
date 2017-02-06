package cn.ittiger.im.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class EmotionViewPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

    public EmotionViewPagerAdapter(List<View> viewList) {

        mViewList = viewList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mViewList.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(mViewList.get(position));
    }

    @Override
    public int getCount() {

        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }
}
