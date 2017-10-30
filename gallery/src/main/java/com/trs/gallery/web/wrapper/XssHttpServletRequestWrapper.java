package com.trs.gallery.web.wrapper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;


/**
 * @ClassName XssHttpServletRequestWrapper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author chenli
 * @Date 2017年2月20日 上午9:16:12
 * @version 1.0.0
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper
{
	HttpServletRequest orgRequest = null;

	public XssHttpServletRequestWrapper(HttpServletRequest request)
	{
		super(request);
		orgRequest = request;
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name)
	{
		String value = super.getParameter(xssEncode(name));
		if (value != null)
		{
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 * getHeaderNames 也可能需要覆盖
	 */
	@Override
	public String getHeader(String name)
	{

		String value = super.getHeader(xssEncode(name));
		if (value != null)
		{
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 * @param s
	 * @return
	 */
	private static String xssEncode(String s)
	{
		if (s == null || s.isEmpty())
		{
			return s;
		}

		return HtmlUtils.htmlEscape(s);
	}
	
	/**
	 * 参数sql检查
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param str
	 * @return
	 */
	protected static boolean sqlValidate(String str) {
		str = str.toLowerCase();// 统一转为小写
		str.replaceAll("--", "——");
		str.replaceAll("'", "’");
		String badStr = "'|or |exec|execute|insert|select|delete|update|master|truncate|javascript|count(*)|"
				+ "declare|create|" + "grant|script|iframe" + "|--";// 过滤掉的sql关键字，可以手动添加
		String[] badStrs = badStr.split("\\|");
		for (int i = 0; i < badStrs.length; i++) {
			if (str.indexOf(badStrs[i].toLowerCase()) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOk() {
		// 判断所有请求参数是否包含sql注入
		Map<String, String[]> rm = orgRequest.getParameterMap();
		Iterator<Entry<String, String[]>> iterator = rm.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String[]> entry = iterator.next();
			String[] value = (String[]) entry.getValue();
			if (value != null && value.length > 0) {
				for (int i = 0, m = value.length; i < m; i++) {
					if (sqlValidate(value[i])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 获取最原始的request
	 * 
	 * @return
	 */
	public HttpServletRequest getOrgRequest()
	{
		return orgRequest;
	}

	/**
	 * 获取最原始的request的静态方法
	 * 
	 * @return
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest req)
	{
		if (req instanceof XssHttpServletRequestWrapper)
		{
			return ((XssHttpServletRequestWrapper) req).getOrgRequest();
		}

		return req;
	}
}