package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

/**
 * @Description: RecyclerView 中只有 TextView 布局
 * Created by Chengbin He on 2016/3/23.
 */
public class RecyclerViewSingleAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;

    public RecyclerViewSingleAdapter (Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getItemCount() {
        return 30;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_textview,parent,false);
        holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(View itemView) {
            super(itemView);
        }
    }

}
