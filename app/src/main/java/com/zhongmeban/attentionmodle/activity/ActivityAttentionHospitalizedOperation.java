package com.zhongmeban.attentionmodle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.postbody.CreateSurgeryRecordBody;
import com.zhongmeban.bean.postbody.SurgeryRecordItem;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionHospitalizedOperationStep1;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionHospitalizedSurgeryMethodsByDisease;
import com.zhongmeban.utils.LayoutActivityTitle;

import java.util.ArrayList;
import java.util.List;

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
 * 住院操作手术Activity
 * Created by Chengbin He on 2016/11/11.
 */

public class ActivityAttentionHospitalizedOperation extends BaseActivity {
    private FragmentAttentionHospitalizedSurgeryMethodsByDisease fragmentAttentionSurgeryMethodsByDisease;//其他设置Fragment
    private FragmentAttentionHospitalizedOperationStep1 fragmentAttentionHospitalizedOperationStep1;
//    private FragmentAttentionHospitalizedOperationStep2 fragmentAttentionHospitalizedOperationStep2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int step = 1;
    private long surgeryRecordId;//化疗ID

    public int patientDiseaseId;//关注人患者癌症Id
    public boolean ISEDIT;//判断是否为编辑标记位
    public boolean ISFROMEHOSP;//判断是否从住院入口进入
    public long time;
    public long id;
    public long inTime;//入院时间
    public long outTime;//出院时间
    public String patientId;
    public String token;
    public String hospName;
    public int hospId;
    public String docName;
    public int docId;
    public String therapeuticName;//手术名称
    public int therapeuticId;//手术Id
    public String description = "";//备注
    public CreateSurgeryRecordBody createSurgeryRecordBody = new CreateSurgeryRecordBody();
    public SurgeryRecordItem surgeryRecordItem = new SurgeryRecordItem();
    private List<Long> methodIds = new ArrayList<Long>();//选中其他手术项ID，创建用
    public List<SurgeryMethods> chooseMethods = new ArrayList<>();//选中其他手术项

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_add_operation);
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        patientId = sp.getString("patientId", "");
        token = sp.getString("token", "");
        patientDiseaseId = sp.getInt("patientDiseaseId", 0);


        Intent intent = getIntent();
        ISEDIT = intent.getBooleanExtra("ISEDIT", false);
        surgeryRecordId = intent.getLongExtra("surgeryMethodId", 0);
//        inTime = intent.getIntExtra("inTime", 0);
        outTime = intent.getIntExtra("outTime", 0);
        time = intent.getLongExtra("operationTime",0);
        hospId = intent.getIntExtra("hospId",0);
        hospName = intent.getStringExtra("hospName");
        docId = intent.getIntExtra("docId",0);
        docName = intent.getStringExtra("docName");

        fragmentAttentionHospitalizedOperationStep1 = new FragmentAttentionHospitalizedOperationStep1();
//        fragmentAttentionHospitalizedOperationStep2 = new FragmentAttentionHospitalizedOperationStep2();
        fragmentAttentionSurgeryMethodsByDisease = new FragmentAttentionHospitalizedSurgeryMethodsByDisease();

        initTitle();
        CreateSurgeryRecordBody mCreateSurgeryRecordBody = (CreateSurgeryRecordBody) intent.getSerializableExtra("CreateSurgeryRecordBody");

        if(ISEDIT){
            //编辑手术
            getDBData((int) surgeryRecordId);
        }else
        { //新增手术
            if (mCreateSurgeryRecordBody != null){
                createSurgeryRecordBody = mCreateSurgeryRecordBody;
//                therapeuticName = createSurgeryRecordBody.getSurgeryRecord().getTherapeuticName();
                therapeuticId = createSurgeryRecordBody.getSurgeryRecord().getTherapeuticId();
                methodIds = createSurgeryRecordBody.getMethodIds();
                description = createSurgeryRecordBody.getSurgeryRecord().getNotes();
                surgeryRecordId = createSurgeryRecordBody.getSurgeryRecord().getId();
                inTime = createSurgeryRecordBody.getSurgeryRecord().getTime();

                //初始化选中手术其他项ID
                //手术项目List
                SurgeryMethodsDao surgeryMethodsDao = ((MyApplication) getApplication())
                        .getAttentionDaoSession().getSurgeryMethodsDao();
                List<SurgeryMethods> surgeryMethodsList = surgeryMethodsDao.queryBuilder()
                        .where(SurgeryMethodsDao.Properties.SurgeryRecordId.eq(surgeryRecordId)).list();
                chooseMethods.clear();
                chooseMethods.addAll(surgeryMethodsList);
            }

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_content, fragmentAttentionHospitalizedOperationStep1);
            fragmentTransaction.commit();


        }

        createSurgeryRecordBody.setSurgeryRecord(surgeryRecordItem);


    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        String titleName = "";
        if (ISEDIT) {
            titleName = "编辑手术记录";
        } else {
            titleName = "新增手术记录";
        }
        title.slideCentertext(titleName);
        title.backSlid(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (step) {
                            case 1:
                                finish();
                                break;
                            case 2:
                                notifyFragment(1);
                                break;
                            case 3:
                                notifyFragment(2);
                                break;
                            default:
                                notifyFragment(1);
                                break;
                        }

                    }
                });
    }

    private void changeTitleName(int step) {
        if (step == 2) {
            title.slideCentertext("添加同期手术");
        } else {
            title.slideCentertext("添加手术项");
        }

    }

    @Override
    public void onBackPressed() {
        switch (step) {
            case 1:
                finish();
                break;
            case 2:
                notifyFragment(1);
                break;
            case 3:
                notifyFragment(2);
                break;
            default:
                notifyFragment(1);
                break;
        }
    }

    /**
     * 更换步骤fragment
     *
     * @param step 1 第一步 2 第二步 3 其他设置
     */
    public void notifyFragment(int step) {
        this.step = step;
        switch (step) {
            case 1:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionHospitalizedOperationStep1);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionHospitalizedOperationStep2);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionSurgeryMethodsByDisease);
                fragmentTransaction.commit();
                break;
            default:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_content, fragmentAttentionHospitalizedOperationStep1);
                fragmentTransaction.commit();
                break;

        }
    }


    /**
     * 网络提交前初始化参数
     */
    public void initSurgeryRecordItem() {
        surgeryRecordItem.setTime(time);
//        surgeryRecordItem.setDoctorId(docId);
        surgeryRecordItem.setPatientId(patientId);
        surgeryRecordItem.setTherapeuticId(therapeuticId);
        surgeryRecordItem.setHospitalId(hospId);
//        surgeryRecordItem.setTherapeuticName(therapeuticName);
        surgeryRecordItem.setNotes(description);
        surgeryRecordItem.setId(id);
        iniChooseMethods();
        createSurgeryRecordBody.setMethodIds(methodIds);
    }

    /**
     * 初始化选中其他手术项
     */
    public void iniChooseMethods() {
        methodIds.clear();
        for (SurgeryMethods surgeryMethods : chooseMethods) {
            methodIds.add(surgeryMethods.getSurgeryMethodId());
        }
    }

    /**
     * 获取数据库数据
     *
     * @param surgeryRecordId
     */
    private void getDBData(final int surgeryRecordId) {
        Observable.create(new Observable.OnSubscribe<SurgerySource>() {

            @Override
            public void call(Subscriber<? super SurgerySource> subscriber) {

                SurgeryRecordDao surgeryRecordDao = ((MyApplication) getApplication())
                        .getAttentionDaoSession().getSurgeryRecordDao();
                //手术项目List
                SurgeryMethodsDao surgeryMethodsDao = ((MyApplication) getApplication())
                        .getAttentionDaoSession().getSurgeryMethodsDao();

                List<SurgeryRecord> surgeryRecordList = surgeryRecordDao.queryBuilder()
                        .where(SurgeryRecordDao.Properties.Id.eq(surgeryRecordId),
                                SurgeryRecordDao.Properties.PatientId.eq(patientId)).list();
                SurgeryRecord surgeryRecord = surgeryRecordList.get(0);

                time = surgeryRecord.getTime();
                hospName = surgeryRecord.getHospitalName();
                hospId = (int) surgeryRecord.getHospitalId();
                docName = surgeryRecord.getDoctorName();
//                docId = (int) surgeryRecord.getDoctorId();
                therapeuticId = (int) surgeryRecord.getTherapeuticId();
                therapeuticName = surgeryRecord.getTherapeuticName();
                id = surgeryRecord.getId();
                description = surgeryRecord.getNotes();

                //初始化选中手术其他项ID
                List<SurgeryMethods> surgeryMethodsList = surgeryMethodsDao.queryBuilder()
                        .where(SurgeryMethodsDao.Properties.SurgeryRecordId.eq(surgeryRecordId)).list();
                chooseMethods.clear();
                chooseMethods.addAll(surgeryMethodsList);

                ((MyApplication) getApplication()).closeAttentionDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SurgerySource>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "ActivityAttentionAddOperation  getDBData onCompleted");
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.fl_content, fragmentAttentionHospitalizedOperationStep1);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "ActivityAttentionAddOperation  getDBData onError");
                        Log.i("hcb", "e " + e);
                    }

                    @Override
                    public void onNext(SurgerySource surgerySource) {
                        Log.i("hcb", "ActivityAttentionAddOperation  getDBData onNext");
                    }
                });

    }
}
