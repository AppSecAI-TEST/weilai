package com.chinamobo.ue.exam.entity;

import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;

public class TomTopic {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TOPIC.TOPIC_ID
     *
     * @mbggenerated
     */
    private Integer topicId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TOPIC.TOPIC_NAME
     *
     * @mbggenerated
     */
	@FormParam("topicName")
    private String topicName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TOPIC.QUESTION_BANK_ID
     *
     * @mbggenerated
     */
	@FormParam("questionBankId")
    private Integer questionBankId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TOPIC.QUESTION_TYPE
     *
     * @mbggenerated
     */
	@FormParam("questionType")
    private String questionType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TOM_TOPIC.CREATE_TIME
     *
     * @mbggenerated
     */
	@FormParam("createTime")
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TOPIC.TOPIC_ID
     *
     * @return the value of TOM_TOPIC.TOPIC_ID
     *
     * @mbggenerated
     */
    private List<TomTopicOption> topicOptions;
    
    private String answer="";
    @FormParam("topicNameEn")
    private String topicNameEn;
    
    public String getTopicNameEn() {
		return topicNameEn;
	}

	public void setTopicNameEn(String topicNameEn) {
		this.topicNameEn = topicNameEn;
	}

	public Integer getTopicId() {
        return topicId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TOPIC.TOPIC_ID
     *
     * @param topicId the value for TOM_TOPIC.TOPIC_ID
     *
     * @mbggenerated
     */
    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TOPIC.TOPIC_NAME
     *
     * @return the value of TOM_TOPIC.TOPIC_NAME
     *
     * @mbggenerated
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TOPIC.TOPIC_NAME
     *
     * @param topicName the value for TOM_TOPIC.TOPIC_NAME
     *
     * @mbggenerated
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName == null ? null : topicName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TOPIC.QUESTION_BANK_ID
     *
     * @return the value of TOM_TOPIC.QUESTION_BANK_ID
     *
     * @mbggenerated
     */
    public Integer getQuestionBankId() {
        return questionBankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TOPIC.QUESTION_BANK_ID
     *
     * @param questionBankId the value for TOM_TOPIC.QUESTION_BANK_ID
     *
     * @mbggenerated
     */
    public void setQuestionBankId(Integer questionBankId) {
        this.questionBankId = questionBankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TOPIC.QUESTION_TYPE
     *
     * @return the value of TOM_TOPIC.QUESTION_TYPE
     *
     * @mbggenerated
     */
    public String getQuestionType() {
        return questionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TOPIC.QUESTION_TYPE
     *
     * @param questionType the value for TOM_TOPIC.QUESTION_TYPE
     *
     * @mbggenerated
     */
    public void setQuestionType(String questionType) {
        this.questionType = questionType == null ? null : questionType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TOM_TOPIC.CREATE_TIME
     *
     * @return the value of TOM_TOPIC.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TOM_TOPIC.CREATE_TIME
     *
     * @param createTime the value for TOM_TOPIC.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public List<TomTopicOption> getTopicOptions() {
		return topicOptions;
	}

	public void setTopicOptions(List<TomTopicOption> topicOptions) {
		this.topicOptions = topicOptions;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}