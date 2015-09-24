package com.wangle.spider.main;
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
 * ���繤���࣬���ڻ�ȡcookie��ģ���½��
 * ����HTML��CSS��js��ͼƬ����Դ���������ļ��У�����һ���Ŀ�������
 * @author wangle
 * @date 2015.09.21
 */

public class NetTool {
	/**
	 * ��ȡ��cookie��������Ҫ��ά��session��JSESSIONID
	 */
	private String cookie;
	
	public NetTool(){}
	/**
	 * 
	 * @param cookie Ҫ���õ�cookie
	 */
	public NetTool(String cookie){
		this.cookie=cookie;
	}
	/**
	 * ���ڻ�ȡcookieֵ
	 * @param url ��ȡcookie��URL
	 * @return cookieֵ
	 */
	public String getCookie(String url){
		String cookie0="";
		try {
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection)obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
            //����Ӧͷ�л�ȡcookie
			cookie0= conn.getHeaderField("Set-Cookie");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return cookie0;
	}
	/**
	 * ģ���½
	 * @param urlstr ��¼URL
	 * @param var �ɱ䳤�����������������ֵ����ʽ��"key=value"
	 */
	public void login(String urlstr,String... var){
		try{
			StringBuffer request=new StringBuffer();
			URL url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//�ض������
	        conn.setInstanceFollowRedirects(true);
			conn.addRequestProperty("Cookie", cookie);
            conn.setConnectTimeout(5 * 1000);
            
			conn.connect();
			//�������
			for(int i=0;i<var.length;i++){
				if(i!=var.length-1){
					request=request.append(var[i]).append("&");
				}
				else{
					request=request.append(var[i]);
				}
			}
			//д���������
			byte[] b = request.toString().getBytes();
			OutputStream out=conn.getOutputStream();
			out.write(b);
			out.flush();
			out.close();

			//�������ӣ�ȷ����¼״̬
			BufferedReader read=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			read.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ������Դ��ֻ�����ı�������Դ������HTML��CSS��js��xml��svg��
	 * @param urlStr ��ԴURL
	 * @return ��ȡ�����ı���Դ���ַ�����
	 */
	public String downloadResource(String urlStr){
		URL url=null;
		HttpURLConnection conn=null;
		BufferedReader read=null;
		StringBuffer result=new StringBuffer();
		String line=null;
		try {
			url=new URL(urlStr);
			conn=(HttpURLConnection)url.openConnection();
			
            conn.addRequestProperty("Cookie", cookie);
	        conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
	        read=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        //���л�ȡ�ı�����β���ϻ��б��ֽṹ
	        while((line=read.readLine())!=null){
	        	result=result.append(line).append("\n");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(read!=null){
					read.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	/**
	 * ����ͼƬ���洢��BufferedImage��
	 * @param url ͼƬ��URL
	 * @return �洢ͼƬ��BufferedImage����
	 */
	public BufferedImage downloadImage(String url){
		BufferedImage image=null;
        try {
        	ByteArrayInputStream in = new ByteArrayInputStream(this.getImage(url));
        	image = ImageIO.read((InputStream)in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
	
	/**
	 * �����ص���Դ���浽�����ļ��У������ı��ļ�
	 * @param path Ҫ�洢�����ļ���·�������ļ�������չ��
	 * @param result ��ȡ�����ı���Դ
	 */
	public void saveToFile(String path,String result){
		File file=new File(path);
		FileWriter out=null;
		if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try{
			out=new FileWriter(file);
			out.write(result);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if(out!=null){
					out.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * �����ص���Դ���浽�����ļ��У�����ͼƬ
	 * @param path Ҫ�洢����ͼƬ��·�������ļ�������չ��
	 * @param result ��ȡ����ͼƬ��Դ
	 */
	public void saveToFile(String path,BufferedImage result){
		File file=new File(path);
		if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream out=null;
		try{
			out=new FileOutputStream(file);
			//��ȡͼƬ��ʽ
			String[] s=path.split("\\.");
			String format=null;
			try{
				if(s.length==1){
					throw new Exception("��ȷ�����·�����ˣ�");
				}
				else{
					format=s[s.length-1];
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			ByteArrayOutputStream os=new ByteArrayOutputStream();
			
			//�ɱ�����ָ�ʽͼƬ�����忴BufferedImage��api
			ImageIO.write(result, format, os);
			byte b[]=os.toByteArray();
			out.write(b);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if(out!=null){
		            out.flush();
					out.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ȡͼƬ���ݵ��ֽ�����
	 * @param strUrl ͼƬ��URL
	 * @return ͼƬ���ݵ��ֽ�����
	 */
    private byte[] getImage(String strUrl){  
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Cookie", cookie);
            conn.setConnectTimeout(5 * 1000);
            conn.connect();
            cookie=conn.getHeaderField("Set-Cookie");
            InputStream inStream = conn.getInputStream();
            byte[] btImg = readImage(inStream);
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }  
        return null;  
    }  
    /**
     * 
     * @param inStream ��ͼƬ���ݵ�������
     * @return ͼƬ���ݵ��ֽ�����
     * @throws Exception
     */
    private byte[] readImage(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }  
	
	public void setCookie(String cookie){
		this.cookie=cookie;
	}
}