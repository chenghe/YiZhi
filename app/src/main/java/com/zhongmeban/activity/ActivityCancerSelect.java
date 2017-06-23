package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.adapter.CancerSelectAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.Tumor;
import de.greenrobot.dao.TumorDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 肿瘤类型选择Activity
 * Created by Chengbin He on 2016/5/24.
 */
public class ActivityCancerSelect extends BaseActivity {
    private RecyclerView recyclerView;
    private CancerSelectAdapter mAdapter;
    private Activity mContext = ActivityCancerSelect.this;
    private TumorDao dao;//认识肿瘤对应数据库表
    private List<Tumor> daoList;//数据库list

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_level);

        initTitle();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


        getDataFromeDB();

    }


    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择肿瘤类型");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    AdapterClickInterface itemClickListener = new AdapterClickInterface() {

        @Override
        public void onItemClick(View v, int position) {
            String cancerName = daoList.get(position).getName();
            String diseaseId = daoList.get(position).getId()+"";
            Intent intent = new Intent();
            intent.putExtra("cancerName",cancerName);
            intent.putExtra("diseaseId",diseaseId);
            mContext.setResult(300,intent);
            mContext.finish();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    /**
     * 数据库请求数据
     */
    private void getDataFromeDB() {

        Observable.create(new Observable.OnSubscribe<Tumor>() {
            @Override
            public void call(Subscriber<? super Tumor> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication)mContext.getApplication()).getDaoSession().getTumorDao();
                List joes = dao.queryBuilder().where(TumorDao.Properties.IsActive.eq(true))
                        .orderAsc(TumorDao.Properties.PinyinName)
                        .list();
                daoList = joes;
                ((MyApplication)mContext.getApplication()).closeDB();
                Log.i("hcb", " daoList.size" + daoList.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Tumor>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", " db onCompleted()");
                        mAdapter = new CancerSelectAdapter(mContext,daoList);
                        mAdapter.setItemClickListener(itemClickListener);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", " db onCompleted()");
                        Log.i("hcb", " e is" +e);
                    }

                    @Override
                    public void onNext(Tumor tumor) {
                        Log.i("hcb", " db onCompleted()");

                    }
                });
    }
}
