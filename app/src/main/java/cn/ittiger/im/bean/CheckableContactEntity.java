package cn.ittiger.im.bean;

import cn.ittiger.indexlist.entity.BaseEntity;

import org.jivesoftware.smack.roster.RosterEntry;

/**
 * 创建群聊时可选联系人实体
 * @author: laohu on 2017/1/24
 * @site: http://ittiger.cn
 */
public class CheckableContactEntity implements BaseEntity {
    private RosterEntry mRosterEntry;
    private boolean mChecked = false;

    public CheckableContactEntity(RosterEntry rosterEntry) {

        mRosterEntry = rosterEntry;
    }

    @Override
    public String getIndexField() {

        return mRosterEntry.getName();
    }

    public RosterEntry getRosterEntry() {

        return mRosterEntry;
    }

    public boolean isChecked() {

        return mChecked;
    }

    public void setChecked(boolean checked) {

        mChecked = checked;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }
        if(obj instanceof CheckableContactEntity) {
            return mRosterEntry.getUser().equals(((CheckableContactEntity) obj).getRosterEntry().getUser()) &&
            mRosterEntry.getName().equals(((CheckableContactEntity) obj).getRosterEntry().getName());
        }

        return false;
    }
}
