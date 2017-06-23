package com.zhongmeban.activity;

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
import com.zhongmeban.bean.NounexplainLists;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.Interpretation;
import de.greenrobot.dao.InterpretationDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:名词解释
 * time：2016/1/4 13:18
 */
public class ActInterpretationList extends BaseActivity {

    private IndexAdapter mAdapter;
    private LayoutActivityTitle title;
    private List<Interpretation> daoList;//数据库list
    private List<NounexplainLists.NounScourceItem> httpList;//网络list
    private Context mContext = ActInterpretationList.this;
    private ListView listView;
    private TextView overlay;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private InterpretationDao dao;//检查项目对应数据库表
    private SharedPreferences serverTimeSP;
    private long interpretationServerTime ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_seach_index);

        serverTimeSP = getSharedPreferences(SPInfo.SPServerTime,MODE_PRIVATE);
        interpretationServerTime = serverTimeSP.getLong(SPInfo.ServerTimeKey_interpretationServerTime,
                SPInfo.ServerTimeValue_interpretationServerTime);


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
                    dao = ((MyApplication) getApplication()).getDaoSession().getInterpretationDao();
                    daoList = dao.queryBuilder()
//                        .whereOr(InterpretationDao.Properties.Name.like("%" + keyword + "%"),
//                            InterpretationDao.Properties.PinyinName.like("%" + keyword + "%"),
//                                InterpretationDao.Properties.IsActive.eq(true))
                            .where(InterpretationDao.Properties.IsActive.eq(true),
                                    dao.queryBuilder().or(InterpretationDao.Properties.Name.like("%" + keyword + "%"),
                            InterpretationDao.Properties.PinyinName.like("%" + keyword + "%")))
                        .orderAsc(InterpretationDao.Properties.PinyinName)
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
    /**
     * 索引item点击事件
     */
    IndexAdapter.OnIndexClickListener onIndexClickListener
        = new IndexAdapter.OnIndexClickListener() {

        @Override
        public void onNameClick(String name, int position) {
            Intent intent = new Intent(mContext, ActInterpretationDetail.class);
            String interpretationId = daoList.get(position).getInterpretationId() + "";
            intent.putExtra("interpretationId", interpretationId);
            Log.i("hcb", "interpretationId is" + interpretationId);
            startActivity(intent);
        }


        @Override
        public void onLocateClick() {

        }
    };


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("名词解释");
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
    public void getDataFromeHttp() {
        HttpService.getHttpService().getInterpretation(interpretationServerTime)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Subscriber<NounexplainLists>() {
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
                public void onNext(NounexplainLists nounexplainLists) {
                    Log.i("hcb", "http onNext");
                    interpretationServerTime = nounexplainLists.getData().getServerTime();
                    serverTimeSP.edit().putLong(SPInfo.ServerTimeKey_interpretationServerTime,interpretationServerTime).commit();
                    Log.i("hcbtest","interpretationServerTime" +interpretationServerTime);

                    if (nounexplainLists.getData().getSource().size() > 0) {
                        httpList = nounexplainLists.getData().getSource();
                        insert();
                    }

                }
            });
    }

    /**
     *数据入库
     */
    private void insert(){
        dao = ((MyApplication) getApplication()).getDaoSession().getInterpretationDao();
        if (httpList != null && httpList.size() > 0) {

            Logger.d("名词解释--" + httpList.toString());
            for (int i = 0; i < httpList.size(); i++) {
                Log.i("hcb", " for i " + i);
                Long interpretationId = httpList.get(i).getInterpretationId();
                String name = httpList.get(i).getInterpretationName();
                String pinyinName = httpList.get(i).getPinyinName();
                boolean isActive = httpList.get(i).isActive();
                Interpretation interpretation = new Interpretation(interpretationId,
                        name, pinyinName, isActive);

                //, InterpretationDao.Properties.IsActive.eq(true),名词解释暂时没有isactive

                long count = dao.queryBuilder()
                        .where(InterpretationDao.Properties.InterpretationId.eq(
                                interpretationId))
                        .count();
                if (count > 0) {
                    dao.update(interpretation);
                } else {
                    dao.insert(interpretation);
                }
                //dao.insert(interpretation);
            }
        }
    }

    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<Interpretation>() {
            @Override
            public void call(Subscriber<? super Interpretation> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication) getApplication()).getDaoSession().getInterpretationDao();
                List joes = dao.queryBuilder().where(InterpretationDao.Properties.IsActive.eq(true))
                    .orderAsc(InterpretationDao.Properties.PinyinName)
                    .list();
                daoList = joes;
                ((MyApplication) getApplication()).closeDB();
                Log.i("hcb", " daoList.size" + daoList.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Interpretation>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", " db onCompleted()");
                    if (daoList != null) {
                        mAdapter = new IndexAdapter(mContext, daoList);
                        mAdapter.setIndexClickListener(onIndexClickListener);
                    } else {
                        daoList = new ArrayList<Interpretation>();
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
                public void onNext(Interpretation interpretation) {
                    Log.i("hcb", " db onCompleted()");
                }
            });
    }
}
