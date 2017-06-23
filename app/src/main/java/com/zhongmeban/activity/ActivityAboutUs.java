package com.zhongmeban.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ZMBUtil;

/**
 * Created by Chengbin He on 2016/11/8.
 */

public class ActivityAboutUs extends BaseActivity{
    private TextView tvNum;
    private TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initTitle();
        tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(getResources().getString(R.string.app_name));
        tvNum = (TextView) findViewById(R.id.tv_version);
        tvNum.setText("版本号："+ZMBUtil.getAPPVersionNameFromAPP(this)+"");

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("关于我们");
        title.backSlid(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

}
