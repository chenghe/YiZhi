package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.contract.AttentionHospitalizedDeatilContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddHospitalizedPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionHospitalizedDetailPresenter;
import com.zhongmeban.base.BaseActivityToolBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;

/**
 * 住院详情Activity
 * Created by Chengbin He on 2016/7/5.
 */
public class AttentionHospitalizedDetailActivity extends BaseActivityToolBar implements View.OnClickListener, AttentionHospitalizedDeatilContract.View {

    private AttentionHospitalizedDeatilContract.Presenter presenter;
    private Activity mContext = AttentionHospitalizedDetailActivity.this;

    private TextView tvName;
    private TextView tvInTime;//入院时间
    private TextView tvHospitalizedType;//入院类型
    private TextView tvDoctName;//主治医生
    private TextView tvDays;//住院天数
    private TextView tvOperationInfoTitle;//手术信息标题
    private TextView tvOperationInfo;//手术信息
    private TextView tvOperationDoctTitle;//主刀医生标题
    private TextView tvOperationDoct;//主刀医生
    private TextView tvNotes;//备注信息
    private Button btEdit;//编辑按钮
    private Button btDelete;//删除按钮
    private SharedPreferences serverTimeSP;
    private List<SurgeryMethods> surgeryMethodsList = new ArrayList<>();//手术其他信息数据库返回数据

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_hospitalized_detail;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("住院详情", "");
    }

    @Override
    protected void initView() {

        tvName = (TextView) findViewById(R.id.tv_hosp_name);
        tvInTime = (TextView) findViewById(R.id.tv_time);
        tvHospitalizedType = (TextView) findViewById(R.id.tv_hosp_purpose);
        tvDoctName = (TextView) findViewById(R.id.tv_doc_name);
        tvOperationInfoTitle = (TextView) findViewById(R.id.tv_surgery_title);
        tvOperationInfo = (TextView) findViewById(R.id.tv_surgery);
        tvOperationDoctTitle = (TextView) findViewById(R.id.tv_surgery_doct_title);
        tvOperationDoct = (TextView) findViewById(R.id.tv_surgery_doct);
        tvNotes = (TextView) findViewById(R.id.tv_notes);
        tvDays = (TextView) findViewById(R.id.tv_days_count);

        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);

        btEdit = (Button) findViewById(R.id.bt_edit);
        btEdit.setOnClickListener(this);

        new AttentionHospitalizedDetailPresenter(mContext,this);
        presenter.start();

    }

    @Override
    public void setInTime(String inTime) {

        tvInTime.setText(inTime);
    }

    @Override
    public void setHospType(String typeName) {
        if (TextUtils.isEmpty(typeName)){
            tvHospitalizedType.setText("无");
        }else {
            tvHospitalizedType.setText(typeName);
        }
    }

    @Override
    public void setNotes(String notes) {
        if (TextUtils.isEmpty(notes)){
            tvNotes.setText("无");
        }else {
            tvNotes.setText(notes);
        }
    }

    @Override
    public void setHospName(String hospName) {
        tvName.setText(hospName);
    }

    @Override
    public void surgeryVisable() {
        tvOperationDoctTitle.setVisibility(View.VISIBLE);
        tvOperationDoct.setVisibility(View.VISIBLE);
        tvOperationInfoTitle.setVisibility(View.VISIBLE);
        tvOperationInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void surgeryGone() {
        tvOperationDoctTitle.setVisibility(View.GONE);
        tvOperationDoct.setVisibility(View.GONE);
        tvOperationInfoTitle.setVisibility(View.GONE);
        tvOperationInfo.setVisibility(View.GONE);
    }

    @Override
    public void setSurgery(String name) {
        if (TextUtils.isEmpty(name)){
            tvOperationInfo.setText("无");
        }else {
            tvOperationInfo.setText(name);
        }
    }

    @Override
    public void setSurgeryDoct(String name) {
        if (TextUtils.isEmpty(name)){
            tvOperationDoct.setText("无");
        }else {
            tvOperationDoct.setText(name);
        }

    }

    @Override
    public void setDays(String days) {
        if (TextUtils.isEmpty(days)){
            tvDays.setText("无");
        }else {
            tvDays.setText(days+"天");
        }
    }

    @Override
    public void setDoctName(String name) {
        if (TextUtils.isEmpty(name)){
            tvDoctName.setText("无");
        }else {
            tvDoctName.setText(name);
        }
    }

    @Override
    public void setPresenter(AttentionHospitalizedDeatilContract.Presenter presenter) {
        if (presenter!=null){
            this.presenter = presenter;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                if (presenter.exitSurgery()) {
                    showDeleteDialog(mContext, positiveCallback, "这条记录与手术有关联，将同时删除手术信息，是否删除？");
                } else {
                    showDeleteDialog(mContext, positiveCallback, "是否删除这条住院记录？");
                }
                break;

            case R.id.bt_edit:
                Intent intent = new Intent(mContext, AttentionAddHospitalizedActivity.class);

                presenter.editHospitalized(intent);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionAddHospitalizedPresenter.AddOrEditHospitalizedCodeOk) {
            mContext.setResult(AttentionAddHospitalizedPresenter.AddOrEditHospitalizedCodeOk);
            mContext.finish();
        }
    }

    /**
     * 删除Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback positiveCallback = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            progressDiaglog = showProgressDialog("正在删除数据，请稍后", mContext);
            presenter.deleteHospitalized();
            dialog.dismiss();
        }
    };

    @Override
    public void dismissProgressDialog() {
        progressDiaglog.dismiss();
    }

}
