package com.wangle.spider.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import com.wangle.spider.main.Project;
import com.wangle.spider.main.PageTool;

public class TestPageTool {

	String host="http://210.42.121.241";

	String str="";
	@Before
	public void before(){
		try{
			File file=new File("F:/1.html");
			BufferedReader read=new BufferedReader(new FileReader(file));
			String temp=null;
			while((temp=read.readLine())!=null){
				str=str+temp+"\n";
			}
			read.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	@Test
	public void testGetGradeList() {
		PageTool pageTool=new PageTool(str,"www.baidu.com");
		LinkedList<Project> list=pageTool.getProjectList();
		for(int i=0;i<list.size();i++){
			//System.out.println(list.get(i));
		}
	}

	@Test
	public void testGetLinkList() {
		PageTool pageTool=new PageTool(str,host);
		LinkedList<String> list=pageTool.getLinkList();
		for(int i=0;i<list.size();i++){
			//System.out.println(list.get(i));
		}
	}

	@Test
	public void testGetResourceList() {
		PageTool pageTool=new PageTool(str,host);
		LinkedList<String> list=pageTool.getResourceList();
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
	}

}
