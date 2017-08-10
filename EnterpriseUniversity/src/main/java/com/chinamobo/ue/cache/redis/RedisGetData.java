/**
 * 
 */
package com.chinamobo.ue.cache.redis;

/**
 * 版本: [1.0]
 * 功能说明: 获取Redis数据模型;
 * 作者: WChao 创建时间: 2017年6月15日 下午4:11:25
 */
public class RedisGetData<T> extends RedisCacheData {
	private T type;
	public RedisGetData(String key, String field) {
		this.key = key;
		this.field = field;
	}
	
	public RedisGetData(String key) {
		this.field = key;
	}
	
	public RedisGetData(String key , T type) {
		this.field = key;
		this.type = type;
	}

	public T getType() {
		return type;
	}
	
}
