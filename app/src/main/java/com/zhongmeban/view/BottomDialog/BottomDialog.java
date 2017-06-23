package com.zhongmeban.view.BottomDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.zhongmeban.R;

/**
 * 自定义 底部弹出可滑动Dialog
 * Created by HeCheng on 2016/4/4.
 */
public class BottomDialog extends Dialog implements CollapsingView.CollapseListener{

    private static final String TAG = "BottomDialog";

    private static final int[] ATTRS = new int[]{
            R.attr.bottom_sheet_bg_color, // 0
            R.attr.bottom_sheet_title_text_appearance, // 1
            R.attr.bottom_sheet_list_text_appearance,// 2
            R.attr.bottom_sheet_grid_text_appearance, // 3
            R.attr.bottom_sheet_message_text_appearance, // 4
            R.attr.bottom_sheet_message_title_text_appearance, // 5
            R.attr.bottom_sheet_button_text_appearance, // 6
            R.attr.bottom_sheet_item_icon_color // 7
    };

    private Builder mBuilder;

    public BottomDialog(Context context,Builder builder) {
        super(context, builder.style);
        mBuilder = builder;
    }
    private final Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化底部View Window
        Window window = getWindow();
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.bottom_sheet_width);

        if (window != null) {
            window.setLayout(width <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : width, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        } else {
            Log.e(TAG, "Window came back as null, unable to set defaults");
        }
        TypedArray ta = getContext().obtainStyledAttributes(ATTRS);

        initViewLayout(ta);
    }


    private void initViewLayout(TypedArray ta) {
        Log.i("hcb", "initViewLayout(TypedArray ta) start");
        CollapsingView collapsingView = new CollapsingView(getContext());
        collapsingView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        collapsingView.setCollapseListener(this);
        mBuilder.view.setBackgroundColor(ta.getColor(0, Color.WHITE));
        collapsingView.addView(mBuilder.view);
        setContentView(collapsingView);
    }


    /**
     * Dialog 滑动时CollapsingView 回掉方法
     */
    @Override
    public void onCollapse() {
        if (getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().post(dismissRunnable);
        } else {
            dismiss();
        }
    }

    /**
     * 内部Builder类，便于对象初始化
     */
    public static class Builder {
        public View view;
        public Context context;
        public int style = R.style.BottomSheet;
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置底部Window 对应View
         */
        public Builder setView(View view) {
            this.view = view;
            return this;
        }
        public BottomDialog create() {
            return new BottomDialog(context,this);
        }

        public BottomDialog showDialog(){
            final BottomDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
