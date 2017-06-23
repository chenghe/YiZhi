package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.orhanobut.logger.Logger;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.ActTipsListAdapter;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.postbody.PageBody;
import com.zhongmeban.bean.treatbean.TreatTipsBean;
import com.zhongmeban.bean.treatbean.WebViewBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhongmeban.R.id.recyclerview;

/**
 * Description:小贴士Activity
 * time：2016/1/7 18:27
 */
public class ActTips extends BaseActivityToolBar implements SwipeRefreshLayout.OnRefreshListener {

    //    private LayoutActivityTitle title;
    private RecyclerView recyclerView;
    private PageBody body;
    private ActTipsListAdapter mAdapter;
    private SwipeRefreshLayout refreshlayout;
    private Context mContext = ActTips.this;
    private List<TreatTipsBean> list
        = new ArrayList<>();
    private int lastVisibleItem;
    private boolean isLoading;

    private LinearLayoutManager mLayoutManager;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 1;

    private int pagerIndex = 1;
    private int pagerCount = 20;

    private Set<String> mSetReadIds = new HashSet<>();


    @Override protected int getLayoutId() {
        return R.layout.act_tips;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("小贴士", "");
    }


    @Override protected void initView() {
        initEmptyView();

        Set<String> stringSet = (Set<String>) SPUtils.getParams(getApplicationContext(),
            SPInfo.Tip_Read_Set,
            mSetReadIds, "");
        mSetReadIds = new HashSet<>(stringSet);

        initTitle();
        iniView();

        body = new PageBody(pagerIndex, pagerCount);
        getHttpData(body, false);

    }


    private void iniView() {
        recyclerView = (RecyclerView) findViewById(recyclerview);
        recyclerView.setLayoutManager(mLayoutManager = new LinearLayoutManager(mContext));
        mAdapter = new ActTipsListAdapter(mContext, list, mSetReadIds);
        mAdapter.setItemClickListener(adapterClick);
        recyclerView.setAdapter(mAdapter);

        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeResources(R.color.app_green);
        refreshlayout.post(new Runnable() {
            @Override
            public void run() {
                //refreshlayout.setRefreshing(true);
            }
        });
        refreshlayout.setOnRefreshListener(this);

        scrollRecycleView();
    }


    /**
     * 初始化Title
     */
    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("小贴士");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * RecyclerView 被点击监听
     */
    AdapterClickInterface adapterClick = new AdapterClickInterface() {

        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(mContext, TreatNewsDetailActivity.class);
            //            intent.putExtra(ActTipsDetails.EXTRA_TIP_BEAN, list.get(position));

            TreatTipsBean item = list.get(position);

            WebViewBean webViewBean = new WebViewBean(item.getUrlAddress(), item.getTitle(),
                item.getId() + "", TreatNewsDetailActivity.FROM_TIPS);

            intent.putExtra(TreatNewsDetailActivity.WEBVIEWBEAN, webViewBean);

            mSetReadIds.add(String.valueOf(list.get(position).getId()));
            SPUtils.putApply(getApplicationContext(), SPInfo.Tip_Read_Set, mSetReadIds, "");

            if (list.get(position) != null) {
                startActivity(intent);
            } else {
                ToastUtil.showText(ActTips.this, "暂无内容");
            }
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };


    @Override protected void onResume() {
        super.onResume();

        mAdapter.notifyDataSetChanged();
    }


    /**
     * 网络请求获取数据
     */
    private void getHttpData(PageBody body, final boolean isLoadMore) {
        HttpService.getHttpService().getTipList(body.getPageIndex(), body.getPageSize())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<HttpResult<List<TreatTipsBean>>>() {

                @Override
                public void onStart() {
                    super.onStart();
                    Log.i("hcb", "onStart()");
                    recyclerView.setVisibility(View.VISIBLE);
                }


                @Override
                public void onCompleted() {
                    refreshlayout.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.setRefreshing(false);
                        }
                    });

                    isLoading = false;
                }


                @Override
                public void onError(Throwable e) {
                    Logger.e(e.toString());
                    refreshlayout.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.setRefreshing(false);
                            isLoading = false;
                        }
                    });
                    recyclerView.setVisibility(View.GONE);
                    showEmptyLayoutState(LOADING_STATE_NO_NET);
                }


                @Override
                public void onNext(HttpResult<List<TreatTipsBean>> httpResult) {

                    showEmptyLayoutState(LOADING_STATE_SUCESS);
                    Log.i("hcb", "onNext(TipsLists tipsLists)");
                    if (httpResult != null && httpResult.getData() != null) {

                        if (isLoadMore) {
                            pagerIndex++;
                            list.addAll(httpResult.getData());
                            if (httpResult.getData().size() == 0) {
                                ToastUtil.showText(ActTips.this, "没有更多数据");
                            }

                        } else {
                            list.clear();
                            list = httpResult.getData();

                            if (list.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                showEmptyLayoutState(LOADING_STATE_EMPTY);
                            }
                            pagerIndex = 2;
                        }
                        mAdapter.notifyData(list);

                    } else {
                        showEmptyLayoutState(LOADING_STATE_EMPTY);
                    }
                }
            });
    }


    @Override
    public void onRefresh() {
        pagerIndex = 1;
        getHttpData(new PageBody(pagerIndex, pagerCount), false);
    }


    private boolean isScrollUp = false;


    /**
     * recyclerView Scroll listener , maybe in here is wrong ?
     */
    public void scrollRecycleView() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    totalItemCount = mLayoutManager.getItemCount();
                    lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    Logger.d("totalItemCount =" + totalItemCount + "-----" +
                        "lastVisibleItemPosition =" + lastVisibleItemPosition);
                    //if (!loading && visibleItemCount < (visibleLastIndex + VISIBLE_THRESHOLD) && dy > 0)
                    if (!isLoading &&
                        totalItemCount <= (lastVisibleItemPosition + visibleThreshold)&&isScrollUp) {
                        //此时是刷新状态
                        onLoadMore();
                        isLoading = true;
                    }
                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrollUp = dy > 0 ? true : false;
            }
        });
    }


    private void onLoadMore() {

        refreshlayout.setRefreshing(true);
        getHttpData(new PageBody(pagerIndex, pagerCount), true);

    }


    @Override public void onClickReLoadData() {
        super.onClickReLoadData();
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        getHttpData(new PageBody(1, pagerCount), false);
    }
}
