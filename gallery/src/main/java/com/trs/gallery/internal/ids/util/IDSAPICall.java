package com.trs.gallery.internal.ids.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trs.gallery.internal.component.IDSConfig;
import com.trs.gallery.util.SpringUtils;
import com.trs.gallery.util.StringUtil;


public class IDSAPICall {
	
	private static IDSConfig idsConfig = SpringUtils.getBean(IDSConfig.class);

	public static final String IDS_URL = idsConfig.getUrl() + "/service?idsServiceType=remoteapi";
	/**
	 * 加密密钥
	 */
	public static final String SECRETKEY = idsConfig.getSecretkey();
	/**
	 * 消息摘要加密算法
	 */
	public static final String DIGESTALGORITHM = idsConfig.getDigestalgorithm();

	/**
	 * 协作应用名
	 */
	public static final String APP_NAME = idsConfig.getAppName();

	public static String Call(String method, String data, String returnType) throws Exception {
		if (StringUtil.isEmpty(returnType)) {
			returnType = "json";
		}
		IDSAPIClient client = new IDSAPIClient(IDS_URL + "&method=" + method, SECRETKEY, DIGESTALGORITHM, returnType);
		return client.processor(APP_NAME, data);
	}

	public static String Call2Json(String method, String data) throws Exception {
		return Call(method, data, "json");
	}

	public static String Call2Xml(String method, String data) throws Exception {
		return Call(method, data, "xml");
	}

	public static String Call(String method, Map<String, String> params, String returnType) throws Exception {
		String data = "";
		if (null != params && !params.isEmpty()) {
			for (Iterator<Entry<String, String>> iterator = params.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String> v = (Entry<String, String>) iterator.next();
				if (StringUtil.isEmpty(data)) {
					data = v.getKey() + "=" + v.getValue();
				} else {
					data += "&" + v.getKey() + "=" + v.getValue();
				}
			}
		}
		return Call(method, data, returnType);
	}

	public static String Call2Json(String method, Map<String, String> params) throws Exception {
		return Call(method, params, "json");
	}
	
	public static String Call2Xml(String method, Map<String, String> params) throws Exception {
		return Call(method, params, "xml");
	}
	

	public static String[] Call2Fields(String method, String data, String[] fieldNames) throws Exception {
		String str[] = null;
		List<String> list = new ArrayList<String>();  
		String result = Call(method, data, "json");
		if(result!=null){
			JSONObject oJSONObject = JSONObject.parseObject(result);
			if(oJSONObject.getInteger("code")==0){
				for (int i=0;i<fieldNames.length;i++){
					String fieldName = fieldNames[i];
					if(fieldName.indexOf("IDSEXT")==0){
						JSONArray oJSONArray = oJSONObject.getJSONArray("customs");
						for (int j=0;j<oJSONArray.size();j++){
							JSONObject oJSONObject2 = oJSONArray.getJSONObject(j);
							if(oJSONObject2.getString("fieldName").equals(fieldName)){
								list.add(oJSONObject2.getString("value"));
							}
						}
					}else{
						JSONObject oJSONObject3 = oJSONObject.getJSONObject("entry");
						list.add(oJSONObject3.getString(fieldName));
					}
				}
			}
		}
		str = list.toArray(new String[1]);
		return str;
	}
	
	public static void main(String[] args) throws Exception {

	}
}
