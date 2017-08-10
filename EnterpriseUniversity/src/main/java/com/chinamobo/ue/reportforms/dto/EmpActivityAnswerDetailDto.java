package com.chinamobo.ue.reportforms.dto;

import java.util.Date;

public class EmpActivityAnswerDetailDto {
	private String code;
	private String name;
	private String orgname;//所属组织
	private String onedeptname;
	private String deptname;//当前部门
	private String depttopname;//上级部门
	private String twoName;
	private String threeName;//三级部门
	private String secretEmail;//邮箱
	private String activityNumber;//活动编号
	private String jobname;//职务
	private String isAdmin;//分配管理（是否是管理员）
	private String activityName;//活动名称
	
	private String activityType;//活动类别
	
	private int projectId;
	
	private String projectName;
	
	private Date createTime;//创建时间
	
	private String isRequired;//选修必修(活动性质)
	
	private Date activityStartTime ;//活动开始时间
	
	private Date activityEndTime;//活动结束时间
	
	private String status;//活动状态（进行中/已结束）
	
	private String activityStatus;//活动状态(sql)
	
	private String admins;//管理授权
	
	private String needApply;//是否需要报名
	
	private Integer activityId;//活动ID
	
	private Integer courseId;//课程ID
	
	private int examId;
	
	private String examName;
	
	private Integer topicId;
	
	private String topic;//题目
	
	private String answer;
	
	private String empAnswer;//客观题答案
	
	private String subjectiveItemAnswer;//主观题答案
	
	private String examType;//考试类型

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

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getDepttopname() {
		return depttopname;
	}

	public void setDepttopname(String depttopname) {
		this.depttopname = depttopname;
	}

	public String getTwoName() {
		return twoName;
	}

	public void setTwoName(String twoName) {
		this.twoName = twoName;
	}

	public String getThreeName() {
		return threeName;
	}

	public void setThreeName(String threeName) {
		this.threeName = threeName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getNeedApply() {
		return needApply;
	}

	public void setNeedApply(String needApply) {
		this.needApply = needApply;
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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getEmpAnswer() {
		return empAnswer;
	}

	public void setEmpAnswer(String empAnswer) {
		this.empAnswer = empAnswer;
	}

	public String getSubjectiveItemAnswer() {
		return subjectiveItemAnswer;
	}

	public void setSubjectiveItemAnswer(String subjectiveItemAnswer) {
		this.subjectiveItemAnswer = subjectiveItemAnswer;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getOnedeptname() {
		return onedeptname;
	}

	public void setOnedeptname(String onedeptname) {
		this.onedeptname = onedeptname;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	
	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}
	
	

}
