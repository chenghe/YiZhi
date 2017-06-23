package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

/**
 * Created by Chengbin He on 2016/3/23.
 */
public class AnswersAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;

    public AnswersAdapter(Context mContext){
        this.mContext = mContext;
    }


    @Override
    public int getItemCount() {
        return 40;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_myarticle, parent, false);
        holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.tv_add.setVisibility(View.GONE);
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView tv_add;
        public MyHolder(View itemView) {
            super(itemView);
            tv_add = (TextView) itemView.findViewById(R.id.tv_add);
        }
    }


}
