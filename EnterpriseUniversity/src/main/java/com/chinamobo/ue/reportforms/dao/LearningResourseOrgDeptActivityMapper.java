package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptActivityDto;


/**
 * 描述 [学习资源-组织部门报表统计（活动）]
 * 创建者 LXT
 * 创建时间 2017年3月21日 下午3:20:01
 */
public interface LearningResourseOrgDeptActivityMapper {

	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:20:56
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptActivityDto> findPageList(Map<String,Object> map);

	/**
	 * 功能描述 [条件 查询记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:21:09
	 * @param map
	 * @return
	 */
	Integer findCount(Map<String,Object> map);

	/**
	 * 功能描述 [条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:21:22
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptActivityDto> findList(Map<String,Object> map);
	
	/**
	 * 功能描述 [查询活动类别]
	 * 创建者 LXT
	 * 创建时间 2017年3月25日 下午1:34:46
	 * @return
	 */
	List<LearningResourseOrgDeptActivityDto> findActivityType();
}
