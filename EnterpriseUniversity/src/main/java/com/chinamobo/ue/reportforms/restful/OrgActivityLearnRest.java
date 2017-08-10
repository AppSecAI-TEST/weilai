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

import com.chinamobo.ue.reportforms.dto.EmpActivityDetaListDto;
import com.chinamobo.ue.reportforms.dto.OrgActivityLearnDto;
import com.chinamobo.ue.reportforms.service.OrgActivityLearnService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;

@Path("/orgActivityLearn")
@Scope("request")
@Component
public class OrgActivityLearnRest {
	
	@Autowired
	private OrgActivityLearnService orgActivityLearnService;
	
	/**
	 * 功能描述：[统计报表-部门-活动学习数据 ]
	 * 作者：TYX
	 * 创建时间：2017年3月20日 下午7:03:49
	 * @throws Exception
	 */
	@GET
	@Path("/orgActivitylearn")
	public String orgActivityLearn(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return jsonMapper.toJson(orgActivityLearnService.orgActivityLearnListFrom(pageNum, pageSize,orgName,oneDeptName,twoDeptName,threeDeptName));
		
	}
	
	/**
	 * 功能描述：[导出部门-活动学习数据 ]
	 * 作者：TYX
	 * 创建时间：2017年3月25日 上午11:05:45
	 * @param pageNum
	 * @param pageSize
	 * @param orgName
	 * @return
	 */
	@GET
	@Path("/orgActivityLearnExcel")
	public Response empActivityDetailExcel(@Context HttpServletRequest request,@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("orgName") String orgName,@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,
			@QueryParam("threeDeptName") String threeDeptName){
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<OrgActivityLearnDto> orgActivityLearnList = orgActivityLearnService.queryorgActivityLearnList(pageNum, pageSize,orgName,oneDeptName,twoDeptName,threeDeptName);
			String fileName = "活动统计.xls";
			String path = orgActivityLearnService.OrgActivityLearnListExcel(orgActivityLearnList, fileName);
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
			response.encoding("utf-8");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie orgActivityLearnCookie = new NewCookie("orgActivityLearn","ok","/",null,null,1,false);
			response.cookie(orgActivityLearnCookie);
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
}
