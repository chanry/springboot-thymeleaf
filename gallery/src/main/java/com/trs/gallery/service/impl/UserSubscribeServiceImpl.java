package com.trs.gallery.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.gallery.entity.generated.UserSubscribeInfoExample;
import com.trs.gallery.entity.generated.UserSubscribeInfoKey;
import com.trs.gallery.mapper.custom.SubscribeDao;
import com.trs.gallery.mapper.generated.UserSubscribeInfoMapper;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.service.UserSubscribeService;

@Service
public class UserSubscribeServiceImpl implements UserSubscribeService {
	@Autowired
	private UserSubscribeInfoMapper userSubscribeInfoMapper;
	@Autowired
	private SubscribeDao subscribeDao;
	@Autowired
	private DocumentService documentService;
	
	@Override
	public List<UserSubscribeInfoKey> findList() {
		return userSubscribeInfoMapper.selectByExample(new UserSubscribeInfoExample());
	}

	@Override
	public UserSubscribeInfoKey findRecord(UserSubscribeInfoKey t) {
		return null;
	}
	
	/**
	 * 通过用户名查询该用户订阅的栏目
	 */
	@Override
	public List<UserSubscribeInfoKey> findList(UserSubscribeInfoKey t) {
		UserSubscribeInfoExample sample = new UserSubscribeInfoExample();
		sample.createCriteria().andUserNameEqualTo(t.getUserName());
		return userSubscribeInfoMapper.selectByExample(sample);
	}

	@Override
	public void deleteRecord(UserSubscribeInfoKey t) {
		userSubscribeInfoMapper.deleteByPrimaryKey(t);
	}

	@Override
	public void addRecord(UserSubscribeInfoKey t) {
		userSubscribeInfoMapper.insertSelective(t);
	}

	@Override
	public void addRecords(String userName, List<String> channelIds) {
		subscribeDao.addRecords(userName, channelIds);
	}

	@Override
	public List<String> findList(Integer channelId) {
		List<String> uStrings = new ArrayList<>();
		Integer parentChnlId = documentService.findTopChannelBychannelId(channelId);
		uStrings = subscribeDao.selectSubscribeUser(parentChnlId);
		return uStrings;
	}

}
