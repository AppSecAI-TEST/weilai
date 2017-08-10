package com.chinamobo.ue.activity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.activity.common.DataEntity;

public class TomActivityExapInfo extends DataEntity<TomActivityExapInfo>{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_ID
     *
     * @mbggenerated
     */
	@FormParam("retakingId")
    private Integer retakingId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_EXAM_INFO.ACTIVITY_ID
     *
     * @mbggenerated
     */
	@FormParam("activityId")
    private Integer activityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_EXAM_INFO.EXAM_ID
     *
     * @mbggenerated
     */
	@FormParam("examId")
    private Integer examId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_SORT
     *
     * @mbggenerated
     */
	@FormParam("retakingSort")
    private Integer retakingSort;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_BEGIN_TIME
     *
     * @mbggenerated
     */
	@FormParam("retakingExamBeginTime")
    private Date retakingExamBeginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_END_TIME
     *
     * @mbggenerated
     */
	@FormParam("retakingExamEndTime")
    private Date retakingExamEndTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_ID
     *
     * @return the value of TOM_ACTIVITY_EXAM_INFO.RETAKING_ID
     *
     * @mbggenerated
     */
    public Integer getRetakingId() {
        return retakingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_ID
     *
     * @param retakingId the value for TOM_ACTIVITY_EXAM_INFO.RETAKING_ID
     *
     * @mbggenerated
     */
    public void setRetakingId(Integer retakingId) {
        this.retakingId = retakingId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_EXAM_INFO.ACTIVITY_ID
     *
     * @return the value of TOM_ACTIVITY_EXAM_INFO.ACTIVITY_ID
     *
     * @mbggenerated
     */
    public Integer getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_EXAM_INFO.ACTIVITY_ID
     *
     * @param activityId the value for TOM_ACTIVITY_EXAM_INFO.ACTIVITY_ID
     *
     * @mbggenerated
     */
    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_EXAM_INFO.EXAM_ID
     *
     * @return the value of TOM_ACTIVITY_EXAM_INFO.EXAM_ID
     *
     * @mbggenerated
     */
    public Integer getExamId() {
        return examId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_EXAM_INFO.EXAM_ID
     *
     * @param examId the value for TOM_ACTIVITY_EXAM_INFO.EXAM_ID
     *
     * @mbggenerated
     */
    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_SORT
     *
     * @return the value of TOM_ACTIVITY_EXAM_INFO.RETAKING_SORT
     *
     * @mbggenerated
     */
    public Integer getRetakingSort() {
        return retakingSort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_SORT
     *
     * @param retakingSort the value for TOM_ACTIVITY_EXAM_INFO.RETAKING_SORT
     *
     * @mbggenerated
     */
    public void setRetakingSort(Integer retakingSort) {
        this.retakingSort = retakingSort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_BEGIN_TIME
     *
     * @return the value of TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_BEGIN_TIME
     *
     * @mbggenerated
     */
    public Date getRetakingExamBeginTime() {
        return retakingExamBeginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_BEGIN_TIME
     *
     * @param retakingExamBeginTime the value for TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_BEGIN_TIME
     *
     * @mbggenerated
     */
    public void setRetakingExamBeginTime(Date retakingExamBeginTime) {
        this.retakingExamBeginTime = retakingExamBeginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_END_TIME
     *
     * @return the value of TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_END_TIME
     *
     * @mbggenerated
     */
    public Date getRetakingExamEndTime() {
        return retakingExamEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_END_TIME
     *
     * @param retakingExamEndTime the value for TOM_ACTIVITY_EXAM_INFO.RETAKING_EXAM_END_TIME
     *
     * @mbggenerated
     */
    public void setRetakingExamEndTime(Date retakingExamEndTime) {
        this.retakingExamEndTime = retakingExamEndTime;
    }
}