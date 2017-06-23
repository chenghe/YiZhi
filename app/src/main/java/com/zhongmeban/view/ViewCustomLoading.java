package com.zhongmeban.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/7 14:05
 */
public class ViewCustomLoading extends FrameLayout {

    private ImageView ivLoading;
    private TextView tvLoading;


    public ViewCustomLoading(Context context) {
        this(context, null);
    }


    public ViewCustomLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ViewCustomLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_loading_yizhi, this);

        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvLoading = (TextView) findViewById(R.id.tv_loading);

        rotateLoading();

    }


    public void rotateLoading() {
/*        Animation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1000);
        animation.setFillAfter(true);
        animation.setDuration(600);
        ivLoading.startAnimation(animation);*/

        ObjectAnimator animator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 360f);
        animator.setDuration(600);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }
}
