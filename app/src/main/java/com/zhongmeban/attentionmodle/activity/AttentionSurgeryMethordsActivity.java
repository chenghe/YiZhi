package com.zhongmeban.attentionmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.adapter.AttentionTherapeuticAdapter;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.AttentionOtherTherapeutic;
import com.zhongmeban.bean.SurgeryMethodsByDiseaseId;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.okhttp.utils.L;
import com.zhongmeban.utils.genericity.SPInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 手术其他手术项Activity
 * Created by Chengbin He on 2017/1/5.
 */

public class AttentionSurgeryMethordsActivity extends BaseActivityToolBar {

    public static final String SurgeryMethodsList = "surgeryMethods";
    public static final int SurgeryMethodsOk = 400;

    private Context mContext = AttentionSurgeryMethordsActivity.this;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private AttentionTherapeuticAdapter mAdapter;
    private List<SurgeryMethods>mChooseMethods = new ArrayList<>();//选中其他手术项
    private List<AttentionOtherTherapeutic> mSurgeryMethodsList
            = new ArrayList<AttentionOtherTherapeutic>();//其他设置 手术项
    private int patientDiseaseId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention_surgery_methords;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("请选择其他手术项", "");
    }

    @Override
    protected void initView() {

        Intent intent = getIntent();
        if (intent.getSerializableExtra(SurgeryMethodsList)!=null){
            mChooseMethods = (List<SurgeryMethods>) intent.getSerializableExtra(SurgeryMethodsList);
        }

        SharedPreferences sp = getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientDiseaseId =  sp.getInt(SPInfo.UserKey_patientDiseaseId,0);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        getSurgeryMethodsByDiseaseId(patientDiseaseId);
    }

    /**
     * 获取某种癌症的所有手术的手术
     */
    private void getSurgeryMethodsByDiseaseId(final int id) {
        HttpService.getHttpService().getSurgeryMethodsByDiseaseId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SurgeryMethodsByDiseaseId>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getSurgeryMethodsByDiseaseId onCompleted()");
                        mAdapter = new AttentionTherapeuticAdapter(mContext, mSurgeryMethodsList);
                        mAdapter.setItemClickListener(new AdapterClickInterface() {
                            @Override
                            public void onItemClick(View v, int position) {

                            }

                            @Override
                            public void onItemLongClick(View v, int position) {

                            }
                        });
                        recyclerView.setAdapter(mAdapter);
                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mChooseMethods.clear();
                                for (AttentionOtherTherapeutic attentionOtherTherapeutic: mAdapter.getList()){
                                    if (attentionOtherTherapeutic.isCheck()){
                                        SurgeryMethods surgeryMethods = new SurgeryMethods();
                                        surgeryMethods.setSurgeryMethodId((long) attentionOtherTherapeutic.getData().getId());
                                        surgeryMethods.setSurgeryMethodName(attentionOtherTherapeutic.getData().getName());
                                        surgeryMethods.setSurgeryRecordId((long) 0);
                                        mChooseMethods.add(surgeryMethods);
                                    }
                                }

                                Intent intent = new Intent();
                                intent.putExtra(SurgeryMethodsList, (Serializable) mChooseMethods);
                                Log.i("hcbtest","mChooseMethods.size()"+mChooseMethods.size());
                                setResult(SurgeryMethodsOk,intent);
                                finish();

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getSurgeryMethodsByDiseaseId onError()");
                        Log.i("hcb", "getSurgeryMethodsByDiseaseId e is" + e);
                    }

                    @Override
                    public void onNext(SurgeryMethodsByDiseaseId surgeryMethodsByDiseaseId) {
                        Log.i("hcb", "getSurgeryMethodsByDiseaseId onNext()");
                        mSurgeryMethodsList.clear();
                        if (surgeryMethodsByDiseaseId.getData() != null && surgeryMethodsByDiseaseId.getData().size() > 0) {
                            List<SurgeryMethodsByDiseaseId.Data> list = surgeryMethodsByDiseaseId.getData();
                            for (int i = 0; i < surgeryMethodsByDiseaseId.getData().size(); i++) {
                                SurgeryMethodsByDiseaseId.Data data = list.get(i);
                                AttentionOtherTherapeutic attentionOtherTherapeutic =
                                        new AttentionOtherTherapeutic();
                                attentionOtherTherapeutic.setData(data);
                                if (checkIsChoose(data.getId(), mChooseMethods)) {
                                    attentionOtherTherapeutic.setCheck(true);
                                }else {
                                    attentionOtherTherapeutic.setCheck(false);
                                }
                                mSurgeryMethodsList.add(attentionOtherTherapeutic);
                            }
                        }

                    }
                });
    }

    /**
     * 判断是否选中
     *
     * @param id
     * @return
     */
    private boolean checkIsChoose(int id, List<SurgeryMethods> chooseMethodsList) {
        boolean isCheck = false;
        for (SurgeryMethods surgeryMethods : chooseMethodsList) {
            if (id == surgeryMethods.getSurgeryMethodId()){
                isCheck =  true;
                break;
            }
        }
        return isCheck;
    }
}
