package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Description:小贴士的详情
 * user: Chong Chen
 * time：2016/1/22 20:05
 * 邮箱：cchen@ideabinder.com
 */
public class TipsDetails extends BaseBean {
    private TipsDetailsData data;

    public TipsDetailsData getData() {
        return data;
    }

    public void setData(TipsDetailsData data) {
        this.data = data;
    }

    public class TipsDetailsData implements Serializable {
        private String informationId;
        private long time;
        private String writer;
        private String title;
        private int type;
        private String picture;
        private String content;



        public String getInformationId() {
            return informationId;
        }

        public void setInformationId(String informationId) {
            this.informationId = informationId;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getWriter() {
            return writer;
        }

        public void setWriter(String writer) {
            this.writer = writer;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
