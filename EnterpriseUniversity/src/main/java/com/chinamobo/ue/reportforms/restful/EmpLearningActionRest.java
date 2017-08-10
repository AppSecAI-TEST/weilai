package com.chinamobo.ue.reportforms.restful;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.reportforms.dto.EmpActivityAnswerDetailDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.reportforms.dto.ParamterDto;
import com.chinamobo.ue.reportforms.dto.PingtaideptDto;
import com.chinamobo.ue.reportforms.dto.StuLearnResourceDto;
import com.chinamobo.ue.reportforms.service.EmpLearningActionService;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/empLearningAction")
@Scope("request")
@Component
public class EmpLearningActionRest {
	@Autowired
	private EmpLearningActionService empLearningActionService;
	
	ObjectMapper mapper = new ObjectMapper(); 
	/**
	 * 创建者：Acemon
	 * 功能描述：学员学习行为统计报表
	 * 创建时间：2017-02-28
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/empLearningActionList")
	public String empLearningActionList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("name") String name,@QueryParam("code") String code,@QueryParam("threeCode") String threeCode,
			@QueryParam("twoCode") String twoCode,@QueryParam("onedeptname") String onedeptname,@QueryParam("orgname") String orgname) throws Exception{
		JsonMapper jsonMapper = new JsonMapper();
		PageData<EmpLearningActionDto> empLearningAction = empLearningActionService.queryEmpLearningAction(pageNum, pageSize, beginTimeq, endTimeq,name,code,threeCode,twoCode,onedeptname,orgname);
		String json = jsonMapper.toJson(empLearningAction);
		return json;
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询所属组织
	 * 创建时间：2017-03-08
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/downloadempLearningActionExcel")
	public Response downloadempLearningActionExcel(@Context HttpServletResponse response1,@Context HttpServletRequest request,
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("beginTimeq") String beginTimeq,@QueryParam("endTimeq") String endTimeq,
			@QueryParam("name") String name,@QueryParam("code") String code,@QueryParam("threeCode") String threeCode,
			@QueryParam("twoCode") String twoCode,@QueryParam("onedeptname") String onedeptname,@QueryParam("orgname") String orgname) throws Exception{
		
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<EmpLearningActionDto> list = empLearningActionService.queryEmpLearningAction(pageNum, pageSize, beginTimeq, endTimeq, name, code, threeCode, twoCode, onedeptname, orgname).getData();
			String fileName = "学习行为统计.xls";
			String path = empLearningActionService.LearningActionBycodeExcel(list, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName;
			String userAgent = request.getHeader("USER-AGENT");
			if (userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1) {
				downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else{
				downFileName = URLEncoder.encode(fileName, "UTF8");
			}
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie empLearningActionCookie = new NewCookie("empLearningAction","ok","/",null,null,1,false);
			response.cookie(empLearningActionCookie);
	        return response.build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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
		List<TomOrg> list = empLearningActionService.findOrgname();
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
		List<TomDept> list = empLearningActionService.findFirstDeptName(orgcode);
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
		List<TomDept> list = empLearningActionService.findSecondDeptName(topcode);
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
	 * 创建时间：2017-03-06
	 * @return
	 */
	@GET
	@Path("/findThirdDeptName")
	public String findThirdDeptName(@QueryParam("code") String topcode){
		List<TomDept> list = empLearningActionService.findThirdDeptName(topcode);
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
		List<TomProjectClassify> list = empLearningActionService.findActivityClassify();
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
	 * 功能描述：根据项目id查找活动
	 * 创建时间：2017-03-06
	 * @return
	 */
	@GET
	@Path("/findActivityName")
	public String findActivityName(@QueryParam("projectId") String projectId){
		List<TomActivity> list = empLearningActionService.findActivityName(projectId);
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
	 * 功能描述：问卷明细
	 * 创建时间：2017-03-11
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/findQuestionnaireDetailList")
	public String findQuestionnaireDetailList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("completedTime") String completedTime,
			@QueryParam("examName") String examName,@QueryParam("projectId") String projectId,@QueryParam("isRequired") String isRequired,
			@QueryParam("activityName") String activityName,@QueryParam("activityStatus") String activityStatus,
			@QueryParam("name") String name,@QueryParam("code") String code,@QueryParam("threeCode") String threeCode,
			@QueryParam("twoCode") String twoCode,@QueryParam("onedeptname") String onedeptname,@QueryParam("orgname") String orgname) throws Exception{
		PageData<EmpActivityAnswerDetailDto> list = empLearningActionService.findQuestionnaireDetailList(pageNum, pageSize, completedTime,examName,projectId,isRequired,activityName,activityStatus, name, code, threeCode, twoCode, onedeptname, orgname);
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
	 * 功能描述：问卷明细
	 * 创建时间：2017-03-23
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/downLoadQuestionnaireDetailExcel")
	public Response downLoadQuestionnaireDetailExcel(@Context HttpServletResponse response1,@Context HttpServletRequest request,
			@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("-1") @QueryParam("pageSize") int pageSize,@QueryParam("completedTime") String completedTime,
			@QueryParam("examName") String examName,@QueryParam("projectId") String projectId,@QueryParam("isRequired") String isRequired,
			@QueryParam("activityName") String activityName,@QueryParam("activityStatus") String activityStatus,
			@QueryParam("name") String name,@QueryParam("code") String code,@QueryParam("threeCode") String threeCode,
			@QueryParam("twoCode") String twoCode,@QueryParam("onedeptname") String onedeptname,@QueryParam("orgname") String orgname) throws Exception{
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			List<EmpActivityAnswerDetailDto> empActivityAnswerDetailDto = empLearningActionService.findQuestionnaireDetailList(pageNum, pageSize, completedTime, examName, projectId, isRequired, activityName, activityStatus, name, code, threeCode, twoCode, onedeptname, orgname).getData();
			String fileName = "问卷-学员内容.xls";
			String path = empLearningActionService.QuestionnaireDetailExcel(empActivityAnswerDetailDto, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			String downFileName; 
			String userAgent = request.getHeader("USER-AGENT");
			if (userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1) {
				downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else{
				downFileName = URLEncoder.encode(fileName, "UTF8");
			}
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie questionnaireDetailCookie = new NewCookie("questionnaireDetail","ok","/",null,null,1,false);
			response.cookie(questionnaireDetailCookie);
	        return response.build();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("/orgPingtaiList")
	public String orgPingtaiList(@QueryParam("beginTime") String startTime,@QueryParam("endTime") String endTime,
			@QueryParam("onedeptname") String onedeptId,@QueryParam("deptname") String deptcode,@QueryParam("thirdName") String thirdcode){
		JsonMapper jsonMapper = new JsonMapper();
		if("".equals(thirdcode) || null== thirdcode){
			thirdcode =deptcode;
			deptcode = "";
		}
		return jsonMapper.toJson(empLearningActionService.findActityCostList(startTime,endTime,onedeptId,deptcode,thirdcode));
	}
	
	@GET
	@Path("/stuLearnResourcesList")
	public String stuLearnResourcesList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,
			@DefaultValue("20") @QueryParam("pageSize") int pageSize, @QueryParam("beginTimeq") String startTime,@QueryParam("endTimeq") String endTime
			,@QueryParam("onedeptname") String onedeptId,@QueryParam("deptname") String deptcode,@QueryParam("thirdName") String thirdcode,@QueryParam("name") String username
			,@QueryParam("columes") String columes){
		
		JsonMapper jsonMapper = new JsonMapper();
		ParamterDto paramter  = new ParamterDto();
		paramter.setStartTime(startTime);
		paramter.setEndTime(endTime);
		paramter.setOnedeptId(onedeptId);
		paramter.setDeptId(deptcode);
		//if(!"thirdcode".equals(thirdcode) &&!"undefined".equals(thirdcode)){
			paramter.setThirdcode(thirdcode);
		
		paramter.setUserName(username);
		return jsonMapper.toJson(empLearningActionService.stuResourceseList(pageSize,pageNum,paramter,columes));
	}
	
	@GET
	@Path("/downLoadStuleranResourceExcel")
	public Response downLoadStuleranResourceExcel(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@Context HttpServletRequest request,
			@DefaultValue("-1") @QueryParam("pageSize") int pageSize, @QueryParam("beginTimeq") String startTime,@QueryParam("endTimeq") String endTime,
			@QueryParam("deptname") String deptname,@QueryParam("name") String username ,@QueryParam("columes") String columes,
			@QueryParam("thirdName") String thirdcode,@QueryParam("onedeptname") String onedeptname,@QueryParam("orgname") String orgname) throws Exception{
		try {	
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			JsonMapper jsonMapper = new JsonMapper();
			Map<String,String> map0 = new LinkedHashMap<String,String>();
			Map<String,String> map1 = new LinkedHashMap<String,String>();
			Map<String,String> map2 = new LinkedHashMap<String,String>();
			Map<String,String> map3 = new LinkedHashMap<String,String>();
			Map<String,String> map4 = new LinkedHashMap<String,String>();
			Map<String,String> map5 = new LinkedHashMap<String,String>();
			Map<String,String> map6 = new LinkedHashMap<String,String>();
			Map<String,String> map7 = new LinkedHashMap<String,String>();
			Map<String,String> map = new LinkedHashMap<String,String>();
			map0.put("userName", "用户名");map0.put("orgName", "所属组织");map0.put("onedept", "一级部门");map0.put("twodept", "二级部门");map0.put("threedept", "三级部门");map0.put("empEmail", "邮箱");//map0.put("rolename", "管理角色分配");map0.put("job", "职务");map0.put("jobtier", "职级");
			map1.put("coursenuma", "被分配课程数");map1.put("startCoursenuma", "已开始课程数");map1.put("notAtCoursenuma", "未开始课程");map1.put("finishCoursenuma", "已完成课程数");map1.put("courseAlltimea", "总学习时长");map1.put("avgCoulearnTimea", "平均学习时长");map1.put("avgfinishratea", "平均完成率");
			map1.put("avetempoa", "平均学习进度");map1.put("needExamnuma", "需参加考试总数");map1.put("finExamnuma", "已完成考试数");map1.put("unfinExamnuma", "未完成考试数");map1.put("exampassratea", "通过考试数");map1.put("avgExampassratea", "平均考试通过率");
			
			map2.put("coursenumb", "被分配课程数");map2.put("startCoursenumb", "已开始课程数");map2.put("notAtCoursenumb", "未开始课程");map2.put("finishCoursenumb", "已完成课程数");map2.put("courseAlltimeb", "总学习时长");map2.put("avgCoulearnTimeb", "平均学习时长");map2.put("avgfinishrateb", "平均完成率");
			map2.put("avetempob", "平均学习进度");map2.put("needExamnumb", "需参加考试总数");map2.put("finExamnumb", "已完成考试数");map2.put("unfinExamnumb", "未完成考试数");map2.put("exampassrateb", "通过考试数");map2.put("avgExampassrateb", "平均考试通过率");map2.put("needEbb", "需获得积分");map2.put("getEbb", "已获得总积分");
			
			map3.put("coursenumc", "被分配课程数");map3.put("startCoursenumc", "已开始课程数");map3.put("notAtCoursenumc", "未开始课程");map3.put("finishCoursenumc", "已完成课程数");map3.put("courseAlltimec", "总学习时长");map3.put("avgCoulearnTimec", "平均学习时长");map3.put("avgfinishratec", "平均完成率");
			map3.put("avetempoc", "平均学习进度");map3.put("needExamnumc", "需参加考试总数");map3.put("finExamnumc", "已完成考试数");map3.put("unfinExamnumc", "未完成考试数");map3.put("exampassratec", "通过考试数");map3.put("avgExampassratec", "平均考试通过率");map3.put("needEbc", "需获得积分");map3.put("getEbc", "已获得总积分");
			
			map4.put("coursenumd", "被分配课程数");map4.put("openCoursenumd", "被开放课程数");map4.put("applyCoursenumd", "已报名课程数");map4.put("unappCoursenumd", "未报名课程数");map4.put("signCoursenumd", "已签到课程数");map4.put("unsignCoursenumd", "未签到课程数");map4.put("avgaApprated", "平均报名率");
			map4.put("avetSignrated", "平均签到率");map4.put("needEbd", "需获得积分");map4.put("getEbd", "已获得总积分");
			
			map5.put("coursenume", "被分配课程数");map5.put("openCoursenume", "被开放课程数");map5.put("applyCoursenume", "已报名课程数");map5.put("unappCoursenume", "未报名课程数");map5.put("signCoursenume", "已签到课程数");map5.put("unsignCoursenume", "未签到课程数");map5.put("avgaAppratee", "平均报名率");
			map5.put("avetSignratee", "平均签到率");map5.put("needEbe", "需获得积分");map5.put("getEbe", "已获得总积分");
			map7.put("sumResnum", "总学习资源数");map7.put("onlineResnum", "线上学习资源数");map7.put("offlineResnum", "线下学习资源数");map7.put("onlineExamnum", "线上考试数");map7.put("onlinelearnAlltime", "线上总学习时长");map7.put("avgOnschedule", "线上平均学习进度");map7.put("avgOnfinishrate", "线上平均完成率");
			map7.put("onExamfinishrate", "线上考试通过率");map7.put("offcourseApplyrate", "线下课程报名率");map7.put("offcourseSign", "线下课程签到率");
			
			//List<EmpActivityAnswerDetailDto> empActivityAnswerDetailDto = empLearningActionService.findQuestionnaireDetailList(pageNum, pageSize, completedTime, examName, projectId, isRequired, activityName, activityStatus, name, code, deptname, depttopname, onedeptname, orgname).getData();
			ParamterDto paramter  = new ParamterDto();
			paramter.setStartTime(startTime);
			paramter.setEndTime(endTime);
			paramter.setOnedeptId(onedeptname);
			paramter.setDeptId(deptname);
			if(!"thirdcode".equals("") &&!"undefined".equals(thirdcode)){
				paramter.setDeptId(thirdcode);
			}
			
			paramter.setUserName(username);
			List<StuLearnResourceDto> StuLearnResourceDto =  empLearningActionService.stuResourceseList(pageSize,pageNum,paramter,columes).getData();
			map.putAll(map0);
			map6.put("m0_"+map0.size(), "用户信息");
			map.putAll(map7);
			map6.put("m0_"+map7.size(), "总学习资源");
			if(!"".equals(columes) && !"undefind".equals(columes) && null!=columes){
				String[] fields = columes.split(",");
				
				for(int i=0;i<fields.length;i++){
					if("mokuai1".equals(fields[i])){
						map.putAll(map1);
						map6.put("m1_"+map1.size(), "必修活动内线上课程和考试");
					}
					if("mokuai2".equals(fields[i])){
						map.putAll(map2);
						map6.put("m2_"+map2.size(), "选修活动内线上课程和考试");
					}
					if("mokuai3".equals(fields[i])){
						map.putAll(map3);
						map6.put("m3_"+map3.size(), "非活动内线上课程和考试");
					}
					if("mokuai4".equals(fields[i])){
						map.putAll(map4);
						map6.put("m4_"+map4.size(), "必修活动内线下课程");
					}
					if("mokuai5".equals(fields[i])){
						map.putAll(map5);
						map6.put("m5_"+map5.size(), "选修活动内线下课程");
					}
				}
				
			}
			map.put("1", "1");
			map.putAll(map6);
			String fileName = "学员学习统计.xls";
			String path = empLearningActionService.stuLearnResourceDetailExcelnew(StuLearnResourceDto, fileName,map);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			//String downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			String downFileName = "";
			String userAgent = request.getHeader("USER-AGENT");
			if(userAgent.indexOf("Firefox")!=-1 ){	
				downFileName=new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else {
				downFileName=URLEncoder.encode(fileName,"UTF8");
			}
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie stuleranResourceCookie = new NewCookie("stuleranResource","ok","/",null,null,1,false);
			response.cookie(stuleranResourceCookie);
	        return response.build();
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			return null;
	}
	
	@GET
	@Path("/downLoadPingtaiExcel")
	public Response downLoadPingtaiExcel(@QueryParam("beginTime") String startTime,@QueryParam("endTime") String endTime,@Context HttpServletRequest request,
			@QueryParam("onedeptname") String onedeptId,@QueryParam("deptname") String deptcode,@QueryParam("thirdName") String thirdcode) throws Exception{
		try {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			PingtaideptDto list = empLearningActionService.findActityCostList(startTime,endTime,onedeptId,deptcode,thirdcode);
			String fileName = "平台统计.xls";
			String path = empLearningActionService.downLoadPingtaiExcel(list, fileName);
			File file= new File(path);
			ResponseBuilder response = Response.ok((Object) file);
			/*String downFileName;
			downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");*/
			String downFileName = "";
			String userAgent = request.getHeader("USER-AGENT");
			if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1 ){	
				downFileName=new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else {
				downFileName=URLEncoder.encode(fileName,"UTF8");
			}
			response.header("Content-Type", "application/text");
			response.header("Content-Disposition",
	                "attachment; filename=\""+downFileName+"\"");
			//当导出完毕向客户端添加Cookie标识;
			NewCookie PingtaiCookie = new NewCookie("Pingtai","ok","/",null,null,1,false);
			response.cookie(PingtaiCookie);
	        return response.build();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
