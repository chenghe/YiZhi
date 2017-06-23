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
import com.zhongmeban.adapter.DoctLevelSelectAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * 医生等级筛选Activity
 * Created by Chengbin He on 2016/5/24.
 */
public class ActivityDoctLevelSelect extends BaseActivity {
    private RecyclerView recyclerView;
    private DoctLevelSelectAdapter mAdapter;
    private Activity mContext = ActivityDoctLevelSelect.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_level);

        initTitle();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new DoctLevelSelectAdapter(mContext);
        mAdapter.setItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择医生级别");
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
            String doctLevel = "";
            String doctLevelName = "";
            switch (position){
                case 0:
                    doctLevel = "0";
                    doctLevelName = "无等级";
                    break;
                case 1:
                    doctLevel = "1";
                    doctLevelName = "主任医师";
                    break;
                case 2:
                    doctLevel = "2";
                    doctLevelName = "副主任医师";
                    break;
                case 3:
                    doctLevel = "3";
                    doctLevelName = "主治医师";
                    break;
                case 4:
                    doctLevel = "4";
                    doctLevelName = "副主治医师";
                    break;
                case 5:
                    doctLevel = "5";
                    doctLevelName = "住院医师";
                    break;
                case 6:
                    doctLevel = "6";
                    doctLevelName = "其他";
                    break;
                default:
                    doctLevel = "6";
                    doctLevelName = "其他";
                    break;
            }
            Intent intent = new Intent();
            intent.putExtra("doctLevel",doctLevel);
            intent.putExtra("doctLevelName",doctLevelName);
            mContext.setResult(400,intent);//400 为医生等级
            mContext.finish();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };
}
