package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityHospSelect;
import com.zhongmeban.attentionmodle.adapter.AttentionSimpleDoctorAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.SimpleDoctorPage;
import com.zhongmeban.bean.postbody.SimpleDoctorPageSourceItems;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhongmeban.R.id.refreshlayout;

/**
 * 关注模块 医生筛选Activity
 * Created by Chengbin He on 2016/8/10.
 */
public class ActivityAttentionSimpleDoctor extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private Subscription subscription;
    private AttentionSimpleDoctorAdapter mAdapter;
    private List<SimpleDoctorPageSourceItems> httpList = new ArrayList<SimpleDoctorPageSourceItems>();
    private int pageIndex;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView recyclerView;
    private boolean isScroll = true;
    private SimpleDoctorPageSourceItems load = new SimpleDoctorPageSourceItems();
    private Activity mContext = ActivityAttentionSimpleDoctor.this;

    private RelativeLayout rlSelect;//筛选布局
    private TextView tvSelectContent;//筛选内容
    private ImageView ivCancel;//取消筛选
    private EditText searchBox;//搜索框
    private ImageView clearBtn;//清空搜索
    private String level = "";
    private String levelName = "";
    private String cityName = "";
    private String cityId = "";
    private String cancerName = "";
    private String diseaseId ="";
    private String doctLevelName = "";
    private String doctLevel = "";
    private String operationLevelName = "";
    private String operationLevel = "0";
    private String doctName = "";
    private int totalPage;//总页数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_simple_doc_hosp);
        pageIndex = 1;
        load.setViewType(0);
        initTitle();
        initView();
        getHttpData();
    }

    private void initView() {
        //主体内容部分
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(refreshlayout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(refreshlayout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(refreshlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_green);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);
        mAdapter = new AttentionSimpleDoctorAdapter(mContext, httpList);
        mAdapter.setItemClickListener(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("attentionDoctorName",httpList.get(position).getDoctorName());
                intent.putExtra("attentionDoctorId",httpList.get(position).getDoctorId());
                Log.i("hcb","httpList.get(position).getDoctorId()" +httpList.get(position).getDoctorId());
                setResult(300,intent);
                finish();
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        recyclerView.setAdapter(mAdapter);

        //搜索部分
        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(textWatcher);

        //筛选部分
        rlSelect = (RelativeLayout) findViewById(R.id.rl_select);
        tvSelectContent = (TextView) findViewById(R.id.tv_select_content);
        ivCancel = (ImageView) findViewById(R.id.iv_select_cancel);
        ivCancel.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("请选择手术医生");
        title.slideRighttext("筛选", this);
        title.backSlid(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button: //title右侧Button
                Intent intent = new Intent(mContext,ActivityHospSelect.class);
                intent.putExtra("levelName",levelName);
                intent.putExtra("cityName",cityName);
                intent.putExtra("cancerName",cancerName);
                intent.putExtra("doctLevelName",doctLevelName);
                intent.putExtra("operationLevelName",operationLevelName);
                intent.putExtra("doctselect",true);
                startActivityForResult(intent,1);
                break;

            case R.id.ivTitleBtnback://back 图标
                finish();
                break;
            case R.id.iv_select_cancel://取消筛选
                level = "0";
                levelName = "";
                cityName = "";
                cityId = "0";
                cancerName = "";
                diseaseId ="0";
                doctLevelName = "";
                doctLevel = "0";
                operationLevelName = "";
                operationLevel = "0";
                pageIndex = 1;
                rlSelect.setVisibility(View.GONE);
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                getHttpData();
                break;
            case R.id.iv_search_clear://清除搜索内容
                searchBox.setText("");
                doctName = "";
                clearBtn.setVisibility(View.GONE);
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                pageIndex = 1;
                getHttpData();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            level = data.getStringExtra("level");
            cityId = data.getStringExtra("cityId");
            levelName = data.getStringExtra("levelName");
            cityName = data.getStringExtra("cityName");
            cancerName = data.getStringExtra("cancerName");
            diseaseId = data.getStringExtra("diseaseId");
            doctLevel = data.getStringExtra("doctLevel");
            doctLevelName = data.getStringExtra("doctLevelName");
            operationLevel = data.getStringExtra("operationLevel");
            operationLevelName = data.getStringExtra("operationLevelName");

            rlSelect.setVisibility(View.VISIBLE);
            tvSelectContent.setText(cityName + " " + levelName + " " + cancerName + " " + doctLevelName + " " + operationLevelName);

            pageIndex = 1;
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            getHttpData();
        }
    }

    /**
     * 获取网络数据
     */
    private void getHttpData() {
        subscription = HttpService.getHttpService().getSimpleDoctorPage(doctName,diseaseId,cityId,"",
                level,doctLevel,"0",pageIndex,20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SimpleDoctorPage>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","ActivityAttentionSimpleDoctor onCompleted");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","ActivityAttentionSimpleDoctor onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(SimpleDoctorPage simpleDoctorPage) {
                        Log.i("hcb","ActivityAttentionSimpleDoctor onNext");
                        totalPage = simpleDoctorPage.getData().getTotalPage();
                        if (simpleDoctorPage.getData().getTotalCount() > 0) {
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
//                                        tv_noentity.setVisibility(View.GONE);
                            if (pageIndex == 1) {
                                httpList.clear();
                                httpList = simpleDoctorPage.getData().getSourceItems();
                            } else {
                                onPostLoadMore();
                                httpList.addAll(simpleDoctorPage.getData().getSourceItems());
                            }
                            mAdapter.notifyData(httpList);
                        } else {
                            swipeRefreshLayout.setVisibility(View.GONE);
//                                        tv_noentity.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 搜索框，搜素内容监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            doctName = s.toString();
            if (TextUtils.isEmpty(doctName)){
                clearBtn.setVisibility(View.GONE);
            }else{
                clearBtn.setVisibility(View.VISIBLE);

            }
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            pageIndex = 1;
            getHttpData();
        }
    };
    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        pageIndex = 1;
        getHttpData();
    }

    /**
     * 上拉刷新部分
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        boolean isScrolling = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);

            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScroll) {
                int lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                if (lastVisibleItem == (totalItemCount - 1)&&totalPage>pageIndex) {
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
        onPreLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageIndex++;
//                body.setPageIndex(pageIndex);
                getHttpData();
            }
        }, 1000);

    }
}
