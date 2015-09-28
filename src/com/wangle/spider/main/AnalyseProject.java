package com.wangle.spider.main;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分析课程，获得加权绩点，加权平均分
 * @author wangle
 * @date 2015.09.22
 */
public class AnalyseProject {
	LinkedList<Project> list;

	public AnalyseProject(){}
	public AnalyseProject(LinkedList<Project> list){
		this.list=list;
	}
	//获取加权绩点
	public double getWeightedGPA(){
		double totalGPA=0.0;		//绩点*学分
		double totalCredit=0.0;		//总学分
		for(int j=0;j<list.size();j++){
			if(!list.get(j).getGrade().equals("")){
				totalCredit=totalCredit+Double.parseDouble(list.get(j).getCredit());
			}
		}
		for(int j=0;j<list.size();j++){
			if(!list.get(j).getGrade().equals("")){
				totalGPA+=(this.getGPA(list.get(j)))*(Double.parseDouble(list.get(j).getCredit()));
			}
		}
		return totalGPA/totalCredit;
	}
	//获取加权平均分
	public double getWeightedGrade(){
		double totalGrade=0.0;		//成绩*学分
		double totalCredit=0.0;		//总学分
		for(int j=0;j<list.size();j++){
			if(!list.get(j).getGrade().equals("")){
				totalCredit=totalCredit+Double.parseDouble(list.get(j).getCredit());
			}
		}
		for(int j=0;j<list.size();j++){
			if(!list.get(j).getGrade().equals("")){
				double temp=Double.parseDouble(list.get(j).getGrade());
				totalGrade+=(Double.parseDouble(list.get(j).getCredit()))*temp;
			}
		}
		return totalGrade/totalCredit;
	}
	//获取每个分数所对应的绩点
	private double getGPA(Project project){
		double grade = Double.parseDouble(project.getGrade());
		//烦人的绩点转换……
		if(grade>=90&&grade<=100){
			return 4.0;
		}
		else if(grade>=85&&grade<=89){
			return 3.7;
		}
		else if(grade>=82&&grade<=84){
			return 3.3;
		}
		else if(grade>=78&&grade<=81){
			return 3.0;
		}
		else if(grade>=75&&grade<=77){
			return 2.7;
		}
		else if(grade>=72&&grade<=74){
			return 2.3;
		}
		else if(grade>=68&&grade<=71){
			return 2.0;
		}
		else if(grade>=64&&grade<=67){
			return 1.5;
		}
		else if(grade>=60&&grade<=63){
			return 1.0;
		}
		else{
			return 0.0;
		}
	}

	/*
	 * 嗯嗯，最近评奖学金，这个用来算必修与选修的分数
	 */
	//必修的分数
	public double getRequired(){

		double totalGrade=0.0;		//成绩*学分
		double totalCredit=0.0;		//总学分
		for(int j=0;j<list.size();j++){
			Project project=list.get(j);
			Pattern pattern=Pattern.compile("必修");
			Matcher matcher=pattern.matcher(project.getType());
			if(matcher.find()){
				if(!project.getGrade().equals("")){
					totalCredit=totalCredit+Double.parseDouble(list.get(j).getCredit());
				}
			}
		}
		for(int j=0;j<list.size();j++){
			Project project=list.get(j);
			Pattern pattern=Pattern.compile("必修");
			Matcher matcher=pattern.matcher(project.getType());
			if(matcher.find()){
				if(!list.get(j).getGrade().equals("")){
					double temp=Double.parseDouble(list.get(j).getGrade());
					totalGrade+=(Double.parseDouble(list.get(j).getCredit()))*temp;
				}
			}
		}
		return totalGrade/totalCredit;
	}
	//选修的分数
	public double getElective(){
		double totalGrade=0.0;		//成绩*学分
		for(int j=0;j<list.size();j++){
			Project project=list.get(j);
			Pattern pattern=Pattern.compile("选修");
			Matcher matcher=pattern.matcher(project.getType());
			if(matcher.find()){
				if(!list.get(j).getGrade().equals("")){
					double temp=Double.parseDouble(list.get(j).getGrade());
					totalGrade+=(Double.parseDouble(list.get(j).getCredit()))*temp;
				}
			}
		}
		return totalGrade*0.002;
	}
	public void setList(LinkedList<Project> list) {
		this.list = list;
	}
}
