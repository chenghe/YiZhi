package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.DoctDetailMedicase;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

/**
 * 医生案例部分Adapter
 * Created by Chengbin He on 2016/5/11.
 */
public class DoctCaseAdapter extends BaseAdapter {
    private List<DoctDetailMedicase.Medicalcase> list;
    private Context mContext;

    public DoctCaseAdapter(Context mContext, List<DoctDetailMedicase.Medicalcase> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<DoctDetailMedicase.Medicalcase> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_doctcase, parent, false);
            holder.tvCancer = (TextView) convertView.findViewById(R.id.tv_cancer);
            holder.tvSatisfy = (TextView) convertView.findViewById(R.id.tv_satisfy);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        initData(holder,position);
        return convertView;
    }

    private void initData(MyViewHolder holder,int position) {
        if (list.get(position).getDiseases().size()>0){
            holder.tvCancer.setText(list.get(position).getDiseases().get(0).getName());
        }
        holder.tvSatisfy.setText("很满意");
        holder.tvDate.setText(TimeUtils.getTimeFromeToday(list.get(position).getWriteTime()));
        holder.tvContent.setText(list.get(position).getContent());
    }

    class MyViewHolder {
            public TextView tvCancer;
            public TextView tvSatisfy;
            public TextView tvDate;
            public TextView tvContent;
        }

}