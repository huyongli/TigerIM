package cn.ittiger.im.ui.keyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.util.KeyboardUtil;
import cn.ittiger.util.ValueUtil;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * 键盘顶部的输入相关布局
 *
 * @author: laohu on 2017/2/5
 * @site: http://ittiger.cn
 */
public class KeyBoardToolBoxView extends RelativeLayout {
    private Activity mActivity;
    /**
     * 发送表情开关按钮
     */
    @BindView(R.id.cb_keyboard_emotion_toolbox)
    CheckBox mButtonEmotion;
    /**
     * 发送更多类型消息开关按钮
     */
    @BindView(R.id.cb_keyboard_more_toolbox)
    CheckBox mButtonMoreFun;
    /**
     * 发送语音消息按钮
     */
    @BindView(R.id.cb_keyboard_send_voice)
    CheckBox mButtonSendVoice;
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
     * 聊天操作监听
     */
    private ChatKeyboard.KeyboardOperateListener mKeyboardOperateListener;

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
                    mButtonSendVoice.setVisibility(View.VISIBLE);
                    mButtonSendText.setVisibility(View.GONE);
                } else {//文本输入
                    mButtonSendVoice.setVisibility(View.GONE);
                    mButtonSendText.setVisibility(View.VISIBLE);
                }
            }
        });

        mButtonSendText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKeyboardOperateListener != null) {
                    mKeyboardOperateListener.send(mInputView.getText().toString());
                    mInputView.setText("");
                }
            }
        });

        mInputView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                unFocusToolBox();
                return false;
            }
        });
    }

    public void unFocusToolBox() {

        mButtonEmotion.setChecked(false);
        mButtonMoreFun.setChecked(false);
        mButtonSendVoice.setChecked(false);
    }

    public EditText getInputEditText() {

        return mInputView;
    }

    public View getEmotionButton() {

        return mButtonEmotion;
    }

    public View getVoiceButton() {

        return mButtonSendVoice;
    }

    public View getMoreFunButton() {

        return mButtonMoreFun;
    }

    public void setKeyboardOperateListener(ChatKeyboard.KeyboardOperateListener keyboardOperateListener) {

        mKeyboardOperateListener = keyboardOperateListener;
    }
}
