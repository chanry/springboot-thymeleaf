package com.trs.gallery.service.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trs.gallery.entity.custom.AppointmentInfo;
import com.trs.gallery.entity.custom.PageInfo;
import com.trs.gallery.entity.custom.PageResult;
import com.trs.gallery.internal.component.WCMConfig;
import com.trs.gallery.internal.exception.DOPException;
import com.trs.gallery.mapper.custom.AppointmentDao;
import com.trs.gallery.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	private final static Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	@Autowired
	private WCMConfig wcmConfig;

	@Autowired
	private AppointmentDao appointmentDao;

	@Override
	public List<AppointmentInfo> findList(PageInfo pageInfo) {
		return appointmentDao.findList(pageInfo);
	}

	@Override
	public Integer findListTotalNum(PageInfo pageInfo) {
		return appointmentDao.findListTotalNum(pageInfo);
	}

	@Override
	public PageResult<AppointmentInfo> findListAndTotalNum(PageInfo pageInfo) {
		PageResult<AppointmentInfo> pageResult = new PageResult<AppointmentInfo>();
		pageResult.setDataList(this.findList(pageInfo));
		pageResult.setTotalNum(this.findListTotalNum(pageInfo));
		return pageResult;
	}

	@Override
	public void addRecord(String userName, Map<String, String[]> map) throws DOPException {
		String apptDate = map.get("apptDate")[0];
		String ticketNumStr = map.get("ticketNum")[0];
		String IDCardNum = map.get("IDCardNum")[0];
		String mobile = map.get("mobile")[0];
		String dateStr = map.get("dateStr")[0];
		//校验日期、票数、身份证号、手机号
		apptDateCheck(apptDate);
		ticketNumCheck(userName, apptDate, ticketNumStr);
		IDCardNumCheck(IDCardNum);
		mobileCheck(mobile);
		String wcmUrl = wcmConfig.getInfogatePath();
		PostMethod postMethod = new PostMethod(wcmUrl);
		postMethod.addParameter("DocumentId", "0");
		postMethod.addParameter("SiteId", wcmConfig.getSiteId());
		postMethod.addParameter("ChannelId", wcmConfig.getApptChnlId());
		postMethod.addParameter("InfoViewId", wcmConfig.getApptFormId());
		postMethod.addParameter("encoding", "UTF-8");
		postMethod.addParameter("verifycode", "0");
		postMethod.addParameter("needverifycode", "0");
		postMethod.addParameter("infogatePath", wcmUrl);
		// 预约账号
		postMethod.addParameter("userName", userName);
		// 预约人真实姓名
		postMethod.addParameter("realname", map.get("realname")[0]);
		// 身份证号
		postMethod.addParameter("IDCardNum", IDCardNum);
		// 手机号
		postMethod.addParameter("mobile", mobile);
		// 预约票数
		postMethod.addParameter("ticketNum", ticketNumStr);
		// 预约日期
		postMethod.addParameter("apptDate", apptDate);
		// 字符串日期
		postMethod.addParameter("dateStr", dateStr);
		
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

	@Transactional
	@Override
	public void deleteRecord(Integer docId, String userName) {
		appointmentDao.deleteDocument(docId);
		appointmentDao.deleteChnlDoc(docId);
		appointmentDao.deleteAppointment(docId, Integer.valueOf(wcmConfig.getApptFormId()), userName);
	}
	
	/**
	 * 校验预约日期
	 * @param apptDate
	 * @throws DOPException
	 */
	private void apptDateCheck(String apptDate) throws DOPException {
		Pattern pattern = Pattern.compile(wcmConfig.getApptDateReg());
		Matcher matcher = pattern.matcher(apptDate);
		if (!matcher.matches()) throw new DOPException("预约日期格式不对");
	}
	
	/**
	 * 校验预约票数
	 * @param userName
	 * @param apptDate
	 * @param ticketNumStr
	 * @throws DOPException
	 */
	private void ticketNumCheck(String userName, String apptDate, String ticketNumStr) throws DOPException {
		Integer totalNum = appointmentDao.selectTicketNum(userName, apptDate, Integer.valueOf(wcmConfig.getApptFormId()));
		totalNum = (totalNum == null) ? 0 : totalNum;
		Integer ticketNum = 0;
		try {
			ticketNum = Integer.valueOf(ticketNumStr);
			//该日期被预约票总数
			Integer hasApptNum = appointmentDao.selectTicketNumByDate(apptDate, Integer.valueOf(wcmConfig.getApptFormId()));
			hasApptNum = (hasApptNum == null) ? 0 : hasApptNum;
			if ((ticketNum + hasApptNum) > wcmConfig.getTotalTicketNum())
				throw new DOPException("预约票数不足");
		} catch (Exception e) {
			LOG.error("ticketNum转换异常", e);
			throw new DOPException("预约票数格式不对");
		}
		totalNum += ticketNum;
		if (totalNum > wcmConfig.getMaxTicketNum())
			throw new DOPException("个人预约票数超过上限");
	}
	
	/**
	 * 身份证号码校验
	 * @param IDCardNum
	 * @throws DOPException
	 */
	private void IDCardNumCheck(String IDCardNum) throws DOPException {
		Pattern pattern = Pattern.compile("/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/");
		Matcher matcher = pattern.matcher(IDCardNum);
		if (!matcher.matches()) throw new DOPException("身份证号码格式不对");
	}
	
	/**
	 * 身份证号码校验
	 * @param mobile
	 * @throws DOPException
	 */
	private void mobileCheck(String mobile) throws DOPException {
		Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
		Matcher matcher = pattern.matcher(mobile);
		if (!matcher.matches()) throw new DOPException("手机号码格式不对");
	}

	@Override
	public Integer selectTicketNumByDate(String apptDate) {
		return appointmentDao.selectTicketNumByDate(apptDate, Integer.valueOf(wcmConfig.getApptFormId()));
	}
}
