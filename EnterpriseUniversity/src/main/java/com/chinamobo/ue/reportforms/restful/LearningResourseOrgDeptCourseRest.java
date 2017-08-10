package com.chinamobo.ue.reportforms.restful;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.reportforms.service.LearningResourseOrgDeptCourseService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 描述 [学习资源-组织部门统计报表（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月17日 下午5:48:35
 */
@Path("/learningResourseOrgDeptCourse")
@Scope("request")
@Component
public class LearningResourseOrgDeptCourseRest {

	@Autowired
	private LearningResourseOrgDeptCourseService learningResourseOrgDeptCourseService;
	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 功能描述 [条件 分页 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午4:23:14
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseName
	 * @param courseOnline
	 * @param courseType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	@Path("/learningResourseOrgDeptCourseList")
	@GET
	public String learningResourseOrgDeptCourseList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("courseName") String courseName,@QueryParam("courseOnline") String courseOnline,@QueryParam("sectionClassifyName") String sectionClassifyName,
			@QueryParam("openCourse") String openCourse,
			@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name){
		String json = null;
		try {
			json=mapper.writeValueAsString(learningResourseOrgDeptCourseService.findPageList(pageNum,pageSize,beginTimeq,endTimeq,
					courseName,courseOnline,sectionClassifyName,
					openCourse,examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 功能描述 [条件 查询 导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午4:23:32
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseName
	 * @param courseOnline
	 * @param courseType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	@Path("/downloadLearningResourseOrgDeptCourseExcel")
	@GET
	public Response downloadLearningResourseOrgDeptCourseExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("courseName") String courseName,@QueryParam("courseOnline") String courseOnline,@QueryParam("sectionClassifyName") String sectionClassifyName,
			@QueryParam("openCourse") String openCourse,
			@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("column") String column){
			
		Response response=learningResourseOrgDeptCourseService.downloadLearningResourseOrgDeptCourseExcel(pageNum,pageSize,beginTimeq,endTimeq,
				courseName,courseOnline,sectionClassifyName,
				openCourse,examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name,request,column);
		
		return response;
	}
	
	/**
	 * 功能描述 [分页 条件 查询课程评论内容]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 下午4:13:54
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseName
	 * @param courseOnline
	 * @param courseType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @param courseId
	 * @return
	 */
	@Path("/findCourseCommentList")
	@GET
	public String findCourseCommentList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("courseId") String courseId){
		String json = null;
		try {
			json=mapper.writeValueAsString(learningResourseOrgDeptCourseService.findCourseCommentList(pageNum,pageSize,courseId));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 功能描述 [条件 查询 导出课程的评论详细内容]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 下午4:11:40
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseName
	 * @param courseOnline
	 * @param courseType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @param courseId
	 * @return
	 */
	@Path("/downloadCourseCommentByCourseIdExcel")
	@GET
	public Response downloadCourseCommentByCourseIdExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("courseId") String courseId){
			
		Response response=learningResourseOrgDeptCourseService.downloadCourseCommentByCourseIdExcel(pageNum,pageSize,courseId,request);
		
		return response;
	}
	
	/**
	 * 功能描述 [查询资源二级类别 页面下拉框使用]
	 * 创建者 LXT
	 * 创建时间 2017年3月25日 下午3:57:51
	 * @return
	 */
	@Path("/findSectionClassify")
	@GET
	public String findSectionClassify(){
		String json=null;
		try {
			json=mapper.writeValueAsString(learningResourseOrgDeptCourseService.findSectionClassify());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 功能描述 [导出课程点赞员工信息 excel]
	 * 创建者 LXT
	 * 创建时间 2017年5月2日 下午1:35:32
	 * @param request
	 * @param courseId
	 * @param beginTimeq
	 * @param endTimeq
	 * @param examTotalTime
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @param code
	 * @param name
	 * @return
	 */
	@Path("/downLoadCourseThumbUpExcel")
	@GET
	public Response downLoadCourseThumbUpExcel(@Context HttpServletRequest request,
			@QueryParam("courseId") String courseId,@QueryParam("courseName") String courseName,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name){
		
		Response response=learningResourseOrgDeptCourseService.downLoadCourseThumbUpExcel(request,courseId,courseName,beginTimeq,endTimeq,
				examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name);
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
		List<TomOrg> list = learningResourseOrgDeptCourseService.findOrgname();
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
		List<TomDept> list = learningResourseOrgDeptCourseService.findFirstDeptName(orgcode);
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
		List<TomDept> list = learningResourseOrgDeptCourseService.findSecondDeptName(topcode);
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
		List<TomDept> list = learningResourseOrgDeptCourseService.findThirdDeptName(topcode);
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
		List<TomProjectClassify> list = learningResourseOrgDeptCourseService.findActivityClassify();
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
