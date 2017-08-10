package com.chinamobo.ue.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	private static String host=Config.getProperty("redisHost");
	private static String auth=Config.getProperty("redisAuth");
	private static int port=Integer.valueOf(Config.getProperty("redisPort"));
	//可用连接实例的最大数目，默认值为8；
    private static int MAX_ACTIVE = -1;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 10;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。
    private static int MAX_WAIT = 60*1000;
    
    private static int TIMEOUT = 60*1000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    private Jedis jedis = null;
    
    /** 
     * 初始化Redis连接池 
     */  
    private static void initialPool(){  
        try {  
            JedisPoolConfig config = new JedisPoolConfig();  
            config.setMaxTotal(MAX_ACTIVE);  
            config.setMaxIdle(MAX_IDLE);  
            config.setMaxWaitMillis(MAX_WAIT);  
            config.setTestWhileIdle(true);
            config.setTimeBetweenEvictionRunsMillis(200);
            config.setTestOnBorrow(TEST_ON_BORROW);  
            config.setTestOnReturn(true);
            jedisPool = new JedisPool(config,host, port, TIMEOUT,auth);
            //jedisPool = new JedisPool(config,"localhost",6379, TIMEOUT);  
        } catch(Exception e2){  
              	e2.printStackTrace();
            }  
    } 
    
    /** 
     * 在多线程环境同步初始化 
     */  
    private static void poolInit() {//双重锁同步单列;
        if (jedisPool == null) { 
        	synchronized (RedisUtils.class) {
				if(jedisPool == null){
					initialPool();  
				}
			}
        }  
    } 
    /** 
     * 同步获取Jedis实例 
     * @return Jedis 
     */  
    public static Jedis getJedis() {    
        if (jedisPool == null) {    
            poolInit();  
        }  
        Jedis jedis = null;  
        try { 
        	
            if (jedisPool != null) {    
                jedis = jedisPool.getResource();   
            }  
        } catch (Exception e) {   
        	System.out.println("wite"+jedisPool.getNumWaiters());
        	System.out.println("active"+jedisPool.getNumActive());
            e.printStackTrace();
        } 
        return jedis;  
    } 
    
    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }
    
    public static void returnBrokenResoutce(final Jedis jedis){
    	if(jedis !=null){
    		jedisPool.returnBrokenResource(jedis);
    	}
    }
    
    /**
     */
    public  static void hset(String key,String field,String value){
    	Jedis jedis=null;
    	try{
    		jedis=getJedis();
    		jedis.hset(key, field, value);
    	}catch(Exception e){
    		if(jedis!=null){
    			returnBrokenResoutce(jedis);
    		}
    		e.printStackTrace();
    	}finally{  
    		if(jedis!=null){
    			jedis.close();
    		}
        } 
    }
    public static void set(String key ,List<?> datas){
    	Jedis jedis = null;
    	try{
	    	if(datas != null && datas.size() > 0){
	    	 jedis = getJedis();
	    		for(Object data : datas){
	        		jedis.lpush(key, JSONObject.toJSONString(data));
	    		}
	    	}
    	}catch(Exception e){
    		if(jedis!=null){
    			returnBrokenResoutce(jedis);
    		}
    		e.printStackTrace();
    	}finally{  
    		if(jedis!=null){
    			jedis.close();
    		}
        } 
    }
    public static <T> List<T> get(String key,Class<T> clazz){
    	Jedis jedis = null;
    	List<T> returnDatas = null;
    	try{
    		jedis = getJedis();
	    	List<String> datas = jedis.lrange(key, 0,-1);
	    	if(datas != null && datas.size() >0){
	    		returnDatas = new ArrayList<T>();
	    		for(String data : datas){
	    			T obj = (T)JSONObject.parseObject(data, clazz);
	    			returnDatas.add(obj);
	    		}
	    	}
    	}catch(Exception e){
    		if(jedis!=null){
    			returnBrokenResoutce(jedis);
    		}
    		e.printStackTrace();
    	}finally{  
    		if(jedis!=null){
    			jedis.close();
    		}
        } 
    	return returnDatas;
    }
    public  static String hget(String key,String field){
    	Jedis jedis=null;
    	String res=null;
    	try{
    		jedis=getJedis();
    		res=jedis.hget(key, field);
    		if(jedisPool.getNumActive()>100){
    			System.out.println(jedisPool.getNumActive());
    		}
    	}catch(Exception e){
    		if(jedis!=null){
    			returnBrokenResoutce(jedis);
    		}
    		e.printStackTrace();
    	}finally{  
    		if(jedis!=null){
    			jedis.close();
    		}
        } 
    	 return res;
    }
    public static void clear(){
    	Jedis jedis=null;
        try { 
        	jedis=getJedis();
        	Set<String> keys = jedis.keys("*");
        	for(String key :keys){
        		jedis.del(key);
        	}
        } catch (Exception e) { 
        	if(jedis!=null){
        		returnBrokenResoutce(jedis);
    		}
        	e.printStackTrace();
        }  finally{  
    		if(jedis!=null)
    			 jedis.close();
        } 
    }
    public static Long del(String key){
    	Jedis jedis=null;
        try { 
        	jedis=getJedis();
        	return jedis.del(key);
        }catch (Exception e) { 
        	if(jedis!=null){
        		returnBrokenResoutce(jedis);
    		}
        	e.printStackTrace();
        }finally{  
    		if(jedis!=null)
    			 jedis.close();
       }
        return Long.valueOf(0);
    }
    public  static void setString(String key ,String value){  
    	Jedis jedis=null;
        try {  
        	jedis=getJedis();
            value = StringUtil.isEmpty(value) ? "" : value;  
            jedis.set(key,value);  
        } catch (Exception e) { 
        	if(jedis!=null){
        		returnBrokenResoutce(jedis);
    		}
        	e.printStackTrace();
        }  finally{  
    		if(jedis!=null)
    			 jedis.close();
            } 
    }
    public  static String getString(String key){ 
    	Jedis jedis=null;
    	String res=null;
    	try{
    		jedis=getJedis();
    		res=jedis.get(key);
    	}catch(Exception e){
    		if(jedis!=null){
    			returnBrokenResoutce(jedis);
    		}
    		e.printStackTrace();
    	}finally{  
    		if(jedis!=null)
               jedis.close();
            } 
    	 return res;
    }
}
