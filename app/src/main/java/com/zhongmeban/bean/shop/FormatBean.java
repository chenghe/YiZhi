package com.zhongmeban.bean.shop;

import java.io.Serializable;

/**
 * Created by User on 2016/10/20.
 */

public class FormatBean implements Serializable {
    public  int postion;
    public int id;


    public FormatBean(int postion, int id) {
        this.postion = postion;
        this.id = id;
    }


    @Override public boolean equals(Object o) {
        if (o instanceof FormatBean) {
            FormatBean other = (FormatBean) o;
            if (postion == other.postion && id == other.id) {

                return true;
            }
            return false;
        }

        return false;
    }


    @Override public String toString() {
        return "FormatBean{" +
            "postion=" + postion +
            ", id=" + id +
            '}';
    }
}
