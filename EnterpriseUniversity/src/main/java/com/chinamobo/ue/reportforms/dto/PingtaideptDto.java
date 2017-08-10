package com.chinamobo.ue.reportforms.dto;

public class PingtaideptDto {
	//com.chinamobo.ue.reportforms.dto.PingtaideptDto
	
	private Integer empall;//总用户数
	
	private Long learnAlltime;//总学时时长
	private double avglearntime;//人均学时
	private Integer logincount;//登录人数
	private Integer h5logincount;//移动端登录人数
	private Integer offlinlogin;//未上线人数
	private String  onlinerate;//上线率
	
	private Integer allEb;
	private double avgempEb;//平均获得学分
	private Integer allactivity;//总活动数
	private Integer finishActivity;//完成活动数
	private String avgActivefinrate;//活动平均完成率
	private Integer allcourse;//总课程数
	private Integer finishCourse;//完成课程数
	private String  avgfinCourserate;//课程平均完成率
	public Integer getEmpall() {
		return empall;
	}
	public void setEmpall(Integer empall) {
		this.empall = empall;
	}
	public Long getLearnAlltime() {
		return learnAlltime;
	}
	public void setLearnAlltime(Long learnAlltime) {
		this.learnAlltime = learnAlltime;
	}
	public double getAvglearntime() {
		return avglearntime;
	}
	public void setAvglearntime(double avglearntime) {
		this.avglearntime = avglearntime;
	}
	public Integer getLogincount() {
		return logincount;
	}
	public void setLogincount(Integer logincount) {
		this.logincount = logincount;
	}
	public Integer getOfflinlogin() {
		return offlinlogin;
	}
	public void setOfflinlogin(Integer offlinlogin) {
		this.offlinlogin = offlinlogin;
	}
	public String getOnlinerate() {
		return onlinerate;
	}
	public void setOnlinerate(String onlinerate) {
		this.onlinerate = onlinerate;
	}
	public Integer getAllEb() {
		return allEb;
	}
	public void setAllEb(Integer allEb) {
		this.allEb = allEb;
	}
	public double getAvgempEb() {
		return avgempEb;
	}
	public void setAvgempEb(double avgempEb) {
		this.avgempEb = avgempEb;
	}
	public Integer getAllactivity() {
		return allactivity;
	}
	public void setAllactivity(Integer allactivity) {
		this.allactivity = allactivity;
	}
	public Integer getFinishActivity() {
		return finishActivity;
	}
	public void setFinishActivity(Integer finishActivity) {
		this.finishActivity = finishActivity;
	}
	public String getAvgActivefinrate() {
		return avgActivefinrate;
	}
	public void setAvgActivefinrate(String avgActivefinrate) {
		this.avgActivefinrate = avgActivefinrate;
	}
	public Integer getAllcourse() {
		return allcourse;
	}
	public void setAllcourse(Integer allcourse) {
		this.allcourse = allcourse;
	}
	public Integer getFinishCourse() {
		return finishCourse;
	}
	public void setFinishCourse(Integer finishCourse) {
		this.finishCourse = finishCourse;
	}
	public String getAvgfinCourserate() {
		return avgfinCourserate;
	}
	public void setAvgfinCourserate(String avgfinCourserate) {
		this.avgfinCourserate = avgfinCourserate;
	}
	public Integer getH5logincount() {
		return h5logincount;
	}
	public void setH5logincount(Integer h5logincount) {
		this.h5logincount = h5logincount;
	}
	
	
}
