package com.chinamobo.ue.statistics.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.chinamobo.ue.course.dto.TomEmpCourseDto;

import com.chinamobo.ue.course.dto.TomNeedLearnDto;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.restful.CourseRest;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.statistics.entity.TomAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomDetailedAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomOpenCourseStatistic;
import com.chinamobo.ue.statistics.service.CourseStatisticsService;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;

@Path("/attendanceStatistics")
@Scope("request") 
@Component
public class CourseStatisticsRest {
	private static Logger logger = LoggerFactory.getLogger(CourseRest.class);
	@Autowired
	private CourseStatisticsService attendanceStatisticsService;
	
	@Autowired
	private CourseService courseService;
	ObjectMapper mapper = new ObjectMapper(); 
	
	@Autowired
	private EmpServise empServise;
	/**
	 * 
	 * 功能描述：[活动内课程统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月30日
	 * @param pageNum
	 * @param pageSize
	 * @param attendanceStatistics
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/attendanceStatisticsList")
	public String attendanceStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName,
			@QueryParam("courseOnline") String courseOnline,@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(attendanceStatisticsService.queryAttendanceStatistics(pageNum, pageSize, courseName,courseOnline,beginTimeq,endTimeq));
	}
	
	/**
     * 
     * 功能描述：[生成课程统计并下载]
     *
     * 创建者：JCX
     * 创建时间: 2016年5月30日
     * @param
     * @return
	 * @throws Exception 
     */
	@GET
    @Path("/downloadAttendanceStatisticsExcel")
	public Response downloadAttendanceStatisticsExcel(@QueryParam("codes") String codes,@QueryParam("activeId") String activeIds,@Context HttpServletRequest request) throws Exception{
		try{
			/*List<TomAttendanceStatistics> attendanceStatistics = attendanceStatisticsService.queryAttendanceStatistics(pageNum, pageSize, courseName, courseOnline,beginTimeq,endTimeq).getData();*/
			
			List<TomAttendanceStatistics> attendanceStatistics = attendanceStatisticsService.queryAttendanceBycodes(codes.split(","),activeIds.split(","));
			String fileName = "活动课程.xls";
			String path = attendanceStatisticsService.AttendanceStatisticsExcel(attendanceStatistics, fileName);
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
	/**
	 * 
	 * 功能描述：[查看课程学习统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @param pageNum
	 * @param pageSize
	 * @param
	 * @return
	 */
	@GET
	@Path("/seeAttendanceStatistics")
	public String seeAttendanceStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@QueryParam("courseId") int courseId){
		String json;
		try {
			json = mapper.writeValueAsString(attendanceStatisticsService.seeAttendanceStatistics(pageNum,pageSize,activityId,courseId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
     * 
     * 功能描述：[生成课程详细统计并下载]
     *
     * 创建者：GW
     * 创建时间: 2016年6月3日
     * @param
     * @return
     */
	@GET
    @Path("/downloadAttendanceStatisticsDetailedExcel")
	public Response downloadAttendanceStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics = attendanceStatisticsService.seeAttendanceStatistics(pageNum,pageSize, activityId, courseId).getData();
			TomCourses courses = courseService.selectCourseById(courseId);
			String fileName = "《"+courses.getCourseName()+"》统计.xls";
			String path = attendanceStatisticsService.AttendanceStatisticsDetailedExcel(detailedAttendanceStatistics, fileName);
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
	
	/**
	 * 
	 * 功能描述：[公开课统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 上午11:25:57
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/openCourseStatisticsList")
	public String openCourseStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(attendanceStatisticsService.openCourseStatistics(pageNum, pageSize, courseName));
	}
	/**
	 * 
	 * 功能描述：[线下课统计]
	 *
	 * 创建者：LYM
	 * 创建时间: 2017年1月16日 上午11:25:57
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/offlineCourseStatisticsList")
	public String offlineCourseStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("10") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(attendanceStatisticsService.offlineCourseStatistics(pageNum, pageSize, courseName));
	}
	/**
	 * 
	 * 功能描述：[公开课学习信息查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:28:12
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	@GET
	@Path("/openCourseLearnStatistics")
	public String openCourseLearnStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		String json;
		try {
			json = mapper.writeValueAsString(attendanceStatisticsService.openCourseLearnStatistics(pageNum,pageSize,courseId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	@GET
	@Path("/offlineCourseSignStatistics")
	public String offlineCourseSignStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		String json;
		try {
			json = mapper.writeValueAsString(attendanceStatisticsService.openCourseLearnStatistics(pageNum,pageSize,courseId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[生成公开课统计并下载]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:50:20
	 * @return
	 * @throws Exception 
	 */
	@GET
    @Path("/downloadOpenCourseExcel")
	public Response downloadOpenCourseExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName,@Context HttpServletRequest request) throws Exception{
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<TomOpenCourseStatistic> openCourseStatistics = attendanceStatisticsService.openCourseStatistics(pageNum, pageSize, courseName).getData();
			String fileName = "线上课程.xls";
			String path = attendanceStatisticsService.openCourseStatisticsExcel(openCourseStatistics, fileName);
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
	@GET
    @Path("/downloadOfflineCourseExcel")
	public Response downloadOfflineCourseExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseName") String courseName,@Context HttpServletRequest request) throws Exception{
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<TomOpenCourseStatistic> openCourseStatistics = attendanceStatisticsService.offlineCourseStatistics(pageNum, pageSize, courseName).getData();
			String fileName = "线下课程统计.xls";
			String path = attendanceStatisticsService.offlineCourseStatisticsExcel(openCourseStatistics, fileName);
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
	/**
	 * 
	 * 功能描述：[公开课学习记录导出]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午1:18:02
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	@GET
    @Path("/downloadOpenCourseLearnExcel")
	public Response downloadOpenCourseLearnExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try{
			List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics = attendanceStatisticsService.openCourseLearnStatistics(pageNum, pageSize, courseId).getData();
			TomCourses courses = courseService.selectCourseById(courseId);
			String fileName = "《"+courses.getCourseName()+"》完成人员统计.xls";
			String path = attendanceStatisticsService.openCourseLearnExcel(detailedAttendanceStatistics, fileName);
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
	@GET
	@Path("/findempidPage")
	public String findCourseforempidByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("code") String code){
		try {
			//TomCourseEmpRelation emprel = new TomCourseEmpRelation();
			String json= mapper.writeValueAsString(attendanceStatisticsService.selectempidByPage(pageNum, pageSize,code));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}	
		return "{\"result\":\"error\"}";
	}
	@GET
    @Path("/downloadEmpCourseStatisticsDetailedExcel")
	public Response downloadEmpCourseStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("code") String code,@Context HttpServletRequest request){
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<TomEmpCourseDto> courseList = attendanceStatisticsService.selectempidByPage(pageNum, pageSize,code).getData();
			TomEmp emp = empServise.selectByCode(code);
			String fileName = "学员《"+emp.getName()+"》课程统计.xls";
			String path = attendanceStatisticsService.empCourseStatisticsDetailedExcel(courseList, fileName);
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
	/**
	 * 功能描述:[查询应该学习课程人员名单]
	 * 创建时间：2017-01-10
	 */
	@GET
    @Path("/viewCourseNeedStatistics")
	public String viewCourseNeedStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewCourseNeedStatistics(pageNum, pageSize, courseId));
		
		return json;
		
	}
	/**
	 * 功能描述:[查询应该学习课程人员名单]
	 * 创建时间：2017-01-10
	 */
	@GET
    @Path("/viewCourseCanlearnStatistics")
	public String viewCourseCanlearnStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewCourseCanlearnStatistics(pageNum, pageSize, courseId));
		
		return json;
		
	}
	@GET
    @Path("/viewCourseSignStatistics")
	public String viewCourseSignStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@QueryParam("topDept")String topDept){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewCourseSignStatistics(pageNum, pageSize, courseId,topDept));
		
		return json;
		
	}
	/**
	 * 功能描述：[导出应该学习课程人员名单]
	 * 创建时间：2017-01-11
	 */
	@GET
	@Path("/downloadViewCourseNeedStatistics")
	public Response downloadViewCourseNeedStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewCourseNeedStatistics(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》应该学习人员统计.xls";
		String path = attendanceStatisticsService.downloadViewCourseNeedStatistics(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
				return null;
		
	}
	/**
	 * 功能描述：[导出应该学习课程人员名单]
	 * 创建时间：2017-01-11
	 */
	@GET
	@Path("/downloadViewCourseCanlearnStatistics")
	public Response downloadViewCourseCanlearnStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewCourseCanlearnStatistics(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》签到人员统计.xls";
		String path = attendanceStatisticsService.downloadCourseEmpSignStatistics(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
				return null;
		
	}
	/**
	 * 功能描述：[导出应该学习课程人员名单]
	 * 创建时间：2017-01-11
	 */
	@GET
	@Path("/downloadViewCourseSignStatistics")
	public Response downloadViewCourseSignStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@QueryParam("topDept")String topDept,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewCourseSignStatistics(pageNum, pageSize, courseId,topDept).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》应学人员统计.xls";
		String path = attendanceStatisticsService.downloadCourseEmpSignStatistics(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
				return null;
		
	}
	/**
	 * 功能描述：[查询点赞人员名单]
	 * 创建时间：2017-01-11
	 */
	@GET
	@Path("/viewCourseThumbUpStatistic")
	public String viewCourseThumbUpStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewCourseThumbUpStatistic(pageNum, pageSize, courseId));
		return json;
		
	}
	/**
	 * 功能描述：[导出点赞人员名单]
	 * 创建时间：2017-01-11
	 * 
	 */
	@GET
	@Path("/downloadViewCourseThumbUpStatistic")
	public Response downloadViewCourseThumbUpStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewCourseThumbUpStatistic(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》点赞人员统计.xls";
		String path = attendanceStatisticsService.downloadViewCourseThumbUpStatistic(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				return null;
	}
	/**
	 * 功能描述：[查询课程收藏名单]
	 * 创建时间：2017-01-11
	 */
	@GET
	@Path("/viewFavouriteCourseStatistic")
	public String viewFavouriteCourseStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewFavouriteCourseStatistic(pageNum, pageSize, courseId));
		return json;
		
	}
	/**
	 * 功能描述：[导出收藏人员名单]
	 * 创建时间：2017-01-11
	 */
	@GET
	@Path("/downloadViewFavouriteCourseStatistic")
	public Response downloadViewFavouriteCourseStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewFavouriteCourseStatistic(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》收藏人员统计.xls";
		String path = attendanceStatisticsService.downloadViewFavouriteCourseStatistic(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				return null;
	}
	/**
	 * 功能描述：[查询课程评论内容]
	 * 创建时间：2017-01-12
	 */
	@GET
	@Path("/viewCourseCommentStatistic")
	public String viewCourseCommentStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewCourseComment(pageNum, pageSize, courseId));
		return json;
		
	}
	/**
	 * 功能描述：[导出课程评论]
	 * 创建时间：2017-01-12
	 */
	@GET
	@Path("/downloadViewCourseCommentStatistic")
	public Response downloadViewCourseCommentStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewCourseComment(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》课程评论人员统计.xls";
		String path = attendanceStatisticsService.downloadViewCourseCommentStatistic(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				return null;
	}
	/**
	 * 查询课程评分
	 * 创建时间：2017-01-12
	 */
	@GET
	@Path("/viewCourseScoreStatistic")
	public String viewCourseScoreStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewCourseScore(pageNum, pageSize, courseId));
		return json;
		
	}
	/**
	 * 导出课程评分
	 * 创建时间：2017-01-12
	 */
	@GET
	@Path("/downloadViewCourseScoreStatistic")
	public Response downloadViewCourseScoreStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewCourseScore(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》课程评分人员统计.xls";
		String path = attendanceStatisticsService.downloadViewCourseScoreStatistic(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				return null;
	}
	/**
	 * 查询开始学习人员名单（章节）
	 * 创建时间：2017-01-12
	 */
	@GET
	@Path("/viewStartLearnSectionStatistic")
	public String viewStartLearnSectionStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		JsonMapper jsonMapper = new JsonMapper();
		String json = jsonMapper.toJson(attendanceStatisticsService.viewStartLearnSection(pageNum, pageSize, courseId));
		return json;
		
	}
	/**
	 * 导出开始学习人员名单
	 * 创建时间：2017-01-12
	 */
	@GET
	@Path("/downloadViewStartLearnSectionStatistic")
	public Response downloadViewStartLearnSectionStatistic(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
		List<TomNeedLearnDto> list = attendanceStatisticsService.viewStartLearnSection(pageNum, pageSize, courseId).getData();
		TomCourses courses = courseService.selectCourseById(courseId);
		String fileName = "《"+courses.getCourseName()+"》开始学习人员统计（章节）.xls";
		String path = attendanceStatisticsService.downloadViewStartLearnSectionStatistic(list, fileName);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				return null;
	}
}
