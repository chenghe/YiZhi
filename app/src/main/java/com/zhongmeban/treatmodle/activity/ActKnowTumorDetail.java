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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TumorInfoAdapter;
import com.zhongmeban.bean.DiseaseDetail;
import com.zhongmeban.bean.TnmDbsBean;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.ToastUtil;
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
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description:肿瘤详情页面
 * time：2016/1/26 11:41
 */
public class ActKnowTumorDetail extends BaseScrollActivity
    implements ObservableScrollViewCallbacks {
    private TextView mainTitle;//内容Title
    private TextView title;//顶部RelativeLayout title
    private TextViewExpandableAnimation mTvOrganFunction;//器官功能
    private TextViewExpandableAnimation mTvDiseaseExplain;//疾病详解
    private TextViewExpandableAnimation mTvModeType;//分类类型
    private TextViewExpandableAnimation mTvSymptom;//常见症状
    private TextView mTvStage0;//描述
    private TextView mTvStage1;//描述
    private TextView mTvStage2;//描述
    private TextView mTvIntroducation;//简介
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private RelativeLayout mTitle;//顶部RelativeLayout
    private View mImageView;
    private ImageView mBackView;
    private ImageView mShare;
    private ViewCustomLoading progressBar;
    private Activity mContext = ActKnowTumorDetail.this;
    private BottomDialog dialog;
    private String diseaseId;
    private Subscription mSubscription;
    private ScrollLinearLayout scrollLinearLayout;
    private TagCloudView tagCloudView;//定性手段TagView
    private TagCloudView tagCloudView2;//定量手段TagView
    private String userId;
    private String token;

    private List<String> inspectionName = new ArrayList<String>();
    private List<String> inspectionName2 = new ArrayList<String>();
    private List<DiseaseDetail.DataBean.InspectionsBean> inspectionList = new ArrayList<>();
    private List<DiseaseDetail.DataBean.InspectionsBean> inspectionList2 = new ArrayList<>();
    private List<TnmDbsBean> tnmDbsList = new ArrayList<>();

    private LinearLayout mExpendLayout1;
    private TextView mExpendTv1;
    private ImageView mExpendIv1;
    private LinearLayout mExpendLayout2;
    private TextView mExpendTv2;
    private ImageView mExpendIv2;
    private boolean mExpendState1;
    private boolean mExpendState2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konwtumer_detail);

        Intent intent = getIntent();
        diseaseId = intent.getStringExtra("diseaseId");
        Log.i("hcb", "diseaseId is" + diseaseId);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("token", "");

        initView();
        mScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        getHttpData();
    }


    private void initView() {

        scrollLinearLayout = (ScrollLinearLayout) findViewById(R.id.external_ll);

        tagCloudView = (TagCloudView) findViewById(R.id.tag_cloud_view);
        tagCloudView2 = (TagCloudView) findViewById(R.id.tag_cloud_view2);
        tagCloudView.setOnTagClickListener(onTagClickListener);
        tagCloudView2.setOnTagClickListener(onTagClickListener2);

        mainTitle = (TextView) findViewById(R.id.tv_tumorname);
        mTvIntroducation = (TextView) findViewById(R.id.tv_result);
        mTvOrganFunction = (TextViewExpandableAnimation) findViewById(R.id.tv_organ_function);
        //mTvOrganFunction.setText(getString(R.string.expend_test_content));

        mTvDiseaseExplain = (TextViewExpandableAnimation) findViewById(R.id.tv_disease_explain);
        mTvModeType = (TextViewExpandableAnimation) findViewById(R.id.tv_mode_type);
        mTvStage0 = (TextView) findViewById(R.id.id_tv_stage0);
        mTvStage1 = (TextView) findViewById(R.id.id_tv_stage1);
        mTvStage2 = (TextView) findViewById(R.id.id_tv_stage2);
        mTvSymptom = (TextViewExpandableAnimation) findViewById(R.id.tv_symptom);

        mExpendLayout1 = (LinearLayout) findViewById(R.id.id_expend_layout1);
        mExpendIv1 = (ImageView) findViewById(R.id.id_expend_iv1);
        mExpendTv1 = (TextView) findViewById(R.id.id_expend_tv1);
        mExpendLayout2 = (LinearLayout) findViewById(R.id.id_expend_layout2);
        mExpendIv2 = (ImageView) findViewById(R.id.id_expend_iv2);
        mExpendTv2 = (TextView) findViewById(R.id.id_expend_tv2);

        mImageView = findViewById(R.id.rl_image);
        progressBar = (ViewCustomLoading) findViewById(R.id.progressBar);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);
        mExpendLayout1.setOnClickListener(onClickListener);
        mExpendLayout2.setOnClickListener(onClickListener);

        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(onClickListener);

        mTitle = (RelativeLayout) findViewById(R.id.ll_title);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.title_bg)));

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
        //////////////////////////////9 是懒癌测试数据/////////////////////////////////
        //mSubscription = HttpService.getHttpService().getTumorDetail("9")
        mSubscription = HttpService.getHttpService().getTumorDetail(diseaseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<DiseaseDetail>() {
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
                    Logger.e("http e" + e);
                    progressBar.setVisibility(View.GONE);
                    ToastUtil.showText(ActKnowTumorDetail.this,"加载失败");
                }


                @Override
                public void onNext(DiseaseDetail diseaseDetail) {
                    Logger.i(diseaseDetail.toString());
                    mainTitle.setText(diseaseDetail.getData().getDiseaseName());
                    title.setText(diseaseDetail.getData().getDiseaseName());
                    mTvIntroducation.setText(diseaseDetail.getData().getNotes());

                    mTvOrganFunction.setText(diseaseDetail.getData().getOrganFunction());


                    mTvSymptom.setText(diseaseDetail.getData().getSymptom());
                    mTvModeType.setText(diseaseDetail.getData().getModeType());
                    mTvStage0.setText(diseaseDetail.getData().getEarlyStage());
                    mTvStage1.setText(diseaseDetail.getData().getInterimStage());
                    mTvStage2.setText(diseaseDetail.getData().getLaterStage());
                    mTvDiseaseExplain.setText(diseaseDetail.getData().getDiseaseExplain());

                    if (diseaseDetail.getData().getTnmDbs() != null) {
                        mergeTnmDbs(diseaseDetail);
                    }
                    List<DiseaseDetail.DataBean.InspectionsBean> inspections
                        = diseaseDetail.getData().getInspections();

                    for (DiseaseDetail.DataBean.InspectionsBean insBean : inspections) {
                        if (insBean.getType() == 1) {
                            inspectionList.add(insBean);
                        } else if (insBean.getType() == 2) {
                            inspectionList2.add(insBean);
                        }
                    }

                    for (int i = 0; i < inspectionList.size(); i++) {
                        String tagName = inspectionList.get(i).getName();
                        inspectionName.add(tagName);
                    }
                    for (int i = 0; i < inspectionList2.size(); i++) {
                        String tagName = inspectionList2.get(i).getName();
                        inspectionName2.add(tagName);
                    }

                    tagCloudView.setTags(inspectionName);
                    tagCloudView2.setTags(inspectionName2);
                    tagCloudView2.measure(View.MeasureSpec.AT_MOST,View.MeasureSpec.EXACTLY);
                    tagCloudView.measure(View.MeasureSpec.AT_MOST,View.MeasureSpec.EXACTLY);
                   final int expendHeight = getResources().getDimensionPixelSize(
                        R.dimen.expend_tag_size);
                    tagCloudView.post(new Runnable() {
                        @Override public void run() {
                            int measuredHeight = tagCloudView2.getMeasuredHeight();
                            Logger.i(
                                tagCloudView2.sizeHeight+"==测量高度---" + measuredHeight + "--真是--" + tagCloudView2.totalHeight);
                                if (expendHeight>tagCloudView.totalHeight){
                                    mExpendLayout1.setVisibility(View.GONE);
                                } else{
                                    mExpendLayout1.setVisibility(View.VISIBLE);
                                }
                        }
                    });
                    tagCloudView2.post(new Runnable() {
                        @Override public void run() {
                            if (expendHeight>tagCloudView.totalHeight){
                                mExpendLayout2.setVisibility(View.GONE);
                            } else{
                                mExpendLayout2.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                    //tagCloudView.set
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
                    //                    BottomDialog.Builder builder = new BottomDialog.Builder(mContext);
                    //                    dialog = new BottomDialog(mContext, builder);
                    //                    View view = LayoutInflater.from(mContext)
                    //                        .inflate(R.layout.layout_share_dialog, null);
                    //                    TextView cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                    //                    cancel.setOnClickListener(onClickListener);
                    //                    builder.setView(view);
                    //                    dialog = builder.showDialog();
                    if (!TextUtils.isEmpty(userId)) {
                        int tumorId = Integer.parseInt(diseaseId);
//                        TreatMentShareBean bean = new TreatMentShareBean(userId, tumorId,
//                            TreatMentShare.DISEASE);
//                        ShareDialog.showShareDialog(mContext, bean, token);
                    }
                    break;
                //
                //                case R.id.tv_dialog_cancel:
                //                    dialog.dismiss();
                //                    break;
                case R.id.id_expend_layout1:
                    if (mExpendState1) {
                        mExpendState1 = false;

                        mExpendIv1.setImageResource(R.drawable.open);
                        mExpendTv1.setText("展开");
                        ViewGroup.LayoutParams layoutParams = tagCloudView.getLayoutParams();

                        layoutParams.height = getResources().getDimensionPixelSize(
                            R.dimen.expend_tag_size);
                        tagCloudView.setLayoutParams(layoutParams);

                        tagCloudView.post(new Runnable() {
                            @Override public void run() {
                                mScrollView.smoothScrollTo(0,-(int)(tagCloudView.getY())+ ZMBUtil.getHeight());
                            }
                        });

                    } else {
                        mExpendState1 = true;
                        mExpendIv1.setImageResource(R.drawable.close);
                        mExpendTv1.setText("收起");
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                        //tagCloudView.measure(View.MeasureSpec.AT_MOST,View.MeasureSpec.AT_MOST);
                        //layoutParams.height = tagCloudView.totalHeight+getResources().getDimensionPixelSize(R.dimen.headerView_time_height);
                        //int multiTotalHeight = tagCloudView.getMultiTotalHeight(0, 0);
                        //Logger.d("hei--"+layoutParams.height+"--测量--"+multiTotalHeight);

                        tagCloudView.setLayoutParams(layoutParams);
                        tagCloudView.postInvalidate();

                    }

                    break;
                case R.id.id_expend_layout2:

                    if (mExpendState2) {
                        mExpendState2 = false;
                        mExpendIv2.setImageResource(R.drawable.open);
                        mExpendTv2.setText("展开");
                        ViewGroup.LayoutParams layoutParams = tagCloudView2.getLayoutParams();

                        layoutParams.height = getResources().getDimensionPixelSize(
                            R.dimen.expend_tag_size);
                        tagCloudView2.setLayoutParams(layoutParams);

                        tagCloudView2.post(new Runnable() {
                            @Override public void run() {
                                mScrollView.smoothScrollTo(0,-(int)(tagCloudView2.getY())+ZMBUtil.getHeight());
                            }
                        });

                    } else {
                        mExpendState2 = true;
                        mExpendIv2.setImageResource(R.drawable.close);
                        mExpendTv2.setText("收起");
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                        tagCloudView2.setLayoutParams(layoutParams);
                        tagCloudView2.postInvalidate();


                    }
                    break;
            }
        }
    };

    /**
     * Tag 点击事件 定性手段
     */
    TagCloudView.OnTagClickListener onTagClickListener = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int position) {
            String inspectionId = inspectionList.get(position).getId() + "";
            Intent intent = new Intent(mContext, ActInspectionDetail.class);
            intent.putExtra("inspectionId", inspectionId);
            startActivity(intent);
        }
    };
    /**
     * Tag 点击事件 定量手段
     */
    TagCloudView.OnTagClickListener onTagClickListener2 = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int position) {
            String inspectionId = inspectionList2.get(position).getId() + "";
            Intent intent = new Intent(mContext, ActInspectionDetail.class);
            intent.putExtra("inspectionId", inspectionId);
            startActivity(intent);
        }
    };


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }


    @Override
    public void onScrollChanged(final int scrollY, boolean firstScroll, boolean dragging) {
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
        tagCloudView.post(new Runnable() {
            @Override public void run() {
                int[] location = new  int[2] ;
                tagCloudView.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                tagCloudView.getLocationOnScreen(location);

                //Logger.v(location[1]+"--坐标--gety--"+tagCloudView.getY()+"scrolly--"+scrollY+"--getY00=="+tagCloudView.getScrollY());
            }
        });


    }


    @Override
    public void onDownMotionEvent() {

    }


    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    /**
     * T、N、M 的titile和具体的分期解释合并成一个集合
     */
    public void mergeTnmDbs(DiseaseDetail diseaseDetail) {
        tnmDbsList.add(new TnmDbsBean("T", diseaseDetail.getData().getTnotes(), 0));
        Observable.from(diseaseDetail.getData().getTnmDbs())
            .filter(new Func1<DiseaseDetail.DataBean.TnmDbsBean, Boolean>() {
                @Override
                public Boolean call(DiseaseDetail.DataBean.TnmDbsBean tnmDbsBean) {
                    return tnmDbsBean.getType() == 1;
                }
            })
            .subscribe(
                new Action1<DiseaseDetail.DataBean.TnmDbsBean>() {
                    @Override
                    public void call(DiseaseDetail.DataBean.TnmDbsBean tnmDbsBean) {
                        tnmDbsList.add(
                            new TnmDbsBean(tnmDbsBean.getTnmName(), tnmDbsBean.getNotes(), 1));
                    }
                });
        tnmDbsList.add(new TnmDbsBean("N", diseaseDetail.getData().getNnotes(), 0));
        Observable.from(diseaseDetail.getData().getTnmDbs())
            .filter(new Func1<DiseaseDetail.DataBean.TnmDbsBean, Boolean>() {
                @Override
                public Boolean call(DiseaseDetail.DataBean.TnmDbsBean tnmDbsBean) {
                    return tnmDbsBean.getType() == 2;
                }
            })
            .subscribe(
                new Action1<DiseaseDetail.DataBean.TnmDbsBean>() {
                    @Override
                    public void call(DiseaseDetail.DataBean.TnmDbsBean tnmDbsBean) {
                        tnmDbsList.add(
                            new TnmDbsBean(tnmDbsBean.getTnmName(), tnmDbsBean.getNotes(), 1));
                    }
                });
        tnmDbsList.add(new TnmDbsBean("M", diseaseDetail.getData().getMnotes(), 0));
        Observable.from(diseaseDetail.getData().getTnmDbs())
            .filter(new Func1<DiseaseDetail.DataBean.TnmDbsBean, Boolean>() {
                @Override
                public Boolean call(DiseaseDetail.DataBean.TnmDbsBean tnmDbsBean) {
                    return tnmDbsBean.getType() == 3;
                }
            })
            .subscribe(
                new Action1<DiseaseDetail.DataBean.TnmDbsBean>() {
                    @Override
                    public void call(DiseaseDetail.DataBean.TnmDbsBean tnmDbsBean) {
                        tnmDbsList.add(
                            new TnmDbsBean(tnmDbsBean.getTnmName(), tnmDbsBean.getNotes(), 1));
                    }
                });

        scrollLinearLayout.setAdapter(new TumorInfoAdapter(mContext,
            tnmDbsList));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("hcb", "ActKnowTumorDetail onDestroy");
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
