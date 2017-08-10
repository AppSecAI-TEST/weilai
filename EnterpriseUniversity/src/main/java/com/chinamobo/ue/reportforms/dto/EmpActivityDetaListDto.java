package com.chinamobo.ue.reportforms.dto;

import java.util.Date;

public class EmpActivityDetaListDto {
	
	private int openNumber;//总人数
	
	public int getOpenNumber() {
		return openNumber;
	}
	public void setOpenNumber(int openNumber) {
		this.openNumber = openNumber;
	}
	private int courseId;//活动内课程数
	private int activityId;
	private String code;//用户名
	private String name;//姓名
	private String orgName;//所属组织
	private String oneDeptName;//一级部门
	private String twoDeptName;//二级部门
	private String threeDeptName;//三级部门
	private String isAdmin;//管理角色分配
	private String jobName;//职务
	private String secretEmail;//邮箱
	
	private String activityNumber;//活动编号
	
	private String activityName;//活动名称
	private String activityType;//活动类别
	private String isRequired;//选修必修(活动性质)
	private String status;//活动状态（进行中/已结束）
	private Date activityFinishTime;//活动完成时间
	private Date activityFinishTime1;
	private Date activityStartTime ;//活动开始时间
	private Date activityEndTime;//活动结束时间
	private Date protocolStartTime;//授权日期
	private String admins;//管理授权
	private Date fristJoinTime;//首次访问时间
	private long totalTime;//活动所需时长
	private double studyTime;//活动学习时长
	private  String  learnProgress;//活动学习进度
	private int onlineCourse;//活动中线上课程数
	private int offlineCourse;//活动中线下课程数
	private Integer offlineCourseApply;//活动中线下课程报名数
	private int onlineExam;//活动中线上考试
	private String needApply;//是否需要报名
	private String totalIntegral;//目标总积分
	private int  integral;//获得积分
	private int requireIntegral;//必修活动积分
	private int optionIntegral;//选修活动积分
	private Double averageScore;//平均成绩
	private String averageCourseProgress;//课程平均学习进度
	private String offlineCourseApplyRate;//线下课程报名率
	private String offilneCourseSignRate;//线下课程签到率
	
	private String depttopcode;//上级部门
	private String deptcode;//当前部门
	private String depttopname;//上级部门
	private String deptname;//当前部门
	private String onedeptcode;
	
	private Integer totalNum;//活动中的所有的课程和考试
	private Integer studyNum;//完成的课程和考试
	private Date signFinishTime;//线下课程签到时间
	private Date signFinishTime1;
	private int offilneCompleteCourse;//线下课程完成数
	private int onlineCompleteCourse;//线上课程完成数
	private int onlineCompleteExam;//线上考试完成数
	private String  avgPassRate;//考试平均通过率
	private int activityExamCount;//活动中的考试数
	
	private int courseLearning;//完成的课程数
	private int num;
	
	
	

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Date getActivityFinishTime1() {
		return activityFinishTime1;
	}
	public void setActivityFinishTime1(Date activityFinishTime1) {
		this.activityFinishTime1 = activityFinishTime1;
	}
	public Date getSignFinishTime1() {
		return signFinishTime1;
	}
	public void setSignFinishTime1(Date signFinishTime1) {
		this.signFinishTime1 = signFinishTime1;
	}
	public int getCourseLearning() {
		return courseLearning;
	}
	public void setCourseLearning(int courseLearning) {
		this.courseLearning = courseLearning;
	}
	public int getActivityExamCount() {
		return activityExamCount;
	}
	public void setActivityExamCount(int activityExamCount) {
		this.activityExamCount = activityExamCount;
	}
	public int getOnlineCompleteExam() {
		return onlineCompleteExam;
	}
	public void setOnlineCompleteExam(int onlineCompleteExam) {
		this.onlineCompleteExam = onlineCompleteExam;
	}

	public String getAvgPassRate() {
		return avgPassRate;
	}
	public void setAvgPassRate(String avgPassRate) {
		this.avgPassRate = avgPassRate;
	}
	public int getOffilneCompleteCourse() {
		return offilneCompleteCourse;
	}
	public void setOffilneCompleteCourse(int offilneCompleteCourse) {
		this.offilneCompleteCourse = offilneCompleteCourse;
	}
	public int getOnlineCompleteCourse() {
		return onlineCompleteCourse;
	}
	public void setOnlineCompleteCourse(int onlineCompleteCourse) {
		this.onlineCompleteCourse = onlineCompleteCourse;
	}
	public Date getSignFinishTime() {
		return signFinishTime;
	}
	public void setSignFinishTime(Date signFinishTime) {
		this.signFinishTime = signFinishTime;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public Integer getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	public Integer getStudyNum() {
		return studyNum;
	}
	public void setStudyNum(Integer studyNum) {
		this.studyNum = studyNum;
	}
	public String getDepttopcode() {
		return depttopcode;
	}
	public void setDepttopcode(String depttopcode) {
		this.depttopcode = depttopcode;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getDepttopname() {
		return depttopname;
	}
	public void setDepttopname(String depttopname) {
		this.depttopname = depttopname;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getOnedeptcode() {
		return onedeptcode;
	}
	public void setOnedeptcode(String onedeptcode) {
		this.onedeptcode = onedeptcode;
	}
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
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
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
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
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Date getActivityFinishTime() {
		return activityFinishTime;
	}
	public void setActivityFinishTime(Date activityFinishTime) {
		this.activityFinishTime = activityFinishTime;
	}
	public Date getProtocolStartTime() {
		return protocolStartTime;
	}
	public void setProtocolStartTime(Date protocolStartTime) {
		this.protocolStartTime = protocolStartTime;
	}
	public String getAdmins() {
		return admins;
	}
	public void setAdmins(String admins) {
		this.admins = admins;
	}
	public Date getFristJoinTime() {
		return fristJoinTime;
	}
	public void setFristJoinTime(Date fristJoinTime) {
		this.fristJoinTime = fristJoinTime;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	public double getStudyTime() {
		return studyTime;
	}
	public void setStudyTime(double studyTime) {
		this.studyTime = studyTime;
	}
	public String getLearnProgress() {
		return learnProgress;
	}
	public void setLearnProgress(String learnProgress) {
		this.learnProgress = learnProgress;
	}
	public int getOnlineCourse() {
		return onlineCourse;
	}
	public void setOnlineCourse(int onlineCourse) {
		this.onlineCourse = onlineCourse;
	}
	public int getOfflineCourse() {
		return offlineCourse;
	}
	public void setOfflineCourse(int offlineCourse) {
		this.offlineCourse = offlineCourse;
	}
	public Integer getOfflineCourseApply() {
		return offlineCourseApply;
	}
	public void setOfflineCourseApply(Integer offlineCourseApply) {
		this.offlineCourseApply = offlineCourseApply;
	}
	public int getOnlineExam() {
		return onlineExam;
	}
	public void setOnlineExam(int onlineExam) {
		this.onlineExam = onlineExam;
	}
	public String getNeedApply() {
		return needApply;
	}
	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}
	public String getTotalIntegral() {
		return totalIntegral;
	}
	public void setTotalIntegral(String totalIntegral) {
		this.totalIntegral = totalIntegral;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getRequireIntegral() {
		return requireIntegral;
	}
	public void setRequireIntegral(int requireIntegral) {
		this.requireIntegral = requireIntegral;
	}
	public int getOptionIntegral() {
		return optionIntegral;
	}
	public void setOptionIntegral(int optionIntegral) {
		this.optionIntegral = optionIntegral;
	}
	public Double getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}
	public String getAverageCourseProgress() {
		return averageCourseProgress;
	}
	public void setAverageCourseProgress(String averageCourseProgress) {
		this.averageCourseProgress = averageCourseProgress;
	}
	public String getOfflineCourseApplyRate() {
		return offlineCourseApplyRate;
	}
	public void setOfflineCourseApplyRate(String offlineCourseApplyRate) {
		this.offlineCourseApplyRate = offlineCourseApplyRate;
	}
	public String getOffilneCourseSignRate() {
		return offilneCourseSignRate;
	}
	public void setOffilneCourseSignRate(String offilneCourseSignRate) {
		this.offilneCourseSignRate = offilneCourseSignRate;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	
	
}
