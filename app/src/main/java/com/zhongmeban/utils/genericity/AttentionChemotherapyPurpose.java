package com.zhongmeban.utils.genericity;

/**
 * 化疗目的泛型
 * Created by Chengbin He on 2016/10/11.
 */

public class AttentionChemotherapyPurpose {
    /**
     * 根治性化疗
     */
    public final static int Radical = 1;
    /**
     * 姑息性化疗
     */
    public final static int Palliative = 2;
    /**
     * 辅助化疗
     */
    public final static int Assist = 4;
    /**
     * 新辅助化疗
     */
    public final static int NewAssist = 8;
    /**
     * 局部化疗
     */
    public final static int Part = 16;
}
