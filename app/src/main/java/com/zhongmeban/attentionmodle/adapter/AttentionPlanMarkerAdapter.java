package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexs;
import java.util.List;

/**
 * 计划标志物部分Adapter
 * Created by Chengbin He on 2016/8/1.
 */
public class AttentionPlanMarkerAdapter extends BaseAdapter {

    private Context mContext;
    private List<AttentionMarkerIndexs> list;
    private AdapterClickInterface adapterClickInterface;


    public AttentionPlanMarkerAdapter(Context mContext, List<AttentionMarkerIndexs> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void updateData(List<AttentionMarkerIndexs> list) {
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
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext)
            .inflate(R.layout.item_marker_add_plan, parent, false);
        TextView title = (TextView) view.findViewById(R.id.tv_abnormal_marker);
        TextView num = (TextView) view.findViewById(R.id.tv_num);
        TextView unit = (TextView) view.findViewById(R.id.tv_abnormal_unit);
        EditText etValue = (EditText) view.findViewById(R.id.et_abnormal_value);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_delete);
        num.setText(position + 1 + "");
        title.setText(list.get(position).getName());

        unit.setText(list.get(position).getUnit());
        etValue.setText(list.get(position).getValue());

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onItemClick(v, position);
            }
        });

        etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override public void afterTextChanged(Editable s) {
                if (s != null || !TextUtils.isEmpty(s.toString())) {
                    list.get(position).setValue(s.toString());
                }
            }
        });
        return view;
    }


    public void setItemClickListenter(AdapterClickInterface adapterClickInterface) {
        this.adapterClickInterface = adapterClickInterface;
    }
}
