package com.trs.gallery.entity.custom;

public class Message {
	/**
	 * 字段名
	 */
	private String key;
	
	/**
	 * 错误描述
	 */
	private String desc;

	
	public Message(String key, String desc) {
		super();
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
