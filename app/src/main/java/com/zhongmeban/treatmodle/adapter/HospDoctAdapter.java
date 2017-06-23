package com.zhongmeban.treatmodle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.bean.Doctor;
import com.zhongmeban.utils.CircleTransform;

import com.zhongmeban.utils.genericity.ImageUrl;
import java.util.List;

import me.next.tagview.TagCloudView;

/**
 * @Description:医院列表中医生ListView Adapter
 * Created by Chengbin He on 2016/4/5.
 */
public class HospDoctAdapter extends BaseAdapter{
    public String imageUrl = ImageUrl.BaseImageUrl;
    private Context mContext;
    private List<Doctor> list;

    public HospDoctAdapter(Context mContext,List<Doctor> list){
        this.mContext = mContext;
    }

    public void notifyData(List<Doctor> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyViewHolder holder = null;
        if (holder == null){
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_doctor,parent,false);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_doct_evaluate = (TextView) convertView.findViewById(R.id.tv_doct_evaluate);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tag_cloud_view = (TagCloudView) convertView.findViewById(R.id.tag_view);
            holder.line =  convertView.findViewById(R.id.line);        }else{
            holder = (MyViewHolder) convertView.getTag();
        }
        initData(holder,position);
        return convertView;
    }

    private void initData(MyViewHolder holder,int position) {
        holder.tv_name.setText(list.get(position).getDoctorName());
        holder.tv_address.setText(list.get(position).getHospitalName());
        holder.tv_doct_evaluate.setText(list.get(position).getEvaluate());
        holder.tv_des.setText(getDoctLevel(position));
        holder.line.setVisibility(View.GONE);

        if(list.get(position).getDiseaseNames() != null){
            holder.tag_cloud_view.setTags(list.get(position).getDiseaseNames());
        }

        if (list.get(position).getAvatar()!= null){
            String url = list.get(position).getAvatar();
            Log.i("hcb", "url is " + imageUrl + url);
            Picasso.with(mContext).load(imageUrl+url).placeholder(R.drawable.doc_list_small).error(R.drawable.doc_list_small)
                    .transform(new CircleTransform()).into(holder.iv_avatar);
        }
    }

    public String getDoctLevel(int position){
        String level = "";
        switch (list.get(position).getTitle()){
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

    public String getIcon(int position){
        String iconUrl = imageUrl+list.get(position).getAvatar();
        return iconUrl;
    }
    class MyViewHolder{
        public TextView tv_name;
        public TextView tv_des;
        public TextView tv_address;
        public TextView tv_doct_evaluate;
        public ImageView iv_avatar;
        public View line;
        public me.next.tagview.TagCloudView tag_cloud_view;
    }

}
