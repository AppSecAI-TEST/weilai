package com.chinamobo.ue.system.dao;

import java.util.Date;
import java.util.Map;

import com.chinamobo.ue.system.entity.TomLoginRecord;

public interface TomLoginRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomLoginRecord record);

    int insertSelective(TomLoginRecord record);

    TomLoginRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomLoginRecord record);

    int updateByPrimaryKey(TomLoginRecord record);
    
    TomLoginRecord selectByMap(Map<Object,Object> map);
    
    //查询总登陆次数与最近登陆时间
    TomLoginRecord selectByDateCount(String code);
}