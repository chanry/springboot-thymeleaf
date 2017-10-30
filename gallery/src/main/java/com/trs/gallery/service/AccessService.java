package com.trs.gallery.service;

import java.util.List;

import com.trs.gallery.entity.custom.DocumentInfo;
import com.trs.gallery.entity.generated.UserAccessInfo;

public interface AccessService extends BaseService<UserAccessInfo> {
	/**
	 * 查询最近浏览数据
	 * @param pageInfo
	 * @return
	 */
	public List<DocumentInfo> findLatelyAccessList(String userName, Integer channelId);
}
