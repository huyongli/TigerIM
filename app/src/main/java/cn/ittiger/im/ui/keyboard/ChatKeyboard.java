package cn.ittiger.im.ui.keyboard;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;
import cn.ittiger.im.constant.KeyBoardMoreFunType;
import cn.ittiger.ui.BaseKeyboardLayout;

public class ChatKeyboard extends BaseKeyboardLayout implements
        KeyBoardMoreFunView.OnMoreFunItemClickListener{
    @BindView(R.id.keyboardContentContainer)
    View mKeyboardContentContainer;
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
     * 表情
     */
    @BindView(R.id.keyboard_emotion_view)
    View mEmotionView;
    /**
     * 聊天操作监听，如：发送消息，发送文件，选择图片
     */
    private KeyboardOperateListener mKeyboardOperateListener;

    public ChatKeyboard(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    public ChatKeyboard(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public ChatKeyboard(Context context) {

        super(context);
    }

    @Override
    protected void inflateView(Context context) {

        inflate(context, R.layout.chat_keyboard_layout, this);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {

        mRecordVoiceView.setRecordListener(mRecordListener);
        mKeyBoardMoreView.setOnMoreFunItemClickListener(this);

        mToolBoxView.getVoiceButton().setOnClickListener(this);
        addToShowViewList(mRecordVoiceView);
        addToViewMappingMap(mToolBoxView.getVoiceButton(), SHOW_VOICE, mRecordVoiceView);

        mToolBoxView.getMoreFunButton().setOnClickListener(this);
        addToShowViewList(mKeyBoardMoreView);
        addToViewMappingMap(mToolBoxView.getMoreFunButton(), SHOW_MORE_FUN, mKeyBoardMoreView);

        mToolBoxView.getEmotionButton().setOnClickListener(this);
        addToShowViewList(mEmotionView);
        addToViewMappingMap(mToolBoxView.getEmotionButton(), SHOW_EMOTION, mEmotionView);
    }

    @Override
    protected View getKeyboradContentContainer() {

        return mKeyboardContentContainer;
    }

    @Override
    public EditText getInputEditText() {

        return mToolBoxView.getInputEditText();
    }

    @Override
    protected View getEvokeKeyBoardView() {

        return null;
    }

    @Override
    public void hideKeyBoardView() {

        super.hideKeyBoardView();
        mToolBoxView.unFocusToolBox();
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

    public void setKeyboardOperateListener(KeyboardOperateListener keyboardOperateListener) {

        mKeyboardOperateListener = keyboardOperateListener;
        mToolBoxView.setKeyboardOperateListener(mKeyboardOperateListener);
    }

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
}
