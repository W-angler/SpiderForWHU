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

public class AboutDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	public AboutDialog(){
		this.setModal(true);
		setBackground(new Color(153, 204, 255));
		setTitle("关于");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit tool=this.getToolkit();
		Image logo=tool.getImage(this.getClass().getResource("/images/about.png"));
		this.setIconImage(logo);
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
				"<h1 align=\"center\">关于</h1><br>"+
				"<p>Author：国际软件学院  汪鹏程</p><br>"+
				"<p>E-mail：<a href=\"mailto:048117wangle@gmail.com\">048117wangle@gmail.com</a></p><br><br>"+
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
