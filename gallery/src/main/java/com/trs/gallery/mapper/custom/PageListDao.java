package com.trs.gallery.mapper.custom;

import java.util.List;

import com.trs.gallery.entity.custom.PageInfo;

public interface PageListDao<T> {
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
	
}
