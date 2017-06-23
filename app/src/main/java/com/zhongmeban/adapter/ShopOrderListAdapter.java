package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

/**
 * Created by Chengbin He on 2016/10/21.
 */

public class ShopOrderListAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;

    public ShopOrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_order_list_item,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;

        myHolder.icRecyclerView
                .setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        myHolder.icRecyclerView.setAdapter(new ShopOrderListIconAdapter(mContext));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public RecyclerView icRecyclerView;
        public MyHolder(View itemView) {
            super(itemView);
            icRecyclerView = (RecyclerView) itemView.findViewById(R.id.icon_recyclerview);
        }
    }
}
