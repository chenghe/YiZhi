package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.BaseBean;
import com.zhongmeban.utils.SmoothCheckBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 关注医生 RecyclerView Adapter
 * Created by Chengbin He on 2016/3/21.
 */
public class AttenDoctorAdapter extends BaseRecyclerViewAdapter {
    private Context mContext;
    private List<RecyclerView.ViewHolder> viewList = new ArrayList<RecyclerView.ViewHolder>();
    public HashMap<Integer, Boolean> visiblecheck = new HashMap<Integer, Boolean>(); ;//用来记录是否显示checkBox
    public HashMap<Integer, Integer> checkPosition = new HashMap<Integer, Integer>();
    private List<Integer> checkedItem = new ArrayList<Integer>();
    private List<BaseBean> dataList;
    private Boolean checkVisable = false;
    private Boolean isFirst = true;

    public AttenDoctorAdapter(Context mContext ,List<BaseBean> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;

    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_attendoc, parent, false);
        holder = new MyHolder(v);
        viewList.add(holder);
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.i("hcb", "onBindViewHolder");
        Log.i("hcb", "position is"+position);
        checkPosition.put(position,position);
        final MyHolder myHolder = (MyHolder) holder;
        if (visiblecheck.get(myHolder.getAdapterPosition())!=null&&visiblecheck.get(myHolder.getAdapterPosition())){
            myHolder.scb.setChecked(false);
            myHolder.scb.setVisibility(View.VISIBLE);
        }else{
            myHolder.scb.setVisibility(View.GONE);
        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
                Log.i("hcb", "AttenDoctorAdapter item onClick");
                if ( myHolder.scb.isChecked()) {
                    myHolder.scb.setChecked(false, true);
                }else{
                    myHolder.scb.setChecked(true, true);
                }
            }
        });

        myHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClick.onItemLongClick(v,position);
                Log.i("hcb", "AttenDoctorAdapter item onLongClickListener");
                for (int i = 0; i < viewList.size(); i++) {
                    MyHolder holder = (MyHolder) viewList.get(i);
                    holder.scb.setChecked(false);
                    holder.scb.setVisibility(View.VISIBLE);
                }
                for(int i = 0; i < dataList.size(); i++){
                    visiblecheck.put(i,true);
                }
                return true;
            }
        });

    }

    public void setCheckGone(){
//        if (viewList.size() == dataList.size()&& viewList.size()!=0){
//            for (int i = 0; i < viewList.size(); i++) {
//                MyHolder holder = (MyHolder) viewList.get(i);
//                holder.scb.setVisibility(View.GONE);
//            }
//        }else{
            for (int i = 0; i < viewList.size(); i++) {
                MyHolder holder = (MyHolder) viewList.get(i);
                holder.scb.setVisibility(View.GONE);
            }
            for(int i = 0; i < dataList.size(); i++){
                visiblecheck.put(i, false);
            }
//        }

    }

    public void setSelectAll(){
        for (int i = 0; i < viewList.size(); i++) {
            MyHolder holder = (MyHolder) viewList.get(i);
            checkedItem.add(i);
            holder.scb.setChecked(true);
        }
        Log.i("hcbtest","viewList.size() is"+viewList.size());
        Log.i("hcbtest","checkedItem.size() is"+checkedItem.size());
    }

    public void setUnSelectAll(){
        for (int i = viewList.size()-1; i >= 0; i--) {
            MyHolder holder = (MyHolder) viewList.get(i);
            checkedItem.remove(i);
            holder.scb.setChecked(false);
        }
        Log.i("hcbtest","viewList.size() is"+viewList.size());
        Log.i("hcbtest","checkedItem.size() is"+checkedItem.size());
    }

    public void removeItem(){
        for (int i = 0; i < viewList.size(); i++ ){
            MyHolder holder = (MyHolder) viewList.get(i);
            if (holder.scb.isChecked()){
                int b = holder.getAdapterPosition();
                checkedItem.add(b);
            }
        }

        Log.i("hcbtest","checkedItem.size() is"+checkedItem.size());
        for (int i = checkedItem.size()-1; i >= 0; i--) {
            int a = checkedItem.get(i);
            Log.i("hcbtest", "int a is" + a);
            Log.i("hcbtest", "int i is" + i);
            dataList.remove(i);
            viewList.remove(i);
            notifyItemRemoved(a);
//            notifyDataSetChanged();
//            notifyItemRangeChanged(checkedItem.get(i), getItemCount());
        }
        checkedItem.clear();
    }


//    public int getCheckNumber(){
//        for (int i = 0; i < checkState.size(); i++) {
//            MyHolder holder = (MyHolder) viewList.get(i);
//            if (holder.scb.isChecked()){
//                checkNumber++;
//            }
//        }
//        return checkNumber;
//    }




    class MyHolder extends RecyclerView.ViewHolder {
        public SmoothCheckBox scb;
        public View mView;

        public MyHolder(View itemView) {
            super(itemView);
            mView = itemView;
            scb = (SmoothCheckBox) itemView.findViewById(R.id.scb);
        }
    }


}
