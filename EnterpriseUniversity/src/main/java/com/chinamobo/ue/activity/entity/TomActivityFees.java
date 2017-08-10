package com.chinamobo.ue.activity.entity;

import java.util.Date;

import javax.ws.rs.FormParam;

import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Global;
import com.chinamobo.ue.utils.MapManager;

public class TomActivityFees extends TomActivityFeesKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.FEE_NAME
     *
     * @mbggenerated
     */
	@FormParam("feeName")
    private String feeName;
    
	@FormParam("creatorId")
    private String creatorId;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.FEE
     *
     * @mbggenerated
     */
	@FormParam("fee")
    private Double fee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.REMARK
     *
     * @mbggenerated
     */
	@FormParam("remark")
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.STATUS
     *
     * @mbggenerated
     */
	@FormParam("status")
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.CREATOR
     *
     * @mbggenerated
     */
	@FormParam("creator")
    private String creator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("operator")
    private String operator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_ACTIVITY_FEES.UPDATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("updateTime")
    private Date updateTime;
    
    
	@FormParam("createTime")
    private Date createTime;
    
    
	/**
	 * 插入前调用
	 */
	public void preInsertData(){
	
		//String user  = ShiroUtils.getCurrentUser().getName();
//		String user  = "4";
//		this.operator = user;
//		this.creator = user;
//		this.updateTime = new Date();
//		this.createTime = new Date();
		//this.setCreateTime(new Date());
		this.status = Global.YES;//Y 正常  N 删除
	}
	
	/**
	 * 插入后调用
	 */
	public void preUpdateData(){
		//String user  = ShiroUtils.getCurrentUser().getName();
//		String user  = "4";
//		this.operator = user;
//		this.updateTime = new Date();
	}

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.FEE_NAME
     *
     * @return the value of TOM_ACTIVITY_FEES.FEE_NAME
     *
     * @mbggenerated
     */
    public String getFeeName() {
        return feeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.FEE_NAME
     *
     * @param feeName the value for TOM_ACTIVITY_FEES.FEE_NAME
     *
     * @mbggenerated
     */
    public void setFeeName(String feeName) {
        this.feeName = feeName == null ? null : feeName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.FEE
     *
     * @return the value of TOM_ACTIVITY_FEES.FEE
     *
     * @mbggenerated
     */
    public Double getFee() {
        return fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.FEE
     *
     * @param fee the value for TOM_ACTIVITY_FEES.FEE
     *
     * @mbggenerated
     */
    public void setFee(Double fee) {
        this.fee = fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.REMARK
     *
     * @return the value of TOM_ACTIVITY_FEES.REMARK
     *
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.REMARK
     *
     * @param remark the value for TOM_ACTIVITY_FEES.REMARK
     *
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.STATUS
     *
     * @return the value of TOM_ACTIVITY_FEES.STATUS
     *
     * @mbggenerated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.STATUS
     *
     * @param status the value for TOM_ACTIVITY_FEES.STATUS
     *
     * @mbggenerated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.CREATOR
     *
     * @return the value of TOM_ACTIVITY_FEES.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.CREATOR
     *
     * @param creator the value for TOM_ACTIVITY_FEES.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.OPERATOR
     *
     * @return the value of TOM_ACTIVITY_FEES.OPERATOR
     *
     * @mbggenerated
     */
    public String getOperator() {
        return operator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.OPERATOR
     *
     * @param operator the value for TOM_ACTIVITY_FEES.OPERATOR
     *
     * @mbggenerated
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_ACTIVITY_FEES.UPDATE_TIME
     *
     * @return the value of TOM_ACTIVITY_FEES.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_ACTIVITY_FEES.UPDATE_TIME
     *
     * @param updateTime the value for TOM_ACTIVITY_FEES.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
    
}