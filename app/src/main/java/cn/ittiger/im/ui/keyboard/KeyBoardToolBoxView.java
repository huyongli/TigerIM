package cn.ittiger.im.ui.keyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.util.KeyboardUtil;
import cn.ittiger.util.PreferenceHelper;
import cn.ittiger.util.ValueUtil;

import com.orhanobut.logger.Logger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 键盘顶部的输入相关布局
 *
 * @author: laohu on 2017/2/5
 * @site: http://ittiger.cn
 */
public class KeyBoardToolBoxView extends RelativeLayout implements View.OnClickListener {
    private Activity mActivity;
    /**
     * 发送表情开关按钮
     */
    @BindView(R.id.cb_keyboard_face_toolbox)
    CheckBox mCheckBoxFace;
    /**
     * 发送更多类型消息开关按钮
     */
    @BindView(R.id.cb_keyboard_more_toolbox)
    CheckBox mCheckBoxMoreFun;
    /**
     * 发送语音消息按钮
     */
    @BindView(R.id.cb_keyboard_send_voice)
    CheckBox mCheckBoxSendVoice;
    /**
     * 发送文本消息按钮
     */
    @BindView(R.id.btn_keyboard_send_txt)
    Button mButtonSendText;
    /**
     * 文本消息输入框
     */
    @BindView(R.id.et_keyboard_input_toolbox)
    EditText mInputView;
    /**
     * 当前是否为正在录音界面
     */
    private boolean mIsRecordVoice = false;
    /**
     * 当前是否在more功能界面
     */
    private boolean mIsMoreFun = false;
    /**
     * ToolBox视图展示监听
     */
    private ToolBoxViewListener mToolBoxViewListener;
    /**
     * 聊天操作监听
     */
    private ChatKeyboard.KeyboardOperateListener mKeyboardOperateListener;
    /**
     * ToolBox视图中每个选项是否点击选中时监听
     */
    private ToolBoxItemFocusListener mToolBoxItemFocusListener;

    public KeyBoardToolBoxView(Context context) {

        this(context, null);
    }

    public KeyBoardToolBoxView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public KeyBoardToolBoxView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        mActivity = (Activity) context;
        inflate(context, R.layout.chat_keyboard_toolbox_layout, this);
        ButterKnife.bind(this);

        mInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mInputView.getText().toString();
                if (ValueUtil.isEmpty(text)) {//切换语音输入与文本输入
                    mCheckBoxSendVoice.setVisibility(View.VISIBLE);
                    mButtonSendText.setVisibility(View.GONE);
                } else {//文本输入
                    mCheckBoxSendVoice.setVisibility(View.GONE);
                    mButtonSendText.setVisibility(View.VISIBLE);
                }
            }
        });
        mInputView.setOnClickListener(this);
        mButtonSendText.setOnClickListener(this);
        mCheckBoxMoreFun.setOnClickListener(this);
        mCheckBoxFace.setOnClickListener(this);
        mCheckBoxSendVoice.setOnClickListener(this);
        mInputView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && mToolBoxViewListener != null) {//获取文本输入框焦点时会自动出现软键盘，不需要手动弹出
                    if(mToolBoxItemFocusListener != null) {
                        mToolBoxItemFocusListener.toolBoxItemViewFocus(true, R.id.et_keyboard_input_toolbox);
                    }
                    mToolBoxViewListener.hideMoreFunView();
                    mToolBoxViewListener.hideRecordVoiceView();
                    mToolBoxViewListener.hideFaceView();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.et_keyboard_input_toolbox://文本输入框
                if(mToolBoxItemFocusListener != null) {
                    mToolBoxItemFocusListener.toolBoxItemViewFocus(true, R.id.et_keyboard_input_toolbox);
                }
                if(mToolBoxViewListener != null) {//点击文本输入框时会自动出现软键盘，不需要手动弹出
                    mToolBoxViewListener.hideMoreFunView();
                    mToolBoxViewListener.hideRecordVoiceView();
                    mToolBoxViewListener.hideFaceView();
                }
                break;
            case R.id.btn_keyboard_send_txt://发送文本消息按钮
                if (mKeyboardOperateListener != null) {
                    String msg = mInputView.getText().toString();
                    if(!ValueUtil.isEmpty(msg)) {
                        mKeyboardOperateListener.send(msg);
                        mInputView.setText("");
                    }
                }
                break;
            case R.id.cb_keyboard_send_voice://切换到语音输入界面
                mIsRecordVoice = !mIsRecordVoice;
                switchKeyboradToolView(R.id.cb_keyboard_send_voice, mIsRecordVoice);
                break;
            case R.id.cb_keyboard_more_toolbox://切换到more功能界面
                mIsMoreFun = !mIsMoreFun;
                switchKeyboradToolView(R.id.cb_keyboard_more_toolbox, mIsMoreFun);
                break;
        }
    }

    /**
     * 切换布局文件
     *
     * @param funFlag 功能代码
     * @param isShow  是否显示
     */
    private void switchKeyboradToolView(int funFlag, boolean isShow) {

        if(mToolBoxItemFocusListener != null) {
            mToolBoxItemFocusListener.toolBoxItemViewFocus(isShow, funFlag);
        }
        if (isShow == false) {
            KeyboardUtil.showKeyboard(mActivity);
            showInputView();
            if(mToolBoxViewListener != null) {
                mToolBoxViewListener.hideRecordVoiceView();
                mToolBoxViewListener.hideMoreFunView();
                mToolBoxViewListener.hideFaceView();
            }
            return;
        }
        KeyboardUtil.hideKeyboard(mActivity);
        switch (funFlag) {
            case R.id.cb_keyboard_send_voice://语音
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
                        hideInputView();
                        if(mToolBoxViewListener != null) {
                            mToolBoxViewListener.showRecordVoiceView();
                            mToolBoxViewListener.hideMoreFunView();
                            mToolBoxViewListener.hideFaceView();
                        }
                    }
                }, 100);
                break;
            case R.id.cb_keyboard_more_toolbox://more
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
                        showInputView();
                        if(mToolBoxViewListener != null) {
                            mToolBoxViewListener.hideRecordVoiceView();
                            mToolBoxViewListener.showMoreFunView();
                            mToolBoxViewListener.hideFaceView();
                        }
                    }
                }, 100);
                break;
            case R.id.cb_keyboard_face_toolbox://表情
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
                        showInputView();
                        if(mToolBoxViewListener != null) {
                            mToolBoxViewListener.hideRecordVoiceView();
                            mToolBoxViewListener.hideMoreFunView();
                            mToolBoxViewListener.showFaceView();
                        }
                    }
                }, 100);
                break;
        }
    }

    public void unFocusFaceTool() {

        mCheckBoxFace.setChecked(false);
    }

    public void focusFaceTool() {

        mCheckBoxFace.setChecked(true);
    }

    public void unFocusMoreFunTool() {

        mIsMoreFun = false;
        mCheckBoxMoreFun.setChecked(false);
    }

    public void focusMoreFunTool() {

        mCheckBoxMoreFun.setChecked(true);
    }

    public void unFocusVoicTool() {

        mIsRecordVoice = false;
        mCheckBoxSendVoice.setChecked(false);
    }

    public void focusVoicTool() {

        mCheckBoxSendVoice.setChecked(true);
    }

    public void showInputView() {

        if(mInputView.getVisibility() == GONE) {
            mInputView.setVisibility(VISIBLE);
        }
    }

    public void hideInputView() {

        if(mInputView.getVisibility() == VISIBLE) {
            mInputView.setVisibility(GONE);
        }
    }

    public void setToolBoxViewListener(ToolBoxViewListener toolBoxViewListener) {

        mToolBoxViewListener = toolBoxViewListener;
    }

    public void setKeyboardOperateListener(ChatKeyboard.KeyboardOperateListener keyboardOperateListener) {

        mKeyboardOperateListener = keyboardOperateListener;
    }

    public void setToolBoxItemFocusListener(ToolBoxItemFocusListener toolBoxItemFocusListener) {

        mToolBoxItemFocusListener = toolBoxItemFocusListener;
    }

    public interface ToolBoxViewListener {

        void hideMoreFunView();

        void showMoreFunView();

        void hideRecordVoiceView();

        void showRecordVoiceView();

        void hideFaceView();

        void showFaceView();
    }

    public interface ToolBoxItemFocusListener {

        void toolBoxItemViewFocus(boolean hasFocus, int resId);
    }
}
