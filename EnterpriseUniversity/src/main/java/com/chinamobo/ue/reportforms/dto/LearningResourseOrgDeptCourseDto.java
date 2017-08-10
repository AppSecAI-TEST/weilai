package com.chinamobo.ue.reportforms.dto;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 描述 [学习资源-组织部门统计报表（课程） 改成 课程统计]
 * 创建者 LXT
 * 创建时间 2017年3月17日 上午9:24:57
 */
public class LearningResourseOrgDeptCourseDto {
	/*课程表  线上课程 */
	private Integer courseId;//资源id  -没用上
	private String courseNumber;//资源编号
	private String courseName;//资源名称
	private Integer ecurrency;//资源积分
	private String openCourse;//资源属性
	private String courseOnline;//资源类别1级
	private String sectionClassifyCode;//资源类别2级code
	private String sectionClassifyName;//资源类别2级name
	
	
	/*章节表*/
	private Integer sectionCount;//章节数量
	
	private Integer viewers;//资源访问人次
	private Integer viewer;//资源访问人数   目前没用上
	
	/*课程学习人员表 */
	private Integer courseTotalNumber;//资源开放人数   取课程下的所有人,公开课取全部在职员工数
	private Integer courseNoLearningNumber;//资源未学习人数     计算
	private Integer courseLearningNumber;//资源学习人数	  
	
	/*关联课程章节表（取章节id）- 关联章节学习记录表（根据章节id 和学习状态learning_states为Y 判断为学习完成人数）*/
	private Integer courseCompleteLearningNumber;//资源完成学习人数    
	
	private String coursePartRate;//资源参与率		资源学习人数 除以 资源开放人数
	private String courseAverageCompletionRate;//资源平均完成率    资源完成学习人数 除以 资源开放人数
	private Double courseTimes;//资源总学习总时长	
	/**/
	private String courseAverageLearningTimes;//资源平均学习时长	资源总学习总时长 除以 资源学习人数
	private Integer learningSectionCount;//资源学完章节数
	private String courseAverageLearningSchedule;//资源平均学习进度  资源中学员进度的平均数
	
	/* 线下课程*/
	private Integer courseSignUpNumber;//资源报名人数		课程学习人员表finish_status为Y
	private Integer courseDistributionNumber;//资源分配人数 	  取课程学习人员表下的所有人
	/*tom_course_classes 班次表*/
	private Integer courseClassesNumber;//班次数量	根据课程id 取其有几个班次
	/*课程签到记录表 */
	private Integer courseSignNumber;//签到数量
	private Integer courseSignPeopleNumber;//签到人数
	private String courseAverageSignRate;//平均签到率		签到人数 除以 资源报名人数
	private String courseAverageSignUpRate;//平均报名率		资源报名人数 除以 资源分配人数
	
	private Integer courseThumbUpCount;//下载数
	private Integer courseFavoriteNumber;//收藏数
	private Integer courseCommentCount;//评论数
	
	/*课程评论内容详细*/
	private String courseCommentContent;//评论内容
	private Date commentCreateTime;//评论时间
	private String empCode;//工号
	private String empName;//姓名
	private String empDeptname;//部门
	private String empPostname;//岗位
	private String empSecretEmail;//邮箱
	private String empMobile;//电话
	
	private Date thumbUpCreateTime;//点赞时间
	
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Integer getEcurrency() {
		return ecurrency;
	}
	public void setEcurrency(Integer ecurrency) {
		this.ecurrency = ecurrency;
	}
	public String getOpenCourse() {
		return openCourse;
	}
	public void setOpenCourse(String openCourse) {
		this.openCourse = openCourse;
	}
	public String getCourseOnline() {
		return courseOnline;
	}
	public void setCourseOnline(String courseOnline) {
		this.courseOnline = courseOnline;
	}

	public String getSectionClassifyCode() {
		return sectionClassifyCode;
	}
	public void setSectionClassifyCode(String sectionClassifyCode) {
		this.sectionClassifyCode = sectionClassifyCode;
	}
	public String getSectionClassifyName() {
		return sectionClassifyName;
	}
	public void setSectionClassifyName(String sectionClassifyName) {
		this.sectionClassifyName = sectionClassifyName;
	}
	public Integer getSectionCount() {
		return sectionCount;
	}
	public void setSectionCount(Integer sectionCount) {
		this.sectionCount = sectionCount;
	}
	public Integer getViewers() {
		return viewers;
	}
	public void setViewers(Integer viewers) {
		this.viewers = viewers;
	}
	public Integer getCourseTotalNumber() {
		return courseTotalNumber;
	}
	public void setCourseTotalNumber(Integer courseTotalNumber) {
		this.courseTotalNumber = courseTotalNumber;
	}
	public Integer getCourseNoLearningNumber() {
		if(getCourseTotalNumber()!=null && getCourseLearningNumber()!=null){
			return getCourseTotalNumber()-getCourseLearningNumber();
		}
		return courseNoLearningNumber;
	}
	public void setCourseNoLearningNumber(Integer courseNoLearningNumber) {
		this.courseNoLearningNumber = courseNoLearningNumber;
	}
	public Integer getCourseLearningNumber() {
		return courseLearningNumber;
	}
	public void setCourseLearningNumber(Integer courseLearningNumber) {
		this.courseLearningNumber = courseLearningNumber;
	}
	public Integer getCourseCompleteLearningNumber() {
		return courseCompleteLearningNumber;
	}
	public void setCourseCompleteLearningNumber(Integer courseCompleteLearningNumber) {
		this.courseCompleteLearningNumber = courseCompleteLearningNumber;
	}
	public String getCoursePartRate() {
		if(getCourseLearningNumber()==null || getCourseLearningNumber()==0 || getCourseTotalNumber()==null || getCourseTotalNumber()==0){
			return "0.00%";
		}
		return  new DecimalFormat("##0.00").format((double)getCourseLearningNumber()/getCourseTotalNumber()*100)+"%";
	}
	public void setCoursePartRate(String coursePartRate) {
		this.coursePartRate = coursePartRate;
	}
	public String getCourseAverageCompletionRate() {
		if(getCourseCompleteLearningNumber()==null || getCourseCompleteLearningNumber()==0 || getCourseTotalNumber()==null || getCourseTotalNumber()==0){
			return "0.00%";
		}
		return  new DecimalFormat("##0.00").format((double)getCourseCompleteLearningNumber()/getCourseTotalNumber()*100)+"%";
	}
	public void setCourseAverageCompletionRate(String courseAverageCompletionRate) {
		this.courseAverageCompletionRate = courseAverageCompletionRate;
	}
	public Double getCourseTimes() {
		if(courseTimes!=null && courseTimes!=0){
			return  Double.parseDouble(new DecimalFormat("0.00").format((double)courseTimes/60));
		}
		return courseTimes;
	}
	public void setCourseTimes(Double courseTimes) {
		this.courseTimes = courseTimes;
	}
	public Integer getCourseSignUpNumber() {
		return courseSignUpNumber;
	}
	public void setCourseSignUpNumber(Integer courseSignUpNumber) {
		this.courseSignUpNumber = courseSignUpNumber;
	}
	public Integer getCourseDistributionNumber() {
		return courseDistributionNumber;
	}
	public void setCourseDistributionNumber(Integer courseDistributionNumber) {
		this.courseDistributionNumber = courseDistributionNumber;
	}
	public Integer getCourseClassesNumber() {
		return courseClassesNumber;
	}
	public void setCourseClassesNumber(Integer courseClassesNumber) {
		this.courseClassesNumber = courseClassesNumber;
	}
	public Integer getCourseSignNumber() {
		return courseSignNumber;
	}
	public void setCourseSignNumber(Integer courseSignNumber) {
		this.courseSignNumber = courseSignNumber;
	}
	public String getCourseAverageSignRate() {
		if(getCourseSignPeopleNumber()==null || getCourseSignPeopleNumber()==0|| getCourseSignUpNumber()==null || getCourseSignUpNumber()==0){
			return "0.00%";
		}
		return new DecimalFormat("##0.00").format((double)getCourseSignPeopleNumber()/getCourseSignUpNumber()*100)+"%";
	}
	public void setCourseAverageSignRate(String courseAverageSignRate) {
		this.courseAverageSignRate = courseAverageSignRate;
	}
	public String getCourseAverageSignUpRate() {
		if(getCourseSignUpNumber()==null || getCourseSignUpNumber()==0 || getCourseDistributionNumber()==null || getCourseDistributionNumber()==0){
			return "0.00%";
		}
		return new DecimalFormat("##0.00").format((double)getCourseSignUpNumber()/getCourseDistributionNumber()*100)+"%";
	}
	public void setCourseAverageSignUpRate(String courseAverageSignUpRate) {
		this.courseAverageSignUpRate = courseAverageSignUpRate;
	}
	public Integer getViewer() {
		return viewer;
	}
	public void setViewer(Integer viewer) {
		this.viewer = viewer;
	}
	public String getCourseAverageLearningTimes() {
		if(getCourseTimes()==null || getCourseTimes()==0 || getCourseLearningNumber()==null || getCourseLearningNumber()==0){
			return "0";
		}
		return  new DecimalFormat("0.00").format((double)getCourseTimes()/getCourseLearningNumber());
	}
	public void setCourseAverageLearningTimes(String courseAverageLearningTimes) {
		this.courseAverageLearningTimes = courseAverageLearningTimes;
	}
	public String getCourseAverageLearningSchedule() {
		if(getLearningSectionCount()==null || getLearningSectionCount()==0 || getSectionCount()==null || getSectionCount()==0 || getCourseLearningNumber()==null || getCourseLearningNumber() ==0){
			return "0.00%";
		}
		return new DecimalFormat("##0.00").format((double)getLearningSectionCount()/(getSectionCount()*getCourseLearningNumber())*100)+"%";
	}

	public void setCourseAverageLearningSchedule(String courseAverageLearningSchedule) {
		this.courseAverageLearningSchedule = courseAverageLearningSchedule;
	}
	public Integer getCourseThumbUpCount() {
		return courseThumbUpCount;
	}
	public void setCourseThumbUpCount(Integer courseThumbUpCount) {
		this.courseThumbUpCount = courseThumbUpCount;
	}
	public Integer getCourseFavoriteNumber() {
		return courseFavoriteNumber;
	}
	public void setCourseFavoriteNumber(Integer courseFavoriteNumber) {
		this.courseFavoriteNumber = courseFavoriteNumber;
	}
	public Integer getCourseCommentCount() {
		return courseCommentCount;
	}
	public void setCourseCommentCount(Integer courseCommentCount) {
		this.courseCommentCount = courseCommentCount;
	}
	public String getCourseCommentContent() {
		return courseCommentContent;
	}
	public void setCourseCommentContent(String courseCommentContent) {
		this.courseCommentContent = courseCommentContent;
	}
	public Date getCommentCreateTime() {
		return commentCreateTime;
	}
	public void setCommentCreateTime(Date commentCreateTime) {
		this.commentCreateTime = commentCreateTime;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpDeptname() {
		return empDeptname;
	}
	public void setEmpDeptname(String empDeptname) {
		this.empDeptname = empDeptname;
	}
	public String getEmpPostname() {
		return empPostname;
	}
	public void setEmpPostname(String empPostname) {
		this.empPostname = empPostname;
	}
	public String getEmpSecretEmail() {
		return empSecretEmail;
	}
	public void setEmpSecretEmail(String empSecretEmail) {
		this.empSecretEmail = empSecretEmail;
	}
	public String getEmpMobile() {
		return empMobile;
	}
	public void setEmpMobile(String empMobile) {
		this.empMobile = empMobile;
	}
	public Integer getLearningSectionCount() {
		return learningSectionCount;
	}
	public void setLearningSectionCount(Integer learningSectionCount) {
		this.learningSectionCount = learningSectionCount;
	}
	public Integer getCourseSignPeopleNumber() {
		return courseSignPeopleNumber;
	}
	public void setCourseSignPeopleNumber(Integer courseSignPeopleNumber) {
		this.courseSignPeopleNumber = courseSignPeopleNumber;
	}
	public Date getThumbUpCreateTime() {
		return thumbUpCreateTime;
	}
	public void setThumbUpCreateTime(Date thumbUpCreateTime) {
		this.thumbUpCreateTime = thumbUpCreateTime;
	}
	
	
		
	
}
