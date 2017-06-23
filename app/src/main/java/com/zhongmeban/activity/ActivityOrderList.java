package com.zhongmeban.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.zhongmeban.R;
import com.zhongmeban.adapter.ShopOrderListAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.BaseActivityToolBar;

/**
 * 订单列表List
 * Created by Chengbin He on 2016/10/21.
 */

public class ActivityOrderList extends BaseActivityRefresh{

    private RecyclerView recyclerView;
    private Context mContext = ActivityOrderList.this;

    @Override
    protected int getLayoutIdRefresh() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void initToolbarRefresh() {
        initToolbarCustom("我的订单","");
    }

    @Override
    protected void initViewRefresh() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRefreshLayout.post(new Runnable() {
            @Override public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(new ShopOrderListAdapter(mContext));
    }
}
