package com.chinamobo.ue.reportforms.dto;

public class ParamterDto {

	private String startTime;
	private String endTime;
	private String org;
	private String onedeptId;
	private String deptId;
	private String thirdcode;
	private String name;
	private String userName;
	
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public String getOnedeptId() {
		return onedeptId;
	}
	public void setOnedeptId(String onedeptId) {
		this.onedeptId = onedeptId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getThirdcode() {
		return thirdcode;
	}
	public void setThirdcode(String thirdcode) {
		this.thirdcode = thirdcode;
	}
	
	
}
