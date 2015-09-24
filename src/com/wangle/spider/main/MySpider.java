package com.wangle.spider.main;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class MySpider {
	public static void main(String[] args){
		//String index="http://210.42.121.241";
		String login="http://210.42.121.241/servlet/Login";
		String img="http://210.42.121.241/servlet/GenImg";
		//String page="http://210.42.121.241/stu/stu_index.jsp";
		String grade="http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0";
		
		NetTool netTool=new NetTool();
		
		String cookie=netTool.getCookie(login);
		netTool.setCookie(cookie);
		BufferedImage image=netTool.downloadImage(img);
		//netTool.saveToFile("F:/test/html/1.png", image);
		netTool.saveToFile("./1.png", image);
		Scanner in=new Scanner(System.in);
		String xdvfb=in.nextLine();
		netTool.login(login, "id=2014302580349","pwd=048117wang","xdvfb="+xdvfb);
		
		String html=netTool.downloadResource(grade);
		//netTool.saveToFile("F:/test/html/1.html", html);
		netTool.saveToFile("./html/1.html", html);
		
		String css1=netTool.downloadResource("http://210.42.121.241/css/style.css?v=2.002");
		//netTool.saveToFile("F:/test/css/style.css", css1);
		netTool.saveToFile("./css/style.css", css1);
		
		String css2=netTool.downloadResource("http://210.42.121.241/css/tab.css?v=2.002");
		//netTool.saveToFile("F:/test/css/tab.css", css2);
		netTool.saveToFile("./css/tab.css", css2);
		
		String js1=netTool.downloadResource("http://210.42.121.241/js/jquery.tools.min.js");
		//netTool.saveToFile("F:/test/js/jquery.tools.min.js", js1);
		netTool.saveToFile("./js/jquery.tools.min.js", js1);
		
		String js2=netTool.downloadResource("http://210.42.121.241/js/table.js?v=2.002");
		//netTool.saveToFile("F:/test/js/table.js", js2);
		netTool.saveToFile("./js/table.js", js2);
		
		BufferedImage img1=netTool.downloadImage("http://210.42.121.241/images/btn_bg.png");
		//netTool.saveToFile("F:/test/images/btn_bg.png", img1);
		netTool.saveToFile("./images/btn_bg.png", img1);
		
		BufferedImage img2=netTool.downloadImage("http://210.42.121.241/images/button_bg.png");
		//netTool.saveToFile("F:/test/images/button_bg.png", img2);
		netTool.saveToFile("./images/button_bg.png", img2);
		
		in.close();
		
	}

}
