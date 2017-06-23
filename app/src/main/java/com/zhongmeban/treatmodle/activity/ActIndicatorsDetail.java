package com.zhongmeban.treatmodle.activity;

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
import com.orhanobut.logger.Logger;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.ExternalLinearCommonAdapter;
import com.zhongmeban.bean.ExternalLinearCommonBean;
import com.zhongmeban.bean.IndiacatorDetail;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.ShareDialog;
import com.zhongmeban.view.TextViewExpandableAnimation;
import com.zhongmeban.view.ViewCustomLoading;
import java.util.ArrayList;
import java.util.List;
import me.next.tagview.TagCloudView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:指标解释详情
 */
public class ActIndicatorsDetail extends BaseScrollActivity
    implements ObservableScrollViewCallbacks {

    private TextView mainTitle;//内容Title
    private TextView title;//顶部Title
    private TextView result;
    private TextViewExpandableAnimation explainContent;//名词解释
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private RelativeLayout mTitle;
    private View mImageView;
    private ImageView mBackView;
    private ImageView mShare;
    private ViewCustomLoading progressBar;
    private Activity mContext = ActIndicatorsDetail.this;
    private BottomDialog dialog;
    private String indexId;
    private TagCloudView tagCloudView;//测定内容TagView
    private ScrollLinearLayout scrollLinearLayout;
    private ExternalLinearCommonAdapter externalAdapter;
    private List<IndiacatorDetail.Inspection> inspectionList;
    private List<ExternalLinearCommonBean> adapterList = new ArrayList<>();
    //private List <String> diseaseNameList = new ArrayList<>();
    //private List <Doctor.Disease> diseaseList;
    private String userId;
    private String token;
    private TextViewExpandableAnimation mTvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_indicatorsdetail);

        Intent intent = getIntent();
        indexId = intent.getStringExtra("indexId");
        Log.i("hcb", "indexId is" + indexId);
        initView();

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("token", "");

        mScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getHttpData();
    }


    private void initView() {

        tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud_view);
        scrollLinearLayout = (ScrollLinearLayout) findViewById(R.id.external_ll);
        mainTitle = (TextView) findViewById(R.id.tv_indexname);
        result = (TextView) findViewById(R.id.tv_result_content);
        mTvResult = (TextViewExpandableAnimation) findViewById(R.id.tv_cankaojieguo);
        explainContent = (TextViewExpandableAnimation) findViewById(R.id.tv_content1);

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


    /**
     * 网络请求部分
     */
    public void getHttpData() {
        HttpService.getHttpService().getIndicatorDetail(indexId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<IndiacatorDetail>() {
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
                    Logger.e("错误--" + e.toString());
                    mTvResult.setText("");
                    explainContent.setText("");
                }


                @Override
                public void onNext(IndiacatorDetail indiacatorDetail) {
                    Logger.v("错误--" + indiacatorDetail.toString());
                    mainTitle.setText(ZMBUtil.getNotEmpty(indiacatorDetail.getData().getIndexName()));
                    title.setText(ZMBUtil.getNotEmpty(indiacatorDetail.getData().getIndexName()));
                    explainContent.setText(ZMBUtil.getNotEmpty(indiacatorDetail.getData().getDescripiton()));
                    mTvResult.setText(ZMBUtil.getNotEmpty(indiacatorDetail.getData().getResult()));

                    //参考内容
                    inspectionList = indiacatorDetail.getData().getInspections();
                    for (int i = 0; i < inspectionList.size(); i++) {
                        String name = inspectionList.get(i).getName();
                        String insurance = "";
                        if (inspectionList.get(i).isInsurance()) {
                            insurance = "医保药";
                        } else {
                            insurance = "非医保药";
                        }
                        ExternalLinearCommonBean bean = new ExternalLinearCommonBean();
                        bean.setName(name);
                        bean.setSubName(insurance);
                        adapterList.add(bean);
                    }
                    externalAdapter = new ExternalLinearCommonAdapter(mContext, adapterList);
                    externalAdapter.setClickAdapterListener(adapterClickInterface);
                    scrollLinearLayout.setAdapter(externalAdapter);

                    //测定内容
                        /*diseaseList = indiacatorDetail.getData().getDiseases();
                        for (int i = 0;i<diseaseList.size();i++){
                            String name = diseaseList.get(i).getName();
                            diseaseNameList.add(name);
                        }
                        tagCloudView.setTags(diseaseNameList);
                        tagCloudView.setOnTagClickListener(onTagClickListener);*/
                }
            });
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
                        int id = Integer.parseInt(indexId);
//                        TreatMentShareBean bean = new TreatMentShareBean(userId, id,
//                            TreatMentShare.INDEX);
//                        ShareDialog.showShareDialog(mContext, bean, token);
                    }
                    break;

                //                case R.id.tv_dialog_cancel:
                //                    dialog.dismiss();
                //                    break;
            }
        }
    };

    /**
     * 测定内容Tag点击事件
     */
    //TagCloudView.OnTagClickListener onTagClickListener = new TagCloudView.OnTagClickListener() {
    //    @Override
    //    public void onTagClick(int position) {
    //        String diseaseId = diseaseList.get(position).getId()+"";
    //        Intent intent = new Intent(mContext,ActKnowTumorDetail.class);
    //        intent.putExtra("diseaseId",diseaseId);
    //        startActivity(intent);
    //    }
    //};

    /**
     * 参考测量Item点击事件
     */
    AdapterClickInterface adapterClickInterface = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            String inspectionId = inspectionList.get(position).getId() + "";
            Intent intent = new Intent(mContext, ActInspectionDetail.class);
            intent.putExtra("inspectionId", inspectionId);
            startActivity(intent);
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };


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
