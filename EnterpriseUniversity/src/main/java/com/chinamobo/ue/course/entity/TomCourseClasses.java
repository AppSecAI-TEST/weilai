package com.chinamobo.ue.course.entity;

import java.util.Date;

public class TomCourseClasses {
    private Integer id;

    private Integer courseId;

    private Integer classes;

    private Date beginTime;

    private Date endTime;
    
    private String applyStatus;
    
    private String classesName;
    
    private String signStatus;
    
    private String classesAddress;
    
    private String classesNameEn;
    
    private String classesAddressEn;
    
    private String filepath;//临时用
    
    

    public String getClassesNameEn() {
		return classesNameEn;
	}

	public void setClassesNameEn(String classesNameEn) {
		this.classesNameEn = classesNameEn;
	}

	public String getClassesAddressEn() {
		return classesAddressEn;
	}

	public void setClassesAddressEn(String classesAddressEn) {
		this.classesAddressEn = classesAddressEn;
	}

	public String getClassesAddress() {
		return classesAddress;
	}

	public void setClassesAddress(String classesAddress) {
		this.classesAddress = classesAddress;
	}

	public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	public String getClassesName() {
		return classesName;
	}

	public void setClassesName(String classesName) {
		this.classesName = classesName;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
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

    public Integer getClasses() {
        return classes;
    }

    public void setClasses(Integer classes) {
        this.classes = classes;
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

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
    
}