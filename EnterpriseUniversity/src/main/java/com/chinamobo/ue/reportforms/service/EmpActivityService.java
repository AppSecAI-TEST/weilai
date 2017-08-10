package com.chinamobo.ue.reportforms.service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomOfflineSignMapper;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.reportforms.dao.EmpActivityMapper;
import com.chinamobo.ue.reportforms.dto.EmpActivityDto;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.ExcelUtil;

@Service
public class EmpActivityService {
	@Autowired
	private EmpActivityMapper empActivityMapper;
	@Autowired
	private TomCourseLearningRecordMapper tomCourseLearningRecordMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomCoursesMapper tomCoursesMapper;
	@Autowired
	private TomOfflineSignMapper tomOfflineSignMapper;
	@Autowired
	private LearningDeptExamService learningDeptExamService;
	String filePath=Config.getProperty("file_path");
	
	public PageData<EmpActivityDto> empActivityList(int pageNum, int pageSize, String startTime,
			String endTime, Integer projectId, String isRequired, String code, String name, String orgcode, String onedeptcode,
			String deptcode2, String deptcode3 ,String queryColumnStr) throws ParseException{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpActivityDto> page = new PageData<EmpActivityDto>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("code", code);
		map.put("name", name);
		map.put("orgcode", orgcode);
		map.put("onedeptcode", onedeptcode);
		map.put("deptcode2", deptcode2);
		map.put("deptcode3", deptcode3);
		int count = empActivityMapper.empActivityCount(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<EmpActivityDto> list = empActivityMapper.empActivityList(map);
		for(EmpActivityDto empActivityDto : list){
			//换算总学习时长/计算平均学习时长
			if(empActivityDto.getTotalLengthOfStudy()!=0&&empActivityDto.getTotalNumberOfLandings()!=0){
				empActivityDto.setTotalLengthOfStudy(Math.round((double)empActivityDto.getTotalLengthOfStudy()/60*100)/100.0);
				empActivityDto.setAverageLengthOfLearning(Math.round((double)empActivityDto.getTotalLengthOfStudy()/empActivityDto.getTotalNumberOfLandings()*100)/100.0);
			}else{
				empActivityDto.setAverageLengthOfLearning(0.00);
			}
			
			//设置默认值
			empActivityDto.setAverageLearningProgress("0.00%");
			empActivityDto.setActivityParticipationRate("0.00%");
			empActivityDto.setActivityCompletionRate("0.00%");
			empActivityDto.setNumberOfOnlineCoursesCompleted(0);
			empActivityDto.setNumberOfCoursesCompletedInTheEvent(0);
			empActivityDto.setNumberOfOnlineExamsCompleted(0);
			empActivityDto.setAveragePassRate("0.00%");
			
			empActivityDto.setRequiredActivity(0);
			empActivityDto.setNumberOfRequiredActivitiesToStartLearning(0);
			empActivityDto.setRequiredActivitySchedule("0.00%");
			empActivityDto.setNumberOfRequiredActivitiesCompleted(0);
			empActivityDto.setRequiredParticipationRate("0.00%");
			empActivityDto.setRequiredCompletionRate("0.00%");
			empActivityDto.setCompulsoryExaminationQuantity(0);
			empActivityDto.setCompulsoryExaminationAverageScore(0.00);
			empActivityDto.setCompulsoryExaminationAverage(0.00);
			empActivityDto.setRequiredExaminationPassRate("0.00%");
			empActivityDto.setRequiredActivityPoints(0);
			empActivityDto.setRequiredActivitiesToGetPoints(0);
			
			empActivityDto.setNumberOfOpenElectiveActivities(0);
			empActivityDto.setNumberOfEnrollment(0);
			empActivityDto.setAverageEnrollmentRate("0.00%");
			empActivityDto.setNumberOfElectivesThatHaveBegunToLearn(0);
			empActivityDto.setAverageActivitySchedule("0.00%");
			empActivityDto.setNumberOfElectiveActivitiesCompleted(0);
			empActivityDto.setParticipationRate("0.00%");
			empActivityDto.setAverageCompletionRate("0.00%");
			empActivityDto.setNumberOfElectives(0);
			empActivityDto.setAverageScoreOfElectiveActivities(0D);
			empActivityDto.setElectiveExaminationAverage(0.00);
			empActivityDto.setElectiveExaminationPassRate("0.00%");
			empActivityDto.setOptionalActivityPoints(0);
			empActivityDto.setElectiveActivitiesGetPoints(0);
			
			empActivityDto.setRequiredExaminationNumber(0);
			empActivityDto.setTheNumberOfExamsForElectiveActivities(0);
			empActivityDto.setExamPassNum(0);
			Map<Object,Object> map1=new HashMap<Object,Object>();
			map1.put("code", empActivityDto.getCode());
			map1.put("startTime", startTime);
			map1.put("endTime", endTime);
			map1.put("projectId", projectId);
			map1.put("isRequired", isRequired);
			//根据条件查询总活动数
			int totalActivity = empActivityMapper.totalActivityCount(map1);
			empActivityDto.setTotalActivity(totalActivity);
			//判断总活动数
			if(totalActivity>0){
				//根据条件查询活动内完成线上课程数
				int numberOfOnlineCoursesCompleted = empActivityMapper.numberOfOnlineCoursesCompleted(map1);
				empActivityDto.setNumberOfOnlineCoursesCompleted(numberOfOnlineCoursesCompleted);
				//根据条件查询活动内完成线下课程数
				int numberOfCoursesCompletedInTheEvent = empActivityMapper.numberOfCoursesCompletedInTheEvent(map1);
				empActivityDto.setNumberOfCoursesCompletedInTheEvent(numberOfCoursesCompletedInTheEvent);
				//根据条件查询活动内完成线上考试数
				int numberOfOnlineExamsCompleted = empActivityMapper.numberOfOnlineExamsCompleted(map1);
				empActivityDto.setNumberOfOnlineExamsCompleted(numberOfOnlineExamsCompleted);
				//根据条件查询必修活动数/开放选修活动数
				List<EmpActivityDto> requiredElectiveCount = empActivityMapper.requiredElectiveCount(map1);
				for(EmpActivityDto reCount : requiredElectiveCount){
					if(reCount.getIsRequired().equals("Y")){
						//必修活动数
						empActivityDto.setRequiredActivity(reCount.getCurrencyCount());
					}else{
						//开放选修活动数
						empActivityDto.setNumberOfOpenElectiveActivities(reCount.getCurrencyCount());
					}
				}
				//根据条件查询已开始学习的必修活动数
				int numberOfRequiredActivitiesToStartLearning = empActivityMapper.numberOfRequiredActivitiesToStartLearning(map1);
				empActivityDto.setNumberOfRequiredActivitiesToStartLearning(numberOfRequiredActivitiesToStartLearning);
				//根据条件查询已开始学习的选修活动数量
				int numberOfElectivesThatHaveBegunToLearn = empActivityMapper.numberOfElectivesThatHaveBegunToLearn(map1);
				empActivityDto.setNumberOfElectivesThatHaveBegunToLearn(numberOfElectivesThatHaveBegunToLearn);
				//根据统计区间查询报名选修活动数
				int numberOfEnrollment = empActivityMapper.numberOfEnrollment(map1);
				empActivityDto.setNumberOfEnrollment(numberOfEnrollment);
				//选修活动平均报名率
				if(numberOfEnrollment>0){
					DecimalFormat df = new DecimalFormat("######0.00");
					empActivityDto.setAverageEnrollmentRate(df.format((double)numberOfEnrollment/empActivityDto.getNumberOfOpenElectiveActivities()*100)+"%");
				}
				//根据条件查询必修活动考试数量/选修活动考试数量
				List<EmpActivityDto> requiredElectiveExamCount = empActivityMapper.requiredElectiveExamCount(map1);
				for(EmpActivityDto reExamCount : requiredElectiveExamCount){
					if(reExamCount.getIsRequired().equals("Y")){
						//必修活动考试数量
						empActivityDto.setCompulsoryExaminationQuantity(reExamCount.getCurrencyCount());
					}else{
						//选修活动考试数量
						empActivityDto.setNumberOfElectives(reExamCount.getCurrencyCount());
					}
				}
				//统计通过考试数量
				empActivityDto.setExamPassNum(empActivityMapper.examPassNum(map1));
				//根据条件查询必修活动考试总分/选修活动考试总分
//				List<EmpActivityDto> requiredElectiveExamScore = empActivityMapper.requiredElectiveExamScore(map1);
//				for(EmpActivityDto reExamScore : requiredElectiveExamScore){
//					if(reExamScore.getIsRequired().equals("Y")){
//						if(reExamScore.getCurrencyCount()!=0||empActivityDto.getCompulsoryExaminationQuantity()!=0){
//							//必修活动考试平均成绩
//							empActivityDto.setCompulsoryExaminationAverageScore(Math.round((double)reExamScore.getCurrencyCount()/empActivityDto.getCompulsoryExaminationQuantity()*100)/100.0);
//							//必修活动考试平均分
//							empActivityDto.setCompulsoryExaminationAverage(Math.round((double)reExamScore.getCurrencyCount()/empActivityDto.getCompulsoryExaminationQuantity()*100)/100.0);
//						}
//					}else{
//						if(reExamScore.getCurrencyCount()!=0||empActivityDto.getNumberOfElectives()!=0){
//							//选修活动考试平均成绩
//							empActivityDto.setAverageScoreOfElectiveActivities(Math.round((double)reExamScore.getCurrencyCount()/empActivityDto.getNumberOfElectives()*100)/100.0);
//							//选修活动考试平均分
//							empActivityDto.setElectiveExaminationAverage(Math.round((double)reExamScore.getCurrencyCount()/empActivityDto.getNumberOfElectives()*100)/100.0);
//						}
//					}
//				}
				//根据条件查询必修活动考试及格数/选修活动考试及格数
				List<EmpActivityDto> requiredElectiveExamPass = empActivityMapper.requiredElectiveExamPass(map1);
				for(EmpActivityDto reExamPass : requiredElectiveExamPass){
					if(reExamPass.getIsRequired().equals("Y")){
						if(reExamPass.getCurrencyCount()!=0||empActivityDto.getCompulsoryExaminationQuantity()!=0){
							empActivityDto.setRequiredExaminationNumber(reExamPass.getCurrencyCount());
							DecimalFormat df = new DecimalFormat("######0.00");
							//必修活动考试及格率
							empActivityDto.setRequiredExaminationPassRate(df.format((double)reExamPass.getCurrencyCount()/empActivityDto.getCompulsoryExaminationQuantity()*100)+"%");
						}
					}else{
						if(reExamPass.getCurrencyCount()!=0||empActivityDto.getNumberOfElectives()!=0){
							empActivityDto.setTheNumberOfExamsForElectiveActivities(reExamPass.getCurrencyCount());
							DecimalFormat df = new DecimalFormat("######0.00");
							//选修活动考试及格率
							empActivityDto.setElectiveExaminationPassRate(df.format((double)reExamPass.getCurrencyCount()/empActivityDto.getNumberOfElectives()*100)+"%");
						}
					}
				}
			}
			//总考试数量
			int examAll = empActivityDto.getCompulsoryExaminationQuantity()+empActivityDto.getNumberOfElectives();
			//总考试及格数量
			int jgExamAll = empActivityDto.getRequiredExaminationNumber()+empActivityDto.getTheNumberOfExamsForElectiveActivities();
			//平均及格率
			if(examAll>0&&jgExamAll>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setAveragePassRate(df.format((double)jgExamAll/examAll*100)+"%");
			}
			
			int rCourseIntegral = 0;//必修活动课程积分
			rCourseIntegral = empActivityMapper.rCourseIntegral(map1);
			int eCourseIntegral = 0;//选修活动课程积分
			eCourseIntegral = empActivityMapper.eCourseIntegral(map1);
			//根据条件查询必修活动课程积分/选修活动课程积分
//			List<EmpActivityDto> requiredElectiveCourseIntegral = empActivityMapper.requiredElectiveCourseIntegral(map1);
//			for(EmpActivityDto reCourseIntegral : requiredElectiveCourseIntegral){
//				if(reCourseIntegral.getIsRequired().equals("Y")){
//					rCourseIntegral = reCourseIntegral.getCurrencyCount();
//				}else{
//					eCourseIntegral = reCourseIntegral.getCurrencyCount();
//				}
//			}
			int rExamIntegral = 0;//必修活动考试积分
			int eExamIntegral = 0;//选修活动考试积分
			//根据条件查询必修活动考试积分/选修活动考试积分
			List<EmpActivityDto> requiredElectiveExamIntegral = empActivityMapper.requiredElectiveExamIntegral(map1);
			for(EmpActivityDto reExamIntegral : requiredElectiveExamIntegral){
				if(reExamIntegral.getIsRequired().equals("Y")){
					rExamIntegral = reExamIntegral.getCurrencyCount();
				}else{
					eExamIntegral = reExamIntegral.getCurrencyCount();
				}
			}
			//必修活动积分
			empActivityDto.setRequiredActivityPoints(rCourseIntegral+rExamIntegral);
			//选修活动积分
			empActivityDto.setOptionalActivityPoints(eCourseIntegral+eExamIntegral);
			
			int rsGetCourseIntegral = 0;//必修活动线上课程获得积分
			rsGetCourseIntegral = empActivityMapper.rsGetCourseIntegral(map1);
			int esGetCourseIntegral = 0;//选修活动线上课程获得积分
			esGetCourseIntegral = empActivityMapper.esGetCourseIntegral(map1);
			//根据条件查询必修活动获得线上课程积分/选修活动获得线上课程积分
//			List<EmpActivityDto> resquiredElectiveGetCourseIntegral = empActivityMapper.resquiredElectiveGetCourseIntegral(map1);
//			for(EmpActivityDto resGetCourseIntegral : resquiredElectiveGetCourseIntegral){
//				if(resGetCourseIntegral.getIsRequired().equals("Y")){
//					rsGetCourseIntegral = resGetCourseIntegral.getCurrencyCount();
//				}else{
//					esGetCourseIntegral = resGetCourseIntegral.getCurrencyCount();
//				}
//			}
			int rxGetCourseIntegral = 0;//必修活动获得线下课程积分
			rxGetCourseIntegral = empActivityMapper.rxGetCourseIntegral(map1);
			int exGetCourseIntegral = 0;//选修活动获得线下课程积分
			exGetCourseIntegral = empActivityMapper.exGetCourseIntegral(map1);
			//根据条件查询必修活动获得线下课程积分/选修活动获得线下课程积分
//			List<EmpActivityDto> rexquiredElectiveGetCourseIntegral = empActivityMapper.rexquiredElectiveGetCourseIntegral(map1);
//			for(EmpActivityDto rexGetCourseIntegral : rexquiredElectiveGetCourseIntegral){
//				if(rexGetCourseIntegral.getIsRequired().equals("Y")){
//					rxGetCourseIntegral = rexGetCourseIntegral.getCurrencyCount();
//				}else{
//					exGetCourseIntegral = rexGetCourseIntegral.getCurrencyCount();
//				}
//			}
			
			int rGetExamIntegral = 0;//必修活动完成考试获得积分
			int eGetExanIntegral = 0;//选修活动完成考试获得积分
			//根据条件查询必修活动完成考试获得积分/选修活动完成考试获得积分
			List<EmpActivityDto> requiredElectiveGetExamIntegral = empActivityMapper.requiredElectiveGetExamIntegral(map1);
			for(EmpActivityDto reGetExamIntegral : requiredElectiveGetExamIntegral){
				if(reGetExamIntegral.getIsRequired().equals("Y")){
					rGetExamIntegral = reGetExamIntegral.getCurrencyCount();
				}else{
					eGetExanIntegral = reGetExamIntegral.getCurrencyCount();
				}
			}
			int rDeductionExamIntegral = 0;//必修活动未完成考试扣除积分
			int eDeductionExanIntegral = 0;//选修活动未完成考试扣除积分
			//根据条件查询必修活动未完成考试扣除积分/选修活动未完成考试扣除积分
			List<EmpActivityDto> requiredElectiveDeductionExamIntegral = empActivityMapper.requiredElectiveDeductionExamIntegral(map1);
			for(EmpActivityDto reDeductionExamIntegral : requiredElectiveDeductionExamIntegral){
				if(reDeductionExamIntegral.getIsRequired().equals("Y")){
					rDeductionExamIntegral = reDeductionExamIntegral.getCurrencyCount();
				}else{
					eDeductionExanIntegral = reDeductionExamIntegral.getCurrencyCount();
				}
			}
			
			//必修活动获得积分
			empActivityDto.setRequiredActivitiesToGetPoints(rsGetCourseIntegral+rxGetCourseIntegral+(rGetExamIntegral-rDeductionExamIntegral));
			//选修活动获得积分
			empActivityDto.setElectiveActivitiesGetPoints(esGetCourseIntegral+exGetCourseIntegral+(eGetExanIntegral-eDeductionExanIntegral));
			
			int wwcBxActivityCount = 0;//未完成必修活动
			int wwcXxActivityCount = 0;//未完成选修活动
			int cyBxActivityCount = 0;//参与必修活动数
			int cyXxActivityCount = 0;//参与选修活动数
			//根据条件查询所有活动ID,忽略选修未报名
			List<EmpActivityDto> activityIdList = empActivityMapper.activityIdList(map1);
			for(EmpActivityDto activityId : activityIdList){
				//根据活动ID查询课程/考试ID
				List<EmpActivityDto> courseExamIdList = empActivityMapper.requiredElectiveCourseExamId(activityId.getActivityId());
				for(EmpActivityDto courseExamId : courseExamIdList){
					if(courseExamId.getExamId()!=null){
						Map<Object,Object> mapExam=new HashMap<Object,Object>();
						mapExam.put("code", empActivityDto.getCode());
						mapExam.put("examId", courseExamId.getExamId());
						int examCount = tomExamScoreMapper.selectByExamCode(mapExam);
						if(examCount<1){
							if(courseExamId.getIsRequired().equals("Y")){
								wwcBxActivityCount+=1;
								break;
							}else{
								wwcXxActivityCount+=1;
								break;
							}
						}
					}else{
						//查询课程信息
						TomCourses tomCourses = tomCoursesMapper.selectByPrimaryKey(courseExamId.getCourseId());
						Map<Object,Object> mapCourse=new HashMap<Object,Object>();
						mapCourse.put("code", empActivityDto.getCode());
						mapCourse.put("courseId", courseExamId.getCourseId());
						if(tomCourses.getCourseOnline().equals("Y")){
							int courseCount = tomCourseLearningRecordMapper.selectByCousesCode(mapCourse);
							if(courseCount<1){
								if(courseExamId.getIsRequired().equals("Y")){
									wwcBxActivityCount+=1;
									break;
								}else{
									wwcXxActivityCount+=1;
									break;
								}
							}
						}else{
							int courseCount = tomOfflineSignMapper.signCount(mapCourse);
							if(courseCount<1){
								if(courseExamId.getIsRequired().equals("Y")){
									wwcBxActivityCount+=1;
									break;
								}else{
									wwcXxActivityCount+=1;
									break;
								}
							}
						}
					}
				}
				
				for(EmpActivityDto courseExamId : courseExamIdList){
					if(courseExamId.getExamId()!=null){
						Map<Object,Object> mapExam=new HashMap<Object,Object>();
						mapExam.put("code", empActivityDto.getCode());
						mapExam.put("examId", courseExamId.getExamId());
						int examCount = tomExamScoreMapper.selectByExamCode(mapExam);
						if(examCount>0){
							if(courseExamId.getIsRequired().equals("Y")){
								cyBxActivityCount+=1;
								break;
							}else{
								cyXxActivityCount+=1;
								break;
							}
						}
					}else{
						//查询课程信息
						TomCourses tomCourses = tomCoursesMapper.selectByPrimaryKey(courseExamId.getCourseId());
						Map<Object,Object> mapCourse=new HashMap<Object,Object>();
						mapCourse.put("code", empActivityDto.getCode());
						mapCourse.put("courseId", courseExamId.getCourseId());
						if(tomCourses.getCourseOnline().equals("Y")){
							int courseCount = tomCourseLearningRecordMapper.selectByCousesCode(mapCourse);
							if(courseCount>0){
								if(courseExamId.getIsRequired().equals("Y")){
									cyBxActivityCount+=1;
									break;
								}else{
									cyXxActivityCount+=1;
									break;
								}
							}
						}else{
							int courseCount = tomOfflineSignMapper.signCount(mapCourse);
							if(courseCount>0){
								if(courseExamId.getIsRequired().equals("Y")){
									cyBxActivityCount+=1;
									break;
								}else{
									cyXxActivityCount+=1;
									break;
								}
							}
						}
					}
				}
			}
			
			int ksRequired = 0;//开始必修活动
			int ksElective = 0;//开始选修活动
			//根据条件查询已开始的必修活动/选修活动
			List<EmpActivityDto> ksRequiredElective = empActivityMapper.ksRequiredElective(map1);
			for(EmpActivityDto ksRE : ksRequiredElective){
				if(ksRE.getIsRequired().equals("Y")){
					ksRequired = ksRE.getCurrencyCount();
				}else{
					ksElective = ksRE.getCurrencyCount();
				}
			}
			//必修活动参与率
			if(cyBxActivityCount>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setRequiredParticipationRate(df.format((double)cyBxActivityCount/ksRequired*100)+"%");
			}
			//选修活动参与率
			if(cyXxActivityCount>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setParticipationRate(df.format((double)cyXxActivityCount/ksElective*100)+"%");
			}
			
			//根据条件查询未报名活动数
			int wbmActivity = empActivityMapper.wbmActivity(map1);
			//完成必修活动数
			int wcBxActivityCount = empActivityDto.getRequiredActivity()-wwcBxActivityCount;
			//完成选修活动数
			int wcXxActivityCount = empActivityDto.getNumberOfOpenElectiveActivities()-(wwcXxActivityCount+wbmActivity);
			empActivityDto.setNumberOfRequiredActivitiesCompleted(wcBxActivityCount);
			empActivityDto.setNumberOfElectiveActivitiesCompleted(wcXxActivityCount);
			//必修活动平均完成率
			if(wcBxActivityCount>0&&cyBxActivityCount>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setRequiredCompletionRate(df.format((double)wcBxActivityCount/cyBxActivityCount*100)+"%");
			}
			//选修活动平均完成率
			if(wcXxActivityCount>0&&cyXxActivityCount>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setAverageCompletionRate(df.format((double)wcXxActivityCount/cyXxActivityCount*100)+"%");
			}
			
			//总活动参与数
			int cyActivityCount = cyBxActivityCount+cyXxActivityCount;
			//总活动开始数
			int ksActivityCount = ksRequired+ksElective;
			//活动参与率
			if(cyActivityCount>0&&ksActivityCount>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setActivityParticipationRate(df.format((double)cyActivityCount/ksActivityCount*100)+"%");
			}
			
			//总活动完成数
			int wcActivityCount = wcBxActivityCount+wcXxActivityCount;
			//活动完成率
			if(cyActivityCount>0&&wcActivityCount>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setActivityCompletionRate(df.format((double)wcActivityCount/cyActivityCount*100)+"%");
			}
			
			//必修活动平均进度
			if(wcBxActivityCount>0&&empActivityDto.getRequiredActivity()>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setRequiredActivitySchedule(df.format((double)wcBxActivityCount/empActivityDto.getRequiredActivity()*100)+"%");
			}
			//选修活动平均进度
			if(wcXxActivityCount>0&&empActivityDto.getNumberOfOpenElectiveActivities()>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setAverageActivitySchedule(df.format((double)wcXxActivityCount/empActivityDto.getNumberOfOpenElectiveActivities()*100)+"%");
			}
			
			//活动平均进度
			if(wcActivityCount>0&&empActivityDto.getTotalActivity()>0){
				DecimalFormat df = new DecimalFormat("######0.00");
				empActivityDto.setAverageLearningProgress(df.format((double)wcActivityCount/empActivityDto.getTotalActivity()*100)+"%");
			}
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	//导出
	public String empActivityExcel(List<EmpActivityDto> list, String fileName , String queryColumnStr){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<String,String> columnMap = learningDeptExamService.getColumnMap(queryColumnStr);//获取自定义列;
		ExcelUtil excel = new ExcelUtil(fileName);
		Map<Integer,String> headers = new HashMap<Integer,String>();
		headers.put(0, "用户名");
		headers.put(1, "姓名");
		headers.put(2, "所属组织");
		headers.put(3, "一级部门");
		headers.put(4, "二级部门");
		headers.put(5, "三级部门");
		headers.put(6, "管理角色分配");
		headers.put(7, "邮箱");
		headers.put(8, "最近登陆时间");
		headers.put(9, "总登录次数");
		headers.put(10, "总学习时长");
		headers.put(11, "平均学习时长");
		headers.put(12, "平均学习进度");
		headers.put(13, "总活动数");
		headers.put(14, "活动参与率");
		headers.put(15, "活动完成率");
		headers.put(16, "活动内完成线上课程数");
		headers.put(17, "活动内完成线下课程数");
		headers.put(18, "活动内完成线上考试数");
		headers.put(19, "平均及格率");
		if(columnMap.get("column1") != null && !"".equals(columnMap.get("column1"))){
			int currentIndex = headers.size();
			headers.put(currentIndex,"必修活动数");
			headers.put(currentIndex+1,"进行中的必修活动数");
			headers.put(currentIndex+2,"必修活动平均进度");
			headers.put(currentIndex+3,"已完成必修活动数量");
			headers.put(currentIndex+4,"必修活动参与率");
			headers.put(currentIndex+5,"必修活动平均完成率");
			headers.put(currentIndex+6,"必修活动考试数量");
//			headers.put(currentIndex+7,"必修活动考试平均成绩");
//			headers.put(currentIndex+8,"必修活动考试平均分");
			headers.put(currentIndex+7,"必修活动考试及格率");
			headers.put(currentIndex+8,"必修活动积分");
			headers.put(currentIndex+9,"必修活动获得积分");
		}
		if(columnMap.get("column2") != null && !"".equals(columnMap.get("column2"))){
			int currentIndex = headers.size();
			headers.put(currentIndex,"开放选修活动数");
			headers.put(currentIndex+1,"报名选修活动数");
			headers.put(currentIndex+2,"选修活动平均报名率");
			headers.put(currentIndex+3,"进行中的选修活动数量");
			headers.put(currentIndex+4,"选修活动平均进度");
			headers.put(currentIndex+5,"已完成选修活动数量");
			headers.put(currentIndex+6,"选修活动参与率");
			headers.put(currentIndex+7,"选修活动平均完成率");
			headers.put(currentIndex+8,"选修活动考试数量");
//			headers.put(currentIndex+9,"选修活动考试平均成绩");
//			headers.put(currentIndex+10,"选修活动考试平均分");
			headers.put(currentIndex+9,"选修活动考试及格率");
			headers.put(currentIndex+10,"选修活动积分");
			headers.put(currentIndex+11,"选修活动获得积分");
		}
		excel.insertRowData(0, headers);//添加表头;
		Map<Integer,String> datas = new HashMap<Integer,String>();
        for (int i = 0; i < list.size(); i++){
        	datas.clear();
        	EmpActivityDto empActivity = list.get(i);
        	
        	datas.put(0, empActivity.getCode());
        	datas.put(1, empActivity.getName());
        	datas.put(2, empActivity.getOrgName());
        	datas.put(3, empActivity.getOneDeptName());
        	datas.put(4, empActivity.getTwoDeptName());
        	datas.put(5, empActivity.getThreeDeptName());
        	if(empActivity.getIsAdmin().equals("Y")){
        		datas.put(6, "管理员");
        	}else{
        		datas.put(6, "-");
        	}
        	datas.put(7, empActivity.getSecretEmail());
        	if(empActivity.getRecentLandingTime()!=null){
        		datas.put(8, empActivity.getRecentLandingTime());
        	}else{
        		datas.put(8, "-");
        	}
        	datas.put(9, empActivity.getTotalNumberOfLandings()+"");
        	datas.put(10, empActivity.getTotalLengthOfStudy()+"");
        	datas.put(11, empActivity.getAverageLengthOfLearning()+"");
        	if(empActivity.getAverageLearningProgress()!=null){
        		datas.put(12, empActivity.getAverageLearningProgress());
        	}else{
        		datas.put(12, "0");
        	}
        	datas.put(13, empActivity.getTotalActivity()+"");
        	datas.put(14, empActivity.getActivityParticipationRate());
        	datas.put(15, empActivity.getActivityCompletionRate());
        	datas.put(16, empActivity.getNumberOfOnlineCoursesCompleted()+"");
        	datas.put(17, empActivity.getNumberOfCoursesCompletedInTheEvent()+"");
        	datas.put(18, empActivity.getNumberOfOnlineExamsCompleted()+"");
        	datas.put(19, empActivity.getAveragePassRate());
        	if(columnMap.get("column1") != null && !"".equals(columnMap.get("column1"))){
    			int currentIndex = datas.size();
	        	datas.put(currentIndex, empActivity.getRequiredActivity()+"");
	        	datas.put(currentIndex+1, empActivity.getNumberOfRequiredActivitiesToStartLearning()+"");
	        	datas.put(currentIndex+2, empActivity.getRequiredActivitySchedule());
	        	datas.put(currentIndex+3, empActivity.getNumberOfRequiredActivitiesCompleted()+"");
	        	datas.put(currentIndex+4, empActivity.getRequiredParticipationRate());
	        	datas.put(currentIndex+5, empActivity.getRequiredCompletionRate());
	        	datas.put(currentIndex+6, empActivity.getCompulsoryExaminationQuantity()+"");
//	        	datas.put(currentIndex+7, empActivity.getCompulsoryExaminationAverageScore()+"");
//	        	datas.put(currentIndex+8, empActivity.getCompulsoryExaminationAverage()+"");
	        	datas.put(currentIndex+7, empActivity.getRequiredExaminationPassRate());
	        	datas.put(currentIndex+8, empActivity.getRequiredActivityPoints()+"");
	        	datas.put(currentIndex+9, empActivity.getRequiredActivitiesToGetPoints()+"");
        	}
        	if(columnMap.get("column2") != null && !"".equals(columnMap.get("column2"))){
    			int currentIndex = datas.size();
    		    datas.put(currentIndex, empActivity.getNumberOfOpenElectiveActivities()+"");
	        	datas.put(currentIndex+1, empActivity.getNumberOfEnrollment()+"");
	        	datas.put(currentIndex+2, empActivity.getAverageEnrollmentRate());
	        	datas.put(currentIndex+3, empActivity.getNumberOfElectivesThatHaveBegunToLearn()+"");
	        	datas.put(currentIndex+4, empActivity.getAverageActivitySchedule());
	        	datas.put(currentIndex+5, empActivity.getNumberOfElectiveActivitiesCompleted()+"");
	        	datas.put(currentIndex+6, empActivity.getParticipationRate());
	        	datas.put(currentIndex+7, empActivity.getAverageCompletionRate());
	        	datas.put(currentIndex+8, empActivity.getNumberOfElectives()+"");
//	        	datas.put(currentIndex+9, empActivity.getAverageScoreOfElectiveActivities()+"");
//	        	datas.put(currentIndex+10, empActivity.getElectiveExaminationAverage()+"");
	        	datas.put(currentIndex+9, empActivity.getElectiveExaminationPassRate());
	        	datas.put(currentIndex+10, empActivity.getOptionalActivityPoints()+"");
	        	datas.put(currentIndex+11, empActivity.getElectiveActivitiesGetPoints()+"");
        	}
        	excel.insertRowData(i + 1, datas);//添加一行数据;
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        excel.save(path);
        return path+"/"+fileName;
	}
}
