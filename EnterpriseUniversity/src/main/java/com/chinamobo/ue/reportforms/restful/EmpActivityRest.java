package com.chinamobo.ue.reportforms.restful;

import java.io.File;
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

import com.chinamobo.ue.reportforms.dto.EmpActivityDto;
import com.chinamobo.ue.reportforms.dto.LearningDeptExamDto;
import com.chinamobo.ue.reportforms.service.EmpActivityService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/empActivity") 
@Scope("request") 
@Component
public class EmpActivityRest {
	@Autowired
	private EmpActivityService empActivityService;
	ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/empActivityList")
	public String learningDeptExamList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String startTime, @QueryParam("endTimeq") String endTime,
			@QueryParam("method5") Integer projectId,@QueryParam("isRequired") String isRequired,
			@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("method1") String orgcode,
			@QueryParam("method2") String onedeptcode, @QueryParam("method3") String deptcode2, @QueryParam("method4") String deptcode3){
		try{
			String json= mapper.writeValueAsString(empActivityService.empActivityList(pageNum, pageSize, startTime, endTime, projectId, isRequired, code, name, orgcode, onedeptcode, deptcode2, deptcode3,null));
			return json;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//导出
	@GET
    @Path("/downloadEmpActivityExcel")
	public Response downloadEmpActivityExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String startTime, @QueryParam("endTimeq") String endTime,
			@QueryParam("method5") Integer projectId,@QueryParam("isRequired") String isRequired,
			@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("method1") String orgcode,
			@QueryParam("method2") String onedeptcode, @QueryParam("method3") String deptcode2, @QueryParam("method4") String deptcode3,@QueryParam("queryColumnStr") String queryColumnStr){
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<EmpActivityDto> empActivity = empActivityService.empActivityList(pageNum, pageSize, startTime, endTime, projectId, isRequired, code, name, orgcode, onedeptcode, deptcode2, deptcode3 , queryColumnStr).getData();
			String fileName = "学员-组织统计.xls";
			String path = empActivityService.empActivityExcel(empActivity, fileName , queryColumnStr);
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
			response.header("Content-Disposition","attachment; filename=\""+downFileName+"\"");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie empActivityCookie = new NewCookie("empActivity","ok","/",null,null,1,false);
			response.cookie(empActivityCookie);
	        return response.build();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
