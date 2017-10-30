package com.trs.gallery.internal.ids.actor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trs.idm.client.actor.ActorException;
import com.trs.idm.client.actor.SSOUser;
import com.trs.idm.client.actor.StdHttpSessionBasedActor;


/**
 * @Title: DOPLoginActor.java
 * @Description: 与IDS进行socket通信，实现登录、退出以及用户同步等
 * @author chenli
 * @date 2016年12月13日 上午10:10:43
 * @version V1.0.0
 */
public class DOPLoginActor extends StdHttpSessionBasedActor {

	private static final Logger LOG = LoggerFactory.getLogger(DOPLoginActor.class);
	/**
	 * ixm已登录用户名。 注：区别于通行证，若登录用户未实名，则另存session保存通行证相关登录状态，
	 * 通过PASSPORT_LOGIN_USER获取
	 */
	public static final String DOP_USER_NAME = "dopLoginUserName";
	/**
	 * ixm已登录用户实体
	 */
	public static final String DOP_USER = "dopLoginUser";
	
	/**
	 * 用户头像
	 */
	public static final String HEAD_IMG_URL = "headUrl";
	
	

	
	@Override
	public boolean addUser(SSOUser ssoUser, HttpServletRequest request) throws ActorException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean disableUser(SSOUser ssoUser) throws ActorException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean enableUser(SSOUser ssoUser) throws ActorException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean removeUser(SSOUser ssoUser, HttpServletRequest request) throws ActorException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUser(SSOUser ssoUser, HttpServletRequest request) throws ActorException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean userExist(SSOUser ssoUser) throws ActorException {
		return ssoUser != null;
	}

	@Override
	public String extractUserName(HttpServletRequest request) throws ActorException {
		String userName = request.getParameter("userName");
		userName = userName != null ? userName : "";
		return userName;
	}

	@Override
	public String extractUserPwd(HttpServletRequest request) throws ActorException {
		String password = request.getParameter("password");
		password = password != null ? password : "";
		return password;
	}

	@Override
	public boolean checkLocalLogin(HttpSession session) throws ActorException {
		String username = (String) session.getAttribute(DOP_USER_NAME);
		return !(username == null || "".equals(username));
	}

	@Override
	public void loadLoginUser(HttpServletRequest request, SSOUser ssoUser) throws ActorException {
		HttpSession session = request.getSession();
		String username = ssoUser.getProperty("idsUName");
		session.setAttribute(DOP_USER_NAME, username);
		session.setAttribute(DOP_USER, ssoUser);
		session.setAttribute(HEAD_IMG_URL, ssoUser.getProperty("headUrl"));
	}

	@Override
	public void logout(HttpSession session) throws ActorException {
		try {
			session.setAttribute(DOP_USER_NAME, null);
			session.setAttribute(DOP_USER, null);
			session.invalidate();
		} catch (Exception e) {
			LOG.error("dop logout, error:", e);
		}
	}

}
