package com.chinamobo.ue.reportforms.dto;

import java.text.DecimalFormat;

public class OrgActivityLearnDto {
	
	private String code;//编号
	
	private String orgName;//公司部门
	
	private Integer codeNum;//员工数量
	
	private Integer joinStudyNum;//参加学习的人数
	
	private String  joinStudyNumRate;//参加学习率
	
	private Integer  requireCodeNum;//必修统计人数
	
	private Integer requireStudyTime;//必修总学习时长
	
	private Double requireAvgStudyTime;//必修平均学习时长
	
	private  Integer requireComSection;//必修完成的章节数
	
	private Integer requireTotalSection;//必修的总学习章节
	
	private String requireStudyAvgProcess;//必修平均学习进度
	
	private Double requireAvgScore;//必修平均成绩
	
	private  Integer requireCourseENum;//必修活动课程E币
	
	private Double requireAvgInter;//必修平均获得积分
	
	private Integer requireScorePassNum;//必修考试及格人数
	
	private Integer requirescoreNum;//考试总人数
	
	private String requirePassRate;//必修活动考试及格率
	
	private Integer requireCourseCom;//必修活动课程完成人数
	
	private String requireCourseRate;//必修活动课程完成率
	
	private Integer requireCourseNum;//必修活动课程总人数
	
	private Integer requirePassExamE;//必修活动考试获得的E币
	
	private Integer requireTotalActivity;//必修总活动数量
	
	
	private Integer  optionCodeNum;//选修统计人数
	
	private Double optionAvgStudyTime;//选修平均学习时长
	
	private Integer optionStudyTime;//选修总学习时长
	
	private Integer optionComSection;//选修完成的章节数
	
	private Integer optionTotalSection;//选修总的章节数
	
	private String optionStudyAvgProcess;//选修平均学习进度
	
	private Double optionAvgScore;//选修平均成绩
	
	private Integer optionComCouNumE;//选修活动完城课程的E币数
	
	private Double optionAvgInter;//选修平均获得积分
	
	private Integer optionScorePassNum;//选修考试及格人数
	
	private Integer optionscoreNum;//选修考试总人数
	
	private String optionPassRate;//选修活动考试及格率
	
	private Integer optionCourseCom;//选修活动课程完成人数
	
	private Integer optionCourseNum;//选修活动课程总人数
	
	private String optionCourseRate;//选修活动课程完成率
	
	private Integer optionPassExamE;//选修活动考试获得的E币
	
	private Integer optionTotalActivity;//选修总活动数量


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(Integer codeNum) {
		this.codeNum = codeNum;
	}

	public Integer getJoinStudyNum() {
		return joinStudyNum;
	}

	public void setJoinStudyNum(Integer joinStudyNum) {
		this.joinStudyNum = joinStudyNum;
	}


	public Integer getRequireCodeNum() {
		return requireCodeNum;
	}

	public void setRequireCodeNum(Integer requireCodeNum) {
		this.requireCodeNum = requireCodeNum;
	}

	public Integer getRequireStudyTime() {
		return requireStudyTime;
	}

	public void setRequireStudyTime(Integer requireStudyTime) {
		this.requireStudyTime = requireStudyTime;
	}

	public Double getRequireAvgStudyTime() {
		return requireAvgStudyTime;
	}

	public void setRequireAvgStudyTime(Double requireAvgStudyTime) {
		this.requireAvgStudyTime = requireAvgStudyTime;
	}

	public Integer getRequireComSection() {
		return requireComSection;
	}

	public void setRequireComSection(Integer requireComSection) {
		this.requireComSection = requireComSection;
	}

	public Integer getRequireTotalSection() {
		return requireTotalSection;
	}

	public void setRequireTotalSection(Integer requireTotalSection) {
		this.requireTotalSection = requireTotalSection;
	}

	public String getRequireStudyAvgProcess() {
		return requireStudyAvgProcess;
	}

	public void setRequireStudyAvgProcess(String requireStudyAvgProcess) {
		this.requireStudyAvgProcess = requireStudyAvgProcess;
	}

	public Double getRequireAvgScore() {
		return requireAvgScore;
	}

	public void setRequireAvgScore(Double requireAvgScore) {
		this.requireAvgScore = requireAvgScore;
	}

	public Integer getRequireCourseENum() {
		return requireCourseENum;
	}

	public void setRequireCourseENum(Integer requireCourseENum) {
		this.requireCourseENum = requireCourseENum;
	}

	public Double getRequireAvgInter() {
		return requireAvgInter;
	}

	public void setRequireAvgInter(Double requireAvgInter) {
		this.requireAvgInter = requireAvgInter;
	}

	public Integer getRequireScorePassNum() {
		return requireScorePassNum;
	}

	public void setRequireScorePassNum(Integer requireScorePassNum) {
		this.requireScorePassNum = requireScorePassNum;
	}

	public Integer getRequirescoreNum() {
		return requirescoreNum;
	}

	public void setRequirescoreNum(Integer requirescoreNum) {
		this.requirescoreNum = requirescoreNum;
	}

	public String getRequirePassRate() {
		return requirePassRate;
	}

	public void setRequirePassRate(String requirePassRate) {
		this.requirePassRate = requirePassRate;
	}

	public Integer getRequireCourseCom() {
		return requireCourseCom;
	}

	public void setRequireCourseCom(Integer requireCourseCom) {
		this.requireCourseCom = requireCourseCom;
	}

	public String getRequireCourseRate() {
		return requireCourseRate;
	}

	public void setRequireCourseRate(String requireCourseRate) {
		this.requireCourseRate = requireCourseRate;
	}

	public Integer getRequireCourseNum() {
		return requireCourseNum;
	}

	public void setRequireCourseNum(Integer requireCourseNum) {
		this.requireCourseNum = requireCourseNum;
	}

	public Integer getRequirePassExamE() {
		return requirePassExamE;
	}

	public void setRequirePassExamE(Integer requirePassExamE) {
		this.requirePassExamE = requirePassExamE;
	}

	public Integer getRequireTotalActivity() {
		return requireTotalActivity;
	}

	public void setRequireTotalActivity(Integer requireTotalActivity) {
		this.requireTotalActivity = requireTotalActivity;
	}

	public Integer getOptionCodeNum() {
		return optionCodeNum;
	}

	public void setOptionCodeNum(Integer optionCodeNum) {
		this.optionCodeNum = optionCodeNum;
	}

	public Double getOptionAvgStudyTime() {
		return optionAvgStudyTime;
	}

	public void setOptionAvgStudyTime(Double optionAvgStudyTime) {
		this.optionAvgStudyTime = optionAvgStudyTime;
	}

	public Integer getOptionStudyTime() {
		return optionStudyTime;
	}

	public void setOptionStudyTime(Integer optionStudyTime) {
		this.optionStudyTime = optionStudyTime;
	}

	public Integer getOptionComSection() {
		return optionComSection;
	}

	public void setOptionComSection(Integer optionComSection) {
		this.optionComSection = optionComSection;
	}

	public Integer getOptionTotalSection() {
		return optionTotalSection;
	}

	public void setOptionTotalSection(Integer optionTotalSection) {
		this.optionTotalSection = optionTotalSection;
	}

	public String getOptionStudyAvgProcess() {
		return optionStudyAvgProcess;
	}

	public void setOptionStudyAvgProcess(String optionStudyAvgProcess) {
		this.optionStudyAvgProcess = optionStudyAvgProcess;
	}

	public Double getOptionAvgScore() {
		return optionAvgScore;
	}

	public void setOptionAvgScore(Double optionAvgScore) {
		this.optionAvgScore = optionAvgScore;
	}

	public Integer getOptionComCouNumE() {
		return optionComCouNumE;
	}

	public void setOptionComCouNumE(Integer optionComCouNumE) {
		this.optionComCouNumE = optionComCouNumE;
	}

	public Double getOptionAvgInter() {
		return optionAvgInter;
	}

	public void setOptionAvgInter(Double optionAvgInter) {
		this.optionAvgInter = optionAvgInter;
	}

	public Integer getOptionScorePassNum() {
		return optionScorePassNum;
	}

	public void setOptionScorePassNum(Integer optionScorePassNum) {
		this.optionScorePassNum = optionScorePassNum;
	}

	public Integer getOptionscoreNum() {
		return optionscoreNum;
	}

	public void setOptionscoreNum(Integer optionscoreNum) {
		this.optionscoreNum = optionscoreNum;
	}

	public String getOptionPassRate() {
		return optionPassRate;
	}

	public void setOptionPassRate(String optionPassRate) {
		this.optionPassRate = optionPassRate;
	}

	public Integer getOptionCourseCom() {
		return optionCourseCom;
	}

	public void setOptionCourseCom(Integer optionCourseCom) {
		this.optionCourseCom = optionCourseCom;
	}

	public Integer getOptionCourseNum() {
		return optionCourseNum;
	}

	public void setOptionCourseNum(Integer optionCourseNum) {
		this.optionCourseNum = optionCourseNum;
	}

	public String getOptionCourseRate() {
		return optionCourseRate;
	}

	public void setOptionCourseRate(String optionCourseRate) {
		this.optionCourseRate = optionCourseRate;
	}

	public Integer getOptionPassExamE() {
		return optionPassExamE;
	}

	public void setOptionPassExamE(Integer optionPassExamE) {
		this.optionPassExamE = optionPassExamE;
	}

	public Integer getOptionTotalActivity() {
		return optionTotalActivity;
	}

	public void setOptionTotalActivity(Integer optionTotalActivity) {
		this.optionTotalActivity = optionTotalActivity;
	}

	
/*	public Integer getRequireStudyTime() {
		return requireStudyTime;
	}

	public void setRequireStudyTime(Integer requireStudyTime) {
		this.requireStudyTime = requireStudyTime;
	}

	public Integer getOptionStudyTime() {
		return optionStudyTime;
	}

	public void setOptionStudyTime(Integer optionStudyTime) {
		this.optionStudyTime = optionStudyTime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getCodeNum() {
		return codeNum;
	}

	public void setCodeNum(Integer codeNum) {
		this.codeNum = codeNum;
	}

	public Integer getJoinStudyNum() {
		return joinStudyNum;
	}

	public void setJoinStudyNum(Integer joinStudyNum) {
		this.joinStudyNum = joinStudyNum;
	}

	

	public Integer getRequireCodeNum() {
		return requireCodeNum;
	}

	public void setRequireCodeNum(Integer requireCodeNum) {
		this.requireCodeNum = requireCodeNum;
	}

	public Integer getRequireAvgStudyTime() {
		if(getRequireStudyTime() ==null || getJoinStudyNum()==null || getRequireStudyTime() ==0 || getJoinStudyNum()==0){
			return 0;
		}
		return getRequireStudyTime()/getJoinStudyNum();
	}

	public void setRequireAvgStudyTime(Double requireAvgStudyTime) {
		this.requireAvgStudyTime = requireAvgStudyTime;
	}

	public String getRequireStudyAvgProcess() {
		if( getRequireComSection()== null || getRequireTotalSection()==null || getRequireTotalActivity()==null || getRequireTotalActivity()==0 || getRequireComSection()==0 || getRequireTotalSection()== 0){
			return "0%";
		}
		return	new DecimalFormat("##0.00").format( (double)getRequireComSection()/(getRequireTotalSection()*getRequireTotalActivity())*100)+"%";
		
	}

	public void setRequireStudyAvgProcess(String requireStudyAvgProcess) {
		this.requireStudyAvgProcess = requireStudyAvgProcess;
	}

	public Double getRequireAvgScore() {
		return requireAvgScore;
	}

	public void setRequireAvgScore(Double requireAvgScore) {
		this.requireAvgScore = requireAvgScore;
	}

	public Double getRequireAvgInter() {
		if(getRequireCodeNum()== null ||  (getRequireCourseENum()+getRequirePassExamE())==0 || getRequireCodeNum()==0){
			return 0.0;
		}
		return	 (double) ((getRequireCourseENum()+getRequirePassExamE())/getRequireCodeNum());
	}

	public void setRequireAvgInter(Double requireAvgInter) {
		this.requireAvgInter = requireAvgInter;
	}

	public String getRequirePassRate() {
		if(getRequirescoreNum()==null || getRequireScorePassNum()== null ||getRequireScorePassNum()==0 ||  getRequirescoreNum()==0 ){
			return "0%";
		}
		return	new DecimalFormat("#00.00").format((double) getRequireScorePassNum()/getRequirescoreNum()*100)+"%";
	}

	public void setRequirePassRate(String requirePassRate) {
		this.requirePassRate = requirePassRate;
	}

	public String getRequireCourseRate() {
		
		if( getRequireTotalSection()==null  ||  getRequireComSection()== null || getRequireTotalActivity()==null || getRequireComSection()== 0 || getRequireTotalSection()==0|| getRequireTotalActivity()== 0){
			return "0%";
		}
		return	new DecimalFormat("##0.00").format( (double)getRequireComSection()/(getRequireTotalSection()*getJoinStudyNum())*100)+"%";
	}

	public void setRequireCourseRate(String requireCourseRate) {
		this.requireCourseRate = requireCourseRate;
	}

	public Integer getOptionCodeNum() {
		return optionCodeNum;
	}

	public void setOptionCodeNum(Integer optionCodeNum) {
		this.optionCodeNum = optionCodeNum;
	}

	public Integer getOptionAvgStudyTime() {
		if(getOptionStudyTime() ==null || getJoinStudyNum()==null || getOptionStudyTime() ==0 || getJoinStudyNum()==0){
			return 0;
		}
		return getOptionStudyTime()/getJoinStudyNum();
	}
	
	public void setOptionAvgStudyTime(Double optionAvgStudyTime) {
		this.optionAvgStudyTime = optionAvgStudyTime;
	}

	public String getOptionStudyAvgProcess() {
		
		if( getOptionTotalSection()==null  ||  getOptionComSection()== null || getOptionTotalActivity()==null || getOptionComSection()== 0 || getRequireTotalSection()==0|| getOptionTotalActivity()== 0){
			return "0%";
		}
		return	new DecimalFormat("##0.00").format( (double)getOptionComSection()/(getOptionTotalSection()*getOptionTotalActivity())*100)+"%";
		
	}

	public void setOptionStudyAvgProcess(String optionStudyAvgProcess) {
		this.optionStudyAvgProcess = optionStudyAvgProcess;
	}

	public Double getOptionAvgScore() {
		return optionAvgScore;
	}

	public void setOptionAvgScore(Double optionAvgScore) {
		this.optionAvgScore = optionAvgScore;
	}

	public Double getOptionAvgInter() {
		if( getOptionCodeNum()==null || getOptionComCouNumE()==null|| getOptionPassExamE()==null || (getOptionComCouNumE()+getOptionPassExamE())==0 || getOptionCodeNum()==0){
			return 0.0;
		}
		return	 (double) ((getOptionComCouNumE()+getOptionPassExamE())/getOptionCodeNum());
	}

	public void setOptionAvgInter(Double optionAvgInter) {
		this.optionAvgInter = optionAvgInter;
	}

	public String getOptionPassRate() {
		if(getOptionScorePassNum()==null || getOptionscoreNum()== null || getOptionScorePassNum()==0 ||  getOptionscoreNum()==0 ){
			return "0%";
		}
		return new DecimalFormat("#00.00").format( (double)getOptionScorePassNum()/getOptionscoreNum()*100)+"%";
	
		
	}

	public void setOptionPassRate(String optionPassRate) {
		this.optionPassRate = optionPassRate;
	}

	public String getOptionCourseRate() {
	
		if( getOptionTotalSection()==null  ||  getOptionComSection()== null || getOptionTotalActivity()==null || getOptionComSection()== 0 || getRequireTotalSection()==0|| getOptionTotalActivity()== 0){
			return "0%";
		}
		return	new DecimalFormat("##0.00").format( (double)getOptionComSection()/(getOptionTotalSection()*getJoinStudyNum())*100)+"%";
	}

	public void setOptionCourseRate(String optionCourseRate) {
		this.optionCourseRate = optionCourseRate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getRequireComSection() {
		return requireComSection;
	}

	public void setRequireComSection(Integer requireComSection) {
		this.requireComSection = requireComSection;
	}

	public Integer getRequireTotalSection() {
		return requireTotalSection;
	}

	public void setRequireTotalSection(Integer requireTotalSection) {
		this.requireTotalSection = requireTotalSection;
	}

	public Integer getRequireCourseENum() {
		return requireCourseENum;
	}

	public void setRequireCourseENum(Integer requireCourseENum) {
		this.requireCourseENum = requireCourseENum;
	}

	public Integer getRequireScorePassNum() {
		return requireScorePassNum;
	}

	public void setRequireScorePassNum(Integer requireScorePassNum) {
		this.requireScorePassNum = requireScorePassNum;
	}

	public Integer getRequirescoreNum() {
		return requirescoreNum;
	}

	public void setRequirescoreNum(Integer requirescoreNum) {
		this.requirescoreNum = requirescoreNum;
	}

	public Integer getRequireCourseCom() {
		return requireCourseCom;
	}

	public void setRequireCourseCom(Integer requireCourseCom) {
		this.requireCourseCom = requireCourseCom;
	}

	public Integer getRequireCourseNum() {
		return requireCourseNum;
	}

	public void setRequireCourseNum(Integer requireCourseNum) {
		this.requireCourseNum = requireCourseNum;
	}

	public Integer getRequirePassExamE() {
		return requirePassExamE;
	}

	public void setRequirePassExamE(Integer requirePassExamE) {
		this.requirePassExamE = requirePassExamE;
	}

	public Integer getOptionComSection() {
		return optionComSection;
	}

	public void setOptionComSection(Integer optionComSection) {
		this.optionComSection = optionComSection;
	}

	public Integer getOptionTotalSection() {
		return optionTotalSection;
	}

	public void setOptionTotalSection(Integer optionTotalSection) {
		this.optionTotalSection = optionTotalSection;
	}

	public Integer getOptionComCouNumE() {
		return optionComCouNumE;
	}

	public void setOptionComCouNumE(Integer optionComCouNumE) {
		this.optionComCouNumE = optionComCouNumE;
	}

	public Integer getOptionScorePassNum() {
		return optionScorePassNum;
	}

	public void setOptionScorePassNum(Integer optionScorePassNum) {
		this.optionScorePassNum = optionScorePassNum;
	}

	public Integer getOptionscoreNum() {
		return optionscoreNum;
	}

	public void setOptionscoreNum(Integer optionscoreNum) {
		this.optionscoreNum = optionscoreNum;
	}

	public Integer getOptionCourseCom() {
		return optionCourseCom;
	}

	public void setOptionCourseCom(Integer optionCourseCom) {
		this.optionCourseCom = optionCourseCom;
	}

	public Integer getOptionCourseNum() {
		return optionCourseNum;
	}

	public void setOptionCourseNum(Integer optionCourseNum) {
		this.optionCourseNum = optionCourseNum;
	}

	public Integer getOptionPassExamE() {
		return optionPassExamE;
	}

	public void setOptionPassExamE(Integer optionPassExamE) {
		this.optionPassExamE = optionPassExamE;
	}

	public Integer getRequireTotalActivity() {
		return requireTotalActivity;
	}

	public void setRequireTotalActivity(Integer requireTotalActivity) {
		this.requireTotalActivity = requireTotalActivity;
	}

	public Integer getOptionTotalActivity() {
		return optionTotalActivity;
	}

	public void setOptionTotalActivity(Integer optionTotalActivity) {
		this.optionTotalActivity = optionTotalActivity;
	}*/
	
	
	public String getJoinStudyNumRate() {
		if(getJoinStudyNum()==null || getJoinStudyNum()==0 ||  getCodeNum()==0){
			return "0%";
		}
		return	new DecimalFormat("##0.00").format( (double)getJoinStudyNum()/getCodeNum()*100)+"%";
	}

	public void setJoinStudyNumRate(String joinStudyNumRate) {
		this.joinStudyNumRate = joinStudyNumRate;
	}
	
	
	
}
