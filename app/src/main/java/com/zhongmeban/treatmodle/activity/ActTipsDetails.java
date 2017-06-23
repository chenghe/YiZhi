package com.zhongmeban.treatmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityCardLogin;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.NewsDetail;
import com.zhongmeban.bean.TipsLists;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.CircularProgressBar.CircularProgressBar;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;
import java.util.Arrays;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:小贴士详情
 * time：2016/1/22 19:23
 */
public class ActTipsDetails extends BaseScrollActivity implements ObservableScrollViewCallbacks {

    public static final String EXTRA_TIP_BEAN = "EXTRA_TIP_BEAN";
    public static final String EXTRA_FROM_NEWS = "EXTRA_FROM_NEWS";
    public static final String EXTRA_NEWS_ID = "EXTRA_NEWS_ID";
    public static final String EXTRA_NEWS_HTML = "EXTRA_NEWS_HTML";

    private TextView mainTitle;
    private TextView title;
    private TextView content;
    private TextView time;
    private TextView address;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private RelativeLayout mTitle;
    private View mImageView;
    private ImageView mBackView;
    private ImageView mShare;
    private CircularProgressBar progressBar;
    private Activity mContext = ActTipsDetails.this;

    private String userId;
    private String token;

    private TipsLists.TipDataScourceItem mTipBean;
    private WebView mWebView;

    private boolean isFromNews;
    private String infoID;//小提示ID
    private String newsHtml;//小提示ID
    private boolean isFavorite;

    private float alpha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_tips);
        Intent intent = getIntent();
        mTipBean = (TipsLists.TipDataScourceItem) intent.getSerializableExtra(EXTRA_TIP_BEAN);
        infoID = intent.getStringExtra(EXTRA_NEWS_ID);
        newsHtml = intent.getStringExtra(EXTRA_NEWS_HTML);
        isFromNews = intent.getBooleanExtra(EXTRA_FROM_NEWS, false);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("token", "");

        initView();

/*        if (TextUtils.isEmpty(intent.getStringExtra("informationId"))) {
            //咨询详情
            String newsId = intent.getStringExtra("newsId");
            //getHttpDataFromeNews(newsId);
        } else {
            //小贴士详情
            infoID = intent.getStringExtra("informationId");
            //getHttpDataFromeTips();
        }*/

        mScrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }


    private void initView() {
        mainTitle = (TextView) findViewById(R.id.tv_maintitle);
        time = (TextView) findViewById(R.id.tv_time);
        address = (TextView) findViewById(R.id.tv_address);
        content = (TextView) findViewById(R.id.tv_content);
        mImageView = findViewById(R.id.rl_image);
        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);
        mWebView = (WebView) findViewById(R.id.id_news_webview);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);

        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(onClickListener);

        mTitle = (RelativeLayout) findViewById(R.id.ll_title);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.title_bg)));

        title = (TextView) findViewById(R.id.tv_title);
        title.setTextColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.top_text)));

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        if (isFromNews) {

            content.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            initWebView();

            //getHttpDataFromeTips();
            mWebView.loadUrl(newsHtml);

            mShare.setVisibility(View.VISIBLE);

            //String ss = "<div style=\"text-align: center;\"><b><i>测试information</i></b></div>";
            //content.setText(Html.fromHtml(ss));

        } else {
            content.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            mShare.setVisibility(View.GONE);

            address.setText(TextUtils.isEmpty(mTipBean.getWriter()) ? "" : mTipBean.getWriter());

            if (!TextUtils.isEmpty(mTipBean.getTitle())) {
                mainTitle.setText(mTipBean.getTitle());
                title.setText(mTipBean.getTitle());
            }
            if (!TextUtils.isEmpty(mTipBean.getContent())) {

                content.setText(mTipBean.getContent());
            }
            if (mTipBean.getTime() != null) {
                time.setText(TimeUtils.changeDateToString(mTipBean.getTime()));
            }
        }

    }


    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    onBackPressed();
                    break;
                case R.id.iv_share:
                    //创建分享Dialog
                    //                    ShareDialog.showShareDialog(mContext);
                    if (!TextUtils.isEmpty(userId)) {
                        //                        int tipID = Integer.parseInt(infoID);
                        TreatMentShareBean bean = new TreatMentShareBean(Integer.valueOf(infoID),
                            TreatMentShare.INFOMATION);
                        //ShareDialog.showShareDialog(mContext, bean, token);
                        if (isFavorite) {
                            Integer[] list = new Integer[] { Integer.valueOf(infoID) };
                            cancleFavoriteType(TreatMentShare.INFOMATION, Arrays.asList(list));
                        } else {
                            favoriteType(Integer.valueOf(infoID), TreatMentShare.INFOMATION);
                        }
                    } else {
                        startActivityForResult(
                            new Intent(ActTipsDetails.this, ActivityCardLogin.class), 6);
                    }
                    break;

            }
        }
    };


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            userId = sp.getString("userId", "");
            token = sp.getString("token", "");

        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, ActivityMyCollectionInfo.class);
        intent.putExtra("isFavorite", isFavorite);
        setResult(RESULT_OK, intent);
        finish();
    }


    public void favoriteType(int favoriteId, int type) {

        HttpService.getHttpService()
            .postCreateUserFavorite(new TreatMentShareBean(favoriteId, type), token)
            .compose(RxUtil.<CreateOrDeleteBean>normalSchedulers())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    ToastUtil.showText(ActTipsDetails.this, "收藏失败");
                }


                @Override public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean == null ||
                        Integer.valueOf(createOrDeleteBean.getErrorCode()) != 0) {
                        return;
                    }
                    ToastUtil.showText(ActTipsDetails.this, "已收藏");
                    isFavorite = true;

                    updateTopBarUI(isFavorite);
                }
            });
    }


    public void cancleFavoriteType(int type, List<Integer> list) {

        HttpService.getHttpService().postDeleteFavorite(new DeleteFavoriteBody(type, list), token)
            .compose(RxUtil.<CreateOrDeleteBean>normalSchedulers())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    ToastUtil.showText(ActTipsDetails.this, "取消失败");
                }


                @Override public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean == null ||
                        Integer.valueOf(createOrDeleteBean.getErrorCode()) != 0) {
                        return;
                    }
                    ToastUtil.showText(ActTipsDetails.this, "已取消");
                    isFavorite = false;

                    updateTopBarUI(isFavorite);
                }
            });
    }


    /**
     * 网络请求部分
     */
    public void getHttpDataFromeTips() {
        HttpService.getHttpService().getNewsDetail(infoID, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<NewsDetail>() {
                @Override
                public void onStart() {
                    super.onStart();
                }


                @Override
                public void onCompleted() {
                    Log.i("hcb", "onCompleted()");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 1000);

                }


                @Override
                public void onError(Throwable e) {
                }


                @Override
                public void onNext(NewsDetail newsDetail) {

                    if (newsDetail == null || newsDetail.getErrorCode() != 0) return;
                    isFavorite = newsDetail.getData().isIsFavorite();
                    mainTitle.setText(newsDetail.getData().getTitle());
                    content.setText(Html.fromHtml(newsDetail.getData().getContent()));
                    title.setText(newsDetail.getData().getTitle());
                    time.setText(TimeUtils.changeDateToString(newsDetail.getData().getTime()));
                    //mWebView.loadData(newsDetail.getData().getContent(), "text/html", "UTF-8");

                    mWebView.loadDataWithBaseURL(null,
                        newsDetail.getData().getContent(),
                        "text/html", "utf-8", null);
                    //mWebView.loadDataWithBaseURL(null,
                    //    getHtmlText(newsDetail.getData().getContent(),
                    //        DisplayUtil.sp2px(ActTipsDetails.this, 18)),
                    //    "text/html", "utf-8", null);
                    updateTopBarUI(isFavorite);
                    address.setText(TextUtils.isEmpty(newsDetail.getData().getWriter())
                                    ? ""
                                    : newsDetail.getData().getWriter());
                }
            });
    }


    public void initWebView() {
        //WebSettings settings = mWebView.getSettings();
        //settings.setJavaScriptEnabled(true);
        //settings.setBuiltInZoomControls(true);// 设置支持缩放
        //settings.setSupportZoom(false);// 不支持缩放
        //settings.setUseWideViewPort(false);// 将图片调整到适合webview大小
        //settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//支持缓存

        //下边的这句代码加上后会特别卡，并且有可能 显示的内容超出屏幕
        //mWebView.setInitialScale(100);//这里一定要设置，数值可以根据各人的需求而定，我这里设置的是50%的缩放

        WebSettings webSettings = mWebView.getSettings();
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(false);// support zoom
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        //webSettings.setTextZoom(300);
        //webSettings.setTextSize(WebSettings.TextSize.LARGER);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.toolbar_color);
        int baseTextColor = getResources().getColor(R.color.top_text);
        alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        Log.i("hcb", "alpha is" + alpha);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        title.setTextColor(ScrollUtils.getColorWithAlpha(alpha, baseTextColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);

        updateTopBarUI(isFavorite);

    }


    private void updateTopBarUI(boolean love) {
        if (!love) {
            if (alpha > 0.5f) {
                mBackView.setImageResource(R.drawable.back_grey);
                mShare.setImageResource(R.drawable.collect_grey);
            } else {
                mBackView.setImageResource(R.drawable.back_white);
                mShare.setImageResource(R.drawable.collect_white);
            }
        } else {
            mShare.setImageResource(R.drawable.collect_selected);
            mBackView.setImageResource(alpha > 0.5f?R.drawable.back_grey:R.drawable.back_white);
        }

    }


    private String getHtmlText(String content, int textSize) {
        String start = "<div style=\"font-size:" + textSize + "px;\">";

        String end = "</div>";

        Logger.v(start + content + end);
        return start + content + end;

    }


    @Override
    public void onDownMotionEvent() {

    }


    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

}
