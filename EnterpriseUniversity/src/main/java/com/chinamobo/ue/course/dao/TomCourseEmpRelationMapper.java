package com.chinamobo.ue.course.dao;

import java.util.List;

import com.chinamobo.ue.course.entity.TomCourseEmpRelation;

public interface TomCourseEmpRelationMapper {
    int insert(TomCourseEmpRelation record);

    int insertSelective(TomCourseEmpRelation record);
    
    List<TomCourseEmpRelation> selectByEmpId (String empcode);
    List<TomCourseEmpRelation> selectByEmpIdTwo (String empcode);
    /**
     * 
     * 功能描述：[根据条件查询课程人员关联]
     *
     * 创建者：JCX
     * 创建时间: 2016年4月12日 下午1:29:50
     * @param courseEmpRelation
     * @return
     */
	List<TomCourseEmpRelation> selectByExample(TomCourseEmpRelation courseEmpRelation);

	List<TomCourseEmpRelation> selectByExample2(TomCourseEmpRelation courseEmpRelation);
	
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月12日 下午2:16:17
	 * @param courseEmpRelation
	 */
	void update(TomCourseEmpRelation courseEmpRelation);

	/**
	 * 
	 * 功能描述：[删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月13日 下午12:56:06
	 * @param courseEmpRelationExample
	 */
	void deleteByExample(TomCourseEmpRelation courseEmpRelationExample);

	List<TomCourseEmpRelation> selectByCourseId(Integer courseId);

	List<TomCourseEmpRelation> selectbyCode(String code);
	
	void insertList(List<TomCourseEmpRelation> list);
	
	void deleteByCourseId(TomCourseEmpRelation courseEmpRelationExample);

}