package com.chinamobo.ue.system.entity;

import java.util.Date;

public class TomRollingPictureRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE_RECORD.PICTURE_ID
     *
     * @mbggenerated
     */
    private Integer pictureId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE_RECORD.EMP_CODE
     *
     * @mbggenerated
     */
    private String empCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE_RECORD.EMP_NAME
     *
     * @mbggenerated
     */
    private String empName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ROLLING_PICTURE_RECORD.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ROLLING_PICTURE_RECORD.PICTURE_ID
     *
     * @return the value of TOM_ROLLING_PICTURE_RECORD.PICTURE_ID
     *
     * @mbggenerated
     */
    public Integer getPictureId() {
        return pictureId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ROLLING_PICTURE_RECORD.PICTURE_ID
     *
     * @param pictureId the value for TOM_ROLLING_PICTURE_RECORD.PICTURE_ID
     *
     * @mbggenerated
     */
    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ROLLING_PICTURE_RECORD.EMP_CODE
     *
     * @return the value of TOM_ROLLING_PICTURE_RECORD.EMP_CODE
     *
     * @mbggenerated
     */
    public String getEmpCode() {
        return empCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ROLLING_PICTURE_RECORD.EMP_CODE
     *
     * @param empCode the value for TOM_ROLLING_PICTURE_RECORD.EMP_CODE
     *
     * @mbggenerated
     */
    public void setEmpCode(String empCode) {
        this.empCode = empCode == null ? null : empCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ROLLING_PICTURE_RECORD.EMP_NAME
     *
     * @return the value of TOM_ROLLING_PICTURE_RECORD.EMP_NAME
     *
     * @mbggenerated
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ROLLING_PICTURE_RECORD.EMP_NAME
     *
     * @param empName the value for TOM_ROLLING_PICTURE_RECORD.EMP_NAME
     *
     * @mbggenerated
     */
    public void setEmpName(String empName) {
        this.empName = empName == null ? null : empName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ROLLING_PICTURE_RECORD.CREATE_TIME
     *
     * @return the value of TOM_ROLLING_PICTURE_RECORD.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ROLLING_PICTURE_RECORD.CREATE_TIME
     *
     * @param createTime the value for TOM_ROLLING_PICTURE_RECORD.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}