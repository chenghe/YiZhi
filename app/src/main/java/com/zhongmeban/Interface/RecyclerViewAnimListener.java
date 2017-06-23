package com.zhongmeban.Interface;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * 功能描述：
 * 作者：ysf on 2017/1/9 14:38
 */
public class RecyclerViewAnimListener extends RecyclerView.OnScrollListener {

    private View mView;
    boolean isAnim = false;


    public RecyclerViewAnimListener(View mView) {
        this.mView = mView;
    }


    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            if (isAnim) return;
            if (mView.getVisibility() == View.VISIBLE) {
                animateOut(mView);
            }

        } else {
            if (mView.getVisibility() == View.GONE) {
                animateIn(mView);
            }
        }

    }


    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }


    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();


    // Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
    private void animateOut(final View button) {
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(button)
                .translationY(button.getHeight() + getMarginBottom(button))
                .setInterpolator(INTERPOLATOR)
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        isAnim = true;
                    }


                    public void onAnimationCancel(View view) {
                        isAnim = true;
                    }


                    public void onAnimationEnd(View view) {
                        view.setVisibility(View.GONE);
                        isAnim = false;
                    }
                })
                .start();
        } else {

        }
    }


    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    private void animateIn(View button) {
        button.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(button).translationY(0)
                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                .start();
        } else {

        }
    }


    private int getMarginBottom(View v) {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

}
