package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.bean.TreatMethodLists;
import com.zhongmeban.utils.DisplayUtil;
import java.util.List;

/**
 * 治疗方案2级界面Adapter
 * Created by Chengbin He on 2016/5/5.
 */
public class TreatByDisAdapetr extends BaseAdapter {
    private Context mContext;
    private List<TreatMethodLists.Data> list;
    private boolean isBaXiangZhiLiao;


    public TreatByDisAdapetr(Context mContext, List<TreatMethodLists.Data> list, boolean baXiangZhiLiao) {
        this.mContext = mContext;
        this.list = list;
        this.isBaXiangZhiLiao = baXiangZhiLiao;
    }


    public void notifyData(List<TreatMethodLists.Data> list) {
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
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_textview_only, parent, false);
        TextView tv = (TextView) v.findViewById(R.id.tv);
        TextView tvSub = (TextView) v.findViewById(R.id.tv_sub_name);

        if (isBaXiangZhiLiao){
            tv.setText(list.get(position).getShowName());
            tvSub.setText(list.get(position).getTherapeuticName());
            ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dip2px(mContext,60));
            v.setLayoutParams(params);

        } else{
            tv.setText(list.get(position).getTherapeuticName());
        }

        tvSub.setVisibility(isBaXiangZhiLiao ? View.VISIBLE : View.GONE);
        View line = v.findViewById(R.id.line);
        line.setVisibility(View.GONE);
        return v;
    }
}
