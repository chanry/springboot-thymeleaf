package com.trs.gallery.service;

import java.util.List;

import com.trs.gallery.entity.custom.AttachmentInfo;

/**
 * wcm附件信息查询接口
 * @author chenli
 *
 */
public interface AttachmentService {
	
	/**
	 * 根据文档ID查询文档细览地址
	 * @param docId
	 * @return
	 */
	public String findDocDetailUrl(Integer docId);
	
	/**
	 * 根据文档ID和附件类型查询该文档附件列表信息
	 * @param docId
	 * @param fileExts
	 * @return
	 */
	public List<AttachmentInfo> findAttachmentList(Integer docId, String[] fileExts); 
	
	/**
	 * 根据文档ID数组和附件类型查询出每个文档对应类型的第一个附件信息
	 * @param docIds
	 * @param fileExts
	 * @return
	 */
	public List<AttachmentInfo> findAttachmentListByDocIds(Integer[] docId, String[] fileExts);
	
}
