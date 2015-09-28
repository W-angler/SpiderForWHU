package com.wangle.spider.main;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
/**
 * 页面工具类，采用了第三方的HTMLParser，
 * 主要是解析网页内容，获取课程列表，以及爬虫所需的地址链接、网页相关的资源文件地址，
 * 默认网页的解析的编码格式为UTF-8
 * @author wangle
 * @date 2015.09.22
 */
public class PageTool {
	private String result;				//所要解析的网页的字符串
	private String charset;				//字符解析的编码格式
	private String host;				//当前主机名，用于将相对路径转换为url

	public PageTool(String result,String host){
		this.result=result;
		this.charset="UTF-8";
		this.host=host;
	}
	public PageTool(String result,String host,String charset){
		this.result=result;
		this.charset=charset;
		this.host=host;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * 设置解析的字符编码
	 * @param charset 字符编码
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 获取课程列表
	 * @return 保存了课程列表的List
	 */
	public LinkedList<Project> getProjectList(){
		LinkedList<Project> projectList=new LinkedList<Project>();
		try {
			Parser parser=Parser.createParser(result, charset);
			//获取tr节点过滤器
			NodeFilter filter = new TagNameFilter ("tr");
			//获得所有的tr节点
			NodeList trNodes = parser.extractAllNodesThatMatch(filter);
			//存储tr节点下所有的td节点
			List<Node> list=new LinkedList<Node>();
			if(trNodes!=null) {
				for (int i = 0; i < trNodes.size(); i++) {
					Node trNode = (Node) trNodes.elementAt(i);
					//获取该tr节点的子节点，即td节点
					NodeList tdNodes=trNode.getChildren();
					if(tdNodes!=null){
						for(int j=0;j<tdNodes.size();j++){
							Node node=(Node)tdNodes.elementAt(j);
							//如果该节点是td节点，则存储进list里，每一门课共有11个tr节点
							//其中最后一个是课程操作，内容是一堆的空格，抛弃，第10个是成绩，无成绩的节点内容为空
							if(node instanceof TableColumn){
								list.add(node);
							}
						}
					}
				}
				//将list模拟为矩阵，将每行的信息存储进Project对象并添加到projectList
				for(int k=0;k<list.size()/11;k++){
					String head=list.get(k*11+0).toPlainTextString();			//课头号
					String name=list.get(k*11+1).toPlainTextString();			//课程名称
					String type=list.get(k*11+2).toPlainTextString();			//课程类型
					String credit=list.get(k*11+3).toPlainTextString();			//学分	
					String teacher=list.get(k*11+4).toPlainTextString();		//教师
					String academy=list.get(k*11+5).toPlainTextString();		//授课学院
					String learnType=list.get(k*11+6).toPlainTextString();		//学习类型
					String year=list.get(k*11+7).toPlainTextString();			//学年
					String term=list.get(k*11+8).toPlainTextString();			//学期
					String grade=list.get(k*11+9).toPlainTextString();			//成绩
					projectList.add(new Project(head, name,
							type, credit,
							teacher, academy,
							learnType, year,
							term, grade));
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return projectList;
	}
	/**
	 * 解析整个网页，获取可见的所有链接（意思就是无法获取ajax等中的链接）
	 * @return 存储了整个网页中所有可见链接的List
	 */
	public LinkedList<String> getLinkList(){
		LinkedList<String> linkList=new LinkedList<String>();
		try{
			Parser parser=Parser.createParser(result, charset);
			//创建两个过滤器，过滤<a>,<iframe>节点
			NodeFilter a=new TagNameFilter("a"); 
			NodeFilter iframe=new TagNameFilter("iframe");
			//或运算过滤器
			OrFilter or=new OrFilter(a,iframe);
			//开始过滤
			NodeList nodeList=parser.extractAllNodesThatMatch(or);
			if(nodeList!=null){
				for(int i=0;i<nodeList.size();i++){
					Node node=nodeList.elementAt(i);
					//如果是<a>标签
					if(node instanceof LinkTag){
						//获取href属性
						String href=((LinkTag) node).getAttribute("href");
						//正则匹配，看是否是相对路径
						Pattern pattern=Pattern.compile(host);
						Matcher matcher=pattern.matcher(href);
						if(!matcher.find()){
							//如果是相对路径，则将路径转换为有效的url,协议默认为http,将起始位置的(../)(./)(/)替换掉
							//像是../../这样的……呃呃呃……
							href="http://"+host+"/"+href.replaceAll("^\\.\\./|^\\./|^/", "");
							linkList.add(href);
						}
						else{
							linkList.add(href);
						}
					}
					//否则就是<iframe>标签
					else{
						//获取src属性
						String src=((TagNode) node).getAttribute("src");
						Pattern pattern=Pattern.compile(host);
						Matcher matcher=pattern.matcher(src);
						if(!matcher.find()){
							//同上
							src="http://"+host+"/"+src.replaceAll("^\\.\\./|^\\./|^/", "");
							linkList.add(src);
						}
						else{
							linkList.add(src);
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return linkList;
	}

	/**
	 * 获取与所解析的网页相关的资源文件，如css、js
	 * @return 存储了整个网页中所有相关资源的的List
	 */
	public LinkedList<String> getResourceList(){
		LinkedList<String> resourceList=new LinkedList<String>();
		try{
			Parser parser=Parser.createParser(result, charset);
			//<script>,<link>节点
			NodeFilter script=new TagNameFilter("script");
			NodeFilter link=new TagNameFilter("link");
			//或运算过滤器
			OrFilter or=new OrFilter(link,script);
			//开始过滤
			NodeList nodeList=parser.extractAllNodesThatMatch(or);
			if(nodeList!=null){
				for(int i=0;i<nodeList.size();i++){
					Node node=nodeList.elementAt(i);
					//如果是<script>标签
					if(node instanceof ScriptTag){
						//获取src属性
						String src=((ScriptTag) node).getAttribute("src");
						if(src!=null){
							Pattern pattern=Pattern.compile("http://");
							Matcher matcher=pattern.matcher(src);
							if(!matcher.find()){
								src="http://"+host+"/"+src.replaceAll("^\\.\\./|^\\./|^/", "");
								resourceList.add(src);
							}
							else{
								resourceList.add(src);
							}
						}
					}
					//否则就是<link>标签
					else{
						//获取href属性
						String href=((TagNode) node).getAttribute("href");
						Pattern pattern=Pattern.compile("http://");
						Matcher matcher=pattern.matcher(href);
						if(!matcher.find()){
							//同上
							href="http://"+host+"/"+href.replaceAll("^\\.\\./|^\\./|^/", "");
							resourceList.add(href);
						}
						resourceList.add(href);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resourceList;
	}
	public LinkedList<String> getImageList(){
		LinkedList<String> imageList=new LinkedList<String>();
		try{
			Parser parser=Parser.createParser(result, charset);
			//过滤<img>
			NodeFilter img=new TagNameFilter("img");
			//获取含有背景的节点
			NodeFilter bg=new HasAttributeFilter("background");
			NodeFilter or=new OrFilter(img,bg);
			//开始过滤
			NodeList nodeList=parser.extractAllNodesThatMatch(or);
			if(nodeList!=null){
				for(int i=0;i<nodeList.size();i++){
					Node node=nodeList.elementAt(i);
					//如果是<img>标签
					if(node instanceof ImageTag){
						//获取src属性
						String href=((ImageTag) node).getAttribute("src");
						Pattern pattern=Pattern.compile("http://");
						Matcher matcher=pattern.matcher(href);
						if(!matcher.find()){
							href="http://"+host+"/"+href.replaceAll("^\\.\\./|^\\./|^/", "");
							imageList.add(href);
						}
						else{
							imageList.add(href);
						}
					}
					else{
						//获取backgound属性
						String backgound=((ImageTag) node).getAttribute("backgound");
						Pattern pattern=Pattern.compile("http://");
						Matcher matcher=pattern.matcher(backgound);
						if(!matcher.find()){
							backgound="http://"+host+"/"+backgound.replaceAll("^\\.\\./|^\\./|^/", "");
							imageList.add(backgound);
						}
						else{
							imageList.add(backgound);
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return imageList;
	}
}