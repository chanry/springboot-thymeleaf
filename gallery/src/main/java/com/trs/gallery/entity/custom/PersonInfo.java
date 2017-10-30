package com.trs.gallery.entity.custom;

public class PersonInfo {
	/**
	 * 电子邮箱
	 */
	private String email;
	
	/**
	 * 真实姓名
	 */
	private String nickName;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 身份证号
	 */
	private String IDCard;
	
	/**
	 * 头像
	 */
	private String headImgUrl;
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
}
