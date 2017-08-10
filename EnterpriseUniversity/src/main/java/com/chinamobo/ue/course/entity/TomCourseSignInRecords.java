package com.chinamobo.ue.course.entity;

import java.util.Date;

public class TomCourseSignInRecords {
    private Integer courseId;

    private String code;

    private Date createDate;
    
    private Integer activityId;
    
    private Date beginTime;
    
    private Date endTime;
    private String classesid;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getClassesid() {
		return classesid;
	}

	public void setClassesid(String classesid) {
		this.classesid = classesid;
	}
    
}