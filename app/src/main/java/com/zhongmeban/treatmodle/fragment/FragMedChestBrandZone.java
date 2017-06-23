package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceuticalFactory;
import com.zhongmeban.adapter.FragMedChestBrandZoneAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.MedicineFactoryLists;
import com.zhongmeban.bean.MedicineScourceItem;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:药箱子-品牌专区
 * user: Chengbin He
 * time：2016/1/7 10:46
 */
public class FragMedChestBrandZone extends BaseFragment
    implements SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshlayout;
    private FragMedChestBrandZoneAdapter mAdapter;
    private MedicineScourceItem load = new MedicineScourceItem();
    private GridLayoutManager mLinearLayoutManager;
    private int pageIndex;
    private int pageTotal;
    private boolean isScroll = true;
    private List<MedicineScourceItem> httpList = new ArrayList<MedicineScourceItem>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_with_recyclerview, container,false);
        mContext = getActivity();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageIndex = 1;
        load.setViewType(0);

        refreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeResources(R.color.app_green);
        refreshlayout.post(new Runnable() {
            @Override
            public void run() {
                refreshlayout.setRefreshing(true);
            }
        });
        refreshlayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mAdapter = new FragMedChestBrandZoneAdapter(mContext, httpList);
        mAdapter.setItemClickListener(adapterListener);
        mLinearLayoutManager = new GridLayoutManager(mContext, 3);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.setAdapter(mAdapter);

                getHttpData();
    }


    /**
     * 上拉刷新部分
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        Boolean isScrolling = false;


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScroll) {
                int lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                if (lastVisibleItem == (totalItemCount - 1)) {
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


    public void onPreLoadMore() {

        httpList.add(load);
        mAdapter.notifyData(httpList);
    }


    public void onPostLoadMore() {
        httpList.remove(load);
        isScroll = true;
    }


    public void LoadMore() {
        if (pageIndex>pageTotal){
            ToastUtil.showText(getActivity(),"没有数据了");
            return;
        }
        onPreLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageIndex++;
                getHttpData();
            }
        }, 1000);

    }


    /**
     * 下拉刷新部分
     */
    @Override
    public void onRefresh() {
        pageIndex = 1;
        getHttpData();
    }


    /**
     * 网络请求获取数据
     */
    public void getHttpData() {

        HttpService.getHttpService().postMedicineIcon(pageIndex,50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<MedicineFactoryLists>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "http onCompleted()");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.setRefreshing(false);
                        }
                    }, 1000);
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "http onError()");
                    Log.i("hcb", "http e " + e);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.setRefreshing(false);
                        }
                    }, 2000);
                    Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                    //                        tv_noentity.setVisibility(View.VISIBLE);
                }


                @Override
                public void onNext(MedicineFactoryLists medicineFactoryLists) {
                    Log.i("hcb", "http onNext()");
                    if (pageIndex == 1) {
                        httpList.clear();
                        httpList = medicineFactoryLists.getData().getSourceItems();
                    } else {
                        onPostLoadMore();
                        httpList.addAll(medicineFactoryLists.getData().getSourceItems());
                    }

                    pageTotal = medicineFactoryLists.getData().getTotalPage();
                    mAdapter.notifyData(httpList);
                }
            });

    }


    //Recycleradapter 点击事件接口
    AdapterClickInterface adapterListener = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(mContext, ActPharmaceuticalFactory.class);
            String manufacturerId = httpList.get(position).getId();
            intent.putExtra("manufacturerId", manufacturerId);
            startActivity(intent);
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };
}

