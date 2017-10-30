package com.trs.gallery.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

import com.trs.gallery.entity.custom.DocumentInfo;
import com.trs.gallery.entity.generated.UserLayoutInfoKey;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.service.LayoutService;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;

@Controller
public class IndexController extends BaseController {

	private final static Logger LOG = LoggerFactory.getLogger(IndexController.class);

	@Value("${dop.login.success-url}")
	private String successUrl;
	@Autowired
	private LayoutService layoutService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private WCMConfig wcmConfig;

	@RequestMapping(value = "/")
	public String index(HttpServletRequest request) {
		if (request.getSession().getAttribute("returnUrl") != null) {
			return "redirect:" + request.getSession().getAttribute("returnUrl");
		}
		return "redirect:" + successUrl;
	}

	@RequestMapping(value = "/pcenter")
	public String pcenter(HttpServletRequest request, Model model) {
		return "index";
	}

	/**
	 * 个人中心获取自定义栏目数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPcenterInfo", method = RequestMethod.GET)
	@ResponseBody
	public Object getPcenterInfo(HttpServletRequest request) {
		String userName = "cl1993";
		//String userName = UserUtil.getLoginUserName(request);
		UserLayoutInfoKey userLayoutInfo = new UserLayoutInfoKey(userName, null);
		List<UserLayoutInfoKey> uInfo = layoutService.findList(userLayoutInfo);
		List<Integer> channelIdList = new ArrayList<>();
		boolean isContainDCJS = false;
		if (uInfo == null || CollectionUtils.isEmpty(uInfo)) {
			channelIdList.add(wcmConfig.getExbChnlId());
			channelIdList.add(wcmConfig.getMediaChnlId());
		} else {
			for (UserLayoutInfoKey uLayoutInfo : uInfo) {
				Integer channelId = uLayoutInfo.getChannelId();
				if (channelId == wcmConfig.getDcjsChnlId()) {
					isContainDCJS = true;
					continue;
				}
				channelIdList.add(channelId);
			}
		}

		/**
		 * 获取个人中心自定义页面栏目数据 以栏目名为key，栏目下的文档数据列表为value
		 */
		Map<String, Object> result = documentService.findDocumentsBychnlIds(channelIdList);
		if (isContainDCJS) {
			result.put("典藏鉴赏", documentService.findDCJSDocuments(userName, 1, 1, 4));
		}
		return success(result);
	}

	/**
	 * 个人中心点击日历获取指定栏目数据
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDateDocument", method = RequestMethod.GET)
	public Object getDateDocument(HttpServletRequest request, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date selectDate = null;
		List<DocumentInfo> news = new ArrayList<>();
		List<DocumentInfo> shows = new ArrayList<>();
		List<DocumentInfo> acts = new ArrayList<>();
		Pattern pattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}$");
		Map<String, Object> result = new HashMap<>();
		try {
			if (StringUtil.isEmpty(date)) {
				selectDate = new Date();
			} else if (!pattern.matcher(date).matches()) {
				selectDate = new Date();
			} else {
				selectDate = sdf.parse(date);
			}
			news = documentService.findDocumentsByDate(selectDate, Integer.valueOf(wcmConfig.getNewsChnlId()));
			shows = documentService.findDocumentsByDate(selectDate, Integer.valueOf(wcmConfig.getExbChnlId()));
			acts = documentService.findDocumentsByDate(selectDate, Integer.valueOf(wcmConfig.getActChnlId()));
		} catch (ParseException e) {
			LOG.error("日期转换异常：", e);
			return failed("日期格式不对！");
		} catch (Exception e) {
			LOG.error("查询异常：", e);
			return failed("查询失败！");
		}
		result.put("news", news);
		result.put("shows", shows);
		result.put("acts", acts);
		return success(result);
	}
	
	/**
	 * 获取栏目列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getChannelList")
	public Object getChannelList(HttpServletRequest request) {
		try {
			List<Integer> chnlIds = new ArrayList<>();
			chnlIds.add(wcmConfig.getExbChnlId());
			chnlIds.add(wcmConfig.getMediaChnlId());
			chnlIds.add(wcmConfig.getDcjsChnlId());
			chnlIds.add(wcmConfig.getActChnlId());
			chnlIds.add(wcmConfig.getYjczChnlId());
			return success(documentService.findChannelListByChannelIds(chnlIds));
		} catch (Exception e) {
			LOG.error("查询异常：", e);
			return failed("查询栏目列表失败!");
		}
	}
}
