package cn.ittiger.im.ui.keyboard.emotion;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.base.BaseActivity;
import cn.ittiger.im.R;
import cn.ittiger.im.decoration.CommonVerticalItemDecoration;
import cn.ittiger.im.fragment.BaseEmotionFragment;
import cn.ittiger.im.constant.EmotionType;
import cn.ittiger.im.ui.recyclerview.CommonRecyclerView;
import cn.ittiger.im.ui.recyclerview.HeaderAndFooterAdapter;
import cn.ittiger.im.ui.recyclerview.ViewHolder;
import cn.ittiger.im.util.EmotionDataHelper;
import cn.ittiger.ui.NoHorizontalScrollViewPager;

import com.orhanobut.logger.Logger;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class KeyBoardEmotionView extends LinearLayout implements CommonRecyclerView.OnItemClickListener {
    /**
     * 使用ViewPager来实现每种表情Tab的内容视图
     */
    @BindView(R.id.noHoriScrollViewPager)
    NoHorizontalScrollViewPager mNoHorizontalScrollViewPager;
    /**
     * 类似与QQ和微信表情页底部的表情类型切换视图，横向滑动的Tab
     */
    @BindView(R.id.emotionTabView)
    CommonRecyclerView mEmotionTabView;

    EmotionTabAdapter mEmotionTabAdapter;
    EditText mEditText;//输入框

    public KeyBoardEmotionView(Context context) {

        super(context);
        initView(context);
    }

    public KeyBoardEmotionView(Context context, AttributeSet attrs) {

        super(context, attrs);
        initView(context);
    }

    public KeyBoardEmotionView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KeyBoardEmotionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {

        setOrientation(VERTICAL);
        inflate(context, R.layout.chat_keyboard_emotion_layout, this);
        ButterKnife.bind(this);

        //设置RecyclerView水平方向
        mEmotionTabView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mEmotionTabView.setOnItemClickListener(this);
        mEmotionTabView.addItemDecoration(new CommonVerticalItemDecoration());
        mEmotionTabAdapter = new EmotionTabAdapter(EmotionDataHelper.getEmotionTabList());

        mEmotionTabView.setAdapter(mEmotionTabAdapter);
        FragmentManager fragmentManager = ((BaseActivity)context).getSupportFragmentManager();
        mNoHorizontalScrollViewPager.setAdapter(new NoHorScrollViewPagerAdapter(fragmentManager, EmotionDataHelper.getEmotionTabList()));
    }

    @Override
    public void onItemClick(HeaderAndFooterAdapter adapter, int position, View itemView) {

        mEmotionTabAdapter.setCurClickPosition(position);
        mEmotionTabAdapter.notifyItemChanged(mEmotionTabAdapter.getOldClickPosition());
        mEmotionTabAdapter.notifyItemChanged(position);

        mNoHorizontalScrollViewPager.setCurrentItem(position, false);
    }

    /**
     * 绑定EditText输入框，以便处理表情输入
     * @param inputEditText
     */
    public void bindToEditText(EditText inputEditText) {

        mEditText = inputEditText;
    }

    class EmotionTabAdapter extends HeaderAndFooterAdapter<EmotionType> {

        private int mOldClickPosition = -1;//上次点击
        private int mCurClickPosition = 0;//当前点击，默认第一个

        public EmotionTabAdapter(List<EmotionType> list) {

            super(list);
        }

        @Override
        public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.chat_keyboard_emotion_tab_item_layout, parent, false);
            return new EmotionTabViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position, EmotionType item) {

            EmotionTabViewHolder viewHolder = (EmotionTabViewHolder) holder;
            viewHolder.icon.setImageResource(item.getEmotionTypeIcon());
            if(position == mCurClickPosition) {//当前点击
                viewHolder.root.setBackgroundColor(getResources().getColor(R.color.chat_keyboard_emotion_tab_item_checked_color));
            } else {
                viewHolder.root.setBackgroundColor(getResources().getColor(R.color.chat_keyboard_emotion_tab_item_unchecked_color));
            }
        }

        public void setCurClickPosition(int curClickPosition) {

            mOldClickPosition = mCurClickPosition;
            mCurClickPosition = curClickPosition;
        }

        public int getOldClickPosition() {

            return mOldClickPosition;
        }
    }

    class EmotionTabViewHolder extends ViewHolder {
        @BindView(R.id.emotionTabItemRoot)
        View root;
        @BindView(R.id.emotionTabItemIcon)
        ImageView icon;

        public EmotionTabViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NoHorScrollViewPagerAdapter extends FragmentPagerAdapter {
        private Map<EmotionType, BaseEmotionFragment> mFragmentMap;
        private List<EmotionType> mEmotionTypeList;

        public NoHorScrollViewPagerAdapter(FragmentManager fm, List<EmotionType> emotionTypeList) {

            super(fm);
            mEmotionTypeList = emotionTypeList;
            mFragmentMap = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {

            EmotionType emotionType = mEmotionTypeList.get(position);
            if(!mFragmentMap.containsKey(emotionType)) {
                try {
                    BaseEmotionFragment fragment = (BaseEmotionFragment) emotionType.getFragmentClass().newInstance();
                    //因为在初始化成功时，表情Fragment还未初始化，
                    //因此在表情Fragment开始初始化时，mEditText已经成功绑定到此视图，因此不用担心mEditText为null
                    fragment.bindToEditText(mEditText);
                    fragment.setEmotionType(emotionType);
                    mFragmentMap.put(emotionType, fragment);
                } catch (Exception e) {
                    Logger.e(e, "create Fragment failure");
                }
            }

            return mFragmentMap.get(emotionType);
        }

        @Override
        public int getCount() {

            return mEmotionTypeList.size();
        }
    }
}
