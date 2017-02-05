package cn.ittiger.im.ui.keyboard;

import cn.ittiger.im.util.IMUtil;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * 软键盘监听器助手
 */
public class SoftKeyboardStateHelper implements
        ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);

        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<SoftKeyboardStateListener>();
    private final View activityRootView;
    private int lastSoftKeyboardHeightInPx;
    private boolean isSoftKeyboardOpened;
    private View mKeyboardTopView;

    public SoftKeyboardStateHelper(View activityRootView, View keyboardTopView) {
        this(activityRootView, false);
        mKeyboardTopView = keyboardTopView;
    }

    public SoftKeyboardStateHelper(View activityRootView,
                                   boolean isSoftKeyboardOpened) {
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        // r will be populated with the coordinates of your view that area still
        // visible.
        activityRootView.getWindowVisibleDisplayFrame(r);

        int screenHeight = activityRootView.getRootView().getHeight();
        final int heightDiff = screenHeight - (r.bottom - r.top);//键盘高度

        if(heightDiff > 100) {
            // if more than 100 pixels, its probably a keyboard
            // get status bar height
            int statusBarHeight = IMUtil.getStatusBarHeight();
            int keyboardTopViewHeight = mKeyboardTopView.getHeight();
            int realKeyboardHeight = heightDiff - statusBarHeight - keyboardTopViewHeight;
            if(realKeyboardHeight > 100 && !IMUtil.isKeyboardHeightStored()) {
                IMUtil.storeKeyboardHeight(realKeyboardHeight);
                Log.d("KeyBoard", "The keyboard height is :" + realKeyboardHeight);
            }
        }
        if (!isSoftKeyboardOpened && heightDiff > 100) { // if more than 100
            // pixels, its probably
            // a keyboard...
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (isSoftKeyboardOpened && heightDiff < 100) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    /**
     * Default value is zero (0)
     * 
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;

        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}
