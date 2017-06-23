package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.zhongmeban.MyApplication;
import com.zhongmeban.attentionmodle.contract.AttentionOperationListContract;
import com.zhongmeban.bean.SurgeryRecordsList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;
import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgeryMethodsDao;
import de.greenrobot.dao.attention.SurgeryRecord;
import de.greenrobot.dao.attention.SurgeryRecordDao;
import de.greenrobot.dao.attention.SurgerySource;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2017/1/3.
 */

public class AttentionOperationListPresenter implements AttentionOperationListContract.Presenter{

    private Activity mContext;
    private AttentionOperationListContract.View view;
    private String patientId;
    private String token;
    private SharedPreferences serverTimeSP;
    private String surgeryServerTime;//手术Servertime
    private List<SurgeryRecordsList.Source> httpList = new ArrayList<SurgeryRecordsList.Source>();
    private List<SurgerySource> dbList = new ArrayList<SurgerySource>();
    private SurgeryMethodsDao surgeryMethodsDao;
    private SurgeryRecordDao surgeryRecordDao;

    public AttentionOperationListPresenter(Activity mContext, AttentionOperationListContract.View view) {
        this.mContext = mContext;
        this.view = view;
        initData();
        view.setPresenter(this);
    }

    @Override
    public void start() {
        getData();
    }

    private void initData() {
        serverTimeSP = mContext.getSharedPreferences(SPInfo.SPServerTime, Context.MODE_PRIVATE);
        SharedPreferences sp = mContext.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        getServerTime();

    }

    private void getServerTime() {
        surgeryServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_surgeryServerTime, SPInfo.ServerTimeDefaultValue);
    }
    @Override
    public void getData() {
        getServerTime();
        getHttpData(surgeryServerTime,patientId,token);
    }

    @Override
    public Intent startOperationDetail(Intent intent, int position) {

        SurgerySource surgerySource = dbList.get(position);
        intent.putExtra(AttentionOperationDetailPresenter.OPERATION,surgerySource);
        return intent;
    }


    /**
     * 获取网络数据
     * @param serverTime
     * @param patientId
     * @param token
     */
    private void getHttpData(String serverTime,String patientId,String token){
        HttpService.getHttpService().getSurgeryRecords(serverTime,patientId,token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<SurgeryRecordsList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","FragmentOperationList getHttpData onCompleted");
                        insertDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","FragmentOperationList getHttpData onError");
                        Log.i("hcb","e " +e);
                        view.noNet();
                        ToastUtil.showText(mContext,"请检查网络");
                    }

                    @Override
                    public void onNext(SurgeryRecordsList surgeryRecordsList) {
                        Log.i("hcb","FragmentOperationList getHttpData onNext");
                        if (surgeryRecordsList.getData().getSource().size()>0){

                            httpList = surgeryRecordsList.getData().getSource();
                            SharedPreferences.Editor editor = serverTimeSP.edit();
                            String surgeryServerTime = surgeryRecordsList.getData().getServerTime();
                            editor.putString(SPInfo.ServerTimeKey_surgeryServerTime, surgeryServerTime);
                            editor.commit();
                        }
                    }
                });
    }

    /**
     * 插入数据库数据
     */
    private void insertDB(){
        if (httpList.size() > 0) {//判断是否有最新数据需要入库
            surgeryRecordDao = ((MyApplication) (mContext.getApplication()))
                    .getAttentionDaoSession().getSurgeryRecordDao();
            surgeryMethodsDao = ((MyApplication) (mContext.getApplication()))
                    .getAttentionDaoSession().getSurgeryMethodsDao();

            for (int i = 0; i < httpList.size(); i++) {
                //surgeryRecord 入库
                SurgeryRecordsList.SurgeryRecord surgeryRecord = httpList.get(i).getSurgeryRecord();
                int id = surgeryRecord.getId();
                int hospitalId = surgeryRecord.getHospitalId();
                int therapeuticId = surgeryRecord.getTherapeuticId();

                long time = surgeryRecord.getTime();
                long hospitalRecordId = surgeryRecord.getHospitalRecordId();
                String notes = surgeryRecord.getNotes();
                String doctorName = surgeryRecord.getDoctorName();
                String therapeuticName = surgeryRecord.getTherapeuticName();
                String patientId = surgeryRecord.getPatientId();
                String hospitalName = surgeryRecord.getHospitalName();
                boolean isActive = surgeryRecord.isActive();

                SurgeryRecord dbSurgeryRecord = new SurgeryRecord();
                dbSurgeryRecord.setId(id);
                dbSurgeryRecord.setDoctorName(doctorName);
                dbSurgeryRecord.setHospitalId(hospitalId);
                dbSurgeryRecord.setTherapeuticId(therapeuticId);
                dbSurgeryRecord.setTime(time);
                dbSurgeryRecord.setHospitalRecordId(hospitalRecordId);
                dbSurgeryRecord.setNotes(notes);
                dbSurgeryRecord.setTherapeuticName(therapeuticName);
                dbSurgeryRecord.setPatientId(patientId);
                dbSurgeryRecord.setHospitalName(hospitalName);
                dbSurgeryRecord.setIsActive(isActive);

                long count = surgeryRecordDao.queryBuilder()
                        .where(SurgeryRecordDao.Properties.Id.eq(id)).count();//判断是否更新
                if (count > 0){
                    surgeryRecordDao.update(dbSurgeryRecord);
                }else {
                    surgeryRecordDao.insert(dbSurgeryRecord);
                }

                //surgeryMethods入库
                List<SurgeryRecordsList.SurgeryMethods> list = httpList.get(i).getSurgeryMethods();

                List<SurgeryMethods>dbSurgeryMethodsList =  surgeryMethodsDao.queryBuilder().where(SurgeryMethodsDao.Properties
                        .SurgeryRecordId.eq(id)).list();
                for (SurgeryMethods surgeryMethods :dbSurgeryMethodsList){
                    surgeryMethodsDao.delete(surgeryMethods);
                }

                for(int a=0;a<list.size();a++){
                    SurgeryRecordsList.SurgeryMethods httpSurgeryMethods = list.get(a);
                    int surgeryMethodId = httpSurgeryMethods.getSurgeryMethodId();
                    int surgeryRecordId = id;
                    String surgeryMethodName = httpSurgeryMethods.getSurgeryMethodName();
                    SurgeryMethods dbSurgeryMethods = new SurgeryMethods();
                    dbSurgeryMethods.setSurgeryMethodId((long) surgeryMethodId);//其他手术项ID
                    dbSurgeryMethods.setSurgeryRecordId((long) id);//手术ID
                    dbSurgeryMethods.setSurgeryMethodName(surgeryMethodName);
                    surgeryMethodsDao.insert(dbSurgeryMethods);

                }
            }
        }

        getDBData();
    }

    /**
     * 获取数据库数据
     */
    private void getDBData(){
        Observable.create(new Observable.OnSubscribe<SurgerySource>() {

            @Override
            public void call(Subscriber<? super SurgerySource> subscriber) {
                surgeryRecordDao = ((MyApplication) (mContext.getApplication()))
                        .getAttentionDaoSession().getSurgeryRecordDao();
                surgeryMethodsDao = ((MyApplication) (mContext.getApplication()))
                        .getAttentionDaoSession().getSurgeryMethodsDao();

                List<SurgeryRecord> surgeryRecordList = surgeryRecordDao.queryBuilder()
                        .where(SurgeryRecordDao.Properties.IsActive.eq(true),
                                SurgeryRecordDao.Properties.PatientId.eq(patientId))
                        .orderDesc(SurgeryRecordDao.Properties.Time)
                        .list();
                dbList.clear();
                for (int i=0 ; i<surgeryRecordList.size() ; i++ ){
                    int SurgeryRecordId = (int) surgeryRecordList.get(i).getId();
                    List<SurgeryMethods> surgeryMethodsList = surgeryMethodsDao.queryBuilder()
                            .where(SurgeryMethodsDao.Properties.SurgeryRecordId.eq(SurgeryRecordId))
                            .list();
                    SurgerySource surgerySource = new SurgerySource(surgeryMethodsList,
                            surgeryRecordList.get(i));
                    dbList.add(surgerySource);
                }

                ((MyApplication) (mContext.getApplication())).closeAttentionDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SurgerySource>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","FragmentOperationList getDBData onCompleted");
                        if (dbList.size() > 0) {
                            view.exitData();
                            view.showData(dbList);
                        } else {
                            view.noData();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","FragmentOperationList getDBData onError");
                        Log.i("hcb","e " +e);
                        view.noData();
                        ToastUtil.showText(mContext, "数据获取异常");
                    }

                    @Override
                    public void onNext(SurgerySource surgerySource) {
                        Log.i("hcb","FragmentOperationList getDBData onNext");
                    }
                });
    }

}
