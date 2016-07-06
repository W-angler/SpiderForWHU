package com.w_angler.spider.subject;

public class Subject {
	private String head;
	private String name;
	private String type;
	private String credit;
	private String teacher;
	private String academy;
	private String learnType;
	private String year;
	private String term;
	private String grade;
	
	public Subject(){}
	public Subject(String head,String name,
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
}
