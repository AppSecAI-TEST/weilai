package com.chinamobo.ue.course.entity;

import java.util.Date;

/**
 * 课程学习记录
 * @author ZXM
 *
 */
public class TomCourseLearningRecord {
	
	private Integer courseId;//课程id
	private String code;//用户code
	private Date learningDate;//学习日期
	private String name; //人名
	
	
	private String mindate;
	private String maxdate;
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
		this.code = code;
	}
	public Date getLearningDate() {
		return learningDate;
	}
	public void setLearningDate(Date learningDate) {
		this.learningDate = learningDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMindate() {
		return mindate;
	}
	public void setMindate(String mindate) {
		this.mindate = mindate;
	}
	public String getMaxdate() {
		return maxdate;
	}
	public void setMaxdate(String maxdate) {
		this.maxdate = maxdate;
	}
	
	

}
