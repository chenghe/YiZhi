package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.activity.ActivityMedicieneQuestion;
import com.zhongmeban.adapter.MedicineQuestionAdapter;
import com.zhongmeban.bean.DrugRelatedQA;
import com.zhongmeban.bean.postbody.MedicineQuestionListBody;
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
 * 药品问答
 * Created by Chengbin He on 2016/5/26.
 */
public class FragmentMedicineQuestion extends BaseScrollFragment {

    private ActPharmaceutiaclDetail parentActivity;
    private List<DrugRelatedQA.DrugQASourceItem> httpList = new ArrayList<>();
    private TextView tvNoData;
    private MedicineQuestionListBody body;
    private Subscription mSubscription;
    private MedicineQuestionAdapter mAdapter;
    private String medicineId;
    private ObservableListView listView;
    private TextView tvQuestion;//我要提问
//    private CircularProgressBar progressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActPharmaceutiaclDetail) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_question, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        medicineId = parentActivity.medicineId;
        body = new MedicineQuestionListBody(medicineId);
        body.setPageIndex(1);
        body.setPageSize(100);

//        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
        tvNoData = (TextView) view.findViewById(R.id.tv_nodata);
        tvQuestion = (TextView) view.findViewById(R.id.tv_question);
        tvQuestion.setOnClickListener(onClickListener);

        listView = (ObservableListView) view.findViewById(R.id.scroll);
        mAdapter = new MedicineQuestionAdapter(parentActivity,httpList);
        listView.setAdapter(mAdapter);

//        progressBar.setVisibility(View.VISIBLE);
//        listView.setVisibility(View.GONE);
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_question:
                    Intent intent = new Intent(parentActivity, ActivityMedicieneQuestion.class);
                    intent.putExtra("medicineId",medicineId);
                    startActivityForResult(intent,1);
                    break;
            }

        }
    };
    /**
     * 网络请求获取数据
     */
    private void getHttpData() {
        mSubscription = HttpService.getHttpService().postMedicineQAndAPage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DrugRelatedQA>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()");
                        Log.i("hcb", "e " + e);
//                        progressBar.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(DrugRelatedQA drugRelatedQA) {
                        Log.i("hcb", "http onNext()");
//                        progressBar.setVisibility(View.GONE);
                        httpList = drugRelatedQA.getData().getSourceItems();
                        if (httpList.size()==0){
                            listView.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }else {
                            tvNoData.setVisibility(View.GONE);
                            mAdapter.notifyData(httpList);
                            listView.setVisibility(View.VISIBLE);
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
