package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.EmpLearningDetailedCourseDto;
import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptCourseDto;


/**
 * 描述 [学习资源-组织部门统计报表（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月17日 下午5:47:38
 */
public interface LearningResourseOrgDeptCourseMapper {

	/**
	 * 功能描述 [条件 分页 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午2:57:13
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptCourseDto> findPageList(Map<String,Object> map);
	/**
	 * 功能描述 [条件 查询总记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午2:57:35
	 * @param map
	 * @return
	 */
	Integer findCount(Map<String,Object> map);
	/**
	 * 功能描述 [条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午2:57:57
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptCourseDto> findList(Map<String,Object> map);
	
	/**
	 * 功能描述 [分页 查询 课程评论内容 支持不分页用于导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 下午1:56:47
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptCourseDto> findCommentListByCourseId(Map<String,Object> map);
	
	/**
	 * 功能描述 [条件 查询课程评论记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 下午2:57:43
	 * @param map
	 * @return
	 */
	Integer findCommentListCount(Map<String,Object> map);
	/**
	 * 功能描述 [查询资源类别2级 页面下拉框使用]
	 * 创建者 LXT
	 * 创建时间 2017年3月25日 下午3:52:22
	 * @return
	 */
	List<LearningResourseOrgDeptCourseDto> findSectionClassify();
	
	List<LearningResourseOrgDeptCourseDto> findThumbUpListByCourseId(Map<String,Object> map);
	
	Integer findLearningSectionCount(Map<String,Object> map);
}
