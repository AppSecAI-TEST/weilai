package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.EmpActivityDto;

public interface EmpActivityMapper {
	List<EmpActivityDto> empActivityList(Map<Object,Object> map);
	int empActivityCount(Map<Object,Object> map);
	//根据条件查询总活动数总活动数
	int totalActivityCount(Map<Object,Object> map);
	//根据条件查询活动内完成线上课程数
	int numberOfOnlineCoursesCompleted(Map<Object,Object> map);
	//根据条件查询活动内完成线下课程数
	int numberOfCoursesCompletedInTheEvent(Map<Object,Object> map);
	//根据条件查询活动内完成线上考试数
	int numberOfOnlineExamsCompleted(Map<Object,Object> map);
	//根据条件查询必修活动数/开放选修活动数
	List<EmpActivityDto> requiredElectiveCount(Map<Object,Object> map);
	//根据条件查询已开始学习的必修活动数
	int numberOfRequiredActivitiesToStartLearning(Map<Object,Object> map);
	//根据条件查询已开始学习的选修活动数量
	int numberOfElectivesThatHaveBegunToLearn(Map<Object,Object> map);
	//根据条件查询报名选修活动数
	int numberOfEnrollment(Map<Object,Object> map);
	//根据条件查询必修活动考试数量/选修活动考试数量
	List<EmpActivityDto> requiredElectiveExamCount(Map<Object,Object> map);
	//根据条件查询必修活动考试总分/选修活动考试总分
	List<EmpActivityDto> requiredElectiveExamScore(Map<Object,Object> map);
	//根据条件查询必修活动考试及格数/选修活动考试及格数
	List<EmpActivityDto> requiredElectiveExamPass(Map<Object,Object> map);
	//根据条件查询必修活动课程积分/选修活动课程积分
	List<EmpActivityDto> requiredElectiveCourseIntegral(Map<Object,Object> map);
	//根据条件查询必修活动考试积分/选修活动考试积分
	List<EmpActivityDto> requiredElectiveExamIntegral(Map<Object,Object> map);
	//根据条件查询必修活动获得线上课程积分/选修活动获得线上课程积分
	List<EmpActivityDto> resquiredElectiveGetCourseIntegral(Map<Object,Object> map);
	//根据条件查询必修活动获得线下课程积分/选修活动获得线下课程积分
	List<EmpActivityDto> rexquiredElectiveGetCourseIntegral(Map<Object,Object> map);
	//根据条件查询必修活动完成考试获得积分/选修活动完成考试获得积分
	List<EmpActivityDto> requiredElectiveGetExamIntegral(Map<Object,Object> map);
	//根据条件查询必修活动未完成考试扣除积分/选修活动未完成考试扣除积分
	List<EmpActivityDto> requiredElectiveDeductionExamIntegral(Map<Object,Object> map);
	//根据条件查询所有活动ID,忽略选修未报名
	List<EmpActivityDto> activityIdList(Map<Object,Object> map);
	//根据活动ID查询课程/考试ID
	List<EmpActivityDto> requiredElectiveCourseExamId(Integer activityId);
	//根据条件查询已开始的必修活动/选修活动
	List<EmpActivityDto> ksRequiredElective(Map<Object,Object> map);
	//根据条件查询未报名总活动数
	int wbmActivity(Map<Object,Object> map);
	//根据条件查询必修活动课程积分
	int rCourseIntegral(Map<Object,Object> map);
	//根据条件查询选修活动课程积分
	int eCourseIntegral(Map<Object,Object> map);
	//根据条件查询必修活动线上课程获得积分
	int rsGetCourseIntegral(Map<Object,Object> map);
	//根据条件查询选修活动线上课程获得积分
	int esGetCourseIntegral(Map<Object,Object> map);
	//根据条件查询必修活动线下课程获得积分
	int rxGetCourseIntegral(Map<Object,Object> map);
	//根据条件查询选修活动线下课程获得积分
	int exGetCourseIntegral(Map<Object,Object> map);
	/**
	 * 
	 * 功能描述：[统计通过考试数]
	 * 创建者：Acemon
	 * 创建时间：2017年7月21日
	 */
	int examPassNum(Map<Object,Object> map);
}
