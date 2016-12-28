package cn.ittiger.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtil {
	/**
	 * 获取缩放图
	 * @param file
	 * @param width
	 * @return
	 */
	public static Bitmap createBitmapWithFile(String filePath, int width) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = options.outWidth / width;
		if(options.outWidth == 0) {
			//避免图片还没有保存成功
			options.outWidth = width;			
			options.outHeight = width * 4 / 3;
		}else {
			options.outHeight = options.outHeight * width / options.outWidth;
			options.outWidth = width;
		}
		options.inDither = false;						//图片不抖动
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}
	
	/***
	 * 通过bitmap获取图片
	 * @param filePath
	 * @param bitmap
	 */
	public static void createPictureWithBitmap(String filePath, Bitmap bitmap) {
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
		}
	}
	
	/**
	 * 通过bitmap获取图片
	 * @param filePath
	 * @param bitmap
	 * @param percent  获取图片质量
	 */
	public static void createPictureWithBitmap(String filePath, Bitmap bitmap, int percent) {
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, percent, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
		}
	}
	
}
