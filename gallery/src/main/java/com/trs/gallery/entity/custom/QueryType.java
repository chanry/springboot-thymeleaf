package com.trs.gallery.entity.custom;

public enum QueryType {
	
	HOT("热门数据", 1), COLLECT("收藏数据", 2), ACCESS("最近访问", 3);

	private String desc;

	private Integer index;

	private QueryType(String desc, Integer index) {
		this.desc = desc;
		this.index = index;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

}
