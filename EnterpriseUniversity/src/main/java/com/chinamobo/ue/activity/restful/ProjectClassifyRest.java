package com.chinamobo.ue.activity.restful;

import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.activity.service.ProjectClassifyService;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程分类rest
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 上午10:02:00
 */
@Path("/projectClassify") 
@Scope("request") 
@Component
public class ProjectClassifyRest {
	
	@Autowired
	private ProjectClassifyService projectClassifyService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	ShiroUser user=ShiroUtils.getCurrentUser();
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:53:57
	 * @param projectClassify
	 * @return
	 */
	@POST
	@Path("/add")
	public String addProjectClassify(@BeanParam TomProjectClassify projectClassify){
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			projectClassify.setCreator(user.getName());
			projectClassify.setCreatorId(user.getCode());
			projectClassify.setOperator(user.getName());
			projectClassifyService.addProjectClassify(projectClassify);
			return "success";	
	}
	
	/**
	 * 
	 * 功能描述：[按id查询]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:53:57
	 * @param projectClassify
	 * @return
	 */
	@GET
	@Path("/find")
	public String findProjectClassify (@QueryParam("projectId") int classifyId){	
		
		try {
			return mapper.writeValueAsString(projectClassifyService.selectById(classifyId));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:54:35
	 * @param pageNum
	 * @param pageSize
	 * @param classifyName
	 * @param classifyId
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("projectName") String classifyName,@DefaultValue("0") @QueryParam("projectId")int classifyId){
		
		try {
			String json = mapper.writeValueAsString(projectClassifyService.selectByPage(pageNum, pageSize,classifyName,classifyId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:54:57
	 * @param projectClassify
	 * @return
	 */
	@PUT
	@Path("/edit")
	public String editProjectClassify(@BeanParam TomProjectClassify projectClassify){
		projectClassify.setOperator(user.getName());
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		projectClassifyService.updateProjectClassify(projectClassify);
		return "{\"result\":\"success\"}";
	}
	/**
	 * 
	 * 功能描述：[删除]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:55:10
	 * @param classifyId
	 * @return
	 */
	@DELETE
	@Path("/delete")
	public String deleteProjectClassify (@QueryParam("projectId") int classifyId){
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			TomProjectClassify projectClassify=new TomProjectClassify();
//			projectClassify.setOperator(user.getName());
			projectClassify.setProjectId(classifyId);	
			String status=projectClassifyService.updateStatus(projectClassify);
			return "{\"result\":\""+status+"\"}";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[查询一级分类]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:55:24
	 * @return
	 */
	@GET
	@Path("/findHighest")
	public String findHighest(){
		List<TomProjectClassify> list=projectClassifyService.selectByParentClassifyId(0);
		String json;
		try {
			json = mapper.writeValueAsString(list);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
	/**
	 * 
	 * 功能描述：[获取课程分类树形菜单]
	 *
	 * 创建者：cjx
	 * 创建时间: 2017年2月22日 下午4:55:36
	 * @return
	 */
	@GET
	@Path("/tree")
	public String getTree(){
		Tree tree=projectClassifyService.getClassifyTree();
		String json;
		try {
			json = mapper.writeValueAsString(tree);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	
}
