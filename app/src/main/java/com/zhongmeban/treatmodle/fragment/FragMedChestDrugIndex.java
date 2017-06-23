package com.zhongmeban.treatmodle.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
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
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.adapter.MedicineIndexAdapter;
import com.zhongmeban.adapter.OnIndexClickListener;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.MedicineList;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.genericity.SPInfo;
import de.greenrobot.dao.Medicine;
import de.greenrobot.dao.MedicineDao;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Description:药箱子-全部药品
 * user: Chengbin He
 * time：2016/1/7 10:45
 */
public class FragMedChestDrugIndex extends BaseFragment {

    private Activity mContext;
    private MedicineIndexAdapter mAdapter;
    private List<Medicine> daoList;//数据库list
    private List<MedicineList.DrugScourceItem> httpList = new ArrayList<>();//网络list
    private ListView listView;
    private TextView overlay;
    private EditText searchBox;//搜索editText
    private ImageView clearBtn;//清除搜索内容
    private ViewGroup emptyView;
    private SideLetterBar mLetterBar;
    private MedicineDao dao;//检查项目对应数据库表
    private long medicineServerTime ;
    private SharedPreferences serverTimeSP;
    private boolean finish = true;

    private  MedicineList.DrugScourceItem mCurrentBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_index, null);
        mContext = getActivity();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serverTimeSP = mContext.getSharedPreferences(SPInfo.SPServerTime,Context.MODE_PRIVATE);
        medicineServerTime = serverTimeSP.getLong(SPInfo.ServerTimeKey_medicineServerTime,SPInfo.ServerTimeValue_medicineServerTime);

        initView(view);
        getDataFromeHttp();
//        getDataFromeDB();
    }


    private void initView(View view) {

        listView = (ListView) view.findViewById(R.id.listview);
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

        overlay = (TextView) view.findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) view.findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);

        emptyView = (ViewGroup) view.findViewById(R.id.empty_view);
        clearBtn = (ImageView) view.findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(onClickListener);
        searchBox = (EditText) view.findViewById(R.id.et_search);
        searchBox.addTextChangedListener(textWatcher);
    }


    /**
     * 网络请求数据
     */
    public void getDataFromeHttp() {
        HttpService.getHttpService().getMedicine(medicineServerTime)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext(new Action1<MedicineList>() {
                @Override public void call(MedicineList medicineList) {
                    Logger.e("线程");

                    medicineServerTime = medicineList.getData().getServerTime();
                    serverTimeSP.edit().putLong(SPInfo.ServerTimeKey_medicineServerTime,medicineServerTime).commit();
                    Log.i("hcbtest","medicineServerTime" +medicineServerTime);
                    finish = medicineList.getData().isFinish();
                    if (medicineList.getData().getSource().size() > 0) {
                        httpList = medicineList.getData().getSource();
                        insert();
                    }

                    getDataFromeDB();
                }
            })
            .subscribe(new Subscriber<MedicineList>() {
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
                    Log.i("hcb", "e is " + e);
                    getDataFromeDB();
                }


                @Override
                public void onNext(MedicineList medicineList) {
                    Log.i("hcb", "http onNext");


                }
            });
    }

    /**
     *数据入库
     */
    private void insert(){
        dao = ((MyApplication) mContext.getApplication()).getDaoSession()
                .getMedicineDao();
        if (httpList.size() > 0) {
            Log.i("hcb", "httpList.size()>0");
            for (int i = 0; i < httpList.size(); i++) {
                Log.i("hcb", " for i " + i);
                MedicineList.DrugScourceItem bean = httpList.get(i);
                Medicine medicine = new Medicine();

                medicine.setMedicineId(bean.getMedicineId());
                medicine.setUpdateTime(bean.getUpdateTime());
                medicine.setChemicalId(bean.getChemicalId());
                medicine.setInsurance(bean.getInsurance());
                medicine.setClassify(bean.getClassify());
                medicine.setImports(bean.getImports());
                medicine.setType(bean.getType());
                medicine.setSpecial(bean.getSpecial());
                medicine.setPrescription(bean.getPrescription());
                medicine.setStatus(bean.getStatus());
                medicine.setShowName(bean.getShowName());
                medicine.setChemicalName(bean.getChemicalName());
                medicine.setMedicineName(bean.getMedicineName());
                medicine.setPinyinName(bean.getPinyinName());
                medicine.setPriceMax(bean.getPriceMax());
                medicine.setPriceMin(bean.getPriceMin());
                medicine.setIsActive(bean.isActive());

                long count = dao.queryBuilder()
                        .where(MedicineDao.Properties.MedicineId.eq(bean.getMedicineId()),
                                MedicineDao.Properties.IsActive.eq(true))
                        .count();
                if (count > 0) {
                    dao.update(medicine);
                } else {
                    dao.insert(medicine);
                }
                //dao.insert(medicine);
            }
        }
        ((MyApplication) mContext.getApplication()).closeDB();
    }


    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {

/*        //数据库查询后返回一个Observable
        Observable.create(
            new Observable.OnSubscribe<List<Medicine>>() {

                @Override public void call(Subscriber<? super List<Medicine>> subscriber) {
                    dao = ((MyApplication) mContext.getApplication()).getDaoSession()
                        .getMedicineDao();
                    List<Medicine> joes = dao.queryBuilder()
                        .orderAsc(MedicineDao.Properties.PinyinName)
                        .list();
                    QueryBuilder<Medicine> qb = dao.queryBuilder();
                    qb.where(MedicineDao.Properties.Western.eq("Joe"),qb.and(MedicineDao.Properties.Western.eq(1970),MedicineDao.Properties.PriceMax.ge(10)));
                    ((MyApplication) mContext.getApplication()).closeDB();
                    subscriber.onNext(joes);
                }
            }).subscribe(new Action1<List<Medicine>>() {
            @Override public void call(List<Medicine> medicines) {
                Logger.e("要箱子" + medicines.toString());
            }
        });*/

        Observable.create(new Observable.OnSubscribe<Medicine>() {
            @Override
            public void call(Subscriber<? super Medicine> subscriber) {
                Log.i("hcb", " db call");
                dao = ((MyApplication) mContext.getApplication()).getDaoSession().getMedicineDao();
                List joes = dao.queryBuilder().where(MedicineDao.Properties.IsActive.eq(true))
                    .orderAsc(MedicineDao.Properties.PinyinName)
                    .list();
                daoList = joes;
                ((MyApplication) mContext.getApplication()).closeDB();
                Log.i("hcb", " daoList.size" + daoList.size());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Medicine>() {
                @Override
                public void onCompleted() {

                    if (daoList != null) {
                        mAdapter = new MedicineIndexAdapter(mContext, daoList);
                        mAdapter.setIndexClickListener(onIndexClickListener);
                    } else {
                        daoList = new ArrayList<Medicine>();
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
                public void onNext(Medicine medicine) {
                    Log.i("hcb", " db onCompleted()");
                }
            });
    }


    OnIndexClickListener onIndexClickListener = new OnIndexClickListener() {
        @Override
        public void onNameClick(int position) {
            Intent intent = new Intent(mContext, ActPharmaceutiaclDetail.class);

            intent.putExtra(ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID,daoList.get(position).getMedicineId()+"");

            startActivity(intent);
        }


        @Override
        public void onLocateClick() {

        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
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

    TextWatcher textWatcher = new TextWatcher() {
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
                dao = ((MyApplication) mContext.getApplication()).getDaoSession().getMedicineDao();
                daoList = dao.queryBuilder()
//                    .whereOr(MedicineDao.Properties.MedicineName.like("%" + keyword + "%"),
//                        MedicineDao.Properties.PinyinName.like("%" + keyword + "%"),
//                            MedicineDao.Properties.IsActive.eq(true))
                        .where(MedicineDao.Properties.IsActive.eq(true),
                                dao.queryBuilder().or(MedicineDao.Properties.MedicineName.like("%" + keyword + "%"),
                        MedicineDao.Properties.PinyinName.like("%" + keyword + "%")))
                    .orderAsc(MedicineDao.Properties.PinyinName)
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
    };


    //中间字母索引是否显示
    public void isShowOverlay(Boolean flag) {
        overlay.setVisibility(flag ? View.VISIBLE : View.GONE);
    }
}
