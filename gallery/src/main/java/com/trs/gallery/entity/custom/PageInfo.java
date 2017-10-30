package com.trs.gallery.entity.custom;

import java.util.Map;

import com.trs.gallery.util.ActionResultUtil;

public class PageInfo {
	
	public PageInfo() {
		
	}
	
	
	public PageInfo(Integer pageSize, Integer curPage) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
	}
	
	/**
	 * 
	 * @param pageSize
	 * @param curPage
	 * @param extraField 额外参数，适用仅有一个参数时入参。
	 */
	public PageInfo(Integer pageSize, Integer curPage, String extraField) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
		this.extraField = extraField;
	}


	public PageInfo(Integer pageSize, Integer curPage, Map<String, Object> params) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
		this.params = params;
	}
	
	public PageInfo(Integer pageSize, Integer curPage, Map<String, Object> params, String extraField) {
		super();
		this.pageSize = pageSize;
		this.curPage = curPage;
		this.params = params;
		this.extraField = extraField;
	}


	/**
	 * 额外参数
	 */
	private String extraField;
	
	/**
	 * 分页数量
	 */
	private Integer pageSize;
	
	/**
	 * 查询页面
	 */
	private Integer curPage;
	
	/**
	 * 查询偏移量
	 */
	private Integer offset;
	
	/**
	 * 表字段-参数值
	 */
	private Map<String, Object> params = null;
	

	
	/**
	 * @return the 分页数量
	 */
	public Integer getPageSize() {
		return pageSize == null ? ActionResultUtil.PAGE_NUM_DEFAULT_VAL : pageSize;
	}
	


	/**
	 * @param pageSize the 分页数量 to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	


	/**
	 * @return the 查询页面
	 */
	public Integer getCurPage() {
		return curPage == null ? ActionResultUtil.CUR_PAGE_DEFAULT_VAL : curPage;
	}
	


	/**
	 * @param curPage the 查询页面 to set
	 */
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}


	/**
	 * @return the 查询偏移量
	 */
	public Integer getOffset() {
		this.offset = (getCurPage() - 1) * getPageSize();
		return offset;
	}
	


	/**
	 * @param offset the 查询偏移量 to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}


	/**
	 * @return the 表字段-参数值
	 */
	public Map<String, Object> getParams() {
		return params;
	}
	


	/**
	 * @param params the 表字段-参数值 to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getExtraField() {
		return extraField;
	}

	public void setExtraField(String extraField) {
		this.extraField = extraField;
	}
	
	
	
	
}
