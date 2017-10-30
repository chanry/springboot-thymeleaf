package com.trs.gallery.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class ExcelUtil {

	public static String NO_DEFINE = "no_define";// 未定义的字段
	public static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";// 默认日期格式
	public static int DEFAULT_COLOUMN_WIDTH = 17;

	/**
	 * 根据excel文件路径解析出数据列表（使用POIjar包）
	 * 
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param path
	 */
	public static Map<Integer, Map<String, Object>> getExcelObjectListByPOI(String path) {
		Map<Integer, Map<String, Object>> resultMap = new LinkedHashMap<>();
		try {
			File excelFile = new File(path); // 创建文件对象
			FileInputStream is = new FileInputStream(excelFile); // 文件流
			Workbook workbook = WorkbookFactory.create(is);
			resultMap = getExcelObjectListByWorkbook(workbook);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public static Map<Integer, Map<String, Object>> getExcelObjectListByPOI(URL fileUrl) throws IOException {
		Map<Integer, Map<String, Object>> resultMap = new LinkedHashMap<>();
		URLConnection conn = fileUrl.openConnection();
		// 以流的形式下载文件
		InputStream inStream = conn.getInputStream();
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(inStream);
			resultMap = getExcelObjectListByWorkbook(workbook);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	private static Map<Integer, Map<String, Object>> getExcelObjectListByWorkbook(Workbook workbook) {
		Map<Integer, Map<String, Object>> resultMap = new LinkedHashMap<>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			Map<String, Object> sheetMap = new HashMap<>();
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Sheet sheet = workbook.getSheetAt(i);
			sheetMap.put("sheetName", sheet.getSheetName());
			JSONObject jsonObject = new JSONObject();
			for (int j = 0; j <= sheet.getPhysicalNumberOfRows() - 1; j++) {
				Row row = sheet.getRow(j);
				Map<String, String> map = new LinkedHashMap<String, String>();
				for (int k = 0; k < row.getLastCellNum(); k++) {
					map.put(getExcelColumnName(k), getStringCellValue(row.getCell(k)));
					if (j == sheet.getPhysicalNumberOfRows() - 1)
						jsonObject.put(getExcelColumnName(k), sheet.getColumnWidth(k));
				}
				if (!org.springframework.util.CollectionUtils.isEmpty(map))
					list.add(map);
				map = null;
			}
			if (CollectionUtils.isNotEmpty(list)) {
				sheetMap.put("data", list);
				sheetMap.put("fieldStyle", jsonObject.toString());
				resultMap.put(i, sheetMap);
			}
			list = null;
			sheetMap = null;
		}
		return resultMap;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private static String getStringCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
				SimpleDateFormat sdf = null;
				if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
					sdf = new SimpleDateFormat("HH:mm");
				} else if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm")) {// 日期
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				} else {
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = cell.getDateCellValue();
				strCell = sdf.format(date);
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				strCell = sdf.format(date);
			} else {
				double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				DecimalFormat format = new DecimalFormat();
				String temp = style.getDataFormatString();
				// 单元格设置成常规
				if (temp.equals("General")) {
					format.applyPattern("#");
				}
				strCell = format.format(value);
			}
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	/**
	 * 设置单元格列名
	 * 
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param str
	 * @param col
	 */
	private static void setExcelColumnName(StringBuilder str, int col) {
		str.append("FIELD_" + col);
	}

	/**
	 * 根据单元格列数序号得到列名
	 * 
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param col
	 * @return
	 */
	public static String getExcelColumnName(int col) {
		StringBuilder str = new StringBuilder();
		setExcelColumnName(str, col);
		return str.toString();
	}

	/**
	 * 获取指定路径json文件的json数据对象
	 * 
	 * @param filePath
	 * @return
	 */
	public static JSONObject getJsonFileData(String filePath) {
		JSONObject jsonObject = new JSONObject();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			jsonObject = JSONObject.parseObject(stringBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 导出Excel 2007 OOXML (.xlsx)格式
	 * 
	 * @param title
	 *            标题行
	 * @param headMap
	 *            属性-列头
	 * @param jsonArray
	 *            数据集
	 * @param datePattern
	 *            日期格式，传null值则默认 年月日
	 * @param colWidth
	 *            列宽 默认 至少17个字节
	 * @param out
	 *            输出流
	 * @param jsonKey
	 *            如果指定获取json里某个key的对象          
	 */
	public static void exportExcelX(String title, Map<String, String> headMap, JSONArray jsonArray, 
			String datePattern, int colWidth, OutputStream out, String... jsonKey) {
		if (datePattern == null)
			datePattern = DEFAULT_DATE_PATTERN;
		// 声明一个工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook(1000);// 缓存
		workbook.setCompressTempFiles(true);
		// 表头样式
		CellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font titleFont = workbook.createFont();
		titleFont.setFontHeightInPoints((short) 20);
		titleFont.setBoldweight((short) 700);
		titleStyle.setFont(titleFont);
		// 列头样式
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillPattern(HSSFCellStyle.NO_FILL);
		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerStyle.setFont(headerFont);
		// 单元格样式
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
		Font cellFont = workbook.createFont();
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(cellFont);
		// 生成一个(带标题)表格
		SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet();
		// 设置列宽
		int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
		int[] arrColWidth = new int[headMap.size()];
		// 产生表格标题行,以及设置列宽
		String[] properties = new String[headMap.size()];
		String[] headers = new String[headMap.size()];
		int ii = 0;
		for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
			String fieldName = iter.next();

			properties[ii] = fieldName;
			headers[ii] = headMap.get(fieldName);

			int bytes = fieldName.getBytes().length;
			arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
			sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
			ii++;
		}
		// 遍历集合数据，产生数据行
		int rowIndex = 0;
		for (Object obj : jsonArray) {
			if (rowIndex == 65535 || rowIndex == 0) {
				if (rowIndex != 0) {
					sheet = (SXSSFSheet) workbook.createSheet();// 如果数据超过了，则在第二页显示
				}

				SXSSFRow titleRow = (SXSSFRow) sheet.createRow(0);// 表头
																	// rowIndex=0
				titleRow.createCell(0).setCellValue(title);
				titleRow.getCell(0).setCellStyle(titleStyle);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

				SXSSFRow headerRow = (SXSSFRow) sheet.createRow(1); // 列头
																	// rowIndex
																	// =1
				for (int i = 0; i < headers.length; i++) {
					headerRow.createCell(i).setCellValue(headers[i]);
					headerRow.getCell(i).setCellStyle(headerStyle);

				}
				rowIndex = 2;// 数据内容从 rowIndex=2开始
			}
			JSONObject rowData = JSON.parseObject(obj.toString());
			if (null != jsonKey && jsonKey.length > 0) {
				for (String key : jsonKey) {
					JSONObject json = (JSONObject) JSON.parseObject(obj.toString()).get(key);
					rowData.putAll(json);
				}
			}
			SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);
			for (int i = 0; i < properties.length; i++) {
				SXSSFCell newCell = (SXSSFCell) dataRow.createCell(i);

				Object o = rowData.get(properties[i]);
				if (o == null || o.toString() == "null") {
					newCell.setCellValue("");
				} else if (o instanceof Date) {
					newCell.setCellValue(new SimpleDateFormat(datePattern).format(o));
				} else if (o instanceof Float || o instanceof Double || o instanceof Integer) {
					newCell.setCellValue(new BigDecimal(o.toString()).doubleValue());
				} else {
					o.getClass().getName();
					newCell.setCellValue(o.toString());
				}
				newCell.setCellStyle(cellStyle);
			}
			rowIndex++;
		}
		// 自动调整宽度
		//for (int i = 0; i < headers.length; i++) { sheet.autoSizeColumn(i); }
		 
		try {
			workbook.write(out);
			workbook.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		// getExcelObjectListByPOI("C:\\Users\\chenli\\Downloads\\公园活动信息%282%29.xls");
		// System.out.println(getJsonFileData("C:\\Users\\chenli\\Desktop\\厦门天地图API梳理\\json\\机关单位.json").toString());
		String filePath = "C:\\Users\\chenli\\Desktop\\厦门天地图API梳理\\json\\机关单位.json";
		JSONArray jsonArray = getJsonFileData(filePath).getJSONArray("features");
		File file = new File("C:\\Users\\chenli\\Desktop\\test.xlsx");
		OutputStream outputStream = new FileOutputStream(file);
		Map<String, String> headMap = new LinkedHashMap<>();
		headMap.put("OBJECTID", "单位编号");
		headMap.put("NAME", "单位名称");
		headMap.put("FLMC", "单位级别");
		headMap.put("PTMC", "单位类型");
		headMap.put("x", "x坐标");
		headMap.put("y", "y坐标");
		exportExcelX("厦门市机关单位汇总", headMap, jsonArray, null, 25, outputStream, new String[]{"attributes", "geometry"});
	}
}
