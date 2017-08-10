package com.chinamobo.ue.reportforms.dto;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityDeptListDto {
	
		private Integer actitvtyId;//活动ID
		
		private Integer courseId;//课程ID
		
		private Integer examId;//考试ID
		
		private String code;//人员Code
		private String codes;//该部门下参与该活动的所有人;
		
		private String orgcode;//所属组织编码
		private String orgname;//所属组织
		private String oneDeptCode;//一级部门编码
		private String oneDeptName;//一级部门
		private String twoDeptCode;//二级部门编码
		private String twoDeptName;//二级部门
		private String threeDeptCode;//三级部门编码
		private String threeDeptName;//三级部门
		private String activityNumber;//活动编号
		
		private String activityName;//活动名称
		
		private String activityType;//活动类别
		
		private Date createTime;//创建时间
		
		private String isRequired;//选修必修(活动性质)
		
		private Date activityStartTime ;//活动开始时间
		
		private Date activityEndTime;//活动结束时间
		
		private String status;//活动状态（进行中/已结束）
		
		private String admins;//管理授权
		
		private String adminsName;
		
		private String needApply;//是否需要报名
		
		private Integer openNum =0;//开放人数
		
		private Integer  isLearningNumber;//学习中人数
		
		private Integer toCourse; //完成人数(课程)
		
		private Integer notLearnNumber;//未学习人数
		
		private Integer toExam; //完成人数(考试)
		
		private double totalTime;//总时长
		
		private String completedRate;//活动完成比率
		
		private Integer Score;//总成绩
		
		private String averageScore;//平均成绩
		private String averagePassRate;//平均通过率
		private Integer joinExam;//考试参加人数
		private Integer OpenNumber;//选修开放人数
		private Integer numberOfParticipants ;//选项选修报名人数
		private String numberOfParticipantsRate ;//选修报名率
		private Integer  integral;//获得积分
		private Integer toBe;//活动学习人数
		private Integer to;//完成人数
		private Integer joinCourse;//参加课程的人数
		private Integer examCount;//活动中考试数
		
		
		
		
		public Integer getExamId() {
			return examId;
		}

		public void setExamId(Integer examId) {
			this.examId = examId;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Integer getExamCount() {
			return examCount;
		}

		public void setExamCount(Integer examCount) {
			this.examCount = examCount;
		}

		public Integer getJoinCourse() {
			return joinCourse;
		}

		public void setJoinCourse(Integer joinCourse) {
			this.joinCourse = joinCourse;
		}

		public Integer getCourseId() {
			return courseId;
		}

		public void setCourseId(Integer courseId) {
			this.courseId = courseId;
		}
		private Date endTime;//
		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}
		/*private String orgname; //组织
		
		private String deptname;//一级部门
		
		private String depttopname;//二级部门
		
		private String deptthname;//三级部门
*/		
		

		
		public String getOrgcode() {
			return orgcode;
		}

		public void setOrgcode(String orgcode) {
			this.orgcode = orgcode;
		}

		public String getOrgname() {
			return orgname;
		}

		public void setOrgname(String orgname) {
			this.orgname = orgname;
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

		
		
		public String getAdminsName() {
			return adminsName;
		}

		public void setAdminsName(String adminsName) {
			this.adminsName = adminsName;
		}

		
		
		public Integer getOpenNum() {
			return openNum;
		}

		public void setOpenNum(Integer openNum) {
			this.openNum = openNum;
		}
		
	
		
		public Integer getNotLearnNumber() {
		/*	if(getIsLearningNumber()==null || getToCourse()==null || getIsLearningNumber()==0 || getToCourse()==0){
				return  0;
			}
			return getOpenNum()-getIsLearningNumber()-getToCourse();*/
//			if(getJoinCourse()==null || getOpenNum()==null || getOpenNum()==0 || getJoinCourse()==0){
//				return 0;
//			}
//			return getOpenNum()-getJoinCourse();
			return getOpenNum()-getIsLearningNumber()-getTo();
		}

		public void setNotLearnNumber(Integer notLearnNumber) {
			this.notLearnNumber = notLearnNumber;
		}
		

		
		public Integer getToCourse() {
			return toCourse;
		}

		public void setToCourse(Integer toCourse) {
			this.toCourse = toCourse;
		}

		
		public Integer getToExam() {
			return toExam;
		}

		public void setToExam(Integer toExam) {
			this.toExam = toExam;
		}
		
	
		
		public double getTotalTime() {
			return totalTime;
//			return (int) ((getActivityEndTime().getTime()-getActivityStartTime().getTime())/1000/60);
		}

		public void setTotalTime(double totalTime) {
			this.totalTime = totalTime;
		}

		
		

		public String getCompletedRate() {
			if(getOpenNum()==null || getTo()==null || getOpenNum()==0 || getTo()==0){
				return "0%";
			}
			return	new DecimalFormat("##0.00").format((double)getTo()/getOpenNum()*100)+"%";
		}

		public void setCompletedRate(String completedRate) {
			this.completedRate = completedRate;
		}
		
		public Integer getScore() {
			return Score;
		}
		public void setScore(Integer score) {
			Score = score;
		}
		
		
		public String getAverageScore() {
			if(getScore()==null || getScore()==0 || getOpenNum()==null || getOpenNum()==0){
				return "0";
			}
			/*return (double) (getScore()/getToExam())+"";*/
			return  (new DecimalFormat("#.##").format((double)getScore()/(double)getOpenNum()))+"";
		}

		
	
		public String getAveragePassRate() {
			if(getToExam()==null || getJoinExam()==null || getExamCount()==null || getToExam()==0 ||  getJoinExam()==0 || getExamCount()==0){
				return "0%";
			}
			return averagePassRate;
//			return new DecimalFormat("##0.00").format( (double)getToExam()/getJoinExam()*100)+"%";
//			return	new DecimalFormat("##0.00").format( (double)getToExam()/(getJoinExam()*getExamCount())*100)+"%";
			/*return	new DecimalFormat("##0.00").format( (double)getToExam()/getOpenNum()*100)+"%";*/
			
		}

		public void setAveragePassRate(String averagePassRate) {
			this.averagePassRate = averagePassRate;
		}
		
		
		public Integer getJoinExam() {
			return joinExam;
		}

		public void setJoinExam(Integer joinExam) {
			this.joinExam = joinExam;
		}
		
		public Integer getOpenNumber() {
			return OpenNumber;
		}

		public void setOpenNumber(Integer openNumber) {
			OpenNumber = openNumber;
		}
		

		public Integer getNumberOfParticipants() {
			return numberOfParticipants;
		}

		public void setNumberOfParticipants(Integer numberOfParticipants) {
			this.numberOfParticipants = numberOfParticipants;
		}
		
		
		public String getNumberOfParticipantsRate() {
			if(getNumberOfParticipants()==null || getOpenNumber()==null ||getNumberOfParticipants()==0 || getOpenNumber()==0){
				return "0%";
			}
			return	new DecimalFormat("###.00").format( (double)getNumberOfParticipants()/getOpenNumber()*100)+"%";
		}

		public void setNumberOfParticipantsRate(String numberOfParticipantsRate) {
			this.numberOfParticipantsRate = numberOfParticipantsRate;
		}
		

		public Integer getIntegral() {
			return integral;
		}

		public void setIntegral(Integer integral) {
			this.integral = integral;
		}
		
		
		

		public Integer getToBe() {
			return toBe;
		}

		public void setToBe(Integer toBe) {
			this.toBe = toBe;
		}
		

/*		public Integer getTo() {
			return to;
			if(getToCourse()==null || getToExam()==null || getToCourse()==0 || getToExam()==0){
				return 0;
			}else if(getToCourse()==getToExam()){
				return getToCourse();
			}else{
				return Math.abs(getToCourse()-getToExam());
			}
		}
		public void setTo(Integer to) {
			this.to = to;
		}
*/
		
		public Integer getTo() {
			return to;
		}

		public void setTo(Integer to) {
			this.to = to;
		}
		public Integer getActitvtyId() {
			return actitvtyId;
		}

		public void setActitvtyId(Integer actitvtyId) {
			this.actitvtyId = actitvtyId;
		}


		public String getActivityNumber() {
			return activityNumber;
		}

		public void setActivityNumber(String activityNumber) {
			this.activityNumber = activityNumber;
		}

		public String getActivityName() {
			return activityName;
		}

		public void setActivityName(String activityName) {
			this.activityName = activityName;
		}

		public String getActivityType() {
			return activityType;
		}

		public void setActivityType(String activityType) {
			this.activityType = activityType;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getIsRequired() {
			return getNeedApply();
		}

		public void setIsRequired(String isRequired) {
			this.isRequired = isRequired;
		}

		public Date getActivityStartTime() {
			return activityStartTime;
		}

		public void setActivityStartTime(Date activityStartTime) {
			this.activityStartTime = activityStartTime;
		}

		public Date getActivityEndTime() {
			return activityEndTime;
		}

		public void setActivityEndTime(Date activityEndTime) {
			this.activityEndTime = activityEndTime;
		}
		public String getStatus() {
			if(getActivityStartTime().getTime()<new Date().getTime() && getActivityEndTime().getTime()>new Date().getTime()){
				return "1";
			}else if(getActivityStartTime().getTime()>new Date().getTime() ){
				return "2";
			}
			return "3";
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getAdmins() {
			return admins;
		}

		public void setAdmins(String admins) {
			this.admins = admins;
		}

		public String getNeedApply() {
			return needApply;
		}

		public void setNeedApply(String needApply) {
			this.needApply = needApply;
		}

		public String getCodes() {
			return codes;
		}

		public void setCodes(String codes) {
			this.codes = codes;
		}

		public Integer  getIsLearningNumber() {
			return isLearningNumber;
		}

		public void setIsLearningNumber(Integer isLearningNumber) {
			this.isLearningNumber = isLearningNumber;
		}
		
}
