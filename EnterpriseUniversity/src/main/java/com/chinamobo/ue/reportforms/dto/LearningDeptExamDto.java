package com.chinamobo.ue.reportforms.dto;

import java.util.Date;

public class LearningDeptExamDto {
	private String code;//用户名
	private String name;//姓名
	private String orgName;//所属组织
	private String oneDeptName;//一级部门
	private String twoDeptName;//二级部门
	private String threeDeptName;//三级部门
	private String secretEmail;//邮箱
	private String isAdmin;//管理角色分配
	private String examName;//资源名称
	private String examNumber;//资源编号
	private Integer examScore;//考试及格分
	private Integer examinationScore;//考试总分
	private Integer passEb;//资源积分
	private Integer firstTotalPoints;//首次考试成绩
	private Integer totalPoints;//考试成绩
	private String gradeState;//是否及格
	private Integer examCount;//考试次数
	//学习状态
	private Date examTotalTime;//完成时间
	private String getEb;//获得积分
	private String theActivityName;//所属活动名称
	private String parentProjectClassifyName;//活动类别
	private String activityNumber;//活动编号
	private Date createTime;//活动创建日期
	private String isRequired;//必修/选修
	private String needApply;//是否报名
	private Date applicationStartTime;//活动报名时间
	private Date activityStartTime;//活动开始时间
	private Date activityEndTime;//活动结束时间
	private Date protocolStartTime;//授权日期
	
	private Integer examId;
	private String examType;
	private Date examCreateTime;
	private Integer notPassEb;
	
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
	public String getSecretEmail() {
		return secretEmail;
	}
	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getExamNumber() {
		return examNumber;
	}
	public void setExamNumber(String examNumber) {
		this.examNumber = examNumber;
	}
	public Integer getExamScore() {
		return examScore;
	}
	public void setExamScore(Integer examScore) {
		this.examScore = examScore;
	}
	public Integer getExaminationScore() {
		return examinationScore;
	}
	public void setExaminationScore(Integer examinationScore) {
		this.examinationScore = examinationScore;
	}
	public Integer getPassEb() {
		return passEb;
	}
	public void setPassEb(Integer passEb) {
		this.passEb = passEb;
	}
	public Integer getFirstTotalPoints() {
		return firstTotalPoints;
	}
	public void setFirstTotalPoints(Integer firstTotalPoints) {
		this.firstTotalPoints = firstTotalPoints;
	}
	public Integer getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}
	public String getGradeState() {
		return gradeState;
	}
	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}
	public Integer getExamCount() {
		return examCount;
	}
	public void setExamCount(Integer examCount) {
		this.examCount = examCount;
	}
	public Date getExamTotalTime() {
		return examTotalTime;
	}
	public void setExamTotalTime(Date examTotalTime) {
		this.examTotalTime = examTotalTime;
	}
	public String getGetEb() {
		return getEb;
	}
	public void setGetEb(String getEb) {
		this.getEb = getEb;
	}
	public String getTheActivityName() {
		return theActivityName;
	}
	public void setTheActivityName(String theActivityName) {
		this.theActivityName = theActivityName;
	}
	public String getParentProjectClassifyName() {
		return parentProjectClassifyName;
	}
	public void setParentProjectClassifyName(String parentProjectClassifyName) {
		this.parentProjectClassifyName = parentProjectClassifyName;
	}
	public String getActivityNumber() {
		return activityNumber;
	}
	public void setActivityNumber(String activityNumber) {
		this.activityNumber = activityNumber;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
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
	public Date getProtocolStartTime() {
		return protocolStartTime;
	}
	public void setProtocolStartTime(Date protocolStartTime) {
		this.protocolStartTime = protocolStartTime;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public String getExamType() {
		return examType;
	}
	public void setExamType(String examType) {
		this.examType = examType;
	}
	public Date getExamCreateTime() {
		return examCreateTime;
	}
	public void setExamCreateTime(Date examCreateTime) {
		this.examCreateTime = examCreateTime;
	}
	public Integer getNotPassEb() {
		return notPassEb;
	}
	public void setNotPassEb(Integer notPassEb) {
		this.notPassEb = notPassEb;
	}
	
}
