package com.zhongmeban.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bugtags.library.Bugtags;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhongmeban.BuildConfig;
import com.zhongmeban.R;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import com.zhongmeban.view.DatePicker.OnSinglePickedListener;
import com.zhongmeban.view.DatePicker.SinglePickerPopWin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscription;

/**
 * Created by Chengbin He on 2016/3/21.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public LayoutActivityTitle title;//公共Title
    public static final int minYear = 1971;//timepicker设置最小时间
    public static final int maxYear = 2020;//timepicker设置最大时间
    public MaterialDialog progressDiaglog;//进度条Dialog

    protected Subscription subscripBase;


    /**
     * 初始化Title
     */
    protected abstract void initTitle();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(this.getClass().getSimpleName() + "---oncreat");
    }


    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        if (!BuildConfig.IS_DEBUG) {
            Bugtags.onResume(this);
            MobclickAgent.onResume(this);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        if (!BuildConfig.IS_DEBUG) {
            Bugtags.onPause(this);
            MobclickAgent.onPause(this);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //注：回调 3
        if (!BuildConfig.IS_DEBUG) {
            Bugtags.onDispatchTouchEvent(this, ev);
        }

        return super.dispatchTouchEvent(ev);
    }


    /***
     * 转换时间戳为时间（YYYY年MM月DD天）
     *
     * @return YYYY年MM月DD天
     */
    public String changeDateToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sdf.format(new Date(timestamp));
        return date;
    }


    /***
     * 获取今天时间 转化为 YYYY-MM-DD
     *
     * @return YYYY-MM-DD
     */
    public String getTodayData() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todyDate = df.format(d);
        return todyDate;
    }


    /***
     * 转换时间戳为时间（YYYY.MM.DD）
     *
     * @return YYYY.MM.DD
     */
    public String changeDateToPointString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date(timestamp));
        return date;
    }


    /***
     * 转化时间为时间戳
     *
     * @return timestamp
     */
    public long changeDateToLong(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long timestamp = sdf.parse(date).getTime();
            return timestamp;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /***
     * ProgressDialog
     *
     * @param content 弹框内容
     */
    public MaterialDialog showProgressDialog(String content, Context context) {

        Log.i("hcb", "showProgressDialog");
        progressDiaglog = new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .progressIndeterminateStyle(false).show();
        return progressDiaglog;
    }


    /***
     * 显示是否删除弹框
     */
    public void showDeleteDialog(Context context, MaterialDialog.SingleButtonCallback positiveCallback,
                                 String content) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText("确定")
                .positiveColor(getResources().getColor(R.color.button_red_normanl))
                .negativeText("取消")
                .negativeColor(getResources().getColor(R.color.app_green))
                .onPositive(positiveCallback)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    /***
     * 显示是否删除弹框
     */
    public void showDeleteDialog(Context context, MaterialDialog.SingleButtonCallback positiveCallback,
                                 MaterialDialog.SingleButtonCallback negativeCallback, String content) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText("确定")
                .positiveColor(getResources().getColor(R.color.button_red_normanl))
                .negativeText("取消")
                .negativeColor(getResources().getColor(R.color.app_green))
                .onPositive(positiveCallback)
                .onNegative(negativeCallback).show();
    }


    /***
     * 显示日期选择Dialog
     */
    public void showDatePicker(Activity activity, OnDatePickedListener onDatePickedListener, String dateChose) {
        new DatePickerPopWin.Builder(activity, onDatePickedListener)
                .minYear(minYear) //min year in loop
                .maxYear(maxYear) // max year in loop
                .dateChose(dateChose) // date chose when init popwindow
                .build().showPopWin(activity);
    }


    /***
     * 显示日期选择Dialog 带最大最小时间
     */
    public void showDatePicker(Activity activity, OnDatePickedListener onDatePickedListener,
                               String dateChose, int minYear, int maxYear) {
        new DatePickerPopWin.Builder(activity, onDatePickedListener)
                .minYear(minYear) //min year in loop
                .maxYear(maxYear) // max year in loop
                .dateChose(dateChose) // date chose when init popwindow
                .build().showPopWin(activity);
    }


    /***
     * 显示是否删除弹框
     */
    public void showNormalDialog(Context context, MaterialDialog.SingleButtonCallback positiveCallback,
                                 MaterialDialog.SingleButtonCallback negativeCallback,
                                 String content, String positiveText, String negativeText) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(positiveText)
                .positiveColor(getResources().getColor(R.color.app_green))
                .negativeText(negativeText)
                .negativeColor(getResources().getColor(R.color.app_green))
                .onPositive(positiveCallback)
                .onNegative(negativeCallback).show();
    }


    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    public void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }


    public void dissmisProgress() {
        if (progressDiaglog != null) {
            progressDiaglog.dismiss();
        }
    }

    /**
     * 展示滑轮控件
     * @param onSinglePickedListener
     * @param mdata
     * @param deaultPosition
     */
    public void showSelectDialog(OnSinglePickedListener onSinglePickedListener, List<String> mdata, int deaultPosition) {
//        mdata = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            mdata.add((i + 1) + "");
//        }
        if (deaultPosition > mdata.size()) {
            deaultPosition = 0;
        }
        new SinglePickerPopWin.Builder(this, onSinglePickedListener)
                .currentPos(deaultPosition).setData(mdata).build().showPopWin(this);
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscripBase != null && !subscripBase.isUnsubscribed()) {
            subscripBase.unsubscribe();
        }
    }
}
