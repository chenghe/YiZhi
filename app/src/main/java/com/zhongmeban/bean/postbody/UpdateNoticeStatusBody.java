package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/9/14.
 */
public class UpdateNoticeStatusBody {

    private long noticeId;//通知Id
    private int status;//关注人状态 1未读 2已读

    public long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(long noticeId) {
        this.noticeId = noticeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
