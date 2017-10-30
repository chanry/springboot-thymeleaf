package com.trs.gallery.entity.custom;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChannelInfo {
	/**
	 * 栏目ID
	 */
	private Integer channelId;
	
	/**
	 * 父栏目ID
	 */
	@JsonIgnore
	private String parentId;
	
	/**
	 * 栏目名称
	 */
	private String chnlnName;
	
	/**
	 * 子栏目集合
	 */
	@JsonIgnore
	private List<ChannelInfo> children;

	
	
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getChnlnName() {
		return chnlnName;
	}

	public void setChnlnName(String chnlnName) {
		this.chnlnName = chnlnName;
	}

	public List<ChannelInfo> getChildren() {
		return children;
	}

	public void setChildren(List<ChannelInfo> children) {
		this.children = children;
	}
	
}
