package com.zhongmeban.base.baseadapter.wrapper;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.base.baseadapter.utils.WrapperUtils;
import com.zhongmeban.base.weight.LoadingFooter;

/**
 * Created by zhy on 16/6/23.
 */
public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;
    LoadingFooter mFooter;


    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }
    public LoadMoreWrapper(RecyclerView.Adapter adapter, Context context) {
        mInnerAdapter = adapter;
        mFooter =  new LoadingFooter(context);
        setLoadMoreView(mFooter);
    }


    private boolean hasLoadMore() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }


    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }


    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent,
                    mLoadMoreLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                ////////////////////////
                if (mLoadMoreView instanceof LoadingFooter) {
                    LoadingFooter footer = (LoadingFooter) mLoadMoreView;
                    if (footer.getState().equals( LoadingFooter.State.TheEnd)||footer.getState().equals( LoadingFooter.State.Loading)) {
                        return;
                    }
                }
                setState(LoadingFooter.State.Loading);
                //////////////////////// 这段代码是自己写的16.12.22
                mOnLoadMoreListener.onLoadMoreRequested();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView,
            new WrapperUtils.SpanSizeCallback() {
                @Override
                public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                    if (isShowLoadMore(position)) {
                        return layoutManager.getSpanCount();
                    }
                    if (oldLookup != null) {
                        return oldLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }


    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
            && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p
                = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }


    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public void removeLoadMore() {
        if (mLoadMoreView != null) {
            mLoadMoreView = null;
        }
    }


    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }


    private OnLoadMoreListener mOnLoadMoreListener;


    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener;
        }
        return this;
    }

    public void setState(LoadingFooter.State status, boolean showView){
        if (mFooter!=null){
            mFooter.setState(status,showView);
        }
    }
    public void setState(LoadingFooter.State status){
        if (mFooter!=null){
            mFooter.setState(status);

            if (status.equals(LoadingFooter.State.NetWorkError)){
                mFooter.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if (mOnLoadMoreListener != null) {
                            setState(LoadingFooter.State.Loading);
                            mOnLoadMoreListener.onLoadMoreRequested();
                        }
                    }
                });
            }
        }
    }

    public LoadMoreWrapper setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }


    public LoadMoreWrapper setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }
}
