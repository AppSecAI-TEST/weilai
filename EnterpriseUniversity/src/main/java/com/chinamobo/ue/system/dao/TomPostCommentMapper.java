package com.chinamobo.ue.system.dao;

import com.chinamobo.ue.system.entity.TomPostComment;

public interface TomPostCommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_POST_COMMENT
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer postCommentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_POST_COMMENT
     *
     * @mbggenerated
     */
    int insert(TomPostComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_POST_COMMENT
     *
     * @mbggenerated
     */
    int insertSelective(TomPostComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_POST_COMMENT
     *
     * @mbggenerated
     */
    TomPostComment selectByPrimaryKey(Integer postCommentId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_POST_COMMENT
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomPostComment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_POST_COMMENT
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomPostComment record);
}