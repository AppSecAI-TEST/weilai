/**
 * Project Name:enterpriseuniversity 
 * File Name:QueueManager.java 
 * Package Name:com.chinamobo.ue.api.queue
 * @author Acemon
 * Date:2017年7月28日上午9:55:29
 */
package com.chinamobo.ue.api.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.chinamobo.ue.utils.Config;

import jxl.common.Logger;

/**
 * ClassName:QueueManager
 * Function:TODO
 * @author Acemon
 * 2017年7月28日
 */
public class QueueManager {
	
	Logger logger = Logger.getLogger(QueueManager.class);
	private static QueueManager me;
	private List<Runnable> runnables = new ArrayList<Runnable>();
	private int thread_size = 1;
	private QueueManager(){};
	
	public static QueueManager me(){
		if(me == null){
			synchronized (QueueManager.class) {
				if(me == null){
					me = new QueueManager();
					me.setThread_size(Integer.parseInt(Config.getProperty("queue_thread_size", "1")));
				}
			}
		}
		return me;
	}
	public QueueManager addQueRunnable(Runnable runnable){
		runnables.add(runnable);
		return this;
	}
	@SuppressWarnings("unchecked")
	public <T> T getQueRunnable(Class<T> clazz){
		for(Runnable runnbale : runnables){
			if(runnbale.getClass() == clazz){
				
				return (T)runnbale;
			}
		}
		return null;
	}
	public void run(){
		logger.info("缓存队列启动...");
		int poolSize = runnables.size();
		if(poolSize > 0){
			for(int i = 0 ; i<poolSize ; i++){
				ThreadPoolExecutor pool = new ThreadPoolExecutor(thread_size, thread_size, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue());
				for(int index = 0 ;index<thread_size;index++){
					pool.execute(runnables.get(i));
				}
			}
		}
	}

	public void setThread_size(int thread_size) {
		this.thread_size = thread_size;
	}
	
}
