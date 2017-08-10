package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.StuCourseRankingDto;

public interface StuCourseRankingMapper {

	//学员活动，学分排行
	public Integer findEmpActivityCount(Map<String,Object> map);
	
	List<StuCourseRankingDto> findEmpActivity(Map<String,Object> map);
	
	//学员活动，学时排行
	public Integer findActivityScoreCount(Map<String,Object> map);
	
	List<StuCourseRankingDto>findActivityScore(Map<String,Object> map);
	
	//学员活动，课程评分排行
	public Integer findActivityCourseScoreCount(Map<String,Object> map);
	
	List<StuCourseRankingDto>findActivityCourseScore(Map<String,Object> map);
	
}
