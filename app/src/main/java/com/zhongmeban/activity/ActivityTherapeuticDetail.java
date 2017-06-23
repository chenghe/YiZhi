package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.TherapeuticDetailBean;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.mymodle.activity.ActivityMyTreatment;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.fragment.FragTherapeuticDetail1;
import com.zhongmeban.treatmodle.fragment.FragTherapeuticDetail2;
import com.zhongmeban.treatmodle.fragment.FragTherapeuticDetail3;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ViewCustomLoading;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import java.util.Arrays;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:治疗详情(新)
 * Created by Chengbin He on 2016/3/30.
 */
public class ActivityTherapeuticDetail extends BaseScrollActivity
    implements ObservableScrollViewCallbacks {

    private Activity mContext = ActivityTherapeuticDetail.this;
    private View mImageView;
    private ImageView mBackView;
    private ImageView mShare;
    private RelativeLayout mTitle;//顶部标题RelativeLayout
    private TextView mainTitle;//主标题
    private TextView contentTitle;//主内容标题
    private TextView tv_label;//主内容标题 下标签

    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private BottomDialog dialog;
    private ViewCustomLoading progressBar;
    private String label;

    private String userId;
    private String token;
    private String therapeuticId;

    private FrameLayout mContainer;
    //治疗模块的fragment
    private FragTherapeuticDetail1 mFragment1;
    private boolean isFavorite;
    private float alpha;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapeuticdetail);

        Intent intent = getIntent();
        therapeuticId = intent.getStringExtra("therapeuticId");
        label = intent.getStringExtra("label");
        Log.i("hcb", "therapeuticId is" + therapeuticId);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("token", "");

        initView();
        mScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(therapeuticId)) {

            /////////////////////新接口  1 是手术，69是化疗 70放疗  测试用的/////////////////////////////////
            //getHttpData("1");
            getHttpData(therapeuticId);
        }
    }


    private void getHttpData(final String therapeuticId) {
        HttpService.getHttpService().getTherapeuticDetail(therapeuticId,userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<TherapeuticDetailBean>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "onCompleted()");
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
                    Log.i("hcb", "onCompleted()");
                    Log.i("hcb", "http e is" + e);
                }


                @Override
                public void onNext(TherapeuticDetailBean therapeuticDetail) {
                    mainTitle.setText(therapeuticDetail.getData().getName());
                    contentTitle.setText(therapeuticDetail.getData().getName());

                    showFragment(therapeuticDetail);

                    isFavorite = therapeuticDetail.getData().isIsFavorite();
                    updateTopBarUI(isFavorite);

                    Logger.i("治疗信息----" + therapeuticDetail.toString());
                }
            });
    }


    private void initView() {
        //content 不带效果部分
        progressBar = (ViewCustomLoading) findViewById(R.id.progressBar);
        contentTitle = (TextView) findViewById(R.id.tv_sub_title);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_label.setText(label);

        // 带效果部分
        mImageView = findViewById(R.id.rl_image);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);
        mShare.setVisibility(View.VISIBLE);

        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(onClickListener);

        mTitle = (RelativeLayout) findViewById(R.id.ll_title);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.title_bg)));

        mainTitle = (TextView) findViewById(R.id.tv_title);
        mainTitle.setTextColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.top_text)));

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        mContainer = (FrameLayout) findViewById(R.id.id_therapeutic_detail_container);

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    onBackPressed();
                    break;
                case R.id.iv_share:

                    int id = Integer.parseInt(therapeuticId);

                    //创建分享Dialog
                    //                    ShareDialog.showShareDialog(mContext);
                    if (!TextUtils.isEmpty(userId)) {
                        //                        int tipID = Integer.parseInt(infoID);
                        //ShareDialog.showShareDialog(mContext, bean, token);
                        if (isFavorite) {
                            Integer[] list = new Integer[] { Integer.valueOf(id) };
                            cancleFavoriteType(TreatMentShare.THERAPEUTIC, Arrays.asList(list));
                        } else {
                            favoriteType(id, TreatMentShare.THERAPEUTIC);
                        }
                    } else {
                        startActivityForResult(new Intent(ActivityTherapeuticDetail.this, ActivityCardLogin.class),6);
                    }
                    break;

                //                case R.id.tv_dialog_cancel:
                //                    dialog.dismiss();
                //                    break;
            }
        }
    };

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            userId = sp.getString("userId", "");
            token = sp.getString("token", "");

        }
    }


    public void showFragment(TherapeuticDetailBean therapeuticDetail) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //获取治疗的种类 1 手术 ，2 化疗，3 放疗,4 靶向治疗
        switch (therapeuticDetail.getData().getCategoryType()) {

            case 1:
                fragmentTransaction.replace(R.id.id_therapeutic_detail_container,
                    mFragment1 = FragTherapeuticDetail1
                        .newInstance(therapeuticDetail));
                break;
            case 2:
                fragmentTransaction.replace(R.id.id_therapeutic_detail_container,
                    FragTherapeuticDetail2
                        .newInstance(therapeuticDetail));
                break;
            case 3:
                fragmentTransaction.replace(R.id.id_therapeutic_detail_container,
                    FragTherapeuticDetail3
                        .newInstance(therapeuticDetail));
                break;
        }
        fragmentTransaction.commit();
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
        alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        Log.i("hcb", "alpha is" + alpha);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        mainTitle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, baseTextColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
        updateTopBarUI(isFavorite);
    }


    private void updateTopBarUI(boolean love) {
        if (!love) {
            if (alpha > 0.5f) {
                mBackView.setImageResource(R.drawable.back_grey);
                mShare.setImageResource(R.drawable.collect_grey);
            } else {
                mBackView.setImageResource(R.drawable.back_white);
                mShare.setImageResource(R.drawable.collect_white);
            }
        } else {
            mShare.setImageResource(R.drawable.collect_selected);
            mBackView.setImageResource(alpha > 0.5f?R.drawable.back_grey:R.drawable.back_white);
        }

    }


    @Override
    public void onDownMotionEvent() {

    }


    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    //处理 视频全屏播放的返回问题
    @Override public void onBackPressed() {
        if (mFragment1 != null) {
            if (JCVideoPlayer.backPress()) {
                return;
            }
        }
        Intent intent = new Intent(mContext,ActivityMyTreatment.class);
        intent.putExtra("isFavorite",isFavorite);
        setResult(RESULT_OK,intent);
        finish();
    }


    //不可见的时候释放视频播放器资源
    @Override
    protected void onPause() {
        super.onPause();
        if (mFragment1 != null) {
            JCVideoPlayer.releaseAllVideos();
        }
    }


    public void favoriteType(int favoriteId, int type) {

        HttpService.getHttpService()
            .postCreateUserFavorite(new TreatMentShareBean(favoriteId, type), token)
            .compose(RxUtil.<CreateOrDeleteBean>normalSchedulers())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    ToastUtil.showText(ActivityTherapeuticDetail.this, "收藏失败");
                }


                @Override public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean == null ||
                        Integer.valueOf(createOrDeleteBean.getErrorCode()) != 0) {
                        return;
                    }
                    ToastUtil.showText(ActivityTherapeuticDetail.this, "已收藏");
                    isFavorite = true;

                    updateTopBarUI(isFavorite);
                }
            });
    }


    public void cancleFavoriteType(int type, List<Integer> list) {

        HttpService.getHttpService().postDeleteFavorite(new DeleteFavoriteBody(type, list), token)
            .compose(RxUtil.<CreateOrDeleteBean>normalSchedulers())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    ToastUtil.showText(ActivityTherapeuticDetail.this, "取消失败");
                }


                @Override public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean == null ||
                        Integer.valueOf(createOrDeleteBean.getErrorCode()) != 0) {
                        return;
                    }
                    ToastUtil.showText(ActivityTherapeuticDetail.this, "已取消");
                    isFavorite = false;

                    updateTopBarUI(isFavorite);
                }
            });
    }

}

