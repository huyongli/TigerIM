package cn.ittiger.im.adapter;

import cn.ittiger.im.bean.ContactEntity;
import cn.ittiger.indexlist.adapter.IndexStickyViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ylhu on 16-12-26.
 */
public class ContactAdapter extends IndexStickyViewAdapter<ContactEntity> {

    public ContactAdapter(List<ContactEntity> originalList) {

        super(originalList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {

        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

        return null;
    }

    @Override
    public void onBindIndexViewHolder(RecyclerView.ViewHolder holder, int position, String indexName) {

    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position, ContactEntity itemData) {

    }
}
