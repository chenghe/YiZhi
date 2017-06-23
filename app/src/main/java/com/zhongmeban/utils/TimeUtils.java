package com.zhongmeban.utils;

import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chengbin He on 2016/8/18.
 */
public class TimeUtils {

    /***
     * 转换时间戳为时间（YYYY年MM月DD天）
     *
     * @return YYYY年MM月DD天
     */
    public static String changeDateToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sdf.format(new Date(timestamp));
        return date;
    }


    /***
     * 获取时间 转化为 yyyy-MM-dd
     *
     * @return yyyy-MM-dd
     */
    public static String formatTime(Long timestamp) {
        if (timestamp == null || timestamp < 100000) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(timestamp));
        return date;
    }


    /***
     * 获取今天时间 转化为 YYYY-MM-DD
     *
     * @return YYYY-MM-DD
     */
    public static String getTodayData() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todyDate = df.format(d);
        return todyDate;
    }


    /***
     * 转化时间为时间戳
     *
     * @return timestamp
     */
    public static long changeDateToLong(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long timestamp = sdf.parse(date).getTime();
            return timestamp;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(long birthdayTime) {
        Date birthday = new Date(birthdayTime);
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }


    /**
     * 开始时间判断
     */
    public static boolean checkStartTime(long startTime, long endTime, Context context) {
        if (endTime == 0) {
            if (startTime > changeDateToLong(getTodayData())) {
                ToastUtil.showText(context, "日期必须小于今天");
                return false;
            } else {
                return true;
            }
        } else if (endTime > 0 && endTime > startTime) {
            return true;
        } else {
            ToastUtil.showText(context, "开始时间必须小于结束时间");
            return false;
        }
    }


    /**
     * 结束时间判断
     */
    public static boolean checkEndTime(long startTime, long endTime, Context context) {
        if (startTime == 0) {
            if (endTime > changeDateToLong(getTodayData())) {
                ToastUtil.showText(context, "日期必须小于今天");
                return false;
            } else {
                return true;
            }
        } else if (startTime > 0 && startTime < endTime) {
            return true;
        } else {
            ToastUtil.showText(context, "开始时间必须小于结束时间");
            return false;
        }
    }


    /**
     * 获取距今天数
     */
    public static String fromeTodayDays(long time) {
        Date date = new Date();
        long between_days = (date.getTime() - time) / (1000 * 3600 * 24);
        int day = Integer.parseInt(String.valueOf(between_days));
        return day + "天";
    }


    /**
     * 转换天数为毫秒
     */
    public static long changeDayToLong(int day) {
        long time = day * 24 * 60 * 60 * 1000;
        return time;
    }


    /**
     * 获取距离今天某天毫秒
     */
    public static long getDateTime(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        long time = cal.getTime().getTime();
        return time;
    }


    /**
     * 计算距离当前时间多久
     */
    public static String getTimeFromeToday(long beforeTime) {
        String time = "";
        Calendar cal = Calendar.getInstance();
        long now = cal.getTime().getTime();
        long timeBuket = now - beforeTime;
        if (timeBuket < 60 * 1000) {
            //1分钟内显示刚刚
            time = "刚刚";
        } else if (timeBuket < 60 * 60 * 1000) {
            time = timeBuket / (1000 * 60) + "分前";
        } else if (timeBuket < 24 * 60 * 60 * 1000) {
            time = timeBuket / (60 * 60 * 1000) + "小时前";
        } else if (timeBuket < 3 * 24 * 60 * 60 * 1000) {
            time = timeBuket / (24 * 60 * 60 * 1000) + "天前";
        } else {
            time = changeDateToString(beforeTime);
        }
        return time;
    }
}