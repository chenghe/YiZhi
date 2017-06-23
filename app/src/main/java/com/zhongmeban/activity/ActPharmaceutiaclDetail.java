package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TabViewFragmentPagerAdapter;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.DrugOverview;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.fragment.FragmentMedicineDocu;
import com.zhongmeban.fragment.FragmentMedicineQuestion;
import com.zhongmeban.mymodle.activity.ActivityMyMedicine;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.treatmodle.fragment.FragmentHospDetail;
import com.zhongmeban.treatmodle.fragment.FragmentMedicineDetail;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.ScrollView.BaseScrollActivity;
import com.zhongmeban.utils.ScrollView.BaseScrollListViewFragment;
import com.zhongmeban.utils.ScrollView.ObservableListView;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.utils.ScrollView.ScrollState;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.ScrollView.Scrollable;
import com.zhongmeban.utils.ScrollView.TouchInterceptionFrameLayout;
import com.zhongmeban.utils.SlidingTabLayout;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ViewCustomLoading;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rx.Subscriber;

/**
 * Description:药箱子-药品详情
 * Created by Chengbin He on 2016/4/7.
 */
public class ActPharmaceutiaclDetail extends BaseScrollActivity
    implements ObservableScrollViewCallbacks {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final int INVALID_POINTER = -1;
    public static final String EXTRA_MEDICAL_ID = "ectra_medical_id";
    //private static final String[] TITLES = new String[]{"概述", "问答","文献"};
    private static final String[] TITLES = new String[] { "概述" };

    private ImageView mImageView;
    private View mOverlayView;
    private TouchInterceptionFrameLayout mInterceptionLayout;
    private VelocityTracker mVelocityTracker;
    private OverScroller mScroller;
    private float mBaseTranslationY;
    private int mMaximumVelocity;
    private int mActivePointerId = INVALID_POINTER;
    private int mSlop;
    private int titleHeight;
    private boolean mScrolled;
    //    private boolean isImport;//是否为进口药
    //    private boolean medicalInsurance;//是否为医保药
    //    private boolean prescription;//是否为处方药
    //    private boolean western;//是否为西药
    //    private boolean special;//特种药
    //    private boolean chinese;//是否为中药
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private ViewPager mPager;
    private Activity mContext = ActPharmaceutiaclDetail.this;
    private TabViewFragmentPagerAdapter mPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private RelativeLayout mTitle;
    private TextView tvLabel;//药品类型
    private TextView tvSubTitle;//药品名称
    private TextView tvTitle;//顶部Title
    private TextView tvLabel2;//药品医学类型
    private TextView tvCost;//药品价格
    private LinearLayout llCost;
    private ImageView mBackView;
    private ImageView mShare;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private BottomDialog dialog;
    private String isImportName = "";
    private String medicalInsuranceName = "";
    private String prescriptionName = "";
    private String westernName = "";
    private String specialName = "";
    private String chineseName = "";
    //    private String priceMax = "";
    public String medicineName = "";
    public String chemicalName = "";
    public String medicineId;
    private FragmentMedicineQuestion fragmentMedicineQuestion;//问答
    private FragmentMedicineDetail fragmentMedicineDetail;//概述
    private FragmentMedicineDocu fragmentMedicineDocu;//文献
    private String userId;
    private String token;
    private ViewCustomLoading progressBar;

    public boolean isFavorite;
    private float alpha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_medicine_detail);

        Intent intent = getIntent();
        medicineId = intent.getStringExtra(EXTRA_MEDICAL_ID);

        if (TextUtils.isEmpty(medicineId)) {

            finish();
            return;
        }

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sp.getString("userId", "");
        token = sp.getString("token", "");

        initView();
    }


    public void initLabel(boolean isImport, boolean isMedicalInsurance, boolean isPrescription,
                          boolean isWestern, boolean isSpecial, boolean isChinese, String priceMax) {
        if (isImport) {
            isImportName = "进口药 ";
        }
        if (isMedicalInsurance) {
            medicalInsuranceName = "医保药 ";
        }
        if (isPrescription) {
            prescriptionName = "处方药 ";
        }
        if (isWestern) {
            westernName = "西药 ";
        }
        if (isSpecial) {
            specialName = "特效药 ";
        }
        if (isChinese) {
            chineseName = "中药 ";
        }
    }


    private void initView() {
        //初始化title
        mTitle = (RelativeLayout) findViewById(R.id.ll_title);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.title_bg)));

        mImageView = (ImageView) findViewById(R.id.image);
        progressBar = (ViewCustomLoading) findViewById(R.id.progressBar_medicine);
        progressBar.setVisibility(View.VISIBLE);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setTextColor(ScrollUtils.getColorWithAlpha(0,
            getResources().getColor(R.color.top_text)));

        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);

        tvLabel = (TextView) findViewById(R.id.tv_label);
        tvLabel.setTextSize(16);

        tvCost = (TextView) findViewById(R.id.tv_cost);

        tvLabel2 = (TextView) findViewById(R.id.tv_label2);
        tvLabel2.setVisibility(View.VISIBLE);
        //tvLabel2.setText(isImportName+medicalInsuranceName+prescriptionName+westernName
        //                    +specialName+chineseName);

        llCost = (LinearLayout) findViewById(R.id.ll_cost);
        llCost.setVisibility(View.VISIBLE);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);
        mShare.setVisibility(View.VISIBLE);

        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(onClickListener);

        //初始化相关Fragment

        fragmentMedicineDetail = new FragmentMedicineDetail();
        fragmentMedicineQuestion = new FragmentMedicineQuestion();
        fragmentMedicineDocu = new FragmentMedicineDocu();
        mFragments.add(fragmentMedicineDetail);
        mFragments.add(fragmentMedicineQuestion);
        mFragments.add(fragmentMedicineDocu);

        mPagerAdapter = new TabViewFragmentPagerAdapter(getSupportFragmentManager(), mFragments,
            TITLES);

        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(
            R.dimen.flexible_space_image_height);
        mTabHeight = 0;
        //mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            @Override
            public void onPageSelected(int position) {
                Log.i("hcb", "onPageSelected position is " + position);

                switch (position) {
                    case 1:
                        fragmentMedicineQuestion.notifyData();
                        break;
                    case 2:
                        mPager.setOffscreenPageLimit(2);
                        break;
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("hcb", "onPageScrollStateChanged state is " + state);
            }
        });
        //mSlidingTabLayout.setViewPager(mPager);

        mOverlayView = findViewById(R.id.overlay);
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(
            R.dimen.flexible_space_image_height);
        mTabHeight = 0;
        //mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        titleHeight = getResources().getDimensionPixelSize(R.dimen.title_height);

        findViewById(R.id.pager_wrapper).setPadding(0, mFlexibleSpaceHeight + mTabHeight, 0, 0);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        slidingTabLayout.setViewPager(mPager);

        ((FrameLayout.LayoutParams) slidingTabLayout.getLayoutParams()).topMargin
            = mFlexibleSpaceHeight;

        ViewConfiguration vc = ViewConfiguration.get(this);
        mSlop = vc.getScaledTouchSlop();
        mMaximumVelocity = vc.getScaledMaximumFlingVelocity();
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.container);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mScroller = new OverScroller(getApplicationContext());
        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams lp
                    = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                lp.height = getScreenHeight() + mFlexibleSpaceHeight;
                mInterceptionLayout.requestLayout();

                updateFlexibleSpace();
            }
        });

    }


    public void handUIText(DrugOverview.DrugOverviewData mCurrentBean) {

        String yibao = Constants.getMedicalInsurance(mCurrentBean.getInsurance());

        String chufangyao = Constants.isSpicalMedical(mCurrentBean.getPrescription())
                            ? "处方药"
                            : "非处方";
        String zhongyao = Constants.getMedicineType(mCurrentBean.getClassify());

        tvLabel2.setText(yibao + " " + chufangyao + " " + zhongyao);

        tvCost.setText(ZMBUtil.getFormatPrice(mCurrentBean.getPriceMax()));
        medicineName = mCurrentBean.getMedicineName();

        chemicalName = mCurrentBean.getChemicalName();

        Logger.e("medicineId" + medicineId);

        tvSubTitle.setText(mCurrentBean.getShowName());
        tvTitle.setText(medicineName);
        tvLabel.setText(chemicalName);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, ActivityMyMedicine.class);
        intent.putExtra("isFavorite", isFavorite);
        setResult(RESULT_OK, intent);
        finish();
    }


    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    onBackPressed();
                    break;

                case R.id.iv_share:
                    //创建分享Dialog
                    int id = Integer.parseInt(medicineId);

                    if (!TextUtils.isEmpty(userId)) {
                        //                        int tipID = Integer.parseInt(infoID);
                        TreatMentShareBean bean = new TreatMentShareBean(Integer.valueOf(id),
                            TreatMentShare.MEDICINE);
                        //ShareDialog.showShareDialog(mContext, bean, token);
                        if (isFavorite) {
                            Integer[] list = new Integer[] { Integer.valueOf(id) };
                            cancleFavoriteType(TreatMentShare.MEDICINE, Arrays.asList(list));
                        } else {
                            favoriteType(id, TreatMentShare.MEDICINE);
                        }
                    } else {
                        startActivityForResult(
                            new Intent(ActPharmaceutiaclDetail.this, ActivityCardLogin.class), 6);
                    }
                    break;

            }
        }
    };


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }


    @Override
    public void onDownMotionEvent() {
    }


    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }


    /**
     * 滑动时间拦截，滑动冲出
     */
    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener
        = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                // Horizontal scroll is maybe handled by ViewPager
                return false;
            }

            Scrollable scrollable = getCurrentScrollable();
            if (scrollable == null) {
                mScrolled = false;
                return false;
            }

            // If interceptionLayout can move, it should intercept.
            // And once it begins to move, horizontal scroll shouldn't work any longer.
            int flexibleSpace = mFlexibleSpaceHeight - titleHeight;
            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
            boolean scrollingDown = 0 < diffY;
            boolean scrollingUp = diffY < 0;
            if (scrollingDown) {
                if (getCurrentViewScrollableY() > 0) {
                    return false;
                } else if (getCurrentListViewScrollableY() > 0) {
                    return false;
                } else {
                    if (translationY < 0) {
                        mScrolled = true;
                        return true;
                    }
                }

            } else if (scrollingUp) {
                if (-flexibleSpace < translationY) {
                    mScrolled = true;
                    return true;
                }
            }
            mScrolled = false;
            return false;
        }


        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            mActivePointerId = ev.getPointerId(0);
            mScroller.forceFinished(true);
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
            mBaseTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
            mVelocityTracker.addMovement(ev);
        }


        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            int flexibleSpace = mFlexibleSpaceHeight - titleHeight;
            float translationY = ScrollUtils.getFloat(
                ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -flexibleSpace, 0);
            MotionEvent e = MotionEvent.obtainNoHistory(ev);
            e.offsetLocation(0, translationY - mBaseTranslationY);
            mVelocityTracker.addMovement(e);
            updateFlexibleSpace(translationY);
        }


        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
            int velocityY = (int) mVelocityTracker.getYVelocity(mActivePointerId);
            mActivePointerId = INVALID_POINTER;
            mScroller.forceFinished(true);
            int baseTranslationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);

            int minY = -(mFlexibleSpaceHeight - titleHeight);
            int maxY = 0;
            mScroller.fling(0, baseTranslationY, 0, velocityY, 0, 0, minY, maxY);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateLayout();
                }
            });
        }
    };


    private void updateLayout() {
        boolean needsUpdate = false;
        float translationY = 0;
        if (mScroller.computeScrollOffset()) {
            translationY = mScroller.getCurrY();
            int flexibleSpace = mFlexibleSpaceHeight - titleHeight;
            if (-flexibleSpace <= translationY && translationY <= 0) {
                needsUpdate = true;
            } else if (translationY < -flexibleSpace) {
                translationY = -flexibleSpace;
                needsUpdate = true;
            } else if (0 < translationY) {
                translationY = 0;
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            updateFlexibleSpace(translationY);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateLayout();
                }
            });
        }
    }


    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.scroll);
    }


    //监听ScrollView滑动，是否滑到顶端
    private int getCurrentViewScrollableY() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return -1;
        }
        View view = fragment.getView();
        if (view == null) {
            return -1;
        }
        if (fragment instanceof FragmentHospDetail) {
            ObservableScrollView mScroll = (ObservableScrollView) view.findViewById(R.id.scroll);
            mScroll.getCurrentScrollY();
        }

        return view.getScrollY();
    }


    //监听ListView滑动，是否滑到顶端
    private int getCurrentListViewScrollableY() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return -1;
        }
        View view = fragment.getView();
        if (view == null) {
            return -1;
        }
        if (fragment instanceof BaseScrollListViewFragment) {
            ObservableListView listview = (ObservableListView) view.findViewById(R.id.scroll);
            listview.getCurrentScrollY();
            return listview.getCurrentScrollY();
        }

        return -1;
    }


    private void updateFlexibleSpace() {
        updateFlexibleSpace(ViewHelper.getTranslationY(mInterceptionLayout));
    }


    /**
     * 滑动相关动画
     */

    private void updateFlexibleSpace(float translationY) {
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);
        int minOverlayTransitionY = getActionBarSize() - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mImageView,
            ScrollUtils.getFloat(-translationY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        float flexibleRange = mFlexibleSpaceHeight - getActionBarSize();
        ViewHelper.setAlpha(mOverlayView,
            ScrollUtils.getFloat(-translationY / flexibleRange, 0, 1));

        // title 相关动画
        alpha = Math.min(1, ((float) -translationY / mFlexibleSpaceHeight) * 2);
        int baseColor = getResources().getColor(R.color.toolbar_color);
        int baseTextColor = getResources().getColor(R.color.top_text);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        tvTitle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, baseTextColor));
        updateTopBarUI(isFavorite);
    }


    public void updateTopBarUI(boolean favorite) {
        if (!favorite) {
            if (alpha > 0.5f) {
                mBackView.setImageResource(R.drawable.back_grey);
                mShare.setImageResource(R.drawable.collect_grey);
            } else {
                mBackView.setImageResource(R.drawable.back_white);
                mShare.setImageResource(R.drawable.collect_white);
            }
        } else {
            mShare.setImageResource(R.drawable.collect_selected);
            mBackView.setImageResource(alpha > 0.5f ? R.drawable.back_grey : R.drawable.back_white);
        }

    }


    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItem(mPager.getCurrentItem());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 200) {//提问发起成功
            Log.i("hcb", "ActPharmaceutiaclDetail onActivityResult resultCode == 200");
            fragmentMedicineQuestion.notifyData();
        } else if (resultCode == 300) {//同类药品回调
            Log.i("hcb", "ActPharmaceutiaclDetail onActivityResult resultCode == 300");
            medicineId = data.getStringExtra("medicineId");
            chemicalName = data.getStringExtra("chemicalName");
            medicineName = data.getStringExtra("medicineName");
            //tvLabel.setText(chemicalName);
            //tvSubTitle.setText(medicineName);
            //tvTitle.setText(medicineName);
            //fragmentMedicineDetail.notifyData(medicineId);
            finish();
            Intent intent = new Intent(this, ActPharmaceutiaclDetail.class);
            intent.putExtra(EXTRA_MEDICAL_ID, medicineId);
            startActivity(intent);

            //mInterceptionLayout.post(new Runnable() {
            //    @Override public void run() {
            //        mScroller.startScroll(0,0,0,0);
            //        mInterceptionLayout.scrollTo(0,0);
            //    }
            //});

        }

        if (resultCode == RESULT_OK) {
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            userId = sp.getString("userId", "");
            token = sp.getString("token", "");

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
                    ToastUtil.showText(ActPharmaceutiaclDetail.this, "收藏失败");
                }


                @Override public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean == null ||
                        Integer.valueOf(createOrDeleteBean.getErrorCode()) != 0) {
                        return;
                    }
                    ToastUtil.showText(ActPharmaceutiaclDetail.this, "已收藏");
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
                    ToastUtil.showText(ActPharmaceutiaclDetail.this, "取消失败");
                }


                @Override public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean == null ||
                        Integer.valueOf(createOrDeleteBean.getErrorCode()) != 0) {
                        return;
                    }
                    ToastUtil.showText(ActPharmaceutiaclDetail.this, "已取消");
                    isFavorite = false;

                    updateTopBarUI(isFavorite);
                }
            });
    }


    public void setProgressBarShow(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
