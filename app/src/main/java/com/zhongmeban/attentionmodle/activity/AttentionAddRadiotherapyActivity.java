package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.contract.AttentionAddRadiotherapyContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddRadiotherapyPresenter;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.CommonEditView;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.DatePicker.OnSinglePickedListener;
import com.zhongmeban.view.TypeSelectLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增放疗记录 1.0.2
 * Created by Chengbin He on 2017/1/9.
 */

public class AttentionAddRadiotherapyActivity extends BaseActivityToolBar implements View.OnClickListener, AttentionAddRadiotherapyContract.View {

    private AttentionAddRadiotherapyContract.Presenter presenter;
    private Activity mContext = AttentionAddRadiotherapyActivity.this;

    private TypeSelectLinearLayout typeSelectLinearLayout;
    private CommonEditView evStartTime;//开始时间
    private CommonEditView evDose;//放疗剂量
    private CommonEditView evTimesCount;//放疗次数
    private CommonEditView evWeeksCount;//治疗时长
    private EditText etNotes;//备注信息
    private Button btSave;
    private List<String> mdata = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_radiotherapy;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("新增放疗", "");
    }

    @Override
    protected void initView() {
        typeSelectLinearLayout = (TypeSelectLinearLayout) findViewById(R.id.typeselectlinearlayout);
        typeSelectLinearLayout.initTitleName("周期结束", "耐受终止", "手术准备", "治疗进行中");
        typeSelectLinearLayout.setShowNum(4);
        typeSelectLinearLayout.setTypeClickListenter(new TypeSelectLinearLayout.TypeClickListenter() {
            @Override
            public void onTypeClick(int position) {
                presenter.setResultType(position);
            }
        });

        evStartTime = (CommonEditView) findViewById(R.id.ev_starttime);
        evStartTime.setOnClickListener(this);
        evDose = (CommonEditView) findViewById(R.id.ev_dose);
        evDose.setEditTextInputNum();
        evTimesCount = (CommonEditView) findViewById(R.id.ev_times_count);
        evTimesCount.setOnClickListener(this);
        evWeeksCount = (CommonEditView) findViewById(R.id.ev_weeks_count);
        evWeeksCount.setOnClickListener(this);
        btSave = (Button) findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);

        etNotes = (EditText) findViewById(R.id.common_edit_remark);

        new AttentionAddRadiotherapyPresenter(mContext,this);
        presenter.start();

        for (int i = 0; i < 20; i++) {
            mdata.add((i + 1) + "");
        }
    }

    @Override
    public void setPresenter(AttentionAddRadiotherapyContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ev_times_count:
                showSelectDialog(new OnSinglePickedListener() {
                    @Override
                    public void onPickCompleted(int postion) {
                        presenter.setTimeCount(mdata.get(postion));
                        evTimesCount.setRightText(mdata.get(postion));
                    }
                },mdata,0);
                break;
            case R.id.ev_weeks_count:
                showSelectDialog(new OnSinglePickedListener() {
                    @Override
                    public void onPickCompleted(int postion) {
                        presenter.setWeekCount(mdata.get(postion));
                        evWeeksCount.setRightText(mdata.get(postion));
                    }
                },mdata,0);
                break;
            case R.id.ev_starttime:
                showDatePicker(mContext,inTimeDatePickedListener,getTodayData());
                break;
            case R.id.bt_save:
                presenter.setDose(evDose.getEditString());
                presenter.setNotes(etNotes.getText().toString());
                if (presenter.canCommit()){
                    progressDiaglog = showProgressDialog("正在上传数据，请稍后", mContext);
                    presenter.setNotes(etNotes.getText().toString());
                    presenter.commitData();
                }
                break;
        }
    }
    /**
     * 开始时间datepicker点击回调
     */
    OnDatePickedListener inTimeDatePickedListener =  new OnDatePickedListener(){

        @Override
        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
            evStartTime.setRightText(year+"-"+month+"-"+day);
            presenter.setStartTime(changeDateToLong(dateDesc));
        }
    };

    @Override
    public void dismissProgressDialog() {
        progressDiaglog.dismiss();
    }

    @Override
    public void setStartTime(String time) {
        evStartTime.setRightText(time);
    }

    @Override
    public void setResultType(int resultType) {
        typeSelectLinearLayout.setSelectPosition(resultType);
    }

    @Override
    public void setDose(String dose) {
        evDose.setRightEtText(dose);
    }

    @Override
    public void setWeekCount(String weekCount) {
        evWeeksCount.setRightText(weekCount);
    }

    @Override
    public void setTimeCount(String daysCount) {
        evTimesCount.setRightText(daysCount);
    }

    @Override
    public void setNotes(String notes) {
        etNotes.setText(notes);
    }

    @Override
    public void showToast(String name) {

        ToastUtil.showText(mContext,name);
    }
}
