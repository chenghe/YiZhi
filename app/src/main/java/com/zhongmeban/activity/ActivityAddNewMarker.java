package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexs;
import com.zhongmeban.bean.postbody.CreateIndexRecordBody;
import com.zhongmeban.fragment.FragmentAddNewMarker;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionIndicators;

import com.zhongmeban.utils.disk.CacheUtil;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.MarkerSource;
import de.greenrobot.dao.attention.RecordIndex;
import de.greenrobot.dao.attention.RecordIndexDao;
import de.greenrobot.dao.attention.RecordIndexItem;
import de.greenrobot.dao.attention.RecordIndexItemDao;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 添加新的标志物
 * Created by Chengbin He on 2016/6/29.
 */
public class ActivityAddNewMarker extends BaseActivity {

    private Activity mContext = ActivityAddNewMarker.this;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public FragmentAddNewMarker fragmentAddNewMarker;//添加标志物Fragment
    public FragmentAttentionIndicators fragmentAttentionIndicators;//标志物Fragment
    private Subscription dbSubscription;
    //    private RecordIndexDao recordIndexDao;
    //    private RecordIndexItemDao recordIndexItemDao;
    private RecordIndexItem dbIndexItem;

    public List<RecordIndex> dbIndexList;
    public List<AttentionMarkerIndexs> indexsList = new ArrayList<AttentionMarkerIndexs>();//上传index
    public String token;
    public String patientId;
    public int purposeType;//检查目的type 1.诊断 2.复查 3.疗效评估 4.身体状况检查
    public long chooseDate;//检查日期
    public int indexItemId;
    public String hospName;
    public long hospId;
    public boolean ISEDIT = false;//判断是新增标志物还是编辑标志物
    public CreateIndexRecordBody createIndexRecordBody = new CreateIndexRecordBody();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewmarker);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        token = sp.getString("token", "");
        patientId = sp.getString("patientId", "");

        Intent intent = getIntent();
        ISEDIT = intent.getBooleanExtra("ISEDIT", false);
        indexItemId = intent.getIntExtra("indexItemId", 0);

        Log.i("hcb", "indexItemId" + indexItemId);
        Log.i("hcb", "ISEDIT" + ISEDIT);
        if (ISEDIT) {
            getDBDate(indexItemId);
        } else {

            //查找上次保存的指标的缓存
            List<AttentionMarkerIndexs> indexsCache = CacheUtil.getInstance()
                .getCacheHelper()
                .getAsSerializable(FragmentAddNewMarker.KEY_CACHE_INDEXS + patientId);
            if (indexsCache != null) {
                indexsList.addAll(indexsCache);
            }

            fragmentAddNewMarker = new FragmentAddNewMarker();
            fragmentAttentionIndicators = new FragmentAttentionIndicators();

            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            if (fragmentAddNewMarker.isAdded()) {
                fragmentTransaction.show(fragmentAddNewMarker);
            } else {
                fragmentTransaction.add(R.id.fl_content, fragmentAddNewMarker);
            }
            fragmentTransaction.commit();
        }

    }


    public void startAttentionIndicatorsFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (fragmentAttentionIndicators.isAdded()) {
            fragmentTransaction.show(fragmentAttentionIndicators);
        } else {
            fragmentTransaction.add(R.id.fl_content, fragmentAttentionIndicators);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    protected void initTitle() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        fragmentAttentionIndicators.closeDB();
        //        if (dbSubscription.isUnsubscribed()){
        //            dbSubscription.unsubscribe();
        //        }
    }


    /***
     * 获取数据库信息
     */
    private void getDBDate(final int indexItemId) {
        dbSubscription = Observable.create(new Observable.OnSubscribe<MarkerSource>() {
            @Override
            public void call(Subscriber<? super MarkerSource> subscriber) {
                Log.i("hcb", "call");
                RecordIndexDao recordIndexDao = ((MyApplication) getApplication())
                    .getAttentionDaoSession().getRecordIndexDao();
                RecordIndexItemDao recordIndexItemDao = ((MyApplication) getApplication())
                    .getAttentionDaoSession().getRecordIndexItemDao();
                List<RecordIndexItem> indexItemList = recordIndexItemDao
                    .queryBuilder().where(RecordIndexItemDao.Properties.Id.eq(indexItemId)).list();
                Log.i("hcb", "indexItemList.size " + indexItemList.size());
                dbIndexItem = indexItemList.get(0);

                dbIndexList = recordIndexDao
                    .queryBuilder()
                    .where(RecordIndexDao.Properties.IndexItemRecordId.eq(indexItemId))
                    .list();
                //                MarkerSource markerSource = new MarkerSource(indexList, indexItem);
                ((MyApplication) getApplication()).closeAttentionDB();

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<MarkerSource>() {

                @Override
                public void onCompleted() {
                    Log.i("hcb", "ActivityAddNewMarker getDateForDB onCompleted");
                    purposeType = (int) dbIndexItem.getType();
                    chooseDate = dbIndexItem.getTime();
                    hospId = dbIndexItem.getHospitalId();
                    hospName = dbIndexItem.getHospitalName();
                    indexsList.clear();
                    for (int i = 0; i < dbIndexList.size(); i++) {
                        RecordIndex recordIndex = dbIndexList.get(i);
                        int id = (int) recordIndex.getId();
                        int indexId = (int) recordIndex.getIndexId();
                        String value = recordIndex.getValue();
                        long time = recordIndex.getTime();
                        String unit = recordIndex.getUnitName();
                        String name = recordIndex.getIndexName();
                        AttentionMarkerIndexs attentionMarkerIndexs = new AttentionMarkerIndexs();
                        attentionMarkerIndexs.setId(id);
                        attentionMarkerIndexs.setIndexId(indexId);
                        attentionMarkerIndexs.setValue(value);
                        attentionMarkerIndexs.setTime(time);
                        attentionMarkerIndexs.setName(name);
                        attentionMarkerIndexs.setUnit(unit);
                        indexsList.add(attentionMarkerIndexs);
                    }

                    fragmentAddNewMarker = new FragmentAddNewMarker();
                    fragmentAttentionIndicators = new FragmentAttentionIndicators();

                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fl_content, fragmentAddNewMarker);
                    fragmentTransaction.commit();
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "ActivityAddNewMarker getDateForDB onError");
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(MarkerSource markerSource) {
                    Log.i("hcb", "ActivityAddNewMarker getDateForDB onNext");
                }
            });
    }


    /**
     * 网络请求前更新Body
     */
    public void initBody() {
        createIndexRecordBody.getIndexItem().setId(indexItemId);
        createIndexRecordBody.getIndexItem().setPatientId(patientId);
        createIndexRecordBody.getIndexItem().setHospitalId(hospId);
        createIndexRecordBody.getIndexItem().setTime(chooseDate);
        createIndexRecordBody.getIndexItem().setType(purposeType);
        if (indexsList.size() > 0) {
            for (AttentionMarkerIndexs attentionMarkerIndexs : indexsList) {
                attentionMarkerIndexs.setTime(chooseDate);
            }
        }
        createIndexRecordBody.setIndexs(indexsList);
    }
}
