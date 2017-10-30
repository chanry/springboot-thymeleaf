package com.trs.gallery.service;

import java.util.List;

import com.trs.gallery.entity.custom.UserCollectInfoBiz;
import com.trs.gallery.entity.generated.UserCollectInfo;

public interface CollectService extends PageListService<UserCollectInfoBiz> {
	/**
	 * 插入一条用户收藏记录
	 * @param userCollectInfo
	 */
	public void addRecord(UserCollectInfo userCollectInfo);
	
	/**
	 * 删除一条用户收藏记录
	 * @param userName
	 * @param docId
	 */
	public void deleteRecord(String userName, Integer docId);
	
	/**
	 * 根据栏目ID查询收藏记录
	 * @param userName
	 * @param channelId
	 * @return
	 */
	public List<UserCollectInfoBiz> findListBychnlId(String userName, Integer channelId, Integer curPage, Integer pageSize);
	
	/**
	 * 查询文档是否已收藏
	 * @param userName
	 * @param docId
	 * @return
	 */
	public boolean isCollect(String userName, Integer docId);
}
