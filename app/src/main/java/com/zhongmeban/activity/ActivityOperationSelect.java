package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.OperationSelectAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * 是否可手术筛选Activity
 * Created by Chengbin He on 2016/5/24.
 */
public class ActivityOperationSelect extends BaseActivity{
    private RecyclerView recyclerView;
    private OperationSelectAdapter mAdapter;
    private Activity mContext = ActivityOperationSelect.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_level);

        initTitle();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new OperationSelectAdapter(mContext);
        mAdapter.setItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择是否可手术");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    AdapterClickInterface itemClickListener = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            String operationLevel = "";
            String operationLevelName = "";
            switch (position){
                case 0:
                    operationLevelName = "全部";
                    operationLevel = "0";
                    break;
                case 1:
                    operationLevelName = "可手术";
                    operationLevel = "1";
                    break;
                case 2:
                    operationLevelName = "不可手术";
                    operationLevel = "2";
                    break;
                default:
                    operationLevelName = "其他";
                    operationLevel = "0";
                    break;
            }
            Intent intent = new Intent();
            intent.putExtra("operationLevel",operationLevel);
            intent.putExtra("operationLevelName",operationLevelName);
            mContext.setResult(500,intent);//500 是否可手术
            mContext.finish();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };
}
