package com.trs.gallery.entity.custom;

/**
 * @className AppointmentInfo.java
 * @author chenli
 * @date 2017年5月4日 上午10:59:39
 */
public class AppointmentInfo {
	/**
	 * 文档ID
	 */
	private Integer docId;
	
	/**
	 * 真实姓名
	 */
	private String realname;
	
	/**
	 * 身份证号
	 */
	private String IDCardNum;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 预约票数
	 */
	private Integer ticketNum;
	
	/**
	 * 预约日期
	 */
	private String apptDate;
	
	

	

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getIDCardNum() {
		return IDCardNum;
	}

	public void setIDCardNum(String iDCardNum) {
		IDCardNum = iDCardNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(Integer ticketNum) {
		this.ticketNum = ticketNum;
	}

	public String getApptDate() {
		return apptDate;
	}

	public void setApptDate(String apptDate) {
		this.apptDate = apptDate;
	}
	
}
