package com.trs.gallery.util;

import javax.servlet.http.HttpServletRequest;

import com.trs.idm.client.actor.SSOUser;
import com.trs.gallery.internal.ids.actor.DOPLoginActor;

public class UserUtil {
	
	public static String getLoginUserName(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(DOPLoginActor.DOP_USER_NAME);
	}
	
	public static SSOUser getLoginUser(HttpServletRequest request) {
		return (SSOUser) request.getSession().getAttribute(DOPLoginActor.DOP_USER);
	}
	
	public static String getLoginUserHeadImg(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(DOPLoginActor.HEAD_IMG_URL);
	}
}
