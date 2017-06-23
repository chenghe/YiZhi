package com.zhongmeban.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongmeban.R;

/**
 * 功能描述： 通用的编辑item，左边的说明，右边是提示内容，支持两种样式，textview和edittext,d
 * 当设置 CommonEditView_right_et 这个属性的时，显示edittext这个view，否则显示textview
 * 作者：ysf on 2016/12/23 11:47
 */
public class CommonEditView extends RelativeLayout {

    private TextView tvLeft;
    private TextView tvRight;
    private ImageView ivRight;

    private EditText etRight;
    private String leftText;
    private String rightText;
    private String rightEtText;
    private boolean isShowImg = true;


    public CommonEditView(Context context) {
        this(context, null);
    }


    public CommonEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CommonEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_common_edit, this);
        tvLeft = (TextView) findViewById(R.id.tv_edit_left);
        tvRight = (TextView) findViewById(R.id.tv_edit_right);
        ivRight = (ImageView) findViewById(R.id.iv_edit_right);
        etRight = (EditText) findViewById(R.id.et_right);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonEditView);
        leftText = ta.getString(R.styleable.CommonEditView_left_text);
        rightText = ta.getString(R.styleable.CommonEditView_right_text);
        rightEtText = ta.getString(R.styleable.CommonEditView_right_et);
        isShowImg = ta.getBoolean(R.styleable.CommonEditView_is_show_img, true);

        ta.recycle();
        tvLeft.setText(leftText);
        tvRight.setHint(rightText);
        etRight.setHint(rightEtText);

        showEditText();
    }


    private void showEditText() {
        if (TextUtils.isEmpty(rightEtText)) {
            tvRight.setVisibility(VISIBLE);
            etRight.setVisibility(GONE);
            ivRight.setVisibility(isShowImg ? VISIBLE : INVISIBLE);
        } else {
            tvRight.setVisibility(GONE);
            etRight.setVisibility(VISIBLE);
            ivRight.setVisibility(GONE);
        }
    }


    public String getEditString() {
        return etRight.getText().toString();
    }

    /**
     * 设置只输入数字
     */
    public void setEditTextInputNum(){
        etRight.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }


    public void setRightText(String rightText) {
        this.rightText = rightText;
        tvRight.setText(rightText);
    }


    public void setRightEtText(String rightEtText) {
        this.rightEtText = rightEtText;
        etRight.setText(rightEtText);
    }
}
