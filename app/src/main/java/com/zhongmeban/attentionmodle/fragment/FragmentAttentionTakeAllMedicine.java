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
import com.zhongmeban.bean.postbody.UpdateEndOrDeleteMedicineBody;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import de.greenrobot.dao.attention.MedicineRecord;
import de.greenrobot.dao.attention.MedicineRecordDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 关注部分全部用药
 * Created by Chengbin He on 2016/8/8.
 */
public class FragmentAttentionTakeAllMedicine extends BaseFragmentRefresh {

    private ActivityAttentionMedicineList parentActivity;
    private RecyclerView recyclerView;
    private MedicineRecordDao medicineRecordDao;
    private List<MedicineRecord> dataList = new ArrayList<MedicineRecord>();
    private AttentionTakingMedicineAdapter mAdapter;
    private long endTime;

    public static FragmentAttentionTakeAllMedicine newInstance() {

        Bundle args = new Bundle();

        FragmentAttentionTakeAllMedicine fragment = new FragmentAttentionTakeAllMedicine();
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
        initEmptyView();
        showEmptyLayoutState(LOADING_STATE_SUCESS);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));

        getDBData();
    }



    /**
     * 从数据库中获取正在服用数据
     */
    public void getDBData(){
        if (parentActivity == null){
            return;
        }
        Observable.create(new Observable.OnSubscribe<MedicineRecord>(){

            @Override
            public void call(Subscriber<? super MedicineRecord> subscriber) {
                Log.i("hcb", "call");
                medicineRecordDao =  ((MyApplication)parentActivity.getApplication())
                        .getAttentionDaoSession().getMedicineRecordDao();
                dataList =  medicineRecordDao.queryBuilder()
                        .where(MedicineRecordDao.Properties.IsActive.eq(true),
                                MedicineRecordDao.Properties.PatientId.eq(parentActivity.patientId))
                        .orderAsc(MedicineRecordDao.Properties.Status)
                        .orderDesc(MedicineRecordDao.Properties.StartTime).list();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MedicineRecord>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","FragmentAttentionTakeAllMedicine onCompleted");
                        if (dataList.size()>0){
                            //判断是否有数据
                            showEmptyLayoutState(LOADING_STATE_SUCESS);
                            mAdapter = new AttentionTakingMedicineAdapter(parentActivity,dataList);
                            mAdapter.setItemClickListener(new AdapterClickInterface() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    Intent intent = new Intent(parentActivity,
                                            ActivityAttentionMedicineDetail.class);
                                    long id = dataList.get(position).getId();
                                    intent.putExtra("chooseId",id);
                                    intent.putExtra("fragmentPosition",2);
                                    startActivityForResult(intent,1);
                                }

                                @Override
                                public void onItemLongClick(View v, int position) {

                                }
                            });
                            mAdapter.setOnStopMedicineClickListenter(new AttentionTakingMedicineAdapter.OnStopMedicineClickListenter() {
                                @Override
                                public void onButtonClick(View v, int position) {
                                    ShowDatePicker(position,dataList.get(position).getStartTime());
                                }
                            });
                            recyclerView.setAdapter(mAdapter);
                        }else {
                            showEmptyLayoutState(LOADING_STATE_EMPTY);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","FragmentAttentionTakeAllMedicine onError");
                        Log.i("hcb","e"+e);
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                        ToastUtil.showText(parentActivity,"数据获取异常");
                    }

                    @Override
                    public void onNext(MedicineRecord medicineRecord) {
                        Log.i("hcb","FragmentAttentionTakeAllMedicine onNext");
                    }
                });
    }

    /***
     * 停止用药网络接口
     */
    private void stopMedicine(UpdateEndOrDeleteMedicineBody body, String token) {
        //HttpService.getHttpService().updateEndMedicine(body)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Subscriber<UpdateEndOrDeleteMedicine>() {
        //            @Override
        //            public void onCompleted() {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail stopMedicine onCompleted");
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail stopMedicine onError");
        //                Log.i("hcb", "e" + e);
        //                Toast.makeText(parentActivity, "网络异常，更新失败", Toast.LENGTH_SHORT).show();
        //            }
        //
        //            @Override
        //            public void onNext(UpdateEndOrDeleteMedicine updateEndOrDeleteMedicine) {
        //                Log.i("hcb", "ActivityAttentionMedicineDetail stopMedicine onNext");
        //                if (updateEndOrDeleteMedicine.isResult()) {
        //                    parentActivity.updateHttpData();
        //                }
        //            }
        //
        //        });
    }

    /**
     * 显示DataPicker
     */
    private void ShowDatePicker(final int position , final long startTime) {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(parentActivity, new OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                if (startTime<parentActivity.changeDateToLong(dateDesc)){
                    endTime = parentActivity.changeDateToLong(dateDesc);
                    UpdateEndOrDeleteMedicineBody body = new UpdateEndOrDeleteMedicineBody();
                    body.setEndTime(endTime);
                    body.setRecordId((int) dataList.get(position).getId());
                    stopMedicine(body,parentActivity.token);
                }else {
                    ToastUtil.showText(parentActivity,"开始时间必须小于结束时间请重新输入");
                }

            }
        }).minYear(1990) //min year in loop
                .maxYear(2050) // max year in loop
                .dateChose(parentActivity.getTodayData()) // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(parentActivity);
    }
}
