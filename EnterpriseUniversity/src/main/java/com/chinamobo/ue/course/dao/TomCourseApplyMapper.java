package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseApply;
import com.chinamobo.ue.system.entity.TomUserInfo;

public interface TomCourseApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomCourseApply record);

    int insertSelective(TomCourseApply record);

    TomCourseApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomCourseApply record);

    int updateByPrimaryKey(TomCourseApply record);
    
    TomCourseApply selectApply(Map<Object,Object> map);
    
    List<TomCourseApply> classmates(Map<Object,Object> map);
    List<TomCourseApply> applyList(Map<Object,Object> map);
    int countApply(Map<Object,Object> map);
    int countEmp(Map<Object,Object> map);
}