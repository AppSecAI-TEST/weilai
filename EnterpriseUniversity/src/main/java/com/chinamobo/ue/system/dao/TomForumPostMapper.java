package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.TomForumPost;

public interface TomForumPostMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_POST
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer postId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_POST
     *
     * @mbggenerated
     */
    int insert(TomForumPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_POST
     *
     * @mbggenerated
     */
    int insertSelective(TomForumPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_POST
     *
     * @mbggenerated
     */
    TomForumPost selectByPrimaryKey(Integer postId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_POST
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomForumPost record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_FORUM_POST
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomForumPost record);
}