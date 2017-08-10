/**
 * 
 */
package com.chinamobo.ue.system.dao;

import java.util.List;

import com.chinamobo.ue.system.entity.TomRollingPictureEmp;
/**
 * 版本: [1.0]
 * 功能说明: 
 *
 * 作者: WChao
 * 创建时间: 2017年6月8日 上午10:58:01
 */
public interface TomRollingPictureEmpMapper {
	/**
	 * 
		 * 
		 * 功能描述：[插入轮播图片与轮播人员关系表]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年6月8日 上午11:02:09
		 * @param rollingPictureEmp
		 * @return
	 */
    int insert(TomRollingPictureEmp rollingPictureEmp);

    /**
     * 
    	 * 
    	 * 功能描述：[根据轮播图片ID查询轮播人员]
    	 *
    	 * 创建者：WChao
    	 * 创建时间: 2017年6月8日 上午11:02:31
    	 * @param pictureId
    	 * @return
     */
	List<TomRollingPictureEmp> selectByPictureId(int pictureId);
	/**
	 * 
		 * 
		 * 功能描述：[根据轮播图片删除轮播人员]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年6月8日 上午11:29:29
		 * @param pictureId
		 * @return
	 */
	int deleteByPictureId(String pictureId);
}
