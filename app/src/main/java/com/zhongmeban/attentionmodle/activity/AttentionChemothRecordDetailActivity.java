package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.postbody.DeleteRecordBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.SpannableStringUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 化疗详情Activity
 * Created by
 */
public class AttentionChemothRecordDetailActivity extends BaseActivityToolBar
    implements View.OnClickListener {

    public static final String EXTRA_CHEMOTH_RECORD_INFO = "extra_chemoth_record_info";
    public static final String EXTRA_CHEMOTH_RECORD_MEDICINES = "extra_chemoth_record_medicines";

    private Activity mContext = AttentionChemothRecordDetailActivity.this;
    private Subscription dbSubscription;
    private Button btDelete;
    private Button btEdit;
    private String pationId;
    private TextView tvChemothAnim;
    private TextView tvChemothInfo;

    private ChemotherapyRecordBean.Record mDatasRecord;
    private List<ChemotherapyRecordBean.Medicines> mDatasMedicine = new ArrayList<>();


    @Override protected int getLayoutId() {
        return R.layout.activity_attention_chemoth_record_detail;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("化疗详情", "");
    }


    @Override protected void initView() {

        initUI();
        initData();
    }


    private void initUI() {
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        btEdit = (Button) findViewById(R.id.bt_edit);
        btEdit.setOnClickListener(this);

        tvChemothAnim = (TextView) findViewById(R.id.tv_chemotherapy_target);
        tvChemothInfo = (TextView) findViewById(R.id.tv_chemotherapy_info);
    }


    private void initData() {

        mDatasRecord = (ChemotherapyRecordBean.Record) getIntent().getSerializableExtra(
            EXTRA_CHEMOTH_RECORD_INFO);
        mDatasMedicine = (List<ChemotherapyRecordBean.Medicines>) getIntent().getSerializableExtra(
            EXTRA_CHEMOTH_RECORD_MEDICINES);
        setTvInfo();
    }


    /**
     * 删除化疗记录
     */
    private void deleteChemothRecord(DeleteRecordBody body) {
        showProgressDialog("正在删除数据，请稍后", mContext);
        HttpService.getHttpService().postDeleteChemotherapyRecord(body)
            .compose(RxUtil.<HttpResult>normalSchedulers())
            .subscribe(new Action1<HttpResult>() {
                @Override public void call(HttpResult httpResult) {
                    dissmisProgress();
                    if (httpResult.isResult()) {
                        ToastUtil.showText(mContext, "修改成功");
                        finish();
                    } else {
                        ToastUtil.showText(mContext, httpResult.getErrorMessage());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    dissmisProgress();
                    ToastUtil.showText(mContext, "请求失败");
                    Logger.e("1111" + throwable.toString());
                }
            });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbSubscription != null && dbSubscription.isUnsubscribed()) {
            dbSubscription.unsubscribe();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete://删除
                showDeleteDialog(mContext, positiveCallback, "是否删除这条记录？");

                break;

            case R.id.bt_edit://编辑
                Intent intent = new Intent(mContext, AttentionAddChemothRecordActivity.class);
                intent.putExtra(AttentionAddChemothRecordActivity.EXTRA_IS_FROM_EDIT, true);
                intent.putExtra(AttentionChemothRecordDetailActivity.EXTRA_CHEMOTH_RECORD_INFO,
                    mDatasRecord);
                intent.putExtra(AttentionChemothRecordDetailActivity.EXTRA_CHEMOTH_RECORD_MEDICINES,
                    (Serializable) mDatasMedicine);
                startActivityForResult(intent, 2);
                break;
        }
    }


    /**
     * 删除Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback positiveCallback
        = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            progressDiaglog = showProgressDialog("正在删除数据，请稍后", mContext);
            deleteChemothRecord(new DeleteRecordBody((int) (mDatasRecord.getId())));
            dialog.dismiss();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) {
            Intent intent = new Intent(this, ActivityAttentionList.class);
            setResult(300, intent);
            finish();
        }

        Observable<List<String>> listObservable = Observable.fromCallable(
            new Callable<List<String>>() {
                @Override public List<String> call() throws Exception {
                    List<String> mList = new ArrayList<String>();
                    mList.add("1");
                    mList.add("2");
                    mList.add("3");
                    return mList;
                }
            });
    }


    // 设置标记物各种信息
    private void setTvInfo() {
        tvChemothAnim.setText(
            AttentionUtils.getChemotherapyPurposeName(mDatasRecord.getChemotherapyAim()));

        int content = ContextCompat.getColor(this, R.color.content_two);
        int textOne = ContextCompat.getColor(this, R.color.text_one);
        String docName = TextUtils.isEmpty(mDatasRecord.getDoctorName())
                         ? "无" : mDatasRecord.getDoctorName();
        SpannableStringBuilder builder = SpannableStringUtils.getBuilder("开始日期：\n")
            .setForegroundColor(content)
            .append(TimeUtils.formatTime(mDatasRecord.getStartTime()) + "\n")
            .setForegroundColor(textOne)
            .append("化疗周期：\n")
            .setForegroundColor(content)
            .append(mDatasRecord.getWeeksCount() + "个周期\n")
            .setForegroundColor(textOne)
            .append("主治医师：\n")
            .setForegroundColor(content)
            .append(docName + "\n")
            .setForegroundColor(textOne)
            .append("化疗用药：\n")
            .setForegroundColor(content)
            .append(mDatasRecord.getMedicinesName() + "\n")
            .setForegroundColor(textOne)
            .append("治疗备注：\n")
            .setForegroundColor(content)
            .append(
                AttentionUtils.getStopChemotherapyResaonName(mDatasRecord.getNotesType()) + "\n")
            .setForegroundColor(textOne)
            .append(mDatasRecord.getNotes() + "\n")
            .setForegroundColor(textOne)
            .create();
        tvChemothInfo.setText(builder);
    }

}