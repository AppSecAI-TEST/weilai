package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseClasses;

public interface TomCourseClassesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomCourseClasses record);

    int insertSelective(TomCourseClasses record);

    TomCourseClasses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomCourseClasses record);

    int updateByPrimaryKey(TomCourseClasses record);
    
    int deleteByCourseId(TomCourseClasses record);
    
    List<TomCourseClasses> selectByCourseId(Map<Object,Object> map);
    
    List<TomCourseClasses> selectByCourseIdSort(Map<Object,Object> map);
    
    List<TomCourseClasses> selectByTime(Map<Object,Object> map);
    
    TomCourseClasses selectOne(Map<Object,Object> map);
}