package cn.ittiger.im.adapter;

import cn.ittiger.im.R;
import cn.ittiger.im.util.EmotionDataHelper;
import cn.ittiger.im.constant.EmotionType;
import cn.ittiger.im.ui.recyclerview.HeaderAndFooterAdapter;
import cn.ittiger.im.ui.recyclerview.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 表情适配器
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class EmotionAdapter extends HeaderAndFooterAdapter<String> {
    private Context mContext;
    private EmotionType mEmotionType;
    private int mEmotionSize;

    public EmotionAdapter(Context context, List<String> list, EmotionType emotionType) {

        super(list);
        mContext = context;
        mEmotionType = emotionType;
        mEmotionSize = context.getResources().getDimensionPixelSize(R.dimen.dimen_30);
    }

    @Override
    public int getItemDataCount() {

        return super.getItemDataCount();
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        ImageView imageView = new ImageView(mContext);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(mWidth, mWidth);
        imageView.setLayoutParams(layoutParams);
        return new EmotionViewHolder(imageView);
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, int position, String item) {

        EmotionViewHolder viewHolder = (EmotionViewHolder) holder;
        if(position == getItemDataCount() - 1) {//最后一个显示表情删除按钮
            viewHolder.mImageView.setImageResource(R.drawable.vector_keyboard_emotion_delete);
        } else {
            viewHolder.mImageView.setImageResource(EmotionDataHelper.getEmotionForName(mEmotionType, item));
        }
    }

    int mWidth;
    public void setWidth(int width) {

        mWidth = width;
    }

    class EmotionViewHolder extends ViewHolder {
        ImageView mImageView;
        public EmotionViewHolder(View itemView) {

            super(itemView);
            mImageView = (ImageView) itemView;
        }
    }
}
