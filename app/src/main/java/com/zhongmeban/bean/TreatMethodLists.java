package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:治疗方法列表
 * user: Chong Chen
 * time：2016/1/16 17:19
 * 邮箱：cchen@ideabinder.com
 */
public class TreatMethodLists extends BaseBean {
    private List<Data> data;


    public List<Data> getData() {
        return data;
    }


    public void setData(List<Data> data) {
        this.data = data;
    }


    public class Data implements Serializable {
        //        private String diseaseId;
        //        private String disName;
        //        private String diseaseDescription;
        private String therapeuticId;
        private String therapeuticName;
        private String showName;


        public String getShowName() {
            return showName;
        }


        //        private String therapeuticCategoryId;
        //        private String therapeuticDescripiton;
        private String therapeuticPyName;


        public String getTherapeuticId() {
            return therapeuticId;
        }


        public void setTherapeuticId(String therapeuticId) {
            this.therapeuticId = therapeuticId;
        }


        public String getTherapeuticName() {
            return therapeuticName;
        }


        public void setTherapeuticName(String therapeuticName) {
            this.therapeuticName = therapeuticName;
        }


        public String getTherapeuticPyName() {
            return therapeuticPyName;
        }


        public void setTherapeuticPyName(String therapeuticPyName) {
            this.therapeuticPyName = therapeuticPyName;
        }
    }

}
