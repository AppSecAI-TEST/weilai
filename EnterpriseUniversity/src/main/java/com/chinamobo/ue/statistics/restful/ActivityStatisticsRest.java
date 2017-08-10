package com.chinamobo.ue.statistics.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.dto.TomActivityEmpDto;
import com.chinamobo.ue.activity.dto.TomActivityProcessDto;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.service.ActivityService;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.statistics.entity.TomActivityStatistics;
import com.chinamobo.ue.statistics.service.ActivityStatisticsService;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/activityStatistics")
@Scope("request") 
@Component
public class ActivityStatisticsRest {
	
	@Autowired
	private ActivityStatisticsService activityStatisticsService;
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	private EmpServise empServise;
	
	ObjectMapper mapper = new ObjectMapper(); 
	
	/**
	 * 
	 * 功能描述：[活动统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月26日
	 * @param pageNum
	 * @param pageSize
	 * @param activityStatistics
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/activityStatisticsList")
	public String activityStatisticsList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityName") String activityName,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(activityStatisticsService.queryActivityStatistics(pageNum, pageSize, activityName,beginTimeq,endTimeq));
	}
	
	/**
     * 
     * 功能描述：[生成活动统计并下载]
     *
     * 创建者：GW
     * 创建时间: 2016年5月27日
     * @param
     * @return
	 * @throws Exception 
     */
	@GET
    @Path("/downloadActivityStatisticsExcel")
	public Response downloadActivityStatisticsExcel(@Context HttpServletResponse response1,@Context HttpServletRequest request,@QueryParam("codes") String codes,@QueryParam("activityId")String activityIds) throws Exception{
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<TomActivityStatistics> activityStatistics = activityStatisticsService.queryActivityStatisticsByCodes(codes.split(","), activityIds.split(","));
			String fileName = "活动统计.xls";
			String path = activityStatisticsService.ActivityStatisticsExcel(activityStatistics, fileName);
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
     * 功能描述：[查看活动统计]
     *
     * 创建者：GW
     * 创建时间: 2016年6月1日
     * @param
     * @return
     */
	@GET
	@Path("/seeActivityStatistics")
	public String seeActivityStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@QueryParam("apply")  String apply){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.seeActivityStatistics(pageNum,pageSize,activityId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
     * 
     * 功能描述：[生成活动详细统计并下载]
     *
     * 创建者：GW
     * 创建时间: 2016年6月1日
     * @param
     * @return
     */
	@GET
    @Path("/downloadActivityStatisticsDetailedExcel")
	public Response downloadActivityStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@Context HttpServletRequest request){
		try{
			List<TomActivityProcessDto> activityEmpsRelation = activityStatisticsService.seeActivityStatistics(pageNum,pageSize, activityId).getData();
			TomActivityDto activity = activityService.findActivityDetails(activityId);
			String fileName = "《"+activity.getActivityName()+"》应完成名单统计.xls";
			String path = activityStatisticsService.ActivityStatisticsDetailedExcel(activityEmpsRelation, fileName);
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
     * 功能描述：[查看活动已报名人数统计]
     *
     * 创建者：Acemon
     * 创建时间: 2016年12月27日
     * @param
     * @return
     */
	@GET
	@Path("/seeActivityApplyStatistics")
	public String seeActivityApplyStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.seeActivityApplyStatistics(pageNum,pageSize,activityId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
     * 
     * 功能描述：[生成活动报名人数详细统计并下载]
     *
     * 创建者：Acemon
     * 创建时间: 2016年12月28日
     * @param
     * @return
     */
	@GET
    @Path("/downloadActivityApplyStatisticsDetailedExcel")
	public Response downloadActivityApplyStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@Context HttpServletRequest request){
		try{
			List<TomActivityEmpsRelation> activityEmpsRelation = activityStatisticsService.seeActivityApplyStatistics(pageNum,pageSize, activityId).getData();
			TomActivityDto activity = activityService.findActivityDetails(activityId);
			String fileName = "《"+activity.getActivityName()+"》报名人数统计.xls";
			String path = activityStatisticsService.ActivityApplyStatisticsDetailedExcel(activityEmpsRelation, fileName);
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
	 * 功能描述：[查看已完成活动人员统计]
	 * 创建者：Acemon
	 * 创建时间：2017年5月10日
	 */
	@GET
	@Path("/seeCompletedActivityEmp")
	public String seeCompletedActivityEmp(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@QueryParam("apply")  String apply){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.viewCompletedActivityEmp(pageNum,pageSize,activityId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[导出完成活动人员明细]
	 * 创建者：Acemon
	 * 创建时间：2017年5月11日
	 */
	@GET
    @Path("/downloadCompletedActivityEmpExcel")
	public Response downloadCompletedActivityEmp(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@Context HttpServletRequest request){
		try{
		List<EmpLearningActionDto> list = activityStatisticsService.viewCompletedActivityEmp(pageNum, pageSize, activityId).getData();
		TomActivityDto activity = activityService.findActivityDetails(activityId);
		String fileName = "《"+activity.getActivityName()+"》完成人数统计.xls";
		String path = activityStatisticsService.completedActivityEmpExcel(list, fileName);
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
		} catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * 
     * 功能描述：[查看开放名单详细统计]
     *
     * 创建者：Acemon
     * 创建时间: 2016年12月27日
     * @param
     * @return
     */
	@GET
	@Path("/seeActivityEmpStatistics")
	public String seeActivityEmpStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.seeActivityEmpStatistics(pageNum,pageSize,activityId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	
	/**
     * 
     * 功能描述：[生成活动开放名单详细统计并下载]
     *
     * 创建者：Acemon
     * 创建时间: 2016年12月28日
     * @param
     * @return
     */
	@GET
    @Path("/downloadActivityEmpStatisticsDetailedExcel")
	public Response downloadActivityEmpStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("activityId") int activityId,@Context HttpServletRequest request){
		try{
			List<TomActivityEmpDto> tomActivityEmpDto = activityStatisticsService.seeActivityEmpStatistics(pageNum,pageSize, activityId).getData();
			TomActivityDto activity = activityService.findActivityDetails(activityId);
			String fileName = "《"+activity.getActivityName()+"》开放名单统计.xls";
			String path = activityStatisticsService.ActivityEmpStatisticsDetailedExcel(tomActivityEmpDto, fileName);
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
    @Path("/findEmpActive")
	public String findEmpActive(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("code") String code){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.findEmpActive(pageNum,pageSize,code));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	@GET
    @Path("/findpingtai")
	public String findpingtai(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTime") String beginTime,@QueryParam("endTime") String endTime){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.findpingtai(pageNum,pageSize,beginTime,endTime));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	@GET
    @Path("/findPelPingtai")
	public String findPelPingtai(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("createDate") String createDate){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.findpingtaiPel(pageNum,pageSize,createDate));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	@GET
    @Path("/findLearnPingtai")
	public String findLearnPingtai(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("createDate") String createDate){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.findpingtaiLearn(pageNum,pageSize,createDate));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	@GET
    @Path("/findloginTime")
	public String findloginTime(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTime") String beginTime,@QueryParam("endTime") String endTime,
			@QueryParam("code") String code){
		String json;
		try {
			json = mapper.writeValueAsString(activityStatisticsService.findloginTime(pageNum,pageSize,beginTime,endTime,code));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	@GET
    @Path("/downloadpingtaiStatisticsExcel")
	public Response downloadpingtaiStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTime") String beginTime,@QueryParam("endTime") String endTime,@Context HttpServletRequest request){
		try{
			if("".equals(beginTime)){
				beginTime=null;
			}
			if("".equals(endTime)){
				endTime=null;
			}
			List<TomActivityEmpDto> tomActivityEmpDto = activityStatisticsService.findpingtai(pageNum,pageSize,beginTime,endTime).getData();
			//TomActivityDto activity = activityService.findActivityDetails(activityId);
			
			String fileName = "《平台信息统计》.xls";
			if(beginTime!=null && endTime!=null){
				fileName = "《"+beginTime+"日-"+endTime+"日》平台信息统计.xls";
			}
			String path = activityStatisticsService.pingtaiStatisticsDetailedExcel(tomActivityEmpDto, fileName);
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
    @Path("/downloadEmpActiveStatisticsDetailedExcel")
	public Response downloadActivityEmpStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("code") String code,@Context HttpServletRequest request){
		try{
			List<TomActivityEmpDto> activityEmpDtoList = activityStatisticsService.findEmpActive(pageNum,pageSize,code).getData();
			TomEmp emp = empServise.selectByCode(code);
			String fileName = "学员《"+emp.getName()+"》活动统计.xls";
			String path = activityStatisticsService.empActiveStatisticsDetailedExcel(activityEmpDtoList, fileName);
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
    @Path("/downloadpingtaiLearnStatisticsExcel")
	public Response downloadpingtaiLearnStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("createDate") String createDate,@Context HttpServletRequest request){
		try{
			List<TomEmp> activityEmpDtoList = activityStatisticsService.findpingtaiLearn(pageNum,pageSize,createDate).getData();
			//TomActivityDto activity = activityService.findActivityDetails(activityId);
			String fileName = "《"+createDate+"日》"+"学习人员名单.xls";
			String path = activityStatisticsService.pingtaiLearnStatisticsDetailedExcel(activityEmpDtoList, fileName);
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
    @Path("/downloadpingtailoginStatisticsExcel")
	public Response downloadpingtailoginStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("createDate") String createDate,@Context HttpServletRequest request){
		try{
			List<TomEmp> activityEmpDtoList = activityStatisticsService.findpingtaiPel(pageNum,pageSize,createDate).getData();
			//TomActivityDto activity = activityService.findActivityDetails(activityId);
			String fileName = "《"+createDate+"日》"+"登录人员名单.xls";
			String path = activityStatisticsService.pingtailoginStatisticsDetailedExcel(activityEmpDtoList, fileName);
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
    @Path("/downloadEmplogintimeStatisticsDetailedExcel")
	public Response downloadEmplogintimeStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("beginTime") String beginTime,@QueryParam("endTime") String endTime,
			@QueryParam("code") String code,@Context HttpServletRequest request){
		try{
			List<TomActivityEmpDto> activityEmpDtoList = activityStatisticsService.findloginTime(pageNum,pageSize,beginTime,endTime,code).getData();
			TomEmp emp = empServise.selectByCode(code);
			String fileName = "《学员："+emp.getName()+"》登录统计.xls";
			String path = activityStatisticsService.pingtaiLoginTimeStatisticsExcel(activityEmpDtoList, fileName);
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
