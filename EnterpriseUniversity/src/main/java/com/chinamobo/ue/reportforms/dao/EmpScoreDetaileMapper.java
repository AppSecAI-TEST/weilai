
package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.reportforms.dto.EmpScoreDetaileDto;

/**
 * 功能描述  [学员报表-积分]
 * 创建者 LXT
 * 创建时间 2017年3月14日 下午6:59:45
 */
public interface EmpScoreDetaileMapper {


	/**
	 * 功能描述 [条件 分页 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月15日 上午9:21:32
	 * @param map
	 * @return
	 */
	List<EmpScoreDetaileDto> findPageList(Map<String,Object> map);
	
	
	/**
	 * 功能描述 [条件 查询总记录数]
	 * 创建者 LXT
	 * 创建时间 2017年3月15日 上午9:21:52
	 * @param map
	 * @return
	 */
	Integer findCount(Map<String,Object> map);
	
	/**
	 * 功能描述 [条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月15日 上午10:18:52
	 * @param map
	 * @return
	 */
	List<EmpScoreDetaileDto> findList(Map<String,Object> map);
	
	/**
	 * 功能描述 [条件 查询个人积分明细]
	 * 创建者 LXT
	 * 创建时间 2017年3月16日 上午8:59:49
	 * @param map
	 * @return
	 */
	List<EmpScoreDetaileDto> findOne(Map<String,Object> map);
	
	/**
	 * 功能描述 [条件 查询个人积分总数]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 下午2:39:49
	 * @param map
	 * @return
	 */
	Integer findOneCount(Map<String,Object> map);
}
