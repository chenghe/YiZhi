package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.github.clans.fab.FloatingActionButton;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.Interface.RecyclerViewAnimListener;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.adapter.ChemotherapyListAdapter;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.AttentionAddChemothRecordActivity;
import com.zhongmeban.attentionmodle.activity.AttentionChemothRecordDetailActivity;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.net.HttpResultFunc;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.DividerItemDecoration;
import de.greenrobot.dao.attention.Chemotherapy;
import de.greenrobot.dao.attention.ChemotherapyDao;
import de.greenrobot.dao.attention.ChemotherapyMedicine;
import de.greenrobot.dao.attention.ChemotherapyMedicineDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 化疗记录List
 * Created by Chengbin He on 2016/7/4.
 */
public class FragmentChemotherapyList extends BaseFragmentRefresh implements View.OnClickListener {

    private static  int SAVE = 1;
    private static  int DELETE = 2;

    private RecyclerView recyclerView;
    private ChemotherapyListAdapter mAdapter;
    private FloatingActionButton btnAdd;
    private ActivityAttentionList parentActivity;
    private SharedPreferences serverTimeSP;
    private ChemotherapyDao chemotherapyDao;//化疗
    private ChemotherapyMedicineDao chemotherapyMedicineDao;//化疗用药
    private String chemotherapyServerTime;//化疗Servertime
    private List<ChemotherapyRecordBean.Source> httpChemotherapyList
        = new ArrayList<>();//网络化疗记录

    private List<Chemotherapy> dbList = new ArrayList<Chemotherapy>();
    private List<ChemotherapyRecordBean.Record> mDatasRecord = new ArrayList<>();
    private List<List<ChemotherapyRecordBean.Medicines>> mDatasMedicine = new ArrayList<>();


    public static FragmentChemotherapyList newInstance() {

        Bundle args = new Bundle();

        FragmentChemotherapyList fragment = new FragmentChemotherapyList();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionList) context;
    }


    @Override
    protected int getLayoutIdRefresh() {
        return R.layout.fragment_attention_list_common;
    }


    @Override
    protected void initToolbarRefresh() {

    }


    @Override
    protected void initOnCreateView() {

        initEmptyView();
        serverTimeSP = parentActivity.getSharedPreferences(SPInfo.SPServerTime,
            Context.MODE_PRIVATE);
        chemotherapyServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_chemotherapyServerTime,
            SPInfo.ServerTimeDefaultValue);
        //        chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");

        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        btnAdd = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        mAdapter = new ChemotherapyListAdapter(parentActivity, mDatasRecord);
        mAdapter.setItemClickListener(recyclerItemClick);
        recyclerView.addItemDecoration(
            new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.addOnScrollListener(new RecyclerViewAnimListener(btnAdd));
        recyclerView.setAdapter(mAdapter);

        btnAdd.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        chemotherapyServerTime = serverTimeSP.getString(SPInfo.ServerTimeKey_chemotherapyServerTime,
            SPInfo.ServerTimeDefaultValue);
        getHttpChemotherapy(chemotherapyServerTime, parentActivity.patientId, parentActivity.token);
    }


    @Override
    public void onClick(View v) {

        Intent intentNew = new Intent(parentActivity, AttentionAddChemothRecordActivity.class);
        switch (v.getId()) {

            case R.id.fab:
                startActivityForResult(intentNew, SAVE);
                break;
        }

    }


    // 进入 记录详情页面
    AdapterClickInterface recyclerItemClick = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            List<ChemotherapyRecordBean.Medicines> medicines = mDatasMedicine.get(position);

            Intent intent = new Intent(parentActivity, AttentionChemothRecordDetailActivity.class);
            intent.putExtra(AttentionChemothRecordDetailActivity.EXTRA_CHEMOTH_RECORD_INFO,
                mDatasRecord.get(position));
            intent.putExtra(AttentionChemothRecordDetailActivity.EXTRA_CHEMOTH_RECORD_MEDICINES,
                (Serializable) medicines);
            startActivityForResult(intent, DELETE);
        }
        @Override
        public void onItemLongClick(View v, int position) {
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {//删除回掉
            //            chemotherapyServerTime = serverTimeSP.getString("chemotherapyServerTime", "0");
            //            chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");
            //            getHttpChemotherapy(chemotherapyServerTime, parentActivity.patientId, parentActivity.token);
        }
    }


    /**
     * 获取网络化疗数据
     */
    private void getHttpChemotherapy(String serverTime, String patientId, String token) {

        HttpService.getHttpService().getChemotherapyRecords(serverTime, patientId)
            .compose(RxUtil.<HttpResult<ChemotherapyRecordBean>>normalSchedulers())
            .map(new HttpResultFunc<ChemotherapyRecordBean>())
            .subscribe(new Action1<ChemotherapyRecordBean>() {
                @Override public void call(ChemotherapyRecordBean bean) {

                    showEmptyLayoutState(LOADING_STATE_SUCESS);

                    if (bean.getSource().size() > 0) {
                        httpChemotherapyList = bean.getSource();
                        SharedPreferences.Editor editor = serverTimeSP.edit();
                        chemotherapyServerTime = bean.getServerTime();
                        editor.putString(SPInfo.ServerTimeKey_chemotherapyServerTime,
                            chemotherapyServerTime);
                        editor.commit();
                    }
                    insertChemotherapyDB();
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }


    /**
     * 插入化疗数据库数据
     */
    private void insertChemotherapyDB() {
        if (httpChemotherapyList.size() > 0) {
            chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getChemotherapyDao();
            chemotherapyMedicineDao = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getChemotherapyMedicineDao();

            for (int i = 0; i < httpChemotherapyList.size(); i++) {
                //化疗入库
                ChemotherapyRecordBean.Record record = httpChemotherapyList.get(i)
                    .getRecord();
                long id = record.getId();
                String patientId = record.getPatientId();
                String notes = record.getNotes();
                String doctorName = record.getDoctorName();
                int status = record.getStatus();
                int chemotherapyAim = record.getChemotherapyAim();
                int times = record.getTimes();
                int weeksCount = record.getWeeksCount();
                int notesType = record.getNotesType();
                long startTime = record.getStartTime();
                boolean isActive = record.isActive();

                Chemotherapy chemotherapy = new Chemotherapy();
                chemotherapy.setId(id);
                chemotherapy.setPatientId(patientId);
                chemotherapy.setNotes(notes);
                chemotherapy.setDoctorName(doctorName);
                chemotherapy.setStatus(status);
                chemotherapy.setChemotherapyAim(chemotherapyAim);
                chemotherapy.setTimes(times);
                chemotherapy.setWeeksCount(weeksCount);
                chemotherapy.setNotesType(notesType);
                chemotherapy.setStartTime(startTime);
                chemotherapy.setIsActive(isActive);

                long count = chemotherapyDao.queryBuilder()
                    .where(ChemotherapyDao.Properties.Id.eq(id)).count();
                if (count > 0) {
                    chemotherapyDao.update(chemotherapy);
                } else {
                    chemotherapyDao.insert(chemotherapy);
                }

                //化疗用药入库
                List<ChemotherapyRecordBean.Medicines> medicinesList
                    = httpChemotherapyList.get(i).getMedicines();

                List<ChemotherapyMedicine> dbChemotherapyMedicineList
                    = chemotherapyMedicineDao.queryBuilder()
                    .where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(id)).list();

                for (ChemotherapyMedicine dbChemotherapyMedicine : dbChemotherapyMedicineList) {
                    chemotherapyMedicineDao.delete(dbChemotherapyMedicine);
                }

                for (int a = 0; a < medicinesList.size(); a++) {
                    long medicineId = medicinesList.get(a).getMedicineId();//药品ID
                    long chemotherapyId = id;//化疗ID
                    String medicineName = medicinesList.get(a).getMedicineName();
                    String showName = medicinesList.get(a).getShowName();
                    String chemicalName = medicinesList.get(a).getChemicalName();

                    ChemotherapyMedicine chemotherapyMedicine = new ChemotherapyMedicine();
                    chemotherapyMedicine.setMedicineId(medicineId);
                    chemotherapyMedicine.setChemotherapyId(chemotherapyId);
                    chemotherapyMedicine.setMedicineName(medicineName);
                    chemotherapyMedicine.setChemicalName(chemicalName);
                    chemotherapyMedicine.setShowName(showName);
                    chemotherapyMedicineDao.insert(chemotherapyMedicine);

                }

            }
        }
        getDBData();
    }


    /**
     * 获取数据库数据
     */
    private void getDBData() {
        Observable.create(new Observable.OnSubscribe<Chemotherapy>() {
            @Override
            public void call(Subscriber<? super Chemotherapy> subscriber) {

                chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
                    .getAttentionDaoSession().getChemotherapyDao();
                ChemotherapyMedicineDao chemotherapyMedicineDao =
                    ((MyApplication) (parentActivity.getApplication()))
                        .getAttentionDaoSession().getChemotherapyMedicineDao();

                dbList.clear();
                dbList = chemotherapyDao.queryBuilder()
                    .where(ChemotherapyDao.Properties.IsActive.eq(true),
                        ChemotherapyDao.Properties.PatientId.eq(parentActivity.patientId))
                    .orderDesc(ChemotherapyDao.Properties.Times).list();
                //转换Adapter数据
                mDatasRecord.clear();
                mDatasMedicine.clear();
                for (Chemotherapy chemotherapy : dbList) {

                    ChemotherapyRecordBean.Record recordBean = new ChemotherapyRecordBean.Record(
                        chemotherapy);
                    List<ChemotherapyMedicine> listMedicien = chemotherapyMedicineDao.queryBuilder()
                        .where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(
                            recordBean.getId()))
                        .list();
                    List<ChemotherapyRecordBean.Medicines> medicines = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < listMedicien.size(); i++) {
                        ChemotherapyMedicine medicine = listMedicien.get(i);
                        medicines.add(
                            new ChemotherapyRecordBean.Medicines(medicine.getChemicalName(),
                                medicine.getMedicineName(), medicine.getShowName(),
                                medicine.getMedicineId().intValue()));
                        if (i == listMedicien.size() - 1) {
                            sb.append(medicine.getMedicineName());
                        } else {
                            sb.append(medicine.getMedicineName() + "/");
                        }
                    }

                    mDatasMedicine.add(medicines);

                    recordBean.setMedicinesName(sb.toString());
                    mDatasRecord.add(recordBean);
                }

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Chemotherapy>() {
                @Override
                public void onCompleted() {
                    if (mDatasRecord.size() > 0) {
                        showEmptyLayoutState(LOADING_STATE_SUCESS);
                        mAdapter.updateData(mDatasRecord);
                    } else {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    }

                }

                @Override
                public void onError(Throwable e) {
                    showEmptyLayoutState(LOADING_STATE_EMPTY);
                    ToastUtil.showText(parentActivity, "数据获取异常");
                }

                @Override
                public void onNext(Chemotherapy chemotherapy) {
                    Log.i("hcb", "getDBData onNext");
                }
            });
    }
}
