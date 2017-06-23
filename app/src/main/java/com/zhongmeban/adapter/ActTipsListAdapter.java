package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.treatbean.TreatTipsBean;
import com.zhongmeban.utils.TimeUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description:小贴士Activity 列表Adapter
 * Created by Chengbin He on 2016/4/19.
 */
public class ActTipsListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<TreatTipsBean> list;
    private Set<String> mSetReads = new HashSet<>();

    public ActTipsListAdapter(Context mContext, List<TreatTipsBean> list, Set<String> readIds){
        this.mContext = mContext;
        this.list = list;
        this.mSetReads = readIds;
    }

    public void notifyData(List<TreatTipsBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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
        MyHolder mHolder = (MyHolder) holder;
        mHolder.tv_title.setText(list.get(position).getTitle());
        mHolder.tv_time.setText(TimeUtils.changeDateToString(list.get(position).getTime()));
        mHolder.tv_address.setText(TextUtils.isEmpty(list.get(position).getWriter())?"":list.get(position).getWriter());// 作者或者地址没有信息

        if (mSetReads.contains(String.valueOf(list.get(position).getId()))){
            mHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.content_two));
            mHolder.tv_time.setTextColor(mContext.getResources().getColor(R.color.content_two));
        } else {
            mHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.text_one));
            mHolder.tv_time.setTextColor(mContext.getResources().getColor(R.color.text_one));

        }
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onItemClick(v,position);
            }
        });

    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView tv_address;
        private TextView tv_title;
        private TextView tv_time;
        public MyHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_address = (TextView) itemView.findViewById(R.id.tv_add);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

}
