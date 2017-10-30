package com.trs.gallery.service;

import java.util.List;

import com.trs.gallery.entity.generated.LayoutInfo;
import com.trs.gallery.entity.generated.UserLayoutInfoKey;

public interface LayoutService extends BaseService<UserLayoutInfoKey> {
	/**
	 * 获取所有可页面定制的栏目
	 * @return
	 */
	public List<LayoutInfo> findLayoutList();
	
}
