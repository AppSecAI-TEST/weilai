package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.TomAuthoritiesTypeRole;

public interface TomAuthoritiesTypeRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_AUTHORITIES_TYPE_ROLE
     *
     * @mbggenerated
     */
    int insert(TomAuthoritiesTypeRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_AUTHORITIES_TYPE_ROLE
     *
     * @mbggenerated
     */
    int insertSelective(TomAuthoritiesTypeRole record);
    
    void deleteByPrimaryKey(Integer roleid);
}