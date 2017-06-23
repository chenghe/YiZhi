package com.zhongmeban.treatmodle.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.treatbean.TreatDoctList;
import com.zhongmeban.bean.treatbean.TreatDoctor;
import com.zhongmeban.treatmodle.activity.ActDoctDetail;
import com.zhongmeban.treatmodle.activity.ActHospDetail;
import com.zhongmeban.treatmodle.adapter.HospDoctAdapter;
import com.zhongmeban.bean.Doctor;
import com.zhongmeban.bean.DoctorList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.adapter.TreatHospDoctAdapter;
import com.zhongmeban.utils.ScrollView.BaseScrollListViewFragment;
import com.zhongmeban.utils.ScrollView.ObservableListView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:医院详情 医生页Fragment
 * Created by Chengbin He on 2016/4/6.
 */
public class FragmentHospDoctList extends BaseScrollListViewFragment {

    private Activity parentActivity;
    private ObservableListView listView;
//    private DoctorBody body;
    private List<TreatDoctor> httpList = new ArrayList<TreatDoctor>();
    private TreatHospDoctAdapter mAdapter;
    private TextView tvNoData;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctlistview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       final ActHospDetail mActivity = (ActHospDetail) parentActivity;
//        body = new DoctorBody();
//        body.setPageIndex(1);
//        body.setPageSize(100);
//        body.setHospitalId(mActivity.hospitalId);

        tvNoData = (TextView) view.findViewById(R.id.tv_nodata);

        listView = (ObservableListView) view.findViewById(R.id.scroll);
        mAdapter = new TreatHospDoctAdapter(parentActivity,httpList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parentActivity, ActDoctDetail.class);

                String doctName = httpList.get(position).getDoctorName();
                String dooctLevel = mAdapter.getDoctLevel(position);
                String url = mAdapter.getIcon(position);
                String hospName = mActivity.hospName;
                String doctorId = httpList.get(position).getDoctorId() + "";

                intent.putExtra("doctName", doctName);
                intent.putExtra("dooctLevel", dooctLevel);
                intent.putExtra("url", url);
                intent.putExtra("hospName", hospName);
                intent.putExtra("doctorId", doctorId);

                startActivity(intent);
            }
        });
        listView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
//        getHttpData();
    }

    /**
     * 网络请求获取数据
     */
    private void getHttpData(String hospitalId) {
        HttpService.getHttpService().getDoctorList("", "", hospitalId,
                0, 0,1, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TreatDoctList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()");
                        Log.i("hcb", "e " + e);
                        ToastUtil.showText(parentActivity,"数据错误");
                    }

                    @Override
                    public void onNext(TreatDoctList treatDoctList) {
                        Log.i("hcb", "http onNext()");
                        if (treatDoctList.getData().size()==0){
                            listView.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }else{
                            listView.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                            httpList = treatDoctList.getData();
                            mAdapter.notifyData(httpList);
                        }

                    }
                });
    }

    public void notifyData(String hospitalId ){
        getHttpData(hospitalId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        parentActivity = null;
    }
}
