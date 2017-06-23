package com.zhongmeban.treatmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TabViewFragmentPagerAdapter;
import com.zhongmeban.bean.HospitalDetail;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.treatmodle.fragment.FragmentHospDetail;
import com.zhongmeban.treatmodle.fragment.FragmentHospDoctList;
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
import com.zhongmeban.view.BottomDialog.BottomDialog;
import com.zhongmeban.view.ViewCustomLoading;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:医院详情Activity
 * Created by Chengbin He on 2016/3/21.
 */
public class ActHospDetail extends BaseScrollActivity implements ObservableScrollViewCallbacks {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final int INVALID_POINTER = -1;
    private static final String[] TITLES = new String[]{"基本", "医生"};


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
    private List<Fragment> mFragments = new ArrayList<Fragment>() ;
    private ViewPager mPager;
    private Activity mContext = ActHospDetail.this;
    private TabViewFragmentPagerAdapter mPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private RelativeLayout mTitle;
    private TextView tvLabel;
    private TextView tvSubTitle;
    private TextView tvTitle;
    private ImageView mBackView;
    private ImageView mShare;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private BottomDialog dialog;
    public String hospitalId;
    public String hospLevel;
    public String hospAddress;
    public String hospName;
    private ViewCustomLoading progressBar;
    private FragmentHospDetail fragmentHospDetail;
    private FragmentHospDoctList fragmentHospDoctList;
    private String userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_hosp_detail);

        Intent intent = getIntent();
        hospName = intent.getStringExtra("hospName");
        hospLevel = intent.getStringExtra("hospLevel");
        hospAddress = intent.getStringExtra("hospAddress");
        hospitalId = intent.getStringExtra("hospitalId");

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = sp.getString("userId","");
        token = sp.getString("token","");
        initView();

        getHttpHospDetailData();
    }

    private void initView() {

        progressBar = (ViewCustomLoading) findViewById(R.id.progressBar);
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.container);

        mInterceptionLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        //初始化title
        mTitle= (RelativeLayout) findViewById(R.id.ll_title);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(0,
                getResources().getColor(R.color.toolbar_color)));

        mImageView = (ImageView) findViewById(R.id.image);
        mImageView.setImageResource(R.drawable.hosp_bg);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(hospName);
        tvTitle.setTextColor(ScrollUtils.getColorWithAlpha(0,
                getResources().getColor(R.color.top_text)));

        tvSubTitle = (TextView) findViewById(R.id.tv_sub_title);
        tvSubTitle.setText(hospName);

        tvLabel = (TextView) findViewById(R.id.tv_label);

        mShare = (ImageView) findViewById(R.id.iv_share);
        mShare.setOnClickListener(onClickListener);
        mBackView = (ImageView) findViewById(R.id.iv_back);
        mBackView.setOnClickListener(onClickListener);

        //初始化相关Fragment
        fragmentHospDetail = new FragmentHospDetail();//基本
        fragmentHospDoctList= new FragmentHospDoctList();//医生
        mFragments.add(fragmentHospDetail);
        mFragments.add(fragmentHospDoctList);

        mPagerAdapter = new TabViewFragmentPagerAdapter(getSupportFragmentManager(),mFragments,TITLES);

        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    fragmentHospDoctList.notifyData(hospitalId);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSlidingTabLayout.setViewPager(mPager);

        mOverlayView = findViewById(R.id.overlay);
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        titleHeight= getResources().getDimensionPixelSize(R.dimen.title_height);


        findViewById(R.id.pager_wrapper).setPadding(0, mFlexibleSpaceHeight + mTabHeight, 0, 0);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        slidingTabLayout.setViewPager(mPager);

        ((FrameLayout.LayoutParams) slidingTabLayout.getLayoutParams()).topMargin = mFlexibleSpaceHeight ;

        ViewConfiguration vc = ViewConfiguration.get(this);
        mSlop = vc.getScaledTouchSlop();
        mMaximumVelocity = vc.getScaledMaximumFlingVelocity();

        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mScroller = new OverScroller(getApplicationContext());
        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                lp.height = getScreenHeight() + mFlexibleSpaceHeight;
                mInterceptionLayout.requestLayout();

                updateFlexibleSpace();
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_back:
                    finish();
                break;

                case R.id.iv_share:
                    //创建分享Dialog
//                    BottomDialog.Builder builder = new BottomDialog.Builder(mContext);
//                    dialog = new BottomDialog(mContext,builder);
//                    View view = LayoutInflater.from(mContext).inflate(R.layout.layout_share_dialog,null);
//                    TextView cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
//                    cancel.setOnClickListener(onClickListener);
//                    builder.setView(view);
//                    dialog = builder.showDialog();
//                    if (!TextUtils.isEmpty(userId)){
                        int hospId = Integer.parseInt(hospitalId);
//                        TreatMentShareBean bean = new TreatMentShareBean(userId,hospId, TreatMentShare.HOSPITAL);
//                        ShareDialog.showShareDialog(mContext,bean,token);
                    //}
                    break;

//                case R.id.tv_dialog_cancel:
//                    dialog.dismiss();
//                    break;
            }
        }
    };

    /**
     * 网络请求获取数据
     */
    private void getHttpHospDetailData(){
        HttpService.getHttpService().getHospitalDetail(hospitalId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HospitalDetail>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http detail onCompleted()");
                        mInterceptionLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http detail onError()");
                    }

                    @Override
                    public void onNext(HospitalDetail hospitalDetail) {
                        Log.i("hcb", "http detail onCompleted()");
                        fragmentHospDetail.setHospInfo(hospitalDetail.getData().getContectInfo());
                        fragmentHospDetail.setHospDescription(hospitalDetail.getData().getDescription());
                        tvLabel.setText(hospitalDetail.getData().getDoctorCount()+"位医生");
                    }
                });
    }

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
     * 滑动事件拦截，滑动冲出
     */
    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
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
            int flexibleSpace = mFlexibleSpaceHeight -titleHeight;
            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
            boolean scrollingDown = 0 < diffY;
            boolean scrollingUp = diffY < 0;
            if (scrollingDown) {
                if (getCurrentViewScrollableY()>0){
                    return false;
                }else if(getCurrentListViewScrollableY()>0) {
                    return false;
                }else{
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
            int flexibleSpace = mFlexibleSpaceHeight -titleHeight;
            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -flexibleSpace, 0);
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

            int minY = -(mFlexibleSpaceHeight -titleHeight);
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
            int flexibleSpace = mFlexibleSpaceHeight -titleHeight;
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
        if (fragment instanceof FragmentHospDetail){
            ObservableScrollView  mScroll =  (ObservableScrollView)view.findViewById(R.id.scroll);
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
        if (fragment instanceof BaseScrollListViewFragment){
            ObservableListView listview = (ObservableListView)view.findViewById(R.id.scroll);
            listview.getCurrentScrollY();
            return  listview.getCurrentScrollY();
        }

        return -1;
    }

    private void updateFlexibleSpace() {
        updateFlexibleSpace(ViewHelper.getTranslationY(mInterceptionLayout));
    }

    /**
     * 滑动相关动画
     * @param translationY
     */

    private void updateFlexibleSpace(float translationY) {
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);
        int minOverlayTransitionY = getActionBarSize() - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-translationY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        float flexibleRange = mFlexibleSpaceHeight - getActionBarSize();
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat(-translationY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange + translationY ) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
//        ViewHelper.setPivotY(mTitleView, 0);
//        ViewHelper.setScaleX(mTitleView, scale);
//        ViewHelper.setScaleY(mTitleView, scale);

        // title 相关动画
        float alpha = Math.min(1, ((float) -translationY / mFlexibleSpaceHeight) * 2);
        int baseColor = getResources().getColor(R.color.toolbar_color);
        int baseTextColor = getResources().getColor(R.color.top_text);
        mTitle.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        tvTitle.setTextColor(ScrollUtils.getColorWithAlpha(alpha, baseTextColor));
        if (alpha > 0.5f) {
            mBackView.setImageResource(R.drawable.back_grey);
            mShare.setImageResource(R.drawable.more1);
        } else {
            mBackView.setImageResource(R.drawable.back_white);
            mShare.setImageResource(R.drawable.threepoints);
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItem(mPager.getCurrentItem());
    }


}
