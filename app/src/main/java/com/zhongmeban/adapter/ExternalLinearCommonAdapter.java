package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.bean.ExternalLinearCommonBean;

import java.util.List;

/**
 * Created by Chengbin He on 2016/5/19.
 */
public class ExternalLinearCommonAdapter extends BaseAdapter{

    private Context mContext;
    private List<ExternalLinearCommonBean> list;
    private AdapterClickInterface clickInterface;

    public ExternalLinearCommonAdapter(Context mContext,List<ExternalLinearCommonBean> list){
        this.mContext = mContext;
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_external_linear_common,
                parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvSubName = (TextView) convertView.findViewById(R.id.tv_subname);

        tvName.setText(list.get(position).getName());
        tvSubName.setText(list.get(position).getSubName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemClick(v,position);
            }
        });

        return convertView;
    }

    public void setClickAdapterListener(AdapterClickInterface adapterListener){
        clickInterface = adapterListener;
    }
}
