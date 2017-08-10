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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.reportforms.dto.EmpActivityDetaListDto;
import com.chinamobo.ue.reportforms.dto.StuCourseRankingDto;
import com.chinamobo.ue.reportforms.service.StuCourseRankingService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;

@Path("/stuCourseRanking")
@Scope("request")
@Component
public class StuCourseRankingRest {
	
	@Autowired
	private StuCourseRankingService stuCourseRankingServce;
	
	//学员活动，学时排行
	@GET
	@Path("/stuCourseRankingListStudyTime")
	public String stuCourseRankingListStudyTime(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(stuCourseRankingServce.stuCourseRankingTimerFrom(pageNum, pageSize,beginTimeq,endTimeq,orgName,oneDeptName,twoDeptName,threeDeptName));
	}
	
	@GET
	@Path("/stuCourseRankingListStudyTimeExcel")
	public Response stuCourseRankingListStudyTimeExcel(@Context HttpServletRequest request,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<StuCourseRankingDto> stuCourseRankingServicesList = stuCourseRankingServce.stuCourseRankingTimerFromList(pageNum, pageSize,beginTimeq, endTimeq,
					orgName,oneDeptName,twoDeptName,threeDeptName);
			String fileName = "排行-学习时长.xls";
			String path = stuCourseRankingServce.stuCourseRankingListStudyTimeExcel(stuCourseRankingServicesList, fileName);
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
		}
	
	
	
	//学员活动，学分排行
	@GET
	@Path("/stuCourseRankingListScore")
	public String stuCourseRankingListScore(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(stuCourseRankingServce.stuCourseRankingScoreFrom(pageNum, pageSize,beginTimeq,endTimeq,orgName,oneDeptName,twoDeptName,threeDeptName));
	}
	
	@GET
	@Path("/stuCourseRankingListScoreExcel")
	public Response stuCourseRankingListScoreExcel(@Context HttpServletRequest request,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<StuCourseRankingDto> stuCourseRankingServicesList = stuCourseRankingServce.stuCourseRankingScoreFromList(pageNum, pageSize,beginTimeq, endTimeq,
					orgName,oneDeptName,twoDeptName,threeDeptName);
			String fileName = "排行-学分.xls";
			String path = stuCourseRankingServce.stuCourseRankingScoreFromListExcel(stuCourseRankingServicesList, fileName);
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
		}
	
	
	//学员活动，课程评分排行
	@GET
	@Path("/stuCourseRankingListCourseScore")
	public String stuCourseRankingListCourseScore(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(stuCourseRankingServce.stuCourseRankingCourseScoreFrom(pageNum, pageSize,beginTimeq,endTimeq,orgName,oneDeptName,twoDeptName,threeDeptName));
	}
	
	@GET
	@Path("/stuCourseRankingCourseScoreExcel")
	public Response stuCourseRankingCourseScoreExcel(@Context HttpServletRequest request,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<StuCourseRankingDto> stuCourseRankingServicesList = stuCourseRankingServce.stuCourseRankingCourseScoreFromList(pageNum, pageSize,beginTimeq, endTimeq,
					orgName,oneDeptName,twoDeptName,threeDeptName);
			String fileName = "排行-课程评分.xls";
			String path = stuCourseRankingServce.stuCourseRankingCourseScoreFromListExcel(stuCourseRankingServicesList, fileName);
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
		}
	
}
