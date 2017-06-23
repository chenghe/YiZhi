package com.zhongmeban.attentionmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionMedicineDetail;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionMedicineList;
import com.zhongmeban.attentionmodle.adapter.AttentionTakingMedicineAdapter;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.MedicineRecord;
import de.greenrobot.dao.attention.MedicineRecordDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 关注部分历史用药
 * Created by Chengbin He on 2016/8/8.
 */
public class FragmentAttentionTakedMedicine extends BaseFragmentRefresh {

    private ActivityAttentionMedicineList parentActivity;
    private RecyclerView recyclerView;
    private MedicineRecordDao medicineRecordDao;
    private List<MedicineRecord> dataList = new ArrayList<MedicineRecord>();
    private AttentionTakingMedicineAdapter mAdapter;

    public static FragmentAttentionTakedMedicine newInstance() {

        Bundle args = new Bundle();

        FragmentAttentionTakedMedicine fragment = new FragmentAttentionTakedMedicine();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionMedicineList) context;
    }


    @Override
    protected int getLayoutIdRefresh() {
        return R.layout.fragment_recyclerview_only;
    }

    @Override
    protected void initToolbarRefresh() {

    }

    @Override
    protected void initOnCreateView() {
        parentActivity = (ActivityAttentionMedicineList)getActivity();
        initEmptyView( );
        showEmptyLayoutState(LOADING_STATE_SUCESS);
        parentActivity = (ActivityAttentionMedicineList)getActivity();
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));

        getDBData();
    }


    /**
     * 从数据库中获取正在服用数据
     */
    public void getDBData() {
        if (parentActivity == null){
            return;
        }
        Observable.create(new Observable.OnSubscribe<MedicineRecord>() {

            @Override
            public void call(Subscriber<? super MedicineRecord> subscriber) {
                Log.i("hcb", "call");
                medicineRecordDao = ((MyApplication) parentActivity.getApplication())
                        .getAttentionDaoSession().getMedicineRecordDao();
                dataList = medicineRecordDao.queryBuilder()
                        .where(medicineRecordDao.queryBuilder().and(MedicineRecordDao.Properties.Status.eq(2),
                                MedicineRecordDao.Properties.IsActive.eq(true),
                                MedicineRecordDao.Properties.PatientId.eq(parentActivity.patientId))
                        ).orderDesc(MedicineRecordDao.Properties.StartTime)
                        .orderDesc(MedicineRecordDao.Properties.EndTime).list();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MedicineRecord>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "FragmentAttentionTakedMedicine onCompleted");
                        if (dataList.size() > 0) {
                            //判断是否有数据
                            showEmptyLayoutState(LOADING_STATE_SUCESS);
                            mAdapter = new AttentionTakingMedicineAdapter(parentActivity, dataList);
                            mAdapter.setItemClickListener(new AdapterClickInterface() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    Intent intent = new Intent(parentActivity,
                                            ActivityAttentionMedicineDetail.class);
                                    long id = dataList.get(position).getId();
                                    intent.putExtra("chooseId", id);
                                    intent.putExtra("fragmentPosition", 1);
                                    startActivityForResult(intent, 1);
                                }

                                @Override
                                public void onItemLongClick(View v, int position) {

                                }
                            });
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            showEmptyLayoutState(LOADING_STATE_EMPTY);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "FragmentAttentionTakedMedicine onError");
                        Log.i("hcb", "e" + e);
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                        ToastUtil.showText(parentActivity,"数据获取异常");
                    }

                    @Override
                    public void onNext(MedicineRecord medicineRecord) {
                        Log.i("hcb", "FragmentAttentionTakedMedicine onNext");
                    }
                });
    }
}
