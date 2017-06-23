//package com.zhongmeban.attentionmodle.activity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.view.View;
//
//import com.zhongmeban.MyApplication;
//import com.zhongmeban.R;
//import com.zhongmeban.base.BaseActivity;
//import com.zhongmeban.bean.AttentionHospitalizedDetailBean;
//import com.zhongmeban.bean.postbody.CreateOrUpdateHospitalRecordBody;
//import com.zhongmeban.bean.postbody.CreateSurgeryRecordBody;
//import com.zhongmeban.bean.postbody.HospitalRecordBody;
//import com.zhongmeban.bean.postbody.SurgeryRecordItem;
//import com.zhongmeban.attentionmodle.fragment.FragmentAttentionAddHospitalizedStep1;
//import com.zhongmeban.attentionmodle.fragment.FragmentAttentionAddHospitalizedStep2;
//import com.zhongmeban.utils.LayoutActivityTitle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.dao.attention.Hospitalized;
//import de.greenrobot.dao.attention.HospitalizedDao;
//import de.greenrobot.dao.attention.SurgeryMethods;
//import de.greenrobot.dao.attention.SurgeryRecord;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 新增住院
// * Created by Chengbin He on 2016/7/5.
// */
//public class ActivityAttentionAddHospitalized extends BaseActivity {
//
//    private FragmentAttentionAddHospitalizedStep1 fragmentAttentionAddHospitalizedStep1;
//    private FragmentAttentionAddHospitalizedStep2 fragmentAttentionAddHospitalizedStep2;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private int step = 1;
//    private int hospitalizedId;
////    private Hospitalized hospitalized;//住院数据库返回数据
//    private AttentionHospitalizedDetailBean attentionHospitalizedDetailBean;//详情页面返回数据
//
//    public boolean ISEDIT;//判断是否为编辑标记位
//    public int purposeType;//入院类型 1.观察 2.治疗 3.手术
//    public long inTime;//入院时间
//    public long outTime;//出院时间
//    public String patientId;
//    public String token;
//    public String hospName;
//    public int hospId;
//    public String docName;
//    public int docId;
//    public long iD;
//    public String description = "";//备注
////    public SurgeryRecord surgeryRecord;//手术数据库返回数据
//
//    public long surgeryRecordId;//手术ID
//    public String therapeuticName;//手术名称
//    public CreateOrUpdateHospitalRecordBody createOrUpdateHospitalRecordBody
//            = new CreateOrUpdateHospitalRecordBody();
//    public HospitalRecordBody hospitalRecord = new HospitalRecordBody();
//    public List<CreateSurgeryRecordBody> surgeryRecordsList = new ArrayList<CreateSurgeryRecordBody>();//上传手术信息
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_attention_add_operation);
//
//        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
//        patientId = sp.getString("patientId", "");
//        token = sp.getString("token", "");
//
//        Intent intent = getIntent();
//        ISEDIT = intent.getBooleanExtra("ISEDIT", false);
////        hospitalizedId = intent.getIntExtra("hospitalizedId",0);
//        attentionHospitalizedDetailBean = (AttentionHospitalizedDetailBean) intent.getSerializableExtra("attentionHospitalizedDetailBean");
//
//        fragmentAttentionAddHospitalizedStep1 = new FragmentAttentionAddHospitalizedStep1();
//        fragmentAttentionAddHospitalizedStep2 = new FragmentAttentionAddHospitalizedStep2();
//
//        if (ISEDIT) {
////            getDBData(hospitalizedId);
//            initData();
//        }
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fl_content, fragmentAttentionAddHospitalizedStep1);
//        fragmentTransaction.commit();
//
//
//        initTitle();
//    }
//
//
//    @Override
//    protected void initTitle() {
//        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
//        String titleName = "添加入院记录";
//        if (ISEDIT){
//            titleName = "编辑入院信息记录";
//        }
//        title.slideCentertext(titleName);
//        title.backSlid(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (step == 1) {
//                    finish();
//                } else {
//                    notifyFragment(1);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (step == 1) {
//            finish();
//        } else {
//            notifyFragment(1);
//        }
//    }
//
//    /**
//     * 更换步骤fragment
//     *
//     * @param step 1 第一步 2 第二步
//     */
//    public void notifyFragment(int step) {
//        this.step = step;
//        switch (step) {
//            case 1:
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionAddHospitalizedStep1);
//                fragmentTransaction.commit();
//                break;
//            case 2:
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionAddHospitalizedStep2);
//                fragmentTransaction.commit();
//                break;
//            default:
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionAddHospitalizedStep1);
//                fragmentTransaction.commit();
//                break;
//
//        }
//    }
//
//    /***
//     * 网络提交前初始化参数
//     */
//    public void initHospitalRecordBody() {
//
//        hospitalRecord.setId(iD);
//        hospitalRecord.setDoctorId(docId);
//        hospitalRecord.setDescription(description);
//        hospitalRecord.setHospitalId(hospId);
//        hospitalRecord.setInTime(inTime);
//        hospitalRecord.setOutTime(outTime);
//        hospitalRecord.setType(purposeType);
//        hospitalRecord.setPatientId(patientId);
//
//
//        createOrUpdateHospitalRecordBody.setHospitalRecord(hospitalRecord);
//        createOrUpdateHospitalRecordBody.setSurgeryRecords(surgeryRecordsList);
//    }
//
//    /**
//     * 清楚手术信息
//     */
//    public void clearSurgeryRecords() {
//        surgeryRecordsList.clear();
//    }
//
//    /**
//     * 从详情页返回初始化数据
//     */
//    private void initData() {
//        Hospitalized hospitalized = attentionHospitalizedDetailBean.getHospitalized();//取出住院相关信息
//        inTime = hospitalized.getInTime();
//        outTime = hospitalized.getOutTime();
//        docName = hospitalized.getDoctorName();
//        docId = (int) hospitalized.getDoctorId();
//        hospId = (int) hospitalized.getHospitalId();
//        hospName = hospitalized.getHospitalName();
//        purposeType = hospitalized.getType();
//        iD = hospitalized.getId();
//        description = hospitalized.getDescription();
//        if (purposeType == 3) {
//            //判断时候包含手术内容
//            initSurgery();
//        }
//    }
//
//    private void initSurgery() {
//        SurgeryRecord surgeryRecord = attentionHospitalizedDetailBean.getSurgeryRecord();
//        SurgeryRecordItem surgeryRecordItem = new SurgeryRecordItem();
//
//        surgeryRecordItem.setId(surgeryRecord.getId());
//        surgeryRecordItem.setDoctorId((int) surgeryRecord.getDoctorId());
//        surgeryRecordItem.setHospitalId((int) surgeryRecord.getHospitalId());
//        surgeryRecordItem.setPatientId(surgeryRecord.getPatientId());
//        surgeryRecordItem.setTherapeuticId((int) surgeryRecord.getTherapeuticId());
//        surgeryRecordItem.setTherapeuticName(surgeryRecord.getTherapeuticName());
//        surgeryRecordItem.setNotes(surgeryRecord.getNotes());
//        surgeryRecordItem.setTime(surgeryRecord.getTime());
//        surgeryRecordId = surgeryRecord.getId();
//        therapeuticName = surgeryRecord.getTherapeuticName();
//        List<Long> methodIds = new ArrayList<>();
//        List<SurgeryMethods> surgeryMethodsList = attentionHospitalizedDetailBean.getSurgeryMethodsList();
//        for (SurgeryMethods surgeryMethods : surgeryMethodsList) {
//            methodIds.add(surgeryMethods.getSurgeryMethodId());
//        }
//        CreateSurgeryRecordBody createSurgeryRecordBody = new CreateSurgeryRecordBody();
//        createSurgeryRecordBody.setMethodIds(methodIds);
//        createSurgeryRecordBody.setSurgeryRecord(surgeryRecordItem);
//        surgeryRecordsList.add(createSurgeryRecordBody);
//    }
//
//    /**
//     * 获取本地数据库数据
//     *
//     * @param id
//     */
//    private void getDBData(final int id) {
//
//        Observable.create(new Observable.OnSubscribe<Hospitalized>() {
//            @Override
//            public void call(Subscriber<? super Hospitalized> subscriber) {
//                HospitalizedDao hospitalizedDao = ((MyApplication) getApplication())
//                        .getAttentionDaoSession().getHospitalizedDao();
//
//                List<Hospitalized> hospitalizedList = hospitalizedDao.queryBuilder()
//                        .where(hospitalizedDao.queryBuilder().and(HospitalizedDao.Properties.Id.eq(id),
//                                HospitalizedDao.Properties.IsActive.eq(true),
//                                HospitalizedDao.Properties.PatientId.eq(patientId))).list();
////                hospitalized = hospitalizedList.get(0);//获取住院信息
//
//
//                ((MyApplication) getApplication()).closeAttentionDB();
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Hospitalized>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "ActivityAttentionAddHospitalized getDBData onCompleted");
//
////                        inTime = hospitalized.getInTime();
////                        outTime = hospitalized.getOutTime();
////                        docName = hospitalized.getDoctorName();
////                        docId = (int) hospitalized.getDoctorId();
////                        hospId = (int) hospitalized.getHospitalId();
////                        hospName = hospitalized.getHospitalName();
////                        purposeType = hospitalized.getType();
////                        iD = hospitalized.getId();
//
//                        fragmentManager = getSupportFragmentManager();
//                        fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.add(R.id.fl_content, fragmentAttentionAddHospitalizedStep1);
//                        fragmentTransaction.commit();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "ActivityAttentionAddHospitalized getDBData onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(Hospitalized hospitalized) {
//                        Log.i("hcb", "ActivityAttentionAddHospitalized getDBData onNext");
//                    }
//                });
//    }
//
//
//}
