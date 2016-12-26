package cn.ittiger.im.bean;

import cn.ittiger.indexlist.entity.BaseEntity;

import org.jivesoftware.smack.roster.RosterEntry;

/**
 * Created by ylhu on 16-12-26.
 */
public class ContactEntity implements BaseEntity {
    private RosterEntry mRosterEntry;

    public ContactEntity(RosterEntry rosterEntry) {

        mRosterEntry = rosterEntry;
    }

    @Override
    public String getIndexField() {

        return mRosterEntry.getName();
    }

    public RosterEntry getRosterEntry() {

        return mRosterEntry;
    }
}
