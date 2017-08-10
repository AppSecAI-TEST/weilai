/**
 * 
 */
package com.chinamobo.ue.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年6月14日 下午4:17:19
 */
public abstract class AbstractCacheManager implements CacheManager {

	protected List<Cacher> cachers = new ArrayList<Cacher>();
	/**
	 * 
		 * 功能描述：[初始化方法，由子类实现]
		 * 创建者：WChao 创建时间: 2017年6月14日 下午2:42:34
		 *
	 */
	public abstract void init()throws Exception;

	@Override
	public void registerCacher(Cacher cacher) {
		cachers.add(cacher);
		
	}

	@Override
	public void removeCacher(Cacher cacher) {
		cachers.remove(cacher);
		
	}
	public abstract Cacher getCacher(Class<?> clazz);
	
	public List<Cacher> getCachers() {
		return cachers;
	}

	public void setCachers(List<Cacher> cachers) {
		this.cachers = cachers;
	}
	
}
