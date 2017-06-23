package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;

/**
 *医院列表RecyclerView Adapter
 * Created by Chengbin He on 2016/8/16.
 */
public class AttentionHospListAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;
    private List<Hospitalized> list;

    public AttentionHospListAdapter(Context mContext,List<Hospitalized> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<Hospitalized> list){
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
        MyHolder myHolder  = (MyHolder) holder;
        myHolder.ll2.setVisibility(View.GONE);
        myHolder.ll3.setVisibility(View.GONE);
        myHolder.tvHospName.setText(list.get(position).getHospitalName());
        String type = "";
        switch (list.get(position).getType()){
            case 1:
                type = "观察";
                break;
            case 2:
                type = "治疗";
                break;
            case 3:
                type = "手术";
                break;

        }
        myHolder.tvType.setText(type);
        myHolder.tvTypeTitle.setText("入院目的：");
        String inTime = TimeUtils.formatTime(list.get(position).getInTime());
        myHolder.tvTime.setText(inTime);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tvHospName;
        public TextView tvTypeTitle;
        public TextView tvType;
        public TextView tvTime;
        public LinearLayout ll3;
        public LinearLayout ll2;
        public MyHolder(View itemView) {
            super(itemView);
            tvHospName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTypeTitle = (TextView) itemView.findViewById(R.id.tv_title1);
            tvType = (TextView) itemView.findViewById(R.id.tv_content1);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ll3 = (LinearLayout) itemView.findViewById(R.id.ll3);
            ll2 = (LinearLayout) itemView.findViewById(R.id.ll2);
        }
    }
}
