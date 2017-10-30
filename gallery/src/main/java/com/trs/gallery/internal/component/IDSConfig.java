package com.trs.gallery.internal.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ids")
@Component
public class IDSConfig {
	/**
	 * 根地址
	 */
	private String index;
	
	/**
	 * 登录地址
	 */
	private String loginUrl;
	
	/**
	 * 应用名
	 */
	private String appName;
	
	/**
	 * 密钥
	 */
	private String secretkey;
	
	/**
	 * 摘要算法
	 */
	private String digestalgorithm;
	
	/**
	 * ids地址
	 */
	private String url;

	/**
	 * @return the 根地址
	 */
	public String getIndex() {
		return index;
	}
	

	/**
	 * @param index the 根地址 to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	

	/**
	 * @return the 登录地址
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	

	/**
	 * @param loginUrl the 登录地址 to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	

	/**
	 * @return the 应用名
	 */
	public String getAppName() {
		return appName;
	}
	

	/**
	 * @param appName the 应用名 to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getSecretkey() {
		return secretkey;
	}


	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}


	public String getDigestalgorithm() {
		return digestalgorithm;
	}


	public void setDigestalgorithm(String digestalgorithm) {
		this.digestalgorithm = digestalgorithm;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
}
