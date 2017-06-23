package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.adapter.AttentionOperationOtherAdapter;
import com.zhongmeban.attentionmodle.contract.AttentionAddOperationContract;
import com.zhongmeban.attentionmodle.fragment.AttentionHospChoosePresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionAddOperationPresent;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.utils.StringUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.CommonEditView;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.ScrollLinearLayout;

import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;

/**
 * 新增手术 1.0.2
 * Created by Chengbin He on 2017/1/4.
 */

public class AttentionAddOperationActivity extends BaseActivityToolBar implements View.OnClickListener,AttentionAddOperationContract.View {

    private Activity mContext = AttentionAddOperationActivity.this;
    private AttentionAddOperationContract.Presenter presenter;

    private CommonEditView evSurgery;//手术项目
    private CommonEditView evHosp;//治疗医院
    private CommonEditView evTime;//手术时间
    private CommonEditView evDoct;//主治医生
    private ScrollLinearLayout scrollLinearLayout;//同期手术
    private TextView tvAdd;//添加同期手术
    private EditText etNotes;//备注信息
    private Button btSave;
    private AttentionOperationOtherAdapter mAdapter;//同期手术Adapter

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_add_operation;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("","");
    }

    @Override
    protected void initView() {

        evSurgery = (CommonEditView) findViewById(R.id.ev_opera_name);
        evSurgery.setOnClickListener(this);
        evDoct = (CommonEditView) findViewById(R.id.ev_doct);
        evDoct.setOnClickListener(this);
        evHosp = (CommonEditView) findViewById(R.id.ev_hosp);
        evHosp.setOnClickListener(this);
        evTime = (CommonEditView) findViewById(R.id.ev_time);
        evTime.setOnClickListener(this);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(this);
        btSave = (Button) findViewById(R.id.bt_save);
        btSave.setOnClickListener(this);
        scrollLinearLayout = (ScrollLinearLayout) findViewById(R.id.scroll_ll);
        etNotes = (EditText) findViewById(R.id.common_edit_remark);

        new AttentionAddOperationPresent(mContext,this);
        presenter.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ev_opera_name:
                //手术项目
                Intent surgeryIntent = new Intent(mContext, AttentionSurgeryListActivity.class);
                startActivityForResult(surgeryIntent, 1);
                break;
            case R.id.ev_doct:
                //主治医生
                break;
            case R.id.ev_hosp:
                //治疗医院
                Intent intent = new Intent(mContext, AttentionHospChooseActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ev_time:
                showDatePicker(mContext,timeDatePickedListener,getTodayData());
                //手术时间
                break;
            case R.id.tv_add:
                //添加同期手术
                Intent surgeryMethordsIntent = new Intent(mContext,AttentionSurgeryMethordsActivity.class);
                startActivityForResult(presenter.chooseMethods(surgeryMethordsIntent),1);
                break;
            case R.id.bt_save:
                if (!TextUtils.isEmpty(evDoct.getEditString())){
                    if (!StringUtils.checkName(evDoct.getEditString())){
                        ToastUtil.showText(mContext,"医生只能输入中文和英文");
                    }else {
                        presenter.setDoctName(evDoct.getEditString());
                        presenter.setNotes(etNotes.getText().toString());
                        if (presenter.canCommit()){
                            progressDiaglog = showProgressDialog("正在上传数据，请稍后", mContext);
                            presenter.setNotes(etNotes.getText().toString());
                            presenter.commitData();
                        }
                    }
                }else {
                    presenter.setNotes(etNotes.getText().toString());
                    presenter.setDoctName(evDoct.getEditString());
                        if (presenter.canCommit()){
                            progressDiaglog = showProgressDialog("正在上传数据，请稍后", mContext);
                            presenter.setNotes(etNotes.getText().toString());
                            presenter.commitData();
                        }
                    }
                break;
        }
    }
    /**
     * 日期datepicker点击回调
     */
    OnDatePickedListener timeDatePickedListener =  new OnDatePickedListener(){

        @Override
        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
            evTime.setRightText(year+"-"+month+"-"+day);
            presenter.setTime(changeDateToLong(dateDesc));
        }
    };

    @Override
    public void setPresenter(AttentionAddOperationContract.Presenter presenter) {
        if (presenter!=null){
            this.presenter = presenter;
        }
    }

    @Override
    public void dismissProgressDialog() {
        progressDiaglog.dismiss();
    }

    @Override
    public void setTime(String time) {
        evTime.setRightText(time);
    }

    @Override
    public void setDoctName(String name) {
        evDoct.setRightEtText(name);
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
    public void setTherapeuticName(String name) {
        evSurgery.setRightText(name);
    }

    @Override
    public void showToast(String name) {
        ToastUtil.showText(mContext,name);
    }

    @Override
    public void setSurgeryMethords(final List<SurgeryMethods> mChooseMethods) {

        mAdapter = new AttentionOperationOtherAdapter(this,mChooseMethods);
        mAdapter.SetItemClickListenter(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
//                mChooseMethods.remove(position);
                presenter.removeChooseMethodsPosition(position);
                scrollLinearLayout.setAdapter(mAdapter);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        scrollLinearLayout.setAdapter(mAdapter);
//        if (mAdapter == null){
//
//        }else {
//            mAdapter = new
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  AttentionHospChoosePresenter.AttentionChooseHospOk) {//医院选择回调
            String hospName = data.getStringExtra(AttentionHospChoosePresenter.AttentionHospName);
            int hospId = data.getIntExtra(AttentionHospChoosePresenter.AttentionHospId,0);
            presenter.setHospName(hospName);
            presenter.setHospId(hospId);
            evHosp.setRightText(presenter.getHospName());
        }else if (resultCode == AttentionSurgeryListActivity.SurgeryListOK){
            String surgeryName = data.getStringExtra(AttentionSurgeryListActivity.SurgeryName);
            int surgeryId= data.getIntExtra(AttentionSurgeryListActivity.SurgeryId,0);
            Log.i("hcbtest","surgeryName" +surgeryName);
            presenter.setTherapeuticName(surgeryName);
            presenter.setTherapeuticId(surgeryId);
            evSurgery.setRightText(presenter.getTherapeuticName());
        }else if(resultCode == AttentionSurgeryMethordsActivity.SurgeryMethodsOk){
           List<SurgeryMethods> list = (List<SurgeryMethods>) data.getSerializableExtra(AttentionSurgeryMethordsActivity.SurgeryMethodsList);
//            Log.i("hcbtest","list.size()"+list.size());
            presenter.setChooseMethods((List<SurgeryMethods>) data.getSerializableExtra(AttentionSurgeryMethordsActivity.SurgeryMethodsList));
        }
    }
}
