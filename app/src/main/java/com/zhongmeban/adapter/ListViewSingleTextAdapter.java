package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import de.greenrobot.dao.BaseDao;
import java.util.List;

/**
 * 治疗方法癌症部分Adapter
 * Created by Chengbin He on 2016/3/30.
 */
public class ListViewSingleTextAdapter extends BaseAdapter {

    private Context mContext;
    private int type = 0;
    private List<? extends BaseDao> list;


    public ListViewSingleTextAdapter(Context mContext, int type, List<? extends BaseDao> list) {
        this.mContext = mContext;
        this.type = type;
        this.list = list;
    }


    public void notifyData(List<? extends BaseDao> list) {
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
        tv.setText(list.get(position).name);
        ImageView iv = (ImageView) v.findViewById(R.id.iv);
        switch (type) {
            case 1:
                iv.setVisibility(View.VISIBLE);
                break;
            default:
                iv.setVisibility(View.GONE);
                break;

        }

        return v;
    }
}
