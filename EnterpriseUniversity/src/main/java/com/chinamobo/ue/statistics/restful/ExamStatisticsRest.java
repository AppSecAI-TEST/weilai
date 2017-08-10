package com.chinamobo.ue.statistics.restful;

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

import com.chinamobo.ue.course.dto.TomEmpCourseDto;
import com.chinamobo.ue.exam.dto.ExamDto;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.service.ExamService;
import com.chinamobo.ue.statistics.entity.TomExamEmployeeStatistics;
import com.chinamobo.ue.statistics.entity.TomExamStatistics;
import com.chinamobo.ue.statistics.service.ExamStatisticsService;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;

@Path("/examStatistics")
@Scope("request") 
@Component
public class ExamStatisticsRest {
	
	@Autowired
	private ExamStatisticsService examStatisticsService;
	
	@Autowired
	private ExamService examService;
	
	@Autowired
	private EmpServise empServise;
	/**
	 * 
	 * 功能描述：[考试统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月31日
	 * @param pageNum
	 * @param pageSize
	 * @param examName
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/queryExamStatistics")
	public String queryExamStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examName") String examName,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(examStatisticsService.queryExamStatistics(pageNum, pageSize, examName,beginTimeq,endTimeq));
	}
	/**
	 * 功能介绍：[导出考试统计列表]
	 * 创建人：LG
	 * 创建时间：2016年6月3日 19：50
	 * @param 
	 * @return excel表格
	 * @throws Exception 
	 */
	@GET
	@Path("/downloadExamStatisticsExcel")
	public Response downloadExamStatisticsExcel(@QueryParam("codes") String codes,@QueryParam("examId")String examIds,@Context HttpServletRequest request) throws Exception{
		
			try {
				List<TomExamStatistics> exam = examStatisticsService.queryExamStatisticsByCode(codes.split(","), examIds.split(","));
				DBContextHolder.setDbType(DBContextHolder.SLAVE);
//				List<TomExamStatistics> exam=examStatisticsService.queryExamStatistics(pageNum, pageSize, examName,beginTimeq,endTimeq).getData();
				String fileName="考试统计.xls";
				String path=examStatisticsService.downloadExamStatisticsExcel(exam, fileName);
				File  file=new File(path);
				
				ResponseBuilder response=Response.ok((Object)file);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return null;
	}
	/**
	 * 
	 * 功能描述：[考试统计详细信息分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月7日
	 * @param pageNum
	 * @param pageSize
	 * @param examId
	 * @return
	 */
	@GET
	@Path("/queryExamEmployeeStatistics")
	
	public String queryExamEmployeeStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
					@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examId") int examId){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(examStatisticsService.queryExamEmployeeStatistics(pageNum, pageSize, examId));
	}
	/**
	 * 功能介绍：[导出考试统计列表]
	 * 创建人：LG
	 * 创建时间：2016年6月7日 11：50
	 * @param 
	 * @return excel表格
	 */
	@GET
	@Path("/downloadExamEmployeeStatisticsExcel")
	public Response downloadExamEmployeeStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examId") int examId,@Context HttpServletRequest request){
		try{
		 List<TomExamEmployeeStatistics> examlist=examStatisticsService.queryExamEmployeeStatistics(pageNum, pageSize, examId).getData();
		 TomExam exam = examService.findById(examId);
		 String fileName="《"+exam.getExamName()+"》应该参加名单统计.xls";
		 String path=examStatisticsService.downloadExamEmployeeStatisticsExcel(examlist, fileName);
//		 String fileName="考试错题详情.xls";
//		 String path=examStatisticsService.downloadExamAnswerExcel(examId, fileName);
		 File  file=new File(path);
			
			ResponseBuilder response=Response.ok((Object)file);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * 功能描述：[实际参加考试统计详细信息分页查询]
	 *
	 * 创建者：Acemon
	 * 创建时间: 2016年12月29日
	 * @param pageNum
	 * @param pageSize
	 * @param examId
	 * @return
	 */
	@GET
	@Path("/queryExamRealEmployeeStatistics")
	
	public String queryExamRealEmployeeStatistics(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
					@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examId") int examId){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(examStatisticsService.queryExamRealEmployeeStatistics(pageNum, pageSize, examId));
	}
	
	
	/**
	 * 功能介绍：[导出考试统计列表]
	 * 创建人：Acemon
	 * 创建时间：2016年12月29日
	 * @param 
	 * @return excel表格
	 */
	@GET
	@Path("/downloadExamRealEmployeeStatisticsExcel")
	public Response downloadExamRealEmployeeStatisticsExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examId") int examId,@Context HttpServletRequest request){
		try{
		 List<TomExamEmployeeStatistics> examlist=examStatisticsService.queryExamRealEmployeeStatistics(pageNum, pageSize, examId).getData();
		 TomExam exam = examService.findById(examId);
		 String fileName="《"+exam.getExamName()+"》实际参加名单统计.xls";
		 String path=examStatisticsService.downloadExamEmployeeStatisticsExcel(examlist, fileName);
//		 String fileName="考试错题详情.xls";
//		 String path=examStatisticsService.downloadExamAnswerExcel(examId, fileName);
		 File  file=new File(path);
			
			ResponseBuilder response=Response.ok((Object)file);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	@GET
	@Path("/findEmpExam")
	public String findEmpExam(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("code") String code){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		JsonMapper jsonMapper = new JsonMapper();
		String json=jsonMapper.toJson(examStatisticsService.findempExam(pageNum,pageSize,code));
		
		return json;
	}
	@GET
    @Path("/downloadEmpExamStatisticsDetailedExcel")
	public Response downloadEmpExamStatisticsDetailedExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("code") String code,@Context HttpServletRequest request){
		try{
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<ExamDto> examList = examStatisticsService.findempExam(pageNum, pageSize,code).getData();
			TomEmp emp = empServise.selectByCode(code);
			String fileName = "学员《"+emp.getName()+"》考试统计.xls";
			String path = examStatisticsService.empExamStatisticsDetailedExcel(examList, fileName);
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
