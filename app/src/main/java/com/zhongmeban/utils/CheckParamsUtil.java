package com.zhongmeban.utils;

import com.zhongmeban.R;

/**
 * Description:参数的关系转换工具类
 * user: Chong Chen
 * time：2016/1/14 15:24
 * 邮箱：cchen@ideabinder.com
 */
public class CheckParamsUtil {

    //医院的等级
    public static String[] mHospLevels = new String[]{"", "一级甲等", "一级", "二级甲等", "二级", "三级甲等", "三级"};

    /**
     * 根据索引获取医院等级
     */
    public static String getHospLevel(int index) {
        if (index < 0 || index > mHospLevels.length) {
            return "";
        } else {
            return mHospLevels[index];
        }
    }

    /**
     * 根据int值获取对应的医院等级
     */
    public static int getHospLevelIndex(String str) {
        for (int i = 0; i < mHospLevels.length; i++) {
            if (mHospLevels[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }


    //医生的职称
    public static String[] mDoctorProfession = new String[]{"", "教授", "副教授"};

    /**  根据int值获取对应的医生职称 */
    public static String getDoctorProfession(int index) {
        if (index < 0 || index > mDoctorProfession.length) {
            return "";
        } else {
            return mDoctorProfession[index];
        }
    }

    /**
     * 根据医生的职称获取对应的索引
     */
    public static int getDoctorProcessionIndex(String str) {
        for (int i = 0; i < mDoctorProfession.length; i++) {
            if (mDoctorProfession.equals(str)) {
                return i;
            }
        }
        return -1;
    }

    //医生的等级
    public static String[] doctTitles = new String[]{"", "主任医师", "副主任医师"};
    /**  根据int值获取对应的医生等级 */
    public static String getDoctorTitle(int index){
        if(index<0||index>doctTitles.length){
            return "";
        }else{
            return doctTitles[index];
        }
    }

    /**
     * 根据医生等级获取对应的索引
     */
    public static int getDoctorTitleIndex(String str) {
        for (int i = 0; i < doctTitles.length; i++) {
            if (doctTitles[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }


    //痛苦指数1，2，3，4无痛苦，轻微，一般，剧痛
    public static String[] mDiscomfort = new String[]{"无痛苦", "无痛苦", "轻微疼痛", "一般疼痛", "剧烈疼痛"};

    /**  根据索引获取对应的痛苦指数 */
    public static String getDiscomfortStr(int index) {
        if (index < 0 || index > mDiscomfort.length) {
            return "";
        } else {
            return mDiscomfort[index];
        }
    }

    /**
     * 根据痛苦指数的等级获取对应的索引
     */
    public static int getDiscomfortIndex(String str) {
        for (int i = 1; i < mDiscomfort.length; i++) {
            if (mDiscomfort[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    // 疼痛指数对应的表情图
    public static int[] resourceIds = new int[]{R.drawable.little, R.drawable.little, R.drawable.little, R.drawable.middle, R.drawable.gravy};
    public static int getDiscomfortResourceID(int index) {
        if (index < 0 || index > resourceIds.length) {
            return -1;
        } else {
            return resourceIds[index];
        }
    }

    //检查项目的tab标签
    public static String[] inspections = new String[]{"", "影像学检查", "实验室检查", "特殊检查"};
    /**  根据索引获取对应的检查类型 */
    public static String getInspectionByIndex(int index) {
        if (index < 0 || index > inspections.length) {
            return "";
        } else {
            return inspections[index];
        }
    }

    /**
     * 根据指定检查类型获取对应的索引
     */
    public static int getInspectionIndex(String str) {
        for (int i = 1; i < inspections.length; i++) {
            if (inspections[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    //药厂类型，
    private static String[] companyType = new String[]{"未知类型", "外资企业", "国内企业", "合资企业"};
    /**  根据索引获取对应的药厂类型 */
    public static String getDrugFactType(int index) {
        if (index < 0 || index > companyType.length) {
            return "";
        } else {
            return companyType[index];
        }
    }

    /**
     * 根据指定的药厂类型获取对应的索引
     */
    public static int getDrugFactIndex(String str) {
        for (int i = 0; i < companyType.length; i++) {
            if (companyType[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }


    //医生是否可手术
    private static String[] doctOperations = new String[]{"全部", "可手术", "不可手术"};

    // 获取手术对应的索引值
    public static int getOperationIndex(String str) {
        for (int i = 0; i < doctOperations.length; i++) {
            if (doctOperations[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

}
