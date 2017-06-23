package com.zhongmeban.attentionmodle.activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.PauseRadiotherapyBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
 * 暂停放疗
 * Created by HeCheng on 2016/10/4.
 */

public class ActivityAttentionPauseRadiotherapy extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rlPauseDate;
    private TextView tvPauseDate;
    private TextView tvBody;//身体耐受性原因
    private TextView tvOther;//其他
    private Button btSave;
    private EditText etPauseTime;
    private long pauseDate;
    private int endReason;//1 身体耐受性 2 其他
    private String pauseTime;
    private String patientId;
    private String reason;
    private String token;
    private int radiotherapyRecordId;
    private MaterialDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_pause_radiotherapy);
        Intent intent = getIntent();
        radiotherapyRecordId = intent.getIntExtra("radiotherapyId",0);

        SharedPreferences userInfoSP = getSharedPreferences("userInfo",MODE_PRIVATE);
        token = userInfoSP.getString("token","");
        patientId = userInfoSP.getString("patientId","");

        initTitle();
        initView();
    }

    private void initView(){
        rlPauseDate = (RelativeLayout) findViewById(R.id.rl_pause_date);
        rlPauseDate.setOnClickListener(this);
        tvPauseDate = (TextView) findViewById(R.id.tv_pause_date);
        etPauseTime = (EditText) findViewById(R.id.et_pause_time);
        tvBody= (TextView) findViewById(R.id.tv_body);
        tvBody.setOnClickListener(this);
        tvOther = (TextView) findViewById(R.id.tv_other);
        tvOther.setOnClickListener(this);
        btSave = (Button) findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);
        buttonClickable();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save://保存
                progressDialog = showProgressDialog("正在上传数据请稍等",ActivityAttentionPauseRadiotherapy.this);
                pauseTime = etPauseTime.getText().toString();
                if (TextUtils.isEmpty(pauseTime)){
                    ToastUtil.showText(this,"请输入暂停天数");
                }else {
                    PauseRadiotherapyBody body = new PauseRadiotherapyBody();
                    body.setPatientId(patientId);
                    body.setRadiotherapyRecordId(radiotherapyRecordId);
                    body.setReason(reason);
                    body.setStartTime(pauseDate);
                    body.setSuspendDays(pauseTime);
                    pauseHttpRadiotherapy(body,token);
                }

                break;
            case R.id.tv_body://身体耐受性原因
                reason = "1";
                endReason = 1;
                pauseReason(endReason);
                buttonClickable();
                break;
            case R.id.tv_other://其他
                reason = "2";
                endReason = 2;
                pauseReason(endReason);
                buttonClickable();
                break;
            case R.id.rl_pause_date://暂停日期
                showDatePicker(ActivityAttentionPauseRadiotherapy.this, new OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        tvPauseDate.setText(year+"年"+month+"月"+day+"日");
                        pauseDate = changeDateToLong(dateDesc);
                    }
                },getTodayData());
                break;
        }
    }


    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("暂停放疗");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buttonClickable(){
        if (pauseDate>0&&endReason>0){
            btSave.setEnabled(true);
            btSave.setTextColor(getResources().getColor(R.color.white));
        }else {
            btSave.setEnabled(false);
            btSave.setTextColor(getResources().getColor(R.color.content_two));
        }
    }

    private void pauseReason( int type){
        switch (type){
            case 1:
                tvBody.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvBody.setTextColor(getResources().getColor(R.color.white));
                tvOther.setBackgroundResource(R.drawable.bg_box);
                tvOther.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 2:
                tvBody.setBackgroundResource(R.drawable.bg_box);
                tvBody.setTextColor(getResources().getColor(R.color.content_two));
                tvOther.setBackgroundColor(getResources().getColor(R.color.app_green));
                tvOther.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                tvBody.setBackgroundResource(R.drawable.bg_box);
                tvBody.setTextColor(getResources().getColor(R.color.content_two));
                tvOther.setBackgroundResource(R.drawable.bg_box);
                tvOther.setTextColor(getResources().getColor(R.color.content_two));
                break;
        }

    }

    /**
     * 暂停本次放疗
     * @param body
     * @param token
     */
    private void pauseHttpRadiotherapy(PauseRadiotherapyBody body,String token){
        HttpService.getHttpService().postPauseRadiotherapy(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","pauseHttpRadiotherapy onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","pauseHttpRadiotherapy onError");
                        Log.i("hcb","e" + e);
                        progressDialog.dismiss();
                        Toast.makeText(ActivityAttentionPauseRadiotherapy.this,"上传出错，请重新上传",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","pauseHttpRadiotherapy onNext");
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
