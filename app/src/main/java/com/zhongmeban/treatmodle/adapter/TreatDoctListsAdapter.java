package com.zhongmeban.treatmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.bean.treatbean.TreatDoctor;
import com.zhongmeban.utils.CircleTransform;
import com.zhongmeban.utils.ImageLoaderZMB;
import java.util.ArrayList;
import java.util.List;
import me.next.tagview.TagCloudView;

/**
 * Description:医师列表Activity对应RecyclerView Adapter 1.0.1版本更改
 * Created by Chengbin He on 2016/3/30.
 */
public class TreatDoctListsAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<TreatDoctor> list;

    public TreatDoctListsAdapter(Context mContext, List<TreatDoctor> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<TreatDoctor> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
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
                v = LayoutInflater.from(mContext).inflate(R.layout.item_doctor, parent, false);
                holder = new MyHolder(v);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.tv_name.setText(list.get(position).getDoctorName());
            myHolder.tv_address.setText(list.get(position).getHospitalName());
            myHolder.tv_des.setText(getDoctLevel(position));

            if (list.get(position).getDiseaseNames() != null) {
                if (list.get(position).getDiseaseNames().size() > 3) {
                    List<String> nameList = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        nameList.add(list.get(position).getDiseaseNames().get(i));
                    }
                    nameList.add("...");
                    myHolder.tag_cloud_view.setTags(nameList);
                } else {
                    myHolder.tag_cloud_view.setTags(list.get(position).getDiseaseNames());
                }
            }

//            if (list.get(position).getAvatar()!= null){
            String url = list.get(position).getAvatar();
            ImageLoaderZMB.getInstance()
                    .loadTransImage(mContext,url,R.drawable.doc_list_small,
                            new CircleTransform(),myHolder.iv_avatar);
//            if (TextUtils.isEmpty(url)) {
//                url = null;
//            }
//            Picasso.with(mContext).load(url).placeholder(R.drawable.doc_list_small).error(R.drawable.doc_list_small)
//                    .transform(new CircleTransform()).into(myHolder.iv_avatar);
//            }
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClick.onItemClick(v, position);
                }
            });
        }
    }

    public String getDoctLevel(int position) {
        String level = "";
        switch (list.get(position).getTitle()) {
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
                level = "";
                break;
            default:
                level = "";
                break;
        }
        return level;
    }

    public String getIcon(int position) {
        String iconUrl = list.get(position).getAvatar();
        return iconUrl;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_des;
        public TextView tv_address;
        public TextView tv_doct_evaluate;
        public ImageView iv_avatar;
        public TagCloudView tag_cloud_view;

        public MyHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_des = (TextView) itemView.findViewById(R.id.tv_des);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_doct_evaluate = (TextView) itemView.findViewById(R.id.tv_doct_evaluate);
            tag_cloud_view = (TagCloudView) itemView.findViewById(R.id.tag_view);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    class LoadHolder extends RecyclerView.ViewHolder {

        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

}
