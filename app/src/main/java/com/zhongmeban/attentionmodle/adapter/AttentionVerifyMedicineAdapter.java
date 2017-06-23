package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.postbody.CreateMedicineRecordListBody;
import com.zhongmeban.utils.AttentionUtils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Chengbin He on 2016/8/5.
 */
public class AttentionVerifyMedicineAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<CreateMedicineRecordListBody> list;
    private AdapterClickInterface deleteClick;//删除被点击时回掉监听

    public AttentionVerifyMedicineAdapter(Context mContext, List<CreateMedicineRecordListBody> list) {

        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<CreateMedicineRecordListBody> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_attention_verify_medicine, parent, false);

        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.tvNum.setText(position + 1 + "");
        myHolder.tvMedicineName.setText(list.get(position).getMedicineName());
        String startTime = changeDateToString(list.get(position).getStartTime());
        String endTime = " 开始";
        if (list.get(position).getEndTime()>0){
            endTime = "-"+changeDateToString(list.get(position).getEndTime());
        }
        myHolder.tvCycler.setText(startTime+endTime);

        myHolder.tvType.setText(AttentionUtils.getTakeMedicinePurpose(list.get(position).getTypes()));

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v, position);
            }
        });

        myHolder.rlDelete.setOnClickListener(new View.OnClickListener() {
          @Override
            public void onClick(View v) {
              deleteClick.onItemClick(v,position);
              }
        }
        );

    }

    public void setDeleteOnclickListenter( AdapterClickInterface deleteClick){
        this.deleteClick =deleteClick;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        public TextView tvNum;
        public TextView tvMedicineName;
        public TextView tvCycler;
        public TextView tvType;
        public ImageView ivDelete;
        public RelativeLayout rlDelete;

        public MyHolder(View itemView) {
            super(itemView);
            tvNum = (TextView) itemView.findViewById(R.id.tv_num);
            tvMedicineName = (TextView) itemView.findViewById(R.id.tv_medicine_name);
            tvCycler = (TextView) itemView.findViewById(R.id.tv_cycler);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            rlDelete = (RelativeLayout) itemView.findViewById(R.id.rl_delete);
        }
    }

    /***
     * 转换时间戳为时间（YYYY年MM月DD天）
     *
     * @param timestamp
     * @return YYYY年MM月DD天
     */
    public String changeDateToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sdf.format(new Date(timestamp));
        return date;
    }
}
