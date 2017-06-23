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
import com.zhongmeban.utils.ZMBUtil;

/**
 * 关注网格布局Adapter
 * Created by Chengbin He on 2016/4/7.
 */
public class FragAttentionGridAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    public FragAttentionGridAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_card_home_grid, parent, false);
        holder = new MyHolder(v);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
            ZMBUtil.getWidth()/3,(int) (ZMBUtil.getWidth() / 3 * (0.8f))));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        switch (position){
            case 0:
                myHolder.tv_desc.setText("标志物记录");
                myHolder.iv_icon.setImageResource(R.drawable.landmark);
                break;
            case 1:
                myHolder.tv_desc.setText("住院记录");
                myHolder.iv_icon.setImageResource(R.drawable.hospital_attention);
                break;
            case 2:
                myHolder.tv_desc.setText("手术记录");
                myHolder.iv_icon.setImageResource(R.drawable.operation);

                break;
            case 3:
                myHolder.tv_desc.setText("化疗记录");
                myHolder.iv_icon.setImageResource(R.drawable.chemotherapy);

                break;
            case 4:

                myHolder.tv_desc.setText("放疗记录");
                myHolder.iv_icon.setImageResource(R.drawable.radiotherapy);
                break;
            case 5:

                myHolder.tv_desc.setText("辅助用药");
                myHolder.iv_icon.setImageResource(R.drawable.pil);
                break;
        }
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
            }
        });
    }


    class MyHolder extends RecyclerView.ViewHolder{
        public ImageView iv_icon;
        public TextView tv_desc;
        public MyHolder(View itemView) {
            super(itemView);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}

