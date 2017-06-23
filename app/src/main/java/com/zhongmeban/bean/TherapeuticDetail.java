package com.zhongmeban.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:治疗详情
 * user: Chong Chen
 * time：2016/2/2 14:14
 * 邮箱：cchen@ideabinder.com
 */
public class TherapeuticDetail extends BaseBean {
    private TherapeuticData data;

    public TherapeuticData getData() {
        return data;
    }

    public void setData(TherapeuticData data) {
        this.data = data;
    }

    public class TherapeuticData implements Serializable {

        private String id;
        private String name;
        private String description;
        private String advantage;//优势
        private String disadvantage;//劣势
        private String process;//步骤
        private String prepare;//准备期
        private String operation;//操作期
        private String interval;//间隔期
        private String surgery;//手术期
        private String convalescence;//康复期
        private BigDecimal cost;//花费

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAdvantage() {
            return advantage;
        }

        public void setAdvantage(String advantage) {
            this.advantage = advantage;
        }

        public String getDisadvantage() {
            return disadvantage;
        }

        public void setDisadvantage(String disadvantage) {
            this.disadvantage = disadvantage;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getPrepare() {
            return prepare;
        }

        public void setPrepare(String prepare) {
            this.prepare = prepare;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getSurgery() {
            return surgery;
        }

        public void setSurgery(String surgery) {
            this.surgery = surgery;
        }

        public String getConvalescence() {
            return convalescence;
        }

        public void setConvalescence(String convalescence) {
            this.convalescence = convalescence;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }
    }
}
