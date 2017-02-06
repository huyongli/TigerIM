package cn.ittiger.im.decoration;

import cn.ittiger.app.AppContext;
import cn.ittiger.im.R;
import cn.ittiger.indexlist.ItemType;
import cn.ittiger.indexlist.adapter.IndexStickyViewAdapter;
import cn.ittiger.indexlist.entity.IndexStickyEntity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 联系人列表分割线
 * @author: laohu on 2016/12/27
 * @site: http://ittiger.cn
 */
public class ContactItemDecoration extends RecyclerView.ItemDecoration {

    private final Drawable mDivider;
    private final int mSize;
    private final int mLeftMargin;
    private final int mRightMargin;

    public ContactItemDecoration() {

        mDivider = new ColorDrawable(AppContext.getInstance().getResources().getColor(R.color.divider_color));
        mSize = AppContext.getInstance().getResources().getDimensionPixelSize(R.dimen.global_divider_size);
        mLeftMargin = AppContext.getInstance().getResources().getDimensionPixelSize(R.dimen.contact_item_left_margin);
        mRightMargin = AppContext.getInstance().getResources().getDimensionPixelSize(R.dimen.contact_item_right_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        IndexStickyViewAdapter adapter = (IndexStickyViewAdapter)parent.getAdapter();
        IndexStickyEntity entity = adapter.getItem(position);
        if(entity.getItemType() != ItemType.ITEM_TYPE_INDEX && position < (adapter.getItemCount() - 1)
                && adapter.getItem(position + 1).getItemType() != ItemType.ITEM_TYPE_INDEX) {
            outRect.set(0, 0, 0, mSize);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        super.onDraw(c, parent, state);
        int top;
        int bottom;
        int left = parent.getPaddingLeft() + mLeftMargin;
        int right = parent.getWidth() - parent.getPaddingRight() - mRightMargin;
        final int childCount = parent.getChildCount();
        IndexStickyViewAdapter adapter = (IndexStickyViewAdapter)parent.getAdapter();
        IndexStickyEntity entity;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            entity = adapter.getItem(position);
            if(entity.getItemType() != ItemType.ITEM_TYPE_INDEX &&
                    position < (adapter.getItemCount() - 1) &&
                    adapter.getItem(position + 1).getItemType() != ItemType.ITEM_TYPE_INDEX) {
                //获得child的布局信息
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
                top = child.getBottom() + params.bottomMargin;
                bottom = top + mSize;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
