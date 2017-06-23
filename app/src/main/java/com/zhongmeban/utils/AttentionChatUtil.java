package com.zhongmeban.utils;

/**
 * Created by Chengbin He on 2016/11/7.
 */

public class AttentionChatUtil {

    /**
     * 计算图表横坐标位置
     * @param value
     * @param normalValue
     * @return
     */

    public static float changeValueToY(float value,float normalValue){
        float y = 0;
        float percent = (value-normalValue)/normalValue;
        if (percent>2){
            y = 10;//最大200%
        }else if (percent<-1){
            y = 0;//最小-100%
        }else {
            y=(percent*10+10)/3;
        }
        return y;
    }
}
