package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.HospitalScource;
import com.zhongmeban.bean.SimpleHospitalPageSourceItems;

import java.util.List;

/**
 * 关注部分医院筛选Adapter
 * Created by Chengbin He on 2016/7/27.
 */
public class AttentionSimpleHospitalAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;
    private List<HospitalScource> list;

    public AttentionSimpleHospitalAdapter(Context mContext,List<HospitalScource> list){
            this.mContext = mContext;
            this.list = list;
    }

    public void notifyData(List<HospitalScource> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        switch (viewType){
            case 0:
                //上拉刷新
                v = LayoutInflater.from(mContext).inflate(R.layout.load_more_progress, parent, false);
                holder = new LoadHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_attention_doctor_selector, parent, false);
                holder = new MyHolder(v);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
           MyHolder myHolder = (MyHolder) holder;
            myHolder.line.setVisibility(View.VISIBLE);
            myHolder.tv.setText(list.get(position).getHospitalName());
            myHolder.subTv.setText(list.get(position).getAddress());
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(v,position);
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
    }

    @Override
    public int getItemCount() {
        return  list == null ? 0 : list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public TextView tv;
        public TextView subTv;
        public View line;
        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_doc_name);
            subTv = (TextView) itemView.findViewById(R.id.tv_hosp_name);
            line = itemView.findViewById(R.id.line);
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder{

        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

}
