package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.bean.treatbean.TreatDoctor;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.net.okhttp.utils.L;
import com.zhongmeban.treatmodle.activity.ActDoctDetail;
import com.zhongmeban.treatmodle.activity.ActHospDetail;
import com.zhongmeban.treatmodle.activity.TreatCommonSeatchListActivity;
import com.zhongmeban.treatmodle.adapter.TreatDoctListsAdapter;
import com.zhongmeban.treatmodle.adapter.TreatHospListsAdapter;
import com.zhongmeban.treatmodle.contract.TreatSearchDoctListContract;
import com.zhongmeban.treatmodle.contract.TreatSearchHospListContract;

import java.util.List;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

/**
 * 医生搜索Fragment
 * Created by Chengbin He on 2016/12/9.
 */

public class TreatDoctSearchFragment extends BaseFragmentRefresh implements
        TreatCommonSeatchListActivity.ISearchListener, TreatSearchDoctListContract.View {

    private RecyclerView rvList;
    private TreatSearchDoctListContract.Presenter presenter;
    private TreatCommonSeatchListActivity activity;
    private TreatDoctListsAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isScroll = true;

    public static TreatDoctSearchFragment newInstance() {
        Bundle args = new Bundle();

        TreatDoctSearchFragment fragment = new TreatDoctSearchFragment();
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

//        mRefreshLayout  = (SwipeRefreshLayout) mRootView.findViewById(R.id.id_refresh);
        mRefreshLayout.setEnabled(false);

        rvList = (RecyclerView) mRootView.findViewById(R.id.id_recyclerview);
        layoutManager = new LinearLayoutManager(activity);

        rvList.setLayoutManager(layoutManager);
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
        //搜索输出回调
        Log.i("hcb","searchChangeListener text"+text);
        if(TextUtils.isEmpty(text)){
            rvList.removeOnScrollListener(onScrollListener);
        }else {
            rvList.addOnScrollListener(onScrollListener);
        }
        initEmptyView();
        showEmptyLayoutState(LOADING_STATE_SUCESS);
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
        if (mAdapter!=null){
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
    public void showData(List<TreatDoctor> httpList) {
        if (mAdapter == null) {
            mAdapter = new TreatDoctListsAdapter(mContext, httpList);
            mAdapter.setItemClickListener(new AdapterClickInterface(){

                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(mContext, ActDoctDetail.class);

                    String dooctLevel = mAdapter.getDoctLevel(position);
                    String url = mAdapter.getIcon(position);

                    intent.putExtra("dooctLevel",dooctLevel);
                    intent.putExtra("url",url);

                    startActivity( presenter.startDoctDetail(intent,position));
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
    public void setPresenter(TreatSearchDoctListContract.Presenter presenter) {
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
