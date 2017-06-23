package com.zhongmeban.treatmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityHospLevel;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.utils.ImageLoaderZMB;
import java.util.List;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Description:医院列表Activity对应RecyclerView Adapter 1.0.1
 * Created by Chengbin He on 2016/11/30.
 */
public class TreatHospListsAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<TreatHospitalScource> list;


    public TreatHospListsAdapter(Context mContext, List<TreatHospitalScource> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<TreatHospitalScource> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        switch (viewType) {
            case 0:
                //上拉刷新
                v = LayoutInflater.from(mContext).inflate(R.layout.load_more_progress, parent, false);
                holder = new LoadHolder(v);
                break;
            case 1:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_hospital, parent, false);
                holder = new MyHolder(v);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            MyHolder mHolder = (MyHolder) holder;
            mHolder.tv_address.setText(list.get(position).getAddress());
            mHolder.tv_hospital_name.setText(list.get(position).getName());
            mHolder.tv_hospital_level.setText(getLevel(position));


            String url = list.get(position).getPicture();
            Log.i("hcb", "name is " + list.get(position).getName());

            Log.i("hcb", "url is " + url);
            ImageLoaderZMB.getInstance()
                    .loadTransImage(mContext,url,R.drawable.image_hospital,
                            new RoundedCornersTransformation(15, 0, RoundedCornersTransformation.CornerType.ALL),
                            mHolder.image_hosp);
//            if (TextUtils.isEmpty(url)) {
//                url = null;
//            }
//            Picasso.with(mContext).load(url).transform(new RoundedCornersTransformation(15, 0,
//                    RoundedCornersTransformation.CornerType.ALL))
//                    .placeholder(R.drawable.image_hospital).error(R.drawable.image_hospital)
//                    .into(mHolder.image_hosp);


            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(v, position);
                }
            });
        }
    }

    public String getLevel(int position) {
        String level = "";
        switch (list.get(position).getLevel()) {
            case 0:
                level = "无级别";
                break;
            case ActivityHospLevel.One:
                level = "一级";
                break;
            case ActivityHospLevel.OneOne:
                level = "一级甲等";
                break;
            case ActivityHospLevel.Two:
                level = "二级";
                break;
            case ActivityHospLevel.TwoOne:
                level = "二级甲等";
                break;
            case ActivityHospLevel.TwoTwo:
                level = "二级乙等";
                break;
            case ActivityHospLevel.Three:
                level = "三级";
                break;
            case ActivityHospLevel.ThreeOne:
                level = "三级甲等";
                break;
            case ActivityHospLevel.ThreeTwo:
                level = "三级乙等";
                break;
            default:
                level = "无级别";
                break;
        }
        return level;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_hospital_level;
        public TextView tv_address;
        public TextView tv_hospital_name;
        public ImageView image_hosp;

        public MyHolder(View itemView) {
            super(itemView);
            tv_hospital_level = (TextView) itemView.findViewById(R.id.tv_hospital_level);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_hospital_name = (TextView) itemView.findViewById(R.id.tv_hospital_name);
            image_hosp = (ImageView) itemView.findViewById(R.id.image_hosp);
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder {

        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

}
