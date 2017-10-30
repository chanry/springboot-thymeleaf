package com.trs.gallery.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SubscribeDao {
	/**
	 * 添加多条用户订阅记录
	 * @param userName
	 * @param channelIds
	 */
	public void addRecords(@Param("userName") String userName, @Param("channelIds") List<String> channelIds);
	
	/**
	 * 查询相关栏目的订阅者
	 * @param channelId
	 * @return
	 */
	public List<String> selectSubscribeUser(@Param("channelId") Integer channelId);
}
