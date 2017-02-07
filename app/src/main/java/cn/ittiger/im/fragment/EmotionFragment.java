package cn.ittiger.im.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.EmotionAdapter;
import cn.ittiger.im.ui.keyboard.emotion.EmotionItemClickListener;
import cn.ittiger.im.util.EmotionDataHelper;
import cn.ittiger.im.ui.keyboard.emotion.EmotionIndicatorView;
import cn.ittiger.im.adapter.EmotionViewPagerAdapter;
import cn.ittiger.util.DisplayUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情Fragment
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class EmotionFragment extends BaseEmotionFragment {
    @BindView(R.id.emotionIndicator)
    EmotionIndicatorView mIndicatorView;
    @BindView(R.id.emotionViewPager)
    ViewPager mViewPager;
    EmotionViewPagerAdapter mEmotionViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_emotion_layout, container, false);
        ButterKnife.bind(this, view);
        initEmotionData();
        return view;
    }

    private void initEmotionData() {

        // 获取屏幕宽度
        int screenWidth = DisplayUtils.getScreenWidthPixels(getActivity());
        // item的间距
        int spacing = DisplayUtils.dp2px(getActivity(), 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        int gridViewHeight = itemWidth * 3 + spacing * 6;

        List<View> emotionViewPagerData = new ArrayList<>();

        ArrayMap<String, Integer> emotions = EmotionDataHelper.getEmotionsForType(mEmotionType);

        /**
         * 每页展示3行表情，每行7个，最后一个展示表情删除按钮，因此每页共展示表情20个
         * */
        List<String> emotionNames = new ArrayList<>();
        for(int i = 0; i < emotions.size(); i++) {
            emotionNames.add(emotions.keyAt(i));
            if(emotionNames.size() == 20) {//已满一页
                emotionNames.add("");//增加一个表情删除按钮的占位符
                GridView gridView = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gridViewHeight);
                emotionViewPagerData.add(gridView);
                emotionNames = new ArrayList<>();
            }
        }

        if(emotionNames.size() > 0) {//最后一页不满20个
            emotionNames.add("");//增加一个表情删除按钮的占位符
            GridView gridView = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gridViewHeight);
            emotionViewPagerData.add(gridView);
        }

        mIndicatorView.initindicator(emotionViewPagerData.size());

        mEmotionViewPagerAdapter = new EmotionViewPagerAdapter(emotionViewPagerData);
        mViewPager.setAdapter(mEmotionViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mOldPage = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mIndicatorView.moveToNextIndicator(mOldPage, position);
                mOldPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gridViewWidth, int padding, int itemWidth, int gridViewHeight) {
        // 创建GridView
        GridView gridView = new GridView(getActivity());
        //设置点击背景透明
        gridView.setSelector(android.R.color.transparent);
        //设置7列
        gridView.setNumColumns(7);
        gridView.setPadding(padding, padding, padding, padding);
        gridView.setHorizontalSpacing(padding);
        gridView.setVerticalSpacing(padding * 2);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gridViewWidth, gridViewHeight);
        gridView.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionAdapter adapter = new EmotionAdapter(getActivity(), emotionNames, itemWidth, mEmotionType);
        gridView.setAdapter(adapter);
        //设置全局点击事件
        gridView.setOnItemClickListener(new EmotionItemClickListener(getActivity(), mEditText, mEmotionType));
        return gridView;
    }
}
