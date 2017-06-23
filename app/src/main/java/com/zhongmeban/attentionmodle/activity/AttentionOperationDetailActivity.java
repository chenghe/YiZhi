package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.contract.AttentionOperationDetailContract;
import com.zhongmeban.attentionmodle.presenter.AttentionAddOperationPresent;
import com.zhongmeban.attentionmodle.presenter.AttentionOperationDetailPresenter;
import com.zhongmeban.base.BaseActivityToolBar;

/**
 * 手术记录详情Activity
 * Created by Chengbin He on 2016/7/5.
 */
public class AttentionOperationDetailActivity extends BaseActivityToolBar implements View.OnClickListener,AttentionOperationDetailContract.View{

    private AttentionOperationDetailContract.Presenter presenter;
    private Activity mContext = AttentionOperationDetailActivity.this;

    private TextView tvName;//手术名称
    private TextView tvDate;//检查日期
    private TextView tvHosp;//手术医院
    private TextView tvDoct;//主刀医生
    private TextView tvOther;//其他操作
    private TextView tvOtherTitle;//其他操作标题
    private TextView tvNote;//备注信息
    private Button btEdit;//编辑
    private Button btDelete;//删除
//    private SurgeryRecord surgeryRecord;
//    private int surgeryRecordId;
//    private String token;
//    private String patientId;
//    private  List<SurgeryMethods> dbSurgeryMethodsList = new ArrayList<>();//其他手术项



    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_operation_detail;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("手术详情", "");
    }

    @Override
    protected void initView() {
        btEdit = (Button) findViewById(R.id.bt_edit);
        btEdit.setOnClickListener(this);
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        tvDate = (TextView) findViewById(R.id.tv_time);
        tvHosp = (TextView) findViewById(R.id.tv_hosp_name);
        tvDoct = (TextView) findViewById(R.id.tv_doc_name);
        tvOther = (TextView) findViewById(R.id.tv_other);
        tvOtherTitle = (TextView) findViewById(R.id.tv_other_title);
        tvNote = (TextView) findViewById(R.id.tv_notes);
        tvName = (TextView) findViewById(R.id.tv_operation_name);

        new AttentionOperationDetailPresenter(mContext,this);
        presenter.start();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_edit:
                Intent intent = new Intent(mContext,AttentionAddOperationActivity.class);
                startActivityForResult(presenter.editOperation(intent),1);
                break;
            case R.id.bt_delete:
                String name = "是否删除这条手术记录？";
                if (presenter.exitHospitalize()){
                    name = "这条手术与住院关联，删除将同时删除住院信息，是否删除这条手术记录？";
                }

                showDeleteDialog(mContext, positiveCallback, name);
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
            presenter.deleteOperation();
            dialog.dismiss();
        }
    };

    @Override
    public void dismissProgressDialog() {
        progressDiaglog.dismiss();
    }

    @Override
    public void setOperationName(String name) {
        if (TextUtils.isEmpty(name)){
            tvName.setText("无");
        }else {
            tvName.setText(name);
        }

    }

    @Override
    public void setDoctName(String name) {
        if (TextUtils.isEmpty(name)){
            tvDoct.setText("无");
        }else {
            tvDoct.setText(name);
        }

    }

    @Override
    public void setHospName(String name) {
        if (TextUtils.isEmpty(name)){
            tvHosp.setText("无");
        }else {
            tvHosp.setText(name);
        }
    }

    @Override
    public void setTime(String time) {
        tvDate.setText(time);
    }

    @Override
    public void setOther(String name) {
        tvOther.setText(name);
    }

    @Override
    public void setNotes(String notes) {
        if (TextUtils.isEmpty(notes)){
            tvNote.setText("无");
        }else {
            tvNote.setText(notes);
        }


    }

    @Override
    public void otherGone() {
        tvOther.setVisibility(View.GONE);
        tvOtherTitle.setVisibility(View.GONE);
    }

    @Override
    public void otherVisable() {
        tvOther.setVisibility(View.VISIBLE);
        tvOtherTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(AttentionOperationDetailContract.Presenter presenter) {
        if (presenter!=null){
            this.presenter = presenter;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AttentionAddOperationPresent.AddOrEditOperationCodeOk){
            setResult(AttentionAddOperationPresent.AddOrEditOperationCodeOk);
            finish();
        }
    }

//    /**
//     * 删除Dialog回调接口
//     */
//    MaterialDialog.SingleButtonCallback positiveCallback = new MaterialDialog.SingleButtonCallback(){
//
//        @Override
//        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        progressDiaglog = showProgressDialog("正在删除数据，请稍后",mContext);
//                        DeleteRecordBody body = new DeleteRecordBody(surgeryRecordId);
//                        deleteSurgeryRecord(body,token);
//                        dialog.dismiss();
//        }
//    };


}
