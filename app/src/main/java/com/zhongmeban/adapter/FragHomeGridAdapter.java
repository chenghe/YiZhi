package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.JelwelBoxBean;
import com.zhongmeban.utils.ZMBUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 主页网格布局Adapter
 * Created by Chengbin He on 2016/4/7.
 */
public class FragHomeGridAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private List<JelwelBoxBean> mDatas = new ArrayList<>();


    public FragHomeGridAdapter(Context mContext, List<JelwelBoxBean> list) {
        this.mContext = mContext;
        this.mDatas.clear();
        this.mDatas.addAll(list);
    }


    @Override
    public int getItemCount() {
        return 9;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_card_home_grid, parent, false);
        holder = new MyHolder(v);

        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ZMBUtil.getWidth() / 3,
            (int) (ZMBUtil.getWidth() / 3 * (0.8f))));
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;

        myHolder.tv_desc.setText(mDatas.get(position).getName());
        myHolder.iv_icon.setImageResource(mDatas.get(position).getId());
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v, position);
            }
        });
    }


    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_icon;
        public TextView tv_desc;


        public MyHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}

