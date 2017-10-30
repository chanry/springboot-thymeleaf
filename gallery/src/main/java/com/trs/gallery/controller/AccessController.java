package com.trs.gallery.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.gallery.entity.generated.UserAccessInfo;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.internal.exception.NullParamException;
import com.trs.gallery.service.AccessService;
import com.trs.gallery.service.DocumentService;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;


/**
 * @className AccessController.java
 * @author chenli
 * @date 2017年6月19日 下午5:36:23
 */
@Controller
@RequestMapping(value = "/access")
public class AccessController extends BaseController {

	private final static Logger LOG = LoggerFactory.getLogger(AccessController.class);
	
	@Autowired
	private AccessService accessService;
	@Autowired
	private WCMConfig wcmConfig;
	@Autowired
	private DocumentService documentService;
	
	
	/**
	 * 
	 * @param request
	 * @param dataId
	 * @param dataName
	 * @param userName
	 * @param dataType
	 * @param dataArea
	 * @param provider
	 * @param label
	 * @param detailUrl
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addAccessRecord(HttpServletRequest request, String docId) {
    	try {
    		String userName = UserUtil.getLoginUserName(request);
    		UserAccessInfo accessInfo = new UserAccessInfo();
    		accessInfo.setUserName(userName);
    		accessInfo.setDocId(Integer.valueOf(docId));
    		accessInfo.setAccessTime(new Date());
    		accessService.addRecord(accessInfo);
		} catch (Exception e) {
			LOG.error("添加数据ID为[{}]的访问记录失败，异常信息：", docId, e);
		}
    }
	
	@ResponseBody
	@RequestMapping(value = "/lately")
	public Object getMyAccessList(HttpServletRequest request, String channelId) {
		String userName = UserUtil.getLoginUserName(request);
		try {
			if (StringUtil.isEmpty(userName))
				throw new NullParamException("用户未登录，用户名");
			Integer chnlId = Integer.valueOf(channelId);
			if (chnlId == wcmConfig.getDcjsChnlId()) {
				return success(documentService.findDCJSDocuments(userName, 3, 1, 4));
			} else {
				return success(accessService.findLatelyAccessList(userName, Integer.valueOf(channelId)));
			}
		} catch (NullParamException ne) {
			return failed(ne.getMessage());
		} catch (Exception e) {
			LOG.error("查询用户[{}]历史访问记录失败", userName, e);
			return failed("查询最近访问失败");
		}
	}
}
