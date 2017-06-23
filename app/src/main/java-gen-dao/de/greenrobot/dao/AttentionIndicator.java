package de.greenrobot.dao;

/**
 * 关注标志物，用于显示数据
 * Created by Chengbin He on 2016/7/29.
 */
public class AttentionIndicator {

    private String markerDose;
    private boolean attentionMarker;//判断是否选中
    private Indicator indicator;

    public boolean isAttentionMarker() {
        return attentionMarker;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public AttentionIndicator(Indicator indicator, String markerDose, boolean attentionMarker){
        this.indicator = indicator;
        this.markerDose = markerDose;
        this.attentionMarker = attentionMarker;
    }


    public String getMarkerDose() {
        return markerDose;
    }

    public void setMarkerDose(String markerDose) {
        this.markerDose = markerDose;
    }

    public boolean getAttentionMarker() {
        return attentionMarker;
    }

    public void setAttentionMarker(boolean attentionMarker) {
        this.attentionMarker = attentionMarker;
    }
}
