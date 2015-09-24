package com.wangle.spider.main;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * ����POI��javaBean����ΪEXCEL�ĵ� 
 * ������,���ܲ����������͵�javaBean(booleanֵ���������,����boolean�������Ե�getter������isXXX������getXXX)
 * ���䲿�ֲ�����CSDN��һƪ����
 * @author wangle
 * @date 2015.9.21
 * @param <T>
 */

public class Excel<T> {
	private String valueOfTrue;			//boolean��������ֵΪtrueʱ��������ַ�ֵ
	private String valueOfFalse;		//boolean��������ֵΪfalseʱ��������ַ�ֵ
	
	public Excel(){
		this.valueOfTrue="true";
		this.valueOfFalse="false";
	}
	public Excel(String valOfTrue,String valOfFalse){
		this.setBoolValue(valOfTrue,valOfFalse);
	}
	
	public void setBoolValue(String valOfTure,String valOfFlase){
		this.valueOfTrue=valOfTure;
		this.valueOfFalse=valOfFlase;
	}
	/**
	 * 
	 * @param title ��������
	 * @param headers ���������������
	 * @param data ��Ҫ��ʾ�����ݼ���
	 * @param path �������ļ�·��
	 * @param time ����ʱ�������ʽ��Ĭ��Ϊ"yyy-MM-dd"
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(String title, String[] headers,
			Collection<T> data, String path,
			String time){

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		//���Ĭ���п��
		sheet.setDefaultColumnWidth((short) 15);

		/*
		 * ���õ�һ�У���ͷ����ʽ��
		 */
		HSSFCellStyle head = workbook.createCellStyle();
		//������ʽ
		head.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
		head.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		head.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		head.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		head.setBorderRight(HSSFCellStyle.BORDER_THIN);
		head.setBorderTop(HSSFCellStyle.BORDER_THIN);
		head.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// ��������
		HSSFFont font1 = workbook.createFont();
		font1.setColor(HSSFColor.BLACK.index);
		font1.setFontHeightInPoints((short) 14);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//Ӧ������
		head.setFont(font1);
		/*
		 * ���������е���ʽ
		 */
		HSSFCellStyle body = workbook.createCellStyle();
		//������ʽ
		body.setFillForegroundColor(HSSFColor.WHITE.index);
		body.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		body.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		body.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		body.setBorderRight(HSSFCellStyle.BORDER_THIN);
		body.setBorderTop(HSSFCellStyle.BORDER_THIN);
		body.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		body.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// ������һ������
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//Ӧ������
		body.setFont(font2);

		// ������������
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(head);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// �����������ݣ�����������
		Iterator<T> it = data.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// ���÷�����ƣ�����javabean���Ե��Ⱥ�˳�򣬶�̬����getXxx()�����õ�����ֵ
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
					// �ж�ֵ�����ͺ����ǿ������ת��
					String textValue = null;
					if (value instanceof Boolean) {
						//����ֵ�Ļ������������������Ů�������Ƿ�...ɶ��
						if((boolean)value){
							textValue=this.valueOfTrue;
						}
						else{
							textValue=this.valueOfFalse;
						}
					}
					else if (value instanceof Date) {
						Date date = (Date) value;
						//�������ڸ�ʽ
						SimpleDateFormat sdf = new SimpleDateFormat(time);
						textValue = sdf.format(date);
					}
					//ͼƬ���ݣ�ת��Ϊbyte[]
					else if (value instanceof byte[]) {
						// ��ͼƬʱ�������и�Ϊ60px;
						row.setHeightInPoints(60);
						// ����ͼƬ�����п��Ϊ80px
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
					}
					else {
						// �����������Ͷ������ַ����򵥴���
						textValue = value.toString();
					}
					if (textValue != null) {
						//����ƥ�䣬�ж��Ƿ�ȫ������
						Pattern p = Pattern.compile("\\d+(\\.\\d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							//��������֣�ת��Ϊdouble
							cell.setCellValue(Double.parseDouble(textValue));
						}
						else {
							//�������ı�����
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
		try {
			File file=new File(path);
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			OutputStream out = new FileOutputStream(file);
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

	//�����ñ����⣬��ΪĬ�ϵ�sheet
	/**
	 * 
	 * @param headers ���������������
	 * @param data ��Ҫ��ʾ�����ݼ���
	 * @param path �������ļ�·��
	 * @param time ����ʱ�������ʽ��Ĭ��Ϊ"yyy-MM-dd"
	 */
	public void exportExcel(String[] headers,
			Collection<T> data, String path,
			String time){
		this.exportExcel("sheet", headers, data, path, time);
	}

	//�����ñ�����,Ĭ�����ڸ�ʽ
	/**
	 * 
	 * @param headers ���������������
	 * @param data ��Ҫ��ʾ�����ݼ���
	 * @param path �������ļ�·��
	 */
	public void exportExcel(String[] headers,Collection<T> data, String path){
		this.exportExcel(headers, data, path, "yyy-MM-dd");
	}

	//������
	/**
	 * 
	 * @param title ��������
	 * @param headers ���������������
	 * @param data ��Ҫ��ʾ�����ݼ���
	 * @param path �������ļ�·��
	 */
	public void exportExcel(String title,String[] headers,Collection<T> data, String path){
		this.exportExcel(title,headers, data, path);
	}

	public String getValueOfTrue() {
		return valueOfTrue;
	}

	public String getValueOfFalse() {
		return valueOfFalse;
	}

}