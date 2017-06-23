package com.zhongmeban.utils;

/**
 * 泛型类
 * Created by User on 2016/9/19.
 */
public class Constants {

    public static final int PAGER_COUNT = 20;//分页条数

    /**
     * 是不是医保类型药物
     * @param num
     * @return
     */
    public static boolean isMedicalInsurance(int num) {
        switch (num) {
            case 0:
                return false;//不知道
            case 1:
                return true;//甲类医保
            case 2:
                return true;//一类医保
            case 3:
                return false;//工商
            case 4:
                return false;//否
        }

        return false;
    }

    /**
     * 返回医保类型药物
     * @param num
     * @return
     */
    public static String getMedicalInsurance(int num) {
        switch (num) {
            case 0:
                return "";//不知道
            case 1:
                return "甲类";//甲类医保
            case 2:
                return "乙类";//一类医保
            case 3:
                return "工伤";//工商
            case 4:
                return "";//否
        }

        return "";
    }

    /**
     * 是不是特殊哎哟品  ，是不是处方药，是不是进口都可以用这个
     * @param num
     * @return
     */
    public static boolean isSpicalMedical(int num) {
        switch (num) {
            case 1:
                return true;//是
            case 2:
                return true;//否
        }

        return false;
    }

    /**
     * 是不是特殊哎哟品  ，是不是处方药，是不是进口都可以用这个
     * @param type
     * @return
     */
    public static String getMedicineType(int type) {
        switch (type) {
            case 1:
                return "西药";//
            case 2:
                return "中药";//
            case 3:
                return "生物血液制品";//
        }

        return "";
    }


    ////////////////治疗详情titile////////////////////
    /**
     * 治疗准备
     */
    public static final int TherapeuticQATypeTreatPrepare = 101;
    /**
     * 治疗过程
     */
    public static final int TherapeuticQATypeTreatProcess = 102;
    /**
     * 治疗注意
     */
    public static final int TherapeuticQATypeTreatAttention = 103;
    /**
     * 不良反应
     */
    public static final int TherapeuticQATypeBadReaction = 104;
    /**
     * 靶向耐药
     */
    public static final int TherapeuticQATypeTargetMedicine = 105;
    /**
     * 切除范围
     */
    public static final int TherapeuticQATypeResectionScope = 106;
    /**
     * 优势
     */
    public static final int TherapeuticQATypeAdvantage = 107;
    /**
     * 劣势
     */
    public static final int TherapeuticQATypeDisadvantage = 108;
    /**
     * 手术准备
     */
    public static final int TherapeuticQATypeSurgeryPrepare = 109;
    /**
     * 麻醉方式
     */
    public static final int TherapeuticQATypeAnaesthesiaMethod = 110;
    /**
     * 大致流程
     */
    public static final int TherapeuticQATypeRoughlyFolw = 111;
    /**
     * 术后恢复
     */
    public static final int TherapeuticQATypeSurgeryRecover = 112;
    /**
     * 护理特点
     */
    public static final int TherapeuticQATypeNurseCharacteristic = 113;

    /**
     * 关注 入院部分
     */
    public static final int HospitalizedObserve = 1;//观察
    public static final int HospitalizedTreatment = 2;//治疗
    public static final int HospitalizedOperation = 3;//手术
    /**
     * 治疗方法
     */
    public static final int SHOU_SHU = 1;//手术
    public static final int HUA_LIAO = 2;//化疗
    public static final int FANG_LIAO = 3;//放疗
    public static final int BA_XIANG_ZHI_LIAO = 4;//靶向治疗

}
