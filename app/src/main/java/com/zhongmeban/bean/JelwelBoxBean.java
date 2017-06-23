package com.zhongmeban.bean;

/**
 *
 * 百宝箱的bean，id： 资源id clazz： 功能对应的activity
 * Created by User on 2016/9/18.
 */
public class JelwelBoxBean extends BaseBean {


    private int id;
    private String name;
    private Class clazz;


    public JelwelBoxBean(int id, String name, Class clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Class getClazz() {
        return clazz;
    }
}
