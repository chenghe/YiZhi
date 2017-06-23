package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.DoctorWorkTime;

import java.util.List;

/**
 * Created by Chengbin He on 2016/5/18.
 */
public class DoctWorkTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<DoctorWorkTime.WorkTimeList> list;
    private DoctorWorkTime doctorWorkTime;
    private String hospName;

    public DoctWorkTimeAdapter(Context mContext,List<DoctorWorkTime.WorkTimeList> list,String hospName,DoctorWorkTime doctorWorkTime){
        this.mContext = mContext;
        this.list = list;
        this.doctorWorkTime = doctorWorkTime;
        this.hospName = hospName;
    }

    public void notifyData(List<DoctorWorkTime.WorkTimeList> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (holder == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_doctworktime, parent, false);
            holder.tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tvHosp = (TextView) convertView.findViewById(R.id.tv_hosp);
            holder.tvCost = (TextView) convertView.findViewById(R.id.tv_cost);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        initData(holder,position);
        return convertView;
    }

    private void initData(MyViewHolder holder, int position) {

        holder.tvHosp.setText(hospName);

        String week = "";
        switch (list.get(position).getWeek()){
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
            case 7:
                week = "星期日";
                break;
            default:
                week = "星期一";
                break;
        }
        holder.tvWeek.setText(week);

        String time = "";
        switch (list.get(position).getTime()){
            case 1:
                time = "上午";
                break;
            case 2:
                time = "下午";
                break;
           default:
               time = "上午";
                break;
        }
        holder.tvTime.setText(time);

        String desc = "";
        switch (list.get(position).getType()){
            case 0:
                desc = "无";
                break;
            case 1:
                desc = "普通门诊";
                break;
            case 2:
                desc = "专家门诊";
                break;
            case 3:
                desc = "特需门诊";
                break;
            default:
                desc = "无";
                break;
        }
        holder.tvDesc.setText(desc);

        String cost = "";
        switch (list.get(position).getType()){
            case 1://普通门诊
                cost = doctorWorkTime.getData().getNormalFee() + " 元";
                break;
            case 2://专家门诊
                cost = doctorWorkTime.getData().getExperFee() + " 元";
                break;
            case 3://特殊门诊
                cost = doctorWorkTime.getData().getSpecialFee() + " 元";
                break;
            default:
                cost = "0 元";
                break;
        }
        holder.tvCost.setText(cost);
    }


    class MyViewHolder {
        public TextView tvWeek;
        public TextView tvTime;
        public TextView tvDesc;
        public TextView tvHosp;
        public TextView tvCost;
    }

}
