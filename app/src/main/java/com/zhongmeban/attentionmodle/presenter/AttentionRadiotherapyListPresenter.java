package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.zhongmeban.MyApplication;
import com.zhongmeban.attentionmodle.contract.AttentionRadiotherapyListContract;
import com.zhongmeban.bean.RadiotherapyRecordsListBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.Radiotherapy;
import de.greenrobot.dao.attention.RadiotherapyDao;
import de.greenrobot.dao.attention.RadiotherapySuspendedRecords;
import de.greenrobot.dao.attention.RadiotherapySuspendedRecordsDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2017/1/9.
 */

public class AttentionRadiotherapyListPresenter implements AttentionRadiotherapyListContract.Presenter{

    private Activity mContext;
    private AttentionRadiotherapyListContract.View view;
    private RadiotherapyDao radiotherapyDao;
    private List<RadiotherapyRecordsListBean.Source> httpList = new ArrayList<RadiotherapyRecordsListBean.Source>();
    private List<Radiotherapy> dbList = new ArrayList<Radiotherapy>();
    private String patientId;
    private String token;
    private SharedPreferences serverTimeSP;
    private String radiotherapyServerTime;

    public AttentionRadiotherapyListPresenter(Activity mContext, AttentionRadiotherapyListContract.View view) {
        this.mContext = mContext;
        this.view = view;
        view.setPresenter(this);
    }

    private void initData() {
        serverTimeSP = mContext.getSharedPreferences(SPInfo.SPServerTime, Context.MODE_PRIVATE);
        SharedPreferences sp = mContext.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        getServerTime();

    }

    @Override
    public void start() {
        initData();
    }

    private void getServerTime() {
        radiotherapyServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_surgeryServerTime, SPInfo.ServerTimeDefaultValue);
    }

    @Override
    public void getData() {
        getServerTime();
        getHttpData(radiotherapyServerTime);
    }

    @Override
    public Intent startRadiotherapyDetail(Intent intent, int position) {
        intent.putExtra(AttentionRadiotherapyDetailPresenter.Radiotherapy,dbList.get(position));
        return intent;
    }

    /**
     * 获取网络数据
     *
     * @param serverTime

     */
    private void getHttpData(String serverTime) {
        HttpService.getHttpService().getRadiotherapyRecordsList(serverTime, patientId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<RadiotherapyRecordsListBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "FragmentRadiotherapyList getHttpData onCompleted");
                        insertDB();
                        getDBData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "FragmentRadiotherapyList getHttpData onError");
                        Log.i("hcb", "e" + e);
                        getDBData();
                    }

                    @Override
                    public void onNext(RadiotherapyRecordsListBean radiotherapyRecordsListBean) {
                        Log.i("hcb", "FragmentRadiotherapyList getHttpData onNext");
//                        if (radiotherapyRecordsListBean.getData().getSource().size() > 0) {

                        httpList = radiotherapyRecordsListBean.getData().getSource();
                        SharedPreferences.Editor editor = serverTimeSP.edit();
                        String radiotherapyServerTime = radiotherapyRecordsListBean.getData().getServerTime();
                        editor.putString(SPInfo.ServerTimeKey_radiotherapyServerTime, radiotherapyServerTime);
                        editor.commit();
//                        }
                    }
                });
    }

    /**
     * 插入数据库数据
     */
    private void insertDB() {
        if (httpList.size() > 0) {
            //判断是否有最新数据需要入库
            radiotherapyDao = ((MyApplication) (mContext.getApplication()))
                    .getAttentionDaoSession().getRadiotherapyDao();

            for (int i = 0; i < httpList.size(); i++) {
                Log.i("hcbtest","httpList.size()" +httpList.size());
                RadiotherapyRecordsListBean.Source source = httpList.get(i);
                long id = source.getId();
                long startTime = source.getStartTime();
                int resultType = source.getResultType();
                String predictDosage = source.getPredictDosage();
                String weeksCount = source.getWeeksCount();
                String currentCount = source.getCurrentCount();
                String patientId = source.getPatientId();
                String notes = source.getNotes();
                boolean isActive = source.isActive();

                Radiotherapy  radiotherapy = new Radiotherapy();
                radiotherapy.setId(id);
                radiotherapy.setStartTime(startTime);
                radiotherapy.setResultType(resultType);
                radiotherapy.setPredictDosage(predictDosage);
                radiotherapy.setWeeksCount(weeksCount);
                radiotherapy.setCurrentCount(currentCount);
                radiotherapy.setPatientId(patientId);
                radiotherapy.setNotes(notes);
                radiotherapy.setIsActive(isActive);

                long count = radiotherapyDao.queryBuilder()
                        .where(RadiotherapyDao.Properties.Id.eq(id)).count();//判断是否更新
                if (count > 0) {
                    radiotherapyDao.update(radiotherapy);
                } else {
                    radiotherapyDao.insert(radiotherapy);
                }

            }
        }

    }



    /**
     * 获取数据库数据
     */
    private void getDBData() {
        Observable.create(new Observable.OnSubscribe<Radiotherapy>() {
            @Override
            public void call(Subscriber<? super Radiotherapy> subscriber) {
                radiotherapyDao = ((MyApplication) (mContext.getApplication()))
                        .getAttentionDaoSession().getRadiotherapyDao();

                dbList = radiotherapyDao.queryBuilder().where(RadiotherapyDao.Properties.IsActive.eq(true),
                        RadiotherapyDao.Properties.PatientId.eq(patientId))
                        .orderDesc(RadiotherapyDao.Properties.StartTime).list();

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Radiotherapy>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "FragmentRadiotherapyList getDBData onCompleted");
                        if (dbList.size() > 0) {
                            view.exitData();
                            view.showData(dbList);
                        } else {
                            view.noData();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "FragmentRadiotherapyList getDBData onError");
                        Log.i("hcb", "e" + e);
                        view.noData();
                        ToastUtil.showText(mContext, "数据获取异常");
                    }

                    @Override
                    public void onNext(Radiotherapy radiotherapy) {
                        Log.i("hcb", "FragmentRadiotherapyList getDBData onNext");
                    }
                });

    }


}
