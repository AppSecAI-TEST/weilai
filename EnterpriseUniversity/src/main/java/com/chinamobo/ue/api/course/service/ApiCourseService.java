package com.chinamobo.ue.api.course.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.api.activity.service.ActivityApiService;
import com.chinamobo.ue.api.course.dto.CourseSectionLearningRecord;
import com.chinamobo.ue.api.course.dto.CourseTeacherGradeResults;
import com.chinamobo.ue.api.course.dto.GradeResult;
import com.chinamobo.ue.api.course.dto.MyCourse;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.MD5Util;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.commodity.service.CommodityService;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseApplyMapper;
import com.chinamobo.ue.course.dao.TomCourseClassesMapper;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseCnEuMapper;
import com.chinamobo.ue.course.dao.TomCourseCommentMapper;
import com.chinamobo.ue.course.dao.TomCourseDetailMapper;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomGradeDimensionMapper;
import com.chinamobo.ue.course.dao.TomGradeRecordsMapper;
import com.chinamobo.ue.course.dao.TomLearnTimeLogMapper;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.dao.TomOfflineSignMapper;
import com.chinamobo.ue.course.dao.TomTestMapper;
import com.chinamobo.ue.course.dto.OfflineCourseDto;
import com.chinamobo.ue.course.dto.TomTestDto;
import com.chinamobo.ue.course.entity.TomCourseApply;
import com.chinamobo.ue.course.entity.TomCourseClasses;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseCnEu;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.course.entity.TomCourseCommentThumbUp;
import com.chinamobo.ue.course.entity.TomCourseDetail;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.course.entity.TomCourseSectionLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourseThumbUp;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomFavoriteCourse;
import com.chinamobo.ue.course.entity.TomGradeDimension;
import com.chinamobo.ue.course.entity.TomGradeRecords;
import com.chinamobo.ue.course.entity.TomLearnTimeLog;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.course.entity.TomOfflineSign;
import com.chinamobo.ue.course.entity.TomTest;
import com.chinamobo.ue.course.service.CourseCommentService;
import com.chinamobo.ue.course.service.CourseSectionService;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.course.service.TestService;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomMessageDetailsMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dao.TomUserLogMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.entity.WxMessage;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.system.service.UserInfoServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.PathUtil;
import com.chinamobo.ue.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 版本: [1.0] 功能说明: 课程接口实现 selectTaskStart 作者: ZXM 创建时间: 2016年3月16日 下午4:36:48
 */
@Service
public class ApiCourseService {
	@Autowired
	private CourseSectionService courseSectionService;
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	@Autowired
	private TomCourseClassifyMapper CourseClassifyMapper;

	@Autowired
	private TomLecturerMapper lecturerMapper;

	@Autowired
	private TomCourseCommentMapper courseCommentMapper;

	@Autowired
	private TomUserInfoMapper userInfoMapper;

	@Autowired
	private TomUserLogMapper userLogMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomCourseSectionMapper courseSectionMapper;

	@Autowired
	private TomEmpMapper empMapper;

	@Autowired
	private TomGradeRecordsMapper gradeRecordsMapper;

	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;

	@Autowired
	private TomGradeDimensionMapper gradeDimensionMapper;

	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;

	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;

	@Autowired
	private TomActivityMapper activityMapper;

	@Autowired
	private TomCourseSectionLearningRecordMapper courseSectionLearningRecordMapper;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;

	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private ActivityApiService activityApiService;

	@Autowired
	private TomLearnTimeLogMapper learnTimeLogMapper;

	@Autowired
	private TomTestMapper tomTestMapper;

	@Autowired
	private CourseService courseService;
	@Autowired
	private CourseCommentService courseCommentService;
	@Autowired
	private CommodityService commodityService;
	@Autowired
	private UserInfoServise userInfoServise;
	@Autowired
	private TestService testService;
	@Autowired
	private TomActivityEmpsRelationMapper tomActivityEmpsRelationMapper;
	@Autowired
	private TomCoursesMapper tomCoursesMapper;
	@Autowired
	private TomActivityPropertyMapper tomActivityPropertyMapper;
	@Autowired
	private TomCourseLearningRecordMapper tomCourseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper tomCourseSignInRecordsMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;

	@Autowired
	private TomCourseClassesMapper classesMapper;
	@Autowired
	private TomCourseApplyMapper applyMapper;
	@Autowired
	private TomOfflineSignMapper signMapper;
	@Autowired
	private TomCourseDetailMapper detailMapper;

	@Autowired
	private TomCourseCnEuMapper courseCnEuMapper;

	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * 功能描述：4.3.时长排行榜
	 *
	 * 创建者：ZXM 创建时间: 2016年3月24日下午1:39:09
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleTopTimeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomUserInfo> page = new PageData<TomUserInfo>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		TomUserInfo example = new TomUserInfo();
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
		int count = userInfoMapper.countByExample(example);
		List<TomUserInfo> list = userInfoMapper.selectTopTime(map);
		for (TomUserInfo userInfo : list) {
			TomEmp emp = empMapper.selectByPrimaryKey(userInfo.getCode());
			userInfo.setName(emp.getName());
			userInfo.setDeptName(emp.getDeptname());
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");

	}

	/**
	 * 
	 * 功能描述：4.4.学习头条
	 *
	 * 创建者：ZXM 创建时间: 2016年3月23日下午6:01:00
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleLearningHeadlines(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<TomCourseLearningRecord> list = coursesMapper.selectCourseLearningNO3();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String name = "";
		JSONArray ja = new JSONArray();
		for (TomCourseLearningRecord courseLearning : list) {
			JSONObject jo = new JSONObject();
			name = "";
			// 查询员工详细信息
			TomEmp emp = empMapper.selectByPrimaryKey(courseLearning.getCode());
			TomCourses courses = coursesMapper.selectByPrimaryKey(courseLearning.getCourseId());
			if (null != emp && null != courses) {
				name = "【" + emp.getName() + "】" + emp.getDeptname() + "，刚刚学完《" + courses.getCourseName() + "》";
			} else {
				name = "【未知】，刚刚学完《未知》";
			}
			jo.put("name", name);
			ja.add(jo);
		}
		JSONArray ja1 = new JSONArray();
		JSONObject jo1 = new JSONObject();
		jo1.put("name", "欢迎来到蔚乐学！");
		ja1.add(jo1);
		jo1.put("name", "Welcome to Welearn!");
		ja1.add(jo1);
		String pageJson = mapper.writeValueAsString(ja1);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：4.8.在线课程
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日上午11:33:53
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleOnlineCourseList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String lang = request.getParameter("lang");
		String terminal = request.getParameter("terminal");
		PageData<TomCourses> page = new PageData<TomCourses>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		TomCourses example = new TomCourses();
		String keyWord = request.getParameter("KeyWord");
		if (keyWord != null) {
			keyWord = keyWord.replaceAll("/", "//");
			keyWord = keyWord.replaceAll("%", "/%");
			keyWord = keyWord.replaceAll("_", "/_");

		}
		String userId = request.getParameter("userId");

		example.setCode(userId);
		if ("en".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPcen("Y");
			} else {
				example.setH5en("Y");
			}
			example.setCourseNameEn(keyWord);
		} else if ("cn".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPccn("Y");
			} else {
				example.setH5cn("Y");
			}
			example.setCourseName(keyWord);
		}
		if (!"".equals(request.getParameter("classifyId")) && request.getParameter("classifyId") != null) {
			example.setCourseType("," + request.getParameter("classifyId") + ",");
		}
		example.setIsExcellentCourse(request.getParameter("isExcellentCourse"));
		example.setStatus("Y");
		example.setCourseOnline("Y");
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));// (int)map.get("startNum")+
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
		int count = coursesMapper.countByExampleApi(example);
		List<TomCourses> list = coursesMapper.selectListByPageApi(map);
		// list.addAll(coursesMapper.selectOpen());//加入公开课
		for (TomCourses tomCourses : list) {
			if ("en".equals(lang)) {
				tomCourses.setCourseName(tomCourses.getCourseNameEn());
				tomCourses.setCourseIntroduction(tomCourses.getCourseIntroductionEn());
				tomCourses.setCourseImg(tomCourses.getCourseImgEn());
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：4.9.课程分类
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日上午11:46:31
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseClassifyList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String lang = request.getParameter("lang");
		List<TomCourseClassify> list = CourseClassifyMapper.selectByParentId(0);
		String userId = request.getParameter("userId");
		for (TomCourseClassify courseClassify : list) {
			// TomCourses courseExample=new TomCourses();
			// courseExample.setCourseType(","+String.valueOf(courseClassify.getClassifyId())+",");
			// int courseCounts=coursesMapper.countByExample(courseExample);

			TomCourses example = new TomCourses();
			example.setCode(userId);
			example.setCourseType("," + String.valueOf(courseClassify.getClassifyId()) + ",");
			example.setStatus("Y");
			String terminal = request.getParameter("terminal");
			if ("en".equals(lang)) {
				if ("pc".equals(terminal)) {
					example.setPcen("Y");
				} else {
					example.setH5en("Y");
				}
			} else if ("cn".equals(lang)) {
				if ("pc".equals(terminal)) {
					example.setPccn("Y");
				} else {
					example.setH5cn("Y");
				}
			}
			example.setCourseOnline("Y");
			if (request.getParameter("isExcellentCourse") != null) {
				example.setIsExcellentCourse(request.getParameter("isExcellentCourse"));
			}
			if ("en".equals(lang)) {
				example.setCourseName(example.getCourseNameEn());
				example.setCourseIntroduction(example.getCourseIntroductionEn());
				courseClassify.setClassifyName(courseClassify.getClassifyNameEn());
				courseClassify.setClassifyIntroduction(courseClassify.getClassifyIntroductionEn());
			}
			int courseCounts = coursesMapper.countByExampleApi(example);
			courseClassify.setCourseCounts(courseCounts);
		}
		// page.setDate(list);
		// page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(list);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	public Result eleOfflineCourseClassifyList(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String lang = request.getParameter("lang");
		List<TomCourseClassify> list = CourseClassifyMapper.selectByParentId(0);
		String userId = request.getParameter("userId");
		for (TomCourseClassify courseClassify : list) {
			// TomCourses courseExample=new TomCourses();
			// courseExample.setCourseType(","+String.valueOf(courseClassify.getClassifyId())+",");
			// int courseCounts=coursesMapper.countByExample(courseExample);

			TomCourses example = new TomCourses();
			example.setCode(userId);
			example.setCourseType("," + String.valueOf(courseClassify.getClassifyId()) + ",");
			example.setStatus("Y");
			example.setCourseOnline("N");
			String terminal = request.getParameter("terminal");
			if ("en".equals(lang)) {
				if ("pc".equals(terminal)) {
					example.setPcen("Y");
				} else {
					example.setH5en("Y");
				}
			} else if ("cn".equals(lang)) {
				if ("pc".equals(terminal)) {
					example.setPccn("Y");
				} else {
					example.setH5cn("Y");
				}
			}
			if (request.getParameter("isExcellentCourse") != null) {
				example.setIsExcellentCourse(request.getParameter("isExcellentCourse"));
			}
			if ("en".equals(lang)) {
				example.setCourseName(example.getCourseNameEn());
				example.setCourseIntroduction(example.getCourseIntroductionEn());
				courseClassify.setClassifyName(courseClassify.getClassifyNameEn());
				courseClassify.setClassifyIntroduction(courseClassify.getClassifyIntroductionEn());
			}
			int courseCounts = coursesMapper.countByExampleApi(example);
			courseClassify.setCourseCounts(courseCounts);
		}
		// page.setDate(list);
		// page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(list);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：4.6.精品课程
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日下午3:57:10
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleBoutiqueCourseList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String lang = request.getParameter("lang");
		PageData<TomCourses> page = new PageData<TomCourses>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		TomCourses example = new TomCourses();
		String userId = request.getParameter("userId");
		example.setCode(userId);
		if (!"".equals(request.getParameter("classifyId")) && request.getParameter("classifyId") != null) {
			example.setCourseType("," + request.getParameter("classifyId") + ",");
		}
		example.setIsExcellentCourse("Y");
		example.setCourseOnline("Y");
		example.setStatus("Y");
		if ("en".equals(lang)) {
			example.setCourseName(example.getCourseNameEn());
			example.setCourseIntroduction(example.getCourseIntroductionEn());
		}
		String terminal = request.getParameter("terminal");
		if ("en".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPcen("Y");
			} else {
				example.setH5en("Y");
			}
		} else if ("cn".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPccn("Y");
			} else {
				example.setH5cn("Y");
			}
		}
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
		int count = coursesMapper.countByExampleApi(example);
		List<TomCourses> list = coursesMapper.selectListByPageApi(map);
		for (TomCourses coures : list) {
			if ("en".equals(lang)) {
				coures.setCourseName(coures.getCourseNameEn());
				coures.setLecturerName(coures.getLecturerNameEn());
				coures.setCourseIntroduction(coures.getCourseIntroductionEn());
				coures.setCourseImg(coures.getCourseImgEn());
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：4.5.最新课程
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日下午4:24:38
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleNewCourseList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String lang = request.getParameter("lang");
		PageData<TomCourses> page = new PageData<TomCourses>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		TomCourses example = new TomCourses();
		String userId = request.getParameter("userId");
		example.setCode(userId);
		example.setCourseOnline("Y");
		example.setStatus("Y");
		if ("en".equals(lang)) {
			example.setCourseName(example.getCourseNameEn());
			example.setCourseIntroduction(example.getCourseIntroductionEn());
		}
		String terminal = request.getParameter("terminal");
		if ("en".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPcen("Y");
			} else {
				example.setH5en("Y");
			}
		} else if ("cn".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPccn("Y");
			} else {
				example.setH5cn("Y");
			}
		}
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", "update_time");
		map.put("orderBy", "desc");
		int count = coursesMapper.countByExampleApi(example);
		List<TomCourses> list = coursesMapper.selectListByPageApi(map);
		for (TomCourses coures : list) {
			if ("en".equals(lang)) {
				coures.setCourseName(coures.getCourseNameEn());
				coures.setLecturerName(coures.getLecturerNameEn());
				coures.setCourseIntroduction(coures.getCourseIntroductionEn());
				coures.setCourseImg(coures.getCourseImgEn());
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.11.课程列表
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日下午4:46:37
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String lang = request.getParameter("lang");
		PageData<TomCourses> page = new PageData<TomCourses>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String userId = request.getParameter("userId");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", userId);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}

		String terminal = request.getParameter("terminal");
		if ("en".equals(lang)) {
			if ("pc".equals(terminal)) {
				map.put("pcen", "Y");
			} else {
				map.put("h5en", "Y");
			}
		} else if ("cn".equals(lang)) {
			if ("pc".equals(terminal)) {
				map.put("pccn", "Y");
			} else {
				map.put("h5cn", "Y");
			}
		}

		map.put("listOrder", "create_time");
		map.put("orderBy", "desc");

		int count = coursesMapper.countTaskCourse(map);
		List<TomCourses> list = coursesMapper.selectTaskCourse(map);
		for (TomCourses course : list) {
			TomActivityProperty activityProperty = new TomActivityProperty();
			activityProperty.setCourseId(course.getCourseId());
			List<TomActivityProperty> proList = activityPropertyMapper.selectByExample(activityProperty);
			String preStatus = activityApiService.getPreStatus(proList, userId);
			course.setPreStatus(preStatus);

			// 判断课程是否需要评分
			TomGradeRecords example = new TomGradeRecords();
			example.setCode(userId);
			example.setCourseId(course.getCourseId());
			example.setGradeType("C");
			List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(example);
			example.setGradeType("L");
			List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(example);
			if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
				course.setIsGrade("Y");
			} else {
				course.setIsGrade("N");
			}
			TomCourseSignInRecords signInExample = new TomCourseSignInRecords();

			if ("Y".equals(course.getCourseOnline())) {
				/*
				 * TomCourseLearningRecord record = new
				 * TomCourseLearningRecord(); record.setCode(userId);
				 * record.setCourseId(course.getCourseId());
				 */
				Map<Object, Object> map1 = new HashMap<Object, Object>();
				// map1.put("learningDate", course.getStartTime());
				map1.put("courseId", course.getCourseId());
				map1.put("code", userId);
				TomCourseLearningRecord learnRecord = courseLearningRecordMapper.selectlearntime(map1);
				if (learnRecord != null) {
					if (format.parse(course.getStartTime()).getTime() > format.parse(learnRecord.getMaxdate())
							.getTime()) {
						course.setStatus("W");// 未开始
					} else if (format.parse(course.getEndTime()).getTime() < format.parse(learnRecord.getMindate())
							.getTime()) {
						course.setStatus("G");// 已过期
					} else if (format.parse(course.getEndTime()).getTime() > format.parse(learnRecord.getMaxdate())
							.getTime()
							&& format.parse(course.getStartTime()).getTime() < format.parse(learnRecord.getMaxdate())
									.getTime()) {
						if (course.getPreStatus() == "Y") {
							course.setStatus("Y");//
						} else {
							course.setStatus("W");
						}
					} else if (format.parse(course.getEndTime()).getTime() > format.parse(learnRecord.getMindate())
							.getTime()
							&& format.parse(course.getStartTime()).getTime() < format.parse(learnRecord.getMindate())
									.getTime()) {

						if (course.getPreStatus() == "Y") {
							course.setStatus("Y");//
						} else {
							course.setStatus("W");
						}
					} else {
						course.setStatus("U");// 进行中
					}
				} else {
					if (format.parse(course.getStartTime()).getTime() > new Date().getTime()) {
						course.setStatus("W");// 未开始
					} else if (format.parse(course.getEndTime()).getTime() < new Date().getTime()) {
						course.setStatus("G");// 已过期
					} else {
						if (course.getPreStatus() == "Y") {
							course.setStatus("U");// 进行中
						} else {
							course.setStatus("W");// 未开始
						}
					}
				}
			} else {
				signInExample.setCode(userId);
				signInExample.setCourseId(course.getCourseId());
				TomCourseSignInRecords classtime = courseSignInRecordsMapper.selectClassTimeBycourseId(signInExample);
				// int signInNum =
				// courseSignInRecordsMapper.countByExample(signInExample);
				// List<TomCourseSignInRecords> signRecord =
				// courseSignInRecordsMapper.selectClassesSignRecord(signInExample);
				if (classtime.getCreateDate() != null && !"".equals(classtime.getCreateDate())) {
					Map<Object, Object> map1 = new HashMap<Object, Object>();
					map1.put("createDate", classtime.getEndTime());
					map1.put("courseId", course.getCourseId());
					map1.put("code", userId);
					if (classtime.getCreateDate().before(classtime.getBeginTime())) {
						course.setStatus("W");
					} else if (classtime.getCreateDate().after(classtime.getEndTime())) {
						if (signMapper.selectSignRecord(map).size() > 0 && "Y".equals(course.getPreStatus())) {
							course.setStatus("Y");
						} else {
							course.setStatus("G");
						}
					} else {
						if (signMapper.selectSignRecord(map).size() > 0 && "Y".equals(course.getPreStatus())) {
							course.setStatus("Y");
						} else {
							course.setStatus("U");
						}
					}
				} else {
					if (new Date().before(classtime.getBeginTime())) {
						course.setStatus("W");
					} else if (new Date().after(classtime.getEndTime())) {
						course.setStatus("G");
					} else {
						course.setStatus("U");
					}
				}
			}

			activityProperty.setActivityId(course.getActivityId());
			TomActivityProperty activityProperty1 = activityPropertyMapper.selectByExample(activityProperty).get(0);

			if (activityProperty1 != null) {
				if (activityProperty1.getStartTime() != null) {
					course.setBeginTime(format.format(activityProperty1.getStartTime()));
					course.setBeginTimeS(String.valueOf(activityProperty1.getStartTime().getTime()));
				}
				if (activityProperty1.getEndTime() != null) {
					course.setEndTime(format.format(activityProperty1.getEndTime()));
					course.setEndTimeS(String.valueOf(activityProperty1.getEndTime().getTime()));
				}
			}
			if ("en".equals(lang)) {
				example.setGradeDimensionName(example.getGradeDimensionNameEn());
				course.setCourseName(course.getCourseNameEn());
				course.setCourseIntroduction(course.getCourseIntroductionEn());
				course.setCourseImg(course.getCourseImgEn());
			}

		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.12.课程详情
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日下午4:52:23
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PageData<TomCourses> page = new PageData<TomCourses>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 获取当前登陆员工Code
		String code = request.getParameter("userId");
		String lang = request.getParameter("lang");
		TomCourses courses = coursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		String activityId = request.getParameter("activityId");
		// 讲师id与姓名
		courses.setLecturerId(courses.getLecturers());

		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("activityId", activityId);
		map1.put("courseId", courses.getCourseId());
		TomActivityProperty activityProperty = activityPropertyMapper.selectByCourseIdAndActivityId(map1);
		if (activityProperty != null) {
			if (activityProperty.getStartTime() != null) {
				courses.setBeginTime(format.format(activityProperty.getStartTime()));
			}
			if (activityProperty.getEndTime() != null) {
				courses.setEndTime(format.format(activityProperty.getEndTime()));
			}
			// courses.setBeginTime(format.format(activityProperty.getStartTime()));
			// courses.setEndTime(format.format(activityProperty.getEndTime()));
		}

		// 判断课程是否需要评分
		TomGradeRecords gradeExample = new TomGradeRecords();
		gradeExample.setCode(code);
		gradeExample.setCourseId(courses.getCourseId());
		gradeExample.setGradeType("C");
		List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
		gradeExample.setGradeType("L");
		List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
		if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
			courses.setIsGrade("Y");
		} else {
			courses.setIsGrade("N");
		}

		if (null != courses.getLecturers()) {

			if ("Y".equals(courses.getCourseOnline())) {
				int lectureKeyId = Integer.parseInt(courses.getLecturers().split(",")[1]);
				TomLecturer lecturer = lecturerMapper.selectByPrimaryKey(lectureKeyId);
				if (null != lecturer) {
					if ("en".equals(lang)) {
						courses.setLecturerName(lecturer.getLecturerNameEn());
					} else {
						courses.setLecturerName(lecturer.getLecturerName());
					}
				} else {
					courses.setLecturerName("");
				}

				TomCourseSection example = new TomCourseSection();
				example.setCourseId(courses.getCourseId());
				example.setStatus("Y");
				courses.setSectionNum(courseSectionMapper.countByExample(example));

				/* start 查询课程学习状态 */
				Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("code", code);
				map.put("courseId", courses.getCourseId());
				List<TomCourseLearningRecord> record = courseLearningRecordMapper.selectLearnRecord(map);
				if (record.size() > 0) {
					courses.setLearnStatus("Y");
				} else {
					courses.setLearnStatus("N");
				}
			} else {
				int lectureKeyId = Integer.parseInt(courses.getLecturers().split(",")[1]);
				TomLecturer lecturer = lecturerMapper.selectByPrimaryKey(lectureKeyId);
				if (null != lecturer) {
					if ("en".equals(lang)) {
						courses.setLecturerName(lecturer.getLecturerNameEn());
					} else {
						courses.setLecturerName(lecturer.getLecturerName());
					}
				} else {
					courses.setLecturerName("");
				}

				courses.setLecturerId(activityProperty.getLecturers());
				TomCourseSignInRecords signInRecordsExample = new TomCourseSignInRecords();
				signInRecordsExample.setCode(code);
				signInRecordsExample.setCourseId(courses.getCourseId());
				int count = courseSignInRecordsMapper.countByExample(signInRecordsExample);
				if (count > 0) {
					courses.setLearnStatus("Y");
				} else {
					courses.setLearnStatus("N");
				}
			}

		}
		// 收藏、点赞状态
		TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
		favoriteCourse.setCode(code);
		favoriteCourse.setCourseId(courses.getCourseId());
		favoriteCourse.setStatus("Y");
		int countFavorite = coursesMapper.countByCourseIdAndCode(favoriteCourse);
		if (countFavorite > 0) {
			courses.setCollect("Y");
		} else {
			courses.setCollect("N");
		}

		TomCourseThumbUp courseThumbUp = new TomCourseThumbUp();
		courseThumbUp.setCode(code);
		courseThumbUp.setCourseId(courses.getCourseId());
		courseThumbUp.setStatus("Y");
		int countThumbUp = coursesMapper.countThumbUpByCourseIdAndCode(courseThumbUp);
		if (countThumbUp > 0) {
			courses.setPraise("Y");
		} else {
			courses.setPraise("N");
		}
		if ("en".equals(lang)) {
			courses.setCourseName(courses.getCourseNameEn());
			courses.setCourseIntroduction(courses.getCourseIntroductionEn());
			courses.setCourseImg(courses.getCourseImgEn());
		}
		/* end */
		List<TomCourses> list = new ArrayList<TomCourses>();
		;
		list.add(courses);
		page.setDate(list);
		page.setCount(1);
		page.setPageNum(0);
		page.setPageSize(1);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.13.更改课程收藏状态
	 *
	 * 创建者：ZXM 创建时间: 2016年3月18日下午5:56:17
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public Result eleCourseCollectStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
		favoriteCourse.setCode(request.getParameter("userId"));
		if (null != request.getParameter("courseId") || "".equals(request.getParameter("courseId"))) {
			favoriteCourse.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		}
		if (null == favoriteCourse.getCode() || "".equals(favoriteCourse.getCode())) {
			favoriteCourse.setCode("1");
		}
		if (null == favoriteCourse.getCourseId() || "".equals(favoriteCourse.getCourseId())) {
			favoriteCourse.setCourseId(1);
		}

		int count = coursesMapper.countByCourseIdAndCode(favoriteCourse);
		// 判断是否收藏过课程信息，如果收藏过就更新收藏状态，如果未收藏就添加收藏关联
		if (count > 0) {
			TomFavoriteCourse favoriteCourseOld = new TomFavoriteCourse();
			favoriteCourseOld = coursesMapper.selectByCourseIdAndCode(favoriteCourse);
			// 判断员工已经收藏过的课程是“收藏”状态还是“取消收藏”状态，如果是“收藏”则改为“取消收藏”，如果是“取消收藏”则改为“收藏”
			if ("Y".equals(favoriteCourseOld.getStatus())) {
				favoriteCourse.setStatus("N");
			} else {
				favoriteCourse.setStatus("Y");
			}
			favoriteCourse.setCreateTime(new Date());
			courseService.updateByCourseIdandCode(favoriteCourse);
		} else {
			favoriteCourse.setStatus("Y");
			favoriteCourse.setCreateTime(new Date());
			courseService.insertFavorite(favoriteCourse);
		}
		return new Result("Y", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：5.14.更改课程点赞状态
	 *
	 * 创建者：ZXM 创建时间: 2016年3月21日下午3:03:15
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public Result eleCoursePraiseStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomCourseThumbUp courseThumbUp = new TomCourseThumbUp();
		courseThumbUp.setCode(request.getParameter("userId"));
		if (null != request.getParameter("courseId") || "".equals(request.getParameter("courseId"))) {
			courseThumbUp.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		}
		if (null == courseThumbUp.getCode() || "".equals(courseThumbUp.getCode())) {
			courseThumbUp.setCode("1");
		}
		if (null == courseThumbUp.getCourseId() || "".equals(courseThumbUp.getCourseId())) {
			courseThumbUp.setCourseId(1);
		}
		TomCourses course = coursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		int praiseCount = course.getThumbUpCount();
		int count = coursesMapper.countThumbUpByCourseIdAndCode(courseThumbUp);
		// 判断是否为课程信息点赞，如果点过就更新点赞状态，如果未点赞就添加点赞关联
		if (count > 0) {
			TomCourseThumbUp courseThumbUpOld = new TomCourseThumbUp();
			courseThumbUpOld = coursesMapper.selectThumbUpByCourseIdAndCode(courseThumbUp);
			// 判断员工已经点赞过的课程是“点赞”状态还是“取消点赞”状态，如果是“点赞”则改为“取消点赞”，如果是“取消点赞”则改为“点赞”
			if ("Y".equals(courseThumbUpOld.getStatus())) {
				courseThumbUp.setStatus("N");
				praiseCount--;
			} else {
				courseThumbUp.setStatus("Y");
				praiseCount++;
			}
			courseService.updateThumbUpByCourseIdandCode(courseThumbUp);
		} else {
			courseThumbUp.setStatus("Y");
			courseThumbUp.setCreateTime(new Date());
			courseService.insertThumbUp(courseThumbUp);
			praiseCount++;
		}
		course.setThumbUpCount(praiseCount);
		courseService.updateByPrimaryKey(course);
		return new Result("Y", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：5.15.查看课程评论
	 *
	 * 创建者：ZXM 创建时间: 2016年3月21日下午6:10:15
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseCommentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PageData<TomCourseComment> page = new PageData<TomCourseComment>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object, Object> map = new HashMap<Object, Object>();
		String code = request.getParameter("userId");
		TomCourseComment example = new TomCourseComment();
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		example.setStatus("Y");
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = courseCommentMapper.countByExample(example);
		List<TomCourseComment> list = courseCommentMapper.selectListByPage(map);
		// 为课程评论人加载头像路径；以及查看当前登陆人是否点过赞
		for (TomCourseComment courseCommentNew : list) {
			TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(courseCommentNew.getCode());
			if (courseCommentNew.getAnonymity().equals("N")) {
				courseCommentNew.setHeadImg("-");
				courseCommentNew.setName("匿名用户");
			} else if (null != userInfo) {
				courseCommentNew.setHeadImg(userInfo.getHeadImg());
			}

			TomCourseCommentThumbUp courseCommentThumbUp = new TomCourseCommentThumbUp();
			courseCommentThumbUp.setCode(code);
			courseCommentThumbUp.setCourseCommentId(courseCommentNew.getCourseCommentId());
			int countNew = courseCommentMapper.countThumbUpByCourseIdAndCode(courseCommentThumbUp);
			if (countNew > 0) {
				courseCommentNew.setPraise("Y");
			} else {
				courseCommentNew.setPraise("N");
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.16.课程评论点赞
	 *
	 * 创建者：ZXM 创建时间: 2016年3月22日上午11:16:40
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public Result eleCourseCommentPraiseSubmit(String json) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		JSONObject jsonObject = JSONObject.fromObject(json);
		TomCourseCommentThumbUp courseCommentThumbUp = new TomCourseCommentThumbUp();
		courseCommentThumbUp.setCode(jsonObject.getString("userId"));

		if (null != jsonObject.getString("courseCommentId") || "".equals(jsonObject.getString("courseCommentId"))) {
			courseCommentThumbUp.setCourseCommentId(Integer.parseInt(jsonObject.getString("courseCommentId")));
		}
		if (null == courseCommentThumbUp.getCode() || "".equals(courseCommentThumbUp.getCode())) {
			courseCommentThumbUp.setCode("1");
		}
		if (null == courseCommentThumbUp.getCourseCommentId() || "".equals(courseCommentThumbUp.getCourseCommentId())) {
			courseCommentThumbUp.setCourseCommentId(1);
		}

		int count = courseCommentMapper.countThumbUpByCourseIdAndCode(courseCommentThumbUp);
		// 判断是否为课程评论信息点过赞，如果点过就更新点赞状态，如果未点赞就添加点赞关联
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if (count > 0) {
			TomCourseCommentThumbUp courseCommentThumbUpOld = new TomCourseCommentThumbUp();
			courseCommentThumbUpOld = courseCommentMapper.selectThumbUpByCourseIdAndCode(courseCommentThumbUp);
			// 判断员工已经点过赞的课程评论是“点赞”状态还是“取消点赞”状态，如果是“点赞”则改为“取消点赞”，如果是“取消点赞”则改为“点赞”
			if ("Y".equals(courseCommentThumbUpOld.getStatus())) {
				courseCommentThumbUp.setStatus("N");
			} else {
				courseCommentThumbUp.setStatus("Y");
			}

			courseCommentService.updateThumbUpByCourseIdandCode(courseCommentThumbUp);
		} else {
			courseCommentThumbUp.setStatus("Y");
			courseCommentThumbUp.setCreateTime(new Date());
			courseCommentService.insertThumbUp(courseCommentThumbUp);

			// 点赞人增加EB
			TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			Map<Object, Object> map2 = new HashMap<Object, Object>();
			SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
			map2.put("userId", jsonObject.getString("userId"));
			map2.put("road", "7");
			map2.put("date", simple.format(new Date()));
			List<TomEbRecord> ebRList = ebRecordMapper.selectByRode(map2);
			int ebCount = 0;
			for (TomEbRecord er : ebRList) {
				ebCount += er.getExchangeNumber();
			}
			if (ebCount < 100) {
				userInfo.seteNumber(userInfo.geteNumber() + 2);
				userInfo.setAddUpENumber(userInfo.getAddUpENumber() + 2);
				TomEbRecord ebRecord = new TomEbRecord();
				ebRecord.setCode(jsonObject.getString("userId"));
				ebRecord.setUpdateTime(new Date());
				ebRecord.setExchangeNumber(1);
				ebRecord.setRoad("8");
				commodityService.insertSelective(ebRecord);
				userInfoServise.updateByCodetoAPI(userInfo);
			}

			// 被点赞的课程的人增加EB
			Integer courseId = courseCommentThumbUp.getCourseCommentId();
			TomCourseComment userIn = courseCommentMapper.selectByPrimaryKey(courseId);
			String code = userIn.getCode();
			TomUserInfo userInfo1 = userInfoMapper.selectByPrimaryKey(code);
			Map<Object, Object> map3 = new HashMap<Object, Object>();
			SimpleDateFormat simple1 = new SimpleDateFormat("yyyy-MM-dd");
			map3.put("userId", code);
			map3.put("road", "8");
			map3.put("date", simple1.format(new Date()));
			List<TomEbRecord> ebRList1 = ebRecordMapper.selectByRode(map3);
			int ebCount1 = 0;
			for (TomEbRecord er : ebRList1) {
				ebCount1 += er.getExchangeNumber();
			}
			if (ebCount1 < 100) {
				userInfo1.seteNumber(userInfo1.geteNumber() + 2);
				userInfo1.setAddUpENumber(userInfo1.getAddUpENumber() + 2);
				TomEbRecord ebRecord = new TomEbRecord();
				ebRecord.setCode(code);
				ebRecord.setUpdateTime(new Date());
				ebRecord.setExchangeNumber(2);
				ebRecord.setRoad("8");
				commodityService.insertSelective(ebRecord);
				userInfoServise.updateByCodetoAPI(userInfo1);
			}
		}
		TomCourseComment comment = courseCommentMapper
				.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseCommentId")));
		int thumbUpNumber = comment.getThumbUpNumber() + 1;
		comment.setThumbUpNumber(thumbUpNumber);
		courseCommentService.updateByPrimaryKey(comment);
		return new Result("Y", "{\"thumbUpNumber\":" + thumbUpNumber + "}", "0", "");
	}

	/**
	 * 
	 * 功能描述：5.17.评论课程
	 *
	 * 创建者：ZXM 创建时间: 2016年3月23日上午9:58:45
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public Result eleCourseCommentSubmit(String json) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		JSONObject jsonObject = JSONObject.fromObject(json);
		TomCourseComment courseComment = new TomCourseComment();
		courseComment.setCode(jsonObject.getString("userId"));
		TomUserLog userLog = userLogMapper.getUserByCode(courseComment.getCode());
		TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
		Map<Object, Object> map = new HashMap<Object, Object>();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		map.put("userId", jsonObject.getString("userId"));
		map.put("road", "11");
		map.put("date", simple.format(new Date()));
		List<TomEbRecord> ebRList = ebRecordMapper.selectByRode(map);
		int ebCount = 0;
		for (TomEbRecord er : ebRList) {
			ebCount += er.getExchangeNumber();
		}
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if (ebCount < 100) {
			userInfo.seteNumber(userInfo.geteNumber() + 5);
			userInfo.setAddUpENumber(userInfo.getAddUpENumber() + 5);
			TomEbRecord ebRecord = new TomEbRecord();
			ebRecord.setCode(jsonObject.getString("userId"));
			ebRecord.setUpdateTime(new Date());
			ebRecord.setExchangeNumber(2);
			ebRecord.setRoad("15");
			commodityService.insertSelective(ebRecord);
			userInfoServise.updateByCodetoAPI(userInfo);
		}
		if (null != userLog) {
			courseComment.setName(userLog.getName());
		}
		courseComment.setCourseId(Integer.parseInt(jsonObject.getString("courseId")));
		courseComment.setCourseCommentContent(jsonObject.getString("courseCommentContent"));
		courseComment.setCreateTime(new Date());
		courseComment.setThumbUpNumber(0);
		courseComment.setStatus("Y");
		courseComment.setAnonymity(userInfo.getAnonymity());
		courseCommentService.insertSelective(courseComment);
		TomCourses course = coursesMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseId")));
		course.setCommentCount(course.getCommentCount() + 1);
		courseService.updateByPrimaryKey(course);
		return new Result("Y", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：5.18.课程章节列表
	 *
	 * 创建者：ZXM 创建时间: 2016年3月23日上午10:59:28
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseSectionsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourseSection> page = new PageData<TomCourseSection>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		TomCourseSection example = new TomCourseSection();
		String driver = request.getParameter("driver");
		String lang = request.getParameter("lang");
		String code = request.getParameter("userId");
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		TomCourses course = tomCoursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		example.setStatus("Y");
		map.put("example", example);
		// map.put("status", example);
		map.put("courseId", Integer.parseInt(request.getParameter("courseId")));
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		String type = "";
		if (driver == null) {
			driver = "h5";
			type = "H";
		} else {
			type = "P";
			type="H";
		}
		if (lang == null) {
			lang = "cn";
		}
		map.put("driver", driver);
		map.put("lang", lang);
		int count = courseSectionMapper.countByDriver(map);
		List<TomCourseSection> list = courseSectionMapper.pageByDriver(map);
		for (TomCourseSection courseSectionNew : list) {
			TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
			sectionLearning.setCourseSectionId(courseSectionNew.getSectionId());
			sectionLearning.setCode(code);
			// 课程内容
			if (null != course && "Y".equals(course.getCourseDownloadable())) {
				if (null == courseSectionNew.getFileUrl() || "".equals(courseSectionNew.getFileUrl())) {
					courseSectionNew.setCourseFileuploadAddress(courseSectionNew.getSectionAddress());
				} else {
					if (courseSectionNew.getFileUrl().contains(".pdf") || courseSectionNew.getFileUrl().contains(".ppt")
							|| courseSectionNew.getFileUrl().contains(".pptx")) {
						courseSectionNew.setCourseFileuploadAddress(courseSectionNew.getFileUrl());
					} else {
						courseSectionNew.setCourseFileuploadAddress(courseSectionNew.getSectionAddress());
					}
				}
			} else {
				courseSectionNew.setCourseFileuploadAddress(courseSectionNew.getSectionAddress());
			}
			// courseSectionNew.setCourseFileuploadAddress(courseSectionNew.getSectionAddress());
			// 查询是否学习过
			int sectionLearnCount = courseSectionMapper.countByCodeAndSectionId(sectionLearning);
			if (sectionLearnCount > 0) {
				courseSectionNew.setLearningStates("Y");
				courseSectionNew.setSystemType(type);
				if ("7".equals(courseSectionNew.getSectionType()) || "6".equals(courseSectionNew.getSectionType())) {
					courseSectionNew.setCorpid(Config.getProperty("zoCorpid"));
					courseSectionNew
							.setSig(MD5Util
									.MD5(Config.getProperty("zosecretKey") + MD5Util
											.MD5(courseSectionNew.getCorpid() + code + code + code
													+ courseSectionNew.getTid() + courseSectionNew.getCid())
											.toLowerCase())
									.toLowerCase());
				}
			} else {
				courseSectionNew.setLearningStates("N");
				if ("7".equals(courseSectionNew.getSectionType()) || "6".equals(courseSectionNew.getSectionType())) {
					Map<Object, Object> map1 = new HashMap<Object, Object>();
					map1.put("courseId", courseSectionNew.getCourseId());
					map1.put("sort", courseSectionNew.getSectionSort());
					List<TomCourseSection> sectionList = courseSectionMapper.selectBySort(map1);
					if (sectionList.size() > 1) {
						int countAll = 0;
						for (TomCourseSection tomCourseSection : sectionList) {
							Map<Object, Object> map2 = new HashMap<Object, Object>();
							map2.put("userId", code);
							map2.put("sectionId", tomCourseSection.getSectionId());
							int countCnEu = courseCnEuMapper.selectByexample(map2);
							if (1 == countCnEu) {
								countAll++;
							}
							if (1 == countCnEu
									&& tomCourseSection.getSectionId().equals(courseSectionNew.getSectionId())) {
								courseSectionNew.setSystemType(type);
							}
							if (1 == countCnEu
									&& !tomCourseSection.getSectionId().equals(courseSectionNew.getSectionId())) {
								if (type.equals("H")) {
									courseSectionNew.setSystemType("P");
								} else {
									courseSectionNew.setSystemType("H");
								}
							}
						}
						if (2 == countAll || 0 == countAll) {
							courseSectionNew.setSystemType(type);
						}
					} else {
						courseSectionNew.setSystemType(type);
					}
					courseSectionNew.setCorpid(Config.getProperty("zoCorpid"));
					courseSectionNew
							.setSig(MD5Util
									.MD5(Config.getProperty("zosecretKey") + MD5Util
											.MD5(courseSectionNew.getCorpid() + code + code + code
													+ courseSectionNew.getTid() + courseSectionNew.getCid())
											.toLowerCase())
									.toLowerCase());
				}
			}
			if ("en".equals(lang)) {
				courseSectionNew.setSectionName(courseSectionNew.getSectionNameEn());
			}

		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.22.章节学习记录
	 *
	 * 创建者：ZXM 创建时间: 2016年3月23日下午3:38:42
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public Result eleSectionsLearnningRecord(String json) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		JSONObject jsonObject = JSONObject.fromObject(json);
		if ("N".equals(jsonObject.getString("learningStates"))) {
			return new Result("Y", "", "0", "");
		}
		String pageJson = null;
		TomCourseSection courseSection = courseSectionMapper
				.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseSectionId")));
		TomCourses courses = coursesMapper.selectByPrimaryKey(courseSection.getCourseId());
		if (courses.getCourseOnline().equals("Y")) {
			List<TomCourseSection> sectionList = new ArrayList<TomCourseSection>();
			if (courseSection.getSectionSort() == null) {
				sectionList.add(courseSection);
			} else {
				Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("sort", courseSection.getSectionSort());
				map.put("courseId", courseSection.getCourseId());
				sectionList = courseSectionMapper.selectBySort(map);
			}
			int flag = 1;
			for (TomCourseSection section : sectionList) {
				TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
				sectionLearning.setCode(jsonObject.getString("userId"));
				sectionLearning.setCourseSectionId(section.getSectionId());
				flag = courseSectionLearningRecordMapper.countByExample(sectionLearning);
				if (flag == 0) {
					sectionLearning.setCreateTime(new Date());
					sectionLearning.setLearningStates(jsonObject.getString("learningStates"));
					courseService.insertSectionLearning(sectionLearning);
				}
			}
			TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			TomEmp emp = empMapper.selectByPrimaryKey(jsonObject.getString("userId"));
			if (Integer.parseInt(jsonObject.getString("learningTime")) < 3600 * 24) {
				userInfo.setLearningTime(String.valueOf(Integer.parseInt(userInfo.getLearningTime())
						+ Integer.parseInt(jsonObject.getString("learningTime"))));
			}

			// 插入操作日志
			TomLearnTimeLog learnTimeLog = new TomLearnTimeLog();
			learnTimeLog.setCreateTime(new Date());
			learnTimeLog.setCode(emp.getCode());
			learnTimeLog.setName(emp.getName());
			learnTimeLog.setCourseId(courses.getCourseId());
			learnTimeLog.setSectionId(courseSection.getSectionId());
			learnTimeLog.setTime(Integer.parseInt(jsonObject.getString("learningTime")));
			learnTimeLog.setUseragent(jsonObject.getString("userAgent"));
			courseService.insertSelective(learnTimeLog);

			// 判断课程是否需要评分
			TomGradeRecords examples = new TomGradeRecords();
			TomCourses course = new TomCourses();
			examples.setCode(jsonObject.getString("userId"));
			examples.setCourseId(courseSection.getCourseId());
			examples.setGradeType("C");
			List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(examples);
			examples.setGradeType("L");
			List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(examples);
			if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
				course.setIsGrade("Y");
			} else {
				course.setIsGrade("N");
			}

			if (flag == 0) {
				TomCourseSection example = new TomCourseSection();
				example.setCourseId(courseSection.getCourseId());
				List<TomCourseSection> courseSections = courseSectionMapper.selectListByExample(example);
				for (TomCourseSection courseSection2 : courseSections) {
					TomCourseSectionLearningRecord courseSectionLearningRecord = new TomCourseSectionLearningRecord();
					courseSectionLearningRecord.setCourseSectionId(courseSection2.getSectionId());
					courseSectionLearningRecord.setCode(jsonObject.getString("userId"));
					if (courseSectionLearningRecordMapper.countByExample(courseSectionLearningRecord) == 0) {
						userInfoServise.updateByCodetoAPI(userInfo);
						CourseSectionLearningRecord page = new CourseSectionLearningRecord();
						page.setCourseStudyStates("N");
						page.setWhetherGrade(course.getIsGrade());
						page.setGradeStatus(course.getGradeStatus());
						pageJson = mapper.writeValueAsString(page);
						pageJson = pageJson.replaceAll(":null", ":\"\"");
						return new Result("Y", pageJson, "0", "");
					}
				}

				TomCourseLearningRecord courseLearningRecord = new TomCourseLearningRecord();
				courseLearningRecord.setCode(jsonObject.getString("userId"));
				courseLearningRecord.setCourseId(courseSection.getCourseId());
				if (courseLearningRecordMapper.countByExample(courseLearningRecord) == 0) {
					courseLearningRecord.setLearningDate(new Date());
					courseService.insertSelective(courseLearningRecord);

					DBContextHolder.setDbType(DBContextHolder.MASTER);
					// 前置任务消息
					Map<Object, Object> propertyMap = new HashMap<Object, Object>();
					propertyMap.put("code", jsonObject.getString("userId"));
					propertyMap.put("courseId", courseSection.getCourseId());
					// 根据课程与人员查询正在进行的活动
					List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
							.selectByCourseAndCodeMessage(propertyMap);
					courseService.preTask(jsonObject.getString("userId"), selectByCourseAndCode);

					userInfo.setCourseNumber(String.valueOf(Integer.parseInt(userInfo.getCourseNumber()) + 1));
					// Map<Object, Object> map = new HashMap<Object, Object>();
					// SimpleDateFormat simple = new
					// SimpleDateFormat("yyyy-MM-dd");
					// map.put("userId", jsonObject.getString("userId"));
					// map.put("road", "2");
					// map.put("date", simple.format(new Date()));
					// List<TomEbRecord>
					// ebRList=ebRecordMapper.selectByRode(map);
					// int ebCount=0;
					// for(TomEbRecord er:ebRList){
					// ebCount+=er.getExchangeNumber();
					// }
					// int addEb=100-ebCount;
					// if(addEb>=courses.geteCurrency()){
					userInfo.seteNumber(userInfo.geteNumber() + courses.geteCurrency());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber() + courses.geteCurrency());
					TomEbRecord ebRecord = new TomEbRecord();
					ebRecord.setCode(jsonObject.getString("userId"));
					ebRecord.setUpdateTime(new Date());
					ebRecord.setExchangeNumber(courses.geteCurrency());
					ebRecord.setRoad("2");
					commodityService.insertSelective(ebRecord);
					// }else {
					// userInfo.seteNumber(userInfo.geteNumber()+ addEb);
					// userInfo.setAddUpENumber(userInfo.getAddUpENumber()+
					// addEb);
					// TomEbRecord ebRecord = new TomEbRecord();
					// ebRecord.setCode(jsonObject.getString("userId"));
					// ebRecord.setUpdateTime(new Date());
					// ebRecord.setExchangeNumber(addEb);
					// ebRecord.setRoad("2");
					// ebRecordMapper.insertSelective(ebRecord);
					// }

					//
					/*
					 * if("Y".equals(cExamStatus)){ Date date = new Date();
					 * List<TomActivityEmpsRelation> list =
					 * tomActivityEmpsRelationMapper
					 * .selectByActivitycode(jsonObject.getString("userId"));
					 * //查找这个人所有活动 for (TomActivityEmpsRelation
					 * tomActivityEmpsRelation : list) { Date endTime =
					 * activityMapper.selectByPrimaryKey(tomActivityEmpsRelation
					 * .getActivityId()) .getActivityEndTime(); //活动的结束时间
					 * List<TomActivityProperty> list2 = activityPropertyMapper
					 * .selectByActivityId(tomActivityEmpsRelation.getActivityId
					 * ()); //活动中的所有任务
					 * if("N".equals(tomActivityEmpsRelation.getIsRequired())){
					 * //判断选修 for (TomActivityProperty tomActivityProperty :
					 * list2) { SimpleDateFormat simple = new SimpleDateFormat(
					 * "yyyy-MM-dd HH:mm:ss"); if (null !=
					 * tomActivityProperty.getCourseId()) { if
					 * (tomActivityProperty.getCourseId().equals(courses.
					 * getCourseId()) && date.before(endTime)) {
					 * //List<TomActivityProperty> actPro =
					 * tomActivityPropertyMapper.selectByActivityId(
					 * tomActivityProperty.getActivityId());
					 * List<TomActivityProperty> property = new
					 * ArrayList<TomActivityProperty>(); Integer sort =
					 * tomActivityProperty.getSort(); for(TomActivityProperty
					 * tomActivityProperty2 :list2){
					 * if(null==tomActivityProperty2.getCourseId()){ String
					 * preTask = tomActivityProperty2.getPreTask(); String[]
					 * split = preTask.split(","); for(String str :split){
					 * if(str.equals(String.valueOf(sort))){
					 * property.add(tomActivityProperty2); } } } }
					 * for(TomActivityProperty tomActivityProperty3 :property){
					 * TomExam tomExam =
					 * tomExamMapper.selectByPrimaryKey(tomActivityProperty3.
					 * getExamId()); TomExamPaper examPaper =
					 * tomExamPaperMapper.selectByExamId(tomActivityProperty3.
					 * getExamId()); String cn =cExam.replace("<name>",
					 * tomExam.getExamName()).replace("<beginTime>",simple.
					 * format(tomActivityProperty3.getStartTime())).replace(
					 * "<endTime>",simple.format(tomActivityProperty3.getEndTime
					 * ())); String en =cExamEn.replace("<name>",
					 * tomExam.getExamNameEn()).replace("<beginTime>",simple.
					 * format(tomActivityProperty3.getStartTime())).replace(
					 * "<endTime>",simple.format(tomActivityProperty3.getEndTime
					 * ())); String examApp =cn+"/r/n"+en; String
					 * exam=Config.getProperty("h5Index")+
					 * "views/task/exam_examDetail.html?examId="+
					 * tomActivityProperty3.getExamId(); String exameEmail
					 * =examApp+"<a href=\""+Config.getProperty("pcIndex")+
					 * "views/exam/exam_index.html?examId="+tomActivityProperty3
					 * .getExamId()+"\">"+Config.getProperty("pcIndex")+
					 * "views/exam/exam_index.html?examId="+tomActivityProperty3
					 * .getExamId()+"</a>"; List<String> listEmp =
					 * finishStatusDown(tomActivityProperty3); List<String>
					 * listEmail = new ArrayList<String>(); for(String
					 * code:listEmp){ TomEmp emp2 =
					 * tomEmpMapper.selectByPrimaryKey(code);
					 * if(emp2.getSecretEmail()!=null)
					 * listEmail.add(emp2.getSecretEmail()); } TomMessages
					 * message = tomMessagesMapper.selectByPrimaryKey(0);
					 * if(message.getSendToEmail().equals("Y")){ SendMail sm=
					 * SendMailUtil.getMail(listEmail,"【蔚乐学】任务通知",date,"蔚乐学",
					 * exameEmail ); sm.sendMessage(); }
					 * if(message.getSendToApp().equals("Y")){ List<WxMessage>
					 * listWx = new ArrayList<WxMessage>(); WxMessage wx = new
					 * WxMessage(tomExam.getExamName(),examApp,exam,Config.
					 * getProperty("localUrl")+PathUtil.PROJECT_NAME+examPaper.
					 * getExamPaperPicture()); listWx.add(wx); String
					 * wxNewsMessage = sendMessageService.wxNewsMessage(listEmp,
					 * listWx); List<String> listApp =
					 * sendMessageService.sendStatus(listEmp, wxNewsMessage);
					 * if(listApp.size()!=0){
					 * sendMessageService.wxNewsMessage(listApp, listWx); }
					 * //sendMessageService.wxMessage(listEmp, exam); }
					 * TomMessages activityMessage = new TomMessages();
					 * activityMessage.setMessageTitle(tomActivityProperty3.
					 * getExamName()); activityMessage.setMessageDetails(cn);
					 * activityMessage.setMessageTitleEn(tomExam.getExamNameEn()
					 * ); activityMessage.setMessageDetailsEn(en);
					 * activityMessage.setAppUrl(Config.getProperty("h5Index")+
					 * "views/task/exam_examDetail.html?examId="+
					 * tomActivityProperty3.getExamId());
					 * activityMessage.setPcUrl("<a href=\""
					 * +Config.getProperty("pcIndex")+
					 * "views/exam/exam_index.html?examId="+tomActivityProperty3
					 * .getExamId()+"\">"+Config.getProperty("pcIndex")+
					 * "views/exam/exam_index.html?examId="+tomActivityProperty3
					 * .getExamId()+"</a>"); activityMessage.setEmpIds(listEmp);
					 * activityMessage.setMessageType("4");
					 * activityMessage.setCreateTime(date);
					 * activityMessage.setViewCount(0); if
					 * ("Y".equals(message.getSendToApp())) {
					 * activityMessage.setSendToApp("Y"); } else {
					 * activityMessage.setSendToApp("N"); } if
					 * ("Y".equals(message.getSendToEmail())) {
					 * activityMessage.setSendToEmail("Y"); } else {
					 * activityMessage.setSendToEmail("N"); } if
					 * ("Y".equals(message.getSendToPhone())) {
					 * activityMessage.setSendToPhone("Y"); } else {
					 * activityMessage.setSendToPhone("N"); }
					 * tomMessagesMapper.insertSelective(activityMessage); for
					 * (String code : listEmp) { TomMessagesEmployees
					 * tomMessagesEmployees = new TomMessagesEmployees();
					 * tomMessagesEmployees.setCreateTime(date);
					 * tomMessagesEmployees.setEmpCode(code);
					 * tomMessagesEmployees.setMessageId(activityMessage.
					 * getMessageId()); tomMessagesEmployees.setIsView("N");
					 * tomMessagesEmployeesMapper.insertSelective(
					 * tomMessagesEmployees); } } } } }
					 * 
					 * } } }
					 */
				}else if(courseLearningRecordMapper.countByExample(courseLearningRecord) > 0){
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					// 前置任务消息
					Map<Object, Object> propertyMap = new HashMap<Object, Object>();
					propertyMap.put("code", jsonObject.getString("userId"));
					propertyMap.put("courseId", courseSection.getCourseId());
					// 根据课程与人员查询正在进行的活动
					List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
							.selectByCourseAndCodeMessage(propertyMap);
					courseService.preTask(jsonObject.getString("userId"), selectByCourseAndCode);
				}

				TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
				courseEmpRelationExample.setCourseId(courseSection.getCourseId());
				courseEmpRelationExample.setCode(jsonObject.getString("userId"));
				List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper
						.selectByExample(courseEmpRelationExample);
				if (courseEmpRelations.size() != 0) {
					TomCourseEmpRelation courseEmpRelation = courseEmpRelations.get(0);
					courseEmpRelation.setFinishStatus("Y");
					courseEmpRelation.setFinishTime(new Date());
					courseEmpRelation.setUpdateTime(new Date());
					courseService.update(courseEmpRelation);
				}
			}
			userInfoServise.updateByCodetoAPI(userInfo);
			TomCourseSection tomCourseSection = new TomCourseSection();
			tomCourseSection = courseSectionMapper
					.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseSectionId")));
			List<TomCourseSection> list = courseSectionMapper.selectListByCourseId(tomCourseSection.getCourseId());
			int count = courseSectionMapper.countByExample(tomCourseSection);
			TomCourseSectionLearningRecord tomCourseSectionLearningRecord1 = new TomCourseSectionLearningRecord();
			tomCourseSectionLearningRecord1.setWhetherGrade(course.getIsGrade());
			for (TomCourseSection tomCourseSections : list) {
				if ("Y".equals(tomCourseSections.getStatus())) {
					Map<Object, Object> map = new HashMap<Object, Object>();
					map.put("code", jsonObject.getString("userId"));
					map.put("courseSectionId", tomCourseSections.getSectionId());
					TomCourseSectionLearningRecord tomCourseSectionLearningRecord = new TomCourseSectionLearningRecord();
					tomCourseSectionLearningRecord = courseSectionLearningRecordMapper.selectByPrimaryKey(map);
					CourseSectionLearningRecord page = new CourseSectionLearningRecord();
					if (null != tomCourseSectionLearningRecord) {
						if ("N".equals(tomCourseSectionLearningRecord.getLearningStates())) {
							page.setCourseStudyStates("N");
							page.setWhetherGrade(course.getIsGrade());
							page.setGradeStatus(course.getGradeStatus());
							pageJson = mapper.writeValueAsString(page);
							pageJson = pageJson.replaceAll(":null", ":\"\"");
							return new Result("Y", pageJson, "0", "");
						} else {
							page.setCourseStudyStates("Y");
							page.setWhetherGrade(course.getIsGrade());
							page.setGradeStatus(course.getGradeStatus());
							count--;
							if (count == 0) {
								pageJson = mapper.writeValueAsString(page);
								pageJson = pageJson.replaceAll(":null", ":\"\"");
								return new Result("Y", pageJson, "0", "");
							}
						}
					} else {
						page.setCourseStudyStates("N");
						page.setWhetherGrade(course.getIsGrade());
						pageJson = mapper.writeValueAsString(page);
						pageJson = pageJson.replaceAll(":null", ":\"\"");
						return new Result("Y", pageJson, "0", "");
					}
				}
			}
		}
		return new Result("Y", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：5.21.获取讲师详情
	 *
	 * 创建者：ZXM 创建时间: 2016年3月24日下午3:32:15
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleTeacherDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String lang = request.getParameter("lang");
		PageData<TomLecturer> page = new PageData<TomLecturer>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		TomLecturer example = new TomLecturer();
		String lecturerId = request.getParameter("lecturerId");
		example.setLecturerId(Integer.parseInt(lecturerId));
		if ("en".equals(lang)) {
			example.setLecturerName(example.getLecturerIntroductionEn());
			example.setLecturerIntroduction(example.getLecturerIntroductionEn());
		}
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
		int count = lecturerMapper.countByExample(example);
		List<TomLecturer> list = lecturerMapper.selectListByPage(map);
		if ("en".equals(lang)) {
			for (TomLecturer tomLecturer : list) {
				tomLecturer.setLecturerIntroduction(tomLecturer.getLecturerIntroductionEn());
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.19.课程/讲师评分
	 *
	 * 创建者：ZXM 创建时间: 2016年3月25日下午3:06:06
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public Result eleCourseTeacherGrade(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String lang = request.getParameter("lang");
		String code = request.getParameter("userId");
		String gradeType = request.getParameter("gradeType");
		TomCourses courses = coursesMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("courseId")));
		if ("N".equals(courses.getGradeStatus())) {
			return new Result("Y", "", "0", "");
		}
		courses.setLecturerName(lecturerMapper.selectByPrimaryKey(Integer.parseInt(courses.getLecturers().split(",")[1])).getLecturerName());
		JSONObject jo = new JSONObject();

		if ("en".equals(lang)) {
			jo.put("courseName", courses.getCourseNameEn());
			jo.put("lecturerName", courses.getLecturerNameEn());
		}

		jo.put("courseAverage", courses.getCourseAverage());
		jo.put("lecturerAverage",
				lecturerMapper.selectLecturerAvg(Integer.parseInt(courses.getLecturers().split(",")[1])));
		List<TomGradeRecords> list = new ArrayList<TomGradeRecords>();
		TomGradeRecords example = new TomGradeRecords();
		example.setCourseId(Integer.parseInt(request.getParameter("courseId")));
		example.setGradeType(gradeType);
		example.setCode(code);
		list = gradeRecordsMapper.selectListByRecords(example);
		JSONArray ja = new JSONArray();
		if (list.size() == 0) {
			String gradeIds[] = null;
			if ("C".equals(gradeType) && null != courses.getCourseGradeDimensions()
					&& StringUtil.isNotBlank(courses.getCourseGradeDimensions())) {
				gradeIds = courses.getCourseGradeDimensions().split(",");
			} else if ("L".equals(gradeType) && null != courses.getLecturerGradeDimensions()
					&& StringUtil.isNotBlank(courses.getLecturerGradeDimensions())) {
				gradeIds = courses.getLecturerGradeDimensions().split(",");
			}
			for (String gradeId : gradeIds) {
				TomGradeDimension gd = gradeDimensionMapper.selectByPrimaryKey(Integer.parseInt(gradeId));
				JSONObject graderjo = new JSONObject();

				graderjo.put("gradeDimensionId", gd.getGradeDimensionId());
				graderjo.put("score", "");
				if ("en".equals(lang)) {
					if (null == gd.getGradeDimensionNameEn()) {
						gd.setGradeDimensionNameEn("");
					}
					graderjo.put("gradeDimensionName", gd.getGradeDimensionNameEn());
				} else {
					graderjo.put("gradeDimensionName", gd.getGradeDimensionName());
				}
				ja.add(graderjo);
			}
		} else {
			for (TomGradeRecords gradeRecord : list) {

				JSONObject graderjo = new JSONObject();
				graderjo.put("gradeDimensionId", gradeRecord.getGradeDimensionId());
				graderjo.put("score", gradeRecord.getScore());
				if ("en".equals(lang)) {
					if (null == gradeRecord.getGradeDimensionNameEn()) {
						gradeRecord.setGradeDimensionNameEn("");
					}
					graderjo.put("gradeDimensionName", gradeRecord.getGradeDimensionNameEn());
				} else {
					graderjo.put("gradeDimensionName", gradeRecord.getGradeDimensionName());
				}
				ja.add(graderjo);
			}
		}
		jo.put("gradeDimensionList", ja);
		String pageJson = mapper.writeValueAsString(jo);
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：5.20.课程/讲师评分提交
	 *
	 * 创建者：ZXM 创建时间: 2016年3月31日上午10:28:29
	 * 
	 * @param json
	 * @return
	 */

	public Result eleCourseTeacherGradeSubmit(String json) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		JSONObject jsonObject = JSONObject.fromObject(json);
		CourseTeacherGradeResults courseTeacherGradeResults = new CourseTeacherGradeResults();
		courseTeacherGradeResults.setCourseId(Integer.parseInt(jsonObject.getString("courseId")));
		courseTeacherGradeResults.setUserId(jsonObject.getString("userId"));
		courseTeacherGradeResults.setGradeType(jsonObject.getString("gradeType"));
		List<GradeResult> gradeResults = new ArrayList<GradeResult>();

		TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(jsonObject.getString("userId"));
		Map<Object, Object> map1 = new HashMap<Object, Object>();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		map1.put("userId", jsonObject.getString("userId"));
		if ("C".equals(jsonObject.getString("gradeType"))) {
			map1.put("road", "5");
		} else if ("L".equals(jsonObject.getString("gradeType"))) {
			map1.put("road", "6");
		}
		map1.put("date", simple.format(new Date()));
		List<TomEbRecord> ebRList = ebRecordMapper.selectByRode(map1);
		int ebCount = 0;
		for (TomEbRecord er : ebRList) {
			ebCount += er.getExchangeNumber();
		}
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if (ebCount < 100) {
			userInfo.seteNumber(userInfo.geteNumber() + 2);
			userInfo.setAddUpENumber(userInfo.getAddUpENumber() + 2);
			TomEbRecord ebRecord = new TomEbRecord();
			ebRecord.setCode(jsonObject.getString("userId"));
			ebRecord.setUpdateTime(new Date());
			ebRecord.setExchangeNumber(2);
			if ("C".equals(jsonObject.getString("gradeType"))) {
				ebRecord.setRoad("5");
			} else if ("L".equals(jsonObject.getString("gradeType"))) {
				ebRecord.setRoad("6");
			}
			commodityService.insertSelective(ebRecord);
			userInfoServise.updateByCodetoAPI(userInfo);
		}

		GradeResult gradeResult1;
		for (int i = 0; i < jsonObject.getJSONArray("gradeDimensionList").size(); i++) {
			gradeResult1 = mapper.readValue(jsonObject.getJSONArray("gradeDimensionList").get(i).toString(),
					GradeResult.class);
			gradeResults.add(gradeResult1);
		}
		courseTeacherGradeResults.setGradeResult(gradeResults);

		List<GradeResult> list = courseTeacherGradeResults.getGradeResult();
		TomGradeRecords gradeRecord = new TomGradeRecords();
		gradeRecord.setCourseId(courseTeacherGradeResults.getCourseId());
		gradeRecord.setCode(courseTeacherGradeResults.getUserId());
		gradeRecord.setCreateTime(new Date());
		gradeRecord.setGradeType(courseTeacherGradeResults.getGradeType());
		for (GradeResult gradeResult : list) {
			gradeRecord.setGradeDimensionId(Integer.parseInt(gradeResult.getGradeDimensionId()));
			gradeRecord.setScore(Double.parseDouble(gradeResult.getScore()));
			TomGradeDimension gradeDimension = gradeDimensionMapper
					.selectByPrimaryKey(Integer.parseInt(gradeResult.getGradeDimensionId()));
			if (null != gradeDimension) {
				gradeRecord.setGradeDimensionName(gradeDimension.getGradeDimensionName());
			}
			courseService.insertSelective(gradeRecord);
		}
		// 更新平均分
		TomCourses courses = coursesMapper.selectByPrimaryKey(Integer.parseInt(jsonObject.getString("courseId")));
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", Integer.parseInt(jsonObject.getString("courseId")));
		map.put("gradeType", "C");
		courses.setTotScore(gradeRecordsMapper.getTotScore(map));
		courses.setCourseAverage(gradeRecordsMapper.getAverageScore(map));
		map.put("gradeType", "L");
		courses.setLecturerAverage(gradeRecordsMapper.getAverageScore(map));
		courseService.updateByPrimaryKeySelective(courses);//
		return new Result("Y", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：8.5.我的课程
	 *
	 * 创建者：ZXM 创建时间: 2016年3月29日下午6:22:08
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleMyCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PageData<TomCourses> page = new PageData<TomCourses>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		List<TomCourses> list = new ArrayList<TomCourses>();
		String lang = request.getParameter("lang");
		String code = request.getParameter("userId");
		String identifying = request.getParameter("identifying");// 查询标识
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", code);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		count0 = coursesMapper.countFinishedCourse(map);
		count1 = coursesMapper.countUnfinishedCourse(map);
		TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
		favoriteCourse.setCode(code);
		favoriteCourse.setStatus("Y");
		count2 = coursesMapper.countByCourseIdAndCode(favoriteCourse);
		if ("0".equals(identifying)) {
			list = coursesMapper.selectFinishedCourse(map);
			// List<TomCourses>
			// onlineList=coursesMapper.selectFinishedOnlineCourse(map);
			// List<TomCourses>
			// offlineList=coursesMapper.selectFinishedOfflineCourse(map);
			page.setCount(count0);
		} else if ("1".equals(identifying)) {
			list = coursesMapper.selectUnfinishedCourse(map);
			for (TomCourses course : list) {
				TomActivityProperty activityProperty = new TomActivityProperty();
				activityProperty.setCourseId(course.getCourseId());
				List<TomActivityProperty> proList = activityPropertyMapper.selectByExample(activityProperty);
				String preStatus = activityApiService.getPreStatus(proList, code);
				course.setPreStatus(preStatus);
			}
			page.setCount(count1);
		} else if ("2".equals(identifying)) {
			list = coursesMapper.selectByCode(map);
			page.setCount(count2);
		}

		MyCourse myCourse = new MyCourse();
		myCourse.setCourseCount0(count0);
		myCourse.setCourseCount1(count1);
		myCourse.setCourseCount2(count2);

		for (TomCourses course : list) {
			// 判断课程是否需要评分
			TomGradeRecords gradeExample = new TomGradeRecords();
			gradeExample.setCode(code);
			gradeExample.setCourseId(course.getCourseId());
			gradeExample.setGradeType("C");
			List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
			gradeExample.setGradeType("L");
			List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
			if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
				course.setIsGrade("Y");
			} else {
				course.setIsGrade("N");
			}

			if ("Y".equals(course.getCourseOnline())) {// 线上课程通过查询学习记录, 来判断是否已完成
				TomCourseLearningRecord record = new TomCourseLearningRecord();
				record.setCode(code);
				record.setCourseId(course.getCourseId());
				int learningNum = courseLearningRecordMapper.countByExample(record);
				if (learningNum > 0) {
					course.setCourseStatus("Y");// 已完成
				} else {
					// 未完成, 需要区分已过期G/未开始W/进行中U
					// course.setCourseStatus("W");// 未开始
				}
			} else {// 线下课程通过查询签到记录, 来判断是否已完成
				TomCourseSignInRecords signInExample = new TomCourseSignInRecords();
				signInExample.setCode(code);
				signInExample.setCourseId(course.getCourseId());
				int signInNum = courseSignInRecordsMapper.countByExample(signInExample);
				if (signInNum > 0) {
					course.setCourseStatus("Y");// 已签到
				} else {
					// 未签到, 需要区分已过期G/未开始W/进行中U
					// course.setCourseStatus("1");
				}
			}

			// 查询开始结束时间，不查收藏课程
			TomActivityProperty activityProperty = null;
			if ("1".equals(identifying)) {//未完成时, 才查询对应的活动
				Map<Object, Object> map1 = new HashMap<Object, Object>();
				map1.put("code", code);
				map1.put("courseId", course.getCourseId());
				map1.put("activityId", course.getActivityId());
				activityProperty = activityPropertyMapper.selectByNewDate(map1);
			}

			Date startTime = null;
			Date endTime = null;
			if (null != activityProperty) {
//				course.setActivityName(
//						activityMapper.selectByPrimaryKey(activityProperty.getActivityId()).getActivityName());

				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				if (activityProperty.getStartTime() != null) {// 线上课程有开始时间
					startTime = activityProperty.getStartTime();
					course.setBeginTime(format.format(activityProperty.getStartTime()));
					course.setBeginTimeS(String.valueOf(activityProperty.getStartTime().getTime()));
				} else {// 线下课程没有开始时间, 以活动的时间计算
					TomActivity activity = activityMapper.selectByPrimaryKey(activityProperty.getActivityId());
					startTime = activity.getActivityStartTime();
				}
				if (activityProperty.getEndTime() != null) {// 线上课程有结束时间
					endTime = activityProperty.getEndTime();
					course.setEndTime(format.format(activityProperty.getEndTime()));
					course.setEndTimeS(String.valueOf(activityProperty.getEndTime().getTime()));
				} else {// 线下课程没有结束时间, 以活动的时间计算
					TomActivity activity = activityMapper.selectByPrimaryKey(activityProperty.getActivityId());
					endTime = activity.getActivityEndTime();
				}
			} else {
				course.setBeginTime("");
				course.setEndTime("");
			}

			if (startTime != null) {//防止脏数据造成的异常, 如活动被删除,但相关的表未删除干净
				if ("1".equals(identifying)) {//未完成
					// 判断活动中的课程状态
					if (!"Y".equals(course.getCourseStatus())) {// 未完成时,
																// 需要区分已过期G/未开始W/进行中U

						if (startTime.after(new Date())) {// 开始时间在当前之后, 未开始
							course.setCourseStatus("W");
						} else if (endTime.before(new Date())) {// 结束时间在当前之前, 已过期
							course.setCourseStatus("G");
						} else {// 进行中
							course.setCourseStatus("U");
						}
					}
				}
			}

			if ("en".equals(lang)) {
				course.setCourseName(course.getCourseNameEn());
				course.setCourseIntroduction(course.getCourseIntroductionEn());
				course.setCourseImg(course.getCourseImgEn());
			}
		}
		page.setDate(list);
		myCourse.setCourseList(page);
		String pageJson = mapper.writeValueAsString(myCourse);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：[5.24 课程浏览接口]
	 *
	 * 创建者：wjx 创建时间: 2016年5月5日 下午3:17:48
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseViewers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String courseId = request.getParameter("courseId");
		TomCourses course = coursesMapper.selectByPrimaryKey(Integer.parseInt(courseId));
		course.setViewers(course.getViewers() + 1);
		courseService.updateByPrimaryKey(course);
		return new Result();
	}

	/**
	 * 
	 * 功能描述：[5.25章节详情接口]
	 *
	 * 创建者：JCX 创建时间: 2016年5月14日 上午10:44:06
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleCourseSectionDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourseSection courseSection = courseSectionMapper
				.selectByPrimaryKey(Integer.parseInt(request.getParameter("sectionId")));
		courseSection.setCourseFileuploadAddress(courseSection.getSectionAddress());
		String pageJson = mapper.writeValueAsString(courseSection);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：[5.3测试数据导出] 创建时间：2016年11月23日 下午18:00:00
	 * 
	 * 
	 */

	public Result addTest(String json) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomTest tomTest = new TomTest();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jo = JSONObject.fromObject(json);
		List answerList = jo.getJSONArray("testAnswers");
		JsonMapper mapper = new JsonMapper();
		for (int i = 0; i < answerList.size(); i++) {

			TomTestDto tomTestDto = mapper.fromJson(answerList.get(i).toString(), TomTestDto.class);
			tomTest.setCourseId(Integer.parseInt(jo.getString("courseId")));
			tomTest.setTestName(jo.getString("testName"));
			tomTest.setTestUserId(jo.getString("userId"));
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("courseId", Integer.parseInt(jo.getString("courseId")));
			map.put("testUserId", jo.getString("userId"));
			map.put("testQuestionId", tomTestDto.getTestQuestionId());
			TomTest tomList = tomTestMapper.selectByCourseIdUserId(map);
			TomEmp emp = empMapper.selectByPrimaryKey(jo.getString("userId"));
			tomTest.setTestUserName(emp.getName());
			tomTest.setTestQuestionId(tomTestDto.getTestQuestionId());
			tomTest.setTestQuestionName(tomTestDto.getTestQuestionName());
			tomTest.setTestStartTime(format.parse(jo.getString("testStartTime")));
			tomTest.setTestEndTime(format.parse(jo.getString("testEndTime")));
			tomTest.setTestUserAnswer(tomTestDto.getTestUserAnswer());

			if (tomList != null) {
				tomTest.setTestId(tomList.getTestId());
				testService.updateByPrimaryKey(tomTest);
			} else {
				testService.insert(tomTest);
			}

		}
		return new Result("Y", "", "0", "");

	}

	/**
	 * 
	 * 功能描述：[线上课程后通过前置任务的考试的人数
	 *
	 * 创建者：cjx 创建时间: 2016年12月12日 上午9:43:52
	 * 
	 * @param tomSendMessage
	 * @return
	 */
	public List<String> finishStatusDown(TomActivityProperty tomActivityProperty) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<String> listEmp = new ArrayList<String>();
		List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
				.selectByActivityId2(tomActivityProperty.getActivityId());
		String preTask = tomActivityProperty.getPreTask();
		String[] split = preTask.split(",");
		for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
			String code = tomActivityEmpsRelation.getCode();
			boolean a = false;
			int i = 0;
			for (String str : split) {
				TomActivityProperty example = new TomActivityProperty();
				example.setActivityId(tomActivityProperty.getActivityId());
				example.setSort(Integer.parseInt(str));
				TomActivityProperty selectTask = tomActivityPropertyMapper.selectTask(example);

				if (null != selectTask) {
					if (null != selectTask.getCourseId()) {
						TomCourses course = tomCoursesMapper.selectByPrimaryKey(selectTask.getCourseId());
						if ("Y".equals(course.getCourseOnline())) {
							a = true;
							Map<Object, Object> map = new HashMap<Object, Object>();
							map.put("courseId", selectTask.getCourseId());
							map.put("code", code);
							List<TomCourseLearningRecord> selectLearnRecord = tomCourseLearningRecordMapper
									.selectLearnRecord(map);
							if (null != selectLearnRecord) {
								i++;
							}
						} else {

							TomCourseSignInRecords courseSig = new TomCourseSignInRecords();
							courseSig.setCode(code);
							courseSig.setCourseId(selectTask.getCourseId());
							List<TomCourseSignInRecords> selectByExample = tomCourseSignInRecordsMapper
									.selectByExample(courseSig);
							if (null != selectByExample) {
								i++;
							}
						}
					} else {
						TomExamScore scoreExample = new TomExamScore();
						scoreExample.setCode(code);
						scoreExample.setExamId(selectTask.getExamId());
						List<TomExamScore> selectListByExample = tomExamScoreMapper.selectListByExample(scoreExample);
						if (null != selectListByExample) {
							i++;
						}
					}
				}
			}
			if (i == split.length && a == true) {
				listEmp.add(code);
			}
		}
		return listEmp;

	}

	public Result minuteupdateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String courseId = request.getParameter("courseId");
		String userId = request.getParameter("userId");
		String sectionId = request.getParameter("courseSectionId");
		String driver = request.getParameter("driver");
		TomCourseSection tomCourseSection = courseSectionMapper.selectByPrimaryKey(Integer.valueOf(sectionId));
		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<Object, Object> page = new HashMap<Object, Object>();
		String json = "";
		map.put("userId", userId);
		map.put("courseId", courseId);
		map.put("sectionId", sectionId);
		TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
		userInfo.setLearningTime(String.valueOf(Integer.parseInt(userInfo.getLearningTime()) + 60));
		// 插入操作日志
		TomEmp emp = empMapper.selectByPrimaryKey(userId);
		TomLearnTimeLog learnTimeLog = new TomLearnTimeLog();
		learnTimeLog.setCreateTime(new Date());
		learnTimeLog.setCode(userId);
		learnTimeLog.setName(emp.getName());
		learnTimeLog.setCourseId(Integer.valueOf(courseId));
		learnTimeLog.setSectionId(Integer.valueOf(sectionId));
		learnTimeLog.setTime(60);
		learnTimeLog.setUseragent("-");
		courseService.insertSelective(learnTimeLog);
		TomCourseCnEu learnMessage = courseCnEuMapper.selectCourseLearn(map);
		if (learnMessage == null) {
			TomCourseCnEu cneu = zhongOudetail(courseId, userId, sectionId);
			if (cneu == null) {
				return new Result("N", "error", "0", "");
			} else {
				page.put("mintime", cneu.getMinTime());
				page.put("historytime", cneu.getHistoryTime());
				if (cneu.getHistoryTime() >= cneu.getMinTime()) {
					page.put("status", "Y");
				} else {
					page.put("status", "N");
				}
				json = mapper.writeValueAsString(page);
				json = json.replaceAll(":null", ":\"\"");
			}
		} else {
			learnMessage.setHistoryTime(learnMessage.getHistoryTime() + 1);
			Integer m = learnMessage.getHistoryTime() - learnMessage.getMinTime();
			if (learnMessage.getMinTime() != 0) {
				Integer s = (learnMessage.getHistoryTime() / learnMessage.getMinTime()) * 100;
				if (s < 100 || s == 100) {
					learnMessage.setLearningSchedule(String.valueOf(s) + "%");
				} else {
					learnMessage.setLearningSchedule("100%");
				}
			} else {
				learnMessage.setLearningSchedule("0%");
			}
			if (m > 0 || m == 0) {
				courseCnEuMapper.updateSerlective(learnMessage);
				TomCourseSection example = new TomCourseSection();
				example.setCourseId(Integer.valueOf(courseId));
				List<TomCourseSection> allSection = courseSectionMapper.selectListByExample(example);
				boolean status = true;
				for (TomCourseSection section1 : allSection) {
					TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
					sectionLearning.setCode(userId);
					sectionLearning.setCourseSectionId(section1.getSectionId());
					int flag1 = courseSectionLearningRecordMapper.countByExample(sectionLearning);
					if (section1.getSectionSort() == tomCourseSection.getSectionSort()) {
						if (flag1 == 0) {
							sectionLearning.setCreateTime(new Date());
							sectionLearning.setLearningStates("Y");
							courseService.insertSectionLearning(sectionLearning);
						}
					} else {
						if (flag1 == 0) {
							status = false;
						}
					}
				}
				if (status) {
					TomCourseLearningRecord courseLearningRecord = new TomCourseLearningRecord();
					courseLearningRecord.setCode(userId);
					courseLearningRecord.setCourseId(Integer.valueOf(courseId));
					if (courseLearningRecordMapper.countByExample(courseLearningRecord) == 0) {
						
						TomCourses courses = coursesMapper.selectByPrimaryKey(Integer.valueOf(courseId));
						courseLearningRecord.setLearningDate(new Date());
						courseService.insertSelective(courseLearningRecord);

						DBContextHolder.setDbType(DBContextHolder.MASTER);
						// 前置任务消息
						Map<Object, Object> propertyMap = new HashMap<Object, Object>();
						propertyMap.put("code", userId);
						propertyMap.put("courseId", tomCourseSection.getCourseId());
						// 根据课程与人员查询正在进行的活动
						List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
								.selectByCourseAndCodeMessage(propertyMap);
						courseService.preTask(userId, selectByCourseAndCode);

						userInfo.setCourseNumber(String.valueOf(Integer.parseInt(userInfo.getCourseNumber()) + 1));
						userInfo.seteNumber(userInfo.geteNumber() + courses.geteCurrency());
						userInfo.setAddUpENumber(userInfo.getAddUpENumber() + courses.geteCurrency());
						TomEbRecord ebRecord = new TomEbRecord();
						ebRecord.setCode(userId);
						ebRecord.setUpdateTime(new Date());
						ebRecord.setExchangeNumber(courses.geteCurrency());
						ebRecord.setRoad("2");
						commodityService.insertSelective(ebRecord);
						
					}
					// else{
					// DBContextHolder.setDbType(DBContextHolder.MASTER);
					// //前置任务消息
					// Map<Object,Object> propertyMap = new
					// HashMap<Object,Object>();
					// propertyMap.put("code",userId);
					// propertyMap.put("courseId",
					// tomCourseSection.getCourseId());
					// //根据课程与人员查询正在进行的活动
					// List<TomActivityProperty> selectByCourseAndCode =
					// activityPropertyMapper.selectByCourseAndCodeMessage(propertyMap);
					// courseService.preTask(userId, selectByCourseAndCode);
					// }
				}
				page.put("status", "Y");
			} else {
				courseCnEuMapper.updateSerlective(learnMessage);
				page.put("status", "N");
			}
			page.put("mintime", learnMessage.getMinTime());
			page.put("historytime", learnMessage.getHistoryTime());
			userInfoServise.updateByCodetoAPI(userInfo);
			json = mapper.writeValueAsString(page);
			json = json.replaceAll(":null", ":\"\"");
		}
		return new Result("Y", json, "0", "");
	}

	// 请求中欧数据接口
	public TomCourseCnEu zhongOudetail(String courseId, String userId, String sectionId) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String pageJson = null;
		String function = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		function = "index.php?r=front/scorm/getInfo";
		TomCourseSection tomCourseSection = courseSectionMapper.selectByPrimaryKey(Integer.valueOf(sectionId));
		TomCourses courses = coursesMapper.selectByPrimaryKey(tomCourseSection.getCourseId());
		String apiUrl = Config.getProperty("zoApiUrl");
		String cid = tomCourseSection.getCid();
		String corpid = Config.getProperty("zoCorpid");
		String secret_key = Config.getProperty("zosecretKey");
		String timestamp = MD5Util.create_timestamp();
		String sig = MD5Util.MD5(secret_key + MD5Util.MD5(corpid + cid).toLowerCase()).toLowerCase();
		HttpGet get = new HttpGet(
				apiUrl + function + "&corpid=" + corpid + "&cid=" + cid + "&timestamp=" + timestamp + "&sig=" + sig);

		HttpResponse res = HttpClients.createDefault().execute(get);
		String result = EntityUtils.toString(res.getEntity());
		JSONObject object = JSONObject.fromObject(result);
		String min_study_time = "";
		String total_time = "";
		String data = object.getString("data");
		TomCourseCnEu learndetail = new TomCourseCnEu();
		if ("null".equals(data)) {
			learndetail = null;
		} else {
			JSONObject data1 = JSONObject.fromObject(data);
			min_study_time = data1.getString("min_study_time");
			total_time = data1.getString("total_time");

			learndetail.setUserId(userId);
			learndetail.setCourseId(courseId);
			map.put("userId", userId);
			map.put("courseId", courseId);
			map.put("sectionId", sectionId);
			TomCourseCnEu learnMessage = courseCnEuMapper.selectCourseLearn(map);
			if (learnMessage == null) {
				learndetail.setUserId(userId);
				learndetail.setCourseId(courseId);
				learndetail.setSectionId(sectionId);
				learndetail.setLearningTime(Integer.parseInt(total_time));
				learndetail.setMinTime(Integer.parseInt(min_study_time));
				learndetail.setFirstTime(sdf.format(new Date()));
				learndetail.setHistoryTime(1);
				learndetail.setLearningNumber(1);
				if (learndetail.getMinTime() != 0) {
					Integer s = (learndetail.getHistoryTime() / learndetail.getMinTime()) * 100;
					if (s < 100 || s == 100) {
						learndetail.setLearningSchedule(String.valueOf(s) + "%");
					} else {
						learndetail.setLearningSchedule("100%");
					}
				} else {
					learndetail.setLearningSchedule("0%");
				}
				courseCnEuMapper.insertSelective(learndetail);
			} else {
				learnMessage.setLearningNumber(learnMessage.getLearningNumber() + 1);
				courseCnEuMapper.updateSerlective(learnMessage);
			}
		}
		return learndetail;
	}

	public Result learnStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String pageJson = null;
		String sectionId = request.getParameter("courseSectionId");
		String userId = request.getParameter("userId");
		String driver = request.getParameter("driver");
		String function = "";
		CourseSectionLearningRecord page = new CourseSectionLearningRecord();
		if ("pc".equals(driver)) {
			function = "index.php?r=front/scorm/shareStudyProgressWithTrainplan";
		} else {
			function = "apps/controller/html5/partner/trainplan_course.php?method=show_progress&format=json";
		}
		TomCourseSection tomCourseSection = courseSectionMapper.selectByPrimaryKey(Integer.valueOf(sectionId));
		TomCourses courses = coursesMapper.selectByPrimaryKey(tomCourseSection.getCourseId());
		String apiUrl = Config.getProperty("zoApiUrl");
		String tid = tomCourseSection.getTid();
		String cid = tomCourseSection.getCid();
		String corpid = Config.getProperty("zoCorpid");
		String secret_key = Config.getProperty("zosecretKey");
		String timestamp = MD5Util.create_timestamp();
		String sig = MD5Util.MD5(secret_key + MD5Util.MD5(corpid + userId + userId + userId + tid + cid).toLowerCase())
				.toLowerCase();
		HttpGet get = new HttpGet(apiUrl + function + "&corpid=" + corpid + "&uid=" + userId + "&loginname=" + userId
				+ "&username=" + userId + "&tid=" + tid + "&cid=" + cid + "&timestamp=" + timestamp + "&sig=" + sig);
		HttpResponse res = HttpClients.createDefault().execute(get);
		String result = EntityUtils.toString(res.getEntity());
		if ("pc".equals(driver)) {
			result = result.substring(1, result.length() - 2);
		}
		JSONObject object = JSONObject.fromObject(result);
		String lesson_status = "";
		String total_time = "";
		if ("pc".equals(driver)) {
			lesson_status = object.getString("lesson_status");
			total_time = object.getString("total_time");
		} else {
			JSONObject data = JSONObject.fromObject(object.getString("data"));
			lesson_status = data.getString("lesson_status");
			total_time = data.getString("total_time");
		}
		int flag = 1;
		String totalTime[] = total_time.split(":");
		Integer total = Integer.valueOf(totalTime[0]) * 3600 + Integer.valueOf(totalTime[1]) * 60
				+ Integer.valueOf(totalTime[2].substring(0, totalTime[2].length() - 3));
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("sectionId", sectionId);
		map.put("code", userId);
		Integer history = learnTimeLogMapper.sumTime(map);
		if (history == null) {
			history = 0;
		}
		TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
		
		TomGradeRecords cords = new TomGradeRecords();
		cords.setCode(userId);
		cords.setCourseId(tomCourseSection.getCourseId());
		List<TomGradeRecords> cordslist = gradeRecordsMapper.selectListByRecords(cords);
		String istrue = "true";
		if ("completed".equals(lesson_status)) {
			TomCourseSection example = new TomCourseSection();
			example.setCourseId(tomCourseSection.getCourseId());
			List<TomCourseSection> allSection = courseSectionMapper.selectListByExample(example);
			boolean status = true;
			for (TomCourseSection section1 : allSection) {
				TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
				sectionLearning.setCode(userId);
				sectionLearning.setCourseSectionId(section1.getSectionId());
				int flag1 = courseSectionLearningRecordMapper.countByExample(sectionLearning);
				if (section1.getSectionSort() == tomCourseSection.getSectionSort()) {
					if (flag1 == 0) {
						sectionLearning.setCreateTime(new Date());
						sectionLearning.setLearningStates("Y");
						courseService.insertSectionLearning(sectionLearning);
					}
				} else {
					if (flag1 == 0) {
						status = false;
					}
				}
			}
			if (status) {
				TomCourseLearningRecord courseLearningRecord = new TomCourseLearningRecord();
				courseLearningRecord.setCode(userId);
				courseLearningRecord.setCourseId(tomCourseSection.getCourseId());
				if (courseLearningRecordMapper.countByExample(courseLearningRecord) == 0) {
					// TomUserInfo userInfo =
					// userInfoMapper.selectByPrimaryKey(userId);
					// TomCourses
					// courses=coursesMapper.selectByPrimaryKey(tomCourseSection.getCourseId());
					courseLearningRecord.setLearningDate(new Date());
					DBContextHolder.setDbType(DBContextHolder.MASTER);
					courseService.insertSelective(courseLearningRecord);
					
					userInfo.setLearningTime(String.valueOf(Integer.parseInt(userInfo.getLearningTime()) + total - history));
					// 插入操作日志
					TomEmp emp = empMapper.selectByPrimaryKey(userId);
					TomLearnTimeLog learnTimeLog = new TomLearnTimeLog();
					learnTimeLog.setCreateTime(new Date());
					learnTimeLog.setCode(userId);
					learnTimeLog.setName(emp.getName());
					learnTimeLog.setCourseId(courses.getCourseId());
					learnTimeLog.setSectionId(Integer.parseInt(sectionId));
					learnTimeLog.setTime(total - history);
					learnTimeLog.setUseragent("-");
					courseService.insertSelective(learnTimeLog);
					
					// 前置任务消息
					Map<Object, Object> propertyMap = new HashMap<Object, Object>();
					propertyMap.put("code", userId);
					propertyMap.put("courseId", tomCourseSection.getCourseId());
					// 根据课程与人员查询正在进行的活动
					List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
							.selectByCourseAndCodeMessage(propertyMap);
					courseService.preTask(userId, selectByCourseAndCode);

					userInfo.setCourseNumber(String.valueOf(Integer.parseInt(userInfo.getCourseNumber()) + 1));
					userInfo.seteNumber(userInfo.geteNumber() + courses.geteCurrency());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber() + courses.geteCurrency());
					TomEbRecord ebRecord = new TomEbRecord();
					ebRecord.setCode(userId);
					ebRecord.setUpdateTime(new Date());
					ebRecord.setExchangeNumber(courses.geteCurrency());
					ebRecord.setRoad("2");
					commodityService.insertSelective(ebRecord);
					userInfoServise.updateByCodetoAPI(userInfo);
				}
				// else{
				// DBContextHolder.setDbType(DBContextHolder.MASTER);
				// //前置任务消息
				// Map<Object,Object> propertyMap = new
				// HashMap<Object,Object>();
				// propertyMap.put("code",userId);
				// propertyMap.put("courseId", tomCourseSection.getCourseId());
				// //根据课程与人员查询正在进行的活动
				// List<TomActivityProperty> selectByCourseAndCode =
				// activityPropertyMapper.selectByCourseAndCodeMessage(propertyMap);
				// courseService.preTask(userId, selectByCourseAndCode);
				// }
				page.setCourseStudyStates("Y");
			} else {
				page.setCourseStudyStates("N");
			}

			// List<TomCourseSection> sectionList=new
			// ArrayList<TomCourseSection>();
			// if(tomCourseSection.getSectionSort()==null){
			// sectionList.add(tomCourseSection);
			// }else{
			// Map<Object,Object> map1 = new HashMap<Object,Object>();
			// map1.put("sort", tomCourseSection.getSectionSort());
			// map1.put("courseId", tomCourseSection.getCourseId());
			// sectionList=courseSectionMapper.selectBySort(map1);
			// }
			// for(TomCourseSection section:sectionList){
			// TomCourseSectionLearningRecord sectionLearning = new
			// TomCourseSectionLearningRecord();
			// sectionLearning.setCode(userId);
			// sectionLearning.setCourseSectionId(section.getSectionId());
			// flag =
			// courseSectionLearningRecordMapper.countByExample(sectionLearning);
			// if (flag == 0) {
			// sectionLearning.setCreateTime(new Date());
			// sectionLearning.setLearningStates("Y");
			// courseService.insertSectionLearning(sectionLearning);
			// }
			// }
			// TomCourseSection example = new TomCourseSection();
			// example.setCourseId(tomCourseSection.getCourseId());
			// if("h5".equals(driver)){
			// example.setSectionType("6");
			// }else{
			// example.setSectionType("7");
			// }
			// List<TomCourseSection> courseSections =
			// courseSectionMapper.selectListByExample(example);
			// for (TomCourseSection courseSection2 : courseSections) {
			/*
			 * TomCourseSectionLearningRecord courseSectionLearningRecord = new
			 * TomCourseSectionLearningRecord();
			 * courseSectionLearningRecord.setCourseSectionId(courseSection2.
			 * getSectionId()); courseSectionLearningRecord.setCode(userId);
			 */
			// if(tomCourseSection.getSectionSort().equals(courseSection2.getSectionSort())
			// &&
			// !tomCourseSection.getSectionId().equals(courseSection2.getSectionId())){
			// }else{
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			map1.put("sectionId", sectionId);
			map1.put("userId", userId);
			// map1.put("learningSchedule", "100%");
			List<TomCourseCnEu> tcce = courseCnEuMapper.selectCourseBycode(map1);
			if (tcce.size() == 0) {
				TomCourseCnEu record = new TomCourseCnEu();
				record.setCourseId(tomCourseSection.getCourseId().toString());
				record.setUserId(userId);
				record.setFirstTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

				record.setSectionId(sectionId);
				String function1 = "index.php?r=front/scorm/getInfo";
				String apiUrl1 = Config.getProperty("zoApiUrl");
				String cid1 = tomCourseSection.getCid();
				String corpid1 = Config.getProperty("zoCorpid");
				String secret_key1 = Config.getProperty("zosecretKey");
				String timestamp1 = MD5Util.create_timestamp();
				String sig1 = MD5Util.MD5(secret_key1 + MD5Util.MD5(corpid1 + cid1).toLowerCase()).toLowerCase();
				HttpGet get1 = new HttpGet(apiUrl1 + function1 + "&corpid=" + corpid1 + "&cid=" + cid1 + "&timestamp="
						+ timestamp1 + "&sig=" + sig1);

				HttpResponse res1 = HttpClients.createDefault().execute(get1);
				String result1 = EntityUtils.toString(res1.getEntity());
				JSONObject object1 = JSONObject.fromObject(result1);
				String min_study_time1 = "";
				String total_time1 = "";
				String data = object1.getString("data");
				if ("null".equals(data)) {
				} else {
					JSONObject data1 = JSONObject.fromObject(data);
					min_study_time1 = data1.getString("min_study_time");
					total_time1 = data1.getString("total_time");
				}
				record.setMinTime(Integer.parseInt(min_study_time1));
				record.setLearningTime(Integer.parseInt(total_time1));
				record.setHistoryTime(record.getMinTime());
				record.setLearningNumber(1);
				record.setLearningSchedule("100%");
				courseCnEuMapper.insertSelective(record);
			}
			// }
			// }
			/*
			 * if("false".equals(istrue)){ page.setCourseStudyStates("N");
			 * }else{
			 * 
			 * }
			 */

			if (cordslist.size() > 0) {
				page.setWhetherGrade("Y");
			} else {
				page.setWhetherGrade("N");
			}
			pageJson = mapper.writeValueAsString(page);
			pageJson = pageJson.replaceAll(":null", ":\"\"");
			TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
			courseEmpRelationExample.setCourseId(tomCourseSection.getCourseId());
			courseEmpRelationExample.setCode(userId);
			List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper
					.selectByExample(courseEmpRelationExample);
			if (courseEmpRelations.size() != 0) {
				TomCourseEmpRelation courseEmpRelation = courseEmpRelations.get(0);
				courseEmpRelation.setFinishStatus("Y");
				courseEmpRelation.setFinishTime(new Date());
				courseEmpRelation.setUpdateTime(new Date());
				courseService.update(courseEmpRelation);
			}
		} else {
			// TomCourseSection example = new TomCourseSection();
			// example.setCourseId(tomCourseSection.getCourseId());
			//
			// if("h5".equals(driver)){
			// example.setSectionType("6");
			// }else{
			// example.setSectionType("7");
			// }
			// List<TomCourseSection> courseSections =
			// courseSectionMapper.selectListByExample(example);
			// for (TomCourseSection courseSection2 : courseSections) {
			//
			// if(tomCourseSection.getSectionSort().equals(courseSection2.getSectionSort())
			// &&
			// !tomCourseSection.getSectionId().equals(courseSection2.getSectionId())){
			// }else{
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			map1.put("sectionId", sectionId);
			map1.put("userId", userId);
			map1.put("learningSchedule", "100%");
			List<TomCourseCnEu> tcce = courseCnEuMapper.selectCourseBycode(map1);
			if (tcce.size() > 0) {
				TomCourseSection example = new TomCourseSection();
				example.setCourseId(tomCourseSection.getCourseId());
				List<TomCourseSection> allSection = courseSectionMapper.selectListByExample(example);
				boolean status = true;
				for (TomCourseSection section1 : allSection) {
					TomCourseSectionLearningRecord sectionLearning = new TomCourseSectionLearningRecord();
					sectionLearning.setCode(userId);
					sectionLearning.setCourseSectionId(section1.getSectionId());
					int flag1 = courseSectionLearningRecordMapper.countByExample(sectionLearning);
					if (section1.getSectionSort() == tomCourseSection.getSectionSort()) {
						if (flag1 == 0) {
							sectionLearning.setCreateTime(new Date());
							sectionLearning.setLearningStates("Y");
							courseService.insertSectionLearning(sectionLearning);
						}
					} else {
						if (flag1 == 0) {
							status = false;
						}
					}
				}
				if (status) {
					TomCourseLearningRecord courseLearningRecord = new TomCourseLearningRecord();
					courseLearningRecord.setCode(userId);
					courseLearningRecord.setCourseId(tomCourseSection.getCourseId());
					if (courseLearningRecordMapper.countByExample(courseLearningRecord) == 0) {
						// TomUserInfo userInfo =
						// userInfoMapper.selectByPrimaryKey(userId);
						// TomCourses
						// courses=coursesMapper.selectByPrimaryKey(tomCourseSection.getCourseId());
						courseLearningRecord.setLearningDate(new Date());
						DBContextHolder.setDbType(DBContextHolder.MASTER);
						courseService.insertSelective(courseLearningRecord);

						
						// 前置任务消息
						Map<Object, Object> propertyMap = new HashMap<Object, Object>();
						propertyMap.put("code", userId);
						propertyMap.put("courseId", tomCourseSection.getCourseId());
						// 根据课程与人员查询正在进行的活动
						List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
								.selectByCourseAndCodeMessage(propertyMap);
						courseService.preTask(userId, selectByCourseAndCode);

						userInfo.setCourseNumber(String.valueOf(Integer.parseInt(userInfo.getCourseNumber()) + 1));
						userInfo.seteNumber(userInfo.geteNumber() + courses.geteCurrency());
						userInfo.setAddUpENumber(userInfo.getAddUpENumber() + courses.geteCurrency());
						TomEbRecord ebRecord = new TomEbRecord();
						ebRecord.setCode(userId);
						ebRecord.setUpdateTime(new Date());
						ebRecord.setExchangeNumber(courses.geteCurrency());
						ebRecord.setRoad("2");
						commodityService.insertSelective(ebRecord);
						userInfoServise.updateByCodetoAPI(userInfo);
					}
					page.setCourseStudyStates("Y");
				}
			} else {
				page.setCourseStudyStates("N");
			}
			if (cordslist.size() > 0) {
				page.setWhetherGrade("Y");
			} else {
				page.setWhetherGrade("N");
			}

			pageJson = mapper.writeValueAsString(page);
			pageJson = pageJson.replaceAll(":null", ":\"\"");
		}
		// if("true".equals(istrue)){
		// TomCourseLearningRecord courseLearningRecord = new
		// TomCourseLearningRecord();
		// courseLearningRecord.setCode(userId);
		// courseLearningRecord.setCourseId(tomCourseSection.getCourseId());
		// if (courseLearningRecordMapper.countByExample(courseLearningRecord)
		// == 0) {
		// courseLearningRecord.setLearningDate(new Date());
		// courseService.insertSelective(courseLearningRecord);
		// userInfo.setCourseNumber(String.valueOf(Integer.parseInt(userInfo.getCourseNumber())+1));
		// userInfo.seteNumber(userInfo.geteNumber()+ courses.geteCurrency());
		// userInfo.setAddUpENumber(userInfo.getAddUpENumber()+
		// courses.geteCurrency());
		// TomEbRecord ebRecord = new TomEbRecord();
		// ebRecord.setCode(userId);
		// ebRecord.setUpdateTime(new Date());
		// ebRecord.setExchangeNumber(courses.geteCurrency());
		// ebRecord.setRoad("2");
		// commodityService.insertSelective(ebRecord);
		//userInfoServise.updateByCodetoAPI(userInfo);
		// }
		// }
		userInfoServise.updateByCodetoAPI(userInfo);
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：[线下课程报名]
	 *
	 * 创建者：wjx 创建时间: 2017年1月3日 下午4:20:53
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Result offlineApply(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String code = request.getParameter("userId");
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		Integer courseClasses = Integer.valueOf(request.getParameter("courseClasses"));
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		map.put("code", code);
		map.put("courseClasses", courseClasses);
		TomCourseApply apply = applyMapper.selectApply(map);
		if (apply == null) {
			apply = new TomCourseApply();
			apply.setApplyTime(new Date());
			apply.setCode(code);
			apply.setCourseId(courseId);
			apply.setCourseClasses(courseClasses);
			apply.setStatus("Y");
			applyMapper.insert(apply);
		} else if ("Y".equals(apply.getStatus())) {
			apply.setStatus("N");
			applyMapper.updateByPrimaryKeySelective(apply);
		} else if ("N".equals(apply.getStatus())) {
			apply.setStatus("Y");
			applyMapper.updateByPrimaryKeySelective(apply);
		}
		return new Result("Y", "", "0", "");
	}

	/**
	 * 
	 * 功能描述：[线下课程签到]
	 *
	 * 创建者：wjx 创建时间: 2017年1月3日 下午5:00:21
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Result offlineSign(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String code = request.getParameter("userId");
		String classId = request.getParameter("classId");
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		TomCourses tomCourse = tomCoursesMapper.selectByPrimaryKey(courseId);
		
		TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(code);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		map.put("code", code);
		map.put("classId", classId);
		int signCount = signMapper.signCount(map);
		if(signCount >= 1 && "N".equals(tomCourse.getReapeatSign())){//不可重复签到;
			return new Result("N", "\"您已经签过到了！\"", "0", "");
		}
		List<TomCourseClasses> classesList = classesMapper.selectByCourseId(map);
		if (classesList.size() > 0) {
			TomOfflineSign sign = new TomOfflineSign();
			sign.setClassesId(Integer.parseInt(classId));
			sign.setCode(code);
			sign.setCourseId(courseId);
			sign.setSignTime(new Date());
			signMapper.insert(sign);

			DBContextHolder.setDbType(DBContextHolder.MASTER);
			// 前置任务消息
			Map<Object, Object> propertyMap = new HashMap<Object, Object>();
			propertyMap.put("code", code);
			propertyMap.put("courseId", courseId);
			// 根据课程与人员查询正在进行的活动
			List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
					.selectByCourseAndCodeMessage(propertyMap);
			courseService.preTask(code, selectByCourseAndCode);

			userInfo.seteNumber(userInfo.geteNumber() + tomCourse.geteCurrency());
			userInfoMapper.updateByPrimaryKeySelective(userInfo);
			TomEbRecord ebRecord = new TomEbRecord();
			ebRecord.setCode(code);
			ebRecord.setUpdateTime(new Date());
			ebRecord.setExchangeNumber(tomCourse.geteCurrency());
			ebRecord.setRoad("11");
			commodityService.insertSelective(ebRecord);
			return new Result("Y", "\"签到成功！\"", "0", "");
		}
		return new Result("N", "\"没有此班次！\"", "0", "");
	}

	/**
	 * 
	 * 功能描述：[下线课程列表]
	 *
	 * 创建者：wjx 创建时间: 2017年1月4日 上午10:16:15
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Result offlineList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page = new PageData<TomCourses>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		String lang = request.getParameter("lang");
		TomCourses example = new TomCourses();
		String userId = request.getParameter("userId");
		String keyWord = request.getParameter("KeyWord");
		if (keyWord != null) {
			keyWord = keyWord.replaceAll("/", "//");
			keyWord = keyWord.replaceAll("%", "/%");
			keyWord = keyWord.replaceAll("_", "/_");

		}
		String terminal = request.getParameter("terminal");
		if ("en".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPcen("Y");
			} else {
				example.setH5en("Y");
			}
			example.setCourseNameEn(keyWord);
		} else if ("cn".equals(lang)) {
			if ("pc".equals(terminal)) {
				example.setPccn("Y");
			} else {
				example.setH5cn("Y");
			}
			example.setCourseName(keyWord);
		}
		example.setCode(userId);
		example.setCourseType(request.getParameter("classifyId"));
		example.setIsExcellentCourse(request.getParameter("isExcellentCourse"));
		example.setStatus("Y");
		example.setCourseOnline("N");
		map.put("example", example);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum", Integer.parseInt(request.getParameter("pageSize")));
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		map.put("listOrder", request.getParameter("listOrder"));
		map.put("orderBy", request.getParameter("orderBy"));
		int count = coursesMapper.countByExampleApi(example);
		List<TomCourses> list = coursesMapper.selectListByPageApi(map);
		if ("en".equals(lang)) {
			for (TomCourses course : list) {
				course.setCourseName(course.getCourseNameEn());
				course.setCourseIntroduction(course.getCourseIntroductionEn());
				course.setCourseImg(course.getCourseImgEn());
			}
		}
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	/**
	 * 
	 * 功能描述：[线下课程详情]
	 *
	 * 创建者：wjx 创建时间: 2017年1月4日 上午10:16:28
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Result offlineDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Integer courseId = Integer.valueOf(request.getParameter("courseId"));
		String code = request.getParameter("userId");
		String lang = request.getParameter("lang");
		OfflineCourseDto dto = new OfflineCourseDto();
		TomCourses courses = coursesMapper.selectByPrimaryKey(courseId);
		// 封装章节信息
		TomCourseSection example = new TomCourseSection();
		example.setCourseId(courseId);
		example.setLang(lang);
		List<TomCourseSection> courseSections = courseSectionService.selectListByEXample(example);
		if ("en".equals(lang)) {
			courses.setCourseName(courses.getCourseNameEn());
			courses.setCourseIntroduction(courses.getCourseIntroductionEn());
			courses.setCourseImg(courses.getCourseImgEn());
			for (TomCourseSection tomCourseSection : courseSections) {
				tomCourseSection.setSectionName(tomCourseSection.getSectionNameEn());
			}
		}
		// 判断课程是否需要评分
		TomGradeRecords gradeExample = new TomGradeRecords();
		gradeExample.setCode(code);
		gradeExample.setCourseId(courses.getCourseId());
		gradeExample.setGradeType("C");
		List<TomGradeRecords> gradeRecords1 = gradeRecordsMapper.selectListByRecords(gradeExample);
		gradeExample.setGradeType("L");
		List<TomGradeRecords> gradeRecords2 = gradeRecordsMapper.selectListByRecords(gradeExample);
		if (gradeRecords1.size() > 0 && gradeRecords2.size() > 0) {
			courses.setIsGrade("Y");
		} else {
			courses.setIsGrade("N");
		}
		// 收藏、点赞状态
		TomFavoriteCourse favoriteCourse = new TomFavoriteCourse();
		favoriteCourse.setCode(code);
		favoriteCourse.setCourseId(courses.getCourseId());
		favoriteCourse.setStatus("Y");
		int countFavorite = coursesMapper.countByCourseIdAndCode(favoriteCourse);
		if (countFavorite > 0) {
			courses.setCollect("Y");
		} else {
			courses.setCollect("N");
		}

		TomCourseThumbUp courseThumbUp = new TomCourseThumbUp();
		courseThumbUp.setCode(code);
		courseThumbUp.setCourseId(courses.getCourseId());
		courseThumbUp.setStatus("Y");
		int countThumbUp = coursesMapper.countThumbUpByCourseIdAndCode(courseThumbUp);
		if (countThumbUp > 0) {
			courses.setPraise("Y");
		} else {
			courses.setPraise("N");
		}
		List<TomLecturer> lecturerList = new ArrayList<TomLecturer>();
		if (courses.getLecturers() != null) {
			String lecturerIds[] = courses.getLecturers().split(",");
			for (String lecturerId : lecturerIds) {
				if (StringUtil.isNotBlank(lecturerId)) {
					TomLecturer lecturer = lecturerMapper.selectByPrimaryKey(Integer.valueOf(lecturerId));
					if (null != lecturer) {
						if ("en".equals(lang)) {
							lecturer.setLecturerName(lecturer.getLecturerNameEn());
							lecturer.setLecturerIntroduction(lecturer.getLecturerIntroductionEn());
						}
						lecturerList.add(lecturer);
					}
				}
			}
		}
		PropertyUtils.copyProperties(dto, courses);
		dto.setNeedApply(courses.getOpenComment());
		dto.setSectionList(courseSections);
		dto.setLecturerList(lecturerList);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		List<TomCourseClasses> classList = classesMapper.selectByCourseId(map);
		for (TomCourseClasses classes : classList) {
			if ("en".equals(lang)) {
				classes.setClassesName(classes.getClassesNameEn());
				classes.setClassesAddress(classes.getClassesAddressEn());
			}
			// map.put("courseId", courseId);
			map.put("code", code);
			map.put("courseClasses", classes.getClasses());
			TomCourseApply apply = applyMapper.selectApply(map);
			if (apply == null) {
				classes.setApplyStatus("N");
			} else {
				classes.setApplyStatus(apply.getStatus());

			}
			List<TomOfflineSign> signList = signMapper.selectSignRecord(map);
			if (signList.size() > 0) {
				classes.setSignStatus("Y");
			} else {
				classes.setSignStatus("N");
			}
		}
		dto.setClassesList(classList);
		TomCourseDetail detail = detailMapper.selectByCourseId(map);
		if (detail != null) {
			if ("en".equals(lang)) {
				dto.setCourseDetail(detail.getCourseDetailEn());
			} else {
				dto.setCourseDetail(detail.getCourseDetail());
			}
		}
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(dto);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}

	public Result classMates(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String courseId = request.getParameter("courseId");
		String courseClasses = request.getParameter("courseClasses");
		String code = request.getParameter("userId");
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		map.put("code", code);
		map.put("courseClasses", courseClasses);
		List<TomCourseApply> applyList = applyMapper.classmates(map);
		List<TomUserInfo> userList = new ArrayList<TomUserInfo>();
		for (TomCourseApply apply : applyList) {
			TomUserInfo user = userInfoMapper.selectByPrimaryKey(apply.getCode());
			userList.add(user);
		}
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(userList);
		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, "0", "");
	}
}
