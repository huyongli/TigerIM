package cn.ittiger.ui;

import cn.ittiger.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义聊天键盘基类
 *
 * @author: laohu on 2017/2/5
 * @site: http://ittiger.cn
 */
public abstract class BaseKeyboardLayout extends LinearLayout implements View.OnClickListener {

    /**
     * 软键盘
     */
    public final static int SHOW_KEYBOARD = 0X1;
    /**
     * 表情
     */
    public final static int SHOW_EMOTION = 0X10;
    /**
     * 其他
     */
    public final static int SHOW_OTHER = 0X11;
    /**
     * 所处Activity的DecorView
     */
    private View mDecorView;
    /**
     * 当前Activity或Fragment的根视图，请保证此根视图的id为rootView
     */
    private View mRootView;
    /**
     * 手动直接唤起软键盘的按键视图
     */
    private View mEvokeKeyBoardView;
    /**
     * emotionView，moreView等视图的容器
     */
    private View mKeyboradContentContainer;
    /**
     * 输入框视图
     */
    private EditText mInputEditText;
    /**
     * 软键盘当前是否显示
     */
    private boolean mIsKeyboardShow;
    /**
     * 当前展示视图的类型
     */
    private int mShowViewType;
    /**
     * 软键盘的高度
     */
    private int mKeyboardHeight;
    /**
     * 除了键盘以外的最小高度
     */
    private int mMminOtherBoardHeight = 300;
    /**
     * Keyboard要展示的所有view，如：emotionView，moreView
     */
    private List<View> showViewList;
    /**
     * 如：emotion按钮与emotionView对应的ViewHolder的映射关系
     */
    private Map<View, KeyboardContentViewHolder> mViewKeyboardContentViewHolderMap;
    /**
     * 手机底部虚拟导航条高度
     */
    private int mNavigationBarHeight = -1;
    private int mHiddenHeight;
    private int mShownHeight;
    private int mLastCoverHeight;
    private int mLastHitBottom;

    public static class KeyboardContentViewHolder {
        private int showType;
        private View showView;

        public KeyboardContentViewHolder(int showType, View showView) {

            this.showType = showType;
            this.showView = showView;
        }

        public int getShowType() {

            return showType;
        }

        public View getShowView() {

            return showView;
        }
    }

    public BaseKeyboardLayout(Context context) {

        super(context);
        init(context);
    }

    public BaseKeyboardLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {

        mViewKeyboardContentViewHolderMap = new HashMap<>();
        showViewList = new ArrayList<>();
        inflateView();
        if (context instanceof Activity) {
            mDecorView = ((Activity) context).getWindow().getDecorView();
        } else {
            mDecorView = this;
        }
        mRootView = mDecorView.findViewById(R.id.keyboard_root_id);
        if(mRootView == null) {
            throw new IllegalStateException("this activity or fragment first view's id must be @id/keyboard_root_id");
        }

        mEvokeKeyBoardView = getEvokeKeyBoardView();
        mInputEditText = getInputEditText();
        mKeyboradContentContainer = getKeyboradContentContainer();
        mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                detectKeyBoardState();
                if (mIsKeyboardShow) {
                    if (mShowViewType == SHOW_KEYBOARD) {
                        hideAllViewExceptKeyBoard();
                    }
                    showView(mKeyboradContentContainer);
                } else {
                    if (mShowViewType == 0) {
                        hideView(mKeyboradContentContainer);
                    } else {
                        showView(mKeyboradContentContainer);
                    }
                }
            }
        });
    }

    /**
     * 渲染自定义布局
     */
    protected abstract void inflateView();

    protected abstract View getKeyboradContentContainer();

    public abstract EditText getInputEditText();

    /**
     * @return 返回键盘按键, 若无则返回null
     */
    protected abstract View getEvokeKeyBoardView();

    /**
     * 检测键盘弹出状态
     */
    private void detectKeyBoardState() {

        Rect visibleRect = new Rect();
        mDecorView.getWindowVisibleDisplayFrame(visibleRect);
        Rect hitRect = new Rect();
        mDecorView.getHitRect(hitRect);
        int coverHeight = hitRect.bottom - visibleRect.bottom;
        if (mLastCoverHeight == coverHeight && mLastHitBottom == hitRect.bottom) { // fix魅族动态显示/隐藏navigationbar没有及时响应
            return;
        }
        mLastHitBottom = hitRect.bottom;
        int deltaCoverHeight = coverHeight - mLastCoverHeight;
        mLastCoverHeight = coverHeight;
        if (coverHeight > mNavigationBarHeight) {
            if ((deltaCoverHeight == mNavigationBarHeight || deltaCoverHeight == -mNavigationBarHeight) && mIsKeyboardShow) {
                // 华为显示/隐藏navigationBar
                mHiddenHeight += deltaCoverHeight;
            }
            mShownHeight = coverHeight - mHiddenHeight;
            int height = mShownHeight;
            int overMinHeight = 0;
            if (height < mMminOtherBoardHeight) {
                overMinHeight = mMminOtherBoardHeight - height;
                height = mMminOtherBoardHeight;
            }
            if (mKeyboardHeight != height) {
                mKeyboardHeight = height;
                mKeyboradContentContainer.getLayoutParams().height = height;
                mKeyboradContentContainer.requestLayout();
            }
            mIsKeyboardShow = true;
            mShowViewType = SHOW_KEYBOARD;
            refreshFrame(visibleRect.bottom + mShownHeight + overMinHeight);
        } else {
            if ((deltaCoverHeight == mNavigationBarHeight || deltaCoverHeight == -mNavigationBarHeight) && !mIsKeyboardShow) {
                // 华为显示/隐藏navigationBar
                mHiddenHeight += deltaCoverHeight;
            }
            if (coverHeight != mHiddenHeight) {
                mHiddenHeight = coverHeight;
            }
            refreshFrame(visibleRect.bottom);
            mIsKeyboardShow = false;
            if (mShowViewType == SHOW_KEYBOARD) {
                mShowViewType = 0;
            }
        }
    }

    /**
     * 刷新frame高度
     *
     * @param bottom
     */
    private void refreshFrame(int bottom) {

        Rect rect = new Rect();
        mRootView.getHitRect(rect);
        int[] location = new int[2];
        mRootView.getLocationInWindow(location);
        int height = bottom - rect.top - location[1];
        if (height != mRootView.getLayoutParams().height) {
            mRootView.getLayoutParams().height = height;
            mRootView.requestLayout();
        }
    }

    protected void addToViewMappingMap(View view, int showType, View showView) {

        mViewKeyboardContentViewHolderMap.put(view, new KeyboardContentViewHolder(showType, showView));
    }

    protected void addToShowViewList(View view) {

        showViewList.add(view);
    }

    @Override
    public void onClick(View v) {

        if (v == mEvokeKeyBoardView) {
            // 点击键盘
            if (mShowViewType == SHOW_KEYBOARD) {
                mShowViewType = 0;
                hideSoftInput();
            } else if (mShowViewType == 0) {
                mShowViewType = SHOW_KEYBOARD;
                showSoftInput();
                showView(mKeyboradContentContainer);
            } else {
                mShowViewType = SHOW_KEYBOARD;
                hideAllViewExceptKeyBoard();
                showSoftInput();
            }
        } else {
            KeyboardContentViewHolder viewHolder = mViewKeyboardContentViewHolderMap.get(v);
            if (viewHolder != null) {
                int show_type = viewHolder.getShowType();
                View showView = viewHolder.getShowView();
                // 点击表情
                if (mShowViewType == show_type) {
                    // 隐藏表情,隐藏layout
                    mShowViewType = 0;
                    hideView(showView);
                    hideView(mKeyboradContentContainer);
                } else if (mShowViewType == SHOW_KEYBOARD) {
                    mShowViewType = show_type;
                    hideSoftInput();
                    showView(showView);
                    showView(mKeyboradContentContainer);
                } else {
                    mShowViewType = show_type;
                    hideAllViewExceptKeyBoard();
                    showView(showView);
                    showView(mKeyboradContentContainer);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNavigationBarHeight == -1) {
            mRootView.getLayoutParams().height = getMeasuredHeight();
            mNavigationBarHeight = getNavigationBarHeight(getContext());
        }
    }

    private void hideSoftInput() {

        if (mInputEditText == null) return;
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputEditText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showSoftInput() {

        if (mInputEditText == null) return;
        mInputEditText.requestFocus();
        ((InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(mInputEditText, 0);
    }

    private void hideView(View view) {

        view.setVisibility(GONE);
    }

    /**
     * 隐藏除了键盘外的view
     */
    private void hideAllViewExceptKeyBoard() {

        for (int i = 0; i < showViewList.size(); i++) {
            hideView(showViewList.get(i));
        }
    }

    public void hideKeyBoardView() {

        mShowViewType = 0;
        hideSoftInput();
        hideView(mKeyboradContentContainer);
    }

    private void showView(View view) {

        view.setVisibility(VISIBLE);
    }

    private static int getNavigationBarHeight(Context context) {

        int navigationBarHeight = 0;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0) {
                navigationBarHeight = rs.getDimensionPixelSize(id);
            }
        } catch (Exception e) {
            // default 0
        }
        return navigationBarHeight;
    }

    /**
     * 设置最小高度(除了键盘外的最小高度)
     *
     * @param minOtherBoardHeight
     */
    public void setMinOtherBoardHeight(int minOtherBoardHeight) {

        this.mMminOtherBoardHeight = minOtherBoardHeight;
    }

}
