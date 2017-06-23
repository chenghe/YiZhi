package com.zhongmeban.view.DatePicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.zhongmeban.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chengbin He on 2016/7/21.
 */
public class SinglePickerPopWin extends PopupWindow implements View.OnClickListener {

    private static final int DEFAULT_MIN_YEAR = 1900;
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView mLoopView;
    public View pickerContainerV;
    public View contentView;//root view
    private TextView tvTitle;

    private int mPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;
    private String titleName;
    private boolean showDayMonthYear;

    List<String> mData = new ArrayList();


    public static class Builder {

        //Required
        private Context context;
        private OnSinglePickedListener listener;
        private int mPostion;
        private List<String> data;


        public Builder(Context context, OnSinglePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }


        //Option
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private String titleName;
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;


        public Builder currentPos(int postion) {
            this.mPostion = postion;
            if (mPostion<0){
                mPostion=0;
            }
            return this;
        }


        public Builder setData(List<String> dataBuilder) {
            this.data = dataBuilder;
            return this;
        }


        public Builder titleName(String titleName) {
            this.titleName = titleName;
            return this;
        }


        public Builder textCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }


        public Builder textConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }


        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }


        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }


        /**
         * set btn text btnTextSize
         *
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize) {
            this.btnTextSize = textSize;
            return this;
        }


        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public SinglePickerPopWin build(){
            if(data==null||data.size()<1){
                throw new IllegalArgumentException();
            }
            return new SinglePickerPopWin(this);
        }

    }


    public SinglePickerPopWin(Builder builder) {
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.titleName = builder.titleName;
        this.mPos = builder.mPostion;
        this.mData = builder.data;
        initView();
    }


    private OnSinglePickedListener mListener;


    private void initView() {

        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_single_picker, null);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        mLoopView = (LoopView) contentView.findViewById(R.id.view_single_picker);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        tvTitle = (TextView) contentView.findViewById(R.id.date_picker_title);
        if (titleName != null) {
            tvTitle.setText(titleName);
        }

        mLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                mPos = item;
            }
        });

        initPickerViews(); // init year and month loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }


    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        mLoopView.setDataList(mData);
        mLoopView.setInitPosition(mPos);
    }


    /**
     * Show date picker popWindow
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                0, 0);
            trans.setDuration(300);
            trans.setInterpolator(new AccelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }


    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(300);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }


            @Override
            public void onAnimationRepeat(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }


    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

            dismissPopWin();
        } else if (v == confirmBtn) {
            if (null != mListener) {
                mListener.onPickCompleted(mPos);
            }

            dismissPopWin();
        }
    }


    /**
     * Transform int to String with prefix "0" if less than 10
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }


    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}

