package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexs;

import java.util.List;

import de.greenrobot.dao.attention.RecordIndex;

/**
 * 计划标志物详情部分Adapter
 * Created by Chengbin He on 2016/8/1.
 */
public class AttentionDetailMarkerAdapter extends BaseAdapter{

    private Context mContext;
    private List<RecordIndex> list;

    public AttentionDetailMarkerAdapter(Context mContext, List<RecordIndex> list){
            this.mContext = mContext;
            this.list = list;
    }

    public void updateData(List<RecordIndex> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list== null ? 0: list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_plan_marker,parent,false);
        TextView title = (TextView) view.findViewById(R.id.tv_abnormal_marker);
        TextView num = (TextView) view.findViewById(R.id.tv_num);
        TextView dose = (TextView) view.findViewById(R.id.tv_abnormal_num);

        num.setText(position+1+"");
        title.setText(list.get(position).getIndexName());
        String unit = "";
        if (list.get(position).getUnitName()!=null){
            unit = list.get(position).getUnitName();
        }

        dose.setText(list.get(position).getValue() + " "+unit);
        return view;
    }
}
