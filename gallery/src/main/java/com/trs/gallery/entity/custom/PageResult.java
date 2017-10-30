package com.trs.gallery.entity.custom;

import java.util.List;

public class PageResult<T> {
	
	private List<T> dataList;
	
	private Integer totalNum;

	/**
	 * @return the dataList
	 */
	public List<T> getDataList() {
		return dataList;
	}
	

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	

	/**
	 * @return the totalNum
	 */
	public Integer getTotalNum() {
		return totalNum;
	}
	

	/**
	 * @param totalNum the totalNum to set
	 */
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	
}
