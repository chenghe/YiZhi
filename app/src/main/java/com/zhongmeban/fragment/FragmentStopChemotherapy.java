//package com.zhongmeban.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionChemotherapyDetail;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.StopChemotherapyBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.utils.ToastUtil;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 结束本次化疗
// * Created by Chengbin He on 2016/8/30.
// */
//public class FragmentStopChemotherapy extends BaseFragment implements  View.OnClickListener{
//
//    private ActivityAttentionChemotherapyDetail parentActivity;
//    private RelativeLayout rlEndTime;
//    private TextView tvEndTime;
//    private TextView tvFinish;
//    private TextView tvStop;
//    private TextView tvChange;
//    private TextView tvName;
//    private TextView tvStatus;
//    private Button btSave;
//    private EditText etNotes;
//    private MaterialDialog progressDialog;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionChemotherapyDetail) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_attention_stop_chemotherapy,container,false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initTitle(view);
//
//        etNotes = (EditText) view.findViewById(R.id.et_question);
//        tvName = (TextView) view.findViewById(R.id.tv_chemotherapy_name);
//        tvName.setText(parentActivity.chemotherapyAimName);
//        tvStatus = (TextView) view.findViewById(R.id.tv_chemotherapy_state);
//        tvStatus.setText(parentActivity.statusName);
//        tvFinish = (TextView) view.findViewById(R.id.tv_finish);
//        tvFinish.setOnClickListener(this);
//        tvStop = (TextView) view.findViewById(R.id.tv_stop);
//        tvStop.setOnClickListener(this);
//        tvChange = (TextView) view.findViewById(R.id.tv_change);
//        tvChange.setOnClickListener(this);
//
//        rlEndTime = (RelativeLayout) view.findViewById(R.id.rl_date_out);
//        rlEndTime.setOnClickListener(this);
//        tvEndTime = (TextView) view.findViewById(R.id.tv_date_out);
//        btSave = (Button) view.findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        buttonClickable();
//    }
//
//    protected void initTitle(View view) {
//        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//
//        title.slideCentertext("终止化疗");
//        title.backSlid(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//    }
//
//    /**
//     * 终止化疗原因
//     *
//     * @param type 1.疗程结束 2.终止化疗 3.更换方案
//     */
//    private void purpose(int type) {
//        switch (type) {
//            case 1:
//                tvFinish.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvFinish.setTextColor(getResources().getColor(R.color.white));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                tvChange.setBackgroundResource(R.drawable.bg_box);
//                tvChange.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 2:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvStop.setTextColor(getResources().getColor(R.color.white));
//                tvChange.setBackgroundResource(R.drawable.bg_box);
//                tvChange.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 3:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                tvChange.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvChange.setTextColor(getResources().getColor(R.color.white));
//                break;
//
//            default:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                tvChange.setBackgroundResource(R.drawable.bg_box);
//                tvChange.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_finish:
//                parentActivity.endReason = 1;
//                purpose(1);
//                buttonClickable();
//                break;
//            case R.id.tv_stop:
//                purpose(2);
//                parentActivity.endReason = 2;
//                buttonClickable();
//                break;
//            case R.id.tv_change:
//                parentActivity.endReason = 3;
//                purpose(3);
//                buttonClickable();
//                break;
//            case R.id.bt_save:
//                progressDialog = showProgressDialog("正在上传数据，请稍后",parentActivity);
//                stopChemotherapy();
//                break;
//            case R.id.rl_date_out:
//                showDatePicker(parentActivity,onEndDatePickedListener,getTodayData());
//                break;
//        }
//    }
//
//    //结束时间Dialog监听
//    OnDatePickedListener onEndDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            if (parentActivity.startTime<changeDateToLong(dateDesc)){
//                tvEndTime.setText(year+"年"+month+"月"+day+"日");
//                parentActivity.endTime = changeDateToLong(dateDesc);
//            }else {
//                ToastUtil.showText(parentActivity,"开始日期必须小于结束日期，请重新输入");
//            }
//            buttonClickable();
//        }
//    };
//
//    private void buttonClickable(){
//        if (parentActivity.endTime>0&&parentActivity.endReason>0){
//            btSave.setEnabled(true);
//            btSave.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            btSave.setEnabled(false);
//            btSave.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//
//    /**
//     * 结束本次化疗 网络请求
//     */
//    private void stopChemotherapy(){
//        StopChemotherapyBody body = new StopChemotherapyBody();
//        String notes = etNotes.getText().toString();
//        body.setNotes(notes);
//        body.setEndTime(parentActivity.endTime);
//        body.setEndReason(parentActivity.endReason);
//        body.setRecordId((int) parentActivity.chemotherapyId);
//
//        HttpService.getHttpService().postUpdateChemotherapyRecordEnd(body,parentActivity.token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","stopChemotherapy onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","stopChemotherapy onError");
//                        Log.i("hcb","e"+e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","stopChemotherapy onNext");
//                        if (createOrDeleteBean.getResult()){
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
////                                    parentActivity.notifyFragment(parentActivity.CHEMOTHERAPY);
//                                    Intent intent = new Intent(parentActivity, ActivityAttentionList.class);
//                                    parentActivity.setResult(200, intent);
//                                    parentActivity.finish();
//                                }
//                            },1000);
//                        }
//                    }
//                });
//    }
//}
