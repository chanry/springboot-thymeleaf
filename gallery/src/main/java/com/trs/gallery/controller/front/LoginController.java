package com.trs.gallery.controller.front;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.gallery.internal.component.DOPConfig;
import com.trs.gallery.util.ActionResultUtil;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;
import com.trs.idm.client.actor.SSOUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description 
 * @author guojiahua
 * @version 1.0.0
 * @creatime 2017年1月18日上午10:40:27
 */
@Api(value = "Swagger动态文档demo", description = "this is desc", position = 100, protocols = "http")
@Controller
@RequestMapping(value = "/front/login")
public class LoginController {

	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);
	
	
	@Autowired
	private DOPConfig dopConfig;
	
	@ApiOperation(value = "获取当前登录用户信息", httpMethod = "GET", response = Object.class)
	@ResponseBody	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public Object showLoginInfo(HttpServletRequest request, HttpServletResponse response){
		try {
			SSOUser ssoUser = UserUtil.getLoginUser(request);
			if(ssoUser != null){
				String userName = ssoUser.getUserName();
				String nickName = ssoUser.getNickName();
				if (StringUtil.isEmpty(nickName)) {
					nickName = userName;
				}
				String headImgUrl = UserUtil.getLoginUserHeadImg(request);
				Map<String, String> userMap = new Hashtable<String, String>();
				userMap.put("userName", userName);
				userMap.put("headImgUrl", headImgUrl);
				userMap.put("nickName", nickName);
				userMap.put("logoutUrl", dopConfig.getLogoutUrl());
				return ActionResultUtil.result(true, "isLogined!", userMap);
			}
			return ActionResultUtil.result(false, "noLogin!", "");
		} catch (Exception e) {
			LOG.error("get ids login info fail! Exception:" + e);
			return ActionResultUtil.result(false, "noLogin!", "");
		}
		
	}

}
