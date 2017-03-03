package cn.ittiger.im.util;

import cn.ittiger.im.constant.MessageType;
import cn.ittiger.util.DateUtil;

import com.orhanobut.logger.Logger;

import android.Manifest;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 录音
 * @author: laohu on 2017/2/7
 * @site: http://ittiger.cn
 */
public class RecordVoiceManager {
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
    private File mVoiceDir;

    public RecordVoiceManager() {

        mVoiceDir = AppFileHelper.getAppChatMessageDir(MessageType.MESSAGE_TYPE_VOICE.value());
    }

    /**
     * 按下录音
     */
    public void startRecordVoice() {

        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置音频采集原
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 内容输出格式
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码方式

            audioFile = new File(mVoiceDir, DateUtil.formatDatetime(new Date(), "yyyyMMddHHmmss") + ".3gp");
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
    public void stopRecordVoice() {

        try {
            recorder.stop();// 停止刻录
            recorder.reset();// 重设
            recorder.release();// 刻录完成一定要释放资源
            recorder = null;
        } catch(Exception e) {
            Logger.e("RecordVoice", e);
        }
    }

    /**
     * 录音过程中的监听
     * @auther: hyl
     * @time: 2015-10-29下午3:11:01
     */
    public static abstract class RecordFinishListener {
        /**
         * 录音结束回调，UI线程
         * @param audioFile		录音文件
         */
        public abstract void onFinish(File audioFile);

        public abstract void onStart();

        public abstract void onCancel(File audioFile);

        public abstract void prepareCancel();
    }

    public File getVoiceFile() {

        return audioFile;
    }
}
