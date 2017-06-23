package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.zhongmeban.R;
import com.zhongmeban.bean.NounDetails;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ShareDialog;
import com.zhongmeban.view.ViewCustomLoading;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:名词解释item详情
 */
public class ActInterpretationDetail extends BaseScrollActivity
    implements ObservableScrollViewCallbacks {
    private TextView mainTitle;
    private TextView title;
    private TextView content;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private RelativeLayout mTitle;
    private View mImageView;
    private ImageView mBackView;
    private ImageView mShare;
    private ViewCustomLoading progressBar;
    private Activity mContext = ActInterpretationDetail.this;
    private BottomDialog dialog;
    private String interpretationId;
    private String userId;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpretation_detail);

        Intent intent = getIntent();
        interpretationId = intent.getStringExtra("interpretationId");

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("token", "");

        initView();
        mScrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        getHttpData();
    }


    private void initView() {
        mainTitle = (TextView) findViewById(R.id.tv_maintitle);
        content = (TextView) findViewById(R.id.tv_content);
        mImageView = findViewById(R.id.rl_image);
        progressBar = (ViewCustomLoading) findViewById(R.id.progressBar);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);

        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(onClickListener);

        mTitle = (RelativeLayout) findViewById(R.id.ll_title);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.toolbar_color)));

        title = (TextView) findViewById(R.id.tv_title);
        title.setTextColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.top_text)));

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
    }


    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.iv_share:
                    //创建分享Dialog
                    if (!TextUtils.isEmpty(userId)) {
                        int id = Integer.parseInt(interpretationId);
//                        TreatMentShareBean bean = new TreatMentShareBean(userId, id,
//                            TreatMentShare.INTERPRETATION);
//                        ShareDialog.showShareDialog(mContext, bean, token);
                    }
                    break;

            }
        }
    };


    /**
     * 网络请求部分
     */
    public void getHttpData() {
        HttpService.getHttpService().getInterpretationDetail(interpretationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<NounDetails>() {
                @Override
                public void onStart() {
                    super.onStart();
                }


                @Override
                public void onCompleted() {
                    Log.i("hcb", "http onCompleted()");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 500);

                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "http e is" + e);
                    mScrollView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    ToastUtil.showText(ActInterpretationDetail.this, getString(R.string.Load_fail));
                }


                @Override
                public void onNext(NounDetails nounDetails) {
                    if (nounDetails.getErrorCode() != 0) {
                        mScrollView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                        ToastUtil.showText(ActInterpretationDetail.this,
                            getString(R.string.load_empty));

                        return;
                    }

                    mainTitle.setText(nounDetails.getData().getInterpretationName());
                    content.setText(nounDetails.getData().getDescription());
                    title.setText(nounDetails.getData().getInterpretationName());
                }
            });
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.toolbar_color);
        int baseTextColor = getResources().getColor(R.color.top_text);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        Log.i("hcb", "alpha is" + alpha);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        title.setTextColor(ScrollUtils.getColorWithAlpha(alpha, baseTextColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
        if (alpha > 0.5f) {
            mBackView.setImageResource(R.drawable.back_grey);
            mShare.setImageResource(R.drawable.more1);
        } else {
            mBackView.setImageResource(R.drawable.back_white);
            mShare.setImageResource(R.drawable.threepoints);
        }
    }


    @Override
    public void onDownMotionEvent() {

    }


    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
