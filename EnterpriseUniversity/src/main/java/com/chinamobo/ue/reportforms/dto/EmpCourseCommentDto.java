package com.chinamobo.ue.reportforms.dto;

import java.util.Date;


/**
 * 描述 [学员报表-课程评论]
 * 创建者 LXT
 * 创建时间 2017年3月22日 下午3:11:34
 */
public class EmpCourseCommentDto {

	/* 1.6	员工表 EHR（tom_emp） */
	private String code;//用户名
	private String name;//姓名
	private String orgCode;//所属组织编码
	private String orgName;//所属组织
	private String oneDeptCode;//一级部门编码
	private String oneDeptName;//一级部门
	private String twoDeptCode;//二级部门编码
	private String twoDeptName;//二级部门
	private String threeDeptCode;//三级部门编码
	private String threeDeptName;//三级部门
	private String isAdmin;//管理角色分配（人员类别）
	private String jobCode;//职务编码
	private String jobName;//职务
	private String secretEmail;//邮箱
	
	/* 6.8	7.1	课程表（tom_courses） 关联 7.4	课程学习人员表(tom_course_emp_relation） */
	private String courseNumber;//资源编号(课程编号)
	private String courseName;//资源名称(课程名称)
	
	/* 7.6	课程评论表(tom_course_comment） */
	private Date commentDate;//评论时间
	
	/* 7.10	评分记录表(tom_grade_records）*/
	private Double score;//课程评分
	
	/* 7.9	课程和讲师评分维度表(tom_grade_dimension） 关联课程表*/
	private String courseGradeDimensionsC1;//课程维度1评分
	private String courseGradeDimensionsC2;//课程维度2评分
	private String courseGradeDimensionsC3;//课程维度3评分
	private String courseGradeDimensionsC4;//课程维度4评分
	private String courseGradeDimensionsC5;//课程维度5评分
	
	private String courseGradeDimensionsL1;//讲师维度1评分
	private String courseGradeDimensionsL2;//讲师维度2评分
	private String courseGradeDimensionsL3;//讲师维度3评分
	private String courseGradeDimensionsL4;//讲师维度4评分
	private String courseGradeDimensionsL5;//讲师维度5评分
	
	/* 收藏课程表(tom_favorite_course）   课程点赞表(tom_course_thumb_up） */
	private String favoriteStatus;//收藏 ， Y 收藏  N 取消收藏
	private String thumbUpStatus;//点赞 ， Y 点赞  N 取消点赞
	
	/* 7.6	课程评论表(tom_course_comment） */
	private String oneCourseCommentContent;//课程评论内容

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOneDeptName() {
		return oneDeptName;
	}

	public void setOneDeptName(String oneDeptName) {
		this.oneDeptName = oneDeptName;
	}

	public String getTwoDeptName() {
		return twoDeptName;
	}

	public void setTwoDeptName(String twoDeptName) {
		this.twoDeptName = twoDeptName;
	}

	public String getThreeDeptName() {
		return threeDeptName;
	}

	public void setThreeDeptName(String threeDeptName) {
		this.threeDeptName = threeDeptName;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getSecretEmail() {
		return secretEmail;
	}

	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
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

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getCourseGradeDimensionsC1() {
		return courseGradeDimensionsC1;
	}

	public void setCourseGradeDimensionsC1(String courseGradeDimensionsC1) {
		this.courseGradeDimensionsC1 = courseGradeDimensionsC1;
	}

	public String getCourseGradeDimensionsC2() {
		return courseGradeDimensionsC2;
	}

	public void setCourseGradeDimensionsC2(String courseGradeDimensionsC2) {
		this.courseGradeDimensionsC2 = courseGradeDimensionsC2;
	}

	public String getCourseGradeDimensionsC3() {
		return courseGradeDimensionsC3;
	}

	public void setCourseGradeDimensionsC3(String courseGradeDimensionsC3) {
		this.courseGradeDimensionsC3 = courseGradeDimensionsC3;
	}

	public String getCourseGradeDimensionsC4() {
		return courseGradeDimensionsC4;
	}

	public void setCourseGradeDimensionsC4(String courseGradeDimensionsC4) {
		this.courseGradeDimensionsC4 = courseGradeDimensionsC4;
	}

	public String getCourseGradeDimensionsC5() {
		return courseGradeDimensionsC5;
	}

	public void setCourseGradeDimensionsC5(String courseGradeDimensionsC5) {
		this.courseGradeDimensionsC5 = courseGradeDimensionsC5;
	}

	public String getCourseGradeDimensionsL1() {
		return courseGradeDimensionsL1;
	}

	public void setCourseGradeDimensionsL1(String courseGradeDimensionsL1) {
		this.courseGradeDimensionsL1 = courseGradeDimensionsL1;
	}

	public String getCourseGradeDimensionsL2() {
		return courseGradeDimensionsL2;
	}

	public void setCourseGradeDimensionsL2(String courseGradeDimensionsL2) {
		this.courseGradeDimensionsL2 = courseGradeDimensionsL2;
	}

	public String getCourseGradeDimensionsL3() {
		return courseGradeDimensionsL3;
	}

	public void setCourseGradeDimensionsL3(String courseGradeDimensionsL3) {
		this.courseGradeDimensionsL3 = courseGradeDimensionsL3;
	}

	public String getCourseGradeDimensionsL4() {
		return courseGradeDimensionsL4;
	}

	public void setCourseGradeDimensionsL4(String courseGradeDimensionsL4) {
		this.courseGradeDimensionsL4 = courseGradeDimensionsL4;
	}

	public String getCourseGradeDimensionsL5() {
		return courseGradeDimensionsL5;
	}

	public void setCourseGradeDimensionsL5(String courseGradeDimensionsL5) {
		this.courseGradeDimensionsL5 = courseGradeDimensionsL5;
	}

	public String getFavoriteStatus() {
		return favoriteStatus;
	}

	public void setFavoriteStatus(String favoriteStatus) {
		this.favoriteStatus = favoriteStatus;
	}

	public String getThumbUpStatus() {
		return thumbUpStatus;
	}

	public void setThumbUpStatus(String thumbUpStatus) {
		this.thumbUpStatus = thumbUpStatus;
	}

	public String getOneCourseCommentContent() {
		return oneCourseCommentContent;
	}

	public void setOneCourseCommentContent(String oneCourseCommentContent) {
		this.oneCourseCommentContent = oneCourseCommentContent;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOneDeptCode() {
		return oneDeptCode;
	}

	public void setOneDeptCode(String oneDeptCode) {
		this.oneDeptCode = oneDeptCode;
	}

	public String getTwoDeptCode() {
		return twoDeptCode;
	}

	public void setTwoDeptCode(String twoDeptCode) {
		this.twoDeptCode = twoDeptCode;
	}

	public String getThreeDeptCode() {
		return threeDeptCode;
	}

	public void setThreeDeptCode(String threeDeptCode) {
		this.threeDeptCode = threeDeptCode;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	
	
	
	
}
