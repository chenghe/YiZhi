package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.orhanobut.logger.Logger;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActInterpretationList;
import com.zhongmeban.adapter.FragHomeGridAdapter;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.bean.BannerTreatBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.JelwelBoxBean;
import com.zhongmeban.bean.NewsLists;
import com.zhongmeban.bean.treatbean.WebViewBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.activity.ActIndicators;
import com.zhongmeban.treatmodle.activity.ActInspection;
import com.zhongmeban.treatmodle.activity.ActKnowTumor;
import com.zhongmeban.treatmodle.activity.ActTips;
import com.zhongmeban.treatmodle.activity.TreatCancerTypeListActivity;
import com.zhongmeban.treatmodle.activity.TreatDoctListsActicity;
import com.zhongmeban.treatmodle.activity.TreatHospListsActicity;
import com.zhongmeban.treatmodle.activity.TreatMedicineTypeListActivity;
import com.zhongmeban.treatmodle.activity.TreatNewsDetailActivity;
import com.zhongmeban.treatmodle.adapter.HomeNewsAdapter;
import com.zhongmeban.utils.CircularProgressBar.CircularProgressBar;
import com.zhongmeban.utils.NetworkImageHolderView;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.UmengUtils;
import com.zhongmeban.utils.genericity.ImageUrl;
import com.zhongmeban.view.DividerGridItemDecoration;
import com.zhongmeban.view.ScrollLinearLayout;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:治疗首页
 * user: Administrator
 * time：2015/12/25 19:27
 */
public class FragHome extends BaseFragmentRefresh {

    private Context mContext;

    //    private FlashView flashView;
    private ArrayList<NewsLists.NewsItem> flashNews;
    private ArrayList<String> imageUrls;
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private List<String> networkImages = new ArrayList<String>();

    public String imageBaseUrl = ImageUrl.BaseImageUrl;
    private List<NewsLists.NewsItem> mDatasNews = new ArrayList<>();//新闻详情
    private List<BannerTreatBean> mDatasBanner = new ArrayList<>();//轮播图
    private RecyclerView recyclerView;
    private FragHomeGridAdapter adapter;
    private CircularProgressBar progressBar;
    private int newsCurrPage = 1;//新闻的当前页数
    private int newsTotalPage = 0;//新闻的总页数
    private TextView loadingMore;
    private ScrollLinearLayout externalView;//自定义扩展LinearLayout
    private HomeNewsAdapter newsListAdapter;
    private Subscription mSubscriptionBanner;
    private Subscription mSubscriptionNews;
    private ObservableScrollView scrollView;

    //////////////////////////
    private List<JelwelBoxBean> mDatas = new ArrayList<>();
    private int mNewsIndex = 1;
    private int mNewsPageTotal = 1;
    private int mNewsSize = 20;
    private boolean isLoading;


    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override protected int getLayoutIdRefresh() {
        return R.layout.frag_home;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("首页", "");
        //有标题，但是不显示
        mToolbar.setVisibility(View.GONE);
    }


    @Override protected void initOnCreateView() {
        initDatas();
    }


    private void initConvenientBanner() {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages)
                .setPageIndicator(
                        new int[] { R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused })
                .setOnItemClickListener(onItemClickListener);
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);

        scrollView = (ObservableScrollView) view.findViewById(R.id.scrollview);

        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
        scrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                if (scrollView.getChildAt(0).getMeasuredHeight() <=
                        scrollY + view.getHeight()) {//判断是否滑动到底部
                    if (mNewsIndex > mNewsPageTotal) {
                        ToastUtil.showText(mContext, "没有更多信息");
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    if (isLoading) return;
                    getNewsData(mNewsIndex, mNewsSize);
                }

            }


            @Override
            public void onDownMotionEvent() {

            }


            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

            }
        });

        scrollView.post(new Runnable() {
            @Override public void run() {
                //scrollView.smoothScrollTo(0,0);
                scrollView.scrollTo(0, 0);
            }
        });

        //百宝箱
        recyclerView = (RecyclerView) view.findViewById(R.id.grid_recyclerview);
        adapter = new FragHomeGridAdapter(mContext, mDatas);
        adapter.setItemClickListener(adapterListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
            @Override public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        recyclerView.setAdapter(adapter);
        //新闻资讯
        externalView = (ScrollLinearLayout) view.findViewById(R.id.external_linearlayout);

        newsListAdapter = new HomeNewsAdapter(mContext, mDatasNews);
        newsListAdapter.setAdapterClickListenter(newsListItemClickListenter);
        externalView.setAdapter(newsListAdapter);

        getHttpDataBanner(1, 6);
        //recyclerView.postDelayed(new Runnable() {
        //    @Override public void run() {
        //        getNewsData(mNewsIndex,mNewsSize);
        //    }
        //},3000);
        //

    }


    //Recycleradapter 点击事件接口
    AdapterClickInterface adapterListener = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(mContext, mDatas.get(position).getClazz());
            startActivity(intent);

            UmengUtils.JelweBox.clickJelweBox(getActivity(), mDatas.get(position).getName());
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    //轮播图点击事件监听
    com.bigkoo.convenientbanner.listener.OnItemClickListener onItemClickListener
            = new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
//            Intent intent = new Intent(mContext, ActTipsDetails.class);
//            String newsId = mDatasBanner.get(position).getContentId() + "";
//            String newsHtml = mDatasBanner.get(position).getUrlAddress();
//            intent.putExtra(ActTipsDetails.EXTRA_NEWS_ID, newsId);
//            intent.putExtra(ActTipsDetails.EXTRA_NEWS_HTML, newsHtml);
//            intent.putExtra(ActTipsDetails.EXTRA_FROM_NEWS, true);
//            startActivity(intent);
            Intent intent = new Intent(mContext,TreatNewsDetailActivity.class);
            String newsHtml = mDatasBanner.get(position).getUrlAddress();
            String title = mDatasBanner.get(position).getTitle();
            String newsId = mDatasBanner.get(position).getId()+"";

            WebViewBean webViewBean = new WebViewBean(newsHtml,title,newsId,TreatNewsDetailActivity.FROM_INFO);
            intent.putExtra(TreatNewsDetailActivity.WEBVIEWBEAN,webViewBean);

            startActivity(intent);
        }
    };

    //底部新闻列表点击事件监听
    AdapterClickInterface newsListItemClickListenter = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {

            Intent intent = new Intent(mContext,TreatNewsDetailActivity.class);
            String newsHtml = mDatasNews.get(position).getUrlAddress();
            String title = mDatasNews.get(position).getTitle();
            String newsId = mDatasNews.get(position).getId();

            WebViewBean webViewBean = new WebViewBean(newsHtml,title,newsId,TreatNewsDetailActivity.FROM_INFO);
            intent.putExtra(TreatNewsDetailActivity.WEBVIEWBEAN,webViewBean);

            startActivity(intent);
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };


    /**
     * 网络请求获取数据轮播图 数据
     */
    private void getHttpDataBanner(int index, int pageSize) {
        mSubscriptionBanner = HttpService.getHttpService().getNewsList(index, pageSize)
                //.compose(RxUtil.<HttpResult<BannerNewsBean>>normalSchedulers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<List<BannerTreatBean>>>() {
                    @Override
                    public void onCompleted() {
                        initConvenientBanner();
                        // 防止 并行请求数据冲突
                        getNewsData(mNewsIndex, mNewsSize);
                    }


                    @Override
                    public void onError(Throwable e) {
                        Logger.e("banner---" + e.toString());
                        getNewsData(mNewsIndex, mNewsSize);
                    }


                    @Override
                    public void onNext(HttpResult<List<BannerTreatBean>> httpResult) {
                        Logger.e("banner---" + httpResult.toString());

                        if (httpResult == null && httpResult.getData() == null) return;

                        mDatasBanner = httpResult.getData();
                        for (int i = 0; i < mDatasBanner.size(); i++) {
                            String url = imageBaseUrl + mDatasBanner.get(i).getPicture();
                            networkImages.add(url);
                        }
                    }
                });
    }


    /**
     * 网络请求获取数据
     */
    private void getNewsData(int index, int pageSize) {
        isLoading = true;
        mSubscriptionNews = HttpService.getHttpService().getNewsListBottom(index, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsLists>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                        progressBar.setVisibility(View.GONE);
                        isLoading = false;
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()");
                        progressBar.setVisibility(View.GONE);
                        isLoading = false;
                    }


                    @Override
                    public void onNext(NewsLists newsLists) {

                        Logger.v("news---" + newsLists.toString());
                        if (newsLists == null && newsLists.getData() == null) return;

                        mDatasNews.addAll(newsLists.getData().getSourceItems());
                        newsListAdapter.notifyDataSetChanged();
                        externalView.bindLinearLayout();

                        mNewsPageTotal = newsLists.getData().getTotalPage();
                        mNewsIndex = newsLists.getData().getIndexPage() + 1;

                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(3000);
    }


    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptionNews != null) mSubscriptionNews.unsubscribe();
        if (mSubscriptionBanner != null) mSubscriptionBanner.unsubscribe();
    }


    /**
     * 百宝箱的数据信息
     */
    private void initDatas() {
        mDatas.clear();
        mDatas.add(new JelwelBoxBean(R.drawable.know, "认识肿瘤", ActKnowTumor.class));
        mDatas.add(new JelwelBoxBean(R.drawable.lab, "检查项目", ActInspection.class));
        mDatas.add(new JelwelBoxBean(R.drawable.cure, "治疗方式", TreatCancerTypeListActivity.class));
        //mDatas.add(new JelwelBoxBean(R.drawable.cure, "治疗方式", ActTreatment.class));
        mDatas.add(new JelwelBoxBean(R.drawable.index, "指标解读", ActIndicators.class));
        mDatas.add(new JelwelBoxBean(R.drawable.book, "名词解释", ActInterpretationList.class));
        mDatas.add(new JelwelBoxBean(R.drawable.tips_icon, "小贴士", ActTips.class));
        mDatas.add(new JelwelBoxBean(R.drawable.hospital_icon, "医院查询", TreatHospListsActicity.class));
        mDatas.add(new JelwelBoxBean(R.drawable.doctor_icon, "医生查询", TreatDoctListsActicity.class));
        //mDatas.add(new JelwelBoxBean(R.drawable.pil, "药品查询", ActMedicineChest.class));
        mDatas.add(new JelwelBoxBean(R.drawable.pil, "药品查询", TreatMedicineTypeListActivity.class));

    }


    @Override public void clickLeft() {

    }


    @Override public void clickRight() {

    }
}
