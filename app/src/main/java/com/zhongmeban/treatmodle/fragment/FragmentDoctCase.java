package com.zhongmeban.treatmodle.fragment;

import android.app.Activity;
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
import com.zhongmeban.adapter.DoctCaseAdapter;
import com.zhongmeban.bean.DoctDetailMedicase;
import com.zhongmeban.bean.postbody.DoctorCaseBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollListViewFragment;
import com.zhongmeban.utils.ScrollView.ObservableListView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 医生详情，案例部分Fragment
 * Created by Chengbin He on 2016/5/11.
 */
public class FragmentDoctCase extends BaseScrollListViewFragment {
    private Activity parentActivity;
    private ObservableListView listView;
    private DoctCaseAdapter mAdapter;
    private List<DoctDetailMedicase.Medicalcase> httpList = new ArrayList<>();
    private DoctorCaseBody body;
    private TextView tvNoData;
    private Subscription mSubscription;
    private String mDoctorId;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctcase_listview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActDoctDetail mActivity = (ActDoctDetail) parentActivity;
        body = new DoctorCaseBody();
        body.setPageIndex(1);
        body.setPageSize(10);
        body.setDoctorId(mActivity.doctorId);
        mDoctorId = mActivity.doctorId;

        tvNoData = (TextView) view.findViewById(R.id.tv_nodata);

        listView = (ObservableListView) view.findViewById(R.id.scroll);
        mAdapter = new DoctCaseAdapter(parentActivity,httpList);
        listView.setAdapter(mAdapter);
        listView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getHttpData();
    }

    /**
     * 网络请求获取数据
     */
    private void getHttpData() {
        mSubscription = HttpService.getHttpService().getDoctorCase(mDoctorId,100,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DoctDetailMedicase>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()");
                        Log.i("hcb", "e " + e);
                    }

                    @Override
                    public void onNext(DoctDetailMedicase doctDetailMedicase) {
                        Log.i("hcb", "http onNext()");
                        if (doctDetailMedicase.getData().getSourceItems().size()==0){
                            listView.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }else{
                            listView.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                            httpList = doctDetailMedicase.getData().getSourceItems();
                            mAdapter.notifyData(httpList);
                        }

                    }
                });
    }

    public void notifyData(){
        getHttpData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("hcb","FragmentMedicineDetail onDestroy");
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
