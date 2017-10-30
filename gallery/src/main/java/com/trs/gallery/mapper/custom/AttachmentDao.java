package com.trs.gallery.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trs.gallery.entity.custom.AttachmentInfo;

public interface AttachmentDao {
	
	/**
	 * 根据文档ID查询文档细览地址
	 * @param docId
	 * @return
	 */
	public String findDocDetailUrl(@Param("docId")Integer docId);
	
	/**
	 * 根据文档ID和附件类型查询该文档附件列表信息
	 * @param docId
	 * @param fileExt
	 * @return
	 */
	public List<AttachmentInfo> findAttachmentList(@Param("docId")Integer docId, @Param("fileExts")String[] fileExts);
	
	/**
	 * 根据文档ID数组和附件类型查询出每个文档对应类型的第一个附件信息
	 * @param docIds
	 * @param fileExt
	 * @return
	 */
	public List<AttachmentInfo> findAttachmentListByDocIds(@Param("docIds")Integer[] docId, @Param("fileExts")String[] fileExts);
}
