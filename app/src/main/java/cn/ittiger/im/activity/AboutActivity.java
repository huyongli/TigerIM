package cn.ittiger.im.activity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ittiger.im.R;
import cn.ittiger.util.ActivityUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * 关于
 * @author: laohu on 2016/12/27
 * @site: http://ittiger.cn
 */
public class AboutActivity extends IMBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示ToolBar的标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbarLayout.setTitle(getString(R.string.nav_about));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
    }

    @OnClick({R.id.tv_about_blog, R.id.tv_about_github})
    public void onClick(View view) {

        String url;
        if(view.getId() == R.id.tv_about_blog) {
            url = "http://ittiger.cn";
        } else {
            url = "https://github.com/huyongli/TigerIM";
        }
        Intent intent = new Intent(mActivity, WebPageActivity.class);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        ActivityUtil.startActivity(mActivity, intent);
    }
}
