package com.zhongmeban.attentionmodle.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.postbody.UpdateEndOrDeleteMedicineBody;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import de.greenrobot.dao.attention.MedicineRecord;
import de.greenrobot.dao.attention.MedicineRecordDao;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用药详情
 * Created by Chengbin He on 2016/8/8.
 */
public class ActivityAttentionMedicineDetail extends BaseActivity implements View.OnClickListener {

    private Context mContext = ActivityAttentionMedicineDetail.this;
    private Button btStop;
    private Button btDelete;
    private TextView tvMedicineName;
    private TextView tvMedicineStatus;
    private TextView tvCycler;
    private TextView tvType;
    private MedicineRecordDao medicineRecordDao;
    private MedicineRecord itemMedicineRecord;
    private UpdateEndOrDeleteMedicineBody body;
    private long chooseId;
    private String token;
    private MaterialDialog progressDiaglog;
    private long endTime;
    private long startTime;
    private int fragmentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_medicine_detail);

        Intent intent = getIntent();
        chooseId = intent.getLongExtra("chooseId", 0);
        fragmentPosition = intent.getIntExtra("fragmentPosition", 0);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        token = sp.getString("token", "");

        body = new UpdateEndOrDeleteMedicineBody();
        btStop = (Button) findViewById(R.id.bt_stop);
        btStop.setOnClickListener(this);
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        tvMedicineName = (TextView) findViewById(R.id.tv_marker_name);
        tvMedicineStatus = (TextView) findViewById(R.id.tv_medicine_state);
        tvCycler = (TextView) findViewById(R.id.tv_cycler);
        tvType = (TextView) findViewById(R.id.tv_type);

        if (chooseId > 0) {
            getDBData(chooseId);
        }

        initTitle();

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("用药详情");
        title.backSlid(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTitleBtnback://title左侧返回键
                finish();
                break;
            case R.id.bt_stop://停止用药
                ShowDatePicker();
                break;
            case R.id.bt_delete://删除用药
                showDeleteDialog();
                break;
        }
    }

    /**
     * 从数据库中获取正在服用数据
     */
    private void getDBData(final long id) {
        Observable.create(new Observable.OnSubscribe<MedicineRecord>() {

            @Override
            public void call(Subscriber<? super MedicineRecord> subscriber) {
                Log.i("hcb", "call");
                medicineRecordDao = ((MyApplication) getApplication())
                        .getAttentionDaoSession().getMedicineRecordDao();
                List<MedicineRecord> dbList = medicineRecordDao.queryBuilder()
                        .where(MedicineRecordDao.Properties.Id.eq(id),
                                MedicineRecordDao.Properties.IsActive.eq(true)).list();
                itemMedicineRecord = dbList.get(0);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MedicineRecord>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "ActivityAttentionMedicineDetail getDBData onCompleted");

                        body.setRecordId((int) itemMedicineRecord.getId());
                        tvMedicineName.setText(itemMedicineRecord.getMedicineName());
                        //tvType.setText(AttentionUtils.getTakeMedicinePurpose((int) itemMedicineRecord.getType()));
                        startTime = itemMedicineRecord.getStartTime();
                        String beginTime = changeDateToString(itemMedicineRecord.getStartTime());
                        if (itemMedicineRecord.getStatus() == 2) {
                            btStop.setVisibility(View.GONE);
                            tvMedicineStatus.setText("已停用");
                            String endTime = changeDateToString(itemMedicineRecord.getEndTime());
                            tvCycler.setText(beginTime + "-" + endTime);
                        } else {
                            tvCycler.setText(beginTime  + " 开始");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "ActivityAttentionMedicineDetail getDBData onError");
                        Log.i("hcb", "e" + e);
                    }

                    @Override
                    public void onNext(MedicineRecord medicineRecord) {
                        Log.i("hcb", "ActivityAttentionMedicineDetail getDBData onNext");
                    }
                });
    }

    /***
     * 停止用药网络接口
     */
    private void stopMedicine(UpdateEndOrDeleteMedicineBody body, String token) {
        //HttpService.getHttpService().updateEndMedicine(body)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Subscriber<UpdateEndOrDeleteMedicine>() {
        //            @Override
        //            public void onCompleted() {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail stopMedicine onCompleted");
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail stopMedicine onError");
        //                Log.i("hcb", "e" + e);
        //                progressDiaglog.dismiss();
        //                Toast.makeText(mContext, "网络异常，更新失败", Toast.LENGTH_SHORT).show();
        //            }
        //
        //            @Override
        //            public void onNext(UpdateEndOrDeleteMedicine updateEndOrDeleteMedicine) {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail stopMedicine onNext");
        //                if (updateEndOrDeleteMedicine.isResult()) {
        //                    new Handler().postDelayed(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            progressDiaglog.dismiss();
        //                            Intent intent = new Intent(mContext,ActivityAttentionMedicineList.class);
        //                            intent.putExtra("fragmentPosition",fragmentPosition);
        //                            setResult(200,intent);
        //                            finish();
        //                        }
        //                    }, 1000);
        //                }
        //            }
        //        });
    }


    /***
     * 删除用药网络接口
     */
    private void deleteMedicine(UpdateEndOrDeleteMedicineBody body, String token) {
        //HttpService.getHttpService().deleteMedicine(body)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Subscriber<UpdateEndOrDeleteMedicine>() {
        //            @Override
        //            public void onCompleted() {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail deleteMedicine onCompleted");
        //
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail deleteMedicine onError");
        //                Log.i("hcb", "e" + e);
        //                progressDiaglog.dismiss();
        //                Toast.makeText(mContext, "网络异常，删除失败", Toast.LENGTH_SHORT).show();
        //
        //            }
        //
        //            @Override
        //            public void onNext(UpdateEndOrDeleteMedicine updateEndOrDeleteMedicine) {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail deleteMedicine onNext");
        //                if (updateEndOrDeleteMedicine.isResult()) {
        //                    new Handler().postDelayed(new Runnable() {
        //                        @Override
        //                        public void run() {
        //                            progressDiaglog.dismiss();
        //                            Intent intent = new Intent(mContext,ActivityAttentionMedicineList.class);
        //                            intent.putExtra("fragmentPosition",fragmentPosition);
        //                            setResult(200,intent);
        //                            finish();
        //                        }
        //                    }, 1000);
        //                }
        //            }
        //        });
    }

    /***
     * 显示是否删除弹框
     */
    private void showDeleteDialog() {
        new MaterialDialog.Builder(this)
                .content("是否删除这条用药记录？")
                .positiveText("确定")
                .positiveColor(getResources().getColor(R.color.button_red_normanl))
                .negativeText("取消")
                .negativeColor(getResources().getColor(R.color.app_green))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        progressDiaglog = showProgressDialog("正在删除请稍等");
                        deleteMedicine(body, token);
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /***
     * ProgressDialog
     *
     * @param content 弹框内容
     */
    private MaterialDialog showProgressDialog(String content) {

        Log.i("hcb", "showProgressDialog");
        return new MaterialDialog.Builder(this)
                .content(content)
                .progress(true, 0)
                .progressIndeterminateStyle(false).show();
    }

    /**
     * 显示DataPicker
     */
    private void ShowDatePicker() {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(mContext, new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                if (changeDateToLong(dateDesc)>startTime){
                    endTime = changeDateToLong(dateDesc);
                    body.setEndTime(endTime);
                    progressDiaglog = showProgressDialog("正在更新数据请稍等");
                    stopMedicine(body,token);
                }else {
                    ToastUtil.showText(mContext,"结束时间必须小于开始时间");
                }

            }
        }).minYear(1990) //min year in loop
                .maxYear(2050) // max year in loop
                .dateChose(getTodayData()) // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(this);
    }

}
