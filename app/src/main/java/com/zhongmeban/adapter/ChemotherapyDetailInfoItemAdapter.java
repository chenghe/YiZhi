//package com.zhongmeban.adapter;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.zhongmeban.R;
//import com.zhongmeban.bean.ChemotherapyDetailInfoBean;
//import com.zhongmeban.utils.AttentionUtils;
//import com.zhongmeban.utils.TimeUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.dao.attention.Chemotherapy;
//import de.greenrobot.dao.attention.ChemotherapyMedicine;
//
///**
// * Created by Chengbin He on 2016/10/13.
// */
//
//public class ChemotherapyDetailInfoItemAdapter extends BaseAdapter{
//
//    private Context mContext;
//    private Chemotherapy chemotherapy;
//    private List<ChemotherapyMedicine> dbChemotherapyMedicine;
//    private List<ChemotherapyDetailInfoBean> statusList = new ArrayList<>();
//
//    public ChemotherapyDetailInfoItemAdapter(Context mContext, Chemotherapy chemotherapy,List<ChemotherapyMedicine> dbChemotherapyMedicine) {
//        this.mContext = mContext;
//        this.chemotherapy = chemotherapy;
//        this.dbChemotherapyMedicine = dbChemotherapyMedicine;
//        initList(chemotherapy);
//
//    }
//
//    private void initList(Chemotherapy chemotherapy) {
//        String startTime = TimeUtils.changeDateToString(chemotherapy.getStartTime());
//        statusList.add(new ChemotherapyDetailInfoBean("开始时间：",startTime));
//        if (chemotherapy.getEndTime()>0){
//            String endTime = TimeUtils.changeDateToString(chemotherapy.getEndTime());
//            statusList.add(new ChemotherapyDetailInfoBean("结束时间：",endTime));
//        }
//        String chemotherapyPurposeName  = AttentionUtils.getChemotherapyPurposeName(chemotherapy.getChemotherapyAim());
//        statusList.add(new ChemotherapyDetailInfoBean("化疗目的：",chemotherapyPurposeName));
//
//        String takeMedicine = AttentionUtils.getChemotherapyMedicineName(dbChemotherapyMedicine);
//        statusList.add(new ChemotherapyDetailInfoBean("化疗用药：",takeMedicine));
//        if (chemotherapy.getCourseCount()>0){
//            String courseCount = chemotherapy.getCourseCount() +" 周期";
//            statusList.add(new ChemotherapyDetailInfoBean("预计周期：",courseCount));
//        }
//        if (chemotherapy.getDayOfCourse()>0){
//            String cayOfCourse = chemotherapy.getDayOfCourse() +" 天";
//            statusList.add(new ChemotherapyDetailInfoBean("用药天数：",cayOfCourse));
//        }
//        if (chemotherapy.getCourseInterval()>0){
//            String courseInterval = chemotherapy.getCourseInterval() +" 天";
//            statusList.add(new ChemotherapyDetailInfoBean("休息天数：",courseInterval));
//        }
//        if (chemotherapy.getDescription()!=null&& !TextUtils.isEmpty(chemotherapy.getDescription())){
//            String notes = chemotherapy.getDescription();
//            statusList.add(new ChemotherapyDetailInfoBean("备注信息：",notes));
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return statusList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View  view = LayoutInflater.from(mContext).inflate(R.layout.item_chemotherapy_info,parent,false);
//        TextView tvName = (TextView) view.findViewById(R.id.tv_title_name);
//        tvName.setText(statusList.get(position).getName());
//        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
//        tvContent.setText(statusList.get(position).getContent());
//        return view;
//    }
//}
