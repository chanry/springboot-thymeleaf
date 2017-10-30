package com.trs.gallery.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.trs.gallery.entity.custom.Message;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.internal.exception.DOPException;
import com.trs.gallery.internal.ids.util.IDSAPICall;
import com.trs.gallery.service.UserManagerService;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;

@Service
public class UserManagerServiceImpl implements UserManagerService {
	
	private final static Logger LOG = LoggerFactory.getLogger(UserManagerServiceImpl.class);

	@Autowired
	private WCMConfig wcmConfig;
	@Value("${web.upload-path}")
	private String webUploadPath;
	
	@Override
	public List<Message> addVolunteer(HttpServletRequest request) throws DOPException {
		List<Message> messages = new ArrayList<>();
		String userName = UserUtil.getLoginUserName(request);	
		String name = request.getParameter("name");				
		String age = request.getParameter("age");				
		String sex = request.getParameter("sex");				
		String maritalStatus = request.getParameter("maritalStatus");
		String politicalStatus = request.getParameter("politicalStatus");
		String professionalType = request.getParameter("professionalType");
		String professionalStatus = request.getParameter("professionalStatus");
		String company = request.getParameter("company");
		String school = request.getParameter("school");
		String major = request.getParameter("major");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String favorite = request.getParameter("favorite");
		String speciality = request.getParameter("speciality");
		// 参数校验
		paramValidate("name", name, "姓名", messages);
		paramValidate("age", age, "年龄", messages);
		paramValidate("sex", sex, "性别", messages);
		paramValidate("major", major, "所学专业", messages);
		numberValidate("age", age, "年龄", messages);
		paramValidate("professionalStatus", professionalStatus, "职业状态", messages);
		dateStrValidate("startTime", startTime, "开始时间", messages);
		dateStrValidate("endTime", endTime, "结束时间", messages);
		if (messages.size() > 0) {
			return messages;
		}
		
		String wcmUrl = wcmConfig.getInfogatePath();
		PostMethod postMethod = new PostMethod(wcmUrl);
		postMethod.addParameter("DocumentId", "0");
		postMethod.addParameter("SiteId", wcmConfig.getSiteId());
		postMethod.addParameter("ChannelId", wcmConfig.getZyzChnlId());
		postMethod.addParameter("InfoViewId", wcmConfig.getZyzFormId());
		postMethod.addParameter("encoding", "UTF-8");
		postMethod.addParameter("verifycode", "0");
		postMethod.addParameter("needverifycode", "0");
		postMethod.addParameter("infogatePath", wcmUrl);
		// 志愿者信息
		postMethod.addParameter("userName", userName);
		postMethod.addParameter("name", name);
		postMethod.addParameter("age", age);
		postMethod.addParameter("sex", sex);
		postMethod.addParameter("maritalStatus", maritalStatus);
		postMethod.addParameter("politicalStatus", politicalStatus);
		postMethod.addParameter("professionalType", professionalType);
		postMethod.addParameter("company", company);
		postMethod.addParameter("school", school);
		postMethod.addParameter("major", major);
		postMethod.addParameter("startTime", startTime);
		postMethod.addParameter("endTime", endTime);
		postMethod.addParameter("favorite", favorite);
		postMethod.addParameter("speciality", speciality);
		
		boolean isSuc = false;
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
		client.getHttpConnectionManager().getParams().setSoTimeout(60000);
		try {
			client.executeMethod(postMethod);
			String responseText = postMethod.getResponseBodyAsString();
			isSuc = responseText.contains("提交表单数据成功");
			if (!isSuc) {
				LOG.info("表单提交失败 ，SiteId:{},ChannelId:{},InfoViewId:{}, responseText:{}", postMethod.getParameter("SiteId"),
						postMethod.getParameter("ChannelId"), postMethod.getParameter("InfoViewId"), responseText);
				throw new DOPException("表单提交失败");
			}
			LOG.debug(postMethod.getResponseBodyAsString());	
			return messages;
		} catch (ConnectTimeoutException ce) {
			LOG.error("请求超时", ce);
			throw new DOPException("请求超时");
		} catch (SocketTimeoutException se) {
			LOG.error("响应超时", se);
			throw new DOPException("响应超时");
		} catch (IOException ie) {
			LOG.error("IO异常", ie);
			throw new DOPException("IO异常");
		}
	}
	
	private void paramValidate(String key, String value, String desc, List<Message> messages) {
		if (StringUtil.isEmpty(value)) {
			Message message = new Message(key, desc + "不能为空");
			messages.add(message);
		}
	}
	
	private void dateStrValidate(String key, String value, String desc, List<Message> messages) {
		Pattern pattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			Message message = new Message(key, desc+"格式不对,参考：2008-8-20");
			messages.add(message);
		}
	}
	
	private void numberValidate(String key, String str, String desc, List<Message> messages){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
		   Message message = new Message(key, desc+"格式不对，参考：20");
		   messages.add(message);
	   } 
	}

	@Override
	public JSONObject getUserInfo(String userName) throws DOPException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userName", userName);
		try {
			String responseText = IDSAPICall.Call2Json("userQueryForManage", paramMap);
			JSONObject user = JSONObject.parseObject(responseText);
			return user.getJSONObject("entry");
		} catch (Exception e) {
			LOG.error("调用IDS查询用户信息接口异常", e);
			throw new DOPException("调用IDS查询用户信息接口失败");
		}
	}

	@Override
	public JSONObject updateUser(Map<String, String> param) throws DOPException {
		param.put("manageServiceTag", "managerUpdateUser");
		try {
			String responseJson = IDSAPICall.Call2Json("userManageService", param);
			LOG.info("update user interface return:" + responseJson);
			JSONObject jsonObject = JSONObject.parseObject(responseJson);
			return jsonObject;
		} catch (Exception e) {
			LOG.error("use update user interface error", e);
			throw new DOPException("调用IDS更新用户信息接口失败");
		}
		
	}

	@Override
	public String getUploadImgPath(String userName) {
		String temp = "images" + File.separator + "upload" + File.separator;
		// 数据库保存的目录
		String dataDirectory = temp.concat(String.valueOf(userName)).concat(File.separator);
		// 文件路径
		String filePath = webUploadPath.concat(dataDirectory);
		return filePath;
	}
}
