package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.MedicineScourceItem;

import com.zhongmeban.utils.genericity.ImageUrl;
import java.util.List;

/**
 * 药箱子，药厂icon列表Adapter
 * Created by Chengbin He on 2016/4/7.
 */
public class FragMedChestBrandZoneAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private String imageUrl = ImageUrl.BaseImageUrl;
    private List<MedicineScourceItem> list;

    public FragMedChestBrandZoneAdapter(Context mContext,List<MedicineScourceItem> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<MedicineScourceItem> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return  list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        switch (viewType) {
            case 0:
                //上拉刷新
                v = LayoutInflater.from(mContext).inflate(R.layout.load_more_progress, parent, false);
                holder = new LoadHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_card_brandzone, parent, false);
                holder = new MyHolder(v);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            MyHolder myHolder = (MyHolder) holder;
            if(list.get(position).getLogo()!= null){
                String url = list.get(position).getLogo();
                Log.i("hcb", "url is " + imageUrl + url);
//                int height = BaseRecyclerViewAdapter.dip2px(mContext,100);
//                int width = BaseRecyclerViewAdapter.dip2px(mContext,100);
                Picasso.with(mContext).load(imageUrl+url).placeholder(R.drawable.picasso_medicine_box).error(R.drawable.picasso_medicine_box)
                        .into(myHolder.iv_icon);
            }

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(v, position);
                }
            });
        }
    }


    class MyHolder extends RecyclerView.ViewHolder{
        public ImageView iv_icon;
        public MyHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder{

        public LoadHolder(View itemView) {
            super(itemView);
        }
    }
}

