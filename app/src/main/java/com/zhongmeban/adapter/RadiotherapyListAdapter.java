package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

import de.greenrobot.dao.attention.Radiotherapy;

import static com.bugtags.library.obfuscated.f.m;
import static java.lang.Boolean.TRUE;

/**
 * 放疗列表RecycleView Adapter
 * Created by Chengbin He on 2016/8/19.
 */
public class RadiotherapyListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<Radiotherapy> list;

    public RadiotherapyListAdapter(Context mContext,List<Radiotherapy> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<Radiotherapy> list){
        this.list.clear();
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
        myHolder.tvDose.setText("剂量："+list.get(position).getPredictDosage()+"Gy");
        myHolder.tvTimesCountTitle.setText("次数：");
        myHolder.tvTimesCount.setText(list.get(position).getCurrentCount()+"次");
        myHolder.tvWeeksCountTitle.setText("时长：");
        myHolder.tvWeeksCount.setText(list.get(position).getWeeksCount()+"周");
        myHolder.tvTime.setText(TimeUtils.formatTime(list.get(position).getStartTime()));
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
        public TextView tvDose;
        public TextView tvTimesCountTitle;
        public TextView tvTimesCount;
        public TextView tvWeeksCountTitle;
        public TextView tvWeeksCount;
        public TextView tvTime;
        public LinearLayout ll3;
        public LinearLayout ll2;
        public MyHolder(View itemView) {
            super(itemView);
            tvDose = (TextView) itemView.findViewById(R.id.tv_name);
            tvTimesCountTitle = (TextView) itemView.findViewById(R.id.tv_title1);
            tvTimesCount = (TextView) itemView.findViewById(R.id.tv_content1);
            tvWeeksCountTitle = (TextView) itemView.findViewById(R.id.tv_title2);
            tvWeeksCount = (TextView) itemView.findViewById(R.id.tv_content2);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ll3 = (LinearLayout) itemView.findViewById(R.id.ll3);
            ll2 = (LinearLayout) itemView.findViewById(R.id.ll2);
        }
    }

}
