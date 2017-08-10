package com.chinamobo.ue.exam.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
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

import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.exam.entity.TomQuestionBank;
import com.chinamobo.ue.exam.entity.TomQuestionClassification;
import com.chinamobo.ue.exam.service.QuestionBankService;
import com.chinamobo.ue.exam.service.QuestionClassifyService;
import com.chinamobo.ue.ums.DBContextHolder;
//import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.PathUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/questionBank")
@Scope("request")
@Component
public class QuestionBankRest {

	private static Logger logger = LoggerFactory.getLogger(QuestionBankRest.class);

	@Autowired
	private QuestionBankService questionBankService;
	@Autowired
	private QuestionClassifyService questionClassifyService;
	ObjectMapper mapper = new ObjectMapper(); 
	
	String filePath1=Config.getProperty("file_path");
	ShiroUser user=ShiroUtils.getCurrentUser();
	
	@GET
	@Path("/view")
	public void view(@Context HttpServletResponse response, @Context HttpServletRequest request) {
		RequestDispatcher dispatcher = request
				.getRequestDispatcher(PathUtil.VIEW_LOCAL_PATH + "/exam/qbClassification.html");
		try {
			dispatcher.forward(request, response);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@GET
	@Path("/find")
	@Produces({ MediaType.APPLICATION_JSON })
	public String find(@QueryParam("id") int id, @Context HttpServletResponse response) {
		JsonMapper jsonMapper = new JsonMapper();
		TomQuestionBank t = questionBankService.findOne(id);
		String s = jsonMapper.toJson(t);
		return s;
	}

	/***
	 * 
	 * 功能描述：[题库分类添加]
	 *
	 * 创建者：wjx 创建时间: 2016年3月2日 下午4:00:32
	 * 
	 * @param questionClassificationName
	 * @param parentClassificationId
	 * @param questionClassificationAbstra
	 * @param adminIds
	 * @param response
	 * @return
	 */
	@POST
	@Path("/addClassification")
	public String addClassification(@BeanParam TomQuestionClassification classification,
			@Context HttpServletResponse response) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER); 
			ShiroUser user = ShiroUtils.getCurrentUser();
			classification.setCreateTime(new Date());
			classification.setUpdateTime(new Date());
			classification.setCreator(user.getName());
			classification.setCreatorId(user.getCode());
			classification.setLastOperator(user.getName());
			classification.setLastOperatorId(user.getCode());
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			questionBankService.addClassification(classification);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}

	/**
	 * 
	 * 功能描述：[题库分类查询-id]
	 *
	 * 创建者：wjx 创建时间: 2016年3月2日 下午4:01:14
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/findClassification")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findClassification(@QueryParam("questionClassificationId") int questionClassificationId) {
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(questionBankService.queryClassification(questionClassificationId));
	}

	/**
	 * 
	 * 功能描述：[修改题库分类]
	 *
	 * 创建者：wjx 创建时间: 2016年3月2日 下午4:02:43
	 * 
	 * @param id
	 * @param creator
	 * @param questionClassificationName
	 * @param parentClassificationId
	 * @param questionClassificationAbstra
	 * @param adminIds
	 * @param response
	 * @return
	 */
	@PUT
	@Path("/updateClassification")
	public String update(@BeanParam TomQuestionClassification questionClassification) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			questionClassification.setUpdateTime(new Date());
			questionClassification.setLastOperator(user.getName());
			questionClassification.setLastOperatorId(user.getCode());
			questionBankService.updateClassfication(questionClassification);
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[题库分类分页]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午3:27:19
	 * @param pageNum
	 * @param pageSize
	 * @param questionClassificationName
	 * @param questionClassificationId
	 * @return
	 */
	@GET
	@Path("/classificationList")
	public String findClassificationByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("questionClassificationName") String questionClassificationName,@DefaultValue("0") @QueryParam("questionClassificationId")int questionClassificationId){
		
		try {
			String json = mapper.writeValueAsString(questionClassifyService.selectByPage(pageNum, pageSize,questionClassificationName,questionClassificationId));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}

	/**
	 * 
	 * 功能描述：[删除题库分类]
	 *
	 * 创建者：wjx 创建时间: 2016年3月2日 下午4:35:12
	 * 
	 * @param questionClassificationId
	 * @return
	 */
	@DELETE
	@Path("/deleteClassification")
	public String deleteClassification(@QueryParam("questionClassificationId") int questionClassificationId) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			String status=questionClassifyService.deleteClassification(questionClassificationId);
			return "{\"result\":\""+status+"\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return "{\"result\":\"error\"}";
	}
	
//	/**
//	 * 
//	 * 功能描述：[批量删除题库分类]
//	 *
//	 * 创建者：JCX
//	 * 创建时间: 2016年4月6日 下午1:52:53
//	 * @param quetionBankId
//	 * @return
//	 */
//	@GET
//	@Path("/deleteClassificationl")
//	public String deleteClassificationl(@QueryParam("questionClassificationId") String questionClassificationId) {
//		try {
//			questionBankService.deleteClassifications(questionClassificationId);
//			//System.out.println(questionClassificationId);
//			return "{\"result\":\"success\"}";
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return "{\"result\":\"error\"}";
//		}
//	}
	
	/**
	 * 
	 * 功能描述：[查询题库-byid]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午9:45:19
	 * 
	 * @param questionBankId
	 * @return
	 */
	@GET
	@Path("/findQuestionBank")
	public String findQuestionBank(@QueryParam("questionBankId") int questionBankId) {
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(questionBankService.findQuestionBank(questionBankId));
	}

	/**
	 * 
	 * 功能描述：[添加题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:12:10
	 * 
	 * @param questionBankName
	 * @param questionClassificationId
	 * @param filePath
	 * @param adminId
	 * @return
	 */
	@POST
	@Path("/addQuestionBank")
	public String addQuestionBank(@BeanParam TomQuestionBank questionBank) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			questionBank.setCreateTime(new Date());
			questionBank.setUpdateTime(new Date());
			questionBank.setCreator(user.getName());
			questionBank.setCreatorId(user.getCode());
			questionBank.setLastOperator(user.getName());
			questionBank.setLastOperatorId(user.getCode());
			//questionBank.setQuestionBankId(UUID.randomUUID());
			questionBankService.addQuestionBank(questionBank);
		
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			if("rightOptionError".equals(e.getMessage())){
				return "{\"result\":\"请为所有选项设置正确答案\"}";
			}else if("questionTypeError".equals(e.getMessage())){
				return "{\"result\":\"题目类型不合法\"}";
			}else{
				return "{\"result\":\"模板不正确！请仔细检查再提交\"}";
			}
		}
	}

	/**
	 * 
	 * 功能描述：[题库分页查询]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月12日 下午5:34:45
	 * @param pageNum
	 * @param pageSize
	 * @param questionBankName
	 * @return
	 */
//	@GET
//	@Path("/queryBankList")
//	public String queryBankList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
//			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
//			@QueryParam("questionBankName") String questionBankName) {
//		JsonMapper jsonMapper = new JsonMapper();
//		return jsonMapper.toJson(questionBankService.questionBankList(pageNum, pageSize, questionBankName));
//	}
	
	@GET
	@Path("/chooseQuestionBank")
	public String chooseQuestionBank(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("questionBankName") String questionBankName){
		JsonMapper jsonMapper = new JsonMapper();
		return jsonMapper.toJson(questionBankService.queryQuestionBank(pageNum, pageSize, questionBankName));
	}

	/**
	 * 
	 * 功能描述：[修改题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:12:25
	 * 
	 * @param questionBankId
	 * @param questionBankName
	 * @param questionClassificationId
	 * @param filePath
	 * @param createTime
	 * @param creator
	 * @param adminId
	 * @return
	 */
	@PUT
	@Path("/updateQuestionBank")
	public String updateQuestionBank(@BeanParam TomQuestionBank questionBank) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try {
			questionBank.setUpdateTime(new Date());
			questionBank.setLastOperator(user.getName());
			questionBank.setLastOperatorId(user.getCode());
			if(questionBankService.updateQuestionBank(questionBank).equals("N")){
				return "{\"result\":\"protected\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		
	}

	/**
	 * 
	 * 功能描述：[删除题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月3日 上午11:20:39
	 * 
	 * @param quetionBankId
	 * @return
	 */
	@DELETE
	@Path("/deleteQuestionBank")
	public String deleteQuestionBank(@QueryParam("questionBankId") int questionBankId) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			if(questionBankService.deleteQuestionBank(questionBankId).equals("N")){
				return "{\"result\":\"protected\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
	}

	/**
	 * 
	 * 功能描述：[追加题库]
	 *
	 * 创建者：wjx 创建时间: 2016年3月10日 上午10:11:07
	 * 
	 * @param questionBankId
	 * @param filePath
	 * @return
	 */
	@POST
	@Path("/superadditionBank")
	public String superadditionBank(@FormParam("questionBankId") int questionBankId,
			@FormParam("filePath") String filePath) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			questionBankService.excle(filePath1+filePath, questionBankId);

		} catch (Exception e) {
			if(e.getMessage().equals("rightOptionError")){
				return "{\"result\":\"请为所有选项设置正确答案\"}";
			}else if(e.getMessage().equals("questionTypeError")){
				return "{\"result\":\"题目类型不合法\"}";
			}else{
				return "{\"result\":\"模板不正确！请仔细检查再提交\"}";
			}
			
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 
	 * 功能描述：[批量删除题库]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月6日 下午1:52:53
	 * @param quetionBankId
	 * @return
	 */
	@GET
	@Path("/deleteQuestionBank")
	public String deleteQuestionBan1k(@QueryParam("quetionBankId") String quetionBankId) {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			if(questionBankService.deleteQuestionBanks(quetionBankId).equals("N")){
				return "{\"result\":\"protected\"}";
			}
			return "{\"result\":\"success\"}";
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
	}
	
	/**
	 * 
	 * 功能描述：[获取题库分类树]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午4:57:37
	 * @return
	 */
	@GET
	@Path("/tree")
	public String getTree(){
		Tree tree=questionClassifyService.getClassifyTree();
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
	
	 /**
     * 
     * 功能描述：[生成题库excel并下载]
     *
     * 创建者：JCX
     * 创建时间: 2016年4月26日 下午5:51:20
     * @param filePath
     * @param fileName
     * @return
     */
    @GET
    @Path("/downloadExcel")
	public Response fileDownloadExcel(@Context HttpServletRequest request,@QueryParam("questionBankId") int questionBankId){
    	try {
	    	TomQuestionBank questionBank = questionBankService.findQuestionBank(questionBankId);
	    	String fileName=questionBank.getQuestionBankName();
	    	String path=questionBankService.TopicsToExcel(questionBank.getTopics(), fileName);
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
	        response.header("Content-Disposition","attachment; filename=\""+downFileName+".xls"+"\"");//
	        return response.build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return null;
	}
}
