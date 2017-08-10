package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.JsApi;

public interface JsApiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JsApi record);

    int insertSelective(JsApi record);

    JsApi selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JsApi record);

    int updateByPrimaryKey(JsApi record);
}