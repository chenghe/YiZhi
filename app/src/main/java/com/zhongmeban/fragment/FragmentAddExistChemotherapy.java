//package com.zhongmeban.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
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
//import com.zhongmeban.utils.TimeUtils;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//import com.zhongmeban.view.ScrollLinearLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.dao.AttentionMedicine;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 添加历史化疗Fragment
// * Created by Chengbin He on 2016/7/7.
// */
//public class FragmentAddExistChemotherapy extends BaseFragment implements View.OnClickListener{
//
//    private RecyclerView recyclerPurpose;
//    private ChemotherapyPurposeAdapter chemotherapyPurposeAdapter;
//    private ActivityAttentionAddChemotherapy parentActivity;
//    private RelativeLayout rlStartTime;
//    private RelativeLayout rlEndTime;
//    private TextView tvStartTime;
//    private TextView tvEndTime;
//    private TextView tvFinish;
//    private TextView tvStop;
//    private TextView tvChange;
//    private TextView tvAddMedicine;
//    private Button btSave;
//    private EditText etNotes;
//    private ScrollLinearLayout scrollLinearLayout;//添加用药列表
//    private AttentionChemotherapyMedicineAdapter scrollLinearLayoutAdapter;
//    private MaterialDialog progressDialog;
//    public List<AttentionMedicine> mChooseMedicineList = new ArrayList<AttentionMedicine>();//操作list
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
//        View view = inflater.inflate(R.layout.fragment_add_exist_chemotherapy,container,false);
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
//        purpose(parentActivity.endReason);
//    }
//
//    protected void initTitle(View view) {
//        LayoutActivityTitle  title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//        String titleName = "";
//        if (parentActivity.ISEDIT){
//            titleName = "编辑历史化疗记录";
//        }else {
//            titleName = "添加历史化疗记录";
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
//    private void initView(View view){
//
//        etNotes = (EditText) view.findViewById(R.id.et_question);
//        if (!TextUtils.isEmpty(parentActivity.description)){
//            etNotes.setText(parentActivity.description);
//        }
//
//        recyclerPurpose = (RecyclerView) view.findViewById(R.id.recyclerview_purpose);
//        recyclerPurpose.setLayoutManager(new GridLayoutManager(parentActivity,3));
//        chemotherapyPurposeAdapter = new ChemotherapyPurposeAdapter(parentActivity,parentActivity.chemotherapyAim);
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
//        recyclerPurpose.setAdapter(chemotherapyPurposeAdapter );
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
//        rlEndTime = (RelativeLayout) view.findViewById(R.id.rl_date_out);
//        rlEndTime.setOnClickListener(this);
//
//        tvStartTime = (TextView) view.findViewById(R.id.tv_date_in);
//        if (parentActivity.startTime>0){
//            tvStartTime.setText(changeDateToString(parentActivity.startTime));
//        }
//        tvEndTime = (TextView) view.findViewById(R.id.tv_date_out);
//        if (parentActivity.endTime>0){
//            tvEndTime.setText(changeDateToString(parentActivity.endTime));
//        }
//        tvFinish = (TextView) view.findViewById(R.id.tv_finish);
//        tvFinish.setOnClickListener(this);
//        tvStop = (TextView) view.findViewById(R.id.tv_stop);
//        tvStop.setOnClickListener(this);
//        tvChange = (TextView) view.findViewById(R.id.tv_change);
//        tvChange.setOnClickListener(this);
//
//        tvAddMedicine = (TextView) view.findViewById(R.id.tv_add_medicine);
//        tvAddMedicine.setOnClickListener(this);
//
//        btSave = (Button) view.findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        buttonClickable();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.rl_date_in:
//                showDatePicker(parentActivity,onStartDatePickedListener,getTodayData());
//                break;
//            case R.id.rl_date_out:
//                showDatePicker(parentActivity,onEndDatePickedListener,getTodayData());
//                break;
//            case R.id.tv_finish:
//                parentActivity.endReason = 1;
//                purpose(1);
//                buttonClickable();
//                break;
//            case R.id.tv_stop:
//                purpose(2);
//                parentActivity.endReason = 2;
//                buttonClickable();
//                break;
//            case R.id.tv_change:
//                parentActivity.endReason = 3;
//                purpose(3);
//                buttonClickable();
//                break;
//            case R.id.tv_add_medicine:
//                parentActivity.startChemotherapyMedicineFragment();
//                break;
//            case R.id.bt_save:
//               parentActivity.chemotherapyAim =  chemotherapyPurposeAdapter.getCount();
//                parentActivity.description = etNotes.getText().toString();
//                progressDialog = showProgressDialog("正在上传数据请稍等",parentActivity);
//                if (parentActivity.ISEDIT){//编辑化疗
//                    updateChemotherapyRecord(parentActivity.body,parentActivity.token);
//                }else {//新增化疗
//                    createChemotherapyRecord(parentActivity.body,parentActivity.token);
//                }
//                break;
//        }
//
//    }
//
//    //开始时间Dialog监听
//    OnDatePickedListener onStartDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//
//            long startTime =changeDateToLong(dateDesc);
//            if (TimeUtils.checkStartTime(startTime,parentActivity.endTime,parentActivity)){
//                parentActivity.startTime = startTime;
//                tvStartTime.setText(year+"年"+month+"月"+day+"日");
//                buttonClickable();
//            }
//        }
//    };
//
//    //结束时间Dialog监听
//    OnDatePickedListener onEndDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//
//            long endTime =changeDateToLong(dateDesc);
//            if (TimeUtils.checkEndTime(parentActivity.startTime,endTime,parentActivity)){
//                parentActivity.endTime = changeDateToLong(dateDesc);
//                tvEndTime.setText(year+"年"+month+"月"+day+"日");
//                buttonClickable();
//            }
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
//                        Log.i("hcb","FragmentAddExistChemotherapy createChemotherapyRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","FragmentAddExistChemotherapy createChemotherapyRecord onError");
//                        Log.i("hcb","e"+e);
//                        progressDialog.dismiss();
//                        Toast.makeText(parentActivity,"上传出错，请重新上传",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","FragmentAddExistChemotherapy createChemotherapyRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    Intent intent = new Intent(parentActivity,ActivityAttentionList.class);
//                                    parentActivity.setResult(200,intent);
//                                    parentActivity.finish();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//
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
//
//                                    parentActivity.finish();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 终止化疗原因
//     *
//     * @param type 1.疗程结束 2.终止化疗 3.更换方案
//     */
//    private void purpose(int type) {
//        switch (type) {
//            case 1:
//                tvFinish.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvFinish.setTextColor(getResources().getColor(R.color.white));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                tvChange.setBackgroundResource(R.drawable.bg_box);
//                tvChange.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 2:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvStop.setTextColor(getResources().getColor(R.color.white));
//                tvChange.setBackgroundResource(R.drawable.bg_box);
//                tvChange.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 3:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                tvChange.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvChange.setTextColor(getResources().getColor(R.color.white));
//                break;
//
//            default:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                tvChange.setBackgroundResource(R.drawable.bg_box);
//                tvChange.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//        }
//    }
//
//    private void buttonClickable(){
//        if (parentActivity.startTime>0&&parentActivity.endTime>0&&parentActivity.endReason>0&&parentActivity.chemotherapyAim>0){
//            btSave.setEnabled(true);
//            btSave.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            btSave.setEnabled(false);
//            btSave.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//}
