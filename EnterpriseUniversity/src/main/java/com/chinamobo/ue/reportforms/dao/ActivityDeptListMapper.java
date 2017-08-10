package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;

public interface ActivityDeptListMapper {
	
	int CountActivityDept(Map<Object,Object>map);
	
	List<ActivityDeptListDto>findList(Map<Object,Object>map);
	
	int findActivityDeptCount(Map<Object,Object>map);
	/**2：参加课程的人数**/
	int findJoinCourseCount(Map<Object,Object> map);
	/**3:学习人数(学习中的人数)**/
	int findIsLearningNumber(Map<Object,Object> map);
	/**4：完成人数(课程)**/
	int findToCourse(Map<Object,Object> map);
	/**5:总时长(课程)**/
	int findTotalTime(Map<Object,Object> map);
	/**6:总成绩 **/
	int findScore(Map<Object,Object> map);
	/**7: 考试通过人数**/
    int findToExam(Map<Object,Object> map);
    /**8：考试参加人数**/
    int findJoinExam(Map<Object,Object> map);
    /**9：选修开放人数**/
    int findOpenNumber(Map<Object,Object> map);
    /**10：选修报名人数**/
    int findNumberOfParticipants(Map<Object,Object> map);
    /**11：获得积分(课程+考试)**/
    int findCourseEcurrency(Map<Object,Object> map);
    int findExamPassEB(Map<Object,Object> map);
    /**12:活动学习人数**/
    int findToBe(Map<Object,Object> map);
    /**13:活动中的考试数 **/
    int selectExam(Map<Object,Object> map);
    /**14:通过考试的人员code，考试id **/
    List<ActivityDeptListDto>findToExam1(Map<Object,Object> map);
    /** 15: 完成课程的人员code，课程id**/
    List<ActivityDeptListDto>findToCourse1(Map<Object,Object> map);
    /**查询指定活动参与人所参加课程+考试情况**/
    List<ActivityDeptListDto> findEmpActivityCourseAndExam(Map<Object,Object> map);
	List<ActivityDeptListDto>findActivityDept(Map<Object,Object>map);
	List<ActivityDeptListDto>findActivityDeptView(Map<Object,Object>map);
	List<ActivityDeptListDto> findActivityDeptExcel(Map<Object,Object>map);
	
}
