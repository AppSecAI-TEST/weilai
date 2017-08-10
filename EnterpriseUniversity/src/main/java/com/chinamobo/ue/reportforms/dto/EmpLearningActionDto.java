package com.chinamobo.ue.reportforms.dto;

public class EmpLearningActionDto {
	private String code;
	private String name;
	private String orgname;//所属组织
	private String deptname;//当前部门
	private String depttopname;//上级部门
	private String thirdName;//三级部门
	private String isAdmin;//管理角色分配（人员类别）
	private String secretEmail;
	private String onLineTime;//在线时长x
	private int learningTime;
	private String addUpENumber;//积分（E币）
	private int sumSignInTimes;//登陆次数（累计签到）
	private int sumSignInTimesH5;//微信端登陆次数（累计签到）
	private int loadTime;//登录时长x
	private int loadTimeH5;//微信端登录时长x
	private int eNumber;
	private String twoName;//一级部门
	private String threeName;//二级部门
	private String onedeptname;//表字段一级部门
	private String mobile;
	private String jobname;
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
	
	public String getThirdName() {
		return thirdName;
	}
	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
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
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getSecretEmail() {
		return secretEmail;
	}
	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}
	public String getOnLineTime() {
		return onLineTime;
	}
	public void setOnLineTime(String onLineTime) {
		this.onLineTime = onLineTime;
	}
	
	public int getLearningTime() {
		return learningTime;
	}
	public void setLearningTime(int learningTime) {
		this.learningTime = learningTime;
	}
	public String getAddUpENumber() {
		return addUpENumber;
	}
	public void setAddUpENumber(String addUpENumber) {
		this.addUpENumber = addUpENumber;
	}
	public int getSumSignInTimes() {
		return sumSignInTimes;
	}
	public void setSumSignInTimes(int sumSignInTimes) {
		this.sumSignInTimes = sumSignInTimes;
	}
	public int getLoadTime() {
		return loadTime;
	}
	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public int geteNumber() {
		return eNumber;
	}
	public void seteNumber(int eNumber) {
		this.eNumber = eNumber;
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
	public String getOnedeptname() {
		return onedeptname;
	}
	public void setOnedeptname(String onedeptname) {
		this.onedeptname = onedeptname;
	}
	public int getSumSignInTimesH5() {
		return sumSignInTimesH5;
	}
	public void setSumSignInTimesH5(int sumSignInTimesH5) {
		this.sumSignInTimesH5 = sumSignInTimesH5;
	}
	public int getLoadTimeH5() {
		return loadTimeH5;
	}
	public void setLoadTimeH5(int loadTimeH5) {
		this.loadTimeH5 = loadTimeH5;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	 

	

}
