package cn.ittiger.im.util;

import cn.ittiger.im.R;
import cn.ittiger.im.constant.EmotionType;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ylhu on 17-2-6.
 */
public class EmotionUtil {

    /**
     * 处理输入框中的表情，将表情大小进行压缩
     *
     * @param context
     * @param emotionType
     * @param tv
     * @param source
     * @return
     */
    public static SpannableString getInputEmotionContent(Context context, EmotionType emotionType, final TextView tv, String source) {

        // 表情图片
        int size = (int) tv.getTextSize() * 13 / 10;
        return getEmotionContent(context, emotionType, size, source);
    }

    /**
     * 处理聊天消息中的表情
     *
     * @param context
     * @param emotionType
     * @param source
     * @return
     */
    public static SpannableString getEmotionContent(Context context, EmotionType emotionType, String source) {

        // 表情图片
        int size = context.getResources().getDimensionPixelSize(R.dimen.dimen_30);
        return getEmotionContent(context, emotionType, size, source);
    }

    private static SpannableString getEmotionContent(Context context, EmotionType emotionType, int emotionSize, String source) {

        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();

        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer emotionRes = EmotionDataHelper.getEmotionForName(emotionType, key);
            // 表情图片
            Bitmap bitmap = BitmapFactory.decodeResource(res, emotionRes);
            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, emotionSize, emotionSize, true);

            ImageSpan span = new ImageSpan(context, scaleBitmap);
            spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
