//package com.zhongmeban.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddRadiotherapy;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.AddRadiotherapyDoseBody;
//import com.zhongmeban.bean.postbody.CreateOrUpdateRadiotherapyBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//import com.zhongmeban.view.DoseDialog;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 新增新的放疗Fragment
// * Created by Chengbin He on 2016/7/13.
// */
//public class FragmentAddNewRadiotherapy extends BaseFragment implements  View.OnClickListener{
//
//    private ActivityAttentionAddRadiotherapy parentActivity;
//    private RelativeLayout rlStartTime;
//    private RelativeLayout rlPredictDose;
//    private RelativeLayout rlCurrentDose;
//    private RelativeLayout rlCurrentCount;
//    private TextView tvStartTime;
//    private EditText etDose;//预计次数
//    private EditText etCurrentDose;//放疗计量
//    private EditText etCurrentCount;//放疗次数
//    private Button btSave;
//    private ViewGroup parentView;
//    private MaterialDialog progressDialog;
//    private int mRadiotherapyId;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionAddRadiotherapy) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_add_new_radiotherapy,container,false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        initTitle(view);
//
//        parentView = (ViewGroup) view;
//        rlStartTime = (RelativeLayout) view.findViewById(R.id.rl_date_in);
//        rlStartTime.setOnClickListener(this);
//
//        rlPredictDose = (RelativeLayout) view.findViewById(R.id.rl_dosage);
//        rlPredictDose.setOnClickListener(this);
//        rlCurrentCount = (RelativeLayout) view.findViewById(R.id.rl_num);
//        rlCurrentCount.setOnClickListener(this);
//        rlCurrentDose = (RelativeLayout) view.findViewById(R.id.rl_corrent_dosage);
//        rlCurrentDose.setOnClickListener(this);
//
//        etCurrentDose = (EditText) view.findViewById(R.id.et_corrent_dose);
//        etCurrentDose.setText(parentActivity.currentDosage);
//        etCurrentCount = (EditText) view.findViewById(R.id.et_time);
//        etCurrentCount.setText(parentActivity.currentCount);
//
//        etDose = (EditText) view.findViewById(R.id.et_dose);
//        if (parentActivity.predictDosage!=null){
//            etDose.setText(parentActivity.predictDosage);
//        }
//        tvStartTime = (TextView) view.findViewById(R.id.tv_date_in);
//        if (parentActivity.startTime>0){
//            tvStartTime.setText(changeDateToString(parentActivity.startTime));
//        }
//        btSave = (Button) view.findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        if (parentActivity.startTime>0){
//            btSave.setTextColor(getResources().getColor(R.color.white));
//            btSave.setEnabled(true);
//        }else {
//            btSave.setTextColor(getResources().getColor(R.color.content_two));
//            btSave.setEnabled(false);
//        }
//        if (parentActivity.ISEDIT){
//            rlCurrentCount.setVisibility(View.VISIBLE);
//            rlCurrentDose.setVisibility(View.VISIBLE);
//        }
//    }
//    protected void initTitle(View view) {
//        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//        String titleName = "添加新的放疗记录";
//        if (parentActivity.ISEDIT){
//            titleName = "编辑新的放疗记录";
//        }
//        title.slideCentertext(titleName);
//        title.backSlid(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        parentActivity.finish();
//                    }
//                });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.rl_date_in:
//                showDatePicker(parentActivity,onDatePickedListener,getTodayData());
//                break;
//            case R.id.rl_dosage:
////                showDoseDialog();
//                break;
//            case R.id.bt_save:
//                progressDialog = showProgressDialog("正在上传数据请稍等",parentActivity);
//                parentActivity.predictDosage = etDose.getText().toString();
//                parentActivity.initNewRadiotherapy();
//                if (parentActivity.ISEDIT){
//                    updateRadiotherapyRecord(parentActivity.body,parentActivity.token);
//                }else {
//                    createRadiotherapyRecord(parentActivity.body,parentActivity.token);
//                }
//                break;
//        }
//    }
//
//    //开始时间Dialog监听
//    OnDatePickedListener onDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            tvStartTime.setText(year+"年"+month+"月"+day+"日");
//            parentActivity.startTime = changeDateToLong(dateDesc);
//            if (parentActivity.startTime>0){
//                btSave.setTextColor(getResources().getColor(R.color.white));
//                btSave.setEnabled(true);
//            }else {
//                btSave.setTextColor(getResources().getColor(R.color.content_two));
//                btSave.setEnabled(false);
//            }
//        }
//    };
//
//
//    /**
//     * 创建放疗记录
//     * @param body
//     * @param token
//     */
//    private void createRadiotherapyRecord(CreateOrUpdateRadiotherapyBody body, String token){
//
//        HttpService.getHttpService().postCreateRadiotherapyRecord(body,token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","createRadiotherapyRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","createRadiotherapyRecord onError");
//                        Log.i("hcb","e"+e);
//                        progressDialog.dismiss();
//                        Toast.makeText(parentActivity,"上传出错，请重新上传",Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","createRadiotherapyRecord onNext");
//                        if (createOrDeleteBean.getResult()){
//                            mRadiotherapyId = Integer.valueOf(createOrDeleteBean.getData());
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    showAddCourseDialog();
//                                }
//                            }, 1000);
//                        }
//
//                    }
//                });
//    }
//
//    /**
//     * 更新放疗记录
//     * @param body
//     * @param token
//     */
//    private void updateRadiotherapyRecord(CreateOrUpdateRadiotherapyBody body, String token){
//            HttpService.getHttpService().postUpdateRadiotherapyRecord(body,token)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                        @Override
//                        public void onCompleted() {
//                            Log.i("hcb","updateRadiotherapyRecord onCompleted");
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.i("hcb","updateRadiotherapyRecord onError");
//                            Log.i("hcb","e" + e);
//                            progressDialog.dismiss();
//                            Toast.makeText(parentActivity,"上传出错，请重新上传",Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                            Log.i("hcb","updateRadiotherapyRecord onNext");
//                            if (createOrDeleteBean.getResult()){
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        progressDialog.dismiss();
//                                        parentActivity.setResult(200);
//                                        parentActivity.finish();
//                                    }
//                                }, 1000);
//                            }
//                        }
//                    });
//    }
//    /**
//     * 增加放疗剂量
//     *
//     * @param dose
//     * @param token
//     */
//    private void addRadiotherapyDose(String dose, final String token) {
//        AddRadiotherapyDoseBody body = new AddRadiotherapyDoseBody();
//        body.setId(mRadiotherapyId);
//        body.setCurrentDosage(dose);
//        HttpService.getHttpService().postUpdateRadiotherapyRecordAddDosage(body, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "addRadiotherapyDose onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "addRadiotherapyDose onError");
//                        Log.i("hcb", "e" + e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb", "addRadiotherapyDose onNext");
//                        if (createOrDeleteBean.getResult()) {
//                            parentActivity.setResult(200);
//                            parentActivity.finish();
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 显示是否添加疗程Dialog
//     */
//    private void showAddCourseDialog(){
//        new MaterialDialog.Builder(parentActivity)
//                .content("已添加本次放疗，是否继续添加一次放疗剂量？")
//                .positiveText("是")
//                .positiveColor(getResources().getColor(R.color.button_red_normanl))
//                .negativeText("否")
//                .negativeColor(getResources().getColor(R.color.app_green))
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        showDoseDialog();
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        parentActivity.setResult(200);
//                        parentActivity.finish();
//                    }
//                }).canceledOnTouchOutside(false).show();
//    }
//    /**
//     * 显示计量Dialog
//     */
//    private void showDoseDialog() {
//
//        DoseDialog.Builder builder = new DoseDialog.Builder(parentActivity, parentView);
//        View bottomDialog = LayoutInflater.from(parentActivity).inflate(R.layout.dialog_marker_dose, parentView, false);
//        final EditText edit = (EditText) bottomDialog.findViewById(R.id.edit);
//        TextView tvFinish = (TextView) bottomDialog.findViewById(R.id.tv_finish);
//        TextView tvCancel = (TextView) bottomDialog.findViewById(R.id.tv_cancel);
//        builder.setView(bottomDialog);
//        final DoseDialog dialog = builder.showDialog();
//
//        tvFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                String dose = edit.getText().toString();
//                closeInputKeyBord(edit);
//                addRadiotherapyDose(dose, parentActivity.token);
//
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//            }
//        });
//
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                parentActivity.setResult(200);
//                parentActivity.finish();
//                closeInputKeyBord(edit);
//            }
//        });
//
//    }
//
//    /**
//     * 收起软键盘
//     */
//    private void closeInputKeyBord(EditText edit){
//        InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromInputMethod(edit.getWindowToken(), 0);
////        InputMethodManager imm =  (InputMethodManager)parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
////        if(imm != null) {
////            imm.hideSoftInputFromWindow(parentActivity.getWindow().getDecorView().getWindowToken(),
////                    0);
////        }
//    }
//
//}
