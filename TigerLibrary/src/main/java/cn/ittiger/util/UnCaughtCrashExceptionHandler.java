package cn.ittiger.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnCaughtCrashExceptionHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    public static final boolean DEBUG = true;
    private UncaughtExceptionHandler mDefaultHandler;
    private static UnCaughtCrashExceptionHandler INSTANCE;
    private Context mContext;
    private String mLogPath;

    private UnCaughtCrashExceptionHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static UnCaughtCrashExceptionHandler getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new UnCaughtCrashExceptionHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {

        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mLogPath = SdCardUtil.getCacheDir(mContext) + File.separator + "log";
    }

    /**
     * 设置系统崩溃日志记录目录，请在init()方法之后执行
     *
     * @param logPath
     */
    public void setLogPath(String logPath) {

        this.mLogPath = logPath;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */

    @SuppressLint("SimpleDateFormat")
    public void uncaughtException(Thread thread, Throwable ex) {

        String logdir = mLogPath;
        File logDirFile = new File(logdir);
        boolean mkSuccess = false;
        if (!logDirFile.isDirectory()) {
            mkSuccess = logDirFile.mkdirs();
            if (!mkSuccess) {
                mkSuccess = logDirFile.mkdirs();
            }
        }

        StringBuffer sb = new StringBuffer();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            String time = formatter.format(new Date());
            File logFile = new File(logdir, time + ".log");
            if(!logFile.exists()) {
                logFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(logFile);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }

        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {

        if (ex == null) {
            return true;
        }
        final String msg = ex.getLocalizedMessage();
        new Thread() {
            public void run() {

                Looper.prepare();
                Toast.makeText(mContext, "程序出错啦:" + msg, Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }
}
