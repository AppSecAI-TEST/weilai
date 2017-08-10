package com.chinamobo.ue.course.dao;

import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseDetail;

public interface TomCourseDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomCourseDetail record);

    int insertSelective(TomCourseDetail record);

    TomCourseDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomCourseDetail record);

    int updateByPrimaryKeyWithBLOBs(TomCourseDetail record);

    int updateByPrimaryKey(TomCourseDetail record);
    
    TomCourseDetail selectByCourseId(Map<Object,Object> map);
}