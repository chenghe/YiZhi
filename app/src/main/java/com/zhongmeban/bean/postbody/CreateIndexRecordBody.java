package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * 创建指标检查记录 Body
 * Created by Chengbin He on 2016/7/26.
 */
public class CreateIndexRecordBody {

    private AttentionMarkerIndexItem indexItem;
    private List<AttentionMarkerIndexs> indexs;

    public CreateIndexRecordBody() {
        AttentionMarkerIndexItem indexItem = new AttentionMarkerIndexItem();
        this.indexItem = indexItem;
    }

    public AttentionMarkerIndexItem getIndexItem() {
        return indexItem;
    }

    public void setIndexItem(AttentionMarkerIndexItem indexItem) {
        this.indexItem = indexItem;
    }

    public List<AttentionMarkerIndexs> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<AttentionMarkerIndexs> indexs) {
        this.indexs = indexs;
    }



}
