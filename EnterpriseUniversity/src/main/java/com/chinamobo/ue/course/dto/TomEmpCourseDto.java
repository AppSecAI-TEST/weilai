package com.chinamobo.ue.course.dto;

import java.util.Date;

public class TomEmpCourseDto {
	private String courseId;
	private String courseName;
	private String code;//人员id
	private	String name;
	private String courseOnline;//课程类型
	private String courseType;//课程分类编号
	private String courseTypeName;//课程分类名称
	private String finishTime;//完成时间
	private String finishStatus;//课程是否完成
	private String status;//是否收藏
	private String courseGrade;//课程评分
	private Double finishStation;//已学完章节
	private Double sumStation;//课程总章节
	private String studycount;//学习次数
	private String inLinetime;// 在线时间
	private String coursePlan;//完成进度
	
	private String thumbStatus;//是否点赞
	private String isRequired;//是否必修
	private String commentContent;//评论
	private String startTime;//课程开始时间
	private	String endTime;//课程结束时间
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCourseTypeName() {
		return courseTypeName;
	}
	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCourseGrade() {
		return courseGrade;
	}
	public void setCourseGrade(String courseGrade) {
		this.courseGrade = courseGrade;
	}
	public Double getFinishStation() {
		return finishStation;
	}
	public void setFinishStation(Double finishStation) {
		this.finishStation = finishStation;
	}
	public Double getSumStation() {
		return sumStation;
	}
	public void setSumStation(Double sumStation) {
		this.sumStation = sumStation;
	}
	public String getStudycount() {
		return studycount;
	}
	public void setStudycount(String studycount) {
		this.studycount = studycount;
	}
	public String getInLinetime() {
		return inLinetime;
	}
	public void setInLinetime(String inLinetime) {
		this.inLinetime = inLinetime;
	}
	public String getCoursePlan() {
		return coursePlan;
	}
	public void setCoursePlan(String coursePlan) {
		this.coursePlan = coursePlan;
	}
	public String getThumbStatus() {
		return thumbStatus;
	}
	public void setThumbStatus(String thumbStatus) {
		this.thumbStatus = thumbStatus;
	}
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String  getStartTime() {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCourseOnline() {
		return courseOnline;
	}
	public void setCourseOnline(String courseOnline) {
		this.courseOnline = courseOnline;
	}
	
	
}
