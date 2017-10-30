package com.trs.gallery.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.trs.gallery.entity.custom.Message;
import com.trs.gallery.entity.custom.PersonInfo;
import com.trs.gallery.internal.exception.DOPException;
import com.trs.gallery.internal.ids.actor.DOPLoginActor;
import com.trs.gallery.internal.ids.util.IDSAPICall;
import com.trs.gallery.service.UserManagerService;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;

@Controller
@RequestMapping(value = "/setting")
public class SettingController extends BaseController {

	private final static Logger LOG = LoggerFactory.getLogger(SettingController.class);

	@Autowired
	private UserManagerService userManagerService;

	@RequestMapping
	public String index(HttpServletRequest request, Model model) {
		return "setting/setting";
	}

	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
	public Object getUserInfo(HttpServletRequest request) {
		String userName = UserUtil.getLoginUserName(request);
		JSONObject user;
		try {
			user = userManagerService.getUserInfo(userName);
		} catch (DOPException e) {
			return failed(e.getMessage());
		}
		PersonInfo pInfo = new PersonInfo();
		pInfo.setEmail(getStringVal(user.getString("email")));
		pInfo.setIDCard(getStringVal(user.getString("creditID")));
		pInfo.setMobile(getStringVal(user.getString("mobile")));
		pInfo.setNickName(getStringVal(user.getString("nickName")));
		pInfo.setHeadImgUrl(user.getString("headUrl"));
		return success(pInfo);
	}

	/**
	 * 修改个人信息
	 * 
	 * @param request
	 * @param email
	 * @param realname
	 * @param mobile
	 * @param IDCard
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Object update(HttpServletRequest request, String email, String nickName, String mobile, String IDCard) {
		String userName = UserUtil.getLoginUserName(request);
		Map<String, String> params = new HashMap<>();
		params.put("userName", userName);
		params.put("email", email);
		params.put("nickName", nickName);
		params.put("mobile", mobile);
		params.put("creditID", IDCard);
		try {
			JSONObject jsonObject = userManagerService.updateUser(params);
			if (null != jsonObject && jsonObject.getIntValue("code") == 0) {
				return success();
			} else {
				return failed(jsonObject.getString("desc"));
			}
		} catch (Exception e) {
			LOG.info("修改用户信息异常:", e);
		}
		return success();
	}

	private String getStringVal(String str) {
		str = str == null ? "" : str;
		return str;
	}

	/**
	 * 个人设置 - 修改密码
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xgmm", method = RequestMethod.GET)
	public String xgmm(HttpServletRequest request, Model model) {
		return "setting/changePassword";
	}

	/**
	 * 修改密码
	 * 
	 * @param request
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/pwdReset", method = RequestMethod.POST)
	@ResponseBody
	public Object pwdReset(HttpServletRequest request, String oldPwd, String newPwd, String ensurePwd) {
		String userName = UserUtil.getLoginUserName(request);
		Map<String, String> params = new HashMap<>();
		params.put("attrName", "userName");
		params.put("attrValue", userName);
		params.put("oldPassword", oldPwd);
		params.put("newPassword", newPwd);
		params.put("ensurePassword", ensurePwd);

		try {
			String responseJson = IDSAPICall.Call2Json("pwdReset", params);
			LOG.info("接口调用返回结果:" + responseJson);
			JSONObject jsonObject = JSONObject.parseObject(responseJson);
			if (null != jsonObject && jsonObject.getIntValue("code") == 0) {
				return success();
			} else {
				return failed(jsonObject.getString("desc"));
			}
		} catch (Exception e) {
			LOG.info("修改密码异常:", e);
		}
		return success();
	}

	/**
	 * 申请志愿者页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sqzyz", method = RequestMethod.GET)
	public String xqzyz(HttpServletRequest request, Model model) {
		return "setting/addVolunteer";
	}
	
	/**
	 * 修改头像页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/xgtx", method = RequestMethod.GET)
	public String xgtx(HttpServletRequest request, Model model) {
		return "setting/uploadHead";
	}

	@ResponseBody
	@RequestMapping(value = "/addVolunteer", method = RequestMethod.POST)
	public Object addVolunteer(HttpServletRequest request) {
		try {
			List<Message> messages = userManagerService.addVolunteer(request);
			if (messages.size() == 0) {
				return success();
			} else {
				return failed(messages, "");
			}
		} catch (DOPException e) {
			return failed(e.getMessage());
		} catch (Exception e) {
			LOG.error("添加志愿者异常:", e);
			return failed("申请失败");
		}

	}

	/**
	 * 基于用户标识的头像上传
	 * 
	 * @param file
	 *            图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		String userName = UserUtil.getLoginUserName(request);
		if (file.isEmpty())
			return failed("上传失败，请选择要上传的图片!");
		if (!file.getContentType().contains("image"))
			return failed("上传的文件不是图片类型，请重新上传!");
		try {
			// 获取图片的文件名
			String fileName = file.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = StringUtils.substringAfter(fileName, ".");
			// 新的图片文件名 = 获取时间戳+"."图片扩展名
			String newFileName = String.valueOf(System.currentTimeMillis()) + "." + extensionName;
			// 文件相对路径
			String filePath = userManagerService.getUploadImgPath(userName);

			File dest = new File(filePath, newFileName);
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}
			// 判断是否有旧头像，如果有就先删除旧头像，再上传
			JSONObject user;
			try {
				user = userManagerService.getUserInfo(userName);
			} catch (DOPException e) {
				return failed(e.getMessage());
			}
			String headImgUrl = user.getString("headUrl");
			if (StringUtil.isNotEmpty(headImgUrl)) {
				String oldFilePath = filePath.concat(headImgUrl);
				File oldFile = new File(oldFilePath);
				if (oldFile.exists()) {
					oldFile.delete();
				}
			}
			// 上传到指定目录
			file.transferTo(dest);
			// 更新头像地址到IDS
			Map<String, String> params = new HashMap<>();
			params.put("userName", userName);
			params.put("headUrl", newFileName);

			try {
				JSONObject jsonObject = userManagerService.updateUser(params);
				if (null == jsonObject || jsonObject.getIntValue("code") != 0) {
					return failed("更新头像路径失败：" + jsonObject.getString("desc"));
				}
				// 由于无法更新SSOUser中的头像字段，只能更新到session中
				request.getSession().setAttribute(DOPLoginActor.HEAD_IMG_URL, newFileName);
			} catch (DOPException e) {
				return failed(e.getMessage());
			}
			return success();
		} catch (IOException e) {
			return failed("上传失败!");
		}
	}

	/**
	 * 获取头像
	 * 
	 * @param request
	 * @param response
	 * @param imgUrl
	 */
	@RequestMapping(value = "/getHeadImg", method = RequestMethod.GET)
	public void getHeadImg(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("imgUrl") String imgUrl) {
		String userName = UserUtil.getLoginUserName(request);
		String relativePath = userManagerService.getUploadImgPath(userName);
		try {
			FileInputStream inputStream = new FileInputStream(relativePath + imgUrl);
			int i = inputStream.available();
			// byte数组用于存放图片字节数据
			byte[] buff = new byte[i];
			inputStream.read(buff);
			// 记得关闭输入流
			inputStream.close();
			// 设置发送到客户端的响应内容类型
			response.setContentType("image/*");
			OutputStream out = response.getOutputStream();
			out.write(buff);
			// 关闭响应输出流
			out.close();
		} catch (FileNotFoundException e) {
			LOG.error("找不到本地文件[{}]", imgUrl, e);
		} catch (IOException e) {
			LOG.error("IO异常", e);
		}
	}
}
