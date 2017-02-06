package cn.ittiger.im.ui.keyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.constant.KeyBoardMoreFunType;
import cn.ittiger.im.ui.recyclerview.CommonRecyclerView;
import cn.ittiger.im.ui.recyclerview.HeaderAndFooterAdapter;
import cn.ittiger.im.ui.recyclerview.ViewHolder;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * KeyBoard更多功能视图
 * @author: laohu on 2017/2/4
 * @site: http://ittiger.cn
 */
public class KeyBoardMoreFunView extends FrameLayout implements CommonRecyclerView.OnItemClickListener {
    @BindView(R.id.moreFunGridView)
    CommonRecyclerView mRecyclerView;
    OnMoreFunItemClickListener mOnItemClickListener;
    MoreAdapter mAdapter;

    public KeyBoardMoreFunView(Context context) {

        this(context, null);
    }

    public KeyBoardMoreFunView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public KeyBoardMoreFunView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        inflate(context, R.layout.chat_keyboard_more_layout, this);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        mRecyclerView.setOnItemClickListener(this);

        TypedArray iconAr = context.getResources().obtainTypedArray(R.array.keyboard_more_icons);
        TypedArray nameAr = context.getResources().obtainTypedArray(R.array.keyboard_more_names);
        if(iconAr.length() != nameAr.length()) {
            throw new IllegalArgumentException("array keyboard_more_icons's length must be equals to array keyboard_more_names");
        }
        int len = iconAr.length();
        int[] icons = new int[len];
        int[] names = new int[len];

        for (int i = 0; i < len; i++) {
            icons[i] = iconAr.getResourceId(i, 0);
            names[i] = nameAr.getResourceId(i, 0);
        }
        iconAr.recycle();
        nameAr.recycle();

        List<MoreFunBean> list = new ArrayList<>();
        for(int i = 0; i < icons.length; i++) {
            list.add(new MoreFunBean(icons[i], names[i], i));
        }
        mAdapter = new MoreAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    class MoreAdapter extends HeaderAndFooterAdapter<MoreFunBean> {
        public MoreAdapter(List<MoreFunBean> list) {

            super(list);
        }

        @Override
        public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.chat_keyboard_more_item_layout, parent, false);
            return new MoreViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(ViewHolder holder, int position, MoreFunBean item) {

            MoreViewHolder viewHolder = (MoreViewHolder) holder;
            viewHolder.mImageView.setImageResource(item.mIconResId);
            viewHolder.mTextView.setText(getResources().getString(item.mNameResId));
        }
    }

    class MoreViewHolder extends ViewHolder {
        @BindView(R.id.more_fun_icon)
        ImageView mImageView;
        @BindView(R.id.more_fun_name)
        TextView mTextView;
        public MoreViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class MoreFunBean {
        int mIconResId;//功能图标
        int mNameResId;//功能名称
        int mIndex;//功能索引

        public MoreFunBean(int iconResId, int nameResId, int index) {

            mIconResId = iconResId;
            mNameResId = nameResId;
            mIndex = index;
        }
    }

    @Override
    public void onItemClick(HeaderAndFooterAdapter adapter, int position, View itemView) {

        if(mOnItemClickListener != null) {
            KeyBoardMoreFunType type = KeyBoardMoreFunType.getFunType(mAdapter.getItem(position).mIndex);
            mOnItemClickListener.onMoreFunItemClick(type);
        }
    }

    public void setOnMoreFunItemClickListener(OnMoreFunItemClickListener onItemClickListener) {

        mOnItemClickListener = onItemClickListener;
    }

    public interface OnMoreFunItemClickListener {

        void onMoreFunItemClick(KeyBoardMoreFunType type);
    }
}
