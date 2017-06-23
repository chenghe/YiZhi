package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.RecordlistBean;
import com.zhongmeban.utils.TimeUtils;

import java.util.List;

/**
 * Created by Chengbin He on 2016/6/27.
 */
public class AttentionInfoTimeAdapter extends BaseAdapter {

    public final static int NOTYPE = 0;
    public final static int MEDICINE_RECORD = 20;//用药记录
    public final static int HOSPITAL_RECORD = 30;//住院记录
    public final static int SURGERY_RECORD = 40;//手术记录
    public final static int INDEX_RECORD = 10;//标志物记录
    public final static int RADIOTHERAPY_RECORD = 60;//放疗记录
    public final static int RADIOTHERAPY_SUSPENDED_RECORD = 61;//放疗疗程记录
    public final static int CHEMOTHERAPY_RECORD = 50;//化疗记录
    public final static int CHEMOTHERAPY_COURSE_RECORD = 51;//化疗疗程记录


    private Context mContext;
    private List<RecordlistBean.SourceItems> list;

    public AttentionInfoTimeAdapter(Context mContext,List<RecordlistBean.SourceItems> list){
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list == null ? 0:list.size();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_attention_info_time,parent,false);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvTime = (TextView) view.findViewById(R.id.tv_time);//开始 结束 时间
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_sub_title);//备注
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//内容
        TextView tvContext = (TextView) view.findViewById(R.id.tv_context);//剂量
        View leftLine = view.findViewById(R.id.line);

        int type = list.get(position).getType();
        String time1 = TimeUtils.changeDateToString(list.get(position).getTime1());
        String time2 = "至今";
        if (list.get(position).getTime2()>0){
            time2 = TimeUtils.changeDateToString(list.get(position).getTime2());
        }
        String subTitle = list.get(position).getSubTitle();
        String context = list.get(position).getContext();
        String content = list.get(position).getContent();

        switch (type){
            case MEDICINE_RECORD://用药记录
                tvTime.setText("用药记录："+time1+"-"+time2);
                tvSubTitle.setVisibility(View.GONE);
                ivIcon.setImageResource(R.drawable.pil_attention_type);
                tvContent.setText(content);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_pill_color));
                break;
            case HOSPITAL_RECORD://入院记录
                tvTime.setText("入院记录："+time1+"-"+time2);
                if (subTitle!=null){
                    tvSubTitle.setText("入院备注："+subTitle);
                }else {
                    tvSubTitle.setText("-");
                }
                ivIcon.setImageResource(R.drawable.hospital_attention_type);
                tvContent.setText(content);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_blue));
                break;
            case SURGERY_RECORD://手术记录
                tvTime.setText("手术记录："+time1+"-"+time2);
                tvSubTitle.setVisibility(View.GONE);
                tvContent.setText(content);
                ivIcon.setImageResource(R.drawable.lab_attention_type);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_blue));
                break;
            case INDEX_RECORD://标志物记录
                tvTime.setText("标志物记录："+time1);
                tvContent.setText(content);
                if (context!=null){
                    tvContext.setText(context);
                }
                tvSubTitle.setVisibility(View.GONE);
                ivIcon.setImageResource(R.drawable.landmark_attention_type);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.app_green));
                break;
            case RADIOTHERAPY_RECORD://放疗记录
                tvTime.setText("放疗记录："+time1);
                tvContent.setText(content);
                tvContext.setText(context);
                tvSubTitle.setVisibility(View.GONE);
                ivIcon.setImageResource(R.drawable.radiotherapy_attention_type);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_radiotherapy_color));
                break;
            case RADIOTHERAPY_SUSPENDED_RECORD://放疗暂停记录
                tvTime.setText("放疗暂停记录："+time1+"-"+time2);
                tvContent.setText(content);
                tvSubTitle.setVisibility(View.GONE);
                ivIcon.setImageResource(R.drawable.radiotherapy_attention_type);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_radiotherapy_color));
                break;
            case CHEMOTHERAPY_RECORD://化疗记录
                tvTime.setText("化疗记录："+time1+"-"+time2);
                tvContent.setText(content);
                tvSubTitle.setVisibility(View.GONE);
                ivIcon.setImageResource(R.drawable.chemotherapy_attention_type);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_chemotherapy_color));
                break;
            case CHEMOTHERAPY_COURSE_RECORD://化疗疗程记录
                tvTime.setText("化疗疗程记录："+time1+"-"+time2);
                tvContent.setText(content);
                tvSubTitle.setVisibility(View.GONE);
                ivIcon.setImageResource(R.drawable.chemotherapy_attention_type);
                leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.attention_chemotherapy_color));
                break;
            case NOTYPE:
                break;

        }


        return view;
    }
}
