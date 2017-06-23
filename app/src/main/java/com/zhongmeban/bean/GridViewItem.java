package com.zhongmeban.bean;

/**
 * 药厂列表的item
 */
public class GridViewItem {
    private String name;
    private int imageId;
	public GridViewItem(String name, int imageId) {
		super();
		this.name = name;
		this.imageId = imageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

}
