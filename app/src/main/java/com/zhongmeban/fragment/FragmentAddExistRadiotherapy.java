//package com.zhongmeban.fragment;
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
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddRadiotherapy;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.CreateOrUpdateRadiotherapyBody;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.LayoutActivityTitle;
//import com.zhongmeban.utils.ToastUtil;
//import com.zhongmeban.view.DatePicker.OnDatePickedListener;
//
//import java.util.Calendar;
//
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 新增历史放疗Fragment
// * Created by Chengbin He on 2016/7/13.
// */
//public class FragmentAddExistRadiotherapy extends BaseFragment implements View.OnClickListener {
//
//    private ActivityAttentionAddRadiotherapy parentActivity;
//    private RelativeLayout rlStartTime;
//    private RelativeLayout rlEndTime;
//    private RelativeLayout rlPredictDose;
//    private RelativeLayout rlCurrentCount;
//    private TextView tvStartTime;
//    private TextView tvEndTime;
//    private TextView tvDosage;
//    private TextView tvCurrentCount;
//    private Button btSave;
//    private ViewGroup parentView;
//    private RelativeLayout rlStopReason;//选择终止放疗 原因
//    private TextView tvFinish;//疗程结束
//    private TextView tvStop;//终止放疗
//    private TextView tvOperation;//准备手术
//    private TextView tvTolerance;//耐受性原因
//    private TextView tvOther;//其他原因
//    private EditText etDose;//放疗剂量
//    private EditText etTime;//放疗次数
//    private EditText etNotes;//备注信息
//    private MaterialDialog progressDialog;
//
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
//        View view = inflater.inflate(R.layout.fragment_add_exist_radiotherapy, container, false);
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
//
//        etDose = (EditText) view.findViewById(R.id.et_dose);
//        etDose.setText(parentActivity.currentDosage);
//        etTime = (EditText) view.findViewById(R.id.et_time);
//        etTime.setText(parentActivity.currentCount);
//        etNotes = (EditText) view.findViewById(R.id.et_question);
//        if (!TextUtils.isEmpty(parentActivity.notes)){
//            etNotes.setText(parentActivity.notes);
//        }
//
//        rlStartTime = (RelativeLayout) view.findViewById(R.id.rl_date_in);
//        rlStartTime.setOnClickListener(this);
//
//        rlEndTime = (RelativeLayout) view.findViewById(R.id.rl_date_out);
//        rlEndTime.setOnClickListener(this);
//
//        rlPredictDose = (RelativeLayout) view.findViewById(R.id.rl_dosage);
//
//        rlCurrentCount = (RelativeLayout) view.findViewById(R.id.rl_num);
////        tvDosage = (TextView) view.findViewById(R.id.tv_dosage);
//        tvStartTime = (TextView) view.findViewById(R.id.tv_date_in);
//        if (parentActivity.startTime > 0) {
//            tvStartTime.setText(changeDateToString(parentActivity.startTime));
//        }
//
//        tvEndTime = (TextView) view.findViewById(R.id.tv_date_out);
//        if (parentActivity.endTime > 0) {
//            tvEndTime.setText(changeDateToString(parentActivity.endTime));
//        }
//        tvCurrentCount = (TextView) view.findViewById(R.id.tv_num);
//
//        rlStopReason = (RelativeLayout) view.findViewById(R.id.rl_stop_reason);
//
//        tvFinish = (TextView) view.findViewById(R.id.tv_finish);
//        tvFinish.setOnClickListener(this);
//
//        tvStop = (TextView) view.findViewById(R.id.tv_stop);
//        tvStop.setOnClickListener(this);
//
//        tvTolerance = (TextView) view.findViewById(R.id.tv_tolerance);
//        tvTolerance.setOnClickListener(this);
//
//        tvOther = (TextView) view.findViewById(R.id.tv_other);
//        tvOther.setOnClickListener(this);
//
//        tvOperation = (TextView) view.findViewById(R.id.tv_operation);
//        tvOperation.setOnClickListener(this);
//
//        btSave = (Button) view.findViewById(R.id.bt_save);
//        btSave.setOnClickListener(this);
//        buttonClickable();
//        endRadiotherapyUI(parentActivity.status);
//        endRaiotherapyReason(parentActivity.endReason);
//        if (parentActivity.status == 3){
//            rlStopReason.setVisibility(View.VISIBLE);
//        }
//    }
//
//    protected void initTitle(View view) {
//        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
//        String titleName = "添加历史放疗记录";
//        if (parentActivity.ISEDIT){
//            titleName = "编辑历史放疗记录";
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
//        Calendar cal = Calendar.getInstance();
//        switch (v.getId()) {
//            case R.id.rl_date_in://开始日期
//                showDatePicker(parentActivity, onStartDatePickedListener, getTodayData(), BaseFragment.minYear,
//                        cal.get(Calendar.YEAR) + 1);
//                break;
//            case R.id.rl_date_out://结束日期
//                showDatePicker(parentActivity, onEndDatePickedListener, getTodayData(), BaseFragment.minYear,
//                        cal.get(Calendar.YEAR) + 1);
//                break;
////            case R.id.rl_dosage://放疗剂量
////                showDoseDialog(true);
////                break;
////            case R.id.rl_num://放疗次数
////                showDoseDialog(false);
////                break;
//            case R.id.bt_save://保存
//                progressDialog = showProgressDialog("正在上传数据请稍等", parentActivity);
//                parentActivity.currentDosage = etDose.getText().toString();
//                parentActivity.currentCount = etTime.getText().toString();
//                parentActivity.notes = etNotes.getText().toString();
//                if (TextUtils.isEmpty(parentActivity.currentDosage)||TextUtils.isEmpty(parentActivity.currentCount)){
//                    ToastUtil.showText(parentActivity,"请输入放疗剂量和放疗次数");
//                }else {
//                    parentActivity.initNewRadiotherapy();
//                    if (parentActivity.ISEDIT) {
//                        updateRadiotherapyRecord(parentActivity.body, parentActivity.token);
//                    } else {
//                        createRadiotherapyRecord(parentActivity.body, parentActivity.token);
//                    }
//                }
//
//                break;
//            case R.id.tv_finish://疗程结束
//                parentActivity.status = 2;
//                parentActivity.endReason = 0;
//                endRadiotherapyUI(2);
//                rlStopReason.setVisibility(View.GONE);
//                endRaiotherapyReason(0);
//                break;
//            case R.id.tv_stop://终止放疗
//                parentActivity.status = 3;
//                endRadiotherapyUI(3);
//                rlStopReason.setVisibility(View.VISIBLE);
//                break;
//            case R.id.tv_operation://准备手术
//                parentActivity.endReason = 1;
//                endRaiotherapyReason(1);
//                break;
//            case R.id.tv_tolerance://耐受性原因
//                parentActivity.endReason = 2;
//                endRaiotherapyReason(2);
//                break;
//            case R.id.tv_other://其他原因
//                parentActivity.endReason = 3;
//                endRaiotherapyReason(3);
//                break;
//        }
//    }
//
//    //开始时间Dialog监听
//    OnDatePickedListener onStartDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            if (changeDateToLong(dateDesc) >= changeDateToLong(getTodayData())) {
//                ToastUtil.showText(parentActivity, "开始时间必须小于今天请重新选择");
//            } else if (parentActivity.endTime > changeDateToLong(dateDesc)) {
//                tvStartTime.setText(year + "年" + month + "月" + day + "日");
//                parentActivity.startTime = changeDateToLong(dateDesc);
//                buttonClickable();
//            } else if (parentActivity.endTime == 0) {
//                tvStartTime.setText(year + "年" + month + "月" + day + "日");
//                parentActivity.startTime = changeDateToLong(dateDesc);
//                buttonClickable();
//            } else {
//                ToastUtil.showText(parentActivity, "开始时间必须小于结束时间请重新选择");
//            }
//
//        }
//    };
//
//    //结束时间Dialog监听
//    OnDatePickedListener onEndDatePickedListener = new OnDatePickedListener() {
//        @Override
//        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
//            if (changeDateToLong(dateDesc) > changeDateToLong(getTodayData())) {
//                ToastUtil.showText(parentActivity, "结束时间必须小于今天请重新选择");
//            } else if (changeDateToLong(dateDesc) > parentActivity.startTime) {
//                tvEndTime.setText(year + "年" + month + "月" + day + "日");
//                parentActivity.endTime = changeDateToLong(dateDesc);
//            } else {
//                ToastUtil.showText(parentActivity, "开始时间必须小于结束时间请重新选择");
//            }
//
//        }
//    };
//
//    private void buttonClickable(){
//        if (parentActivity.startTime>0&&parentActivity.endTime>0&&parentActivity.status>1){
//            btSave.setEnabled(true);
//            btSave.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            btSave.setEnabled(false);
//            btSave.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//
//    /**
//     * 创建放疗记录
//     *
//     * @param body
//     * @param token
//     */
//    private void createRadiotherapyRecord(CreateOrUpdateRadiotherapyBody body, String token) {
//
//        HttpService.getHttpService().postCreateRadiotherapyRecord(body, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "createRadiotherapyRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "createRadiotherapyRecord onError");
//                        Log.i("hcb", "e" + e);
//                        progressDialog.dismiss();
//                        Toast.makeText(parentActivity, "上传出错，请重新上传", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb", "createRadiotherapyRecord onNext");
//                        if (createOrDeleteBean.getResult()) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    parentActivity.setResult(200);
//                                    parentActivity.finish();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//
//    }
//
//
//    /**
//     * 更新放疗记录
//     *
//     * @param body
//     * @param token
//     */
//    private void updateRadiotherapyRecord(CreateOrUpdateRadiotherapyBody body, String token) {
//        HttpService.getHttpService().postUpdateRadiotherapyRecord(body, token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "updateRadiotherapyRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "updateRadiotherapyRecord onError");
//                        Log.i("hcb", "e" + e);
//                        progressDialog.dismiss();
//                        Toast.makeText(parentActivity, "上传出错，请重新上传", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb", "updateRadiotherapyRecord onNext");
//                        if (createOrDeleteBean.getResult()) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    progressDialog.dismiss();
//                                    parentActivity.setResult(200);
//                                    parentActivity.finish();
//                                }
//                            }, 1000);
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 结束放疗
//     */
//    private void endRadiotherapyUI(int type) {
//        switch (type) {
//            case 2:
//                tvFinish.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvFinish.setTextColor(getResources().getColor(R.color.white));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 3:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvStop.setTextColor(getResources().getColor(R.color.white));
//                break;
//            default:
//                tvFinish.setBackgroundResource(R.drawable.bg_box);
//                tvFinish.setTextColor(getResources().getColor(R.color.content_two));
//                tvStop.setBackgroundResource(R.drawable.bg_box);
//                tvStop.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//        }
//        buttonClickable();
//    }
//
//
//    /**
//     * 终止放疗原因
//     */
//    private void endRaiotherapyReason(int type) {
//        switch (type) {
//            case 1:
//                tvOperation.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvOperation.setTextColor(getResources().getColor(R.color.white));
//                tvTolerance.setBackgroundResource(R.drawable.bg_box);
//                tvTolerance.setTextColor(getResources().getColor(R.color.content_two));
//                tvOther.setBackgroundResource(R.drawable.bg_box);
//                tvOther.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 2:
//                tvOperation.setBackgroundResource(R.drawable.bg_box);
//                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
//                tvTolerance.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvTolerance.setTextColor(getResources().getColor(R.color.white));
//                tvOther.setBackgroundResource(R.drawable.bg_box);
//                tvOther.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//            case 3:
//                tvOperation.setBackgroundResource(R.drawable.bg_box);
//                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
//                tvTolerance.setBackgroundResource(R.drawable.bg_box);
//                tvTolerance.setTextColor(getResources().getColor(R.color.content_two));
//                tvOther.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvOther.setTextColor(getResources().getColor(R.color.white));
//                break;
//            default:
//                tvOperation.setBackgroundResource(R.drawable.bg_box);
//                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
//                tvTolerance.setBackgroundResource(R.drawable.bg_box);
//                tvTolerance.setTextColor(getResources().getColor(R.color.content_two));
//                tvOther.setBackgroundResource(R.drawable.bg_box);
//                tvOther.setTextColor(getResources().getColor(R.color.content_two));
//                break;
//        }
//    }
//
//}
