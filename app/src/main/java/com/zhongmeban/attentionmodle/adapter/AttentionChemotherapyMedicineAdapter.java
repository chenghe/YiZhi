package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexs;

import java.util.List;

import de.greenrobot.dao.AttentionMedicine;

/**
 * 化疗添加用药部分，用药列表Adapter
 * Created by Chengbin He on 2016/8/25.
 */
public class AttentionChemotherapyMedicineAdapter extends BaseAdapter{

    private Context mContext;
    private List<AttentionMedicine> list;
    private AdapterClickInterface adapterClickInterface;

    public AttentionChemotherapyMedicineAdapter(Context mContext,List<AttentionMedicine> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<AttentionMedicine> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_plan_marker,parent,false);
        TextView title = (TextView) view.findViewById(R.id.tv_abnormal_marker);
        TextView num = (TextView) view.findViewById(R.id.tv_num);
        TextView dose = (TextView) view.findViewById(R.id.tv_abnormal_num);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_delete);
        iv.setVisibility(View.VISIBLE);
        num.setText(position+1+"");
        title.setText(list.get(position).getMedicine().getMedicineName());
        dose.setText(list.get(position).getMedicine().getChemicalName());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onItemClick(v,position);
            }
        });
        return view;
    }

    public void setItemClickListenter(AdapterClickInterface adapterClickInterface){
        this.adapterClickInterface = adapterClickInterface;
    }
}
