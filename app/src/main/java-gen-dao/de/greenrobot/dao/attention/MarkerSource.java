package de.greenrobot.dao.attention;

import java.io.Serializable;
import java.util.List;

/**
 * 关注模块标志物记录
 * Created by Chengbin He on 2016/8/2.
 */
public class MarkerSource implements Serializable{

    private List<RecordIndex> indexList;
    private List<RecordIndex> statusList;
    private RecordIndexItem indexItem;

    public MarkerSource(List<RecordIndex> indexList, List<RecordIndex> statusList, RecordIndexItem indexItem) {
        this.indexList = indexList;
        this.statusList = statusList;
        this.indexItem = indexItem;
    }

    public List<RecordIndex> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<RecordIndex> indexList) {
        this.indexList = indexList;
    }

    public RecordIndexItem getIndexItem() {
        return indexItem;
    }

    public void setIndexItem(RecordIndexItem indexItem) {
        this.indexItem = indexItem;
    }

    public List<RecordIndex> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<RecordIndex> statusList) {
        this.statusList = statusList;
    }
}
