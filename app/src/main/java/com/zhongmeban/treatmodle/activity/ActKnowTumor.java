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
import com.zhongmeban.bean.TrumerLists;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.Tumor;
import de.greenrobot.dao.TumorDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:认识肿瘤页面
 * time：2015/12/29 15:30
 */
public class ActKnowTumor extends BaseActivity {
    private IndexAdapter mAdapter;
    private List<Tumor> daoList;//数据库list
    private List<TrumerLists.TrumerSource> httpList;//网络list
    private Context mContext = ActKnowTumor.this;
    private ListView listView;
    private TextView overlay;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private TumorDao dao;//认识肿瘤对应数据库表
    private long mServerTime ;
    private SharedPreferences serverTimeSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seach_index);

        serverTimeSP = getSharedPreferences(SPInfo.SPServerTime,MODE_PRIVATE);
        mServerTime = serverTimeSP.getLong(SPInfo.ServerTimeKey_tumorServerTime,SPInfo.ServerTimeValue_tumorServerTime);


        initView();
        initTitle();

        getDataFromeHttp(mServerTime);
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
                    dao = ((MyApplication) getApplication()).getDaoSession().getTumorDao();
                    daoList = dao.queryBuilder()
//                        .whereOr(TumorDao.Properties.Name.like("%" + keyword + "%"),
//                            TumorDao.Properties.PinyinName.like("%" + keyword + "%"),
//                                TumorDao.Properties.IsActive.eq(true))
                            .where(TumorDao.Properties.IsActive.eq(true),
                                    dao.queryBuilder().or(TumorDao.Properties.Name.like("%" + keyword + "%"),
                            TumorDao.Properties.PinyinName.like("%" + keyword + "%")))
                        .orderAsc(TumorDao.Properties.PinyinName)
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
    IndexAdapter.OnIndexClickListener onIndexClickListener
        = new IndexAdapter.OnIndexClickListener() {

        @Override
        public void onNameClick(String name, int position) {
            Intent intent = new Intent(mContext, ActKnowTumorDetail.class);
            String diseaseId = daoList.get(position).getId() + "";
            intent.putExtra("diseaseId", diseaseId);
            startActivity(intent);
        }


        @Override
        public void onLocateClick() {

        }
    };


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("认识肿瘤");
        title.backSlid(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 网络请求数据
     */
    public void getDataFromeHttp(Long serverTime) {
        HttpService.getHttpService().getTumor(serverTime)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Subscriber<TrumerLists>() {
                @Override
                public void onCompleted() {

                    getDataFromeDB();
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "http onError");
                    getDataFromeDB();
                }


                @Override
                public void onNext(TrumerLists trumerLists) {
                    Logger.d("认识肿瘤---" + trumerLists.toString());

                    mServerTime = trumerLists.getData().getServerTime();
                    serverTimeSP.edit().putLong(SPInfo.ServerTimeKey_tumorServerTime,mServerTime).commit();
                    Log.i("hcbtest","mServerTime" +mServerTime);
                    if (trumerLists.getData().getSource().size() > 0) {
                        httpList = trumerLists.getData().getSource();
                        insert();
                    }



                }
            });
    }

    /**
     *数据入库
     */
    private void insert(){
        dao = ((MyApplication) getApplication()).getDaoSession().getTumorDao();
        if (httpList.size() > 0) {
            Log.i("hcb", "hettpList.size()>0");
            for (int i = 0; i < httpList.size(); i++) {
                String name = httpList.get(i).getName();
                String pinyinName = httpList.get(i).getPinyinName();
                long id = httpList.get(i).getId();
                int status = httpList.get(i).getStatus();
                Boolean isActive = httpList.get(i).getIsActive();
                Tumor tumor = new Tumor(id, name, pinyinName,
                        status, isActive);
                long count = dao.queryBuilder()
                        .where(TumorDao.Properties.Id.eq(id),
                                TumorDao.Properties.IsActive.eq(true))
                        .count();
                if (count > 0) {
                    dao.update(tumor);
                } else {
                    dao.insert(tumor);
                }

            }
        }
    }

    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<Tumor>() {
            @Override
            public void call(Subscriber<? super Tumor> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication) getApplication()).getDaoSession().getTumorDao();
                List joes = dao.queryBuilder().where(TumorDao.Properties.IsActive.eq(true))
                    .orderAsc(TumorDao.Properties.PinyinName)
                    .list();
                daoList = joes;
                ((MyApplication) getApplication()).closeDB();

                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Tumor>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", " db onCompleted()");
                    if (daoList != null) {
                        mAdapter = new IndexAdapter(mContext, daoList);
                        mAdapter.setIndexClickListener(onIndexClickListener);
                    } else {
                        daoList = new ArrayList<Tumor>();
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
                public void onNext(Tumor tumor) {
                    Log.i("hcb", " db onCompleted()");
                }
            });
    }

}
