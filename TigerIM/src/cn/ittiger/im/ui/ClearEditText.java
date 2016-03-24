package cn.ittiger.im.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import cn.ittiger.im.R;

/**
 * 可清除的EditText
 * @auther: hyl
 * @time: 2015-10-20下午3:02:42
 */
public class ClearEditText extends EditText {
	/**
	 * 清除图标是否显示
	 */
	private boolean mClearIconVisible = false;
	/**
	 * 清除图标
	 */
	private Drawable mClearIcon;

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ClearEditText(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		mClearIcon = getResources().getDrawable(R.drawable.tigerframe_edittext_clear);
		mClearIcon.setBounds(0, 0, mClearIcon.getMinimumWidth(), mClearIcon.getMinimumHeight());
		
		addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String text = getText().toString();
				if(isFocused() && text != null && !text.isEmpty()) {
					if(!mClearIconVisible) {
						setCompoundDrawables(null, null, mClearIcon, null);
						mClearIconVisible = true;
					}
				} else {
					setCompoundDrawables(null, null, null, null);
					mClearIconVisible = false;
				}
			}
		});
	}
	
	 /** 
     * 添加触摸事件 点击之后 出现 清空editText的效果 
     */  
    @SuppressLint("ClickableViewAccessibility")
	@Override  
    public boolean onTouchEvent(MotionEvent motionEvent) {  
        if(mClearIconVisible && mClearIcon != null && motionEvent.getAction() == MotionEvent.ACTION_UP) {  
            if(motionEvent.getX() > getWidth() - getPaddingRight() - mClearIcon.getIntrinsicWidth()) {
                setText("");  
                motionEvent.setAction(MotionEvent.ACTION_CANCEL);  
            }  
        }  
        return super.onTouchEvent(motionEvent);  
    }
}
