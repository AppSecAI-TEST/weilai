/**
 * Project Name:enterpriseuniversity 
 * File Name:ExamBean.java 
 * Package Name:com.chinamobo.ue.api.exam.service
 * @author Acemon
 * Date:2017年7月28日上午9:58:27
 */
package com.chinamobo.ue.api.exam.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.chinamobo.ue.api.exam.dto.AnswerResults;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamScore;

/**
 * ClassName:ExamBean
 * Function:TODO
 * @author Acemon
 * 2017年7月28日
 */
public class ExamBean {
	private JSONObject data;
	private AnswerResults answerResults;
	private TomExamScore examScore;
	private List<TomExamAnswerDetails> examAnswerDetails;
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	public AnswerResults getAnswerResults() {
		return answerResults;
	}
	public void setAnswerResults(AnswerResults answerResults) {
		this.answerResults = answerResults;
	}
	public TomExamScore getExamScore() {
		return examScore;
	}
	public void setExamScore(TomExamScore examScore) {
		this.examScore = examScore;
	}
	public List<TomExamAnswerDetails> getExamAnswerDetails() {
		return examAnswerDetails;
	}
	public void setExamAnswerDetails(List<TomExamAnswerDetails> examAnswerDetails) {
		this.examAnswerDetails = examAnswerDetails;
	}
}
