package cn.ittiger.im.app;

import cn.ittiger.app.AppContext;
import cn.ittiger.database.SQLiteDBConfig;
import cn.ittiger.im.R;
import cn.ittiger.im.util.AppFileHelper;
import cn.ittiger.util.UnCaughtCrashExceptionHandler;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.IOException;

public class App extends Application implements IDbApplication {
    /**
     * 本地数据库配置
     */
    private SQLiteDBConfig mDBConfig;

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        AppContext.init(base);
    }

    @Override
    public void onCreate() {

        super.onCreate();

        UnCaughtCrashExceptionHandler handler = UnCaughtCrashExceptionHandler.getInstance();
        handler.init(this);
        handler.setLogPath(AppFileHelper.getAppCrashDir());

        Logger.init("Smack");
        initImageLoader();
    }

    private void initImageLoader() {

        File cacheDir = new File(AppFileHelper.getAppImageCacheDir());
        if(!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        try {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                    .threadPoolSize(3)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    .diskCache(new LruDiskCache(cacheDir, new Md5FileNameGenerator(), 50 * 1024 * 1024))
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .diskCacheFileCount(100)
                    .writeDebugLogs()
                    .build();
            ImageLoader.getInstance().init(config);
        } catch (IOException e) {
            Logger.e(e, "ImageLoader init failure");
        }
    }

    @Override
    public SQLiteDBConfig getGlobalDbConfig() {

        if(mDBConfig == null) {
            synchronized (App.class) {
                if(mDBConfig == null) {
                    mDBConfig = new SQLiteDBConfig(AppContext.getInstance());
                    mDBConfig.setDbName(getResources().getString(R.string.app_name) + ".db");
                    //本地数据库文件保存在应用文件目录
                    mDBConfig.setDbDirectoryPath(AppFileHelper.getAppDBDir());
                }
            }
        }
        return mDBConfig;
    }
}
