package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.github.clans.fab.FloatingActionButton;
import com.orhanobut.logger.Logger;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.Interface.RecyclerViewAnimListener;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityAddNewMarker;
import com.zhongmeban.activity.ActivityMarkerDetail;
import com.zhongmeban.adapter.MarkerListAdapter;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.bean.IndexRecords;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DividerItemDecoration;
import de.greenrobot.dao.attention.MarkerSource;
import de.greenrobot.dao.attention.RecordIndex;
import de.greenrobot.dao.attention.RecordIndexDao;
import de.greenrobot.dao.attention.RecordIndexItem;
import de.greenrobot.dao.attention.RecordIndexItemDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 标志物记录List
 * Created by Chengbin He on 2016/7/4.
 */
public class FragmentMarkerList extends BaseFragmentRefresh {

    public static final int FragmentMarkerListBack = 400;
    private RecyclerView recyclerView;
    private MarkerListAdapter markerListAdapter;
    private FloatingActionButton floatingActionButton;
    private ActivityAttentionList parentActivity;
    private Subscription subscription;
    private List<IndexRecords.Source> httpList = new ArrayList<IndexRecords.Source>();
    private List<MarkerSource> sourceList = new ArrayList<MarkerSource>();//显示List
    private RecordIndexDao recordIndexDao;
    private RecordIndexItemDao recordIndexItemDao;
    private SharedPreferences serverTimeSP;
    private SharedPreferences userSP;
    private String markerServerTime;
    private String token;
    private String patientId;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAttentionList) context;
    }


    public static FragmentMarkerList newInstance() {

        Bundle args = new Bundle();
        FragmentMarkerList fragment = new FragmentMarkerList();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutIdRefresh() {
        return R.layout.fragment_attention_list_common;
    }


    @Override
    protected void initToolbarRefresh() {

    }


    @Override
    protected void initOnCreateView() {

        initEmptyView();
        userSP = parentActivity.getSharedPreferences("userInfo", 0);
        token = userSP.getString("token", "");
        patientId = userSP.getString("patientId", "");

        serverTimeSP = parentActivity.getSharedPreferences("serverTime", Context.MODE_PRIVATE);
        markerServerTime = serverTimeSP.getString("markerServerTime", "0");
        //        getHttpData(markerServerTime, patientId, token);
        getDBDate();
        floatingActionButton = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentActivity));
        recyclerView.addItemDecoration(
            new DividerItemDecoration(parentActivity, DividerItemDecoration.VERTICAL_LIST));
        markerListAdapter = new MarkerListAdapter(parentActivity, sourceList);
        markerListAdapter.setItemClickListener(recyclerItemClick);
        recyclerView.addOnScrollListener(new RecyclerViewAnimListener(floatingActionButton));
        recyclerView.setAdapter(markerListAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parentActivity, ActivityAddNewMarker.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    AdapterClickInterface recyclerItemClick = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(parentActivity, ActivityMarkerDetail.class);
            int indexItemId = (int) (sourceList.get(position)).getIndexItem().getId();
            intent.putExtra("indexItemId", indexItemId);
            intent.putExtra(ActivityMarkerDetail.EXTRA_MARKER_INFO, sourceList.get(position));
            startActivityForResult(intent, 1);
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) {
            showEmptyLayoutState(LOADING_STATE_SUCESS);
            markerServerTime = serverTimeSP.getString("markerServerTime", "0");
            getHttpData(markerServerTime, patientId, token);
        }
    }


    /***
     * 获取网络数据
     */
    private void getHttpData(String serverTime, String patientId, String token) {

        HttpService.getHttpService().getIndexRecords(serverTime, patientId, token)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Subscriber<IndexRecords>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "getHttpData onCompleted");
                    insertDB();
                    getDBDate();
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "getHttpData onError");
                    Log.i("hcb", "e is" + e);
                    getDBDate();
                }


                @Override
                public void onNext(IndexRecords indexRecords) {
                    Log.i("hcb", "getHttpData onNext");
                    httpList = indexRecords.getData().getSource();

                    SharedPreferences.Editor editor = serverTimeSP.edit();
                    String markerServerTime = indexRecords.getData().getServerTime();
                    editor.putString("markerServerTime", markerServerTime);
                    editor.commit();
                }
            });
    }


    /**
     * 插入数据库数据
     */
    private void insertDB() {
        if (httpList.size() > 0) {//判断是否有最新数据需要入库
            recordIndexDao = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getRecordIndexDao();
            recordIndexItemDao = ((MyApplication) (parentActivity.getApplication()))
                .getAttentionDaoSession().getRecordIndexItemDao();
            for (int i = 0; i < httpList.size(); i++) {
                //indexItem 入库
                IndexRecords.IndexItem indexItem = httpList.get(i).getIndexItem();
                int id = indexItem.getId();
                String patientId = indexItem.getPatientId();
                long hospitalId = indexItem.getHospitalId();
                long time = indexItem.getTime();
                String hospitalName = indexItem.getHospitalName();
                int type = indexItem.getType();
                boolean isActive = indexItem.getActive();

                RecordIndexItem recordIndexItem = new RecordIndexItem();
                recordIndexItem.setId(id);
                recordIndexItem.setPatientId(patientId);
                recordIndexItem.setHospitalId(hospitalId);
                recordIndexItem.setTime(time);
                recordIndexItem.setHospitalName(hospitalName);
                recordIndexItem.setType(type);
                recordIndexItem.setIsActive(isActive);

                long count = recordIndexItemDao.queryBuilder()
                    .where(RecordIndexItemDao.Properties.Id.eq(id))
                    .count();//判断是否更新
                if (count > 0) {
                    Log.i("hcbtest", " recordIndexItemDao.update count" + recordIndexItem.getId());
                    recordIndexItemDao.update(recordIndexItem);
                } else {
                    recordIndexItemDao.insert(recordIndexItem);
                }

                //indexs 入库(标志物)
                List<IndexRecords.Indexs> indexsList = httpList.get(i).getIndexs();

                List<RecordIndex> dbRecordIndexList = recordIndexDao.queryBuilder()
                    .where(RecordIndexDao.Properties.IndexItemRecordId.eq(id)).list();
                for (RecordIndex dbRecordIndex : dbRecordIndexList) {
                    recordIndexDao.delete(dbRecordIndex);
                }
                for (int a = 0; a < indexsList.size(); a++) {
                    IndexRecords.Indexs indexs = indexsList.get(a);
                    int indexItemRecordId = indexs.getIndexItemRecordId();
                    int iId = indexs.getId();
                    //                                    String unitName = indexs.getUnitName();
                    String iPatientId = indexs.getPatientId();
                    String indexName = indexs.getIndexName();
                    //                                    String unitId = indexs.getUnitId();
                    int indexId = indexs.getIndexId();
                    int status = indexs.getStatus();
                    long iTime = indexs.getTime();
                    float normalMin = indexs.getNormalMin();
                    float normalMax = indexs.getNormalMax();
                    String unitName = indexs.getUnitName();
                    String value = indexs.getValue();
                    boolean iIsActive = indexs.getActive();

                    RecordIndex recordIndex = new RecordIndex();
                    recordIndex.setId(iId);
                    recordIndex.setIndexId(indexId);
                    recordIndex.setIndexItemRecordId(indexItemRecordId);
                    recordIndex.setPatientId(iPatientId);
                    recordIndex.setIndexName(indexName);
                    recordIndex.setTime(iTime);
                    recordIndex.setStatus(status);
                    recordIndex.setValue(value);
                    recordIndex.setIsActive(iIsActive);
                    recordIndex.setNormalMin(normalMin);
                    recordIndex.setNormalMax(normalMax);
                    recordIndex.setUnitName(unitName);

                    recordIndexDao.insert(recordIndex);

                    //                    if (recordIndexDao.queryBuilder().where(RecordIndexDao.Properties.Id.eq(iId)).count()>0){
                    //                        Log.i("hcbtest"," recordIndexDao.update count"+recordIndex.getId());
                    //                        recordIndexDao.update(recordIndex);
                    //                    }else {
                    //                        recordIndexDao.insert(recordIndex);
                    //                    }

                }
            }
        }

    }


    /**
     * 获取数据库数据
     */
    private void getDBDate() {
        subscription = Observable
            .create(new Observable.OnSubscribe<MarkerSource>() {
                        @Override
                        public void call(Subscriber<? super MarkerSource> subscriber) {
                            Log.i("hcb", "call");
                            recordIndexDao = ((MyApplication) (parentActivity.getApplication()))
                                .getAttentionDaoSession().getRecordIndexDao();
                            recordIndexItemDao = ((MyApplication) (parentActivity.getApplication()))
                                .getAttentionDaoSession().getRecordIndexItemDao();
                            List<RecordIndexItem> indexItemList = recordIndexItemDao.queryBuilder()
                                .where(RecordIndexItemDao.Properties.IsActive.eq(true)
                                    , RecordIndexItemDao.Properties.PatientId.eq(patientId))
                                .orderDesc(RecordIndexItemDao.Properties.Time).list();
                            if (indexItemList.size() > 0) {
                                userSP.edit()
                                    .putLong("recentlyCheck", indexItemList.get(0).getTime())
                                    .commit();
                            }
                            sourceList.clear();
                            for (int i = 0; i < indexItemList.size(); i++) {
                                int id = (int) indexItemList.get(i).getId();
                                List<RecordIndex> indexList = recordIndexDao.queryBuilder()
                                    .where(RecordIndexDao.Properties.IndexItemRecordId.eq(id),
                                        RecordIndexDao.Properties.IsActive.eq(true),
                                        RecordIndexDao.Properties.PatientId.eq(parentActivity.patientId))
                                    .orderDesc(RecordIndexDao.Properties.Time)
                                    .orderDesc(RecordIndexDao.Properties.Status)
                                    .list();
                                List<RecordIndex> statusList = recordIndexDao.queryBuilder()
                                    .where(recordIndexDao.queryBuilder().and(
                                        RecordIndexDao.Properties.IndexItemRecordId.eq(id),
                                        RecordIndexDao.Properties.IsActive.eq(true),
                                        RecordIndexDao.Properties.PatientId.eq(parentActivity.patientId),
                                        RecordIndexDao.Properties.Status.eq(2)))
                                    .list();
                                MarkerSource markerSource = new MarkerSource(indexList, statusList,
                                    indexItemList.get(i));
                                sourceList.add(markerSource);
                            }
                            ((MyApplication) (parentActivity.getApplication())).closeAttentionDB();
                            subscriber.onCompleted();
                        }
                    }
            ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<MarkerSource>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "getDateForDB onCompleted");
                    if (sourceList.size() > 0) {
                        showEmptyLayoutState(LOADING_STATE_SUCESS);
                        markerListAdapter.updateDate(sourceList);
                    } else {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    }

                }


                @Override
                public void onError(Throwable e) {
                    Logger.e("e==" + e.toString());
                    ToastUtil.showText(parentActivity, "数据获取异常");
                    showEmptyLayoutState(LOADING_STATE_EMPTY);
                }


                @Override
                public void onNext(MarkerSource markerSource) {
                    Log.i("hcb", "getDateForDB onNext");
                }
            });
    }
    //
    //    @Override
    //    public void onDestroy() {
    //        super.onDestroy();
    //        if (subscription.isUnsubscribed()) {
    //            subscription.unsubscribe();
    //        }
    //    }
}
