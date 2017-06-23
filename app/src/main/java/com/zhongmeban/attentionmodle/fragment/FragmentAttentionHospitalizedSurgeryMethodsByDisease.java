package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionHospitalizedOperation;
import com.zhongmeban.attentionmodle.adapter.AttentionTherapeuticAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.AttentionOtherTherapeutic;
import com.zhongmeban.bean.SurgeryMethodsByDiseaseId;
import com.zhongmeban.net.HttpService;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  住院关联手术部分其他设置Fragment
 * Created by Chengbin He on 2016/8/11.
 */
public class FragmentAttentionHospitalizedSurgeryMethodsByDisease extends BaseFragment {

    private ActivityAttentionHospitalizedOperation parentActivity;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private AttentionTherapeuticAdapter mAdapter;
//    public List<Integer> mChooseMethodIds = new ArrayList<Integer>();//选中手术项
    public List<SurgeryMethods>mChooseMethods = new ArrayList<>();//选中其他手术项
    private List<AttentionOtherTherapeutic> mSurgeryMethodsList
            = new ArrayList<AttentionOtherTherapeutic>();//其他设置 手术项


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionHospitalizedOperation) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recyclerview_floatingbar_notitle, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChooseMethods.clear();
        mChooseMethods.addAll(parentActivity.chooseMethods);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        getSurgeryMethodsByDiseaseId(parentActivity.patientDiseaseId);
    }

    /**
     * 获取某种癌症的所有手术的手术
     */
    private void getSurgeryMethodsByDiseaseId(int id) {
        HttpService.getHttpService().getSurgeryMethodsByDiseaseId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SurgeryMethodsByDiseaseId>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getSurgeryMethodsByDiseaseId onCompleted()");
                        mAdapter = new AttentionTherapeuticAdapter(parentActivity,
                                mSurgeryMethodsList);
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
//                mSurgeryMethodsList.clear();
//                mSurgeryMethodsList.addAll(mAdapter.getList());
//
//                parentActivity.surgeryMethodsList.clear();
//                parentActivity.surgeryMethodsList.addAll(mSurgeryMethodsList);
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
                                parentActivity.chooseMethods.clear();
                                parentActivity.chooseMethods.addAll(mChooseMethods);
                                parentActivity.notifyFragment(2);
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
