package com.zhongmeban.mymodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.FavoriteMedicineList;
import com.zhongmeban.bean.FavoriteTreatMethodLists;
import com.zhongmeban.bean.MedicineList;
import com.zhongmeban.utils.SmoothCheckBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.next.tagview.TagCloudView;

/**
 * Created by Chengbin He on 2016/11/8.
 */

public class FavoriteMedicineListAdapter extends BaseRecyclerViewAdapter {

    private List<FavoriteMedicineList.DrugScourceItem> list;
    private Context mContext;
    private Set<String> ChooseSet = new HashSet<>();
    private boolean isChoose = false;//是否为筛选模式

    public FavoriteMedicineListAdapter(List<FavoriteMedicineList.DrugScourceItem> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
        if(!isChoose){
            ChooseSet.clear();
        }
        notifyDataSetChanged();
    }
    public List getChooseList(){
        List<Integer> list = new ArrayList<>();
        for (String mId: ChooseSet){
            int id = Integer.valueOf(mId);
            list.add(id);
        }
        return list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.favorite_common_item, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder mHolder = (MyHolder) holder;
        mHolder.name.setText(list.get(position).getShowName());
        mHolder.describe.setText(list.get(position).getChemicalName());
        if (isChoose){
            mHolder.checkBox.setChecked(false);
            mHolder.checkBox.setVisibility(View.VISIBLE);
        }else {
            mHolder.checkBox.setChecked(false);
            mHolder.checkBox.setVisibility(View.GONE);
        }
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChoose){
                    if (mHolder.checkBox.isChecked()){
                        mHolder.checkBox.setChecked(false);
                        Log.i("hcbtest","ChooseSet.remove(list.get(position).getInformationId())" +list.get(position).getMedicineId());
                        ChooseSet.remove(list.get(position).getMedicineId()+"");
                    }else {
                        mHolder.checkBox.setChecked(true);
                        Log.i("hcbtest","ChooseSet.add(list.get(position).getInformationId())" +list.get(position).getMedicineId());
                        ChooseSet.add(list.get(position).getMedicineId()+"");
                    }
                }else {
                    mClick.onItemClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView describe;
        public SmoothCheckBox checkBox;

        public MyHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            describe = (TextView) view.findViewById(R.id.tv_desc);
            checkBox = (SmoothCheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
