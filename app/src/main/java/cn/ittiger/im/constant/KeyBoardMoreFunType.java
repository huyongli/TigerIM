package cn.ittiger.im.constant;

/**
 * KeyBoard更多功能选项类型
 * @author: laohu on 2017/2/4
 * @site: http://ittiger.cn
 */
public enum KeyBoardMoreFunType {
    NONE(-1),
    /**
     * 选择图片
     */
    FUN_TYPE_IMAGE(0),
    /**
     * 拍照
     */
    FUN_TYPE_TAKE_PHOTO(1);

    int value;

    KeyBoardMoreFunType(int value) {

        this.value = value;
    }

    public int value() {

        return value;
    }

    public static KeyBoardMoreFunType getFunType(int value) {

        if (value == FUN_TYPE_IMAGE.value()) {
            return FUN_TYPE_IMAGE;
        } else if(value == FUN_TYPE_TAKE_PHOTO.value()) {
            return FUN_TYPE_TAKE_PHOTO;
        } else {
            return NONE;
        }
    }
}
