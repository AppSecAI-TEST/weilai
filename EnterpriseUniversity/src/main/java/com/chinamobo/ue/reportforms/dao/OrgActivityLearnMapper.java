package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.OrgActivityLearnDto;

/**
 * 功能描述 [ ]
 * @作者 TYX
 *	创建时间：2017年3月20日 下午6:53:23
 */
public interface OrgActivityLearnMapper {

	/**
	 * 功能描述：[ ]
	 * 作者：TYX
	 * 创建时间：2017年3月20日 下午7:02:40
	 * @param map
	 * @return
	 */
	 List<OrgActivityLearnDto>findList(Map<String,Object> map);
	
	 Integer findListCoun(Map<String,Object> map);
	 
	  // 学习总时长
	 Integer RStudyTime(Map<String,Object> map);
	  // 学习完成课程
	 Integer RComCourseSection(Map<String,Object> map);
	 //平均成绩
	double RAvgScore(Map<String,Object> map);
	//活动中课程E
	 int RCourseE(Map<String,Object> map);
	 //考试及格人数
	 int RPassExam(Map<String,Object> map);
	 //考试总人数
	 int RExamTotal(Map<String,Object> map);
	 //课程完成人数
	 int RComCourseNum(Map<String,Object> map);
	 //课程总人数
	 int RJoinCourseNum(Map<String,Object> map);
	 //通过考试E
	 int RPassExamE(Map<String,Object> map);
	 
	  // 学习总时长
		 Integer CStudyTime(Map<String,Object> map);
		  // 学习完成课程
		 Integer CComCourseSection(Map<String,Object> map);
		 //平均成绩
		double CAvgScore(Map<String,Object> map);
		//活动中课程E
		 int CCourseE(Map<String,Object> map);
		 //考试及格人数
		 int CPassExam(Map<String,Object> map);
		 //考试总人数
		 int CExamTotal(Map<String,Object> map);
		 //课程完成人数
		 int CComCourseNum(Map<String,Object> map);
		 //课程总人数
		 int CJoinCourseNum(Map<String,Object> map);
		 //通过考试E
		 int CPassExamE(Map<String,Object> map);
	 
	 
	 
	
}
