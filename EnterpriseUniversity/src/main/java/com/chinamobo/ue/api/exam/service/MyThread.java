/**
 * Project Name:enterpriseuniversity 
 * File Name:MyThread.java 
 * Package Name:com.chinamobo.ue.api.exam.service
 * @author Acemon
 * Date:2017年7月31日上午11:50:14
 */
package com.chinamobo.ue.api.exam.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONObject;
import com.chinamobo.ue.api.exam.service.ExamEntity.Topic;

/**
 * ClassName:MyThread
 * Function:TODO
 * @author Acemon
 * 2017年7月31日
 */
public class MyThread implements Runnable{
	
	private String token;
	private String userId;
	public MyThread(String userId,String token){
		this.token = token;
		this.userId = userId;
	}
	@Override
	public void run() {
		ExamEntity entity = new ExamEntity();
		entity.setApiName("ELE_EXAM_SUBMIT");
		entity.setApiType("exam");
		entity.setExamId("262");
		entity.setExamTotalTime("20");
		entity.setToken(token);
		entity.setUserId(userId);
		List<Topic> topics = new ArrayList<Topic>();
		topics.add(new ExamEntity().new Topic(188,474));
		topics.add(new ExamEntity().new Topic(189,479));
		topics.add(new ExamEntity().new Topic(190,483));
		topics.add(new ExamEntity().new Topic(191,485));
		topics.add(new ExamEntity().new Topic(192,490));
		topics.add(new ExamEntity().new Topic(195,504));
		topics.add(new ExamEntity().new Topic(196,507));
		topics.add(new ExamEntity().new Topic(189,479));
		topics.add(new ExamEntity().new Topic(242,594));
		topics.add(new ExamEntity().new Topic(243,598));
		topics.add(new ExamEntity().new Topic(244,602));
		topics.add(new ExamEntity().new Topic(245,607));
		topics.add(new ExamEntity().new Topic(246,612));
		topics.add(new ExamEntity().new Topic(247,614));
		topics.add(new ExamEntity().new Topic(248,618));
		topics.add(new ExamEntity().new Topic(249,622));
		topics.add(new ExamEntity().new Topic(250,626));
		topics.add(new ExamEntity().new Topic(223,557));
		topics.add(new ExamEntity().new Topic(224,559));
		topics.add(new ExamEntity().new Topic(225,560));
		topics.add(new ExamEntity().new Topic(226,563));
		topics.add(new ExamEntity().new Topic(227,564));
		topics.add(new ExamEntity().new Topic(228,566));
		topics.add(new ExamEntity().new Topic(229,568));
		topics.add(new ExamEntity().new Topic(230,571));
		entity.setExamResult(topics);
		System.out.println(JSONObject.toJSONString(entity));
        //建立一个NameValuePair数组，用于存储欲传递的参数
         HttpClient client = new HttpClient();
        try {
            //设置编码
            //发送Post,并返回一个HttpResponse对象  
            PostMethod post = new PostMethod("http://localhost:8080/enterpriseuniversity/services/api/postMethod?token="+token);
            post.setRequestEntity(new StringRequestEntity(JSONObject.toJSONString(entity)));
            post.addRequestHeader("userAgent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");
            int ss = client.executeMethod(post);
        }catch(Exception e){
        	e.printStackTrace();
        }
	}

}
