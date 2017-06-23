//package com.zhongmeban.attentionmodle.activity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import com.zhongmeban.MyApplication;
//import com.zhongmeban.R;
//import com.zhongmeban.activity.ActivityBaseChemotherapy;
//import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyBody;
//import com.zhongmeban.fragment.FragmentAddExistChemotherapy;
//import com.zhongmeban.fragment.FragmentAddNewChemotherapy;
//import com.zhongmeban.fragment.FragmentChemotherapyMedicine;
//import de.greenrobot.dao.AttentionMedicine;
//import de.greenrobot.dao.Medicine;
//import de.greenrobot.dao.MedicineDao;
//import de.greenrobot.dao.attention.Chemotherapy;
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
// * 添加化疗Activity
// * Created by Chengbin He on 2016/7/7.
// */
//public class ActivityAttentionAddChemotherapy extends ActivityBaseChemotherapy {
//
//
//    private FragmentAddExistChemotherapy fragmentAddExistChemotherapy;//添加历史化疗
//    private FragmentAddNewChemotherapy fragmentAddNewChemotherapy;//添加新的化疗
//    //    private FragmentChemotherapyMedicine fragmentChemotherapyMedicine;//化疗用药
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private int fragmentType;//判断当前位置 1 新增化疗 2 化疗用药
//
//    public boolean ISNEW;//是否为新增化疗标记位
//    public boolean ISEDIT;//判断是否为编辑标记位
//    public String patientId;
//    public String token;
//    public long startTime;
//    public long endTime;
//    public long chemotherapyId;//编辑时，传入化疗ID
//    public int courseCount;//预计疗程次数
//    public int courseInterval;//疗程间隔天数
//    public int dayOfCourse;//每个疗程天数(疗程周期)
//    public int endReason;//结束原因 1.疗程结束 2.终止化疗 3.更换方案
//    public int status;//化疗状态 1.进行中 2.已结束
//    public int chemotherapyAim;//化疗目的
//    public String description;
//
//    public CreateOrUpdateChemotherapyBody body = new CreateOrUpdateChemotherapyBody();
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment_common);
//
//        Intent intent = getIntent();
//        ISNEW = intent.getBooleanExtra("ISNEW", false);
//        ISEDIT = intent.getBooleanExtra("ISEDIT", false);
//        chemotherapyId = intent.getLongExtra("chemotherapyId", 0);
//
//        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
//        patientId = sp.getString("patientId", "");
//        token = sp.getString("token", "");
//
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentChemotherapyMedicine = new FragmentChemotherapyMedicine();
//        if (ISEDIT) {//判断是否为编辑化疗
//            getDBData(chemotherapyId);
//        } else {
//            if (ISNEW) {
//                status = 1;
//                fragmentAddNewChemotherapy = new FragmentAddNewChemotherapy();
//                fragmentTransaction.add(R.id.fl_content, fragmentAddNewChemotherapy);
//                fragmentTransaction.commit();
//            } else {
//                status = 2;
//                fragmentAddExistChemotherapy = new FragmentAddExistChemotherapy();
//                fragmentTransaction.add(R.id.fl_content, fragmentAddExistChemotherapy);
//                fragmentTransaction.commit();
//            }
//        }
//
//
//
//        initRecord();
//    }
//
//    @Override
//    protected void initTitle() {
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (fragmentType == 2) {
//            backChemotherapyFragment();
//        } else {
//            finish();
//        }
//    }
//
//    public void startChemotherapyMedicineFragment() {
//        fragmentType = 2;
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fl_content, fragmentChemotherapyMedicine);
//        fragmentTransaction.commit();
//    }
//
//    @Override
//    public void backChemotherapyFragment() {
//        super.backChemotherapyFragment();
//        fragmentType = 1;
//        fragmentTransaction = fragmentManager.beginTransaction();
//        if (ISNEW) {
//            fragmentTransaction.replace(R.id.fl_content, fragmentAddNewChemotherapy);
//        } else {
//            fragmentTransaction.replace(R.id.fl_content, fragmentAddExistChemotherapy);
//        }
//        fragmentTransaction.commit();
//    }
//
//    /**
//     * 网络提交前 初始化
//     */
//    public void initRecord() {
//        //body.getRecord().setStartTime(startTime);
//        //body.getRecord().setEndTime(endTime);
//        //body.getRecord().setStatus(status);
//        //body.getRecord().setDayOfCourse(dayOfCourse);
//        //body.getRecord().setCourseCount(courseCount);
//        //body.getRecord().setCourseInterval(courseInterval);
//        //body.getRecord().setChemotherapyAim(chemotherapyAim);
//        //body.getRecord().setPatientId(patientId);
//        //body.getRecord().setEndReason(endReason);
//        //body.getRecord().setNotes(description);
//        //body.getRecord().setId(chemotherapyId);
//        initMedicine();
//    }
//
//    /**
//     * 网络提交前 初始化用药
//     */
//    public void initMedicine() {
//        if (chooseMedicineList.size() > 0) {
//            List<Integer> list = new ArrayList<Integer>();
//            for (int i = 0; i < chooseMedicineList.size(); i++) {
//                list.add((int) chooseMedicineList.get(i).getMedicine().getMedicineId());
//            }
//            body.setMedicineIds(list);
//        }
//    }
//
//    /**
//     * 获取数据库化疗数据
//     *
//     * @param id
//     */
//    private void getDBData(final long id) {
//        Observable.create(new Observable.OnSubscribe<Chemotherapy>() {
//            @Override
//            public void call(Subscriber<? super Chemotherapy> subscriber) {
//                ChemotherapyDao chemotherapyDao = ((MyApplication) getApplication())
//                        .getAttentionDaoSession().getChemotherapyDao();
//                List<Chemotherapy> dbList = chemotherapyDao.queryBuilder()
//                        .where(ChemotherapyDao.Properties.Id.eq(id)).list();
//                Chemotherapy chemotherapy = dbList.get(0);
//
//                subscriber.onNext(chemotherapy);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<Chemotherapy>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "getDBData onCompleted");
//                        getDBMedicine(id);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "getDBData onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(Chemotherapy chemotherapy) {
//                        Log.i("hcb", "getDBData onNext");
//                        startTime = chemotherapy.getStartTime();
//                        endTime = chemotherapy.getEndTime();
//                        status = chemotherapy.getStatus();
//                        courseCount = chemotherapy.getCourseCount();
//                        courseInterval = chemotherapy.getCourseInterval();
//                        endReason = chemotherapy.getEndReason();
//                        chemotherapyAim = chemotherapy.getChemotherapyAim();
//                        dayOfCourse = chemotherapy.getDayOfCourse();
//                        description = chemotherapy.getDescription();
//                    }
//                });
//    }
//
//    private void getDBMedicine(final long chemotherapyId) {
//        Observable.create(new Observable.OnSubscribe<ChemotherapyMedicine>() {
//
//            @Override
//            public void call(Subscriber<? super ChemotherapyMedicine> subscriber) {
//                ChemotherapyMedicineDao chemotherapyMedicineDao = ((MyApplication) getApplication())
//                        .getAttentionDaoSession().getChemotherapyMedicineDao();
//                MedicineDao medicineDao = ((MyApplication) getApplication()).getDaoSession().getMedicineDao();
//
//                List<ChemotherapyMedicine> chemotherapyMedicineList = chemotherapyMedicineDao.queryBuilder()
//                        .where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(chemotherapyId)).list();
//                List<Long> medicineIdList = new ArrayList<Long>();
//
//                for (int i = 0; i < chemotherapyMedicineList.size(); i++) {
//                    medicineIdList.add(chemotherapyMedicineList.get(i).getMedicineId());
//                }
//                Log.i("hcb","medicineIdList.size()"+medicineIdList.size());
//                if (medicineIdList.size() > 0) {
//                    //判断是否有用药
//                    List<Medicine> medicineList = medicineDao.queryBuilder()
//                            .where(MedicineDao.Properties.MedicineId.in(medicineIdList)).list();
//                    List<AttentionMedicine> attentionMedicineList = new ArrayList<AttentionMedicine>();
//                    for (int a = 0; a < medicineIdList.size(); a++) {
//                        Medicine medicine = medicineList.get(a);
//                        AttentionMedicine attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
//                        attentionMedicineList.add(attentionMedicine);
//                    }
//                    chooseMedicineList.clear();
//                    chooseMedicineList.addAll(attentionMedicineList);
//                }
//
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ChemotherapyMedicine>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "getDBMedicine onCompleted");
//                        if (ISNEW) {
//                            status = 1;
//                            fragmentAddNewChemotherapy = new FragmentAddNewChemotherapy();
//                            fragmentTransaction.add(R.id.fl_content, fragmentAddNewChemotherapy);
//                            fragmentTransaction.commit();
//                        } else {
//                            status = 2;
//                            fragmentAddExistChemotherapy = new FragmentAddExistChemotherapy();
//                            fragmentTransaction.add(R.id.fl_content, fragmentAddExistChemotherapy);
//                            fragmentTransaction.commit();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "getDBMedicine onError");
//                    }
//
//                    @Override
//                    public void onNext(ChemotherapyMedicine chemotherapyMedicine) {
//                        Log.i("hcb", "getDBMedicine onNext");
//                    }
//                });
//    }
//
//}
