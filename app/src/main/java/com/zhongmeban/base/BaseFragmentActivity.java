package com.zhongmeban.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseFragmentActivity extends FragmentActivity implements IShowFragment {

    public static final String TAG = "BaseFragmentActivity";
    public String mCurMenuFragmentTag = "";
    private int mContentResourceId = -1;

    public BaseFragmentActivity() {
        super();
        mContentResourceId = getContentResourceId();
    }

    /**
     * 
     * @return 返回此Activity的fragment的容器ID
     */
    public abstract int getContentResourceId();

    @Override
    public void showFragment(String curTag, String nxtTag, Bundle args) {

        FragmentManager fm = getSupportFragmentManager();
        Fragment curFragment = fm.findFragmentByTag(curTag);

        // 1. The current fragment is null, not a valid condition.
        if (curFragment == null) {
            Log.d(TAG, "Invalid current displayed fragment");
            return;
        }
        mCurMenuFragmentTag = curTag;
        FragmentTransaction ft = fm.beginTransaction();

        // 2. To be shown fragment is the same as the current displayed.
        if (curTag.equals(nxtTag)) {
            Log.d(TAG, "The to be shown item is current displayn item");
            if (curFragment.isHidden()) {
                ft.show(curFragment).commit();
            }
            return;
        }

        // 3. The next fragment is invalid fragment.
        if (TextUtils.isEmpty(nxtTag)) {
            Log.e(TAG, "The fragment tag should be an valid value");
            return;
        }
        // 4. Set the animation
        if (fm instanceof IFragmentAnimation) {
            IFragmentAnimation fa = (IFragmentAnimation) fm;
            ft.setCustomAnimations(fa.getInAnimation(), fa.getOutAnimation());
        }

        // 5. Hide the current fragment
        ft.hide(curFragment);

        // 6. Try to show the next fragment.
        Fragment nxtFragment = fm.findFragmentByTag(nxtTag);
        if (nxtFragment == null) {
            // 6.1 The next fragment is not created yet.
            nxtFragment = FragmentFactory.getInstance().getFragment(nxtTag);
        }
        if (nxtFragment == null) {
            return;
        }

        // 7. Set the argument when next display the fragment.

        if (null != args && !nxtFragment.isAdded()) {
            nxtFragment.setArguments(args);
        }

        if (null != args && nxtFragment instanceof BaseFragment) {
            ((BaseFragment) nxtFragment).setBundle(args);
        }

        // 8. Show or add the fragment.
        if (nxtFragment.isHidden()) {
            ft.show(nxtFragment).commit();
            Log.i(TAG, "The fragment " + nxtTag + " reverts to be shown");
        } else if (!nxtFragment.isAdded()) {
            ft.add(mContentResourceId, nxtFragment, nxtTag).commit();
            Log.i(TAG, "The fragment " + nxtTag + " added to fragment manager");
        } else {
            ft.commit();
            Log.i(TAG, "The fragment " + nxtTag + " is already shown.");
        }

        mCurMenuFragmentTag = nxtTag;
    }

    /***
     * 转换时间戳为时间（YYYY年MM月DD天）
     * @param timestamp
     * @return YYYY年MM月DD天
     */
    public String changeDateToString(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sdf.format(new Date(timestamp));
        return date;
    }
}
