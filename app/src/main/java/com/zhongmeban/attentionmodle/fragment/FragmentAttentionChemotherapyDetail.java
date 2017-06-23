//package com.zhongmeban.attentionmodle.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zhongmeban.AdapterClickInterface;
//import com.zhongmeban.MyApplication;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddChemotherapy;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionChemotherapyDetail;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
//import com.zhongmeban.adapter.AddTreatmentAdapter;
//import com.zhongmeban.adapter.ChemotherapyDetailInfoItemAdapter;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.ChemotherapyRecordBean;
//import com.zhongmeban.bean.ChemotherapyCourseListBean;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.DeleteRecordBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.AttentionUtils;
//import com.zhongmeban.utils.CircularProgressBar.CircularProgressBar;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.view.ScrollLinearLayout;
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
//
///**
// * 化疗详情Fragment
// * Created by Chengbin He on 2016/7/12.
// */
//public class FragmentAttentionChemotherapyDetail extends BaseFragment implements View.OnClickListener {
//
//
//    private ActivityAttentionChemotherapyDetail parentActivity;
//    private ScrollView scrollContent;//主体内容
//    private CircularProgressBar progressBar;
//    private ChemotherapyDao chemotherapyDao;//化疗Dao
//    private Chemotherapy chemotherapy;
//    private ChemotherapyCourseDao chemotherapyCourseDao;//化疗疗程Dao
//    private ChemotherapyMedicineDao chemotherapyMedicineDao;//化疗用药Dao
//    private ChemotherapyCourseMedicineDao chemotherapyCourseMedicineDao;//化疗疗程Dao
//    private LayoutActivityTitle title;
//    private TextView tvName;//化疗名称
//    private TextView tvStatus;//化疗状态
//    private TextView tvAddTreatment;
//    private ScrollLinearLayout slChemotherapyDetailInfo;//化疗疗程内容
//    private ScrollLinearLayout scrollLinearLayout;//化疗疗程内容
//    private ImageView ivEdit;//化疗编辑Icon
//    private SharedPreferences serverTimeSP;
//    private String chemotherapyCourseServerTime;//化疗疗程Servertime
//    private String chemotherapyServerTime;//化疗Servertime
//    private List<ChemotherapyRecordBean.Source> httpChemotherapyCourseList
//            = new ArrayList<ChemotherapyRecordBean.Source>();//网络化疗疗程记录
//    private List<ChemotherapyRecordBean.Source> httpChemotherapyList
//            = new ArrayList<ChemotherapyRecordBean.Source>();//网络化疗记录
//    private List<ChemotherapyCourse> dbList = new ArrayList<ChemotherapyCourse>();//展示化疗疗程数据List
//    private List<ChemotherapyMedicine> dbChemotherapyMedicine = new ArrayList<>();//化疗用药数据库查询List
//    private List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicine = new ArrayList<>();//化疗疗程用药数据库查询List
//    private List<ChemotherapyCourseListBean> adapterList = new ArrayList<>();//化疗疗程adapter显示用List
//    private AddTreatmentAdapter mAdapter;
//    private Button btDelete;//删除本次化疗
//    private Button btStop;//结束本次化疗
//    private MaterialDialog progressDialog;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionChemotherapyDetail) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_attention_chemotherapy_detail, container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        scrollContent = (ScrollView) view.findViewById(R.id.scroll_content);
//        scrollContent.setVisibility(View.GONE);
//        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE);
//
//        serverTimeSP = parentActivity.getSharedPreferences("serverTime", Context.MODE_PRIVATE);
//        chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");
//        chemotherapyServerTime = serverTimeSP.getString("chemotherapyServerTime", "0");
//
//        initTitle(view);
//        initView(view);
//
//        getHttpChemotherapyCourse(chemotherapyCourseServerTime, parentActivity.patientId, parentActivity.token);
//    }
//
//    private void initView(View view) {
//        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(R.id.scroll_linearlayout);
//        tvAddTreatment = (TextView) view.findViewById(R.id.tv_add_treatment);
//        tvAddTreatment.setOnClickListener(this);
//
//        slChemotherapyDetailInfo = (ScrollLinearLayout) view.findViewById(R.id.sl_info);
//
//        tvName = (TextView) view.findViewById(R.id.tv_chemotherapy_name);
//        tvStatus = (TextView) view.findViewById(R.id.tv_chemotherapy_state);
////        tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
////        tvPurpose = (TextView) view.findViewById(R.id.tv_purpose);
////        tvMedicine = (TextView) view.findViewById(R.id.tv_project);
////        tvTreatmentOrEndReason = (TextView) view.findViewById(R.id.tv_treatment);
////        tvPeriodOrNotes = (TextView) view.findViewById(R.id.tv_period);
////        tvInterval = (TextView) view.findViewById(R.id.tv_interval);
//
//
//        btDelete = (Button) view.findViewById(R.id.bt_delete);
//        btDelete.setOnClickListener(this);
//        btStop = (Button) view.findViewById(R.id.bt_stop);
//        btStop.setOnClickListener(this);
//
//        ivEdit = (ImageView) view.findViewById(R.id.iv_edit);
//        ivEdit.setOnClickListener(this);
//
////        getDBChemotherapyCourse(parentActivity.chemotherapyId);
//    }
//
//    private void initTitle(View view) {
//        title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//        title.slideCentertext("化疗详情");
//        title.backSlid(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                parentActivity.onBackPressed();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.tv_add_treatment://新增疗程
////                parentActivity.clearEditText();
//                parentActivity.notifyFragment(2);
//                break;
//
//            case R.id.bt_delete:
//                showDeleteDialog(parentActivity, positiveCallback, "是否删除本次化疗？");
//                break;
//
//            case R.id.bt_stop:
//                parentActivity.notifyFragment(parentActivity.STOPCHEMOTHERAPY);
//                break;
//            case R.id.iv_edit:
//                Intent intent = new Intent(parentActivity, ActivityAttentionAddChemotherapy.class);
//                intent.putExtra("ISEDIT", true);
//                intent.putExtra("chemotherapyId", parentActivity.chemotherapyId);
//                if (parentActivity.ISNEWCHEMOTHERAPY) {
//                    intent.putExtra("ISNEW", true);
//                }
//                startActivityForResult(intent, 1);
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 200) {
//            //其他回掉
//            chemotherapyCourseServerTime = serverTimeSP.getString("chemotherapyCourseServerTime", "0");
//            chemotherapyServerTime = serverTimeSP.getString("chemotherapyServerTime", "0");
//            getHttpChemotherapyCourse(chemotherapyCourseServerTime, parentActivity.patientId, parentActivity.token);
//        } else if (resultCode == 300) {
//            //编辑疗程回调
//            Intent intent = new Intent(parentActivity, ActivityAttentionList.class);
//            parentActivity.setResult(200, intent);
//            parentActivity.finish();
//
//        }
//    }
//
//    /**
//     * 删除Dialog 确认按钮
//     */
//    MaterialDialog.SingleButtonCallback positiveCallback = new MaterialDialog.SingleButtonCallback() {
//
//        @Override
//        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//            progressDialog = showProgressDialog("正在删除数据请稍等", parentActivity);
//            deleteChemotherapy((int) parentActivity.chemotherapyId);
//        }
//    };
//
//    /**
//     * 获取数据库 化疗记录
//     *
//     * @param id
//     */
//    private void getDBChemotherapy(final long id) {
//        Observable.create(new Observable.OnSubscribe<Chemotherapy>() {
//            @Override
//            public void call(Subscriber<? super Chemotherapy> subscriber) {
//
//                chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
//                        .getAttentionDaoSession().getChemotherapyDao();
//                chemotherapyMedicineDao = ((MyApplication) (parentActivity.getApplication()))
//                        .getAttentionDaoSession().getChemotherapyMedicineDao();
//                //获取化疗数据
//                List<Chemotherapy> dbList = chemotherapyDao.queryBuilder()
//                        .where(ChemotherapyDao.Properties.Id.eq(id),
//                                ChemotherapyDao.Properties.PatientId.eq(parentActivity.patientId)).list();
//                //获取化疗用药数据
//                dbChemotherapyMedicine.clear();
//                dbChemotherapyMedicine = chemotherapyMedicineDao.queryBuilder().where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(id)).list();
//
//                if (dbList.size()>0){
//                    chemotherapy = dbList.get(0);
//                    subscriber.onCompleted();
//                }
//
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Chemotherapy>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "getDBChemotherapy onCompleted");
//                        chemotherapy.getStatus();
//                        parentActivity.chemotherapyAimName = AttentionUtils.getChemotherapyPurposeName(chemotherapy.getChemotherapyAim());
//                        tvName.setText( AttentionUtils.getChemotherapyPurposeName(chemotherapy.getChemotherapyAim()));
//                        parentActivity.timesName = "第" + chemotherapy.getTimes() + "次化疗";
////                        title.slideCentertext("第" + chemotherapy.getTimes() + "次化疗");
//                        parentActivity.ISNEWCHEMOTHERAPY = true;
////                        tvBeginTime.setText("开始时间： " + changeDateToString(chemotherapy.getStartTime()));
////                        tvPurpose.setText("化疗目的： " + AttentionUtils.getChemotherapyPurposeName(chemotherapy.getChemotherapyAim()));
//                        String statusName;
//                        if (chemotherapy.getStatus() == 2) {
//                            //已完成的化疗
//                            statusName = AttentionUtils.getStopChemotherapyResaonName(chemotherapy.getEndReason());
//                            btStop.setVisibility(View.GONE);
//                            parentActivity.ISNEWCHEMOTHERAPY = false;
//                        } else {
//                            //正在进行中的化疗
//                            statusName = "进行中";
//                            parentActivity.courseInterval = chemotherapy.getCourseInterval();
//                            parentActivity.courseCount = chemotherapy.getCourseCount();
//                            parentActivity.dayOfCourse = chemotherapy.getDayOfCourse();
//                        }
//                        //初始化化疗时间
//                        parentActivity.startTime = chemotherapy.getStartTime();
//                        parentActivity.endTime = chemotherapy.getEndTime();
//
//
//                        slChemotherapyDetailInfo.setAdapter(new ChemotherapyDetailInfoItemAdapter(parentActivity, chemotherapy, dbChemotherapyMedicine));
//                        tvStatus.setText(statusName);
//                        parentActivity.statusName = statusName;
//
//                        scrollContent.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "getDBChemotherapy onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(Chemotherapy chemotherapy) {
//                        Log.i("hcb", "getDBChemotherapy onNext");
//                    }
//                });
//    }
//
//
//    /**
//     * 获取疗程接口
//     *
//     * @param serverTime
//     * @param patientId
//     * @param token
//     */
//    private void getHttpChemotherapyCourse(String serverTime, String patientId, String token) {
//
//        HttpService.getHttpService().getChemotherapyCourseRecords(serverTime, patientId, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<ChemotherapyRecordBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "getHttpChemotherapyCourse onCompleted");
//                        insertChemotherapyCordsDB();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "getHttpChemotherapyCourse onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(ChemotherapyRecordBean chemotherapyAndChemotherapyCourseBean) {
//                        Log.i("hcb", "getHttpChemotherapyCourse onNext");
//                        if (chemotherapyAndChemotherapyCourseBean.getData().getSource().size() > 0) {
//                            httpChemotherapyCourseList = chemotherapyAndChemotherapyCourseBean.getData().getSource();
//                            SharedPreferences.Editor editor = serverTimeSP.edit();
//                            String chemotherapyCourseServerTime = chemotherapyAndChemotherapyCourseBean.getData().getServerTime();
//                            editor.putString("chemotherapyCourseServerTime", chemotherapyCourseServerTime);
//                            editor.commit();
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 插入化疗疗程数据库数据
//     */
//    private void insertChemotherapyCordsDB() {
//        if (httpChemotherapyCourseList.size() > 0) {
//            chemotherapyCourseDao = ((MyApplication) (parentActivity.getApplication()))
//                    .getAttentionDaoSession().getChemotherapyCourseDao();
//            ChemotherapyCourseMedicineDao chemotherapyCourseMedicineDao = ((MyApplication) (parentActivity.getApplication()))
//                    .getAttentionDaoSession().getChemotherapyCourseMedicineDao();
//
//            for (int i = 0; i < httpChemotherapyCourseList.size(); i++) {
//                //化疗疗程入库
//                ChemotherapyRecordBean.Record record = httpChemotherapyCourseList.get(i).getRecord();
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
//                ChemotherapyCourse chemotherapyCourse = new ChemotherapyCourse(id, chemotherapyRecordId, startTime, endTime, endReason,
//                        status, times, description, patientId, isActive);
//                long count = chemotherapyCourseDao.queryBuilder()
//                        .where(ChemotherapyCourseDao.Properties.Id.eq(id)).count();
//                if (count > 0) {
//                    chemotherapyCourseDao.update(chemotherapyCourse);
//                } else {
//                    chemotherapyCourseDao.insert(chemotherapyCourse);
//                }
//
//                //化疗疗程用药入库
//                List<ChemotherapyRecordBean.Medicines> medicinesList
//                        = httpChemotherapyCourseList.get(i).getMedicines();
//                List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicineList = chemotherapyCourseMedicineDao.queryBuilder()
//                        .where(ChemotherapyCourseMedicineDao.Properties.ChemotherapyCourseId.eq(id)).list();
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
//                    ChemotherapyCourseMedicine chemotherapyCourseMedicine = new ChemotherapyCourseMedicine();
//                    chemotherapyCourseMedicine.setMedicineId(medicineId);
//                    chemotherapyCourseMedicine.setChemotherapyCourseId(chemotherapyCourseId);
//                    chemotherapyCourseMedicine.setMedicineName(medicineName);
//
//                    chemotherapyCourseMedicineDao.insert(chemotherapyCourseMedicine);
//                }
//
//            }
//        }
//        if (parentActivity.ISNEWCHEMOTHERAPY) {
//            getHttpChemotherapy(chemotherapyServerTime, parentActivity.patientId, parentActivity.token);
//        } else {
//            getDBChemotherapyCourse(parentActivity.chemotherapyId);
//        }
//
//    }
//
//
//    /**
//     * 获取本次化疗 全部疗程数据库数据
//     *
//     * @param id 化疗Id
//     */
//    private void getDBChemotherapyCourse(final long id) {
//        Observable.create(new Observable.OnSubscribe<ChemotherapyCourse>() {
//            @Override
//            public void call(Subscriber<? super ChemotherapyCourse> subscriber) {
//                chemotherapyCourseDao = ((MyApplication) (parentActivity.getApplication()))
//                        .getAttentionDaoSession().getChemotherapyCourseDao();
//
//                chemotherapyCourseMedicineDao = ((MyApplication) (parentActivity.getApplication()))
//                        .getAttentionDaoSession().getChemotherapyCourseMedicineDao();
//
//                dbList.clear();
//                dbList = chemotherapyCourseDao.queryBuilder()
//                        .where(chemotherapyCourseDao.queryBuilder().and(ChemotherapyCourseDao.Properties.ChemotherapyRecordId.eq(id),
//                                ChemotherapyCourseDao.Properties.PatientId.eq(parentActivity.patientId)),
//                                ChemotherapyCourseDao.Properties.IsActive.eq(true))
//                        .orderAsc(ChemotherapyCourseDao.Properties.Times).list();
//                List<ChemotherapyCourse> dbChemotherapyCourseList = chemotherapyCourseDao.queryBuilder()
//                        .where(chemotherapyCourseDao.queryBuilder().and(ChemotherapyCourseDao.Properties.ChemotherapyRecordId.eq(id),
//                                ChemotherapyCourseDao.Properties.PatientId.eq(parentActivity.patientId)),
//                                ChemotherapyCourseDao.Properties.IsActive.eq(true))
//                        .orderAsc(ChemotherapyCourseDao.Properties.Times).list();
//                //转换数据
//                adapterList.clear();
//                for (ChemotherapyCourse chemotherapyCourse : dbChemotherapyCourseList) {
//                    List<ChemotherapyCourseMedicine> chemotherapyCourseMedicineList = chemotherapyCourseMedicineDao.queryBuilder()
//                            .where(ChemotherapyCourseMedicineDao.Properties.ChemotherapyCourseId.eq(chemotherapyCourse.getId())).list();
//                    ChemotherapyCourseListBean bean = new ChemotherapyCourseListBean(chemotherapyCourse, chemotherapyCourseMedicineList);
//                    adapterList.add(bean);
//                }
//
//
//                Log.i("hcb", "dbList" + dbList.size());
//                subscriber.onCompleted();
//
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ChemotherapyCourse>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "getDBChemotherapyCourse onCompleted");
////                            mAdapter.updateData(dbList);
//                        mAdapter = new AddTreatmentAdapter(parentActivity, adapterList);
//                        mAdapter.setItemClickListenter(new AdapterClickInterface() {
//                            @Override
//                            public void onItemClick(View v, int position) {
//                                ChemotherapyCourse chemotherapyCourse = dbList.get(position);
//                                parentActivity.editChemotherapyCourse(chemotherapyCourse);
//                                parentActivity.notifyFragment(parentActivity.CHEMOTHERAPYCOURSE);
////                                parentActivity.fragmentAttentionChemotherapyAddTreatment.setNotes();
//
//                            }
//
//                            @Override
//                            public void onItemLongClick(View v, int position) {
//
//                            }
//                        });
//                        scrollLinearLayout.setAdapter(mAdapter);
//                        getDBChemotherapy(parentActivity.chemotherapyId);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "getDBChemotherapyCourse onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(ChemotherapyCourse chemotherapyCourse) {
//                        Log.i("hcb", "getDBChemotherapyCourse onNext");
//                    }
//                });
//    }
//
//    /**
//     * 获取网络化疗数据
//     *
//     * @param serverTime
//     * @param patientId
//     * @param token
//     */
//    private void getHttpChemotherapy(String serverTime, String patientId, String token) {
//        HttpService.getHttpService().getChemotherapyRecords(serverTime, patientId, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<ChemotherapyRecordBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "getHttpChemotherapy onCompleted");
//                        insertChemotherapyDB();
//                        getDBChemotherapyCourse(parentActivity.chemotherapyId);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "getHttpChemotherapy onError");
//                        Log.i("onNext", "e" + e);
//
//                    }
//
//                    @Override
//                    public void onNext(ChemotherapyRecordBean chemotherapyAndChemotherapyCourseBean) {
//                        Log.i("hcb", "getHttpChemotherapy onNext");
//                        if (chemotherapyAndChemotherapyCourseBean.getData().getSource().size() > 0) {
//                            httpChemotherapyList = chemotherapyAndChemotherapyCourseBean.getData().getSource();
//                            SharedPreferences.Editor editor = serverTimeSP.edit();
//                            String chemotherapyServerTime = chemotherapyAndChemotherapyCourseBean.getData().getServerTime();
//                            editor.putString("chemotherapyServerTime", chemotherapyServerTime);
//                            editor.commit();
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 插入化疗数据库数据
//     */
//    private void insertChemotherapyDB() {
//        if (httpChemotherapyList.size() > 0) {
//            chemotherapyDao = ((MyApplication) (parentActivity.getApplication()))
//                    .getAttentionDaoSession().getChemotherapyDao();
//            chemotherapyMedicineDao = ((MyApplication) (parentActivity.getApplication()))
//                    .getAttentionDaoSession().getChemotherapyMedicineDao();
//
//            for (int i = 0; i < httpChemotherapyList.size(); i++) {
//                //化疗入库
//                ChemotherapyRecordBean.Record record = httpChemotherapyList.get(i).getRecord();
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
//                Chemotherapy chemotherapy = new Chemotherapy(id, startTime, endTime, courseCount, courseInterval,
//                        status, endReason, chemotherapyAim, dayOfCourse, times, description, patientId, isActive);
//                long count = chemotherapyDao.queryBuilder()
//                        .where(ChemotherapyDao.Properties.Id.eq(id)).count();
//                if (count > 0) {
//                    chemotherapyDao.update(chemotherapy);
//                } else {
//                    chemotherapyDao.insert(chemotherapy);
//                }
//
//                //化疗用药入库
//                List<ChemotherapyRecordBean.Medicines> medicinesList
//                        = httpChemotherapyList.get(i).getMedicines();
//
//                List<ChemotherapyMedicine> dbChemotherapyMedicineList = chemotherapyMedicineDao.queryBuilder()
//                        .where(ChemotherapyMedicineDao.Properties.ChemotherapyId.eq(id)).list();
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
//
//    }
//
//
//    /**
//     * 删除化疗
//     *
//     * @param id
//     */
//    private void deleteChemotherapy(int id) {
//        DeleteRecordBody body = new DeleteRecordBody(id);
//        HttpService.getHttpService().postDeleteChemotherapyRecord(body, parentActivity.token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "deleteChemotherapy onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "deleteChemotherapy onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb", "deleteChemotherapy onNext");
//                        if (createOrDeleteBean.getResult()) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    Intent intent = new Intent(parentActivity, ActivityAttentionList.class);
//                                    parentActivity.setResult(200, intent);
//                                    parentActivity.finish();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//    }
//}
