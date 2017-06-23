package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionAddOperation;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionMedicineList;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionTherapeuticByDisId;
import com.zhongmeban.attentionmodle.adapter.AttentionOperationOtherAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.AttentionOtherTherapeutic;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.view.ScrollLinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 新增手术第二步
 * Created by Chengbin He on 2016/7/5.
 */
public class FragmentAttentionAddOperationStep2 extends BaseFragment implements View.OnClickListener {

    private Button button;
    private ActivityAttentionAddOperation parentActivity;
    private RelativeLayout rlType;
    private TextView tvType;
    private TextView tvAdd;//添加项目
    private TextView tvNoOther;
    private EditText edDesc;//备注
    private ScrollLinearLayout scrollLinearLayout;
    private AttentionOperationOtherAdapter mAdapter;
    private MaterialDialog progressDialog;
    private List<AttentionOtherTherapeutic> otherTherapeuticList = new ArrayList<AttentionOtherTherapeutic>();
    public List<SurgeryMethods>mChooseMethods = new ArrayList<>();//选中其他手术项

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionAddOperation) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention_add_operation_step2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChooseMethods.clear();
        Log.i("hcbtest","parentActivity.chooseMethods.size()"+parentActivity.chooseMethods.size());
        mChooseMethods.addAll(parentActivity.chooseMethods);

        edDesc = (EditText) view.findViewById(R.id.et_desc);
        if (!TextUtils.isEmpty(parentActivity.description)){
            edDesc.setText(parentActivity.description);
        }

        button = (Button) view.findViewById(R.id.bt_save);
        button.setOnClickListener(this);
        if (parentActivity.therapeuticId>0) {
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.white));
        } else {
            button.setEnabled(false);
            button.setTextColor(getResources().getColor(R.color.content_two));
        }

        rlType = (RelativeLayout) view.findViewById(R.id.rl_type);
        tvType = (TextView) view.findViewById(R.id.tv_type);
        rlType.setOnClickListener(this);
        if (!TextUtils.isEmpty(parentActivity.therapeuticName)) {
            tvType.setText(parentActivity.therapeuticName);
        }

        tvAdd = (TextView) view.findViewById(R.id.tv_add);
        tvAdd.setOnClickListener(this);

        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(R.id.scroll_ll);
//        tvNoOther = (TextView) view.findViewById(R.id.tv_no_other);
        mAdapter = new AttentionOperationOtherAdapter(parentActivity, mChooseMethods);
        mAdapter.SetItemClickListenter(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                mChooseMethods.remove(position);
                parentActivity.chooseMethods.clear();
                parentActivity.chooseMethods.addAll(mChooseMethods);
                scrollLinearLayout.setAdapter(mAdapter);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        scrollLinearLayout.setAdapter(mAdapter);
//        if (parentActivity.surgeryMethodsList.size() > 0) {
//
//
//            for (int i = 0; i < parentActivity.surgeryMethodsList.size(); i++) {
//                if (parentActivity.surgeryMethodsList.get(i).isCheck()) {
//                    otherTherapeuticList.add(parentActivity.surgeryMethodsList.get(i));
//                }
//            }
//            if (otherTherapeuticList.size()>0){
//                tvNoOther.setVisibility(View.GONE);
//                scrollLinearLayout.setVisibility(View.VISIBLE);
//
//                mAdapter = new AttentionOperationOtherAdapter(parentActivity,
//                        otherTherapeuticList);
//                mAdapter.SetItemClickListenter(new AdapterClickInterface() {
//                    @Override
//                    public void onItemClick(View v, int position) {
////                        otherTherapeuticList.remove(position);
////                        parentActivity.surgeryMethodsList.get(position).setCheck(false);
////                        mAdapter.notifyData(otherTherapeuticList);
//                    }
//
//                    @Override
//                    public void onItemLongClick(View v, int position) {
//
//                    }
//                });
//                scrollLinearLayout.setAdapter(mAdapter);
//            }else {
//                tvNoOther.setVisibility(View.VISIBLE);
//                scrollLinearLayout.setVisibility(View.GONE);
//            }
//
//        } else {
//            tvNoOther.setVisibility(View.VISIBLE);
//            scrollLinearLayout.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                parentActivity.description = edDesc.getText().toString();
                if (parentActivity.ISEDIT){
                    //判断是从编辑页面进入
                    progressDialog = showProgressDialog("正在上传数据，请稍等",parentActivity);
                    updateSurgeryRecord();

                }else if (parentActivity.ISFROMEHOSP){
                    //判断是从住院页面进入
//                    List<Integer> list = new ArrayList<Integer>();
//                    for (int i = 0; i < parentActivity.surgeryMethodsList.size(); i++) {
//                        if (parentActivity.surgeryMethodsList.get(i).isCheck()) {
//                            int id =  parentActivity.surgeryMethodsList.get(i).getData().getId();
//                            list.add(id);
//                        }
//                    }

//                    parentActivity.createSurgeryRecordBody.setMethodIds(list);
                    parentActivity.initSurgeryRecordItem();
//                    Intent intent = new Intent(parentActivity, ActivityAttentionAddHospitalized.class);
//                    intent.putExtra("CreateSurgeryRecordBody",parentActivity.createSurgeryRecordBody);
//                    parentActivity.setResult(200,intent);
//                    parentActivity.finish();

                } else {
                    //判断是新增手术
                    progressDialog = showProgressDialog("正在上传数据，请稍等",parentActivity);
                    createSurgeryRecord();
                }

//                parentActivity.finish();
                break;

            case R.id.rl_type:
                Intent intent = new Intent(parentActivity, ActivityAttentionTherapeuticByDisId.class);
                intent.putExtra("diseaseId",parentActivity.patientDiseaseId);
                startActivityForResult(intent, 1);
                break;

            case R.id.tv_add:
                parentActivity.notifyFragment(3);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            parentActivity.therapeuticName = data.getStringExtra("therapeuticName");
            parentActivity.therapeuticId = data.getIntExtra("therapeuticId",0);
            Log.i("hcb"," parentActivity.therapeuticId" + parentActivity.therapeuticId);
            tvType.setText(parentActivity.therapeuticName);
            if (parentActivity.therapeuticId>0) {
                button.setEnabled(true);
                button.setTextColor(getResources().getColor(R.color.white));
            } else {
                button.setEnabled(false);
                button.setTextColor(getResources().getColor(R.color.content_two));
            }

        }
    }

    /**
     * 创建手术记录
     */
    private void createSurgeryRecord(){
//        List<Integer> list = new ArrayList<Integer>();
//        for (int i = 0; i < parentActivity.surgeryMethodsList.size(); i++) {
//            if (parentActivity.surgeryMethodsList.get(i).isCheck()) {
//               int id =  parentActivity.surgeryMethodsList.get(i).getData().getId();
//                list.add(id);
//            }
//        }
//
//        parentActivity.createSurgeryRecordBody.setMethodIds(list);
//        parentActivity.description = edDesc.getText().toString();
        parentActivity.initSurgeryRecordItem();

        HttpService.getHttpService().postCreateSurgeryRecord(parentActivity.createSurgeryRecordBody,
                parentActivity.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","createSurgeryRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","createSurgeryRecord onError");
                        Log.i("hcb","e" +e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","createSurgeryRecord onNext");
                        if(createOrDeleteBean.getResult()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(parentActivity,ActivityAttentionMedicineList.class);
                                    parentActivity.setResult(200,intent);
                                    parentActivity.finish();
                                }
                            }, 1000);
                        }
                    }
                });

    }


    /**
     * 更新手术记录
     */
    private void updateSurgeryRecord(){
//        List<Integer> list = new ArrayList<Integer>();
//        for (int i = 0; i < parentActivity.surgeryMethodsList.size(); i++) {
//            if (parentActivity.surgeryMethodsList.get(i).isCheck()) {
//                int id =  parentActivity.surgeryMethodsList.get(i).getData().getId();
//                list.add(id);
//            }
//        }
//        parentActivity.createSurgeryRecordBody.setMethodIds(list);
//        parentActivity.description = edDesc.getText().toString();
        parentActivity.initSurgeryRecordItem();

        HttpService.getHttpService().postUpdateSurgeryRecord(parentActivity.createSurgeryRecordBody,
                parentActivity.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","updateSurgeryRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","updateSurgeryRecord onError");
                        Log.i("hcb","e" +e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","updateSurgeryRecord onNext");
                        if(createOrDeleteBean.getResult()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(parentActivity,ActivityAttentionList.class);
                                    parentActivity.setResult(200,intent);
                                    parentActivity.finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }

}
