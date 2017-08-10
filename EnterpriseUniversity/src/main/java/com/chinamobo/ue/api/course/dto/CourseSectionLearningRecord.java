package com.chinamobo.ue.api.course.dto;

public class CourseSectionLearningRecord {
	private String courseStudyStates;//课程学习状态 Y/N
	private String whetherGrade;//是否评分 Y/N
	private String gradeStatus;
	
	
	public String getGradeStatus() {
		return gradeStatus;
	}
	public void setGradeStatus(String gradeStatus) {
		this.gradeStatus = gradeStatus;
	}
	public String getCourseStudyStates() {
		return courseStudyStates;
	}
	public void setCourseStudyStates(String courseStudyStates) {
		this.courseStudyStates = courseStudyStates;
	}
	public String getWhetherGrade() {
		return whetherGrade;
	}
	public void setWhetherGrade(String whetherGrade) {
		this.whetherGrade = whetherGrade;
	}
}
