package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.zhongmeban.MyApplication;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.contract.AttentionOperationDetailContract;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.DeleteRecordBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.SPInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;
import de.greenrobot.dao.attention.HospitalizedDao;
import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgerySource;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 手术详情Presenter
 * Created by Chengbin He on 2017/1/5.
 */

public class AttentionOperationDetailPresenter implements AttentionOperationDetailContract.Presenter{

    public static final String OPERATION = "surgerySource";
    public static final int DeleteOperationCodeOk = 300;

    private Activity mContext;
    private AttentionOperationDetailContract.View view;
    private String patientId;
    private String token;
    private SurgerySource surgerySource;//手术信息
    private List<SurgeryMethods> chooseMethods = new ArrayList<>();//选中其他手术项

    public AttentionOperationDetailPresenter(Activity mContext, AttentionOperationDetailContract.View view) {
        this.mContext = mContext;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        initData();
        initView();
    }
    private void initData(){
        SharedPreferences sp = mContext.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        Intent intent = mContext.getIntent();
        surgerySource = (SurgerySource) intent.getSerializableExtra(OPERATION);
    }

    private void initView(){
        view.setOperationName(surgerySource.getSurgeryRecord().getTherapeuticName());
        view.setTime(TimeUtils.formatTime(surgerySource.getSurgeryRecord().getTime()));
        view.setHospName(surgerySource.getSurgeryRecord().getHospitalName());
        view.setDoctName(surgerySource.getSurgeryRecord().getDoctorName());
        view.setNotes(surgerySource.getSurgeryRecord().getNotes());

        chooseMethods = surgerySource.getSurgeryMethods();
        if (chooseMethods.size() > 0) {
            view.setOther(AttentionUtils.getOtherOperationName(chooseMethods));
        }else {
            view.setOther("无");
        }
    }

    @Override
    public void deleteOperation() {
        DeleteRecordBody body = new DeleteRecordBody((int) surgerySource.getSurgeryRecord().getId());
        deleteSurgeryRecord(body);
    }

    @Override
    public Intent editOperation(Intent intent) {
        intent.putExtra(OPERATION,surgerySource);
        return intent;
    }

    @Override
    public boolean exitHospitalize() {

        return surgerySource.getSurgeryRecord().getHospitalRecordId()>0;
    }

    /**
     * 删除手术接口
     * @param body
     */
    private void deleteSurgeryRecord(DeleteRecordBody body){

        HttpService.getHttpService().postDeleteSurgeryRecord(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","AttentionOperationDetailActivity deleteSurgeryRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","AttentionOperationDetailActivity deleteSurgeryRecord onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","AttentionOperationDetailActivity deleteSurgeryRecord onNext");
                        if (createOrDeleteBean.getResult()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext,ActivityAttentionList.class);
                                    mContext.setResult(DeleteOperationCodeOk,intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }


}
