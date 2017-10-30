package com.trs.gallery.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trs.gallery.entity.custom.AttachmentInfo;
import com.trs.gallery.mapper.custom.AttachmentDao;
import com.trs.gallery.service.AttachmentService;

@Service
public class AttachmentServiceImpl implements AttachmentService {
	
	private AttachmentDao attachmentDao;
	
	@Override
	public String findDocDetailUrl(Integer docId) {
		return attachmentDao.findDocDetailUrl(docId);
	}

	@Override
	public List<AttachmentInfo> findAttachmentList(Integer docId, String[] fileExts) {
		return attachmentDao.findAttachmentList(docId, fileExts);
	}

	@Override
	public List<AttachmentInfo> findAttachmentListByDocIds(Integer[] docId, String[] fileExts) {
		return attachmentDao.findAttachmentListByDocIds(docId, fileExts);
	}

}
