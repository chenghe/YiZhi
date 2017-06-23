package com.zhongmeban.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.adapter.ListViewSingleTextAdapter;
import com.zhongmeban.adapter.TreatmentCancerAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.TreatCategoryLists;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.activity.ActivityTreatByDis;
import com.zhongmeban.utils.LayoutActivityTitle;
import de.greenrobot.dao.Tumor;
import de.greenrobot.dao.TumorDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:治疗方法Activity
 * Created by Chengbin He on 2016/3/30.
 */
public class ActSeachTreatmentMethod extends BaseActivity {

    private Context mContext = ActSeachTreatmentMethod.this;
    private ListView leftListView;
    private ListView rightListView;
    private int diseaseId = 1;//默认肺癌
    private TreatmentCancerAdapter leftAdapter;
    private ListViewSingleTextAdapter rightAdapter;
    private List<Tumor> diseasesList = new ArrayList<Tumor>();//癌症list
    private List<TreatCategoryLists.TreatCategorySource> therapeuticList = new ArrayList<TreatCategoryLists.TreatCategorySource>();//癌症list
    private TumorDao dao;//认识肿瘤对应数据库表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_treatment);

        initTitle();
        initView();
//        initData();

        getHttpDiseasesData();
        getHttpTherapeuticData(diseaseId);
    }

    /**
     * 网络请求癌症数据
     */
    private void getHttpDiseasesData() {


        Observable.create(new Observable.OnSubscribe<Tumor>() {
            @Override
            public void call(Subscriber<? super Tumor> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication)getApplication()).getDaoSession().getTumorDao();
                List joes = dao.queryBuilder()
                        .orderAsc(TumorDao.Properties.PinyinName)
                        .list();
                diseasesList = joes;
                ((MyApplication)getApplication()).closeDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Tumor>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", " db onCompleted()");
                        leftAdapter.notifyData(diseasesList);

                        //左边的癌症列表获取到后需要 展示默认的癌症所对应的治疗方法
                        if (diseasesList.size()>0)
                        getHttpTherapeuticData((int) diseasesList.get(0).getId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", " db onCompleted()");
                    }

                    @Override
                    public void onNext(Tumor tumor) {
//            HttpService.getHttpService().getTumor(0)
        Log.i("hcb", " db onCompleted()");

    }
});



    //                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<TrumerLists>() {
//                        @Override
//                        public void onCompleted() {
//                            Log.i("hcb", "http onCompleted()");
//                            leftAdapter.notifyData(diseasesList);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.i("hcb", "http onError");
//                            Log.i("hcb", "e is " + e);
//                        }
//
//                        @Override
//                        public void onNext(TrumerLists trumerLists) {
//                            Log.i("hcb", "http onNext");
//                            if (trumerLists.getData().getSource().size() > 0) {
//                                diseasesList = trumerLists.getData().getSource();
//                            }
//
//                        }
//                    });
    }

    /**
     * 网络请求癌症相关的治疗方法分类列表
     */
    private void getHttpTherapeuticData(int diseaseId) {
        HttpService.getHttpService().getTherapeuticCategorysByDiseaseId(diseaseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TreatCategoryLists>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                        rightAdapter.notifyData(therapeuticList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError");
                        Log.i("hcb", "e is " + e);
                    }

                    @Override
                    public void onNext(TreatCategoryLists treatCategoryLists) {
                        Log.i("hcb", "http onNext");
                        if (treatCategoryLists.getData()!= null ) {
                            therapeuticList = treatCategoryLists.getData();
                        }

                    }
                });
    }


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("治疗方法");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        leftListView = (ListView) findViewById(R.id.left_listview);
        rightListView = (ListView) findViewById(R.id.right_listview);
        leftAdapter = new TreatmentCancerAdapter(mContext,diseasesList);
        rightAdapter = new ListViewSingleTextAdapter(mContext,1,therapeuticList);

        rightListView.setAdapter(rightAdapter);
        rightListView.setSelector(R.drawable.selector_enter);
        leftAdapter.setAdapterClickListener(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                diseaseId = (int) diseasesList.get(position).getId();
                getHttpTherapeuticData(diseaseId);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        leftListView.setAdapter(leftAdapter);
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,ActivityTreatByDis.class);
                intent.putExtra("titleName",therapeuticList.get(position).getName());
                intent.putExtra("diseaseId",diseaseId+"");
                intent.putExtra("therapeuticCategoryId",therapeuticList.get(position).getId());
                startActivity(intent);
            }
        });
    }
}
