package com.trs.gallery.entity.custom;

import java.util.Date;
import java.util.List;

import com.trs.gallery.entity.generated.UserCollectInfo;

public class UserCollectInfoBiz extends UserCollectInfo  {
	/**
	 * 文档标题
	 */
	private String title;
	
	/**
	 * 文档内容
	 */
	private String content;
	
	/**
	 * 图片地址
	 */
	private String imgUrl = "/images/2016cjy-sscont02.jpg";
	
	/**
	 * 文档地址
	 */
	private String pubUrl;
	
	/**
	 * 扩展字段
	 */
	private List<CustomField> customFields;
	

	public UserCollectInfoBiz(String userName, Integer docId, Date collectTime) {
        super(userName, docId, collectTime);
    }
	

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public List<CustomField> getCustomFields() {
		return customFields;
	}


	public void setCustomFields(List<CustomField> customFields) {
		this.customFields = customFields;
	}


	public String getPubUrl() {
		return pubUrl;
	}


	public void setPubUrl(String pubUrl) {
		this.pubUrl = pubUrl;
	}
	
}
