package com.chinamobo.ue.course.dto;

import java.util.List;

import com.chinamobo.ue.course.entity.TomCourseApply;
import com.chinamobo.ue.course.entity.TomCourseClasses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.course.entity.TomOfflineSign;

public class OfflineCourseDto extends CourseJsonDto{

	private String courseDetail;
	private String courseDetailEn;
	private List<TomCourseApply> applyList;
	private List<TomOfflineSign> signList;
	private List<TomCourseClasses> classesList;
	private List<TomLecturer> lecturerList;
	private String needApply;
	
	
	public String getCourseDetailEn() {
		return courseDetailEn;
	}
	public void setCourseDetailEn(String courseDetailEn) {
		this.courseDetailEn = courseDetailEn;
	}
	public String getNeedApply() {
		return needApply;
	}
	public void setNeedApply(String needApply) {
		this.needApply = needApply;
	}
	public List<TomLecturer> getLecturerList() {
		return lecturerList;
	}
	public void setLecturerList(List<TomLecturer> lecturerList) {
		this.lecturerList = lecturerList;
	}
	public String getCourseDetail() {
		return courseDetail;
	}	
	public void setCourseDetail(String courseDetail) {
		this.courseDetail = courseDetail;
	}
	public List<TomCourseApply> getApplyList() {
		return applyList;
	}
	public void setApplyList(List<TomCourseApply> applyList) {
		this.applyList = applyList;
	}
	public List<TomOfflineSign> getSignList() {
		return signList;
	}
	public void setSignList(List<TomOfflineSign> signList) {
		this.signList = signList;
	}
	public List<TomCourseClasses> getClassesList() {
		return classesList;
	}
	public void setClassesList(List<TomCourseClasses> classesList) {
		this.classesList = classesList;
	}
	
}
