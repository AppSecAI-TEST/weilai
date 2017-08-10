package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.EmpLearningDetailedCourseDto;


/**
 * 描述 [学员报表-学习资源详细（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月22日 下午4:35:57
 */
public interface EmpLearningDetailedCourseMapper {
	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月22日 下午4:37:40
	 * @param map
	 * @return
	 */
	List<EmpLearningDetailedCourseDto> findPageList(Map<String,Object> map);

	/**
	 * 功能描述 [条件 查询记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月22日 下午4:37:50
	 * @param map
	 * @return
	 */
	Integer findCount(Map<String,Object> map);

	/**
	 * 功能描述 [条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月22日 下午4:38:01
	 * @param map
	 * @return
	 */
	List<EmpLearningDetailedCourseDto> findList(Map<String,Object> map);
	
	/**
	 * 功能描述 [查询资源类别2级 页面下拉框使用]
	 * 创建者 LXT
	 * 创建时间 2017年3月25日 下午3:52:22
	 * @return
	 */
	List<EmpLearningDetailedCourseDto> findSectionClassify();
}
