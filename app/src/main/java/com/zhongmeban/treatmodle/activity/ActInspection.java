package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.adapter.IndexAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.InspectionLists;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.Inspection;
import de.greenrobot.dao.InspectionDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Description:检查项目Activity
 * Created by Chengbin He on 2016/3/21.
 */
public class ActInspection extends BaseActivity {
    private IndexAdapter mAdapter;
    private LayoutActivityTitle title;
    private List<Inspection> daoList;//数据库list
    private List<InspectionLists.InspectionScource> httpList;//网络list
    private Context mContext = ActInspection.this;
    private ListView listView;
    private TextView overlay;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private InspectionDao dao;//检查项目对应数据库表
    private SharedPreferences serverTimeSP;
    private boolean finish = true;
    private long inspectionServerTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seach_index);

        serverTimeSP = getSharedPreferences("serverTime",MODE_PRIVATE);
        inspectionServerTime = serverTimeSP.getLong(SPInfo.ServerTimeKey_inspectionServerTime,SPInfo.ServerTimeValue_inspectionServerTime);

        initView();
        initTitle();

        getDataFromeHttp();
//        getDataFromeDB();
    }


    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(onClickListener);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    mLetterBar.setVisibility(View.VISIBLE);
                    getDataFromeDB();
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    mLetterBar.setVisibility(View.GONE);
                    dao = ((MyApplication) getApplication()).getDaoSession().getInspectionDao();
                    daoList = dao.queryBuilder()
//                        .whereOr(InspectionDao.Properties.Name.like("%" + keyword + "%"),
//                            InspectionDao.Properties.PinyinName.like("%" + keyword + "%"),
//                                InspectionDao.Properties.IsActive.eq(true))
                            .where(InspectionDao.Properties.IsActive.eq(true),
                                    dao.queryBuilder().or(InspectionDao.Properties.Name.like("%" + keyword + "%"),
                            InspectionDao.Properties.PinyinName.like("%" + keyword + "%")))
                        .orderAsc(InspectionDao.Properties.PinyinName)
                        .list();
                    if (daoList == null || daoList.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mAdapter.changeData(daoList);
                        listView.setSelection(0);
                    }
                }
            }
        });

    }


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("检查项目");
        title.backSlid(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_search_clear:
                    searchBox.setText("");
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    break;
            }
        }
    };
    /**
     * 索引item点击事件
     */
    IndexAdapter.OnIndexClickListener onIndexClickListener
        = new IndexAdapter.OnIndexClickListener() {

        @Override
        public void onNameClick(String name, int position) {
            Intent intent = new Intent(mContext, ActInspectionDetail.class);
            int inspectionId = (int) daoList.get(position).getId();
            intent.putExtra("inspectionId", inspectionId + "");
            Log.i("hcb", "inspectionId is" + inspectionId);
            startActivity(intent);
        }


        @Override
        public void onLocateClick() {

        }
    };


    /**
     * 网络请求数据
     */
    public void getDataFromeHttp() {
        HttpService.getHttpService().getInspectionList(inspectionServerTime)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext(new Action1<InspectionLists>() {
                @Override public void call(InspectionLists inspectionLists) {

                    inspectionServerTime = inspectionLists.getData().getServerTime();
                    serverTimeSP.edit().putLong(SPInfo.ServerTimeKey_inspectionServerTime,inspectionServerTime).commit();
                    Log.i("hcbtest","inspectionServerTime" +inspectionServerTime);
                    finish = inspectionLists.getData().isFinish();
                    if (inspectionLists.getData().getSource().size() > 0) {
                        httpList = inspectionLists.getData().getSource();
                        insert();
                    }

                }
            })
            .subscribe(new Subscriber<InspectionLists>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "http onCompleted()");
                    if (!finish){
                        getDataFromeHttp();
                    }else {
                        getDataFromeDB();
                    }

                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "http onError");
                    getDataFromeDB();

                }


                @Override
                public void onNext(InspectionLists inspectionLists) {
                    Log.i("hcb", "http onNext");

                }
            });
    }

    /**
     *数据入库
     */
    private void insert(){
        dao = ((MyApplication) getApplication()).getDaoSession().getInspectionDao();
        if (httpList.size() > 0) {
            Log.i("hcb", "httpList.size()>0");

            for (int i = 0; i < httpList.size(); i++) {
                Log.i("hcb", " for i " + i);
                InspectionLists.InspectionScource bean = httpList.get(i);
                String name = httpList.get(i).getName();
                String pinyinName = httpList.get(i).getPinyinName();
                long id = httpList.get(i).getId();
                int status = httpList.get(i).getStatus();
                Boolean insurance = httpList.get(i).getIsInsurance();
                Boolean isActive = httpList.get(i).getIsActive();
                //long id, Long updateTime, String name, String pinyinName, String pinyinFullName, String fullName, Integer status, Boolean isActive, Boolean insurance) {

                Inspection inspection = new Inspection();
                inspection.setFullName(bean.getFullName());
                inspection.setId(bean.getId());
                inspection.setInsurance(bean.getIsInsurance());
                inspection.setIsActive(bean.isActive());
                inspection.setName(bean.getName());
                inspection.setUpdateTime(bean.getUpdateTime());
                inspection.setPinyinName(bean.getPinyinName());
                inspection.setPinyinFullName(bean.getPinyinFullName());
                inspection.setStatus(bean.getStatus());

                long count = dao.queryBuilder()
                        .where(InspectionDao.Properties.Id.eq(id),
                                InspectionDao.Properties.IsActive.eq(true))
                        .count();
                if (count > 0) {
                    dao.update(inspection);
                } else {
                    dao.insert(inspection);
                }
            }
        }
    }


    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<Inspection>() {
            @Override
            public void call(Subscriber<? super Inspection> subscriber) {

                dao = ((MyApplication) getApplication()).getDaoSession().getInspectionDao();
                List joes = dao.queryBuilder().where(InspectionDao.Properties.IsActive.eq(true))
                    .orderAsc(InspectionDao.Properties.PinyinName)
                    .list();
                daoList = joes;
                List<Inspection> temp = new ArrayList<Inspection>();
                Iterator<Inspection> iterator = daoList.iterator();
                while (iterator.hasNext()) {
                    Inspection next = iterator.next();
                    //if (Character.isDigit(next.getName().charAt(0))) {
                    if (!Character.isLetter(next.getName().charAt(0))) {
                        temp.add(next);
                        iterator.remove();
                    }
                }
                daoList.addAll(temp);
                ((MyApplication) getApplication()).closeDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Inspection>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", " db onCompleted()");
                    if (daoList != null) {
                        mAdapter = new IndexAdapter(mContext, daoList);
                        mAdapter.setIndexClickListener(onIndexClickListener);
                    } else {
                        daoList = new ArrayList<Inspection>();
                    }
                    listView.setAdapter(mAdapter);
                    mLetterBar.setOnLetterChangedListener(
                        new SideLetterBar.OnLetterChangedListener() {
                            @Override
                            public void onLetterChanged(String letter) {
                                int position = mAdapter.getLetterPosition(letter);
                                listView.setSelection(position);
                            }
                        });
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", " db onCompleted()");
                }


                @Override
                public void onNext(Inspection Inspection) {
                    Log.i("hcb", " db onCompleted()");
                }
            });
    }

}
