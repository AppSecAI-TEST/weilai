package com.chinamobo.ue.exam.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.exam.entity.TomExamPaper;

public interface TomExamPaperMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_PAPER
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer examPaperId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_PAPER
     *
     * @mbggenerated
     */
    int insert(TomExamPaper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_PAPER
     *
     * @mbggenerated
     */
    int insertSelective(TomExamPaper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_PAPER
     *
     * @mbggenerated
     */
    TomExamPaper selectByPrimaryKey(Integer examPaperId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_PAPER
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomExamPaper record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_PAPER
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomExamPaper record);

    /**
     * 
     * 功能描述：[获得记录数]
     *
     * 创建者：JCX
     * 创建时间: 2016年3月12日 下午1:43:24
     * @param example
     * @return
     */
	int countByExample(TomExamPaper example);

	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 下午1:43:41
	 * @param map
	 * @return
	 */
	List<TomExamPaper> selectListByPage(Map<Object, Object> map);
	
	
	TomExamPaper selectByExamId(int examId);
	/**
	 * 
	 * 功能描述：[查询所有试卷]
	 * 创建者：Acemon
	 * 创建时间：2017年7月28日
	 */
	List<TomExamPaper> selectAll();
}