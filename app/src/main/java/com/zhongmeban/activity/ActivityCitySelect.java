package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.zhongmeban.bean.CityList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.City;
import de.greenrobot.dao.CityDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 城市筛选
 * Created by Chengbin He on 2016/5/24.
 */
public class ActivityCitySelect extends BaseActivity{
    private ListView listView;
    private TextView overlay;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private IndexAdapter mAdapter;
    private List<City> daoList;//数据库List
    private List<CityList.City> httpList;//网络List
    private CityDao dao;//城市对应Dao
//    private long mServerTime =1479710530321L;
//    private SharedPreferences serverTimeSP;
    private Activity mContext = ActivityCitySelect.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seach_index);

        initView();
        initTitle();
//        serverTimeSP = getSharedPreferences("serverTime",MODE_PRIVATE);
//        mServerTime = serverTimeSP.getLong(SPInfo.ServerTimeKey_cityServerTime,0);
//        getDataFromeHttp();
        getDataFromeDB();

    }

    private void initView() {

        listView = (ListView) findViewById(R.id.listview);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listView.getWindowToken(),0);
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
                    getDataFromeDB();
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    dao = ((MyApplication)mContext.getApplication()).getDaoSession().getCityDao();
                    daoList = dao.queryBuilder()
                            .whereOr(CityDao.Properties.Name.like("%" + keyword + "%"),
                                    CityDao.Properties.PinyinName.like("%" + keyword + "%"))
                            .orderAsc(CityDao.Properties.PinyinName)
                            .list();
                    if (daoList == null || daoList.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mAdapter.changeData(daoList);
                    }
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择治疗城市");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_search_clear:
                    searchBox.setText("");
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    break;
            }
        }
    };
    IndexAdapter.OnIndexClickListener onIndexClickListener = new IndexAdapter.OnIndexClickListener(){

        @Override
        public void onNameClick(String name , int position) {
            Intent intent = new Intent();
            String cityId = daoList.get(position).getId()+"";
            String cityName = daoList.get(position).getName();
            Log.i("hcb","cityId"+cityId);
            Log.i("hcb","cityName"+cityName);
            intent.putExtra("cityId",cityId);
            intent.putExtra("cityName",cityName);
            mContext.setResult(100,intent);//100选择城市
            mContext.finish();

        }

        @Override
        public void onLocateClick() {

        }
    };

    /**
     * 网络请求数据
     */
    public void getDataFromeHttp() {
        HttpService.getHttpService().getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CityList>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                        dao = ((MyApplication)mContext.getApplication()).getDaoSession().getCityDao();
                        if (httpList.size() > 0) {
                            Log.i("hcb", "hettpList.size()>0");
                            for (int i = 0; i < httpList.size(); i++) {
                                Log.i("hcb", " for i " + i);
                                String name = httpList.get(i).getName();
                                String pinyinName = httpList.get(i).getPinyinName();
                                long id = httpList.get(i).getId();
                                City city = new City(id, name, pinyinName);

                                long count = dao.queryBuilder()
                                    .where(CityDao.Properties.Id.eq(id))
                                    .count();
                                if (count > 0) {
                                    dao.update(city);
                                } else {
                                    dao.insert(city);
                                }
                                //dao.insert(city);
                            }
                        }
//                        MyApplication.closeDB();
                        getDataFromeDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError");
                        Log.i("hcb", "e is"+e);
                    }

                    @Override
                    public void onNext(CityList cityList) {
                        Log.i("hcb", "http onNext");
//                        serverTimeSP.edit().putLong("indicatorServerTime",cityList.getServerTime()).commit();
                        if (cityList.getData().size() > 0) {
                            httpList = cityList.getData();
                        }

                    }
                });
    }

    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<City>() {
            @Override
            public void call(Subscriber<? super City> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication)mContext.getApplication()).getDaoSession().getCityDao();
                List joes = dao.queryBuilder()
                        .orderAsc(CityDao.Properties.PinyinName)
                        .list();
                daoList = joes;
                ((MyApplication)mContext.getApplication()).closeDB();
                Log.i("hcb", " daoList.size" + daoList.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<City>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", " db onCompleted()");
                        if (daoList != null) {
                            mAdapter = new IndexAdapter(mContext, daoList);
                            mAdapter.setIndexClickListener(onIndexClickListener);
                        } else {
                            daoList = new ArrayList<City>();
                        }
                        listView.setAdapter(mAdapter);
                        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
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
                    public void onNext(City city) {
                        Log.i("hcb", " db onCompleted()");
                    }
                });
    }
}
