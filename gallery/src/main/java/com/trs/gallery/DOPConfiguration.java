package com.trs.gallery;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;

import com.trs.idm.client.servlet.LoginProxyServlet;
import com.trs.gallery.web.filter.DOPXssFilter;
import com.trs.gallery.web.filter.WhiteListFilter;


/**
 * @Title: DOPConfiguration.java 
 * @Description: 替代传统的web.xml，使用代码的形式做配置 
 * @author chenli
 * @date 2016年12月13日 上午10:41:28
 * @version V1.0.0   
 */

@EnableAutoConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(classes = Controller.class))
@EntityScan
@Import(DOPWebConfiguration.class)
public class DOPConfiguration {
	
	/**
	 * Xss filter
	 */
	@Bean
	public FilterRegistrationBean XssFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new DOPXssFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("XssFilter");
		return registrationBean;
	}
	
	/**
	 * IDS单点登录filter
	 */
	@Bean
	public FilterRegistrationBean SSOLoginFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new WhiteListFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("SSOLoginFilter");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registrationBean;
	}
	
	/**
	 * IDS单点登录相关servlet
	 */
	@Bean
    public ServletRegistrationBean TRSIdSSSOProxyServlet() {
    	ServletRegistrationBean servletRegistration = new ServletRegistrationBean();
    	servletRegistration.setServlet(new LoginProxyServlet());
    	servletRegistration.addUrlMappings("/TRSIdSSSOProxyServlet");
    	return servletRegistration;
    }
	
	/**
	 * IDS单点登录相关servlet
	 */
	@Bean
    public ServletRegistrationBean GetLongUrlServlet() {
    	ServletRegistrationBean servletRegistration = new ServletRegistrationBean();
    	servletRegistration.setServlet(new com.trs.idm.client.servlet.GetLongUrlServlet());
    	servletRegistration.addUrlMappings("/idsAgents/GetLongUrlServlet");
    	return servletRegistration;
    }
}
