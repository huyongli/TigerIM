package cn.ittiger.im.ui.keyboard;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.constant.KeyBoardMoreFunType;
import cn.ittiger.util.PreferenceHelper;

import com.orhanobut.logger.Logger;

public class ChatKeyboard extends RelativeLayout implements
        SoftKeyboardStateHelper.SoftKeyboardStateListener,
        KeyBoardMoreFunView.OnMoreFunItemClickListener,
        KeyBoardToolBoxView.ToolBoxViewListener,
        KeyBoardToolBoxView.ToolBoxItemFocusListener {
    private static final String KEY_SOFT_INPUT_HEIGHT = "soft_input_height";
    private Activity mActivity;
    /**
     * 展示消息内容的视图，一般是AbsListView 或 RecyclerView
     */
    private View mContentView;
    /**
     * 软键盘监听助手
     */
    private SoftKeyboardStateHelper mKeyboardHelper;
    /**
     * 输入框及其他功能布局视图
     */
    @BindView(R.id.keyboard_toolbox_view)
    KeyBoardToolBoxView mToolBoxView;
    /**
     * 录音界面布局 控件
     */
    @BindView(R.id.keyboard_record_voice_view)
    KeyBoardRecordVoiceView mRecordVoiceView;
    /**
     * 选择更多聊天功能布局
     */
    @BindView(R.id.keyboard_more_fun_view)
    KeyBoardMoreFunView mKeyBoardMoreView;
    /**
     * 聊天操作监听，如：发送消息，发送文件，选择图片
     */
    private KeyboardOperateListener mKeyboardOperateListener;

    public ChatKeyboard(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        init(context);
    }

    public ChatKeyboard(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    public ChatKeyboard(Context context) {

        super(context);
        init(context);
    }

    private void init(Context context) {

        this.mActivity = (Activity) context;
        inflate(context, R.layout.chat_keyboard_layout, this);
        ButterKnife.bind(this);
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        initKeyboardHelper();
        this.initWidget();
        resetViewHieght();
    }

    public void bindToContentView(View view) {

        mContentView = view;
    }

    private void initKeyboardHelper() {

//        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
//                .getWindow().getDecorView(), mToolBoxView);
//        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }

    private void resetViewHieght() {

//        if(!IMUtil.isKeyboardHeightStored()) {
//            return;
//        }
//        int keyboardHeight = IMUtil.getKeyboardHeight();
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, keyboardHeight);
//        mKeyBoardMoreView.setLayoutParams(params);
//        mRecordVoiceView.setLayoutParams(params);
    }

    /**
     * 初始化相关控件
     */
    private void initWidget() {

        mRecordVoiceView.setRecordListener(mRecordListener);
        mKeyBoardMoreView.setOnMoreFunItemClickListener(this);
        mToolBoxView.setKeyboardOperateListener(mKeyboardOperateListener);
        mToolBoxView.setToolBoxViewListener(this);
        mToolBoxView.setToolBoxItemFocusListener(this);
    }

    /**
     * 更多功能点击响应
     *
     * @param funType
     */
    @Override
    public void onMoreFunItemClick(KeyBoardMoreFunType funType) {

        if (mKeyboardOperateListener != null) {
            mKeyboardOperateListener.functionClick(funType);
        }
    }

    @Override
    public void hideMoreFunView() {

        if(mKeyBoardMoreView.getVisibility() == VISIBLE) {
            mKeyBoardMoreView.setVisibility(GONE);
        }
        mToolBoxView.unFocusMoreFunTool();
    }

    @Override
    public void showMoreFunView() {

        if(mKeyBoardMoreView.getVisibility() == GONE) {
            mKeyBoardMoreView.setVisibility(VISIBLE);
        }
        mToolBoxView.focusMoreFunTool();
    }

    @Override
    public void hideRecordVoiceView() {

        if(mRecordVoiceView.getVisibility() == VISIBLE) {
            mRecordVoiceView.setVisibility(GONE);
        }
        mToolBoxView.unFocusVoicTool();
    }

    @Override
    public void showRecordVoiceView() {

        if(mRecordVoiceView.getVisibility() == GONE) {
            mRecordVoiceView.setVisibility(VISIBLE);
        }
        mToolBoxView.focusVoicTool();
    }

    @Override
    public void hideFaceView() {

        mToolBoxView.unFocusFaceTool();
    }

    @Override
    public void showFaceView() {

        mToolBoxView.focusFaceTool();
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {

        mToolBoxView.showInputView();
        hideMoreFunView();
        hideRecordVoiceView();
    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    public void setKeyboardOperateListener(KeyboardOperateListener listener) {

        this.mKeyboardOperateListener = listener;
    }

    /**
     * 录音过程中的监听
     */
    private KeyBoardRecordVoiceView.RecordListener mRecordListener = new KeyBoardRecordVoiceView.RecordListener() {

        @Override
        public void recordFinish(File audioFile) {

            if (mKeyboardOperateListener != null) {
                mKeyboardOperateListener.sendVoice(audioFile);
            }
        }
    };

    /**
     * 聊天操作监听接口
     *
     * @auther: hyl
     * @time: 2015-10-28下午4:39:06
     */
    public interface KeyboardOperateListener {
        /**
         * 发送文本消息接口
         *
         * @param message
         */
        void send(String message);

        /**
         * 录音完成，发送语音文件，UI线程
         *
         * @param audioFile
         */
        void sendVoice(File audioFile);

        /**
         * 点击触发的功能
         *
         * @param funType 功能类型
         */
        void functionClick(KeyBoardMoreFunType funType);
    }

    /**-------------- 防闪烁 -------------------**/


    @Override
    public void toolBoxItemViewFocus(boolean hasFocus, int resId) {

        lockContentViewHeight();
        switch (resId) {
            case R.id.cb_keyboard_send_voice://语音
                lockToolContentViewHeight(mRecordVoiceView);
                break;
            case R.id.cb_keyboard_more_toolbox://more
                lockToolContentViewHeight(mKeyBoardMoreView);
                break;
            case R.id.cb_keyboard_face_toolbox://表情
                break;
        }
        unlockContentViewHeightDelayed();
    }

    private void lockToolContentViewHeight(View toolContentView) {

        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = getKeyBoardHeight();
        }
        toolContentView.getLayoutParams().height = softInputHeight;
        unlockContentViewHeightDelayed();
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentViewHeight() {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentViewHeightDelayed() {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 300L);
    }

    /**------------------------ 键盘高度相关 ----------------------------**/
    /**
     * 获取软件盘的高度
     * @return
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getNavigationBarHeight();
        }

        if (softInputHeight < 0) {
            Logger.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
            PreferenceHelper.putInt(KEY_SOFT_INPUT_HEIGHT, softInputHeight);
        }
        return softInputHeight;
    }


    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getNavigationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     * @return
     */
    public int getKeyBoardHeight(){

        return PreferenceHelper.getInt(KEY_SOFT_INPUT_HEIGHT, 787);
    }
}
