package com.trs.gallery.internal.component;

import java.util.HashMap;
import java.util.Map;

import com.trs.gallery.mapper.custom.DocumentDao;
import com.trs.gallery.util.SpringUtils;

public class ChannelDataPath {
	
	private static Map<Integer, Map<String, String>> chnlMap = null;
	
	private static Map<Integer, String> docPath = new HashMap<>();
	
	public static Map<Integer, Map<String, String>> getChnlMap() {
		if (null == chnlMap) {
			DocumentDao documentDao = (DocumentDao) SpringUtils.getBean("documentDao");
			chnlMap = documentDao.findChnlDataPath();
		}
		return chnlMap;
	}
	
	public static String getValue(Integer key) {
		return getChnlMap().get(key).get("DATAPATH");
	}
	
	public static String getDocPathValue(Integer key) {
		return docPath.get(key);
	}
	
	public static void setDocPathValue(Integer key, String value) {
		docPath.put(key, value);
	}
	
}
