package com.zhongmeban.base;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {

    private static FragmentFactory mInstance;

    private static Object mLockObj = new Object();

    private Map<String, Class<? extends BaseFragment>> mFramentMap = new HashMap<String, Class<? extends BaseFragment>>();

    private FragmentFactory() {
        super();
    }

    public static FragmentFactory getInstance() {
        if (null == mInstance) {
            synchronized (mLockObj) {
                mInstance = new FragmentFactory();
            }
        }
        return mInstance;
    }

    public void addFragmen(String TAG, Class<? extends BaseFragment> bb) {
        mFramentMap.put(TAG, bb);
    }

    public void addFragment(Class<? extends BaseFragment> bb) {
        try {
            mFramentMap.put(bb.newInstance().TAG, bb);
            Log.e(this.getClass().getSimpleName(), "mFramentMap" + mFramentMap.size());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public BaseFragment getFragment(String tag) {
        Class<? extends BaseFragment> fragmentClass = mFramentMap.get(tag);
        if (null != fragmentClass) {
            try {
                return fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
