package com.w_angler.spider.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 页面
 * 主要是解析网页内容，获取课程列表，以及爬虫所需的地址链接、网页相关的资源文件地址，
 * 默认网页的解析的编码格式为UTF-8
 * @author wangle
 * @date 2015.09.22
 */
public class Page {
	private String host;				//当前主机名，用于将相对路径转换为url
	private Document doc;				//html
	Pattern pattern=Pattern.compile("http://");

	public Page(String result,String host){
		this.host=host;
		doc=Jsoup.parse(result,host);
	}

	public String getHost() {
		return host;
	}

	/**
	 * 获取课程列表
	 * @return 保存了课程列表的List
	 */
	public List<Subject> getSubjectList(){
		List<Subject> projectList=new ArrayList<>();
		Elements elements=doc.getElementsByTag("td");
		//将list模拟为矩阵，将每行的信息存储进Project对象并添加到projectList
		for(int i=0;i<elements.size()/11;i++){
			String head=elements.get(i*11+0).text();//课头号
			String name=elements.get(i*11+1).text();//课程名称
			String type=elements.get(i*11+2).text();//课程类型
			String credit=elements.get(i*11+3).text();//学分	
			String teacher=elements.get(i*11+4).text();//教师
			String academy=elements.get(i*11+5).text();//授课学院
			String learnType=elements.get(i*11+6).text();//学习类型
			String year=elements.get(i*11+7).text();//学年
			String term=elements.get(i*11+8).text();//学期
			String grade=elements.get(i*11+9).text();//成绩
			projectList.add(
					new Subject(head, name,
							type, credit,
							teacher, academy,
							learnType, year,
							term, grade));
		}
		return projectList;
	}
	/**
	 * 解析整个网页，获取可见的所有链接（意思就是无法获取ajax等中的链接）
	 * @return 存储了整个网页中所有可见链接的List
	 */
	public List<String> getLinkList(){
		List<String> linkList=new ArrayList<>();
		Elements aElements=doc.getElementsByTag("a");
		Elements iframeElements=doc.getElementsByTag("iframe");
		aElements.forEach((e)->{
			String href=e.attr("href");
			if(href!=null&&!href.equals("")){
				Matcher matcher=pattern.matcher(href);
				if(!matcher.find()){
					href="http://"+host+"/"+href.replaceAll("^\\.\\./|^\\./|^/", "");
					linkList.add(href);
				}
				else{
					linkList.add(href);
				}
			}
		});
		iframeElements.forEach((e)->{
			String src=e.attr("src");
			if(src!=null&&!src.equals("")){
				Matcher matcher=pattern.matcher(src);
				if(!matcher.find()){
					src="http://"+host+"/"+src.replaceAll("^\\.\\./|^\\./|^/", "");
					linkList.add(src);
				}
				else{
					linkList.add(src);
				}
			}
		});
		return linkList;
	}

	/**
	 * 获取与所解析的网页相关的资源文件，如css、js
	 * @return 存储了整个网页中所有相关资源的的List
	 */
	public List<String> getResourceList(){
		List<String> resourceList=new ArrayList<>();
		Elements scriptElements=doc.getElementsByTag("script");
		Elements linkElements=doc.getElementsByTag("link");
		scriptElements.forEach((e)->{
			String src=e.attr("src");
			if(src!=null&&!src.equals("")){
				Matcher matcher=pattern.matcher(src);
				if(!matcher.find()){
					src="http://"+host+"/"+src.replaceAll("^\\.\\./|^\\./|^/", "");
					resourceList.add(src);
				}
				else{
					resourceList.add(src);
				}
			}
		});
		linkElements.forEach((e)->{
			String href=e.attr("href");
			if(href!=null&&!href.equals("")){
				Matcher matcher=pattern.matcher(href);
				if(!matcher.find()){
					href="http://"+host+"/"+href.replaceAll("^\\.\\./|^\\./|^/", "");
					resourceList.add(href);
				}
				else{
					resourceList.add(href);
				}
			}
		});
		return resourceList;
	}
	public List<String> getImageList(){
		List<String> imageList=new ArrayList<>();
		Elements img=doc.getElementsByTag("img");
		Elements bg=doc.select("[background]");
		img.forEach((e)->{
			String src=e.attr("src");
			if(src!=null&&!src.equals("")){
				Matcher matcher=pattern.matcher(src);
				if(!matcher.find()){
					src="http://"+host+"/"+src.replaceAll("^\\.\\./|^\\./|^/", "");
					imageList.add(src);
				}
				else{
					imageList.add(src);
				}
			}
		});
		bg.forEach((e)->{
			String background=e.absUrl("backgound");
			if(background!=null&&!background.equals("")){
				Matcher matcher=pattern.matcher(background);
				if(!matcher.find()){
					background="http://"+host+"/"+background.replaceAll("^\\.\\./|^\\./|^/", "");
					imageList.add(background);
				}
				else{
					imageList.add(background);
				}
			}
		});
		return imageList;
	}
}