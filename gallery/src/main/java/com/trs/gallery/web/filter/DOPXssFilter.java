package com.trs.gallery.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.trs.gallery.web.wrapper.XssHttpServletRequestWrapper;


/**
 * @ClassName DOPXssFilter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author chenli
 * @Date 2017年2月20日 上午9:16:38
 * @version 1.0.0
 */
public class DOPXssFilter implements Filter
{

	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException
	{
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		filterChain.doFilter(xssRequest, response);
	}

	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

}
