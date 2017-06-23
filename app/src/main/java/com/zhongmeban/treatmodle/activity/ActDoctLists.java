//package com.zhongmeban.treatmodle.activity;
//
//import android.content.Context;
//import android.content.Intent;
//
//import android.os.Bundle;
//
//import android.os.Handler;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.orhanobut.logger.Logger;
//import com.zhongmeban.AdapterClickInterface;
//import com.zhongmeban.R;
//import com.zhongmeban.activity.ActivityHospSelect;
//import com.zhongmeban.treatmodle.adapter.DoctListsAdapter;
//import com.zhongmeban.base.BaseActivity;
//
//import com.zhongmeban.bean.Doctor;
//
//import com.zhongmeban.bean.DoctorList;
//
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
//
///**
// * Description:医生列表页面
// * Created by Chengbin He on 2016/3/30.
// */
//public class ActDoctLists extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
//
//    private Context mContext = ActDoctLists.this;
//    private RecyclerView recyclerView;
//    private DoctListsAdapter mAdapter;
////    private DoctorBody body;`
//    private LinearLayout tv_noentity;
//    private SwipeRefreshLayout refreshlayout;
//    private int pageIndex;
//    private Doctor load = new Doctor();
//    private LinearLayoutManager mLinearLayoutManager;
//    private boolean isScroll = true;
//    private List<Doctor> httpList = new ArrayList<Doctor>();
//    private Subscription mSubscription;
//    private RelativeLayout rlSelect;//筛选布局
//    private TextView tvSelectContent;//筛选内容
//    private ImageView ivCancel;//取消筛选
//    private EditText searchBox;//搜索框
//    private ImageView clearBtn;//清空搜索
//    private String level = "";
//    private String levelName = "";
//    private String cityName = "";
//    private String cityId = "";
//    private String cancerName = "";
//    private String diseaseId ="";
//    private String doctLevelName = "";
//    private String doctLevel = "";
//    private String operationLevelName = "";
//    private String operationLevel = "0";
//    private String doctName = "";
//    private int totalPage;//总页数
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_hosplists);
//        pageIndex = 1;
////        body = new DoctorBody();
////        body.setPageIndex(pageIndex);
////        body.setPageSize(10);
//
//        load.setViewType(0);
//        initTitle();
//        initView();
//
//        getHttpData();
//
//    }
//
//
//    private void initView() {
//
//        //搜索部分
//        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
//        clearBtn.setOnClickListener(onClickListener);
//        searchBox = (EditText) findViewById(R.id.et_search);
//        searchBox.addTextChangedListener(textWatcher);
//
//        //筛选部分
//        rlSelect = (RelativeLayout) findViewById(R.id.rl_select);
//        tvSelectContent = (TextView) findViewById(R.id.tv_select_content);
//        ivCancel = (ImageView) findViewById(R.id.iv_select_cancel);
//        ivCancel.setOnClickListener(onClickListener);
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
//        mAdapter = new DoctListsAdapter(mContext, httpList);
//        mAdapter.setItemClickListener(adapterListener);
//        recyclerView.setAdapter(mAdapter);
//    }
//
//    //Item 点击事件
//    AdapterClickInterface adapterListener = new AdapterClickInterface() {
//        @Override
//        public void onItemClick(View v, int position) {
//            Intent intent = new Intent(mContext, ActDoctDetail.class);
//
//            String doctName = httpList.get(position).getDoctorName();
//            String dooctLevel = mAdapter.getDoctLevel(position);
//            String url = mAdapter.getIcon(position);
//            String hospName = httpList.get(position).getHospitalName();
//            String doctorId = httpList.get(position).getDoctorId()+"";
//
//            intent.putExtra("doctName",doctName);
//            intent.putExtra("dooctLevel",dooctLevel);
//            intent.putExtra("url",url);
//            intent.putExtra("hospName",hospName);
//            intent.putExtra("doctorId",doctorId);
//
//            startActivity(intent);
//        }
//
//        @Override
//        public void onItemLongClick(View v, int position) {
//
//        }
//    };
//
//    View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.right_button: //title右侧Button
//                    Intent intent = new Intent(mContext,ActivityHospSelect.class);
//                    intent.putExtra("levelName",levelName);
//                    intent.putExtra("cityName",cityName);
//                    intent.putExtra("cancerName",cancerName);
//                    intent.putExtra("doctLevelName",doctLevelName);
//                    intent.putExtra("operationLevelName",operationLevelName);
//                    intent.putExtra("doctselect",true);
//                    startActivityForResult(intent,1);
//                    break;
//
//                case R.id.ivTitleBtnback://back 图标
//                    finish();
//                    break;
//                case R.id.iv_select_cancel://取消筛选
//                    level = "0";
//                    levelName = "";
//                    cityName = "";
//                    cityId = "0";
//                    cancerName = "";
//                    diseaseId ="0";
//                    doctLevelName = "";
//                    doctLevel = "0";
//                    operationLevelName = "";
//                    operationLevel = "0";
//
//                    pageIndex = 1;
//
////                    body.setPageIndex(pageIndex);
////                    body.setHospitalLevel(level);
////                    body.setCityId(cityId);
////                    body.setDiseaseId(diseaseId);
////                    body.setDoctorTitle(doctLevel);
////                    body.setIsOperation(operationLevel);
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
//                    doctName = "";
//                    clearBtn.setVisibility(View.GONE);
//                    refreshlayout.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            refreshlayout.setRefreshing(true);
//                        }
//                    });
//                    pageIndex = 1;
////                    body.setPageIndex(pageIndex);
////                    body.setName(doctName);
//                    getHttpData();
//                    break;
//            }
//        }
//    };
//
//    protected void initTitle() {
//        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
//        title.slideCentertext("医生列表");
//        title.slideRighttext("筛选", onClickListener);
//        title.backSlid(onClickListener);
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
//            doctName = s.toString();
//            if (TextUtils.isEmpty(doctName)){
//                clearBtn.setVisibility(View.GONE);
//            }else{
//                clearBtn.setVisibility(View.VISIBLE);
//            }
//            refreshlayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    refreshlayout.setRefreshing(true);
//                }
//            });
//            pageIndex = 1;
////            body.setPageIndex(pageIndex);
////            body.setName(doctName);
//            getHttpData();
//        }
//    };
//    /**
//     * 上拉刷新部分
//     */
//    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
//        Boolean isScrolling = false;
//
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//
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
//    };
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
//                pageIndex++;
////                body.setPageIndex(pageIndex);
//                getHttpData();
//            }
//        }, 1000);
//
//    }
//
//    /**
//     * 下拉刷新
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
//        if (TextUtils.isEmpty(operationLevel)){
//            operationLevel = "0";
//        }
//         mSubscription = HttpService.getHttpService().getDoctorList(doctName,diseaseId,cityId,"",
//                level,doctLevel,operationLevel,pageIndex,20)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DoctorList>() {
//                    @Override
//                    public void onCompleted() {
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
//
//                        Logger.e("医生--"+e.toString());
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshlayout.setRefreshing(false);
//                            }
//                        }, 2000);
//                        Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
//                        recyclerView.setVisibility(View.GONE);
//                        tv_noentity.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onNext(DoctorList doctorList) {
//                        Logger.v("医生--"+doctorList.toString());
//                        totalPage = doctorList.getData().getTotalPage();
//                        if (doctorList.getData().getSourceItems().size() > 0) {
//                            refreshlayout.setVisibility(View.VISIBLE);
//                            tv_noentity.setVisibility(View.GONE);
//                            if (pageIndex == 1) {
//                                httpList.clear();
//                                httpList = doctorList.getData().getSourceItems();
//                                Log.i("hcb", "httpList.size()" + httpList.size());
//                            } else {
//                                onPostLoadMore();
//
//                                httpList.addAll(doctorList.getData().getSourceItems());
//                            }
//                            mAdapter.notifyData(httpList);
//                        }else{
//                            refreshlayout.setVisibility(View.GONE);
//                            tv_noentity.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == 200){
//            level = data.getStringExtra("level");
//            cityId = data.getStringExtra("cityId");
//            levelName = data.getStringExtra("levelName");
//            cityName = data.getStringExtra("cityName");
//            cancerName = data.getStringExtra("cancerName");
//            diseaseId = data.getStringExtra("diseaseId");
//            doctLevel = data.getStringExtra("doctLevel");
//            doctLevelName = data.getStringExtra("doctLevelName");
//            operationLevel = data.getStringExtra("operationLevel");
//            operationLevelName = data.getStringExtra("operationLevelName");
//
//            rlSelect.setVisibility(View.VISIBLE);
//            tvSelectContent.setText(cityName+" "+levelName+" "+cancerName+" "+doctLevelName+" "+operationLevelName);
//
//            pageIndex = 1;
////            body.setPageIndex(pageIndex);
////            body.setHospitalLevel(level);
////            body.setCityId(cityId);
////            body.setDiseaseId(diseaseId);
////            body.setDoctorTitle(doctLevel);
////            body.setIsOperation(operationLevel);
//            refreshlayout.post(new Runnable() {
//                @Override
//                public void run() {
//                    refreshlayout.setRefreshing(true);
//                }
//            });
//            getHttpData();
//
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.i("hcb","ActDoctLists onDestroy");
//        if (mSubscription != null) mSubscription.unsubscribe();
//    }
//}
