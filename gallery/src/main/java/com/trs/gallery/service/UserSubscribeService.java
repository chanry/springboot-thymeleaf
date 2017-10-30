package com.trs.gallery.service;

import java.util.List;

import com.trs.gallery.entity.generated.UserSubscribeInfoKey;

public interface UserSubscribeService extends BaseService<UserSubscribeInfoKey> {
	/**
	 * 批量添加订阅栏目
	 * @param userName
	 * @param channelIds
	 */
	public void addRecords(String userName, List<String> channelIds);
	
	/**
	 * 通过栏目ID查询订阅该栏目的所有用户名
	 * @param channelId
	 * @return
	 */
	public List<String> findList(Integer channelId);
	
}
