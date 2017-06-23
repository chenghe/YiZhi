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
//import com.zhongmeban.base.BaseActivity;
//import com.zhongmeban.bean.postbody.CreateOrUpdateRadiotherapyBody;
//import com.zhongmeban.fragment.FragmentAddExistRadiotherapy;
//import com.zhongmeban.fragment.FragmentAddNewRadiotherapy;
//import de.greenrobot.dao.attention.Radiotherapy;
//import de.greenrobot.dao.attention.RadiotherapyDao;
//import java.util.List;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 添加放疗Activity
// * Created by Chengbin He on 2016/7/13.
// */
//public class ActivityAttentionAddRadiotherapy extends BaseActivity {
//
//    private boolean ISNEW;//是否为新增放疗标记位
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private FragmentAddNewRadiotherapy fragmentAddNewRadiotherapy;//添加新的放疗
//    private FragmentAddExistRadiotherapy fragmentAddExistRadiotherapy;//添加历史放疗
//    private Radiotherapy radiotherapy;//数据库获取数据
//
//    public boolean ISEDIT;//是否为编辑标记
//    public long startTime;//开始时间
//    public long endTime;//结束时间
//    public String predictDosage;//预计总剂量数
//    public String times;//放疗次数
//    public String currentDosage;//当前放疗剂量
//    public String currentCount;//当前放疗次数
//    public int radiotherapyId;//放疗疗程id
//    public String token;
//    public String patientId;
//    public String notes;//备注
//    public int status; //状态：1.进行中 2.结束 3.停止
//    public int endReason;//结束原因 当status为3时，需要此字段
//    public CreateOrUpdateRadiotherapyBody body = new CreateOrUpdateRadiotherapyBody();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment_common);
//
//        Intent intent = getIntent();
//        ISNEW = intent.getBooleanExtra("ISNEW", false);
//        radiotherapyId = intent.getIntExtra("radiotherapyId", 0);
//        ISEDIT = intent.getBooleanExtra("ISEDIT", false);
//
//
//        SharedPreferences userInfoSP = getSharedPreferences("userInfo", MODE_PRIVATE);
//        token = userInfoSP.getString("token", "");
//        patientId = userInfoSP.getString("patientId", "");
//
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        if (ISEDIT) {
//            getDBData(radiotherapyId);
//        } else {//非编辑页面进入
//            if (ISNEW) {
//                status = 1;
//                fragmentAddNewRadiotherapy = new FragmentAddNewRadiotherapy();
//                fragmentTransaction.add(R.id.fl_content, fragmentAddNewRadiotherapy);
//            } else {
////                status = 2;
//                fragmentAddExistRadiotherapy = new FragmentAddExistRadiotherapy();
//                fragmentTransaction.add(R.id.fl_content, fragmentAddExistRadiotherapy);
//            }
//            fragmentTransaction.commit();
//        }
//
//    }
//
//    @Override
//    protected void initTitle() {
////        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
////        String titleName = "";
////        if (ISNEW){
////            titleName = "添加新放疗";
////        }else {
////            titleName = "添加历史放疗";
////        }
////        title.slideCentertext(titleName);
////        title.backSlid(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                finish();
////            }
////        });
//    }
//
//
//    public void initNewRadiotherapy() {
//        if(currentDosage == null||currentDosage.equals("")){
//            currentDosage = "0";
//        }
//        if(currentCount == null||currentCount.equals("")){
//            currentCount = "0";
//        }
//        if(predictDosage == null||predictDosage.equals("")){
//            predictDosage = "0";
//        }
////        body.setStartTime(startTime);
////        body.setEndTime(endTime);
////        body.setStatus(status);
////        body.setPatientId(patientId);
////        body.setPredictDosage(predictDosage);
////        body.setCurrentCount(currentCount);
////        body.setCurrentDosage(currentDosage);
////        body.setStatus(status);
////        body.setEndTime(endTime);
////        body.setTimes(times);
////        body.setNotes(notes);
////        body.setEndReason(endReason);
////        body.setId(radiotherapyId);
//    }
//
//    /**
//     * 获取数据库数据
//     */
//    private void getDBData(final int id) {
//        Observable.create(new Observable.OnSubscribe<Radiotherapy>() {
//            @Override
//            public void call(Subscriber<? super Radiotherapy> subscriber) {
//                RadiotherapyDao radiotherapyDao = ((MyApplication) getApplication())
//                        .getAttentionDaoSession().getRadiotherapyDao();
//                List<Radiotherapy> dbList = radiotherapyDao.queryBuilder().where(radiotherapyDao.queryBuilder()
//                                .and(RadiotherapyDao.Properties.IsActive.eq(true),
//                                        RadiotherapyDao.Properties.PatientId.eq(patientId)),
//                        RadiotherapyDao.Properties.Id.eq(id)).list();
//                radiotherapy = dbList.get(0);
////                ((MyApplication) (parentActivity.getApplication())).closeAttentionDB();
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Radiotherapy>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "ActivityAttentionAddRadiotherapy getDBData onCompleted");
//                        if (ISNEW){
//                            //正在进行的放疗编辑
//                            startTime = radiotherapy.getStartTime();
//                            predictDosage = radiotherapy.getPredictDosage() +"";
//                            currentCount = radiotherapy.getCurrentCount()+"";
//                            currentDosage = radiotherapy.getCurrentDosage()+"";
//                            status = radiotherapy.getStatus();
//
//                            fragmentAddNewRadiotherapy = new FragmentAddNewRadiotherapy();
//                            fragmentTransaction.add(R.id.fl_content, fragmentAddNewRadiotherapy);
//
//                        }else {
//                            //已停止的放疗编辑
//                            startTime = radiotherapy.getStartTime();
//                            endTime = radiotherapy.getEndTime();
//                            currentCount = radiotherapy.getCurrentCount()+"";
//                            currentDosage = radiotherapy.getCurrentDosage()+"";
//                            endReason = radiotherapy.getEndReason();
//                            status = radiotherapy.getStatus();
//                            notes = radiotherapy.getNotes();
//
//                            fragmentAddExistRadiotherapy = new FragmentAddExistRadiotherapy();
//                            fragmentTransaction.add(R.id.fl_content, fragmentAddExistRadiotherapy);
//                        }
//
//                        fragmentTransaction.commit();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "ActivityAttentionAddRadiotherapy getDBData onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(Radiotherapy radiotherapy) {
//                        Log.i("hcb", "ActivityAttentionAddRadiotherapy getDBData onNext");
//                    }
//                });
//
//    }
//}
