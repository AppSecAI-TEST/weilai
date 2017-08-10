package com.chinamobo.ue.exam.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.exam.entity.TomQuestionBank;

public interface TomQuestionBankMapper {
	
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_QUESTION_BANK
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer questionBankId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_QUESTION_BANK
     *
     * @mbggenerated
     */
    int insert(TomQuestionBank record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_QUESTION_BANK
     *
     * @mbggenerated
     */
    int insertSelective(TomQuestionBank record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_QUESTION_BANK
     *
     * @mbggenerated
     */
    TomQuestionBank selectByPrimaryKey(Integer questionBankId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_QUESTION_BANK
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomQuestionBank record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_QUESTION_BANK
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomQuestionBank record);
    
    /**
     * 
     * 功能描述：[查询全部]
     *
     * 创建者：wjx
     * 创建时间: 2016年3月12日 下午5:02:11
     * @param questionBankName
     * @return
     */
    List<TomQuestionBank> queryList(TomQuestionBank questionBank);
    
    /**
     * 
     * 功能描述：[分页查询]
     *
     * 创建者：wjx
     * 创建时间: 2016年3月12日 下午5:02:21
     * @param map
     * @return
     */
    List<TomQuestionBank> selectPage(Map<Object,Object> map);
    
    /**
     * 
     * 功能描述：[查询数量]
     *
     * 创建者：wjx
     * 创建时间: 2016年3月12日 下午5:02:31
     * @param questionBankName
     * @return
     */
    int count(TomQuestionBank questionBank);
}