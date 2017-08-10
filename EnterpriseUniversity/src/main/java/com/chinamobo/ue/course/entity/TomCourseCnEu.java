package com.chinamobo.ue.course.entity;

public class TomCourseCnEu {
	private String userId;
	
	private Integer minTime;
	
	private Integer historyTime;
	
    private String courseId;
    
    private String sectionId;

    private String firstTime;

    private Integer learningNumber;

    private Integer learningTime;

    private String learningSchedule;

   
    public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime == null ? null : firstTime.trim();
    }

    public Integer getLearningNumber() {
        return learningNumber;
    }

    public void setLearningNumber(Integer learningNumber) {
        this.learningNumber = learningNumber;
    }

    public Integer getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(Integer learningTime) {
        this.learningTime = learningTime;
    }

    public String getLearningSchedule() {
        return learningSchedule;
    }

    public void setLearningSchedule(String learningSchedule) {
        this.learningSchedule = learningSchedule == null ? null : learningSchedule.trim();
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getMinTime() {
		return minTime;
	}

	public void setMinTime(Integer minTime) {
		this.minTime = minTime;
	}

	public Integer getHistoryTime() {
		return historyTime;
	}

	public void setHistoryTime(Integer historyTime) {
		this.historyTime = historyTime;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
    
    
}