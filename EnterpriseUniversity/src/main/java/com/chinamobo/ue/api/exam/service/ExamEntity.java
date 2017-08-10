/**
 * Project Name:enterpriseuniversity 
 * File Name:ExamEntity.java 
 * Package Name:com.chinamobo.ue.api.exam.service
 * @author Acemon
 * Date:2017年7月31日下午1:43:03
 */
package com.chinamobo.ue.api.exam.service;

import java.util.List;

/**
 * ClassName:ExamEntity
 * Function:TODO
 * @author Acemon
 * 2017年7月31日
 */
public class ExamEntity {
	private String userAgent="Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0";
	private String apiName;
	private String apiType;
	private String token;
	private String userId;
	private String examId;
	private String examTotalTime;
	private List<Topic> examResult;
	public class Topic{
		private Integer topicId;
		private Integer optionId;
		public Topic(Integer topicId,Integer optionId){
			this.topicId = topicId;
			this.optionId = optionId;
		}
		public Integer getTopicId() {
			return topicId;
		}
		public void setTopicId(Integer topicId) {
			this.topicId = topicId;
		}
		public Integer getOptionId() {
			return optionId;
		}
		public void setOptionId(Integer optionId) {
			this.optionId = optionId;
		}
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getApiType() {
		return apiType;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamTotalTime() {
		return examTotalTime;
	}
	public void setExamTotalTime(String examTotalTime) {
		this.examTotalTime = examTotalTime;
	}
	public List<Topic> getExamResult() {
		return examResult;
	}
	public void setExamResult(List<Topic> examResult) {
		this.examResult = examResult;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}
