package com.trs.gallery.service;

import java.util.List;

public interface BaseService<T> {
	/**
	 * 查询单表所有记录
	 * @return
	 */
	public List<T> findList();
	
	/**
	 * 查询单条记录
	 * @param t
	 * @return
	 */
	public T findRecord(T t);
	
	/**
	 * 查询多条记录
	 * @param t
	 * @return
	 */
	public List<T> findList(T t);
	
	/**
	 * 删除记录
	 * @param t
	 */
	public void deleteRecord(T t);
	
	/**
	 * 新增记录
	 * @param t
	 */
	public void addRecord(T t);
}
