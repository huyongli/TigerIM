package cn.ittiger.im.ui;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import cn.ittiger.im.R;
import cn.ittiger.util.DateUtil;
import cn.ittiger.util.SdCardUtil;

/**
 * 按住说话
 * @auther: hyl
 * @time: 2015-10-29下午2:43:38
 */
@SuppressLint("ClickableViewAccessibility")
public class RecordVoiceView extends RelativeLayout {
	/**
	 * 录音对象
	 */
	private MediaRecorder recorder;
	/**
	 * 当前录音文件
	 */
	private File audioFile;
	/**
	 * 文件存储目录
	 */
	private String fileDir;
	/**
	 * 录音监听
	 */
	private RecordListener recordListener;

	public RecordVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public RecordVoiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RecordVoiceView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.setClickable(true);
		fileDir = SdCardUtil.getCacheDir(context);
		inflate(context, R.layout.chat_keyboard_voice_layout, this);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()) {
			case MotionEvent.ACTION_DOWN://按下
				if(recordListener != null) {
					recordListener.recordStart();
				}
				startRecordVoice();
				break;
			case MotionEvent.ACTION_UP://松开手指
				stopRecordVoice();
				if(recordListener != null) {
					recordListener.recordFinish(audioFile);
				}
				break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
	/**
	 * 按下录音
	 */
	private void startRecordVoice() {
		try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置音频采集原
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 内容输出格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码方式
            
            audioFile = new File(fileDir, DateUtil.formatDatetime(new Date(), "yyyyMMddHHmmss") + ".3gp");
            if(!audioFile.exists()) {
        		boolean flag = audioFile.createNewFile();
        		Log.i("speak", String.valueOf(flag));
        	}
            recorder.setOutputFile(audioFile.getAbsolutePath());

            recorder.prepare(); // 预期准备
            recorder.start();//开始录音

        } catch (IllegalStateException e) {
            recorder = null;
        } catch (IOException e) {
        	recorder = null;
        }
	}
	
	/**
	 * 停止录音
	 */
	private void stopRecordVoice() {
		recorder.stop();// 停止刻录
        recorder.reset();// 重设
        recorder.release();// 刻录完成一定要释放资源
        recorder = null;
	}
	
	public RecordListener getRecordListener() {
		return recordListener;
	}

	public void setRecordListener(RecordListener recordListener) {
		this.recordListener = recordListener;
	}

	/**
	 * 录音过程中的监听
	 * @auther: hyl
	 * @time: 2015-10-29下午3:11:01
	 */
	public static abstract class RecordListener {
		/**
		 * 录音结束回调，UI线程
		 * @param audioFile		录音文件
		 */
		public abstract void recordFinish(File audioFile);
		/**
		 * 录音开始，UI线程
		 */
		public void recordStart() {
			
		}
	}
}
