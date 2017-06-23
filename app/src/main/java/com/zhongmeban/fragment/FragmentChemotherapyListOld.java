//package com.zhongmeban.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import com.github.clans.fab.FloatingActionButton;
//import com.zhongmeban.AdapterClickInterface;
//import com.zhongmeban.MyApplication;
//import com.zhongmeban.R;
//import com.zhongmeban.activity.ActivityStopChemotherapy;
//import com.zhongmeban.adapter.ChemotherapyListAdapter;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddChemotherapy;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionChemotherapyDetail;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
//import com.zhongmeban.attentionmodle.activity.AttentionAddChemothRecordActivity;
//import com.zhongmeban.base.BaseFragmentRefresh;
//import com.zhongmeban.bean.ChemotherapyRecordBean;
//import com.zhongmeban.bean.ChemotherapyListBean;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.ToastUtil;
//import com.zhongmeban.view.DividerItemDecoration;
//import de.greenrobot.dao.attention.Chemotherapy;
//import de.greenrobot.dao.attention.ChemotherapyCourse;
//import de.greenrobot.dao.attention.ChemotherapyCourseDao;
//import de.greenrobot.dao.attention.ChemotherapyCourseMedicine;
//import de.greenrobot.dao.attention.ChemotherapyCourseMedicineDao;
//import de.greenrobot.dao.attention.ChemotherapyDao;
//import de.greenrobot.dao.attention.ChemotherapyMedicine;
//import de.greenrobot.dao.attention.ChemotherapyMedicineDao;
//import java.util.ArrayList;
//import java.util.List;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 化疗记录List
// * Created by Chengbin He on 2016/7/4.
// */
//public class FragmentChemotherapyListOld extends BaseFragmentRefresh implements View.OnClickListener {
//    private RecyclerView recyclerView;
//    private ChemotherapyListAdapter mAdapter;
//    private FloatingActionButton btnAdd;
//    private ActivityAttentionList parentActivity;
//    private SharedPreferences serverTimeSP;
//    private ChemotherapyDao chemotherapyDao;//化疗
//    private ChemotherapyCourseDao chemotherapyCourseDao;//化疗疗程
//    private ChemotherapyMedicineDao chemotherapyMedicineDao;//化疗用药
//    private String chemotherapyServerTime;//化疗Servertime
//    private String chemotherapyCourseServerTime;//化疗疗程Servertime
//    private List<ChemotherapyRecordBean.Source> httpChemotherapyList
//        = new ArrayList<ChemotherapyRecordBean.Source>();//网络化疗记录
//    private List<ChemotherapyRecordBean.Source> httpChemotherapyCourseList
//        = new ArrayList<ChemotherapyRecordBean.Source>();//网络化疗疗程记录
//    private List<Chemotherapy> dbList = new ArrayList<Chemotherapy>();
//    private List<ChemotherapyListBean> adapterList = new ArrayList<>();
//
//
//    public static FragmentChemotherapyListOld newInstance() {
//
//        Bundle args = new Bundle();
//
//        FragmentChemotherapyListOld fragment = new FragmentChemotherapyListOld();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionList) context;
//    }
//
//
//    @Override
//    protected int getLayoutIdRefresh() {
//        return R.layout.fragment_attention_list_common;
//    }
//
//
//    @Override
//    protected void initToolbarRefresh() {
//
//    }
//
//
//    @Override
//    protected void initOnCreateView() {
//
//        initEmptyView();
//        showEmptyLayoutState(LOADING_STATE_SUCESS);
//
//        serverTimeSP = parentActivity.getSharedPreferences("serverTime", Context.MODE_PRIVATE);
//        chemotherapyServerTime = serverTimeSP.getString("chemotherapyServerTime", "0");
//        chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");
//
//        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
//        mAdapter = new ChemotherapyListAdapter(parentActivity, adapterList);
//        mAdapter.setItemClickListener(recyclerItemClick);
//        recyclerView.addItemDecoration(
//            new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//        recyclerView.setAdapter(mAdapter);
//
//        btnAdd = (FloatingActionButton) mRootView.findViewById(R.id.fab);
//        btnAdd.setOnClickListener(this);
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        chemotherapyServerTime = serverTimeSP.getString("chemotherapyServerTime", "0");
//        chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");
//        getHttpChemotherapy(chemotherapyServerTime, parentActivity.patientId, parentActivity.token);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//        Intent intent = new Intent(parentActivity, ActivityAttentionAddChemotherapy.class);
//        Intent intentNew = new Intent(parentActivity, AttentionAddChemothRecordActivity.class);
//        switch (v.getId()) {
///*            case R.id.float_button_new:
//                if (checkHasNewChemotherapy()) {
//                    //                    ToastUtil.showText(parentActivity, "目前有增在进行的化疗，请先停止在添加");
//                    showDeleteDialog(parentActivity, new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(
//                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            stopChemotherapy();
//                        }
//                    }, "是否要结束正在进行的化疗？");
//                } else {
//                    intent.putExtra("ISNEW", true);
//                    startActivityForResult(intent, 1);
//                }
//                floatingActionMenu.close(true);
//                break;*/
//            case R.id.fab:
//                //intent.putExtra("ISNEW", false);
//                //floatingActionMenu.close(true);
//                //startActivityForResult(intent, 1);
//                startActivityForResult(intentNew, 1);
//                break;
//        }
//
//    }
//
//
//    AdapterClickInterface recyclerItemClick = new AdapterClickInterface() {
//        @Override
//        public void onItemClick(View v, int position) {
//
//            Intent intent = new Intent(parentActivity, ActivityAttentionChemotherapyDetail.class);
//            long chemotherapyId = dbList.get(position).getId();
//            intent.putExtra("chemotherapyId", chemotherapyId);
//            startActivityForResult(intent, 1);
//        }
//
//
//        @Override
//        public void onItemLongClick(View v, int position) {
//
//        }
//    };
//
//
//    private void stopChemotherapy() {
//        chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
//            .getAttentionDaoSession().getChemotherapyDao();
//        List<Chemotherapy> list = chemotherapyDao.queryBuilder()
//            .where(chemotherapyDao.queryBuilder().
//                and(ChemotherapyDao.Properties.PatientId.eq(parentActivity.patientId),
//                    ChemotherapyDao.Properties.IsActive.eq(true),
//                    ChemotherapyDao.Properties.Status.eq(1)))
//            .list();
//        Chemotherapy chemotherapy = list.get(0);
//        Intent intent = new Intent(parentActivity, ActivityStopChemotherapy.class);
//        intent.putExtra("chemotherapy", chemotherapy);
//        intent.putExtra("token", parentActivity.token);
//        startActivityForResult(intent, 1);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 200) {//删除回掉
//            //            chemotherapyServerTime = serverTimeSP.getString("chemotherapyServerTime", "0");
//            //            chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");
//            //            getHttpChemotherapy(chemotherapyServerTime, parentActivity.patientId, parentActivity.token);
//        }
//    }
//
//
//    /**
//     * 判断时候有正在进行的化疗
//     */
//    private boolean checkHasNewChemotherapy() {
//        chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
//            .getAttentionDaoSession().getChemotherapyDao();
//        List<Chemotherapy> list = chemotherapyDao.queryBuilder()
//            .where(chemotherapyDao.queryBuilder().
//                and(ChemotherapyDao.Properties.PatientId.eq(parentActivity.patientId),
//                    ChemotherapyDao.Properties.IsActive.eq(true),
//                    ChemotherapyDao.Properties.Status.eq(1)))
//            .list();
//        return list.size() > 0;
//    }
//
//
//    /**
//     * 获取网络化疗数据
//     */
//    private void getHttpChemotherapy(String serverTime, String patientId, String token) {
//        HttpService.getHttpService().getChemotherapyRecords(serverTime, patientId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .subscribe(new Subscriber<ChemotherapyRecordBean>() {
//                @Override
//                public void onCompleted() {
//                    Log.i("hcb", "getHttpChemotherapy onCompleted");
//                    insertChemotherapyDB();
//                    getHttpChemotherapyCords(chemotherapyCourseServerTime, parentActivity.patientId,
//                        parentActivity.token);
//                }
//
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.i("hcb", "getHttpChemotherapy onError");
//                    Log.i("onNext", "e" + e);
//                    ToastUtil.showText(parentActivity, "请检查网络");
//                    showEmptyLayoutState(LOADING_STATE_EMPTY);
//
//                }
//
//
//                @Override
//                public void onNext(ChemotherapyRecordBean bean) {
//
//                    showEmptyLayoutState(LOADING_STATE_SUCESS);
//                    if (bean == null || bean.getData() == null) {
//                        showEmptyLayoutState(LOADING_STATE_NO_NET);
//                        return;
//                    }
//
//                    if (bean.getData().getSource().size() > 0) {
//                        httpChemotherapyList = bean.getData().getSource();
//                        SharedPreferences.Editor editor = serverTimeSP.edit();
//                        chemotherapyServerTime = bean.getData().getServerTime();
//                        editor.putString("chemotherapyServerTime", chemotherapyServerTime);
//                        Log.i("hcbtest", "chemotherapyServerTime" + bean.getData().getServerTime());
//                        editor.commit();
//                    }
//                }
//            });
//    }
//
//
//    /**
//     * 插入化疗数据库数据
//     */
//    private void insertChemotherapyDB() {
//        if (httpChemotherapyList.size() > 0) {
//            chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
//                .getAttentionDaoSession().getChemotherapyDao();
//            chemotherapyMedicineDao = ((MyApplication) (parentActivity.getApplication()))
//                .getAttentionDaoSession().getChemotherapyMedicineDao();
//
//            for (int i = 0; i < httpChemotherapyList.size(); i++) {
//                //化疗入库
//                ChemotherapyRecordBean.Record record = httpChemotherapyList.get(i)
//                    .getRecord();
//                long id = record.getId();
//                String patientId = record.getPatientId();
//                String description = record.getNotes();
//                int endReason = record.getEndReason();
//                int dayOfCourse = record.getDayOfCourse();
//                int courseCount = record.getCourseCount();
//                int courseInterval = record.getCourseInterval();
//                int status = record.getStatus();
//                int chemotherapyAim = record.getChemotherapyAim();
//                int times = record.getTimes();
//                long startTime = record.getStartTime();
//                long endTime = record.getEndTime();
//                long chemotherapyRecordId = record.getChemotherapyRecordId();
//                boolean isActive = record.isActive();
//
//                Chemotherapy chemotherapy = new Chemotherapy(id, startTime, endTime, courseCount,
//                    courseInterval,
//                    status, endReason, chemotherapyAim, dayOfCourse, times, description, patientId,
//                    isActive);
//                long count = chemotherapyDao.queryBuilder()
//                    .where(ChemotherapyDao.Properties.Id.eq(id)).count();
//                if (count > 0) {
//                    chemotherapyDao.update(chemotherapy);
//                } else {
//                    chemotherapyDao.insert(chemotherapy);
//                }
//
//                //化疗用药入库
//                List<ChemotherapyRecordBean.Medicines> medicinesList
//                    = httpChemotherapyList.get(i).getMedicines();
//
//                List<ChemotherapyMedicine> dbChemotherapyMedicineList
//                    = chemotherapyMedicineDao.queryBuilder()
//                    .where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(id)).list();
//
//                for (ChemotherapyMedicine dbChemotherapyMedicine : dbChemotherapyMedicineList) {
//                    chemotherapyMedicineDao.delete(dbChemotherapyMedicine);
//                }
//
//                for (int a = 0; a < medicinesList.size(); a++) {
//                    long medicineId = medicinesList.get(a).getMedicineId();//药品ID
//                    long chemotherapyId = id;//化疗ID
//                    String medicineName = medicinesList.get(a).getMedicineName();
//
//                    ChemotherapyMedicine chemotherapyMedicine = new ChemotherapyMedicine();
//                    chemotherapyMedicine.setMedicineId(medicineId);
//                    chemotherapyMedicine.setChemotherapyId(id);
//                    chemotherapyMedicine.setMedicineName(medicineName);
//                    chemotherapyMedicineDao.insert(chemotherapyMedicine);
//
//                }
//
//            }
//        }
//        getDBData();
//    }
//
//
//    /**
//     * 获取网络化疗 疗程数据
//     */
//    private void getHttpChemotherapyCords(String serverTime, String patientId, String token) {
//        HttpService.getHttpService().getChemotherapyCordsRecords(serverTime, patientId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .subscribe(new Subscriber<ChemotherapyRecordBean>() {
//                @Override
//                public void onCompleted() {
//                    Log.i("hcb", "getHttpChemotherapyCords onCompleted");
//                    insertChemotherapyCordsDB();
//                }
//
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.i("hcb", "getHttpChemotherapyCords onError");
//                    Log.i("hcb", "e" + e);
//                    ToastUtil.showText(parentActivity, "请检查网络");
//                    showEmptyLayoutState(LOADING_STATE_EMPTY);
//                }
//
//
//                @Override
//                public void onNext(ChemotherapyRecordBean chemotherapyAndChemotherapyCourseBean) {
//                    Log.i("hcb", "getHttpChemotherapyCords onNext");
//                    if (chemotherapyAndChemotherapyCourseBean.getData().getSource().size() > 0) {
//                        httpChemotherapyCourseList = chemotherapyAndChemotherapyCourseBean.getData()
//                            .getSource();
//                        SharedPreferences.Editor editor = serverTimeSP.edit();
//                        chemotherapyCourseServerTime
//                            = chemotherapyAndChemotherapyCourseBean.getData().getServerTime();
//                        editor.putString("chemotherapyCourseServerTime",
//                            chemotherapyCourseServerTime);
//                        Log.i("hcbtest", "chemotherapyCourseServerTime" +
//                            chemotherapyAndChemotherapyCourseBean.getData().getServerTime());
//                        editor.commit();
//                    }
//                }
//            });
//    }
//
//
//    /**
//     * 插入化疗 疗程数据库数据
//     */
//    private void insertChemotherapyCordsDB() {
//        if (httpChemotherapyCourseList.size() > 0) {
//            chemotherapyCourseDao = ((MyApplication) (parentActivity.getApplication()))
//                .getAttentionDaoSession().getChemotherapyCourseDao();
//            ChemotherapyCourseMedicineDao chemotherapyCourseMedicineDao
//                = ((MyApplication) (parentActivity.getApplication()))
//                .getAttentionDaoSession().getChemotherapyCourseMedicineDao();
//
//            for (int i = 0; i < httpChemotherapyCourseList.size(); i++) {
//                //化疗疗程入库
//                ChemotherapyRecordBean.Record record
//                    = httpChemotherapyCourseList.get(i).getRecord();
//                long id = record.getId();
//                String patientId = record.getPatientId();
//                String description = record.getNotes();
//                int endReason = record.getEndReason();
//                int dayOfCourse = record.getDayOfCourse();
//                int courseCount = record.getCourseCount();
//                int courseInterval = record.getCourseInterval();
//                int status = record.getStatus();
//                int chemotherapyAim = record.getChemotherapyAim();
//                int times = record.getTimes();
//                long startTime = record.getStartTime();
//                long endTime = record.getEndTime();
//                long chemotherapyRecordId = record.getChemotherapyRecordId();
//                boolean isActive = record.isActive();
//
//                ChemotherapyCourse chemotherapyCourse = new ChemotherapyCourse(id,
//                    chemotherapyRecordId, startTime, endTime, endReason,
//                    status, times, description, patientId, isActive);
//                long count = chemotherapyCourseDao.queryBuilder()
//                    .where(ChemotherapyCourseDao.Properties.Id.eq(id)).count();
//                if (count > 0) {
//                    chemotherapyCourseDao.update(chemotherapyCourse);
//                } else {
//                    chemotherapyCourseDao.insert(chemotherapyCourse);
//                }
//
//                //化疗疗程用药入库
//                List<ChemotherapyRecordBean.Medicines> medicinesList
//                    = httpChemotherapyCourseList.get(i).getMedicines();
//                List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicineList
//                    = chemotherapyCourseMedicineDao.queryBuilder()
//                    .where(ChemotherapyCourseMedicineDao.Properties.ChemotherapyCourseId.eq(id))
//                    .list();
//
//                for (ChemotherapyCourseMedicine dbChemotherapyCourseMedicine : dbChemotherapyCourseMedicineList) {
//                    chemotherapyCourseMedicineDao.delete(dbChemotherapyCourseMedicine);
//                }
//
//                for (int a = 0; a < medicinesList.size(); a++) {
//                    long medicineId = medicinesList.get(a).getMedicineId();
//                    long chemotherapyCourseId = id;
//                    String medicineName = medicinesList.get(a).getMedicineName();
//
//                    ChemotherapyCourseMedicine chemotherapyCourseMedicine
//                        = new ChemotherapyCourseMedicine();
//                    chemotherapyCourseMedicine.setMedicineId(medicineId);
//                    chemotherapyCourseMedicine.setChemotherapyCourseId(chemotherapyCourseId);
//                    chemotherapyCourseMedicine.setMedicineName(medicineName);
//
//                    chemotherapyCourseMedicineDao.insert(chemotherapyCourseMedicine);
//
//                }
//
//            }
//        }
//    }
//
//
//    /**
//     * 获取数据库数据
//     */
//    private void getDBData() {
//        Observable.create(new Observable.OnSubscribe<Chemotherapy>() {
//            @Override
//            public void call(Subscriber<? super Chemotherapy> subscriber) {
//
//                chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
//                    .getAttentionDaoSession().getChemotherapyDao();
//                chemotherapyCourseDao = ((MyApplication) (parentActivity.getApplication()))
//                    .getAttentionDaoSession().getChemotherapyCourseDao();
//
//                dbList.clear();
//                dbList = chemotherapyDao.queryBuilder()
//                    .where(ChemotherapyDao.Properties.IsActive.eq(true),
//                        ChemotherapyDao.Properties.PatientId.eq(parentActivity.patientId))
//                    .orderDesc(ChemotherapyDao.Properties.Times).list();
//                //转换Adapter数据
//                adapterList.clear();
//                for (Chemotherapy chemotherapy : dbList) {
//                    List<ChemotherapyCourse> dbChemotherapyCourseist
//                        = chemotherapyCourseDao.queryBuilder()
//                        .where(chemotherapyCourseDao.queryBuilder()
//                            .and(ChemotherapyCourseDao.Properties.ChemotherapyRecordId.eq(
//                                chemotherapy.getId()),
//                                ChemotherapyCourseDao.Properties.PatientId.eq(
//                                    parentActivity.patientId),
//                                ChemotherapyCourseDao.Properties.IsActive.eq(true)))
//                        .list();
//                    ChemotherapyListBean bean = new ChemotherapyListBean(chemotherapy,
//                        dbChemotherapyCourseist);
//                    adapterList.add(bean);
//                }
//
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Subscriber<Chemotherapy>() {
//                @Override
//                public void onCompleted() {
//                    Log.i("hcb", "getDBData onCompleted");
//                    if (adapterList.size() > 0) {
//                        showEmptyLayoutState(LOADING_STATE_SUCESS);
//                        mAdapter.updateData(adapterList);
//                    } else {
//                        showEmptyLayoutState(LOADING_STATE_EMPTY);
//                    }
//
//                }
//
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.i("hcb", "getDBData onError");
//                    Log.i("hcb", "e" + e);
//                    showEmptyLayoutState(LOADING_STATE_EMPTY);
//                    ToastUtil.showText(parentActivity, "数据获取异常");
//                }
//
//
//                @Override
//                public void onNext(Chemotherapy chemotherapy) {
//                    Log.i("hcb", "getDBData onNext");
//                }
//            });
//    }
//}
