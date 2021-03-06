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
 * 医院等级筛选Adapter
 * Created by Chengbin He on 2016/5/23.
 */
public class HospLevelAdapter extends BaseRecyclerViewAdapter{

    private Context mContext;

    public HospLevelAdapter(Context mContext){
        this.mContext = mContext;
    }


    @Override
    public int getItemCount() {
        return 8;
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
                level = "三级";
                break;
            case 1:
                level = "三级甲等";
                break;
            case 2:
                level = "三级乙等";
                break;
            case 3:
                level = "二级";
                break;
            case 4:
                level = "二级甲等";
                break;
            case 5:
                level = "二级乙等";
                break;
            case 6:
                level = "一级";
                break;
            case 7:
                level = "一级甲等";
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
