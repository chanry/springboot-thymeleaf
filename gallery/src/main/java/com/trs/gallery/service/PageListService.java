package com.trs.gallery.service;

import java.util.List;

import com.trs.gallery.entity.custom.PageInfo;
import com.trs.gallery.entity.custom.PageResult;
import com.trs.gallery.internal.exception.NullParamException;

public interface PageListService<T> {
	
	/**
	 * 查询数据列表
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param pageInfo
	 * @return
	 */
	public List<T> findList(PageInfo pageInfo);
	
	/**
	 * 查询数据总数
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param pageInfo
	 * @return
	 */
	public Integer findListTotalNum(PageInfo pageInfo);
	
	/**
	 * 查询数据列表和总数
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param pageInfo
	 * @return
	 * @throws NullParamException 
	 */
	public PageResult<T> findListAndTotalNum(PageInfo pageInfo);
	
}
