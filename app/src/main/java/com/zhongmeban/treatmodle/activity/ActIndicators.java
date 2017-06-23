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
import com.orhanobut.logger.Logger;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.adapter.IndexAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.IndiacatorList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.Indicator;
import de.greenrobot.dao.IndicatorDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:指标解读
 * Created by Chengbin He on 2016/4/1
 */
public class ActIndicators extends BaseActivity {

    private IndexAdapter mAdapter;
    private LayoutActivityTitle title;
    private List<Indicator> daoList;//数据库list
    private List<IndiacatorList.IndiacatorSource> httpList;//网络list
    private Context mContext = ActIndicators.this;
    private ListView listView;
    private TextView overlay;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private IndicatorDao dao;//指标解读对应数据库表
    private long indicatorServerTime;
    private SharedPreferences serverTimeSP;
    private boolean finish = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seach_index);

        serverTimeSP = getSharedPreferences(SPInfo.SPServerTime,MODE_PRIVATE);
        indicatorServerTime = serverTimeSP.getLong(SPInfo.ServerTimeKey_indicatorServerTime,SPInfo.ServerTimeValue_indicatorServerTime);

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
                    dao = ((MyApplication) getApplication()).getDaoSession().getIndicatorDao();
                    daoList = dao.queryBuilder()
//                        .whereOr(IndicatorDao.Properties.Name.like("%" + keyword + "%"),
//                            IndicatorDao.Properties.PinyinName.like("%" + keyword + "%"),
//                            IndicatorDao.Properties.IsActive.eq(true))
                            .where(IndicatorDao.Properties.IsActive.eq(true),
                                    dao.queryBuilder().or(IndicatorDao.Properties.Name.like("%" + keyword + "%"),
                            IndicatorDao.Properties.PinyinName.like("%" + keyword + "%")))
                        .orderAsc(IndicatorDao.Properties.PinyinName)
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


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("指标解读");
        title.backSlid(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 索引item点击事件
     */
    IndexAdapter.OnIndexClickListener onIndexClickListener
        = new IndexAdapter.OnIndexClickListener() {

        @Override
        public void onNameClick(String name, int position) {
            Intent intent = new Intent(mContext, ActIndicatorsDetail.class);
            int indicatorId = (int) daoList.get(position).getId();
            intent.putExtra("indexId", indicatorId + "");
            Log.i("hcb", "indicatorId is" + indicatorId);
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

        HttpService.getHttpService().getIndicatorList(indicatorServerTime)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<IndiacatorList>() {
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
                        getDataFromeDB();
                    }

                    @Override
                    public void onNext(IndiacatorList indiacatorList) {
                        Logger.v("错误--"+indiacatorList.toString());
                        indicatorServerTime = indiacatorList.getData().getServerTime();
                        Log.i("hcbtest","indicatorServerTime" +indicatorServerTime);
                        serverTimeSP.edit().putLong(SPInfo.ServerTimeKey_indicatorServerTime,indicatorServerTime).commit();
                        finish = indiacatorList.getData().isFinish();
                        if (indiacatorList.getData().getSource().size() > 0) {
                            httpList = indiacatorList.getData().getSource();
                            insert();

                    }

                }
            });
    }


    /**
     *数据入库
     */
    private void insert(){
        dao = ((MyApplication)getApplication()).getDaoSession().getIndicatorDao();
        if (httpList.size() > 0) {
            Log.i("hcb", "hettpList.size()>0");
            for (int i = 0; i < httpList.size(); i++) {
                Log.i("hcb", " for i " + i);
                IndiacatorList.IndiacatorSource bean = httpList.get(i);
                String name = httpList.get(i).getName();
                String pinyinName = httpList.get(i).getPinyinName();
                long id = httpList.get(i).getId();
                int status = httpList.get(i).getStatus();
                Boolean isActive = httpList.get(i).getIsActive();
                //long id, Long updateTime, Integer status, String name, String pinyinName, String pinyinFullName, String fullName, Boolean isActive) {
                Indicator indicator = new Indicator();
                indicator.setId(bean.getId());
                indicator.setUpdateTime(bean.getUpdateTime());
                indicator.setStatus(bean.getStatus());
                indicator.setName(bean.getName());
                indicator.setPinyinName(bean.getPinyinName());
                indicator.setPinyinFullName(bean.getPinyinFullName());
                indicator.setFullName(bean.getFullName());
                indicator.setUnit(bean.getUnit());
                indicator.setIsActive(bean.getIsActive());
                indicator.setIsLandmark(bean.getLandmark());

                long count = dao.queryBuilder()
                        .where(IndicatorDao.Properties.Id.eq(id), IndicatorDao.Properties.IsActive.eq(true))
                        .count();
                if (count > 0) {
                    dao.update(indicator);
                } else {
                    dao.insert(indicator);
                }

                //dao.insert(indicator);
            }
        }
    }

    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<Indicator>() {
            @Override
            public void call(Subscriber<? super Indicator> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication) getApplication()).getDaoSession().getIndicatorDao();
                List joes = dao.queryBuilder().where(IndicatorDao.Properties.IsActive.eq(true))
                    .orderAsc(IndicatorDao.Properties.PinyinName)
                    .list();
                daoList = joes;
                List<Indicator> temp = new ArrayList<Indicator>();
                Iterator<Indicator> iterator = daoList.iterator();
                while (iterator.hasNext()) {
                    Indicator next = iterator.next();
                    //if (Character.isDigit(next.getName().charAt(0))) {
                    if (!Character.isLetter(next.getName().charAt(0))) {
                        temp.add(next);
                        iterator.remove();
                    }
                }
                daoList.addAll(temp);

                ((MyApplication) getApplication()).closeDB();
                Log.i("hcb", " daoList.size" + daoList.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Indicator>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", " db onCompleted()");
                    if (daoList != null) {
                        mAdapter = new IndexAdapter(mContext, daoList);
                        mAdapter.setIndexClickListener(onIndexClickListener);
                    } else {
                        daoList = new ArrayList<Indicator>();
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
                public void onNext(Indicator indicator) {
                    Log.i("hcb", " db onCompleted()");
                }
            });
    }
}
