package com.wangle.spider.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;

import com.wangle.spider.main.Excel;
import com.wangle.spider.main.Project;
import com.wangle.spider.main.PageTool;

public class TestExcel {

	String host="http://210.42.121.241";
	
	@Test
	public void testExportExcel() {
		try{
			Excel<Project> ex = new Excel<Project>();
			String[] headers = {"课头号","课程名称",
					"课程类型","学分",
					"教师","授课学院",
					"学习类型","学年",
					"学期","成绩"};
			File file=new File("F:/1.html");
			BufferedReader read=new BufferedReader(new FileReader(file));
			String str="";
			String temp=null;
			while((temp=read.readLine())!=null){
				str=str+temp+"\n";
			}
			read.close();
			PageTool pageTool=new PageTool(str,host);
			LinkedList<Project> list=pageTool.getProjectList();

			System.out.println(list);
			ex.exportExcel(headers, list, "F://a.xls");
			System.out.println("excel导出成功！");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}
