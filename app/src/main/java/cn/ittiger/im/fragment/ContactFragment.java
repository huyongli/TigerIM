package cn.ittiger.im.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.base.BaseFragment;
import cn.ittiger.im.R;
import cn.ittiger.im.adapter.ContactAdapter;
import cn.ittiger.im.adapter.ContactItemDecoration;
import cn.ittiger.im.adapter.ContactViewHolder;
import cn.ittiger.im.bean.ContactEntity;
import cn.ittiger.im.bean.ContactMenuEntity;
import cn.ittiger.im.smack.SmackManager;
import cn.ittiger.indexlist.IndexStickyView;
import cn.ittiger.indexlist.adapter.IndexHeaderFooterAdapter;
import cn.ittiger.indexlist.listener.OnItemClickListener;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import org.jivesoftware.smack.roster.RosterEntry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 联系人列表
 * @author: laohu on 2016/12/24
 * @site: http://ittiger.cn
 */
public class ContactFragment extends BaseFragment {
    @BindView(R.id.indexStickyView)
    IndexStickyView mIndexStickyView;
    ContactAdapter mAdapter;

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, null);
        ButterKnife.bind(this, view);
        mIndexStickyView.addItemDecoration(new ContactItemDecoration());
        return view;
    }

    @Override
    public void refreshData() {

        Observable.create(new Observable.OnSubscribe<List<ContactEntity>>() {
            @Override
            public void call(Subscriber<? super List<ContactEntity>> subscriber) {

                Set<RosterEntry> friends = SmackManager.getInstance().getAllFriends();
                List<ContactEntity> list = new ArrayList<>();
                for (RosterEntry friend : friends) {
                    list.add(new ContactEntity(friend));
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())//指定上面的Subscriber线程
        .observeOn(AndroidSchedulers.mainThread())//指定下面的回调线程
        .subscribe(new Observer<List<ContactEntity>>() {
            @Override
            public void onCompleted() {

                refreshSuccess();
            }

            @Override
            public void onError(Throwable e) {

                refreshFailed();
            }

            @Override
            public void onNext(List<ContactEntity> contacts) {

                mAdapter = new ContactAdapter(mContext, contacts);
                mAdapter.setOnItemClickListener(mContactItemClickListener);
                mIndexStickyView.setAdapter(mAdapter);
                mIndexStickyView.addIndexHeaderAdapter(getHeaderMenuAdapter());
            }
        });
    }

    private IndexHeaderFooterAdapter<ContactMenuEntity> getHeaderMenuAdapter() {

        String indexValue = getString(R.string.contact_index_menu);
        List<ContactMenuEntity> list = new ArrayList<>();
        list.add(new ContactMenuEntity(getString(R.string.contact_multi_chat), ContactMenuEntity.MenuType.MULTI_CHAT));
        list.add(new ContactMenuEntity(getString(R.string.contact_grouping), ContactMenuEntity.MenuType.GROUP));

        IndexHeaderFooterAdapter<ContactMenuEntity> adapter = new IndexHeaderFooterAdapter<ContactMenuEntity>(indexValue, null, list) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.contact_item_view, parent, false);
                return new ContactViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, ContactMenuEntity itemData) {

                ContactViewHolder viewHolder = (ContactViewHolder) holder;
                viewHolder.getImageView().setImageResource(itemData.getAvatar());
                viewHolder.getTextView().setText(itemData.getMenuName());
            }
        };
        adapter.setOnItemClickListener(mContactMenuItemClickListener);
        return adapter;
    }

    OnItemClickListener<ContactEntity> mContactItemClickListener = new OnItemClickListener<ContactEntity>() {
        @Override
        public void onItemClick(View childView, int position, ContactEntity item) {

        }
    };

    OnItemClickListener<ContactMenuEntity> mContactMenuItemClickListener = new OnItemClickListener<ContactMenuEntity>() {
        @Override
        public void onItemClick(View childView, int position, ContactMenuEntity item) {

        }
    };

    @Override
    public String getTitle() {

        return getString(R.string.text_contact);
    }
}
