package cn.ittiger.im.util;

import cn.ittiger.im.R;
import cn.ittiger.im.constant.EmotionType;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情数据帮助类
 *
 * @author: laohu on 2017/2/6
 * @site: http://ittiger.cn
 */
public class EmotionDataHelper {
    /**
     * key -- 表情名称
     * value -- 表情图片resId
     */
    private static ArrayMap<String, Integer> sEmotionClassicMap;
    /**
     * 表情页底部的表情类型Tab数据
     */
    private static List<EmotionType> sEmotionTabList;

    static {
        sEmotionClassicMap = new ArrayMap<>();

        sEmotionTabList = new ArrayList<>();
        sEmotionTabList.add(EmotionType.EMOTION_TYPE_CLASSIC);
        sEmotionTabList.add(EmotionType.EMOTION_TYPE_MORE);

        sEmotionClassicMap.put("[呵呵]", R.drawable.d_hehe);
        sEmotionClassicMap.put("[嘻嘻]", R.drawable.d_xixi);
        sEmotionClassicMap.put("[哈哈]", R.drawable.d_haha);
        sEmotionClassicMap.put("[爱你]", R.drawable.d_aini);
        sEmotionClassicMap.put("[挖鼻屎]", R.drawable.d_wabishi);
        sEmotionClassicMap.put("[吃惊]", R.drawable.d_chijing);
        sEmotionClassicMap.put("[晕]", R.drawable.d_yun);
        sEmotionClassicMap.put("[泪]", R.drawable.d_lei);
        sEmotionClassicMap.put("[馋嘴]", R.drawable.d_chanzui);
        sEmotionClassicMap.put("[抓狂]", R.drawable.d_zhuakuang);
        sEmotionClassicMap.put("[哼]", R.drawable.d_heng);
        sEmotionClassicMap.put("[可爱]", R.drawable.d_keai);
        sEmotionClassicMap.put("[怒]", R.drawable.d_nu);
        sEmotionClassicMap.put("[汗]", R.drawable.d_han);
        sEmotionClassicMap.put("[害羞]", R.drawable.d_haixiu);
        sEmotionClassicMap.put("[睡觉]", R.drawable.d_shuijiao);
        sEmotionClassicMap.put("[钱]", R.drawable.d_qian);
        sEmotionClassicMap.put("[偷笑]", R.drawable.d_touxiao);
        sEmotionClassicMap.put("[笑cry]", R.drawable.d_xiaoku);
        sEmotionClassicMap.put("[doge]", R.drawable.d_doge);
        sEmotionClassicMap.put("[喵喵]", R.drawable.d_miao);
        sEmotionClassicMap.put("[酷]", R.drawable.d_ku);
        sEmotionClassicMap.put("[衰]", R.drawable.d_shuai);
        sEmotionClassicMap.put("[闭嘴]", R.drawable.d_bizui);
        sEmotionClassicMap.put("[鄙视]", R.drawable.d_bishi);
        sEmotionClassicMap.put("[花心]", R.drawable.d_huaxin);
        sEmotionClassicMap.put("[鼓掌]", R.drawable.d_guzhang);
        sEmotionClassicMap.put("[悲伤]", R.drawable.d_beishang);
        sEmotionClassicMap.put("[思考]", R.drawable.d_sikao);
        sEmotionClassicMap.put("[生病]", R.drawable.d_shengbing);
        sEmotionClassicMap.put("[亲亲]", R.drawable.d_qinqin);
        sEmotionClassicMap.put("[怒骂]", R.drawable.d_numa);
        sEmotionClassicMap.put("[太开心]", R.drawable.d_taikaixin);
        sEmotionClassicMap.put("[懒得理你]", R.drawable.d_landelini);
        sEmotionClassicMap.put("[右哼哼]", R.drawable.d_youhengheng);
        sEmotionClassicMap.put("[左哼哼]", R.drawable.d_zuohengheng);
        sEmotionClassicMap.put("[嘘]", R.drawable.d_xu);
        sEmotionClassicMap.put("[委屈]", R.drawable.d_weiqu);
        sEmotionClassicMap.put("[吐]", R.drawable.d_tu);
        sEmotionClassicMap.put("[可怜]", R.drawable.d_kelian);
        sEmotionClassicMap.put("[打哈气]", R.drawable.d_dahaqi);
        sEmotionClassicMap.put("[挤眼]", R.drawable.d_jiyan);
        sEmotionClassicMap.put("[失望]", R.drawable.d_shiwang);
        sEmotionClassicMap.put("[顶]", R.drawable.d_ding);
        sEmotionClassicMap.put("[疑问]", R.drawable.d_yiwen);
        sEmotionClassicMap.put("[困]", R.drawable.d_kun);
        sEmotionClassicMap.put("[感冒]", R.drawable.d_ganmao);
        sEmotionClassicMap.put("[拜拜]", R.drawable.d_baibai);
        sEmotionClassicMap.put("[黑线]", R.drawable.d_heixian);
        sEmotionClassicMap.put("[阴险]", R.drawable.d_yinxian);
        sEmotionClassicMap.put("[打脸]", R.drawable.d_dalian);
        sEmotionClassicMap.put("[傻眼]", R.drawable.d_shayan);
        sEmotionClassicMap.put("[猪头]", R.drawable.d_zhutou);
        sEmotionClassicMap.put("[熊猫]", R.drawable.d_xiongmao);
        sEmotionClassicMap.put("[兔子]", R.drawable.d_tuzi);
    }

    /**
     * 根据表情名称获取当前表情图标R值
     *
     * @param emotionName 表情名称
     * @return
     */
    public static int getEmotionForName(EmotionType emotionType, String emotionName) {

        ArrayMap<String, Integer> emotionMap = getEmotionsForType(emotionType);
        Integer emotionId = emotionMap.get(emotionName);
        return emotionId == null ? R.drawable.vector_default_image : emotionId.intValue();
    }

    public static List<EmotionType> getEmotionTabList() {

        return sEmotionTabList;
    }

    /**
     * 根据表情类型获取对应的表情列表
     *
     * @param emotionType
     * @return
     */
    public static ArrayMap<String, Integer> getEmotionsForType(EmotionType emotionType) {

        switch (emotionType) {
            case EMOTION_TYPE_CLASSIC:
                return sEmotionClassicMap;
            case EMOTION_TYPE_MORE:
                return new ArrayMap<>(0);
            default:
                return new ArrayMap<>(0);
        }
    }
}
