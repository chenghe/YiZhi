package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 城市列表实体
 * user: Chong Chen
 * time：2016/3/1 16:49
 * 邮箱：cchen@ideabinder.com
 */
public class CityList extends BaseBean {
    private List<City> data;
    private long serverTime;

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public List<City> getData() {
        return data;
    }

    public void setData(List<City> data) {
        this.data = data;
    }

    public class City implements Serializable {
        private String name;
        private int id;
        private String pinyinName;

        public String getPinyinName() {
            return pinyinName;
        }

        public void setPinyinName(String pinyinName) {
            this.pinyinName = pinyinName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
