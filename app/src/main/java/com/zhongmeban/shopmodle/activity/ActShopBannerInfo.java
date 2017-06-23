package com.zhongmeban.shopmodle.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.shop.PromotionBean;

/**
 * Created by User on 2016/10/9.
 */

public class ActShopBannerInfo extends BaseActivityToolBar implements View.OnClickListener {


    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String urlTest = "http://www.tttpeng.com/zmb/activity1";

    private PromotionBean mPromotionBean;

    public static final String EXTRA_SHOP_BANNER = "extra_shop_banner";

    @Override protected int getLayoutId() {
        return R.layout.act_shop_banner;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("轮播图","");
    }


    @Override protected void initView() {

        mPromotionBean  = (PromotionBean) getIntent().getSerializableExtra(EXTRA_SHOP_BANNER);

        mWebView = (WebView) findViewById(R.id.id_frag_product_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.id_frag_product_progress);

        initData();
    }

    @Override public void onClick(View v) {

    }


    private void initData() {
        initWebView();
        //mWebView.loadUrl(urlTest);

        mWebView.loadDataWithBaseURL(null,mPromotionBean.getIntroduction(), "text/html",  "utf-8", null);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("zmb")) {
                    startActivity(new Intent(ActShopBannerInfo.this, ActShoppingCart.class));
                } else if (url.contains("pgyer")) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    Logger.e("打开的地址----" + url);
                } else {
                    view.loadUrl(url);
                }
                Logger.d("打开的地址----" + url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    //mProgressBar.setVisibility(View.VISIBLE);
                    //mProgressBar.setProgress(newProgress);
                } else {
                    //mProgressBar.setVisibility(View.GONE);
                }
                Logger.i("当前进度----" + newProgress);

                super.onProgressChanged(view, newProgress);
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


        mWebView.setInitialScale(100);//这里一定要设置，数值可以根据各人的需求而定，我这里设置的是50%的缩放

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(false);// support zoom
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);

    }


    @Override public void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
    }
}