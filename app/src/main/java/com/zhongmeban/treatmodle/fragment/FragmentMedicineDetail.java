package com.zhongmeban.treatmodle.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.treatmodle.activity.ActKnowTumorDetail;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.activity.ActivitySameMedicine;
import com.zhongmeban.bean.DrugOverview;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.CircularProgressBar.CircularProgressBar;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.ScrollView.BaseScrollFragment;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import com.zhongmeban.view.TextViewExpandableAnimation;
import java.util.ArrayList;
import java.util.List;
import me.next.tagview.TagCloudView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 药品详情概要Fragment
 * Created by Chengbin He on 2016/5/16.
 */
public class FragmentMedicineDetail extends BaseScrollFragment {

    private Activity parentActivity;
    private ActPharmaceutiaclDetail mActivity;
    private LinearLayout llContent;
    private CircularProgressBar progressBar;
    private RelativeLayout rlSameMedicine;
    private TextView tvSameMedicine;
    private TextView tvFactory;//厂商信息
    private TextView tvChemicalName;//主要成分
    private Subscription mSubscription;
    private TagCloudView tagCloudView;//适应症TagView
    private List<DrugOverview.DiseasesBean> httpList = new ArrayList<>();
    private List<String> tumorName = new ArrayList<String>();
    private boolean isScrolled;
    private boolean isImport;//是否为进口药
    private boolean isMedicalInsurance;//是否为医保药
    private boolean isPrescription;//是否为处方药
    private boolean isWestern;//是否为西药
    private boolean isSpecial;//特种药
    private boolean isChinese;//是否为中药
    private String priceMax = "";
    private ObservableScrollView scrollView;
    private int sameMeidicne;

    private TextViewExpandableAnimation mTvYongFa;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (Activity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_detail, container, false);

        scrollView= (ObservableScrollView) view.findViewById(
            R.id.scroll);
        scrollView.setTouchInterceptionViewGroup(
            (ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (ActPharmaceutiaclDetail) parentActivity;
        getHttpData(mActivity.medicineId);
    }


    private void initView(View view) {
        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        progressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);
        llContent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mTvYongFa  = (TextViewExpandableAnimation) view.findViewById(R.id.tv_yongfa_yongliang);
        tvFactory = (TextView) view.findViewById(R.id.tv_content1);
        tvChemicalName = (TextView) view.findViewById(R.id.tv_content2);

        tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud_view);
//        tagCloudView.setOnTagClickListener(onTagClickListener);

        rlSameMedicine = (RelativeLayout) view.findViewById(R.id.rl_same_medicine);
        rlSameMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sameMeidicne<1) return;

                Intent intent = new Intent(parentActivity, ActivitySameMedicine.class);
                intent.putExtra("medicineId", mActivity.medicineId);
                startActivityForResult(intent, 1);
            }
        });
        tvSameMedicine = (TextView) view.findViewById(R.id.tv_same_medicine);
    }


    /**
     * 联网获取网络数据
     */
    private void getHttpData(String medicineId) {
        String userId = (String) SPUtils.getParams(getActivity().getApplicationContext(),
            SPInfo.UserKey_userId, "", SPInfo.SPUserInfo);

        mSubscription = HttpService.getHttpService().getMedicineDetail(medicineId, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<DrugOverview>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "http onCompleted");
                    llContent.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mActivity.setProgressBarShow(false);
                    mActivity.initLabel(isImport, isMedicalInsurance, isPrescription,
                        isWestern, isSpecial, isChinese, priceMax);
                }


                @Override
                public void onError(Throwable e) {
                    Logger.i("e " + e);
                    progressBar.setVisibility(View.GONE);
                    mActivity.setProgressBarShow(false);
                    ToastUtil.showText(getActivity(),getString(R.string.Load_fail));
                }


                @Override
                public void onNext(DrugOverview drugOverview) {
                    if (drugOverview == null || drugOverview.getErrorCode() != 0) return;

                    httpList.clear();
                    tumorName.clear();
                    httpList = drugOverview.getData().getDiseases();

                    //isScrolled = drugOverview.getData().isSpecial();
                    //isImport = drugOverview.getData().isImport();
                    //isMedicalInsurance = drugOverview.getData().isMedicalInsurance();
                    //isPrescription = drugOverview.getData().isPrescription();
                    //isWestern = drugOverview.getData().isWestern();
                    //isSpecial = drugOverview.getData().isSpecial();
                    //isChinese = drugOverview.getData().isChinese();
                    priceMax = drugOverview.getData().getPriceMax() + "";
                    sameMeidicne = drugOverview.getData().getSameMedicineNum();
                    tvSameMedicine.setText(sameMeidicne+ "种");
                    tvFactory.setText(drugOverview.getData().getManufacturers().get(0).getName());
                    tvChemicalName.setText(drugOverview.getData().getChemicalName());
                    mTvYongFa.setText(drugOverview.getData().getUseInfo());
                    for (int i = 0; i < httpList.size(); i++) {
                        String tagName = httpList.get(i).getName();
                        tumorName.add(tagName);
                    }
                    //更新是否收藏
                    mActivity.isFavorite =drugOverview.getData().isIsFavorite();
                    mActivity.updateTopBarUI(drugOverview.getData().isIsFavorite());
                    mActivity.handUIText(drugOverview.getData());
                    Logger.v(drugOverview.toString());
                    tagCloudView.setTags(tumorName);
                }
            });
    }


    public void notifyData(String medicineId) {
        llContent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getHttpData(medicineId);

        scrollView.post(new Runnable() {
            @Override public void run() {
                //scrollView.smoothScrollTo(0,0);
                scrollView.scrollTo(0,-(360));
                //scrollView.scrollTo(0,-getResources().getDimensionPixelSize(
                //    R.dimen.flexible_space_image_height));
            }
        });

    }


    /**
     * Tag 点击事件
     */
    TagCloudView.OnTagClickListener onTagClickListener = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int position) {
            String diseaseId = httpList.get(position).getId() + "";
            Intent intent = new Intent(parentActivity, ActKnowTumorDetail.class);
            intent.putExtra("diseaseId", diseaseId);
            startActivity(intent);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("hcb", "FragmentMedicineDetail onDestroy");
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
