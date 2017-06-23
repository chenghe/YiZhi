//package com.zhongmeban.attentionmodle.fragment;
//
//import android.content.Context;
//import android.content.Intent;
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
//import com.zhongmeban.R;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddHospitalized;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionHospitalizedOperation;
//import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
//import com.zhongmeban.base.BaseFragment;
//import com.zhongmeban.bean.CreateOrDeleteBean;
//import com.zhongmeban.bean.postbody.CreateSurgeryRecordBody;
//import com.zhongmeban.net.HttpService;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 添加入院第二步
// * Created by Chengbin He on 2016/7/5.
// */
//public class FragmentAttentionAddHospitalizedStep2 extends BaseFragment implements View.OnClickListener {
//
//    private Button button;
//    private ActivityAttentionAddHospitalized parentActivity;
//    private TextView tvObserve;
//    private TextView tvTreatment;
//    private TextView tvOperation;
//    private TextView tvOperationChoose;//所选择手术名
//    private EditText edNotes;//备注
//    private RelativeLayout rlOperation;
//    private MaterialDialog progressDialog;
//    private  CreateSurgeryRecordBody createSurgeryRecordBody;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        parentActivity = (ActivityAttentionAddHospitalized) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_attention_add_hospitalized_step2, container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        edNotes = (EditText) view.findViewById(R.id.et_question);
//        if (!TextUtils.isEmpty(parentActivity.description)){
//            edNotes.setText(parentActivity.description);
//        }
//        button = (Button) view.findViewById(R.id.bt_save);
//        button.setOnClickListener(this);
//        tvOperation = (TextView) view.findViewById(R.id.tv_operation);
//        tvOperation.setOnClickListener(this);
//        tvTreatment = (TextView) view.findViewById(R.id.tv_treatment);
//        tvTreatment.setOnClickListener(this);
//        tvObserve = (TextView) view.findViewById(R.id.tv_observe);
//        tvObserve.setOnClickListener(this);
//        rlOperation = (RelativeLayout) view.findViewById(R.id.rl_operation);
//        rlOperation.setOnClickListener(this);
//        tvOperationChoose = (TextView) view.findViewById(R.id.tv_operation_choose);
//        purpose(parentActivity.purposeType);
//
//
//        checkButtonClickAble();
//        if (parentActivity.purposeType == 3){
//            tvOperationChoose.setText(parentActivity.therapeuticName);
//        }
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.bt_save://保存Button
//                progressDialog = showProgressDialog("正在上传数据请稍等",parentActivity);
//                if(parentActivity.ISEDIT){
//                    updateHospitalRecord();
//                }else {
//                    createHospitalRecord();
//                }
//                break;
//            case R.id.tv_observe://观察
//                parentActivity.purposeType = 1;
//                purpose(parentActivity.purposeType);
//
//                checkButtonClickAble();
//                break;
//            case R.id.tv_treatment://治疗
//                parentActivity.purposeType = 2;
//                purpose(parentActivity.purposeType);
//
//                checkButtonClickAble();
//                break;
//            case R.id.tv_operation://手术
//                parentActivity.purposeType = 3;
//                purpose(parentActivity.purposeType);
//                break;
//            case R.id.rl_operation://手术项目
//                Intent intent = new Intent(parentActivity, ActivityAttentionHospitalizedOperation.class);
//                if (parentActivity.ISEDIT){
//                    intent.putExtra("ISEDIT", true);
//                    intent.putExtra("surgeryMethodId",parentActivity.surgeryRecordId);
//                }else {
////                    intent.putExtra("operationTime", parentActivity.inTime);
//                    intent.putExtra("hospId", parentActivity.hospId);
//                    intent.putExtra("hospName", parentActivity.hospName);
//                    intent.putExtra("docId", parentActivity.docId);
//                    intent.putExtra("docName", parentActivity.docName);
//                    intent.putExtra("inTime", parentActivity.inTime);
//                    intent.putExtra("outTime", parentActivity.outTime);
//
//                    if (parentActivity.surgeryRecordId>0){
//                        intent.putExtra("surgeryMethodId",parentActivity.surgeryRecordId);
//                        intent.putExtra("CreateSurgeryRecordBody",createSurgeryRecordBody);
//                    }
//                }
//                startActivityForResult(intent, 1);
//                break;
//        }
//    }
//
//    /**
//     * 更换入院类型
//     *
//     * @param type 1.观察 2.治疗 3.手术
//     */
//    private void purpose(int type) {
//        switch (type) {
//            case 1:
//                tvObserve.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvObserve.setTextColor(getResources().getColor(R.color.white));
//                tvTreatment.setBackgroundResource(R.drawable.bg_box);
//                tvTreatment.setTextColor(getResources().getColor(R.color.content_two));
//                tvOperation.setBackgroundResource(R.drawable.bg_box);
//                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
//                rlOperation.setVisibility(View.GONE);
//                break;
//            case 2:
//                tvObserve.setBackgroundResource(R.drawable.bg_box);
//                tvObserve.setTextColor(getResources().getColor(R.color.content_two));
//                tvTreatment.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvTreatment.setTextColor(getResources().getColor(R.color.white));
//                tvOperation.setBackgroundResource(R.drawable.bg_box);
//                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
//                rlOperation.setVisibility(View.GONE);
//                break;
//            case 3:
//                tvObserve.setBackgroundResource(R.drawable.bg_box);
//                tvObserve.setTextColor(getResources().getColor(R.color.content_two));
//                tvTreatment.setBackgroundResource(R.drawable.bg_box);
//                tvTreatment.setTextColor(getResources().getColor(R.color.content_two));
//                tvOperation.setBackgroundColor(getResources().getColor(R.color.app_green));
//                tvOperation.setTextColor(getResources().getColor(R.color.white));
//                rlOperation.setVisibility(View.VISIBLE);
//                break;
//
//            default:
//                tvObserve.setBackgroundResource(R.drawable.bg_box);
//                tvObserve.setTextColor(getResources().getColor(R.color.content_two));
//                tvTreatment.setBackgroundResource(R.drawable.bg_box);
//                tvTreatment.setTextColor(getResources().getColor(R.color.content_two));
//                tvOperation.setBackgroundResource(R.drawable.bg_box);
//                tvOperation.setTextColor(getResources().getColor(R.color.content_two));
//                rlOperation.setVisibility(View.GONE);
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 200) {
//            //获取手术信息
//            createSurgeryRecordBody = (CreateSurgeryRecordBody) data.getSerializableExtra("CreateSurgeryRecordBody");
//            String therapeuticName = createSurgeryRecordBody.getSurgeryRecord().getTherapeuticName();
//            tvOperationChoose.setText(therapeuticName);
//            parentActivity.surgeryRecordsList.add(createSurgeryRecordBody);
//            parentActivity.surgeryRecordId = createSurgeryRecordBody.getSurgeryRecord().getTherapeuticId();
//            Log.i("hcbtest","parentActivity.surgeryRecordId" + parentActivity.surgeryRecordId);
//            checkButtonClickAble();
//        }
//    }
//
//    /**
//     *创建住院记录
//     */
//   private void createHospitalRecord(){
//       parentActivity.description = edNotes.getText().toString();
//       parentActivity.initHospitalRecordBody();
//       HttpService.getHttpService().postCreateHospitalRecord(parentActivity.createOrUpdateHospitalRecordBody,
//               parentActivity.token)
//               .subscribeOn(Schedulers.io())
//               .observeOn(AndroidSchedulers.mainThread())
//               .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                   @Override
//                   public void onCompleted() {
//                       Log.i("hcb","createHospitalRecord onCompleted");
//                   }
//
//                   @Override
//                   public void onError(Throwable e) {
//                       Log.i("hcb","createHospitalRecord onError");
//                       Log.i("hcb","e"+e);
//                   }
//
//                   @Override
//                   public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                       Log.i("hcb","createHospitalRecord onNext");
//                       if(createOrDeleteBean.getResult()){
//                           new Handler().postDelayed(new Runnable() {
//                               @Override
//                               public void run() {
//                                   progressDialog.dismiss();
//                                   Intent intent = new Intent(parentActivity,ActivityAttentionList.class);
//                                   parentActivity.setResult(200,intent);
//                                   parentActivity.finish();
//                               }
//                           }, 1000);
//                       }
//                   }
//               });
//
//   }
//
//    /**
//     *更新住院记录
//     */
//    private void updateHospitalRecord(){
//        parentActivity.description = edNotes.getText().toString();
//        parentActivity.initHospitalRecordBody();
//        HttpService.getHttpService().postUpdateHospitalRecord(parentActivity.createOrUpdateHospitalRecordBody,
//                parentActivity.token)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<CreateOrDeleteBean>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb","updateHospitalRecord onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb","updateHospitalRecord onError");
//                        Log.i("hcb","e"+e);
//                    }
//
//                    @Override
//                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
//                        Log.i("hcb","updateHospitalRecord onNext");
//                        if(createOrDeleteBean.getResult()){
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
//    private void checkButtonClickAble(){
//        if (parentActivity.purposeType ==1||parentActivity.purposeType ==2){
//            button.setEnabled(true);
//            button.setTextColor(getResources().getColor(R.color.white));
//        }else if(parentActivity.purposeType==3&&parentActivity.surgeryRecordId>0){
//            button.setEnabled(true);
//            button.setTextColor(getResources().getColor(R.color.white));
//        }else {
//            button.setEnabled(false);
//            button.setTextColor(getResources().getColor(R.color.content_two));
//        }
//    }
//}
