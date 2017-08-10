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
import com.chinamobo.ue.reportforms.dto.EmpActivityDetaListDto;
import com.chinamobo.ue.reportforms.service.EmpActivityDetaListService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/empActivityDetail")
@Scope("request")
@Component
public class EmpActivityDetaListRest {
	
	@Autowired
	private EmpActivityDetaListService  empActivityDetaListService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	
	@GET
	@Path("/empActivityDetailList")
	public String empActivity(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize, @QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examTotalTime") String examTotalTime,
			@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("needApply") String needApply,
			@QueryParam("activityName") String activityName, @QueryParam("method6") Integer activityId, @QueryParam("activityIn") String activityIn,
			@QueryParam("method5") Integer projectId, @QueryParam("examName") String examName, @QueryParam("method1") String orgcode,
			@QueryParam("method2") String onedeptcode, @QueryParam("method3") String deptcode2, @QueryParam("method4") String deptcode3) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(empActivityDetaListService.empActivityDetaListFrom1(pageNum, pageSize,beginTimeq, endTimeq,examTotalTime, code,
				name, needApply, activityName, activityId, activityIn, projectId, examName, orgcode, onedeptcode, deptcode2, deptcode3));
		
	}
	
	@GET
	@Path("/empActivityDetailExcel")
	public Response empActivityDetailExcel(@Context HttpServletRequest request,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize, @QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examTotalTime") String examTotalTime,
			@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("needApply") String needApply,
			@QueryParam("activityName") String activityName, @QueryParam("method6") Integer activityId, @QueryParam("activityIn") String activityIn,
			@QueryParam("method5") Integer projectId, @QueryParam("examName") String examName, @QueryParam("method1") String orgcode,
			@QueryParam("method2") String onedeptcode, @QueryParam("method3") String deptcode2, @QueryParam("method4") String deptcode3){
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<EmpActivityDetaListDto> empActivityDeplist = empActivityDetaListService.queryEmpActivityDeptlist(pageNum, pageSize,beginTimeq, endTimeq,examTotalTime, code,
					name, needApply, activityName, activityId, activityIn, projectId, examName, orgcode, onedeptcode, deptcode2, deptcode3);
			String fileName = "学员活动报表.xls";
			String path = empActivityDetaListService.EmpActivityDeptlistExcel(empActivityDeplist, fileName);
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
			NewCookie empActivityDetaCookie = new NewCookie("empActivityDeta","ok","/",null,null,1,false);
			response.cookie(empActivityDetaCookie);
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
/*	public Response empActivityDetailExcel(@Context HttpServletRequest request,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("code") String code,@QueryParam("method6") Integer activityId){
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<EmpActivityDetaListDto> empActivityDeplist = empActivityDetaListService.queryEmpActivityDeptlist(pageNum, pageSize,code,activityId);
			String fileName = "学员报表-活动详情.xls";
			String path = empActivityDetaListService.EmpActivityDeptlistExcel(empActivityDeplist, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName = "";
			String userAgent = request.getHeader("USER-AGENT");
			if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
				downFileName=new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else {
				downFileName=URLEncoder.encode(fileName,"UTF8");
			}
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}*/
}
