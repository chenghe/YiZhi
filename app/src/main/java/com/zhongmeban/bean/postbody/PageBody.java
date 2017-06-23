package com.zhongmeban.bean.postbody;

/**
 * @Description: Post请求，分页用body
 * Created by Chengbin He on 2016/4/19.
 */
public class PageBody {
    int pageIndex =1;
    int pageSize = 10;
    public PageBody(){

    }
    public PageBody(int pageIndex,int pageSize){
        this.pageIndex = pageIndex ;
        this.pageSize = pageSize;

    }
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


}
