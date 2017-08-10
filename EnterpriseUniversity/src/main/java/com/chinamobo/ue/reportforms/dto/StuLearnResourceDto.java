package com.chinamobo.ue.reportforms.dto;

public class StuLearnResourceDto {
	
	private String courseId;
	private String timestatep;
	private Long time;
	private String gradeState;
	private String status;
	private String openCourseStatus;
	private Integer sectionNum;
	private Integer passSectionNum;
	private Double numbers;
	private String userId;
	private String userName;
	private String orgId;
	private String orgName;
	private String onedept;
	private String twodept;
	private String threedept;
	private String deptcode;
	private String rolename;
	private String job;
	private String jobtier;
	private String empEmail;
	private String ebNumber;
	private Integer shouldeb;
	
	private String sumResnum;//总学习资源数
	private Integer onlineResnum;//线上学习资源数
	private Integer offlineResnum;//线下学习资源数
	private Integer onlineExamnum;//线上考试数
	private Double onlinelearnAlltime;//线上总学习时长
	private String avgOnfinishrate;//线上平均完成率
	private String avgOnschedule;//平均学习进度
	private String onExamfinishrate;//线上考试通过率
	private String offcourseApplyrate;//线下课程报名率
	private String offcourseSign;//线下课程签到率
	//必修活动内线上课程和考试
	private Integer coursenuma;//被分配课程
	private Integer startCoursenuma;//已开始课程数
	private Integer notAtCoursenuma;//未开始课程数
	private Integer finishCoursenuma;//已完成课程数
	private Double courseAlltimea;//课程总学习时长
	private String avgCoulearnTimea;//课程平均学习时长
	private String avgfinishratea;//课程平均完成率
	private String avetempoa;//课程学习进度
	private Integer needExamnuma;//需要考试数
	private Integer finExamnuma;//完成考试数
	private Integer unfinExamnuma;//未完成考试数
	private String exampassratea;//考试通过数
	private String avgExampassratea;//平均考试通过率
	//选修活动内线上课程和考试
	private Integer coursenumb;//被分配课程
	private Integer startCoursenumb;//已开始课程数
	private Integer notAtCoursenumb;//未开始课程数
	private Integer finishCoursenumb;//已完成课程数
	private Double courseAlltimeb;//课程总学习时长
	private String avgCoulearnTimeb;//课程平均学习时长
	private String avgfinishrateb;//课程平均完成率
	private String avetempob;//课程学习进度
	private Integer needExamnumb;//需要考试数
	private Integer finExamnumb;//完成考试数
	private Integer unfinExamnumb;//未完成考试数
	private String exampassrateb;//考试通过率
	private String avgExampassrateb;//平均考试通过率
	private Integer needEbb;
	private Integer getEbb;
	//非活动内线上课程和考试
	private Integer coursenumc;//被分配课程
	private Integer startCoursenumc;//已开始课程数
	private Integer notAtCoursenumc;//未开始课程数
	private Integer finishCoursenumc;//已完成课程数
	private Double courseAlltimec;//课程总学习时长
	private String avgCoulearnTimec;//课程平均学习时长
	private String avgfinishratec;//课程平均完成率
	private String avetempoc;//课程学习进度
	private Integer needExamnumc;//需要考试数
	private Integer finExamnumc;//完成考试数
	private Integer unfinExamnumc;//未完成考试数
	private String exampassratec;//考试通过率
	private String avgExampassratec;//平均考试通过率
	private Integer needEbc;
	private Integer getEbc;
	
	private String activeOnlineExamNum;
	private String activeOffcourseNum;
	private String unActiveOpencourse;
	
	//必修活动内线下课程
	private Integer coursenumd;//被分配课程
	private Integer openCoursenumd;//开放课程数
	private Integer applyCoursenumd;//报名课程数
	private Integer unappCoursenumd;//未报名课程数
	private Integer signCoursenumd;//已签到课程
	private Integer unsignCoursenumd;//未签到课程
	private String avgaApprated;//平均报名率
	private String avetSignrated;//平均签到率
	private Integer needEbd;
	private Integer getEbd;
	//选修修活动内线下课程
	private Integer coursenume;//被分配课程
	private Integer openCoursenume;//开放课程数
	private Integer applyCoursenume;//报名课程数
	private Integer unappCoursenume;//未报名课程数
	private Integer signCoursenume;//已签到课程
	private Integer unsignCoursenume;//未签到课程
	private String avgaAppratee;//平均报名率
	private String avetSignratee;//平均签到率
	private Integer needEbe;
	private Integer getEbe;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOnedept() {
		return onedept;
	}
	public void setOnedept(String onedept) {
		this.onedept = onedept;
	}
	public String getTwodept() {
		return twodept;
	}
	public void setTwodept(String twodept) {
		this.twodept = twodept;
	}
	public String getThreedept() {
		return threedept;
	}
	public void setThreedept(String threedept) {
		this.threedept = threedept;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getJobtier() {
		return jobtier;
	}
	public void setJobtier(String jobtier) {
		this.jobtier = jobtier;
	}
	public String getEmpEmail() {
		return empEmail;
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	public String getSumResnum() {
		return sumResnum;
	}
	public void setSumResnum(String sumResnum) {
		this.sumResnum = sumResnum;
	}
	public Integer getOnlineResnum() {
		return onlineResnum;
	}
	public void setOnlineResnum(Integer onlineResnum) {
		this.onlineResnum = onlineResnum;
	}
	public Integer getOfflineResnum() {
		return offlineResnum;
	}
	public void setOfflineResnum(Integer offlineResnum) {
		this.offlineResnum = offlineResnum;
	}
	public Integer getOnlineExamnum() {
		return onlineExamnum;
	}
	public void setOnlineExamnum(Integer onlineExamnum) {
		this.onlineExamnum = onlineExamnum;
	}
	public Double getOnlinelearnAlltime() {
		return onlinelearnAlltime;
	}
	public void setOnlinelearnAlltime(Double onlinelearnAlltime) {
		this.onlinelearnAlltime = onlinelearnAlltime;
	}
	public String getAvgOnfinishrate() {
		return avgOnfinishrate;
	}
	public void setAvgOnfinishrate(String avgOnfinishrate) {
		this.avgOnfinishrate = avgOnfinishrate;
	}
	public String getAvgOnschedule() {
		return avgOnschedule;
	}
	public void setAvgOnschedule(String avgOnschedule) {
		this.avgOnschedule = avgOnschedule;
	}
	public String getOnExamfinishrate() {
		return onExamfinishrate;
	}
	public void setOnExamfinishrate(String onExamfinishrate) {
		this.onExamfinishrate = onExamfinishrate;
	}
	public String getOffcourseApplyrate() {
		return offcourseApplyrate;
	}
	public void setOffcourseApplyrate(String offcourseApplyrate) {
		this.offcourseApplyrate = offcourseApplyrate;
	}
	public String getOffcourseSign() {
		return offcourseSign;
	}
	public void setOffcourseSign(String offcourseSign) {
		this.offcourseSign = offcourseSign;
	}
	public Integer getCoursenuma() {
		return coursenuma;
	}
	public void setCoursenuma(Integer coursenuma) {
		this.coursenuma = coursenuma;
	}
	public Integer getStartCoursenuma() {
		return startCoursenuma;
	}
	public void setStartCoursenuma(Integer startCoursenuma) {
		this.startCoursenuma = startCoursenuma;
	}
	public Integer getNotAtCoursenuma() {
		return notAtCoursenuma;
	}
	public void setNotAtCoursenuma(Integer notAtCoursenuma) {
		this.notAtCoursenuma = notAtCoursenuma;
	}
	public Integer getFinishCoursenuma() {
		return finishCoursenuma;
	}
	public void setFinishCoursenuma(Integer finishCoursenuma) {
		this.finishCoursenuma = finishCoursenuma;
	}
	public Double getCourseAlltimea() {
		return courseAlltimea;
	}
	public void setCourseAlltimea(Double courseAlltimea) {
		this.courseAlltimea = courseAlltimea;
	}
	public String getAvgCoulearnTimea() {
		return avgCoulearnTimea;
	}
	public void setAvgCoulearnTimea(String avgCoulearnTimea) {
		this.avgCoulearnTimea = avgCoulearnTimea;
	}
	public String getAvgfinishratea() {
		return avgfinishratea;
	}
	public void setAvgfinishratea(String avgfinishratea) {
		this.avgfinishratea = avgfinishratea;
	}
	public String getAvetempoa() {
		return avetempoa;
	}
	public void setAvetempoa(String avetempoa) {
		this.avetempoa = avetempoa;
	}
	public Integer getNeedExamnuma() {
		return needExamnuma;
	}
	public void setNeedExamnuma(Integer needExamnuma) {
		this.needExamnuma = needExamnuma;
	}
	public Integer getFinExamnuma() {
		return finExamnuma;
	}
	public void setFinExamnuma(Integer finExamnuma) {
		this.finExamnuma = finExamnuma;
	}
	public Integer getUnfinExamnuma() {
		return unfinExamnuma;
	}
	public void setUnfinExamnuma(Integer unfinExamnuma) {
		this.unfinExamnuma = unfinExamnuma;
	}
	public String getExampassratea() {
		return exampassratea;
	}
	public void setExampassratea(String exampassratea) {
		this.exampassratea = exampassratea;
	}
	public String getAvgExampassratea() {
		return avgExampassratea;
	}
	public void setAvgExampassratea(String avgExampassratea) {
		this.avgExampassratea = avgExampassratea;
	}
	public Integer getCoursenumb() {
		return coursenumb;
	}
	public void setCoursenumb(Integer coursenumb) {
		this.coursenumb = coursenumb;
	}
	public Integer getStartCoursenumb() {
		return startCoursenumb;
	}
	public void setStartCoursenumb(Integer startCoursenumb) {
		this.startCoursenumb = startCoursenumb;
	}
	public Integer getNotAtCoursenumb() {
		return notAtCoursenumb;
	}
	public void setNotAtCoursenumb(Integer notAtCoursenumb) {
		this.notAtCoursenumb = notAtCoursenumb;
	}
	public Integer getFinishCoursenumb() {
		return finishCoursenumb;
	}
	public void setFinishCoursenumb(Integer finishCoursenumb) {
		this.finishCoursenumb = finishCoursenumb;
	}
	public Double getCourseAlltimeb() {
		return courseAlltimeb;
	}
	public void setCourseAlltimeb(Double courseAlltimeb) {
		this.courseAlltimeb = courseAlltimeb;
	}
	public String getAvgCoulearnTimeb() {
		return avgCoulearnTimeb;
	}
	public void setAvgCoulearnTimeb(String avgCoulearnTimeb) {
		this.avgCoulearnTimeb = avgCoulearnTimeb;
	}
	public String getAvgfinishrateb() {
		return avgfinishrateb;
	}
	public void setAvgfinishrateb(String avgfinishrateb) {
		this.avgfinishrateb = avgfinishrateb;
	}
	public String getAvetempob() {
		return avetempob;
	}
	public void setAvetempob(String avetempob) {
		this.avetempob = avetempob;
	}
	public Integer getNeedExamnumb() {
		return needExamnumb;
	}
	public void setNeedExamnumb(Integer needExamnumb) {
		this.needExamnumb = needExamnumb;
	}
	public Integer getFinExamnumb() {
		return finExamnumb;
	}
	public void setFinExamnumb(Integer finExamnumb) {
		this.finExamnumb = finExamnumb;
	}
	public Integer getUnfinExamnumb() {
		return unfinExamnumb;
	}
	public void setUnfinExamnumb(Integer unfinExamnumb) {
		this.unfinExamnumb = unfinExamnumb;
	}
	public String getExampassrateb() {
		return exampassrateb;
	}
	public void setExampassrateb(String exampassrateb) {
		this.exampassrateb = exampassrateb;
	}
	public String getAvgExampassrateb() {
		return avgExampassrateb;
	}
	public void setAvgExampassrateb(String avgExampassrateb) {
		this.avgExampassrateb = avgExampassrateb;
	}
	public Integer getNeedEbb() {
		return needEbb;
	}
	public void setNeedEbb(Integer needEbb) {
		this.needEbb = needEbb;
	}
	public Integer getGetEbb() {
		return getEbb;
	}
	public void setGetEbb(Integer getEbb) {
		this.getEbb = getEbb;
	}
	
	public Integer getStartCoursenumc() {
		return startCoursenumc;
	}
	public void setStartCoursenumc(Integer startCoursenumc) {
		this.startCoursenumc = startCoursenumc;
	}
	public Integer getNotAtCoursenumc() {
		return notAtCoursenumc;
	}
	public void setNotAtCoursenumc(Integer notAtCoursenumc) {
		this.notAtCoursenumc = notAtCoursenumc;
	}
	public Integer getFinishCoursenumc() {
		return finishCoursenumc;
	}
	public void setFinishCoursenumc(Integer finishCoursenumc) {
		this.finishCoursenumc = finishCoursenumc;
	}
	public Double getCourseAlltimec() {
		return courseAlltimec;
	}
	public void setCourseAlltimec(Double courseAlltimec) {
		this.courseAlltimec = courseAlltimec;
	}
	public String getAvgCoulearnTimec() {
		return avgCoulearnTimec;
	}
	public void setAvgCoulearnTimec(String avgCoulearnTimec) {
		this.avgCoulearnTimec = avgCoulearnTimec;
	}
	public String getAvgfinishratec() {
		return avgfinishratec;
	}
	public void setAvgfinishratec(String avgfinishratec) {
		this.avgfinishratec = avgfinishratec;
	}
	public String getAvetempoc() {
		return avetempoc;
	}
	public void setAvetempoc(String avetempoc) {
		this.avetempoc = avetempoc;
	}
	public Integer getNeedExamnumc() {
		return needExamnumc;
	}
	public void setNeedExamnumc(Integer needExamnumc) {
		this.needExamnumc = needExamnumc;
	}
	public Integer getFinExamnumc() {
		return finExamnumc;
	}
	public void setFinExamnumc(Integer finExamnumc) {
		this.finExamnumc = finExamnumc;
	}
	public Integer getUnfinExamnumc() {
		return unfinExamnumc;
	}
	public void setUnfinExamnumc(Integer unfinExamnumc) {
		this.unfinExamnumc = unfinExamnumc;
	}
	public String getExampassratec() {
		return exampassratec;
	}
	public void setExampassratec(String exampassratec) {
		this.exampassratec = exampassratec;
	}
	public String getAvgExampassratec() {
		return avgExampassratec;
	}
	public void setAvgExampassratec(String avgExampassratec) {
		this.avgExampassratec = avgExampassratec;
	}
	public Integer getNeedEbc() {
		return needEbc;
	}
	public void setNeedEbc(Integer needEbc) {
		this.needEbc = needEbc;
	}
	public Integer getGetEbc() {
		return getEbc;
	}
	public void setGetEbc(Integer getEbc) {
		this.getEbc = getEbc;
	}
	
	public Integer getOpenCoursenumd() {
		return openCoursenumd;
	}
	public void setOpenCoursenumd(Integer openCoursenumd) {
		this.openCoursenumd = openCoursenumd;
	}
	public Integer getApplyCoursenumd() {
		return applyCoursenumd;
	}
	public void setApplyCoursenumd(Integer applyCoursenumd) {
		this.applyCoursenumd = applyCoursenumd;
	}
	public Integer getUnappCoursenumd() {
		return unappCoursenumd;
	}
	public void setUnappCoursenumd(Integer unappCoursenumd) {
		this.unappCoursenumd = unappCoursenumd;
	}
	public Integer getSignCoursenumd() {
		return signCoursenumd;
	}
	public void setSignCoursenumd(Integer signCoursenumd) {
		this.signCoursenumd = signCoursenumd;
	}
	public Integer getUnsignCoursenumd() {
		return unsignCoursenumd;
	}
	public void setUnsignCoursenumd(Integer unsignCoursenumd) {
		this.unsignCoursenumd = unsignCoursenumd;
	}
	public String getAvgaApprated() {
		return avgaApprated;
	}
	public void setAvgaApprated(String avgaApprated) {
		this.avgaApprated = avgaApprated;
	}
	public String getAvetSignrated() {
		return avetSignrated;
	}
	public void setAvetSignrated(String avetSignrated) {
		this.avetSignrated = avetSignrated;
	}
	public Integer getNeedEbd() {
		return needEbd;
	}
	public void setNeedEbd(Integer needEbd) {
		this.needEbd = needEbd;
	}
	public Integer getGetEbd() {
		return getEbd;
	}
	public void setGetEbd(Integer getEbd) {
		this.getEbd = getEbd;
	}
	
	public Integer getCoursenumc() {
		return coursenumc;
	}
	public void setCoursenumc(Integer coursenumc) {
		this.coursenumc = coursenumc;
	}
	public Integer getCoursenumd() {
		return coursenumd;
	}
	public void setCoursenumd(Integer coursenumd) {
		this.coursenumd = coursenumd;
	}
	public Integer getCoursenume() {
		return coursenume;
	}
	public void setCoursenume(Integer coursenume) {
		this.coursenume = coursenume;
	}
	public Integer getOpenCoursenume() {
		return openCoursenume;
	}
	public void setOpenCoursenume(Integer openCoursenume) {
		this.openCoursenume = openCoursenume;
	}
	public Integer getApplyCoursenume() {
		return applyCoursenume;
	}
	public void setApplyCoursenume(Integer applyCoursenume) {
		this.applyCoursenume = applyCoursenume;
	}
	public Integer getUnappCoursenume() {
		return unappCoursenume;
	}
	public void setUnappCoursenume(Integer unappCoursenume) {
		this.unappCoursenume = unappCoursenume;
	}
	public Integer getSignCoursenume() {
		return signCoursenume;
	}
	public void setSignCoursenume(Integer signCoursenume) {
		this.signCoursenume = signCoursenume;
	}
	public Integer getUnsignCoursenume() {
		return unsignCoursenume;
	}
	public void setUnsignCoursenume(Integer unsignCoursenume) {
		this.unsignCoursenume = unsignCoursenume;
	}
	public String getAvgaAppratee() {
		return avgaAppratee;
	}
	public void setAvgaAppratee(String avgaAppratee) {
		this.avgaAppratee = avgaAppratee;
	}
	public String getAvetSignratee() {
		return avetSignratee;
	}
	public void setAvetSignratee(String avetSignratee) {
		this.avetSignratee = avetSignratee;
	}
	public Integer getNeedEbe() {
		return needEbe;
	}
	public void setNeedEbe(Integer needEbe) {
		this.needEbe = needEbe;
	}
	public Integer getGetEbe() {
		return getEbe;
	}
	public void setGetEbe(Integer getEbe) {
		this.getEbe = getEbe;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getTimestatep() {
		return timestatep;
	}
	public void setTimestatep(String timestatep) {
		this.timestatep = timestatep;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getGradeState() {
		return gradeState;
	}
	public void setGradeState(String gradeState) {
		this.gradeState = gradeState;
	}
	public String getEbNumber() {
		return ebNumber;
	}
	public void setEbNumber(String ebNumber) {
		this.ebNumber = ebNumber;
	}
	public Integer getShouldeb() {
		return shouldeb;
	}
	public void setShouldeb(Integer shouldeb) {
		this.shouldeb = shouldeb;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOpenCourseStatus() {
		return openCourseStatus;
	}
	public void setOpenCourseStatus(String openCourseStatus) {
		this.openCourseStatus = openCourseStatus;
	}
	public String getActiveOnlineExamNum() {
		return activeOnlineExamNum;
	}
	public void setActiveOnlineExamNum(String activeOnlineExamNum) {
		this.activeOnlineExamNum = activeOnlineExamNum;
	}
	public String getActiveOffcourseNum() {
		return activeOffcourseNum;
	}
	public void setActiveOffcourseNum(String activeOffcourseNum) {
		this.activeOffcourseNum = activeOffcourseNum;
	}
	public Integer getSectionNum() {
		return sectionNum;
	}
	public void setSectionNum(Integer sectionNum) {
		this.sectionNum = sectionNum;
	}
	public Integer getPassSectionNum() {
		return passSectionNum;
	}
	public void setPassSectionNum(Integer passSectionNum) {
		this.passSectionNum = passSectionNum;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getUnActiveOpencourse() {
		return unActiveOpencourse;
	}
	public void setUnActiveOpencourse(String unActiveOpencourse) {
		this.unActiveOpencourse = unActiveOpencourse;
	}
	public Double getNumbers() {
		return numbers;
	}
	public void setNumbers(Double numbers) {
		this.numbers = numbers;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	
	
}
