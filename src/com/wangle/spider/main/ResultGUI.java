package com.wangle.spider.main;


import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class ResultGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private String result;	//��˵����ȡ�����ĳɼ���ҳ
	private String name;	//��ҳ��ץȡ������
	private String host="210.42.121.241";
	private LinkedList<Project> list;

	public ResultGUI(String result,String name){
		this.setResult(result);
		this.name=name;

		String _name=this.getName();
		if(!isInDatabase(_name)){
			PageTool pageTool=new PageTool(result,host);
			list=pageTool.getProjectList();
		}
		else{
			list=this.getListFromDatabase(_name);
		}

		setForeground(new Color(153, 204, 255));
		setBackground(new Color(153, 204, 255));

		AnalyseProject analyse=new AnalyseProject(list);
		setTitle(this.getName()
				+"       ���㣺"+String.format("%.5f", new Double(analyse.getWeightedGPA()))
				+"       ��Ȩƽ���֣�"+String.format("%.5f", new Double(analyse.getWeightedGrade())));
		setResizable(true);
		//����logo
		try{
			Toolkit tool=this.getToolkit();
			Image logo=tool.getImage(this.getClass().getResource("/images/result.png"));
			this.setIconImage(logo);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		//������ʾ
		//setBounds(width/2-800, height/2-400, 1600, 800);	�ҵĵ��Էֱ���̫���ˡ�������ڱ��˵����ϲ�����ȫռ����������Ļ
		setBounds(width/2-600, height/2-300, 1200, 600);

		Container container = this.getContentPane();

		//��ӳɼ����
		String[] headers = {"��ͷ��","�γ�����",
				"�γ�����","ѧ��",
				"��ʦ","�ڿ�ѧԺ",
				"ѧϰ����","ѧ��",
				"ѧ��","�ɼ�"};
		String[][] data=new String[list.size()][10];
		for(int i=0;i<list.size();i++){
			Project project=list.get(i);
			data[i][0]=project.getHead();
			data[i][1]=project.getName();
			data[i][2]=project.getType();
			data[i][3]=project.getCredit();
			data[i][4]=project.getTeacher();
			data[i][5]=project.getAcademy();
			data[i][6]=project.getLearnType();
			data[i][7]=project.getYear();
			data[i][8]=project.getTerm();
			data[i][9]=project.getGrade();
		}
		JTable table = new JTable(data,headers);
		//����table����ʾ����
		table.setRowHeight(28);
		table.setFont(new Font("����", Font.PLAIN, 18));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setGridColor(new Color(80, 180, 255));
		//���ñ�ͷ
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setFont(new Font("����", Font.BOLD, 20));
		table.getTableHeader().setBackground(new Color(33, 147, 229));
		table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
		//�������ݵ����п�
		this.FitTableColumns(table);

		//������ż�е���ɫ���Լ�������ʾ����
		this.setRowProp(table);
		JScrollPane jsp = new JScrollPane(table);
		container.add(jsp);
		/*
		 * ��ֻ��һ���������ķֽ���(*^��^*)
		 */
		//�˵�
		JMenu menu=new JMenu("����");
		menu.setFont(new Font("΢���ź� Light", Font.PLAIN, 18));
		//�˵���
		JMenuItem html=new JMenuItem("����ΪHTML");
		html.setBackground(Color.WHITE);
		html.setForeground(new Color(0, 0, 0));
		html.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));

		JMenuItem excel=new JMenuItem("����ΪExcel");
		excel.setBackground(Color.WHITE);
		excel.setForeground(new Color(0, 0, 0));
		excel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));

		JMenuItem database=new JMenuItem("���������ݿ�");
		database.setBackground(Color.WHITE);
		database.setForeground(new Color(0, 0, 0));
		database.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));

		JMenuItem pride=new JMenuItem("��ѧ�����");
		pride.setBackground(Color.WHITE);
		pride.setForeground(new Color(0, 0, 0));
		pride.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));

		//��Ӳ˵����������
		html.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				NetTool netTool=new NetTool();

				String host="210.42.121.241";
				PageTool pageTool=new PageTool(result, host);
				LinkedList<String> resourceList=pageTool.getResourceList();

				//���سɼ���ҳ
				netTool.saveToFile("result/html/�ɼ�ҳ��.html", result);
				//���������Դ
				for(int i=0;i<resourceList.size();i++){
					String url=resourceList.get(i);
					String temp=netTool.downloadResource(url);
					String path=url.replaceAll("http://"+host, "result").replaceAll("\\?.*", "");
					netTool.saveToFile(path, temp);
				}
				//��ťͼƬ
				BufferedImage img1=netTool.downloadImage("http://210.42.121.241/images/btn_bg.png");
				netTool.saveToFile("result/images/btn_bg.png", img1);
				BufferedImage img2=netTool.downloadImage("http://210.42.121.241/images/button_bg.png");
				netTool.saveToFile("result/images/button_bg.png", img2);

				File file=new File("result/html/�ɼ�ҳ��.html");
				JOptionPane.showConfirmDialog(null,"��ҳ�Ѿ���������"+file.getAbsolutePath(),
						"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
			}
		});

		excel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){

				JFileChooser chooser=new JFileChooser(".");
				chooser.setSelectedFile(new File("�ɼ���.xls"));
				FileFilter filter = new FileNameExtensionFilter("xls",".xls");
				chooser.setFileFilter(filter);//��ʼ����
				int flag=chooser.showSaveDialog(getParent());
				if(flag==JFileChooser.APPROVE_OPTION){
					File file=chooser.getSelectedFile();
					String fileName=file.getAbsolutePath();
					if(!fileName.endsWith(".xls")){
						fileName+=".xls";
					}
					Excel<Project> excel=new Excel<Project>();
					if (file.exists()){
						int copy = JOptionPane.showConfirmDialog(null,"�ļ��Ѵ��ڣ��Ƿ�Ҫ���ǵ�ǰ�ļ���",
								"����", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if (copy == JOptionPane.YES_OPTION){
							excel.exportExcel(headers, list, fileName);
							JOptionPane.showConfirmDialog(null,"�ɼ��Ѿ���������"+fileName,
									"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else{
						excel.exportExcel(headers, list, fileName);
						JOptionPane.showConfirmDialog(null,"�ɼ��Ѿ���������"+fileName,
								"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		database.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(!isInDatabase(_name)){
						exportDatabase();
						JOptionPane.showConfirmDialog(null,"�ɼ��Ѿ����������ݿ�",
								"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					}
					else{
						JOptionPane.showConfirmDialog(null,"�ɼ��Ѵ������ݿ���",
								"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					}
				}
				catch(Exception e){
					JOptionPane.showConfirmDialog(null,"��ȷ���㵼�����ݿ��ˣ�",
							"�Ҵ��ˣ�����ȥ��", JOptionPane.ERROR_MESSAGE,JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		pride.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showConfirmDialog(null,
						"���ޣ�"+
								String.format("%.5f", new Double(analyse.getRequired()))+
								"ѡ�ޣ�"+
								String.format("%.5f", new Double(analyse.getElective())),
								"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(html);
		menu.add(excel);
		menu.add(database);
		menu.add(pride);

		JMenuBar bar=new JMenuBar();
		bar.setBackground(Color.WHITE);
		bar.add(menu);
		this.setJMenuBar(bar);
		this.setVisible(true);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	//��ȡ����
	public String getName(){
		String name="�ɼ�";
		try {
			Parser parser=Parser.createParser(this.name, "UTF-8");
			NodeFilter div=new TagNameFilter("div");
			NodeList divList=parser.extractAllNodesThatMatch(div);
			if(divList!=null){
				for(int i=0;i<divList.size();i++){
					Node node=divList.elementAt(i);
					//��ȡsrc����
					String idVal=((Div) node).getAttribute("id");
					if(idVal!=null&&idVal.equals("nameLable")){
						name=name+"��"+node.toPlainTextString().replaceAll(" ","").replaceAll("\r|\n","");
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return name;
	}
	//�������ݵ����п�
	public void FitTableColumns(JTable table) {
		JTableHeader header = table.getTableHeader();
		int rowCount = table.getRowCount();
		Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(
					column.getIdentifier());
			int width = (int) table.getTableHeader().getDefaultRenderer()
					.getTableCellRendererComponent(table,
							column.getIdentifier(), false, false, -1, col)
							.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) table.getCellRenderer(row, col)
						.getTableCellRendererComponent(table,
								table.getValueAt(row, col), false, false,
								row, col).getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column);
			column.setWidth(width + table.getIntercellSpacing().width);
		}
	}
	public void setRowProp(JTable table) {  
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {  

				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					if (row % 2 == 0) {
						setBackground(Color.white); //���������е�ɫ 
					} else if (row % 2 == 1) {  
						setBackground(new Color(206, 231, 255)); //����ż���е�ɫ  
					}
					return super.getTableCellRendererComponent(table, value,  
							isSelected, hasFocus, row, column);  
				}  
			};

			tcr.setHorizontalAlignment(JLabel.CENTER);
			for (int i = 0; i < table.getColumnCount(); i++) {  
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);  
			}  
		} catch (Exception ex) {  
			ex.printStackTrace();  
		}  

	}

	public LinkedList<Project> getList() {
		return list;
	}

	
	//���ݿ���������˵�JDBC����
	
	/**
	 * ������ݿ�����
	 * @return ���ݿ�����
	 */
	public String[] getDatabaseProperties(){
		Properties prop=new Properties();
		String driverClass=null;
		String url=null;
		String user=null;
		String password=null;
		try {
			FileInputStream in=new FileInputStream("database.properties");
			prop.load(in);
			driverClass=prop.getProperty("driverClass");
			url=prop.getProperty("url");
			user=prop.getProperty("user");
			password=prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String[]{driverClass,url,user,password};
	}
	/**
	 * ���������ݿ�
	 */
	public void exportDatabase(){
		String[] prop=this.getDatabaseProperties();
		String driverClass=prop[0];
		String url=prop[1];
		String user=prop[2];
		String password=prop[3];
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con=null;
		PreparedStatement preSta=null;
		Statement sta=null;
		try {
			con=DriverManager.getConnection(url,user,password);
			String name=this.getName();
			String[] s=name.split("��");
			preSta=con.prepareStatement("INSERT INTO projects VALUES("+
					"\""+s[1]+"\",?,?,?,?,?,?,?,?,?,?)");
			for(int k=0;k<this.list.size();k++){

				Project project=this.list.get(k);

				preSta.setString(1,project.getHead());
				preSta.setString(2,project.getName());
				preSta.setString(3,project.getType());
				preSta.setString(4,project.getCredit());
				preSta.setString(5,project.getTeacher());
				preSta.setString(6,project.getAcademy());
				preSta.setString(7,project.getLearnType());
				preSta.setString(8,project.getYear());
				preSta.setString(9,project.getTerm());
				preSta.setString(10,project.getGrade());

				preSta.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(preSta!=null){
				try {
					preSta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(sta!=null){
				try {
					sta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ��ǰ�����ĳɼ��Ƿ������ݿ���
	 * @param name ����
	 * @return �Ƿ�
	 */
	public boolean isInDatabase(String name){
		boolean in=false;
		String[] prop=this.getDatabaseProperties();
		String driverClass=prop[0];
		String url=prop[1];
		String user=prop[2];
		String password=prop[3];
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		Connection con=null;
		Statement sta=null;
		try{
			con=DriverManager.getConnection(url,user,password);
			sta=con.createStatement();
			String[] s=name.split("��");
			ResultSet result=sta.executeQuery("SELECT COUNT(*) FROM projects WHERE owner_name=\""+s[1]+"\"");
			if(result.next()){
				int num=result.getInt(1);
				in=(num!=0);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(sta!=null){
				try {
					sta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return in;
	}
	/**
	 * �����ݿ��л�ȡ�ɼ��б�
	 * @param owner_name ����
	 * @return �ɼ��б�
	 */
	public LinkedList<Project> getListFromDatabase(String owner_name){
		String[] prop=this.getDatabaseProperties();
		String driverClass=prop[0];
		String url=prop[1];
		String user=prop[2];
		String password=prop[3];
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		LinkedList<Project> projectList=new LinkedList<Project>();
		Connection con=null;
		Statement sta=null;
		try{
			con=DriverManager.getConnection(url,user,password);
			sta=con.createStatement();
			String[] s=owner_name.split("��");
			ResultSet result=sta.executeQuery("SELECT * FROM projects WHERE owner_name=\""+s[1]+"\"");
			while(result.next()){
				String head=result.getString(2);
				String name=result.getString(3);
				String type=result.getString(4);
				String credit=result.getString(5);
				String teacher=result.getString(6);
				String academy=result.getString(7);
				String learnType=result.getString(8);
				String year=result.getString(9);
				String term=result.getString(10);
				String grade=result.getString(11);
				projectList.add(new Project(head, name,
						type, credit,
						teacher, academy,
						learnType, year,
						term, grade));
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(sta!=null){
				try {
					sta.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return projectList;
	}
}
