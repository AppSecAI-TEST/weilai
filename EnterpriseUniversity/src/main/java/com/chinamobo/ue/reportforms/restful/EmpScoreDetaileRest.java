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
import com.chinamobo.ue.reportforms.service.EmpScoreDetaileService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 功能描述  [学员报表-积分 控制器]
 * 创建者 LXT
 * 创建时间 2017年3月14日 下午7:03:45
 */
@Path("/empScoreDetaile")
@Scope("request")
@Component
public class EmpScoreDetaileRest {

	@Autowired
	private EmpScoreDetaileService empScoreDetaileService;
	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 上午11:23:49
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
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
	@Path("/empScoreDetaileList")
	@GET
	public String empScoreDetaileList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("projectId") String projectId,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName){
		String json = null;
		try {
			json=mapper.writeValueAsString(empScoreDetaileService.findPageList(pageNum,pageSize,beginTimeq,endTimeq,
					code,name,orgName,jobName,projectId,oneDeptName,twoDeptName,threeDeptName));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 功能描述 [分页 条件 查询个人积分明细]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 上午11:24:11
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param code
	 * @param name
	 * @param orgName
	 * @param jobName
	 * @param projectId
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @param empCode
	 * @return
	 */
	@Path("/findEmpScoreDetaileOne")
	@GET
	public String findEmpScoreDetaileOne(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("projectId") String projectId,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("empCode") String empCode){
		String json=null;
		try {
			json=mapper.writeValueAsString(empScoreDetaileService.findOne(pageNum,pageSize,beginTimeq, endTimeq, code, name, orgName, jobName, projectId, oneDeptName, twoDeptName, threeDeptName, empCode));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 功能描述 [条件 导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 上午11:25:20
	 * @param beginTimeq
	 * @param endTimeq
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
	@Path("/downloadEmpScoreDetaileExcel")
	@GET
	public Response downloadEmpScoreDetaileExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("projectId") String projectId,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName){
			
		Response response=empScoreDetaileService.downloadEmpScoreDetaileExcel(pageNum,pageSize,beginTimeq,endTimeq,
				code,name,orgName,jobName,projectId,oneDeptName,twoDeptName,threeDeptName,request);
		
		return response;
	}
	
	
	/**
	 * 功能描述 [条件 导出个人积分明细]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 上午11:26:09
	 * @param beginTimeq
	 * @param endTimeq
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
	@Path("/downloadEmpScoreDetaileOneExcel")
	@GET
	public Response downloadEmpScoreDetaileOneExcel(
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@Context HttpServletRequest request,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("code") String code,@QueryParam("name") String name,@QueryParam("orgName") String orgName,@QueryParam("jobName") String jobName,
			@QueryParam("projectId") String projectId,
			@QueryParam("oneDeptName") String oneDeptName,@QueryParam("twoDeptName") String twoDeptName,@QueryParam("threeDeptName") String threeDeptName,
			@QueryParam("empCode") String empCode){
			
		Response response=empScoreDetaileService.downloadEmpScoreDetaileOneExcel(pageNum,pageSize,beginTimeq,endTimeq,
				code,name,orgName,jobName,projectId,oneDeptName,twoDeptName,threeDeptName,empCode,request);
		
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
		List<TomOrg> list = empScoreDetaileService.findOrgname();
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
		List<TomDept> list = empScoreDetaileService.findFirstDeptName(orgcode);
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
		List<TomDept> list = empScoreDetaileService.findSecondDeptName(topcode);
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
		List<TomDept> list = empScoreDetaileService.findThirdDeptName(topcode);
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
		List<TomProjectClassify> list = empScoreDetaileService.findActivityClassify();
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
