package com.zhongmeban.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by User on 2016/9/22.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showText(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }

        toast.setText(text);
        toast.show();
    }

}
