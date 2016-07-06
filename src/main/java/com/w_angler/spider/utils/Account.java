package com.w_angler.spider.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Account {
	private static final Properties prop=new Properties();
	private static final String path="accounts.properties";
	static{
		try {
			FileInputStream in=new FileInputStream(path);
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] getIds(){
		List<String> ids=new ArrayList<>();
		prop.keySet().forEach(e->{
			ids.add((String) e);
		});
		String[] accounts=new String[ids.size()];
		for(int i=0;i<ids.size();i++){
			accounts[i]=ids.get(i);
		}
		return accounts;
	}
	public static String getPassword(String id){
		return prop.getProperty(id);
	}
	public static void add(String id,String password){
		prop.put(id, password);
	}
	public static void save(){
		try {
			prop.store(new FileOutputStream(path), "accounts");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
