package com.chinamobo.ue.course.restful;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.course.dao.TomCourseClassifyRelationMapper;
import com.chinamobo.ue.course.dto.CopyOnlineCourse;
import com.chinamobo.ue.course.entity.TomCourseClassifyRelation;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程rest
 *
 * 作者: JCX
 * 创建时间: 2016年3月3日 下午3:41:04
 */
@Path("/course") 
@Scope("request") 
@Component
public class CourseRest {

	private static Logger logger = LoggerFactory.getLogger(CourseRest.class);
	@Autowired
	private CourseService courseService;
	@Autowired
	private TomCourseClassifyRelationMapper courseClassifyRelationMapper;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	@Autowired
    private TomRollingPictureMapper rollingPictureMapper;
	
	ObjectMapper mapper = new ObjectMapper(); 
	ShiroUser user=ShiroUtils.getCurrentUser();
	/**
	 * 
	 * 功能描述：[查询课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:41:20
	 * @param courseId
	 * @return
	 */
	@GET
	@Path("/find")
	public String findCourse (@QueryParam("courseId") int courseId){	
		try {
			String json= mapper.writeValueAsString(courseService.selectCourseDetail(courseId));
			return json;
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(),e);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(),e);
		}	
		return "{\"result\":\"error\"}";
	}
	
	@GET
	@Path("/test")
	public String findCourse1 (@QueryParam("courseId") int courseId){	
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		classifyRelationExample.setCourseId(courseId);
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		return "ok";
	}
	
	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:41:35
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @param courseType
	 * @param courseOnline
	 * @param status
	 * @return
	 */
	@GET
	@Path("/findPage")
	public String findCourseByPage (@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,
			@QueryParam("courseName") String courseName,@QueryParam("courseType") String courseType,
			@QueryParam("courseOnline") String courseOnline,@QueryParam("lecturers") String lecturers,@QueryParam("status") String status){

		try {
			if(courseName!=null){
				courseName=courseName.replaceAll("/", "//");
				courseName=courseName.replaceAll("%", "/%");
				courseName=courseName.replaceAll("_", "/_");
				
			}
			
			TomCourses example=new TomCourses();
			example.setLecturers(lecturers);
			example.setCourseName(courseName);
			if(!"".equals(courseType) && courseType!=null){
				example.setCourseType(","+courseType+",");
			}
			example.setCourseOnline(courseOnline);
			example.setStatus(status);
			
			String json= mapper.writeValueAsString(courseService.selectListByPage(pageNum, pageSize,example));
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}	
		return "{\"result\":\"error\"}";
	}
	@GET
	@Path("/findclasses")
	public String findclasses(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize
			,@QueryParam("courseId") String courseId){
		try {
			String json= mapper.writeValueAsString(courseService.findClasses(courseId,pageNum,pageSize));
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
	/**
	 * 
	 * 功能描述：[根据人员id分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:41:35
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @param courseType
	 * @param courseOnline
	 * @param status
	 * @return
	 */
	
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:27:46
	 * @param courses
	 * @return
	 */
	@POST
	@Path("/add")
	public String addCourse(@BeanParam TomCourses courses){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		courses.setCreator(user.getName());
		courses.setCreatorId(user.getCode());
		courses.setOperator(user.getName());
		//courses.setCourseDownloadable("N");
		try {
			courseService.addCourse(courses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	@POST
	@Path("/addCourse")
	public String addCourseNew(String json){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			courseService.addCourseNew(json);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:28:08
	 * @param courses
	 * @return
	 */
	@PUT
	@Path("/edit")
	public String editCourse(@BeanParam TomCourses courses){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomActivityProperty example=new TomActivityProperty();
		example.setCourseId(courses.getCourseId());
//		if(activityPropertyMapper.selectByExample(example).size()>0){
//			return "{\"result\":\"inActivity\"}";
//		}
		courses.setOperator(user.getName());
		//courses.setCourseDownloadable("N");
		try {
			courseService.updateCourse(courses);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	@PUT
	@Path("editCourse")
	public String editCourseNew(String json){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			courseService.updateNew(json);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	/**
	 * 
	 * 功能描述：[上架/下架]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:42:15
	 * @param courseId
	 * @param operator
	 * @param status
	 * @return
	 */
	@PUT
	@Path("/updateStatus")
	public String downShelf (@QueryParam("courseId") int courseId,@QueryParam("status") String status){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		//是否在活动中
		TomActivityProperty example=new TomActivityProperty();
		example.setCourseId(courseId);
		if(activityPropertyMapper.selectByExample(example).size()>0){
			return "{\"result\":\"inActivity\"}";
		}
		//是否在轮播图中
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("resourceId", courseId);
		map.put("pictureCategory", "C");
		map.put("isRelease", "Y");
		int count=rollingPictureMapper.countByPage(map);
		if(count!=0){
			return "{\"result\":\"protected\"}";
		}
		TomCourses course=new TomCourses();	
		course.setCourseId(courseId);
		course.setStatus(status);	
		course.setOperator(user.getName());
		courseService.updateStatus(course);
		
		return "{\"result\":\"success\"}";
	}
	
	@Path("/addOffline")
	@POST
	public String addOffline(String json){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			courseService.addOffline(json);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	
	@Path("/editOffline")
	@PUT
	public String editOffline(String json){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			courseService.editOffline(json);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	@Path("/copyCourseOffline")
	@PUT
	public String copyCourseOffline(@QueryParam("courseId") int courseId){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			courseService.copyCourseOffline(courseId);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
	@Path("/findOffline")
	@GET
	public String findOffline(@QueryParam("courseId") int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String json="";
		try {
			json=mapper.writeValueAsString(courseService.findOffline(courseId));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return json;
	}
	
	@Path("/findApplyList")
	@GET
	public String findApplyList(@DefaultValue("1") @QueryParam("pageNum") int pageNum,@DefaultValue("20") @QueryParam("pageSize") int pageSize,@QueryParam("courseId") int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String json="";
		try {
			json=mapper.writeValueAsString(courseService.findApplyList(pageNum,pageSize,courseId));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return json;
	}
	
	@Path("/importApplyResults")
	@GET
	public String importApplyResults(@QueryParam("courseId") int courseId,@QueryParam("filePath") String filePath){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			return "{\"result\":\""+courseService.importResults(courseId,Config.getProperty("file_path")+filePath)+"\"}";
		}catch(Exception e){
			logger.error(e.getMessage());
			return "{\"result\":\"error\"}";
		}
	}
	/**
	 * 
	 * 功能描述：[复制线上课程]
	 * 创建者：Acemon
	 * 创建时间：2017年7月25日
	 */
	@PUT
	@Path("/copyOnlineCourse")
	public String copyOnlineCourse(@QueryParam("courseId") int courseId){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		try{
			courseService.copyOnlineCourse(courseId);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return "{\"result\":\"error\"}";
		}
		return "{\"result\":\"success\"}";
	}
}
