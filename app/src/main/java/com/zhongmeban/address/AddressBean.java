package com.zhongmeban.address;

import java.io.Serializable;

/**
 * Created by User on 2016/10/20.
 */

public class AddressBean implements Serializable{

    /**
     * id : 2
     * name : 东城区
     * fullName : 北京市东城区
     * treePath : ,1,
     * grade : 1
     */

    private int id;
    public String name;
    private String fullName;
    private String treePath;
    private int grade;


    public int getId() { return id;}


    public void setId(int id) { this.id = id;}


    public String getName() { return name;}


    public void setName(String name) { this.name = name;}


    public String getFullName() { return fullName;}


    public void setFullName(String fullName) { this.fullName = fullName;}


    public String getTreePath() { return treePath;}


    public void setTreePath(String treePath) { this.treePath = treePath;}


    public int getGrade() { return grade;}


    public void setGrade(int grade) { this.grade = grade;}
}
