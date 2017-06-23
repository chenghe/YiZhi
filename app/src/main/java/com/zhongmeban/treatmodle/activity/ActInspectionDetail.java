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
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.ExternalLinearCommonAdapter;
import com.zhongmeban.bean.ExternalLinearCommonBean;
import com.zhongmeban.bean.InspectionDetail;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.genericity.TreatMentShare;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.ShareDialog;
import com.zhongmeban.view.TextViewExpandableAnimation;
import com.zhongmeban.view.ViewCustomLoading;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:检查项目详情页面
 * time：2016/1/18 15:13
 */
public class ActInspectionDetail extends BaseScrollActivity implements ObservableScrollViewCallbacks {


    private TextView mainTitle;//内容Title
    private TextView title;//顶部Title
    private TextView result;
    private TextView tvInspect;
    private TextView tvPain;
    private TextViewExpandableAnimation explainContent;//项目简介
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private RelativeLayout mTitle;
    private View mImageView;
    private ImageView mBackView;
    private ImageView ivPain;
    private ImageView mShare;
    private ViewCustomLoading progressBar;
    private Activity mContext = ActInspectionDetail.this;
    private BottomDialog dialog;
    private String inspectionId;
    private ScrollLinearLayout scrollLinearLayout;
    private ExternalLinearCommonAdapter externalAdapter;
    private List <ExternalLinearCommonBean> adapterList = new ArrayList<>();
    private List <InspectionDetail.Index> indexList;

    private String userId;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_inspectiondetail);


        Intent intent = getIntent();
        inspectionId = intent.getStringExtra("inspectionId");
        initView();

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = sp.getString("userId","");
        token = sp.getString("token","");

        mScrollView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getHttpData();
    }

    private void initView() {

        scrollLinearLayout = (ScrollLinearLayout) findViewById(R.id.external_ll);
        mainTitle = (TextView) findViewById(R.id.tv_indexname);
        result = (TextView) findViewById(R.id.tv_result);
        ivPain = (ImageView) findViewById(R.id.iv_pain);
        tvInspect = (TextView) findViewById(R.id.tv_inspect);
        tvPain = (TextView) findViewById(R.id.tv_pain);
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
        HttpService.getHttpService().getInspectionDetail(inspectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InspectionDetail>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

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
                        Log.i("hcb", "onError");
                        Log.i("hcb", "e is" +e);
                    }

                    @Override
                    public void onNext(InspectionDetail inspectionDetail) {
                        Log.i("hcb", "onNext");
                        mainTitle.setText(inspectionDetail.getData().getInspectionName());
                        title.setText(inspectionDetail.getData().getInspectionName());
                        if(inspectionDetail.getData().getIsInsurance()){
                            result.setText("医保");
                        }else{
                            result.setText("非医保");
                        }
                        switch (inspectionDetail.getData().getDiscomfortIndex()){
                            case 1:
                                ivPain.setImageResource(R.drawable.happy);
                                tvPain.setText("无疼痛");
                                break;
                            case 2:
                                ivPain.setImageResource(R.drawable.bad);
                                tvPain.setText("轻微疼痛");
                                break;
                            case 3:
                                ivPain.setImageResource(R.drawable.pain);
                                tvPain.setText("较高疼痛");
                                break;
                        }
                        tvInspect.setText(inspectionDetail.getData().getInspectionMethodName());
                        explainContent.setText(inspectionDetail.getData().getDescription());
                        //result.setText(indiacatorDetail.getData().getResult());

                        indexList = inspectionDetail.getData().getIndexs();
                        for(int i = 0;i<indexList.size();i++){
                            String name = indexList.get(i).getIndexName();
                            String result = indexList.get(i).getResult();

                            ExternalLinearCommonBean bean = new ExternalLinearCommonBean();
                            bean.setName(name);
                            bean.setSubName(result);
                            adapterList.add(bean);
                        }
                        externalAdapter = new ExternalLinearCommonAdapter(mContext,adapterList);
                        externalAdapter.setClickAdapterListener(adapterClickInterface);
                        scrollLinearLayout.setAdapter(externalAdapter);

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

                    if (!TextUtils.isEmpty(userId)){
                        int id = Integer.parseInt(inspectionId);
//                        TreatMentShareBean bean = new TreatMentShareBean(userId,id, TreatMentShare.INSPECTION);
//                        ShareDialog.showShareDialog(mContext,bean,token);
                    }
                    break;

//                case R.id.tv_dialog_cancel:
//                    dialog.dismiss();
//                    break;
            }
        }
    };

    /**
     * 参考指标Item点击事件
     */
    AdapterClickInterface adapterClickInterface = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            String indexId = indexList.get(position).getIndexId();
            Intent intent = new Intent(mContext,ActIndicatorsDetail.class);
            intent.putExtra("indexId",indexId);
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
