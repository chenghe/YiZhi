package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2016/9/19.
 */
public class TherapeuticDetailBean extends BaseBean {

    /**
     * qas : [{"sequence":1,"question":"切除范围是哪些？","answer":"全身","createTime":1471833207406,"relationId":1,"updateTime":1471855273112,"id":27,"type":5,"category":106,"isActive":true},{"sequence":2,"question":"切除范围是哪些？切除范围是哪些？","answer":"切除范围是哪些？切除范围是哪些？切除范围是哪些？切除范围是哪些？切除范围是哪些？切除范围是哪些？切除范围是哪些？切除范围是哪些？切除范围是哪些？End","createTime":1471855273112,"relationId":1,"updateTime":1471855273112,"id":31,"type":5,"category":106,"isActive":true},{"sequence":3,"question":"切除范围是哪些？","answer":"切除范围是哪些？切除范围是哪些？EEEEND","createTime":1471855273114,"relationId":1,"updateTime":1471855273114,"id":32,"type":5,"category":106,"isActive":true},{"sequence":1,"question":"我的优势是什么","answer":"呵呵你猜","createTime":1471833207401,"relationId":1,"updateTime":1471855273107,"id":22,"type":5,"category":107,"isActive":true},{"sequence":1,"question":"我的优势是什么","answer":"呵呵呵你猜","createTime":1471833207402,"relationId":1,"updateTime":1471855273108,"id":23,"type":5,"category":108,"isActive":true},{"sequence":1,"question":"手术前需要做什么准备吗","answer":"不需要","createTime":1471833207403,"relationId":1,"updateTime":1471855273109,"id":24,"type":5,"category":109,"isActive":true},{"sequence":1,"question":"麻醉方式是什么","answer":"2瓶伏特加吧","createTime":1471833207404,"relationId":1,"updateTime":1471855273110,"id":25,"type":5,"category":110,"isActive":true},{"sequence":1,"question":"怎样的一个流程呢","answer":"这个我也不知道","createTime":1471833207405,"relationId":1,"updateTime":1471855273110,"id":26,"type":5,"category":111,"isActive":true},{"sequence":2,"question":"流程啊啊啊啊","answer":"我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程我是一个超级长的流程End","createTime":1471855273111,"relationId":1,"updateTime":1471855273111,"id":30,"type":5,"category":111,"isActive":true},{"sequence":1,"question":"术后恢复大致多长时间？","answer":"+1s","createTime":1471833207407,"relationId":1,"updateTime":1471855273115,"id":28,"type":5,"category":112,"isActive":true},{"sequence":1,"question":"需要特殊护理吗","answer":"估计不需要吧","createTime":1471833207408,"relationId":1,"updateTime":1471855273115,"id":29,"type":5,"category":113,"isActive":true}]
     * caseType : 1  1是一线方案 2是二线方案
     * notes : 呵呵不切你就没救了
     * updateTime : 1471855273099
     * isActive : true
     * medicineDbs : [{"therapeuticId":0,"medicineId":1,"id":14,"medicineName":"TNF-α"}] //化疗治疗方案
     * extendDbs : [{"therapeuticId":1,"notes":"xxx","createTime":1466654285055,"updateTime":1466654285055,"id":1,"isActive":true,"url":"xxx"},{"therapeuticId":1,"notes":"ssss","createTime":1466654285055,"updateTime":1466654285055,"id":2,"isActive":true,"url":"sss"}]
     * categoryType : 1
     * pinyinName : lanaigenzhiqiechushu
     * createTime : 1464836627296
     * name : 懒癌根治切除术
     * id : 1
     * categoryId : 1
     * isFavorite : false
     * status : 1
     */

    private DataBean data;


    public DataBean getData() { return data;}


    public void setData(DataBean data) { this.data = data;}


    public static class DataBean  implements Serializable{
        private int caseType;
        private String notes;
        private long updateTime;
        private boolean isActive;
        private int categoryType;
        private String pinyinName;
        private long createTime;
        private String name;
        private int id;
        private int categoryId;
        private boolean isFavorite;
        private int status;
        /**
         * sequence : 1
         * question : 切除范围是哪些？
         * answer : 全身
         * createTime : 1471833207406
         * relationId : 1
         * updateTime : 1471855273112
         * id : 27
         * type : 5
         * category : 106
         * isActive : true
         */

        private List<QasBean> qas;
        /**
         * therapeuticId : 0
         * medicineId : 1
         * id : 14
         * medicineName : TNF-α
         */

        private List<MedicineDbsBean> medicineDbs;
        /**
         * therapeuticId : 1
         * notes : xxx
         * createTime : 1466654285055
         * updateTime : 1466654285055
         * id : 1
         * isActive : true
         * url : xxx
         */

        private List<ExtendDbsBean> extendDbs;


        public int getCaseType() { return caseType;}


        public void setCaseType(int caseType) { this.caseType = caseType;}


        public String getNotes() { return notes;}


        public void setNotes(String notes) { this.notes = notes;}


        public long getUpdateTime() { return updateTime;}


        public void setUpdateTime(long updateTime) { this.updateTime = updateTime;}


        public boolean isIsActive() { return isActive;}


        public void setIsActive(boolean isActive) { this.isActive = isActive;}


        public int getCategoryType() { return categoryType;}


        public void setCategoryType(int categoryType) { this.categoryType = categoryType;}


        public String getPinyinName() { return pinyinName;}


        public void setPinyinName(String pinyinName) { this.pinyinName = pinyinName;}


        public long getCreateTime() { return createTime;}


        public void setCreateTime(long createTime) { this.createTime = createTime;}


        public String getName() { return name;}


        public void setName(String name) { this.name = name;}


        public int getId() { return id;}


        public void setId(int id) { this.id = id;}


        public int getCategoryId() { return categoryId;}


        public void setCategoryId(int categoryId) { this.categoryId = categoryId;}


        public boolean isIsFavorite() { return isFavorite;}


        public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite;}


        public int getStatus() { return status;}


        public void setStatus(int status) { this.status = status;}


        public List<QasBean> getQas() { return qas;}


        public void setQas(List<QasBean> qas) { this.qas = qas;}


        public List<MedicineDbsBean> getMedicineDbs() { return medicineDbs;}


        public void setMedicineDbs(List<MedicineDbsBean> medicineDbs) {
            this.medicineDbs = medicineDbs;
        }


        public List<ExtendDbsBean> getExtendDbs() { return extendDbs;}


        public void setExtendDbs(List<ExtendDbsBean> extendDbs) { this.extendDbs = extendDbs;}


        public static class QasBean  implements Serializable{
            private int sequence;
            private String question;
            private String answer;
            private long createTime;
            private int relationId;
            private long updateTime;
            private int id;
            private int type;
            private int category;
            private boolean isActive;


            public int getSequence() { return sequence;}


            public void setSequence(int sequence) { this.sequence = sequence;}


            public String getQuestion() { return question;}


            public void setQuestion(String question) { this.question = question;}


            public String getAnswer() { return answer;}


            public void setAnswer(String answer) { this.answer = answer;}


            public long getCreateTime() { return createTime;}


            public void setCreateTime(long createTime) { this.createTime = createTime;}


            public int getRelationId() { return relationId;}


            public void setRelationId(int relationId) { this.relationId = relationId;}


            public long getUpdateTime() { return updateTime;}


            public void setUpdateTime(long updateTime) { this.updateTime = updateTime;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public int getType() { return type;}


            public void setType(int type) { this.type = type;}


            public int getCategory() { return category;}


            public void setCategory(int category) { this.category = category;}


            public boolean isIsActive() { return isActive;}


            public void setIsActive(boolean isActive) { this.isActive = isActive;}


            @Override public String toString() {
                return "QasBean{" +
                    "sequence=" + sequence +
                    ", question='" + question + '\'' +
                    ", answer='" + answer + '\'' +
                    ", createTime=" + createTime +
                    ", relationId=" + relationId +
                    ", updateTime=" + updateTime +
                    ", id=" + id +
                    ", type=" + type +
                    ", category=" + category +
                    ", isActive=" + isActive +
                    '}';
            }
        }


        public static class MedicineDbsBean implements Serializable {
            private int therapeuticId;
            private int medicineId;
            private int id;
            private String medicineName;


            public int getTherapeuticId() { return therapeuticId;}


            public void setTherapeuticId(int therapeuticId) { this.therapeuticId = therapeuticId;}


            public int getMedicineId() { return medicineId;}


            public void setMedicineId(int medicineId) { this.medicineId = medicineId;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public String getMedicineName() { return medicineName;}


            public void setMedicineName(String medicineName) { this.medicineName = medicineName;}


            @Override public String toString() {
                return "MedicineDbsBean{" +
                    "therapeuticId=" + therapeuticId +
                    ", medicineId=" + medicineId +
                    ", id=" + id +
                    ", medicineName='" + medicineName + '\'' +
                    '}';
            }
        }


        public static class ExtendDbsBean  implements Serializable {
            private int therapeuticId;
            private String notes;
            private String picUrl;
            private long createTime;
            private long updateTime;
            private int id;
            private boolean isActive;
            private String url;

            private String title;

            public int getTherapeuticId() { return therapeuticId;}


            public void setTherapeuticId(int therapeuticId) { this.therapeuticId = therapeuticId;}


            public String getNotes() { return notes;}


            public String getTitle() {
                return title;
            }


            public void setNotes(String notes) { this.notes = notes;}


            public long getCreateTime() { return createTime;}


            public void setCreateTime(long createTime) { this.createTime = createTime;}


            public long getUpdateTime() { return updateTime;}


            public void setUpdateTime(long updateTime) { this.updateTime = updateTime;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public boolean isIsActive() { return isActive;}


            public void setIsActive(boolean isActive) { this.isActive = isActive;}


            public String getUrl() { return url;}


            public void setUrl(String url) { this.url = url;}


            public String getPicUrl() {
                return picUrl;
            }


            @Override public String toString() {
                return "ExtendDbsBean{" +
                    "therapeuticId=" + therapeuticId +
                    ", notes='" + notes + '\'' +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    ", id=" + id +
                    ", isActive=" + isActive +
                    ", url='" + url + '\'' +
                    '}';
            }
        }


        @Override public String toString() {
            return "DataBean{" +
                "caseType=" + caseType +
                ", notes='" + notes + '\'' +
                ", updateTime=" + updateTime +
                ", isActive=" + isActive +
                ", categoryType=" + categoryType +
                ", pinyinName='" + pinyinName + '\'' +
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", categoryId=" + categoryId +
                ", isFavorite=" + isFavorite +
                ", status=" + status +
                ", qas=" + qas +
                ", medicineDbs=" + medicineDbs +
                ", extendDbs=" + extendDbs +
                '}';
        }
    }


    @Override public String toString() {
        return "TherapeuticDetailBean{" +
            "data=" + data +
            '}';
    }
}
