package com.zhongmeban.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.zhongmeban.Interface.IEmptyLayoutListener;
import com.zhongmeban.Interface.IToolbarListener;
import com.zhongmeban.R;
import com.zhongmeban.view.ToolbarView;
import com.zhongmeban.view.ViewEmptyLayout;

/**
 * Created by Chengbin He on 2016/3/21.
 */
public abstract class BaseActivityToolBar extends BaseActivity
    implements IToolbarListener, IEmptyLayoutListener {

    protected static final int LOADING_STATE_SUCESS = 1;//加載成功后隱藏
    protected static final int LOADING_STATE_SEARCH_FAIL = -1;//加载失败后
    protected static final int LOADING_STATE_PEOGRESS = 0;//加载过程中
    protected static final int LOADING_STATE_EMPTY = 2;//加载成功，但是没有内容的时候
    protected static final int LOADING_STATE_NO_NET = -2;//网络错误

    protected ToolbarView mToolbar;
    protected ViewEmptyLayout mEmptyLayout;


    @Override protected void initTitle() {

    }


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initToolbar();
        initView();
    }


    /**
     * 初始化自定义的toolbar,子类必须实现
     *
     * @param title 中间的标题
     * @param actionName 右边的可能会有的动作的名称，没有就写 “”
     */
    public void initToolbarCustom(String title, String actionName) {
        mToolbar = (ToolbarView) findViewById(R.id.id_toolbar_view);
        mToolbar.setToolBarListener(this);
        mToolbar.setTitle(title);
        mToolbar.setActionName(actionName);
    }

    /**
     * 初始化带搜索自定义的toolbar,
     *
     * @param title 中间的标题
     */
    public void initSearchToolBar(String title , IToolbarListener iToolbarListener){
        mToolbar = (ToolbarView) findViewById(R.id.id_toolbar_view);
        mToolbar.setToolBarListener(iToolbarListener);
        mToolbar.setTitle(title);
        mToolbar.setActionName(R.drawable.home_search);

    }

    /**
     * 初始化带分享自定义的toolbar,
     *
     * @param title 中间的标题
     */
    public void initShareToolBar(String title , IToolbarListener iToolbarListener){
        mToolbar = (ToolbarView) findViewById(R.id.id_toolbar_view);
        mToolbar.setToolBarListener(iToolbarListener);
        mToolbar.setTitle(title);
        mToolbar.setActionName(R.drawable.tool_menu);

    }
    /**
     * 初始化空布局详情，子类根据需要调用
     */
    protected void initEmptyView() {
        mEmptyLayout = (ViewEmptyLayout) findViewById(R.id.id_empty_view);
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


    //返回键 的点击事件，子类根据需要重写，默认是 back
    @Override public void clickLeft() {
        onBackPressed();
    }


    //toolbar 右边 的点击事件，子类根据需要重写
    @Override public void clickRight() {
    }


    //toolbar 右边 的  左側 点击事件，子类根据需要重写
    @Override public void clickRightSecond() {

    }


    protected abstract int getLayoutId();
    protected abstract void initToolbar();
    protected abstract void initView();


    //空布局 有可能会有的点击事件 比如：购物车去逛逛，子类根据需要重写
    @Override public void onClickReLoadData() {
    }

}
