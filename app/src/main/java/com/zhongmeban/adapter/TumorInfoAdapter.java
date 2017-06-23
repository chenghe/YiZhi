package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.bean.TnmDbsBean;
import java.util.List;

/**
 * 癌症分期部分Adapter
 * Created by Chengbin He on 2016/5/18.
 */
public class TumorInfoAdapter extends BaseAdapter{
    private Context mContext;
    private List<TnmDbsBean> mDatas;

    public TumorInfoAdapter(Context mContext,List<TnmDbsBean> list){
        this.mContext = mContext;
        this.mDatas = list;
    }


    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TnmDbsBean tnmDbsBean = mDatas.get(position);
        // type 是指的是 tnm分期的大标题（例子：T 的详细解释），type 1指的是分段解释
        if (getItemViewType(position)==0){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tumor_tnm, parent, false);

            TextView tv_title = (TextView) convertView.findViewById(R.id.id_item_tumor_tnm_title);
            TextView tv_content = (TextView) convertView.findViewById(R.id.id_item_tumor_tnm_title_content);
            tv_title.setText(tnmDbsBean.getTnmName());
            tv_content.setText(tnmDbsBean.getNotes());
        } else if (getItemViewType(position)==1){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tumor_tnm2, parent, false);
            TextView tv_title = (TextView) convertView.findViewById(R.id.id_item_tumor_tnm_stage);
            TextView tv_content = (TextView) convertView.findViewById(R.id.id_item_tumor_tnm_stage_content);
            tv_title.setText(tnmDbsBean.getTnmName());
            tv_content.setText(tnmDbsBean.getNotes());
        }
        return convertView;
    }
}
