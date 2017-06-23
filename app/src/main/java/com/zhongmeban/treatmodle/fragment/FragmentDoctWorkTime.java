package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.treatmodle.activity.ActDoctDetail;
import com.zhongmeban.adapter.DoctWorkTimeAdapter;
import com.zhongmeban.bean.DoctorWorkTime;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollFragment;
import com.zhongmeban.utils.ScrollView.ObservableListView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 医生出诊Fragment
 * Created by Chengbin He on 2016/5/17.
 */
public class FragmentDoctWorkTime  extends BaseScrollFragment  {

    private ActDoctDetail parentActivity;
    private Subscription mSubscription;
    private ObservableListView listView;
    private DoctWorkTimeAdapter mAdapter;
    private TextView tvNoData;
    private TextView tvDesc;
    private List<DoctorWorkTime.WorkTimeList> httpList = new ArrayList<>();
    private String doctorId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActDoctDetail) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctworktime_listview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ActDoctDetail mActivity = (ActDoctDetail) parentActivity;
        doctorId = parentActivity.doctorId;

        tvNoData = (TextView) view.findViewById(R.id.tv_nodata);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);

        listView = (ObservableListView) view.findViewById(R.id.scroll);
//        mAdapter = new DoctWorkTimeAdapter(parentActivity,httpList,mActivity.hospName);
//        listView.setAdapter(mAdapter);
        listView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getHttpData(doctorId);
    }

    /**
     * 网络请求获取数据
     */
    private void getHttpData(String doctorId) {
        mSubscription = HttpService.getHttpService().getDoctorWorkTime(doctorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DoctorWorkTime>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()");
                        Log.i("hcb", " e is" + e);
                    }

                    @Override
                    public void onNext(DoctorWorkTime doctorWorkTime) {
                        Log.i("hcb", "http onNext()");
                        if (doctorWorkTime.getData().getWorkTimeList().size()==0){
                            listView.setVisibility(View.GONE);
                            tvDesc.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }else{
                            listView.setVisibility(View.VISIBLE);
                            tvDesc.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                            httpList = doctorWorkTime.getData().getWorkTimeList();
                            mAdapter = new DoctWorkTimeAdapter(parentActivity,httpList,parentActivity.hospName,doctorWorkTime);
                            listView.setAdapter(mAdapter);
                        }
                    }
                });
    }


    public void notifyData(String doctorId){
        getHttpData(doctorId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
