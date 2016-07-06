package com.w_angler.spider.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 利用POI将POJO导出为EXCEL文档 
 * @author wangle
 * @date 2015.9.21
 * @param <T>
 */

public class Excel<T> {
	private static String valueOfTrue="true";//boolean类型属性值为true时所代表的字符值
	private static String valueOfFalse="false";//boolean类型属性值为false时所代表的字符值
	private static String timeFormat="yyy-MM-dd";//时间格式
	private Excel(){}

	public static void setBoolValue(String valOfTure,String valOfFlase){
		Excel.valueOfTrue=valOfTure;
		Excel.valueOfFalse=valOfFlase;
	}
	public static void setTimeFormat(String timeFormat){
		Excel.timeFormat=timeFormat;
	}
	/**
	 * 
	 * @param headers 表格属性列名数组
	 * @param data 需要显示的数据集合
	 * @param path 导出的文件路径
	 */
	public static <T> void export(String[] headers,Collection<T> data, String path){
		Excel.export("sheet", headers, data, path);
	}
	/**
	 * 
	 * @param title 表格标题名
	 * @param headers 表格属性列名数组
	 * @param data 需要显示的数据集合
	 * @param path 导出的文件路径
	 */
	@SuppressWarnings("deprecation")
	public static <T> void export(String title, String[] headers,Collection<T> data, String path){
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		//表格默认列宽度
		sheet.setDefaultColumnWidth((short) 15);
		/*
		 * 设置第一行（表头的样式）
		 */
		HSSFCellStyle head = workbook.createCellStyle();
		//设置样式
		head.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		head.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		head.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		head.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		head.setBorderRight(HSSFCellStyle.BORDER_THIN);
		head.setBorderTop(HSSFCellStyle.BORDER_THIN);
		head.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成字体
		HSSFFont font1 = workbook.createFont();
		font1.setColor(HSSFColor.BLACK.index);
		font1.setFontHeightInPoints((short) 14);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//应用字体
		head.setFont(font1);
		/*
		 * 设置数据行的样式
		 */
		HSSFCellStyle body = workbook.createCellStyle();
		//设置样式
		body.setFillForegroundColor(HSSFColor.WHITE.index);
		body.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		body.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		body.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		body.setBorderRight(HSSFCellStyle.BORDER_THIN);
		body.setBorderTop(HSSFCellStyle.BORDER_THIN);
		body.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		body.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//应用字体
		body.setFont(font2);
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			row.createCell(1, 0);
			cell.setCellStyle(head);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = data.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射机制，根据POJO属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(body);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				try {
					@SuppressWarnings("unchecked")
					Class<T> tClass = (Class<T>) t.getClass();
					Method getMethod = tClass.getMethod(getMethodName,new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Boolean) {
						//布尔值的话，视情况而定，“男女”，“是否”...啥的
						if((boolean)value){
							textValue=Excel.valueOfTrue;
						}
						else{
							textValue=Excel.valueOfFalse;
						}
					}
					else if (value instanceof Date) {
						Date date = (Date) value;
						//设置日期格式
						SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
						textValue = sdf.format(date);
					}
					else {
						// 其它数据类型都当作字符串简单处理
						textValue = value.toString();
					}
					if (textValue != null) {
						//正则匹配，判断是否全是数字
						if (textValue.matches("\\d+(\\.\\d+)?$")) {
							//如果是数字，转化为double
							cell.setCellValue(Double.parseDouble(textValue));
						}
						else {
							//否则当做文本处理
							HSSFRichTextString richString = new HSSFRichTextString(textValue);
							HSSFFont font3 = workbook.createFont();
							font3.setColor(HSSFColor.BLUE.index);
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		File file=new File(path);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try(OutputStream out = new FileOutputStream(file)){
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}