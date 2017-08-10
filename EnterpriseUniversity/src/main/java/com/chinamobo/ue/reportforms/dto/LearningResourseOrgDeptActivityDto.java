package com.chinamobo.ue.reportforms.dto;

import java.util.Date;

/**
 * 描述 [学习资源-组织部门报表统计（活动）]
 * 创建者 LXT
 * 创建时间 2017年3月21日 下午2:47:37
 */
public class LearningResourseOrgDeptActivityDto {
	private Integer activityId;//活动id
	private String activityNumber;//活动编号
	private String activityName;//活动名称
	private String activityType;//活动类别
	private Date createTime;//活动创建日期
	private String needApply;//活动报名  	Y：需要报名 选修		N：不需要报名 必修
	private Date applicationStartTime;//活动报名时间
	private Date activityStartTime;//活动开始时间
	private Date activityEndTime;//活动结束时间
	private Date updateTime;//授权日期
	private String admins;//管理员id	“,” 隔开
	private String adminNames;//管理员		“,” 隔开
	private String activityTotalEb;//活动积分
	private String activityEb;//活动获得积分
	
	
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
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

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getAdmins() {
		return admins;
	}
	public void setAdmins(String admins) {
		this.admins = admins;
	}
	public String getAdminNames() {
		return adminNames;
	}
	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}
	public String getActivityTotalEb() {
		return activityTotalEb;
	}
	public void setActivityTotalEb(String activityTotalEb) {
		this.activityTotalEb = activityTotalEb;
	}
	public String getActivityEb() {
		return activityEb;
	}
	public void setActivityEb(String activityEb) {
		this.activityEb = activityEb;
	}
	
	
}
