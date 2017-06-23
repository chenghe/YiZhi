package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.BaseBean;

import java.util.List;

/**
 * Created by Chengbin He on 2016/3/22.
 */
public class MyArticleAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;


    public MyArticleAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return 3;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
                Log.i("hcb", "MyArticleAdapter item onClick");
            }
        });
    }



    class MyHolder extends RecyclerView.ViewHolder{
        private TextView tv_address;
        private TextView tv_title;
        public MyHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);

        }
    }

}
