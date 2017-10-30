package com.trs.gallery.mapper.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.trs.gallery.entity.custom.ChannelInfo;
import com.trs.gallery.entity.custom.DCJSDocumentInfo;
import com.trs.gallery.entity.custom.DocumentInfo;

public interface DocumentDao {
	/**
	 * 根据文档ID查询文档列表
	 * @param docIds
	 * @return
	 */
	public List<DocumentInfo> findDocumentsBychnldocIds(@Param("chnldocIds") List<Integer> chnldocIds);
	
	/**
	 * 根据栏目ID查询文档列表
	 * @param docIds
	 * @return
	 */
	public List<DocumentInfo> findDocumentsBychnlIds(@Param("channelIds")List<Integer> channelIds);
	
	/**
	 * 根据栏目ID获取栏目信息
	 * @param channelIds
	 * @return
	 */
	@MapKey("CHANNELID")
	public Map<Integer, Map<String, String>> findChnlInfo(@Param("channelIds")List<Integer> channelIds);
	
	/**
	 * 传入需要查询的扩展字段和文档ID查询扩展字段内容
	 * @param field
	 * @param docId
	 * @return
	 */
	public List<Map<String, Object>> selectCustomFieldValue(@Param("field") String field, @Param("docIds") List<Integer> docIds);
	
	/**
	 * 根据日期查询当天新闻
	 * @param date
	 * @return
	 */
	public List<DocumentInfo> findNewsByDate(@Param("date") Date date, @Param("channelIds")List<Integer> channelIds);
	
	/**
	 * 根据日期查询展览时间包含该日期的展览
	 * @param date
	 * @return
	 */
	public List<DocumentInfo> findExhibitionByDate(@Param("date") Date date, @Param("channelIds")List<Integer> channelIds);
	
	/**
	 * 根据日期查询包含该日期的活动
	 * @param date
	 * @return
	 */
	public List<DocumentInfo> findActivityByDate(@Param("date") Date date, @Param("channelIds")List<Integer> channelIds);
	
	/**
	 * 根据栏目ID获取所有子栏目ID
	 * @param channelId
	 * @return
	 */
	public List<ChannelInfo> findAllChildChnlIdsByChnlId(@Param("channelId")Integer channelId);
	
	/**
	 * 查询栏目信息
	 * @param channelId
	 * @return
	 */
	public List<ChannelInfo> findChnlInfos(@Param("channelId")Integer channelId);
	
	/**
	 * 查询热门典藏鉴赏数据
	 * @param channelId
	 * @return
	 */
	public List<DCJSDocumentInfo> findHotDCJSDocuemnt();
	
	/**
	 * 查询收藏典藏鉴赏数据
	 * @param channelId
	 * @return
	 */
	public List<DCJSDocumentInfo> findCollectDCJSDocuemnt(@Param("userName")String userName);
	
	/**
	 * 查询所有收藏典藏鉴赏数据
	 * @param pageSize 
	 * @param curPage 
	 * @param channelId
	 * @return
	 */
	public List<DCJSDocumentInfo> findAllCollectDCJSDocuemnt(@Param("userName")String userName, @Param("start")Integer start, @Param("pageSize")Integer pageSize);
	
	/**
	 * 查询最近访问典藏鉴赏数据
	 * @param channelId
	 * @return
	 */
	public List<DCJSDocumentInfo> findAccessDCJSDocuemnt(@Param("userName")String userName);
	
	/**
	 * 根据栏目ID获取顶级栏目ID
	 * @param channelId
	 * @return
	 */
	public Integer findTopChannelBychannelId(@Param("channelId")Integer channelId);
	
	/**
	 * 根据栏目IDS获取栏目信息
	 * @param chnlIds
	 * @return
	 */
	public List<ChannelInfo> findChannelListByChannelIds(@Param("chnlIds")List<Integer> chnlIds);
	
	/**
	 * 获取栏目数据存放地址
	 * @return
	 */
	@MapKey("FOLDERID")
	public Map<Integer, Map<String, String>> findChnlDataPath();
}
