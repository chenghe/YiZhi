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
import de.greenrobot.dao.attention.SurgeryMethods;
import java.util.List;

/**
 * Created by Chengbin He on 2016/8/12.
 */
public class AttentionOperationOtherAdapter extends BaseAdapter{

    private Context mContext;
    private List<SurgeryMethods> list;
    private AdapterClickInterface clickInterface;

    public AttentionOperationOtherAdapter(Context mContext, List<SurgeryMethods> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<SurgeryMethods> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_textview_delete,parent,false);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(list.get(position).getSurgeryMethodName());
        TextView tvNum = (TextView) convertView.findViewById(R.id.tv_num);
        tvNum.setText(position+1+"");
        ImageView ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemClick(v,position);
            }
        });
        return convertView;
    }

    public void SetItemClickListenter(AdapterClickInterface clickInterface){
        this.clickInterface = clickInterface;
    }
}
