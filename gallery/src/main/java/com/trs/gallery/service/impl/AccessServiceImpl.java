package com.trs.gallery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.gallery.entity.custom.ChannelInfo;
import com.trs.gallery.entity.custom.DocumentInfo;
import com.trs.gallery.entity.generated.UserAccessInfo;
import com.trs.gallery.entity.generated.UserAccessInfoExample;
import com.trs.gallery.mapper.custom.DocumentDao;
import com.trs.gallery.mapper.custom.UserAccessDao;
import com.trs.gallery.mapper.generated.UserAccessInfoMapper;
import com.trs.gallery.service.AccessService;
import com.trs.gallery.service.DocumentService;

@Service
public class AccessServiceImpl implements AccessService {
	
	@Autowired
	private UserAccessInfoMapper userAccessInfoMapper;
	@Autowired
	private UserAccessDao userAccessDao;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private DocumentDao documentDao;
	
	@Override
	public List<UserAccessInfo> findList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserAccessInfo findRecord(UserAccessInfo t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserAccessInfo> findList(UserAccessInfo t) {
		UserAccessInfoExample example = new UserAccessInfoExample();
		example.createCriteria().andUserNameEqualTo(t.getUserName());
		return userAccessInfoMapper.selectByExample(example);
	}

	@Override
	public void deleteRecord(UserAccessInfo t) {}

	@Override
	public void addRecord(UserAccessInfo t) {
		userAccessInfoMapper.insertSelective(t);
	}

	@Override
	public List<DocumentInfo> findLatelyAccessList(String userName, Integer channelId) {
		List<ChannelInfo> channelInfos = documentDao.findChnlInfos(channelId);
		List<Integer> chnlIds = documentService.getChnlIdList(channelInfos);
		List<DocumentInfo> documentInfos = userAccessDao.findLatelyAccessList(userName, chnlIds);
		documentService.setImageUrl(documentInfos);
		documentService.setCustomFieldVal(documentInfos);
		return documentInfos;
	}

}
