package com.w_angler.spider.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

class HelpDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	public HelpDialog(){
		this.setModal(true);
		setForeground(new Color(153, 204, 255));
		setBackground(new Color(153, 204, 255));
		setTitle("帮助");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit tool=this.getToolkit();
		Image help=tool.getImage(this.getClass().getResource("/images/help.png"));
		this.setIconImage(help);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		//居中显示
		setBounds(width/2-240, height/2-160, 480, 320);

		//主面板
		JPanel panel = new JPanel();
		panel.setBackground(new Color(153, 204, 255));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel=(JPanel)getContentPane(); 
		panel.setOpaque(false);
		panel.setLayout(null);

		//JLabe支持HTML来显示内容
		JLabel label=new JLabel("",JLabel.CENTER);
		String text="<html>"+
				"<h1 align=\"center\">帮助</h1><br>"+
				"<p>1、输入正确的学号密码登录</p>"+
				"<p>2、点击验证码刷新</p>"+
				"<p>3、成绩界面，点击导出->导出为HTML,导出网页文件</p>"+
				"<p>4、成绩界面，点击导出->导出为Excel,导出Excel</p>"+
				"<p>5、成绩界面，点击导出->奖学金分析,分析奖学金</p>"+
				"<p>（仅限于没有将某些选修当做b1）</p>"+
				"<p>6、成绩界面，点击导出->保研分析,进行保研分析</p>"+
				"<p></p><br><br>"+
				"</html>";
		label.setText(text);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("宋体", Font.PLAIN, 20));
		label.setBounds(0, 0, 480, 320);
		panel.add(label);
		setContentPane(panel);
		this.setVisible(true);
	}
}
