package cn.ittiger.im.fragment;

import cn.ittiger.base.BaseFragment;
import cn.ittiger.im.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by laohu on 16-12-14.
 */
public class MessageFragment extends BaseFragment {

    @Override
    public void refreshData() {

        refreshFailed();
    }

    @Override
    public View getContentView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, null);
        return view;
    }

    @Override
    public String getTitle() {

        return getString(R.string.text_message);
    }
}
