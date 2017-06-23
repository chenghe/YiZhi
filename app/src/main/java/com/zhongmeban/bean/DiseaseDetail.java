package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:肿瘤详情
 * user: Chong Chen
 * time：2016/1/26 12:07
 * 邮箱：cchen@ideabinder.com
 */
public class DiseaseDetail extends BaseBean {

    /**
     * notes : 所谓懒癌，就是形容一个人懒到了极致。懒癌分早、中、晚期，其中，懒癌晚期是懒到极致中的极致，也是人们对懒人常用的形容词。据科学家研究，懒癌晚期患者，其实是因为基因突变所导致的。
     * nnotes : nnn
     * laterStage : 懒癌晚期的人，大概就是那种“不要和我比懒，我都懒得和你比”的类型的人。这种破罐子破摔的节奏，觉得自己反正也不可能会有所改变了，如果有人提出抗议，你一定会懒得去理他来应对对方的招式吧。那种诸如为了减少上厕所，所以连水都不喝的人，就是你了。虽然你觉得这也没有妨碍谁，但是不喝水这件事情其实是对自己的身体新陈代谢相当不利的，最后还是你自己吃亏。果然会印证那句“如果有一天你会死，你一定是懒死的”。
     * inspections : [{"name":"CYP3A4基因多态性","id":1,"type":1},{"name":"EGFR基因扩增FISH检测","id":2,"type":2}]
     * earlyStage : 生活中有太多事情是我们不想做的，也是我们不想面对和逃避的，你纯粹就是因为懒得去做，所以放纵自己。最开始你是会告诉自己说，今天的事情推到明天，到了明天又推到后天，这样一天拖一天，你也由最初的拖延症，成为了懒癌。好在你只是处在初期阶段，好好控制一下，还是有得救的，。但是这一切都要基于你成为一个勤快的人，而且成为勤快的人就要有一个引诱的契机。比如恋爱，比如想要有钱。不过你大概是很难找到这样的契机的。

     * diseaseName : 懒癌
     * tnmDbs : [{"sequence":0,"tnmName":"T1","notes":"懒得快没救了","id":8,"type":1,"diseaseId":9},{"sequence":0,"tnmName":"T2","notes":"等死吧","id":9,"type":1,"diseaseId":9},{"sequence":0,"tnmName":"T0","notes":"为时尚早","id":13,"type":1,"diseaseId":9},{"sequence":0,"tnmName":"N0","notes":"你猜行不行","id":20,"type":2,"diseaseId":9},{"sequence":0,"tnmName":"N1","notes":"肯定不行","id":21,"type":2,"diseaseId":9},{"sequence":0,"tnmName":"N2","notes":"那就不行吧","id":22,"type":2,"diseaseId":9},{"sequence":0,"tnmName":"M0","notes":"哈哈快死了","id":23,"type":3,"diseaseId":9},{"sequence":0,"tnmName":"M1","notes":"嘿嘿还活着","id":24,"type":3,"diseaseId":9},{"sequence":0,"tnmName":"M2","notes":"呵呵你看啊","id":25,"type":3,"diseaseId":9}]
     * modeType : 懒癌分早、中、晚期，其中，懒癌晚期是懒到极致中的极致
     * symptom : 1、我有一个声控的台灯，一拍手掌就亮起来，再拍下就灭了。可是吧，我实在是懒的拍手掌，所以我就把自己拍手掌的声音给录了下来，然后设计了个程序用键盘上的一个小小的键来控制声音的播放。
     * mnotes : mmm
     * tnotes : ttt
     * diseaseExplain : 所谓懒癌，指的就是懒到了极致。而在在谈癌色变的当下，懒癌反倒被一群人津津乐道了起来，他们虽然不会干好事，但也不会干坏事，所以从某种意义上说有懒癌也未必是件坏事。而形容懒癌最严重的程度，当然就是懒癌晚期了。
     * interimStage : 作为一名懒癌中期的人，你是那种错过了治疗懒惰的最佳时期，接着要是再不控制一下，就有可能会转为懒癌晚期的类型。懒癌中期的你，对于一些事情也看得开了，还可能会觉得自己的人生也就这样了，尽管有一线机会可能让你彻底转变，可是你还真的是懒得去改变。像那种生活中的懒惰事情，也就不用列举了，倒是那类明明只要努力一下，你的工作、事业可能就会有转机，但是你懒得去努力打拼……渐渐地，还是会走向懒癌末期的哦。

     * organFunction : 并没有什么卵用
     * diseaseId : 9
     * isFavorite : false
     */

    private DataBean data;


    public DataBean getData() { return data;}


    public void setData(DataBean data) { this.data = data;}


    public static class DataBean  implements Serializable {
        private String notes = " ";
        private String nnotes = " ";
        private String laterStage = " ";
        private String earlyStage = " ";
        private String diseaseName = " ";
        private String modeType = " ";
        private String symptom = " ";
        private String mnotes = " ";
        private String tnotes = " ";
        private String diseaseExplain = " ";
        private String interimStage = " ";
        private String organFunction = " ";
        private int diseaseId;
        private boolean isFavorite;
        /**
         * name : CYP3A4基因多态性
         * id : 1
         * type : 1
         */

        private List<InspectionsBean> inspections;
        /**
         * sequence : 0
         * tnmName : T1
         * notes : 懒得快没救了
         * id : 8
         * type : 1
         * diseaseId : 9
         */

        private List<TnmDbsBean> tnmDbs;


        public String getNotes() { return notes;}


        public void setNotes(String notes) { this.notes = notes;}


        public String getNnotes() { return nnotes;}


        public void setNnotes(String nnotes) { this.nnotes = nnotes;}


        public String getLaterStage() { return laterStage;}


        public void setLaterStage(String laterStage) { this.laterStage = laterStage;}


        public String getEarlyStage() { return earlyStage;}


        public void setEarlyStage(String earlyStage) { this.earlyStage = earlyStage;}


        public String getDiseaseName() { return diseaseName;}


        public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName;}


        public String getModeType() { return modeType;}


        public void setModeType(String modeType) { this.modeType = modeType;}


        public String getSymptom() { return symptom;}


        public void setSymptom(String symptom) { this.symptom = symptom;}


        public String getMnotes() { return mnotes;}


        public void setMnotes(String mnotes) { this.mnotes = mnotes;}


        public String getTnotes() { return tnotes;}


        public void setTnotes(String tnotes) { this.tnotes = tnotes;}


        public String getDiseaseExplain() { return diseaseExplain;}


        public void setDiseaseExplain(String diseaseExplain) {
            this.diseaseExplain = diseaseExplain;
        }


        public String getInterimStage() { return interimStage;}


        public void setInterimStage(String interimStage) { this.interimStage = interimStage;}


        public String getOrganFunction() { return organFunction;}


        public void setOrganFunction(String organFunction) { this.organFunction = organFunction;}


        public int getDiseaseId() { return diseaseId;}


        public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId;}


        public boolean isIsFavorite() { return isFavorite;}


        public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite;}


        public List<InspectionsBean> getInspections() { return inspections;}


        public void setInspections(List<InspectionsBean> inspections) {
            this.inspections = inspections;
        }


        public List<TnmDbsBean> getTnmDbs() { return tnmDbs;}


        public void setTnmDbs(List<TnmDbsBean> tnmDbs) { this.tnmDbs = tnmDbs;}


        public static class InspectionsBean implements Serializable {
            private String name = " ";
            private int id;
            private int type;


            public String getName() { return name;}


            public void setName(String name) { this.name = name;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public int getType() { return type;}


            public void setType(int type) { this.type = type;}


            @Override public String toString() {
                return "InspectionsBean{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", type=" + type +
                    '}';
            }
        }


        public static class TnmDbsBean implements Serializable {
            private int sequence;
            private String tnmName = "";
            private String notes = "";
            private int type;
            private int id;
            private int diseaseId;

            public TnmDbsBean(String tnmName, String notes,int type) {
                this.tnmName = tnmName;
                this.notes = notes;
                this.type = type;
            }


            public int getSequence() { return sequence;}


            public void setSequence(int sequence) { this.sequence = sequence;}


            public String getTnmName() { return tnmName;}


            public void setTnmName(String tnmName) { this.tnmName = tnmName;}


            public String getNotes() { return notes;}


            public void setNotes(String notes) { this.notes = notes;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public int getType() { return type;}


            public void setType(int type) { this.type = type;}


            public int getDiseaseId() { return diseaseId;}


            public void setDiseaseId(int diseaseId) { this.diseaseId = diseaseId;}


            @Override public String toString() {
                return "TnmDbsBean{" +
                    "sequence=" + sequence +
                    ", tnmName='" + tnmName + '\'' +
                    ", notes='" + notes + '\'' +
                    ", type=" + type +
                    ", id=" + id +
                    ", diseaseId=" + diseaseId +
                    '}';
            }
        }


        @Override public String toString() {
            return "DataBean{" +
                "notes='" + notes + '\'' +
                ", nnotes='" + nnotes + '\'' +
                ", laterStage='" + laterStage + '\'' +
                ", earlyStage='" + earlyStage + '\'' +
                ", diseaseName='" + diseaseName + '\'' +
                ", modeType='" + modeType + '\'' +
                ", symptom='" + symptom + '\'' +
                ", mnotes='" + mnotes + '\'' +
                ", tnotes='" + tnotes + '\'' +
                ", diseaseExplain='" + diseaseExplain + '\'' +
                ", interimStage='" + interimStage + '\'' +
                ", organFunction='" + organFunction + '\'' +
                ", diseaseId=" + diseaseId +
                ", isFavorite=" + isFavorite +
                ", inspections=" + inspections +
                ", tnmDbs=" + tnmDbs +
                '}';
        }
    }


    @Override public String toString() {
        return "DiseaseDetail{" +
            "data=" + data +
            '}';
    }
}
