package cn.ittiger.im.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.EmotionAdapter;
import cn.ittiger.im.decoration.EmotionItemDecoration;
import cn.ittiger.im.util.EmotionDataHelper;
import cn.ittiger.im.ui.keyboard.emotion.EmotionIndicatorView;
import cn.ittiger.im.ui.keyboard.emotion.EmotionItemClickListener;
import cn.ittiger.im.adapter.EmotionViewPagerAdapter;
import cn.ittiger.im.ui.recyclerview.CommonRecyclerView;
import cn.ittiger.util.DisplayUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        int spacing = DisplayUtils.dp2px(getActivity(), 15);
        // 动态计算item的宽度和高度
        int itemWidth = (int) ((screenWidth - spacing * 8) / 7 + 0.5f);

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
                CommonRecyclerView gridView = createEmotionPageView(emotionNames, spacing, itemWidth);
                emotionViewPagerData.add(gridView);
                emotionNames = new ArrayList<>();
            }
        }

        if(emotionNames.size() > 0) {//最后一页不满20个
            emotionNames.add("");//增加一个表情删除按钮的占位符
            CommonRecyclerView gridView = createEmotionPageView(emotionNames, spacing, itemWidth);
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

    private CommonRecyclerView createEmotionPageView(List<String> emotionNames, int padding, int width) {

        CommonRecyclerView gridView = new CommonRecyclerView(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gridView.addItemDecoration(new EmotionItemDecoration(padding));
        gridView.setLayoutParams(layoutParams);
        gridView.setLayoutManager(new GridLayoutManager(getActivity(), 7));//7列
        EmotionAdapter adapter = new EmotionAdapter(getActivity(), emotionNames, mEmotionType);
        adapter.setWidth(width);
        gridView.setOnItemClickListener(new EmotionItemClickListener(getActivity(), mEditText, mEmotionType));
        gridView.setAdapter(adapter);
        return gridView;
    }
}
