package com.trs.gallery.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trs.idm.client.filter.GeneralSSOFilter;


public class WhiteListFilter extends GeneralSSOFilter{
	private static final Logger LOG = LoggerFactory.getLogger(WhiteListFilter.class);

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String servletPath = request.getServletPath();
		if (request.getParameter("returnUrl") != null) {
			request.getSession().setAttribute("returnUrl", request.getParameter("returnUrl"));
		}
		// 路径配置待优化
		if (servletPath.startsWith("/interface/") || servletPath.startsWith("/front/")){
			if (!servletPath.startsWith("/front/common"))
				LOG.debug("拦截到白名单请求路径[{}],直接放行", servletPath);
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}
		super.doFilter(servletRequest, servletResponse, filterChain);
	}
}
