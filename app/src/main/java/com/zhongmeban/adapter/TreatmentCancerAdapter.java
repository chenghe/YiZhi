package com.zhongmeban.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import de.greenrobot.dao.BaseDao;
import java.util.List;

/**
 * 治疗方法，筛选癌症部分Adapter
 * Created by Chengbin He on 2016/5/30.
 */
public class TreatmentCancerAdapter extends BaseAdapter {

    private Context mContext;
    private List<? extends BaseDao> list;
    private AdapterClickInterface clickInterface;
    private int mLastCheckedPosition = -1;

    private int selectPos;


    public TreatmentCancerAdapter(Context mContext, List<? extends BaseDao> list) {
        this.mContext = mContext;
        this.list = list;
        selectPos = 0;

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
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext)
            .inflate(R.layout.item_treatment_cancer, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(list.get(position).name);
        final RelativeLayout item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
        final View focused = convertView.findViewById(R.id.view_line);

        if (position == selectPos) {
            focused.setVisibility(View.VISIBLE);
            item.setBackgroundColor(Color.WHITE);
        } else {
            focused.setVisibility(View.INVISIBLE);
            item.setBackgroundResource(R.color.title_bg);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLastCheckedPosition = position;
                clickInterface.onItemClick(v, position);
                if (selectPos!=position){
                    selectPos = position;
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }


    public void setAdapterClickListener(AdapterClickInterface clickListener) {
        clickInterface = clickListener;
    }
}
