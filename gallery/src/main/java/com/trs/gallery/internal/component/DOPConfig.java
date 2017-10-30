package com.trs.gallery.internal.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "dop")
@Component
public class DOPConfig {
	/**
	 * 域名
	 */
	private String domainName;
	
	/**
	 * 首页
	 */
	private String index;
	
	/**
	 * 退出地址
	 */
	private String logoutUrl;

	/**
	 * @return the 域名
	 */
	public String getDomainName() {
		return domainName;
	}
	

	/**
	 * @param domainName the 域名 to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	

	/**
	 * @return the 首页
	 */
	public String getIndex() {
		return index;
	}
	

	/**
	 * @param index the 首页 to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	

	/**
	 * @return the 退出地址
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}
	

	/**
	 * @param logoutUrl the 退出地址 to set
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
}
