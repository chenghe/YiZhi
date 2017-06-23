package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.Interface.IToolbarListener;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityHospSelect;
import com.zhongmeban.adapter.HospListsAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.Doctor;
import com.zhongmeban.bean.DropDownBean;
import com.zhongmeban.bean.HospitalLists;
import com.zhongmeban.bean.HospitalScource;
import com.zhongmeban.bean.treatbean.CityListBean;
import com.zhongmeban.bean.treatbean.TreatHospitalScource;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.adapter.DoctListsAdapter;
import com.zhongmeban.treatmodle.adapter.TreatHospListsAdapter;
import com.zhongmeban.treatmodle.contract.TreatDoctListsContract;
import com.zhongmeban.treatmodle.contract.TreatHospListsContract;
import com.zhongmeban.treatmodle.presenter.TreatDoctListsPresenter;
import com.zhongmeban.treatmodle.presenter.TreatHospListsPresenter;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.genericity.TreatDocHospLevelGen;
import com.zhongmeban.view.PopDropDownMenu;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 医生列表Activity
 * Created by Chengbin He on 2016/12/2.
 */

public class TreatHospListsActicity extends BaseActivityToolBar implements SwipeRefreshLayout.OnRefreshListener,
        TreatHospListsContract.View {

    private Context mContext = TreatHospListsActicity.this;
    private TreatHospListsContract.Presenter presenter;
    private RecyclerView recyclerView;
    private boolean isScroll = true;
    private TreatHospListsAdapter mAdapter;
    private SwipeRefreshLayout refreshlayout;
    private LinearLayoutManager mLinearLayoutManager;
    private PopDropDownMenu popDropDownMenu;



    @Override
    protected int getLayoutId() {
        return R.layout.act_hosplists;
    }

    @Override
    protected void initToolbar() {
        initSearchToolBar("医院列表", new IToolbarListener() {
            @Override
            public void clickLeft() {
                Log.i("hcbtest","clickLeft");
                finish();
            }

            @Override
            public void clickRight() {
                Log.i("hcbtest","clickRight");
                Intent intent = new Intent(mContext,TreatCommonSeatchListActivity.class);
                intent.putExtra(TreatCommonSeatchListActivity.EXTRA_SEARCH_TYPE,
                        TreatCommonSeatchListActivity.SEARCH_HOSPITAL_TYPE);
                startActivity(intent);
            }

            @Override
            public void clickRightSecond() {
                Log.i("hcbtest","clickRightSecond");
            }
        });
    }


    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        popDropDownMenu = (PopDropDownMenu) findViewById(R.id.dropdownmenu);
        //主体部分
        initEmptyView();
        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeResources(R.color.app_green);
//        refreshlayout.post(new Runnable() {
//            @Override
//            public void run() {
//                refreshlayout.setRefreshing(true);
//            }
//        });
        refreshlayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);

        new TreatHospListsPresenter(this, mContext);
        presenter.start();
        initDropDownMenu();
    }

    /**
     * 初始化级联菜单
     */
    private void initDropDownMenu(){
        popDropDownMenu.setShowType(PopDropDownMenu.LEFT_RIGHT);


        popDropDownMenu.setmMenuRightList(presenter.getHospLevelList());
        presenter.getHttpProvinceList();
        popDropDownMenu.setMenuSelectedListener(new PopDropDownMenu.OnMenuSelectedListener() {
            @Override
            public void onSelected(View listview, int position, int type) {
                switch (type){
                    case PopDropDownMenu.LEFT_LEFT_CLICK:
                        //城市 省份被点击
                        if (position == 0){
                            //点击全部城市
                            presenter.resetHospCityCode();
                            popDropDownMenu.setLeftTitle("全部城市");
                            popDropDownMenu.dissMissLeftMenu();
                            presenter.setSelectAllCity(new CityListBean("","全部城市"));
                        }else {
                            presenter.getHttpCityList(position);
                        }

                        break;
                    case PopDropDownMenu.LEFT_RIGHT_CLICK:
                        //城市 市被点击
                        presenter.setHospCityCode(position);
                        break;
                    case PopDropDownMenu.RIGHT_CLICK:
                        //医院等级
                        presenter.setHospLevel(position);
                        break;
                }
            }
        });
    }


    @Override
    public void showProvince(List<DropDownBean> provinceList) {
        popDropDownMenu.setmMenuLeftList1(provinceList);
    }

    @Override
    public void showCity(List<DropDownBean> cityList) {
        popDropDownMenu.setmMenuLeftList2(cityList);
    }

    @Override
    public void setCityTitle(String title) {
        popDropDownMenu.setLeftTitle(title);
    }

    @Override
    public void setHospLevelTitle(String title) {
        popDropDownMenu.setRightTitle(title);
    }


    @Override
    public void showData(List<TreatHospitalScource> httpList) {
        if (mAdapter == null) {
            mAdapter = new TreatHospListsAdapter(mContext, httpList);
            mAdapter.setItemClickListener(new AdapterClickInterface() {

                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(mContext, ActHospDetail.class);
                    String hospLevel = mAdapter.getLevel(position);
                    intent.putExtra("hospLevel", hospLevel);
                    startActivity(presenter.startHospDetail(intent,position));

                }

                @Override
                public void onItemLongClick(View v, int position) {

                }
            });
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyData(httpList);
        }
    }



    @Override
    public void setPresenter(TreatHospListsContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void startRefresh() {
        refreshlayout.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        refreshlayout.setRefreshing(false);
    }

    @Override
    public void exitData() {
        recyclerView.setVisibility(View.VISIBLE);
        showEmptyLayoutState(BaseActivityToolBar.LOADING_STATE_SUCESS);
    }

    @Override
    public void noData() {
        recyclerView.setVisibility(View.GONE);
        showEmptyLayoutState(BaseActivityToolBar.LOADING_STATE_EMPTY);
    }

    @Override
    public void noNet() {
        recyclerView.setVisibility(View.GONE);
        showEmptyLayoutState(BaseActivityToolBar.LOADING_STATE_NO_NET);
    }

    @Override
    public void onClickReLoadData() {
        //没网点击事件重新刷新
        showEmptyLayoutState(LOADING_STATE_PEOGRESS);
        presenter.getHttpData();
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

    public void onPreLoadMore() {
        presenter.addLoadMore();
        mAdapter.notifyData(presenter.getHttpList());
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

    /**
     * 下拉刷新部分
     */
    @Override
    public void onRefresh() {
       presenter.resetIndex();
       presenter.getHttpData();
    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivTitleBtnback://back 图标
                    finish();
                    break;

            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("hcb", "ActHospLists onDestroy");
       presenter.destory();
    }


}

