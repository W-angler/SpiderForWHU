package com.wangle.spider.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import org.junit.Test;

import com.wangle.spider.main.AnalyseProject;
import com.wangle.spider.main.PageTool;
import com.wangle.spider.main.Project;

public class TestAnalyseProject {

	String host="http://210.42.121.241";
	
	@Test
	public void testGetWeightedGPA() {
		try {
			File file=new File("F:/1.html");
			BufferedReader read;
			read = new BufferedReader(new FileReader(file));
			String str="";
			String temp=null;
			while((temp=read.readLine())!=null){
				str=str+temp+"\n";
			}
			read.close();
			PageTool pageTool=new PageTool(str,host);
			LinkedList<Project> list=pageTool.getProjectList();
			AnalyseProject analyse=new AnalyseProject(list);
			System.out.printf("%.5f",analyse.getWeightedGPA());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetWeightedGrade() {
		try {
			File file=new File("F:/1.html");
			BufferedReader read;
			read = new BufferedReader(new FileReader(file));
			String str="";
			String temp=null;
			while((temp=read.readLine())!=null){
				str=str+temp+"\n";
			}
			read.close();
			PageTool pageTool=new PageTool(str,host);
			LinkedList<Project> list=pageTool.getProjectList();
			AnalyseProject analyse=new AnalyseProject(list);
			System.out.printf("%.5f",analyse.getWeightedGrade());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
