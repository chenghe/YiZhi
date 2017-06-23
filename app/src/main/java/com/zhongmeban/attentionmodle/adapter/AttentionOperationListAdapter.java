package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

import de.greenrobot.dao.attention.SurgerySource;

/**
 * 关注手术首页列表Adapter
 * Created by Chengbin He on 2016/8/9.
 */
public class AttentionOperationListAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;
    private List<SurgerySource> list;

    public AttentionOperationListAdapter(Context mContext,List<SurgerySource> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<SurgerySource> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attention_common,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.ll3.setVisibility(View.GONE);
        myHolder.tvSurgeyName.setText(list.get(position).getSurgeryRecord().getTherapeuticName());
        myHolder.tvHospTitle.setText("治疗医院：");
        myHolder.tvHospName.setText(list.get(position).getSurgeryRecord().getHospitalName());
        myHolder.tvDoctTitle.setText("主刀医生：");
        String name = "无";
        if(!TextUtils.isEmpty(list.get(position).getSurgeryRecord().getDoctorName())){
            name = list.get(position).getSurgeryRecord().getDoctorName();
        }
        myHolder.tvDoctName.setText(name);
        myHolder.tvTime.setText(TimeUtils.formatTime(list.get(position).getSurgeryRecord().getTime()));
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list ==null ? 0 : list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public TextView tvSurgeyName;
        public TextView tvHospTitle;
        public TextView tvHospName;
        public TextView tvDoctTitle;
        public TextView tvDoctName;
        public TextView tvTime;
        public LinearLayout ll3;
        public LinearLayout ll2;
        public MyHolder(View itemView) {
            super(itemView);
            tvSurgeyName = (TextView) itemView.findViewById(R.id.tv_name);
            tvHospTitle = (TextView) itemView.findViewById(R.id.tv_title1);
            tvHospName = (TextView) itemView.findViewById(R.id.tv_content1);
            tvDoctTitle = (TextView) itemView.findViewById(R.id.tv_title2);
            tvDoctName = (TextView) itemView.findViewById(R.id.tv_content2);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ll3 = (LinearLayout) itemView.findViewById(R.id.ll3);
            ll2 = (LinearLayout) itemView.findViewById(R.id.ll2);
        }
    }
}
