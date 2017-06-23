package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

import de.greenrobot.dao.BaseDao;

/**
 * Description:疾病类型的列表
 * user: Chong Chen
 * time：2016/1/15 16:05
 * 邮箱：cchen@ideabinder.com
 */
public class TrumerLists extends BaseBean {
    private TrumerListsData data;

    public TrumerListsData getData() {
        return data;
    }

    public void setData(TrumerListsData data) {
        this.data = data;
    }

    public class TrumerListsData implements Serializable {
        private List<TrumerSource> source;
        private long serverTime;

        public List<TrumerSource> getSource() {
            return source;
        }

        public void setSource(List<TrumerSource> source) {
            this.source = source;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }


        @Override public String toString() {
            return "TrumerListsData{" +
                "source=" + source +
                ", serverTime=" + serverTime +
                '}';
        }
    }

    public class TrumerSource extends BaseDao implements Serializable {
        private int id;
        private boolean isActive;
//        private String name;
//        private String pinyinName;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public String getPinyinName() {
            return pinyinName;
        }

        public void setPinyinName(String pinyinName) {
            this.pinyinName = pinyinName;
        }


        @Override public String toString() {
            return "TrumerSource{" +
                "name"+name+
                "id=" + id +
                ", isActive=" + isActive +
                ", status=" + status +
                '}';
        }
    }


    @Override public String toString() {
        return "TrumerLists{" +
            "data=" + data +
            '}';
    }
}
