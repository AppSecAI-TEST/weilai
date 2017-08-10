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

import com.chinamobo.ue.reportforms.dto.LearningDeptExamDto;
import com.chinamobo.ue.reportforms.service.LearningDeptExamService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/learningDeptExam") 
@Scope("request") 
@Component
public class LearningDeptExamRest {
	@Autowired
	private LearningDeptExamService learningDeptExamService;
	ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/learningDeptExamList")
	public String learningDeptExamList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize, @QueryParam("examTotalTime") String examTotalTime,
			@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("needApply") String needApply,
			@QueryParam("method6") Integer activityId, @QueryParam("activityIn") String activityIn,
			@QueryParam("method5") Integer projectId, @QueryParam("examName") String examName, @QueryParam("method1") String orgcode,
			@QueryParam("method2") String onedeptcode, @QueryParam("method3") String deptcode2, @QueryParam("method4") String deptcode3){
		try{
			String json= mapper.writeValueAsString(learningDeptExamService.learningDeptExamList(pageNum, pageSize, examTotalTime, code, name, needApply, activityId, activityIn, projectId, examName, orgcode, onedeptcode, deptcode2, deptcode3,null));
			return json;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	//导出
	@GET
    @Path("/downloadLearningDeptExamExcel")
	public Response downloadLearningDeptExamExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@Context HttpServletRequest request,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize, @QueryParam("examTotalTime") String examTotalTime,
			@QueryParam("code") String code, @QueryParam("name") String name, @QueryParam("needApply") String needApply,
			@QueryParam("method6") Integer activityId, @QueryParam("activityIn") String activityIn,
			@QueryParam("method5") Integer projectId, @QueryParam("examName") String examName, @QueryParam("method1") String orgcode,
			@QueryParam("method2") String onedeptcode, @QueryParam("method3") String deptcode2, @QueryParam("method4") String deptcode3,@QueryParam("queryColumnStr") String queryColumnStr){
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<LearningDeptExamDto> learningDeptExam = learningDeptExamService.learningDeptExamList(pageNum, pageSize, examTotalTime, code, name, needApply, activityId, activityIn, projectId, examName, orgcode, onedeptcode, deptcode2, deptcode3,queryColumnStr).getData();
			String fileName = "学员考试报表.xls";
			String path = learningDeptExamService.learningDeptExamExcel(learningDeptExam, fileName,queryColumnStr);
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
			response.header("Content-Disposition","attachment; filename=\""+downFileName+"\"");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie learningDeptExamCookie = new NewCookie("learningDeptExam","ok","/",null,null,1,false);
			response.cookie(learningDeptExamCookie);
	        return response.build();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
}
