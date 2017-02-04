package cn.ittiger.im.util;

import cn.ittiger.im.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片参数帮助类
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public class ImageLoaderHelper {

    /*
    String imageUri = "http://site.com/image.png"; // 网络图片
    String imageUri = "file:///mnt/sdcard/image.png"; // sd卡图片
    String imageUri = "content://media/external/audio/albumart/13"; //  content provider
    String imageUri = "assets://image.png"; // assets文件夹图片
    String imageUri = "drawable://" + R.drawable.image; // drawable图片
    */
    private static volatile DisplayImageOptions sImageOptions;

    public static DisplayImageOptions getChatImageOptions() {

        if(sImageOptions == null) {
            synchronized (ImageLoaderHelper.class) {
                if(sImageOptions == null) {
                    sImageOptions = new DisplayImageOptions.Builder()
                            .cacheOnDisk(true)//图片下载后是否缓存到SDCard
                            .cacheInMemory(true)//图片下载后是否缓存到内存
                            .bitmapConfig(Bitmap.Config.RGB_565)//图片解码类型，推荐此种方式，减少OOM
                            .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                            .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                            .showImageOnFail(R.drawable.vector_default_image)//图片加载失败后显示的图片
                            .showImageOnLoading(R.drawable.vector_default_image)
                            .build();
                }
            }
        }
        return sImageOptions;
    }

    public static void displayImage(ImageView imageView, String url) {

        displayImage(imageView, url, null);
    }

    public static void displayImage(ImageView imageView, String url, ImageLoadingListener imageLoadingListener) {

        ImageLoader.getInstance().displayImage(url, imageView, getChatImageOptions(), imageLoadingListener);
    }
}
