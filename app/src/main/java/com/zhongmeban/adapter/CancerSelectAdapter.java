package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

import java.util.List;

import de.greenrobot.dao.Tumor;


/**
 * 肿瘤筛选Adapter
 * Created by Chengbin He on 2016/5/23.
 */
public class CancerSelectAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;
    private List<Tumor> list;

    public CancerSelectAdapter(Context mContext,List<Tumor> list){
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public int getItemCount() {
       return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_textview_only,parent,false);
        HyViewHolder holder = new HyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HyViewHolder mHolder = (HyViewHolder) holder;
        mHolder.line.setVisibility(View.VISIBLE);
        mHolder.tvName.setText(list.get(position).getName());

        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
            }
        });
    }

    class HyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private View line;

        public HyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv);
            line = itemView.findViewById(R.id.line);
        }
    }
}
