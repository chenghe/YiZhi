//package com.zhongmeban.attentionmodle.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.orhanobut.logger.Logger;
//import com.zhongmeban.AdapterClickInterface;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionChemotherapyDetail;
//import com.zhongmeban.attentionmodle.adapter.AttentionChemotherapyMedicineAdapter;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyCourseBody;
//import com.zhongmeban.bean.postbody.DeleteRecordBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.utils.TimeUtils;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//import com.zhongmeban.view.ScrollLinearLayout;
//import de.greenrobot.dao.AttentionMedicine;
//import java.util.ArrayList;
//import java.util.List;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 化疗添加疗程Fragment
// * Created by Chengbin He on 2016/7/12.
// */
//public class FragmentAttentionChemotherapyAddTreatment extends BaseFragment implements View.OnClickListener{
//
//    private ActivityAttentionChemotherapyDetail parentActivity;
//    private Button btSave;
//    private Button btDelete;
////    private Button btSaveAndNext;
//    private RelativeLayout rlStartTime;
//    private RelativeLayout rlEndTime;
//    private TextView tvStartTime;
//    private TextView tvEndTime;
//    private TextView tvAddMedicine;
//    private ScrollLinearLayout scrollLinearLayout;//添加用药列表
//    private AttentionChemotherapyMedicineAdapter scrollLinearLayoutAdapter;
//    private MaterialDialog progressDialog;
//    private EditText editText;
//    public List<AttentionMedicine> mChooseMedicineList = new ArrayList<AttentionMedicine>();//显示用list
//
//    public boolean ISEDIT = false;
//    public boolean FORMEMEDICENE = false;//重用药返回
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
//        View view = inflater.inflate(R.layout.fragment_attention_add_treatment,container,false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initTitle(view);
//        if (!ISEDIT){
//            //新增状态
//            if (!FORMEMEDICENE){
//                parentActivity.getChemotherapyChooseMedicine();
//            }
////            if (parentActivity.endTime==0&&parentActivity.dayOfCourse>0&&parentActivity.courseCount>0&&parentActivity.courseInterval>0){
////                Log.i("hcb","parentActivity.endTime==0&&parentActivity.dayOfCourse>0&&parentActivity.courseCount>0&&parentActivity.courseInterval>0");
////                //判断是否为正在进行的化疗,根据周期计算开始时间结束时间
////                parentActivity.courseStartTime = parentActivity.startTime;
////                parentActivity.courseEndTime = parentActivity.startTime+TimeUtils.changeDayToLong(parentActivity.courseInterval+parentActivity.dayOfCourse);
////            }
//        }else {
////            //编辑状态
////            if (FORMEMEDICENE){
////
////            }
//        }
//
//        mChooseMedicineList.clear();
//        mChooseMedicineList.addAll(parentActivity.chooseMedicineList);
//
//        editText = (EditText) view.findViewById(R.id.et_question);
//
//        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(R.id.scrollLinearLayout);
//        scrollLinearLayoutAdapter = new AttentionChemotherapyMedicineAdapter(parentActivity,
//                mChooseMedicineList);
//        scrollLinearLayoutAdapter.setItemClickListenter(new AdapterClickInterface() {
//            @Override
//            public void onItemClick(View v, int position) {
//                mChooseMedicineList.remove(position);
//                parentActivity.chooseMedicineList.clear();
//                parentActivity.chooseMedicineList.addAll(mChooseMedicineList);
//                scrollLinearLayoutAdapter.notifyDataSetChanged();
//                scrollLinearLayout.setAdapter(scrollLinearLayoutAdapter);
//            }
//
//            @Override
//            public void onItemLongClick(View v, int position) {
//
//            }
//        });
//        scrollLinearLayout.setAdapter(scrollLinearLayoutAdapter);
//
//        btSave = (Button) view.findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        buttonClickable();
//        btDelete = (Button) view.findViewById(R.id.bt_delete);
//        btDelete.setOnClickListener(this);
////        btSaveAndNext = (Button) view.findViewById(R.id.bt_save_next);
////        btSaveAndNext.setVisibility(View.GONE);
//        if (ISEDIT){
//            btDelete.setVisibility(View.VISIBLE);
////            btSaveAndNext.setVisibility(View.GONE);
//        }else {
//            btDelete.setVisibility(View.GONE);
////            btSaveAndNext.setVisibility(View.VISIBLE);
//        }
//
//        tvAddMedicine = (TextView) view.findViewById(R.id.tv_add_medicine);
//        tvAddMedicine.setOnClickListener(this);
//        rlStartTime = (RelativeLayout) view.findViewById(R.id.rl_date_in);
//        rlStartTime.setOnClickListener(this);
//        rlEndTime = (RelativeLayout) view.findViewById(R.id.rl_date_out);
//        rlEndTime.setOnClickListener(this);
//        tvStartTime = (TextView) view.findViewById(R.id.tv_date_in);
//        if (parentActivity.courseStartTime>0){
//            tvStartTime.setText(changeDateToString(parentActivity.courseStartTime));
//        }
//        tvEndTime = (TextView) view.findViewById(R.id.tv_date_out);
//        if (parentActivity.courseEndTime>0){
//            tvEndTime.setText(changeDateToString(parentActivity.courseEndTime));
//        }
//
//    }
//    private void initTitle(View view) {
//        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//        String titleName = "";
//        if (ISEDIT){
//            titleName = "编辑周期信息";
//        }else {
//            titleName = "添加新的周期";
//        }
//        title.slideCentertext(titleName);
//        title.backSlid(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                parentActivity.resetBody();
//                parentActivity.notifyFragment(parentActivity.CHEMOTHERAPY);
//            }
//        });
//    }
//
//
//    //开始时间Dialog监听
//    OnDatePickedListener onStartDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            long startTime = changeDateToLong(dateDesc);
//            if (TimeUtils.checkStartTime(startTime,parentActivity.courseEndTime,parentActivity)){
//                tvStartTime.setText(year+"年"+month+"月"+day+"日");
//                parentActivity.courseStartTime = startTime;
//            }
//            buttonClickable();
//
//        }
//    };
//
//    //结束时间Dialog监听
//    OnDatePickedListener onEndDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//
//            long endTime = changeDateToLong(dateDesc);
//            if (TimeUtils.checkEndTime(parentActivity.courseStartTime,endTime,parentActivity)){
//                tvEndTime.setText(year+"年"+month+"月"+day+"日");
//                parentActivity.courseEndTime = endTime;
//            }
//            buttonClickable();
//
//        }
//    };
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setNotes();
//    }
//
//    public void setNotes(){
//        if (!TextUtils.isEmpty(parentActivity.courseDescription)){
//            editText.setText(parentActivity.courseDescription);
//        }
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
//            case R.id.tv_add_medicine:
//                parentActivity.notifyFragment(3);
//                break;
//            case R.id.bt_save:
//                progressDialog = showProgressDialog("正在上传数据请稍等",parentActivity);
//                parentActivity.courseDescription = editText.getText().toString();
//                Logger.i("FragmentAttentionChemotherapyAddTreatment parentActivity.description"+parentActivity.courseDescription);
//                parentActivity.initChemotherapyCourseBody();
//                if (ISEDIT){
//                    updateChemotherapyCourseRecord(parentActivity.body,parentActivity.token);
//                }else {
//                    createChemotherapyCourseRecord(parentActivity.body,parentActivity.token);
//                }
//
//                break;
//            case R.id.bt_delete:
//                progressDialog = showProgressDialog("正在上传数据请稍等",parentActivity);
//                deleteChemotherapyCourseRecord((int) parentActivity.chemotherapyCourseId,parentActivity.token);
//                break;
//        }
//    }
//
//
//    private void buttonClickable(){
//        if (parentActivity.courseStartTime>0&&parentActivity.courseEndTime>0
//                &&parentActivity.courseStartTime<parentActivity.courseEndTime
//                &&mChooseMedicineList.size()>0){
//            btSave.setEnabled(true);
//            btSave.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            btSave.setEnabled(false);
//            btSave.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//
//    /**
//     * 创建化疗疗程数据
//     * @param body
//     * @param token
//     */
//    private void createChemotherapyCourseRecord( CreateOrUpdateChemotherapyCourseBody body,String token){
//
//        HttpService.getHttpService().postCreateChemotherapyCourseRecord(body,token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","createChemotherapyCourseRecord onCompleted"      );
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","createChemotherapyCourseRecord onError");
//                        Log.i("hcb","e"+e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","createChemotherapyCourseRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    editText.setText("");
//                                    parentActivity.resetBody();
//                                    parentActivity.notifyFragment(parentActivity.CHEMOTHERAPY);
//                                }
//                            },1000);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 修改化疗疗程
//     * @param body
//     * @param token
//     */
//    private void updateChemotherapyCourseRecord( CreateOrUpdateChemotherapyCourseBody body,String token){
//        HttpService.getHttpService().postUpdateChemotherapyCourseRecord(body,token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","updateChemotherapyCourseRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","updateChemotherapyCourseRecord onError");
//                        Log.i("hcb","e"+e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","updateChemotherapyCourseRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    editText.setText("");
//                                    parentActivity.resetBody();
//                                    parentActivity.notifyFragment(parentActivity.CHEMOTHERAPY);
//                                }
//                            },1000);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 删除放疗疗程记录
//     * @param id
//     * @param token
//     */
//    private void deleteChemotherapyCourseRecord(int id,String token){
//        DeleteRecordBody body = new DeleteRecordBody(id);
//        HttpService.getHttpService().posteleteChemotherapyCourseRecord(body,token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","deleteChemotherapyCourseRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","deleteChemotherapyCourseRecord onError");
//                        Log.i("hcb","e"+e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","deleteChemotherapyCourseRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    editText.setText("");
//                                    parentActivity.resetBody();
//                                    parentActivity.notifyFragment(parentActivity.CHEMOTHERAPY);
//                                }
//                            },1000);
//                        }
//                    }
//                });
//    }
//
//    public void  clearEditText(){
//        editText.setText("");
//    }
//}
