package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.TimeUtils;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.attention.MedicineRecord;

/**
 * Created by Chengbin He on 2016/7/4.
 */
public class AttentionTakingMedicineAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<MedicineRecord> list;
    private OnStopMedicineClickListenter onStopMedicineClickListenter;

    public AttentionTakingMedicineAdapter(Context mContext, List<MedicineRecord> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<MedicineRecord> list) {
        this.list = list;
    }

    public void setOnStopMedicineClickListenter(OnStopMedicineClickListenter onStopMedicineClickListenter) {
        this.onStopMedicineClickListenter = onStopMedicineClickListenter;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attention_medicine, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.tvName.setText(list.get(position).getMedicineName());

        //计算间隔天数，显示日期
        long startTime = list.get(position).getStartTime();
        long endTime;
        String endDate = " 开始";
        if (list.get(position).getEndTime() > 0) {
            endTime = list.get(position).getEndTime();
            endDate = "-"+TimeUtils.changeDateToString(endTime);
        } else {
            Date d = new Date();
            endTime = d.getTime();
        }
        Log.i("hcb", "endTime" + endTime);
        long between_days = (endTime - startTime) / (1000 * 3600 * 24);
        int day = Integer.parseInt(String.valueOf(between_days))+1;
        if (day>0){
            myHolder.tvTitle.setVisibility(View.VISIBLE);
            myHolder.tvTimeLine.setText(day + "天");
        }else {
            myHolder.tvTimeLine.setText("未开始");
            myHolder.tvTitle.setVisibility(View.GONE);
        }

        myHolder.tvTime.setText(TimeUtils.changeDateToString(startTime) + endDate);


        if (list.get(position).getStatus() == 2) {
            myHolder.btStop.setEnabled(false);
            myHolder.btStop.setTextColor(mContext.getResources().getColor(R.color.content_two));
            myHolder.btStop.setText("已停用");
        }else {
            myHolder.btStop.setEnabled(true);
            myHolder.btStop.setTextColor(mContext.getResources().getColor(R.color.white));
            myHolder.btStop.setText("停止用药");
        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v, position);
            }
        });
        myHolder.btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStopMedicineClickListenter.onButtonClick(v, position);
            }
        });

    }

    class MyHolder extends RecyclerView.ViewHolder {

        public Button btStop;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvTimeLine;
        public TextView tvTitle;

        public MyHolder(View itemView) {
            super(itemView);
            btStop = (Button) itemView.findViewById(R.id.bt_stop);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvTimeLine = (TextView) itemView.findViewById(R.id.tv_time_line);
            tvTitle = (TextView) itemView.findViewById(R.id.take_title);
        }
    }

    public interface OnStopMedicineClickListenter {
        void onButtonClick(View v, int position);
    }

}
