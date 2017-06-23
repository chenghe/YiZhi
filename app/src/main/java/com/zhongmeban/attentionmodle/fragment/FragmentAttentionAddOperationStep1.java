package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddOperation;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionSimpleDoctor;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionSimpleHospital;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;

/**
 * 新增手术第一步
 * Created by Chengbin He on 2016/7/5.
 */
public class FragmentAttentionAddOperationStep1 extends BaseFragment implements View.OnClickListener{

    private Button button;
    private TextView tvDate;
    private TextView tvHospital;
    private TextView tvDoctor;
    private RelativeLayout rlDate;
    private RelativeLayout rlHospital;
    private RelativeLayout rlDoctor;
    private ActivityAttentionAddOperation parentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionAddOperation) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention_add_operation_step1,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = (Button) view.findViewById(R.id.bt_next);
        button.setOnClickListener(this);
        if (parentActivity.time>0&&parentActivity.hospId>0){
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.white));
        }else {
            button.setEnabled(false);
            button.setTextColor(getResources().getColor(R.color.content_two));
        }

        tvDate = (TextView) view.findViewById(R.id.tv_date);
        rlDate = (RelativeLayout) view.findViewById(R.id.rl_date);
        rlDate.setOnClickListener(this);
        if (parentActivity.time>0){
            tvDate.setText(changeDateToString(parentActivity.time));
        }

        tvDoctor = (TextView) view.findViewById(R.id.tv_doctor);
        rlDoctor = (RelativeLayout) view.findViewById(R.id.rl_doctor);
        rlDoctor.setOnClickListener(this);
        if (!TextUtils.isEmpty(parentActivity.docName)){
            tvDoctor.setText(parentActivity.docName);
        }

        tvHospital = (TextView) view.findViewById(R.id.tv_hospital);
        rlHospital = (RelativeLayout) view.findViewById(R.id.rl_hospital);
        rlHospital.setOnClickListener(this);
        if (!TextUtils.isEmpty(parentActivity.hospName)){
            tvHospital.setText(parentActivity.hospName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next://下一步
                parentActivity.notifyFragment(2);
                break;
            case R.id.rl_date://选择时间
                showDatePicker();
                break;
            case R.id.rl_hospital://选择医院
                Intent intent = new Intent(parentActivity, ActivityAttentionSimpleHospital.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.rl_doctor://选择医生
                Intent intentDoctor = new Intent(parentActivity, ActivityAttentionSimpleDoctor.class);
                startActivityForResult(intentDoctor, 1);
                break;
        }
    }

    private void showDatePicker() {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(parentActivity, new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//                parentActivity.chooseDate = year + "年" + month + "月" + day + "日";
                tvDate.setText(year + "年" + month + "月" + day + "日");
                long time = changeDateToLong(dateDesc);
                if (parentActivity.ISFROMEHOSP){
                    //住院进入
                    if (time>=parentActivity.inTime&&parentActivity.outTime>0&&time<=parentActivity.outTime){
                        parentActivity.time = time;
                        if (parentActivity.hospId>0){
                            button.setEnabled(true);
                            button.setTextColor(getResources().getColor(R.color.white));
                        }else {
                            button.setEnabled(false);
                            button.setTextColor(getResources().getColor(R.color.content_two));
                        }
                    }else if (time>=parentActivity.inTime&&parentActivity.outTime==0){
                        parentActivity.time = time;
                        if (parentActivity.hospId>0){
                            button.setEnabled(true);
                            button.setTextColor(getResources().getColor(R.color.white));
                        }else {
                            button.setEnabled(false);
                            button.setTextColor(getResources().getColor(R.color.content_two));
                        }
                    }else {
                        ToastUtil.showText(parentActivity,"手术日期必须在住院周期内");
                        button.setEnabled(false);
                        button.setTextColor(getResources().getColor(R.color.content_two));
                    }
                }else {
                    //手术进入
                    parentActivity.time = time;
                    if (parentActivity.hospId>0){
                        button.setEnabled(true);
                        button.setTextColor(getResources().getColor(R.color.white));
                    }else {
                        button.setEnabled(false);
                        button.setTextColor(getResources().getColor(R.color.content_two));
                    }
                }

            }
        }).minYear(1990) //min year in loop
                .maxYear(2020) // max year in loop
                .dateChose(getTodayData()) // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(parentActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            parentActivity.hospName = data.getStringExtra("attentionMarkerHospName");
            parentActivity.hospId = data.getIntExtra("attentionMarkerHospId",0);
            tvHospital.setText(parentActivity.hospName);
            if (parentActivity.time>0){
                button.setEnabled(true);
                button.setTextColor(getResources().getColor(R.color.white));
            }else {
                button.setEnabled(false);
                button.setTextColor(getResources().getColor(R.color.content_two));
            }
        }else if (resultCode == 300){
            parentActivity.docName = data.getStringExtra("attentionDoctorName");
            parentActivity.docId = data.getIntExtra("attentionDoctorId",0);
            Log.i("hcb","docId " +data.getStringExtra("attentionDoctorId"));
            tvDoctor.setText(parentActivity.docName);
        }
    }

}
