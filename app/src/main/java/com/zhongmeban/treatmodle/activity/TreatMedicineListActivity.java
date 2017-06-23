package com.zhongmeban.treatmodle.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.attentionmodle.activity.AttentionAddAssistMedicineActivity;
import com.zhongmeban.attentionmodle.activity.AttentionAddChemothRecordActivity;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.base.weight.LoadingFooter;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.MedicineListBean;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.view.DividerItemDecoration;
import com.zhongmeban.view.headerfooteradapter.EndlessRecyclerOnScrollListener;
import com.zhongmeban.view.headerfooteradapter.HeaderAndFooterRecyclerViewAdapter;
import com.zhongmeban.view.headerfooteradapter.RecyclerViewStateUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 功能描述： 药品列博
 * 作者：ysf on 2016/12/2 10:52
 */
public class TreatMedicineListActivity extends BaseActivityRefresh {

    public static final String EXTRA_MEDICINE_TYPE_BEAN = "extra_medicine_type_bean";
    public static final String EXTRA_MEDICINE_BEAN = "extra_medicine_bean";
    private static final int REQUEST_COUNT = 20;

    private MedicineTypeBean medicineType;
    private int fromActivityType;// 来自哪个界面启动
    private RecyclerView rvList;
    private List<MedicineListBean> mDatas = new ArrayList<>();
    private CommonAdapter adapter;
    //private LoadMoreWrapper adapterLoadMore;
    private int index = 1;
    private LinearLayoutManager layoutManager;
    private HeaderAndFooterRecyclerViewAdapter footerAdapter;
    private boolean isHaveMore = true;


    @Override protected int getLayoutIdRefresh() {
        return R.layout.act_treat_medicine_list;
    }


    @Override protected void initToolbarRefresh() {
        medicineType = (MedicineTypeBean) getIntent().getSerializableExtra(
            EXTRA_MEDICINE_TYPE_BEAN);
        initToolbarCustom(medicineType.getName(), "");
        mToolbar.setActionName(R.drawable.home_search);

    }


    @Override protected void initViewRefresh() {

        fromActivityType = getIntent().getIntExtra(
            TreatMedicineTypeListActivity.EXTRA_FROM_ACTIVITY, 0);

        initEmptyView();
        mRefreshLayout.setEnabled(false);
        rvList = (RecyclerView) findViewById(R.id.id_recyclerview);
        rvList.setLayoutManager(layoutManager = new LinearLayoutManager(this));
        rvList.addItemDecoration(
            new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        for (int i = 0; i < 10; i++) {
            //mDatas.add(new MedicineListBean("化学名", i, 1, "价格", "显示名", 2));
        }
        initAdapter();

        footerAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvList.setAdapter(footerAdapter);

        Logger.d("第一个加载 false");
        getData(medicineType.getId(), index, false);
    }


    private void initAdapter() {
        adapter =
            new CommonAdapter<MedicineListBean>(this, R.layout.item_treat_medicine_info, mDatas) {

                @Override
                protected void convert(ViewHolder holder, final MedicineListBean medicineBean, final int position) {

                    TextView tvLeft = holder.getView(R.id.tv_medicalInsurance);
                    TextView tvRight = holder.getView(R.id.tv_is_medical_special);

                    holder.setText(R.id.tv_name, medicineBean.getShowName());
                    holder.setText(R.id.tv_desc, medicineBean.getChemicalName());

                    holder.setText(R.id.tv_cost, ZMBUtil.getFormatPrice(medicineBean.getPrice()));

                    boolean isInsurance = Constants.isMedicalInsurance(medicineBean.getInsurance());
                    boolean isSpecial = Constants.isMedicalInsurance(medicineBean.getSpecial());
                    if (isInsurance && isSpecial) {
                        tvLeft.setText("保");
                        tvRight.setText("特");
                        tvLeft.setVisibility(View.VISIBLE);
                        tvRight.setVisibility(View.VISIBLE);
                    } else if (isInsurance) {
                        tvRight.setText("保");
                        tvLeft.setVisibility(View.GONE);
                        tvRight.setVisibility(View.VISIBLE);
                    } else if (isSpecial) {
                        tvRight.setText("特");
                        tvLeft.setVisibility(View.GONE);
                        tvRight.setVisibility(View.VISIBLE);
                    } else {
                        tvLeft.setVisibility(View.GONE);
                        tvRight.setVisibility(View.GONE);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(TreatMedicineListActivity.this,
                                getFromActByType(fromActivityType));
                            intent.putExtra(
                                ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID,
                                medicineBean.getId() + "");
                            intent.putExtra(EXTRA_MEDICINE_BEAN, medicineBean);
                            startActivity(intent);

                            //RxBus.getDefault().post(medicineBean);
                        }
                    });
                }
            };

        rvList.addOnScrollListener(mOnScrollListener);
    }


    private EndlessRecyclerOnScrollListener mOnScrollListener
        = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvList);
            if (state == LoadingFooter.State.Loading) {
                Log.d("@Cundong", "the state is Loading, just wait..");
                return;
            }

            if (isHaveMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(TreatMedicineListActivity.this, rvList,
                    REQUEST_COUNT, LoadingFooter.State.Loading, null);
                getData(medicineType.getId(), index, true);
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(TreatMedicineListActivity.this, rvList,
                    REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };


    private void getData(final int medicineTypeId, int currentIndex, final boolean isLoadMore) {
        HttpService.getHttpService().getMedicinePage("", medicineTypeId, currentIndex, 20)
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
                                TreatMedicineListActivity.this, rvList,
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
                    mDatas.addAll(httpResult.getData());
                    if (mDatas.size() == 0) {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }


    @Override public void onClickReLoadData() {
        super.onClickReLoadData();
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        mDatas.clear();
        getData(medicineType.getId(), 1, false);
    }


    @Override public void onRefresh() {
        super.onRefresh();
        mDatas.clear();
        getData(medicineType.getId(), 1, false);
    }


    @Override public void clickRight() {
        super.clickRight();
        Intent intent = new Intent(this, TreatCommonSeatchListActivity.class);
        intent.putExtra(TreatCommonSeatchListActivity.EXTRA_SEARCH_TYPE,
            TreatCommonSeatchListActivity.SEARCH_MEDICINE_TYPE);
        //凸跳转搜索页面的时候把 从编辑页面来的classtype 带上
        intent.putExtra(TreatMedicineTypeListActivity.EXTRA_FROM_ACTIVITY, fromActivityType);

        startActivity(intent);
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
}
