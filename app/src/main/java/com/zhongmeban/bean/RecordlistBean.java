package com.zhongmeban.bean;

import java.util.List;

/**
 * Created by Chengbin He on 2016/9/2.
 */
public class RecordlistBean {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private int startCount;
        private int totalPage;//总页数
        private int endCount;
        private int pageSize;
        private int indexPage;
        private List<SourceItems> sourceItems;

        public int getIndexPage() {
            return indexPage;
        }

        public void setIndexPage(int indexPage) {
            this.indexPage = indexPage;
        }

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

        public List<SourceItems> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<SourceItems> sourceItems) {
            this.sourceItems = sourceItems;
        }
    }

    public class SourceItems{
//        private long id;
        private int type;
        private long time1;//开始时间
        private long time2;//结束时间（可空）
        private String subTitle;//备注（可空）
        private String content;//内容
        private String context;//描述（可空）
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getTime1() {
            return time1;
        }

        public void setTime1(long time1) {
            this.time1 = time1;
        }

        public long getTime2() {
            return time2;
        }

        public void setTime2(long time2) {
            this.time2 = time2;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }
}
