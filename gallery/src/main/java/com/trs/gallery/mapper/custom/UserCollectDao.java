package com.trs.gallery.mapper.custom;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.trs.gallery.entity.custom.CustomField;
import com.trs.gallery.entity.custom.UserCollectInfoBiz;

public interface UserCollectDao extends PageListDao<UserCollectInfoBiz> {
	/**
	 * 根据栏目ID查询该栏目的扩展字段信息
	 * @param docchannel
	 * @return
	 */
	public List<CustomField> selectCustomFields(@Param("docchannel") Integer docchannel);
	
	/**
	 * 传入需要查询的扩展字段和文档ID查询扩展字段内容
	 * @param field
	 * @param docId
	 * @return
	 */
	public Map<String, Object> selectCustomFieldValue(@Param("field") String field, @Param("docId") Integer docId);

	/**
	 * 根据用户名和栏目ID获取收藏记录
	 * @param userName
	 * @param pageSize 
	 * @param start 
	 * @param channelId
	 * @return
	 */
	public List<UserCollectInfoBiz> findListBychnlIds(@Param("userName") String userName, @Param("channelIds") List<Integer> channelIds, @Param("start")Integer start, @Param("pageSize")Integer pageSize);
	
	/**
	 * 查询某文档是否收藏
	 * @param userName
	 * @param docId
	 * @return
	 */
	public Integer findCountByDocId(@Param("userName") String userName, @Param("docId") Integer docId);
}
