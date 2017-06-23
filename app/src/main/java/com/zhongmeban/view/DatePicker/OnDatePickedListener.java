package com.zhongmeban.view.DatePicker;

/**
 * Created by Chengbin He on 2016/8/5.
 */
public interface OnDatePickedListener {
    /**
     * Listener when date has been checked
     *
     * @param year
     * @param month
     * @param day
     * @param dateDesc  yyyy-MM-dd
     */
    void onDatePickCompleted(int year, int month, int day,
                             String dateDesc);

}