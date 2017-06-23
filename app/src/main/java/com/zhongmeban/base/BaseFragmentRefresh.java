package com.zhongmeban.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhongmeban.Interface.IEmptyLayoutListener;
import com.zhongmeban.Interface.IToolbarListener;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.utils.NetWorkUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.ToolbarView;
import com.zhongmeban.view.ViewEmptyLayout;

/**
 * Created by User on 2016/9/30.
 */

public abstract class BaseFragmentRefresh extends BaseFragment
    implements IToolbarListener, SwipeRefreshLayout.OnRefreshListener, IEmptyLayoutListener {

    public static final int LOADING_STATE_SUCESS = 1;//加載成功后隱藏
    public static final int LOADING_STATE_SEARCH_FAIL = -1;//加载失败后
    public static final int LOADING_STATE_PEOGRESS = 0;//加载过程中
    public static final int LOADING_STATE_EMPTY = 2;//加载成功，但是没有内容的时候
    public static final int LOADING_STATE_NO_NET = -2;//网络错误
    protected SwipeRefreshLayout mRefreshLayout;
    protected View mRootView;
    protected ToolbarView mToolbar;
    private ViewEmptyLayout mEmptyLayout;


    protected void initRefreshLayout(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_refresh);
        if (mRefreshLayout == null) return;
        mRefreshLayout.setColorSchemeResources(R.color.app_green);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.post(new Runnable() {
            @Override public void run() {
                //mRefreshLayout.setRefreshing(true);
            }
        });
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("hcbtest", "BaseFragmentRefresh onCreateView");

        mRootView = inflater.inflate(getLayoutIdRefresh(), container, false);
        initRefreshLayout(mRootView);

        initToolbarRefresh();
        initOnCreateView();
        return mRootView;
    }


    protected abstract int getLayoutIdRefresh();
    protected abstract void initToolbarRefresh();
    protected abstract void initOnCreateView();


    /**
     * 初始化自定义的toolbar,子类必须实现
     *
     * @param title 中间的标题
     * @param actionName 右边的可能会有的动作的名称，没有就写 “”
     */
    public void initToolbarCustom(String title, String actionName) {
        mToolbar = (ToolbarView) mRootView.findViewById(R.id.id_toolbar_view);
        mToolbar.setToolBarListener(this);
        mToolbar.setTitle(title);
        mToolbar.setActionName(actionName);
    }


    @Override public void onRefresh() {

    }


    //加载失败或者为空的话 ，可执行的按钮
    @Override public void onClickReLoadData() {
        if (mRefreshLayout != null) {
            mRefreshLayout.postDelayed(new Runnable() {
                @Override public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }

    }

    /**
     * 初始化空布局详情，子类根据需要调用
     * @param emptyResId 为空时图片
     * @param failResId 失败图片
     * @param emptyText 为空的文字
     * @param failText 失败的文字
     * @param actionText 文字下边按钮的文本
     * @param isShowACtion 是否显示文字下 按钮
     */

    /**
     * 初始化空布局详情，子类根据需要调用
     */
    protected void initEmptyView() {
        mEmptyLayout = (ViewEmptyLayout) mRootView.findViewById(R.id.id_empty_view);
        ViewEmptyLayout.Builder builder = new ViewEmptyLayout.Builder();
        mEmptyLayout.setResource(builder);
        mEmptyLayout.setActionListener(this);
    }


    /**
     * 显示空布局的那种状态 第二个参数为true 显示进度条 false 显示空布局状态
     */
    protected void showEmptyLayoutState(int state) {

        switch (state) {
            case LOADING_STATE_SUCESS:
                mEmptyLayout.showSuccess();
                break;
            case LOADING_STATE_SEARCH_FAIL:
                mEmptyLayout.showSearchFail();
                break;
            case LOADING_STATE_PEOGRESS:
                mEmptyLayout.showProgress();
                break;
            case LOADING_STATE_EMPTY:
                mEmptyLayout.showEmpty();
                break;
            case LOADING_STATE_NO_NET:
                mEmptyLayout.showNoNet();
                break;
        }

    }


    @Override
    public void clickLeft() {

    }


    @Override
    public void clickRight() {

    }


    //toolbar 右边 的  左側 点击事件，子类根据需要重写
    @Override public void clickRightSecond() {

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
            showToastGetData();
            showEmptyLayoutState(LOADING_STATE_NO_NET);
        } else {
            //出现在上拉加载的时候
            showToastGetData();
        }
    }


    protected void showOnNextLayout(HttpResult httpResult) {
        if (httpResult == null || httpResult.getData() == null || httpResult.getErrorCode() != 0) {

            showToastGetData();
            showEmptyLayoutState(LOADING_STATE_EMPTY);
            return;
        }
    }


    protected void showOnNextLayoutSearch(HttpResult httpResult) {
        if (httpResult == null || httpResult.getData() == null || httpResult.getErrorCode() != 0) {

            showToastGetData();
            showEmptyLayoutState(LOADING_STATE_SEARCH_FAIL);
            return;
        }
    }


    protected void showOnErrorSearch(Throwable e, boolean isLoadMore) {

        if (!isLoadMore) {//如果是加载更多,不需要处理界面，保持当前内容就可以
            showToastGetData();
            showEmptyLayoutState(LOADING_STATE_SEARCH_FAIL);
        } else {
            //出现在上拉加载的时候
            showToastGetData();
        }
    }


    private void showToastGetData() {
        if (NetWorkUtils.isNetworkConnected(MyApplication.app)) {
            ToastUtil.showText(MyApplication.app, "服务器连接失败");
        } else {
            ToastUtil.showText(MyApplication.app, "网络连接失败");
        }
    }
}
