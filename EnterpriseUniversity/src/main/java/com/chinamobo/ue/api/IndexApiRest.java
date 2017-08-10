package com.chinamobo.ue.api;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.api.activity.service.ActivityApiService;
import com.chinamobo.ue.api.commodity.service.ApiCommodityService;
import com.chinamobo.ue.api.course.service.ApiCourseService;
import com.chinamobo.ue.api.exam.service.ExamApiService;
import com.chinamobo.ue.api.global.service.ApiGlobalService;
import com.chinamobo.ue.api.message.service.MessageApiService;
import com.chinamobo.ue.api.myInfo.service.MyInfoApiService;
import com.chinamobo.ue.api.qcloud.service.ApiQcloudService;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.upload.UploadService;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.api.weChat.WeChatApiService;
import com.chinamobo.ue.system.dao.TomActiveUserMapper;
import com.chinamobo.ue.system.entity.TomActiveUser;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.PathUtil;

import net.sf.json.JSONObject;


@Component
@Path("/api")
@Scope("request")
public class IndexApiRest {
	private static Logger logger = LoggerFactory.getLogger(IndexApiRest.class);
	@Autowired
	private ExamApiService examService;
	@Autowired
	private ApiCourseService courseService;
	@Autowired
	private ActivityApiService activityService;
	@Autowired
	private ApiGlobalService apiGlobalService;
	@Autowired
	private ApiCommodityService commodityService;
	@Autowired
	private ApiQcloudService qcloudService;
	@Autowired
	private MyInfoApiService myInfoApiService;
	@Autowired
	private MessageApiService messageApiService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private TomActiveUserMapper activeUserMapper;
	@Autowired
	private WeChatApiService weChatApiService;
	/**
	 * 
	 * 功能描述：[接口get入口]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月17日 下午4:45:56
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@Path("/getMethod")
	@Consumes(MediaType.APPLICATION_JSON)  
    @Produces(MediaType.APPLICATION_JSON)  
	public String getMethod(@Context HttpServletRequest request,@Context HttpServletResponse response){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Result result=new Result();
		JsonMapper jsonMapper=new JsonMapper();
		String apiName=PathUtil.camelName(request.getParameter("apiName"));
		String apiType=request.getParameter("apiType");
		String callback=request.getParameter("callback");
		String userId=request.getParameter("userId");
		String token=request.getParameter("token");
		if(userId!=null && !"".equals(userId)){
			TomUserLog userLog=systemService.getUserbyCode(userId);
			if(userLog==null){
				return callback+"("+jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.USER_ILLEGAL),"用户不存在"))+")";
			}
			if(!userLog.getToken().equals(token)){
				return callback+"("+jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SIGN_ILLEGAL),"token不匹配"))+")";
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			map.put("code", userId);
			map.put("date", simple.format(new Date()));
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			List<TomActiveUser> acUser=activeUserMapper.select(map);
			if(acUser.size()<=1){
				
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(new Date());
				activeUserMapper.insert(us);
				
			}else if(acUser.size()==2){
				
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(acUser.get(acUser.size()-1).getCreateDate());
				us.setId(acUser.get(acUser.size()-1).getId());
				activeUserMapper.update(us);
			}else{
				
				for (int i = 0; i < acUser.size(); i++) {
					if (i!=acUser.size()-1 || i!=0) {
						activeUserMapper.delete(acUser.get(i).getId());
					}
				}
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(acUser.get(acUser.size()-1).getCreateDate());
				us.setId(acUser.get(acUser.size()-1).getId());
				activeUserMapper.update(us);
			}
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
		}
		Class clazz=null;
		Method method=null;
		try {
			if("course".equals(apiType)){
				clazz=courseService.getClass();
				method=clazz.getDeclaredMethod(apiName,new Class[]{HttpServletRequest.class,HttpServletResponse.class});
	 			result=(Result)method.invoke(courseService,request,response);
			}else if("exam".equals(apiType)){
				clazz=examService.getClass();
				method=clazz.getDeclaredMethod(apiName,new Class[]{HttpServletRequest.class,HttpServletResponse.class});
	 			result=(Result)method.invoke(examService,request,response);
			}else if("activity".equals(apiType)){
				clazz=activityService.getClass();
				method=clazz.getDeclaredMethod(apiName,new Class[]{HttpServletRequest.class,HttpServletResponse.class});
	 			result=(Result)method.invoke(activityService,request,response);
			}else if("global".equals(apiType)){
				clazz=apiGlobalService.getClass();
				method=clazz.getDeclaredMethod(apiName, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
				result=(Result)method.invoke(apiGlobalService,request,response);
			}else if("commodity".equals(apiType)){
				clazz=commodityService.getClass();
				method=clazz.getDeclaredMethod(apiName, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
				result=(Result)method.invoke(commodityService,request,response);
			}else if("qcloud".equals(apiType)){
				clazz=qcloudService.getClass();
				method=clazz.getDeclaredMethod(apiName, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
				result=(Result)method.invoke(qcloudService,request,response);
			}else if("myInfo".equals(apiType)){
				clazz=myInfoApiService.getClass();
				method=clazz.getDeclaredMethod(apiName, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
				result=(Result)method.invoke(myInfoApiService,request,response);
			}else if("message".equals(apiType)){
				clazz=messageApiService.getClass();
				method=clazz.getDeclaredMethod(apiName, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
				result=(Result)method.invoke(messageApiService,request,response);
			}else if("weChat".equals(apiType)){
				clazz=weChatApiService.getClass();
				method=clazz.getDeclaredMethod(apiName, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
				result=(Result)method.invoke(weChatApiService, request,response);
			}else{
				return callback+"("+jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),"apiType不正确"))+")";
			}
 			if("N".equals(result.getStatus())){
 				return callback+"("+jsonMapper.toJson(result)+")";
 			}else {
 				if("".equals(result.getResults())){
 					return callback+"({\"status\":\"Y\",\"results\":\"\"})";
 				}
 				return callback+"({\"status\":\"Y\",\"results\":"+result.getResults()+"})";
 			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return callback+"("+jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),apiName+"出现异常！"))+")";
//			return callback+"("+jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),e.getMessage()))+")";
		} 
	}
	/**
	 * 
	 * 功能描述：[接口post入口]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月17日 下午4:46:08
	 * @param jsonData
	 * @return
	 */
	@POST
	@Path("/postMethod")
//	@Consumes(MediaType.APPLICATION_JSON)  
//    @Produces(MediaType.APPLICATION_JSON)  
	public String postMethod(String jsonData,@Context HttpServletResponse response,@Context HttpServletRequest request){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Result result=new Result();
 		String userAgent=request.getHeader("User-Agent");
		JsonMapper jsonMapper=new JsonMapper();
		JSONObject jsonObject=JSONObject.fromObject(jsonData);
		String apiName=PathUtil.camelName(jsonObject.getString("apiName"));
		jsonObject.put("userAgent", userAgent);
		String apiType=jsonObject.getString("apiType");
		String userId=jsonObject.getString("userId");
		String token=jsonObject.getString("token");
		jsonData=jsonObject.toString();
		if(userId!=null && !"".equals(userId)){
			TomUserLog userLog=systemService.getUserbyCode(userId);
			if(userLog==null){
				return jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.USER_ILLEGAL),"用户不存在"));
			}
			if(!userLog.getToken().equals(token)){
				return jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SIGN_ILLEGAL),"token不匹配"));
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			map.put("code", userId);
			map.put("date", simple.format(new Date()));
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			List<TomActiveUser> acUser=activeUserMapper.select(map);
			if(acUser.size()<=1){
				
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(new Date());
				activeUserMapper.insert(us);
				
			}else if(acUser.size()==2){
				
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(acUser.get(acUser.size()-1).getCreateDate());
				us.setId(acUser.get(acUser.size()-1).getId());
				activeUserMapper.update(us);
			}else{
				
				for (int i = 0; i < acUser.size(); i++) {
					if (i!=acUser.size()-1 || i!=0) {
						activeUserMapper.delete(acUser.get(i).getId());
					}
				}
				TomActiveUser us=new TomActiveUser();
				us.setCode(userId);
				us.setCreateDate(acUser.get(acUser.size()-1).getCreateDate());
				us.setId(acUser.get(acUser.size()-1).getId());
				activeUserMapper.update(us);
			}
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
		}
		Class clazz=null;
		Method method=null;
		try {
			if("course".equals(apiType)){
				clazz=courseService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(courseService,jsonData);
			}else if("exam".equals(apiType)){
				clazz=examService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(examService,jsonData);
			}else if("activity".equals(apiType)){
				clazz=activityService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(activityService,jsonData);
			}else if("commodity".equals(apiType)){
				clazz=commodityService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(commodityService,jsonData);
			}else if("qcloud".equals(apiType)){
				clazz=qcloudService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(qcloudService,jsonData);
			}else if("myInfo".equals(apiType)){
				clazz=myInfoApiService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(myInfoApiService,jsonData);
			}else if("message".equals(apiType)){
				clazz=messageApiService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(messageApiService,jsonData);
			}else if("upload".equals(apiType)){
				clazz=uploadService.getClass();
				method=clazz.getMethod(apiName,String.class);
				result=(Result)method.invoke(uploadService,jsonData);
			}else{
				return jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),"apiType不正确"));
			}
 			if("N".equals(result.getStatus())){
 				return jsonMapper.toJson(result);
 			}else {
 				if("".equals(result.getResults())){
 					return "{\"status\":\"Y\",\"results\":\"\"}";
 				}
 				return "{\"status\":\"Y\",\"results\":"+result.getResults()+"}";
 			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return jsonMapper.toJson(new Result("N", "",String.valueOf(ErrorCode.SYSTEM_ERROR),apiName+"出现异常！"));
		} 
	}
}
