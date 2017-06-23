package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:名词解释详情的实体
 * user: Chong Chen
 * time：2016/1/27 17:16
 * 邮箱：cchen@ideabinder.com
 */
public class NounDetails extends BaseBean {
    private NounDetailData data;

    public NounDetailData getData() {
        return data;
    }

    public void setData(NounDetailData data) {
        this.data = data;
    }


    public class NounDetailData implements Serializable{
        private String interpretationName;
        private List<String> diseases;
        private String notes;
        private String interpretationId;

        public String getInterpretationName() {
            return interpretationName;
        }

        public void setInterpretationName(String interpretationName) {
            this.interpretationName = interpretationName;
        }

        public List<String> getDiseases() {
            return diseases;
        }

        public void setDiseases(List<String> diseases) {
            this.diseases = diseases;
        }

        public String getDescription() {
            return notes;
        }

        public void setDescription(String description) {
            this.notes = description;
        }

        public String getInterpretationId() {
            return interpretationId;
        }

        public void setInterpretationId(String interpretationId) {
            this.interpretationId = interpretationId;
        }
    }
}
