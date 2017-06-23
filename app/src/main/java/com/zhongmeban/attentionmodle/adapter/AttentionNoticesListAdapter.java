package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

import de.greenrobot.dao.attention.AttentionNotices;

/**
 * Created by Chengbin He on 2016/9/8.
 */
public class AttentionNoticesListAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;
    private List<AttentionNotices> list;

    public AttentionNoticesListAdapter(Context mContext, List<AttentionNotices> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateData(List<AttentionNotices> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attention_notices_list,parent,false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.tvName.setText(TimeUtils.changeDateToString(list.get(position).getTime())+"检查的"+list.get(position).getContent());
        if (list.get(position).getCreateTime()>0){
            myHolder.tvTime.setText(TimeUtils.changeDateToString(list.get(position).getCreateTime()));
        }
        if (list.get(position).getStatus()==1){
            //未读状态
            myHolder.bt.setEnabled(true);
            myHolder.bt.setTextColor(mContext.getResources().getColor(R.color.white));
            myHolder.bt.setText("知道了");
            myHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.text_one));
            myHolder.tvTime.setTextColor(mContext.getResources().getColor(R.color.content_two));
            myHolder.imageView.setImageResource(R.drawable.attention_notice_new);
        }else {
            //已读状态
            myHolder.bt.setEnabled(false);
            myHolder.bt.setTextColor(mContext.getResources().getColor(R.color.content_two));
            myHolder.bt.setText("已读");
            myHolder.tvName.setTextColor(mContext.getResources().getColor(R.color.content_two));
            myHolder.tvTime.setTextColor(mContext.getResources().getColor(R.color.content_two));
            myHolder.imageView.setImageResource(R.drawable.attention_notice_read);
        }
        myHolder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list ==null ? 0:list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvTime;
        public ImageView imageView;
        public Button bt;
        public MyHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            bt = (Button) itemView.findViewById(R.id.bt);
            imageView = (ImageView) itemView.findViewById(R.id.iv_icon);

        }
    }

}
