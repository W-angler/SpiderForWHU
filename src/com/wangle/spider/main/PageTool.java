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
 * ҳ�湤���࣬�����˵�������HTMLParser��
 * ��Ҫ�ǽ�����ҳ���ݣ���ȡ�γ��б��Լ���������ĵ�ַ���ӡ���ҳ��ص���Դ�ļ���ַ��
 * Ĭ����ҳ�Ľ����ı����ʽΪUTF-8
 * @author wangle
 * @date 2015.09.22
 */
public class PageTool {
	private String result;				//��Ҫ��������ҳ���ַ���
	private String charset;				//�ַ������ı����ʽ
	private String host;				//��ǰ�����������ڽ����·��ת��Ϊurl

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
	 * ���ý������ַ�����
	 * @param charset �ַ�����
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * ��ȡ�γ��б�
	 * @return �����˿γ��б��List
	 */
	public LinkedList<Project> getProjectList(){
		LinkedList<Project> projectList=new LinkedList<Project>();
		try {
			Parser parser=Parser.createParser(result, charset);
			//��ȡtr�ڵ������
			NodeFilter filter = new TagNameFilter ("tr");
			//������е�tr�ڵ�
			NodeList trNodes = parser.extractAllNodesThatMatch(filter);
			//�洢tr�ڵ������е�td�ڵ�
			List<Node> list=new LinkedList<Node>();
			if(trNodes!=null) {
				for (int i = 0; i < trNodes.size(); i++) {
					Node trNode = (Node) trNodes.elementAt(i);
					//��ȡ��tr�ڵ���ӽڵ㣬��td�ڵ�
					NodeList tdNodes=trNode.getChildren();
					if(tdNodes!=null){
						for(int j=0;j<tdNodes.size();j++){
							Node node=(Node)tdNodes.elementAt(j);
							//����ýڵ���td�ڵ㣬��洢��list�ÿһ�ſι���11��tr�ڵ�
							//�������һ���ǿγ̲�����������һ�ѵĿո���������10���ǳɼ����޳ɼ��Ľڵ�����Ϊ��
							if(node instanceof TableColumn){
								list.add(node);
							}
						}
					}
				}
				//��listģ��Ϊ���󣬽�ÿ�е���Ϣ�洢��Project������ӵ�projectList
				for(int k=0;k<list.size()/11;k++){
					String head=list.get(k*11+0).toPlainTextString();			//��ͷ��
					String name=list.get(k*11+1).toPlainTextString();			//�γ�����
					String type=list.get(k*11+2).toPlainTextString();			//�γ�����
					String credit=list.get(k*11+3).toPlainTextString();			//ѧ��	
					String teacher=list.get(k*11+4).toPlainTextString();		//��ʦ
					String academy=list.get(k*11+5).toPlainTextString();		//�ڿ�ѧԺ
					String learnType=list.get(k*11+6).toPlainTextString();		//ѧϰ����
					String year=list.get(k*11+7).toPlainTextString();			//ѧ��
					String term=list.get(k*11+8).toPlainTextString();			//ѧ��
					String grade=list.get(k*11+9).toPlainTextString();			//�ɼ�
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
	 * ����������ҳ����ȡ�ɼ����������ӣ���˼�����޷���ȡajax���е����ӣ�
	 * @return �洢��������ҳ�����пɼ����ӵ�List
	 */
	public LinkedList<String> getLinkList(){
		LinkedList<String> linkList=new LinkedList<String>();
		try{
			Parser parser=Parser.createParser(result, charset);
			//��������������������<a>,<iframe>�ڵ�
			NodeFilter a=new TagNameFilter("a"); 
			NodeFilter iframe=new TagNameFilter("iframe");
			//�����������
			OrFilter or=new OrFilter(a,iframe);
			//��ʼ����
			NodeList nodeList=parser.extractAllNodesThatMatch(or);
			if(nodeList!=null){
				for(int i=0;i<nodeList.size();i++){
					Node node=nodeList.elementAt(i);
					//�����<a>��ǩ
					if(node instanceof LinkTag){
						//��ȡhref����
						String href=((LinkTag) node).getAttribute("href");
						//����ƥ�䣬���Ƿ������·��
						Pattern pattern=Pattern.compile(host);
						Matcher matcher=pattern.matcher(href);
						if(!matcher.find()){
							//��������·������·��ת��Ϊ��Ч��url,Э��Ĭ��Ϊhttp,����ʼλ�õ�(../)(./)(/)�滻��
							//����../../�����ġ�������������
							href="http://"+host+"/"+href.replaceAll("^\\.\\./|^\\./|^/", "");
							linkList.add(href);
						}
						else{
							linkList.add(href);
						}
					}
					//�������<iframe>��ǩ
					else{
						//��ȡsrc����
						String src=((TagNode) node).getAttribute("src");
						Pattern pattern=Pattern.compile(host);
						Matcher matcher=pattern.matcher(src);
						if(!matcher.find()){
							//ͬ��
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
	 * ��ȡ������������ҳ��ص���Դ�ļ�����css��js
	 * @return �洢��������ҳ�����������Դ�ĵ�List
	 */
	public LinkedList<String> getResourceList(){
		LinkedList<String> resourceList=new LinkedList<String>();
		try{
			Parser parser=Parser.createParser(result, charset);
			//<script>,<link>�ڵ�
			NodeFilter script=new TagNameFilter("script");
			NodeFilter link=new TagNameFilter("link");
			//�����������
			OrFilter or=new OrFilter(link,script);
			//��ʼ����
			NodeList nodeList=parser.extractAllNodesThatMatch(or);
			if(nodeList!=null){
				for(int i=0;i<nodeList.size();i++){
					Node node=nodeList.elementAt(i);
					//�����<script>��ǩ
					if(node instanceof ScriptTag){
						//��ȡsrc����
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
					//�������<link>��ǩ
					else{
						//��ȡhref����
						String href=((TagNode) node).getAttribute("href");
						Pattern pattern=Pattern.compile("http://");
						Matcher matcher=pattern.matcher(href);
						if(!matcher.find()){
							//ͬ��
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
			//����<img>
			NodeFilter img=new TagNameFilter("img");
			//��ȡ���б����Ľڵ�
			NodeFilter bg=new HasAttributeFilter("background");
			NodeFilter or=new OrFilter(img,bg);
			//��ʼ����
			NodeList nodeList=parser.extractAllNodesThatMatch(or);
			if(nodeList!=null){
				for(int i=0;i<nodeList.size();i++){
					Node node=nodeList.elementAt(i);
					//�����<img>��ǩ
					if(node instanceof ImageTag){
						//��ȡsrc����
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
						//��ȡbackgound����
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