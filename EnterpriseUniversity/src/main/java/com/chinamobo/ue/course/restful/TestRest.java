package com.chinamobo.ue.course.restful;
/**
 * 功能说明：测试Test
 * @author Acemon
 * 2016年11月16日
 */

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dto.TomExportTestDto;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.service.TestService;
import com.chinamobo.ue.ums.DBContextHolder;

@Path("/testa")
@Scope("request")
@Component
public class TestRest {
	@Autowired
	private TestService testService;
	@Autowired
	private TomCoursesMapper tomCoursesMapper;
	
	
	@GET
	@Path("/exportTestExcel")
	public Response exportTestExcel(@QueryParam("courseId") int courseId,@Context HttpServletRequest request){
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
    		List<TomExportTestDto> exportExcel=testService.selectByCourseId(courseId);

    		TomCourses cousrs= tomCoursesMapper.selectByPrimaryKey(courseId);
    		String fileName=cousrs.getCourseName()+"-数据导出.xls";
    		fileName=fileName.replaceAll("/", "");
	    	String path=testService.TopicsToExcel(exportExcel, fileName,courseId);
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
