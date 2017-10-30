package com.trs.gallery.util;

import java.util.HashMap;
import java.util.Map;

public class ActionResultUtil {
	
	private final static String RESULT_KEY = "result";
	
	private final static String MSG_KEY = "msg";
	
	private final static String DATA_KEY = "data";
	
	public final static String PAGE_SIZE = "pageSize";
	
	public final static String CUR_PAGE = "curPage";
	
	public final static Integer PAGE_NUM_DEFAULT_VAL = 10;
	
	public final static Integer CUR_PAGE_DEFAULT_VAL = 1;
	
	public final static String TOTAL_NUM = "totalNum";
	
	
	/**
	 * exception返回信息组成
	 * @param desc 描述，默认用英文半角问号?进行参数替代
	 * @param params 替代参数
	 * @return
	 */
	public static String exResult(String desc, Object ... params){
		return exResult("?", desc, params);
	}
	
	/**
	 * exception返回信息组成
	 * @param flag 分隔符，默认用英文半角问号?进行参数替代
	 * @param desc 描述
	 * @param params 替代参数
	 * @return
	 */
	public static String exResult(String flag, String desc, Object... params){
		for(int i = 0; i < params.length; i++){
			desc = desc.substring(0, desc.indexOf(flag)) + params[i] + desc.substring(desc.indexOf(flag) + 1, desc.length());
		}
		return desc;
	}
	
	
	/**
	 * 前台返回结果集
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param result 查询状态
	 * @param data 数据结果集
	 * @return
	 */
	public static Object success(Object data) {
		return result(true, "", data);
	}
	
	/**
	 * 前台返回结果集
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param result 查询状态
	 * @return
	 */
	public static Object failed(String msg) {
		return result(false, msg, null);
	}
	
	/**
	 * 前台返回结果集
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param result 查询状态
	 * @return
	 */
	public static Object failed(Object data, String msg) {
		return result(false, msg, data);
	}
	
	/**
	 * 前台返回结果集
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param result 查询状态
	 * @return
	 */
	public static Object result(boolean result) {
		return result(result, "", "");
	}
	
	/**
	 * 前台返回结果集
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param result 查询状态
	 * @param msg 结果消息
	 * @param data 数据结果集
	 * @return
	 */
	public static Object result(boolean result, String msg, Object data) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(RESULT_KEY, result);
		resultMap.put(MSG_KEY, msg);
		resultMap.put(DATA_KEY, data);
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Object result(boolean result, String msg, Object data, Integer totalNum) {
		Map<String, Object> resultMap = (Map<String, Object>) result(result, msg, data);
		resultMap.put(TOTAL_NUM, totalNum);
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public static Object success(Object data, Integer totalNum, Map<String, Object> attachMap) {
		Map<String, Object> resultMap = (Map<String, Object>) result(true, "", data, totalNum);
		resultMap.putAll(attachMap);
		return resultMap;
	}
}
