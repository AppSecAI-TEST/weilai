
package com.chinamobo.ue.activity.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.dto.TomActivityPropertyDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.api.activity.dto.ActResults;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dto.TomCoursesDto;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;

import com.chinamobo.ue.reportforms.dto.EmpActivityDetaListDto;

import com.chinamobo.ue.reportforms.dto.EmpActivityAnswerDetailDto;
import com.chinamobo.ue.statistics.entity.TomActivityCostStatistics;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomUserLog;

public interface TomActivityMapper {// extends BaseDao<TomActivity>{
	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int deleteByPrimaryKey(Integer activityId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int insert(TomActivity record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int insertSelective(TomActivity record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	TomActivity selectByPrimaryKey(Integer activityId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKeySelective(TomActivity record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds
	 * to the database table TOM_ACTIVITY
	 *
	 * @mbggenerated
	 */
	int updateByPrimaryKey(TomActivity record);

	/**
	 * 
	 * ����������[��б�]
	 *
	 * �����ߣ�WangLg ����ʱ��: 2016��3��10�� ����5:23:53
	 * 
	 * @param activityId
	 * @return
	 */
	PageData<TomActivity> selectListByParames();

	/**
	 * ����������[API-����ѧԱ�Ļ�б�]
	 *
	 * �����ߣ�WangLg ����ʱ��: 2016��3��10�� ����5:36:19
	 * 
	 * @param activityId
	 * @return
	 */
	PageData<TomActivity> selectListByParames1(TomActivity activityId);

	/**
	 * 培训活动列表
	 * 
	 * @param task
	 * @return
	 */
	int countByList(TomActivity act);

	List<TomActivity> selectListByParam(Map<Object, Object> map);
	
	int countCompletedNum(Map<Object, Object> map);
	/**
	 * 复选框查询
	 * @param act
	 * @return
	 */
	List<TomActivity> selectListByCodes(Map<Object, Object> map);

	List<TomActivity> selectAllList(TomActivity act);

	List<TomActivity> selectList();

	int countByList(Map<Object, Object> map);
	
	int countByListGlobel(Map<Object, Object> map);

	List<ActResults> selectByUserListGlobel(Map<Object, Object> map);

	/**
	 * API 活动详情
	 */
	ActResults selectByUser(Map<Object, Object> map);

	List<ActResults> selectByUserList(Map<Object, Object> map);

	// 培训活动个人报名
	ActResults selectByUserRe(Map<Object, Object> map);

	ActResults selectByUserDetail(Map<Object, Object> map);

	List<TomActivityDto> selectDetails(Map<Object, Object> map);

	List<ActResults> FindAllBylist(Map<Object, Object> Map);

	/**
	 * API 查询活动通过未通过 通过考试成绩状态来查询
	 */

	int FindByCoursecount(Map<Object, Object> Map);

	List<ActResults> FindByCourseList(Map<Object, Object> map);

	/**
	 * API 查询负责人活动报名推送的活动人员 通过登录人员ID查询
	 */

	int FindByUserIdCount(Map<Object, Object> Map);

	List<ActResults> FindByUserIdList(Map<Object, Object> Map);

	/**
	 * 
	 * 功能描述：[任务中活动数量]
	 *
	 * 创建者：JCX 创建时间: 2016年5月26日 下午2:18:05
	 * 
	 * @param queryMap
	 * @return
	 */
	int countByUserList(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[根据员工查出其所有活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午10:14:09
	 * 
	 * @param code
	 * @return
	 */
	List<TomActivity> selectAllByEmp(Map<Object, Object> allByEmp);

	/**
	 * 
	 * 功能描述：[统计未开始活动数]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午11:22:57
	 * 
	 * @param Map
	 * @return
	 */
	int countNotStart(Map<Object, Object> Map);

	/**
	 * 
	 * 功能描述：[统计进行中活动数]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午11:44:25
	 * 
	 * @param queryMap
	 * @return
	 */
	int countUnderway(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[统计已完成活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 上午11:56:11
	 * 
	 * @param queryMap
	 * @return
	 */
	int countFinished(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[统计未通过活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:29:09
	 * 
	 * @param queryMap
	 * @return
	 */
	int countNotPass(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询未开始活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:41:46
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectNotStart(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询已开始活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:46:35
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectUnderway(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询已完成活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:50:14
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectFinished(Map<Object, Object> queryMap);

	/**
	 * 
	 * 功能描述：[查询未通过活动]
	 *
	 * 创建者：JCX 创建时间: 2016年5月30日 下午12:52:29
	 * 
	 * @param queryMap
	 * @return
	 */
	List<TomActivity> selectNotPass(Map<Object, Object> queryMap);

	/**
	 * 功能介绍[根据视图分页查询] 创建者：LG 创建时间：2016年6月3日 15：21
	 */
	List<TomActivityCostStatistics> selectactivityCostList(Map<Object, Object> map);

	int selectCount(Map<Object, Object> map);
	
	List<TomActivity> selectByEmpActivity(Map<Object,Object> map);
	
 int	CountByEmpActivity (Map<Object,Object> map );
   //根据考试id查中英文状态
    List<TomActivity> selectByExamPaper(int examPaperId);
 
 
 
 
/*	List<TomActivity> selectActivity(Map<Object, Object> map);
 
 //计算活动部门
 	int CountActivityDept(Map<Object,Object>map);
 	
	List<TomDept> selectActivityDept(Map<Object, Object> map);
// 	List<TomDept> selectActivityDept(int activityid);
	
	//查询未完成课程的人数
//	int selectByEmpId(int courseid);
	
	//查询活动的积分
	int selectscore1(int activity);
	
	//查询活动中的课程
	List<TomCourses>selectcourseByactiveId(Map<Object, Object> map);
	
	//查询参加该活动中的课程的人
	List<TomCourseEmpRelation>selectCoursefinish(Map<Object, Object> map);
	
	//查询参加活动中考试的人
	List<TomExamScore>selectExamfinish(Map<Object, Object> map);
	
	//参加该活动的人成绩
	double selectScore(Map<Object, Object> map);
	
	List<ActivityDeptListDto> select();
	
	//计算积分
	List<TomUserLog>selectloginemp(Map<Object, Object> map);
	
	int sumlogineb(Map<Object, Object> map);
	
	//活动类型
	List<TomProjectResource> selectActivityType(int activity);*/
	
 
	int CountActivityDept(Map<Object,Object>map);
	List<ActivityDeptListDto> selectActivityDept(Map<Object, Object> map);
	List<ActivityDeptListDto> selectActivityDeptExcel(Map<Object, Object> map);
	List<TomCourses>selectcourseByactiveId(Map<Object, Object> map);
	List<TomCourseEmpRelation>selectCoursefinish(Map<Object, Object> map);
	List<TomExamScore>selectExamfinish(Map<Object, Object> map);
	double selectScore(Map<Object, Object> map);
	List<TomUserLog>selectloginemp(Map<Object, Object> map);
	int sumlogineb(Map<Object, Object> map);
	double selectAvgScore(Map<Object, Object> map);
	int selectOpenNum(Map<Object, Object> map);


   /**
    * 根据项目id查找活动
    */

   List<TomActivity> selectByProjectId(Map<Object,Object> map);
  
   List<TomActivity>selectAcyivityName(Map<Object,Object> map);
   /**
    * 查询活动人员问卷
    */

   List<EmpActivityAnswerDetailDto> selectEmpAnswer(Map<Object,Object> map);
   int countEmpAnswer(Map<Object,Object> map);
 
 int countEmpActivityDeta(Map<Object,Object> map);
 List<EmpActivityDetaListDto>empActivityDetaList(Map<Object,Object> map);
 List<EmpActivityDetaListDto>empActivityDetaListExcel(Map<Object,Object> map);
 int countSection(Map<Object,Object> map);
 int countActivityCourse(Map<Object,Object> map);
 int countCourseOnline(Map<Object,Object> map);
 int countCourseOffline( Map<Object,Object> map);
int countStudyTimer(Map<Object,Object> map);
/*Date activityCourseFinishTime(Map<Object,Object> map);
Date activityExamFinishTime(Map<Object,Object> map);*/
EmpActivityDetaListDto activityFinishTime(Map<Object,Object> map);
int countOnlineExam(Map<Object,Object> map);
int countOnlineExam1(Map<Object,Object> map);
int countInteger(Map<Object, Object> map);
int applyActivity(Map<Object, Object> map);

int applyActivityCourse(Map<Object, Object> map);
int applyActivityExam(Map<Object, Object> map);
int NoApplyActivityCourse(Map<Object, Object> map);
int NoApplyActivityExam(Map<Object, Object> map);

int NoApplyActivity(Map<Object, Object> map);
/*int countCourseApply(Map<Object, Object> map);*/
//int countCourseSign(Map<Object, Object> map);
int selectOfflineCourseApply(Map<Object, Object> map);
TomCoursesDto offlineCourse(int courseId);
int codejoinofflinecourse(Map<Object, Object> map);

String selecttotalIntegral(Map<Object, Object> map);



//活动详细
int courseLearning(Map<Object, Object> map);
int offlinecourselearning(Map<Object, Object> map);
int activitycourse(Map<Object, Object> map);
int ActivityOpenNum(Map<Object, Object> map);
double selectAvgScore1(Map<Object, Object> map);
int onlineComplentExam(Map<Object, Object> map);
int onlineComplentExam1(Map<Object, Object> map);
int onlineCompleteCourse(Map<Object, Object> map);

List<EmpActivityDetaListDto> count(Map<Object, Object> map);
/** 完成线上课程数、线下课程数、获得的eb数（线上课程、线下课程、考试）**/
List<EmpActivityDetaListDto>countExamEb(Map<Object, Object> map);
/**活动中课程id **/
List<TomActivityProperty> getCourseId(Map<Object, Object> maps);

/** 活动中课程考试总数 **/
int activityCECount(Map<Object,Object> map);
}