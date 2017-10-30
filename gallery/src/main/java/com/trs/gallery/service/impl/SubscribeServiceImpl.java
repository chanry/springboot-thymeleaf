package com.trs.gallery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.gallery.entity.generated.SubscribeInfo;
import com.trs.gallery.entity.generated.SubscribeInfoExample;
import com.trs.gallery.mapper.custom.SubscribeDao;
import com.trs.gallery.mapper.generated.SubscribeInfoMapper;
import com.trs.gallery.service.SubscribeService;

@Service
public class SubscribeServiceImpl implements SubscribeService {
	
	@Autowired
	private SubscribeInfoMapper subscribeInfoMapper;
	@Autowired
	private SubscribeDao subscribeDao;
	
	
	@Override
	public List<SubscribeInfo> findList() {
		return subscribeInfoMapper.selectByExample(new SubscribeInfoExample());
	}

	@Override
	public SubscribeInfo findRecord(SubscribeInfo t) {
		return subscribeInfoMapper.selectByPrimaryKey(t.getChannelId());
	}

	@Override
	public List<SubscribeInfo> findList(SubscribeInfo t) {
		return null;
	}

	@Override
	public void deleteRecord(SubscribeInfo t) {
		subscribeInfoMapper.deleteByPrimaryKey(t.getChannelId());
	}

	@Override
	public void addRecord(SubscribeInfo t) {
		subscribeInfoMapper.insertSelective(t);
	}

}
