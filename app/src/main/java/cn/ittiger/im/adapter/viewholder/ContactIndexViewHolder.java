package cn.ittiger.im.adapter.viewholder;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ittiger.im.R;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ylhu on 16-12-27.
 */
public class ContactIndexViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.contact_index_name)
    TextView mTextView;

    public ContactIndexViewHolder(View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getTextView() {

        return mTextView;
    }
}
