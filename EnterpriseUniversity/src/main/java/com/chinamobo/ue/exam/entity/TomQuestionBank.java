package com.chinamobo.ue.exam.entity;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

public class TomQuestionBank {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.QUESTION_BANK_ID
     *
     * @mbggenerated
     */
	@FormParam("questionBankId")
    private Integer questionBankId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.QUESTION_BANK_NUMBER
     *
     * @mbggenerated
     */
	@FormParam("questionBankNumber")
    private String questionBankNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.QUESTION_BANK_NAME
     *
     * @mbggenerated
     */
	@FormParam("questionBankName")
    private String questionBankName;
	
	 /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.QUESTION_BANK_NAME
     *
     * @mbggenerated
     */
	@FormParam("questionBankNameEn")
    private String questionBankNameEn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.FILE_PATH
     *
     * @mbggenerated
     */
	@FormParam("filePath")
    private String filePath;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.QUESTION_CLASSIFICATION_ID
     *
     * @mbggenerated
     */
	@FormParam("questionClassificationId")
    private Integer questionClassificationId;

    private String questionClassificationName;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.ADMIN_ID
     *
     * @mbggenerated
     */
	@FormParam("admins")
	private String admins;
	@FormParam("adminNames")
	private String adminNames;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.CREATOR
     *
     * @mbggenerated
     */
	@FormParam("creator")
    private String creator;
	
	 private String creatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.LAST_OPERATOR
     *
     * @mbggenerated
     */
	@FormParam("lastOperator")
    private String lastOperator;
    private String lastOperatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_QUESTION_BANK.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;
	@FormParam("deleteTopicIds[]")
	private List<Integer> deleteTopicIds;
	
	private List<TomTopic> topics;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.QUESTION_BANK_ID
     *
     * @return the value of TOM_QUESTION_BANK.QUESTION_BANK_ID
     *
     * @mbggenerated
     */
    public Integer getQuestionBankId() {
        return questionBankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.QUESTION_BANK_ID
     *
     * @param questionBankId the value for TOM_QUESTION_BANK.QUESTION_BANK_ID
     *
     * @mbggenerated
     */
    public void setQuestionBankId(Integer questionBankId) {
        this.questionBankId = questionBankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.QUESTION_BANK_NUMBER
     *
     * @return the value of TOM_QUESTION_BANK.QUESTION_BANK_NUMBER
     *
     * @mbggenerated
     */
    public String getQuestionBankNumber() {
        return questionBankNumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.QUESTION_BANK_NUMBER
     *
     * @param questionBankNumber the value for TOM_QUESTION_BANK.QUESTION_BANK_NUMBER
     *
     * @mbggenerated
     */
    public void setQuestionBankNumber(String questionBankNumber) {
        this.questionBankNumber = questionBankNumber == null ? null : questionBankNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.QUESTION_BANK_NAME
     *
     * @return the value of TOM_QUESTION_BANK.QUESTION_BANK_NAME
     *
     * @mbggenerated
     */
    public String getQuestionBankName() {
        return questionBankName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.QUESTION_BANK_NAME
     *
     * @param questionBankName the value for TOM_QUESTION_BANK.QUESTION_BANK_NAME
     *
     * @mbggenerated
     */
    public void setQuestionBankName(String questionBankName) {
        this.questionBankName = questionBankName == null ? null : questionBankName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.FILE_PATH
     *
     * @return the value of TOM_QUESTION_BANK.FILE_PATH
     *
     * @mbggenerated
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.FILE_PATH
     *
     * @param filePath the value for TOM_QUESTION_BANK.FILE_PATH
     *
     * @mbggenerated
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.QUESTION_CLASSIFICATION_ID
     *
     * @return the value of TOM_QUESTION_BANK.QUESTION_CLASSIFICATION_ID
     *
     * @mbggenerated
     */
    public Integer getQuestionClassificationId() {
        return questionClassificationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.QUESTION_CLASSIFICATION_ID
     *
     * @param questionClassificationId the value for TOM_QUESTION_BANK.QUESTION_CLASSIFICATION_ID
     *
     * @mbggenerated
     */
    public void setQuestionClassificationId(Integer questionClassificationId) {
        this.questionClassificationId = questionClassificationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.CREATE_TIME
     *
     * @return the value of TOM_QUESTION_BANK.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.CREATE_TIME
     *
     * @param createTime the value for TOM_QUESTION_BANK.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.CREATOR
     *
     * @return the value of TOM_QUESTION_BANK.CREATOR
     *
     * @mbggenerated
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.CREATOR
     *
     * @param creator the value for TOM_QUESTION_BANK.CREATOR
     *
     * @mbggenerated
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.LAST_OPERATOR
     *
     * @return the value of TOM_QUESTION_BANK.LAST_OPERATOR
     *
     * @mbggenerated
     */
    public String getLastOperator() {
        return lastOperator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.LAST_OPERATOR
     *
     * @param lastOperator the value for TOM_QUESTION_BANK.LAST_OPERATOR
     *
     * @mbggenerated
     */
    public void setLastOperator(String lastOperator) {
        this.lastOperator = lastOperator == null ? null : lastOperator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_QUESTION_BANK.UPDATE_TIME
     *
     * @return the value of TOM_QUESTION_BANK.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_QUESTION_BANK.UPDATE_TIME
     *
     * @param updateTime the value for TOM_QUESTION_BANK.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getAdminNames() {
		return adminNames;
	}

	public void setAdminNames(String adminNames) {
		this.adminNames = adminNames;
	}

	public List<TomTopic> getTopics() {
		return topics;
	}

	public void setTopics(List<TomTopic> topics) {
		this.topics = topics;
	}

	public String getQuestionClassificationName() {
		return questionClassificationName;
	}

	public void setQuestionClassificationName(String questionClassificationName) {
		this.questionClassificationName = questionClassificationName;
	}

	public List<Integer> getDeleteTopicIds() {
		return deleteTopicIds;
	}

	public void setDeleteTopicIds(List<Integer> deleteTopicIds) {
		this.deleteTopicIds = deleteTopicIds;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getLastOperatorId() {
		return lastOperatorId;
	}

	public void setLastOperatorId(String lastOperatorId) {
		this.lastOperatorId = lastOperatorId;
	}

	public String getQuestionBankNameEn() {
		return questionBankNameEn;
	}

	public void setQuestionBankNameEn(String questionBankNameEn) {
		this.questionBankNameEn = questionBankNameEn;
	}
	
	private String questionClassificationNameEn;
	public String getQuestionClassificationNameEn() {
		return questionClassificationNameEn;
	}

	public void setQuestionClassificationNameEn(String questionClassificationNameEn) {
		this.questionClassificationNameEn = questionClassificationNameEn;
	}
	
	
    
}