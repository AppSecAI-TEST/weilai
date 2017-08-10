package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.TomNumberRecord;

public interface TomNumberRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NUMBER_RECORD
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer numberId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NUMBER_RECORD
     *
     * @mbggenerated
     */
    int insert(TomNumberRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NUMBER_RECORD
     *
     * @mbggenerated
     */
    int insertSelective(TomNumberRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NUMBER_RECORD
     *
     * @mbggenerated
     */
    TomNumberRecord selectByPrimaryKey(Integer numberId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NUMBER_RECORD
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomNumberRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NUMBER_RECORD
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomNumberRecord record);
    /**
     * 按belong查询
     * @param numberId
     * @return
     */
    TomNumberRecord selectByBelong(String belong);
}