package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;

/**
 * 新增患者第二步Adapter
 * Created by Chengbin He on 2016/6/21.
 */
public class FragmentAddAttentionAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private int checkPotion = 12;
    private boolean ISMALE = true;
    private int relation;//关系枚举

    public FragmentAddAttentionAdapter(Context context,boolean ISMALE) {
        mContext = context;
        this.ISMALE = ISMALE;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_add_attention, parent, false);
        AddAttentionViewHolder holder = new AddAttentionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AddAttentionViewHolder mHolder = (AddAttentionViewHolder) holder;

        mHolder.textView.setText(getName(position));
        if (position == checkPotion) {
            mHolder.relativeLayout.setBackgroundResource(R.drawable.bg_box_selected);
            mHolder.textView.setTextColor(mContext.getResources().getColor(R.color.white));

        } else {
            mHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            mHolder.textView.setTextColor(mContext.getResources().getColor(R.color.top_text));
        }

        if (position == checkPosition(relation)){
            mHolder.relativeLayout.setBackgroundResource(R.drawable.bg_box_selected);
            mHolder.textView.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            mHolder.relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            mHolder.textView.setTextColor(mContext.getResources().getColor(R.color.top_text));
        }

        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPotion = position;
                Log.i("hcbtest"," checkPotion "+checkPotion);
                Log.i("hcbtest"," position "+position);
                notifyDataSetChanged();
                mClick.onItemClick(v, position);
            }
        });

    }

    private int checkPosition(int relation){
        int position = 5;
        switch (relation){
            case 1:
                position = 0;
                break;
            case 2:
                position = 1;
                break;
            case 3:
                position = 1;
                break;
            case 4:
                position = 2;
                break;
            case 5:
                position = 2;
                break;
            case 6:
                position = 3;
                break;
            case 7:
                position = 3;
                break;
            case 9:
                position = 4;
                break;

        }

        return position;
    }

    private String getName(int position) {
        String name = "";
        switch (position) {
            case 0:
                name = "本人";
                break;
            case 1:
                if (ISMALE){
                    name = "父亲";
                }else {
                    name = "母亲";
                }
                break;
            case 2:
                if(ISMALE){
                    name = "丈夫";
                }else {
                    name = "妻子";
                }
                break;
            case 3:
                if (ISMALE){
                    name = "儿子";
                }else {
                    name = "女儿";
                }
                break;
            case 4:
                name = "其他";
                break;

        }
        return name;
    }

    public int getRelation(int position){
        switch (position){
            case 0:
                relation = 1;
                break;
            case 1:
                if (ISMALE){
                    relation = 2;
                }else {
                    relation = 3;
                }
                break;
            case 2:
                if (ISMALE){
                    relation = 4;
                }else {
                    relation = 5;
                }
                break;
            case 3:
                if (ISMALE){
                    relation = 6;
                }else {
                    relation = 7;
                }
                break;
            case 4:
                relation = 9;
                break;
            default:
                relation = 0;
                break;
        }

        return relation;
    }

    public void setRelation(int relation){
        this.relation = relation;
    }

    class AddAttentionViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;

        public AddAttentionViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_item);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }
    }


}
