package com.chinamobo.ue.activity.dto;

import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;


/**
 * 版本: [1.0]
 * 功能说明: 培训活动包
 *
 * 作者: Wanghb
 * 创建时间: 2016年3月16日 上午10:15:08
 */
public class TomActivityDto {
	
	@FormParam("parentClassifyId")
	private String parentClassifyId;
	
	@FormParam("parentProjectClassifyName")
    private String parentProjectClassifyName;
	
		public String getParentClassifyId() {
		return parentClassifyId;
	}
	public void setParentClassifyId(String parentClassifyId) {
		this.parentClassifyId = parentClassifyId;
	}
	public String getParentProjectClassifyName() {
		return parentProjectClassifyName;
	}
	public void setParentProjectClassifyName(String parentProjectClassifyName) {
		this.parentProjectClassifyName = parentProjectClassifyName;
	}

		@FormParam("cityEn")
		private String cityEn;
		@FormParam("city")
		private String city;
		@FormParam("packageId")
		private Integer packageId;
		@FormParam("introduction")
		private String introduction;
		@FormParam("activityName")
		private String activityName;
		@FormParam("preTaskInfo[]")
		private List<TomActivityPropertyDto> preTaskInfo;
		@FormParam("protocol")
		private String protocol;
		@FormParam("trainFee")
		private String trainFee;
		@FormParam("deptCodes")
	   	private String deptCodes;
		@FormParam("employeeGradeStr")
	   	private String employeeGradeStr;
		@FormParam("empIds[]")
	   	private List<String> empIds;
		@FormParam("empNames[]")
	   	private List<String> empNames;
		@FormParam("deptManagerIds[]")
	   	private List<String> deptManagerIds;
		@FormParam("deptManagerNames[]")
	   	private List<String> deptManagerNames;
		@FormParam("code[]")
	   	private List<String> code;
		@FormParam("admins")
	   	private String admins;
		@FormParam("activityImg")
		private String activityImg;
		@FormParam("activityImgEn")
		private String activityImgEn;
		@FormParam("isCN")
		private String isCN;
		@FormParam("isEN")
		private String isEN;
		
		public String getIsCN() {
			return isCN;
		}
		public void setIsCN(String isCN) {
			this.isCN = isCN;
		}
		public String getIsEN() {
			return isEN;
		}
		public void setIsEN(String isEN) {
			this.isEN = isEN;
		}
		public String getCityEn() {
			return cityEn;
		}
		public void setCityEn(String cityEn) {
			this.cityEn = cityEn;
		}
		public String getActivityImgEn() {
			return activityImgEn;
		}
		public void setActivityImgEn(String activityImgEn) {
			this.activityImgEn = activityImgEn;
		}

		@FormParam("needApply")
	   	private String needApply;
		@FormParam("numberOfParticipants")
	   	private String numberOfParticipants;
		@FormParam("applicationStartTime")
	   	private String applicationStartTime;
		@FormParam("applicationDeadline")
	   	private String applicationDeadline;
		@FormParam("activityStartTime")
	   	private String activityStartTime;
		@FormParam("activityEndTime")
	   	private String activityEndTime;
		@FormParam("protocolStartTime")
		private String protocolStartTime;
		@FormParam("protocolEndTime")
		private String protocolEndTime;
		@FormParam("isPrincipal")
		private String isPrincipal;
		@FormParam("deptManagers")
		private List<Map<String,String>> deptManagers;
		@FormParam("emps")
		private List<Map<String,String>> emps; 
		@FormParam("packages")
		private List<Map<String,Object>> packages;
		
		
		public List<Map<String, Object>> getPackages() {
			return packages;
		}
		public void setPackages(List<Map<String, Object>> packages) {
			this.packages = packages;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getIntroduction() {
			return introduction;
		}
		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}
		public String getActivityName() {
			return activityName;
		}
		public void setActivityName(String activityName) {
			this.activityName = activityName;
		}
		public List<TomActivityPropertyDto> getPreTaskInfo() {
			return preTaskInfo;
		}
		public void setPreTaskInfo(List<TomActivityPropertyDto> preTaskInfo) {
			this.preTaskInfo = preTaskInfo;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
		public String getTrainFee() {
			return trainFee;
		}
		public void setTrainFee(String trainFee) {
			this.trainFee = trainFee;
		}
		public String getDeptCodes() {
			return deptCodes;
		}
		public void setDeptCodes(String deptCodes) {
			this.deptCodes = deptCodes;
		}
		public String getEmployeeGradeStr() {
			return employeeGradeStr;
		}
		public void setEmployeeGradeStr(String employeeGradeStr) {
			this.employeeGradeStr = employeeGradeStr;
		}
		public List<String> getEmpIds() {
			return empIds;
		}
		public void setEmpIds(List<String> empIds) {
			this.empIds = empIds;
		}
		public List<String> getEmpNames() {
			return empNames;
		}
		public void setEmpNames(List<String> empNames) {
			this.empNames = empNames;
		}
		public List<String> getDeptManagerIds() {
			return deptManagerIds;
		}
		public void setDeptManagerIds(List<String> deptManagerIds) {
			this.deptManagerIds = deptManagerIds;
		}
		public List<String> getDeptManagerNames() {
			return deptManagerNames;
		}
		public void setDeptManagerNames(List<String> deptManagerNames) {
			this.deptManagerNames = deptManagerNames;
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
		public String getNumberOfParticipants() {
			return numberOfParticipants;
		}
		public void setNumberOfParticipants(String numberOfParticipants) {
			this.numberOfParticipants = numberOfParticipants;
		}
		public String getApplicationStartTime() {
			return applicationStartTime;
		}
		public void setApplicationStartTime(String applicationStartTime) {
			this.applicationStartTime = applicationStartTime;
		}
		public String getApplicationDeadline() {
			return applicationDeadline;
		}
		public void setApplicationDeadline(String applicationDeadline) {
			this.applicationDeadline = applicationDeadline;
		}
		public String getActivityStartTime() {
			return activityStartTime;
		}
		public void setActivityStartTime(String activityStartTime) {
			this.activityStartTime = activityStartTime;
		}
		public String getActivityEndTime() {
			return activityEndTime;
		}
		public void setActivityEndTime(String activityEndTime) {
			this.activityEndTime = activityEndTime;
		}
		public String getProtocolStartTime() {
			return protocolStartTime;
		}
		public void setProtocolStartTime(String protocolStartTime) {
			this.protocolStartTime = protocolStartTime;
		}
		public String getProtocolEndTime() {
			return protocolEndTime;
		}
		public void setProtocolEndTime(String protocolEndTime) {
			this.protocolEndTime = protocolEndTime;
		}
		public String getActivityImg() {
			return activityImg;
		}
		public void setActivityImg(String activityImg) {
			this.activityImg = activityImg;
		}
		public String getIsPrincipal() {
			return isPrincipal;
		}
		public void setIsPrincipal(String isPrincipal) {
			this.isPrincipal = isPrincipal;
		}
		public List<String> getCode() {
			return code;
		}
		public void setCode(List<String> code) {
			this.code = code;
		}
		public Integer getPackageId() {
			return packageId;
		}
		public void setPackageId(Integer packageId) {
			this.packageId = packageId;
		}
		public List<Map<String, String>> getDeptManagers() {
			return deptManagers;
		}
		public void setDeptManagers(List<Map<String, String>> deptManagers) {
			this.deptManagers = deptManagers;
		}
		public List<Map<String, String>> getEmps() {
			return emps;
		}
		public void setEmps(List<Map<String, String>> emps) {
			this.emps = emps;
		}
		
		@FormParam("introductionEn")
		private String introductionEn;
		@FormParam("activityNameEn")
		private String activityNameEn;


		public String getIntroductionEn() {
			return introductionEn;
		}
		public void setIntroductionEn(String introductionEn) {
			this.introductionEn = introductionEn;
		}
		public String getActivityNameEn() {
			return activityNameEn;
		}
		public void setActivityNameEn(String activityNameEn) {
			this.activityNameEn = activityNameEn;
		}
		
		
}