package com.trs.gallery.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.trs.gallery.entity.custom.Message;
import com.trs.gallery.internal.exception.DOPException;

public interface UserManagerService {
	/**
	 * 添加志愿者
	 * @param request
	 * @throws DOPException 
	 */
	public List<Message> addVolunteer(HttpServletRequest request) throws DOPException;
	
	/**
	 * 调用IDS接口查询用户信息
	 * @param userName
	 * @return
	 * @throws DOPException 
	 */
	public JSONObject getUserInfo(String userName) throws DOPException;
	
	/**
	 * 更新用户信息
	 * @param param
	 * @return
	 * @throws DOPException
	 */
	public JSONObject updateUser(Map<String, String> param) throws DOPException;
	
	/**
	 * 获取用户头像相对路径
	 * @param userName
	 * @return
	 */
	public String getUploadImgPath(String userName);
	
}
