package com.chinamobo.ue.reportforms.dto;

import java.text.DecimalFormat;

/**
 * 描述 [学习资源-组织部门统计报表（线上考试）]
 * 创建者 LXT
 * 创建时间 2017年3月20日 下午6:45:14
 */
public class LearningResourseOrgDeptExamDto {
	private Integer examId;//考试id
	private String examNumber;//考试编号
	private String examName;//考试名称
	private Integer passEb;//考试积分
	private String offlineExam;//考试属性
	private String examType;//考试类别
	private String examType2;//资源类别2级
	private Integer retakingExamCount;//允许考试次数
	private Integer totalePeopleCount;//总考试次数
	private Integer peopleCount;//考试人数
	private Integer examTime;//考试时长
	private Integer passMark;//考试及格分
	private Double avgScore;//平均成绩
	private Integer passCount;//及格人数
	private String avgPassRate;//平均及格率  及格人数除以考试人数
	private Integer totalScore;//考试总成绩
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public String getExamNumber() {
		return examNumber;
	}
	public void setExamNumber(String examNumber) {
		this.examNumber = examNumber;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Integer getPassEb() {
		return passEb;
	}
	public void setPassEb(Integer passEb) {
		this.passEb = passEb;
	}
	public String getOfflineExam() {
		return offlineExam;
	}
	public void setOfflineExam(String offlineExam) {
		this.offlineExam = offlineExam;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public String getExamType2() {
		return examType2;
	}
	public void setExamType2(String examType2) {
		this.examType2 = examType2;
	}
	public Integer getRetakingExamCount() {
		return retakingExamCount;
	}
	public void setRetakingExamCount(Integer retakingExamCount) {
		this.retakingExamCount = retakingExamCount;
	}
	public Integer getTotalePeopleCount() {
		return totalePeopleCount;
	}
	public void setTotalePeopleCount(Integer totalePeopleCount) {
		this.totalePeopleCount = totalePeopleCount;
	}
	public Integer getPeopleCount() {
		return peopleCount;
	}
	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}
	public Integer getExamTime() {
		return examTime;
	}
	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
	}
	public Integer getPassMark() {
		return passMark;
	}
	public void setPassMark(Integer passMark) {
		this.passMark = passMark;
	}
	public Double getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}
	
	public void setAvgPassRate(String avgPassRate) {
		this.avgPassRate = avgPassRate;
	}
	public Integer getPassCount() {
		return passCount;
	}
	public void setPassCount(Integer passCount) {
		this.passCount = passCount;
	}
	public String getAvgPassRate() {
		if(getPassCount()==null || getPassCount()==0 || getPeopleCount()==null || getPeopleCount()==0){
			return "0.00%";
		}
		return new DecimalFormat("##0.00").format((double)getPassCount()/getPeopleCount()*100)+"%";
	}
	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	
}
