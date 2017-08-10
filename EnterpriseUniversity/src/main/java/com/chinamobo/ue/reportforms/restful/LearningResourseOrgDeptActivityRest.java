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
import com.chinamobo.ue.reportforms.service.LearningResourseOrgDeptActivityService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 描述 [学习资源-组织部门报表统计（活动）]
 * 创建者 LXT
 * 创建时间 2017年3月21日 下午3:51:23
 */
@Path("/learningResourseOrgDeptActivity")
@Scope("request")
@Component
public class LearningResourseOrgDeptActivityRest {

	@Autowired
	private LearningResourseOrgDeptActivityService learningResourseOrgDeptActivityService;
	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:56:33
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
	@Path("/learningResourseOrgDeptActivityList")
	@GET
	public String learningResourseOrgDeptActivityList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("activityType") String activityType,@QueryParam("activityName") String activityName,@QueryParam("needApply") String needApply,
			@QueryParam("activityState") String activityState,@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name){
		String json = null;
		try {
			json=mapper.writeValueAsString(learningResourseOrgDeptActivityService.findPageList(pageNum,pageSize,beginTimeq,endTimeq,
					activityType,activityName,needApply,activityState,examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
		return json;
	}
	
	/**
	 * 功能描述 [条件 导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:56:51
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
	@Path("/downloadLearningResourseOrgDeptActivityExcel")
	@GET
	public Response downloadLearningResourseOrgDeptActivityExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("activityType") String activityType,@QueryParam("activityName") String activityName,@QueryParam("needApply") String needApply,
			@QueryParam("activityState") String activityState,@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name){
			
		Response response=learningResourseOrgDeptActivityService.downloadLearningResourseOrgDeptActivityExcel(pageNum,pageSize,beginTimeq,endTimeq,
				activityType,activityName,needApply,activityState,examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name,request);
		
		return response;
	}
	
	/**
	 * 功能描述 [查询活动类型]
	 * 创建者 LXT
	 * 创建时间 2017年3月25日 下午1:42:58
	 * @return
	 */
	@Path("/findActivityType")
	@GET
	public String findActivityType(){
		String json=null;
		try {
			json=mapper.writeValueAsString(learningResourseOrgDeptActivityService.findActivityType());
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
		List<TomOrg> list = learningResourseOrgDeptActivityService.findOrgname();
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
		List<TomDept> list = learningResourseOrgDeptActivityService.findFirstDeptName(orgcode);
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
		List<TomDept> list = learningResourseOrgDeptActivityService.findSecondDeptName(topcode);
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
		List<TomDept> list = learningResourseOrgDeptActivityService.findThirdDeptName(topcode);
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
		List<TomProjectClassify> list = learningResourseOrgDeptActivityService.findActivityClassify();
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
