package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;

import java.util.List;

import de.greenrobot.dao.attention.RadiotherapySuspendedRecords;

import static com.zhongmeban.utils.TimeUtils.changeDateToString;

/**
 * Created by HeCheng on 2016/10/17.
 */

public class StopRadiotherapyAdapter extends BaseAdapter{

    private Context mContext;
    private List<RadiotherapySuspendedRecords> list;
    private AdapterClickInterface adapterClickInterface;

    public StopRadiotherapyAdapter(Context mContext, List<RadiotherapySuspendedRecords> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setAdapterClickInterface(AdapterClickInterface adapterClickInterface) {
        this.adapterClickInterface = adapterClickInterface;
    }

    @Override
    public int getCount() {
        return list.size();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.stop_radiotherapy_item,parent,false);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);

            String suspendedNotes = "";
//            for (int i = 0; i < list.size(); i++) {
                String startDate;
                String reason;
                String date;

                startDate = changeDateToString(list.get(position).getStartTime());
                if (list.get(position).getReason() == 1) {
                    reason = "身体耐受性原因";
                } else {
                    reason = "其他原因";
                }
                date = list.get(position).getSuspendDays() + "天";

                suspendedNotes += startDate +" 因为 "+reason +"暂停 "+date;
//            }
            tvName.setText(suspendedNotes);

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onItemClick(v,position);
            }
        });
        return view;
    }
}
