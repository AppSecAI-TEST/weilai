package com.chinamobo.ue.course.dto;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

public class CopyOnlineCourse {
	private String pcen;
	private String pccn;
	private String h5en;
	private String h5cn;
	private String opennum;//临时字段-开放人数
	
	
	public String getOpennum() {
		return opennum;
	}

	public void setOpennum(String opennum) {
		this.opennum = opennum;
	}

	public String getPcen() {
		return pcen;
	}

	public void setPcen(String pcen) {
		this.pcen = pcen;
	}

	public String getPccn() {
		return pccn;
	}

	public void setPccn(String pccn) {
		this.pccn = pccn;
	}

	public String getH5en() {
		return h5en;
	}

	public void setH5en(String h5en) {
		this.h5en = h5en;
	}

	public String getH5cn() {
		return h5cn;
	}

	public void setH5cn(String h5cn) {
		this.h5cn = h5cn;
	}

	private int countclasses;
	

	@FormParam("parentProjectClassifyName")
    private String parentProjectClassifyName;
	
	@FormParam("parentClassifyId")
	private String parentClassifyId;
	
	private String sectionTypes;
	
	
	
	public String getSectionTypes() {
		return sectionTypes;
	}

	public void setSectionTypes(String sectionTypes) {
		this.sectionTypes = sectionTypes;
	}

	public String getParentProjectClassifyName() {
		return parentProjectClassifyName;
	}

	public void setParentProjectClassifyName(String parentProjectClassifyName) {
		this.parentProjectClassifyName = parentProjectClassifyName;
	}

	public String getParentClassifyId() {
		return parentClassifyId;
	}

	public void setParentClassifyId(String parentClassifyId) {
		this.parentClassifyId = parentClassifyId;
	}


	private String courseImgEn;
	
	public String getCourseImgEn() {
		return courseImgEn;
	}

	public void setCourseImgEn(String courseImgEn) {
		this.courseImgEn = courseImgEn;
	}

	private Integer countApply;
	public Integer getCountApply() {
		return countApply;
	}

	public void setCountApply(Integer countApply) {
		this.countApply = countApply;
	}

	@FormParam("courseId")
    private Integer courseId;

	@FormParam("projectId")
    private String projectId;
	
    public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_NUMBER
     *
     * @mbggenerated
     */
	@FormParam("courseNumber")
    private String courseNumber;

	private String isGrade;
	
	private Integer sectionNum;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_NAME
     *
     * @mbggenerated
     */
	@FormParam("courseName")
    private String courseName;

	@FormParam("courseNameEn")
    private String courseNameEn;
	
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_ONLINE
     *
     * @mbggenerated
     */
	@FormParam("courseOnline")
    private String courseOnline;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_INTRODUCTION
     *
     * @mbggenerated
     */
	@FormParam("courseIntroduction")
    private String courseIntroduction;

	@FormParam("courseIntroductionEn")
    private String courseIntroductionEn;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_TYPE
     *
     * @mbggenerated
     */
	@FormParam("courseType")
    private String courseType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.E_CURRENCY
     *
     * @mbggenerated
     */
	@FormParam("eCurrency")
    private Integer eCurrency;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_GRADE_DIMENSIONS
     *
     * @mbggenerated
     */
	@FormParam("courseGradeDimensions")
    private String courseGradeDimensions;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.LECTURER_GRADE_DIMENSIONS
     *
     * @mbggenerated
     */
	@FormParam("lecturerGradeDimensions")
    private String lecturerGradeDimensions;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.LECTURERS
     *
     * @mbggenerated
     */
	@FormParam("lecturers")
    private String lecturers;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_IMG
     *
     * @mbggenerated
     */
	@FormParam("courseImg")
    private String courseImg;

	@FormParam("gradeStatus")
    private String gradeStatus;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_DOWNLOADABLE
     *
     * @mbggenerated
     */
	@FormParam("courseDownloadable")
    private String courseDownloadable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.IS_EXCELLENT_COURSE
     *
     * @mbggenerated
     */
	@FormParam("isExcellentCourse")
    private String isExcellentCourse;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.STATUS
     *
     * @mbggenerated
     */
	@FormParam("status")
    private String status;
	
//	@FormParam("btnName")
//    private String btnName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.OPEN_COMMENT
     *
     * @mbggenerated
     */
	@FormParam("openComment")
    private String openComment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.OPEN_COURSE
     *
     * @mbggenerated
     */
	@FormParam("openCourse")
    private String openCourse;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.ADMINS
     *
     * @mbggenerated
     */
	@FormParam("admins")
    private String admins;

	@FormParam("adminNames")
    private String adminNames;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.VIEWERS
     *
     * @mbggenerated
     */
	@FormParam("viewers")
    private Integer viewers;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.THUMB_UP_COUNT
     *
     * @mbggenerated
     */
	@FormParam("thumbUpCount")
    private Integer thumbUpCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COMMENT_COUNT
     *
     * @mbggenerated
     */
	@FormParam("commentCount")
    private Integer commentCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.RATETIMES
     *
     * @mbggenerated
     */
	@FormParam("ratetimes")
    private Integer ratetimes;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_AVERAGE
     *
     * @mbggenerated
     */
	@FormParam("courseAverage")
    private Double courseAverage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.TOT_SCORE
     *
     * @mbggenerated
     */
	@FormParam("totScore")
    private Double totScore;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.LECTURER_AVERAGE
     *
     * @mbggenerated
     */
	@FormParam("lecturerAverage")
    private Double lecturerAverage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.COURSE_TIMES
     *
     * @mbggenerated
     */
	@FormParam("courseTimes")
    private Double courseTimes;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.SIGN_IN_TWO_DIMENSION_CODE
     *
     * @mbggenerated
     */
	@FormParam("signInTwoDimensionCode")
    private String signInTwoDimensionCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.GRADE_TWO_DIMENSION_CODE
     *
     * @mbggenerated
     */
	@FormParam("gradeTwoDimensionCode")
    private String gradeTwoDimensionCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.CREATOR
     *
     * @mbggenerated
     */
	@FormParam("creator")
    private String creator;
	
	private String creatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;
    
    @FormParam("fileUrlList[]")
	private List<String> fileUrlList;

    public List<String> getFileUrlList() {
		return fileUrlList;
	}

	public void setFileUrlList(List<String> fileUrlList) {
		this.fileUrlList = fileUrlList;
	}

	private Integer numberOfParticipants;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("operator")
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_COURSES.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;
    
    
	public String getGradeStatus() {
		return gradeStatus;
	}

	public void setGradeStatus(String gradeStatus) {
		this.gradeStatus = gradeStatus;
	}

	/**
	 * 讲师名称
	 */
	@FormParam("lecturerName")
    private String lecturerName;
	
	/**
	 * 讲师名称
	 */
	@FormParam("lecturerNameEn")
    private String lecturerNameEn;
	
	/**
	 * 讲师ID
	 */
	@FormParam("lecturerId")
    private String lecturerId;
	
	/**
	 * 点赞状态
	 */
	@FormParam("praise")
    private String praise;
	
	/**
	 * 收藏状态
	 */
	@FormParam("collect")
    private String collect;
	
	/**
	 * 当前登陆人Code
	 */
	private String code;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_ID
     *
     * @return the value of TOM_COURSES.COURSE_ID
     *
     * @mbggenerated
     */
	private Integer learnedNum;
	
	@FormParam("sectionId[]")
	private List<Integer> sectionId;
	
	@FormParam("sectionNameList[]")
	private List<String> sectionNameList;
	
	@FormParam("sectionAddressList[]")
	private List<String> sectionAddressList;

	@FormParam("sectionType[]")
	private List<String> sectionType;
	
	@FormParam("sectionSize[]")
	private List<Integer> sectionSize;
	
	@FormParam("empIds[]")
	private List<String> empIds;
	
	@FormParam("empNames[]")
	private List<String> empNames;
	private List<String> cityname;
	private List<String> deptname;
	
	@FormParam("classifyIds[]")
	private List<Integer> classifyIds;
	
	@FormParam("classifyNames[]")
	private List<String> classifyNames;
	
	private Integer activityId;
	
	private String activityName;

	private String beginTime;
	
	private String endTime;
	
	private String courseStatus;
	
	private String endTimeS;
	
	private String beginTimeS;
	
	private String learnStatus;//学习状态
	
	private String startTime;//开课时间
	private String courseAddress;//开课地点
	private String preStatus;
	private String testIfNull;
	
	
    public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getTestIfNull() {
		return testIfNull;
	}

	public void setTestIfNull(String testIfNull) {
		this.testIfNull = testIfNull;
	}

	public String getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(String preStatus) {
		this.preStatus = preStatus;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCourseAddress() {
		return courseAddress;
	}

	public void setCourseAddress(String courseAddress) {
		this.courseAddress = courseAddress;
	}

	public String getLearnStatus() {
		return learnStatus;
	}

	public void setLearnStatus(String learnStatus) {
		this.learnStatus = learnStatus;
	}

	public Integer getLearnedNum() {
		return learnedNum;
	}

	public void setLearnedNum(Integer learnedNum) {
		this.learnedNum = learnedNum;
	}

	public Integer getCourseId() {
        return courseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_ID
     *
     * @param courseId the value for TOM_COURSES.COURSE_ID
     *
     * @mbggenerated
     */
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_NUMBER
     *
     * @return the value of TOM_COURSES.COURSE_NUMBER
     *
     * @mbggenerated
     */
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_NUMBER
     *
     * @param courseNumber the value for TOM_COURSES.COURSE_NUMBER
     *
     * @mbggenerated
     */
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber == null ? null : courseNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_NAME
     *
     * @return the value of TOM_COURSES.COURSE_NAME
     *
     * @mbggenerated
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_NAME
     *
     * @param courseName the value for TOM_COURSES.COURSE_NAME
     *
     * @mbggenerated
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName == null ? null : courseName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_ONLINE
     *
     * @return the value of TOM_COURSES.COURSE_ONLINE
     *
     * @mbggenerated
     */
    public String getCourseOnline() {
        return courseOnline;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_ONLINE
     *
     * @param courseOnline the value for TOM_COURSES.COURSE_ONLINE
     *
     * @mbggenerated
     */
    public void setCourseOnline(String courseOnline) {
        this.courseOnline = courseOnline == null ? null : courseOnline.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_INTRODUCTION
     *
     * @return the value of TOM_COURSES.COURSE_INTRODUCTION
     *
     * @mbggenerated
     */
    public String getCourseIntroduction() {
        return courseIntroduction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_INTRODUCTION
     *
     * @param courseIntroduction the value for TOM_COURSES.COURSE_INTRODUCTION
     *
     * @mbggenerated
     */
    public void setCourseIntroduction(String courseIntroduction) {
        this.courseIntroduction = courseIntroduction == null ? null : courseIntroduction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_TYPE
     *
     * @return the value of TOM_COURSES.COURSE_TYPE
     *
     * @mbggenerated
     */
    public String getCourseType() {
        return courseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_TYPE
     *
     * @param courseType the value for TOM_COURSES.COURSE_TYPE
     *
     * @mbggenerated
     */
    public void setCourseType(String courseType) {
        this.courseType = courseType == null ? null : courseType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.E_CURRENCY
     *
     * @return the value of TOM_COURSES.E_CURRENCY
     *
     * @mbggenerated
     */
    public Integer geteCurrency() {
        return eCurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.E_CURRENCY
     *
     * @param eCurrency the value for TOM_COURSES.E_CURRENCY
     *
     * @mbggenerated
     */
    public void seteCurrency(Integer eCurrency) {
        this.eCurrency = eCurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_GRADE_DIMENSIONS
     *
     * @return the value of TOM_COURSES.COURSE_GRADE_DIMENSIONS
     *
     * @mbggenerated
     */
    public String getCourseGradeDimensions() {
        return courseGradeDimensions;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_GRADE_DIMENSIONS
     *
     * @param courseGradeDimensions the value for TOM_COURSES.COURSE_GRADE_DIMENSIONS
     *
     * @mbggenerated
     */
    public void setCourseGradeDimensions(String courseGradeDimensions) {
        this.courseGradeDimensions = courseGradeDimensions == null ? null : courseGradeDimensions.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.LECTURER_GRADE_DIMENSIONS
     *
     * @return the value of TOM_COURSES.LECTURER_GRADE_DIMENSIONS
     *
     * @mbggenerated
     */
    public String getLecturerGradeDimensions() {
        return lecturerGradeDimensions;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.LECTURER_GRADE_DIMENSIONS
     *
     * @param lecturerGradeDimensions the value for TOM_COURSES.LECTURER_GRADE_DIMENSIONS
     *
     * @mbggenerated
     */
    public void setLecturerGradeDimensions(String lecturerGradeDimensions) {
        this.lecturerGradeDimensions = lecturerGradeDimensions == null ? null : lecturerGradeDimensions.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.LECTURERS
     *
     * @return the value of TOM_COURSES.LECTURERS
     *
     * @mbggenerated
     */
    public String getLecturers() {
        return lecturers;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.LECTURERS
     *
     * @param lecturers the value for TOM_COURSES.LECTURERS
     *
     * @mbggenerated
     */
    public void setLecturers(String lecturers) {
        this.lecturers = lecturers == null ? null : lecturers.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_IMG
     *
     * @return the value of TOM_COURSES.COURSE_IMG
     *
     * @mbggenerated
     */
    public String getCourseImg() {
        return courseImg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_IMG
     *
     * @param courseImg the value for TOM_COURSES.COURSE_IMG
     *
     * @mbggenerated
     */
    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg == null ? null : courseImg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_DOWNLOADABLE
     *
     * @return the value of TOM_COURSES.COURSE_DOWNLOADABLE
     *
     * @mbggenerated
     */
    public String getCourseDownloadable() {
        return courseDownloadable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_DOWNLOADABLE
     *
     * @param courseDownloadable the value for TOM_COURSES.COURSE_DOWNLOADABLE
     *
     * @mbggenerated
     */
    public void setCourseDownloadable(String courseDownloadable) {
        this.courseDownloadable = courseDownloadable == null ? null : courseDownloadable.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.IS_EXCELLENT_COURSE
     *
     * @return the value of TOM_COURSES.IS_EXCELLENT_COURSE
     *
     * @mbggenerated
     */
    public String getIsExcellentCourse() {
        return isExcellentCourse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.IS_EXCELLENT_COURSE
     *
     * @param isExcellentCourse the value for TOM_COURSES.IS_EXCELLENT_COURSE
     *
     * @mbggenerated
     */
    public void setIsExcellentCourse(String isExcellentCourse) {
        this.isExcellentCourse = isExcellentCourse == null ? null : isExcellentCourse.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.STATUS
     *
     * @return the value of TOM_COURSES.STATUS
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.STATUS
     *
     * @param status the value for TOM_COURSES.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.OPEN_COMMENT
     *
     * @return the value of TOM_COURSES.OPEN_COMMENT
     *
     * @mbggenerated
     */
    public String getOpenComment() {
        return openComment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.OPEN_COMMENT
     *
     * @param openComment the value for TOM_COURSES.OPEN_COMMENT
     *
     * @mbggenerated
     */
    public void setOpenComment(String openComment) {
        this.openComment = openComment == null ? null : openComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.OPEN_COURSE
     *
     * @return the value of TOM_COURSES.OPEN_COURSE
     *
     * @mbggenerated
     */
    public String getOpenCourse() {
        return openCourse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.OPEN_COURSE
     *
     * @param openCourse the value for TOM_COURSES.OPEN_COURSE
     *
     * @mbggenerated
     */
    public void setOpenCourse(String openCourse) {
        this.openCourse = openCourse == null ? null : openCourse.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.ADMINS
     *
     * @return the value of TOM_COURSES.ADMINS
     *
     * @mbggenerated
     */
    public String getAdmins() {
        return admins;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.ADMINS
     *
     * @param admins the value for TOM_COURSES.ADMINS
     *
     * @mbggenerated
     */
    public void setAdmins(String admins) {
        this.admins = admins == null ? null : admins.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.VIEWERS
     *
     * @return the value of TOM_COURSES.VIEWERS
     *
     * @mbggenerated
     */
    public Integer getViewers() {
        return viewers;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.VIEWERS
     *
     * @param viewers the value for TOM_COURSES.VIEWERS
     *
     * @mbggenerated
     */
    public void setViewers(Integer viewers) {
        this.viewers = viewers;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.THUMB_UP_COUNT
     *
     * @return the value of TOM_COURSES.THUMB_UP_COUNT
     *
     * @mbggenerated
     */
    public Integer getThumbUpCount() {
        return thumbUpCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.THUMB_UP_COUNT
     *
     * @param thumbUpCount the value for TOM_COURSES.THUMB_UP_COUNT
     *
     * @mbggenerated
     */
    public void setThumbUpCount(Integer thumbUpCount) {
        this.thumbUpCount = thumbUpCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COMMENT_COUNT
     *
     * @return the value of TOM_COURSES.COMMENT_COUNT
     *
     * @mbggenerated
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COMMENT_COUNT
     *
     * @param commentCount the value for TOM_COURSES.COMMENT_COUNT
     *
     * @mbggenerated
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.RATETIMES
     *
     * @return the value of TOM_COURSES.RATETIMES
     *
     * @mbggenerated
     */
    public Integer getRatetimes() {
        return ratetimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.RATETIMES
     *
     * @param ratetimes the value for TOM_COURSES.RATETIMES
     *
     * @mbggenerated
     */
    public void setRatetimes(Integer ratetimes) {
        this.ratetimes = ratetimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_AVERAGE
     *
     * @return the value of TOM_COURSES.COURSE_AVERAGE
     *
     * @mbggenerated
     */
    public Double getCourseAverage() {
        return courseAverage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_AVERAGE
     *
     * @param courseAverage the value for TOM_COURSES.COURSE_AVERAGE
     *
     * @mbggenerated
     */
    public void setCourseAverage(Double courseAverage) {
        this.courseAverage = courseAverage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.TOT_SCORE
     *
     * @return the value of TOM_COURSES.TOT_SCORE
     *
     * @mbggenerated
     */
    public Double getTotScore() {
        return totScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.TOT_SCORE
     *
     * @param totScore the value for TOM_COURSES.TOT_SCORE
     *
     * @mbggenerated
     */
    public void setTotScore(Double totScore) {
        this.totScore = totScore;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.LECTURER_AVERAGE
     *
     * @return the value of TOM_COURSES.LECTURER_AVERAGE
     *
     * @mbggenerated
     */
    public Double getLecturerAverage() {
        return lecturerAverage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.LECTURER_AVERAGE
     *
     * @param lecturerAverage the value for TOM_COURSES.LECTURER_AVERAGE
     *
     * @mbggenerated
     */
    public void setLecturerAverage(Double lecturerAverage) {
        this.lecturerAverage = lecturerAverage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.COURSE_TIMES
     *
     * @return the value of TOM_COURSES.COURSE_TIMES
     *
     * @mbggenerated
     */
    public Double getCourseTimes() {
        return courseTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.COURSE_TIMES
     *
     * @param courseTimes the value for TOM_COURSES.COURSE_TIMES
     *
     * @mbggenerated
     */
    public void setCourseTimes(Double courseTimes) {
        this.courseTimes = courseTimes;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.SIGN_IN_TWO_DIMENSION_CODE
     *
     * @return the value of TOM_COURSES.SIGN_IN_TWO_DIMENSION_CODE
     *
     * @mbggenerated
     */
    public String getSignInTwoDimensionCode() {
        return signInTwoDimensionCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.SIGN_IN_TWO_DIMENSION_CODE
     *
     * @param signInTwoDimensionCode the value for TOM_COURSES.SIGN_IN_TWO_DIMENSION_CODE
     *
     * @mbggenerated
     */
    public void setSignInTwoDimensionCode(String signInTwoDimensionCode) {
        this.signInTwoDimensionCode = signInTwoDimensionCode == null ? null : signInTwoDimensionCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.GRADE_TWO_DIMENSION_CODE
     *
     * @return the value of TOM_COURSES.GRADE_TWO_DIMENSION_CODE
     *
     * @mbggenerated
     */
    public String getGradeTwoDimensionCode() {
        return gradeTwoDimensionCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.GRADE_TWO_DIMENSION_CODE
     *
     * @param gradeTwoDimensionCode the value for TOM_COURSES.GRADE_TWO_DIMENSION_CODE
     *
     * @mbggenerated
     */
    public void setGradeTwoDimensionCode(String gradeTwoDimensionCode) {
        this.gradeTwoDimensionCode = gradeTwoDimensionCode == null ? null : gradeTwoDimensionCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.CREATOR
     *
     * @return the value of TOM_COURSES.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.CREATOR
     *
     * @param creator the value for TOM_COURSES.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.CREATE_TIME
     *
     * @return the value of TOM_COURSES.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.CREATE_TIME
     *
     * @param createTime the value for TOM_COURSES.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.OPERATOR
     *
     * @return the value of TOM_COURSES.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.OPERATOR
     *
     * @param operator the value for TOM_COURSES.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_COURSES.UPDATE_TIME
     *
     * @return the value of TOM_COURSES.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_COURSES.UPDATE_TIME
     *
     * @param updateTime the value for TOM_COURSES.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	public String getLecturerId() {
		return lecturerId;
	}

	public void setLecturerId(String lecturerId) {
		this.lecturerId = lecturerId;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Integer> getSectionId() {
		return sectionId;
	}

	public void setSectionId(List<Integer> sectionId) {
		this.sectionId = sectionId;
	}

	public List<String> getSectionNameList() {
		return sectionNameList;
	}

	public void setSectionNameList(List<String> sectionNameList) {
		this.sectionNameList = sectionNameList;
	}

	public List<String> getSectionAddressList() {
		return sectionAddressList;
	}

	public void setSectionAddressList(List<String> sectionAddressList) {
		this.sectionAddressList = sectionAddressList;
	}

	public List<String> getSectionType() {
		return sectionType;
	}

	public void setSectionType(List<String> sectionType) {
		this.sectionType = sectionType;
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

	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

	public List<Integer> getClassifyIds() {
		return classifyIds;
	}

	public void setClassifyIds(List<Integer> classifyIds) {
		this.classifyIds = classifyIds;
	}

	public List<String> getClassifyNames() {
		return classifyNames;
	}

	public void setClassifyNames(List<String> classifyNames) {
		this.classifyNames = classifyNames;
	}

	public List<Integer> getSectionSize() {
		return sectionSize;
	}

	public void setSectionSize(List<Integer> sectionSize) {
		this.sectionSize = sectionSize;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Integer getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public void setNumberOfParticipants(Integer numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public Integer getSectionNum() {
		return sectionNum;
	}

	public void setSectionNum(Integer sectionNum) {
		this.sectionNum = sectionNum;
	}

	public String getIsGrade() {
		return isGrade;
	}

	public void setIsGrade(String isGrade) {
		this.isGrade = isGrade;
	}

	public String getEndTimeS() {
		return endTimeS;
	}

	public void setEndTimeS(String endTimeS) {
		this.endTimeS = endTimeS;
	}

	public String getBeginTimeS() {
		return beginTimeS;
	}

	public void setBeginTimeS(String beginTimeS) {
		this.beginTimeS = beginTimeS;
	}

	public List<String> getCityname() {
		return cityname;
	}

	public void setCityname(List<String> cityname) {
		this.cityname = cityname;
	}

	public List<String> getDeptname() {
		return deptname;
	}

	public void setDeptname(List<String> deptname) {
		this.deptname = deptname;
	}

	public String getCourseNameEn() {
		return courseNameEn;
	}

	public void setCourseNameEn(String courseNameEn) {
		this.courseNameEn = courseNameEn;
	}

	public String getCourseIntroductionEn() {
		return courseIntroductionEn;
	}

	public void setCourseIntroductionEn(String courseIntroductionEn) {
		this.courseIntroductionEn = courseIntroductionEn;
	}

	public String getLecturerNameEn() {
		return lecturerNameEn;
	}

	public void setLecturerNameEn(String lecturerNameEn) {
		this.lecturerNameEn = lecturerNameEn;
	}
	
	private String isRequired;
	public String getIsRequired() {
		return isRequired;
	}

	public void setIdRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public int getCountclasses() {
		return countclasses;
	}

	public void setCountclasses(int countclasses) {
		this.countclasses = countclasses;
	}
	
	private String sort;


	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
