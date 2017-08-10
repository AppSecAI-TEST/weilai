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
import com.chinamobo.ue.reportforms.service.LearningResourseOrgDeptExamService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 描述 [学习资源-组织部门统计报表（线上考试）]
 * 创建者 LXT
 * 创建时间 2017年3月20日 下午7:49:56
 */
@Path("/learningResourseOrgDeptExam")
@Scope("request")
@Component
public class LearningResourseOrgDeptExamRest {

	@Autowired
	private LearningResourseOrgDeptExamService learningResourseOrgDeptExamService;
	ObjectMapper mapper = new ObjectMapper();
	
	@Path("/learningResourseOrgDeptExamList")
	@GET
	public String learningResourseOrgDeptExamList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examName") String examName,@QueryParam("offlineExam") String offlineExam,@QueryParam("examType") String examType,
			@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name){
		String json = null;
		try {
			json=mapper.writeValueAsString(learningResourseOrgDeptExamService.findPageList(pageNum,pageSize,beginTimeq,endTimeq,
					examName,offlineExam,examType,examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	@Path("/downloadLearningResourseOrgDeptExamExcel")
	@GET
	public Response downloadLearningResourseOrgDeptExamExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("examName") String examName,@QueryParam("offlineExam") String offlineExam,@QueryParam("examType") String examType,
			@QueryParam("examTotalTime") String examTotalTime,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("code") String code,@QueryParam("name") String name){
			
		Response response=learningResourseOrgDeptExamService.downloadLearningResourseOrgDeptExamExcel(pageNum,pageSize,beginTimeq,endTimeq,
				examName,offlineExam,examType,examTotalTime,orgName,jobName,oneDeptName,twoDeptName,threeDeptName,code,name,request);
		
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
		List<TomOrg> list = learningResourseOrgDeptExamService.findOrgname();
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
		List<TomDept> list = learningResourseOrgDeptExamService.findFirstDeptName(orgcode);
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
		List<TomDept> list = learningResourseOrgDeptExamService.findSecondDeptName(topcode);
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
		List<TomDept> list = learningResourseOrgDeptExamService.findThirdDeptName(topcode);
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
		List<TomProjectClassify> list = learningResourseOrgDeptExamService.findActivityClassify();
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
