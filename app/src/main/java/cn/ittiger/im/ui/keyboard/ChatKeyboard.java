package cn.ittiger.im.ui.keyboard;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.constant.KeyBoardMoreFunType;
import cn.ittiger.im.ui.KeyBoardMoreView;
import cn.ittiger.im.ui.RecordVoiceView;
import cn.ittiger.im.util.IMUtil;
import cn.ittiger.util.ValueUtil;

public class ChatKeyboard extends RelativeLayout implements
        SoftKeyboardStateHelper.SoftKeyboardStateListener,
        KeyBoardMoreView.OnMoreFunItemClickListener,
        View.OnClickListener{

    private Context context;

    /**
     * 软键盘监听助手
     */
    private SoftKeyboardStateHelper mKeyboardHelper;
    @BindView(R.id.rl_chat_message_toolbox)
    View mKeyBoardTopView;
    /**
     * 发送表情开关按钮
     */
    @BindView(R.id.cb_chat_face_toolbox)
    CheckBox mCbFace;
    /**
     * 发送更多类型消息开关按钮
     */
    @BindView(R.id.cb_chat_more_toolbox)
    CheckBox mCbMore;
    /**
     * 文本消息输入框
     */
    @BindView(R.id.et_chat_message_toolbox)
    EditText mEtMessage;
    /**
     * 发送文本消息按钮
     */
    @BindView(R.id.btn_chat_send_txt)
    Button mBtnSendTxt;
    /**
     * 发送语音消息按钮
     */
    @BindView(R.id.cb_chat_send_voice)
    CheckBox mCbSendVoice;
    /**
     * 录音界面布局 控件
     */
    @BindView(R.id.recordVoiceView)
    RecordVoiceView mRecordVoiceView;
    /**
     * 选择更多聊天功能布局
     */
    @BindView(R.id.keyBoardMoreVIew)
    KeyBoardMoreView mKeyBoardMoreView;
    /**
     * 聊天操作监听
     */
    private ChatKeyboardOperateListener listener;
    /**
     * 当前是否为正在录音界面
     */
    private boolean mIsRecordVoice = false;
    /**
     * 当前是否在more功能界面
     */
    private boolean mIsMoreFun = false;

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

        this.context = context;
        inflate(context, R.layout.chat_keyboard_layout, this);
        ButterKnife.bind(this, this);
    }

    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        initKeyboardHelper();
        this.initWidget();
        resetViewHieght();
    }

    private void initKeyboardHelper() {

        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
                .getWindow().getDecorView(), mKeyBoardTopView);
        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }

    private void resetViewHieght() {

        if(!IMUtil.isKeyboardHeightStored()) {
            return;
        }
        int keyboardHeight = IMUtil.getKeyboardHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, keyboardHeight);
        mKeyBoardMoreView.setLayoutParams(params);
        mRecordVoiceView.setLayoutParams(params);
    }

    /**
     * 初始化相关控件
     */
    private void initWidget() {

        mRecordVoiceView.setRecordListener(mRecordListener);
        mKeyBoardMoreView.setOnMoreFunItemClickListener(this);
        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mEtMessage.getText().toString();
                if (ValueUtil.isEmpty(text)) {//切换语音输入与文本输入
                    mCbSendVoice.setVisibility(View.VISIBLE);
                    mBtnSendTxt.setVisibility(View.GONE);
                } else {//文本输入
                    mCbSendVoice.setVisibility(View.GONE);
                    mBtnSendTxt.setVisibility(View.VISIBLE);
                }
            }
        });
        mEtMessage.setOnClickListener(this);
        mBtnSendTxt.setOnClickListener(this);
        mCbMore.setOnClickListener(this);
        mCbFace.setOnClickListener(this);
        mCbSendVoice.setOnClickListener(this);
        mEtMessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    hideMoreFunView();
                    hideRecordVoiceView();
                    hideFaceView();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.et_chat_message_toolbox://文本输入框
                hideMoreFunView();
                hideRecordVoiceView();
                hideFaceView();
                break;
            case R.id.btn_chat_send_txt://发送文本消息按钮
                if (listener != null) {
                    String msg = mEtMessage.getText().toString();
                    if(!ValueUtil.isEmpty(msg)) {
                        listener.send(msg);
                        mEtMessage.setText("");
                    }
                }
                break;
            case R.id.cb_chat_send_voice://切换到语音输入界面
                mIsRecordVoice = !mIsRecordVoice;
                switchShowView(R.id.cb_chat_send_voice, mIsRecordVoice);
                break;
            case R.id.cb_chat_more_toolbox://切换到more功能界面
                mIsMoreFun = !mIsMoreFun;
                switchShowView(R.id.cb_chat_more_toolbox, mIsMoreFun);
                break;
        }
    }

    /**
     * 更多功能点击响应
     *
     * @param funType
     */
    @Override
    public void onMoreFunItemClick(KeyBoardMoreFunType funType) {

        if (listener != null) {
            listener.functionClick(funType);
        }
    }

    /**
     * 切换布局文件
     *
     * @param funFlag 功能代码
     * @param isShow  是否显示
     */
    private void switchShowView(int funFlag, boolean isShow) {

        if (isShow == false) {
            showKeyboard(context);
            showMessageEditView();
            hideRecordVoiceView();
            hideMoreFunView();
            hideFaceView();
            return;
        }
        hideKeyboard(context);
        switch (funFlag) {
            case R.id.cb_chat_send_voice://语音
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
                        hideMessageEditView();
                        showRecordVoiceView();
                        hideMoreFunView();
                        hideFaceView();
                    }
                }, 100);
                break;
            case R.id.cb_chat_more_toolbox://more
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
                        showMessageEditView();
                        hideRecordVoiceView();
                        showMoreFunView();
                        hideFaceView();
                    }
                }, 100);
                break;
            case R.id.cb_chat_face_toolbox://表情
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
                        showMessageEditView();
                        hideRecordVoiceView();
                        hideMoreFunView();
                        showFaceView();
                    }
                }, 100);
                break;
        }
    }

    void hideMessageEditView() {

        if(mEtMessage.getVisibility() == VISIBLE) {
            mEtMessage.setVisibility(GONE);
        }
    }

    void showMessageEditView() {

        if(mEtMessage.getVisibility() == GONE) {
            mEtMessage.setVisibility(VISIBLE);
        }
    }

    void hideMoreFunView() {

        if(mKeyBoardMoreView.getVisibility() == VISIBLE) {
            mKeyBoardMoreView.setVisibility(GONE);
        }
        mIsMoreFun = false;
        mCbMore.setChecked(false);
    }

    void showMoreFunView() {

        if(mKeyBoardMoreView.getVisibility() == GONE) {
            mKeyBoardMoreView.setVisibility(VISIBLE);
        }
        mCbMore.setChecked(true);
    }

    void hideRecordVoiceView() {

        if(mRecordVoiceView.getVisibility() == VISIBLE) {
            mRecordVoiceView.setVisibility(GONE);
        }
        mIsRecordVoice = false;
        mCbSendVoice.setChecked(false);
    }

    void showRecordVoiceView() {

        if(mRecordVoiceView.getVisibility() == GONE) {
            mRecordVoiceView.setVisibility(VISIBLE);
        }
        mCbSendVoice.setChecked(true);
    }

    void hideFaceView() {

        mCbFace.setChecked(false);
    }

    void showFaceView() {

        mCbFace.setChecked(true);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        mEtMessage.setVisibility(View.VISIBLE);
        hideMoreFunView();
        hideRecordVoiceView();
    }

    @Override
    public void onSoftKeyboardClosed() {

    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(Context context) {

        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {

        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public ChatKeyboardOperateListener getChatKeyboardOperateListener() {

        return listener;
    }

    public void setChatKeyboardOperateListener(ChatKeyboardOperateListener listener) {

        this.listener = listener;
    }

    /**
     * 录音过程中的监听
     */
    private RecordVoiceView.RecordListener mRecordListener = new RecordVoiceView.RecordListener() {

        @Override
        public void recordFinish(File audioFile) {

            if (listener != null) {
                listener.sendVoice(audioFile);
            }
        }

        public void recordStart() {

            if (listener != null) {
                listener.recordStart();
            }
        }

        ;
    };

    /**
     * 聊天操作监听接口
     *
     * @auther: hyl
     * @time: 2015-10-28下午4:39:06
     */
    public interface ChatKeyboardOperateListener {
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
         * 开始录音，UI线程
         */
        void recordStart();

        /**
         * 点击触发的功能
         *
         * @param funType 功能类型
         */
        void functionClick(KeyBoardMoreFunType funType);
    }
}
