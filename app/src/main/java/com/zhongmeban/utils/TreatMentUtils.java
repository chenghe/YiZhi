package com.zhongmeban.utils;

/**
 * 治疗相关Util
 * Created by Chengbin He on 2016/11/8.
 */

public class TreatMentUtils {
    public static final int SURGERY = 1;//手术
    public static final int CHEMOTHERAPY = 2;//化疗
    public static final int RADIOTHERAPY = 3;//放疗
    public static final int TARGETED_THERAPY = 4;//靶向治疗

    public static String getTreatMentName(int type){
        String name;
        switch (type){
            case 1:
                name = "手术";
                break;
            case 2:
                name = "化疗";
                break;
            case 3:
                name = "放疗";
                break;
            case 4:
                name = "靶向治疗";
                break;
            default:
                name = "";
                break;
        }
        return name;
    }
}
