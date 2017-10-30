package com.trs.gallery.mapper.custom;

import org.apache.ibatis.annotations.Param;

import com.trs.gallery.entity.custom.AppointmentInfo;


/**
 * @className AppointmentDao.java
 * @author chenli
 * @date 2017年5月4日 上午11:57:01
 */
public interface AppointmentDao extends PageListDao<AppointmentInfo> {
	/**
	 * 删除document表中的文档记录
	 * @param docId
	 */
	public void deleteDocument(@Param("docId")Integer docId);
	
	/**
	 * 删除预约表中的表单记录
	 * @param docId
	 * @param id 表单ID
	 */
	public void deleteAppointment(@Param("docId")Integer docId, @Param("id")Integer id, @Param("userName") String userName);
	
	/**
	 * 删除文档与栏目的关联记录
	 * @param docId
	 */
	public void deleteChnlDoc(@Param("docId")Integer docId);
	
	/**
	 * 查询某一账号某一天已经预约的票数
	 * @param userName
	 * @param apptDate
	 * @return
	 */
	public Integer selectTicketNum(@Param("userName")String userName, @Param("apptDate")String apptDate, @Param("id") Integer id);
	
	/**
	 * 查询某一天已经预约票数
	 * @param apptDate
	 * @return
	 */
	public Integer selectTicketNumByDate(@Param("apptDate")String apptDate, @Param("id") Integer id);
}