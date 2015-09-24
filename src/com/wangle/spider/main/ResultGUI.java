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
import java.util.Enumeration;
import java.util.LinkedList;

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

	private String result;	//传说中爬取下来的成绩网页
	private String name;	//首页（抓取姓名）
	private String host="210.42.121.241";
	private LinkedList<Project> list;

	public ResultGUI(String result,String name){
		this.setResult(result);
		this.name=name;

		PageTool pageTool=new PageTool(result,host);
		list=pageTool.getProjectList();

		setForeground(new Color(153, 204, 255));
		setBackground(new Color(153, 204, 255));

		AnalyseProject analyse=new AnalyseProject(list);
		setTitle(this.getName()
				+"       绩点："+String.format("%.5f", new Double(analyse.getWeightedGPA()))
				+"       加权平均分："+String.format("%.5f", new Double(analyse.getWeightedGrade())));
		setResizable(true);
		//设置logo
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
		//居中显示
		//setBounds(width/2-800, height/2-400, 1600, 800);	我的电脑分辨率太大了……结果在别人电脑上测试完全占用了整个屏幕
		setBounds(width/2-600, height/2-300, 1200, 600);

		Container container = this.getContentPane();

		//添加成绩表格
		String[] headers = {"课头号","课程名称",
				"课程类型","学分",
				"教师","授课学院",
				"学习类型","学年",
				"学期","成绩"};
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
		//设置table的显示属性
		table.setRowHeight(28);
		table.setFont(new Font("宋体", Font.PLAIN, 18));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setGridColor(new Color(80, 180, 255));
		//设置表头
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setFont(new Font("宋体", Font.BOLD, 20));
		table.getTableHeader().setBackground(new Color(33, 147, 229));
		table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
		//根据内容调整列宽
		this.FitTableColumns(table);

		//设置奇偶行的颜色，以及居中显示（）
		this.setRowProp(table);
		JScrollPane jsp = new JScrollPane(table);
		container.add(jsp);
		/*
		 * 我只是一条华丽丽的分界线(*^▽^*)
		 */
		//菜单
		JMenu menu=new JMenu("导出");
		menu.setFont(new Font("微软雅黑 Light", Font.PLAIN, 18));
		//菜单项
		JMenuItem html=new JMenuItem("导出为HTML");
		html.setBackground(Color.WHITE);
		html.setForeground(new Color(0, 0, 0));
		html.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		JMenuItem excel=new JMenuItem("导出为Excel");
		excel.setBackground(Color.WHITE);
		excel.setForeground(new Color(0, 0, 0));
		excel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));

		//添加菜单点击监听器
		html.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				NetTool netTool=new NetTool();

				String host="210.42.121.241";
				PageTool pageTool=new PageTool(result, host);
				LinkedList<String> resourceList=pageTool.getResourceList();

				//下载成绩网页
				netTool.saveToFile("./result/html/成绩页面.html", result);
				//下载相关资源
				for(int i=0;i<resourceList.size();i++){
					String url=resourceList.get(i);
					String temp=netTool.downloadResource(url);
					String path=url.replaceAll("http://"+host, "./result").replaceAll("\\?.*", "");
					netTool.saveToFile(path, temp);
				}
				//按钮图片
				BufferedImage img1=netTool.downloadImage("http://210.42.121.241/images/btn_bg.png");
				netTool.saveToFile("./result/images/btn_bg.png", img1);
				BufferedImage img2=netTool.downloadImage("http://210.42.121.241/images/button_bg.png");
				netTool.saveToFile("./result/images/button_bg.png", img2);

				File file=new File("./result/html/成绩页面.html");
				JOptionPane.showConfirmDialog(null,"网页已经导入至："+file.getAbsolutePath(),
						"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
			}
		});

		excel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){

				JFileChooser chooser=new JFileChooser(".");
				chooser.setSelectedFile(new File("成绩单.xls"));
				FileFilter filter = new FileNameExtensionFilter("xsl",".xsl");
				chooser.setFileFilter(filter);//开始过滤
				int flag=chooser.showSaveDialog(getParent());
				if(flag==JFileChooser.APPROVE_OPTION){
					File file=chooser.getSelectedFile();
					String fileName=file.getAbsolutePath();
					Excel<Project> excel=new Excel<Project>();
					if (file.exists()){
						int copy = JOptionPane.showConfirmDialog(null,"文件已存在，是否要覆盖当前文件？",
								"保存", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
						if (copy == JOptionPane.YES_OPTION){
							excel.exportExcel(headers, list, fileName);
							JOptionPane.showConfirmDialog(null,"成绩已经导入至："+fileName,
									"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else{
						excel.exportExcel(headers, list, fileName);
						JOptionPane.showConfirmDialog(null,"成绩已经导入至："+fileName,
								"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		menu.add(html);
		menu.add(excel);

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
	//获取姓名
	public String getName(){
		String name="成绩";
		try {
			Parser parser=Parser.createParser(this.name, "UTF-8");
			NodeFilter div=new TagNameFilter("div");
			NodeList divList=parser.extractAllNodesThatMatch(div);
			if(divList!=null){
				for(int i=0;i<divList.size();i++){
					Node node=divList.elementAt(i);
					//获取src属性
					String idVal=((Div) node).getAttribute("id");
					if(idVal!=null&&idVal.equals("nameLable")){
						name=name+"—"+node.toPlainTextString().replaceAll(" ","").replaceAll("\r|\n","");
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return name;
	}
	//根据内容调整列宽
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
						setBackground(Color.white); //设置奇数行底色 
					} else if (row % 2 == 1) {  
						setBackground(new Color(206, 231, 255)); //设置偶数行底色  
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
}
