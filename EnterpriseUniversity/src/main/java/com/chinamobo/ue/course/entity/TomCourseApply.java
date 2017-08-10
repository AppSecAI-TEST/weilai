package com.chinamobo.ue.course.entity;

import java.util.Date;

public class TomCourseApply {
    private Integer id;

    private Integer courseId;

    private Integer courseClasses;

    private String code;

    private String status;

    private Date applyTime;

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

    public Integer getCourseClasses() {
        return courseClasses;
    }

    public void setCourseClasses(Integer courseClasses) {
        this.courseClasses = courseClasses;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
}