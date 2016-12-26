package com.w_angler.spider.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分析课程，获得加权绩点，加权平均分
 * @author wangle
 * @date 2015.09.22
 */
public class SubjectAnalyser {
	List<Subject> list;

	public SubjectAnalyser(List<Subject> list){
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
				totalGPA+=(this.getGPA(list.get(j)))
						*(Double.parseDouble(list.get(j).getCredit()));
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
				totalGrade+=(Double.parseDouble(list.get(j).getCredit()))
						*Double.parseDouble(list.get(j).getGrade());
			}
		}
		return totalGrade/totalCredit;
	}
	//获取每个分数所对应的绩点
	private double getGPA(Subject subject){
		double grade = Double.parseDouble(subject.getGrade());
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
	public double getRequired(int grade){
		double totalGrade=0.0;		//成绩*学分
		double totalCredit=0.0;		//总学分
		for(int j=0;j<list.size();j++){
			Subject subject=list.get(j);
			if(Integer.parseInt(subject.getYear())==grade){
				if(subject.getType().contains("必修")&&!subject.getLearnType().contains("辅修")){
					if(!subject.getGrade().equals("")){
						totalCredit=totalCredit+Double.parseDouble(list.get(j).getCredit());
					}
				}
			}
		}
		for(int j=0;j<list.size();j++){
			Subject subject=list.get(j);
			if(Integer.parseInt(subject.getYear())==grade){
				if(subject.getType().contains("必修")&&!subject.getLearnType().contains("辅修")){
					if(!list.get(j).getGrade().equals("")){
						totalGrade+=(Double.parseDouble(list.get(j).getCredit()))
								*Double.parseDouble(list.get(j).getGrade());
					}
				}
			}
		}
		return totalGrade/totalCredit;
	}
	//选修的分数
	public List<Subject> getElective(int grade){
		List<Subject> topEight=new ArrayList<>();
		for(int j=0;j<list.size();j++){
			Subject subject=list.get(j);
			if(Integer.parseInt(subject.getYear())==grade){
				if(subject.getType().contains("选修")||subject.getLearnType().contains("辅修")){
					if(!subject.getGrade().equals("")){
						topEight.add(subject);
					}
				}
			}
		}
		return topEight.stream()
				.sorted((subject1,subject2)->{
					double g1=Double.parseDouble(subject1.getCredit())*Double.parseDouble(subject1.getGrade());
					double g2=Double.parseDouble(subject2.getCredit())*Double.parseDouble(subject2.getGrade());
					return g1<g2?1:-1;
				})
				.limit(8).collect(Collectors.toList());
	} 
	//保研绩点计算
	/**
	 * 理论可达最高绩点 
	 * @param rest 剩余学分
	 * @return
	 */
	public double getPostgraduate(double rest){
		double totalGPA=4.0*rest;		//绩点*学分
		double totalCredit=0.0+rest;		//总学分
		for(int j=0;j<list.size();j++){
			Subject subject=list.get(j);
			/*
			 * 可能算上大物
			 * subject.getName().contains("物理")
			 */
			if(subject.getType().contains("必修")){
				if(!subject.getGrade().equals("")){
					totalCredit=totalCredit+Double.parseDouble(list.get(j).getCredit());
				}
			}
		}
		for(int j=0;j<list.size();j++){
			Subject subject=list.get(j);
			/*
			 * 可能算上大物
			 * subject.getName().contains("物理")
			 */
			if(subject.getType().contains("必修")){
				if(!subject.getGrade().equals("")){
					totalGPA+=(this.getGPA(subject))
							*(Double.parseDouble(subject.getCredit()));
				}
			}
		}
		return totalGPA/totalCredit;
	}
	/**
	 * 目前的绩点
	 */
	public double getPostgraduate(){
		return this.getPostgraduate(0);
	}
}
