package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.attentionmodle.activity.AttentionAddAssistMedicineActivity;
import com.zhongmeban.attentionmodle.activity.AttentionAddChemothRecordActivity;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.base.weight.LoadingFooter;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.MedicineListBean;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.SmoothCheckBox;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DividerItemDecoration;
import com.zhongmeban.view.headerfooteradapter.EndlessRecyclerOnScrollListener;
import com.zhongmeban.view.headerfooteradapter.HeaderAndFooterRecyclerViewAdapter;
import com.zhongmeban.view.headerfooteradapter.RecyclerViewStateUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 功能描述： 药品列博
 * 作者：ysf on 2016/12/2 10:52
 */
public class TreatMedicineListSelectActivity extends BaseActivityRefresh {

    public static final String EXTRA_MEDICINE_TYPE_BEAN = "extra_medicine_type_bean";
    public static final String EXTRA_MEDICINE_BEAN = "extra_medicine_bean";
    public static final String EXTRA_MEDICINE_SELECT_LIST = "extra_medicine_select_list";
    private static final int REQUEST_COUNT = 20;
    public static int RESLUT_CODE_SELECT = 20;

    private MedicineTypeBean medicineType;
    private int fromActivityType;// 来自哪个界面启动
    private RecyclerView rvList;
    private List<ChemotherapyRecordBean.Medicines> mDatas = new ArrayList<>();
    private List<ChemotherapyRecordBean.Medicines> mDatasSelect;
    private CommonAdapter adapter;
    //private LoadMoreWrapper adapterLoadMore;
    private int index = 1;
    private LinearLayoutManager layoutManager;
    private HeaderAndFooterRecyclerViewAdapter footerAdapter;
    private boolean isHaveMore = true;
    private EditText searchBox;
    private ImageView clearBtn;
    private String mKeyWord = "";


    @Override protected int getLayoutIdRefresh() {
        return R.layout.act_treat_medicine_list;
    }


    @Override protected void initToolbarRefresh() {
        medicineType = (MedicineTypeBean) getIntent().getSerializableExtra(
            EXTRA_MEDICINE_TYPE_BEAN);
        initToolbarCustom(medicineType.getName(), "完成");
        fromActivityType = getIntent().getIntExtra(
            TreatMedicineTypeListActivity.EXTRA_FROM_ACTIVITY, 0);
        List<ChemotherapyRecordBean.Medicines> selectData
            = (List<ChemotherapyRecordBean.Medicines>) getIntent().getSerializableExtra(
            EXTRA_MEDICINE_SELECT_LIST);
        mDatasSelect = new ArrayList<>();
        if (selectData != null) {
            mDatasSelect.addAll(selectData);
        }
    }


    @Override protected void initViewRefresh() {

        initEmptyView();
        findViewById(R.id.layout_searchbar).setVisibility(View.VISIBLE);
        mRefreshLayout.setEnabled(false);
        rvList = (RecyclerView) findViewById(R.id.id_recyclerview);

        layoutManager = new LinearLayoutManager(this) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                hideKeyBoard();
                return super.scrollVerticallyBy(dy, recycler, state);
            }
        };
        rvList.setLayoutManager(layoutManager);
        rvList.addItemDecoration(
            new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        initAdapter();

        footerAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvList.setAdapter(footerAdapter);

        getData(mKeyWord, medicineType.getId(), index, false);
        setSearchListener();
    }


    private void initAdapter() {
        adapter =
            new CommonAdapter<ChemotherapyRecordBean.Medicines>(this,
                R.layout.item_treat_medicine_info_select, mDatas) {

                @Override
                protected void convert(ViewHolder holder, final ChemotherapyRecordBean.Medicines medicineBean, final int position) {

                    SmoothCheckBox cb = holder.getView(R.id.cb_medicine_select);

                    holder.setText(R.id.tv_name, medicineBean.getShowName());
                    holder.setText(R.id.tv_desc, medicineBean.getChemicalName());
                    cb.setChecked(mDatasSelect.contains(medicineBean), false);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {

                            if (mDatasSelect.contains(medicineBean)) {
                                mDatasSelect.remove(medicineBean);
                            } else {
                                if (mDatasSelect.size()==10){
                                    ToastUtil.showText(TreatMedicineListSelectActivity.this,"最多选择10种药物");
                                } else{
                                    mDatasSelect.add(medicineBean);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            //RxBus.getDefault().post(medicineBean);
                        }
                    });
                }
            };

        rvList.addOnScrollListener(mOnScrollListener);
    }


    private void setSearchListener() {
        searchBox = (EditText) findViewById(R.id.et_search);
        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);

        hideKeyBoard();
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                mKeyWord = "";
            }
        });
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
                if (!TextUtils.isEmpty(keyword)) {
                    mKeyWord = keyword;
                    clearBtn.setVisibility(View.VISIBLE);
                    onClickReLoadData();
                }
            }
        });
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener
        = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvList);
            if (state == LoadingFooter.State.Loading) {
                return;
            }

            if (isHaveMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(TreatMedicineListSelectActivity.this,
                    rvList,
                    REQUEST_COUNT, LoadingFooter.State.Loading, null);
                getData(mKeyWord, medicineType.getId(), index, true);
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(TreatMedicineListSelectActivity.this,
                    rvList,
                    REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };


    private void getData(String keyWord, final int medicineTypeId, int currentIndex, final boolean isLoadMore) {
        HttpService.getHttpService().getMedicinePage(keyWord, medicineTypeId, currentIndex, 20)
            .compose(RxUtil.<HttpResult<List<MedicineListBean>>>normalSchedulers())
            .subscribe(new Subscriber<HttpResult<List<MedicineListBean>>>() {
                @Override public void onCompleted() {
                    hideSwipeRefresh();
                }


                @Override public void onError(Throwable e) {

                    hideSwipeRefresh();
                    RecyclerViewStateUtils.setFooterViewState(rvList, LoadingFooter.State.Normal);
                    showOnError(e, isLoadMore);
                }


                @Override
                public void onNext(HttpResult<List<MedicineListBean>> httpResult) {

                    RecyclerViewStateUtils.setFooterViewState(rvList, LoadingFooter.State.Normal);
                    showEmptyLayoutState(LOADING_STATE_SUCESS);

                    if (isLoadMore) {
                        index++;
                        if (httpResult != null && httpResult.getErrorCode() == 0 &&
                            httpResult.getData().size() == 0) {
                            isHaveMore = false;

                            RecyclerViewStateUtils.setFooterViewState(
                                TreatMedicineListSelectActivity.this, rvList,
                                REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                        }
                    } else {

                        showOnNextLayout(httpResult);
                        index = 2;
                        //mDatas.clear();
                        //mDatas.addAll(httpResult.getData());
                        adapter.notifyDataSetChanged();

                        showEmptyLayoutState(LOADING_STATE_SUCESS);
                    }

                    for (MedicineListBean bean : httpResult.getData()) {
                        mDatas.add(new ChemotherapyRecordBean.Medicines(bean.getChemicalName(),
                            bean.getMedicineName(), bean.getShowName(), bean.getId()));
                    }

                    //mDatas.addAll(httpResult.getData());
                    if (mDatas.size() == 0) {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    }
                    adapter.notifyDataSetChanged();
                }
            });

        //HttpService.getHttpService().getMedicinePage(keyWord, medicineTypeId, currentIndex, 20)
        //    .map(new HttpResultFunc<List<MedicineListBean>>())
        //    .flatMap(
        //        new Func1<List<MedicineListBean>, Observable<List<MedicineTypeBean>>>() {
        //            @Override
        //            public Observable<List<MedicineTypeBean>> call(List<MedicineListBean> listHttpResult) {
        //
        //                return Observable.from(listHttpResult).map(
        //                    new Func1<MedicineListBean, MedicineTypeBean>() {
        //                        @Override
        //                        public MedicineTypeBean call(MedicineListBean medicineListBean) {
        //                            return new MedicineTypeBean(medicineListBean.getId(),
        //                                medicineListBean.getMedicineName());
        //                        }
        //                    }).toList();
        //            }
        //        }).compose(RxUtil.<List<MedicineTypeBean>>normalSchedulers())
        //    .subscribe(new Action1<List<MedicineTypeBean>>() {
        //        @Override public void call(List<MedicineTypeBean> medicineTypeBeen) {
        //            Logger.v("我的选择" + medicineTypeBeen.toString());
        //        }
        //    }, new Action1<Throwable>() {
        //        @Override public void call(Throwable throwable) {
        //            Logger.e("挂了--" + throwable.toString());
        //        }
        //    });
    }


    @Override public void onClickReLoadData() {
        super.onClickReLoadData();
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        mDatas.clear();
        getData(mKeyWord, medicineType.getId(), 1, false);
    }


    @Override public void onRefresh() {
        super.onRefresh();
        mDatas.clear();
        getData(mKeyWord, medicineType.getId(), 1, false);
    }


    @Override public void clickRight() {
        startIntent();
    }


    private void startIntent() {
        if (mDatasSelect.size() == 0) {
            ToastUtil.showText(this, "至少选一种");
            return;
        }
        //Intent intent = new Intent(TreatMedicineListSelectActivity.this,
        //    getFromActByType(fromActivityType));
        //intent.putExtra(EXTRA_MEDICINE_SELECT_LIST, (Serializable) mDatasSelect);
        //startActivity(intent);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MEDICINE_SELECT_LIST, (Serializable) mDatasSelect);
        setResult(RESLUT_CODE_SELECT,intent);
        finish();
    }


    //根据来自不同的activity 跳转不同的页面，默认跳转详情
    private Class<?> getFromActByType(int type) {
        switch (type) {
            case 0:
                return ActPharmaceutiaclDetail.class;
            case AttentionAddChemothRecordActivity.EXTRA_CLASS_TYPE:
                return AttentionAddChemothRecordActivity.class;
            case AttentionAddAssistMedicineActivity.EXTRA_CLASS_TYPE:
                return AttentionAddAssistMedicineActivity.class;
        }

        return ActPharmaceutiaclDetail.class;
    }


    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
            Context.INPUT_METHOD_SERVICE);
        if (imm.isActive(searchBox)) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
