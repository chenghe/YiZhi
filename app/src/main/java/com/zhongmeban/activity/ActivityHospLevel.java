package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.HospLevelAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * 医院等级筛选
 * Created by Chengbin He on 2016/5/23.
 */
public class ActivityHospLevel extends BaseActivity{

    public static final int ThreeOne = 39;//三级甲等
    public static final int ThreeTwo = 38;//三级乙等
    public static final int Three = 30;//三级
    public static final int TwoOne = 29;//二级甲等
    public static final int TwoTwo = 28;//二级乙等
    public static final int Two = 20;//二级
    public static final int OneOne = 19;//一级甲等
    public static final int One = 10;//一级


    private RecyclerView recyclerView;
    private HospLevelAdapter mAdapter;
    private Activity mContext = ActivityHospLevel.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_level);

        initTitle();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new HospLevelAdapter(mContext);
        mAdapter.setItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择医院等级");
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
            String level = "";
            String levelName = "";
            switch (position){
                case 0:
                    level = ThreeOne+"";
                    levelName = "三级甲等";
                    break;
                case 1:
                    level = ThreeTwo + "";
                    levelName = "三级乙等";
                    break;
                case 2:
                    level = Three+"";
                    levelName = "三级";
                    break;
                case 3:
                    level = TwoOne + "";
                    levelName = "二级甲等";
                    break;
                case 4:
                    level = TwoTwo + "";
                    levelName = "二级乙等";
                    break;
                case 5:
                    level = Two + "";
                    levelName = "二级";
                    break;
                case 6:
                    level = OneOne +"";
                    levelName = "一级甲等";
                    break;
                case 7:
                    level = One + "";
                    levelName = "一级";
                    break;
                default:
                    level = "0";
                    levelName = "其他";
                    break;
            }
            Intent intent = new Intent();
            intent.putExtra("resultLevel",level);
            intent.putExtra("resultLevelName",levelName);
            mContext.setResult(200,intent);//200 为医院等级
            mContext.finish();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };
}
