package com.chinamobo.ue.course.entity;

public class TomCourseDetail {
    private Integer id;

    private Integer courseId;

    private String courseDetail;
    
    private String courseDetailEn;
    
    

    public String getCourseDetailEn() {
		return courseDetailEn;
	}

	public void setCourseDetailEn(String courseDetailEn) {
		this.courseDetailEn = courseDetailEn;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail == null ? null : courseDetail.trim();
    }
}