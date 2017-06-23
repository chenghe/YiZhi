package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.SurgeryMethodsByDiseaseId;
import com.zhongmeban.bean.TreatMethodLists;

import java.util.List;

/**
 * 治疗手术项目Adapter
 * Created by Chengbin He on 2016/5/5.
 */
public class AttentionTreatByDisAdapter extends BaseAdapter{
    private Context mContext;
    private List<TreatMethodLists.Data> list;

    public AttentionTreatByDisAdapter(Context mContext , List<TreatMethodLists.Data> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<TreatMethodLists.Data> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_textview_only,parent,false);
        TextView tv = (TextView) v.findViewById(R.id.tv);
        tv.setText(list.get(position).getTherapeuticName());
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.GONE);
        return v;
    }
}
