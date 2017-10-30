package com.trs.gallery.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.gallery.entity.generated.UserCollectInfo;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.service.CollectService;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;

@Controller
@RequestMapping(value = "/collect")
public class CollectController extends BaseController {

	private final static Logger LOG = LoggerFactory.getLogger(CollectController.class);

	@Autowired
	private CollectService collectService;
	@Autowired
	private WCMConfig wcmConfig;
	@Autowired
	private DocumentService documentService;

	@RequestMapping
	public String index(HttpServletRequest request, @RequestParam(value = PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = CUR_PAGE, required = false) Integer curPage, Model model) {
		return "collect/exhibition";
	}
	
	/**
	 * 个人中心-我的收藏
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCollects", method = RequestMethod.GET)
	public Object getMyAppts(HttpServletRequest request,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = "4") Integer pageSize,
			@RequestParam(value = CUR_PAGE, required = false, defaultValue = "1") Integer curPage,
			String channelId) {
		String userName = UserUtil.getLoginUserName(request);
		Integer chnlId = Integer.valueOf(channelId);
		try {
			if (chnlId == wcmConfig.getDcjsChnlId()) {
				return success(documentService.findDCJSDocuments(userName, 2, curPage, pageSize));
			} else {
				return success(collectService.findListBychnlId(userName, chnlId, curPage, pageSize));
			}
		} catch (Exception e) {
			LOG.error("查询用户收藏记录失败", e);
			return failed();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, @RequestParam(value = "docId") String docId) {
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		try {
			UserCollectInfo userCollectInfo = new UserCollectInfo(userName, Integer.valueOf(docId), new Date());
			collectService.addRecord(userCollectInfo);
			return success();
		} catch (Exception e) {
			LOG.error("收藏失败", e);
			return failed("收藏失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, @RequestParam(value = "docId") String docId) {
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		if (StringUtil.isEmpty(docId)) 
			return failed("请选择需要取消收藏的文档");
		try {
			String[] docIdArr = docId.split(",");
			for (int i = 0; i < docIdArr.length; i++) {
				collectService.deleteRecord(userName, Integer.valueOf(docIdArr[i]));
			}
			return success();
		} catch (Exception e) {
			LOG.error("取消收藏失败", e);
			return failed("取消收藏失败");
		}
	}
	
	/**
	 * 是否已收藏
	 * @param request
	 * @param docId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public Object isCollectCheck(HttpServletRequest request, @RequestParam(value = "docId") String docId) {
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		boolean isCollect = collectService.isCollect(userName, Integer.valueOf(docId));
		return success(isCollect);
	}
	
	/**
	 * 我的收藏 - 媒体中心
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mtzx", method = RequestMethod.GET)
	public String mtzx(HttpServletRequest request, Model model) {
		return "collect/mediaCenter";
	}
	
	/**
	 * 我的收藏 - 公共教育
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ggjy", method = RequestMethod.GET)
	public String ggjy(HttpServletRequest request, Model model) {
		return "collect/education";
	}
	
	/**
	 * 我的收藏 - 典藏鉴赏
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dcjs", method = RequestMethod.GET)
	public String dcjs(HttpServletRequest request, Model model) {
		return "collect/art";
	}
	
	/**
	 * 我的收藏 - 研究策展
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/yjcz", method = RequestMethod.GET)
	public String yjcz(HttpServletRequest request, Model model) {
		return "collect/study";
	}
	
	/**
	 * 我的收藏 - 社会服务
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/shfw", method = RequestMethod.GET)
	public String shfw(HttpServletRequest request, Model model) {
		return "collect/social";
	}

}
