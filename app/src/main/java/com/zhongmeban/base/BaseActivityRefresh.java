package com.zhongmeban.base;

import android.support.v4.widget.SwipeRefreshLayout;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.utils.NetWorkUtils;
import com.zhongmeban.utils.ToastUtil;

/**
 * Created by User on 2016/9/30.
 */

public abstract class BaseActivityRefresh extends BaseActivityToolBar
    implements SwipeRefreshLayout.OnRefreshListener {

    protected SwipeRefreshLayout mRefreshLayout;


    protected void initRefreshLayout() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh);
        if (mRefreshLayout == null) return;
        mRefreshLayout.setColorSchemeResources(R.color.app_green);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.post(new Runnable() {
            @Override public void run() {
                //mRefreshLayout.setRefreshing(true);
            }
        });
    }


    protected abstract int getLayoutIdRefresh();
    protected abstract void initToolbarRefresh();
    protected abstract void initViewRefresh();


    @Override protected int getLayoutId() {
        return getLayoutIdRefresh();
    }


    @Override protected void initToolbar() {
        initRefreshLayout();
        initToolbarRefresh();
    }


    @Override protected void initView() {

        initViewRefresh();
    }


    @Override public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    public void hideSwipeRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }


    protected void showOnError(Throwable e, boolean isLoadMore) {

        if (!isLoadMore) {//如果是加载更多,不需要处理界面，保持当前内容就可以
       /*     if (e.toString().contains("504") || e.toString().contains("HttpException")) {
                ToastUtil.showText(MyApplication.app, "网络连接错误");
            } else if (e.toString().contains("SocketTimeoutException")) {
                ToastUtil.showText(MyApplication.app, "网络加载失败");
            } else if (e.toString().contains("500") || e.toString().contains("404")) {
                ToastUtil.showText(MyApplication.app, "服务器连接失败");
            } else {
                ToastUtil.showText(MyApplication.app, "连接失败");
            }*/
            showToastGetData();
            showEmptyLayoutState(LOADING_STATE_NO_NET);
        } else {
            //出现在上拉加载的时候
            showToastGetData();
        }
    }


    protected void showOnNextLayout(HttpResult httpResult) {
        if (httpResult == null || httpResult.getData() == null || httpResult.getErrorCode() != 0) {
           /* if (httpResult.getData() == null && !NetWorkUtils.isNetworkConnected(
                MyApplication.app)) {
                showEmptyLayoutState(LOADING_STATE_NO_NET);
                ToastUtil.showText(MyApplication.app, "网络连接错误");
            } else {

                ToastUtil.showText(MyApplication.app, "服务器连接失败");
                showEmptyLayoutState(LOADING_STATE_EMPTY);
            }
*/
            showToastGetData();
            showEmptyLayoutState(LOADING_STATE_EMPTY);
            return;
        }
    }

    private void showToastGetData(){
        if (NetWorkUtils.isNetworkConnected(MyApplication.app)){
            ToastUtil.showText(MyApplication.app, "服务器连接失败");
        } else{
            ToastUtil.showText(MyApplication.app, "网络连接失败");
        }
    }

}
