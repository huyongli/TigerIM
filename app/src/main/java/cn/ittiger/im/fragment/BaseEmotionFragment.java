package cn.ittiger.im.fragment;

import cn.ittiger.im.constant.EmotionType;

import android.support.v4.app.Fragment;
import android.widget.EditText;

/**
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class BaseEmotionFragment extends Fragment {
    protected EmotionType mEmotionType;
    protected EditText mEditText;

    public BaseEmotionFragment() {

    }

    public void setEmotionType(EmotionType emotionType) {

        mEmotionType = emotionType;
    }

    public void bindToEditText(EditText editText) {

        mEditText = editText;
    }
}
