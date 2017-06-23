package com.zhongmeban.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhongmeban.Interface.IEmptyLayoutListener;
import com.zhongmeban.R;

import static com.zhongmeban.R.string.load_no_net;

/**
 * Created by User on 2016/10/10.
 */

public class ViewEmptyLayout extends FrameLayout implements View.OnClickListener {

    private ImageView mIvImg;
    private TextView mTvContent;
    private TextView mTvLoading;
    private Button mBtnAction;

    private LinearLayout mLayoutProgress;
    private LinearLayout lnytLoadContainer;
    private IEmptyLayoutListener mListener;
    private int emptyResId;
    private int failResId;
    private int searchFailResId;
    private int emptyText;
    private int failText;
    private int searchFailText;
    private int loadingText;
    private String actionText;
    private boolean isShowACtion = false;
    private Context mContext;
    private ImageView ivLoading;
    private boolean isNetError = false;


    public ViewEmptyLayout(Context context) {
        this(context, null);
    }


    public ViewEmptyLayout(Context context,
                           @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ViewEmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_empty_layout, this);

        mIvImg = (ImageView) findViewById(R.id.id_view_empty_iv_img);
        mTvContent = (TextView) findViewById(R.id.id_view_empty_tv_content);
        mTvLoading = (TextView) findViewById(R.id.tv_loading);
        mBtnAction = (Button) findViewById(R.id.id_view_empty_btn_action);
        mLayoutProgress = (LinearLayout) findViewById(R.id.id_empty_view_pregress);
        lnytLoadContainer = (LinearLayout) findViewById(R.id.lnyt_load_container);
        ivLoading = (ImageView) findViewById(R.id.iv_loading_custom);

        mBtnAction.setOnClickListener(this);
        lnytLoadContainer.setOnClickListener(this);

        rotateLoading();
    }


    public void rotateLoading() {
/*        Animation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1000);
        animation.setFillAfter(true);
        animation.setDuration(600);
        ivLoading.startAnimation(animation);*/

        ObjectAnimator animator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 360f);
        animator.setDuration(600);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void setResource(Builder builder) {
        this.emptyResId = builder.load_no_data;
        this.failResId = builder.load_no_net_res;
        this.searchFailResId = builder.load_search_fail;
        this.emptyText = builder.noDataContent;
        this.failText = builder.noNetContent;
        this.loadingText = builder.loadingContent;
        this.searchFailText = builder.noDataSearch;

        mTvLoading.setText(mContext.getString(loadingText));

        setActionName(actionText);
        isShowButton(isShowACtion);
    }


    public void setActionListener(IEmptyLayoutListener listener) {
        this.mListener = listener;
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_view_empty_btn_action:
                //if (mListener != null) mListener.onClickReLoadData();
                break;
            case R.id.lnyt_load_container:
                if (mListener != null&&isNetError) mListener.onClickReLoadData();
                break;

        }
    }


    public void showProgress() {
        setVisibility(VISIBLE);
        mLayoutProgress.setVisibility(VISIBLE);
        isNetError = false;
    }


    public void showEmpty() {
        setVisibility(VISIBLE);
        mLayoutProgress.setVisibility(GONE);
        mTvContent.setText(mContext.getString(emptyText));
        mIvImg.setImageResource(emptyResId);
        isNetError = false;
    }


    public void showNoNet() {
        setVisibility(VISIBLE);
        mLayoutProgress.setVisibility(GONE);
        mTvContent.setText(mContext.getString(failText));
        mIvImg.setImageResource(failResId);
        isNetError = true;
    }


    public void showSearchFail() {
        setVisibility(VISIBLE);
        mLayoutProgress.setVisibility(GONE);
        mTvContent.setText(mContext.getString(searchFailText));
        mIvImg.setImageResource(searchFailResId);
        isNetError = false;
    }


    public void showSuccess() {
        setVisibility(GONE);
    }


    // 设置空布局 最底部 的执行动作是否显示：比如购物车需要有个按钮---去逛逛
    public void isShowButton(boolean flag) {
        mBtnAction.setVisibility(flag ? VISIBLE : GONE);
    }


    //设置空布局 执行动作按钮 的 name
    public void setActionName(String actionName) {
        mBtnAction.setText(actionName);
    }


    public static class Builder {

        public int load_no_net_res = R.drawable.load_no_network;
        public int load_no_data = R.drawable.no_attention;
        public int load_search_fail = R.drawable.load_search_fail;

        public int loadingContent =R.string.load_ing;
        public int noNetContent = load_no_net;
        public int noDataContent =R.string.load_no_data;
        public int noDataSearch = R.string.load_no_search;


        public Builder() {
        }


        public Builder setNoNetRes(int load_no_net_res) {
            this.load_no_net_res = load_no_net_res;
            return this;
        }


        public Builder setNoDataRes(int load_no_data) {
            this.load_no_data = load_no_data;
            return this;
        }


        public Builder setSearchFailRes(int load_search_fail) {
            this.load_search_fail = load_search_fail;
            return this;
        }


        public Builder setLoadingText(int loadingContent) {
            this.loadingContent = loadingContent;
            return this;
        }


        public Builder setNoNetText(int noNetContent) {
            this.noNetContent = noNetContent;
            return this;
        }


        public Builder setNoDataText(int noDataContent) {
            this.noDataContent = noDataContent;
            return this;
        }


        public Builder setNoSearchText(int noDataSearch) {
            this.noDataSearch = noDataSearch;
            return this;
        }
    }

}
