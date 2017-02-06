package cn.ittiger.im.constant;

import cn.ittiger.im.R;
import cn.ittiger.im.fragment.BaseEmotionFragment;
import cn.ittiger.im.fragment.EmotionFragment;

/**
 * 表情类型
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public enum EmotionType {
    EMOTION_TYPE_CLASSIC(R.drawable.vector_keyboard_emotion, EmotionFragment.class),   //经典表情
    EMOTION_TYPE_MORE(R.drawable.vector_emotion_more, BaseEmotionFragment.class);       //+点击更多

    private int mEmotionTypeIcon;
    private Class mFragmentClass;

    EmotionType(int emotionTypeIcon, Class fragmentClass) {

        mEmotionTypeIcon = emotionTypeIcon;
        mFragmentClass = fragmentClass;
    }

    public int getEmotionTypeIcon() {

        return mEmotionTypeIcon;
    }

    public Class getFragmentClass() {

        return mFragmentClass;
    }
}
