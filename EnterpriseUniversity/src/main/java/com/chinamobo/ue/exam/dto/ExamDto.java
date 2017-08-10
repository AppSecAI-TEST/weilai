package com.chinamobo.ue.exam.dto;

import java.util.Date;

public class ExamDto {
	
	private Integer examId;
	private String examName;
	private Date beginTime;
    private Date endTime;
    private String code;
    private String empName;
    private String totalpoints;
    private String gradeState;
    private String startDate;
    private String endDate;
    
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getTotalpoints() {
		return totalpoints;
	}
	public void setTotalpoints(String totalpoints) {
		this.totalpoints = totalpoints;
	}
	public String getGradeState() {
		return gradeState;
	}
	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}
    
}
