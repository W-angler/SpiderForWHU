package com.wangle.spider.main;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

/**
 * ��¼����
 * @author wangle
 * @date 2015.09.22
 */
public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	//Swing���
	private JPanel contentPane;

	private JTextField id;				//ѧ�������
	private JPasswordField pwd;			//���������
	private JTextField xdvfb;			//��֤������򣨱�����Ϊʲô��ô����ô����ı��������ʽ���ϵͳ�Ŀ�����ȥ��

	private JLabel id_label;			//ѧ��label
	private JLabel pwd_label;			//����label
	private JLabel val_label;			//��֤��label
	private JLabel picture_label;		//��֤����ʾlabel

	private NetTool netTool=new NetTool();
	private BufferedImage image;			//��֤��

	//�����������ַ
	String login="http://210.42.121.241/servlet/Login";
	String img="http://210.42.121.241/servlet/GenImg";
	String projects="http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0";


	public static void main(String[] args) {
		LoginGUI login  = new LoginGUI();
		login.setVisible(true);
	}

	public LoginGUI() {
		setForeground(new Color(153, 204, 255));
		setBackground(new Color(153, 204, 255));
		setTitle("��¼");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tool=this.getToolkit();
		Image logo=tool.getImage(this.getClass().getResource("/images/logo.png"));
		this.setIconImage(logo);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		//������ʾ
		setBounds(width/2-300, height/2-200, 600, 400);

		//���ñ���
		JLabel bg=new JLabel();
		//bg.setIcon(new ImageIcon("/images/bg.png"));
		bg.setIcon(new ImageIcon(this.getClass().getResource("/images/bg.png")));
		this.getLayeredPane().add(bg,new Integer(Integer.MIN_VALUE));
		bg.setBounds(0, 0, 600, 400);

		//�����
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane=(JPanel)getContentPane(); 
		contentPane.setOpaque(false);
		contentPane.setLayout(null);
		setContentPane(contentPane);

		//ѧ��
		id_label = new JLabel("ѧ�ţ�");
		id_label.setForeground(Color.WHITE);
		id_label.setFont(new Font("����", Font.PLAIN, 18));
		id_label.setBounds(99, 83, 81, 21);
		contentPane.add(id_label);

		//����
		pwd_label = new JLabel("���룺");
		pwd_label.setForeground(Color.WHITE);
		pwd_label.setFont(new Font("����", Font.PLAIN, 18));
		pwd_label.setBounds(99, 119, 81, 21);
		contentPane.add(pwd_label);

		//��֤��
		val_label = new JLabel("��֤�룺");
		val_label.setForeground(Color.WHITE);
		val_label.setFont(new Font("����", Font.PLAIN, 18));
		val_label.setBounds(99, 183, 81, 21);
		contentPane.add(val_label);

		//��¼״̬��ʾ
		JLabel message = new JLabel("");
		message.setForeground(new Color(255,250,50));
		message.setFont(new Font("����", Font.PLAIN, 18));
		message.setBounds(435, 183, 81, 21);
		contentPane.add(message);

		//��֤����ʾ
		picture_label = new JLabel("��֤��");
		try{
			image=netTool.downloadImage(img);		//������֤��
			picture_label.setIcon(new ImageIcon(image));
		}
		catch(Exception e){
			picture_label.setText("����������");
			//e.printStackTrace();
		}
		picture_label.setBounds(300, 178, 120, 30);
		contentPane.add(picture_label);
		//��ӵ���������������ڵ��ʱˢ����֤��
		picture_label.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				image=netTool.downloadImage(img);
				picture_label.setIcon(new ImageIcon(image));
				message.setText("");
			}
			//һ��Balabala�Ľӿ�
			@Override
			public void mouseEntered(MouseEvent arg0) {
				//do nothing
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				//do nothing
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				//do nothing
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				//do nothing
			}
		});


		//ѧ�������
		id = new JTextField();
		id.setBackground(new Color(204, 255, 255));
		id.setFont(new Font("����", Font.PLAIN, 18));
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
		//�ı��ı����
		/*
		Document doc=id.getDocument();
		doc.addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				image=netTool.downloadImage(img);
				picture_label.setIcon(new ImageIcon(image));
				message.setText("");
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				image=netTool.downloadImage(img);
				picture_label.setIcon(new ImageIcon(image));
				message.setText("");
			}

		});
		*/
		contentPane.add(id);

		//���������
		pwd = new JPasswordField();
		pwd.setBackground(new Color(204, 255, 255));
		pwd.setFont(new Font("����", Font.PLAIN, 18));
		pwd.setEchoChar('��');
		pwd.setBounds(162, 119, 274, 27);
		pwd.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
		pwd.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				pwd.setBackground(new Color(255, 255, 255));
				pwd.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				pwd.setBackground(new Color(204, 255, 255));
				pwd.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
			}
		});
		contentPane.add(pwd);

		//��֤�������
		xdvfb = new JTextField();
		xdvfb.setBackground(new Color(204, 255, 255));
		xdvfb.setFont(new Font("����", Font.PLAIN, 18));
		xdvfb.setBounds(189, 180, 96, 27);
		xdvfb.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
		xdvfb.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				xdvfb.setBackground(new Color(255, 255, 255));
				xdvfb.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 255)));
			}
			@Override
			public void focusLost(FocusEvent arg0) {
				xdvfb.setBackground(new Color(204, 255, 255));
				xdvfb.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
			}
		});
		xdvfb.enableInputMethods(false);
		contentPane.add(xdvfb);

		//��¼��ť
		JButton loginBT = new JButton("��¼");
		loginBT.setForeground(new Color(0, 0, 0));
		loginBT.setFont(new Font("����", Font.PLAIN, 22));
		loginBT.setBackground(new Color(30, 144, 255));
		loginBT.setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
		loginBT.setBounds(226, 244, 123, 29);
		//Ϊ��ť��Ӽ�����
		loginBT.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				loginBT.setBackground(new Color(30, 144, 255));

				String idVal=id.getText();
				String pwdVal=new String(pwd.getPassword());
				String xdvfbVal=xdvfb.getText();
				netTool.login(login,"id="+idVal,"pwd="+pwdVal,"xdvfb="+xdvfbVal);


				String result=netTool.downloadResource(projects);
				if(result.length()<10000){
					message.setText("��¼ʧ��");
					image=netTool.downloadImage(img);
					picture_label.setIcon(new ImageIcon(image));
				}
				else{
					String name=netTool.downloadResource("http://210.42.121.241/stu/stu_index.jsp");
					message.setText("��¼�ɹ�");
					new ResultGUI(result,name);
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

		/*
		 * ��ֻ��һ���������ķֽ���(*^��^*)
		 */
		//�˵�
		JMenu menu=new JMenu("�˵�") ;
		menu.setFont(new Font("΢���ź� Light", Font.PLAIN, 18));

		//�˵���
		JMenuItem help=new JMenuItem("����");
		help.setBackground(Color.WHITE);
		help.setForeground(new Color(0, 0, 0));
		help.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		JMenuItem about=new JMenuItem("����");
		about.setBackground(Color.WHITE);
		about.setForeground(new Color(0, 0, 0));
		about.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));

		//��Ӳ˵����������
		help.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new Help();
			}
		});
		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new About();
			}
		});
		menu.add(help);
		menu.add(about);

		JMenuBar  bar=new  JMenuBar();
		bar.setBackground(Color.WHITE);
		bar.add(menu);
		this.setJMenuBar(bar);
	}
}

class About extends JDialog{
	private static final long serialVersionUID = 1L;
	public About(){
		this.setModal(true);
		setBackground(new Color(153, 204, 255));
		setTitle("����");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit tool=this.getToolkit();
		Image logo=tool.getImage(this.getClass().getResource("/images/about.png"));
		this.setIconImage(logo);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		//������ʾ
		setBounds(width/2-240, height/2-160, 480, 320);

		//�����
		JPanel panel = new JPanel();
		panel.setBackground(new Color(153, 204, 255));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel=(JPanel)getContentPane();
		panel.setOpaque(false);
		panel.setLayout(null);

		//JLabe֧��HTML����ʾ����
		JLabel label=new JLabel("",JLabel.CENTER);
		String text="<html>"+
				"<h1 align=\"center\">����</h1><br>"+
				"<p>Author���������ѧԺ  ������</p><br>"+
				"<p>E-mail��<a href=\"mailto:048117wangle@gmail.com\">048117wangle@gmail.com</a></p><br><br>"+
				"</html>";
		label.setText(text);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("����", Font.PLAIN, 20));
		label.setBounds(0, 0, 480, 320);
		panel.add(label);

		setContentPane(panel);
		this.setVisible(true);
	}
}
class Help extends JDialog{
	private static final long serialVersionUID = 1L;
	public Help(){
		this.setModal(true);
		setForeground(new Color(153, 204, 255));
		setBackground(new Color(153, 204, 255));
		setTitle("����");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit tool=this.getToolkit();
		Image help=tool.getImage(this.getClass().getResource("/images/help.png"));
		this.setIconImage(help);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		//������ʾ
		setBounds(width/2-240, height/2-160, 480, 320);

		//�����
		JPanel panel = new JPanel();
		panel.setBackground(new Color(153, 204, 255));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel=(JPanel)getContentPane(); 
		panel.setOpaque(false);
		panel.setLayout(null);

		//JLabe֧��HTML����ʾ����
		JLabel label=new JLabel("",JLabel.CENTER);
		String text="<html>"+
				"<h1 align=\"center\">����</h1><br>"+
				"<p>1��������ȷ��ѧ�������¼</p><br>"+
				"<p>2�������֤��ˢ��</p><br>"+
				"<p>3���ɼ����棬�������->����ΪHTML,������ҳ�ļ�</p><br>"+
				"<p>4���ɼ����棬�������->����ΪExcel,����Excel</p><br>"+
				"<p></p><br><br>"+
				"</html>";
		label.setText(text);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("����", Font.PLAIN, 20));
		label.setBounds(0, 0, 480, 320);
		panel.add(label);
		setContentPane(panel);
		this.setVisible(true);
	}
}
