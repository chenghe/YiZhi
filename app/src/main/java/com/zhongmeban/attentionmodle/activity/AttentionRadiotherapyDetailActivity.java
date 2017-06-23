package com.zhongmeban.attentionmodle.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.contract.AttentionRadiotherapyDetailContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddRadiotherapyPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionRadiotherapyDetailPresenter;
import com.zhongmeban.base.BaseActivityToolBar;

/**
 * 放疗详情Activity
 * Created by Chengbin He on 2016/7/14.
 */
public class AttentionRadiotherapyDetailActivity extends BaseActivityToolBar implements View.OnClickListener, AttentionRadiotherapyDetailContract.View {

    private Activity mContext = AttentionRadiotherapyDetailActivity.this;
    private AttentionRadiotherapyDetailContract.Presenter presenter;
    private TextView tvDose;//放疗剂量
    private TextView tvStartTime;//开始日期
    private TextView tvTimesCount;//放疗次数
    private TextView tvWeeksCount;//治疗时长
    private TextView tvNote;//备注信息
    private Button btEdit;//编辑
    private Button btDelete;//删除

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_radiotherapy_detail;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("放疗详情", "");
    }

    @Override
    protected void initView() {
        tvDose = (TextView) findViewById(R.id.tv_dose);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvTimesCount = (TextView) findViewById(R.id.tv_times_count);
        tvWeeksCount = (TextView) findViewById(R.id.tv_weeks_count);
        tvNote = (TextView) findViewById(R.id.tv_notes);
        btEdit = (Button) findViewById(R.id.bt_edit);
        btEdit.setOnClickListener(this);
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);

        new AttentionRadiotherapyDetailPresenter(mContext,this);
        presenter.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_edit:
                Intent intent = new Intent(mContext,AttentionAddRadiotherapyActivity.class);
                startActivityForResult(presenter.editRadiotherapy(intent),1);
                break;
            case R.id.bt_delete:
                showDeleteDialog(mContext, positiveCallback, "是否删除这条放疗记录？");
                break;
        }
    }
    /**
     * 删除Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback positiveCallback = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            progressDiaglog = showProgressDialog("正在删除数据，请稍后",mContext);
            presenter.deleteRadiotherapy();
            dialog.dismiss();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionAddRadiotherapyPresenter.AddOrEditRadiotherapyCodeOk){
            setResult(AttentionAddRadiotherapyPresenter.AddOrEditRadiotherapyCodeOk);
            finish();
        }
    }

    @Override
    public void dismissProgressDialog() {
        progressDiaglog.dismiss();
    }


    @Override
    public void setDose(String name) {
        tvDose.setText(name+"Gy");
    }

    @Override
    public void setTimesCount(String name) {
        tvTimesCount.setText(name+"次");
    }

    @Override
    public void setStartTime(String time) {
        tvStartTime.setText(time);
    }

    @Override
    public void setWeeksCount(String name) {
        tvWeeksCount.setText(name+"周");
    }

    @Override
    public void setNotes(String notes) {
        tvNote.setText(notes);
    }

    @Override
    public void setPresenter(AttentionRadiotherapyDetailContract.Presenter presenter) {
        if (presenter!=null){
            this.presenter = presenter;
        }
    }
}
