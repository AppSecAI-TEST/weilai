package com.chinamobo.ue.system.dao;

import java.util.Map;

import com.chinamobo.ue.system.entity.TomLoginAuth;

public interface TomLoginAuthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TomLoginAuth record);

    int insertSelective(TomLoginAuth record);

    TomLoginAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TomLoginAuth record);

    int updateByPrimaryKey(TomLoginAuth record);
    
    TomLoginAuth selectByMap(Map<Object,Object> map);
    
    int deleteByCode(Map<Object,Object> map);
}