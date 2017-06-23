//package com.zhongmeban.attentionmodle.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//
//import com.zhongmeban.MyApplication;
//import com.zhongmeban.R;
//import com.zhongmeban.activity.ActivityBaseChemotherapy;
//import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyCourseBody;
//import com.zhongmeban.attentionmodle.fragment.FragmentAttentionChemotherapyAddTreatment;
//import com.zhongmeban.attentionmodle.fragment.FragmentAttentionChemotherapyDetail;
//import com.zhongmeban.fragment.FragmentChemotherapyMedicine;
//import com.zhongmeban.fragment.FragmentStopChemotherapy;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.dao.AttentionMedicine;
//import de.greenrobot.dao.Medicine;
//import de.greenrobot.dao.MedicineDao;
//import de.greenrobot.dao.attention.ChemotherapyCourse;
//import de.greenrobot.dao.attention.ChemotherapyCourseMedicine;
//import de.greenrobot.dao.attention.ChemotherapyCourseMedicineDao;
//import de.greenrobot.dao.attention.ChemotherapyMedicine;
//import de.greenrobot.dao.attention.ChemotherapyMedicineDao;
//
///**
// * 化疗详情Activity
// * Created by Chengbin He on 2016/7/11.
// */
//public class ActivityAttentionChemotherapyDetail extends ActivityBaseChemotherapy {
//
//    public static final int CHEMOTHERAPY = 1;//化疗记录
//    public static final int CHEMOTHERAPYCOURSE = 2;//化疗疗程
//    public static final int CHEMOTHERAPYMEDICINE = 3;//化疗用药
//    public static final int STOPCHEMOTHERAPY = 4;//停止化疗
//
//    private Context mContext = ActivityAttentionChemotherapyDetail.this;
//    private FragmentAttentionChemotherapyDetail fragmentAttentionChemotherapyDetail;//化疗详情Fragment
//    public FragmentAttentionChemotherapyAddTreatment fragmentAttentionChemotherapyAddTreatment;//添加疗程Fragment
//    private FragmentStopChemotherapy fragmentStopChemotherapy;
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//    private int step;// 1 化疗详情 2 添加疗程 3添加疗程用药 4结束本次化疗
//
//    public long chemotherapyId;//化疗ID
//    public long chemotherapyCourseId;//化疗疗程ID
//    public int courseCount;//预计疗程次数
//    public int courseInterval;//疗程间隔天数
//    public int dayOfCourse;//每个疗程天数(疗程周期)
//
//    public long startTime;//化疗开始时间，上传用
//    public long endTime;//化疗结束时间，上传用
//    public long courseStartTime;//疗程开始时间，上传用
//    public long courseEndTime;//疗程结束时间，上传用
//
//    public int endReason;
//    public String token;
//    public String description;//化疗描述
//    public String courseDescription;//化疗疗程描述
//    public String patientId;
//    public boolean ISNEWCHEMOTHERAPY;//是否为正在进行的化疗跳转标记位
//    public boolean ISNEWCHEMOTHERAPYCORDS;//是否为新增化疗跳转疗程
//    public String statusName;//化疗状态名称
//    public String timesName;//第几次化疗
//    public String chemotherapyAimName = "";
//    public CreateOrUpdateChemotherapyCourseBody body = new CreateOrUpdateChemotherapyCourseBody();
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_attention_chemotherapy_detail);
//
//        Intent intent = getIntent();
//        chemotherapyId = intent.getLongExtra("chemotherapyId", 0);
//        ISNEWCHEMOTHERAPY = intent.getBooleanExtra("ISNEWCHEMOTHERAPY", false);
//        ISNEWCHEMOTHERAPYCORDS = intent.getBooleanExtra("ISNEWCHEMOTHERAPYCORDS", false);
//        courseCount = intent.getIntExtra("courseCount", 0);
//        courseInterval = intent.getIntExtra("courseInterval", 0);
//        dayOfCourse = intent.getIntExtra("dayOfCourse", 0);
//        startTime = intent.getLongExtra("startTime", 0);
//        Log.i("hcb", "ISNEWCHEMOTHERAPY" + ISNEWCHEMOTHERAPY);
//
//        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
//        token = sp.getString("token", "");
//        patientId = sp.getString("patientId", "");
//
//        chooseMedicineList.clear();
//
//        fragmentAttentionChemotherapyDetail = new FragmentAttentionChemotherapyDetail();
//        fragmentAttentionChemotherapyAddTreatment = new FragmentAttentionChemotherapyAddTreatment();
//        fragmentChemotherapyMedicine = new FragmentChemotherapyMedicine();
//        fragmentStopChemotherapy = new FragmentStopChemotherapy();
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        if (ISNEWCHEMOTHERAPYCORDS) {
//            step = CHEMOTHERAPYCOURSE;
//            List<AttentionMedicine> list = (List<AttentionMedicine>) intent.getSerializableExtra("chooseMedicineList");
//            Log.i("hcb","chemotherapyId" +chemotherapyId);
//            Log.i("hcb","list.size()" +list.size());
//            chooseMedicineList.clear();
//            chooseMedicineList.addAll(list);
////            getChemotherapyCourseChooseMedicine(chemotherapyId);
////            getChemotherapyChooseMedicine();
//            fragmentTransaction.add(R.id.fl_content, fragmentAttentionChemotherapyAddTreatment);
//        } else {
//            step = CHEMOTHERAPY;
//            fragmentTransaction.add(R.id.fl_content, fragmentAttentionChemotherapyDetail);
//        }
//        fragmentTransaction.commit();
//
//    }
//
//    /**
//     * 更换Fragment
//     *
//     * @param step 1 化疗详情 2 添加疗程 3添加疗程用药 4结束本次化疗
//     */
//    public void notifyFragment(int step) {
//        this.step = step;
//        fragmentTransaction = fragmentManager.beginTransaction();
//        switch (step) {
//            case CHEMOTHERAPY:
//                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionChemotherapyDetail);
//                break;
//            case CHEMOTHERAPYCOURSE:
//                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionChemotherapyAddTreatment);
//                break;
//            case CHEMOTHERAPYMEDICINE:
//                fragmentTransaction.replace(R.id.fl_content, fragmentChemotherapyMedicine);
//                break;
//            case STOPCHEMOTHERAPY:
//                fragmentTransaction.replace(R.id.fl_content, fragmentStopChemotherapy);
//                break;
//        }
//        fragmentTransaction.commit();
//    }
//
//    public void clearEditText(){
//        fragmentAttentionChemotherapyAddTreatment.clearEditText();
//    }
//
//    @Override
//    public void backChemotherapyFragment() {
//        super.backChemotherapyFragment();
//        fragmentAttentionChemotherapyAddTreatment.FORMEMEDICENE = true;
//        notifyFragment(CHEMOTHERAPYCOURSE);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        switch (step) {
//            case CHEMOTHERAPY:
//                if (ISNEWCHEMOTHERAPY) {
//                    Intent intent = new Intent(this, ActivityAttentionList.class);
//                    setResult(200, intent);
//                }
//                finish();
//                break;
//            case CHEMOTHERAPYCOURSE:
//                resetBody();
//                notifyFragment(CHEMOTHERAPY);
//                break;
//            case CHEMOTHERAPYMEDICINE:
//                notifyFragment(CHEMOTHERAPYCOURSE);
//                break;
//            case STOPCHEMOTHERAPY:
//                notifyFragment(CHEMOTHERAPY);
//                break;
//        }
//    }
//
//    @Override
//    protected void initTitle() {
//    }
//
//    /**
//     * 创建网络请求前初始化数据
//     */
//    public void initChemotherapyCourseBody() {
//        body.getRecord().setStartTime(courseStartTime);
//        body.getRecord().setEndTime(courseEndTime);
//        body.getRecord().setChemotherapyRecordId(chemotherapyId);
//        body.getRecord().setPatientId(patientId);
//        body.getRecord().setDescription(courseDescription);
//        body.getRecord().setId(chemotherapyCourseId);
//        initMedicine();
//    }
//
//    /**
//     * 网络提交前 初始化用药
//     */
//    private void initMedicine() {
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
//     * 网络请求完毕 重置数据
//     */
//    public void resetBody() {
//
//        courseStartTime = 0;
//        courseEndTime = 0;
//        courseInterval = 0;
////        patientId = "";
//        courseDescription = "";
//        clearEditText();
//        chooseMedicineList.clear();
//        getChemotherapyChooseMedicine();
//        endReason = 0;
//        chemotherapyCourseId = 0;
//        fragmentAttentionChemotherapyAddTreatment.ISEDIT = false;
//        fragmentAttentionChemotherapyAddTreatment.FORMEMEDICENE = false;
//    }
//
//    /**
//     * 初始化疗计划用药
//     */
//    public void getChemotherapyChooseMedicine() {
//        ChemotherapyMedicineDao chemotherapyMedicineDao = ((MyApplication) getApplication())
//                .getAttentionDaoSession().getChemotherapyMedicineDao();
//        MedicineDao medicineDao = ((MyApplication) getApplication()).getDaoSession().getMedicineDao();
//
//        List<ChemotherapyMedicine> chemotherapyMedicineList = chemotherapyMedicineDao.queryBuilder()
//                .where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(chemotherapyId)).list();
//        List<Long> medicineIdList = new ArrayList<Long>();
//
//        for (int i = 0; i < chemotherapyMedicineList.size(); i++) {
//            medicineIdList.add(chemotherapyMedicineList.get(i).getMedicineId());
//        }
//        Log.i("hcb", "medicineIdList.size()" + medicineIdList.size());
//        if (medicineIdList.size() > 0) {
//            //判断是否有用药
//            List<Medicine> medicineList = medicineDao.queryBuilder()
//                    .where(MedicineDao.Properties.MedicineId.in(medicineIdList)).list();
//            List<AttentionMedicine> attentionMedicineList = new ArrayList<AttentionMedicine>();
//            for (int a = 0; a < medicineIdList.size(); a++) {
//                Medicine medicine = medicineList.get(a);
//                AttentionMedicine attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
//                attentionMedicineList.add(attentionMedicine);
//            }
//            chooseMedicineList.clear();
//            chooseMedicineList.addAll(attentionMedicineList);
//            Log.i("hcbtest", "getChemotherapyChooseMedicine chooseMedicineList.size() " + chooseMedicineList.size());
//
//        }
//    }
//
//    /**
//     * 初始化化疗疗程用药
//     *
//     * @param id
//     */
//    public void getChemotherapyCourseChooseMedicine(long id) {
//
//        MedicineDao medicineDao = ((MyApplication) getApplication()).getDaoSession().getMedicineDao();
//
//        ChemotherapyCourseMedicineDao chemotherapyCourseMedicineDao = ((MyApplication) getApplication())
//                .getAttentionDaoSession().getChemotherapyCourseMedicineDao();
//
//        List<ChemotherapyCourseMedicine> chemotherapyCourseMedicineList = chemotherapyCourseMedicineDao.queryBuilder()
//                .where(ChemotherapyCourseMedicineDao.Properties.ChemotherapyCourseId.eq(chemotherapyCourseId)).list();
//        List<Long> medicineIdList = new ArrayList<Long>();
//
//        for (int i = 0; i < chemotherapyCourseMedicineList.size(); i++) {
//            medicineIdList.add(chemotherapyCourseMedicineList.get(i).getMedicineId());
//        }
//        Log.i("hcb", "medicineIdList.size()" + medicineIdList.size());
//        if (medicineIdList.size() > 0) {
//            //判断是否有用药
//            List<Medicine> medicineList = medicineDao.queryBuilder()
//                    .where(MedicineDao.Properties.MedicineId.in(medicineIdList)).list();
//            List<AttentionMedicine> attentionMedicineList = new ArrayList<AttentionMedicine>();
//            for (int a = 0; a < medicineIdList.size(); a++) {
//                Medicine medicine = medicineList.get(a);
//                AttentionMedicine attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
//                attentionMedicineList.add(attentionMedicine);
//            }
//            chooseMedicineList.clear();
//            chooseMedicineList.addAll(attentionMedicineList);
//            Log.i("hcbtest", "getChemotherapyCourseChooseMedicine chooseMedicineList.size() " + chooseMedicineList.size());
//        }
//    }
//
//
//    //控制选中疗程记录
//    public void editChemotherapyCourse(ChemotherapyCourse course) {
//        fragmentAttentionChemotherapyAddTreatment.ISEDIT = true;
//        courseStartTime = course.getStartTime();
//        courseEndTime = course.getEndTime();
//        courseDescription = course.getDescription();
//        endReason = course.getEndReason();
//        chemotherapyCourseId = course.getId();
//        course.getTimes();
//        fragmentAttentionChemotherapyAddTreatment.ISEDIT = true;
//        getChemotherapyCourseChooseMedicine(chemotherapyCourseId);
//    }
//
//
//}
