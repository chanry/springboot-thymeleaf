package com.trs.gallery.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.trs.gallery.entity.custom.AppointmentInfo;
import com.trs.gallery.internal.exception.DOPException;

public interface AppointmentService extends PageListService<AppointmentInfo>{
	/**
	 * 添加预约
	 * @param userName
	 * @param map
	 * @return
	 * @throws DOPException 
	 */
	public void addRecord(String userName, Map<String, String[]> map) throws DOPException;
	
	/**
	 * 删除预约
	 * @param userName
	 * @param appDate
	 * @return
	 */
	public void deleteRecord(Integer docId, String userName);
	
	/**
	 * 查询某一天已经预约票数
	 * @param apptDate
	 * @return
	 */
	public Integer selectTicketNumByDate(@Param("apptDate")String apptDate);
}
