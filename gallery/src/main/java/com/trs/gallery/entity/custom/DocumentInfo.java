package com.trs.gallery.entity.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DocumentInfo {
	/**
	 * 文档ID
	 */
	private Integer docId;

	/**
	 * 栏目ID
	 */
	private Integer channelId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 发布时间
	 */
	private Date pubTime;
	
	/**
	 * 撰写时间
	 */
	private Date docRelTime;

	/**
	 * 若有图片，则该字段为第一张图片地址
	 */
	private String imgUrl;

	/**
	 * 文档内容
	 */
	private String content;

	/**
	 * 发布地址
	 */
	private String pubUrl;

	/**
	 * 扩展字段
	 */
	private List<CustomField> customFields;
	

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	
	public Date getDocRelTime() {
		return docRelTime;
	}

	public void setDocRelTime(Date docRelTime) {
		this.docRelTime = docRelTime;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPubUrl() {
		return pubUrl;
	}

	public void setPubUrl(String pubUrl) {
		this.pubUrl = pubUrl;
	}

	public List<CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}
	
	public void setCustomFieldsVal(Map<String, Object> map) {
		for (CustomField customField : this.customFields) {
			customField.setValue(map.get(customField.getName()));
		}
	}

}
