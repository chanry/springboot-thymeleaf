package com.trs.gallery.mapper.custom;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trs.gallery.entity.custom.DocumentInfo;

public interface UserAccessDao {
	/**
	 * 查询最近浏览数据
	 * @param userAccessInfo
	 * @return
	 */
	public List<DocumentInfo> findLatelyAccessList(@Param("userName")String userName, @Param("chnlIds")List<Integer> chnlIds);
	
}
