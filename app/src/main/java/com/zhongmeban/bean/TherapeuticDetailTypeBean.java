package com.zhongmeban.bean;

/**
 * 手术治疗详情页面，本地数据使用的bean
 *
 * Created by User on 2016/9/19.
 */
public class TherapeuticDetailTypeBean extends BaseBean {

    private String title;
    private String question;
    private String answer;
    private int imgId;
    private int type;


    public TherapeuticDetailTypeBean(String title, String question, String answer, int imgId, int type) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.imgId = imgId;
        this.type = type;
    }


    public String getTitle() {
        return title;
    }


    public String getQuestion() {
        return question;
    }


    public String getAnswer() {
        return answer;
    }


    public int getImgId() {
        return imgId;
    }


    public int getType() {
        return type;
    }
}
