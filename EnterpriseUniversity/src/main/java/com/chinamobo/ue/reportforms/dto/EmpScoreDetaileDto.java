package com.chinamobo.ue.reportforms.dto;

import java.util.Date;

/**
 * 功能描述  [学员报表-积分]
 * 创建者 LXT
 * 创建时间 2017年3月14日 下午7:04:12
 */
public class EmpScoreDetaileDto {

	private String code; //用户名
	private String name; //姓名
	private String orgCode;//所属组织编码
	private String orgName;//所属组织
	private String oneDeptCode;//一级部门编码
	private String oneDeptName;//一级部门
	private String twoDeptCode;//二级部门编码
	private String twoDeptName;//二级部门
	private String threeDeptCode;//三级部门编码
	private String threeDeptName;//三级部门
	private String isAdmin;//管理角色分配（人员类别）
	private Double enumber;//总积分
	private Double addUpENumber;//获得积分
	private Date getTime; //积分获得时间
	private Double exchangeNumber; //积分数
	private String road;//积分类型
	
	private String roads;//积分类型数组
	private String ebs;//积分数组
	
	private String completeActivityE;//完成活动积分	18
	private String onLineActivityCourseE;//在线活动课程积分	2
	private String onLineOpenCourseE;//在线公开课程积分	13
	private String onLineActivityExamE;//在线活动考试积分		3
	private String onLineAutocephalyExamE;//在线独立考试积分	14
	private String evaluateE;//评价积分	5+6 课程评分+讲师评分
	private String courseEvaluateE;//课程评分 	5
	private String examEvaluateE;//讲师评分		6
	private String commentE;//评论积分	15
	//private String landingE;//登陆积分	10
	private String platformSignE;//平台签到积分	1
	private String offLineCourseSignE;//线下课程签到积分	11
	private String commentthumbUpE;//评论点赞积分		8
	private String discussionTopics;//讨论圈发起话题		7
	private String submitQuestionnaire;//提交问卷积分		4
	private String examNotPass;//考试未通过积分	9
	private String exchangeGoods;//兑换商品		17
	private String learningState;//学习状态积分 	16
	private String archivesE;//档案积分	12
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOneDeptCode() {
		return oneDeptCode;
	}
	public void setOneDeptCode(String oneDeptCode) {
		this.oneDeptCode = oneDeptCode;
	}
	public String getOneDeptName() {
		return oneDeptName;
	}
	public void setOneDeptName(String oneDeptName) {
		this.oneDeptName = oneDeptName;
	}
	public String getTwoDeptCode() {
		return twoDeptCode;
	}
	public void setTwoDeptCode(String twoDeptCode) {
		this.twoDeptCode = twoDeptCode;
	}
	public String getTwoDeptName() {
		return twoDeptName;
	}
	public void setTwoDeptName(String twoDeptName) {
		this.twoDeptName = twoDeptName;
	}
	public String getThreeDeptCode() {
		return threeDeptCode;
	}
	public void setThreeDeptCode(String threeDeptCode) {
		this.threeDeptCode = threeDeptCode;
	}
	public String getThreeDeptName() {
		return threeDeptName;
	}
	public void setThreeDeptName(String threeDeptName) {
		this.threeDeptName = threeDeptName;
	}
	public Double getEnumber() {
		return enumber;
	}
	public void setEnumber(Double enumber) {
		this.enumber = enumber;
	}
	
	public String getCompleteActivityE() {
		int i=0;
		int j=0;
		if(getOnLineActivityCourseE()!=null && !"".equals(getOnLineActivityCourseE())){
			i=Integer.parseInt(getOnLineActivityCourseE());
		}
		if(getOnLineActivityExamE()!=null && !"".equals(getOnLineActivityExamE())){
			j=Integer.parseInt(getOnLineActivityExamE());
		}
		return (i+j)+"";
	}

	public String getOnLineActivityCourseE() {
		return onLineActivityCourseE;
	}
	public void setOnLineActivityCourseE(String onLineActivityCourseE) {
		this.onLineActivityCourseE = onLineActivityCourseE;
	}
	public String getOnLineOpenCourseE() {
		return onLineOpenCourseE;
	}
	public void setOnLineOpenCourseE(String onLineOpenCourseE) {
		this.onLineOpenCourseE = onLineOpenCourseE;
	}
	public String getOnLineActivityExamE() {
		return onLineActivityExamE;
	}
	public void setOnLineActivityExamE(String onLineActivityExamE) {
		this.onLineActivityExamE = onLineActivityExamE;
	}
	public String getOnLineAutocephalyExamE() {
		return onLineAutocephalyExamE;
	}
	public void setOnLineAutocephalyExamE(String onLineAutocephalyExamE) {
		this.onLineAutocephalyExamE = onLineAutocephalyExamE;
	}
	public String getEvaluateE() {
		int c=0;
		int e=0;
		if(getCourseEvaluateE()!=null && !"".equals(getCourseEvaluateE().trim())){
			c=Integer.parseInt(getCourseEvaluateE().trim());
		}
		if(getExamEvaluateE()!=null && !"".equals(getExamEvaluateE().trim())){
			e=Integer.parseInt(getExamEvaluateE().trim());
		}
		return String.valueOf(c+e);
	}

	public String getCommentE() {
		return commentE;
	}
	public void setCommentE(String commentE) {
		this.commentE = commentE;
	}
//	public String getLandingE() {
//		return landingE;
//	}
//	public void setLandingE(String landingE) {
//		this.landingE = landingE;
//	}
	public String getPlatformSignE() {
		return platformSignE;
	}
	public void setPlatformSignE(String platformSignE) {
		this.platformSignE = platformSignE;
	}
	public String getOffLineCourseSignE() {
		return offLineCourseSignE;
	}
	public void setOffLineCourseSignE(String offLineCourseSignE) {
		this.offLineCourseSignE = offLineCourseSignE;
	}
	public String getCommentthumbUpE() {
		return commentthumbUpE;
	}
	public void setCommentthumbUpE(String commentthumbUpE) {
		this.commentthumbUpE = commentthumbUpE;
	}
	public String getLearningState() {
		return learningState;
	}
	public void setLearningState(String learningState) {
		this.learningState = learningState;
	}
	public Double getAddUpENumber() {
		return addUpENumber;
	}
	public void setAddUpENumber(Double addUpENumber) {
		this.addUpENumber = addUpENumber;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getRoads() {
		return roads;
	}
	public void setRoads(String roads) {
		this.roads = roads;
	}
	public String getEbs() {
		return ebs;
	}
	public void setEbs(String ebs) {
		this.ebs = ebs;
	}
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	public Double getExchangeNumber() {
		return exchangeNumber;
	}
	public void setExchangeNumber(Double exchangeNumber) {
		this.exchangeNumber = exchangeNumber;
	}
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getDiscussionTopics() {
		return discussionTopics;
	}
	public void setDiscussionTopics(String discussionTopics) {
		this.discussionTopics = discussionTopics;
	}
	public String getSubmitQuestionnaire() {
		return submitQuestionnaire;
	}
	public void setSubmitQuestionnaire(String submitQuestionnaire) {
		this.submitQuestionnaire = submitQuestionnaire;
	}
	public String getExamNotPass() {
		return examNotPass;
	}
	public void setExamNotPass(String examNotPass) {
		this.examNotPass = examNotPass;
	}
	public String getExchangeGoods() {
		return exchangeGoods;
	}
	public void setExchangeGoods(String exchangeGoods) {
		this.exchangeGoods = exchangeGoods;
	}
	public String getCourseEvaluateE() {
		return courseEvaluateE;
	}
	public void setCourseEvaluateE(String courseEvaluateE) {
		this.courseEvaluateE = courseEvaluateE;
	}
	public String getExamEvaluateE() {
		return examEvaluateE;
	}
	public void setExamEvaluateE(String examEvaluateE) {
		this.examEvaluateE = examEvaluateE;
	}
	public String getArchivesE() {
		return archivesE;
	}
	public void setArchivesE(String archivesE) {
		this.archivesE = archivesE;
	}
	
	
}
