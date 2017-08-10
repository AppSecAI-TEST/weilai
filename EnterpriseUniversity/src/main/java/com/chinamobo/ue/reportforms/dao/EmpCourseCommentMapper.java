package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.EmpCourseCommentDto;

/**
 * 学员报表-课程评论
 * @author LXT
 *
 */
public interface EmpCourseCommentMapper {

	/**
	 * 功能描述 [查询所有]
	 * 创建者 LXT
	 * 创建时间 2017年3月10日 下午4:25:28
	 * @return
	 */
	List<EmpCourseCommentDto> findAll();
	/**
	 * 功能描述 [支持分页]
	 * 创建者 LXT
	 * 创建时间 2017年3月10日 下午4:25:56
	 * @param map
	 * @return
	 */
	List<EmpCourseCommentDto> findPageList(Map<String,Object> map);
	
	/**
	 * 功能描述 [条件  查询记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月13日 下午5:20:29
	 * @param map
	 * @return
	 */
	int findCount(Map<String,Object> map);
	
	/**
	 * 功能描述 [条件查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月14日 上午10:37:03
	 * @param map
	 * @return
	 */
	List<EmpCourseCommentDto> findList(Map<String,Object> map);
}
