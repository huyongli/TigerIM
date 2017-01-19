package cn.ittiger.im.constant;

/**
 * 消息接收过程中文件的加载状态
 * @author: laohu on 2017/1/17
 * @site: http://ittiger.cn
 */
public enum FileLoadState {
    STATE_LOAD_START(0),//加载开始
    STATE_LOAD_SUCCESS(1),//加载成功
    STATE_LOAD_ERROR(2);//加载失败

    int value;
    FileLoadState(int value) {

        this.value = value;
    }

    public int value() {

        return value;
    }
}
