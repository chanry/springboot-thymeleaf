package com.trs.gallery.entity.custom;

public class SampleAppointmentInfo {
	/**
	 * 预约票数
	 */
	private Integer ticketNum;
	
	/**
	 * 预约日期
	 */
	private String apptDate;
	
	/**
	 * 日期字符串
	 */
	private String dateStr;
	
	/**
	 * 闭开馆状态
	 */
	private String status = "开馆";
	

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
}
