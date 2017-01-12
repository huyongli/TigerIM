package cn.ittiger.im.util;

import cn.ittiger.im.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.graphics.Bitmap;

/**
 * 图片参数帮助类
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public class ImageLoaderHelper {

    public static DisplayImageOptions getChatImageOptions() {

        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)//图片下载后是否缓存到SDCard
                .cacheInMemory(true)//图片下载后是否缓存到内存
                .bitmapConfig(Bitmap.Config.RGB_565)//图片解码类型，推荐此种方式，减少OOM
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .showImageOnFail(R.drawable.pic_default)//图片加载失败后显示的图片
                .showImageOnLoading(R.drawable.pic_default)
                .build();
    }
}
