package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.contract.AttentionAddHospitalizedContract;
import com.zhongmeban.attentionmodle.fragment.AttentionHospChoosePresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionAddHospOperationPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionAddHospitalizedPresenter;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.utils.StringUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.CommonEditView;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.TypeSelectLinearLayout;

/**
 * 新增关注住院 activity 1.0.2
 * Created by Chengbin He on 2016/12/28.
 */

public class AttentionAddHospitalizedActivity extends BaseActivityToolBar implements View.OnClickListener, AttentionAddHospitalizedContract.View {

    private AttentionAddHospitalizedContract.Presenter presenter;
    private Activity mContext = AttentionAddHospitalizedActivity.this;

    private TypeSelectLinearLayout typeSelectLinearLayout;
    private CommonEditView evSurgery;//手术选择
    private CommonEditView evHosp;//医院选择
    private CommonEditView evIntime;//入院时间
    private CommonEditView evDays;//住院天数
    private CommonEditView evDoct;//主治医生
    private EditText etNotes;//备注信息
    private Button btSave;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_add_hospitalized;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("", "");
    }


    @Override
    protected void initView() {
        typeSelectLinearLayout = (TypeSelectLinearLayout) findViewById(R.id.typeselectlinearlayout);
        typeSelectLinearLayout.initTitleName("观察", "治疗", "手术");
        typeSelectLinearLayout.setTypeClickListenter(new TypeSelectLinearLayout.TypeClickListenter() {
            @Override
            public void onTypeClick(int position) {
                if (position == 3) {
                    evSurgery.setVisibility(View.VISIBLE);
                } else {
                    evSurgery.setVisibility(View.GONE);
                }

                presenter.setPurposeType(position);
            }
        });

        evSurgery = (CommonEditView) findViewById(R.id.ev_surgery);
        evSurgery.setOnClickListener(this);
        evHosp = (CommonEditView) findViewById(R.id.ev_hosp);
        evHosp.setOnClickListener(this);
        evIntime = (CommonEditView) findViewById(R.id.ev_intime);
        evIntime.setOnClickListener(this);
        evDays = (CommonEditView) findViewById(R.id.ev_days);
        evDays.setEditTextInputNum();
        evDoct = (CommonEditView) findViewById(R.id.ev_doct);
        btSave = (Button) findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);

        etNotes = (EditText) findViewById(R.id.common_edit_remark);

        new AttentionAddHospitalizedPresenter(mContext, this);
        presenter.start();
    }

    @Override
    public void setPresenter(AttentionAddHospitalizedContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ev_surgery:
                //创建手术
                Intent operattionIntent = new Intent(mContext, AttentionAddHospOperationActivity.class);
                startActivityForResult(presenter.addSurgery(operattionIntent), 1);
                break;
            case R.id.ev_hosp:
                Intent intent = new Intent(mContext, AttentionHospChooseActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ev_intime:
                showDatePicker(mContext, inTimeDatePickedListener, getTodayData());
                break;
            case R.id.bt_save:
                if (!TextUtils.isEmpty(evDoct.getEditString())) {
                    if (!StringUtils.checkName(evDoct.getEditString())) {
                        ToastUtil.showText(mContext, "医生只能输入中文和英文");
                    }else {
                        if (presenter.changePurposeType()){
                            showDeleteDialog(mContext, positiveCallback,negativeCallback,"你已经修改入院目的，手术将会被删除");
                        }else {
                            presenter.setDoctName(evDoct.getEditString());
                            if (presenter.canCommit()) {
                                progressDiaglog = showProgressDialog("正在上传数据，请稍后", mContext);
                                presenter.setDays(evDays.getEditString());
                                presenter.setNotes(etNotes.getText().toString());
                                presenter.commitData();
                            }
                        }
                    }
                } else {
                    if (presenter.changePurposeType()){
                        showDeleteDialog(mContext, positiveCallback,negativeCallback, "你已经修改入院目的，手术将会被删除");
                    }else {
                        presenter.setDoctName(evDoct.getEditString());
                    if (presenter.canCommit()) {
                        progressDiaglog = showProgressDialog("正在上传数据，请稍后", mContext);
                        presenter.setDays(evDays.getEditString());
                        presenter.setNotes(etNotes.getText().toString());
                        presenter.commitData();
                    }}
                }
                break;
        }
    }

    /**
     * 入院日期datepicker点击回调
     */
    OnDatePickedListener inTimeDatePickedListener = new OnDatePickedListener() {

        @Override
        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
            evIntime.setRightText(year + "-" + month + "-" + day);
            presenter.setIntime(changeDateToLong(dateDesc));
        }
    };

    /**
     * 确认Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback positiveCallback = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
            presenter.setDoctName(evDoct.getEditString());
            if (presenter.canCommit()) {
                progressDiaglog = showProgressDialog("正在上传数据，请稍后", mContext);
                presenter.setDays(evDays.getEditString());
                presenter.setNotes(etNotes.getText().toString());
                presenter.commitData();
            }
        }
    };
    /**
     * 取消Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback negativeCallback = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
            presenter.setPurposeType(presenter.getBeforePurposeType());
            typeSelectLinearLayout.setSelectPosition(presenter.getBeforePurposeType());
            evSurgery.setVisibility(View.VISIBLE);
            evSurgery.setRightText(presenter.getTherapeuticName());
        }
    };

    @Override
    public void dismissProgressDialog() {
        progressDiaglog.dismiss();
    }

    @Override
    public void setIntime(String time) {
        evIntime.setRightText(time);
    }

    @Override
    public void setDays(String days) {
        evDays.setRightEtText(days);
    }

    @Override
    public void setDoctName(String name) {
        evDoct.setRightEtText(name);
    }

    @Override
    public void setPurposeType(int type) {
        typeSelectLinearLayout.setSelectPosition(type);
        if (type == 3) {
            evSurgery.setVisibility(View.VISIBLE);
        } else {
            evSurgery.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNotes(String notes) {
        etNotes.setText(notes);
    }

    @Override
    public void setHospName(String name) {
        evHosp.setRightText(name);
    }

    @Override
    public void setTitleName(String titleName) {
        mToolbar.setTitle(titleName);
    }

    @Override
    public void setSurgeryName(String name) {
        evSurgery.setRightText(name);
    }

    @Override
    public void showToast(String name) {
        ToastUtil.showText(mContext, name);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionHospChoosePresenter.AttentionChooseHospOk) {//医院选择回调
            String hospName = data.getStringExtra(AttentionHospChoosePresenter.AttentionHospName);
            int hospId = data.getIntExtra(AttentionHospChoosePresenter.AttentionHospId, 0);
            presenter.setHospName(hospName);
            presenter.setHospId(hospId);
            evHosp.setRightText(presenter.getHospName());
        } else if (resultCode == AttentionAddHospOperationPresenter.AddHospOperationOK) {
            //住院手术返回
            presenter.getAddHospOperationResultData(data);
        }
    }
}
