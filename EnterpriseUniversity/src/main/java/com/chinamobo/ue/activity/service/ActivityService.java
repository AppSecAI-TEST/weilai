package com.chinamobo.ue.activity.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomActivityDeptMapper;
import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityFeesMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomActivityQrMapper;
import com.chinamobo.ue.activity.dao.TomProjectResourceMapper;
import com.chinamobo.ue.activity.dto.TomActivityDto;
import com.chinamobo.ue.activity.dto.TomActivityFreesDto;
import com.chinamobo.ue.activity.dto.TomActivityPropertyDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityDept;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelationKey;
import com.chinamobo.ue.activity.entity.TomActivityFees;
import com.chinamobo.ue.activity.entity.TomActivityFeesKey;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.entity.TomActivityQr;
import com.chinamobo.ue.activity.entity.TomProjectResource;
import com.chinamobo.ue.cache.CacheConstant;
import com.chinamobo.ue.cache.cacher.TomExamCacher;
import com.chinamobo.ue.cache.redis.RedisCacheManager;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseClassesMapper;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomLecturerMapper;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomLecturer;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.service.ExamService;
import com.chinamobo.ue.exception.EleException;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomGrpOrgRelationMapper;
import com.chinamobo.ue.system.dao.TomMessageDetailsMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomGrpOrgRelation;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.QRCodeUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import jersey.repackaged.com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 版本: [1.0] 功能说明: 培训活动管理
 *
 * 作者: WangLg 创建时间: 2016年3月9日 下午5:19:50
 */
@Service
@SuppressWarnings("all")
public class ActivityService {// extends BaseService{
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomActivityFeesMapper activityFeesMapper;// 培训活动费用统计
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;// 培训活动配置
	// private TomActivityExapInfoMapper activityExapInfoMapper;//培训活动补考信息
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;// 培训活动人员
	@Autowired
	private TomActivityDeptMapper activityDeptMapper;// 培训活动推送部门负责人
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomGrpOrgRelationMapper tomGrpOrgRelationMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomCourseEmpRelationMapper tomCourseEmpRelationMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	@Autowired
	private TomRetakingExamMapper tomRetakingExamMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	@Autowired
	private ExamService examService;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private TomActivityQrMapper activityQrMapper;
	@Autowired
	private TomLecturerMapper tomLecturerMapper;
	@Autowired
	private TomCourseClassesMapper tomCourseClassesMapper;
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Autowired
	private TomProjectResourceMapper tomProjectResourceMapper;
	ObjectMapper mapper = new ObjectMapper();
	String filePath1 = Config.getProperty("file_path");
	@Autowired
	private RedisCacheManager redisCacheManager;
	
	JsonMapper jsonMappers = new JsonMapper();

	/**
	 * 功能描述：[R-添加培训活动人员]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */

	// TODO
	@Transactional
	public void modifyEmpRelation(TomActivityEmpsRelation activityEmpsR, String needApplyType, String flag) {
		activityEmpsR.preInsertData();
		activityEmpsR.preInsert();
		activityEmpsRelationMapper.insertSelective(activityEmpsR);
		if ("add".equals(flag)) {
			activityEmpsR.preInsertData();
			activityEmpsR.preInsert();
			activityEmpsRelationMapper.insertSelective(activityEmpsR);
		} else {
			activityEmpsR.preUpdateData();
			activityEmpsR.preUpdate();
			activityEmpsRelationMapper.updateByPrimaryKeySelective(activityEmpsR);
		}

	}

	/**
	 * 功能描述：[R-培训活动费用统计]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */
	@Transactional
	public void modifyActivityFees(TomActivityFees activityFees, String flag) {
		activityFees.preInsertData();
		activityFees.preInsert();
		activityFees.setCreateTime(new Date());
		activityFeesMapper.insertSelective(activityFees);
		if ("add".equals(flag)) {
			activityFees.preInsertData();
			activityFees.preInsert();
			activityFees.setCreateTime(new Date());
			activityFeesMapper.insertSelective(activityFees);
		} else {
			activityFees.preUpdateData();
			activityFees.preUpdate();
			activityFees.setCreateTime(new Date());
			activityFeesMapper.updateByPrimaryKeySelective(activityFees);
		}
	}

	/**
	 * 功能描述：[R-培训活动推送部门负责人]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */
	@Transactional
	public void modifyActivityDept(TomActivityDept activityDept, String flag) {
		activityDept.preInsertData();
		activityDept.preInsert();
		activityDeptMapper.insertSelective(activityDept);
		if ("add".equals(flag)) {
			activityDept.preInsertData();
			activityDept.preInsert();
			activityDeptMapper.insertSelective(activityDept);
		} else {
			activityDept.preUpdateData();
			activityDept.preUpdate();
			activityDeptMapper.updateByPrimaryKeySelective(activityDept);
		}
	}

	/**
	 * 功能描述：[R-培训活动配置]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 上午11:54:21
	 */
	@Transactional
	public void modifyActivityProperty(TomActivityProperty activityProperty, String flag) {
		activityProperty.preInsertData();
		activityProperty.preInsert();
		activityPropertyMapper.insertSelective(activityProperty);
		if ("add".equals(flag)) {
			activityProperty.preInsertData();
			activityProperty.preInsert();
			activityPropertyMapper.insertSelective(activityProperty);
		} else {
			activityProperty.preUpdateData();
			activityProperty.preUpdate();
			activityPropertyMapper.updateByPrimaryKeySelective(activityProperty);
		}
	}

	/**
	 * 功能描述：[添加培训活动]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午9:46:33
	 * 
	 * @param activity
	 */
	@Transactional
	public String addActivity(String jsonStr) throws Exception {
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		TomRetakingExam tomRetakingExam = null;
		int examId = 0;
		String BxAct = "";		//必修活动
		String BxActEn = "";
		String BxActStatus = "";
		String XxAct = "";		//选修活动
		String XxActEn = "";
		String XxActStatus = "";
		String BxCDown = "";		//必修线下课程
		String BxCDownEn = "";
		String BxCDownStatus = "";
		 List<TomMessageDetails> selectList = tomMessageDetailsMapper.selectList();
		 for(TomMessageDetails message:selectList){
			 if(message.getId()==1){
				 BxAct = message.getMessage();
				 BxActStatus = message.getStatus();
			 }
			 if(message.getId()==2){
				 BxActEn = message.getMessage();
			 }
			 if(message.getId()==3){
				 XxAct = message.getMessage();
				 XxActStatus = message.getStatus();
			 }
			 if(message.getId()==4){
				 XxActEn = message.getMessage();
			 }
			 if(message.getId()==5){
				 BxCDown = message.getMessage();
				 BxCDownStatus = message.getStatus();
			 }
			 if(message.getId()==6){
				 BxCDownEn = message.getMessage();
			 }
		 }
		JSONObject jsonObject = new JSONObject();
		JsonMapper mapper = new JsonMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObj = jsonObject.fromObject(jsonStr);
		TomActivity tomActivity = new TomActivity();
		String protocol = (String) jsonObj.get("protocol");
		//String packageId = jsonObj.getString("packageId");
		String number = numberRecordService.getNumber(MapManager.numberType("PXHD"));
		tomActivity.setActivityNumber(number);
		tomActivity.setProtocol(protocol);
		Date date = new Date();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ShiroUser user = ShiroUtils.getCurrentUser();
		if (protocol != null && "Y".equals(protocol)) {
			tomActivity.setProtocolStartTime(format1.parse(jsonObj.getString("protocolStartTime")));
			tomActivity.setProtocolEndTime(format1.parse(jsonObj.getString("protocolEndTime")));
			tomActivity.setTrainFee(jsonObj.getDouble("trainFee"));
		}
		String needApply = jsonObj.getString("needApply");
		tomActivity.setNeedApply(needApply);
		if (needApply != null && "Y".equals(needApply)) {
			tomActivity.setNumberOfParticipants(jsonObj.getInt("numberOfParticipants"));
			tomActivity.setApplicationStartTime(format1.parse(jsonObj.getString("applicationStartTime")));
			tomActivity.setApplicationDeadline(format1.parse(jsonObj.getString("applicationDeadline")));
			if (tomActivity.getApplicationDeadline().before(new Date())) {
				return "protected";
			}
		}
		tomActivity.setActivityName(jsonObj.getString("activityName"));
		tomActivity.setActivityNameEn(jsonObj.getString("activityNameEn"));
		tomActivity.setActivityStartTime(format1.parse(jsonObj.getString("activityStartTime")));
		if (tomActivity.getActivityStartTime().before(new Date())) {
			return "protected";
		}
		tomActivity.setActivityEndTime(format1.parse(jsonObj.getString("activityEndTime")));
		tomActivity.setIntroduction(jsonObj.getString("introduction"));
		tomActivity.setIntroductionEn(jsonObj.getString("introductionEn"));
		tomActivity.setActPicture(jsonObj.getString("activityImg"));
		tomActivity.setActPictureEn(jsonObj.getString("activityImgEn"));
		// if(jsonStr.indexOf("\"deptCodes\":")!=-1){
		tomActivity.setDepts(jsonObj.getString("deptCodes"));
		// }
		// if(jsonStr.indexOf("\"employeeGradeStr\":")!=-1){
		tomActivity.setStaffLevels(jsonObj.getString("employeeGradeStr"));
		// }
		// if(jsonStr.indexOf("\"city\":")!=-1){
		tomActivity.setCity(jsonObj.getString("city"));
		tomActivity.setCityEn(jsonObj.getString("cityEn"));
		tomActivity.setIsCN(jsonObj.getString("isCN"));
		tomActivity.setIsEN(jsonObj.getString("isEN"));
		// }

		tomActivity.setAdmins(jsonObj.getString("admins"));
		tomActivity.setAdminNames(jsonObj.getString("adminNames"));
		tomActivity.setCreateTime(date);
		tomActivity.setUpdateTime(date);
		tomActivity.setCreator(user.getName());
		tomActivity.setOperator(user.getName());
		tomActivity.setCreatorId(user.getCode());
		tomActivity.setParentClassifyId(jsonObj.getString("parentClassifyId"));
		tomActivity.setParentProjectClassifyName(jsonObj.getString("parentProjectClassifyName"));
		activityMapper.insertSelective(tomActivity);
		
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(tomActivity.getCreator());
		resource.setOperator(tomActivity.getOperator());
		resource.setParentProjectClassifyName(jsonObj.getString("parentProjectClassifyName"));
		resource.setProjectId(Integer.parseInt(jsonObj.getString("parentClassifyId")));
		resource.setResourceId(tomActivity.getActivityId());
		resource.setStatus("Y");
		resource.setType("A");
		resource.setUpdateTime(new Date());
		tomProjectResourceMapper.insertSelective(resource);
		Integer activityId = tomActivity.getActivityId();

		ObjectMapper jsonMapper = new ObjectMapper();
		JSONArray preTaskInfos = jsonObj.getJSONArray("preTaskInfo");
		List<TomActivityProperty> courseForMessage = new ArrayList<TomActivityProperty>();
		List<TomActivityProperty> examForMessage = new ArrayList<TomActivityProperty>();
		for (int i = 0; i < preTaskInfos.size(); i++) {
			TomActivityPropertyDto preTaskDto = jsonMapper.readValue(preTaskInfos.get(i).toString(),
					TomActivityPropertyDto.class);
			TomActivityProperty tomActivityProperty = new TomActivityProperty();
			tomActivityProperty.setActivityId(activityId);
			tomActivityProperty.setSort(preTaskDto.getSort());
			tomActivityProperty.setPackageId(Integer.valueOf(preTaskDto.getPackageId()));
			if(preTaskDto.getStartTime()!=null)
			tomActivityProperty.setStartTime(format2.parse(preTaskDto.getStartTime()));
			if(preTaskDto.getEndTime()!=null)
			tomActivityProperty.setEndTime(format2.parse(preTaskDto.getEndTime()));
			tomActivityProperty.setPreTask(preTaskDto.getPretaskId());
			tomActivityProperty.setCreateTime(date);
			tomActivityProperty.setUpdateTime(date);
			tomActivityProperty.setCreator(user.getName());
			tomActivityProperty.setOperator(user.getName());
			if (preTaskDto.getRetakingExamTimes() == null) {
				if (preTaskDto.getLecturerId() != null && !"".equals(preTaskDto.getLecturerId())) {
					tomActivityProperty.setLecturers(preTaskDto.getLecturerId());
					tomActivityProperty.setCourseAddress(preTaskDto.getCourseAddress());
					tomActivityProperty.setCourseTime(Double.valueOf(preTaskDto.getCourseTime()));
					if (preTaskDto.getUnitPrice() != null && !"".equals(preTaskDto.getUnitPrice())) {
						tomActivityProperty.setUnitPrice(Double.valueOf(preTaskDto.getUnitPrice()));
						// TomActivityFees activityFees=new TomActivityFees();
						// activityFees.setActivityId(activityId);
						// activityFees.setFeeName("讲师");
						// activityFees.setFee(Double.valueOf(preTaskDto.getTotalPrice()));
						// activityFees.setRemark(preTaskDto.getLecturerId()+"号讲师进行"+Double.valueOf(preTaskDto.getCourseTime())+"分钟的培训费用。");
						// activityFees.setStatus("Y");
						// activityFees.setCreator(user.getName());
						// activityFees.setCreateTime(date);
						// activityFees.setCreatorId(user.getCode());
						// activityFees.setOperator(user.getName());
						// activityFees.setUpdateTime(date);
						// activityFeesMapper.insertSelective(activityFees);
					}
					tomActivityProperty.setTotalPrice(Double.valueOf(preTaskDto.getTotalPrice()));

				}
				tomActivityProperty.setCourseId(Integer.valueOf(preTaskDto.getTaskId()));
//				TomCourses selectByPrimaryKey = coursesMapper.selectByPrimaryKey(Integer.valueOf(preTaskDto.getTaskId()));
//				if("N".equals(selectByPrimaryKey.getCourseOnline())){
//					Map<Object,Object> hashmap = new HashMap<Object,Object>();
//					hashmap.put("courseId", Integer.valueOf(preTaskDto.getTaskId()));
//					List<TomCourseClasses> CourseClassesList = tomCourseClassesMapper.selectByCourseIdSort(hashmap);
//					TomCourseClasses tomCourseClasses = CourseClassesList.get(0);
//					TomCourseClasses tomCourseClasses2 = CourseClassesList.get(CourseClassesList.size()-1);
//					tomActivityProperty.setStartTime(tomCourseClasses.getBeginTime());
//					tomActivityProperty.setEndTime(tomCourseClasses2.getEndTime());
//				}
				
				TomCourses course=coursesMapper.selectByPrimaryKey(Integer.valueOf(preTaskDto.getTaskId()));
//				if("N".equals(course.getCourseOnline())){
//					String filePath1=Config.getProperty("file_path");
//					SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
//					StringBuffer sb=new StringBuffer();
//					sb.append("qrcode:{ \"url\":\"");
//					sb.append(Config.getProperty("qrSignUrl"));
//					sb.append("views/task/course_detail.html?sign=true&courseId=");
//					sb.append(course.getCourseId());
//					sb.append("&activityId=");
//					sb.append(activityId);
//					sb.append("\",\"method\":\"get\"}");
//					QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+"签到二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
//					tomActivityProperty.setSignInTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"签到二维码-"+activityId+".jpg");
//					StringBuffer sb2=new StringBuffer();
//					sb2.append("qrcode:{ \"url\":\"");
//					sb2.append(Config.getProperty("qrGradeUrl"));
//					sb.append("views/task/course_detail.html?sign=true&courseId=");
//					sb2.append(course.getCourseId());
//					sb2.append("&activityId=");
//					sb2.append(activityId);
//					sb2.append("\",\"method\":\"get\"}");
//					QRCodeUtil.courseEncode(sb2.toString(),course.getCourseName()+"评分二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
//					tomActivityProperty.setGradeTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"评分二维码-"+activityId+".jpg");
//				}
				
				courseForMessage.add(tomActivityProperty);
			} else {
				Integer examPaperId = Integer.valueOf(preTaskDto.getTaskId());
				TomExamPaper tomExamPaper = tomExamPaperMapper.selectByPrimaryKey(examPaperId);
				TomExam tomExam = new TomExam();
				tomExam.setExamNumber(numberRecordService.getNumber(MapManager.numberType("KS")));
				tomExam.setExamName(tomExamPaper.getExamPaperName());
				tomExam.setExamNameEn(tomExamPaper.getExamPaperNameEn());
				tomExam.setBeginTime(format2.parse(preTaskDto.getStartTime()));
				tomExam.setEndTime(format2.parse(preTaskDto.getEndTime()));
				tomExam.setExamPaperId(examPaperId);
				tomExam.setExamTime(tomExamPaper.getExamTime());
				tomExam.setRetakingExamCount(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
				tomExam.setExamType("A");
				tomExam.setOfflineExam(preTaskDto.getOfflineExam());
				tomExam.setAdmins(jsonObj.getString("admins"));
				tomExam.setAdminNames(jsonObj.getString("adminNames"));
				tomExam.setCreateTime(date);
				tomExam.setUpdateTime(date);
				tomExam.setCreator(user.getName());
				tomExam.setLastOperator(user.getName());
				tomExam.setCreatorId(user.getId().toString());
				tomExam.setExamAddress(preTaskDto.getExamAddress());
				if (tomExam.getEndTime().getTime() / 1000
						- tomExam.getBeginTime().getTime() / 1000 < tomExam.getExamTime() * 60) {
					throw new EleException("examTimeOut");
				}
				List<TomActivity> list = activityMapper.selectByExamPaper(examPaperId);
				for (TomActivity tomActivity2 : list) {
					tomExam.setIsCN(tomActivity2.getIsCN());
					tomExam.setIsEN(tomActivity2.getIsEN());
				}
				tomExamMapper.insertSelective(tomExam);
				redisCacheManager.hset(CacheConstant.TOM_EXAM, tomExam.getExamId().toString(), jsonMappers.toJson(tomExam));
				TomProjectResource resourceExam = new TomProjectResource();
				resource.setCreateTime(new Date());
				resource.setCreator(tomExam.getCreator());
				resource.setOperator(tomExam.getLastOperator());
				resource.setParentProjectClassifyName(jsonObj.getString("parentProjectClassifyName"));
				resource.setProjectId(Integer.parseInt(jsonObj.getString("parentClassifyId")));
				resource.setResourceId(tomExam.getExamId());
				resource.setStatus("Y");
				resource.setType("E");
				resource.setUpdateTime(new Date());
				tomProjectResourceMapper.insertSelective(resource);
				examId = tomExam.getExamId();

				TomRetakingExam tomRetakingExam1 = new TomRetakingExam();
				tomRetakingExam1.setExamId(examId);
				tomRetakingExam1.setSort(0);
				tomRetakingExam1.setRetakingExamBeginTime(tomExam.getBeginTime());
				tomRetakingExam1.setRetakingExamEndTime(tomExam.getEndTime());
				tomRetakingExamMapper.insertSelective(tomRetakingExam1);
				redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, examId+"_"+tomRetakingExam1.getSort(), jsonMappers.toJson(tomRetakingExam1));
				for (int j = 0; j < preTaskDto.getRetakingExamBeginTimeList().size(); j++) {
					tomRetakingExam.setExamId(examId);
					tomRetakingExam.setSort(j + 1);
					/*tomRetakingExam
							.setRetakingExamBeginTime(format2.parse(preTaskDto.getRetakingExamBeginTimeList().get(j)));
					tomRetakingExam
							.setRetakingExamEndTime(format2.parse(preTaskDto.getRetakingExamEndTimeList().get(j)));*/
					tomRetakingExam.setRetakingExamBeginTime(format1.parse(jsonObj.getString("activityStartTime")));
					tomRetakingExam.setRetakingExamEndTime(format1.parse(jsonObj.getString("activityEndTime")));
					tomRetakingExamMapper.insertSelective(tomRetakingExam);
					redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, examId+"_"+tomRetakingExam.getSort(), jsonMappers.toJson(tomRetakingExam));
				}
				tomActivityProperty.setExamId(examId);
				tomActivityProperty.setExamName(tomExamPaper.getExamPaperName());
				tomActivityProperty.setExamTime(Double.valueOf(tomExamPaper.getExamTime().toString()));
				tomActivityProperty.setRetakingExamTimes(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
				tomActivityProperty.setOfflineExam(preTaskDto.getOfflineExam());
				examForMessage.add(tomActivityProperty);
			}
			activityPropertyMapper.insertSelective(tomActivityProperty);
		}
		JSONArray empIdsArray = jsonObj.getJSONArray("empIds");
		TomExamScore examScore;
		TomExamEmpAllocation examEmpAllocation;
		TomCourseEmpRelation courseEmpRelation;
		for (int i = 0; i < empIdsArray.size(); i++) {

			TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
			tomActivityEmpsRelation.setActivityId(activityId);
			tomActivityEmpsRelation.setCode(empIdsArray.get(i).toString());
			tomActivityEmpsRelation.setStatus("Y");
			if (needApply != null && "N".equals(needApply)) {
				tomActivityEmpsRelation.setApplyStatus("Y");
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setApplyType("E");

				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				activityPropertyExample.setActivityId(activityId);
				List<TomActivityProperty> activityProperties = activityPropertyMapper
						.selectByExample(activityPropertyExample);
				for (TomActivityProperty activityProperty : activityProperties) {
					if (activityProperty.getExamId() != null && !"".equals(activityProperty.getExamId())) {
						// 添加考试员工关联
						TomExamPaper examPaper = examPaperMapper.selectByExamId(activityProperty.getExamId());
						TomExam exam = examMapper.selectByPrimaryKey(activityProperty.getExamId());
						examScore = new TomExamScore();
						examScore.setExamId(activityProperty.getExamId());
						examScore.setCode(empIdsArray.get(i).toString());
						examScore.setEmpName(empMapper.selectByPrimaryKey(empIdsArray.get(i).toString()).getName());
						examScore.setGradeState("D");
						examScore.setExamCount(0);
						examScore.setTotalPoints(0);
						examScore.setExamTotalTime(0);
						examScore.setCreateTime(exam.getBeginTime());
						examScore.setRightNumbe(0);
						examScore.setWrongNumber(examPaper.getTestQuestionCount());
						examScore.setAccuracy(0d);
						examScoreMapper.insertSelective(examScore);
						redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE, examScore.getExamId()+"_"+empIdsArray.get(i).toString(), jsonMappers.toJson(examScore));
						examEmpAllocation = new TomExamEmpAllocation();
						examEmpAllocation.setCode(empIdsArray.get(i).toString());
						examEmpAllocation.setExamId(exam.getExamId());
						examEmpAllocation.setCreateTime(date);
						examEmpAllocationMapper.insertSelective(examEmpAllocation);
					} else if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
						courseEmpRelation = new TomCourseEmpRelation();
						courseEmpRelation.setCode(empIdsArray.get(i).toString());
						courseEmpRelation.setCourseId(activityProperty.getCourseId());
						if (courseEmpRelationMapper.selectByExample(courseEmpRelation).size() == 0) {
							courseEmpRelation.setCreateTime(date);
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreator(user.getName());
							courseEmpRelation.setUpdateTime(date);
							courseEmpRelation.setOperator(user.getName());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setCreatorId(user.getCode());
							courseEmpRelationMapper.insertSelective(courseEmpRelation);
						}

					}
				}
			} else {
				tomActivityEmpsRelation.setApplyStatus("N");
				tomActivityEmpsRelation.setIsRequired("N");
				tomActivityEmpsRelation.setApplyType("E");
			}
			tomActivityEmpsRelation.setCreateTime(date);
			tomActivityEmpsRelation.setUpdateTime(date);
			tomActivityEmpsRelation.setCreator(user.getName());
			tomActivityEmpsRelation.setOperator(user.getName());

			TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
			tomActivityEmpsRelationKey.setActivityId(activityId);
			tomActivityEmpsRelationKey.setCode(empIdsArray.get(i).toString());
			TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
					.selectByPrimaryKey(tomActivityEmpsRelationKey);
			if (activityEmpsRelation == null) {
				activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
			}
		}

		JSONArray deptManagerIdsArray = jsonObj.getJSONArray("deptManagerIds");
		JSONArray codesArray = jsonObj.getJSONArray("codes");
		if (needApply != null && "N".equals(needApply)) {
			for (int i = 0; i < deptManagerIdsArray.size(); i++) {

				TomActivityDept tomActivityDept = new TomActivityDept();
				tomActivityDept.setActivityId(activityId);
				tomActivityDept.setCreateTime(date);
				tomActivityDept.setUpdateTime(date);
				tomActivityDept.setCreator(user.getName());
				tomActivityDept.setOperator(user.getName());
				tomActivityDept.setCode(codesArray.get(i).toString().substring(1));
				tomActivityDept.setHeaderCode(deptManagerIdsArray.get(i).toString());
				tomActivityDept.setStatus("Y");
				tomActivityDept.setPushStatus("N");
				activityDeptMapper.insertSelective(tomActivityDept);

				TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setCode(deptManagerIdsArray.get(i).toString());
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setApplyStatus("N");
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setApplyType("P");
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setUpdateTime(date);
				tomActivityEmpsRelation.setCreator(user.getName());
				tomActivityEmpsRelation.setOperator(user.getName());

				TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
				tomActivityEmpsRelationKey.setActivityId(activityId);
				tomActivityEmpsRelationKey.setCode(deptManagerIdsArray.get(i).toString());
				TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
						.selectByPrimaryKey(tomActivityEmpsRelationKey);
				if (activityEmpsRelation == null) {
					activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
				}

			}
		}
		return "Y";
	}

	/**
	 * 功能描述：[更新培训活动]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午9:51:37
	 * 
	 * @param activity
	 */
	@Transactional
	public String updateActivity(String jsonStr) throws Exception {
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObj = jsonObject.fromObject(jsonStr);
		TomActivity tomActivity = new TomActivity();
		String protocol = (String) jsonObj.get("protocol");
		tomActivity.setActivityId(Integer.parseInt(jsonObj.getString("activityId")));

		TomActivity activity = activityMapper.selectByPrimaryKey(tomActivity.getActivityId());

		tomActivity.setProtocol(protocol);
		Date date = new Date();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ShiroUser user = ShiroUtils.getCurrentUser();
		if (protocol != null && "Y".equals(protocol)) {
			tomActivity.setProtocolStartTime(format1.parse(jsonObj.getString("protocolStartTime")));
			tomActivity.setProtocolEndTime(format1.parse(jsonObj.getString("protocolEndTime")));
			tomActivity.setTrainFee(jsonObj.getDouble("trainFee"));
		}
		String needApply = jsonObj.getString("needApply");
		tomActivity.setNeedApply(needApply);
		if (needApply != null && "Y".equals(needApply)) {
			tomActivity.setNumberOfParticipants(jsonObj.getInt("numberOfParticipants"));
			tomActivity.setApplicationStartTime(format1.parse(jsonObj.getString("applicationStartTime")));
			tomActivity.setApplicationDeadline(format1.parse(jsonObj.getString("applicationDeadline")));
		}
		tomActivity.setActivityName(jsonObj.getString("activityName"));
		tomActivity.setActivityNameEn(jsonObj.getString("activityNameEn"));
		tomActivity.setActivityStartTime(format1.parse(jsonObj.getString("activityStartTime")));
		tomActivity.setActivityEndTime(format1.parse(jsonObj.getString("activityEndTime")));
		tomActivity.setIntroduction(jsonObj.getString("introduction"));
		tomActivity.setIntroductionEn(jsonObj.getString("introductionEn"));
		tomActivity.setActPicture(jsonObj.getString("activityImg"));
		tomActivity.setActPictureEn(jsonObj.getString("activityImgEn"));
		// if(jsonStr.indexOf("\"deptCodes\":")!=-1){
		tomActivity.setDepts(jsonObj.getString("deptCodes"));
		// }
		// if(jsonStr.indexOf("\"employeeGradeStr\":")!=-1){
		tomActivity.setStaffLevels(jsonObj.getString("employeeGradeStr"));
		// }
		// if(jsonStr.indexOf("\"city\":")!=-1){
		tomActivity.setCity(jsonObj.getString("city"));
		tomActivity.setCityEn(jsonObj.getString("cityEn"));
		tomActivity.setIsCN(jsonObj.getString("isCN"));
		tomActivity.setIsEN(jsonObj.getString("isEN"));
		// }
		tomActivity.setAdmins(jsonObj.getString("admins"));
		tomActivity.setAdminNames(jsonObj.getString("adminNames"));
		tomActivity.setUpdateTime(date);
		tomActivity.setOperator(user.getName());
		tomActivity.setParentClassifyId(jsonObj.getString("parentClassifyId"));
		tomActivity.setParentProjectClassifyName(jsonObj.getString("parentProjectClassifyName"));
		activityMapper.updateByPrimaryKeySelective(tomActivity);
		//项目分类
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(tomActivity.getCreator());
		resource.setOperator(tomActivity.getOperator());
		resource.setProjectId(Integer.parseInt(jsonObj.getString("parentClassifyId")));
		resource.setResourceId(tomActivity.getActivityId());
		resource.setStatus("Y");
		resource.setType("A");
		resource.setParentProjectClassifyName(jsonObj.getString("parentProjectClassifyName"));
		resource.setUpdateTime(new Date());
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(resource);
		if(null==selectByResource){
			tomProjectResourceMapper.insertSelective(resource);
		}else
		if(!selectByResource.getProjectId().equals(resource.getProjectId())){//判断与原有项目分类是否是一个
			selectByResource.setStatus("N");
			tomProjectResourceMapper.updateByPrimaryKeySelective(selectByResource);
			tomProjectResourceMapper.insertSelective(resource);
		}
		Integer activityId = tomActivity.getActivityId();

		// 删除之前的关联数据
		TomActivityProperty example = new TomActivityProperty();
		example.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example);
		activityPropertyMapper.deleteByActivityId(activityId);
		List<TomActivityProperty> activityPropertyList = new ArrayList<TomActivityProperty>();
		for (TomActivityProperty activityProperty1 : tomActivityProperties) {
			if (activityProperty1.getExamId() != null) {
				activityPropertyList.add(activityProperty1);
			}
		}

		ObjectMapper jsonMapper = new ObjectMapper();
		JSONArray preTaskInfosExam = jsonObj.getJSONArray("preTaskInfo");
		JSONArray empIdsArrayExam = jsonObj.getJSONArray("empIds");

		for (int k = 0; k < preTaskInfosExam.size(); k++) {
			int count = 0;
			TomActivityPropertyDto preTaskDto = jsonMapper.readValue(preTaskInfosExam.get(k).toString(),
					TomActivityPropertyDto.class);
			if (preTaskDto.getRetakingExamTimes() != null) {
				Integer examPaperId = Integer.valueOf(preTaskDto.getTaskId());
				for (int i = 0; i < activityPropertyList.size(); i++) {
					TomExam tomExam = tomExamMapper.selectByPrimaryKey(activityPropertyList.get(i).getExamId());
					if (examPaperId.equals(tomExam.getExamPaperId())) {
						TomExamPaper tomExamPaper = tomExamPaperMapper.selectByPrimaryKey(examPaperId);
						TomActivityProperty tomActivityProperty = new TomActivityProperty();
						tomActivityProperty.setActivityId(activityId);
						tomActivityProperty.setSort(preTaskDto.getSort());
						tomActivityProperty.setPackageId(Integer.valueOf(preTaskDto.getPackageId()));
						tomActivityProperty.setStartTime(format2.parse(preTaskDto.getStartTime()));
						tomActivityProperty.setEndTime(format2.parse(preTaskDto.getEndTime()));
						String pretaskId = preTaskDto.getPretaskId();
						if(!"".equals(pretaskId)){
						if(pretaskId.charAt(0)==','){
							pretaskId =	pretaskId.substring(1, pretaskId.length());
						}
						if(pretaskId.charAt(pretaskId.length()-1)==','){
							pretaskId =	pretaskId.substring(0, pretaskId.length()-1);
						}
						}
						tomActivityProperty.setPreTask(pretaskId);
						tomActivityProperty.setCreateTime(date);
						tomActivityProperty.setUpdateTime(date);
						tomActivityProperty.setCreator(user.getName());
						tomActivityProperty.setOperator(user.getName());

						count++;
						tomExam.setUpdateTime(new Date());
						tomExam.setBeginTime(format2.parse(preTaskDto.getStartTime()));
						tomExam.setEndTime(format2.parse(preTaskDto.getEndTime()));
						tomExam.setOfflineExam(preTaskDto.getOfflineExam());
						tomExam.setAdmins(jsonObj.getString("admins"));
						tomExam.setAdminNames(jsonObj.getString("adminNames"));
						tomExam.setRetakingExamCount(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
						tomExam.setExamAddress(preTaskDto.getExamAddress());
						examMapper.updateByPrimaryKeySelective(tomExam);
						resource.setResourceId(tomExam.getExamId());
						resource.setType("E");
						tomProjectResourceMapper.updateByPrimaryKeySelective(resource);
						// 删除旧补考
						retakingExamMapper.deleteByExamId(tomExam.getExamId());
						// 插入新补考
						TomRetakingExam retakingExam;
						if (Integer.parseInt(preTaskDto.getRetakingExamTimes()) > 0) {
							TomRetakingExam exam = new TomRetakingExam();
							exam.setExamId(tomExam.getExamId());
							exam.setRetakingExamBeginTime(tomExam.getBeginTime());
							exam.setRetakingExamEndTime(tomExam.getEndTime());
							exam.setSort(0);
							retakingExamMapper.insertSelective(exam);
							for (int j = 0; j < preTaskDto.getRetakingExamEndTimeList().size(); j++) {
								retakingExam = new TomRetakingExam();
								retakingExam.setExamId(tomExam.getExamId());
								retakingExam.setRetakingExamBeginTime(
										format1.parse(jsonObj.getString("activityStartTime")));
								retakingExam.setRetakingExamEndTime(
										format1.parse(jsonObj.getString("activityEndTime")));
								retakingExam.setSort(j + 1);
								retakingExamMapper.insertSelective(retakingExam);
							}
						} else {
							retakingExam = new TomRetakingExam();
							retakingExam.setExamId(tomExam.getExamId());
							retakingExam.setRetakingExamBeginTime(tomExam.getBeginTime());
							retakingExam.setRetakingExamEndTime(tomExam.getEndTime());
							retakingExam.setSort(0);
							retakingExamMapper.insertSelective(retakingExam);
						}
						tomActivityProperty.setExamId(tomExam.getExamId());
						tomActivityProperty.setExamName(tomExam.getExamName());
						tomActivityProperty.setExamTime(Double.valueOf(tomExamPaper.getExamTime().toString()));
						tomActivityProperty.setRetakingExamTimes(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
						tomActivityProperty.setOfflineExam(preTaskDto.getOfflineExam());
						activityPropertyMapper.insertSelective(tomActivityProperty);
						// 删除旧员工关联
						TomExamEmpAllocation exampleExam = new TomExamEmpAllocation();
						exampleExam.setExamId(tomExam.getExamId());
						examEmpAllocationMapper.deleteByExample(exampleExam);
						// 删除旧默认成绩
						TomExamScore examScoreExample = new TomExamScore();
						examScoreExample.setExamId(tomExam.getExamId());
						examScoreExample.setGradeState("D");
						TomExamScore examScoreExample1 = new TomExamScore();
						examScoreExample1.setExamId(tomExam.getExamId());
						examScoreExample1.setGradeState("P");
						List<TomExamScore> Example1 = examScoreMapper.selectListByExample(examScoreExample);
						List<TomExamScore> Example2 = examScoreMapper.selectListByExample(examScoreExample1);
						for (TomExamScore tomExamScore : Example1) {
							int countExam = 0;
							for (TomExamScore tomExamScore2 : Example2) {
								if (tomExamScore.getCode().equals(tomExamScore2.getCode())) {
									countExam++;
								}
							}
							if (countExam == 0) {
								examScoreMapper.deleteByExample(tomExamScore);
							}
						}
						// 添加新考试员工关联
						TomExamScore examScore;
						TomExamEmpAllocation examEmpAllocation;
						TomExamScore examScoreExample2 = new TomExamScore();
						examScoreExample2.setExamId(tomExam.getExamId());
						List<TomExamScore> selectListByExample = examScoreMapper.selectListByExample(examScoreExample2);// 查看已考完人员
						for (int l = 0; l < empIdsArrayExam.size(); l++) {
							int iExam = 0;
							for (TomExamScore score : selectListByExample) {
								if (empIdsArrayExam.get(l).toString().equals(score.getCode())) {
									iExam++;
								}
							}
							if (iExam == 0) {
								examScore = new TomExamScore();
								examScore.setExamId(tomExam.getExamId());
								examScore.setCode(empIdsArrayExam.get(l).toString());
								examScore.setEmpName(
										empMapper.selectByPrimaryKey(empIdsArrayExam.get(l).toString()).getName());
								examScore.setGradeState("D");
								examScore.setExamCount(0);
								examScore.setTotalPoints(0);
								examScore.setExamTotalTime(0);
								examScore.setCreateTime(tomExam.getBeginTime());
								examScore.setRightNumbe(0);
								examScore.setWrongNumber(0);
								examScore.setAccuracy(0d);
								examScoreMapper.insertSelective(examScore);
							}
							examEmpAllocation = new TomExamEmpAllocation();
							examEmpAllocation.setCode(empIdsArrayExam.get(l).toString());
							examEmpAllocation.setExamId(tomExam.getExamId());
							examEmpAllocation.setCreateTime(new Date());
							examEmpAllocationMapper.insertSelective(examEmpAllocation);
						}
						activityPropertyList.remove(i);
					}
				}
				if (count == 0) {
					TomActivityProperty tomActivityProperty = new TomActivityProperty();
					tomActivityProperty.setActivityId(activityId);
					tomActivityProperty.setSort(preTaskDto.getSort());
					tomActivityProperty.setPackageId(Integer.valueOf(preTaskDto.getPackageId()));
					tomActivityProperty.setStartTime(format2.parse(preTaskDto.getStartTime()));
					tomActivityProperty.setEndTime(format2.parse(preTaskDto.getEndTime()));
					String pretaskId = preTaskDto.getPretaskId();
					if(!"".equals(pretaskId)){
					if(pretaskId.charAt(0)==','){
						pretaskId =	pretaskId.substring(1, pretaskId.length());
					}
					if(pretaskId.charAt(pretaskId.length()-1)==','){
						pretaskId =	pretaskId.substring(0, pretaskId.length()-1);
					}
					}
					tomActivityProperty.setPreTask(preTaskDto.getPretaskId());
					tomActivityProperty.setCreateTime(date);
					tomActivityProperty.setUpdateTime(date);
					tomActivityProperty.setCreator(user.getName());
					tomActivityProperty.setOperator(user.getName());
					Integer examPaperId2 = Integer.valueOf(preTaskDto.getTaskId());
					TomExamPaper tomExamPaper = tomExamPaperMapper.selectByPrimaryKey(examPaperId2);
					TomExam tomExam = new TomExam();
					tomExam.setExamNumber(numberRecordService.getNumber(MapManager.numberType("KS")));
					tomExam.setExamName(tomExamPaper.getExamPaperName());
					tomExam.setExamNameEn(tomExamPaper.getExamPaperNameEn());
					tomExam.setBeginTime(format2.parse(preTaskDto.getStartTime()));
					tomExam.setEndTime(format2.parse(preTaskDto.getEndTime()));
					tomExam.setExamPaperId(examPaperId2);
					tomExam.setExamTime(tomExamPaper.getExamTime());
					tomExam.setRetakingExamCount(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
					tomExam.setExamType("A");
					tomExam.setOfflineExam(preTaskDto.getOfflineExam());
					tomExam.setAdmins(tomExamPaper.getAdmins());
					tomExam.setCreateTime(date);
					tomExam.setUpdateTime(date);
					tomExam.setCreator(user.getName());
					tomExam.setLastOperator(user.getName());
					tomExam.setCreatorId(user.getId().toString());
					tomExam.setExamAddress(preTaskDto.getExamAddress());
					tomExam.setIsCN(activity.getIsCN());
					tomExam.setIsEN(activity.getIsEN());
					tomExamMapper.insertSelective(tomExam);
					redisCacheManager.hset(CacheConstant.TOM_EXAM, tomExam.getExamId().toString(), jsonMappers.toJson(tomExam));
					//项目分类
					resource.setResourceId(tomExam.getExamId());
					resource.setType("E");
					tomProjectResourceMapper.insertSelective(resource);
					Integer examId = tomExam.getExamId();

					TomRetakingExam tomRetakingExam1 = new TomRetakingExam();
					tomRetakingExam1.setExamId(examId);
					tomRetakingExam1.setSort(0);
					tomRetakingExam1.setRetakingExamBeginTime(tomExam.getBeginTime());
					tomRetakingExam1.setRetakingExamEndTime(tomExam.getEndTime());
					tomRetakingExamMapper.insertSelective(tomRetakingExam1);
					redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, examId+"_"+tomRetakingExam1.getSort(), jsonMappers.toJson(tomRetakingExam1));
					for (int j = 0; j < preTaskDto.getRetakingExamBeginTimeList().size(); j++) {
						TomRetakingExam tomRetakingExam = new TomRetakingExam();
						tomRetakingExam.setExamId(examId);
						tomRetakingExam.setSort(j + 1);
						tomRetakingExam.setRetakingExamBeginTime(
								format2.parse(preTaskDto.getRetakingExamBeginTimeList().get(j)));
						tomRetakingExam
								.setRetakingExamEndTime(format2.parse(preTaskDto.getRetakingExamEndTimeList().get(j)));
						tomRetakingExamMapper.insertSelective(tomRetakingExam);
						redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, examId+"_"+tomRetakingExam.getSort(), jsonMappers.toJson(tomRetakingExam));
					}
					tomActivityProperty.setExamId(examId);
					tomActivityProperty.setExamName(tomExamPaper.getExamPaperName());
					tomActivityProperty.setExamTime(Double.valueOf(tomExamPaper.getExamTime().toString()));
					tomActivityProperty.setRetakingExamTimes(Integer.valueOf(preTaskDto.getRetakingExamTimes()));
					tomActivityProperty.setOfflineExam(preTaskDto.getOfflineExam());
					activityPropertyMapper.insertSelective(tomActivityProperty);

					TomExamScore examScore;
					TomExamEmpAllocation examEmpAllocation;
					TomExamPaper examPaper = examPaperMapper.selectByExamId(tomExam.getExamId());
					for (int l = 0; l < empIdsArrayExam.size(); l++) {
						examScore = new TomExamScore();
						examScore.setExamId(tomExam.getExamId());
						examScore.setCode(empIdsArrayExam.get(l).toString());
						examScore.setEmpName(empMapper.selectByPrimaryKey(empIdsArrayExam.get(l).toString()).getName());
						examScore.setGradeState("D");
						examScore.setExamCount(0);
						examScore.setTotalPoints(0);
						examScore.setExamTotalTime(0);
						examScore.setCreateTime(tomExam.getBeginTime());
						examScore.setRightNumbe(0);
						examScore.setWrongNumber(examPaper.getTestQuestionCount());
						examScore.setAccuracy(0d);
						examScoreMapper.insertSelective(examScore);
						redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE, examScore.getExamId()+"_"+examScore.getCode(), jsonMappers.toJson(examScore));
						examEmpAllocation = new TomExamEmpAllocation();
						examEmpAllocation.setCode(empIdsArrayExam.get(l).toString());
						examEmpAllocation.setExamId(tomExam.getExamId());
						examEmpAllocation.setCreateTime(new Date());
						examEmpAllocationMapper.insertSelective(examEmpAllocation);
					}
				}
			}
		}
		if (activityPropertyList.size() != 0) {
			for (TomActivityProperty tomActivityProperty : activityPropertyList) {
				examService.deleteexam(tomActivityProperty.getExamId());
				resource.setResourceId(tomActivityProperty.getExamId());
				resource.setType("E");
			}
		}

		for (TomActivityProperty activityProperty : tomActivityProperties) {
			if (activityProperty.getExamId() != null) {

			} else if (activityProperty.getCourseId() != null) {
				TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
				courseEmpRelationExample.setCourseId(activityProperty.getCourseId());
				courseEmpRelationExample.setCreateTime(activityProperty.getCreateTime());
				courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
			}
		}
		// activityPropertyMapper.deleteByActivityId(activityId);
		// activityEmpsRelationMapper.deleteByActivityId(activityId);
		// activityDeptMapper.deleteByActivityId(activityId);
		// TomActivityFees feesExample=new TomActivityFees();
		// feesExample.setActivityId(activityId);
		// activityFeesMapper.deleteByExample(feesExample);

		// ObjectMapper jsonMapper = new ObjectMapper();
		JSONArray preTaskInfos = jsonObj.getJSONArray("preTaskInfo");
		for (int i = 0; i < preTaskInfos.size(); i++) {
			TomActivityPropertyDto preTaskDto = jsonMapper.readValue(preTaskInfos.get(i).toString(),
					TomActivityPropertyDto.class);
			TomActivityProperty tomActivityProperty = new TomActivityProperty();
			tomActivityProperty.setActivityId(activityId);
			tomActivityProperty.setSort(preTaskDto.getSort());
			tomActivityProperty.setPackageId(Integer.valueOf(preTaskDto.getPackageId()));
			if(preTaskDto.getStartTime()!=null){
				tomActivityProperty.setStartTime(format2.parse(preTaskDto.getStartTime()));
			}
			if(preTaskDto.getEndTime()!=null){
				tomActivityProperty.setEndTime(format2.parse(preTaskDto.getEndTime()));
			}
			
			String pretaskId = preTaskDto.getPretaskId();
			if(!"".equals(pretaskId)){
			if(pretaskId.charAt(0)==','){
				pretaskId =	pretaskId.substring(1, pretaskId.length());
			}
			if(pretaskId.charAt(pretaskId.length()-1)==','){
				pretaskId =	pretaskId.substring(0, pretaskId.length()-1);
			}
			}
			tomActivityProperty.setPreTask(preTaskDto.getPretaskId());
			tomActivityProperty.setCreateTime(date);
			tomActivityProperty.setUpdateTime(date);
			tomActivityProperty.setCreator(user.getName());
			tomActivityProperty.setOperator(user.getName());
			if (preTaskDto.getRetakingExamTimes() == null) {
				if (preTaskDto.getLecturerId() != null && !"".equals(preTaskDto.getLecturerId())) {
					tomActivityProperty.setLecturers(preTaskDto.getLecturerId());
					tomActivityProperty.setCourseAddress(preTaskDto.getCourseAddress());
					tomActivityProperty.setCourseTime(Double.valueOf(preTaskDto.getCourseTime()));

					if (preTaskDto.getUnitPrice() != null && !"".equals(preTaskDto.getUnitPrice())) {
						tomActivityProperty.setUnitPrice(Double.valueOf(preTaskDto.getUnitPrice()));
					}

					tomActivityProperty.setTotalPrice(Double.valueOf(preTaskDto.getTotalPrice()));
				}
				tomActivityProperty.setCourseId(Integer.valueOf(preTaskDto.getTaskId()));
//				TomCourses selectByPrimaryKey = coursesMapper.selectByPrimaryKey(Integer.valueOf(preTaskDto.getTaskId()));
//				if("N".equals(selectByPrimaryKey.getCourseOnline())){
//					Map<Object,Object> hashmap = new HashMap<Object,Object>();
//					hashmap.put("courseId", Integer.valueOf(preTaskDto.getTaskId()));
//					List<TomCourseClasses> CourseClassesList = tomCourseClassesMapper.selectByCourseIdSort(hashmap);
//					TomCourseClasses tomCourseClasses = CourseClassesList.get(0);
//					TomCourseClasses tomCourseClasses2 = CourseClassesList.get(CourseClassesList.size()-1);
//					tomActivityProperty.setStartTime(tomCourseClasses.getBeginTime());
//					tomActivityProperty.setEndTime(tomCourseClasses2.getEndTime());
//				}
				Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("activityId", activityId);
				map.put("courseId", Integer.valueOf(preTaskDto.getTaskId()));
				TomActivityProperty selectByCourseIdAndActivityId = activityPropertyMapper
						.selectByCourseIdAndActivityId(map);
				TomCourses course=coursesMapper.selectByPrimaryKey(Integer.valueOf(preTaskDto.getTaskId()));
				if("N".equals(course.getCourseOnline())){
					String filePath1=Config.getProperty("file_path");
					SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
					StringBuffer sb=new StringBuffer();
					sb.append("qrcode:{ \"url\":\"");
					sb.append(Config.getProperty("qrSignUrl"));
					sb.append("views/task/course_detail.html?sign=true&courseId=");
					sb.append(course.getCourseId());
					sb.append("&activityId=");
					sb.append(activityId);
					sb.append("\",\"method\":\"get\"}");
					QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+"签到二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
					tomActivityProperty.setSignInTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"签到二维码-"+activityId+".jpg");
					StringBuffer sb2=new StringBuffer();
					sb2.append("qrcode:{ \"url\":\"");
					sb2.append(Config.getProperty("qrGradeUrl"));
					sb.append("views/task/course_detail.html?sign=true&courseId=");
					sb2.append(course.getCourseId());
					sb2.append("&activityId=");
					sb2.append(activityId);
					sb2.append("\",\"method\":\"get\"}");
					QRCodeUtil.courseEncode(sb2.toString(),course.getCourseName()+"评分二维码-"+activityId, filePath1 +"file"+ File.separator + "tdc"+ File.separator+simple.format(new Date()));
					tomActivityProperty.setGradeTwoDimensionCode("file" + "/tdc"+"/"+simple.format(new Date())+"/"+course.getCourseName()+"评分二维码-"+activityId+".jpg");
				}
				if (null == selectByCourseIdAndActivityId) {
					activityPropertyMapper.insertSelective(tomActivityProperty);
				}
			}

		}
		JSONArray empIdsArray = jsonObj.getJSONArray("empIds");
		TomCourseEmpRelation courseEmpRelation;
		for (int i = 0; i < empIdsArray.size(); i++) {
			if (needApply != null && "N".equals(needApply)) {
				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				activityPropertyExample.setActivityId(activityId);
				List<TomActivityProperty> activityProperties = activityPropertyMapper
						.selectByExample(activityPropertyExample);
				for (TomActivityProperty activityProperty : activityProperties) {
					if (activityProperty.getCourseId() != null && !"".equals(activityProperty.getCourseId())) {
						courseEmpRelation = new TomCourseEmpRelation();
						courseEmpRelation.setCode(empIdsArray.get(i).toString());
						courseEmpRelation.setCourseId(activityProperty.getCourseId());
						if (courseEmpRelationMapper.selectByExample(courseEmpRelation).size() == 0) {
							courseEmpRelation.setCreateTime(date);
							courseEmpRelation.setStatus("Y");
							courseEmpRelation.setCreator(user.getName());
							courseEmpRelation.setUpdateTime(date);
							courseEmpRelation.setOperator(user.getName());
							courseEmpRelation.setFinishStatus("N");
							courseEmpRelation.setCreatorId(user.getCode());
							courseEmpRelationMapper.insertSelective(courseEmpRelation);
						}
					}
				}
			}
		}
		List<String> codes = new ArrayList<String>();
		List<TomActivityEmpsRelation> selectByActivityId = activityEmpsRelationMapper.selectByActivityId2(activityId);
		for (int j = 0; j < selectByActivityId.size(); j++) {
			boolean b = false;
			int count1 = 0;
			for (int i = 0; i < empIdsArray.size(); i++) {
				if (selectByActivityId.get(j).getCode().equals(empIdsArray.get(i))) {
					selectByActivityId.remove(j);
					break;
				}
			}
		}
		if (selectByActivityId.size() != 0) {
			for (TomActivityEmpsRelation tomActivityEmpsRelation : selectByActivityId) {
				activityEmpsRelationMapper.deleteByPrimaryKey(tomActivityEmpsRelation);
			}
		}
		for (int i = 0; i < empIdsArray.size(); i++) {
			TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
			tomActivityEmpsRelation.setActivityId(activityId);
			tomActivityEmpsRelation.setCode(empIdsArray.get(i).toString());
			tomActivityEmpsRelation.setStatus("Y");
			if (needApply != null && "N".equals(needApply)) {
				tomActivityEmpsRelation.setApplyStatus("Y");
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setApplyType("E");

				TomActivityProperty activityPropertyExample = new TomActivityProperty();
				activityPropertyExample.setActivityId(activityId);
				List<TomActivityProperty> activityProperties = activityPropertyMapper
						.selectByExample(activityPropertyExample);
			} else {
				tomActivityEmpsRelation.setApplyStatus("N");
				tomActivityEmpsRelation.setIsRequired("N");
				tomActivityEmpsRelation.setApplyType("E");
			}
			tomActivityEmpsRelation.setCreateTime(date);
			tomActivityEmpsRelation.setUpdateTime(date);
			tomActivityEmpsRelation.setCreator(user.getName());
			tomActivityEmpsRelation.setOperator(user.getName());

			TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
			tomActivityEmpsRelationKey.setActivityId(activityId);
			tomActivityEmpsRelationKey.setCode(empIdsArray.get(i).toString());
			TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
					.selectByPrimaryKey(tomActivityEmpsRelationKey);
			if (activityEmpsRelation == null) {
				activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
			} else {
//				tomActivityEmpsRelation.setApplyStatus(activityEmpsRelation.getApplyStatus());
				activityEmpsRelationMapper.updateByPrimaryKeySelective(tomActivityEmpsRelation);
			}
		}

		JSONArray deptManagerIdsArray = jsonObj.getJSONArray("deptManagerIds");
		if (deptManagerIdsArray.size() == 0) {
			activityDeptMapper.deleteByActivityId(activityId);
		} else {
			List<TomActivityDept> selectByActivityId2 = activityDeptMapper.selectByActivityId(activityId);
			for (int i = 0; i < deptManagerIdsArray.size(); i++) {
				for (int j = 0; j < selectByActivityId2.size(); j++) {
					if (deptManagerIdsArray.get(i).toString().equals(selectByActivityId2.get(i).getHeaderCode())) {
						selectByActivityId2.remove(j);
						break;
					}
				}
			}
			if (selectByActivityId2.size() != 0) {
				for (TomActivityDept tomActivityDept : selectByActivityId2) {
					activityDeptMapper.deleteByExample(tomActivityDept);
				}
			}
		}
		JSONArray codesArray = jsonObj.getJSONArray("codes");
		if (needApply != null && "N".equals(needApply)) {
			for (int i = 0; i < deptManagerIdsArray.size(); i++) {

				TomActivityDept tomActivityDept = new TomActivityDept();
				tomActivityDept.setActivityId(activityId);
				tomActivityDept.setCreateTime(date);
				tomActivityDept.setUpdateTime(date);
				tomActivityDept.setCreator(user.getName());
				tomActivityDept.setOperator(user.getName());
				tomActivityDept.setHeaderCode(deptManagerIdsArray.get(i).toString());
				tomActivityDept.setCode(codesArray.get(i).toString().substring(1));
				tomActivityDept.setStatus("Y");
				tomActivityDept.setPushStatus("N");
				TomActivityDept selectByExample = activityDeptMapper.selectByExample(tomActivityDept);
				if (null == selectByExample) {
					activityDeptMapper.insertSelective(tomActivityDept);
				} else {
					tomActivityDept.setPushStatus(selectByExample.getPushStatus());
					activityDeptMapper.updateByPrimaryKeySelective(tomActivityDept);
				}

				TomActivityEmpsRelation tomActivityEmpsRelation = new TomActivityEmpsRelation();
				tomActivityEmpsRelation.setActivityId(activityId);
				tomActivityEmpsRelation.setCode(deptManagerIdsArray.get(i).toString());
				tomActivityEmpsRelation.setStatus("Y");
				tomActivityEmpsRelation.setApplyStatus("N");
				tomActivityEmpsRelation.setIsRequired("Y");
				tomActivityEmpsRelation.setApplyType("P");
				tomActivityEmpsRelation.setCreateTime(date);
				tomActivityEmpsRelation.setUpdateTime(date);
				tomActivityEmpsRelation.setCreator(user.getName());
				tomActivityEmpsRelation.setOperator(user.getName());

				TomActivityEmpsRelationKey tomActivityEmpsRelationKey = new TomActivityEmpsRelationKey();
				tomActivityEmpsRelationKey.setActivityId(activityId);
				tomActivityEmpsRelationKey.setCode(deptManagerIdsArray.get(i).toString());
				TomActivityEmpsRelation activityEmpsRelation = activityEmpsRelationMapper
						.selectByPrimaryKey(tomActivityEmpsRelationKey);
				if (activityEmpsRelation == null) {
					activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
				} else {
					tomActivityEmpsRelation.setApplyStatus(activityEmpsRelation.getApplyStatus());
					activityEmpsRelationMapper.updateByPrimaryKeySelective(tomActivityEmpsRelation);
				}
			}
		}
		return "Y";
	}

	/**
	 * 功能描述：[删除培训活动]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午9:51:54
	 * 
	 * @param activity
	 */
	@Transactional
	public String deleteActivity(int activityId) {
		TomActivity activity = activityMapper.selectByPrimaryKey(activityId);
		if ("N".equals(activity.getNeedApply()) && activity.getActivityStartTime().before(new Date())) {
			return "protected";
		} else if ("Y".equals(activity.getNeedApply()) && activity.getApplicationStartTime().before(new Date())) {
			return "protected";
		}
		TomProjectResource record = new TomProjectResource();
		TomActivityProperty example = new TomActivityProperty();
		example.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example);
		for (TomActivityProperty activityProperty : tomActivityProperties) {
			if (activityProperty.getExamId() != null) {
				examService.deleteexam(activityProperty.getExamId());
				record.setType("E");
				record.setResourceId(activityProperty.getExamId());
				tomProjectResourceMapper.deleteByPrimaryKey(record);
			} else if (activityProperty.getCourseId() != null) {
				TomCourseEmpRelation courseEmpRelationExample = new TomCourseEmpRelation();
				courseEmpRelationExample.setCourseId(activityProperty.getCourseId());
				courseEmpRelationExample.setCreateTime(activityProperty.getCreateTime());
				courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
			}
		}
		activityMapper.deleteByPrimaryKey(activityId);
		activityPropertyMapper.deleteByActivityId(activityId);
		activityEmpsRelationMapper.deleteByActivityId(activityId);
		activityDeptMapper.deleteByActivityId(activityId);
		TomActivityFees feesExample = new TomActivityFees();
		feesExample.setActivityId(activityId);
		activityFeesMapper.deleteByExample(feesExample);
		record.setType("A");
		record.setResourceId(activityId);
		tomProjectResourceMapper.deleteByPrimaryKey(record);

		return "Y";
	}

	public TomActivityDto copyActDetails(int activityId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomActivity tomActivity = activityMapper.selectByPrimaryKey(activityId);
		TomActivityDto tomActivityDto = new TomActivityDto();
		if (null == tomActivity.getCity()) {
			tomActivityDto.setCity("");
		} else {
			tomActivityDto.setCity(tomActivity.getCity());
		}
		if (null == tomActivity.getCityEn()) {
			tomActivityDto.setCityEn("");
		} else {
			tomActivityDto.setCityEn(tomActivity.getCityEn());
		}
		
		if (null == tomActivity.getDepts()) {
			tomActivityDto.setDeptCodes("");
		} else {
			tomActivityDto.setDeptCodes(tomActivity.getDepts());
		}
		if (null == tomActivity.getStaffLevels()) {
			tomActivityDto.setEmployeeGradeStr("");
		} else {
			tomActivityDto.setEmployeeGradeStr(tomActivity.getStaffLevels());
		}
		tomActivityDto.setIntroduction(tomActivity.getIntroduction());
		tomActivityDto.setActivityName(tomActivity.getActivityName());
		tomActivityDto.setIsCN(tomActivity.getIsCN());
		tomActivityDto.setIsEN(tomActivity.getIsEN());
		tomActivityDto.setIntroductionEn(tomActivity.getIntroductionEn());
		tomActivityDto.setActivityNameEn(tomActivity.getActivityNameEn());
		tomActivityDto.setProtocol(tomActivity.getProtocol());
		if (tomActivity.getProtocol() != null && "Y".equals(tomActivity.getProtocol())) {
			tomActivityDto.setProtocolStartTime(sdf.format(tomActivity.getProtocolStartTime()));
			tomActivityDto.setProtocolEndTime(sdf.format(tomActivity.getProtocolEndTime()));
			tomActivityDto.setTrainFee(tomActivity.getTrainFee().toString());
		}
		if (tomActivity.getNeedApply() != null && "Y".equals(tomActivity.getNeedApply())) {
			tomActivityDto.setNumberOfParticipants(tomActivity.getNumberOfParticipants().toString());
			tomActivityDto.setApplicationStartTime(sdf.format(tomActivity.getApplicationStartTime()));
			tomActivityDto.setApplicationDeadline(sdf.format(tomActivity.getApplicationDeadline()));
		}

		tomActivityDto.setAdmins(tomActivity.getAdmins());
		tomActivityDto.setActivityImg(tomActivity.getActPicture());
		tomActivityDto.setActivityImgEn(tomActivity.getActPictureEn());
		tomActivityDto.setNeedApply(tomActivity.getNeedApply());
		tomActivityDto.setActivityStartTime(sdf.format(tomActivity.getActivityStartTime()));
		tomActivityDto.setActivityEndTime(sdf.format(tomActivity.getActivityEndTime()));

		// 培训活动任务包配置信息
		TomActivityProperty example1 = new TomActivityProperty();
		example1.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example1);
		tomActivityDto.setPackageId(tomActivityProperties.get(0).getPackageId());
		List<TomActivityPropertyDto> preTaskInfo = new ArrayList<TomActivityPropertyDto>();
		for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
			TomActivityPropertyDto tomActivityPropertyDto = new TomActivityPropertyDto();
			if (tomActivityProperty.getCourseId() != null) {
				TomCourses course = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
				if (course.getCourseOnline().equals("N")) {
					tomActivityPropertyDto.setLecturerId(tomActivityProperty.getLecturers());
					if (tomActivityProperty.getUnitPrice() != null) {
						tomActivityPropertyDto.setUnitPrice(tomActivityProperty.getUnitPrice().toString());
					} else {
						tomActivityPropertyDto.setUnitPrice("");
					}
					tomActivityPropertyDto.setTotalPrice(tomActivityProperty.getTotalPrice().toString());
					double courseTime = tomActivityProperty.getCourseTime();
					tomActivityPropertyDto.setCourseTime((int) courseTime);
					tomActivityPropertyDto.setCourseAddress(tomActivityProperty.getCourseAddress());
				}
				tomActivityPropertyDto.setTaskId(tomActivityProperty.getCourseId().toString());
				tomActivityPropertyDto.setIsTaskType("Y");
				tomActivityPropertyDto.setCourseId(tomActivityProperty.getCourseId());
				tomActivityPropertyDto.setCourseName(course.getCourseName());

			}
			if (tomActivityProperty.getExamId() != null) {
				TomExamPaper examPaper = examPaperMapper.selectByExamId(tomActivityProperty.getExamId());

				tomActivityPropertyDto.setTaskId(tomActivityProperty.getExamId().toString());
				tomActivityPropertyDto.setIsTaskType("N");
				tomActivityPropertyDto.setExamId(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamPaperId(examPaper.getExamPaperId());
				tomActivityPropertyDto.setExamName(tomActivityProperty.getExamName());
				double examTime = tomActivityProperty.getExamTime();
				tomActivityPropertyDto.setExamTime((int) examTime);
				tomActivityPropertyDto.setRetakingExamTimes(tomActivityProperty.getRetakingExamTimes().toString());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				TomExam tomExam = tomExamMapper.selectByPrimaryKey(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamAddress(tomExam.getExamAddress());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				// 封装补考
				TomRetakingExam example = new TomRetakingExam();
				example.setExamId(tomActivityProperty.getExamId());
				List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(example);
				List<String> rbeginTime = new ArrayList<String>();
				List<String> rendTime = new ArrayList<String>();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (TomRetakingExam retakingExam : retakingExams) {
					rbeginTime.add(format.format(retakingExam.getRetakingExamBeginTime()));
					rendTime.add(format.format(retakingExam.getRetakingExamEndTime()));
				}
				tomActivityPropertyDto.setRetakingExamBeginTimeList(rbeginTime);
				tomActivityPropertyDto.setRetakingExamEndTimeList(rendTime);
			}
			if(tomActivityProperty.getStartTime()!=null){
				tomActivityPropertyDto.setStartTime(sdf.format(tomActivityProperty.getStartTime()));
			}
			if(tomActivityProperty.getEndTime()!=null){
				tomActivityPropertyDto.setEndTime(sdf.format(tomActivityProperty.getEndTime()));
			}
			tomActivityPropertyDto.setSort(tomActivityProperty.getSort());
			if (tomActivityProperty.getPreTask() != null) {
				tomActivityPropertyDto.setPretaskId(tomActivityProperty.getPreTask());
				String preNames[] = tomActivityProperty.getPreTask().split(",");
				String preName = "";
				for (int i = 0; i < preNames.length; i++) {
					if (i == 0) {
						preName = preName + "任务" + preNames[i];
					} else {
						preName = preName + ",任务" + preNames[i];
					}
				}
				tomActivityPropertyDto.setPreName(preName);
			}
			preTaskInfo.add(tomActivityPropertyDto);
		}
		tomActivityDto.setPreTaskInfo(preTaskInfo);

		// 培训活动人员信息
		List<TomActivityEmpsRelation> tomActivityEmpsRelations = activityEmpsRelationMapper
				.selectByActivityId(activityId);
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		// List<String> empIds=new ArrayList<String>();
		// List<String> empNames=new ArrayList<String>();
		for (TomActivityEmpsRelation activityEmp : tomActivityEmpsRelations) {
			TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(activityEmp.getCode());
			Map<String, String> empMap = new HashMap<String, String>();
			empMap.put("code", activityEmp.getCode());
			empMap.put("name", tomEmp.getName());
			empMap.put("deptcode", tomEmp.getDeptcode());
			empMap.put("deptname", tomEmp.getDeptname());
			empMap.put("cityname", tomEmp.getCityname());
			empMap.put("deptpsncode", tomEmp.getDeptpsncode());
			emps.add(empMap);
		}

		// 培训活动推送部门负责人信息
		List<TomActivityDept> tomActivityDepts = activityDeptMapper.selectByActivityId(activityId);
		List<Map<String, String>> deptManagers = new ArrayList<Map<String, String>>();
		for (TomActivityDept tomActivityDept : tomActivityDepts) {
			TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(tomActivityDept.getHeaderCode());
			Map<String, String> deptMap = new HashMap<String, String>();
			deptMap.put("code", tomActivityDept.getHeaderCode());
			deptMap.put("name", tomEmp.getName());
			deptMap.put("deptcode", tomEmp.getDeptcode());
			deptMap.put("deptname", tomEmp.getDeptname());
			deptMap.put("cityname", tomEmp.getCityname());
			deptMap.put("deptpsncode", tomEmp.getDeptpsncode());
			deptManagers.add(deptMap);
			// 推送范围
			String codes[] = tomActivityDept.getCode().split(",");
			for (String code : codes) {
				Map<String, String> empMap = new HashMap<String, String>();
				TomEmp tomEmp2 = tomEmpMapper.selectByPrimaryKey(code);
				empMap.put("code", code);
				empMap.put("name", tomEmp2.getName());
				empMap.put("deptcode", tomEmp2.getDeptcode());
				empMap.put("deptname", tomEmp2.getDeptname());
				empMap.put("cityname", tomEmp2.getCityname());
				empMap.put("deptpsncode", tomEmp2.getDeptpsncode());
				emps.add(empMap);
			}
		}
		tomActivityDto.setDeptManagers(deptManagers);
		tomActivityDto.setEmps(emps);

		if (tomActivityDepts.size() > 0) {
			tomActivityDto.setIsPrincipal("Y");
		} else {
			tomActivityDto.setIsPrincipal("N");
		}
		TomProjectResource project =new TomProjectResource();
		project.setResourceId(activityId);
		project.setType("A");
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(project);
		if(null!=selectByResource){
			tomActivityDto.setParentProjectClassifyName(selectByResource.getParentProjectClassifyName());
			tomActivityDto.setParentClassifyId(String.valueOf(selectByResource.getProjectId()));
		}
		return tomActivityDto;

	}

	/**
	 * 功能描述：[查看培训活动明细]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午10:00:03
	 * 
	 * @param example
	 * @return
	 */
	public TomActivityDto findActivityDetails(int activityId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TomActivity tomActivity = activityMapper.selectByPrimaryKey(activityId);
		TomActivityDto tomActivityDto = new TomActivityDto();
		tomActivityDto.setIntroduction(tomActivity.getIntroduction());
		tomActivityDto.setActivityName(tomActivity.getActivityName());
		tomActivityDto.setIsCN(tomActivity.getIsCN());
		tomActivityDto.setIsEN(tomActivity.getIsEN());
		tomActivityDto.setIntroductionEn(tomActivity.getIntroductionEn());
		tomActivityDto.setActivityNameEn(tomActivity.getActivityNameEn());
		tomActivityDto.setProtocol(tomActivity.getProtocol());
		if (null == tomActivity.getCity()) {
			tomActivityDto.setCity("");
		} else {
			tomActivityDto.setCity(tomActivity.getCity());
		}
		if (null == tomActivity.getCityEn()) {
			tomActivityDto.setCityEn("");
		} else {
			tomActivityDto.setCityEn(tomActivity.getCityEn());
		}
		if (null == tomActivity.getDepts()) {
			tomActivityDto.setDeptCodes("");
		} else {
			tomActivityDto.setDeptCodes(tomActivity.getDepts());
		}
		if (null == tomActivity.getStaffLevels()) {
			tomActivityDto.setEmployeeGradeStr("");
		} else {
			tomActivityDto.setEmployeeGradeStr(tomActivity.getStaffLevels());
		}
		if (tomActivity.getProtocol() != null && "Y".equals(tomActivity.getProtocol())) {
			tomActivityDto.setProtocolStartTime(sdf.format(tomActivity.getProtocolStartTime()));
			tomActivityDto.setProtocolEndTime(sdf.format(tomActivity.getProtocolEndTime()));
			tomActivityDto.setTrainFee(tomActivity.getTrainFee().toString());
		}
		if (tomActivity.getNeedApply() != null && "Y".equals(tomActivity.getNeedApply())) {
			tomActivityDto.setNumberOfParticipants(tomActivity.getNumberOfParticipants().toString());
			tomActivityDto.setApplicationStartTime(sdf.format(tomActivity.getApplicationStartTime()));
			tomActivityDto.setApplicationDeadline(sdf.format(tomActivity.getApplicationDeadline()));
		}
		tomActivityDto.setAdmins(tomActivity.getAdmins());
		tomActivityDto.setActivityImg(tomActivity.getActPicture());
		tomActivityDto.setActivityImgEn(tomActivity.getActPictureEn());
		tomActivityDto.setNeedApply(tomActivity.getNeedApply());
		tomActivityDto.setActivityStartTime(sdf.format(tomActivity.getActivityStartTime()));
		tomActivityDto.setActivityEndTime(sdf.format(tomActivity.getActivityEndTime()));

		// 培训活动任务包配置信息
		TomActivityProperty example1 = new TomActivityProperty();
		example1.setActivityId(activityId);
		List<TomActivityProperty> tomActivityProperties = activityPropertyMapper.selectByExample(example1);
		//tomActivityDto.setPackageId(tomActivityProperties.get(0).getPackageId());
		Set<Integer> set = new HashSet<Integer>();
		for(TomActivityProperty tomActivityProperty :tomActivityProperties){
			set.add(tomActivityProperty.getPackageId());
		}
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(int id :set){
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("packageId", id);
		List<TomActivityPropertyDto> preTaskInfo = new ArrayList<TomActivityPropertyDto>();
		for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
			if(id==tomActivityProperty.getPackageId()){
			TomActivityPropertyDto tomActivityPropertyDto = new TomActivityPropertyDto();
			if (tomActivityProperty.getCourseId() != null) {
				TomCourses course = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
				String[] split = course.getLecturers().split(",");
				List<TomLecturer> lecturerList = new ArrayList<TomLecturer>();
				for(String lectureId:split){
					if(!"".equals(lectureId)){
					TomLecturer tomLecturer = tomLecturerMapper.selectByPrimaryKey(Integer.parseInt(lectureId));
					lecturerList.add(tomLecturer);
					}
				}
				tomActivityPropertyDto.setLecturerLists(lecturerList);
				if (course.getCourseOnline().equals("N")) {
					tomActivityPropertyDto.setLecturerId(tomActivityProperty.getLecturers());
					if (tomActivityProperty.getUnitPrice() != null) {
						tomActivityPropertyDto.setUnitPrice(tomActivityProperty.getUnitPrice().toString());
					} else {
						tomActivityPropertyDto.setUnitPrice("");
					}
					if(tomActivityProperty.getTotalPrice()!=null){
						tomActivityPropertyDto.setTotalPrice(tomActivityProperty.getTotalPrice().toString());
					}else {
						tomActivityPropertyDto.setTotalPrice("");
					}
					if(tomActivityProperty.getCourseTime()!=null){
						double courseTime = tomActivityProperty.getCourseTime();
						tomActivityPropertyDto.setCourseTime((int) courseTime);
					}
					tomActivityPropertyDto.setCourseAddress(tomActivityProperty.getCourseAddress());
				}
				tomActivityPropertyDto.setTaskId(tomActivityProperty.getCourseId().toString());
				tomActivityPropertyDto.setIsTaskType("Y");
				tomActivityPropertyDto.setCourseId(tomActivityProperty.getCourseId());
				tomActivityPropertyDto.setCourseName(course.getCourseName());
				tomActivityPropertyDto.setCourseOnline(course.getCourseOnline());
				tomActivityPropertyDto.setSignInTwoDimensionCode(tomActivityProperty.getSignInTwoDimensionCode());
				tomActivityPropertyDto.setGradeTwoDimensionCode(tomActivityProperty.getGradeTwoDimensionCode());
			}
			if (tomActivityProperty.getExamId() != null) {
				TomExamPaper examPaper = examPaperMapper.selectByExamId(tomActivityProperty.getExamId());

				tomActivityPropertyDto.setTaskId(tomActivityProperty.getExamId().toString());
				tomActivityPropertyDto.setIsTaskType("N");
				tomActivityPropertyDto.setExamId(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamPaperId(examPaper.getExamPaperId());
				tomActivityPropertyDto.setExamName(tomActivityProperty.getExamName());
				double examTime = tomActivityProperty.getExamTime();
				tomActivityPropertyDto.setExamTime((int) examTime);
				tomActivityPropertyDto.setRetakingExamTimes(tomActivityProperty.getRetakingExamTimes().toString());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				TomExam tomExam = tomExamMapper.selectByPrimaryKey(tomActivityProperty.getExamId());
				tomActivityPropertyDto.setExamAddress(tomExam.getExamAddress());
				tomActivityPropertyDto.setOfflineExam(tomActivityProperty.getOfflineExam());
				// 封装补考
				TomRetakingExam example = new TomRetakingExam();
				example.setExamId(tomActivityProperty.getExamId());
				List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(example);
				List<String> rbeginTime = new ArrayList<String>();
				List<String> rendTime = new ArrayList<String>();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (TomRetakingExam retakingExam : retakingExams) {
					rbeginTime.add(format.format(retakingExam.getRetakingExamBeginTime()));
					rendTime.add(format.format(retakingExam.getRetakingExamEndTime()));
				}
				tomActivityPropertyDto.setRetakingExamBeginTimeList(rbeginTime);
				tomActivityPropertyDto.setRetakingExamEndTimeList(rendTime);
			}
			if(tomActivityProperty.getStartTime()!=null){
				tomActivityPropertyDto.setStartTime(sdf.format(tomActivityProperty.getStartTime()));
			}
			if(tomActivityProperty.getEndTime()!=null){
				tomActivityPropertyDto.setEndTime(sdf.format(tomActivityProperty.getEndTime()));
			}
			tomActivityPropertyDto.setSort(tomActivityProperty.getSort());
			if (tomActivityProperty.getPreTask() != null) {
				tomActivityPropertyDto.setPretaskId(tomActivityProperty.getPreTask());
				String preNames[] = tomActivityProperty.getPreTask().split(",");
				String preName = "";
				for (int i = 0; i < preNames.length; i++) {
					if (i == 0) {
						preName = preName + "任务" + preNames[i];
					} else {
						preName = preName + ",任务" + preNames[i];
					}
				}
				tomActivityPropertyDto.setPreName(preName);
			}
			preTaskInfo.add(tomActivityPropertyDto);
			}
		}
		map.put("taskCoursesOrExamPapers", preTaskInfo);
		list.add(map);
		}
		tomActivityDto.setPackages(list);

		// 培训活动人员信息
		List<TomActivityEmpsRelation> tomActivityEmpsRelations = activityEmpsRelationMapper
				.selectByActivityId(activityId);
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		// List<String> empIds=new ArrayList<String>();
		// List<String> empNames=new ArrayList<String>();
		for (TomActivityEmpsRelation activityEmp : tomActivityEmpsRelations) {
			TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(activityEmp.getCode());
			Map<String, String> empMap = new HashMap<String, String>();
			empMap.put("code", activityEmp.getCode());
			empMap.put("name", tomEmp.getName());
			empMap.put("deptcode", tomEmp.getDeptcode());
			empMap.put("deptname", tomEmp.getDeptname());
			empMap.put("cityname", tomEmp.getCityname());
			empMap.put("deptpsncode", tomEmp.getDeptpsncode());
			emps.add(empMap);
		}

		// 培训活动推送部门负责人信息
		List<TomActivityDept> tomActivityDepts = activityDeptMapper.selectByActivityId(activityId);
		List<Map<String, String>> deptManagers = new ArrayList<Map<String, String>>();
		for (TomActivityDept tomActivityDept : tomActivityDepts) {
			TomEmp tomEmp = tomEmpMapper.selectByPrimaryKey(tomActivityDept.getHeaderCode());
			Map<String, String> deptMap = new HashMap<String, String>();
			deptMap.put("code", tomActivityDept.getHeaderCode());
			deptMap.put("name", tomEmp.getName());
			deptMap.put("deptcode", tomEmp.getDeptcode());
			deptMap.put("deptname", tomEmp.getDeptname());
			deptMap.put("cityname", tomEmp.getCityname());
			deptMap.put("deptpsncode", tomEmp.getDeptpsncode());
			deptManagers.add(deptMap);
			// 推送范围
			String codes[] = tomActivityDept.getCode().split(",");
			for (String code : codes) {
				Map<String, String> empMap = new HashMap<String, String>();
				TomEmp tomEmp2 = tomEmpMapper.selectByPrimaryKey(code);
				empMap.put("code", code);
				empMap.put("name", tomEmp2.getName());
				empMap.put("deptcode", tomEmp2.getDeptcode());
				empMap.put("deptname", tomEmp2.getDeptname());
				empMap.put("cityname", tomEmp2.getCityname());
				empMap.put("deptpsncode", tomEmp2.getDeptpsncode());
				emps.add(empMap);
			}
		}
		tomActivityDto.setDeptManagers(deptManagers);
		tomActivityDto.setEmps(emps);

		if (tomActivityDepts.size() > 0) {
			tomActivityDto.setIsPrincipal("Y");
		} else {
			tomActivityDto.setIsPrincipal("N");
		}
		TomProjectResource project =new TomProjectResource();
		project.setResourceId(activityId);
		project.setType("A");
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(project);
		if(null!=selectByResource){
			tomActivityDto.setParentProjectClassifyName(selectByResource.getParentProjectClassifyName());
			tomActivityDto.setParentClassifyId(String.valueOf(selectByResource.getProjectId()));
		}

		return tomActivityDto;

	}

	/**
	 * 功能描述：[查询培训活动列表]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午10:00:07
	 * 
	 * @param example
	 * @return
	 */
	public PageData<TomActivity> selectListByParam(int pageNum, int pageSize, String activityName) {
		PageData<TomActivity> page = new PageData<TomActivity>();
		Map<Object, Object> map = Maps.newHashMap();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if (activityName != null) {
			activityName = activityName.replaceAll("/", "//");
			activityName = activityName.replaceAll("%", "/%");
			activityName = activityName.replaceAll("_", "/_");

		}
		map.put("activityName", activityName);
		int count = activityMapper.countByList(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		List<TomActivity> list = Lists.newArrayList();

		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);// pageNum *
		list = activityMapper.selectListByParam(map);
		// List<TomActivityQr> qrlist = new ArrayList<TomActivityQr>();
		TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
		for (TomActivity activity : list) {
			activityEmpsRelation.setActivityId(activity.getActivityId());
			activityEmpsRelation.setApplyStatus("Y");
			activity.setNum(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			TomActivityQr qr = new TomActivityQr();
			qr = activityQrMapper.selectByPrActivityId(activity.getActivityId());
			if (qr != null) {
				activity.setImgpath(activityQrMapper.selectByPrActivityId(activity.getActivityId()).getQrPath());
			}
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	/**
	 * 功能描述：[费用统计]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月10日 上午11:00:36
	 * 
	 * @param example
	 * @return
	 */
	public List<TomActivityFees> findActivityCostList(int activityId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomActivityFeesKey activityFeesKey = new TomActivityFeesKey();
		activityFeesKey.setActivityId(activityId);
		List<TomActivityFees> feesList = activityFeesMapper.selectByPrimaryParam(activityFeesKey);
		return feesList;
	}

	/**
	 * 功能描述：[添加项目花费记录]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 下午2:35:29
	 * 
	 * @param dto
	 * @throws Exception
	 */
	@Transactional
	public void addActivityCost(String jsonStr) {
		ObjectMapper jsonMapper = new ObjectMapper();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray jsonArray = jsonObject.getJSONArray("listFrees");
		Date date = new Date();
		ShiroUser user = ShiroUtils.getCurrentUser();
		for (int i = 0; i < jsonArray.size(); i++) {
			try {
				TomActivityFreesDto freesDto = jsonMapper.readValue(jsonArray.get(i).toString(),
						TomActivityFreesDto.class);
				if (freesDto.getFeeName() == null || "".equals(freesDto.getFeeName()) || freesDto.getFee() == null
						|| "".equals(freesDto.getFee())) {
					continue;
				}
				TomActivityFees frees = new TomActivityFees();
				frees.setActivityId(freesDto.getActivityId());
				frees.setCreateTime(date);
				frees.setCreator(user.getName());
				frees.setFee(freesDto.getFee());
				frees.setRemark(freesDto.getRemark());
				frees.setFeeName(freesDto.getFeeName());
				frees.setStatus("Y");
				frees.setOperator(user.getName());
				frees.setCreatorId(user.getCode());
				frees.setUpdateTime(date);
				activityFeesMapper.insertSelective(frees);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能描述：[删除项目花费记录]
	 *
	 * 创建者：WangLg 创建时间: 2016年3月16日 下午2:35:29
	 * 
	 * @param dto
	 * @throws Exception
	 */
	@Transactional
	public void deleteActivityCost(TomActivityFeesKey dto) {
		TomActivityFees tomActivityFees = activityFeesMapper.selectByPrimaryKey(dto);
		tomActivityFees.setStatus("N");
		activityFeesMapper.updateByPrimaryKeySelective(tomActivityFees);
	}

	public PageData<TomGrpOrgRelation> selectByCode(int pageNum, int pageSize, String grpCode) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomGrpOrgRelation example = new TomGrpOrgRelation();
		PageData<TomGrpOrgRelation> page = new PageData<TomGrpOrgRelation>();
		example.setGrpCode(grpCode);
		int count = tomGrpOrgRelationMapper.countByExample(example);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);// pageNum *
		map.put("example", example);
		List<TomGrpOrgRelation> list = tomGrpOrgRelationMapper.selectByCode(map);

		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		for (TomGrpOrgRelation tomGrpOrgRelation : list) {

		}

		return page;

	}

	public PageData<TomEmp> selectByPage(int pageNum, int pageSize, String empcode, String name, String cityname,
			String sex, String code, String statuss, String taskState, String packageId, String country) {
		// 选择了任务包查询
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomEmp> page = new PageData<TomEmp>();
		List<TomEmp> emps = new ArrayList<TomEmp>();
		String ids = "'-1'";
		if (packageId != null && !"".equals(packageId)) {
			List<TomActivityProperty> tomActivityProperties = activityPropertyMapper
					.selectByTaskPackageId(Integer.valueOf(packageId));
			if (tomActivityProperties.size() > 0) {
				Map<String, Integer> finishedMap = new HashMap<String, Integer>();
				for (TomActivityProperty tomActivityProperty : tomActivityProperties) {
					if (tomActivityProperty.getExamId() != null) {
						List<TomExamScore> tomExamScores = tomExamScoreMapper
								.selectByExamId(tomActivityProperty.getExamId());
						for (TomExamScore tomExamScore : tomExamScores) {
							if (tomExamScore.getGradeState().equals("Y")) {
								if (finishedMap.get(tomExamScore.getCode()) == null) {
									finishedMap.put(tomExamScore.getCode(), 0);
								}
								finishedMap.put(tomExamScore.getCode(), finishedMap.get(tomExamScore.getCode()) + 1);
							}
						}
					} else if (tomActivityProperty.getCourseId() != null) {
						TomCourses courses = coursesMapper.selectByPrimaryKey(tomActivityProperty.getCourseId());
						if (courses.getCourseOnline().equals("Y")) {
							Map<Object, Object> map = new HashMap<Object, Object>();
							map.put("courseId", courses.getCourseId());
							map.put("learningDate", tomActivityProperty.getEndTime());
							List<TomCourseLearningRecord> courseLearningRecords = courseLearningRecordMapper
									.selectLearnRecord(map);
							for (TomCourseLearningRecord courseLearningRecord : courseLearningRecords) {
								if (finishedMap.get(courseLearningRecord.getCode()) == null) {
									finishedMap.put(courseLearningRecord.getCode(), 0);
								}
								finishedMap.put(courseLearningRecord.getCode(),
										finishedMap.get(courseLearningRecord.getCode()) + 1);

							}
						} else {
							TomCourseSignInRecords example = new TomCourseSignInRecords();
							example.setCourseId(courses.getCourseId());
							example.setCreateDate(tomActivityProperty.getEndTime());
							List<TomCourseSignInRecords> courseSignInRecords = courseSignInRecordsMapper
									.selectByExample(example);
							for (TomCourseSignInRecords signInRecords : courseSignInRecords) {
								if (finishedMap.get(signInRecords.getCode()) == null) {
									finishedMap.put(signInRecords.getCode(), 0);
								}
								finishedMap.put(signInRecords.getCode(), finishedMap.get(signInRecords.getCode()) + 1);
							}
						}
					}
				}
				for (Map.Entry<String, Integer> entry : finishedMap.entrySet()) {
					if (entry.getValue() == tomActivityProperties.size()) {
						ids = ids + ",'" + entry.getKey() + "'";
					}
				}
			}
		}

		TomEmp example = new TomEmp();
		Map<Object, Object> map = new HashMap<Object, Object>();
		example.setCode(empcode);
		example.setName(name);
		example.setSex(sex);
		example.setCityname(cityname);
		map.put("example", example);
		map.put("taskState", taskState);
		map.put("ids", ids);
		map.put("statuss", statuss);
		map.put("country", country);
		example.setDeptcode(code);
		example.setOrgcode(code);
		int count = tomEmpMapper.countSelectEmp(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);// pageNum *
		emps = tomEmpMapper.selectEmp(map);
		page.setDate(emps);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}

	/**
	 * 
	 * 功能描述：[导入活动人员]
	 *
	 * 创建者：JCX 创建时间: 2016年7月5日 下午5:16:18
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public String importEmps(String filePath) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String path = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		if ("xls".equals(path)) {
			String s = readXls(filePath);
			return s;
		} else if ("xlsx".equals(path)) {
			String s = readXlsx(filePath);
			return s;
		}

		return "模板文件类型不正确。";
	}

	/**
	 * 
	 * 功能描述：[读取xls]
	 *
	 * 创建者：JCX 创建时间: 2016年7月5日 下午5:31:09
	 * 
	 * @param filePath
	 * @param examId
	 * @return
	 * @throws IOException
	 */
	@Transactional
	private String readXls(String filePath) throws IOException {

		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

		Set<String> codes = new HashSet<String>();
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		List<String> errorCodes = new ArrayList<String>();
		Map<Object, Object> result = new HashMap<Object, Object>();

		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null && hssfRow.getCell(0) != null && !"".equals(hssfRow.getCell(0))) {

					HSSFCell code = hssfRow.getCell(0);
					codes.add(getValue(code));
				}
			}
		}

		for (String code : codes) {
			TomEmp emp = empMapper.selectByPrimaryKey(code);
			if (emp != null) {
				Map<String, String> empMap = new HashMap<String, String>();
				empMap.put("code", emp.getCode());
				empMap.put("name", emp.getName());
				empMap.put("deptcode", emp.getDeptcode());
				empMap.put("deptname", emp.getDeptname());
				empMap.put("cityname", emp.getCityname());
				empMap.put("deptpsncode", emp.getDeptpsncode());
				emps.add(empMap);
			} else {
				errorCodes.add(code);
			}
		}

		result.put("emps", emps);
		result.put("errorCodes", errorCodes);
		return mapper.writeValueAsString(result);
	}

	/**
	 * 
	 * 功能描述：[读取xlsx]
	 *
	 * 创建者：JCX 创建时间: 2016年7月5日 下午5:41:10
	 * 
	 * @param filePath
	 * @param examId
	 * @return
	 * @throws IOException
	 */
	@Transactional
	private String readXlsx(String filePath) throws IOException {
		Set<String> codes = new HashSet<String>();
		List<String> errorCodes = new ArrayList<String>();
		List<Map<String, String>> emps = new ArrayList<Map<String, String>>();
		Map<Object, Object> result = new HashMap<Object, Object>();

		InputStream is = new FileInputStream(filePath);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// Read the Sheet
		int m = xssfWorkbook.getNumberOfSheets();
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null && xssfRow.getCell(0) != null && !"".equals(xssfRow.getCell(0))) {
					XSSFCell code = xssfRow.getCell(0);
					codes.add(getValue(code));
				}
			}
		}

		for (String code : codes) {
			/*double id =Double.parseDouble(code); 
			Integer  ids = (int)id;*/
			TomEmp emp = empMapper.selectByPrimaryKey(code);
			if (emp != null && emp.getPoststat().equals("Y")) {
				Map<String, String> empMap = new HashMap<String, String>();
				empMap.put("code", emp.getCode());
				empMap.put("name", emp.getName());
				empMap.put("deptcode", emp.getDeptcode());
				empMap.put("deptname", emp.getDeptname());
				empMap.put("cityname", emp.getCityname());
				empMap.put("deptpsncode", emp.getDeptpsncode());
				emps.add(empMap);
			} else {
				errorCodes.add(code);
			}
		}

		result.put("emps", emps);
		result.put("errorCodes", errorCodes);
		return mapper.writeValueAsString(result);
	}

	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx 创建时间: 2016年3月8日 下午4:20:16
	 * 
	 * @param xssfRow
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx 创建时间: 2016年3月8日 下午4:20:32
	 * 
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	@Transactional
	public void updateByPrimaryKeySelective(TomActivityEmpsRelation activityEmpsRelation) {
		activityEmpsRelationMapper.updateByPrimaryKeySelective(activityEmpsRelation);
	}

	@Transactional
	public void insertSelective(TomActivityEmpsRelation tomActivityEmpsRelation) {
		activityEmpsRelationMapper.insertSelective(tomActivityEmpsRelation);
	}

	@Transactional
	public void updateByPrimaryKeySelective(TomActivityDept activityDept) {
		activityDeptMapper.updateByPrimaryKeySelective(activityDept);
	}

	@Transactional
	public void deleteByPrimaryKey(TomActivityEmpsRelationKey example) {
		activityEmpsRelationMapper.deleteByPrimaryKey(example);
	}
	
	/**
	 * 功能描述：[查询活动完成进度]
	 * 创建者：Acemon
	 * 创建时间：2017-01-04
	 */
	/*public PageData<TomActivityProcessDto> queryActivityProcess(int pageNum, int pageSize,int activityId,String code){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityProcessDto> list = new ArrayList<TomActivityProcessDto>();
		PageData<TomActivityProcessDto> page = new PageData<TomActivityProcessDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("activityId", activityId);
		map.put("code", code);
		TomActivityProcessDto tomActivityProcessDto = new TomActivityProcessDto();
		
		int count = activityMapper.countByList(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);
       for (TomActivityProcessDto tomActivityProcessDtos : list) {
   		int totalTask = activityMapper.selectTotalTask(map);
   		int finishedCourseTask = activityMapper.selectFinishedCourseTask(map);
   		int finishedExamTask = activityMapper.selectFinishedExamTask(map);
   		NumberFormat nf = NumberFormat.getPercentInstance();
           nf.setMaximumFractionDigits(2);
           if (totalTask!=0) {
           if (finishedCourseTask==0&&finishedExamTask==0) {
   			tomActivityProcessDto.setActivityProcess("0%");
   		}else {
   			String string = nf.format(Double.parseDouble(String.valueOf(finishedCourseTask)+String.valueOf(finishedExamTask))/Double.parseDouble(String.valueOf(totalTask)));
   	        tomActivityProcessDto.setActivityProcess(string);
   		}
           }else {
   			tomActivityProcessDto.setActivityProcess("-");
   		}
           
		}
		
		
        page.setDate(list);
        page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
		
	}*/
}
