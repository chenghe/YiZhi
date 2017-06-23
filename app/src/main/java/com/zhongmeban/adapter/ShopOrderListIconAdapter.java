package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

/**
 * Created by Chengbin He on 2016/10/21.
 */

public class ShopOrderListIconAdapter extends BaseRecyclerViewAdapter{
    private Context mContext;

    public ShopOrderListIconAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_order_list_icon_item,parent,false);
        ShopOrderListIconAdapter.MyHolder myHolder = new ShopOrderListIconAdapter.MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public ImageView ivIcon;
        public MyHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
