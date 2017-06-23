package com.zhongmeban.attentionmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.adapter.AttentionNoticesListAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.AttentionNoticesBean;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.UpdateNoticeStatusBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.AttentionNotices;
import de.greenrobot.dao.attention.AttentionNoticesDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *关注提醒详情页
 * Created by Chengbin He on 2016/9/8.
 */
public class ActivityAttentionNoticesList extends BaseActivity{

    private RecyclerView recyclerView;
    private String patientId;
    private String token;
    private List<AttentionNotices> attentionNoticeDBList = new ArrayList<>();
    private String attentionNoticesServerTime;//异常提示servertime
    private SharedPreferences serverTimeSP;
    private AttentionNoticesListAdapter myAdapter;
    private LinearLayout llEmptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_notices_list);

        serverTimeSP = getSharedPreferences("serverTime", Context.MODE_PRIVATE);
        attentionNoticesServerTime = serverTimeSP.getString("attentionNoticesServerTime", "0");

        Intent intent = getIntent();
        patientId = intent.getStringExtra("patientId");

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        token =  sp.getString("token","");

        llEmptyView = (LinearLayout) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new AttentionNoticesListAdapter(ActivityAttentionNoticesList.this,attentionNoticeDBList);
        myAdapter.setItemClickListener(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                updateHttpPatientStatus(attentionNoticeDBList.get(position).getId(),token);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        recyclerView.setAdapter(myAdapter);

        getDBAttentionNotices(patientId);

        initTitle();

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("提醒列表");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 获取数据库 患者异常信息
     * @param patientId
     */
    private void getDBAttentionNotices(final String patientId){
        Observable.create(new Observable.OnSubscribe<AttentionNotices>() {
            @Override
            public void call(Subscriber<? super AttentionNotices> subscriber) {
                AttentionNoticesDao attentionNoticesDao = ((MyApplication) getApplication())
                        .getAttentionDaoSession().getAttentionNoticesDao();
                attentionNoticeDBList.clear();

                attentionNoticeDBList =  attentionNoticesDao.queryBuilder()
                        .where(AttentionNoticesDao.Properties.PatientId.eq(patientId),
                                        AttentionNoticesDao.Properties.IsActive.eq(true))
                        .orderDesc(AttentionNoticesDao.Properties.Time)
                        .list();

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AttentionNotices>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getDBAttentionNotices onCompleted");
                        if (attentionNoticeDBList.size()>0){
                            llEmptyView.setVisibility(View.GONE);
                                myAdapter.updateData(attentionNoticeDBList);
                        }else {
                            llEmptyView.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getDBAttentionNotices onError");
                        Log.i("hcb", "e"+e);
                        llEmptyView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(AttentionNotices attentionNotices) {
                        Log.i("hcb", "getDBAttentionNotices onNext");
                    }
                });
    }


    private void updateHttpPatientStatus(long id , final String token){
        UpdateNoticeStatusBody body = new UpdateNoticeStatusBody();
        body.setNoticeId(id);
        body.setStatus(2);
        HttpService.getHttpService().postUpdateNoticeStatus(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","updateHttpPatientStatus onCompleted()");
                        getHttpAttentionNotices(attentionNoticesServerTime,patientId,token);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","updateHttpPatientStatus onError()");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","updateHttpPatientStatus onNext()");
                    }
                });
    }

    /**
     * 获取异常消息
     */
    private void getHttpAttentionNotices(String serverTime, final String patientId, String token) {
        HttpService.getHttpService().getAttentionNotices(serverTime, patientId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<AttentionNoticesBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getHttpAttentionNotices onCompleted");
                        getDBAttentionNotices(patientId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getHttpAttentionNotices onError");
                        Log.i("hcb", "getHttpAttentionNotices e" + e);
                        llEmptyView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(AttentionNoticesBean attentionNoticesBean) {
                        Log.i("hcb", "getHttpAttentionNotices onNext");
                        if (attentionNoticesBean.getData().getSource().size()>0){
                            insertAttentionNoticeDBData(attentionNoticesBean.getData().getSource());
                            serverTimeSP.edit()
                                    .putString("attentionNoticesServerTime",
                                            attentionNoticesBean.getData().getServerTime()).commit();
                        }
                    }
                });
    }

    /**
     * 异常信息入库
     *
     * @param sourceList
     */
    private void insertAttentionNoticeDBData(List<AttentionNoticesBean.Source> sourceList) {
        AttentionNoticesDao attentionNoticesDao = ((MyApplication)getApplication())
                .getAttentionDaoSession().getAttentionNoticesDao();
        for (int i = 0; i < sourceList.size(); i++) {
            String patientId = sourceList.get(i).getPatientId();
            String content = sourceList.get(i).getContent();
            long time = sourceList.get(i).getTime();
            long createTime = sourceList.get(i).getCreateTime();
            long id = sourceList.get(i).getId();
            int status = sourceList.get(i).getStatus();
            boolean isActive = sourceList.get(i).isActive();

            AttentionNotices attentionNotices = new AttentionNotices();
            attentionNotices.setPatientId(patientId);
            attentionNotices.setContent(content);
            attentionNotices.setTime(time);
            attentionNotices.setId(id);
            attentionNotices.setStatus(status);
            attentionNotices.setIsActive(isActive);
            attentionNotices.setCreateTime(createTime);

            long count = attentionNoticesDao.queryBuilder()
                    .where(AttentionNoticesDao.Properties.Id.eq(id))
                    .count();
            if (count > 0) {
                attentionNoticesDao.update(attentionNotices);
            } else {
                attentionNoticesDao.insert(attentionNotices);
            }
        }
    }

}
