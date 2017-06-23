package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.AttentionOtherTherapeutic;
import com.zhongmeban.bean.SimpleHospitalPageSourceItems;
import com.zhongmeban.bean.SurgeryMethodsByDiseaseId;
import com.zhongmeban.utils.SmoothCheckBox;

import java.util.List;

/**
 * 关注部分手术项筛选
 * Created by Chengbin He on 2016/8/10.
 */
public class AttentionTherapeuticAdapter extends BaseRecyclerViewAdapter {


    private Context mContext;
    private List<AttentionOtherTherapeutic> list;

    public AttentionTherapeuticAdapter(Context mContext, List<AttentionOtherTherapeutic> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<AttentionOtherTherapeutic> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;

        v = LayoutInflater.from(mContext).inflate(R.layout.item_textview_only, parent, false);
        holder = new MyHolder(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyHolder myHolder = (MyHolder) holder;
        myHolder.line.setVisibility(View.VISIBLE);
        myHolder.tv.setText(list.get(position).getData().getName());
        myHolder.checkBox.setVisibility(View.VISIBLE);
        if (list.get(position).isCheck()){
            myHolder.checkBox.setChecked(true);
        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHolder.checkBox.isChecked()){
                    myHolder.checkBox.setChecked(false,true);
                    list.get(position).setCheck(false);
                }else {
                    myHolder.checkBox.setChecked(true,true);
                    list.get(position).setCheck(true);
                }
                mClick.onItemClick(myHolder.itemView, position);
            }
        });
    }

    public List<AttentionOtherTherapeutic> getList(){
        return list;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

   public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public View line;
        public SmoothCheckBox checkBox;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            line = itemView.findViewById(R.id.line);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
        }
    }


}