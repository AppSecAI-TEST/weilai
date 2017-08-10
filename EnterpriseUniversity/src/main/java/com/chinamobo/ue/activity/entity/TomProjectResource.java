package com.chinamobo.ue.activity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

public class TomProjectResource {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.project_id
     *
     * @mbggenerated
     */
    private Integer projectId;
    
    private String parentProjectClassifyName;
    


	public String getParentProjectClassifyName() {
		return parentProjectClassifyName;
	}

	public void setParentProjectClassifyName(String parentProjectClassifyName) {
		this.parentProjectClassifyName = parentProjectClassifyName;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.resource_id
     *
     * @mbggenerated
     */
    private Integer resourceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.creator
     *
     * @mbggenerated
     */
    private String creator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.operator
     *
     * @mbggenerated
     */
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.status
     *
     * @mbggenerated
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tom_project_resource.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.id
     *
     * @return the value of tom_project_resource.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.id
     *
     * @param id the value for tom_project_resource.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.project_id
     *
     * @return the value of tom_project_resource.project_id
     *
     * @mbggenerated
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.project_id
     *
     * @param projectId the value for tom_project_resource.project_id
     *
     * @mbggenerated
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.resource_id
     *
     * @return the value of tom_project_resource.resource_id
     *
     * @mbggenerated
     */
    public Integer getResourceId() {
        return resourceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.resource_id
     *
     * @param resourceId the value for tom_project_resource.resource_id
     *
     * @mbggenerated
     */
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.create_time
     *
     * @return the value of tom_project_resource.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.create_time
     *
     * @param createTime the value for tom_project_resource.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.creator
     *
     * @return the value of tom_project_resource.creator
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.creator
     *
     * @param creator the value for tom_project_resource.creator
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.operator
     *
     * @return the value of tom_project_resource.operator
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.operator
     *
     * @param operator the value for tom_project_resource.operator
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.update_time
     *
     * @return the value of tom_project_resource.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.update_time
     *
     * @param updateTime the value for tom_project_resource.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.status
     *
     * @return the value of tom_project_resource.status
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.status
     *
     * @param status the value for tom_project_resource.status
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tom_project_resource.type
     *
     * @return the value of tom_project_resource.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tom_project_resource.type
     *
     * @param type the value for tom_project_resource.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}