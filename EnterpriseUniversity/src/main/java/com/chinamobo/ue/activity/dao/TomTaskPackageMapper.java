package com.chinamobo.ue.activity.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.dto.TomTaskPackageDto;
import com.chinamobo.ue.activity.entity.TomTaskPackage;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.entity.TomExamPaper;

public interface TomTaskPackageMapper {//extends BaseDao<TomTaskPackage>{
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TASK_PACKAGE
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer packageId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TASK_PACKAGE
     *
     * @mbggenerated
     */
    int insert(TomTaskPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TASK_PACKAGE
     *
     * @mbggenerated
     */
    int insertSelective(TomTaskPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TASK_PACKAGE
     *
     * @mbggenerated
     */
    TomTaskPackage selectByPrimaryKey(Integer packageId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TASK_PACKAGE
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomTaskPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_TASK_PACKAGE
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomTaskPackage record);
    
    /**
     * 
     * WANGLG
     * 任务包查询
     * @param task
     * @return
     */
    int countByList(Map<Object,Object> map);
    List<TomTaskPackage> selectListByParam(Map<Object,Object> map);
    List<TomTaskPackage> selectAllList(TomTaskPackage task);
    /**
     * 
     * 功能描述：[任务包列表]
     *
     * 创建者：WangLg
     * 创建时间: 2016年3月10日 下午5:23:53
     * @param activityId
     * @return
     */
    
     PageData<TomTaskPackage> findList1(TomTaskPackage task);
     
     /**
      * 功能描述：[选择活动包]
      *
      * 创建者：WangLg
      * 创建时间: 2016年3月15日 下午2:59:10
      * @param task
      * @return
      */
     List<TomTaskPackage> selectAllTask();
     
     
     List<TomTaskPackageDto> selectDetails(Map<Object,Object> map);
     
     List<TomTaskPackage> selectByAdminId(int activityId);
     
     List<TomCourses> selectDetailsCourse(Map<Object,Object> map);
     List<TomExamPaper> selectDetailsExamPaper(Map<Object,Object> map);
     
}