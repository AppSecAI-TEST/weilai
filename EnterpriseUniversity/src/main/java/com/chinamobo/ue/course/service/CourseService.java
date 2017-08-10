package com.chinamobo.ue.course.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
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

import com.chinamobo.ue.activity.dao.TomProjectResourceMapper;
import com.chinamobo.ue.activity.entity.TomProjectResource;import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseApplyMapper;
import com.chinamobo.ue.course.dao.TomCourseClassesMapper;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseClassifyRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseCommentMapper;
import com.chinamobo.ue.course.dao.TomCourseDetailMapper;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSectionMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomGradeRecordsMapper;
import com.chinamobo.ue.course.dao.TomLearnTimeLogMapper;
import com.chinamobo.ue.course.dao.TomOfflineSignMapper;
import com.chinamobo.ue.course.dao.TomTestMapper;
import com.chinamobo.ue.course.dto.CopyOnlineCourse;
import com.chinamobo.ue.course.dto.CourseApplyDto;
import com.chinamobo.ue.course.dto.CourseJsonDto;
import com.chinamobo.ue.course.dto.OfflineCourseDto;
import com.chinamobo.ue.course.dto.TomEmpCourseDto;
import com.chinamobo.ue.course.entity.TomCourseApply;
import com.chinamobo.ue.course.entity.TomCourseClasses;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseClassifyRelation;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.course.entity.TomCourseDetail;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSection;
import com.chinamobo.ue.course.entity.TomCourseSectionLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourseThumbUp;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.course.entity.TomFavoriteCourse;
import com.chinamobo.ue.course.entity.TomGradeRecords;
import com.chinamobo.ue.course.entity.TomLearnTimeLog;
import com.chinamobo.ue.course.entity.TomOfflineSign;
import com.chinamobo.ue.course.entity.TomTest;
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
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.system.dto.RollingDto;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.entity.WxMessage;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.PathUtil;
import com.chinamobo.ue.utils.QRCodeUtil;
import com.chinamobo.ue.utils.StringUtil;

import net.sf.json.JSONObject;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程service
 *
 * 作者: JCX
 * 创建时间: 2016年3月3日 下午3:39:13
 */
@Service
public class CourseService {
	
	@Autowired
	private TomActivityEmpsRelationMapper tomActivityEmpsRelationMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private CourseClassifyService classifyService;
	@Autowired
	private TomCourseCommentMapper commentMapper;
	@Autowired
	private CourseSectionService courseSectionService;
	@Autowired
	private TomCourseSectionMapper courseSectionMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private TomCourseClassifyMapper courseClassifyMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	@Autowired
	private TomCourseClassifyRelationMapper courseClassifyRelationMapper;
	@Autowired
    private TomRollingPictureMapper rollingPictureMapper;
	@Autowired
	private TomCourseSectionLearningRecordMapper courseSectionLearningRecordMapper;
	@Autowired
	private TomLearnTimeLogMapper learnTimeLogMapper;
	@Autowired
	private TomGradeRecordsMapper gradeRecordsMapper;
	@Autowired
	private TomCourseSignInRecordsMapper tomCourseSignInRecordsMapper;
	@Autowired
	private TomTestMapper tomTestMapper;
	@Autowired
	private TomCourseClassesMapper classesMapper;
	@Autowired
	private TomCourseDetailMapper detailMapper;
	@Autowired
	private TomCourseApplyMapper applyMapper;
	@Autowired
	private TomProjectResourceMapper tomProjectResourceMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomOfflineSignMapper tomOfflineSignMapper;
	@Autowired
	private TomCourseApplyMapper courseApplyMapper;
	
	String filePath1=Config.getProperty("file_path");
	/**
	 * 
	 * 功能描述：[添加课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:39:25
	 * @param course
	 * @throws Exception 
	 */
	@Transactional
	public void addCourse(TomCourses course) throws Exception {
		course.setCourseNumber(numberRecordService.getNumber(MapManager.numberType("KC")));
		course.setCreateTime(new Date());
		course.setUpdateTime(new Date());
		course.setThumbUpCount(0);
		course.setViewers(0);
		course.setCommentCount(0);
		course.setCourseAverage(0d);
		course.setTotScore(0d);
		course.setLecturerAverage(0d);
		
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		
		coursesMapper.insertSelective(course);
		
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(course.getCreator());
		resource.setOperator(course.getOperator());
		resource.setProjectId(Integer.parseInt(course.getParentClassifyId()));
		resource.setResourceId(course.getCourseId());
		resource.setParentProjectClassifyName(course.getParentProjectClassifyName());
		resource.setStatus("Y");
		resource.setType("C");
		resource.setUpdateTime(new Date());
		tomProjectResourceMapper.insertSelective(resource);
		
		//存储章节子表
		if(course.getSectionAddressList()!=null){
			TomCourseSection courseSection;
			for(int i=0;i<course.getSectionAddressList().size();i++){
				courseSection=new TomCourseSection();
				courseSection.setCourseId(course.getCourseId());
				courseSection.setSectionAddress(course.getSectionAddressList().get(i));
				courseSection.setSectionName(course.getSectionNameList().get(i));
				courseSection.setFileUrl(course.getFileUrlList().get(i));
				courseSection.setCreator(course.getCreator());
				courseSection.setCreatorId(course.getCreatorId());
				courseSection.setSectionType(course.getSectionType().get(i));
				courseSection.setOperator(course.getOperator());
				courseSection.setSectionSize(course.getSectionSize().get(i));
				courseSection.setSectionType(course.getSectionType().get(i));
				courseSection.setStatus("Y");
				courseSection.setCreateTime(new Date());
				courseSection.setUpdateTime(new Date());
				courseSectionMapper.insertSelective(courseSection);
			}
		}
		//存储关联关系
		TomCourseEmpRelation courseEmpRelation;
		for(String code:course.getEmpIds()){
			courseEmpRelation=new TomCourseEmpRelation();
			courseEmpRelation.setCode(code);
			courseEmpRelation.setCourseId(course.getCourseId());
			courseEmpRelation.setCreateTime(course.getCreateTime());
			courseEmpRelation.setFinishStatus("N");
			courseEmpRelation.setStatus("Y");
			courseEmpRelation.setCreator(course.getCreator());
			courseEmpRelation.setOperator(course.getOperator());
			courseEmpRelation.setUpdateTime(course.getCreateTime());
			courseEmpRelationMapper.insertSelective(courseEmpRelation);
		}
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(course.getCreateTime());
			courseClassifyRelation.setUpdateTime(course.getCreateTime());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		if("N".equals(course.getCourseOnline())){

			QRCodeUtil.courseEncode("ele-sign-"+course.getCourseId(),course.getCourseName()+"签到二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"签到二维码-"+course.getCourseId()+".jpg");
			QRCodeUtil.courseEncode("ele-grade-"+course.getCourseId(),course.getCourseName()+"评分二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setGradeTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"评分二维码-"+course.getCourseId()+".jpg");
			coursesMapper.updateByPrimaryKeySelective(course);
		}
			
	}

	/**
	 * 
	 * 功能描述：[查询课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:39:40
	 * @param courseId
	 * @return
	 */
	public TomCourses selectCourseById(int courseId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourses courses= coursesMapper.selectByPrimaryKey(courseId);
		
		//封装章节信息
		TomCourseSection example=new TomCourseSection();
		example.setCourseId(courseId);
		List<TomCourseSection> courseSections=courseSectionService.selectListByEXample(example);
		List<Integer> sectionId=new ArrayList<Integer>();
		List<String> sectionNameList=new ArrayList<String>();
		List<String> sectionAddressList=new ArrayList<String>();
		List<String> fileUrlList=new ArrayList<String>();
		List<String> sectionType=new ArrayList<String>();
		List<Integer> sectionSize=new ArrayList<Integer>();
		for(TomCourseSection courseSection:courseSections){
			sectionId.add(courseSection.getSectionId());
			sectionNameList.add(courseSection.getSectionName());
			sectionAddressList.add(courseSection.getSectionAddress());
			fileUrlList.add(courseSection.getFileUrl() == null ? "" : courseSection.getFileUrl());
			sectionType.add(courseSection.getSectionType());
			sectionSize.add(courseSection.getSectionSize());
		}
		courses.setSectionAddressList(sectionAddressList);
		courses.setSectionId(sectionId);
		courses.setSectionNameList(sectionNameList);
		courses.setSectionType(sectionType);
		courses.setSectionSize(sectionSize);
	
		//封装员工关联
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		map.put("createTime", courses.getCreateTime());
		map.put("status", "Y");
		List<TomEmp> emps = empMapper.selectListByCourseId(map);
		List<String> codes=new ArrayList<String>();
		List<String> names=new ArrayList<String>();
		List<String> citynames=new ArrayList<String>();
		List<String> deptnames=new ArrayList<String>();
		for(TomEmp emp:emps){
			codes.add(emp.getCode());
			names.add(emp.getName());
			citynames.add(emp.getCityname());
			deptnames.add(emp.getDeptname());
		}
		courses.setEmpIds(codes);
		courses.setEmpNames(names);
		courses.setCityname(citynames);
		courses.setDeptname(deptnames);
		//封装课程分类
		List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courseId);
		List<Integer> classifyIds=new ArrayList<Integer>();
		List<String> classifyNames=new ArrayList<String>();
		for(TomCourseClassify courseClassify:courseClassifies){
			classifyIds.add(courseClassify.getClassifyId());
			classifyNames.add(courseClassify.getClassifyName());
		}
		courses.setClassifyIds(classifyIds);
		courses.setClassifyNames(classifyNames);
		
		return courses;
	}
	
	public CourseJsonDto selectCourseDetail(int courseId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomCourses courses= coursesMapper.selectByPrimaryKey(courseId);
		CourseJsonDto dto=new CourseJsonDto();
		
		//封装章节信息
		TomCourseSection example=new TomCourseSection();
		example.setCourseId(courseId);
		List<TomCourseSection> courseSections=courseSectionService.selectListByEXample(example);
		List<Integer> sectionId=new ArrayList<Integer>();
		List<String> sectionNameList=new ArrayList<String>();
		List<String> sectionAddressList=new ArrayList<String>();
		List<String> fileUrlList=new ArrayList<String>();
		List<String> sectionType=new ArrayList<String>();
		List<Integer> sectionSize=new ArrayList<Integer>();
		for(TomCourseSection courseSection:courseSections){
			sectionId.add(courseSection.getSectionId());
			sectionNameList.add(courseSection.getSectionName());
			sectionAddressList.add(courseSection.getSectionAddress());
			fileUrlList.add(courseSection.getFileUrl() == null ? "" : courseSection.getFileUrl());
			sectionType.add(courseSection.getSectionType());
			sectionSize.add(courseSection.getSectionSize());
		}
		courses.setSectionAddressList(sectionAddressList);
		courses.setSectionId(sectionId);
		courses.setSectionNameList(sectionNameList);
		courses.setSectionType(sectionType);
		courses.setSectionSize(sectionSize);
		//装项目分类名称
		TomProjectResource project =new TomProjectResource();
		project.setResourceId(courseId);
		project.setType("C");
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(project);
		if(null!=selectByResource){
		courses.setParentProjectClassifyName(selectByResource.getParentProjectClassifyName());
		courses.setParentClassifyId(String.valueOf(selectByResource.getProjectId()));
		}
		//封装员工关联
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		map.put("createTime", courses.getCreateTime());
		map.put("status", "Y");
		List<TomEmp> emps = empMapper.selectListByCourseId(map);
		List<String> codes=new ArrayList<String>();
		List<String> names=new ArrayList<String>();
		List<String> citynames=new ArrayList<String>();
		List<String> deptnames=new ArrayList<String>();
		for(TomEmp emp:emps){
			codes.add(emp.getCode());
			names.add(emp.getName());
			citynames.add(emp.getCityname());
			deptnames.add(emp.getDeptname());
		}
		courses.setEmpIds(codes);
		courses.setEmpNames(names);
		courses.setCityname(citynames);
		courses.setDeptname(deptnames);
		//封装课程分类
		List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courseId);
		List<Integer> classifyIds=new ArrayList<Integer>();
		List<String> classifyNames=new ArrayList<String>();
		for(TomCourseClassify courseClassify:courseClassifies){
			classifyIds.add(courseClassify.getClassifyId());
			classifyNames.add(courseClassify.getClassifyName());
		}
		courses.setClassifyIds(classifyIds);
		courses.setClassifyNames(classifyNames);
		PropertyUtils.copyProperties(dto,courses);
		dto.setSectionList(courseSections);
		return dto;
	}

	/**
	 * 
	 * 功能描述：[分页查询课程,pageSize=-1则不分页]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:39:59
	 * @param pageNum
	 * @param pageSize
	 * @param example
	 * @return
	 */
	public PageData<TomCourses> selectListByPage(int pageNum, int pageSize,TomCourses example) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourses> page=new PageData<TomCourses>();	
		List<TomCourses> list;
		int count=coursesMapper.countByExample(example);
		
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum",pageSize);//  pageNum*
		map.put("example", example);
		map.put("listOrder", "c.UPDATE_TIME");
		map.put("orderBy", "desc");
		list = coursesMapper.selectListByPage(map);
		
		TomCourseLearningRecord learningRecordExample;
		TomCourseSignInRecords courseSignInRecordExample;
		for(TomCourses courses:list){
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courses.getCourseId());
			Map<Object,Object> map2 = new HashMap<Object,Object>();
			map2.put("courseId", courses.getCourseId());
			List<TomCourseClasses> classList= classesMapper.selectByCourseId(map2);
			courses.setCountclasses(classList.size());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			courses.setCourseType(courseType);
			
			if(courses.getCourseOnline().equals("Y")){
				learningRecordExample=new TomCourseLearningRecord();
				learningRecordExample.setCourseId(courses.getCourseId());
				courses.setLearnedNum(courseLearningRecordMapper.countByExample(learningRecordExample));
			}else{
				Map<Object,Object> map1=new HashMap<Object,Object>();
				map1.put("courseId", courses.getCourseId());
				int count1=applyMapper.countApply(map1);
				courses.setCountApply(count1);
				courseSignInRecordExample=new TomCourseSignInRecords();
				courseSignInRecordExample.setCourseId(courses.getCourseId());
				courses.setLearnedNum(courseSignInRecordsMapper.countByExample(courseSignInRecordExample));
			}
//			Map<Object,Object> map2 = new HashMap<Object,Object>();
//			List<TomExportTestDto> tomTest=tomTestMapper.selectByCourseId(map2);
//判断表tom_test是否有数据			
			List<TomTest> tomTest=tomTestMapper.selectByCourseIdEX(courses.getCourseId());
			
			if(tomTest.size()>0){
				courses.setTestIfNull("Y");
			}else{
				courses.setTestIfNull("N");
			}
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}
	public PageData<TomCourseClasses> findClasses(String courseId,int pageNum, int pageSize){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourseClasses> page=new PageData<TomCourseClasses>();
		Map<Object,Object> map2 = new HashMap<Object,Object>();
		map2.put("courseId", courseId);
		List<TomCourseClasses> classList= classesMapper.selectByCourseId(map2);
		for(TomCourseClasses record : classList){
			record.setFilepath(record.getFilepath()+record.getClasses());
		}
		page.setDate(classList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(classList.size());
		
		return page;
	}
	/**
	 * 
	 * 功能描述：[更新课程]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:40:13
	 * @param course
	 * @throws Exception 
	 */
	@Transactional
	public void updateCourse(TomCourses course) throws Exception {
		course.setUpdateTime(new Date());
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		
		coursesMapper.updateByPrimaryKeySelective(course);
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(course.getCreator());
		resource.setOperator(course.getOperator());
		resource.setProjectId(Integer.parseInt(course.getParentClassifyId()));
		resource.setResourceId(course.getCourseId());
		resource.setStatus("Y");
		resource.setType("C");
		resource.setParentProjectClassifyName(course.getParentProjectClassifyName());
		resource.setUpdateTime(new Date());
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(resource);
		if(!selectByResource.getProjectId().equals(resource.getProjectId())){//判断与原有项目分类是否是一个
			selectByResource.setStatus("N");
			tomProjectResourceMapper.updateByPrimaryKeySelective(selectByResource);
			tomProjectResourceMapper.insertSelective(resource);
		}
		
		//更新章节子表
		if(course.getSectionAddressList()!=null){
			TomCourseSection courseSectionExample=new TomCourseSection();
			courseSectionExample.setStatus("N");
			courseSectionExample.setCourseId(course.getCourseId());
			courseSectionMapper.updateByPrimaryKeySelective(courseSectionExample);
			
			TomCourseSection courseSection;
			for(int i=0;i<course.getSectionAddressList().size();i++){
				courseSection=new TomCourseSection();
				if(course.getSectionId().get(i)!=-1){
					courseSection.setSectionId(course.getSectionId().get(i));
					courseSection.setStatus("Y");
					courseSectionMapper.updateByPrimaryKeySelective(courseSection);
					
				}else{
					courseSection.setCourseId(course.getCourseId());
					courseSection.setSectionAddress(course.getSectionAddressList().get(i));
					courseSection.setSectionName(course.getSectionNameList().get(i));
					courseSection.setFileUrl(course.getFileUrlList().get(i));
					courseSection.setCreator(course.getCreator());
					courseSection.setOperator(course.getOperator());
					courseSection.setStatus("Y");
					courseSection.setSectionSize(course.getSectionSize().get(i));
					courseSection.setSectionType(course.getSectionType().get(i));
					courseSectionMapper.insertSelective(courseSection);
				}
			}
			//更新删除的章节
//			TomCourseSection example=new TomCourseSection();
//			example.setCourseId(course.getCourseId());
//			List<TomCourseSection> courseSections=courseSectionMapper.selectListByExample(example);
//			for(TomCourseSection courseSection2:courseSections){
//				boolean isDelete=true;
//				for(int id:course.getSectionId()){
//					if(id==courseSection2.getSectionId()){
//						isDelete=false;
//						break;
//					}
//				}
//				if(isDelete==true){
//					courseSection2.setStatus("N");
//					courseSection2.setUpdateTime(new Date());
//					courseSectionMapper.updateByPrimaryKeySelective(courseSection2);
//				}
//			}
		}
		//伪删除员工关联
		TomCourses courses=coursesMapper.selectByPrimaryKey(course.getCourseId());
		TomCourseEmpRelation courseEmpRelationExample=new TomCourseEmpRelation();
		courseEmpRelationExample.setCourseId(course.getCourseId());
		courseEmpRelationExample.setCreateTime(courses.getCreateTime());
		courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
		if ("Y".equals(course.getCourseOnline())) {
			//删除签到记录
			TomCourseSignInRecords courseSignInRecords=new TomCourseSignInRecords();
			courseSignInRecords.setCourseId(course.getCourseId());
			courseSignInRecordsMapper.deleteByExample(courseSignInRecords);
			//更新员工关联关系
			TomCourseEmpRelation courseEmpRelation;
			for (String code : course.getEmpIds()) {
				courseEmpRelation = new TomCourseEmpRelation();
				courseEmpRelation.setCode(code);
				courseEmpRelation.setCourseId(course.getCourseId());
				List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper.selectByExample(courseEmpRelation);
				if(courseEmpRelations.size()==0){
					courseEmpRelation.setCreateTime(courses.getCreateTime());
					courseEmpRelation.setFinishStatus("N");
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelation.setCreator(course.getCreator());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelationMapper.insertSelective(courseEmpRelation);
				}else{
					courseEmpRelation=courseEmpRelations.get(0);
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelationMapper.update(courseEmpRelation);
				}
				
			}
		}else{
			//删除线上课程学习记录
			TomCourseLearningRecord courseLearningRecord=new TomCourseLearningRecord();
			courseLearningRecord.setCourseId(course.getCourseId());
			courseLearningRecordMapper.deleteByExample(courseLearningRecord);
		}
		//删除课程分类
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		classifyRelationExample.setCourseId(course.getCourseId());
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(new Date());
			courseClassifyRelation.setUpdateTime(new Date());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		
		//修改轮播图名称
		Map<Object,Object> map=new HashMap<Object, Object>();
	    map.put("resourceId",course.getCourseId());
	    map.put("pictureCategory", "C");
	    map.put("resourceName", course.getCourseName());
	    rollingPictureMapper.updateResourceName(map);
		if("N".equals(course.getCourseOnline())){

			QRCodeUtil.courseEncode("ele-sign-"+course.getCourseId(),course.getCourseName()+"签到二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"签到二维码-"+course.getCourseId()+".jpg");
			QRCodeUtil.courseEncode("ele-grade-"+course.getCourseId(),course.getCourseName()+"评分二维码-"+course.getCourseId(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"course");
			course.setGradeTwoDimensionCode("file" + "/tdc"+"/course/"+course.getCourseName()+"评分二维码-"+course.getCourseId()+".jpg");
			coursesMapper.updateByPrimaryKeySelective(course);
		}
			
	}

	/**
	 * 
	 * 功能描述：[课程上架/下架处理]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月3日 下午3:40:30
	 * @param course
	 */
	public void updateStatus(TomCourses course) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		course.setUpdateTime(new Date());
		coursesMapper.updateByPrimaryKeySelective(course);
	}

	
	/**
	 * 功能描述：[课程统计]
	 *
	 * 创建者：WangLg
	 * 创建时间: 2016年3月9日 上午10:16:49
	 * @param courseName
	 */
	public List<TomCourses> courseCountsList(String courseName) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
		return coursesMapper.courseCountsList(courseName);		
	}

	public List<TomCourses> selectCourseByLecturerId(int lecturerId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
		return coursesMapper.selectByLecturerId(lecturerId);
	}

	/**
	 * 
	 * 功能描述：[更新课程评论数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月15日 下午3:59:04
	 * @param course
	 */
	@Transactional
	public void updateComment(Integer courseId) {
		TomCourseComment example=new TomCourseComment();
		example.setCourseId(courseId);
//		example.setStatus("Y");
		int commentCount=commentMapper.countByExample(example);
		
		TomCourses course=new TomCourses();
		course.setCourseId(courseId);
		course.setCommentCount(commentCount);
		coursesMapper.updateByPrimaryKeySelective(course);
	}

	public PageData<RollingDto> selectListByPageForRooling(int pageNum, int pageSize,TomCourses example) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
		PageData<RollingDto> page=new PageData<RollingDto>();	
		List<TomCourses> list;
		int count=coursesMapper.countByExampleForRooling(example);
		
		if(pageSize==-1){
			pageSize=count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		map.put("example", example);
		map.put("listOrder", "UPDATE_TIME");
		map.put("orderBy", "desc");
		list = coursesMapper.selectListByPageForRooling(map);
		List<RollingDto> rollingList=new ArrayList<RollingDto>();
		TomCourseLearningRecord learningRecordExample;
		for(TomCourses courses:list){
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courses.getCourseId());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			courses.setCourseType(courseType);
			
			learningRecordExample=new TomCourseLearningRecord();
			learningRecordExample.setCourseId(courses.getCourseId());
			courses.setLearnedNum(courseLearningRecordMapper.countByExample(learningRecordExample));
			RollingDto dto=new RollingDto();
			dto.setResourceName(courses.getCourseName());
			dto.setResourceType("课程");
			dto.setResourceId(courses.getCourseId());
			rollingList.add(dto);
		}
		
		page.setDate(rollingList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}
	
	
	@Transactional
	public void insertSelective(TomCourseEmpRelation courseEmpRelation){
		courseEmpRelationMapper.insertSelective(courseEmpRelation);
	}
	@Transactional
	public void update(TomCourseEmpRelation courseEmpRelation){
		courseEmpRelationMapper.update(courseEmpRelation);
	}
	@Transactional
	public void deleteByExample(TomCourseEmpRelation courseEmpRelationExample){
		courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
	}
	@Transactional
	public void updateByCourseIdandCode(TomFavoriteCourse favoriteCourse){
		coursesMapper.updateByCourseIdandCode(favoriteCourse);
	}
	@Transactional
	public void insertFavorite(TomFavoriteCourse favoriteCourse){
		coursesMapper.insertFavorite(favoriteCourse);
	}
	@Transactional
	public void updateThumbUpByCourseIdandCode(TomCourseThumbUp courseThumbUp){
		coursesMapper.updateThumbUpByCourseIdandCode(courseThumbUp);
	}                 
	@Transactional
	public void insertThumbUp(TomCourseThumbUp courseThumbUp){
		coursesMapper.insertThumbUp(courseThumbUp);
	}
	@Transactional
	public void updateByPrimaryKey(TomCourses course){
		coursesMapper.updateByPrimaryKey(course);
	}
	@Transactional
	public void insertSectionLearning(TomCourseSectionLearningRecord sectionLearning){
		courseSectionLearningRecordMapper.insertSectionLearning(sectionLearning);
	}
	@Transactional
	public void insertSelective(TomLearnTimeLog learnTimeLog){
		learnTimeLogMapper.insertSelective(learnTimeLog);
	}
	@Transactional
	public void insertSelective(TomCourseLearningRecord courseLearningRecord){
		courseLearningRecordMapper.insertSelective(courseLearningRecord);
	}
	
	@Transactional
	public void updateByPrimaryKeySelective(TomCourses courses){
		coursesMapper.updateByPrimaryKeySelective(courses);
	}
	@Transactional
	public void insertSelective(TomGradeRecords gradeRecord){
		gradeRecordsMapper.insertSelective(gradeRecord);
	}
	@Transactional
	public void insert (TomCourseSignInRecords tomCourseSignInRecords){
		tomCourseSignInRecordsMapper.insert(tomCourseSignInRecords);
	}
	@Transactional
	public void addCourseNew(String json) throws Exception{
		JsonMapper mapper = new JsonMapper();
		CourseJsonDto courseJsonDto=mapper.fromJson(json, CourseJsonDto.class);
		this.addCourseNew(courseJsonDto);
	}
	@Transactional
	public void addCourseNew(CourseJsonDto courseJsonDto) throws Exception{
		ShiroUser user=ShiroUtils.getCurrentUser();
		TomCourses course=new TomCourses();
		PropertyUtils.copyProperties(course,courseJsonDto);
		course.setCourseNumber(numberRecordService.getNumber(MapManager.numberType("KC")));
		course.setCreateTime(new Date());
		course.setUpdateTime(new Date());
		course.setThumbUpCount(0);
		course.setViewers(0);
		course.setCommentCount(0);
		course.setCourseAverage(0d);
		course.setTotScore(0d);
		course.setLecturerAverage(0d);
		course.setCreator(user.getName());
		course.setOperator(user.getName());
		course.setCreatorId(user.getCode());
		course.setPccn("N");
		course.setPcen("N");
		course.setH5cn("N");
		course.setH5en("N");
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		
		coursesMapper.insertSelective(course);
		
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(course.getCreator());
		resource.setOperator(course.getOperator());
		resource.setProjectId(Integer.parseInt(course.getParentClassifyId()));
		resource.setResourceId(course.getCourseId());
		resource.setParentProjectClassifyName(course.getParentProjectClassifyName());
		resource.setStatus("Y");
		resource.setType("C");
		resource.setUpdateTime(new Date());
		tomProjectResourceMapper.insertSelective(resource);
		
		//存储章节子表
		String sectionTypes="";
		for(TomCourseSection courseSection:courseJsonDto.getSectionList()){
			sectionTypes=sectionTypes+","+courseSection.getSectionType();
			if("1".equals(courseSection.getSectionType()) || "2".equals(courseSection.getSectionType()) || "3".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
					course.setH5cn("Y");
				}
			}else if("4".equals(courseSection.getSectionType()) || "6".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setH5cn("Y");
				}
			}else if("5".equals(courseSection.getSectionType()) || "7".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
				}
			}
			courseSection.setStatus("Y");
			courseSection.setCourseId(course.getCourseId());
			courseSection.setCreator(course.getCreator());
			courseSection.setCreatorId(course.getCreatorId());
			courseSection.setOperator(course.getOperator());
			courseSection.setCreateTime(new Date());
			courseSection.setUpdateTime(new Date());
			courseSectionMapper.insertSelective(courseSection);
		}
		if(sectionTypes.length()>0){
			course.setSectionTypes(sectionTypes.substring(1));
		}
		coursesMapper.updateByPrimaryKeySelective(course);
		//存储关联关系
		List<TomCourseEmpRelation> listEmp=new ArrayList<TomCourseEmpRelation>();
		for(String code:course.getEmpIds()){
			TomCourseEmpRelation courseEmpRelation=new TomCourseEmpRelation();
			courseEmpRelation.setCode(code);
			courseEmpRelation.setCourseId(course.getCourseId());
			courseEmpRelation.setCreateTime(course.getCreateTime());
			courseEmpRelation.setFinishStatus("N");
			courseEmpRelation.setStatus("Y");
			courseEmpRelation.setCreator(course.getCreator());
			courseEmpRelation.setOperator(course.getOperator());
			courseEmpRelation.setUpdateTime(course.getCreateTime());
			listEmp.add(courseEmpRelation);
//			courseEmpRelationMapper.insertSelective(courseEmpRelation);
		}
		if(listEmp.size()>0){
			courseEmpRelationMapper.insertList(listEmp);
		}
		//存储课程分类子表
		for(int classifyId:course.getClassifyIds()){
			TomCourseClassifyRelation courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(course.getCreateTime());
			courseClassifyRelation.setUpdateTime(course.getCreateTime());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
	}
	@Transactional
	public void updateNew(String json) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		ShiroUser user=ShiroUtils.getCurrentUser();
		JsonMapper mapper = new JsonMapper();
		CourseJsonDto courseJsonDto=mapper.fromJson(json, CourseJsonDto.class);
		TomCourses course=new TomCourses();
		PropertyUtils.copyProperties(course,courseJsonDto);
		course.setUpdateTime(new Date());
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		course.setOperator(user.getName());
		//项目名称分类关联
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(course.getCreator());
		resource.setOperator(course.getOperator());
		resource.setProjectId(Integer.parseInt(course.getParentClassifyId()));
		resource.setResourceId(course.getCourseId());
		resource.setStatus("Y");
		resource.setType("C");
		resource.setParentProjectClassifyName(course.getParentProjectClassifyName());
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
		
		
		//更新章节子表
		TomCourseSection courseSectionExample=new TomCourseSection();
		courseSectionExample.setStatus("N");
		courseSectionExample.setCourseId(course.getCourseId());
		courseSectionMapper.updateByPrimaryKeySelective(courseSectionExample);
		course.setPccn("N");
		course.setPcen("N");
		course.setH5cn("N");
		course.setH5en("N");
		String sectionTypes= "";
		for(TomCourseSection courseSection:courseJsonDto.getSectionList()){
			sectionTypes=sectionTypes+","+courseSection.getSectionType();
			if(courseSection.getSectionId()!=-1){
				courseSection.setStatus("Y");
				courseSectionMapper.updateByPrimaryKeySelective(courseSection);
			}else{
				courseSection.setCourseId(course.getCourseId());
				courseSection.setCreator(course.getCreator());
				courseSection.setOperator(course.getOperator());
				courseSection.setStatus("Y");
				courseSectionMapper.insertSelective(courseSection);
			}
			if("1".equals(courseSection.getSectionType()) || "2".equals(courseSection.getSectionType()) || "3".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
					course.setH5cn("Y");
				}
			}else if("4".equals(courseSection.getSectionType()) || "6".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setH5cn("Y");
				}
			}else if("5".equals(courseSection.getSectionType()) || "7".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
				}
			}
		}
		if(sectionTypes.length()>0){
			course.setSectionTypes(sectionTypes.substring(1));
		}
		coursesMapper.updateByPrimaryKeySelective(course);
		//伪删除员工关联
//		TomCourses courses=coursesMapper.selectByPrimaryKey(course.getCourseId());
		TomCourseEmpRelation courseEmpRelationExample=new TomCourseEmpRelation();
		courseEmpRelationExample.setCourseId(course.getCourseId());
//		courseEmpRelationExample.setCreateTime(courses.getCreateTime());
		courseEmpRelationMapper.deleteByCourseId(courseEmpRelationExample);
		List<TomCourseEmpRelation> listEmp=new ArrayList<TomCourseEmpRelation>();
		for(String code:course.getEmpIds()){
			TomCourseEmpRelation courseEmpRelation=new TomCourseEmpRelation();
			courseEmpRelation.setCode(code);
			courseEmpRelation.setCourseId(course.getCourseId());
			courseEmpRelation.setCreateTime(course.getCreateTime());
			courseEmpRelation.setFinishStatus("N");
			courseEmpRelation.setStatus("Y");
			courseEmpRelation.setCreator(course.getCreator());
			courseEmpRelation.setOperator(course.getOperator());
			courseEmpRelation.setUpdateTime(course.getCreateTime());
			listEmp.add(courseEmpRelation);
		}
			if(listEmp.size()>0){
			courseEmpRelationMapper.insertList(listEmp);
			}
		/*courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
		if ("Y".equals(course.getCourseOnline())) {
			//更新员工关联关系
			TomCourseEmpRelation courseEmpRelation;
			for (String code : course.getEmpIds()) {
				courseEmpRelation = new TomCourseEmpRelation();
				courseEmpRelation.setCode(code);
				courseEmpRelation.setCourseId(course.getCourseId());
				List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper.selectByExample(courseEmpRelation);
				if(courseEmpRelations.size()==0){
					courseEmpRelation.setCreateTime(courses.getCreateTime());
					courseEmpRelation.setFinishStatus("N");
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelation.setCreator(course.getCreator());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelationMapper.insertSelective(courseEmpRelation);
				}else{
					courseEmpRelation=courseEmpRelations.get(0);
					courseEmpRelation.setStatus("Y");
					courseEmpRelation.setUpdateTime(new Date());
					courseEmpRelation.setOperator(course.getOperator());
					courseEmpRelationMapper.update(courseEmpRelation);
				}
				
			}
		}*/
		//删除课程分类
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		classifyRelationExample.setCourseId(course.getCourseId());
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(new Date());
			courseClassifyRelation.setUpdateTime(new Date());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		
		//修改轮播图名称
		Map<Object,Object> map=new HashMap<Object, Object>();
	    map.put("resourceId",course.getCourseId());
	    map.put("pictureCategory", "C");
	    map.put("resourceName", course.getCourseName());
	    rollingPictureMapper.updateResourceName(map);
	}
	/**
	 * 
	 * 功能描述：[添加线下课程]
	 *
	 * 创建者：wjx
	 * 创建时间: 2017年1月3日 下午2:40:26
	 * @param json
	 * @throws Exception
	 */
	@Transactional
	public void addOffline(String json) throws Exception{
		JsonMapper mapper = new JsonMapper();
		OfflineCourseDto dto=mapper.fromJson(json, OfflineCourseDto.class);
		this.addOffline(dto);
	}
	@Transactional
	public void addOffline(OfflineCourseDto dto)throws Exception{
		ShiroUser user=ShiroUtils.getCurrentUser();
		TomCourses course=new TomCourses();
		PropertyUtils.copyProperties(course,dto);
		course.setCreateTime(new Date());
		course.setCourseNumber(numberRecordService.getNumber(MapManager.numberType("KC")));
		course.setCreateTime(new Date());
		course.setUpdateTime(new Date());
		course.setThumbUpCount(0);
		course.setViewers(0);
		course.setCommentCount(0);
		course.setCourseAverage(0d);
		course.setTotScore(0d);
		course.setLecturerAverage(0d);
		course.setCreator(user.getName());
		course.setOperator(user.getName());
		course.setCreatorId(user.getCode());
		course.setPccn("N");
		course.setPcen("N");
		course.setH5cn("N");
		course.setH5en("N");
		course.setReapeatSign(dto.getReapeatSign());
		course.setOpenComment(dto.getNeedApply());//借用是否开启评论存储线下课程是否需要报名
		
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		
		coursesMapper.insertSelective(course);
		
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(course.getCreator());
		resource.setOperator(course.getOperator());
		resource.setProjectId(Integer.parseInt(course.getParentClassifyId()));
		resource.setResourceId(course.getCourseId());
		resource.setParentProjectClassifyName(course.getParentProjectClassifyName());
		resource.setStatus("Y");
		resource.setType("C");
		resource.setUpdateTime(new Date());
		tomProjectResourceMapper.insertSelective(resource);
		
		//存储章节子表
		String sectionTypes="";
		for(TomCourseSection courseSection:dto.getSectionList()){
			sectionTypes=sectionTypes+","+courseSection.getSectionType();
			if("1".equals(courseSection.getSectionType()) || "2".equals(courseSection.getSectionType()) || "3".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
					course.setH5cn("Y");
				}
			}else if("4".equals(courseSection.getSectionType()) || "6".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setH5cn("Y");
				}
			}else if("5".equals(courseSection.getSectionType()) || "7".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
				}
			}
			courseSection.setStatus("Y");
			courseSection.setCourseId(course.getCourseId());
			courseSection.setCreator(course.getCreator());
			courseSection.setCreatorId(course.getCreatorId());
			courseSection.setOperator(course.getOperator());
			courseSection.setCreateTime(new Date());
			courseSection.setUpdateTime(new Date());
			courseSectionMapper.insertSelective(courseSection);
		}
		if(sectionTypes.length()>0){
			course.setSectionTypes(sectionTypes.substring(1));
		}
		coursesMapper.updateByPrimaryKeySelective(course);
		//存储关联关系
		for(String code:course.getEmpIds()){
			TomCourseEmpRelation courseEmpRelation=new TomCourseEmpRelation();
			courseEmpRelation.setCode(code);
			courseEmpRelation.setCourseId(course.getCourseId());
			courseEmpRelation.setCreateTime(course.getCreateTime());
			courseEmpRelation.setFinishStatus("N");
			courseEmpRelation.setStatus("Y");
			courseEmpRelation.setCreator(course.getCreator());
			courseEmpRelation.setOperator(course.getOperator());
			courseEmpRelation.setUpdateTime(course.getCreateTime());
			courseEmpRelationMapper.insertSelective(courseEmpRelation);
		}
		//存储课程分类子表
		for(int classifyId:course.getClassifyIds()){
			TomCourseClassifyRelation courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(course.getCreateTime());
			courseClassifyRelation.setUpdateTime(course.getCreateTime());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		for(TomCourseClasses classes:dto.getClassesList()){
			String filePath1=Config.getProperty("file_path");
			SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
			String dataStr=simple.format(new Date());
			StringBuffer sb=new StringBuffer();
			sb.append(Config.getProperty("h5Index"))
				.append("views/task/offline_course.html?sign=true&courseId=")//待前端线下课程页面开发完补全
				.append(course.getCourseId())
				.append("&classId="+classes.getClasses());
			QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+course.getCourseId()+"签到二维码"+"-"+classes.getClasses(), filePath1 +"file"+ File.separator + "tdc"+ File.separator+dataStr);
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/"+dataStr+"/"+course.getCourseName()+course.getCourseId()+"签到二维码-");
			classes.setCourseId(course.getCourseId());
			classesMapper.insert(classes);
		}
		if(StringUtil.isNotEmpty(dto.getCourseDetail())){
			TomCourseDetail detail=new TomCourseDetail();
			detail.setCourseDetail(dto.getCourseDetail());
			detail.setCourseDetailEn(dto.getCourseDetailEn());
			detail.setCourseId(course.getCourseId());
			detailMapper.insert(detail);
		}
		/*String filePath1=Config.getProperty("file_path");
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String dataStr=simple.format(new Date());
		StringBuffer sb=new StringBuffer();
		sb.append(Config.getProperty("h5Index"))
			.append("views/task/offline_course.html?sign=true&courseId=")//待前端线下课程页面开发完补全
			.append(course.getCourseId());
		QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+course.getCourseId()+"签到二维码", filePath1 +"file"+ File.separator + "tdc"+ File.separator+dataStr);
		course.setSignInTwoDimensionCode("file" + "/tdc"+"/"+dataStr+"/"+course.getCourseName()+course.getCourseId()+"签到二维码"+".jpg");*/
		coursesMapper.updateByPrimaryKeySelective(course);
	}
	/**
	 * 
	 * 功能描述：[编辑线下课程]
	 *
	 * 创建者：wjx
	 * 创建时间: 2017年1月3日 下午2:50:36
	 * @param json
	 * @throws Exception
	 */
	@Transactional
	public void editOffline(String json) throws Exception{
		ShiroUser user=ShiroUtils.getCurrentUser();
		JsonMapper mapper = new JsonMapper();
		OfflineCourseDto dto=mapper.fromJson(json, OfflineCourseDto.class);
		TomCourses course=new TomCourses();
		PropertyUtils.copyProperties(course,dto);
		course.setUpdateTime(new Date());
		course.setOpenComment(dto.getNeedApply());//借用是否开启评论存储线下课程是否需要报名
		HashSet<Integer> set = classifyService.getFathers(course.getClassifyIds());
		String ids=",";
		for (int id : set) {  
		    ids+=id+",";
		}
		course.setCourseType(ids);
		course.setOperator(user.getName());
		coursesMapper.updateByPrimaryKeySelective(course);
		
		//项目名称分类关联
				TomProjectResource resource = new TomProjectResource();
				resource.setCreateTime(new Date());
				resource.setCreator(course.getCreator());
				resource.setOperator(course.getOperator());
				resource.setProjectId(Integer.parseInt(course.getParentClassifyId()));
				resource.setResourceId(course.getCourseId());
				resource.setStatus("Y");
				resource.setType("C");
				resource.setParentProjectClassifyName(course.getParentProjectClassifyName());
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
		
		//更新章节子表
		TomCourseSection courseSectionExample=new TomCourseSection();
		courseSectionExample.setStatus("N");
		courseSectionExample.setCourseId(course.getCourseId());
		courseSectionMapper.updateByPrimaryKeySelective(courseSectionExample);
		course.setPccn("N");
		course.setPcen("N");
		course.setH5cn("N");
		course.setH5en("N");
		course.setReapeatSign(dto.getReapeatSign());
		String sectionTypes="";
		for(TomCourseSection courseSection:dto.getSectionList()){
			sectionTypes=sectionTypes+","+courseSection.getSectionType();
			if(courseSection.getSectionId()!=-1){
				courseSection.setStatus("Y");
				courseSectionMapper.updateByPrimaryKeySelective(courseSection);
			}else{
				courseSection.setCourseId(course.getCourseId());
				courseSection.setCreator(course.getCreator());
				courseSection.setOperator(course.getOperator());
				courseSection.setStatus("Y");
				courseSectionMapper.insertSelective(courseSection);
			}
			if("1".equals(courseSection.getSectionType()) || "2".equals(courseSection.getSectionType()) || "3".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
					course.setH5cn("Y");
				}
			}else if("4".equals(courseSection.getSectionType()) || "6".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setH5en("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setH5cn("Y");
				}
			}else if("5".equals(courseSection.getSectionType()) || "7".equals(courseSection.getSectionType())){
				if("en".equals(courseSection.getLang())){
					course.setPcen("Y");
				}else if("cn".equals(courseSection.getLang())){
					course.setPccn("Y");
				}
			}
		}
			
		//伪删除员工关联
		TomCourses courses=coursesMapper.selectByPrimaryKey(course.getCourseId());
		TomCourseEmpRelation courseEmpRelationExample=new TomCourseEmpRelation();
		courseEmpRelationExample.setCourseId(course.getCourseId());
//		courseEmpRelationExample.setCreateTime(courses.getCreateTime());
//		courseEmpRelationMapper.deleteByExample(courseEmpRelationExample);
		courseEmpRelationMapper.deleteByCourseId(courseEmpRelationExample);
		
		//更新员工关联关系
		TomCourseEmpRelation courseEmpRelation;
		for (String code : course.getEmpIds()) {
			courseEmpRelation = new TomCourseEmpRelation();
			courseEmpRelation.setCode(code);
			courseEmpRelation.setCourseId(course.getCourseId());
			List<TomCourseEmpRelation> courseEmpRelations = courseEmpRelationMapper.selectByExample(courseEmpRelation);
			if(courseEmpRelations.size()==0){
				courseEmpRelation.setCreateTime(courses.getCreateTime());
				courseEmpRelation.setFinishStatus("N");
				courseEmpRelation.setStatus("Y");
				courseEmpRelation.setUpdateTime(new Date());
				courseEmpRelation.setCreator(course.getCreator());
				courseEmpRelation.setOperator(course.getOperator());
				courseEmpRelationMapper.insertSelective(courseEmpRelation);
			}else{
				courseEmpRelation=courseEmpRelations.get(0);
				courseEmpRelation.setStatus("Y");
				courseEmpRelation.setUpdateTime(new Date());
				courseEmpRelation.setOperator(course.getOperator());
				courseEmpRelationMapper.update(courseEmpRelation);
			}
			
		}
		
		//删除课程分类
		TomCourseClassifyRelation classifyRelationExample=new TomCourseClassifyRelation();
		classifyRelationExample.setCourseId(course.getCourseId());
		courseClassifyRelationMapper.deleteByExample(classifyRelationExample);
		//存储课程分类子表
		TomCourseClassifyRelation courseClassifyRelation;
		for(int classifyId:course.getClassifyIds()){
			courseClassifyRelation=new TomCourseClassifyRelation();
			courseClassifyRelation.setClassifyId(classifyId);
			courseClassifyRelation.setCourseId(course.getCourseId());
			courseClassifyRelation.setCreateTime(new Date());
			courseClassifyRelation.setUpdateTime(new Date());
			courseClassifyRelation.setStatus("Y");
			courseClassifyRelation.setCreator(course.getCreator());
			courseClassifyRelation.setOperator(course.getOperator());
			courseClassifyRelationMapper.insertSelective(courseClassifyRelation);
		}
		
		//修改轮播图名称
		Map<Object,Object> map=new HashMap<Object, Object>();
	    map.put("resourceId",course.getCourseId());
	    map.put("pictureCategory", "C");
	    map.put("resourceName", course.getCourseName());
	    rollingPictureMapper.updateResourceName(map);
	    if(dto.getClassesList().size()>0){
	    	classesMapper.deleteByCourseId(dto.getClassesList().get(0));
	    }
	    TomActivityProperty property = new TomActivityProperty();
	    property.setCourseId(course.getCourseId());
	    for(TomCourseClasses classes:dto.getClassesList()){
	    	if(1==classes.getClasses()){
	    		property.setStartTime(classes.getBeginTime());
	    	}else if(dto.getClassesList().size()==classes.getClasses()){
	    		property.setEndTime(classes.getEndTime());
	    	}
	    }
	    activityPropertyMapper.updateByCourseId(property);
	    for(TomCourseClasses classes:dto.getClassesList()){
	    	if(1==classes.getClasses()){
	    		property.setStartTime(classes.getBeginTime());
	    	}else if(dto.getClassesList().size()==classes.getClasses()){
	    		property.setEndTime(classes.getEndTime());
	    	}
	    	activityPropertyMapper.updateByCourseId(property);
	    	String filePath1=Config.getProperty("file_path");
			SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
			String dataStr=simple.format(new Date());
			StringBuffer sb=new StringBuffer();
			sb.append(Config.getProperty("h5Index"))
				.append("views/task/offline_course.html?sign=true&courseId=")//待前端线下课程页面开发完补全
				.append(course.getCourseId())
				.append("&classId="+classes.getClasses());
			QRCodeUtil.courseEncode(sb.toString(),course.getCourseName()+course.getCourseId()+"签到二维码-"+classes.getClasses(), filePath1 +"file"+ File.separator + "tdc"+ File.separator+dataStr);
			course.setSignInTwoDimensionCode("file" + "/tdc"+"/"+dataStr+"/"+course.getCourseName()+course.getCourseId()+"签到二维码-");
	    	classes.setCourseId(course.getCourseId());
			classesMapper.insert(classes);
		}
	    if(sectionTypes.length()>0){
			course.setSectionTypes(sectionTypes.substring(1));
		}
	    coursesMapper.updateByPrimaryKeySelective(course);
		if(StringUtil.isNotEmpty(dto.getCourseDetail())){
			Map<Object,Object> map1=new HashMap<Object, Object>();
			map1.put("courseId", course.getCourseId());
			TomCourseDetail detail=detailMapper.selectByCourseId(map1);
			if(detail == null){
				detail=new TomCourseDetail();
				detail.setCourseDetail(dto.getCourseDetail());
				detail.setCourseId(course.getCourseId());
				detail.setCourseDetailEn(dto.getCourseDetailEn());
				detailMapper.insert(detail);
			}else{
				detail.setCourseDetail(dto.getCourseDetail());
				detail.setCourseDetailEn(dto.getCourseDetailEn());
				detailMapper.updateByPrimaryKeySelective(detail);
			}
			
		}
	}
	@Transactional
	public void copyCourseOffline(Integer courseId)throws Exception{
		if(courseId == null)
			return ;
		OfflineCourseDto offlineCourseDto = this.findOffline(courseId);
		offlineCourseDto.setCourseName("Copy Of "+offlineCourseDto.getCourseName());
		offlineCourseDto.setCourseNameEn("Copy Of "+offlineCourseDto.getCourseNameEn());
		this.addOffline(offlineCourseDto);
	}
	/**
	 * 
	 * 功能描述：[查询线下课程]
	 *
	 * 创建者：wjx
	 * 创建时间: 2017年1月3日 下午3:48:05
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	public OfflineCourseDto findOffline(Integer courseId) throws Exception{
		OfflineCourseDto dto=new OfflineCourseDto();
		TomCourses courses= coursesMapper.selectByPrimaryKey(courseId);
		//封装章节信息
		TomCourseSection example=new TomCourseSection();
		example.setCourseId(courseId);
		List<TomCourseSection> courseSections=courseSectionService.selectListByEXample(example);
		List<Integer> sectionId=new ArrayList<Integer>();
		List<String> sectionNameList=new ArrayList<String>();
		List<String> sectionAddressList=new ArrayList<String>();
		List<String> fileUrlList=new ArrayList<String>();
		List<String> sectionType=new ArrayList<String>();
		List<Integer> sectionSize=new ArrayList<Integer>();
		for(TomCourseSection courseSection:courseSections){
			sectionId.add(courseSection.getSectionId());
			sectionNameList.add(courseSection.getSectionName());
			sectionAddressList.add(courseSection.getSectionAddress());
			fileUrlList.add(courseSection.getFileUrl() == null ? "" : courseSection.getFileUrl());
			sectionType.add(courseSection.getSectionType());
			sectionSize.add(courseSection.getSectionSize());
		}
		courses.setSectionAddressList(sectionAddressList);
		courses.setSectionId(sectionId);
		courses.setSectionNameList(sectionNameList);
		courses.setSectionType(sectionType);
		courses.setSectionSize(sectionSize);
		
		TomProjectResource project =new TomProjectResource();
		project.setResourceId(courseId);
		project.setType("C");
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(project);
		if(null!=selectByResource){
		courses.setParentProjectClassifyName(selectByResource.getParentProjectClassifyName());
		courses.setParentClassifyId(String.valueOf(selectByResource.getProjectId()));
		}
		//封装员工关联
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		map.put("createTime", courses.getCreateTime());
		map.put("status", "Y");
		List<TomEmp> emps = empMapper.selectListByCourseId(map);
		List<String> codes=new ArrayList<String>();
		List<String> names=new ArrayList<String>();
		List<String> citynames=new ArrayList<String>();
		List<String> deptnames=new ArrayList<String>();
		for(TomEmp emp:emps){
			codes.add(emp.getCode());
			names.add(emp.getName());
			citynames.add(emp.getCityname());
			deptnames.add(emp.getDeptname());
		}
		courses.setEmpIds(codes);
		courses.setEmpNames(names);
		courses.setCityname(citynames);
		courses.setDeptname(deptnames);
		//封装课程分类
		List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(courseId);
		List<Integer> classifyIds=new ArrayList<Integer>();
		List<String> classifyNames=new ArrayList<String>();
		for(TomCourseClassify courseClassify:courseClassifies){
			classifyIds.add(courseClassify.getClassifyId());
			classifyNames.add(courseClassify.getClassifyName());
		}
		courses.setClassifyIds(classifyIds);
		courses.setClassifyNames(classifyNames);
		PropertyUtils.copyProperties(dto,courses);
		dto.setNeedApply(courses.getOpenComment());
		dto.setSectionList(courseSections);
		dto.setClassesList(classesMapper.selectByCourseId(map));
		TomCourseDetail detail=detailMapper.selectByCourseId(map);
		if(detail!=null){
			dto.setCourseDetail(detail.getCourseDetail());
			dto.setCourseDetailEn(detail.getCourseDetailEn());
		}
		return dto;
	}
	public PageData<CourseApplyDto> findApplyList(Integer pageNum,Integer pageSize,Integer courseId){
		PageData<CourseApplyDto> page=new PageData<CourseApplyDto>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count=applyMapper.countApply(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		List<TomCourseApply> applyList=applyMapper.applyList(map);
		List<CourseApplyDto> pageList=new ArrayList<CourseApplyDto>();
		for(TomCourseApply apply:applyList){
			CourseApplyDto dto =new CourseApplyDto();
			dto.setApplyTime(apply.getApplyTime());
			dto.setCode(apply.getCode());
			dto.setName(empMapper.selectByPrimaryKeys(apply.getCode()).getName());
			map.put("classes", apply.getCourseClasses());
			dto.setClassesName(classesMapper.selectOne(map).getClassesName());
			pageList.add(dto);
		}
		page.setCount(count);
		page.setDate(pageList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	@Transactional
	public String importResults(int courseId, String filePath) throws Exception{
		String path=filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		if("xls".equals(path))
			return readXls(filePath,courseId);
		else if("xlsx".equals(path))
			return readXlsx(filePath, courseId);
		return "模板文件类型不正确。";
	}
	
	private String readXls(String filePath,int courseId) throws Exception{
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		List<TomCourseClasses> classList=classesMapper.selectByCourseId(map);
		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow!=null&&hssfRow.getCell(0) != null&&!"".equals(hssfRow.getCell(0))) {
					HSSFCell codeCell = hssfRow.getCell(0);
					HSSFCell classesCell = hssfRow.getCell(1);
					Integer classes=(int)Double.parseDouble(getValue(classesCell));
					String code=getValue(codeCell);
					if(classes>classList.size()){
						is.close(); 
						return "班级不存在";
					}
					TomEmp emp=empMapper.selectByPrimaryKey(code);
					if(emp==null){
						is.close(); 
						return "学员不存在";
					}
					map.put("courseId", courseId);
					map.put("code", code);
					map.put("courseClasses", classes);
					TomCourseApply apply=applyMapper.selectApply(map);
					if(apply==null){
						apply=new TomCourseApply();
						apply.setApplyTime(new Date());
						apply.setCode(code);
						apply.setCourseId(courseId);
						apply.setCourseClasses(classes);
						apply.setStatus("Y");
						applyMapper.insert(apply);
					}else if("N".equals(apply.getStatus())){
						apply.setStatus("Y");
						applyMapper.updateByPrimaryKeySelective(apply);
					}
				}
			}
		}
		is.close(); 
		return "ok";
	}
	private String readXlsx(String filePath,int courseId) throws Exception{
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("courseId", courseId);
		List<TomCourseClasses> classList=classesMapper.selectByCourseId(map);
		InputStream is = new FileInputStream(filePath);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow!=null&&xssfRow.getCell(0) != null&&!"".equals(xssfRow.getCell(0))) {
					XSSFCell codeCell = xssfRow.getCell(0);
					XSSFCell classesCell = xssfRow.getCell(1);
					Integer classes=(int)Double.parseDouble(getValue(classesCell));
					String code=getValue(codeCell);
					if(classes>classList.size()){
						is.close(); 
						return "班级不存在";
					}
					TomEmp emp=empMapper.selectByPrimaryKey(code);
					if(emp==null){
						is.close(); 
						return "学员不存在";
					}
					map.put("courseId", courseId);
					map.put("code", code);
					map.put("courseClasses", classes);
					TomCourseApply apply=applyMapper.selectApply(map);
					if(apply==null){
						apply=new TomCourseApply();
						apply.setApplyTime(new Date());
						apply.setCode(code);
						apply.setCourseId(courseId);
						apply.setCourseClasses(classes);
						apply.setStatus("Y");
						applyMapper.insert(apply);
					}else if("N".equals(apply.getStatus())){
						apply.setStatus("Y");
						applyMapper.updateByPrimaryKeySelective(apply);
					}
				}
			}
		}
		is.close(); 
		return "ok";
	}
	
	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:16
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
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:32
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
	/**
	 * 
	 * 功能描述：[前置任务]
	 *
	 * @return
	 * @throws Exception
	 */
	public void preTask(String code,List<TomActivityProperty> selectByCourseAndCode) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String localUrl = Config.getProperty("localUrl");
		String cExam = "";		//选修线下课程
		String cExamEn = "";
		String cExamStatus = "";		
		String beforeCOnline = "";
		String beforeCOnlineEn = "";
		String beforeCOnlineStatus = "";
		String downCourseBegin = ""; // 线下课程开始前
		String downCourseBeginEn = "";
		String downCourseBeginStatus = "";
		String lineCourseShift = "";//线下课程班次
		String lineCourseShiftEn = "";
		String lineCourseShiftStatus = "";
		String lineCourseShifts = "";//线下课程班次
		String lineCourseShiftEns = "";
		 List<TomMessageDetails> selectList = tomMessageDetailsMapper.selectList();
		 for(TomMessageDetails message:selectList){
			 if(message.getId()==31){
				 cExam = message.getMessage();
				 cExamStatus = message.getStatus();
			 }
			 if(message.getId()==32){
				 cExamEn = message.getMessage();
			 }
			 if (message.getId() == 29) {
				beforeCOnline = message.getMessage();
				beforeCOnlineStatus = message.getStatus();
			}
			 if (message.getId() == 30) {
				beforeCOnlineEn = message.getMessage();
			}
			 if (message.getId() == 25) {
					downCourseBegin = message.getMessage();
					downCourseBeginStatus = message.getStatus();
			}
			if (message.getId() == 26) {
				downCourseBeginEn = message.getMessage();
			}
			if (message.getId() == 58) {
				lineCourseShift = message.getMessage();
				lineCourseShiftStatus = message.getStatus();
			}
			if (message.getId() == 59) {
				lineCourseShiftEn = message.getMessage();
			}
		 }
		for(TomActivityProperty tomActivityProperty:selectByCourseAndCode){
			Map<Object,Object> map = new HashMap<Object,Object>(); 
			map.put("activityId", tomActivityProperty.getActivityId());
			map.put("pretask",tomActivityProperty.getSort());
			//根据排序查询后续任务
			List<TomActivityProperty> selectBySortMessqge = activityPropertyMapper.selectBySortMessqge(map);
			for(TomActivityProperty tomActivityProperty2:selectBySortMessqge){
				int count=0;
				String preTask = tomActivityProperty2.getPreTask();
				String[] split = preTask.split(",");
				for(String s:split){
					if(s.equals(tomActivityProperty.getSort()) ){
						count++;
					}else{
						TomActivityProperty propertySort = new TomActivityProperty();
						propertySort.setActivityId(tomActivityProperty2.getActivityId());
						propertySort.setSort(Integer.parseInt(s));
						List<TomActivityProperty> selectByExample = activityPropertyMapper.selectByExample(propertySort);
						TomActivityProperty tomActivityProperty3 = selectByExample.get(0);
						if(null!=tomActivityProperty3.getExamId()){
							//查询考试是否完成
							TomExamScore examScore = new TomExamScore();
							examScore.setCode(code);
							examScore.setExamId(tomActivityProperty3.getExamId());
							examScore.setGradeState("Y");
							List<TomExamScore> selectListByExample = tomExamScoreMapper.selectListByExample(examScore);
							if(selectListByExample.size()>0){
								count++;
							}else{
								break;
							}
						}else{
							//查询课程是否完成 
							TomCourses selectByPrimaryKey = coursesMapper.selectByPrimaryKey( tomActivityProperty3.getCourseId());
							if("Y".equals(selectByPrimaryKey.getCourseOnline())){
							Map<Object,Object> courseMap = new HashMap<Object,Object>();
							courseMap.put("courseId", tomActivityProperty3.getCourseId());
							courseMap.put("code",code);
							List<TomCourseLearningRecord> selectLearnRecord = courseLearningRecordMapper.selectLearnRecord(courseMap);
								if(selectLearnRecord.size()>0){
									count++;
								}else{
									break;
								}
						}else{
							Map<Object,Object> courseMap = new HashMap<Object,Object>();
							courseMap.put("courseId", tomActivityProperty3.getCourseId());
							courseMap.put("code",code);
							List<TomOfflineSign> selectSignRecord = tomOfflineSignMapper.selectSignRecord(courseMap);
								if(selectSignRecord.size()>0){
									count++;
								}else{
									break;
								}
							}
						}
					}
				}
				if(count==split.length ){//前置任务都完成了，发消息
					SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date=new Date();
					if(tomActivityProperty2.getExamId()!=null &&  "Y".equals(cExamStatus)){

						TomExam tomExam = tomExamMapper.selectByPrimaryKey(tomActivityProperty2.getExamId());
						TomExamPaper examPaper = tomExamPaperMapper.selectByExamId(tomActivityProperty2.getExamId());
						String cn =cExam.replace("<name>",  tomExam.getExamName()).replace("<beginTime>",simple.format(tomActivityProperty2.getStartTime())).replace("<endTime>",simple.format(tomActivityProperty2.getEndTime()));
						String en =cExamEn.replace("<name>",  tomExam.getExamNameEn()).replace("<beginTime>",simple.format(tomActivityProperty2.getStartTime())).replace("<endTime>",simple.format(tomActivityProperty2.getEndTime()));
						String examApp =cn+"/r/n"+en;
					String exam=Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty2.getExamId();
					String exameEmail =examApp+"<a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty2.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty2.getExamId()+"</a>";
					List<String> listEmp =  new ArrayList<String>();
					listEmp.add(code);
					List<String> listEmail = new ArrayList<String>();
					for(String code1:listEmp){
						TomEmp emp2 = empMapper.selectByPrimaryKey(code1);
						if(emp2.getSecretEmail()!=null)
						listEmail.add(emp2.getSecretEmail());
					}
					TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
					if(message.getSendToEmail().equals("Y")){
						SendMail sm= SendMailUtil.getMail(listEmail,"【蔚乐学】任务通知",date,"蔚乐学",exameEmail );
						sm.sendMessage();
						}
						if(message.getSendToApp().equals("Y")){
							List<WxMessage> listWx = new ArrayList<WxMessage>();
							WxMessage wx = new WxMessage(tomExam.getExamName(),examApp,exam,Config.getProperty("localUrl")+PathUtil.PROJECT_NAME+examPaper.getExamPaperPicture());
							listWx.add(wx);
							 String wxNewsMessage = sendMessageService.wxNewsMessage(listEmp, listWx);
							 List<String>	 listApp = sendMessageService.sendStatus(listEmp, wxNewsMessage);
								if(listApp.size()!=0){
									  sendMessageService.wxNewsMessage(listApp, listWx);
							}
						//sendMessageService.wxMessage(listEmp, exam);
						}
						DBContextHolder.setDbType(DBContextHolder.MASTER);
						TomMessages activityMessage = new TomMessages();
						activityMessage.setMessageTitle(tomActivityProperty2.getExamName());
						activityMessage.setMessageDetails(cn);
						activityMessage.setMessageTitleEn(tomExam.getExamNameEn());
						activityMessage.setMessageDetailsEn(en);
						activityMessage.setAppUrl(Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomActivityProperty2.getExamId());
						activityMessage.setPcUrl("<a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty2.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomActivityProperty2.getExamId()+"</a>");
						activityMessage.setEmpIds(listEmp);
						activityMessage.setMessageType("4");
						activityMessage.setCreateTime(date);
						activityMessage.setViewCount(0);
						if ("Y".equals(message.getSendToApp())) {
							activityMessage.setSendToApp("Y");
						} else {
							activityMessage.setSendToApp("N");
						}
						if ("Y".equals(message.getSendToEmail())) {
							activityMessage.setSendToEmail("Y");
						} else {
							activityMessage.setSendToEmail("N");
						}
						if ("Y".equals(message.getSendToPhone())) {
							activityMessage.setSendToPhone("Y");
						} else {
							activityMessage.setSendToPhone("N");
						}
						tomMessagesMapper.insertSelective(activityMessage);
						for (String code1 : listEmp) {
							TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
							tomMessagesEmployees.setCreateTime(date);
							tomMessagesEmployees.setEmpCode(code1);
							tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
							tomMessagesEmployees.setIsView("N");
							tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
						}
				
					}else if(tomActivityProperty2.getCourseId()!=null){
						
						TomActivity tomActivity = activityMapper.selectByPrimaryKey(tomActivityProperty.getActivityId());
						TomCourses tomCourses = coursesMapper.selectByPrimaryKey(tomActivityProperty2.getCourseId());
						if(tomCourses.getCourseOnline().equals("Y")&&"Y".equals(beforeCOnlineStatus)){
							String cn = beforeCOnline.replace("<name>", tomCourses.getCourseName())
									.replace("<beginTime>", simple.format(tomActivityProperty2.getStartTime()))
									.replace("<endTime>", simple.format(tomActivityProperty2.getEndTime()));
							String en = beforeCOnlineEn.replace("<name>", tomCourses.getCourseNameEn())
									.replace("<beginTime>", simple.format(tomActivityProperty2.getStartTime()))
									.replace("<endTime>", simple.format(tomActivityProperty2.getEndTime()));
							String selectTaskStart1 =cn+ "\r\n" + en;
							String selectTaskEndBx1 = selectTaskStart1 + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
							+ "views/course/course_learning.html?courseId=" + tomActivityProperty2.getCourseId() + "&activityId="
							+ tomActivityProperty2.getActivityId() + "\">" + Config.getProperty("pcIndex")
							+ "views/course/course_learning.html?courseId=" + tomActivityProperty2.getCourseId() + "&activityId="
							+ tomActivityProperty2.getActivityId() + "</a>";
							;
							String selectTaskEndBx2 = Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
									+ tomActivityProperty2.getCourseId();
							List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
									.selectByActivityId2(tomActivityProperty2.getActivityId());
							List<String> list3 = new ArrayList<String>();
							List<String> listEmail = new ArrayList<String>();
							TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
//							for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
//								if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
//									list3.add(tomActivityEmpsRelation.getCode());
//									TomEmp emps = empMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//									if (emps.getSecretEmail() != null)
//										listEmail.add(emps.getSecretEmail());
//								}
//							}
							list3.add(code);
							for(String code1:list3){
								TomEmp emps = empMapper.selectByPrimaryKey(code1);
								if(emps.getSecretEmail()!=null)
								listEmail.add(emps.getSecretEmail());
							}
							if (message.getSendToEmail().equals("Y")) {
								SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
								sm.sendMessage();
							}
							if (message.getSendToApp().equals("Y")) {
								List<WxMessage> wx = new ArrayList<WxMessage>();
								WxMessage setValue = new WxMessage(tomCourses.getCourseName(), selectTaskStart1, selectTaskEndBx2,
										localUrl+PathUtil.PROJECT_NAME+tomCourses.getCourseImg());
								wx.add(setValue);
								String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
								// String wxMessage = sendMessageService.wxMessage(list3,
								// selectTaskStart1);
								List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
								if (listApp.size() != 0) {
									sendMessageService.wxMessage(listApp, selectTaskStart1);
								}
							}
							TomMessages activityMessage = new TomMessages();
							activityMessage.setViewCount(0);
							if ("Y".equals(message.getSendToApp())) {
								activityMessage.setSendToApp("Y");
							} else {
								activityMessage.setSendToApp("N");
							}
							if ("Y".equals(message.getSendToEmail())) {
								activityMessage.setSendToEmail("Y");
							} else {
								activityMessage.setSendToEmail("N");
							}
							if ("Y".equals(message.getSendToPhone())) {
								activityMessage.setSendToPhone("Y");
							} else {
								activityMessage.setSendToPhone("N");
							}
							activityMessage.setMessageTitle(tomCourses.getCourseName());
							activityMessage.setMessageDetails(cn);
							activityMessage.setMessageTitleEn(tomCourses.getCourseNameEn());
							activityMessage.setMessageDetailsEn(en);
							activityMessage.setEmpIds(list3);
							activityMessage.setMessageType("0");
							activityMessage.setCreateTime(new Date());
							activityMessage.setAppUrl(selectTaskEndBx2); 
							activityMessage.setPcUrl( "<a href=\"" + Config.getProperty("pcIndex") 
							+ "views/course/course_learning.html?courseId=" + tomActivityProperty2.getCourseId() + "&activityId="
							+ tomActivityProperty2.getActivityId() + "\">" + Config.getProperty("pcIndex")
							+ "views/course/course_learning.html?courseId=" + tomActivityProperty2.getCourseId() + "&activityId="
							+ tomActivityProperty2.getActivityId() + "</a>");
							tomMessagesMapper.insertSelective(activityMessage);
							for (String code1 : list3) {
								TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
								tomMessagesEmployees.setCreateTime(new Date());
								tomMessagesEmployees.setEmpCode(code1);
								tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
								tomMessagesEmployees.setIsView("N");
								tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
							}
						}else if("Y".equals(lineCourseShiftStatus)){
							Map<Object,Object> map2 = new HashMap<Object,Object>(); 
							map2.put("courseId", tomActivityProperty2.getCourseId());
							map2.put("classes", 1);
							TomCourseClasses courseClasses = classesMapper.selectOne(map2);
							String selectTaskStart1 = lineCourseShift.replace("<name>", tomCourses.getCourseName())
									.replace("<beginTime>", simple.format(courseClasses.getBeginTime()))
									.replace("<address>", courseClasses.getClassesAddress())
									+ "\r\n"
									+ lineCourseShiftEn.replace("<name>", tomCourses.getCourseNameEn())
											.replace("<beginTime>", simple.format(courseClasses.getBeginTime()))
											.replace("<addressEn>", courseClasses.getClassesAddressEn());
							String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
							+ "views/course/offcourse_learning.html?courseId="+tomActivityProperty2.getCourseId()+"\">" + Config.getProperty("pcIndex")
							+ "views/course/offcourse_learning.html?courseId=" + tomActivityProperty2.getCourseId() + "</a>";
							String selectTaskStartApp = Config.getProperty("h5Index") + "views/task/offline_course.html?courseId="+tomActivityProperty2.getCourseId();
//							String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
//								+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//								+ "views/course/course_my.html" + "</a>";
//							String selectTaskStartApp = Config.getProperty("h5Index") + "views/user/my-course.html";
							List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
									.selectByActivityId2(tomActivityProperty2.getActivityId());
							List<String> list3 = new ArrayList<String>();
							List<String> listEmail = new ArrayList<String>();
							TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
//							for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
//								if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
//									list3.add(tomActivityEmpsRelation.getCode());
//									TomEmp emps = empMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//									if (emps.getSecretEmail() != null)
//										listEmail.add(emps.getSecretEmail());
//								}
//							}
							Map<Object,Object> map1 = new HashMap<Object,Object>();
							map1.put("courseId", tomCourses.getCourseId());
							map1.put("code", code);
							int coursed = courseApplyMapper.countEmp(map);
							if(coursed>0){
								list3.add(code);
								for(String code1:list3){
									TomEmp emps = empMapper.selectByPrimaryKey(code1);
									if(emps.getSecretEmail()!=null)
									listEmail.add(emps.getSecretEmail());
								}
							}
							if (message.getSendToEmail().equals("Y")) {
								SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
								sm.sendMessage();
							}
							if(list3.size()>0){
								if (message.getSendToApp().equals("Y")) {
									List<WxMessage> wx = new ArrayList<WxMessage>();
									WxMessage setValue = new WxMessage(tomCourses.getCourseName(), selectTaskStart1, selectTaskStartApp,
											localUrl+PathUtil.PROJECT_NAME+tomCourses.getCourseImg());
									wx.add(setValue);
									String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
									// String wxMessage = sendMessageService.wxMessage(list3,
									// selectTaskStart1);
									List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
									if (listApp.size() != 0) {
										sendMessageService.wxMessage(listApp, selectTaskStart1);
									}
								}
							}
							TomMessages activityMessage = new TomMessages();
							activityMessage.setViewCount(0);
							if ("Y".equals(message.getSendToApp())) {
								activityMessage.setSendToApp("Y");
							} else {
								activityMessage.setSendToApp("N");
							}
							if ("Y".equals(message.getSendToEmail())) {
								activityMessage.setSendToEmail("Y");
							} else {
								activityMessage.setSendToEmail("N");
							}
							if ("Y".equals(message.getSendToPhone())) {
								activityMessage.setSendToPhone("Y");
							} else {
								activityMessage.setSendToPhone("N");
							}
							activityMessage.setMessageTitle(tomCourses.getCourseName());
							activityMessage.setMessageDetails(lineCourseShift.replace("<name>", tomCourses.getCourseName())
									.replace("<beginTime>", simple.format(tomCourses.getBeginTime()))
									.replace("<address>", courseClasses.getClassesAddress()));
							activityMessage.setMessageTitleEn(tomCourses.getCourseNameEn());
							activityMessage.setMessageDetailsEn(lineCourseShiftEn.replace("<name>", tomCourses.getCourseNameEn())
									.replace("<beginTime>", simple.format(tomCourses.getBeginTime()))
									.replace("<addressEn>", courseClasses.getClassesAddressEn()));
							activityMessage.setEmpIds(list3);
							activityMessage.setMessageType("0");
							activityMessage.setCreateTime(new Date());
							activityMessage.setAppUrl(selectTaskStartApp);
							activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
									+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
									+ "views/course/course_my.html" + "</a>");
							tomMessagesMapper.insertSelective(activityMessage);
							for (String code1 : list3) {
								TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
								tomMessagesEmployees.setCreateTime(new Date());
								tomMessagesEmployees.setEmpCode(code1);
								tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
								tomMessagesEmployees.setIsView("N");
								tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 * 功能描述：[复制线上课程]
	 * 创建者：Acemon
	 * 创建时间：2017年7月25日
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void copyOnlineCourse(int courseId) throws Exception{
		CourseJsonDto courseDetail = this.selectCourseDetail(courseId);
		courseDetail.setCourseName("Copy of" + courseDetail.getCourseName());
		courseDetail.setCourseNameEn("Copy of" + courseDetail.getCourseNameEn());
		this.addCourseNew(courseDetail);
	}
}
