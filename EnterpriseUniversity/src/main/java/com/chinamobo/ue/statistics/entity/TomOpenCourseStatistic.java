package com.chinamobo.ue.statistics.entity;

import java.util.Date;

public class TomOpenCourseStatistic {

	private Integer courseId;//课程编码
	
	private String courseNumber;
	
	private String courseName;//课程名称
	
	private String courseType;//类型
	
	private Integer learningNumber;//学习人数
	
	private String startTime;//课程开始时间
	
	private String endTime;//课程结束时间
	
	private String isRequired;//是否必修
	private String commentCount;
	private String openComment;
	
	private String opennum;
	private Integer viewers;
	private String openCourse;

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public Integer getLearningNumber() {
		return learningNumber;
	}

	public void setLearningNumber(Integer learningNumber) {
		this.learningNumber = learningNumber;
	}

	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getOpenComment() {
		return openComment;
	}

	public void setOpenComment(String openComment) {
		this.openComment = openComment;
	}

	public String getOpennum() {
		return opennum;
	}

	public void setOpennum(String opennum) {
		this.opennum = opennum;
	}

	public Integer getViewers() {
		return viewers;
	}

	public void setViewers(Integer viewers) {
		this.viewers = viewers;
	}

	public String getOpenCourse() {
		return openCourse;
	}

	public void setOpenCourse(String openCourse) {
		this.openCourse = openCourse;
	}
	
}
