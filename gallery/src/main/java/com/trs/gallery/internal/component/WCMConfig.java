package com.trs.gallery.internal.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wcm")
@Component
public class WCMConfig {
	/**
	 * ip地址
	 */
	private String ip;
	
	/**
	 * 域名地址
	 */
	private String domain;
	
	/**
	 * 图片地址
	 */
	private String imgUrl;
	
	/**
	 * 表单提交地址
	 */
	private String infogatePath;
	
	/**
	 * 站点ID
	 */
	private String siteId;
	
	/**
	 * 预约栏目ID
	 */
	private String apptChnlId;
	
	/**
	 * 预约表单ID
	 */
	private String apptFormId;
	
	/**
	 * 预约票数最大限制数
	 */
	private Integer maxTicketNum;
	
	/**
	 * 预约日期正则表达式
	 */
	private String apptDateReg;
	
	/**
	 * 每日提供票数
	 */
	private Integer totalTicketNum;
	
	/**
	 * 跨域请求回调方法名
	 */
	private String jsonpParam;
	
	/**
	 * 新闻栏目ID
	 */
	private Integer newsChnlId;
	
	/**
	 * 展厅栏目ID
	 */
	private Integer exbChnlId;
	
	/**
	 * 活动栏目ID
	 */
	private Integer actChnlId;
	
	/**
	 * 媒体中心栏目ID
	 */
	private Integer mediaChnlId;
	
	/**
	 * 典藏鉴赏栏目ID
	 */
	private Integer dcjsChnlId;
	
	/**
	 * 研究策展栏目ID
	 */
	private Integer yjczChnlId;
	
	/**
	 * 志愿者栏目ID
	 */
	private String zyzChnlId;
	
	/**
	 * 志愿者表单ID
	 */
	private String zyzFormId;
	
	/**
	 * 新闻二级栏目ID列表
	 */
	private String newsChnlIds;
	

	/**
	 * @return the ip地址
	 */
	public String getIp() {
		return ip;
	}
	

	/**
	 * @param ip the ip地址 to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	

	public String getDomain() {
		return domain;
	}


	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	
	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}


	/**
	 * @return the 表单提交地址
	 */
	public String getInfogatePath() {
		return infogatePath;
	}
	

	/**
	 * @param infogatePath the 表单提交地址 to set
	 */
	public void setInfogatePath(String infogatePath) {
		this.infogatePath = infogatePath;
	}


	public String getSiteId() {
		return siteId;
	}


	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}


	public String getApptChnlId() {
		return apptChnlId;
	}


	public void setApptChnlId(String apptChnlId) {
		this.apptChnlId = apptChnlId;
	}


	public String getApptFormId() {
		return apptFormId;
	}


	public void setApptFormId(String apptFormId) {
		this.apptFormId = apptFormId;
	}


	public Integer getMaxTicketNum() {
		return maxTicketNum;
	}


	public void setMaxTicketNum(Integer maxTicketNum) {
		this.maxTicketNum = maxTicketNum;
	}


	public String getApptDateReg() {
		return apptDateReg;
	}


	public void setApptDateReg(String apptDateReg) {
		this.apptDateReg = apptDateReg;
	}


	public Integer getTotalTicketNum() {
		return totalTicketNum;
	}


	public void setTotalTicketNum(Integer totalTicketNum) {
		this.totalTicketNum = totalTicketNum;
	}


	public String getJsonpParam() {
		return jsonpParam;
	}


	public void setJsonpParam(String jsonpParam) {
		this.jsonpParam = jsonpParam;
	}


	public Integer getNewsChnlId() {
		return newsChnlId;
	}


	public void setNewsChnlId(Integer newsChnlId) {
		this.newsChnlId = newsChnlId;
	}


	public Integer getExbChnlId() {
		return exbChnlId;
	}


	public void setExbChnlId(Integer exbChnlId) {
		this.exbChnlId = exbChnlId;
	}


	public Integer getActChnlId() {
		return actChnlId;
	}


	public void setActChnlId(Integer actChnlId) {
		this.actChnlId = actChnlId;
	}


	public Integer getMediaChnlId() {
		return mediaChnlId;
	}


	public void setMediaChnlId(Integer mediaChnlId) {
		this.mediaChnlId = mediaChnlId;
	}


	public Integer getDcjsChnlId() {
		return dcjsChnlId;
	}


	public void setDcjsChnlId(Integer dcjsChnlId) {
		this.dcjsChnlId = dcjsChnlId;
	}


	public Integer getYjczChnlId() {
		return yjczChnlId;
	}


	public void setYjczChnlId(Integer yjczChnlId) {
		this.yjczChnlId = yjczChnlId;
	}


	public String getZyzChnlId() {
		return zyzChnlId;
	}


	public void setZyzChnlId(String zyzChnlId) {
		this.zyzChnlId = zyzChnlId;
	}


	public String getZyzFormId() {
		return zyzFormId;
	}


	public void setZyzFormId(String zyzFormId) {
		this.zyzFormId = zyzFormId;
	}


	public String getNewsChnlIds() {
		return newsChnlIds;
	}


	public void setNewsChnlIds(String newsChnlIds) {
		this.newsChnlIds = newsChnlIds;
	}
	
}
