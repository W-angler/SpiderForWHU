package com.w_angler.spider.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class Address {
	public static String ip;
	/**
	 * 验证码地址
	 */
	public static String img;
	/**
	 * 登录地址
	 */
	public static String login;
	/**
	 * 成绩单地址
	 */
	public static String subjects;
	/**
	 * 首页,获取姓名
	 */
	public static String name;
	static{
		try {
			FileInputStream in=new FileInputStream("ip.properties");
			Properties prop=new Properties();
			prop.load(in);
			
			ip=prop.getProperty("ip");
			img="http://"+ip+"/servlet/GenImg";
			login="http://"+ip+"/servlet/Login";
			subjects="http://"+ip+"/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0";
			name="http://"+ip+"/stu/stu_index.jsp";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	private Address(){}
	
	@Test
	public void test(){
		System.out.println(Address.ip);
		System.out.println(Address.img);
		System.out.println(Address.login);
		System.out.println(Address.subjects);
	}
}
