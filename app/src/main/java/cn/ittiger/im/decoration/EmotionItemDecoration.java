package cn.ittiger.im.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 聊天记录列表分割线
 * @author: laohu on 2016/12/27
 * @site: http://ittiger.cn
 */
public class EmotionItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public EmotionItemDecoration(int space) {

        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.right = mSpace;//每列都加一个右边距
        outRect.bottom = mSpace;//每列都加一个底部边距

        int position = parent.getChildLayoutPosition(view);
        if(position % 7 == 0) {//第一列
            outRect.left = mSpace;//最后一列再加一个右边距
        }
        outRect.top = mSpace;
    }
}
