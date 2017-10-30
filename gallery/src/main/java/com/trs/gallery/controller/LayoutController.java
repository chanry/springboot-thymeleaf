package com.trs.gallery.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.gallery.entity.custom.LayoutInfoBiz;
import com.trs.gallery.entity.generated.LayoutInfo;
import com.trs.gallery.entity.generated.UserLayoutInfoKey;
import com.trs.gallery.service.LayoutService;
import com.trs.gallery.util.UserUtil;

@Controller
@RequestMapping(value = "/layout")
public class LayoutController extends BaseController {
	
	private Logger LOG = LoggerFactory.getLogger(LayoutController.class);
	
	@Value("${server.context-path}")
	private String context;
	@Autowired
	private LayoutService layoutService;
	
	@RequestMapping
	public String index(HttpServletRequest request, Model model) {
		model.addAttribute("data", layoutService.findLayoutList());
		return "layout/dingZhi";
	}
	
	/**
	 * 获取自定义栏目
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getLayouts", method = RequestMethod.GET)
	public Object getLayouts(HttpServletRequest request, Model model) {
		try {
			String userName = UserUtil.getLoginUserName(request);
			UserLayoutInfoKey key = new UserLayoutInfoKey();
			key.setUserName(userName);
			// 用户已选择的自定义栏目
			List<UserLayoutInfoKey> uInfoKeys = layoutService.findList(key);
			// 所有可自定义栏目数据
			List<LayoutInfo> layoutInfos = layoutService.findLayoutList();
			// 所有可自定义栏目数据并标注用户是否已选择
			List<LayoutInfoBiz> layoutInfoBizs = new ArrayList<>();
			for (LayoutInfo layoutInfo : layoutInfos) {
				layoutInfo.setIconUrl(context+layoutInfo.getIconUrl());
				LayoutInfoBiz layoutInfoBiz = new LayoutInfoBiz(layoutInfo);
				if (CollectionUtils.isNotEmpty(uInfoKeys)) {
					for (UserLayoutInfoKey userLayoutInfoKey : uInfoKeys) {
						if (userLayoutInfoKey.getChannelId().equals(layoutInfo.getChannelId())) {
							layoutInfoBiz.setHasCheck(true);
						}
					}
				}
				layoutInfoBizs.add(layoutInfoBiz);
			}
			return success(layoutInfoBizs);
		} catch (Exception e) {
			LOG.error("查询异常：", e);
			return failed();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, String channelId) {
		try {
			String userName = UserUtil.getLoginUserName(request);
			UserLayoutInfoKey uInfo = new UserLayoutInfoKey(userName, Integer.valueOf(channelId));
			layoutService.addRecord(uInfo);
			return success();
		} catch (Exception e) {
			LOG.error("查询异常：", e);
			return failed();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, String channelId) {
		try {
			String userName = UserUtil.getLoginUserName(request);
			UserLayoutInfoKey uInfo = new UserLayoutInfoKey(userName, Integer.valueOf(channelId));
			layoutService.deleteRecord(uInfo);
			return success();
		} catch (Exception e) {
			LOG.error("查询异常：", e);
			return failed();
		}
	}
	
}
