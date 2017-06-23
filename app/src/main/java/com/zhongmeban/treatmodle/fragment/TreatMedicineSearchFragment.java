package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.attentionmodle.activity.AttentionAddAssistMedicineActivity;
import com.zhongmeban.attentionmodle.activity.AttentionAddChemothRecordActivity;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.base.weight.LoadingFooter;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.MedicineListBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.activity.TreatCommonSeatchListActivity;
import com.zhongmeban.treatmodle.activity.TreatMedicineListActivity;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.DividerItemDecoration;
import com.zhongmeban.view.headerfooteradapter.EndlessRecyclerOnScrollListener;
import com.zhongmeban.view.headerfooteradapter.HeaderAndFooterRecyclerViewAdapter;
import com.zhongmeban.view.headerfooteradapter.RecyclerViewStateUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

import static com.zhongmeban.utils.ZMBUtil.getFormatPrice;

/**
 * 药品搜索
 */
public class TreatMedicineSearchFragment extends BaseFragmentRefresh implements
    TreatCommonSeatchListActivity.ISearchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "param";

    private String keyWord = "";
    private RecyclerView rvList;
    private CommonAdapter adapter;
    private List<MedicineListBean> mDatas = new ArrayList<>();
    private HeaderAndFooterRecyclerViewAdapter footerAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isHaveMore = true;
    private static final int REQUEST_COUNT = 20;
    private int index = 1;
    private TreatCommonSeatchListActivity activity;

    private int fromActivityType;// 来自哪个界面启动


    public static TreatMedicineSearchFragment newInstance(int classType) {
        TreatMedicineSearchFragment fragment = new TreatMedicineSearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, classType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TreatCommonSeatchListActivity) {
            activity = (TreatCommonSeatchListActivity) context;
            activity.setSearchListener(this);
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromActivityType = getArguments().getInt(ARG_PARAM);
        }
    }


    @Override protected int getLayoutIdRefresh() {

        return R.layout.treat_medicine_search_fragment;
    }


    @Override protected void initToolbarRefresh() {
        mRefreshLayout.setEnabled(false);
    }


    @Override protected void initOnCreateView() {

        initEmptyView();
        showEmptyLayoutState(LOADING_STATE_SUCESS);
        rvList = (RecyclerView) mRootView.findViewById(R.id.id_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                activity.hideKeyBoard();
                return super.scrollVerticallyBy(dy, recycler, state);
            }
        };
        rvList.setLayoutManager(layoutManager);

        rvList.addItemDecoration(
            new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        initAdapter();

        footerAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvList.setAdapter(footerAdapter);
    }


    private void initAdapter() {
        adapter =
            new CommonAdapter<MedicineListBean>(getActivity(), R.layout.item_treat_medicine_info,
                mDatas) {

                @Override
                protected void convert(ViewHolder holder, final MedicineListBean medicineBean, final int position) {

                    TextView tvLeft = holder.getView(R.id.tv_medicalInsurance);
                    TextView tvRight = holder.getView(R.id.tv_is_medical_special);

                    holder.setText(R.id.tv_name, medicineBean.getShowName());
                    //holder.setText(R.id.tv_name, putstr(keyWord,medicineBean.getShowName(),getActivity()).toString());
                    holder.setText(R.id.tv_desc, medicineBean.getChemicalName());

                    holder.setText(R.id.tv_cost, getFormatPrice(medicineBean.getPrice()));
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
                            Intent intent = new Intent(getActivity(),
                                getFromActByType(fromActivityType));
                            intent.putExtra(
                                ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID,
                                medicineBean.getId() + "");
                            intent.putExtra(TreatMedicineListActivity.EXTRA_MEDICINE_BEAN,
                                medicineBean);
                            startActivity(intent);

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

            Logger.e("加载更多");

            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvList);
            if (state == LoadingFooter.State.Loading) {
                return;
            }

            if (isHaveMore) {
                // loading more
                RecyclerViewStateUtils.setFooterViewState(getActivity(), rvList,
                    REQUEST_COUNT, LoadingFooter.State.Loading, null);
                getData(true);
            } else {
                //the end
                RecyclerViewStateUtils.setFooterViewState(getActivity(), rvList,
                    REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
            }
        }
    };


    private void getData(final boolean isLoadMore) {
        HttpService.getHttpService().getMedicinePage(keyWord, 0, index, REQUEST_COUNT)
            .compose(RxUtil.<HttpResult<List<MedicineListBean>>>normalSchedulers())
            .subscribe(new Subscriber<HttpResult<List<MedicineListBean>>>() {
                @Override public void onCompleted() {
                    hideSwipeRefresh();
                }


                @Override public void onError(Throwable e) {
                    showOnErrorSearch(e, isLoadMore);
                    hideSwipeRefresh();
                    RecyclerViewStateUtils.setFooterViewState(rvList, LoadingFooter.State.Normal);
                    ToastUtil.showText(MyApplication.app, "加载失败");
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
                                getActivity(), rvList,
                                REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                        }

                    } else {

                        showOnNextLayoutSearch(httpResult);
                        mDatas.clear();
                        index = 2;
                    }
                    mDatas.addAll(httpResult.getData());
                    if (mDatas.size() == 0) {
                        showEmptyLayoutState(LOADING_STATE_SEARCH_FAIL);
                    } else if (mDatas.size() < REQUEST_COUNT) {
                        isHaveMore = false;

                        RecyclerViewStateUtils.setFooterViewState(
                            getActivity(), rvList,
                            REQUEST_COUNT, LoadingFooter.State.TheEnd, null);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }


    @Override public void onClickReLoadData() {
        super.onClickReLoadData();
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        index = 1; //每次重新搜索都是新的，必须重置为1
        getData(false);
    }


    @Override public void searchChangeListener(String text) {
        keyWord = text;
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        index = 1; //每次重新搜索都是新的，必须重置为1
        mDatas.clear();
        isHaveMore = true;
        getData(false);
    }


    /**
     * 关键字变色
     */
    public SpannableStringBuilder putstr(String keyword, String strtext, Context context) {
        String docInfo = strtext;
        int keywordIndex = strtext.indexOf(keyword);
        SpannableStringBuilder style = new SpannableStringBuilder(docInfo);
        while (keywordIndex != -1) {
            /**
             * 关键字颜色改变
             */
            style.setSpan(
                new ForegroundColorSpan(context.getResources().getColor(R.color.app_green)),
                keywordIndex, keywordIndex + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int tempkeywordTempIndex = keywordIndex + keyword.length();
            strtext = docInfo.substring(tempkeywordTempIndex, docInfo.length());
            keywordIndex = strtext.indexOf(keyword);
            if (keywordIndex != -1) {
                keywordIndex = keywordIndex + tempkeywordTempIndex;
            }
        }
        return style;

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
