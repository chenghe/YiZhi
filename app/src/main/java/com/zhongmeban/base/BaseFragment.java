package com.zhongmeban.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.view.DatePicker.DatePickerPopWin;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseFragment extends Fragment implements IBaseView{

    public String TAG = BaseFragment.class.getSimpleName();
    private Bundle bundle = new Bundle();
    public static final int minYear = 1971;//timepicker设置最小时间
    public static final int maxYear = 2020;//timepicker设置最大时间

    protected Handler mHander = new Handler();

    public BaseFragment() {
        super();
        TAG = this.getClass().getSimpleName();
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }



    /***
     * 转换时间戳为时间（YYYY年MM月DD天）
     * @param timestamp
     * @return YYYY年MM月DD天
     */
    public String changeDateToString(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月dd日");
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    /***
     * 转换时间戳为时间（YYYY.MM.DD）
     * @param timestamp
     * @return YYYY.MM.DD
     */
    public String changeDateToPointString(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date(timestamp));
        return date;
    }


    /***
     * 获取今天时间 转化为 YYYY-MM-DD
     * @return YYYY-MM-DD
     */
    public String getTodayData(){
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todyDate = df.format(d);
        return todyDate;
    }

    /***
     * 转化时间为时间戳
     * @param date
     * @return timestamp
     */
    public long changeDateToLong(String date){
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
    public MaterialDialog showProgressDialog(String content , Context context) {

        Log.i("hcb", "showProgressDialog");
        return new MaterialDialog.Builder(context)
                .content(content)
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .progressIndeterminateStyle(false).show();
    }

    /***
     * 显示是否删除弹框
     */
    public void showDeleteDialog(Context context ,MaterialDialog.SingleButtonCallback positiveCallback,
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
    public void showNormalDialog(Context context ,MaterialDialog.SingleButtonCallback positiveCallback,
                                 MaterialDialog.SingleButtonCallback negativeCallback ,
                                 String content,String positiveText,String negativeText) {
        new MaterialDialog.Builder(context)
                .content(content)
                .positiveText(positiveText)
                .positiveColor(getResources().getColor(R.color.app_green))
                .negativeText(negativeText)
                .negativeColor(getResources().getColor(R.color.app_green))
                .onPositive(positiveCallback)
                .onNegative(negativeCallback).show();
    }


    /***
     * 显示日期选择Dialog
     * @param activity
     * @param onDatePickedListener
     * @param dateChose
     */
    public void showDatePicker(Activity activity,OnDatePickedListener onDatePickedListener,String dateChose) {
         new DatePickerPopWin.Builder(activity,onDatePickedListener )
                .minYear(minYear) //min year in loop
                .maxYear(maxYear) // max year in loop
                .dateChose(dateChose) // date chose when init popwindow
                .build().showPopWin(activity);
    }

    /***
     * 显示日期选择Dialog 带最大最小时间
     * @param activity
     * @param onDatePickedListener
     * @param dateChose
     */
    public void showDatePicker(Activity activity,OnDatePickedListener onDatePickedListener,
                               String dateChose,int minYear,int maxYear) {
        new DatePickerPopWin.Builder(activity,onDatePickedListener )
                .minYear(minYear) //min year in loop
                .maxYear(maxYear) // max year in loop
                .dateChose(dateChose) // date chose when init popwindow
                .build().showPopWin(activity);
    }


    ///////////////////////BaseView 的 实现方法///////////////////////////
    @Override public void showProgress(String message) {

    }


    @Override public void cancelProgress() {

    }


    @Override public void showTheToast(String msg) {

    }


    @Override public void onServerFail(String msg) {

    }
}
