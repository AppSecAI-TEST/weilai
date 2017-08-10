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
import com.chinamobo.ue.reportforms.service.EmpLearningDetailedCourseService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 描述 [学员报表-学习资源详细（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月22日 下午5:13:08
 */
@Path("/empLearningDetailedCourse")
@Scope("request")
@Component
public class EmpLearningDetailedCourseRest {

	@Autowired
	private EmpLearningDetailedCourseService empLearningDetailedCourseService;
	ObjectMapper mapper = new ObjectMapper();
	

	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月22日 下午5:16:01
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param activityType
	 * @param activityName
	 * @param isRequired
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	@Path("/empLearningDetailedCourseList")
	@GET
	public String empLearningDetailedCourseList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examTotalTime") String examTotalTime,
			@QueryParam("parentClassifyId") String parentClassifyId,@QueryParam("activityName") String activityName,@QueryParam("needApply") String needApply,
			@QueryParam("activityState") String activityState,@QueryParam("courseName") String courseName,@QueryParam("code") String code,@QueryParam("name") String name,
			@QueryParam("openCourse") String openCourse,@QueryParam("courseOnline") String courseOnline,
			@QueryParam("sectionClassifyName") String sectionClassifyName,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName){
		String json = null;
		try {
			json=mapper.writeValueAsString(empLearningDetailedCourseService.findPageList(pageNum,pageSize,beginTimeq,endTimeq,
					examTotalTime,parentClassifyId,activityName,needApply,activityState,courseName,code,name,
					openCourse,courseOnline,
					sectionClassifyName,orgName,jobName,oneDeptName,twoDeptName,threeDeptName));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	

	/**
	 * 功能描述 [条件 导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月22日 下午5:16:17
	 * @param beginTimeq
	 * @param endTimeq
	 * @param activityType
	 * @param activityName
	 * @param isRequired
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	@Path("/downloadEmpLearningDetailedCourseExcel")
	@GET
	public Response downloadEmpLearningDetailedCourseExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examTotalTime") String examTotalTime,
			@QueryParam("parentClassifyId") String parentClassifyId,@QueryParam("activityName") String activityName,@QueryParam("needApply") String needApply,
			@QueryParam("activityState") String activityState,@QueryParam("courseName") String courseName,@QueryParam("code") String code,@QueryParam("name") String name,
			@QueryParam("openCourse") String openCourse,@QueryParam("courseOnline") String courseOnline,
			@QueryParam("sectionClassifyName") String sectionClassifyName,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("column") String column){
			
		Response response=empLearningDetailedCourseService.downloadEmpLearningDetailedCourseExcel(pageNum,pageSize,beginTimeq,endTimeq,examTotalTime,
				parentClassifyId,activityName,needApply,activityState,courseName,code,name,
				openCourse,courseOnline,
				sectionClassifyName,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,request,column);
		
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
			json=mapper.writeValueAsString(empLearningDetailedCourseService.findSectionClassify());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
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
		List<TomOrg> list = empLearningDetailedCourseService.findOrgname();
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
		List<TomDept> list = empLearningDetailedCourseService.findFirstDeptName(orgcode);
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
		List<TomDept> list = empLearningDetailedCourseService.findSecondDeptName(topcode);
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
		List<TomDept> list = empLearningDetailedCourseService.findThirdDeptName(topcode);
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
		List<TomProjectClassify> list = empLearningDetailedCourseService.findActivityClassify();
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
