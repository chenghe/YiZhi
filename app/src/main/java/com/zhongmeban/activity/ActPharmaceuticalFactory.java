package com.zhongmeban.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.adapter.ExternalLinearCommonAdapter;
import com.zhongmeban.bean.ExternalLinearCommonBean;
import com.zhongmeban.bean.MedicineFactoryDetail;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.ViewCustomLoading;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:药箱子-品牌详情
 * Created by Chengbin He on 2016/4/7.
 */
public class ActPharmaceuticalFactory extends BaseScrollActivity
    implements ObservableScrollViewCallbacks {

    private TextView mainTitle;//内容Title
    private TextView title;//顶部Title
    private TextView result;
    private TextView explainContent;//项目简介
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private RelativeLayout mTitle;
    private View mImageView;
    private ImageView mBackView;
    private ImageView ivPain;
    private ImageView mShare;
    private ViewCustomLoading progressBar;
    private Context mContext = ActPharmaceuticalFactory.this;
    private BottomDialog dialog;
    private String manufacturerId;
    private ScrollLinearLayout scrollLinearLayout;
    private ExternalLinearCommonAdapter mExternalAdapter;
    private List<MedicineFactoryDetail.Medicine> medicineList;
    private List<ExternalLinearCommonBean> adapterList = new ArrayList<>();
    private String chemicalName;
    private String medicineName;
    private String showName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pharamaceutical_factory);
        Intent intent = getIntent();
        manufacturerId = intent.getStringExtra("manufacturerId");

        initView();
        mScrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        getHttpData();
    }


    private void initView() {

        scrollLinearLayout = (ScrollLinearLayout) findViewById(R.id.external_ll);

        mainTitle = (TextView) findViewById(R.id.tv_name);
        result = (TextView) findViewById(R.id.tv_subname);
        ivPain = (ImageView) findViewById(R.id.iv_pain);
        explainContent = (TextView) findViewById(R.id.tv_content1);

        mImageView = findViewById(R.id.rl_image);
        progressBar = (ViewCustomLoading) findViewById(R.id.progressBar);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);

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
        HttpService.getHttpService().getMedicineIconDetail(manufacturerId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<MedicineFactoryDetail>() {
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
                    Log.i("hcb", "e " + e);
                }


                @Override
                public void onNext(MedicineFactoryDetail medicineFactoryDetail) {
                    Log.i("hcb", "onNext");
                    mainTitle.setText(medicineFactoryDetail.getData().getManufacturerName());
                    title.setText(medicineFactoryDetail.getData().getManufacturerName());
                    result.setText(medicineFactoryDetail.getData().getManufacturerName());
                    explainContent.setText(medicineFactoryDetail.getData().getDescripiton());
                    medicineList = medicineFactoryDetail.getData().getMedicines();
                    if (medicineList != null) {
                        Log.i("hcb", "medicineList.size" + medicineList.size());
                        for (int i = 0; i < medicineList.size(); i++) {
                            chemicalName = medicineList.get(i).getChemicalName();
                            medicineName = medicineList.get(i).getMedicineName();
                            showName = medicineList.get(i).getShowName();
                            ExternalLinearCommonBean bean = new ExternalLinearCommonBean();
                            bean.setName(showName);
                            bean.setSubName(chemicalName);
                            adapterList.add(bean);
                        }
                        mExternalAdapter = new ExternalLinearCommonAdapter(mContext, adapterList);
                        mExternalAdapter.setClickAdapterListener(adapterClickInterface);
                        scrollLinearLayout.setAdapter(mExternalAdapter);
                    }

                }
            });
    }


    /**
     * 相关药品Item点击事件
     */
    AdapterClickInterface adapterClickInterface = new AdapterClickInterface() {
        @Override
        public void onItemClick(View v, int position) {
            String medicineId = medicineList.get(position).getMedicineId();
            Intent intent = new Intent(mContext, ActPharmaceutiaclDetail.class);
            //            intent.putExtra("chemicalName",chemicalName);
            intent.putExtra(ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID, medicineId);
            //            intent.putExtra("medicineName",medicineName);
            startActivity(intent);
        }


        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.iv_share:

                    BottomDialog.Builder builder = new BottomDialog.Builder(mContext);
                    dialog = new BottomDialog(mContext, builder);
                    View view = LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_share_dialog, null);
                    TextView cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
                    cancel.setOnClickListener(onClickListener);
                    builder.setView(view);
                    dialog = builder.showDialog();
                    break;

                case R.id.tv_dialog_cancel:
                    dialog.dismiss();
                    break;
            }
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

