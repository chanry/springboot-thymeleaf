package com.trs.gallery.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.trs.gallery.entity.custom.AttachmentInfo;
import com.trs.gallery.entity.custom.ChannelInfo;
import com.trs.gallery.entity.custom.CustomField;
import com.trs.gallery.entity.custom.DCJSDocumentInfo;
import com.trs.gallery.entity.custom.DocumentInfo;
import com.trs.gallery.entity.custom.QueryType;
import com.trs.gallery.internal.component.ChannelDataPath;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.mapper.custom.AttachmentDao;
import com.trs.gallery.mapper.custom.DocumentDao;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.util.StringUtil;

@Service
public class DocumentServiceImpl implements DocumentService {

	private final static Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	private DocumentDao documentDao;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private WCMConfig wcmConfig;

	@Override
	public List<DocumentInfo> findDocumentsBychnldocIds(List<Integer> chnldocIds) {
		List<DocumentInfo> documentInfos = documentDao.findDocumentsBychnldocIds(chnldocIds);
		setImageUrl(documentInfos);
		return documentInfos;
	}

	@Override
	public Map<String, Object> findDocumentsBychnlIds(List<Integer> channelIds) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(channelIds))
			return resultMap;
		Map<Integer, Map<String, String>> channelInfo = documentDao.findChnlInfo(channelIds);
		for (Integer channelId : channelIds) {
			List<ChannelInfo> channelInfos = documentDao.findChnlInfos(channelId);
			List<Integer> chnlIds = getChnlIdList(channelInfos);
			chnlIds.add(channelId);
			List<DocumentInfo> documentInfos = documentDao.findDocumentsBychnlIds(chnlIds);
			setCustomFieldVal(documentInfos);
			setImageUrl(documentInfos);
			resultMap.put(channelInfo.get(channelId).get("CHNLNAME"), documentInfos);
		}
		return resultMap;
	}

	/**
	 * 设置扩展字段值
	 * 
	 * @param userCollectInfoBiz
	 */
	@Override
	public void setCustomFieldVal(List<DocumentInfo> documentInfos) {
		if (CollectionUtils.isEmpty(documentInfos))
			return;
		List<Integer> docIdList = new ArrayList<>();
		// 获取文档列表的ID以及构造以ID为key的列表map
		for (DocumentInfo documentInfo : documentInfos) {
			docIdList.add(documentInfo.getDocId());
		}
		// 取第一条数据的扩展字段信息拼接成查询结果字段sql
		List<CustomField> cFields = documentInfos.get(0).getCustomFields();
		StringBuilder sBuilder = new StringBuilder();
		for (CustomField customField : cFields) {
			sBuilder.append(customField.getName()).append(",");
		}
		if (sBuilder.length() <= 0)
			return;
		sBuilder.deleteCharAt(sBuilder.length() - 1);
		// 传入扩展字段和文档ID列表获取这些文档的扩展字段内容
		List<Map<String, Object>> vaList = documentDao.selectCustomFieldValue(sBuilder.toString(), docIdList);
		// 遍历查询到的扩展字段内容并赋值给对应的文档
		for (int i = 0; i < documentInfos.size(); i++) {
			DocumentInfo documentInfo = new DocumentInfo();
			documentInfo = documentInfos.get(i);
			for (Map<String, Object> map : vaList) {
				if (map.get("docId").toString().equals(String.valueOf(documentInfo.getDocId()))) {
					documentInfo.setCustomFieldsVal(map);
				}
			}
		}
	}

	@Override
	public List<DocumentInfo> findDocumentsByDate(Date date, Integer channelId) {
		List<ChannelInfo> channelInfos = new ArrayList<>();
		// 针对媒体中心栏目只查询资讯、公告、新闻报道这三个二级栏目
		if (wcmConfig.getNewsChnlId().intValue() == channelId.intValue()) {
			String[] idStrings = wcmConfig.getNewsChnlIds().split(",");
			for (String id : idStrings) {
				channelInfos.addAll(documentDao.findChnlInfos(Integer.valueOf(id)));
			}
		} else {
			channelInfos = documentDao.findChnlInfos(channelId);
		}
		List<Integer> chnlIds = getChnlIdList(channelInfos);
		List<DocumentInfo> documentInfos = new ArrayList<DocumentInfo>();
		chnlIds.add(channelId);
		LOG.info("查询的栏目ID为：" + JSON.toJSONString(chnlIds));
		if (wcmConfig.getNewsChnlId().intValue() == channelId.intValue()) {
			documentInfos = documentDao.findNewsByDate(date, chnlIds);
		} else if (wcmConfig.getExbChnlId().intValue() == channelId.intValue()) {
			documentInfos = documentDao.findExhibitionByDate(date, chnlIds);
			setCustomFieldVal(documentInfos);
		} else if (wcmConfig.getActChnlId().intValue() == channelId.intValue()) {
			documentInfos = documentDao.findActivityByDate(date, chnlIds);
			setCustomFieldVal(documentInfos);
		} 
		return documentInfos;
	}
	
	@Override
	public List<Integer> getChnlIdList(List<ChannelInfo> channelInfos) {
		List<Integer> chnlIds = new ArrayList<>();
		for (ChannelInfo channelInfo : channelInfos) {
			chnlIds.add(channelInfo.getChannelId());
			if (!CollectionUtils.isEmpty(channelInfo.getChildren())) {
				chnlIds.addAll(getChnlIdList(channelInfo.getChildren()));
			}
		}
		return chnlIds;
	}

	@Override
	public List<DCJSDocumentInfo> findDCJSDocuments(String userName, Integer type, Integer curPage, Integer pageSize) {
		List<DCJSDocumentInfo> dcjsDocumentInfos = new ArrayList<>();
		if (type == QueryType.HOT.getIndex()) {
			dcjsDocumentInfos = documentDao.findHotDCJSDocuemnt();
		} else if (type == QueryType.COLLECT.getIndex()) {
			dcjsDocumentInfos = documentDao.findCollectDCJSDocuemnt(userName);
		} else if (type == QueryType.ACCESS.getIndex()) {
			dcjsDocumentInfos = documentDao.findAccessDCJSDocuemnt(userName);
		} else {
			dcjsDocumentInfos = documentDao.findAllCollectDCJSDocuemnt(userName, (curPage - 1)*pageSize, pageSize);
		}
		setImageUrlForDCJS(dcjsDocumentInfos);
		return dcjsDocumentInfos;
	}
	
	/**
	 * 查询文档的图片附件，并赋值图片url
	 * 
	 * @param documentInfos
	 */
	@Override
	public void setImageUrl(List<DocumentInfo> documentInfos) {
		if (CollectionUtils.isEmpty(documentInfos))
			return;
		String attachmentUrl = "http://" + wcmConfig.getIp() + "/webpic/";
		Integer[] docIds = new Integer[documentInfos.size()];
		for (int i = 0; i < documentInfos.size(); i++) {
			docIds[i] = documentInfos.get(i).getDocId();
		}
		List<AttachmentInfo> attachmentInfos = attachmentDao.findAttachmentListByDocIds(docIds,
				new String[] { "jpg", "png" });
		for (DocumentInfo dInfo : documentInfos) {
			// 对查询到的文档图片地址进行赋值
			for (int j = 0; j < attachmentInfos.size(); j++) {
				if (attachmentInfos.get(j).getDocId().equals(dInfo.getDocId())) {
					if (StringUtil.isNotEmpty(attachmentInfos.get(j).getAppFile()))
						dInfo.setImgUrl(attachmentUrl + attachmentInfos.get(j).getImgUrl());
				}
			}
		}
	}
	
	/**
	 * 查询文档的图片附件，并赋值图片url
	 * 
	 * @param documentInfos
	 */
	@Override
	public void setImageUrlForDCJS(List<DCJSDocumentInfo> documentInfos) {
		if (CollectionUtils.isEmpty(documentInfos))
			return;
		String attachmentUrl = "http://" + wcmConfig.getImgUrl();

		for (DCJSDocumentInfo dInfo : documentInfos) {

			if (StringUtil.isNotEmpty(dInfo.getImage())) {
				StringBuilder path = new StringBuilder();
				if (ChannelDataPath.getDocPathValue(dInfo.getChannelId()) != null) {
					path.append(ChannelDataPath.getDocPathValue(dInfo.getChannelId()));
				} else {
					this.getChnlAbsolutePath(dInfo.getChannelId(), path);
					ChannelDataPath.setDocPathValue(dInfo.getChannelId(), path.toString());
				}
				String temp = dInfo.getImage().split(",")[0];
				String imgUrl = path + "/" + temp.substring(2, 8) + "/" + temp;
				dInfo.setImgUrl(attachmentUrl + imgUrl);
			}

		}
	}

	@Override
	public Integer findTopChannelBychannelId(Integer channelId) {
		Integer parentId = documentDao.findTopChannelBychannelId(channelId);
		if (parentId != 0) {
			return findTopChannelBychannelId(parentId);
		} else {
			return channelId;
		}
	}

	@Override
	public List<ChannelInfo> findChannelListByChannelIds(List<Integer> chnlIds) {
		return documentDao.findChannelListByChannelIds(chnlIds);
	}

	@Override
	public Integer getChnlAbsolutePath(Integer chnlId, StringBuilder path) {
		path.insert(0, "/" + ChannelDataPath.getValue(chnlId));
		Integer parentId = documentDao.findTopChannelBychannelId(chnlId);
		if (parentId != 0) {
			return getChnlAbsolutePath(parentId, path);
		} else {
			return 0;
		}
	}

}
