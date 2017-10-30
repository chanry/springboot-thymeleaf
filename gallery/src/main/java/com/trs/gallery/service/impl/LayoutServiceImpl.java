package com.trs.gallery.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.gallery.entity.generated.LayoutInfo;
import com.trs.gallery.entity.generated.LayoutInfoExample;
import com.trs.gallery.entity.generated.UserLayoutInfoExample;
import com.trs.gallery.entity.generated.UserLayoutInfoKey;
import com.trs.gallery.mapper.generated.LayoutInfoMapper;
import com.trs.gallery.mapper.generated.UserLayoutInfoMapper;
import com.trs.gallery.service.LayoutService;

@Service
public class LayoutServiceImpl implements LayoutService {
	@Autowired
	private UserLayoutInfoMapper userLayoutInfoMapper;
	@Autowired
	private LayoutInfoMapper layoutInfoMapper;
	
	@Override
	public List<UserLayoutInfoKey> findList() {
		return null;
	}

	@Override
	public UserLayoutInfoKey findRecord(UserLayoutInfoKey t) {
		return null;
	}

	@Override
	public List<UserLayoutInfoKey> findList(UserLayoutInfoKey t) {
		UserLayoutInfoExample example = new UserLayoutInfoExample();
		example.createCriteria().andUserNameEqualTo(t.getUserName());
		return userLayoutInfoMapper.selectByExample(example);
	}

	@Override
	public void deleteRecord(UserLayoutInfoKey t) {
		userLayoutInfoMapper.deleteByPrimaryKey(t);
	}

	@Override
	public void addRecord(UserLayoutInfoKey t) {
		userLayoutInfoMapper.insertSelective(t);
	}

	@Override
	public List<LayoutInfo> findLayoutList() {
		return layoutInfoMapper.selectByExample(new LayoutInfoExample());
	}

}
