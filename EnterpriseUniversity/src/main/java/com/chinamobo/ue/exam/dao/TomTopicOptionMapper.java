package com.chinamobo.ue.exam.dao;

import java.util.List;

import com.chinamobo.ue.exam.entity.TomTopicOption;

public interface TomTopicOptionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC_OPTION
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC_OPTION
     *
     * @mbggenerated
     */
    int insert(TomTopicOption record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC_OPTION
     *
     * @mbggenerated
     */
    int insertSelective(TomTopicOption record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC_OPTION
     *
     * @mbggenerated
     */
    TomTopicOption selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC_OPTION
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomTopicOption record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TOPIC_OPTION
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomTopicOption record);

    /**
     * 
     * 功能描述：[根据题目id查询选项]
     *
     * 创建者：JCX
     * 创建时间: 2016年3月18日 下午3:31:50
     * @param topicId
     * @return
     */
	List<TomTopicOption> selectByTopicId(Integer topicId);

	/**
	 * 
	 * 功能描述：[根据题目id查询正确选项]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月22日 上午10:19:51
	 * @param topicId
	 * @return
	 */
	List<TomTopicOption> selectRightOption(Integer topicId);

	/**
	 * 
	 * 功能描述：[删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月15日 下午2:52:58
	 * @param topicOption
	 */
	void deleteByExample(TomTopicOption topicOption);

	/**
	 * 
	 * 功能描述：[查询选项，不返回正确状态]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月22日 上午10:25:09
	 * @param topicId
	 * @return
	 */
	List<TomTopicOption> selectByTopicIdNoStatus(Integer topicId);
	 /**
	  * 
	  * 功能描述：[查询所有题目及选项]
	  * 创建者：Acemon
	  * 创建时间：2017年7月28日
	  */
	List<TomTopicOption> selectAll();
}