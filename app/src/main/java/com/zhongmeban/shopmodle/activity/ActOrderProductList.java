package com.zhongmeban.shopmodle.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * 提交订单页面显示的多个商品，点击之后进入本页面---》商品列表
 * Created by User on 2016/10/9.
 */

public class ActOrderProductList extends BaseActivityRefresh implements View.OnClickListener {

    private RecyclerView mRvList;
    private CommonAdapter mAdapter;

    private List<String> mDatas = new ArrayList<>();


    @Override protected int getLayoutIdRefresh() {
        for (int i = 0; i < 20; i++) {
            mDatas.add("" + i);
        }
        return R.layout.activity_act_order_product_list;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("商品清单", "共20件");
    }


    @Override protected void initViewRefresh() {

        mRvList = (RecyclerView) findViewById(R.id.id_act_order_product_list_recycler);
        mRvList.setLayoutManager(new LinearLayoutManager(this));

        mRvList.setAdapter(
            mAdapter = new CommonAdapter<String>(this, R.layout.item_order_product_info, mDatas) {

                @Override protected void convert(ViewHolder holder, String s, final int position) {
                    ImageView ivProduct = holder.getView(
                        R.id.id_item_order_product_img);
                    TextView tvNumber = holder.getView(R.id.id_item_order_product_number);
                    TextView tvPrice = holder.getView(R.id.id_item_order_product_price);
                    TextView tvName = holder.getView(R.id.id_item_order_product_name);

                }
            });

        mRefreshLayout.postDelayed(new Runnable() {
            @Override public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_act_address_list_add:

                break;
        }
    }


    @Override public void onRefresh() {
        super.onRefresh();

        mRefreshLayout.postDelayed(new Runnable() {
            @Override public void run() {
                mRefreshLayout.setRefreshing(false);
                Logger.d("停止刷新");
            }
        }, 1000);
    }


    @Override public void clickLeft() {
        onBackPressed();
    }


    @Override public void clickRight() {

    }
}