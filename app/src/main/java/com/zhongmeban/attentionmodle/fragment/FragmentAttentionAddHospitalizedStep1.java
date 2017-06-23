//package com.zhongmeban.attentionmodle.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddHospitalized;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionSimpleDoctor;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionSimpleHospital;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.utils.ToastUtil;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//
///**
// * 添加入院第一步
// * Created by Chengbin He on 2016/7/5.
// */
//public class FragmentAttentionAddHospitalizedStep1 extends BaseFragment implements View.OnClickListener{
//
//    private ActivityAttentionAddHospitalized parentActivity;
//    private Button button;
//    private RelativeLayout rlDateIn;
//    private RelativeLayout rlDateOut;
//    private RelativeLayout rlHosp;
//    private RelativeLayout rlDoct;
//    private TextView tvDateIn;
//    private TextView tvDateOut;
//    private TextView tvHosp;
//    private TextView tvDoct;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionAddHospitalized) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_attention_add_hospitalized_step1,container,false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        initView(view);
//    }
//
//    private void initView(View view) {
//        button = (Button) view.findViewById(R.id.bt_next);
//        button.setOnClickListener(this);
//
//        rlDateIn = (RelativeLayout) view.findViewById(R.id.rl_date_in);
//        rlDateIn.setOnClickListener(this);
//        rlDateOut = (RelativeLayout) view.findViewById(R.id.rl_date_out);
//        rlDateOut.setOnClickListener(this);
//        rlHosp = (RelativeLayout) view.findViewById(R.id.rl_hospital);
//        rlHosp.setOnClickListener(this);
//        rlDoct = (RelativeLayout) view.findViewById(R.id.rl_doctor);
//        rlDoct.setOnClickListener(this);
//
//        tvDateIn = (TextView) view.findViewById(R.id.tv_date_in);
//        if (parentActivity.inTime>0){
//            tvDateIn.setText(changeDateToString(parentActivity.inTime));
//        }
//        tvDateOut = (TextView) view.findViewById(R.id.tv_date_out);
//        if (parentActivity.outTime>0){
//            tvDateOut.setText(changeDateToString(parentActivity.outTime));
//        }
//        tvHosp = (TextView) view.findViewById(R.id.tv_hospital);
//        if (parentActivity.hospId>0){
//            tvHosp.setText(parentActivity.hospName);
//        }
//        tvDoct = (TextView) view.findViewById(R.id.tv_doctor);
//        if (parentActivity.docId>0){
//            tvDoct.setText(parentActivity.docName);
//        }
//        if (parentActivity.ISEDIT){
//            tvDateIn.setText(changeDateToString(parentActivity.inTime));
//            tvDoct.setText(parentActivity.docName);
//            tvHosp.setText(parentActivity.hospName);
//            if (parentActivity.outTime>0){
//                tvDateOut.setText(changeDateToString(parentActivity.outTime));
//            }
//        }
//
//
//       checkButtonClickAble();
//    }
//
//    /**
//     * 入院日期datepicker点击回调
//     */
//    OnDatePickedListener inTimeDatePickedListener =  new OnDatePickedListener(){
//
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            tvDateIn.setText(year+"年"+month+"月"+day+"日");
//            parentActivity.inTime = changeDateToLong(dateDesc);
//            checkButtonClickAble();
//        }
//    };
//
//    /**
//     * 出院日期datepicker点击回调
//     */
//    OnDatePickedListener outTimeDatePickedListener =  new OnDatePickedListener(){
//
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            if (parentActivity.inTime>0){
//                if (parentActivity.inTime<changeDateToLong(dateDesc)){
//                    tvDateOut.setText(year+"年"+month+"月"+day+"日");
//                    parentActivity.outTime = changeDateToLong(dateDesc);
//                }else {
//                    ToastUtil.showText(parentActivity,"入院时间小于出院时间请重新输入");
//                }
//            }else {
//                ToastUtil.showText(parentActivity,"请先输入入院时间");
//            }
//
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()){
//            case R.id.bt_next://下一步按钮
//                parentActivity.notifyFragment(2);
//                break;
//            case R.id.rl_date_in://入院时间
//                showDatePicker(parentActivity,inTimeDatePickedListener,getTodayData());
//                break;
//            case R.id.rl_date_out://出院时间
//                showDatePicker(parentActivity,outTimeDatePickedListener,getTodayData());
//                break;
//            case R.id.rl_hospital://医生筛选
//                Intent intent = new Intent(parentActivity, ActivityAttentionSimpleHospital.class);
//                startActivityForResult(intent, 1);
//
//                break;
//            case R.id.rl_doctor://医院筛选
//                Intent intentDoctor = new Intent(parentActivity, ActivityAttentionSimpleDoctor.class);
//                startActivityForResult(intentDoctor, 1);
//
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 200) {//医院选择回调
//            parentActivity.hospName = data.getStringExtra("attentionMarkerHospName");
//            parentActivity.hospId = data.getIntExtra("attentionMarkerHospId",0);
//            tvHosp.setText(parentActivity.hospName);
//            checkButtonClickAble();
//
//        }else if (resultCode == 300){//医生选择回调
//            parentActivity.docName = data.getStringExtra("attentionDoctorName");
//            parentActivity.docId = data.getIntExtra("attentionDoctorId",0);
//            Log.i("hcb","docId " +parentActivity.docId);
//            tvDoct.setText(parentActivity.docName);
//            checkButtonClickAble();
//        }
//    }
//
//
//    private void checkButtonClickAble(){
//        if (parentActivity.inTime>0&&parentActivity.hospId>0&&parentActivity.docId>0){
//            button.setEnabled(true);
//            button.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            button.setEnabled(false);
//            button.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//}
