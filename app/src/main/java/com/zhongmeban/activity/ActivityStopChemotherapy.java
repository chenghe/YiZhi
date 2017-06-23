//package com.zhongmeban.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
//import com.zhongmeban.base.BaseActivity;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.StopChemotherapyBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.AttentionUtils;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.utils.ToastUtil;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//
//import de.greenrobot.dao.attention.Chemotherapy;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 停止正在进行的放疗
// * Created by Chengbin He on 2016/10/18.
// */
//
//public class ActivityStopChemotherapy extends BaseActivity implements View.OnClickListener{
//
//    private Activity mContext = ActivityStopChemotherapy.this;
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
//    private String token;
//    private String chemotherapyAim = "";
//    private int chemotherapyId;
//    private int endReason;
//    private long endTime;
//    private long startTime;
//    private int times;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_attention_stop_chemotherapy);
//        Intent intent = getIntent();
//        Chemotherapy chemotherapy = (Chemotherapy) intent.getSerializableExtra("chemotherapy");
//        chemotherapyId = (int) chemotherapy.getId();
//        startTime = chemotherapy.getStartTime();
//        times = chemotherapy.getTimes();
//        token = intent.getStringExtra("token");
//        chemotherapyAim = AttentionUtils.getChemotherapyPurposeName(chemotherapy.getChemotherapyAim());
//        initTitle();
//        initView();
//    }
//
//    @Override
//    protected void initTitle() {
//        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
//
//        title.slideCentertext("终止化疗");
//        title.backSlid(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//
//    private void initView() {
//
//        etNotes = (EditText) findViewById(R.id.et_question);
//        tvName = (TextView) findViewById(R.id.tv_chemotherapy_name);
//        tvName.setText(chemotherapyAim);
//        tvStatus = (TextView) findViewById(R.id.tv_chemotherapy_state);
//        tvStatus.setText("进行中");
//        tvFinish = (TextView) findViewById(R.id.tv_finish);
//        tvFinish.setOnClickListener(this);
//        tvStop = (TextView) findViewById(R.id.tv_stop);
//        tvStop.setOnClickListener(this);
//        tvChange = (TextView) findViewById(R.id.tv_change);
//        tvChange.setOnClickListener(this);
//
//        rlEndTime = (RelativeLayout) findViewById(R.id.rl_date_out);
//        rlEndTime.setOnClickListener(this);
//        tvEndTime = (TextView) findViewById(R.id.tv_date_out);
//        btSave = (Button) findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        buttonClickable();
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
//                endReason = 1;
//                purpose(1);
//                buttonClickable();
//                break;
//            case R.id.tv_stop:
//                purpose(2);
//                endReason = 2;
//                buttonClickable();
//                break;
//            case R.id.tv_change:
//                endReason = 3;
//                purpose(3);
//                buttonClickable();
//                break;
//            case R.id.bt_save:
//                progressDialog = showProgressDialog("正在上传数据，请稍后",mContext);
//                stopChemotherapy();
//                break;
//            case R.id.rl_date_out:
//                showDatePicker( mContext,onEndDatePickedListener,getTodayData());
//                break;
//        }
//    }
//
//    //结束时间Dialog监听
//    OnDatePickedListener onEndDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//
//            if (startTime<changeDateToLong(dateDesc)){
//                tvEndTime.setText(year+"年"+month+"月"+day+"日");
//                endTime = changeDateToLong(dateDesc);
//            }else {
//                ToastUtil.showText(mContext,"开始日期必须小于结束日期，请重新输入");
//            }
//            buttonClickable();
//        }
//    };
//
//    private void buttonClickable(){
//        if (endTime>0&&endReason>0){
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
//        body.setEndTime(endTime);
//        body.setEndReason(endReason);
//        body.setRecordId( chemotherapyId);
//
//        HttpService.getHttpService().postUpdateChemotherapyRecordEnd(body,token)
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
//                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
//                                    setResult(200, intent);
//                                    finish();
//                                }
//                            },1000);
//                        }
//                    }
//                });
//    }
//}
