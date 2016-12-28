package cn.ittiger.im.app;

import cn.ittiger.app.AppContext;
import cn.ittiger.util.UnCaughtCrashExceptionHandler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class App extends Application {

	@Override
	protected void attachBaseContext(Context base) {

		super.attachBaseContext(base);
		AppContext.init(base);
	}

	@Override
	public void onCreate() {
		super.onCreate();

        UnCaughtCrashExceptionHandler.getInstance().init(this);
        initImageLoader();
	}

	private void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
