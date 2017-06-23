package com.zhongmeban.treatmodle.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;

/**
 * 分享跳转Activity
 * Created by Chengbin He on 2016/12/15.
 */

public class TreatShareWebActivity extends BaseActivityToolBar {

    private WebView webView;
    private String url = "";

    @Override
    protected int getLayoutId() {
        return  R.layout.act_new_detail_treat;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("咨询详情","");
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView .getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);
    }
}
