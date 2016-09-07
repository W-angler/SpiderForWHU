package com.w_angler.spider.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.List;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.w_angler.spider.net.Spider;
import com.w_angler.spider.subject.Page;
import com.w_angler.spider.subject.Subject;
import com.w_angler.spider.subject.SubjectAnalyser;
import com.w_angler.spider.utils.Address;
import com.w_angler.spider.utils.Excel;

public class ResultGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JTable table;

	private List<Subject> list;
	private static String[] headers = {
		"课头号","课程名称",
		"课程类型","学分",
		"教师","授课学院",
		"学习类型","学年",
		"学期","成绩"
	};

	public ResultGUI(String result,String name){
		Page page=new Page(result,Address.ip);
		list=page.getSubjectList();
		SubjectAnalyser analyser=new SubjectAnalyser(list);
		{
			setTitle(getName(name)
					+"       绩点："+String.format("%.5f", new Double(analyser.getWeightedGPA()))
					+"       加权平均分："+String.format("%.5f", new Double(analyser.getWeightedGrade())));
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
		}
		{
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int)screensize.getWidth();
			int height = (int)screensize.getHeight();
			//居中显示
			setBounds(width/2-600, height/2-300, 1200, 600);
		}
		{
			String[][] data=new String[list.size()][10];
			for(int i=0;i<list.size();i++){
				Subject subject=list.get(i);
				data[i][0]=subject.getHead();
				data[i][1]=subject.getName();
				data[i][2]=subject.getType();
				data[i][3]=subject.getCredit();
				data[i][4]=subject.getTeacher();
				data[i][5]=subject.getAcademy();
				data[i][6]=subject.getLearnType();
				data[i][7]=subject.getYear();
				data[i][8]=subject.getTerm();
				data[i][9]=subject.getGrade();
			}
			table = new JTable(data,headers);
			//设置table的显示属性
			table.setRowHeight(28);
			table.setFont(new Font("宋体", Font.PLAIN, 18));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.setGridColor(new Color(80, 180, 255));
			{
				//设置表头
				table.getTableHeader().setReorderingAllowed(false);
				table.getTableHeader().setResizingAllowed(false);
				table.getTableHeader().setFont(new Font("宋体", Font.BOLD, 20));
				table.getTableHeader().setBackground(new Color(33, 147, 229));
				table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(33, 147, 239)));
			}
			//根据内容调整列宽
			this.FitTableColumns(table);

			//设置奇偶行的颜色，以及居中显示
			this.setRowProp(table);
			JScrollPane jsp = new JScrollPane(table);
			this.add(jsp);
		}
		/*
		 * 我只是一条华丽丽的分界线(*^▽^*)
		 */
		{
			//菜单
			JMenu menu=new JMenu("导出");
			menu.setFont(new Font("宋体", Font.PLAIN, 18));
			//菜单项
			JMenuItem html=new JMenuItem("导出为HTML");
			html.setBackground(Color.WHITE);
			html.setForeground(new Color(0, 0, 0));
			html.setFont(new Font("宋体", Font.PLAIN, 18));

			JMenuItem excel=new JMenuItem("导出为Excel");
			excel.setBackground(Color.WHITE);
			excel.setForeground(new Color(0, 0, 0));
			excel.setFont(new Font("宋体", Font.PLAIN, 18));

			JMenuItem pride=new JMenuItem("奖学金分析");
			pride.setBackground(Color.WHITE);
			pride.setForeground(new Color(0, 0, 0));
			pride.setFont(new Font("宋体", Font.PLAIN, 18));
			//添加菜单点击监听器
			{
				html.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						Page pageTool=new Page(result, Address.ip);
						List<String> resourceList=pageTool.getResourceList();
						//下载成绩网页
						Spider.saveToFile("result/html/成绩页面.html", result);
						//下载相关资源
						for(int i=0;i<resourceList.size();i++){
							String url=resourceList.get(i);
							String temp=Spider.downloadResource(url);
							String path=url.replaceAll("http://"+Address.ip, "result").replaceAll("\\?.*", "");
							Spider.saveToFile(path, temp);
						}
						//按钮图片
						BufferedImage img1=Spider.downloadImage("http://210.42.121.241/images/btn_bg.png");
						Spider.saveToFile("result/images/btn_bg.png", img1);
						BufferedImage img2=Spider.downloadImage("http://210.42.121.241/images/button_bg.png");
						Spider.saveToFile("result/images/button_bg.png", img2);

						File file=new File("result/html/成绩页面.html");
						JOptionPane.showConfirmDialog(null,"网页已经导入至："+file.getAbsolutePath(),
								"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
			{
				excel.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						JFileChooser chooser=new JFileChooser(".");
						chooser.setSelectedFile(new File("成绩单.xls"));
						FileFilter filter = new FileNameExtensionFilter("xls",".xls");
						chooser.setFileFilter(filter);//开始过滤
						int flag=chooser.showSaveDialog(getParent());
						if(flag==JFileChooser.APPROVE_OPTION){
							File file=chooser.getSelectedFile();
							String fileName=file.getAbsolutePath();
							if(!fileName.endsWith(".xls")){
								fileName+=".xls";
							}
							if (file.exists()){
								int copy = JOptionPane.showConfirmDialog(null,"文件已存在，是否要覆盖当前文件？",
										"保存", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
								if (copy == JOptionPane.YES_OPTION){
									Excel.export(headers, list, fileName);
									JOptionPane.showConfirmDialog(null,"成绩已经导入至："+fileName,
											"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
								}
							}
							else{
								Excel.export(headers, list, fileName);
								JOptionPane.showConfirmDialog(null,"成绩已经导入至："+fileName,
										"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
							}
						}
					}
				});
			}
			{
				pride.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						int year=Integer.parseInt(JOptionPane.showInputDialog(null, "请输入学年", "奖学金分析",JOptionPane.INFORMATION_MESSAGE));
						JOptionPane.showConfirmDialog(null,
								"必修："+String.format("%.5f", new Double(analyser.getRequired(year)))+
								"选修："+String.format("%.5f", new Double(analyser.getElective(year))),
								"确定", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
			menu.add(html);
			menu.add(excel);
			menu.add(pride);

			JMenuBar bar=new JMenuBar();
			bar.setBackground(Color.WHITE);
			bar.add(menu);
			this.setJMenuBar(bar);
		}
		this.setVisible(true);
	}
	/**
	 * 获取姓名
	 */
	public String getName(String html){
		Document doc=Jsoup.parse(html);
		Elements n=doc.select("div#nameLable");
		String name="成绩"+"—"+n.first().text().replaceAll(" ","").replaceAll("\r|\n","");
		return name;
	}
	/**
	 * 根据内容调整列宽
	 * @param table
	 */
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
	/**
	 * 设置JTable的颜色
	 * @param table
	 */
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
}
