package com.zhongmeban.mymodle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.NewsLists;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.SmoothCheckBox;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.ImageUrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import me.next.tagview.TagCloudView;

/**
 * 收藏的信息
 * Created by Chengbin He on 2016/11/8.
 */

public class FavoriteInfoListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<NewsLists.NewsItem> list;
    private Set<String> ChooseSet = new HashSet<>();
    private boolean isChoose = false;//是否为筛选模式

    public FavoriteInfoListAdapter(Context mContext, List<NewsLists.NewsItem> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setChoose(boolean isChoose) {
        this.isChoose = isChoose;
        if(!isChoose){
            ChooseSet.clear();
        }

        notifyDataSetChanged();
    }

    public List getChooseList(){
        List<Integer> list = new ArrayList<>();
        for (String mId: ChooseSet){
            int id = Integer.valueOf(mId);
            list.add(id);
        }
        return list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.favorite_info_item, parent, false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        myHolder.title.setText(list.get(position).getTitle());
        myHolder.author.setText(list.get(position).getWriter());
        myHolder.time.setText(TimeUtils.changeDateToString(list.get(position).getTime()));

        if (isChoose) {
            myHolder.checkBox.setChecked(false);
            myHolder.checkBox.setVisibility(View.VISIBLE);
        } else {
            myHolder.checkBox.setChecked(false);
            myHolder.checkBox.setVisibility(View.GONE);
        }

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChoose){
                    if (myHolder.checkBox.isChecked()){
                        myHolder.checkBox.setChecked(false);
                        Log.i("hcbtest","ChooseSet.remove(list.get(position).getInformationId())" +list.get(position).getId());
                        ChooseSet.remove(list.get(position).getId());
                    }else {
                        myHolder.checkBox.setChecked(true);
                        Log.i("hcbtest","ChooseSet.add(list.get(position).getInformationId())" +list.get(position).getId());
                        ChooseSet.add(list.get(position).getId());
                    }
                }else {
                    mClick.onItemClick(v, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView time;
        public TextView author;
        public SmoothCheckBox checkBox;

        public MyHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_name);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            checkBox = (SmoothCheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
