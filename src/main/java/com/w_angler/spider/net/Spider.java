package com.w_angler.spider.net;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
/**
 * 
 * @author W-angler
 *
 */
public class Spider {
	/**
	 * 获取的cookie，这里主要是维持session的JSESSIONID
	 */
	private static String cookie;
	
	/**
	 * 模拟登陆
	 * @param urlstr 登录URL
	 * @param var 可变长参数，请求参数，键值对形式，"key=value"
	 */
	public static void login(String urlstr,String... var){
		try{
			StringBuffer param=new StringBuffer();
			URL url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//重定向跟踪
			conn.setInstanceFollowRedirects(true);
			conn.addRequestProperty("Cookie", cookie);
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			//请求参数
			for(int i=0;i<var.length;i++){
				if(i!=var.length-1){
					param.append(var[i]).append("&");
				}
				else{
					param.append(var[i]);
				}
			}
			//写入请求参数
			byte[] b = param.toString().getBytes();
			OutputStream out=conn.getOutputStream();
			out.write(b);
			out.flush();
			out.close();
			//尝试连接，确保登录状态
			BufferedReader read=new BufferedReader(new InputStreamReader(conn.getInputStream(),"gbk"));
			read.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 下载资源，只能是文本类型资源，例如HTML、CSS、js、xml、svg等
	 * @param urlStr 资源URL
	 * @return 获取到的文本资源（字符串）
	 */
	public static String downloadResource(String urlStr){
		try {
			URL url=new URL(urlStr);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.addRequestProperty("Cookie", cookie);
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			BufferedReader read=new BufferedReader(new InputStreamReader(conn.getInputStream(),"gbk"));
			//逐行获取文本，结尾加上换行保持结构
			String line=null;
			StringBuffer result=new StringBuffer();
			while((line=read.readLine())!=null){
				result.append(line).append("\n");
			}
			read.close();
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 下载图片，存储到BufferedImage中
	 * @param url 图片的URL
	 * @return 存储图片的BufferedImage对象
	 */
	public static BufferedImage downloadImage(String url){
		BufferedImage image=null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(Spider.getImage(url));
			image = ImageIO.read((InputStream)in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 将下载的资源保存到本地文件中，保存文本文件
	 * @param path 要存储到的文件的路径，含文件名与扩展名
	 * @param result 获取到的文本资源
	 */
	public static void saveToFile(String path,String result){
		File file=new File(path);
		if(file.getParentFile()!=null&&!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try(FileWriter out=new FileWriter(file)){
			out.write(result);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 将下载的资源保存到本地文件中，保存图片
	 * @param path 要存储到的图片的路径，含文件名与扩展名
	 * @param result 获取到的图片资源
	 */
	public static void saveToFile(String path,BufferedImage result){
		File file=new File(path);
		if(file.getParentFile()!=null&&!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try (FileOutputStream out=new FileOutputStream(file)){
			//获取图片格式
			String[] s=path.split("\\.");
			String format=s[s.length-1];
			ByteArrayOutputStream os=new ByteArrayOutputStream();
			//可保存多种格式图片，具体看BufferedImage的api
			ImageIO.write(result, format, os);
			byte b[]=os.toByteArray();
			out.write(b);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取图片数据的字节数组
	 * @param strUrl 图片的URL
	 * @return 图片数据的字节数组
	 */
	private static byte[] getImage(String strUrl){  
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			cookie=conn.getHeaderField("Set-Cookie");
			InputStream inStream = conn.getInputStream();
			return readImage(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}  
	/**
	 * 
	 * @param inStream 含图片数据的输入流
	 * @return 图片数据的字节数组
	 * @throws Exception
	 */
	private static byte[] readImage(InputStream inStream) throws IOException{  
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len=inStream.read(buffer))!= -1){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}