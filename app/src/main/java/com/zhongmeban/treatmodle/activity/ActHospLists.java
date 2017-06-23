//package com.zhongmeban.treatmodle.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.zhongmeban.AdapterClickInterface;
//import com.zhongmeban.R;
//import com.zhongmeban.activity.ActivityHospSelect;
//import com.zhongmeban.adapter.HospListsAdapter;
//import com.zhongmeban.base.BaseActivity;
//import com.zhongmeban.bean.HospitalLists;
//import com.zhongmeban.bean.HospitalScource;
//import com.zhongmeban.net.HttpService;
//import com.zhongmeban.utils.LayoutActivityTitle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import rx.Subscriber;
//import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Description:医院列表Activity
// * Created by Chengbin He on 2016/3/30.
// */
//public class ActHospLists extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
//
//    private Context mContext = ActHospLists.this;
//    private int pageIndex;
////    private HospitalBody body;
//    private LinearLayout tv_noentity;
//    private EditText searchBox;//搜索框
//    private ImageView clearBtn;//清空搜索
//    private RecyclerView recyclerView;
//    private boolean isScroll = true;
//    private HospListsAdapter mAdapter;
//    private SwipeRefreshLayout refreshlayout;
//    private LinearLayoutManager mLinearLayoutManager;
//    private  HospitalScource load = new HospitalScource();
//    private List<HospitalScource> httpList = new ArrayList<HospitalScource>();
//    private Subscription mSubscription;
//    private RelativeLayout rlSelect;//筛选布局
//    private TextView tvSelectContent;//筛选内容
//    private ImageView ivCancel;//取消筛选
//    private String levelName  = "";
//    private String cityName = "";
//    private String level = "";
//    private String cityId = "";
//    private String hospName = "";
//    private int totalPage;//总页数
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_hosplists);
//        pageIndex = 1;
////        body = new HospitalBody();
////        body.setPageIndex(pageIndex);
////        body.setPageSize(10);
//        load.setViewType(0);
//        initTitle();
//        initView();
//
//        getHttpData();
//    }
//
//    /**
//     * 初始化布局
//     */
//
//    private void initView() {
//
//        //筛选部分
//        rlSelect = (RelativeLayout) findViewById(R.id.rl_select);
//        tvSelectContent = (TextView) findViewById(R.id.tv_select_content);
//        ivCancel = (ImageView) findViewById(R.id.iv_select_cancel);
//        ivCancel.setOnClickListener(onClickListener);
//
//        //搜索部分
//        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
//        clearBtn.setOnClickListener(onClickListener);
//        searchBox = (EditText) findViewById(R.id.et_search);
//        searchBox.addTextChangedListener(textWatcher);
//
//        //主体部分
//        tv_noentity = (LinearLayout) findViewById(R.id.empty_view);
//        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
//        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
//        refreshlayout.setColorSchemeResources(R.color.app_green);
//        refreshlayout.post(new Runnable() {
//            @Override
//            public void run() {
//                refreshlayout.setRefreshing(true);
//            }
//        });
//        refreshlayout.setOnRefreshListener(this);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mLinearLayoutManager = new LinearLayoutManager(mContext);
//        recyclerView.setLayoutManager(mLinearLayoutManager);
//        recyclerView.addOnScrollListener(onScrollListener);
//        mAdapter = new HospListsAdapter(mContext, httpList);
//        //为Item设置点击事件
//        mAdapter.setItemClickListener(new AdapterClickInterface() {
//            @Override
//            public void onItemClick(View v, int position) {
//                String hospName = httpList.get(position).getHospitalName();
//                String hospAddress = httpList.get(position).getAddress();
//                String hospitalId = httpList.get(position).getHospitalId()+"";
//                String hospLevel = mAdapter.getLevel(position);
//                if(!TextUtils.isEmpty(hospName)){
//                    Intent intent = new Intent(mContext, ActHospDetail.class);
//                    intent.putExtra("hospName",hospName);
//                    intent.putExtra("hospLevel",hospLevel);
//                    intent.putExtra("hospAddress",hospAddress);
//                    intent.putExtra("hospitalId",hospitalId);
//                    Log.i("hcb","hospName is " +hospName);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onItemLongClick(View v, int position) {
//
//            }
//        });
//        recyclerView.setAdapter(mAdapter);
//    }
//
//
//    /**
//     * 初始化Title
//     */
//    @Override
//    protected void initTitle() {
//        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
//        title.slideCentertext("医院列表");
//        title.slideRighttext("筛选", onClickListener);
//        title.backSlid(onClickListener);
//    }
//
//    /**
//     * 上拉刷新部分
//     */
//    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//        Boolean isScrolling = false;
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            InputMethodManager imm = (InputMethodManager) mContext
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(recyclerView.getWindowToken(),0);
//
//            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScroll) {
//                int lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                int totalItemCount = mLinearLayoutManager.getItemCount();
//                if (lastVisibleItem == (totalItemCount - 1)&&totalPage>pageIndex) {
//                    LoadMore();
//                    isScroll = false;
//                }
//            }
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//
//            if (dy > 0) {
//                isScrolling = true;
//            } else {
//                isScrolling = false;
//            }
//        }
//     };
//
//    public void onPreLoadMore() {
//        httpList.add(load);
//        mAdapter.notifyData(httpList);
//    }
//
//    public void onPostLoadMore() {
//        httpList.remove(load);
//        isScroll = true;
//    }
//
//    public void LoadMore() {
//        onPreLoadMore();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                pageIndex ++;
////                body.setPageIndex(pageIndex);
//                getHttpData();
//            }
//        }, 1000);
//
//    }
//
//    /**
//     * 搜索框，搜素内容监听
//     */
//    TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            hospName = s.toString();
//            if (TextUtils.isEmpty(hospName)){
//                clearBtn.setVisibility(View.GONE);
////                refreshlayout.setVisibility(View.GONE);
////                tv_noentity.setVisibility(View.VISIBLE);
//            }else{
//                clearBtn.setVisibility(View.VISIBLE);
////                refreshlayout.setVisibility(View.VISIBLE);
////                tv_noentity.setVisibility(View.GONE);
//            }
//            refreshlayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    refreshlayout.setRefreshing(true);
//                }
//            });
//            pageIndex = 1;
////            body.setPageIndex(pageIndex);
////            body.setName(hospName);
//            getHttpData();
//        }
//    };
//
//    /**
//     * 下拉刷新部分z
//     */
//    @Override
//    public void onRefresh() {
//        pageIndex = 1;
////        body.setPageIndex(pageIndex);
//        getHttpData();
//    }
//
//    /**
//     * 网络请求获取数据
//     */
//    private void getHttpData() {
//        mSubscription = HttpService.getHttpService().getHospitalList(cityId,level,hospName,pageIndex,20)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<HospitalLists>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.i("hcb", "http onCompleted()");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshlayout.setRefreshing(false);
//                            }
//                        }, 1000);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("hcb", "http onError()");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshlayout.setRefreshing(false);
//                            }
//                        }, 2000);
//                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
//                        refreshlayout.setVisibility(View.GONE);
//                        tv_noentity.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onNext(HospitalLists hospitalLists) {
//                        Log.i("hcb", "http onNext()");
//                        totalPage = hospitalLists.getData().getTotalPage();
//                        if (hospitalLists.getData().getTotalCount()>0){
//                            refreshlayout.setVisibility(View.VISIBLE);
//                            tv_noentity.setVisibility(View.GONE);
//                            if (pageIndex == 1){
//                                httpList.clear();
//                                httpList = hospitalLists.getData().getSourceItems();
//                            }else{
//                                onPostLoadMore();
//                                httpList.addAll(hospitalLists.getData().getSourceItems());
//                            }
//                            mAdapter.notifyData(httpList);
//                        }else{
//                            refreshlayout.setVisibility(View.GONE);
//                            tv_noentity.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//                });
//
//
//    }
//
//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.right_button: //title右侧Button
//                    Intent intent = new Intent(mContext,ActivityHospSelect.class);
//                    intent.putExtra("levelName",levelName);
//                    intent.putExtra("cityName",cityName);
//                    intent.putExtra("doctselect",false);
//                    startActivityForResult(intent,1);
//                    break;
//
//                case R.id.ivTitleBtnback://back 图标
//                    finish();
//                    break;
//                case R.id.iv_select_cancel://取消筛选
//                    level = "";
//                    cityId = "";
//                    levelName = "";
//                    cityName = "";
//                    pageIndex = 1;
////                    body.setPageIndex(pageIndex);
////                    body.setLevel(level);
////                    body.setCityId(cityId);
//                    rlSelect.setVisibility(View.GONE);
//                    refreshlayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            refreshlayout.setRefreshing(true);
//                        }
//                    });
//                    getHttpData();
//                    break;
//
//                case R.id.iv_search_clear://清除搜索内容
//                    searchBox.setText("");
//                    hospName = "";
//                    clearBtn.setVisibility(View.GONE);
//                    refreshlayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            refreshlayout.setRefreshing(true);
//                        }
//                    });
//                    pageIndex = 1;
////                    body.setPageIndex(pageIndex);
////                    body.setName(hospName);
//                    getHttpData();
//                    break;
//            }
//
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == 100){
//             level = data.getStringExtra("level");
//             cityId = data.getStringExtra("cityId");
//            levelName = data.getStringExtra("levelName");
//            cityName = data.getStringExtra("cityName");
//            rlSelect.setVisibility(View.VISIBLE);
//            tvSelectContent.setText(cityName+" "+levelName);
//
//            pageIndex = 1;
////            body.setPageIndex(pageIndex);
////            body.setLevel(level);
////            body.setCityId(cityId);
//            refreshlayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    refreshlayout.setRefreshing(true);
//                }
//            });
//            getHttpData();
//
//        }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.i("hcb","ActHospLists onDestroy");
//        if (mSubscription != null) mSubscription.unsubscribe();
//    }
//}
