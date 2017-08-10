/**
 * Project Name:enterpriseuniversity 
 * File Name:ExamRunnable.java 
 * Package Name:com.chinamobo.ue.api.queue
 * @author Acemon
 * Date:2017年7月28日下午3:03:30
 */
package com.chinamobo.ue.api.queue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.api.exam.service.ExamApiService;
import com.chinamobo.ue.api.exam.service.ExamBean;
import com.chinamobo.ue.system.restful.ContextLoaderListener;
import com.chinamobo.ue.utils.Config;
import com.xiaoleilu.hutool.io.FileUtil;
/**
 * ClassName:ExamRunnable
 * Function:TODO
 * @author Acemon
 * 2017年7月28日
 */
@Service
public class ExamRunnable implements Runnable {
	
	Logger logger = Logger.getLogger(ExamRunnable.class);
	private ConcurrentLinkedQueue<ExamBean> examQueue = new ConcurrentLinkedQueue<ExamBean>();
	private Map<String,ExamBean> empExamInfos = new HashMap<String,ExamBean>();
	private String filePath = Config.getProperty("error_dir")+File.separator+"api"+File.separator+"exam"+File.separator;
	@Autowired
	private ExamApiService examApiService = (ExamApiService)ContextLoaderListener.getApplicationContext().getBean("examApiService");
	public void addExamBean(ExamBean examBean){
		this.examQueue.offer(examBean);
		empExamInfos.put(examBean.getExamScore().getCode()+"_"+examBean.getExamScore().getExamId(), examBean);
	}
	@Override
	public void run() {
		while(true){
			ExamBean examBean= examQueue.poll();
			if(examBean != null){
				String data = examBean.getData().toJSONString();
				String name = examBean.getExamScore().getEmpName();
				try {
					long start = System.currentTimeMillis();
					System.out.println(name+"->开始入库..");
					examApiService.eleExamSubmitTwo(data);
					System.out.println(name+"->入库完毕,耗时:"+(System.currentTimeMillis()-start)+"毫秒!");
				} catch (Exception e) {
					System.out.println(name+"入库出现异常");
					e.printStackTrace();
				}finally{
					File file = FileUtil.touch(filePath+examBean.getExamScore().getExamId()+".json");
					try {
						data += "\n";
						byte[] dataBytes = data.getBytes("utf-8");
						FileUtil.writeBytes(dataBytes,file,0,dataBytes.length,true);
					}catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
					} 
				}
			}else{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
	}
	public Logger getLogger() {
		return logger;
	}
	public ConcurrentLinkedQueue<ExamBean> getExamQueue() {
		return examQueue;
	}
	public Map<String, ExamBean> getEmpExamInfos() {
		return empExamInfos;
	}
	public ExamApiService getExamApiService() {
		return examApiService;
	}
	
}
