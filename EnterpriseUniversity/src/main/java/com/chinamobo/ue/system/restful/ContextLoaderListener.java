package com.chinamobo.ue.system.restful;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.chinamobo.ue.system.service.ContextInitRedisService;
import com.chinamobo.ue.system.service.ContextInitService;
import com.chinamobo.ue.ums.util.ApplicationContextUtils;

public class ContextLoaderListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);
	private static XmlWebApplicationContext applicationContext;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		applicationContext = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(context);
		ApplicationContextUtils.init(applicationContext);
		context.setAttribute("startupDate", System.currentTimeMillis());
		logger.info("初始化数据...");
		applicationContext.getBean(ContextInitService.class).init();
		applicationContext.getBean(ContextInitRedisService.class).init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.error("系统关闭");
	}
	
	public static XmlWebApplicationContext getApplicationContext(){
		return applicationContext;
	}

}
