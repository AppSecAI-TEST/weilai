package com.chinamobo.ue.course.dao;

import java.util.Map;

import com.chinamobo.ue.course.entity.TomLearnTimeLog;

public interface TomLearnTimeLogMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(TomLearnTimeLog record);

    int insertSelective(TomLearnTimeLog record);

    TomLearnTimeLog selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(TomLearnTimeLog record);

    int updateByPrimaryKey(TomLearnTimeLog record);
    
    Integer sumTime(Map<Object,Object> map);
    //查询学习时长在某时间段内
    int countLearningTime(Map<Object,Object> map);
}