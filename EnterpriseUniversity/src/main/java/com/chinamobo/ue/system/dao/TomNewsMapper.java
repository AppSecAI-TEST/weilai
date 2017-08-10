package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.api.myInfo.dto.NewsDto;
import com.chinamobo.ue.system.entity.TomNews;

public interface TomNewsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer newsId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    int insert(TomNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    int insertSelective(TomNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    TomNews selectByPrimaryKey(Integer newsId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TomNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(TomNews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_NEWS
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TomNews record);
    
int countByExample(Map<Object, Object> map1);
	

	List<NewsDto> selectByMany(Map<Object, Object> map);

	TomNews selectByOne(Map<Object, Object> map);
	/**
	 * 功能介绍：[分页查询资讯列表]
	 * 创建人：LG
	 * 创建时间：2016年6月12日 20：48
	 */
    List<TomNews> selectByList(Map<Object, Object> map);
    /**
	 * 功能介绍：[分页查询资讯条数]
	 * 创建人：LG
	 * 创建时间：2016年6月12日 20：48
	 */
    int selectBycount(Map<Object, Object> map);
    /**
     * 功能介绍：[根据咨询ID查询发布状态]
     * 创建人:LG
     * 创建时间：2016年6月13日 16：20
     */
    TomNews selectByReleaser(Map<Object, Object> map);
    /**
     * 功能介绍:[修改发布状态]
     * 创建人：LG
     * 创建时间：2016年6月14日 10：17
     */
    int updateByReleaser(TomNews tomnews);
    /**
     * 功能介绍：[根据ID查询信息]
     * 创建人：LG
     * 创建时间：2016年6月20日 15：24
     */
    TomNews selectById(Integer newId);
    
    int selectBycountForRooling(Map<Object, Object> map);
    
    List<TomNews> selectByListForRoolling(Map<Object, Object> map);
}