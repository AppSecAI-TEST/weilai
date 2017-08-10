package com.chinamobo.ue.reportforms.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.reportforms.service.EmpCourseCommentService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 功能描述 [学员报表-课程评论控制器]
 * 创建者 LXT
 * 创建时间 2017年3月14日 上午9:33:18
 *
 */
@Path("/empCourseComment")
@Scope("request")
@Component
public class EmpCourseCommentRest {

	@Autowired
	private EmpCourseCommentService empCourseCommentService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 功能描述 [条件 分页 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月14日 上午9:33:18
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseNumber
	 * @param courseName
	 * @param code
	 * @param name
	 * @param orgName
	 * @param jobName
	 * @param projectId
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	@Path("/empCourseCommentList")
	@GET
	public String empCourseCommentList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("courseNumber") String courseNumber,@QueryParam("courseName") String courseName,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("projectId") String projectId,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("commentNull") String commentNull){
		String json = null;
		try {
			json=mapper.writeValueAsString(empCourseCommentService.findPageList(pageNum,pageSize,beginTimeq,endTimeq,courseNumber,courseName,
					code,name,orgName,jobName,projectId,oneDeptName,twoDeptName,threeDeptName,commentNull));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 功能描述 [条件 导出excel ]
	 * 创建者 LXT
	 * 创建时间 2017年3月14日 下午7:02:05
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseNumber
	 * @param courseName
	 * @param code
	 * @param name
	 * @param orgName
	 * @param jobName
	 * @param projectId
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	@Path("/downloadEmpCourseCommentExcel")
	@GET
	public Response downloadEmpCourseCommentExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("courseNumber") String courseNumber,@QueryParam("courseName") String courseName,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("projectId") String projectId,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("commentNull") String commentNull){
			
		Response response=empCourseCommentService.downloadEmpCourseCommentExcel(pageNum,pageSize,beginTimeq,endTimeq,courseNumber,courseName,
				code,name,orgName,jobName,projectId,oneDeptName,twoDeptName,threeDeptName,request,commentNull);
		
		return response;
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询所属组织
	 * 创建时间：2017-03-02
	 * @return
	 */
	@GET
	@Path("/findOrgname")
	public String findOrgname(){
		List<TomOrg> list = empCourseCommentService.findOrgname();
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询一级部门
	 * 创建时间：2017-03-03
	 * @return
	 */
	@GET
	@Path("/findFirstDeptName")
	public String findFirstDeptName(@QueryParam("code") String orgcode){
		List<TomDept> list = empCourseCommentService.findFirstDeptName(orgcode);
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询二级部门
	 * 创建时间：2017-03-04
	 * @return
	 */
	@GET
	@Path("/findSecondDeptName")
	public String findSecondDeptName(@QueryParam("code") String topcode){
		List<TomDept> list = empCourseCommentService.findSecondDeptName(topcode);
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询三级部门
	 * 创建时间：2017-03-06
	 * @return
	 */
	@GET
	@Path("/findThirdDeptName")
	public String findThirdDeptName(@QueryParam("code") String topcode){
		List<TomDept> list = empCourseCommentService.findThirdDeptName(topcode);
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询所有项目分类
	 * 创建时间：2017-03-06
	 * @return
	 */
	@GET
	@Path("/findActivityClassify")
	public String findActivityClassify(){
		List<TomProjectClassify> list = empCourseCommentService.findActivityClassify();
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	@Path("/findFirstDeptName")
	@GET
	public void findFirstDeptName(){
		System.out.println("findFirstDeptName·············");
	}
}
