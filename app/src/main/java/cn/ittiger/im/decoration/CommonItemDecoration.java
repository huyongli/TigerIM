package cn.ittiger.im.decoration;

import cn.ittiger.app.AppContext;
import cn.ittiger.im.R;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 聊天记录列表分割线
 * @author: laohu on 2016/12/27
 * @site: http://ittiger.cn
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final int mSize;

    public CommonItemDecoration() {

        mDivider = new ColorDrawable(AppContext.getInstance().getResources().getColor(R.color.divider_color));
        mSize = AppContext.getInstance().getResources().getDimensionPixelSize(R.dimen.global_divider_size);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.set(0, 0, 0, mSize);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        super.onDraw(c, parent, state);
        int top;
        int bottom;
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            top = child.getBottom() + params.bottomMargin;
            bottom = top + mSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
