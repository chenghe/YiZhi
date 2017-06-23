//package com.zhongmeban.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zhongmeban.AdapterClickInterface;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddChemotherapy;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionChemotherapyDetail;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
//import com.zhongmeban.attentionmodle.adapter.AttentionChemotherapyMedicineAdapter;
//import com.zhongmeban.adapter.ChemotherapyPurposeAdapter;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//import com.zhongmeban.view.ScrollLinearLayout;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.dao.AttentionMedicine;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 添加新增化疗Fragment
// * Created by Chengbin He on 2016/7/7.
// */
//public class FragmentAddNewChemotherapy extends BaseFragment implements View.OnClickListener {
//
//    private RecyclerView recyclerPurpose;
//    private ChemotherapyPurposeAdapter chemotherapyPurposeAdapter;
//    private ActivityAttentionAddChemotherapy parentActivity;
//    private RelativeLayout rlStartTime;
//    private Button btSave;
//    private TextView tvStartTime;
//    private EditText edCourseCount;//预计疗程次数
//    private EditText edDayOfCourse;//预计疗程次数
//    private EditText edCourseInterval;//预计疗程次数
//    private TextView tvAddMedicine;//添加用药
//    private ScrollLinearLayout scrollLinearLayout;//添加用药列表
//    private AttentionChemotherapyMedicineAdapter scrollLinearLayoutAdapter;
//    public List<AttentionMedicine> mChooseMedicineList = new ArrayList<AttentionMedicine>();//操作list
//    private MaterialDialog progressDialog;
//    private long chemotherapyId;
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionAddChemotherapy) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_add_new_chemotherapy, container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mChooseMedicineList.clear();
//        mChooseMedicineList.addAll(parentActivity.chooseMedicineList);
//        initTitle(view);
//        initView(view);
//
//    }
//
//    private void initView(View view) {
//
//        btSave = (Button) view.findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        buttonClickable();
//
//        recyclerPurpose = (RecyclerView) view.findViewById(R.id.recyclerview_purpose);
//        chemotherapyPurposeAdapter = new ChemotherapyPurposeAdapter(parentActivity, parentActivity.chemotherapyAim);
//        chemotherapyPurposeAdapter.setItemClickListener(new AdapterClickInterface() {
//            @Override
//            public void onItemClick(View v, int position) {
//                parentActivity.chemotherapyAim = chemotherapyPurposeAdapter.getCount();
//                buttonClickable();
//            }
//
//            @Override
//            public void onItemLongClick(View v, int position) {
//
//            }
//        });
//        recyclerPurpose.setLayoutManager(new GridLayoutManager(parentActivity, 3));
//        recyclerPurpose.setAdapter(chemotherapyPurposeAdapter);
//
//        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(R.id.scrollLinearLayout);
//        scrollLinearLayoutAdapter = new AttentionChemotherapyMedicineAdapter(parentActivity,
//                mChooseMedicineList);
//        scrollLinearLayoutAdapter.setItemClickListenter(new AdapterClickInterface() {
//            @Override
//            public void onItemClick(View v, int position) {
//                mChooseMedicineList.remove(position);
//                scrollLinearLayout.setAdapter(scrollLinearLayoutAdapter);
//                parentActivity.chooseMedicineList.clear();
//                parentActivity.chooseMedicineList.addAll(mChooseMedicineList);
//            }
//
//            @Override
//            public void onItemLongClick(View v, int position) {
//
//            }
//        });
//        scrollLinearLayout.setAdapter(scrollLinearLayoutAdapter);
//
//        rlStartTime = (RelativeLayout) view.findViewById(R.id.rl_date_in);
//        rlStartTime.setOnClickListener(this);
//        tvStartTime = (TextView) view.findViewById(R.id.tv_date_in);
//        if (parentActivity.startTime>0){
//            tvStartTime.setText(changeDateToString(parentActivity.startTime));
//        }
//
//        tvAddMedicine = (TextView) view.findViewById(R.id.tv_add_medicine);
//        tvAddMedicine.setOnClickListener(this);
//
//        edCourseCount = (EditText) view.findViewById(R.id.tv_num_course);
//        if (parentActivity.courseCount>0){
//            edCourseCount.setText(parentActivity.courseCount+"");
//        }
//        edCourseCount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String courseCount = s.toString();
//                try {
//                    parentActivity.courseCount = Integer.parseInt(courseCount);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        edDayOfCourse = (EditText) view.findViewById(R.id.tv_date_course);
//        if (parentActivity.dayOfCourse>0){
//            edDayOfCourse.setText(parentActivity.dayOfCourse+"");
//        }
//        edDayOfCourse.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String dayOfCourse = s.toString();
//                try {
//                    parentActivity.dayOfCourse = Integer.parseInt(dayOfCourse);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        edCourseInterval = (EditText) view.findViewById(R.id.tv_interval_course);
//        if (parentActivity.courseInterval>0){
//            edCourseInterval.setText(parentActivity.courseInterval+"");
//        }
//        edCourseInterval.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String courseInterval = s.toString();
//                try {
//                    parentActivity.courseInterval = Integer.parseInt(courseInterval);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    protected void initTitle(View view) {
//        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//        String titleName = "";
//        if (parentActivity.ISEDIT){
//            titleName = "编辑新的化疗记录";
//        }else {
//            titleName = "添加新的化疗记录";
//        }
//        title.slideCentertext(titleName);
//        title.backSlid(new View.OnClickListener() {
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
//            case R.id.rl_date_in:
//                showDatePicker(parentActivity, onStartDatePickedListener, getTodayData());
//
//                break;
//            case R.id.tv_add_medicine:
//                parentActivity.startChemotherapyMedicineFragment();
//                break;
//            case R.id.bt_save:
//                progressDialog = showProgressDialog("正在上传数据请稍等",parentActivity);
//                if (parentActivity.ISEDIT){//编辑化疗
//                    updateChemotherapyRecord(parentActivity.body,parentActivity.token);
//                }else {//新增化疗
//                    createChemotherapyRecord(parentActivity.body,parentActivity.token);
//                }
//
//                break;
//        }
//    }
//
//    //开始时间Dialog监听
//    OnDatePickedListener onStartDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            tvStartTime.setText(year + "年" + month + "月" + day + "日");
//            parentActivity.startTime = changeDateToLong(dateDesc);
//            buttonClickable();
//        }
//    };
//
//    /**
//     * 创建化疗记录
//     * @param body
//     * @param token
//     */
//    private void createChemotherapyRecord(CreateOrUpdateChemotherapyBody body , String token){
//        parentActivity.initRecord();
//        HttpService.getHttpService().postCreateChemotherapyRecord(body,token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","FragmentAddNewChemotherapy createChemotherapyRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","FragmentAddNewChemotherapy createChemotherapyRecord onError");
//                        Log.i("hcb","e"+e);
//                        progressDialog.dismiss();
//                        Toast.makeText(parentActivity,"上传出错，请重新上传",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(final CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","FragmentAddNewChemotherapy createChemotherapyRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            try {
//                                chemotherapyId = Integer.parseInt(createOrDeleteBean.getData());
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//                            }
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    showAddCourseDialog();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 更新化疗记录
//     * @param body
//     * @param token
//     */
//    private void updateChemotherapyRecord(CreateOrUpdateChemotherapyBody body , String token){
//        parentActivity.initRecord();
//        HttpService.getHttpService().postUpdateChemotherapyRecord(body,token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","FragmentAddNewChemotherapy updateChemotherapyRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","FragmentAddNewChemotherapy updateChemotherapyRecord onError");
//                        Log.i("hcb","e"+e);
//                        progressDialog.dismiss();
//                        Toast.makeText(parentActivity,"上传出错，请重新上传",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(final CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","FragmentAddNewChemotherapy updateChemotherapyRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    Intent intent = new Intent(parentActivity,ActivityAttentionChemotherapyDetail.class);
//                                    if (parentActivity.ISEDIT){
//                                        parentActivity.setResult(300,intent);
//                                    }else {
//                                        parentActivity.setResult(200,intent);
//                                    }
//                                    parentActivity.finish();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 显示是否添加疗程Dialog
//     */
//    private void showAddCourseDialog(){
//        final Intent intent = new Intent(parentActivity,ActivityAttentionChemotherapyDetail.class);
//        intent.putExtra("chemotherapyId",chemotherapyId);
//        intent.putExtra("ISNEWCHEMOTHERAPY",true);
//        new MaterialDialog.Builder(parentActivity)
//                .content("已添加本次化疗，是否继续添加周期信息？")
//                .positiveText("是")
//                .positiveColor(getResources().getColor(R.color.button_red_normanl))
//                .negativeText("否")
//                .negativeColor(getResources().getColor(R.color.app_green))
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        intent.putExtra("ISNEWCHEMOTHERAPYCORDS",true);
//                        intent.putExtra("courseCount",parentActivity.courseCount);
//                        intent.putExtra("courseInterval",parentActivity.courseInterval);
//                        intent.putExtra("dayOfCourse",parentActivity.dayOfCourse);
//                        intent.putExtra("startTime",parentActivity.startTime);
//                        intent.putExtra("chemotherapyId",chemotherapyId);
//                        intent.putExtra("chooseMedicineList", (Serializable) parentActivity.chooseMedicineList);
//                        startActivityForResult(intent,1);
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        startActivityForResult(intent,1);
//                    }
//                }).canceledOnTouchOutside(false).show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 200){
//            Intent intent = new Intent(parentActivity, ActivityAttentionList.class);
//            parentActivity.setResult(200,intent);
//            parentActivity.finish();
//        }
//    }
//
//    private void buttonClickable(){
//        if (parentActivity.startTime>0&&parentActivity.chemotherapyAim>0){
//            btSave.setEnabled(true);
//            btSave.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            btSave.setEnabled(false);
//            btSave.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//}
