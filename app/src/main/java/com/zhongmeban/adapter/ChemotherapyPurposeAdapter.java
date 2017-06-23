package com.zhongmeban.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.SmoothCheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * 化疗目的Adapter
 * Created by Chengbin He on 2016/7/7.
 */
public class ChemotherapyPurposeAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;
    private int count;//选择类型

    public ChemotherapyPurposeAdapter(Context mContext,int count){
        this.mContext = mContext;
        this.count = count;

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_chemotherapy_purpose,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyHolder myHolder = (MyHolder) holder;

        if (isChoose(count,changeType(position))){//判断类型
            myHolder.checkbox.setChecked(true,false);
            myHolder.rlBackGround.setBackgroundColor(mContext.getResources().getColor(R.color.app_green));
            myHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            myHolder.checkbox.setChecked(false,false);
            myHolder.rlBackGround.setBackgroundResource(R.drawable.bg_box);
            myHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.content_two));
        }

        String name = "";
        switch (position){
            case 0:
                name = "根治性化疗";
                break;
            case 1:
                name = "姑息性化疗";
                break;
            case 2:
                name = "辅助化疗";
                break;
            case 3:
                name = "新辅助化疗";
                break;
            case 4:
                name = "局部化疗";
                break;
        }
        myHolder.tvName.setText(name);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHolder.checkbox.isChecked()){
                    count-=changeType(position);
                    myHolder.checkbox.setChecked(false,false);
                    myHolder.rlBackGround.setBackgroundResource(R.drawable.bg_box);
                    myHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.content_two));
                }else {
                    count = 0;
                    count+=changeType(position);
                    myHolder.checkbox.setChecked(true,false);
                    myHolder.rlBackGround.setBackgroundColor(mContext.getResources().getColor(R.color.app_green));
                    myHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.white));
                }
                notifyDataSetChanged();
                mClick.onItemClick(v,position);
            }
        });
    }
    private int changeType(int position){
        int i = 0;
        switch (position){
            case 0:
                i = 1;
                break;
            case 1:
                i = 2;
                break;
            case 2:
                i = 4;
                break;
            case 3:
                i = 8;
                break;
            case 4:
                i = 16;
                break;
        }

        return i;
    }

    private boolean isChoose(int count, int index){
        return (count&index)!=0;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private RelativeLayout rlBackGround;
        private SmoothCheckBox checkbox;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            rlBackGround = (RelativeLayout) itemView.findViewById(R.id.rl_background);
            checkbox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
            checkbox.setClickable(false);
        }
    }
}
