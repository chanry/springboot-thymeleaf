package com.trs.gallery.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trs.gallery.entity.custom.AppointmentInfo;
import com.trs.gallery.entity.custom.Const;
import com.trs.gallery.entity.custom.PageInfo;
import com.trs.gallery.entity.custom.PageResult;
import com.trs.gallery.entity.custom.SampleAppointmentInfo;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.internal.exception.DOPException;
import com.trs.gallery.service.AppointmentService;
import com.trs.gallery.util.DateUtil;
import com.trs.gallery.util.StringUtil;
import com.trs.gallery.util.UserUtil;

@Controller
@RequestMapping(value = "/appt")
public class AppointmentController extends BaseController {

	private final static Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private WCMConfig wcmConfig;
	
	
	/**
	 * 个人中心-获取最近一周预约情况
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping
	public String index(HttpServletRequest request, Model model) {
		// 一周预约信息构造
		Date today = new Date();
		List<Date> dates = DateUtil.getRecentlyDateList(today, 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEE");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		List<SampleAppointmentInfo> sInfos = new ArrayList<>();
		for (Date date : dates) {
			String apptDate = sdf.format(date);
			SampleAppointmentInfo sAppointmentInfo = new SampleAppointmentInfo();
			sAppointmentInfo.setApptDate(apptDate);
			sAppointmentInfo.setDateStr(sdf2.format(date));
			if (apptDate.contains(Const.CLOSE_DAY)) {
				sAppointmentInfo.setStatus(Const.CLOSE_DESC);
				sAppointmentInfo.setTicketNum(0);
			} else {
				try {
					Integer hasApptNum = appointmentService.selectTicketNumByDate(apptDate);
					hasApptNum = (hasApptNum == null) ? 0 : hasApptNum;
					sAppointmentInfo.setTicketNum(hasApptNum);
				} catch (Exception e) {
					LOG.error("查询日期[{}]的已预约票数出错", apptDate, e);
				}
				
			}
			sInfos.add(sAppointmentInfo);
		}
		model.addAttribute("list", sInfos);
		
		// 个人预约信息查询
		String userName = UserUtil.getLoginUserName(request);
		Map<String, Object> param = new HashMap<>();
		param.put("userName", userName);
		PageResult<AppointmentInfo> pageResult = appointmentService
				.findListAndTotalNum(new PageInfo(10, 1, param, wcmConfig.getApptFormId()));
		model.addAttribute("data", pageResult);
		
		return "appointment/yuYue";
	}
	
	/**
	 * 个人中心-门票预约
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Object add(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> reqMap = request.getParameterMap();
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		try {
			appointmentService.addRecord(userName, reqMap);
			return success();
		} catch (DOPException e) {
			return failed(e.getMessage());
		} catch (Exception e) {
			LOG.error("submit Exception:{}", e.getMessage());
			return failed("信息提交失败");
		}
	}

	/**
	 * 个人中心-取消预约
	 * 
	 * @param request
	 * @param docId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Object delete(HttpServletRequest request, @RequestParam(value = "docId") Integer docId) {
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		try {
			appointmentService.deleteRecord(docId, userName);
			return success();
		} catch (Exception e) {
			LOG.error("取消收藏失败", e);
			return failed("取消收藏失败");
		}
	}
	
	/**
	 * 个人中心-预约列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAppts", method = RequestMethod.GET)
	public Object getAppts(HttpServletRequest request,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = CUR_PAGE, required = false, defaultValue = "1") Integer curPage) {
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		try {
			// 一周预约信息构造
			Date today = new Date();
			List<Date> dates = DateUtil.getRecentlyDateList(today, 7);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEE");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			List<SampleAppointmentInfo> sInfos = new ArrayList<>();
			for (Date date : dates) {
				String apptDate = sdf.format(date);
				SampleAppointmentInfo sAppointmentInfo = new SampleAppointmentInfo();
				sAppointmentInfo.setApptDate(apptDate);
				sAppointmentInfo.setDateStr(sdf2.format(date));
				if (apptDate.contains(Const.CLOSE_DAY)) {
					sAppointmentInfo.setStatus(Const.CLOSE_DESC);
					sAppointmentInfo.setTicketNum(0);
				} else {
					try {
						Integer hasApptNum = appointmentService.selectTicketNumByDate(apptDate);
						hasApptNum = (hasApptNum == null) ? 0 : hasApptNum;
						sAppointmentInfo.setTicketNum(wcmConfig.getTotalTicketNum() - hasApptNum);
					} catch (Exception e) {
						LOG.error("查询日期[{}]的已预约票数出错", apptDate, e);
					}
					
				}
				sInfos.add(sAppointmentInfo);
			}
			return success(sInfos);
		} catch (Exception e) {
			LOG.error("show Exception:{}", e.getMessage());
			return failed("查询有误");
		}
	}
	
	/**
	 * 个人中心-我的预约信息展示
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyAppts", method = RequestMethod.GET)
	public Object getMyAppts(HttpServletRequest request,
			@RequestParam(value = PAGE_SIZE, required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = CUR_PAGE, required = false, defaultValue = "1") Integer curPage) {
		String userName = UserUtil.getLoginUserName(request);
		if (StringUtil.isEmpty(userName))
			return failed("用户未登录");
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("userName", userName);
			PageResult<AppointmentInfo> pageResult = appointmentService
					.findListAndTotalNum(new PageInfo(pageSize, curPage, param, wcmConfig.getApptFormId()));
			return result(true, "", pageResult.getDataList(), pageResult.getTotalNum());
		} catch (Exception e) {
			LOG.error("show Exception:{}", e.getMessage());
			return failed("查询有误");
		}
	}
	
}
