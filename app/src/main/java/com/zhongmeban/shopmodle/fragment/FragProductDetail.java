package com.zhongmeban.shopmodle.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.shop.ProductDetailBean;

/**
 * Description: 商品详情 ---》商品详情
 *
 * 邮箱：cchen@ideabinder.com
 */
public class FragProductDetail extends BaseFragment {

    public static final String KEY = "shoushu";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private ProductDetailBean mProductDetail;

    final String mimeType = "text/html";
    final String encoding = "utf-8";


    public static FragProductDetail newInstance(ProductDetailBean productDetail) {
        FragProductDetail fragment = new FragProductDetail();
        Bundle args = new Bundle();
        args.putSerializable(KEY, productDetail);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProductDetail = (ProductDetailBean) getArguments().getSerializable(KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_detail, container, false);

        initWidget(view);

        initData();
        return view;
    }


    public void initWidget(View view) {
        mWebView = (WebView) view.findViewById(R.id.id_frag_product_webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.id_frag_product_progress);
    }


    private void initData() {
        initWebView();
        //try {
        //    mWebView.loadData(URLEncoder.encode(mProductDetail.getIntroduction(), encoding),
        //        mimeType,
        //        encoding);
        //    Logger.d("htnml==="+mProductDetail.getIntroduction());
        //} catch (UnsupportedEncodingException e) {
        //    Logger.e("htnml==="+e.toString());
        //    e.printStackTrace();
        //}
        mWebView.loadDataWithBaseURL(null,mProductDetail.getIntroduction(), "text/html",  "utf-8", null);

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
        //settings.setBuiltInZoomControls(false);// 设置支持缩放
        //settings.setSupportZoom(false);// 不支持缩放
        ////settings.setUseWideViewPort(true);// 将图片调整到适合webview大小
        //settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//支持缓存
        //settings.setDomStorageEnabled(true);//支持缓存


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
