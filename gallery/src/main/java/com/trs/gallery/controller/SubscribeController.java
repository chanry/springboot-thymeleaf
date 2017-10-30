package com.trs.gallery.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.trs.gallery.entity.custom.DocumentInfo;
import com.trs.gallery.entity.custom.SubscribeInfoBiz;
import com.trs.gallery.entity.generated.SubscribeInfo;
import com.trs.gallery.entity.generated.UserSubscribeInfoKey;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.internal.ids.util.IDSAPICall;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.service.SubscribeService;
import com.trs.gallery.service.UserSubscribeService;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;

@Controller
@RequestMapping(value = "/sub")
public class SubscribeController extends BaseController {

	private final static Logger LOG = LoggerFactory.getLogger(SubscribeController.class);
	
	@Value("${server.context-path}")
	private String context;
	@Autowired
	private WCMConfig wcmConfig;
	@Autowired
	private SubscribeService subscribeService;
	@Autowired
	private UserSubscribeService userSubscribeService;
	@Autowired
	private DocumentService documentService;
	
	@RequestMapping
	public String index(HttpServletRequest request, Model model) {
		List<SubscribeInfo> subscribeInfos = new ArrayList<>();
		try {
			subscribeInfos = subscribeService.findList();
		} catch (Exception e) {
			LOG.error("查询可订阅栏目异常:", e);
		}
	    model.addAttribute("list", subscribeInfos);
	    return "subscribe/dingYue";
	}
	
	/**
	 * 获取订阅数据
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSubs")
	public Object getMySub(HttpServletRequest request) {
		try {
			// 所有可订阅栏目数据
			List<SubscribeInfo> subscribeInfos = subscribeService.findList();
			
			String userName = UserUtil.getLoginUserName(request);
			UserSubscribeInfoKey key = new UserSubscribeInfoKey(userName, null);
			// 用户已订阅栏目数据
			List<UserSubscribeInfoKey> uInfoKeys = userSubscribeService.findList(key);
			// 所有可订阅栏目数据并且标注用户是否已订阅
			List<SubscribeInfoBiz> subscribeInfoBizs = new ArrayList<>();
			for (SubscribeInfo subscribeInfo : subscribeInfos) {
				subscribeInfo.setIconUrl(context + subscribeInfo.getIconUrl());
				SubscribeInfoBiz subscribeInfoBiz = new SubscribeInfoBiz(subscribeInfo);
				if (CollectionUtils.isNotEmpty(uInfoKeys)) {
					for (UserSubscribeInfoKey subscribeInfoKey : uInfoKeys) {
						if (subscribeInfo.getChannelId().equals(subscribeInfoKey.getChannelId())) {
							subscribeInfoBiz.setHasSub(true);
						}
					}
				}
				subscribeInfoBizs.add(subscribeInfoBiz);
			}
			return success(subscribeInfoBizs);
		} catch (Exception e) {
			LOG.error("查询用户订阅异常:", e);
			return failed();
		}
	}
	
	/**
	 * 接收wcm推送过来的文档并推送给订阅者
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/receive")
	public Object receive(HttpServletRequest request) throws Exception {
		//if (!request.getRemoteAddr().equals(wcmConfig.getIp()))
			//return wcmConfig.getJsonpParam() + "('非法操作')";
		String chnldocIds = request.getParameter("chnldocIds");
		String channelId = request.getParameter("channelId");
		LOG.info("chnldocIds=" + chnldocIds + ",channelId=" + channelId);

		String[] docArr = StringUtil.split(chnldocIds, ",");
		List<Integer> chnldocIdList = new ArrayList<>();
		for (String docId : docArr) {
			chnldocIdList.add(Integer.valueOf(docId));
		}
		// 根据推送过来的文档关联ID查询出文档具体信息
		List<DocumentInfo> documentInfos = documentService.findDocumentsBychnldocIds(chnldocIdList);
		// 根据推送过来的栏目ID查询出订阅了该栏目的用户名列表
		List<String> userNames = userSubscribeService.findList(Integer.valueOf(channelId));
		// 调用IDS发送邮件接口，将该这些文档数据分别发送给订阅者
		String result = "推送订阅成功";
		String msg = buildHtmlMessage(documentInfos);
		for (String userName : userNames) {
			Map<String, String> params = new HashMap<>();
			params.put("attributeName", "userName");
			params.put("attributeValue", userName);
			params.put("documents", msg);
			params.put("templateName", "subscribe");
			LOG.info("send msg to user,userName="+userName);
			String responseJson = IDSAPICall.Call2Json("sendMessage", params);
			LOG.info("接口调用返回结果:"+responseJson);
			JSONObject jsonObject = JSONObject.parseObject(responseJson);
			if (null == jsonObject || jsonObject.getIntValue("code") != 0) {
				result = "推送订阅失败";
			}
		}
		return wcmConfig.getJsonpParam() + "('"+result+"')";
	}
	
	private String buildHtmlMessage(List<DocumentInfo> documentInfos) {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < documentInfos.size(); i++) {
			DocumentInfo dInfo = documentInfos.get(i);
			sBuilder.append("<p>")
				.append("<img style='width: 200px; height: 180px; vertical-align: bottom;' src='"+dInfo.getImgUrl()+"'/>")
				.append("<span style='margin-right: 8px;'>"). append(dInfo.getTitle()).append(":</span>")
				.append("<a href='"+dInfo.getPubUrl()+"'>").append("查看文档详情").append("</a>")
			.append("</p>");
		}
		return sBuilder.toString();
	}

	/**
	 * 用户对订阅的栏目进行保存
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(HttpServletRequest request, String channelId) {
		try {
			String userName = UserUtil.getLoginUserName(request);
			if (StringUtil.isEmpty(userName))
				return failed("用户未登录");
			UserSubscribeInfoKey key = new UserSubscribeInfoKey();
			key.setUserName(userName);
			key.setChannelId(Integer.valueOf(channelId));
			userSubscribeService.addRecord(key);
			return success();
		} catch (Exception e) {
			LOG.error("保存用户订阅异常:", e);
			return failed();
		}
	}
	
	/**
	 * 取消栏目订阅
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(HttpServletRequest request, String channelId) {
		try {
			String userName = UserUtil.getLoginUserName(request);
			if (StringUtil.isEmpty(userName))
				return failed("用户未登录");
			UserSubscribeInfoKey key = new UserSubscribeInfoKey();
			key.setUserName(userName);
			key.setChannelId(Integer.valueOf(channelId));
			userSubscribeService.deleteRecord(key);
			return success();
		} catch (Exception e) {
			LOG.error("保存用户订阅异常:", e);
			return failed();
		}
	}
	
}
