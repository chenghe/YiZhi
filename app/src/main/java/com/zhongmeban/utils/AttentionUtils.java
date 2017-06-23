package com.zhongmeban.utils;

import com.zhongmeban.utils.genericity.AttentionChemotherapyPurpose;
import com.zhongmeban.utils.genericity.AttentionChemotherapyStopReason;
import de.greenrobot.dao.attention.ChemotherapyCourseMedicine;
import de.greenrobot.dao.attention.ChemotherapyMedicine;
import de.greenrobot.dao.attention.SurgeryMethods;
import java.util.List;

/**
 * Created by Chengbin He on 2016/9/7.
 */
public class AttentionUtils {

    /**
     * 获取关系名称
     */
    public static String getRelationName(int gender, int relation) {
        String relationName = "";
        switch (relation) {

            case 1:
                relationName = "本人";
                break;
            case 2:
                relationName = "父亲";
                break;
            case 3:
                relationName = "母亲";
                break;
            case 4:
                relationName = "丈夫";
                break;
            case 5:
                relationName = "妻子";
                break;
            case 6:
                relationName = "儿子";
                break;
            case 7:
                relationName = "女儿";
                break;
            case 9:
                relationName = "其他";
                break;
        }
        return relationName;
    }


    /**
     * 获取化疗目的名称
     */
    public static String getChemotherapyPurposeName(int chemotherapyAim) {
        String purPoseName = "";
        if ((chemotherapyAim & AttentionChemotherapyPurpose.Radical) != 0) {
            purPoseName += "根治性化疗 ";
        }
        if ((chemotherapyAim & AttentionChemotherapyPurpose.Palliative) != 0) {
            purPoseName += "姑息性化疗 ";
        }
        if ((chemotherapyAim & AttentionChemotherapyPurpose.Assist) != 0) {
            purPoseName += "辅助性化疗 ";
        }
        if ((chemotherapyAim & AttentionChemotherapyPurpose.NewAssist) != 0) {
            purPoseName += "新辅助性化疗 ";
        }
        if ((chemotherapyAim & AttentionChemotherapyPurpose.Part) != 0) {
            purPoseName += "局部性化疗 ";
        }
        //        purPoseName = purPoseName;
        return purPoseName;
    }


    //根据化疗目的 的位置算出type
    public static int getChemotherapyAnimByPos(int pos) {
        switch (pos) {
            case 0:
                return AttentionChemotherapyPurpose.Radical;
            case 1:
                return AttentionChemotherapyPurpose.Palliative;
            case 2:
                return AttentionChemotherapyPurpose.Assist;
            case 3:
                return AttentionChemotherapyPurpose.NewAssist;
            case 4:
                return AttentionChemotherapyPurpose.Part;
            default:
                break;
        }
        return AttentionChemotherapyPurpose.Assist;
    }


    //根据化疗目的 的type算出位置
    public static int getChemotherapyTargetPosByType(int type) {
        switch (type) {
            case AttentionChemotherapyPurpose.Radical:
                return 0;
            case AttentionChemotherapyPurpose.Palliative:
                return 1;
            case AttentionChemotherapyPurpose.Assist:
                return 2;
            case AttentionChemotherapyPurpose.NewAssist:
                return 3;
            case AttentionChemotherapyPurpose.Part:
                return 4;
            default:
                break;
        }
        return 0;
    }


    //根据位置算出化疗备注的type
    public static int getChemotherapyRemarkByPos(int pos) {
        switch (pos) {
            case 0:
                return AttentionChemotherapyStopReason.FINISH;
            case 1:
                return AttentionChemotherapyStopReason.STOP;
            case 2:
                return AttentionChemotherapyStopReason.OTHER;
            case 3:
                return AttentionChemotherapyStopReason.ING;
            default:
                break;
        }
        return AttentionChemotherapyStopReason.OTHER;
    }


    //根据化疗备注的type算出位置
    public static int getChemotherapyRemarkPosByType(int type) {
        switch (type) {
            case AttentionChemotherapyStopReason.FINISH:
                return 0;
            case AttentionChemotherapyStopReason.STOP:
                return 1;
            case AttentionChemotherapyStopReason.OTHER:
                return 2;
            case AttentionChemotherapyStopReason.ING:
                return 3;
            default:
                break;
        }
        return 0;
    }


    /**
     * 获取终止化疗原因
     */
    public static String getStopChemotherapyResaonName(int reason) {
        String name = "";
        switch (reason) {
            case AttentionChemotherapyStopReason.FINISH:
                name = "周期结束";
                break;
            case AttentionChemotherapyStopReason.STOP:
                name = "终止化疗";
                break;
            case AttentionChemotherapyStopReason.OTHER:
                name = "更换方案";
                break;
            case AttentionChemotherapyStopReason.ING:
                name = "正在进行中";
                break;
            default:
                break;
        }
        return name;
    }

    /**
     * 获取终止化疗原因
     */
    public static String getRadiotherapyResultType(int reason) {
        String name = "";
        switch (reason) {
            case 1:
                name = "周期结束";
                break;
            case 2:
                name = "耐受终止";
                break;
            case 3:
                name = "手术准备";
                break;
            case 4:
                name = "治疗进行中";
                break;
            default:
                break;
        }
        return name;
    }
    /**
     * 获取入院目的
     */
    public static String getHospitalizedType(int type) {
        String name = "";
        switch (type) {
            case 1:
                name = "观察";
                break;
            case 2:
                name = "治疗";
                break;
            case 3:
                name = "手术";
                break;
            default:
                break;
        }
        return name;
    }

    /**
     * 获取化疗用药Name
     */
    public static String getChemotherapyMedicineName(List<ChemotherapyMedicine> dbChemotherapyMedicine) {
        String name = "";
        for (ChemotherapyMedicine chemotherapyMedicine : dbChemotherapyMedicine) {
            String chemotherapyMedicineName = chemotherapyMedicine.getMedicineName();
            name += chemotherapyMedicineName + "/";
        }
        return name;
    }


    /**
     * 获取化疗用药Name
     */
    public static String getChemotherapyCourseMedicineName(List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicine) {
        String name = "";
        for (ChemotherapyCourseMedicine chemotherapyCourseMedicine : dbChemotherapyCourseMedicine) {
            String chemotherapyMedicineName = chemotherapyCourseMedicine.getMedicineName();
            name += chemotherapyMedicineName + "/";
        }
        return name;
    }


    /**
     * 获取用药目的
     */
    public static String getTakeMedicinePurpose(int type) {
        String purposeName = "";
        if ((type & 1) != 0) {
            purposeName += "对症用药 ";
        }
        if ((type & 2) != 0) {
            purposeName += "靶向治疗 ";
        }
        if ((type & 4) != 0) {
            purposeName += "镇痛用药 ";
        }
        if ((type & 8) != 0) {
            purposeName += "其他用药 ";
        }
        //        purposeName = purposeName;
        return purposeName;
    }


    /**
     * 获取其他手术项名称
     */
    public static String getOtherOperationName(List<SurgeryMethods> surgeryMethodsList) {
        String name = "";
        for (SurgeryMethods surgeryMethods : surgeryMethodsList) {
            String methodsName = surgeryMethods.getSurgeryMethodName();
            name += methodsName + "/";
        }
        return name;
    }
}
