package com.zhongmeban.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.zhongmeban.R;
import com.zhongmeban.utils.ZMBUtil;

/**
 * Created by Chengbin He on 2016/7/28.
 */
public class DoseDialog extends Dialog{

    private Builder mBuilder;
    private ViewGroup parentView;

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

    protected DoseDialog(Context context,Builder builder,ViewGroup parentView) {
        super(context,builder.style);
        mBuilder = builder;
        this.parentView = parentView;
    }

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
            Log.e("DoseDialog", "Window came back as null, unable to set defaults");
        }

        TypedArray ta = getContext().obtainStyledAttributes(ATTRS);
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.dose_dialog_view,parentView,false);
        RelativeLayout frameLayout = new RelativeLayout(getContext());
        frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mBuilder.view.setBackgroundColor(ta.getColor(0, Color.WHITE));
        view.addView(mBuilder.view);
        window.addContentView(view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        setContentView(frameLayout);
    }

    public static class Builder{
        public View view;
        public Context context;
        public ViewGroup parentView;
        public int style = R.style.DoseDialog;

        public Builder(Context context ,ViewGroup parentView) {
            this.context = context;
        }

        /**
         * 设置底部Window 对应View
         */
        public Builder setView(View view) {
            this.view = view;
            return this;
        }
        public DoseDialog create() {
            return new DoseDialog(context,this,parentView);
        }

        public DoseDialog showDialog(){
            final DoseDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }


    @Override public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        /////////设置高宽
        lp.width = ZMBUtil.getWidth(); // 宽度
        //lp.height = (int) (lp.width * 0.30);     // 高度
        dialogWindow.setAttributes(lp);
    }
}