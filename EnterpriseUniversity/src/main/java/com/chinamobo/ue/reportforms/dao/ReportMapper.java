package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.reportforms.dto.PingtaideptDto;
import com.chinamobo.ue.reportforms.dto.StuLearnResourceDto;
import com.chinamobo.ue.system.entity.TomUserLog;

public interface ReportMapper {
	
	int countAllemp(Map<Object, Object> map);
	
	int selectlogincount(Map<Object, Object> map);
	
	long sumlearntimebydate(Map<Object, Object> map);
	
	int sumlogineb(Map<Object, Object>map);
	
	int countActivebytime(Map<Object, Object> map);
	
	int counth5loginemp(Map<Object, Object> map);
	
	int  selectempCoursefinrate(Map<Object, Object> map);
	
	int  countExampassNum(Map<Object, Object> map);
	
	int countEmpResource(Map<Object, Object> map);
	
	List<TomActivity> selectActivebytime(Map<Object, Object> map);
	
	List<TomCourses> selectcourseByactiveId(Map<Object, Object> map);
	
	List<TomCourseEmpRelation> selectCoursefinish(Map<Object, Object> map);
	
	List<TomExamScore> selectExamfinish(Map<Object, Object> map);
	
	List<TomUserLog> selectloginemp(Map<Object, Object> map);
	
	List<StuLearnResourceDto> selectEmpResource(Map<Object, Object> map);
	
	StuLearnResourceDto selectEmpResourcedetail(Map<Object, Object> map);
	
	StuLearnResourceDto  selectempCourseDetail(Map<Object, Object> map);
	
	StuLearnResourceDto selectempOutExamDetail(Map<Object, Object> map);
	
	List<StuLearnResourceDto> selectUnactivityCourseDetail(Map<Object, Object> map);
	
	List<StuLearnResourceDto> selectUnactivityExamDetail(Map<Object, Object> map);
	
	StuLearnResourceDto  selectempExamDetail(Map<Object, Object> map);
	
	List<StuLearnResourceDto>  selectActivityCourseBycode(Map<Object, Object> map);
	
	List<StuLearnResourceDto>  selectUnActivityCourseBycode(Map<Object, Object> map);
	
	List<StuLearnResourceDto>  selectempSectionDetail(Map<Object, Object> map);
	
	StuLearnResourceDto selectApplyandSignNum(Map<Object, Object> map);
	
	StuLearnResourceDto  selectInActivecoursePo(Map<Object, Object> map);
	
	StuLearnResourceDto  selectOutActivecoursePo(Map<Object, Object> map);
	
	List<List<PingtaideptDto>> selectPingtaiData(Map<Object, Object> map);
	
	
}
