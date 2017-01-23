package cn.ittiger.im.adapter;

import cn.ittiger.im.R;
import cn.ittiger.im.adapter.viewholder.ContactIndexViewHolder;
import cn.ittiger.im.adapter.viewholder.ContactViewHolder;
import cn.ittiger.im.bean.ContactEntity;
import cn.ittiger.indexlist.adapter.IndexStickyViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 联系人列表数据适配器
 * @author: laohu on 2016/12/27
 * @site: http://ittiger.cn
 */
public class ContactAdapter extends IndexStickyViewAdapter<ContactEntity> {
    private Context mContext;
    public ContactAdapter(Context context, List<ContactEntity> originalList) {

        super(originalList);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item_index_view, parent, false);
        return new ContactIndexViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item_view, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindIndexViewHolder(RecyclerView.ViewHolder holder, int position, String indexName) {

        ContactIndexViewHolder viewHolder = (ContactIndexViewHolder) holder;
        viewHolder.getTextView().setText(indexName);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position, ContactEntity itemData) {

        ContactViewHolder viewHolder = (ContactViewHolder) holder;
        viewHolder.getImageView().setImageResource(R.drawable.vector_contact_focus);
        viewHolder.getTextView().setText(itemData.getRosterEntry().getName());
    }
}
