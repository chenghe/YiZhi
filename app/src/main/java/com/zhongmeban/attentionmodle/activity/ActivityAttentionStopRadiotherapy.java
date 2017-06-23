package com.zhongmeban.attentionmodle.activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.StopRadiotherapyBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 停止放疗记录
 * Created by HeCheng on 2016/10/3.
 */

public class ActivityAttentionStopRadiotherapy extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlEndTime;//结束时间
    private TextView tvEndTime;//结束时间
    private RelativeLayout rlStopReason;//选择终止放疗 原因
    private TextView tvFinish;//疗程结束
    private TextView tvStop;//终止放疗
    private TextView tvOperation;//准备手术
    private TextView tvTolerance;//耐受性原因
    private TextView tvOther;//其他原因
    private TextView tvName;//主标题
    private TextView tvSate;
    private EditText etNotes;
    private MaterialDialog progressDialog;
    private Button btSave;
    private long endTime;
    private long startTime;
    private String patientId;
    private String token;
    private String titleName;
    private String notes = "";//描述
    private int radiotherapyId;
    private int status;//1 正在进行 2 停止 3 暂停
    private int endReason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_stop_radiotherapy);

        SharedPreferences userInfoSP = getSharedPreferences("userInfo",MODE_PRIVATE);
        token = userInfoSP.getString("token","");
        patientId = userInfoSP.getString("patientId","");

        Intent intent = getIntent();
        radiotherapyId = intent.getIntExtra("radiotherapyId",0);
        status = intent.getIntExtra("status",0);
        titleName = intent.getStringExtra("titleName");
        startTime = intent.getLongExtra("startTime",0);

        initTitle();
        iniView();
    }

    private void iniView() {
        tvSate = (TextView) findViewById(R.id.tv_chemotherapy_state);
        etNotes = (EditText) findViewById(R.id.et_question);
        rlEndTime = (RelativeLayout)findViewById(R.id.rl_date_out);
        rlEndTime.setOnClickListener(this);
        tvEndTime = (TextView) findViewById(R.id.tv_date_out);
        btSave = (Button) findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);
        rlStopReason = (RelativeLayout) findViewById(R.id.rl_stop_reason);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);

        tvStop = (TextView) findViewById(R.id.tv_stop);
        tvStop.setOnClickListener(this);
        buttonClickable();

        tvTolerance = (TextView) findViewById(R.id.tv_tolerance);
        tvTolerance.setOnClickListener(this);

        tvOther = (TextView) findViewById(R.id.tv_other);
        tvOther.setOnClickListener(this);

        tvOperation = (TextView) findViewById(R.id.tv_operation);
        tvOperation.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.tv_chemotherapy_name);
        String statusName = "";
        switch (status){
            case 1:
                statusName = "进行中";
                break;
            case 2:
                statusName = "已终止";
                break;
            case 3:
                statusName = "已结束";
                break;
        }
        tvName.setText("第"+titleName+"疗程放疗 ");
        tvSate.setText(statusName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save:
                progressDialog = showProgressDialog("正在上传数据请稍等",ActivityAttentionStopRadiotherapy.this);
                notes = etNotes.getText().toString();
                StopRadiotherapyBody body = new StopRadiotherapyBody();
                body.setEndTime(endTime);
                body.setEndReason(endReason);
                body.setId(radiotherapyId);
                body.setPatientId(patientId);
                body.setStatus(status);
                body.setNotes(notes);
                body.setEndReason(endReason);
                stopRadiotherapy(body,token);
                break;
            case R.id.rl_date_out://结束日期
                showDatePicker(ActivityAttentionStopRadiotherapy.this,onEndDatePickedListener,getTodayData());
                break;
            case R.id.tv_finish://疗程结束
                status = 2;
                endReason = 0;
                endRadiotherapyUI(2);
                rlStopReason.setVisibility(View.GONE);
                endRaiotherapyReason(0);
                break;
            case R.id.tv_stop://终止放疗
                status = 3;
                endRadiotherapyUI(3);
                rlStopReason.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_operation://准备手术
                endReason = 1;
                endRaiotherapyReason(1);
                break;
            case R.id.tv_tolerance://耐受性原因
                endReason = 2;
                endRaiotherapyReason(2);
                break;
            case R.id.tv_other://其他原因
                endReason = 3;
                endRaiotherapyReason(3);
                break;
        }
    }


    //结束时间Dialog监听
    OnDatePickedListener onEndDatePickedListener = new OnDatePickedListener() {
        @Override
        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
            if (changeDateToLong(dateDesc)<startTime){
                ToastUtil.showText(ActivityAttentionStopRadiotherapy.this,"结束时间必须小于开始时间，请重新选择");
            }else {
                tvEndTime.setText(year+"年"+month+"月"+day+"日");
                endTime = changeDateToLong(dateDesc);
                buttonClickable();
            }

        }
    };

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("终止放疗");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void buttonClickable(){
        if (endTime>0&&status>0){
            btSave.setEnabled(true);
            btSave.setTextColor(getResources().getColor(R.color.white));
        }else {
            btSave.setEnabled(false);
            btSave.setTextColor(getResources().getColor(R.color.content_two));
        }
    }
    /**
     * 结束放疗
     */
    private void endRadiotherapyUI(int type){
        switch (type){
            case 2:
                tvFinish.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvFinish.setTextColor(getResources().getColor(R.color.white));
                tvStop.setBackgroundResource(R.drawable.bg_box);
                tvStop.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 3:
                tvFinish.setBackgroundResource(R.drawable.bg_box);
                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
                tvStop.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvStop.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                tvFinish.setBackgroundResource(R.drawable.bg_box);
                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
                tvStop.setBackgroundResource(R.drawable.bg_box);
                tvStop.setTextColor(getResources().getColor(R.color.content_two));
                break;
        }
        buttonClickable();
    }


    /**
     *终止放疗原因
     */
    private void endRaiotherapyReason(int type){
        switch (type){
            case 1:
                tvOperation.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvOperation.setTextColor(getResources().getColor(R.color.white));
                tvTolerance.setBackgroundResource(R.drawable.bg_box);
                tvTolerance.setTextColor(getResources().getColor(R.color.content_two));
                tvOther.setBackgroundResource(R.drawable.bg_box);
                tvOther.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 2:
                tvOperation.setBackgroundResource(R.drawable.bg_box);
                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
                tvTolerance.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvTolerance.setTextColor(getResources().getColor(R.color.white));
                tvOther.setBackgroundResource(R.drawable.bg_box);
                tvOther.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 3:
                tvOperation.setBackgroundResource(R.drawable.bg_box);
                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
                tvTolerance.setBackgroundResource(R.drawable.bg_box);
                tvTolerance.setTextColor(getResources().getColor(R.color.content_two));
                tvOther.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvOther.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                tvOperation.setBackgroundResource(R.drawable.bg_box);
                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
                tvTolerance.setBackgroundResource(R.drawable.bg_box);
                tvTolerance.setTextColor(getResources().getColor(R.color.content_two));
                tvOther.setBackgroundResource(R.drawable.bg_box);
                tvOther.setTextColor(getResources().getColor(R.color.content_two));
                break;
        }
    }

    /**
     * 停止放疗
     * @param body
     * @param token
     */
    private void stopRadiotherapy(StopRadiotherapyBody body ,String token){
        HttpService.getHttpService().postStopRadiotherapy(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","stopRadiotherapy  onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","stopRadiotherapy  onError");
                        Log.i("hcb","e" + e);
                        progressDialog.dismiss();
                        Toast.makeText(ActivityAttentionStopRadiotherapy.this,"上传出错，请重新上传",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", "stopRadiotherapy  onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    setResult(200);
                                    finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }
}
