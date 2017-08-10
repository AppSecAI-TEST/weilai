package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptExamDto;

/**
 * 描述 [学习资源-组织部门统计报表（线上考试）]
 * 创建者 LXT
 * 创建时间 2017年3月20日 下午7:56:31
 */
public interface LearningResourseOrgDeptExamMapper {

	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXTs
	 * 创建时间 2017年3月20日 下午7:57:51
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptExamDto> findPageList(Map<String,Object> map);
	/**
	 * 功能描述 [条件 查询记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月20日 下午7:58:32
	 * @param map
	 * @return
	 */
	Integer findCount(Map<String,Object> map);
	/**
	 * 功能描述 [条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月20日 下午7:58:45
	 * @param map
	 * @return
	 */
	List<LearningResourseOrgDeptExamDto> findList(Map<String,Object> map);
}
