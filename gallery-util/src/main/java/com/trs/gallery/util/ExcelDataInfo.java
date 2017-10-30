package com.trs.gallery.util;

import java.util.Map;

public class ExcelDataInfo {
	/**
	 * 主键ID
	 */
	private Integer id;
	
	/**
	 * 外键关联ID
	 */
	private String infoId;
	
	/**
	 * 第一列
	 */
	private String field0;
	
	/**
	 * 第二列
	 */
	private String field1;
	
	/**
	 * 第三列
	 */
	private String field2;
	
	
	private Map<String, String> extendsFields;

	/**
	 * @return the 主键ID
	 */
	public Integer getId() {
		return id;
	}
	

	/**
	 * @param id the 主键ID to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	

	/**
	 * @return the 外键关联ID
	 */
	public String getInfoId() {
		return infoId;
	}
	

	/**
	 * @param infoId the 外键关联ID to set
	 */
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	

	/**
	 * @return the 第一列
	 */
	public String getField0() {
		return field0;
	}
	

	/**
	 * @param field0 the 第一列 to set
	 */
	public void setField0(String field0) {
		this.field0 = field0;
	}
	

	/**
	 * @return the 第二列
	 */
	public String getField1() {
		return field1;
	}
	

	/**
	 * @param field1 the 第二列 to set
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}
	

	/**
	 * @return the 第三列
	 */
	public String getField2() {
		return field2;
	}
	

	/**
	 * @param field2 the 第三列 to set
	 */
	public void setField2(String field2) {
		this.field2 = field2;
	}


	/**
	 * @return the extendsFields
	 */
	public Map<String, String> getExtendsFields() {
		return extendsFields;
	}
	


	/**
	 * @param extendsFields the extendsFields to set
	 */
	public void setExtendsFields(Map<String, String> extendsFields) {
		this.extendsFields = extendsFields;
	}
	
	
	
}
