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

	private String result;	//��˵����ȡ�����ĳɼ���ҳ
	private String name;	//��ҳ��ץȡ������
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

		//��Ӳ˵����������
		html.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				NetTool netTool=new NetTool();

				String host="210.42.121.241";
				PageTool pageTool=new PageTool(result, host);
				LinkedList<String> resourceList=pageTool.getResourceList();

				//���سɼ���ҳ
				netTool.saveToFile("./result/html/�ɼ�ҳ��.html", result);
				//���������Դ
				for(int i=0;i<resourceList.size();i++){
					String url=resourceList.get(i);
					String temp=netTool.downloadResource(url);
					String path=url.replaceAll("http://"+host, "./result").replaceAll("\\?.*", "");
					netTool.saveToFile(path, temp);
				}
				//��ťͼƬ
				BufferedImage img1=netTool.downloadImage("http://210.42.121.241/images/btn_bg.png");
				netTool.saveToFile("./result/images/btn_bg.png", img1);
				BufferedImage img2=netTool.downloadImage("http://210.42.121.241/images/button_bg.png");
				netTool.saveToFile("./result/images/button_bg.png", img2);

				File file=new File("./result/html/�ɼ�ҳ��.html");
				JOptionPane.showConfirmDialog(null,"��ҳ�Ѿ���������"+file.getAbsolutePath(),
						"ȷ��", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
			}
		});

		excel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){

				JFileChooser chooser=new JFileChooser(".");
				chooser.setSelectedFile(new File("�ɼ���.xls"));
				FileFilter filter = new FileNameExtensionFilter("xsl",".xsl");
				chooser.setFileFilter(filter);//��ʼ����
				int flag=chooser.showSaveDialog(getParent());
				if(flag==JFileChooser.APPROVE_OPTION){
					File file=chooser.getSelectedFile();
					String fileName=file.getAbsolutePath();
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
}
