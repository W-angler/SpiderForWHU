package com.wangle.spider.main;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * �����γ̣���ü�Ȩ���㣬��Ȩƽ����
 * @author wangle
 * @date 2015.09.22
 */
public class AnalyseProject {
	LinkedList<Project> list;

	public AnalyseProject(){}
	public AnalyseProject(LinkedList<Project> list){
		this.list=list;
	}
	//��ȡ��Ȩ����
	public double getWeightedGPA(){
		double totalGPA=0.0;		//����*ѧ��
		double totalCredit=0.0;		//��ѧ��
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
	//��ȡ��Ȩƽ����
	public double getWeightedGrade(){
		double totalGrade=0.0;		//�ɼ�*ѧ��
		double totalCredit=0.0;		//��ѧ��
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
	//��ȡÿ����������Ӧ�ļ���
	private double getGPA(Project project){
		double grade = Double.parseDouble(project.getGrade());
		//���˵ļ���ת������
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
	 * ���ţ��������ѧ����������������ѡ�޵ķ���
	 */
	//���޵ķ���
	public double getRequired(){

		double totalGrade=0.0;		//�ɼ�*ѧ��
		double totalCredit=0.0;		//��ѧ��
		for(int j=0;j<list.size();j++){
			Project project=list.get(j);
			Pattern pattern=Pattern.compile("����");
			Matcher matcher=pattern.matcher(project.getType());
			if(matcher.find()){
				if(!project.getGrade().equals("")){
					totalCredit=totalCredit+Double.parseDouble(list.get(j).getCredit());
				}
			}
		}
		for(int j=0;j<list.size();j++){
			Project project=list.get(j);
			Pattern pattern=Pattern.compile("����");
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
	//ѡ�޵ķ���
	public double getElective(){
		double totalGrade=0.0;		//�ɼ�*ѧ��
		for(int j=0;j<list.size();j++){
			Project project=list.get(j);
			Pattern pattern=Pattern.compile("ѡ��");
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
