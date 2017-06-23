package com.zhongmeban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.bean.ChemotherapyCourseListBean;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

import de.greenrobot.dao.attention.ChemotherapyCourse;

/**
 * 化疗增加疗程Adapter
 * Created by Chengbin He on 2016/7/12.
 */
public class AddTreatmentAdapter extends BaseAdapter{

    private Context mContext;
    private List<ChemotherapyCourseListBean> list;
    private AdapterClickInterface adapterClickInterface;

    public  AddTreatmentAdapter(Context mContext,List<ChemotherapyCourseListBean> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<ChemotherapyCourseListBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemClickListenter(AdapterClickInterface adapterClickInterface){
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attention_add_treament,
                parent,false);
        TextView tvCourseName = (TextView) view.findViewById(R.id.tv_treament_num);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_period);//周期
        TextView tvNotes = (TextView) view.findViewById(R.id.tv_remark);//备注
        TextView tvTakeMedicine = (TextView) view.findViewById(R.id.tv_medicine);//用药
        tvTakeMedicine.setText(AttentionUtils.getChemotherapyCourseMedicineName(list.get(position).getDbChemotherapyCourseMedicineList()));

        String endTime = " 开始";
        if (list.get(position).getChemotherapyCourse().getEndTime()>0){
            endTime = "-"+TimeUtils.changeDateToString(list.get(position).getChemotherapyCourse().getEndTime());
        }
        String startTime = TimeUtils.changeDateToString(list.get(position).getChemotherapyCourse().getStartTime());
        tvTime.setText(startTime+endTime);

        tvCourseName.setText("第"+list.get(position).getChemotherapyCourse().getTimes()+"个周期");

        if (list.get(position).getChemotherapyCourse().getDescription()!=null){
            tvNotes.setText(list.get(position).getChemotherapyCourse().getDescription());
        }

        RelativeLayout rlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
        rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onItemClick(v,position);
            }
        });
        return view;
    }
}
