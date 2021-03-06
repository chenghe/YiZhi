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
 * 医生等级筛选Adapter
 * Created by Chengbin He on 2016/5/23.
 */
public class DoctLevelSelectAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;

    public DoctLevelSelectAdapter(Context mContext){
        this.mContext = mContext;
    }


    @Override
    public int getItemCount() {
        return 7;
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
        String level = "";
        switch (position){
            case 0:
                level = "无等级";
                break;
            case 1:
                level = "主任医师";
                break;
            case 2:
                level = "副主任医师";
                break;
            case 3:
                level = "主治医师";
                break;
            case 4:
                level = "副主治医师";
                break;
            case 5:
                level = "住院医师";
                break;
            case 6:
                level = "其他";
                break;
            default:
                level = "其他";
                break;
        }
        mHolder.tvName.setText(level);

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
