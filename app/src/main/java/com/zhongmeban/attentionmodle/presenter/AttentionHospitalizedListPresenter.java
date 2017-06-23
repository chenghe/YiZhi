package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.zhongmeban.MyApplication;
import com.zhongmeban.attentionmodle.contract.AttentionHospitalizedListContract;
import com.zhongmeban.bean.HospitalRecordsBean;
import com.zhongmeban.bean.SurgeryRecordsList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;
import de.greenrobot.dao.attention.HospitalizedDao;
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
 * Created by Chengbin He on 2016/12/27.
 */

public class AttentionHospitalizedListPresenter implements AttentionHospitalizedListContract.Presenter {

    private Activity parentActivity;
    private AttentionHospitalizedListContract.View view;
    private String hospitalizedServerTime;//住院Servertime
    private String surgeryServerTime;//手术Servertime
    private String patientId;
    private String token;
    private SharedPreferences serverTimeSP;
    private HospitalizedDao hospitalizedDao;
    private SurgeryMethodsDao surgeryMethodsDao;
    private SurgeryRecordDao surgeryRecordDao;
    private List<HospitalRecordsBean.Source> httpHospitalList =
            new ArrayList<HospitalRecordsBean.Source>();//住院网络接口List
    private List<SurgeryRecordsList.Source> httpOperationList =
            new ArrayList<SurgeryRecordsList.Source>();//手术网络接口List
    private List<Hospitalized> dbList = new ArrayList<Hospitalized>();


    public AttentionHospitalizedListPresenter(Activity parentActivity, AttentionHospitalizedListContract.View view) {
        this.parentActivity = parentActivity;
        this.view = view;
        initData();
        view.setPresenter(this);
    }

    private void initData() {
        serverTimeSP = parentActivity.getSharedPreferences(SPInfo.SPServerTime, Context.MODE_PRIVATE);
        SharedPreferences sp = parentActivity.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        getServerTime();

    }

    private void getServerTime() {
        hospitalizedServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_hospitalizedServerTime, SPInfo.ServerTimeDefaultValue);
        surgeryServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_surgeryServerTime, SPInfo.ServerTimeDefaultValue);
    }


    @Override
    public Intent startHospitalizedDetail(Intent intent, int position) {
//        int hospitalizedId = (int) dbList.get(position).getId();
        Hospitalized hospitalized = dbList.get(position);
//        intent.putExtra("hospitalizedId", hospitalizedId);
        intent.putExtra(AttentionHospitalizedDetailPresenter.HOSPITALIZED,hospitalized);
        return intent;
    }

    @Override
    public void start() {

    }

    @Override
    public void getData() {
        getServerTime();
        getHttpOperationData(surgeryServerTime);
    }

    /**
     * 获取住院接口网络数据
     *
     * @param serverTime
     */
    private void getHttpHospitalizedData(String serverTime) {
        HttpService.getHttpService().getHospitalRecords(serverTime, patientId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<HospitalRecordsBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "FragmentHospitalizedList getHttpHospitalizedData onCompleted");
                        insertHospitalizedDB();
                        getDBData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "FragmentHospitalizedList getHttpHospitalizedData onError");
//                        showEmptyLayoutState(LOADING_STATE_EMPTY);
//                        ToastUtil.showText(parentActivity,"请检查网络");
                        getDBData();
                    }

                    @Override
                    public void onNext(HospitalRecordsBean hospitalRecordsBean) {
                        Log.i("hcb", "FragmentHospitalizedList getHttpHospitalizedData onNext");
                        if (hospitalRecordsBean.getData().getSource().size() > 0) {

                            httpHospitalList = hospitalRecordsBean.getData().getSource();
                            SharedPreferences.Editor editor = serverTimeSP.edit();
                            hospitalizedServerTime = hospitalRecordsBean.getData().getServerTime();
                            editor.putString(SPInfo.ServerTimeKey_hospitalizedServerTime, hospitalizedServerTime);
                            editor.commit();
                        }
                    }
                });
    }

    /**
     * 插入数据库数据
     */
    private void insertHospitalizedDB() {
        hospitalizedDao = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getHospitalizedDao();
        for (int i = 0; i < httpHospitalList.size(); i++) {
            long id = httpHospitalList.get(i).getId();
            int type = httpHospitalList.get(i).getType();
            int status = httpHospitalList.get(i).getStatus();
            String dayCount = httpHospitalList.get(i).getDayCount();

            long inTime = httpHospitalList.get(i).getInTime();

            String patientId = httpHospitalList.get(i).getPatientId();
            String description = httpHospitalList.get(i).getNotes();
            String hospitalName = httpHospitalList.get(i).getHospitalName();
            long hospitalId = httpHospitalList.get(i).getHospitalId();
            String doctorName = httpHospitalList.get(i).getDoctorName();
            boolean isActive = httpHospitalList.get(i).isActive();

            Hospitalized hospitalized = new Hospitalized();
            hospitalized.setId(id);
            hospitalized.setStatus(status);
            hospitalized.setInTime(inTime);
            hospitalized.setDayCount(dayCount);
            hospitalized.setType(type);
            hospitalized.setPatientId(patientId);
            hospitalized.setDescription(description);
            hospitalized.setHospitalName(hospitalName);
            hospitalized.setHospitalId(hospitalId);
            hospitalized.setDoctorName(doctorName);
            hospitalized.setIsActive(isActive);

            long count = hospitalizedDao.queryBuilder().where(HospitalizedDao.Properties.Id.eq(id))
                    .count();
            if (count > 0) {
                hospitalizedDao.update(hospitalized);
            } else {
                hospitalizedDao.insert(hospitalized);
            }
        }
    }

    /**
     * 获取数据库数据
     */
    private void getDBData() {
        Observable.create(new Observable.OnSubscribe<SurgerySource>() {

            @Override
            public void call(Subscriber<? super SurgerySource> subscriber) {
                hospitalizedDao = ((MyApplication) (parentActivity.getApplication()))
                        .getAttentionDaoSession().getHospitalizedDao();

                List<Hospitalized> hospitalizedList = hospitalizedDao.queryBuilder()
                        .where(HospitalizedDao.Properties.IsActive.eq(true),
                                HospitalizedDao.Properties.PatientId.eq(patientId))
                        .orderDesc(HospitalizedDao.Properties.InTime).list();

                dbList = hospitalizedList;

//                ((MyApplication) (parentActivity.getApplication())).closeAttentionDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SurgerySource>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "FragmentHospitalizedList getDBData onCompleted");
                        if (dbList.size() > 0) {
                            view.exitData();
                            view.showData(dbList);
                        } else {
                            view.noData();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "FragmentHospitalizedList getDBData onError");
                        Log.i("hcb", "e " + e);
//                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                        view.noData();
                        ToastUtil.showText(parentActivity, "数据获取异常");
                    }

                    @Override
                    public void onNext(SurgerySource surgerySource) {
                        Log.i("hcb", "FragmentHospitalizedList getDBData onNext");
                    }
                });
    }

    /**
     * 获取手术网络数据
     *
     * @param serverTime
     */
    private void getHttpOperationData(String serverTime) {
        HttpService.getHttpService().getSurgeryRecords(serverTime, patientId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<SurgeryRecordsList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "FragmentHospitalizedList getHttpOperationData onCompleted");
                        insertOperationDB();
                        getHttpHospitalizedData(hospitalizedServerTime);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "FragmentHospitalizedList getHttpOperationData onError");
                        Log.i("hcb", "e " + e);
                        getData();
                    }

                    @Override
                    public void onNext(SurgeryRecordsList surgeryRecordsList) {
                        Log.i("hcb", "FragmentHospitalizedList getHttpOperationData onNext");
                        if (surgeryRecordsList.getData().getSource().size() > 0) {

                            httpOperationList = surgeryRecordsList.getData().getSource();
                            SharedPreferences.Editor editor = serverTimeSP.edit();
                            String surgeryServerTime = surgeryRecordsList.getData().getServerTime();
                            editor.putString(SPInfo.ServerTimeKey_surgeryServerTime, surgeryServerTime);
                            editor.commit();
                        }
                    }
                });
    }

    /**
     * 手术插入数据库数据
     */
    private void insertOperationDB() {
        if (httpOperationList.size() > 0) {//判断是否有最新数据需要入库
            surgeryRecordDao = ((MyApplication) (parentActivity.getApplication()))
                    .getAttentionDaoSession().getSurgeryRecordDao();
            surgeryMethodsDao = ((MyApplication) (parentActivity.getApplication()))
                    .getAttentionDaoSession().getSurgeryMethodsDao();

            for (int i = 0; i < httpOperationList.size(); i++) {
                //surgeryRecord 入库
                SurgeryRecordsList.SurgeryRecord surgeryRecord = httpOperationList.get(i).getSurgeryRecord();
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
                dbSurgeryRecord.setHospitalId(hospitalId);
                dbSurgeryRecord.setTherapeuticId(therapeuticId);
                dbSurgeryRecord.setTime(time);
                dbSurgeryRecord.setHospitalRecordId(hospitalRecordId);
                dbSurgeryRecord.setNotes(notes);
                dbSurgeryRecord.setDoctorName(doctorName);
                dbSurgeryRecord.setTherapeuticName(therapeuticName);
                dbSurgeryRecord.setPatientId(patientId);
                dbSurgeryRecord.setHospitalName(hospitalName);
                dbSurgeryRecord.setIsActive(isActive);
                long count = surgeryRecordDao.queryBuilder()
                        .where(SurgeryRecordDao.Properties.Id.eq(id)).count();//判断是否更新
                if (count > 0) {
                    surgeryRecordDao.update(dbSurgeryRecord);
                } else {
                    surgeryRecordDao.insert(dbSurgeryRecord);
                }

                //surgeryMethods入库
                List<SurgeryRecordsList.SurgeryMethods> list = httpOperationList.get(i).getSurgeryMethods();
                List<SurgeryMethods> dbSurgeryMethodsList = surgeryMethodsDao.queryBuilder().where(SurgeryMethodsDao.Properties
                        .SurgeryRecordId.eq(id)).list();
                for (SurgeryMethods surgeryMethods :dbSurgeryMethodsList){
                    surgeryMethodsDao.delete(surgeryMethods);
                }
                for (int a = 0; a < list.size(); a++) {
                    SurgeryRecordsList.SurgeryMethods httpSurgeryMethods = list.get(a);
                    int surgeryMethodId = httpSurgeryMethods.getSurgeryMethodId();
                    int surgeryRecordId = id;
                    String surgeryMethodName = httpSurgeryMethods.getSurgeryMethodName();
                    SurgeryMethods dbSurgeryMethods = new SurgeryMethods();
                    dbSurgeryMethods.setSurgeryMethodId((long) surgeryMethodId);
                    dbSurgeryMethods.setSurgeryRecordId((long) id);
                    dbSurgeryMethods.setSurgeryMethodName(surgeryMethodName);
                    surgeryMethodsDao.insert(dbSurgeryMethods);

                }
            }
        }


    }
}
