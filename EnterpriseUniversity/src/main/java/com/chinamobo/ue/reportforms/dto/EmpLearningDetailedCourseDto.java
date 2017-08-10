package com.chinamobo.ue.reportforms.dto;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 描述 [学员报表-学习资源详细（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月22日 下午3:17:31
 */
public class EmpLearningDetailedCourseDto {
	/*用户信息*/
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
	
	/*资源信息*/
	private String courseNumber;//资源编号(课程编号)
	private String courseName;//资源名称(课程名称)
	private Integer ecurrency;//资源积分
	private String openCourse;//资源属性
	private String courseOnline;//资源类别1级
	private String sectionClassifyCode;//资源类别2级code
	private String sectionClassifyName;//资源类别2级name
	/*活动*/
	private String activityNumber;//活动编号
	private String activityName;//活动名称
	private String parentProjectClassifyName;//活动类别
	private Date createTime;//活动创建日期
	private String needApply;//活动报名  	Y：需要报名		N：不需要报名
	private Date applicationStartTime;//活动报名时间
	private Date activityStartTime;//活动开始时间
	private Date activityEndTime;//活动结束时间
	private Date updateTime;//授权日期
	
	/*线上课程*/
	private String firstVisitTime;//首次访问时间
	private String learningCount;//学习次数
	private Double courseTimes;//学习资源时长
	private String learningCourseTimes;//学习时长
	private String learningrate;//学习进度
	
	/*线下班次*/
	private String classesSignUp;//线下报名
	private String classesName;//线下班次名称
	private String classesTime;//线下班次时间
	private String classesAddress;//线下班次地点
	private String classesSignUpTime;//线下班次报名时间
	private Date classesBeginTime;//线下班次开始时间
	private Date classesEndTime;//线下班次结束时间
	private Date classesSignTime;//签到时间
	private String classesIsSign;//班次签到  是 否 判断签到时间是否null
	/*评论*/
	private Date commentDate;//评论时间
	private Double score;//课程评分
	private String courseGradeDimensionsC1;//课程维度1评分
	private String courseGradeDimensionsC2;//课程维度2评分
	private String courseGradeDimensionsC3;//课程维度3评分
	private String courseGradeDimensionsC4;//课程维度4评分
	private String courseGradeDimensionsC5;//课程维度5评分
	private String oneCourseCommentContent;//课程评论内容
	private String favoriteStatus;//收藏 ， Y 收藏  N 取消收藏
	private String thumbUpStatus;//点赞 ， Y 点赞  N 取消点赞
	
	
	
	
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
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOneDeptCode() {
		return oneDeptCode;
	}
	public void setOneDeptCode(String oneDeptCode) {
		this.oneDeptCode = oneDeptCode;
	}
	public String getOneDeptName() {
		return oneDeptName;
	}
	public void setOneDeptName(String oneDeptName) {
		this.oneDeptName = oneDeptName;
	}
	public String getTwoDeptCode() {
		return twoDeptCode;
	}
	public void setTwoDeptCode(String twoDeptCode) {
		this.twoDeptCode = twoDeptCode;
	}
	public String getTwoDeptName() {
		return twoDeptName;
	}
	public void setTwoDeptName(String twoDeptName) {
		this.twoDeptName = twoDeptName;
	}
	public String getThreeDeptCode() {
		return threeDeptCode;
	}
	public void setThreeDeptCode(String threeDeptCode) {
		this.threeDeptCode = threeDeptCode;
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
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
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
	public Integer getEcurrency() {
		return ecurrency;
	}
	public void setEcurrency(Integer ecurrency) {
		this.ecurrency = ecurrency;
	}
	public String getOpenCourse() {
		return openCourse;
	}
	public void setOpenCourse(String openCourse) {
		this.openCourse = openCourse;
	}
	public String getCourseOnline() {
		return courseOnline;
	}
	public void setCourseOnline(String courseOnline) {
		this.courseOnline = courseOnline;
	}
	
	public String getSectionClassifyCode() {
		return sectionClassifyCode;
	}
	public void setSectionClassifyCode(String sectionClassifyCode) {
		this.sectionClassifyCode = sectionClassifyCode;
	}
	public String getSectionClassifyName() {
		return sectionClassifyName;
	}
	public void setSectionClassifyName(String sectionClassifyName) {
		this.sectionClassifyName = sectionClassifyName;
	}
	public String getActivityNumber() {
		return activityNumber;
	}
	public void setActivityNumber(String activityNumber) {
		this.activityNumber = activityNumber;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getParentProjectClassifyName() {
		return parentProjectClassifyName;
	}
	public void setParentProjectClassifyName(String parentProjectClassifyName) {
		this.parentProjectClassifyName = parentProjectClassifyName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getNeedApply() {
		return needApply;
	}
	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}
	public Date getApplicationStartTime() {
		return applicationStartTime;
	}
	public void setApplicationStartTime(Date applicationStartTime) {
		this.applicationStartTime = applicationStartTime;
	}
	public Date getActivityStartTime() {
		return activityStartTime;
	}
	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}
	public Date getActivityEndTime() {
		return activityEndTime;
	}
	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getFirstVisitTime() {
		return firstVisitTime;
	}
	public void setFirstVisitTime(String firstVisitTime) {
		this.firstVisitTime = firstVisitTime;
	}
	public String getLearningCount() {
		return learningCount;
	}
	public void setLearningCount(String learningCount) {
		this.learningCount = learningCount;
	}
	public Double getCourseTimes() {
		if(courseTimes!=null && courseTimes!=0){
			return  Double.parseDouble(new DecimalFormat("0.00").format((double)courseTimes/60));
		}
		return courseTimes;
	}
	public void setCourseTimes(Double courseTimes) {
		this.courseTimes = courseTimes;
	}
	public String getLearningCourseTimes() {
		if(learningCourseTimes!=null && !"".equals(learningCourseTimes)){
			return new DecimalFormat("0.00").format((double)Double.parseDouble(learningCourseTimes)/60);
		}
		return learningCourseTimes;
	}
	public void setLearningCourseTimes(String learningCourseTimes) {
		this.learningCourseTimes = learningCourseTimes;
	}
	public String getLearningrate() {
		if(learningrate==null || learningrate.trim().equals("")){
			return "0.00%";
		}
		return new DecimalFormat("##0.00").format((double)Double.parseDouble(learningrate)*100)+"%";
	}
	public void setLearningrate(String learningrate) {
		this.learningrate = learningrate;
	}
	public String getClassesSignUp() {
		return classesSignUp;
	}
	public void setClassesSignUp(String classesSignUp) {
		this.classesSignUp = classesSignUp;
	}
	public String getClassesName() {
		return classesName;
	}
	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}
	public String getClassesTime() {
		return classesTime;
	}
	public void setClassesTime(String classesTime) {
		this.classesTime = classesTime;
	}
	public String getClassesAddress() {
		return classesAddress;
	}
	public void setClassesAddress(String classesAddress) {
		this.classesAddress = classesAddress;
	}
	public String getClassesSignUpTime() {
		return classesSignUpTime;
	}
	public void setClassesSignUpTime(String classesSignUpTime) {
		this.classesSignUpTime = classesSignUpTime;
	}
	
	public Date getClassesBeginTime() {
		return classesBeginTime;
	}
	public void setClassesBeginTime(Date classesBeginTime) {
		this.classesBeginTime = classesBeginTime;
	}
	public Date getClassesEndTime() {
		return classesEndTime;
	}
	public void setClassesEndTime(Date classesEndTime) {
		this.classesEndTime = classesEndTime;
	}
	public Date getClassesSignTime() {
		return classesSignTime;
	}
	public void setClassesSignTime(Date classesSignTime) {
		this.classesSignTime = classesSignTime;
	}
	public String getClassesIsSign() {
		if(getClassesSignTime()!=null && !"".equals(getClassesSignTime())){
			return "是";
		}
		return "否";
	}
	public void setClassesIsSign(String classesIsSign) {
		this.classesIsSign = classesIsSign;
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
	public String getOneCourseCommentContent() {
		return oneCourseCommentContent;
	}
	public void setOneCourseCommentContent(String oneCourseCommentContent) {
		this.oneCourseCommentContent = oneCourseCommentContent;
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
	
}
