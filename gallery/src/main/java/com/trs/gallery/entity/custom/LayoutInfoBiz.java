package com.trs.gallery.entity.custom;

import com.trs.gallery.entity.generated.LayoutInfo;

public class LayoutInfoBiz extends LayoutInfo {
	
	public LayoutInfoBiz() {
		
	}
	
	public LayoutInfoBiz(LayoutInfo layoutInfo) {
		super(layoutInfo.getChannelId(), layoutInfo.getChannelName(), layoutInfo.getIconUrl());
	}

	private boolean hasCheck = false;

	public boolean isHasCheck() {
		return hasCheck;
	}

	public void setHasCheck(boolean hasCheck) {
		this.hasCheck = hasCheck;
	}
	
}
