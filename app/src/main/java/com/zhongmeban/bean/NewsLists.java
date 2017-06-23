package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:新闻列表的实体
 * user: Chong Chen
 * time：2016/2/23 12:30
 * 邮箱：cchen@ideabinder.com
 */
public class NewsLists extends BaseBean {


    private NewsData data;

    public NewsData getData() {
        return data;
    }

    public void setData(NewsData data) {
        this.data = data;
    }

    public class NewsData implements Serializable {
        private int startCount;
        private int totalPage;
        private int endCount;
        private int pageSize;
        private List<NewsItem> sourceItems;
        private int totalCount;
        private int indexPage;

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

        public List<NewsItem> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<NewsItem> sourceItems) {
            this.sourceItems = sourceItems;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getIndexPage() {
            return indexPage;
        }

        public void setIndexPage(int indexPage) {
            this.indexPage = indexPage;
        }


        @Override public String toString() {
            return "NewsData{" +
                "startCount=" + startCount +
                ", totalPage=" + totalPage +
                ", endCount=" + endCount +
                ", pageSize=" + pageSize +
                ", sourceItems=" + sourceItems +
                ", totalCount=" + totalCount +
                ", indexPage=" + indexPage +
                '}';
        }
    }


    public class NewsItem implements Serializable {
        private String id;
        private long time;
        private String title;
        private String picture;
        private String absContent;
        private String writer;
        private String urlAddress;
        private int type;


        public String getUrlAddress() {
            return urlAddress;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override public String toString() {
            return "NewsItem{" +"urlAddress"+urlAddress+
                ", time=" + time +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", picture='" + picture + '\'' +
                ", absContent='" + absContent + '\'' +
                ", writer='" + writer + '\'' +
                '}';
        }


        public String getAbstractContent() {
            return absContent;
        }

        public void setAbstractContent(String abstractContent) {
            this.absContent = abstractContent;
        }

        public String getWriter() {
            return writer;
        }

        public void setWriter(String writer) {
            this.writer = writer;
        }


        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
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
    }


    @Override public String toString() {
        return "NewsLists{" +
            "data=" + data +
            '}';
    }
}
