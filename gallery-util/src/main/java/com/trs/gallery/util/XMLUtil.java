package com.trs.gallery.util;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	
	public static Document load(File file) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(file); // 读取XML文件,获得document对象
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
	
	public static String getDocumentSql(String nodeName, Document document) {
		Element rootElm = document.getRootElement(); 
		Element memberElm=rootElm.element(nodeName);
		return  memberElm.getText();
	}
}
