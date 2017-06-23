package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.SimpleHospitalPageSourceItems;
import com.zhongmeban.bean.postbody.SimpleDoctorPageSourceItems;

import java.util.List;

/**
 * 关注部分医院接口
 * Created by Chengbin He on 2016/8/10.
 */
public class AttentionSimpleDoctorAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<SimpleDoctorPageSourceItems> list;

    public AttentionSimpleDoctorAdapter(Context mContext,List<SimpleDoctorPageSourceItems> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<SimpleDoctorPageSourceItems> list){
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
            myHolder.tvHospital.setText(list.get(position).getHospitalName());
            myHolder.tvName.setText(list.get(position).getDoctorName());
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
        public TextView tvName;
        public TextView tvHospital;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_doc_name);
            tvHospital = (TextView) itemView.findViewById(R.id.tv_hosp_name);
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder{

        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

}

