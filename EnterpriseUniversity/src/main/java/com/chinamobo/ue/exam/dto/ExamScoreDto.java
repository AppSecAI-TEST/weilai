package com.chinamobo.ue.exam.dto;
/**
 * 
 * ClassName:ExamScoreDto
 * Function:成绩信息实体
 * @author Acemon
 * 2017年7月19日
 */
public class ExamScoreDto {
	private String code; 
	private String empName; 
	private String gradeState; 
	private int examCount; 
	private int totalPoints; 
	private int fullMark; 
	private int passMark;
	private int examId;
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
	public String getGradeState() {
		return gradeState;
	}
	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}
	public int getExamCount() {
		return examCount;
	}
	public void setExamCount(int examCount) {
		this.examCount = examCount;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public int getFullMark() {
		return fullMark;
	}
	public void setFullMark(int fullMark) {
		this.fullMark = fullMark;
	}
	public int getPassMark() {
		return passMark;
	}
	public void setPassMark(int passMark) {
		this.passMark = passMark;
	}
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	} 
	
}
