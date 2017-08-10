package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomOfflineSign;

public interface TomOfflineSignMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomOfflineSign record);

    int insertSelective(TomOfflineSign record);

    TomOfflineSign selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomOfflineSign record);

    int updateByPrimaryKey(TomOfflineSign record);
    
    List<TomOfflineSign> selectSignRecord(Map<Object,Object> map);
    
    //查询该学员是否签到
    int signCount(Map<Object,Object> map);
}