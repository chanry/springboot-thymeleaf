package com.trs.gallery.util;

import org.springframework.util.Assert;



/**
 * @ClassName ParameterCheckUtil
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author chenli
 * @Date 2017年2月20日 下午4:32:56
 * @version 1.0.0
 */
public class ParameterCheckUtil {
	/**
	 * 方法名：校验输入
	 * <p>功能说明：非空、特殊字符、SQL关键字、html等</p>
	 * @param args
	 */
	public static void verifyInput(String[] args)
	{	
		for (String arg : args) {
			arg = arg.toLowerCase();// 统一转为小写
			arg.replaceAll("--", "——");
			arg.replaceAll("'", "’");
			String badStr = "'|or |exec|execute|insert|select|delete|update|master|truncate|javascript|count(*)|"
					+ "declare|create|" + "grant|script|iframe" + "|--";// 过滤掉的sql关键字，可以手动添加
			String[] badStrs = badStr.split("\\|");
			for (int i = 0; i < badStrs.length; i++) {
				if (arg.indexOf(badStrs[i].toLowerCase()) >= 0) {
					Assert.isTrue(false, "检测到请求参数中包含sql关键字");
				}
			}
		}
	}

	/**
	 * 方法名：校验输入
	 * <p>功能说明：</p>
	 * @param ignoreNullOrEmpty	是否忽略空值校验
	 * @param args
	 */
	public static void verifyInput(boolean ignoreNullOrEmpty, String[] args)
	{
		verifyInput(args);
	}

	/**
	 * 方法名：检验URL
	 * <p>功能说明：</p>
	 * @param ignoreNullOrEmpty
	 * @param args
	 */
	public static void verifyUrl(boolean ignoreNullOrEmpty, String[] args)
	{
		verifyInput(args);
	}

	/**
	 * 方法名：校验输入
	 * <p>
	 * 功能说明：
	 * </p>
	 * 
	 * @param ignoreNullOrEmpty
	 *            是否忽略空值校验
	 * @param ignoreSpecialChar
	 *            是否忽略特殊字符校验
	 * @param args
	 */
	public static void verifyInput(boolean ignoreNullOrEmpty, boolean ignoreSpecialChar, String[] args)
	{
		verifyInput(args);
	}

	/**
	 * 方法名：检验Json
	 * <p>功能说明：</p>
	 * @param ignoreNullOrEmpty
	 * @param args
	 */
	public static void verifyJson(boolean ignoreNullOrEmpty, String... args)
	{
		verifyInput(args);
	}

	/**
	 * 方法名：校验参数
	 * <p>功能说明：</p>
	 * @param arg	参数值
	 * @param checkNotNull	非空校验
	 * @param checkSpecialChar	特殊字符校验
	 * @param checkSqlKeyword	SQL关键字校验
	 * @param checkContainHtml	包含HTML校验
	 */
	public static void verifyInput(Object arg, boolean checkNotNull, boolean checkSpecialChar, boolean checkSqlKeyword,
			boolean checkContainHtml)
	{
		
	}

}
