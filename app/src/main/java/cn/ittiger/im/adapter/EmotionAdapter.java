package cn.ittiger.im.adapter;

import cn.ittiger.im.R;
import cn.ittiger.im.constant.EmotionType;
import cn.ittiger.im.util.EmotionDataHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * 表情适配器
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class EmotionAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mEmotionNames;
    private int mItemWidth;
    private EmotionType mEmotionType;

    public EmotionAdapter(Context context, List<String> emotionNames, int itemWidth, EmotionType emotionType) {

        this.mContext = context;
        this.mEmotionNames = emotionNames;
        this.mItemWidth = itemWidth;
        this.mEmotionType = emotionType;
    }

    @Override
    public int getCount() {

        return mEmotionNames.size();
    }

    @Override
    public String getItem(int position) {

        return mEmotionNames.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EmotionViewHolder emotionViewHolder;
        if(convertView == null) {
            ImageView emotionIcon = new ImageView(mContext);
            // 设置内边距
            emotionIcon.setPadding(mItemWidth / 8, mItemWidth / 8, mItemWidth / 8, mItemWidth / 8);
            LayoutParams params = new LayoutParams(mItemWidth, mItemWidth);
            emotionIcon.setLayoutParams(params);

            convertView = emotionIcon;
            emotionViewHolder = new EmotionViewHolder(emotionIcon);
            convertView.setTag(emotionViewHolder);
        } else {
            emotionViewHolder = (EmotionViewHolder) convertView.getTag();
        }

        //判断是否为最后一个item
        if (position == getCount() - 1) {
            emotionViewHolder.mImageView.setImageResource(R.drawable.vector_keyboard_emotion_delete);
        } else {
            String emotionName = mEmotionNames.get(position);
            emotionViewHolder.mImageView.setImageResource(EmotionDataHelper.getEmotionForName(mEmotionType, emotionName));
        }

        return emotionViewHolder.mImageView;
    }

    class EmotionViewHolder {
        ImageView mImageView;

        public EmotionViewHolder(ImageView imageView) {

            mImageView = imageView;
        }
    }
}
