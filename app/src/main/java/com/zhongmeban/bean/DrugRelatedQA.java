package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:药品问答的实体
 * user: Chong Chen
 * time：2016/1/21 16:48
 * 邮箱：cchen@ideabinder.com
 */
public class DrugRelatedQA extends BaseBean {
    private DrugRelatedQAData data;

    public DrugRelatedQAData getData() {
        return data;
    }

    public void setData(DrugRelatedQAData data) {
        this.data = data;
    }

    public class DrugRelatedQAData implements Serializable{
        private int startCount;
        private int totalPage;
        private int endCount;
        private int pageSize;
        private int totalCount;
        private int indexPage;
        private List<DrugQASourceItem> sourceItems;

        public int getStartCount() {
            return startCount;
        }

        public void setStartCount(int startCount) {
            this.startCount = startCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getEndCount() {
            return endCount;
        }

        public void setEndCount(int endCount) {
            this.endCount = endCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getIndexPage() {
            return indexPage;
        }

        public void setIndexPage(int indexPage) {
            this.indexPage = indexPage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<DrugQASourceItem> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<DrugQASourceItem> sourceItems) {
            this.sourceItems = sourceItems;
        }
    }

    public class DrugQASourceItem implements Serializable{
        private String questionContent;
        private long answerTime;
        private long questionTime;
        private String medicineId;
        private String answerContent;

        public String getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(String medicineId) {
            this.medicineId = medicineId;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public void setQuestionContent(String questionContent) {
            this.questionContent = questionContent;
        }

        public long getAnswerTime() {
            return answerTime;
        }

        public void setAnswerTime(long answerTime) {
            this.answerTime = answerTime;
        }

        public long getQuestionTime() {
            return questionTime;
        }

        public void setQuestionTime(long questionTime) {
            this.questionTime = questionTime;
        }
    }
}
