package com.chinamobo.ue.course.entity;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;
public class TomLecturer {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_ID
     *
     * @mbggenerated
     */
	@FormParam("lecturerId")
    private Integer lecturerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_NUMBER
     *
     * @mbggenerated
     */
    @FormParam("lecturerNumber")
    private String lecturerNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_NAME
     *
     * @mbggenerated
     */
    @FormParam("lecturerName")
    private String lecturerName;
    
    
    @FormParam("lecturerNameEn")
    private String lecturerNameEn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_TYPE
     *
     * @mbggenerated
     */
    @FormParam("lecturerType")
    private String lecturerType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.CODE
     *
     * @mbggenerated
     */
    @FormParam("code")
    private String code;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_GRADE
     *
     * @mbggenerated
     */
    @FormParam("lecturerGrade")
    private String lecturerGrade;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.IS_INTERNAL_LECTURER
     *
     * @mbggenerated
     */
    @FormParam("isInternalLecturer")
    private String isInternalLecturer;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_IMG
     *
     * @mbggenerated
     */
    @FormParam("lecturerImg")
    private String lecturerImg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.LECTURER_INTRODUCTION
     *
     * @mbggenerated
     */
    @FormParam("lecturerIntroduction")
    private String lecturerIntroduction;
    
    @FormParam("lecturerIntroductionEn")
    private String lecturerIntroductionEn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.STATUS
     *
     * @mbggenerated
     */
    @FormParam("status")
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.CREATOR
     *
     * @mbggenerated
     */
    @FormParam("creator")
    private String creator;
    
    private String creatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.OPERATOR
     *
     * @mbggenerated
     */
    @FormParam("operator")
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_LECTURE.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;
    
    @FormParam("lecturerScore")
    private Double lecturerScore;

    @FormParam("empIds[]")
	private List<String> empIds;
	
	@FormParam("empNames[]")
	private List<String> empNames;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_ID
     *
     * @return the value of TOM_LECTURE.LECTURER_ID
     *
     * @mbggenerated
     */
    public Integer getLecturerId() {
        return lecturerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_ID
     *
     * @param lecturerId the value for TOM_LECTURE.LECTURER_ID
     *
     * @mbggenerated
     */
    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_NUMBER
     *
     * @return the value of TOM_LECTURE.LECTURER_NUMBER
     *
     * @mbggenerated
     */
    public String getLecturerNumber() {
        return lecturerNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_NUMBER
     *
     * @param lecturerNumber the value for TOM_LECTURE.LECTURER_NUMBER
     *
     * @mbggenerated
     */
    public void setLecturerNumber(String lecturerNumber) {
        this.lecturerNumber = lecturerNumber == null ? null : lecturerNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_NAME
     *
     * @return the value of TOM_LECTURE.LECTURER_NAME
     *
     * @mbggenerated
     */
    public String getLecturerName() {
        return lecturerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_NAME
     *
     * @param lecturerName the value for TOM_LECTURE.LECTURER_NAME
     *
     * @mbggenerated
     */
    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName == null ? null : lecturerName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_TYPE
     *
     * @return the value of TOM_LECTURE.LECTURER_TYPE
     *
     * @mbggenerated
     */
    public String getLecturerType() {
        return lecturerType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_TYPE
     *
     * @param lecturerType the value for TOM_LECTURE.LECTURER_TYPE
     *
     * @mbggenerated
     */
    public void setLecturerType(String lecturerType) {
        this.lecturerType = lecturerType == null ? null : lecturerType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.CODE
     *
     * @return the value of TOM_LECTURE.CODE
     *
     * @mbggenerated
     */
    public String getCode() {
        return code;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.CODE
     *
     * @param code the value for TOM_LECTURE.CODE
     *
     * @mbggenerated
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_GRADE
     *
     * @return the value of TOM_LECTURE.LECTURER_GRADE
     *
     * @mbggenerated
     */
    public String getLecturerGrade() {
        return lecturerGrade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_GRADE
     *
     * @param lecturerGrade the value for TOM_LECTURE.LECTURER_GRADE
     *
     * @mbggenerated
     */
    public void setLecturerGrade(String lecturerGrade) {
        this.lecturerGrade = lecturerGrade == null ? null : lecturerGrade.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.IS_INTERNAL_LECTURER
     *
     * @return the value of TOM_LECTURE.IS_INTERNAL_LECTURER
     *
     * @mbggenerated
     */
    public String getIsInternalLecturer() {
        return isInternalLecturer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.IS_INTERNAL_LECTURER
     *
     * @param isInternalLecturer the value for TOM_LECTURE.IS_INTERNAL_LECTURER
     *
     * @mbggenerated
     */
    public void setIsInternalLecturer(String isInternalLecturer) {
        this.isInternalLecturer = isInternalLecturer == null ? null : isInternalLecturer.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_IMG
     *
     * @return the value of TOM_LECTURE.LECTURER_IMG
     *
     * @mbggenerated
     */
    public String getLecturerImg() {
        return lecturerImg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_IMG
     *
     * @param lecturerImg the value for TOM_LECTURE.LECTURER_IMG
     *
     * @mbggenerated
     */
    public void setLecturerImg(String lecturerImg) {
        this.lecturerImg = lecturerImg == null ? null : lecturerImg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.LECTURER_INTRODUCTION
     *
     * @return the value of TOM_LECTURE.LECTURER_INTRODUCTION
     *
     * @mbggenerated
     */
    public String getLecturerIntroduction() {
        return lecturerIntroduction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.LECTURER_INTRODUCTION
     *
     * @param lecturerIntroduction the value for TOM_LECTURE.LECTURER_INTRODUCTION
     *
     * @mbggenerated
     */
    public void setLecturerIntroduction(String lecturerIntroduction) {
        this.lecturerIntroduction = lecturerIntroduction == null ? null : lecturerIntroduction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.STATUS
     *
     * @return the value of TOM_LECTURE.STATUS
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.STATUS
     *
     * @param status the value for TOM_LECTURE.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.CREATOR
     *
     * @return the value of TOM_LECTURE.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.CREATOR
     *
     * @param creator the value for TOM_LECTURE.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.CREATE_TIME
     *
     * @return the value of TOM_LECTURE.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.CREATE_TIME
     *
     * @param createTime the value for TOM_LECTURE.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.OPERATOR
     *
     * @return the value of TOM_LECTURE.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.OPERATOR
     *
     * @param operator the value for TOM_LECTURE.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_LECTURE.UPDATE_TIME
     *
     * @return the value of TOM_LECTURE.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_LECTURE.UPDATE_TIME
     *
     * @param updateTime the value for TOM_LECTURE.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public Double getLecturerScore() {
		return lecturerScore;
	}

	public void setLecturerScore(Double lecturerScore) {
		this.lecturerScore = lecturerScore;
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
	
	
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getLecturerNameEn() {
		return lecturerNameEn;
	}

	public void setLecturerNameEn(String lecturerNameEn) {
		this.lecturerNameEn = lecturerNameEn;
	}

	public String getLecturerIntroductionEn() {
		return lecturerIntroductionEn;
	}

	public void setLecturerIntroductionEn(String lecturerIntroductionEn) {
		this.lecturerIntroductionEn = lecturerIntroductionEn;
	}

	@Override
	public String toString() {
		return "TomLecturer [lecturerId=" + lecturerId + ", lecturerNumber="
				+ lecturerNumber + ", lecturerName=" + lecturerName
				+ ", lecturerType=" + lecturerType + ", code=" + code
				+ ", lecturerGrade=" + lecturerGrade + ", isInternalLecturer="
				+ isInternalLecturer + ", lecturerImg=" + lecturerImg
				+ ", lecturerIntroduction=" + lecturerIntroduction
				+ ", status=" + status + ", creator=" + creator
				+ ", createTime=" + createTime + ", operator=" + operator
				+ ", updateTime=" + updateTime + "]";
	}
    
}