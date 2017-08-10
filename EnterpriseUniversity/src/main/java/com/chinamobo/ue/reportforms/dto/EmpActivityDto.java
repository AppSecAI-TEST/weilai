package com.chinamobo.ue.reportforms.dto;

public class EmpActivityDto {
	//用户信息
	private String code;//用户名
	private String name;//姓名
	private String orgName;//所属组织
	private String oneDeptName;//一级部门
	private String twoDeptName;//二级部门
	private String threeDeptName;//三级部门
	private String isAdmin;//管理角色分配
	private String secretEmail;//邮箱
	//平台统计
	private String recentLandingTime;//最近登陆时间
	private Integer totalNumberOfLandings;//总登录次数
	private Double totalLengthOfStudy;//总学习时长
	private Double averageLengthOfLearning;//平均学习时长
	private String averageLearningProgress;//活动平均进度
	//活动统计
	private Integer totalActivity;//总活动数
	private String activityParticipationRate;//活动参与率
	private String activityCompletionRate;//活动完成率
	private Integer numberOfOnlineCoursesCompleted;//活动内完成线上课程数
	private Integer numberOfCoursesCompletedInTheEvent;//活动内完成线下课程数
	private Integer numberOfOnlineExamsCompleted;//活动内完成线上考试数
	private String averagePassRate;//平均及格率
	//必修活动统计
	private Integer requiredActivity;//必修活动数
	private Integer numberOfRequiredActivitiesToStartLearning;//进行中的必修活动数
	private String requiredActivitySchedule;//必修活动平均进度
	private Integer numberOfRequiredActivitiesCompleted;//已完成必修活动数量
	private String requiredParticipationRate;//必修活动参与率
	private String requiredCompletionRate;//必修活动平均完成率
	private Integer compulsoryExaminationQuantity;//必修活动考试数量
	private Double compulsoryExaminationAverageScore;//必修活动考试平均成绩
	private Double compulsoryExaminationAverage;//必修活动考试平均分
	private String requiredExaminationPassRate;//必修活动考试及格率
	private Integer requiredActivityPoints;//必修活动积分
	private Integer requiredActivitiesToGetPoints;//必修活动获得积分
	//选修活动统计
	private Integer numberOfOpenElectiveActivities;//开放选修活动数
	private Integer numberOfEnrollment;//报名选修活动数
	private String averageEnrollmentRate;//选修活动平均报名率
	private Integer numberOfElectivesThatHaveBegunToLearn;//进行中的选修活动数量
	private String averageActivitySchedule;//选修活动平均进度
	private Integer numberOfElectiveActivitiesCompleted;//已完成选修活动数量
	private String participationRate;//选修活动参与率
	private String averageCompletionRate;//选修活动平均完成率
	private Integer numberOfElectives;//选修活动考试数量
	private Double averageScoreOfElectiveActivities;//选修活动考试平均成绩
	private Double electiveExaminationAverage;//选修活动考试平均分
	private String electiveExaminationPassRate;//选修活动考试及格率
	private Integer optionalActivityPoints;//选修活动积分
	private Integer electiveActivitiesGetPoints;//选修活动获得积分
	
	private Integer requiredExaminationNumber;//必修活动考试及格数量
	private Integer theNumberOfExamsForElectiveActivities;//选修修活动考试及格数量
	private String isRequired;//通用
	private Integer currencyCount;//通用
	private Integer activityId;//通用
	private Integer courseId;//通用
	private Integer examId;//通用
	private Integer examPassNum;
	
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
	public String getSecretEmail() {
		return secretEmail;
	}
	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}
	public String getRecentLandingTime() {
		return recentLandingTime;
	}
	public void setRecentLandingTime(String recentLandingTime) {
		this.recentLandingTime = recentLandingTime;
	}
	public Integer getTotalNumberOfLandings() {
		return totalNumberOfLandings;
	}
	public void setTotalNumberOfLandings(Integer totalNumberOfLandings) {
		this.totalNumberOfLandings = totalNumberOfLandings;
	}
	public Double getTotalLengthOfStudy() {
		return totalLengthOfStudy;
	}
	public void setTotalLengthOfStudy(Double totalLengthOfStudy) {
		this.totalLengthOfStudy = totalLengthOfStudy;
	}
	public Double getAverageLengthOfLearning() {
		return averageLengthOfLearning;
	}
	public void setAverageLengthOfLearning(Double averageLengthOfLearning) {
		this.averageLengthOfLearning = averageLengthOfLearning;
	}
	public String getAverageLearningProgress() {
		return averageLearningProgress;
	}
	public void setAverageLearningProgress(String averageLearningProgress) {
		this.averageLearningProgress = averageLearningProgress;
	}
	public Integer getTotalActivity() {
		return totalActivity;
	}
	public void setTotalActivity(Integer totalActivity) {
		this.totalActivity = totalActivity;
	}
	public String getActivityParticipationRate() {
		return activityParticipationRate;
	}
	public void setActivityParticipationRate(String activityParticipationRate) {
		this.activityParticipationRate = activityParticipationRate;
	}
	public String getActivityCompletionRate() {
		return activityCompletionRate;
	}
	public void setActivityCompletionRate(String activityCompletionRate) {
		this.activityCompletionRate = activityCompletionRate;
	}
	public Integer getNumberOfOnlineCoursesCompleted() {
		return numberOfOnlineCoursesCompleted;
	}
	public void setNumberOfOnlineCoursesCompleted(Integer numberOfOnlineCoursesCompleted) {
		this.numberOfOnlineCoursesCompleted = numberOfOnlineCoursesCompleted;
	}
	public Integer getNumberOfCoursesCompletedInTheEvent() {
		return numberOfCoursesCompletedInTheEvent;
	}
	public void setNumberOfCoursesCompletedInTheEvent(Integer numberOfCoursesCompletedInTheEvent) {
		this.numberOfCoursesCompletedInTheEvent = numberOfCoursesCompletedInTheEvent;
	}
	public Integer getNumberOfOnlineExamsCompleted() {
		return numberOfOnlineExamsCompleted;
	}
	public void setNumberOfOnlineExamsCompleted(Integer numberOfOnlineExamsCompleted) {
		this.numberOfOnlineExamsCompleted = numberOfOnlineExamsCompleted;
	}
	public String getAveragePassRate() {
		return averagePassRate;
	}
	public void setAveragePassRate(String averagePassRate) {
		this.averagePassRate = averagePassRate;
	}
	public Integer getRequiredActivity() {
		return requiredActivity;
	}
	public void setRequiredActivity(Integer requiredActivity) {
		this.requiredActivity = requiredActivity;
	}
	public Integer getNumberOfRequiredActivitiesToStartLearning() {
		return numberOfRequiredActivitiesToStartLearning;
	}
	public void setNumberOfRequiredActivitiesToStartLearning(Integer numberOfRequiredActivitiesToStartLearning) {
		this.numberOfRequiredActivitiesToStartLearning = numberOfRequiredActivitiesToStartLearning;
	}
	public String getRequiredActivitySchedule() {
		return requiredActivitySchedule;
	}
	public void setRequiredActivitySchedule(String requiredActivitySchedule) {
		this.requiredActivitySchedule = requiredActivitySchedule;
	}
	public Integer getNumberOfRequiredActivitiesCompleted() {
		return numberOfRequiredActivitiesCompleted;
	}
	public void setNumberOfRequiredActivitiesCompleted(Integer numberOfRequiredActivitiesCompleted) {
		this.numberOfRequiredActivitiesCompleted = numberOfRequiredActivitiesCompleted;
	}
	public String getRequiredParticipationRate() {
		return requiredParticipationRate;
	}
	public void setRequiredParticipationRate(String requiredParticipationRate) {
		this.requiredParticipationRate = requiredParticipationRate;
	}
	public String getRequiredCompletionRate() {
		return requiredCompletionRate;
	}
	public void setRequiredCompletionRate(String requiredCompletionRate) {
		this.requiredCompletionRate = requiredCompletionRate;
	}
	public Integer getCompulsoryExaminationQuantity() {
		return compulsoryExaminationQuantity;
	}
	public void setCompulsoryExaminationQuantity(Integer compulsoryExaminationQuantity) {
		this.compulsoryExaminationQuantity = compulsoryExaminationQuantity;
	}
	public Double getCompulsoryExaminationAverageScore() {
		return compulsoryExaminationAverageScore;
	}
	public void setCompulsoryExaminationAverageScore(Double compulsoryExaminationAverageScore) {
		this.compulsoryExaminationAverageScore = compulsoryExaminationAverageScore;
	}
	public Double getCompulsoryExaminationAverage() {
		return compulsoryExaminationAverage;
	}
	public void setCompulsoryExaminationAverage(Double compulsoryExaminationAverage) {
		this.compulsoryExaminationAverage = compulsoryExaminationAverage;
	}
	public String getRequiredExaminationPassRate() {
		return requiredExaminationPassRate;
	}
	public void setRequiredExaminationPassRate(String requiredExaminationPassRate) {
		this.requiredExaminationPassRate = requiredExaminationPassRate;
	}
	public Integer getRequiredActivityPoints() {
		return requiredActivityPoints;
	}
	public void setRequiredActivityPoints(Integer requiredActivityPoints) {
		this.requiredActivityPoints = requiredActivityPoints;
	}
	public Integer getRequiredActivitiesToGetPoints() {
		return requiredActivitiesToGetPoints;
	}
	public void setRequiredActivitiesToGetPoints(Integer requiredActivitiesToGetPoints) {
		this.requiredActivitiesToGetPoints = requiredActivitiesToGetPoints;
	}
	public Integer getNumberOfOpenElectiveActivities() {
		return numberOfOpenElectiveActivities;
	}
	public void setNumberOfOpenElectiveActivities(Integer numberOfOpenElectiveActivities) {
		this.numberOfOpenElectiveActivities = numberOfOpenElectiveActivities;
	}
	public Integer getNumberOfEnrollment() {
		return numberOfEnrollment;
	}
	public void setNumberOfEnrollment(Integer numberOfEnrollment) {
		this.numberOfEnrollment = numberOfEnrollment;
	}
	public String getAverageEnrollmentRate() {
		return averageEnrollmentRate;
	}
	public void setAverageEnrollmentRate(String averageEnrollmentRate) {
		this.averageEnrollmentRate = averageEnrollmentRate;
	}
	public Integer getNumberOfElectivesThatHaveBegunToLearn() {
		return numberOfElectivesThatHaveBegunToLearn;
	}
	public void setNumberOfElectivesThatHaveBegunToLearn(Integer numberOfElectivesThatHaveBegunToLearn) {
		this.numberOfElectivesThatHaveBegunToLearn = numberOfElectivesThatHaveBegunToLearn;
	}
	public String getAverageActivitySchedule() {
		return averageActivitySchedule;
	}
	public void setAverageActivitySchedule(String averageActivitySchedule) {
		this.averageActivitySchedule = averageActivitySchedule;
	}
	public Integer getNumberOfElectiveActivitiesCompleted() {
		return numberOfElectiveActivitiesCompleted;
	}
	public void setNumberOfElectiveActivitiesCompleted(Integer numberOfElectiveActivitiesCompleted) {
		this.numberOfElectiveActivitiesCompleted = numberOfElectiveActivitiesCompleted;
	}
	public String getParticipationRate() {
		return participationRate;
	}
	public void setParticipationRate(String participationRate) {
		this.participationRate = participationRate;
	}
	public String getAverageCompletionRate() {
		return averageCompletionRate;
	}
	public void setAverageCompletionRate(String averageCompletionRate) {
		this.averageCompletionRate = averageCompletionRate;
	}
	public Integer getNumberOfElectives() {
		return numberOfElectives;
	}
	public void setNumberOfElectives(Integer numberOfElectives) {
		this.numberOfElectives = numberOfElectives;
	}
	public Double getAverageScoreOfElectiveActivities() {
		return averageScoreOfElectiveActivities;
	}
	public void setAverageScoreOfElectiveActivities(Double averageScoreOfElectiveActivities) {
		this.averageScoreOfElectiveActivities = averageScoreOfElectiveActivities;
	}
	public Double getElectiveExaminationAverage() {
		return electiveExaminationAverage;
	}
	public void setElectiveExaminationAverage(Double electiveExaminationAverage) {
		this.electiveExaminationAverage = electiveExaminationAverage;
	}
	public String getElectiveExaminationPassRate() {
		return electiveExaminationPassRate;
	}
	public void setElectiveExaminationPassRate(String electiveExaminationPassRate) {
		this.electiveExaminationPassRate = electiveExaminationPassRate;
	}
	public Integer getOptionalActivityPoints() {
		return optionalActivityPoints;
	}
	public void setOptionalActivityPoints(Integer optionalActivityPoints) {
		this.optionalActivityPoints = optionalActivityPoints;
	}
	public Integer getElectiveActivitiesGetPoints() {
		return electiveActivitiesGetPoints;
	}
	public void setElectiveActivitiesGetPoints(Integer electiveActivitiesGetPoints) {
		this.electiveActivitiesGetPoints = electiveActivitiesGetPoints;
	}
	public Integer getRequiredExaminationNumber() {
		return requiredExaminationNumber;
	}
	public void setRequiredExaminationNumber(Integer requiredExaminationNumber) {
		this.requiredExaminationNumber = requiredExaminationNumber;
	}
	public Integer getTheNumberOfExamsForElectiveActivities() {
		return theNumberOfExamsForElectiveActivities;
	}
	public void setTheNumberOfExamsForElectiveActivities(Integer theNumberOfExamsForElectiveActivities) {
		this.theNumberOfExamsForElectiveActivities = theNumberOfExamsForElectiveActivities;
	}
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	public Integer getCurrencyCount() {
		return currencyCount;
	}
	public void setCurrencyCount(Integer currencyCount) {
		this.currencyCount = currencyCount;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Integer getExamId() {
		return examId;
	}
	public void setExamId(Integer examId) {
		this.examId = examId;
	}
	public Integer getExamPassNum() {
		return examPassNum;
	}
	public void setExamPassNum(Integer examPassNum) {
		this.examPassNum = examPassNum;
	}
	
}
