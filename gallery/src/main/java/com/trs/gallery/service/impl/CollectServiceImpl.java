package com.trs.gallery.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.gallery.entity.custom.AttachmentInfo;
import com.trs.gallery.entity.custom.ChannelInfo;
import com.trs.gallery.entity.custom.CustomField;
import com.trs.gallery.entity.custom.PageInfo;
import com.trs.gallery.entity.custom.PageResult;
import com.trs.gallery.entity.custom.UserCollectInfoBiz;
import com.trs.gallery.entity.generated.UserCollectInfo;
import com.trs.gallery.entity.generated.UserCollectInfoKey;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.mapper.custom.AttachmentDao;
import com.trs.gallery.mapper.custom.DocumentDao;
import com.trs.gallery.mapper.custom.UserCollectDao;
import com.trs.gallery.mapper.generated.UserCollectInfoMapper;
import com.trs.gallery.service.CollectService;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.util.StringUtil;

@Service
public class CollectServiceImpl implements CollectService {

	@Autowired
	private UserCollectDao userCollectDao;
	@Autowired
	private UserCollectInfoMapper userCollectInfoMapper;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private WCMConfig wcmConfig;
	@Autowired
	private DocumentDao documentDao;
	@Autowired
	private DocumentService documentService;

	@Override
	public List<UserCollectInfoBiz> findList(PageInfo pageInfo) {
		List<UserCollectInfoBiz> collectInfoBizs = userCollectDao.findList(pageInfo);
		return collectInfoBizs;
	}

	@Override
	public Integer findListTotalNum(PageInfo pageInfo) {
		return userCollectDao.findListTotalNum(pageInfo);
	}

	@Override
	public PageResult<UserCollectInfoBiz> findListAndTotalNum(PageInfo pageInfo) {
		PageResult<UserCollectInfoBiz> pageResult = new PageResult<>();
		pageResult.setDataList(this.findList(pageInfo));
		pageResult.setTotalNum(this.findListTotalNum(pageInfo));
		return pageResult;
	}

	@Override
	public void addRecord(UserCollectInfo userCollectInfo) {
		userCollectInfoMapper.insertSelective(userCollectInfo);
	}

	@Override
	public void deleteRecord(String userName, Integer docId) {
		UserCollectInfoKey userCollectInfoKey = new UserCollectInfoKey(userName, docId);
		userCollectInfoMapper.deleteByPrimaryKey(userCollectInfoKey);
	}

	/**
	 * 设置扩展字段值
	 * 
	 * @param userCollectInfoBiz
	 */
	private void setCustomFieldVal(List<UserCollectInfoBiz> list) {
		for (UserCollectInfoBiz userCollectInfoBiz : list) {
			List<CustomField> cFields = userCollectInfoBiz.getCustomFields();
			StringBuilder sBuilder = new StringBuilder();
			for (CustomField customField : cFields) {
				sBuilder.append(customField.getName()).append(",");
			}
			if (sBuilder.length() > 0) {
				sBuilder.deleteCharAt(sBuilder.length() - 1);
				Map<String, Object> vaList = userCollectDao.selectCustomFieldValue(sBuilder.toString(),
						userCollectInfoBiz.getDocId());
				for (CustomField customField : cFields) {
					customField.setValue(vaList.get(customField.getName()));
				}
			}
		}
	}

	@Override
	public List<UserCollectInfoBiz> findListBychnlId(String userName, Integer channelId, Integer curPage, Integer pageSize) {
		List<ChannelInfo> channelInfos = documentDao.findChnlInfos(channelId);
		List<Integer> chnlIds = documentService.getChnlIdList(channelInfos);
		chnlIds.add(channelId);
		List<UserCollectInfoBiz> collectInfoBizs = userCollectDao.findListBychnlIds(userName, chnlIds, (curPage-1)*pageSize, pageSize);
		setImageUrl(collectInfoBizs);
		setCustomFieldVal(collectInfoBizs);
		return collectInfoBizs;
	}
	

	@Override
	public boolean isCollect(String userName, Integer docId) {
		Integer count = userCollectDao.findCountByDocId(userName, docId);
		return count > 0 ? true : false;
	}
	
	/**
	 * 查询文档的图片附件，并赋值图片url
	 * 
	 * @param documentInfos
	 */
	private void setImageUrl(List<UserCollectInfoBiz> documentInfos) {
		if (CollectionUtils.isEmpty(documentInfos))
			return;
		String attachmentUrl = "http://" + wcmConfig.getIp() + "/webpic/";
		Integer[] docIds = new Integer[documentInfos.size()];
		for (int i = 0; i < documentInfos.size(); i++) {
			docIds[i] = documentInfos.get(i).getDocId();
		}
		List<AttachmentInfo> attachmentInfos = attachmentDao.findAttachmentListByDocIds(docIds,
				new String[] { "jpg", "png" });
		for (UserCollectInfoBiz dInfo : documentInfos) {
			// 对查询到的文档图片地址进行赋值
			for (int j = 0; j < attachmentInfos.size(); j++) {
				if (attachmentInfos.get(j).getDocId().equals(dInfo.getDocId())) {
					if (StringUtil.isNotEmpty(attachmentInfos.get(j).getAppFile()))
						dInfo.setImgUrl(attachmentUrl + attachmentInfos.get(j).getImgUrl());
				}
			}
		}
	}
}
