package com.chinamobo.ue.reportforms.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;
import com.chinamobo.ue.reportforms.service.ActivityDeptListService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/activityDept")
@Scope("request")
@Component
public class ActivityDeptListRest {
	
	@Autowired
	private ActivityDeptListService  activityDeptListService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	@GET
	@Path("/activityDeptList")
	public String offlineCourseStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("activityType") String activityType,@QueryParam("activityName") String activityName,@QueryParam("status") String status,@QueryParam("isRequired") String isRequired,
			@QueryParam("orgname") String orgname,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(activityDeptListService.activityDeplistFrom2(pageNum, pageSize,beginTimeq,endTimeq,activityType,activityName,status,isRequired,orgname,oneDeptName,twoDeptName,threeDeptName));
	}
	
	@GET
	@Path("/downloadActivityDeptListExcel")
	public Response downloadActivityDeptListExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@Context HttpServletRequest request,
			@QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("activityType") String activityType,@QueryParam("activityName") String activityName,@QueryParam("status") String status,@QueryParam("isRequired") String isRequired,
			@QueryParam("orgname") String orgname,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<ActivityDeptListDto> activityDeplist = activityDeptListService.queryActivityDeptlistServiceByCodes(pageNum, pageSize,beginTimeq,endTimeq,activityType,activityName,status,isRequired,orgname,oneDeptName,twoDeptName,threeDeptName);
			String fileName = "部门-活动统计.xls";
			String path = activityDeptListService.ActivityDeptlistExcel(activityDeplist, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName = "";
			String userAgent = request.getHeader("USER-AGENT");
			if(userAgent.indexOf("Firefox")!=-1){
				downFileName=new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else {
				downFileName=URLEncoder.encode(fileName,"UTF8");
			}
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			response.encoding("utf-8");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie activityDeptCookie = new NewCookie("activityDept","ok","/",null,null,1,false);
			response.cookie(activityDeptCookie);
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
}
