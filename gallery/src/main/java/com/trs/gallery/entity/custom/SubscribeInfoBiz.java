package com.trs.gallery.entity.custom;

import com.trs.gallery.entity.generated.SubscribeInfo;

public class SubscribeInfoBiz extends SubscribeInfo {
	
	public SubscribeInfoBiz() {
		
	}
	
	public SubscribeInfoBiz(SubscribeInfo subscribeInfo) {
		super(subscribeInfo.getChannelId(), subscribeInfo.getChannelName(), subscribeInfo.getIconUrl());
	}
	
	private boolean hasSub = false;

	public boolean isHasSub() {
		return hasSub;
	}

	public void setHasSub(boolean hasSub) {
		this.hasSub = hasSub;
	}
	
}
