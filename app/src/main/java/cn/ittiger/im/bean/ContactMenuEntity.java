package cn.ittiger.im.bean;

import cn.ittiger.im.R;
import cn.ittiger.indexlist.entity.BaseEntity;

/**
 * 联系人列表头部的菜单项
 * @author: laohu on 2016/12/26
 * @site: http://ittiger.cn
 */
public class ContactMenuEntity implements BaseEntity {
    private String mMenuName;
    private int mAvatar;
    private MenuType mMenuType;

    public ContactMenuEntity(String menuName, MenuType menuType) {

        mMenuName = menuName;
        mMenuType = menuType;
        mAvatar = menuType.getAvatar();
    }

    @Override
    public String getIndexField() {

        return mMenuName;
    }

    public String getMenuName() {

        return mMenuName;
    }

    public MenuType getMenuType() {

        return mMenuType;
    }

    public int getAvatar() {

        return mAvatar;
    }

    public enum MenuType {
        GROUP(R.drawable.vector_contact_group),          //分组
        MULTI_CHAT(R.drawable.vector_multi_chat);        //群聊

        private int mAvatar;

        MenuType(int avatar) {

            mAvatar = avatar;
        }

        public int getAvatar() {

            return mAvatar;
        }
    }
}
