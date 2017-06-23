package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.SpannableStringUtils;
import com.zhongmeban.utils.TimeUtils;
import java.util.List;

/**
 * 化疗记录 列表 Adapter
 * Created by Chengbin He on 2016/8/23.
 */
public class ChemotherapyListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<ChemotherapyRecordBean.Record> list;


    public ChemotherapyListAdapter(Context mContext, List<ChemotherapyRecordBean.Record> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void updateData(List<ChemotherapyRecordBean.Record> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_attention_chemotherapy_record, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        ChemotherapyRecordBean.Record itemBean = list.get(position);

        String startTime = TimeUtils.formatTime(itemBean.getStartTime());
        myHolder.tvTime.setText(startTime);
        myHolder.tvTargetName.setText(
            AttentionUtils.getChemotherapyPurposeName(itemBean.getChemotherapyAim()));
        setTvInfo(myHolder.tvRecordInfo, itemBean);
        myHolder.tvSMedicine.setText(itemBean.getMedicinesName());
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v, position);
            }
        });
    }


    // 设置标记物各种信息
    private void setTvInfo(TextView tv, ChemotherapyRecordBean.Record itemBean) {
        int content = ContextCompat.getColor(mContext, R.color.content_two);
        int textOne = ContextCompat.getColor(mContext, R.color.text_one);
        SpannableStringBuilder builder = SpannableStringUtils.getBuilder(
            "化疗周期：" + itemBean.getWeeksCount() + "个周期\n")
            .setForegroundColor(content)
            .append("主治医师：" + (TextUtils.isEmpty(itemBean.getDoctorName())
                               ? "无"
                               : itemBean.getDoctorName()))
            .setForegroundColor(content)
            .create();
        tv.setText(builder);
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tvTargetName;
        public TextView tvRecordInfo;
        public TextView tvTime;
        public TextView tvSMedicine;


        public MyHolder(View itemView) {
            super(itemView);
            tvTargetName = (TextView) itemView.findViewById(R.id.tv_chemotherapy_record_target);
            tvRecordInfo = (TextView) itemView.findViewById(R.id.tv_chemotherapy_record_info);
            tvSMedicine = (TextView) itemView.findViewById(R.id.tv_chemotherapy_record_medicine);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
