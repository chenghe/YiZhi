package com.zhongmeban.shopmodle.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
 * Created by User on 2016/10/9.
 */

public class ActAddressSelect extends BaseActivityRefresh implements View.OnClickListener{

    private RecyclerView mRvList;
    private Button mBtnAdd;
    private CommonAdapter mAdapter;

    private int defaultPostion;
    private List<String> mDatas = new ArrayList<>();


    @Override protected int getLayoutIdRefresh() {
        for (int i = 0; i < 20; i++) {
            mDatas.add("" + i);
        }
        return R.layout.activity_act_address_select;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("选择收货地址", "");
    }


    @Override protected void initViewRefresh() {

        mToolbar.setToolBarListener(this);
        mBtnAdd = (Button) findViewById(R.id.id_act_address_list_add);
        mBtnAdd.setOnClickListener(this);
        mRvList = (RecyclerView) findViewById(R.id.id_act_address_list_recycler);
        mRvList.setLayoutManager(new LinearLayoutManager(this));

        mRvList.setAdapter(
            mAdapter = new CommonAdapter<String>(this, R.layout.item_address_select, mDatas) {

                @Override protected void convert(ViewHolder holder, String s, final int position) {
                    ImageView ivIndicator = holder.getView(
                        R.id.id_item_address_select_iv_indicator);
                    TextView tvDefault = holder.getView(R.id.id_item_address_select_default_tv);
                    if (position == 0) {
                        ivIndicator.setVisibility(View.VISIBLE);
                        tvDefault.setVisibility(View.VISIBLE);
                    } else {
                        ivIndicator.setVisibility(View.GONE);
                        tvDefault.setVisibility(View.GONE);
                    }
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
                startActivity(new Intent(ActAddressSelect.this,ActAddAddress.class));
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