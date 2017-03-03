package cn.ittiger.im.ui.keyboard;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.util.RecordVoiceManager;
import cn.ittiger.util.UIUtil;
import cn.ittiger.util.ValueUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.File;

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
    @BindView(R.id.keyboard_emotion_button)
    CheckBox mButtonEmotion;
    /**
     * 发送更多类型消息开关按钮
     */
    @BindView(R.id.keyboard_moreFun_button)
    CheckBox mButtonMoreFun;
    /**
     * 切换到语音输入按钮
     */
    @BindView(R.id.keyboard_voice_button)
    CheckBox mButtonVoice;
    /**
     * 发送文本消息按钮
     */
    @BindView(R.id.keyboard_sendText_button)
    Button mButtonSendText;
    /**
     * 文本消息输入框
     */
    @BindView(R.id.keyboard_input_editText)
    EditText mInputEditText;
    /**
     * 语音录音按钮
     */
    @BindView(R.id.keyboard_record_voice_button)
    Button mButtonRecordVoice;
    /**
     * 聊天操作监听
     */
    private ChatKeyboard.KeyboardOperateListener mKeyboardOperateListener;
    /**
     * 录音管理类
     */
    private RecordVoiceManager mRecordVoiceManager;
    /**
     * 录音结束监听，一般录音结束时发送
     */
    private RecordVoiceManager.RecordFinishListener mRecordFinishListener;

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

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mInputEditText.getText().toString();
                if (ValueUtil.isEmpty(text)) {//切换语音输入与文本输入
                    mButtonMoreFun.setVisibility(View.VISIBLE);
                    mButtonSendText.setVisibility(View.GONE);
                } else {//文本输入
                    mButtonMoreFun.setVisibility(View.GONE);
                    mButtonSendText.setVisibility(View.VISIBLE);
                }
            }
        });

        mButtonSendText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKeyboardOperateListener != null) {
                    mKeyboardOperateListener.send(mInputEditText.getText().toString());
                    mInputEditText.setText("");
                }
            }
        });

        mInputEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                unFocusAllToolButton();
                return false;
            }
        });

        mRecordVoiceManager = new RecordVoiceManager();
        mButtonRecordVoice.setOnTouchListener(mRecordVoiceButtonTouchListener);
    }

    public void unFocusAllToolButton() {

        mButtonVoice.setChecked(false);
        mButtonEmotion.setChecked(false);
        mButtonMoreFun.setChecked(false);
    }

    /**
     * 表情按钮选中
     */
    public void voiceButtonFocus() {

        mButtonMoreFun.setChecked(false);
        mButtonEmotion.setChecked(false);
        switchToVoiceInput();
    }

    /**
     * 表情按钮选中
     */
    public void moreFunButtonFocus() {

        mButtonVoice.setChecked(false);
        mButtonEmotion.setChecked(false);
        switchToTextInput();
    }

    /**
     * 表情按钮选中
     */
    public void emotionButtonFocus() {

        mButtonVoice.setChecked(false);
        mButtonMoreFun.setChecked(false);
        switchToTextInput();
    }

    /**
     * 切换到语音输入
     */
    public void switchToVoiceInput() {

        if(mInputEditText.getVisibility() == VISIBLE) {
            mInputEditText.setVisibility(GONE);
        }
        if(mButtonRecordVoice.getVisibility() == GONE) {
            mButtonRecordVoice.setVisibility(VISIBLE);
        }
    }

    /**
     * 切换到文本输入
     */
    public void switchToTextInput() {

        mInputEditText.setVisibility(VISIBLE);
        mButtonRecordVoice.setVisibility(GONE);
    }

    public EditText getInputEditText() {

        return mInputEditText;
    }

    public View getEmotionButton() {

        return mButtonEmotion;
    }

    public View getVoiceButton() {

        return mButtonVoice;
    }

    public Button getButtonRecordVoice() {

        mButtonRecordVoice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        return mButtonRecordVoice;
    }

    public View getMoreFunButton() {

        return mButtonMoreFun;
    }

    public void setKeyboardOperateListener(ChatKeyboard.KeyboardOperateListener keyboardOperateListener) {

        mKeyboardOperateListener = keyboardOperateListener;
    }

    public void setRecordFinishListener(RecordVoiceManager.RecordFinishListener recordFinishListener) {

        mRecordFinishListener = recordFinishListener;
    }

    OnTouchListener mRecordVoiceButtonTouchListener = new OnTouchListener() {
        float mDownY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = event.getRawY();
                    if(mRecordFinishListener != null) {
                        mRecordFinishListener.onStart();
                    }
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        UIUtil.showToast(getContext(), "此应用录音权限已被禁止，请先打开");
                    } else {
                        mRecordVoiceManager.startRecordVoice();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mRecordVoiceManager.stopRecordVoice();
                    File voiceFile = mRecordVoiceManager.getVoiceFile();
                    if(mRecordFinishListener != null) {
                        float distance = Math.abs(event.getRawY() - mDownY);
                        if(distance > 80) {//滑动超过80则提示取消
                            mRecordFinishListener.onCancel(voiceFile);
                        } else {
                            mRecordFinishListener.onFinish(voiceFile);
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(mRecordFinishListener != null) {
                        float distance = Math.abs(event.getRawY() - mDownY);
                        if(distance > 80) {//滑动超过80则提示取消
                            mRecordFinishListener.prepareCancel();
                        } else {
                            mRecordFinishListener.onStart();
                        }
                    }
                    break;
            }
            return false;
        }
    };
}
