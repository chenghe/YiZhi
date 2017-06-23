package com.zhongmeban.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.orhanobut.logger.Logger;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/8 11:02
 */
public abstract class LoadMoreRecyclerOnScrollListener extends
    RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount,lastVisibleItemPosition;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public LoadMoreRecyclerOnScrollListener(
        LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        Logger.v("loading--"+loading+"--totalItemCount=="+totalItemCount+"--lastPosition=="+lastVisibleItemPosition+"--currentPage=="+currentPage
        +"\n"+"previousTotal=="+previousTotal+"--totalItemCount=="+totalItemCount);
        //if (!loading
        //    && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
        if (!loading
            && totalItemCount <= lastVisibleItemPosition+1) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}