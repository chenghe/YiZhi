package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.fragment.AttentionHospChoosePresenter;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.treatmodle.activity.ActHospDetail;
import com.zhongmeban.treatmodle.activity.TreatCommonSeatchListActivity;
import com.zhongmeban.treatmodle.adapter.TreatHospListsAdapter;
import com.zhongmeban.treatmodle.contract.TreatSearchHospListContract;
import java.util.List;

/**
 * Created by Chengbin He on 2016/12/9.
 */

public class TreatHospitalSearchFragment extends BaseFragmentRefresh implements
    TreatCommonSeatchListActivity.ISearchListener, TreatSearchHospListContract.View {

    private RecyclerView rvList;
    private TreatSearchHospListContract.Presenter presenter;
    private TreatCommonSeatchListActivity activity;
    private TreatHospListsAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isScroll = true;
    private boolean isChoose = false;//是否为医院筛选


    public static TreatHospitalSearchFragment newInstance() {
        Bundle args = new Bundle();

        TreatHospitalSearchFragment fragment = new TreatHospitalSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
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
    protected int getLayoutIdRefresh() {
        return R.layout.treat_medicine_search_fragment;
    }


    @Override
    protected void initToolbarRefresh() {

    }


    @Override
    protected void initOnCreateView() {
        initEmptyView();
        showEmptyLayoutState(LOADING_STATE_SUCESS);

        Intent intent = activity.getIntent();
        isChoose = intent.getBooleanExtra(AttentionHospChoosePresenter.AttentionHospChoose,false);
        //        mRefreshLayout  = (SwipeRefreshLayout) mRootView.findViewById(R.id.id_refresh);
        mRefreshLayout.setEnabled(false);

        rvList = (RecyclerView) mRootView.findViewById(R.id.id_recyclerview);
        rvList.setLayoutManager(layoutManager = new LinearLayoutManager(activity));


        presenter.start();
    }


    /**
     * 上拉刷新部分
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        Boolean isScrolling = false;


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            activity.hideKeyBoard();
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScroll) {
                int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (lastVisibleItem == (totalItemCount - 1) && presenter.exitNextPage()) {
                    LoadMore();
                    isScroll = false;
                }
            }
        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dy > 0) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }
        }
    };


    @Override
    public void searchChangeListener(String text) {
        //搜索内容改变回调
        if (TextUtils.isEmpty(text)){
            rvList.removeOnScrollListener(onScrollListener);
        }else {
            rvList.addOnScrollListener(onScrollListener);
        }
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        showProgress();
        presenter.setSearchKeyword(text);
    }


    @Override
    public void onClickReLoadData() {
        super.onClickReLoadData();
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
    }


    @Override
    public void exitData() {
        rvList.setVisibility(View.VISIBLE);
        showEmptyLayoutState(LOADING_STATE_SUCESS);
    }


    @Override
    public void noData() {
        //        rvList.setVisibility(View.GONE);
        showEmptyLayoutState(LOADING_STATE_SEARCH_FAIL);
    }

    @Override
    public void noNet() {
        showEmptyLayoutState(LOADING_STATE_NO_NET);
    }

    @Override
    public void showProgress() {
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
    }

    public void onPreLoadMore() {
        if (mAdapter != null) {
            presenter.addLoadMore();
            mAdapter.notifyData(presenter.getHttpList());
        }

    }


    @Override
    public void onPostLoadMore() {
        presenter.removeLoadMore();
        isScroll = true;
    }


    public void LoadMore() {
        onPreLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.nextIndex();
                presenter.getHttpData();
            }
        }, 1000);

    }


    @Override
    public void showData(final List<TreatHospitalScource> httpList) {
        if (mAdapter == null) {
            mAdapter = new TreatHospListsAdapter(activity, httpList);
            mAdapter.setItemClickListener(new AdapterClickInterface() {

                @Override
                public void onItemClick(View v, int position) {
                    if (isChoose){
                        Intent chooseIntent = new Intent();
                        chooseIntent.putExtra(AttentionHospChoosePresenter.AttentionHospName,httpList.get(position).getName());
                        chooseIntent.putExtra(AttentionHospChoosePresenter.AttentionHospId,httpList.get(position).getId());
                        activity.setResult(AttentionHospChoosePresenter.AttentionChooseHospOk,chooseIntent);
                        activity.finish();
                    }else {
                        Intent intent = new Intent(activity, ActHospDetail.class);
                        String hospLevel = mAdapter.getLevel(position);
                        intent.putExtra("hospLevel", hospLevel);
                        startActivity(presenter.startHospDetail(intent, position));
                    }


                }


                @Override
                public void onItemLongClick(View v, int position) {

                }
            });
            rvList.setAdapter(mAdapter);
        } else {
            mAdapter.notifyData(httpList);
        }
    }


    @Override
    public void setPresenter(TreatSearchHospListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }
}
