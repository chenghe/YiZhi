package com.zhongmeban.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.utils.DisplayUtil;

/**
 * Created by Chengbin He on 2016/12/29.
 */

public class TypeSelectLinearLayout extends LinearLayout implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private Context mContext;
    private int showNum = 3;
    private int selectPosition;
    private TypeClickListenter typeClickListenter;

    public TypeSelectLinearLayout(Context context) {
        this(context, null);
    }

    public TypeSelectLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_type_select_linearlayout, this);

        tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv2.setOnClickListener(this);
        tv3 = (TextView) findViewById(R.id.tv_3);
        tv3.setOnClickListener(this);
        tv4 = (TextView) findViewById(R.id.tv_4);
        tv4.setOnClickListener(this);

        if (showNum == 4) {
            tv4.setVisibility(VISIBLE);
        } else {
            tv4.setVisibility(GONE);
        }

        initTextSize(showNum);
    }

    private void initTextSize(int showNum) {
        if (showNum == 4) {
            tv1.setTextSize(11);
            tv2.setTextSize(11);
            tv3.setTextSize(11);
            tv4.setTextSize(11);
        } else {
            tv1.setTextSize(14);
            tv2.setTextSize(14);
            tv3.setTextSize(14);
            tv4.setTextSize(14);
        }
    }

    public void initTitleName(String title1, String title2, String title3) {
        tv1.setText(title1);
        tv2.setText(title2);
        tv3.setText(title3);
    }

    public void initTitleName(String title1, String title2, String title3, String title4) {
        tv1.setText(title1);
        tv2.setText(title2);
        tv3.setText(title3);
        tv4.setText(title4);
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
        if (showNum == 4) {
            tv4.setVisibility(VISIBLE);
        } else {
            tv4.setVisibility(GONE);
        }
        initTextSize(showNum);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                selectPosition = 1;
                selectItem(selectPosition);
                typeClickListenter.onTypeClick(selectPosition);
                break;
            case R.id.tv_2:
                selectPosition = 2;
                selectItem(selectPosition);
                typeClickListenter.onTypeClick(selectPosition);
                break;
            case R.id.tv_3:
                selectPosition = 3;
                selectItem(selectPosition);
                typeClickListenter.onTypeClick(selectPosition);
                break;
            case R.id.tv_4:
                selectPosition = 4;
                selectItem(selectPosition);
                typeClickListenter.onTypeClick(selectPosition);
                break;
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        selectItem(selectPosition);
    }

    public void setTypeClickListenter(TypeClickListenter typeClickListenter) {
        this.typeClickListenter = typeClickListenter;
    }

    private void selectItem(int type) {
        switch (type) {
            case 1:
                tv1.setBackgroundResource(R.drawable.bg_box_selected);
                tv1.setTextColor(getResources().getColor(R.color.white));
                tv2.setBackgroundResource(R.drawable.bg_box);
                tv2.setTextColor(getResources().getColor(R.color.content_two));
                tv3.setBackgroundResource(R.drawable.bg_box);
                tv3.setTextColor(getResources().getColor(R.color.content_two));
                tv4.setBackgroundResource(R.drawable.bg_box);
                tv4.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 2:
                tv1.setBackgroundResource(R.drawable.bg_box);
                tv1.setTextColor(getResources().getColor(R.color.content_two));
                tv2.setBackgroundResource(R.drawable.bg_box_selected);
                tv2.setTextColor(getResources().getColor(R.color.white));
                tv3.setBackgroundResource(R.drawable.bg_box);
                tv3.setTextColor(getResources().getColor(R.color.content_two));
                tv4.setBackgroundResource(R.drawable.bg_box);
                tv4.setTextColor(getResources().getColor(R.color.content_two));
                break;
            case 3:
                tv1.setBackgroundResource(R.drawable.bg_box);
                tv1.setTextColor(getResources().getColor(R.color.content_two));
                tv2.setBackgroundResource(R.drawable.bg_box);
                tv2.setTextColor(getResources().getColor(R.color.content_two));
                tv3.setBackgroundResource(R.drawable.bg_box_selected);
                tv3.setTextColor(getResources().getColor(R.color.white));
                tv4.setBackgroundResource(R.drawable.bg_box);
                tv4.setTextColor(getResources().getColor(R.color.content_two));
                break;

            case 4:
                tv1.setBackgroundResource(R.drawable.bg_box);
                tv1.setTextColor(getResources().getColor(R.color.content_two));
                tv2.setBackgroundResource(R.drawable.bg_box);
                tv2.setTextColor(getResources().getColor(R.color.content_two));
                tv3.setBackgroundResource(R.drawable.bg_box);
                tv3.setTextColor(getResources().getColor(R.color.content_two));
                tv4.setBackgroundResource(R.drawable.bg_box_selected);
                tv4.setTextColor(getResources().getColor(R.color.white));
                break;

            default:
                tv1.setBackgroundResource(R.drawable.bg_box);
                tv1.setTextColor(getResources().getColor(R.color.content_two));
                tv2.setBackgroundResource(R.drawable.bg_box);
                tv2.setTextColor(getResources().getColor(R.color.content_two));
                tv3.setBackgroundResource(R.drawable.bg_box);
                tv3.setTextColor(getResources().getColor(R.color.content_two));
                tv4.setBackgroundResource(R.drawable.bg_box);
                tv4.setTextColor(getResources().getColor(R.color.content_two));
                break;
        }
    }
    public interface TypeClickListenter {
        void onTypeClick(int position);
    }
}

