package com.chinamobo.ue.activity.dto;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomActivityEmpDto {
	
	@FormParam("name")
	private String name;
	@FormParam("code")
	private String code;
	@FormParam("secretEmail")
    private String secretEmail;
	@FormParam("officephone")
    private String officephone;
	@FormParam("deptname")
    private String deptname;
	@FormParam("postname")
    private String postname;
	@FormParam("jobname")
    private String jobname;
	private String needApply;
	private Date startTime;
	private Date endTime;
	private String admins;
	private String acitvityId;
	private String createDate;
	private String pelCount;
	private String loginCount;
	private String learnCount;
	private String mintime;
	private String timecount;
	public String getLearnCount() {
		return learnCount;
	}
	public void setLearnCount(String learnCount) {
		this.learnCount = learnCount;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPelCount() {
		return pelCount;
	}
	public void setPelCount(String pelCount) {
		this.pelCount = pelCount;
	}
	public String getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(String loginCount) {
		this.loginCount = loginCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSecretEmail() {
		return secretEmail;
	}
	public void setSecretEmail(String secretEmail) {
		this.secretEmail = secretEmail;
	}
	public String getOfficephone() {
		return officephone;
	}
	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getNeedApply() {
		return needApply;
	}
	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getAdmins() {
		return admins;
	}
	public void setAdmins(String admins) {
		this.admins = admins;
	}
	public String getAcitvityId() {
		return acitvityId;
	}
	public void setAcitvityId(String acitvityId) {
		this.acitvityId = acitvityId;
	}
	public String getMintime() {
		return mintime;
	}
	public void setMintime(String mintime) {
		this.mintime = mintime;
	}
	public String getTimecount() {
		return timecount;
	}
	public void setTimecount(String timecount) {
		this.timecount = timecount;
	}
	

}
