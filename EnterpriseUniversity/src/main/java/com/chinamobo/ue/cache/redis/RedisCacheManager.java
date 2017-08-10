/**
 * 
 */
package com.chinamobo.ue.cache.redis;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.utils.RedisUtils;

/**
 * 版本: [1.0]
 * 功能说明: Redis缓存管理器;
 *
 * 作者: WChao
 * 创建时间: 2017年6月14日 下午2:25:10
 */
@SuppressWarnings({"static-access"})
public class RedisCacheManager extends AbstractCacheManager {

	private RedisUtils client = null;//Redis缓存客户端;
	
	@Override
	public void init()throws Exception{
		this.client = new RedisUtils();
		this.client.getJedis();
		this.client.clear();//清空缓存数据;
		for(Cacher cacher : cachers){
			cacher.init();//初始化相关资源;
			cacher.doCache(this);//初始化模块Cache缓存信息;
		}
	}
	
	@Override
	public Cacher getCacher(Class<?> clazz) {
		for(Cacher cacher : cachers){
			if(cacher.getClass() == clazz){
				return cacher;
			}
		}
		return null;
	}
	public void hset(String key,String field,String value){
		this.client.hset(key, field, value);
	}
	public void set(String key ,List<?> datas){
		this.client.set(key, datas);
	}
	public  <T> List<T> get(String key,Class<T> clazz){
		return this.client.get(key, clazz);
	}
	public  String hget(String key,String field){
		 return this.client.hget(key, field);
	}
	public  void setString(String key ,String value){  
		 this.client.setString(key, value);
	}
	public  String getString(String key){
		 return this.client.getString(key);
	}
	public void clear(){
		this.client.clear();
	}
	public Long del(String key){
		return this.client.del(key);
	}
}
