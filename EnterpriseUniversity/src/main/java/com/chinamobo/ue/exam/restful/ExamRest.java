package com.chinamobo.ue.exam.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.service.ExamService;
import com.chinamobo.ue.system.restful.ContextLoaderListener;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.xiaoleilu.hutool.io.FileUtil;

@Path("/exam") 
@Scope("request") 
@Component
public class ExamRest {
	private static Logger logger = LoggerFactory.getLogger(ExamRest.class);
	@Autowired
	private ExamService examService;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	ShiroUser user=ShiroUtils.getCurrentUser();
	
	String basePath=Config.getProperty("file_path");
	
	@GET
	@Path("/findOne")
	public String findOne(@QueryParam("examId") int examId){
		JsonMapper jsonMapper = new JsonMapper();
		TomExam exam=examService.findById(examId);
		String json=jsonMapper.toJson(exam);
		return json;
	}
	
	/**
	 * 鍔熻兘鎻忚堪锛歔淇濆瓨鑰冭瘯]
	 *
	 * 鍒涘缓鑰咃細xjw
	 * 鍒涘缓鏃堕棿: 2016骞�3鏈�4鏃� 涓嬪崍5:46:59
	 * @param examName
	 * @param beginTime
	 * @param endTime
	 * @param examPaperId
	 * @param examTime
	 * @param retakingExamCount
	 * @param examType
	 * @param offlineExam
	 * @param adminId
	 * @return
	 */
	@POST
	@Path("add")
	public String save(@BeanParam TomExam exam){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			exam.setCreator(user.getName());
			exam.setCreatorId(user.getCode());
			exam.setLastOperator(user.getName());
			exam.setLastOperatorId(user.getCode());
			examService.addexam(exam);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
		
	}
	
	@PUT
	@Path("update")
	public String update(@BeanParam TomExam exam){
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			TomExam tomExam=examService.findById(exam.getExamId());
			TomActivityProperty example=new TomActivityProperty();
			example.setExamId(exam.getExamId());
			if(tomExam.getBeginTime().before(new Date())){
				return "{\"result\":\"protected\"}";
			}else if(activityPropertyMapper.selectByExample(example).size()>0){
				return "{\"result\":\"inActivity\"}";
			}
			exam.setLastOperator(user.getName());
			exam.setLastOperatorId(user.getCode());
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			examService.updateexam(exam);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":\"error\"}";
		}
	}
	
	
	/**
	 * 鍔熻兘鎻忚堪锛歔鑰冭瘯鏌ヨ鍒楄〃]
	 *
	 * 鍒涘缓鑰咃細xjw
	 * 鍒涘缓鏃堕棿: 2016骞�3鏈�4鏃� 涓嬪崍2:12:35
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON})
	public String list(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("examname")String examname){
		JsonMapper jsonMapper = new JsonMapper();
		String json=jsonMapper.toJson(examService.findList(pageNum,pageSize,examname));
		String filePath = ContextLoaderListener.getApplicationContext().getServletContext().getRealPath("/")+"data"+File.separator+"api"+File.separator+"exam"+File.separator;
		File file = FileUtil.touch(filePath+"111.json");
		try {
			json += "\n";
			byte[] dataBytes = json.getBytes("utf-8");
			FileUtil.writeBytes(dataBytes,file,0,dataBytes.length,true);
			FileUtil.writeBytes(dataBytes,file,0,dataBytes.length,true);
			FileUtil.writeBytes(dataBytes,file,0,dataBytes.length,true);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		logger.info(json);
		return json;
	}
	
	/**
	 * 鍔熻兘鎻忚堪锛歔鍒犻櫎涓�涓�冭瘯]
	 *
	 * 鍒涘缓鑰咃細xjw
	 * 鍒涘缓鏃堕棿: 2016骞�3鏈�4鏃� 涓嬪崍2:26:42
	 * @param examId
	 * @return
	 */
	@DELETE
	@Path("/deleteExam")
	public String delete(@QueryParam("examId")int examId){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			TomExam tomExam=examService.findById(examId);
			TomActivityProperty example=new TomActivityProperty();
			example.setExamId(examId);
			if(tomExam.getBeginTime().before(new Date())){
				return "{\"result\":\"protected\"}";
			}else if(activityPropertyMapper.selectByExample(example).size()>0){
				return "{\"result\":\"inActivity\"}";
			}
			examService.deleteexam(examId);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return "{\"result\":\"error\"}";
	}
	
	@GET
	@Path("/deleteExamList")
	public String delete(@QueryParam("examIds")String examIds){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			String[] ids=examIds.split(",");
			for(String id:ids){
				if (!id.equals("0")) {
					TomExam tomExam = examService
							.findById(Integer.parseInt(id));
					if (tomExam.getBeginTime().before(new Date())) {
						return "{\"result\":\"protected\"}";
					}
				}
			}
			for(String id:ids){
				if (!id.equals("0")) {
					examService.deleteexam(Integer.parseInt(id));
				}
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[导入成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:49:22
	 * @param examId
	 * @param filePath
	 * @return
	 */
	@GET
	@Path("/importResults")
	public String importResults(@QueryParam("examId") int examId,@QueryParam("filePath") String filePath){
		try {
			return "{\"result\":\""+examService.importResults(examId,basePath+filePath)+"\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
 	@GET
	@Path("/exportscore")
	public Response exportscore(@Context HttpServletRequest request,@QueryParam("codes") String codes,@QueryParam("examId")Integer examId){
		try{
			TomExam tomExam = examService.findById(examId);
			if(tomExam==null){
				return null;
			}
			String fileName = tomExam.getExamName()+".xls";
			String path = examService.exportscore(tomExam, codes, fileName);
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