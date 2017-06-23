package com.zhongmeban.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.Interface.IToolbarListener;
import com.zhongmeban.R;
import com.zhongmeban.utils.DisplayUtil;

/**
 * Created by User on 2016/10/10.
 */

public class ToolbarView extends FrameLayout implements View.OnClickListener {

    private TextView mTvTitle;
    private TextView mTvAction;
    private Toolbar mToolbar;
    private ImageView mIvRightAction;
    private ImageView mIvRightSecondAction;
    private IToolbarListener mListener;
    private Context mContext;


    public ToolbarView(Context context) {
        this(context, null);
    }


    public ToolbarView(Context context,
                       @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ToolbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.view_toolbar_title, this);

        mTvTitle = (TextView) findViewById(R.id.id_toolbar_title);
        mTvAction = (TextView) findViewById(R.id.id_toolbar_action);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_layout);

        mIvRightAction = (ImageView) findViewById(R.id.iv_toolbar_action);
        mIvRightSecondAction = (ImageView) findViewById(R.id.iv_toolbar_second_action);

        mTvAction.setOnClickListener(this);
        mIvRightAction.setOnClickListener(this);
        mIvRightSecondAction.setOnClickListener(this);

        mIvRightSecondAction.setVisibility(GONE);
        if (mContext instanceof AppCompatActivity){
            ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);
        }

        //mToolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));
        //设置 toolbar 导航栏图片和点击事件
        mToolbar.setNavigationIcon(R.drawable.back_grey);

        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (mListener != null) mListener.clickLeft();
            }
        });

        Toolbar.LayoutParams params = new Toolbar.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mTvTitle.setGravity(Gravity.CENTER);
        //params.width = ZMBUtil.getWidth()- DisplayUtil.dip2px(context,180);
        //params.leftMargin = DisplayUtil.dip2px(context,4);
        params.rightMargin= DisplayUtil.dip2px(context,72);
        mTvTitle.setLayoutParams(params);
    }


    public void setToolBarListener(IToolbarListener listener) {
        this.mListener = listener;
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_toolbar_action:
                if (mListener != null) mListener.clickRight();
                break;
            case R.id.iv_toolbar_action:
                if (mListener != null) mListener.clickRight();
                break;
            case R.id.iv_toolbar_second_action:
                if (mListener != null) mListener.clickRightSecond();
                break;
        }
    }


    public ToolbarView setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }


    /**
     * 最右边的动作，文字类型
     * @param title
     * @return
     */
    public ToolbarView setActionName(String title) {
        mTvAction.setText(title);
        mTvAction.setVisibility(VISIBLE);
        mIvRightAction.setVisibility(GONE);
        return this;
    }
    /**
     * 最右边的动作，图片类型
     * @param resId
     * @return
     */
    public ToolbarView setActionName(int resId) {
        mIvRightAction.setImageResource(resId);
        mTvAction.setVisibility(GONE);
        mIvRightAction.setVisibility(VISIBLE);
        return this;
    }


    /**
     * 最右边的按钮左边的 按钮
     * @param resId
     * @return
     */
    public ToolbarView setActionSecond(int resId) {
        mIvRightSecondAction.setImageResource(resId);
        mIvRightSecondAction.setVisibility(VISIBLE);
        return this;
    }


    public Toolbar getmToolbar() {
        return mToolbar;
    }
}
