package cn.ittiger.im.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ittiger.im.R;

/**
 * 每个Activity的头部自定义控件，包含如下内容：
 * 1.左边返回按钮，可以设置图标和文字
 * 2.当前Activity的标题文字
 * 3.右边的功能按钮
 * @auther: hyl
 * @time: 2015-10-20上午10:29:26
 */
public class TopTitleBar extends RelativeLayout {
	/**
	 * 左边返回按钮部分布局
	 */
	private LinearLayout mLeftLayout;
	/**
	 * 左边的返回图标
	 */
	private ImageView mLeftImageView;
	/**
	 * 左边返回按钮的文字
	 */
	private TextView mLeftTextView;
	/**
	 * 中间的标题
	 */
	private TextView mTitleView;
	/**
	 * 右边菜单项布局
	 */
	private LinearLayout mRightLayout;
	
	
	/**
	 * 左边返回按钮文字颜色，默认为白色
	 */
	private int mLeftTextColor;
	/**
	 * 左边返回按钮文字，默认为：返回
	 */
	private String mLeftText;
	/**
	 * 左边返回按钮文字大小，默认大小：14
	 */
	private float mLeftTextSize;
	/**
	 * 左边返回图标
	 */
	private Drawable mLeftDrawale;
	/**
	 * 左边返回部分布局是否显示，默认显示
	 */
	private boolean mLeftVisible;
	/**
	 * 左边返回部分的文字是否显示，默认显示，当mLeftVisible为false时，该变量不起作用
	 */
	private boolean mLeftTextVisible;
	/**
	 * 坐标返回图标是否显示，默认显示，当mLeftVisible为false时，该变量不起作用
	 */
	private boolean mLeftImageVisible;
	
	/**
	 * 标题文字
	 */
	private String mTitleText;
	/**
	 * 标题文字颜色，默认为白色
	 */
	private int mTitleTextColor;
	/**
	 * 标题文字大小，默认大小：14
	 */
	private float mTitleTextSize;
	
	/**
	 * 头部整个控件的背景颜色，默认为#18B5EA
	 */
	private int mBackgroundColor;
	
	/**
	 * 返回按钮点击事件
	 */
	private LeftClickListener mLeftClickListener;
	
	/**
	 * 默认字体大小
	 */
	private final int DEFAULT_TEXT_SIZE = 15;
	/**
	 * 头部边距
	 */
	private final int VIEW_PADDING = 5;
	
	public TopTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TopTitleBar(Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	public TopTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		float scale = context.getResources().getDisplayMetrics().density;

		TypedArray attArray = context.obtainStyledAttributes(attrs, R.styleable.TopTitleBar);
		
		mBackgroundColor = attArray.getColor(R.styleable.TopTitleBar_backgroundColor, getResources().getColor(R.color.topTitleBar_default_background));
		
		mLeftText = attArray.getString(R.styleable.TopTitleBar_leftText);
		mLeftTextColor = attArray.getColor(R.styleable.TopTitleBar_leftTextColor, getResources().getColor(R.color.white));
		mLeftTextSize = attArray.getDimension(R.styleable.TopTitleBar_leftTextSize, DEFAULT_TEXT_SIZE);
		mLeftDrawale = attArray.getDrawable(R.styleable.TopTitleBar_leftDrawable);
		mLeftVisible = attArray.getBoolean(R.styleable.TopTitleBar_leftVisible, true);
		mLeftTextVisible = attArray.getBoolean(R.styleable.TopTitleBar_leftTextVisible, true);
		mLeftImageVisible = attArray.getBoolean(R.styleable.TopTitleBar_leftImageVisible, true);
		
		mTitleText = attArray.getString(R.styleable.TopTitleBar_titleText);
		mTitleTextColor = attArray.getColor(R.styleable.TopTitleBar_titleTextColor, getResources().getColor(R.color.white));
		mTitleTextSize = attArray.getDimension(R.styleable.TopTitleBar_titleTextSize, DEFAULT_TEXT_SIZE);
		
		//获取完TypedArray的值后，一般要调用recyle方法来避免重新创建的时候的错误
		attArray.recycle();
		
		setBackgroundColor(mBackgroundColor);
		
		mLeftImageView = new ImageView(context);
		mLeftTextView = new TextView(context);
		mTitleView = new TextView(context);
		
		//左边图标与文字布局
		mLeftLayout = new LinearLayout(context);
		mLeftLayout.setOrientation(LinearLayout.HORIZONTAL);
		mLeftLayout.setClickable(true);
		
		//为创建的控件赋属性值，值即为在xml中配置的属性值
		if(mLeftDrawale != null) {
			mLeftImageView.setBackgroundDrawable(mLeftDrawale);
		} else {
			mLeftImageView.setBackgroundResource(R.drawable.tigerframe_icon_back_btn_selector);
		}
		
		if(mLeftText == null || mLeftText.length() == 0) {//坐标文字为空
			mLeftText = "返回"; 
		}

		LinearLayout.LayoutParams leftChildParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		mLeftLayout.addView(mLeftImageView, leftChildParams);
		if(!mLeftImageVisible) {
			mLeftImageView.setVisibility(View.GONE);
		}
		
		mLeftTextView.setText(mLeftText);
		mLeftTextView.setPadding((int) (VIEW_PADDING * scale + 0.5f), 0, 0, 0);
		mLeftTextView.setTextColor(mLeftTextColor);
		mLeftTextView.setTextSize(mLeftTextSize);
		mLeftTextView.setGravity(Gravity.CENTER_VERTICAL);
		mLeftLayout.addView(mLeftTextView, leftChildParams);
		if(!mLeftTextVisible) {
			mLeftTextView.setVisibility(View.GONE);
		}
		
		mTitleView.setText(mTitleText);
		mTitleView.setTextColor(mTitleTextColor);
		mTitleView.setTextSize(mTitleTextSize);
		mTitleView.setGravity(Gravity.CENTER);
		
		RelativeLayout.LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
	    //添加左边返回部分到RelativeLayout
	    addView(mLeftLayout, leftParams);
	    
	    //添加右边菜单项布局到当前RelativeLayout
	    mRightLayout = new LinearLayout(context);
	    mRightLayout.setOrientation(LinearLayout.HORIZONTAL);
	    mRightLayout.setGravity(Gravity.CENTER_VERTICAL);
	    RelativeLayout.LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
	    rightParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
	    addView(mRightLayout, rightParams);
	    
	    RelativeLayout.LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
	    //添加标题部分到ViewGroup
	    addView(mTitleView, titleParams);
	    
		int padding = (int) (VIEW_PADDING * scale + 0.5f);
	    setPadding(padding, padding, padding, padding);
	    
	    setLeftVisible(mLeftVisible);
	    mLeftLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLeftClickListener != null) {
					mLeftClickListener.onLeftClick();
				}
			}
		});
	}
	
	/**
	 * 获取菜单布局，菜单项需要自己实现
	 * @return
	 */
	public LinearLayout getMenuLayout() {
		return mRightLayout;
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title) {
		mTitleView.setText(title);
	}
	
	/**
	 * 设置左边文字
	 * @param txt
	 */
	public void setLeftText(String txt) {
		mLeftTextView.setText(txt);
	}
	
	/**
	 * 设置左边图标
	 * @param resId
	 */
	public void setLeftDrawable(int resId) {
		mLeftImageView.setBackgroundResource(resId);
	}
	
	/**
	 * 设置左边按钮点击事件
	 * @param listener
	 */
	public void setLeftClickListener(LeftClickListener listener) {
		mLeftClickListener = listener;
	}
	
	/**
	 * 设置左边返回部分布局是否显示
	 * @param visible	true：显示，false：不显示
	 */
	public void setLeftVisible(boolean visible) {
		if(visible) {
			mLeftLayout.setVisibility(View.VISIBLE);
		} else {
			mLeftLayout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 左边按点击事件
	 * @auther: hyl
	 * @time: 2015-10-20上午11:34:28
	 */
	public interface LeftClickListener {
		public void onLeftClick();
	}
	
	
	/**
	 * 设置左边返回的图标是否显示,默认显示
	 * 
	 * @param visisble
	 */
	public boolean setLeftImageVisible(boolean visisble) {
		if (visisble) {
			mLeftImageView.setVisibility(View.VISIBLE);
			return true;
		} else {
			mLeftImageView.setVisibility(View.GONE);
			mLeftTextView.setPadding(5, 15, 0, 0);
			return false;
		}
	}
	
	/**
	 * 设置左边返回的文字是否显示，默认显示
	 * @param visible
	 * @return
	 */
	public boolean setLeftTextVisible(boolean visible) {
		if(visible) {
			mLeftTextView.setVisibility(View.VISIBLE);
			return true;
		} else {
			mLeftTextView.setVisibility(View.GONE);
			return false;
		}
	}
}
