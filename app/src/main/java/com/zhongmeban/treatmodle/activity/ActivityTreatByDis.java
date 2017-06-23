package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.activity.ActivityTherapeuticDetail;
import com.zhongmeban.adapter.TreatByDisAdapetr;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.bean.TreatMethodLists;
import com.zhongmeban.net.HttpService;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 获取某种癌症的某种治疗方法分类的所有治疗方法
 * Created by Chengbin He on 2016/5/5.
 */
public class ActivityTreatByDis extends BaseActivityRefresh implements View.OnClickListener {

    public static String EXTRA_IS_BAXIANGZHILIAO = "extra_is_baxiangzhiliao";

    private ListView listView;
    private Context mContext = ActivityTreatByDis.this;
    private String titleName;
    private TreatByDisAdapetr mAdapter;
    private List<TreatMethodLists.Data> httpList = new ArrayList<TreatMethodLists.Data>();//癌症list

    private String diseaseId;
    private String therapeuticCategoryId;
    private String mSearchName = "";
    private EditText searchBox;//搜索框
    private ImageView clearBtn;//清空搜索

    private boolean isBaXiangZhiLiao;


    @Override protected int getLayoutIdRefresh() {
        return R.layout.activity_treatbydis;
    }


    @Override protected void initToolbarRefresh() {

        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        diseaseId = intent.getStringExtra("diseaseId");
        therapeuticCategoryId = intent.getStringExtra("therapeuticCategoryId");
        isBaXiangZhiLiao = intent.getBooleanExtra(EXTRA_IS_BAXIANGZHILIAO, false);

        initToolbarCustom(titleName, "");
    }


    @Override protected void initViewRefresh() {

        initViewLayout();
        initEmptyView();
        showEmptyLayoutState(LOADING_STATE_SUCESS);
        getHttpData(diseaseId, therapeuticCategoryId, mSearchName);
    }


    private void initViewLayout() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setSelector(R.drawable.selector_enter);

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(textWatcher);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isBaXiangZhiLiao) {
                    Intent intent = new Intent(mContext, ActPharmaceutiaclDetail.class);
                    String therapeuticId = httpList.get(position).getTherapeuticId();
                    intent.putExtra(ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID, therapeuticId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ActivityTherapeuticDetail.class);
                    String therapeuticId = httpList.get(position).getTherapeuticId();
                    intent.putExtra("therapeuticId", therapeuticId);
                    intent.putExtra("label", titleName);
                    startActivity(intent);
                }

            }
        });
    }


    /**
     * 网络请求获取数据
     */
    private void getHttpData(String diseaseId, String therapeuticCategoryId, String searchName) {
        HttpService.getHttpService()
            .getTherapeuticByDisId(diseaseId, therapeuticCategoryId, searchName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<TreatMethodLists>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "http onCompleted()");
                    mAdapter = new TreatByDisAdapetr(mContext, httpList,isBaXiangZhiLiao );
                    listView.setAdapter(mAdapter);
                    mRefreshLayout.post(new Runnable() {
                        @Override public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });

                }


                @Override
                public void onError(Throwable e) {
                    mRefreshLayout.post(new Runnable() {
                        @Override public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                    showEmptyLayoutState(LOADING_STATE_EMPTY);
                }


                @Override
                public void onNext(TreatMethodLists treatMethodLists) {

                    if (treatMethodLists.getErrorCode() != 0) return;
                    Log.i("hcb", "http onNext");
                    if (treatMethodLists.getData() == null) {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    } else {
                        httpList = treatMethodLists.getData();
                        if (httpList.size() == 0) {
                            showEmptyLayoutState(LOADING_STATE_EMPTY);
                        } else {
                            showEmptyLayoutState(LOADING_STATE_SUCESS);
                        }
                    }
                }
            });

    }


    /**
     * 搜索框，搜素内容监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            mSearchName = s.toString();
            if (TextUtils.isEmpty(mSearchName)) {
                clearBtn.setVisibility(View.GONE);
            } else {
                clearBtn.setVisibility(View.VISIBLE);
            }
            mRefreshLayout.setRefreshing(true);

            getHttpData(diseaseId, therapeuticCategoryId, mSearchName);
        }
    };


    @Override public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_search_clear://清除搜索内容
                searchBox.setText("");
                mSearchName = "";
                clearBtn.setVisibility(View.GONE);
                mRefreshLayout.setRefreshing(true);
                getHttpData(diseaseId, therapeuticCategoryId, mSearchName);
                break;
        }
    }


    @Override public void onRefresh() {
        super.onRefresh();
        mRefreshLayout.postDelayed(new Runnable() {
            @Override public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
