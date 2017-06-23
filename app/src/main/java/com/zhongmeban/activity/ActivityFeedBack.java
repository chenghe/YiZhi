package com.zhongmeban.activity;

import android.os.Bundle;
import android.view.View;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * Created by Chengbin He on 2016/3/23.
 */
public class ActivityFeedBack extends BaseActivity {
    private LayoutActivityTitle title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initTitle();
    }


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("意见反馈");
        title.slideRighttext("完成", onClickListener);
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }
        }
    };
}
