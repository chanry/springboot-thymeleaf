package com.trs.gallery.entity.custom;

public class CustomField {
	/**
	 * 字段ID 
	 * 注：此处id为文档ID，目的是为了避免查询同一栏目下的
	 * 文档列表时由于所有扩展字段都一样，导致mybatis查询的
	 * 所有CustomField实体集合指向同一个引用，以至于之后
	 * 对value赋值会覆盖其他CustomFied对象的value
	 */
	private Integer id;
	/**
	 * 字段名
	 */
	private String name;
	
	/**
	 * 字段描述
	 */
	private String desc;
	
	/**
	 * 字段值
	 */
	private Object value;
	
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object object) {
		this.value = object;
	}
}
