package com.zhongmeban.mymodle.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.MyArticleAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * @Description: 我的文章
 * Created by Chengbin He on 2016/3/22.
 */
public class ActivityMyArticle extends BaseActivity {

    private LayoutActivityTitle title;
    private RecyclerView recyclerView;
    private MyArticleAdapter mAdapter;
    private Context mContext = ActivityMyArticle.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_article);

        initTitle();
        initView();

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityMyArticle.this));
        mAdapter = new MyArticleAdapter(mContext);
        mAdapter.setItemClickListener(adapterClick);
        recyclerView.setAdapter(mAdapter);
    }

    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("我的文章");
        title.slideRighttext("编辑", onClickListener);
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };

    /**
     * RecyclerView 被点击监听
     */
    AdapterClickInterface adapterClick = new AdapterClickInterface() {


        @Override
        public void onItemClick(View v, int position) {

        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };
}
