package com.zhongmeban.bean;

import java.io.Serializable;

/**
 *
 * Description:新闻详情
 * user: Chong Chen
 * time：2016/2/24 11:22
 * 邮箱：cchen@ideabinder.com
 */
public class NewsDetail extends BaseBean {
    private NewsDetailDetail data;

    public NewsDetailDetail getData() {
        return data;
    }

    public void setData(NewsDetailDetail data) {
        this.data = data;
    }

    public class NewsDetailDetail implements Serializable {

        /**
         * informationId : 1
         * time : 1478167973098
         * writer : 测试information
         * title : 测试information
         * type : 0
         * picture :
         * content : <div style="text-align: center;"><b><i>测试information</i></b></div>
         * isFavorite : false
         */

        private int informationId;
        private long time;
        private String writer;
        private String title;
        private int type;
        private String picture;
        private String content;
        private boolean isFavorite;


        public int getInformationId() { return informationId;}


        public void setInformationId(int informationId) { this.informationId = informationId;}


        public long getTime() { return time;}


        public void setTime(long time) { this.time = time;}


        public String getWriter() { return writer;}


        public void setWriter(String writer) { this.writer = writer;}


        public String getTitle() { return title;}


        public void setTitle(String title) { this.title = title;}


        public int getType() { return type;}


        public void setType(int type) { this.type = type;}


        public String getPicture() { return picture;}


        public void setPicture(String picture) { this.picture = picture;}


        public String getContent() { return content;}


        public void setContent(String content) { this.content = content;}


        public boolean isIsFavorite() { return isFavorite;}


        public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite;}
    }

}
