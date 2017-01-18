package cn.ittiger.im.event;

import cn.ittiger.im.bean.ContactEntity;

/**
 * 联系人事件
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class ContactEvent {
    private ContactEntity mContactEntity;

    public ContactEvent(ContactEntity contactEntity) {

        mContactEntity = contactEntity;
    }

    public ContactEntity getContactEntity() {

        return mContactEntity;
    }
}
