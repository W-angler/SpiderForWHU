package com.wangle.spider.main;

public class Project {
	private String head;		//课头号
	private String name;		//课程名称
	private String type;		//课程类型
	private String credit;		//学分	
	private String teacher;		//教师
	private String academy;		//授课学院
	private String learnType;	//学习类型
	private String year;		//学年
	private String term;		//学期
	private String grade;		//成绩
	
	public Project(){}
	public Project(String head,String name,
				 String type,String credit,
				 String teacher,String academy,
				 String learnType,String year,
				 String term,String grade){
		
		this.head=head;
		this.name=name;
		this.type=type;
		this.credit=credit;
		this.teacher=teacher;
		this.academy=academy;
		this.learnType=learnType;
		this.year=year;
		this.term=term;
		this.grade=grade;
		
	}
	
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getAcademy() {
		return academy;
	}
	public void setAcademy(String academy) {
		this.academy = academy;
	}
	public String getLearnType() {
		return learnType;
	}
	public void setLearnType(String learnType) {
		this.learnType = learnType;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String toString(){
		return
		"Grade:"+
		"["+
		"课头号:"+this.head+
		",课程名称:"+this.name+
		",课程类型:"+this.type+
		",学分:"+this.credit+
		",教师:"+this.teacher+
		",授课学院:"+this.academy+
		",学习类型:"+this.learnType+
		",学年:"+this.year+
		",学期:"+this.term+
		",成绩:"+this.grade+
		"]";
	}
}
