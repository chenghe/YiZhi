package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

import de.greenrobot.dao.BaseDao;

/**
 * Description:治疗方法种类列表
 * user: Chong Chen
 * time：2016/1/15 17:15
 * 邮箱：cchen@ideabinder.com
 */
public class TreatCategoryLists extends BaseBean {
    private List<TreatCategorySource> data;

    public List<TreatCategorySource> getData() {
        return data;
    }

    public void setData(List<TreatCategorySource> data) {
        this.data = data;
    }

    public class TreatCategorySource extends BaseDao implements Serializable {
        private String id;
//        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
