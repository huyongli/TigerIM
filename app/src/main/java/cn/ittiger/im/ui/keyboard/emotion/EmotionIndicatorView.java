package cn.ittiger.im.ui.keyboard.emotion;

import cn.ittiger.im.R;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * 表情指示器
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class EmotionIndicatorView extends LinearLayout {
    private Map<Integer, View> mIndicatorViewMap = new HashMap<>();//指示器页码索引与指示器圆的映射
    private int mPointIndicatorSize;
    private int mMargin;

    public EmotionIndicatorView(Context context) {

        super(context);
        init();
    }

    public EmotionIndicatorView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public EmotionIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmotionIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        mPointIndicatorSize = getResources().getDimensionPixelSize(R.dimen.dimen_5);
        mMargin = getResources().getDimensionPixelSize(R.dimen.dimen_10);
    }

    public void initindicator(int pageSize) {

        this.removeAllViews();

        LinearLayout.LayoutParams params;
        for(int i = 0; i < pageSize; i++) {
            params = new LayoutParams(mPointIndicatorSize, mPointIndicatorSize);
            if(i != 0) {
                params.leftMargin = mMargin;
            }
            View view = new View(getContext());
            view.setLayoutParams(params);

            if(i == 0) {
                view.setBackgroundResource(R.drawable.keyboard_emotion_indicator_select_bg);
            } else {
                view.setBackgroundResource(R.drawable.keyboard_emotion_indicator_nomal_bg);
            }
            this.mIndicatorViewMap.put(i, view);
            this.addView(view);
        }
    }

    public void moveToNextIndicator(int startPosition, int nextPosition) {

        if(startPosition == nextPosition) {
            return;
        }
        View startIndicator = mIndicatorViewMap.get(startPosition);
        View nextIndicator = mIndicatorViewMap.get(nextPosition);

        startIndicator.setBackgroundResource(R.drawable.keyboard_emotion_indicator_nomal_bg);
        nextIndicator.setBackgroundResource(R.drawable.keyboard_emotion_indicator_select_bg);
    }
}
