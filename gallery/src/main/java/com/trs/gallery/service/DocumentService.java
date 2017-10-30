package com.trs.gallery.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trs.gallery.entity.custom.ChannelInfo;
import com.trs.gallery.entity.custom.DCJSDocumentInfo;
import com.trs.gallery.entity.custom.DocumentInfo;

public interface DocumentService {
	/**
	 * 根据文档关联ID查询文档列表
	 * @param docIds
	 * @return
	 */
	public List<DocumentInfo> findDocumentsBychnldocIds(List<Integer> docIds);
	
	/**
	 * 根据栏目ID查询文档列表
	 * @param docIds
	 * @return
	 */
	public Map<String, Object> findDocumentsBychnlIds(List<Integer> channelIds);
	
	/**
	 * 根据日期和栏目ID查询文档列表
	 * @param pubDate
	 * @return
	 */
	public List<DocumentInfo> findDocumentsByDate(Date date, Integer channelId);
	
	/**
	 * 查询典藏鉴赏数据
	 * @param channelId
	 * @return
	 */
	public List<DCJSDocumentInfo> findDCJSDocuments(String userName, Integer type, Integer curPage, Integer pageSize);
	
	/**
	 * 根据栏目ID获取顶级栏目ID
	 * @param channelId
	 * @return
	 */
	public Integer findTopChannelBychannelId(Integer channelId);
	
	/**
	 * 根据栏目IDS获取栏目信息
	 * @param chnlIds
	 * @return
	 */
	public List<ChannelInfo> findChannelListByChannelIds(List<Integer> chnlIds);
	
	/**
	 * 设置栏目存放地址的路径
	 * @param chnlId
	 * @param path
	 * @return
	 */
	public Integer getChnlAbsolutePath(Integer chnlId, StringBuilder path);
	
	/**
	 * 设置普通文档的图片地址
	 * @param documentInfos
	 */
	public void setImageUrl(List<DocumentInfo> documentInfos);
	
	/**
	 * 设置典藏鉴赏文档的图片地址
	 * @param documentInfos
	 */
	public void setImageUrlForDCJS(List<DCJSDocumentInfo> documentInfos);
	
	/**
	 * 设置扩展字段
	 * @param documentInfos
	 */
	public void setCustomFieldVal(List<DocumentInfo> documentInfos);
	
	/**
	 * 获取栏目ID列表
	 * @param channelInfos
	 * @return
	 */
	public List<Integer> getChnlIdList(List<ChannelInfo> channelInfos);
}
