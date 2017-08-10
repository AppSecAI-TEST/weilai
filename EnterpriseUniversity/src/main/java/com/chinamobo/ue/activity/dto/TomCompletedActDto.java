package com.chinamobo.ue.activity.dto;

public class TomCompletedActDto {
	
	private String code;
	
	private String name;
	
	private String deptname;
	
	private String jobname;
	
	private String secretEmail;
	
	private String mobile;
	
	private int courseId;
	
	private int totalTask;//总任务数
	
	private int completedTask;//完成任务数
	
	private int learningCourse;//学过课程数
	
	private int passedExam;//通过考试数

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getTotalTask() {
		return totalTask;
	}

	public void setTotalTask(int totalTask) {
		this.totalTask = totalTask;
	}

	public int getCompletedTask() {
		return completedTask;
	}

	public void setCompletedTask(int completedTask) {
		this.completedTask = completedTask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getSecretEmail() {
		return secretEmail;
	}

	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getLearningCourse() {
		return learningCourse;
	}

	public void setLearningCourse(int learningCourse) {
		this.learningCourse = learningCourse;
	}

	public int getPassedExam() {
		return passedExam;
	}

	public void setPassedExam(int passedExam) {
		this.passedExam = passedExam;
	}
	

}
