package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:名词解释列表
 * user: Chong Chen
 * time：2016/1/27 16:32
 * 邮箱：cchen@ideabinder.com
 */
public class NounexplainLists extends BaseBean {
    private NoundExplainData data;

    public NoundExplainData getData() {
        return data;
    }

    public void setData(NoundExplainData data) {
        this.data = data;
    }

    public class NoundExplainData implements Serializable{
        private long serverTime;
        private List<NounScourceItem> source;

        public List<NounScourceItem> getSource() {
            return source;
        }

        public void setSource(List<NounScourceItem> source) {
            this.source = source;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }




    }

    public class NounScourceItem implements Serializable{
        private String interpretationName;
        private Long interpretationId;
        private String pinyinName;
        private boolean isActive;


        public boolean isActive() {
            return isActive;
        }


        public String getPinyinName() {
            return pinyinName;
        }

        public void setPinyinName(String pinyinName) {
            this.pinyinName = pinyinName;
        }

        public String getInterpretationName() {
            return interpretationName;
        }

        public void setInterpretationName(String interpretationName) {
            this.interpretationName = interpretationName;
        }

        public Long getInterpretationId() {
            return interpretationId;
        }

        public void setInterpretationId(Long interpretationId) {
            this.interpretationId = interpretationId;
        }


        @Override public String toString() {
            return "NounScourceItem{" +
                "interpretationName='" + interpretationName + '\'' +
                ", interpretationId=" + interpretationId +
                ", pinyinName='" + pinyinName + '\'' +
                '}';
        }
    }


}
