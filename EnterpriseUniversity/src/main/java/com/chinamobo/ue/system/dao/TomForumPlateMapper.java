package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.TomForumPlate;

public interface TomForumPlateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_PLATE
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer plateId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_PLATE
     *
     * @mbggenerated
     */
    int insert(TomForumPlate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_PLATE
     *
     * @mbggenerated
     */
    int insertSelective(TomForumPlate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_PLATE
     *
     * @mbggenerated
     */
    TomForumPlate selectByPrimaryKey(Integer plateId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_PLATE
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomForumPlate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_PLATE
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomForumPlate record);
}