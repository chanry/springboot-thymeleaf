package com.trs.gallery.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @className DateUtil.java
 * @author chenli
 * @date 2017年5月8日 上午11:22:58
 */
public class DateUtil {
	/**
	 * 根据当前日期获取未来几天的日期（包含今日）
	 * 
	 * @param mdate
	 * @return
	 */
	public static List<Date> getRecentlyDateList(Date date, int days) {
		List<Date> list = new ArrayList<Date>();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		list.add(c.getTime());
		for (int i = 0; i < days - 1; i++) {
			c.add(Calendar.DAY_OF_YEAR, 1);
			list.add(c.getTime());
		}
		return list;
	}
}
