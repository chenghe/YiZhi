package com.zhongmeban.treatmodle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.socialize.UMShareAPI;
import com.zhongmeban.Interface.IToolbarListener;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.bean.treatbean.UShareBean;
import com.zhongmeban.bean.treatbean.WebViewBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.utils.genericity.TreatMentShare;
import com.zhongmeban.view.ShareDialog;

import rx.Subscriber;

/**
 * 新闻详情Activity H5
 * Created by Chengbin He on 2016/12/12.
 */

public class TreatNewsDetailActivity extends BaseActivityToolBar {


    public static final String WEBVIEWBEAN = "webViewBean";
    public static final int FROM_INFO = 1;//新闻进入
    public static final int FROM_TIPS = 2;//小贴士进入

    private Activity mContext = TreatNewsDetailActivity.this;
    private WebView webView;
    private String url = "";
    private String title = "";
    private String webTitle = "";
    private String id = "";
    private String token = "";
    private String userId = "";
    private SharedPreferences sharedPreferences;
    private boolean ISFAVORITE;
    private int favoriteId;
    private int isFrome;
    private ShareDialog shareDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.act_new_detail_treat;
    }

    @Override
    protected void initToolbar() {
        initData();
        initShareToolBar(title, iToolbarListener);
    }

    IToolbarListener iToolbarListener = new IToolbarListener() {
        @Override
        public void clickLeft() {
            finish();
        }

        @Override
        public void clickRight() {
            switch (isFrome) {
                case FROM_TIPS:
                    UShareBean shareBeanTips = new UShareBean(url, title, "专注服务于肿瘤患者的App");
                    new ShareDialog(mContext, shareBeanTips);
                    break;

                default:
                    if (TextUtils.isEmpty(token)) {
                        UShareBean shareBean = new UShareBean(url, title, "专注服务于肿瘤患者的App");
                        TreatMentShareBean treatMentShareBean = new TreatMentShareBean(userId, favoriteId, TreatMentShare.INFOMATION);
                        new ShareDialog(mContext, treatMentShareBean, shareBean, token, ISFAVORITE, true);
                    } else {
                        isFavorite();
                    }
                    break;
            }
        }

        @Override
        public void clickRightSecond() {

        }
    };

    @Override
    protected void initView() {

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new SSLTolerentWebViewClient());
        webView.loadUrl(url);

    }


    private void initData() {
        Intent intent = getIntent();
        WebViewBean webViewBean = (WebViewBean) intent.getSerializableExtra(WEBVIEWBEAN);
        url = webViewBean.getUrl();
        id = webViewBean.getId();
        isFrome = webViewBean.getFrome();
        title = webViewBean.getTitle();
        favoriteId = Integer.parseInt(id);

        sharedPreferences = getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(SPInfo.UserKey_userId, "");
        token = sharedPreferences.getString(SPInfo.UserKey_token, "");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        com.umeng.socialize.utils.Log.d("result", "onActivityResult");
        if (resultCode == RESULT_OK) {
            userId = sharedPreferences.getString(SPInfo.UserKey_userId, "");
            token = sharedPreferences.getString(SPInfo.UserKey_token, "");
            isFavorite();
//            TreatMentShareBean treatMentShareBean = new TreatMentShareBean(userId,favoriteId,TreatMentShare.NEWS);
//            shareDialog.setLoginData(treatMentShareBean,token);
        }
    }

    private void isFavorite() {
        HttpService.getHttpService().getInformationIsFavorite(favoriteId, TreatMentShare.INFOMATION, token)
                .compose(RxUtil.<HttpResult<Boolean>>normalSchedulers())
                .subscribe(new Subscriber<HttpResult<Boolean>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "isFavorite onCompleted");
                        UShareBean shareBean = new UShareBean(url, title, "易治分享");
                        TreatMentShareBean treatMentShareBean = new TreatMentShareBean(userId, favoriteId, TreatMentShare.INFOMATION);
                        new ShareDialog(mContext, treatMentShareBean, shareBean, token, ISFAVORITE, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "isFavorite onError e" + e);
                    }

                    @Override
                    public void onNext(HttpResult<Boolean> booleanHttpResult) {
                        Log.i("hcb", "isFavorite onNext booleanHttpResult" + booleanHttpResult);
                        if (booleanHttpResult != null)
                            ISFAVORITE = booleanHttpResult.getData();
                    }
                });
    }

    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }


    }
}