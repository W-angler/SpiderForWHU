package com.w_angler.spider.gui;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.w_angler.spider.net.Spider;
import com.w_angler.spider.utils.Account;
import com.w_angler.spider.utils.Address;
import com.w_angler.spider.utils.MD5;

/**
 * 登录界面
 * @author wangle
 * @date 2015.09.22
 */
public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	//Swing组件
	private JPanel contentPane;
	private JComboBox<String> id;//学号输入框
	private JPasswordField password;//密码输入框
	private JTextField captcha;//验证码输入框
	private JLabel id_label;//学号label
	private JLabel password_label;//密码label
	private JLabel captcha_label;//验证码label
	private JLabel picture_label;//验证码显示label
	private JLabel message;//登录信息

	private BufferedImage image;//验证码

	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			LoginGUI login  = new LoginGUI();
			login.setVisible(true);
		});
	}

	public LoginGUI() {
		setTitle("登录");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(getToolkit().getImage(this.getClass().getResource("/images/logo.png")));
		{
			//居中显示
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int)screensize.getWidth();
			int height = (int)screensize.getHeight();
			setBounds(width/2-300, height/2-200, 600, 400);
		}
		{
			//设置背景
			JLabel bg=new JLabel();
			bg.setIcon(new ImageIcon(this.getClass().getResource("/images/bg.png")));
			this.getLayeredPane().add(bg,new Integer(Integer.MIN_VALUE));
			bg.setBounds(0, 0, 600, 400);
		}
		{
			//主面板
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane=(JPanel)getContentPane(); 
			contentPane.setOpaque(false);
			contentPane.setLayout(null);
			setContentPane(contentPane);
		}

		{
			//学号
			id_label = new JLabel("学号：");
			id_label.setForeground(Color.WHITE);
			id_label.setFont(new Font("宋体", Font.PLAIN, 18));
			id_label.setBounds(99, 83, 81, 21);
			contentPane.add(id_label);
		}

		{
			//密码
			password_label = new JLabel("密码：");
			password_label.setForeground(Color.WHITE);
			password_label.setFont(new Font("宋体", Font.PLAIN, 18));
			password_label.setBounds(99, 119, 81, 21);
			contentPane.add(password_label);
		}

		{
			//验证码
			captcha_label = new JLabel("验证码：");
			captcha_label.setForeground(Color.WHITE);
			captcha_label.setFont(new Font("宋体", Font.PLAIN, 18));
			captcha_label.setBounds(99, 183, 81, 21);
			contentPane.add(captcha_label);
		}

		{
			//登录状态显示
			message=new JLabel("");
			message.setAutoscrolls(true);
			message.setForeground(new Color(255,250,50));
			message.setFont(new Font("宋体", Font.PLAIN, 18));
			message.setBounds(435, 173, 120, 40);
			contentPane.add(message);
		}

		{
			//验证码显示
			picture_label = new JLabel("验证码");
			try{
				image=Spider.downloadImage(Address.img);//下载验证码
				picture_label.setIcon(new ImageIcon(image));
			}
			catch(Exception e){
				picture_label.setText("无网络连接");
			}
			picture_label.setBounds(300, 178, 120, 30);
			contentPane.add(picture_label);
			//添加点击鼠标监听器，用于点击时刷新验证码
			picture_label.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					image=Spider.downloadImage(Address.img);
					picture_label.setIcon(new ImageIcon(image));
					message.setText("");
				}
			});
		}
		{
			//学号输入框
			id = new JComboBox<String>(Account.getIds());
			id.setEditable(true);
			id.setBackground(new Color(204, 255, 255));
			id.setFont(new Font("宋体", Font.PLAIN, 18));
			id.setBounds(162, 80, 274, 27);
			id.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
			id.addFocusListener(new FocusListener(){
				@Override
				public void focusGained(FocusEvent arg0) {
					id.setBackground(new Color(255, 255, 255));
					id.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
				}
				@Override
				public void focusLost(FocusEvent arg0) {
					id.setBackground(new Color(204, 255, 255));
					id.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
				}
			});
			id.addActionListener((e)->{
				password.setText(Account.getPassword((String) id.getSelectedItem()));
			});
			contentPane.add(id);
		}

		{
			//密码输入框
			password = new JPasswordField();
			password.setText(Account.getPassword((String) id.getSelectedItem()));
			password.setBackground(new Color(204, 255, 255));
			password.setFont(new Font("宋体", Font.PLAIN, 18));
			password.setEchoChar('*');
			password.setBounds(162, 119, 274, 27);
			password.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
			password.addFocusListener(new FocusListener(){
				@Override
				public void focusGained(FocusEvent arg0) {
					password.setBackground(new Color(255, 255, 255));
					password.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
				}
				@Override
				public void focusLost(FocusEvent arg0) {
					password.setBackground(new Color(204, 255, 255));
					password.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
					password.setText(MD5.encrypt(new String(password.getPassword())));
				}
			});
			contentPane.add(password);
		}

		{
			//验证码输入框
			captcha = new JTextField();
			captcha.setBackground(new Color(204, 255, 255));
			captcha.setFont(new Font("宋体", Font.PLAIN, 18));
			captcha.setBounds(189, 180, 96, 27);
			captcha.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
			captcha.addFocusListener(new FocusListener(){
				@Override
				public void focusGained(FocusEvent arg0) {
					captcha.setBackground(new Color(255, 255, 255));
					captcha.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
				}
				@Override
				public void focusLost(FocusEvent arg0) {
					captcha.setBackground(new Color(204, 255, 255));
					captcha.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
				}
			});
			captcha.enableInputMethods(false);
			contentPane.add(captcha);
		}

		{
			//登录按钮
			JButton loginBT = new JButton("登录");
			loginBT.setForeground(new Color(0, 0, 0));
			loginBT.setFont(new Font("宋体", Font.PLAIN, 22));
			loginBT.setBackground(new Color(30, 144, 255));
			loginBT.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
			loginBT.setBounds(226, 244, 123, 29);
			//为按钮添加监听器
			loginBT.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					loginBT.setBackground(new Color(30, 144, 255));
					
					String idVal=(String) id.getSelectedItem();
					String pwdVal=new String(password.getPassword());
					String xdvfbVal=captcha.getText();
					Spider.login(Address.login,"id="+idVal,"pwd="+pwdVal,"xdvfb="+xdvfbVal);

					String name=Spider.downloadResource(Address.name);
					
					if(name.contains("csrftoken")){
						String token=null;
						Document doc=Jsoup.parse(name);
						Element element=doc.select("li#btn1").first();
						Pattern pattern=Pattern.compile("(\\w+)(-\\w+)+");
						Matcher m=pattern.matcher(element.attr("onclick"));
						if(m.find()){
							token=m.group();
						}
						String result=Spider.downloadResource(Address.subjects+"&csrftoken="+token);
						message.setText("登录成功");
						image=Spider.downloadImage(Address.img);
						picture_label.setIcon(new ImageIcon(image));
						Account.add(idVal, new String(password.getPassword()));
						Account.save();
						new ResultGUI(result,name);
					}
					else{
						String login=Spider.downloadResource(Address.login);
						Document doc=Jsoup.parse(login);
						String longString = doc.getElementById("alertp").parent().text();
						StringBuilder builder = new StringBuilder("<html>");
						char[] chars = longString.toCharArray();
						FontMetrics fontMetrics = message.getFontMetrics(message.getFont());
						for (int beginIndex = 0, limit = 1;; limit++) {
							if (fontMetrics.charsWidth(chars, beginIndex, limit) < message.getWidth()) {
								if (beginIndex + limit < chars.length) {
									continue;
								}
								builder.append(chars, beginIndex, limit);
								break;
							}
							builder.append(chars, beginIndex, limit - 1).append("<br/>");
							beginIndex += limit - 1;
							limit = 1;
						}
						builder.append("</html>");
						message.setText(builder.toString());
						image=Spider.downloadImage(Address.img);
						picture_label.setIcon(new ImageIcon(image));
					}
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					loginBT.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
					loginBT.setBackground(new Color(60, 164, 255));
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					loginBT.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
					loginBT.setBackground(new Color(30, 144, 255));
				}
				@Override
				public void mousePressed(MouseEvent arg0) {
					loginBT.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
					loginBT.setBackground(new Color(30, 144, 255));
				}
				@Override
				public void mouseReleased(MouseEvent arg0) {
					loginBT.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
					loginBT.setBackground(new Color(30, 144, 255));
				}
			});
			contentPane.add(loginBT);
		}

		/*
		 * 我只是一条华丽丽的分界线(*^▽^*)
		 */
		//菜单
		JMenu menu=new JMenu("菜单") ;
		menu.setFont(new Font("宋体", Font.PLAIN, 18));

		//菜单项
		JMenuItem help=new JMenuItem("帮助");
		help.setBackground(Color.WHITE);
		help.setForeground(new Color(0, 0, 0));
		help.setFont(new Font("宋体", Font.PLAIN, 18));
		JMenuItem about=new JMenuItem("关于");
		about.setBackground(Color.WHITE);
		about.setForeground(new Color(0, 0, 0));
		about.setFont(new Font("宋体", Font.PLAIN, 18));

		//添加菜单点击监听器
		help.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new HelpDialog();
			}
		});
		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutDialog();
			}
		});
		menu.add(help);
		menu.add(about);

		JMenuBar bar=new JMenuBar();
		bar.setBackground(Color.WHITE);
		bar.add(menu);
		this.setJMenuBar(bar);
	}
}
