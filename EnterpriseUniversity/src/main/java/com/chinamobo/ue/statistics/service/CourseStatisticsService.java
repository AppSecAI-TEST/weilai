package com.chinamobo.ue.statistics.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Facet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseClassifyMapper;
import com.chinamobo.ue.course.dao.TomCourseCommentMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dto.TomEmpCourseDto;

import com.chinamobo.ue.course.dto.TomNeedLearnDto;
import com.chinamobo.ue.course.entity.TomCourseClassify;
import com.chinamobo.ue.course.entity.TomCourseComment;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.statistics.entity.TomAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomDetailedAttendanceStatistics;
import com.chinamobo.ue.statistics.entity.TomOpenCourseStatistic;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class CourseStatisticsService {
	
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;
	
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	
	@Autowired
	private TomCoursesMapper coursesMapper;
	
	@Autowired
	private TomActivityMapper activityMapper;
	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	@Autowired
	private TomCourseClassifyMapper courseClassifyMapper;
	@Autowired
	private TomCourseCommentMapper tomCourseCommentMapper;
	
	String filePath=Config.getProperty("file_path");
	/**
	 * 
	 * 功能描述：[课程导出数据查询]
	 *
	 * 创建者：lym
	 * 创建时间: 2017年2月13日
	 * @param pageNum
	 * @param pageSize
	 * @param attendanceStatistics
	 * @return
	 * @throws Exception 
	 */
	public List<TomAttendanceStatistics> queryAttendanceBycodes(String[] codes,String[] activeIds){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomAttendanceStatistics> list = new ArrayList<TomAttendanceStatistics>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("codes", codes);
		map.put("Ids", activeIds);
		List<TomActivityProperty> activityProperty = activityPropertyMapper.InquiryActivityBycodes(map);
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		for(TomActivityProperty activityPropertys : activityProperty){
			TomAttendanceStatistics attendanceStatistics = new TomAttendanceStatistics();
			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
			activityEmpsRelation.setActivityId(activityPropertys.getActivityId());
			activityEmpsRelation.setApplyStatus("Y");
			
			TomActivity activity = activityMapper.selectByPrimaryKey(activityPropertys.getActivityId());
			TomCourses courses = coursesMapper.selectByPrimaryKey(activityPropertys.getCourseId());
			attendanceStatistics.setActivityId(activityPropertys.getActivityId());
			attendanceStatistics.setCourseId(activityPropertys.getCourseId());
			attendanceStatistics.setActivityName(activity.getActivityName());
			attendanceStatistics.setCourseName(courses.getCourseName());
			attendanceStatistics.setCourseNumber(courses.getCourseNumber());
			if(activityPropertys.getStartTime()!=null){
				attendanceStatistics.setCourseStartTime(activityPropertys.getStartTime());
			}
			if(activityPropertys.getEndTime()!=null){
				attendanceStatistics.setCourseEndTime(activityPropertys.getEndTime());
			}
			
			attendanceStatistics.setToBe(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			Map<Object, Object> maps = new HashMap<Object, Object>();
			maps.put("activityId", activityPropertys.getActivityId());
			maps.put("courseId", activityPropertys.getCourseId());
			maps.put("learningDate", activityPropertys.getEndTime());
			if(courses.getCourseOnline().equals("Y")){
				attendanceStatistics.setCourseOnline("Y");
				attendanceStatistics.setTo(courseLearningRecordMapper.countByActivity(maps));
			}else{
				attendanceStatistics.setCourseOnline("N");
				attendanceStatistics.setTo(courseSignInRecordsMapper.countByExamples(maps));
			}
			if(attendanceStatistics.getToBe()!=0){
				attendanceStatistics.setPercentage(nf.format((double)attendanceStatistics.getTo()/(double)attendanceStatistics.getToBe()));
			}else{
				attendanceStatistics.setPercentage("0%");
			}
			
			list.add(attendanceStatistics);
		}
		return list;
	}
	/**
	 * 
	 * 功能描述：[课程分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月26日
	 * @param pageNum
	 * @param pageSize
	 * @param attendanceStatistics
	 * @return
	 * @throws Exception 
	 */
	public PageData<TomAttendanceStatistics> queryAttendanceStatistics(int pageNum, int pageSize,String courseName,String courseOnline,String beginTimeq,String endTimeq) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(courseName!=null){
			courseName=courseName.replaceAll("/", "//");
			courseName=courseName.replaceAll("%", "/%");
			courseName=courseName.replaceAll("_", "/_");
		}
		
		List<TomAttendanceStatistics> list = new ArrayList<TomAttendanceStatistics>();
		PageData<TomAttendanceStatistics> page = new PageData<TomAttendanceStatistics>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("beginTimeq",beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("courseName",courseName);
		map.put("courseOnline", courseOnline);
		//int count = courseSignInRecordsMapper.countByExample(courseSignInRecords);
		int count = activityPropertyMapper.countByExample(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityProperty> activityProperty = activityPropertyMapper.InquiryActivityCurriculum(map);
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		for(TomActivityProperty activityPropertys : activityProperty){
			TomAttendanceStatistics attendanceStatistics = new TomAttendanceStatistics();
			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
			activityEmpsRelation.setActivityId(activityPropertys.getActivityId());
			activityEmpsRelation.setApplyStatus("Y");
			
			TomActivity activity = activityMapper.selectByPrimaryKey(activityPropertys.getActivityId());
			if (activity == null) {
				continue;
			}
			
			TomCourses courses = coursesMapper.selectByPrimaryKey(activityPropertys.getCourseId());
			attendanceStatistics.setActivityId(activityPropertys.getActivityId());
			attendanceStatistics.setCourseId(activityPropertys.getCourseId());
			attendanceStatistics.setActivityName(activity.getActivityName());
			attendanceStatistics.setCourseName(courses.getCourseName());
			attendanceStatistics.setCourseNumber(courses.getCourseNumber());
			if(activityPropertys.getStartTime()!=null){
				attendanceStatistics.setCourseStartTime(activityPropertys.getStartTime());
			}
			if(activityPropertys.getEndTime()!=null){
				attendanceStatistics.setCourseEndTime(activityPropertys.getEndTime());
			}
			
			attendanceStatistics.setToBe(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			Map<Object, Object> maps = new HashMap<Object, Object>();
			maps.put("activityId", activityPropertys.getActivityId());
			maps.put("courseId", activityPropertys.getCourseId());
			maps.put("learningDate", activityPropertys.getEndTime());
			if(courses.getCourseOnline().equals("Y")){
				attendanceStatistics.setCourseOnline("Y");
				attendanceStatistics.setTo(courseLearningRecordMapper.countByActivity(maps));
			}else{
				attendanceStatistics.setCourseOnline("N");
				attendanceStatistics.setTo(courseSignInRecordsMapper.countByExamples(maps));
			}
			if(attendanceStatistics.getToBe()!=0){
				attendanceStatistics.setPercentage(nf.format((double)attendanceStatistics.getTo()/(double)attendanceStatistics.getToBe()));
			}else{
				attendanceStatistics.setPercentage("0%");
			}
			
			list.add(attendanceStatistics);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	
//public PageData<TomAttendanceStatistics> queryAttendanceStatistics(){
//		List<TomAttendanceStatistics> list = new ArrayList<TomAttendanceStatistics>();
//		PageData<TomAttendanceStatistics> page = new PageData<TomAttendanceStatistics>();
//		Map<Object, Object> map = new HashMap<Object, Object>();
//		//int count = courseSignInRecordsMapper.countByExample(courseSignInRecords);
//		int count = activityPropertyMapper.countByExample(map);
//		int pageSize = count;
//		int pageNum = 1;
//		map.put("startNum", (pageNum - 1) * pageSize);
//		map.put("endNum", pageNum * pageSize);
//		List<TomActivityProperty> activityProperty = activityPropertyMapper.InquiryActivityCurriculum(map);
//		NumberFormat nf = NumberFormat.getPercentInstance();
//        nf.setMaximumFractionDigits(2);
//		for(TomActivityProperty activityPropertys : activityProperty){
//			TomAttendanceStatistics attendanceStatistics = new TomAttendanceStatistics();
//			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
//			activityEmpsRelation.setActivityId(activityPropertys.getActivityId());
//			activityEmpsRelation.setApplyStatus("Y");
//			
//			TomActivity activity = activityMapper.selectByPrimaryKey(activityPropertys.getActivityId());
//			TomCourses courses = coursesMapper.selectByPrimaryKey(activityPropertys.getCourseId());
//			attendanceStatistics.setActivityId(activityPropertys.getActivityId());
//			attendanceStatistics.setCourseId(activityPropertys.getCourseId());
//			attendanceStatistics.setActivityName(activity.getActivityName());
//			attendanceStatistics.setCourseName(courses.getCourseName());
//			attendanceStatistics.setCourseNumber(courses.getCourseNumber());
//			attendanceStatistics.setCourseStartTime(activityPropertys.getStartTime());
//			attendanceStatistics.setCourseEndTime(activityPropertys.getEndTime());
//			attendanceStatistics.setToBe(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
//			Map<Object, Object> maps = new HashMap<Object, Object>();
//			maps.put("activityId", activityPropertys.getActivityId());
//			maps.put("courseId", activityPropertys.getCourseId());
//			maps.put("learningDate", activityPropertys.getEndTime());
//			if(courses.getCourseOnline().equals("Y")){
//				attendanceStatistics.setCourseOnline("Y");
//				attendanceStatistics.setTo(courseLearningRecordMapper.countByActivity(maps));
//			}else{
//				attendanceStatistics.setCourseOnline("N");
//				attendanceStatistics.setTo(courseSignInRecordsMapper.countByExamples(maps));
//			}
//			if(attendanceStatistics.getToBe()!=0){
//				attendanceStatistics.setPercentage(nf.format((double)attendanceStatistics.getTo()/(double)attendanceStatistics.getToBe()));
//			}else{
//				attendanceStatistics.setPercentage("0%");
//			}
//			
//			list.add(attendanceStatistics);
//		}
//		
//		page.setDate(list);
//		page.setPageNum(pageNum);
//		page.setPageSize(pageSize);
//		page.setCount(count);
//		return page;
//	}
	
	/**
	 * 
	 * 功能描述：[导出课程统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @param topics
	 * @param
	 * @return
	 */
	public String AttendanceStatisticsExcel(List<TomAttendanceStatistics> attendanceStatistics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("课程编号");
		headers.add("课程名称");
		headers.add("所属活动");
		headers.add("类型");
		headers.add("课程开始时间");
		headers.add("课程结束时间");
		headers.add("应学人数");
		headers.add("完成人数");
		headers.add("完成率");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < attendanceStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomAttendanceStatistics attendanceStatisticss = attendanceStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(attendanceStatisticss.getCourseNumber());
            row.createCell((short) 1).setCellValue(attendanceStatisticss.getCourseName());
            row.createCell((short) 2).setCellValue(attendanceStatisticss.getActivityName());
            if(attendanceStatisticss.getCourseOnline().equals("Y")){
            	row.createCell((short) 3).setCellValue("线上");
            }else{
            	row.createCell((short) 3).setCellValue("线下");
            }
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(attendanceStatisticss.getCourseStartTime()!=null){
            	row.createCell((short) 4).setCellValue(format2.format(attendanceStatisticss.getCourseStartTime()));
            }else {
            	row.createCell((short) 4).setCellValue("-");
            }
            if(attendanceStatisticss.getCourseEndTime()!=null){
            	row.createCell((short) 5).setCellValue(format2.format(attendanceStatisticss.getCourseEndTime()));
            }else{
            	row.createCell((short) 5).setCellValue("-");
            }
            row.createCell((short) 6).setCellValue(attendanceStatisticss.getToBe());
            row.createCell((short) 7).setCellValue(attendanceStatisticss.getTo());
            row.createCell((short) 8).setCellValue(attendanceStatisticss.getPercentage());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	
	/**
	 * 
	 * 功能描述：[查看课程学习统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月2日
	 * @param
	 * @param
	 * @return
	 */
	public PageData<TomDetailedAttendanceStatistics> seeAttendanceStatistics(int pageNum, int pageSize, int activityId,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomDetailedAttendanceStatistics> lists = new ArrayList<TomDetailedAttendanceStatistics>();
		PageData<TomDetailedAttendanceStatistics> page = new PageData<TomDetailedAttendanceStatistics>();
		TomCourses course=coursesMapper.selectByPrimaryKey(courseId);
		
		TomActivityProperty activityPropertyExample=new TomActivityProperty();
		activityPropertyExample.setActivityId(activityId);
		activityPropertyExample.setCourseId(courseId);
		TomActivityProperty activityProperty=activityPropertyMapper.selectByExample(activityPropertyExample).get(0);
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("activityId",activityId);
		map.put("applyStatus", "Y");
		TomActivityEmpsRelation activityEmpsRelationss = new TomActivityEmpsRelation();
		activityEmpsRelationss.setActivityId(activityId);
		activityEmpsRelationss.setApplyStatus("Y");
		int count = activityEmpsRelationMapper.countByExample(activityEmpsRelationss);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityEmpsRelation> activityEmpsRelation = activityEmpsRelationMapper.selectListExample(map);
		for(TomActivityEmpsRelation activityEmps : activityEmpsRelation){
			TomDetailedAttendanceStatistics detailedAttendanceStatistics = new TomDetailedAttendanceStatistics();
			TomEmp emp=empMapper.selectByPrimaryKey(activityEmps.getCode());
			
			detailedAttendanceStatistics.setCode(activityEmps.getCode());
			detailedAttendanceStatistics.setName(activityEmps.getName());
			detailedAttendanceStatistics.setDeptName(emp.getDeptname());
			detailedAttendanceStatistics.setOneDeptName(emp.getOnedeptname());
			if(course.getCourseOnline().equals("Y")){
				TomCourseLearningRecord example=new TomCourseLearningRecord();
				example.setCourseId(courseId);
				example.setCode(activityEmps.getCode());
				example.setLearningDate(activityProperty.getEndTime());
				Map<Object, Object> map1 = new HashMap<Object, Object>();
				map1.put("courseId", courseId);
				map1.put("code", activityEmps.getCode());
				map1.put("learningDate", activityProperty.getEndTime());
				if(courseLearningRecordMapper.countByExample(example)!=0){
					detailedAttendanceStatistics.setAttendance("是");
					detailedAttendanceStatistics.setSignTime(format.format(courseLearningRecordMapper.selectLearnRecord(map1).get(0).getLearningDate()));
				}else{
					detailedAttendanceStatistics.setAttendance("否");
					detailedAttendanceStatistics.setSignTime("-");
				}
			}else{
				TomCourseSignInRecords courseSignInRecords = new TomCourseSignInRecords();
				courseSignInRecords.setCourseId(courseId);
				courseSignInRecords.setCode(activityEmps.getCode());
				courseSignInRecords.setCreateDate(activityProperty.getEndTime());
				if(courseSignInRecordsMapper.countByExample(courseSignInRecords)!=0){
					detailedAttendanceStatistics.setAttendance("是");
					detailedAttendanceStatistics.setSignTime(format.format(courseSignInRecordsMapper.selectByExample(courseSignInRecords).get(0).getCreateDate()));
				}else{
					detailedAttendanceStatistics.setAttendance("否");
					detailedAttendanceStatistics.setSignTime("-");
				}
			}
			
			lists.add(detailedAttendanceStatistics);
		}
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[导出签到详细统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月3日
	 * @param topics
	 * @param
	 * @return
	 */
	public String AttendanceStatisticsDetailedExcel(List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("一级部门");
		headers.add("完成");
		headers.add("完成时间");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < detailedAttendanceStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomDetailedAttendanceStatistics detailedAttendanceStatisticss = detailedAttendanceStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(detailedAttendanceStatisticss.getName());
            row.createCell((short) 1).setCellValue(detailedAttendanceStatisticss.getCode());
            row.createCell((short) 2).setCellValue(detailedAttendanceStatisticss.getDeptName());
            row.createCell((short) 3).setCellValue(detailedAttendanceStatisticss.getOneDeptName());
            row.createCell((short) 4).setCellValue(detailedAttendanceStatisticss.getAttendance());
//            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String activitySignTime = format2.format(detailedAttendanceStatisticss.getSignTime());
            row.createCell((short) 5).setCellValue(detailedAttendanceStatisticss.getSignTime());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}


	/**
	 * 
	 * 功能描述：[公开课统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 上午11:26:10
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @return
	 * @throws Exception 
	 */
	public PageData<TomOpenCourseStatistic> openCourseStatistics(int pageNum, int pageSize,String courseName) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(courseName!=null){
			courseName=courseName.replaceAll("/", "//");
			courseName=courseName.replaceAll("%", "/%");
			courseName=courseName.replaceAll("_", "/_");
		}
		
		PageData<TomOpenCourseStatistic> page=new PageData<TomOpenCourseStatistic>();
		List<TomOpenCourseStatistic> list=new ArrayList<TomOpenCourseStatistic>();
		
		TomCourses example=new TomCourses();
		example.setCourseName(courseName);
		example.setCourseOnline("Y");
		
		int count=coursesMapper.countByExample(example);
		if(pageSize==-1){
			pageSize = count;
		}
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		map.put("listOrder", "c.UPDATE_TIME");
		map.put("orderBy", "desc");
		List<TomCourses> courses=coursesMapper.selectListByPage(map);
		
		TomCourseLearningRecord learningRecordExample;
		for(TomCourses course:courses){
			TomOpenCourseStatistic openCourseStatistic=new TomOpenCourseStatistic();
			openCourseStatistic.setCourseId(course.getCourseId());
			openCourseStatistic.setCourseNumber(course.getCourseNumber());
			openCourseStatistic.setCourseName(course.getCourseName());	
			learningRecordExample=new TomCourseLearningRecord();
			learningRecordExample.setCourseId(course.getCourseId());
			openCourseStatistic.setLearningNumber(courseLearningRecordMapper.countByExample(learningRecordExample));
			openCourseStatistic.setOpennum(course.getOpennum());
			openCourseStatistic.setOpenCourse(course.getOpenCourse());
			if(null==course.getViewers()){
				course.setViewers(0);
			}
			openCourseStatistic.setViewers(course.getViewers());
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(course.getCourseId());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			openCourseStatistic.setCourseType(courseType);
			map.put("course_id", course.getCourseId());
			List<TomEmpCourseDto> courseDateList = coursesMapper.selectCourseDateProperty(map);
			if (courseDateList.size()>0) {
				openCourseStatistic.setStartTime(courseDateList.get(0).getStartTime());
				openCourseStatistic.setEndTime(courseDateList.get(0).getEndTime());
				openCourseStatistic.setIsRequired(courseDateList.get(0).getIsRequired());
			}
			
			list.add(openCourseStatistic);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}

	/**
	 * 
	 * 功能描述：[公开课学习人员统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:31:31
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	public PageData<TomDetailedAttendanceStatistics> openCourseLearnStatistics(int pageNum, int pageSize,int courseId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomDetailedAttendanceStatistics> page=new PageData<TomDetailedAttendanceStatistics>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		TomCourseLearningRecord example=new TomCourseLearningRecord();
		example.setCourseId(courseId);
		int count=courseLearningRecordMapper.countByExample(example);
		if(pageSize==-1){
			pageSize = count;
		}
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomCourseLearningRecord> courseLearningRecords=courseLearningRecordMapper.selectByPage(map);
		
		
		List<TomDetailedAttendanceStatistics> list=new ArrayList<TomDetailedAttendanceStatistics>();
		for(TomCourseLearningRecord courseLearningRecord:courseLearningRecords){
			TomEmp emp=empMapper.selectByPrimaryKey(courseLearningRecord.getCode());
			TomDetailedAttendanceStatistics detailedAttendanceStatistics=new TomDetailedAttendanceStatistics();
			detailedAttendanceStatistics.setCode(emp.getCode());
			detailedAttendanceStatistics.setName(emp.getName());
			detailedAttendanceStatistics.setDeptName(emp.getDeptname());
			detailedAttendanceStatistics.setOneDeptName(emp.getOnedeptname());
			detailedAttendanceStatistics.setEmail(emp.getSecretEmail());
			detailedAttendanceStatistics.setPhoneNumber(emp.getMobile());
			detailedAttendanceStatistics.setSignTime(format.format(courseLearningRecord.getLearningDate()));
			
			list.add(detailedAttendanceStatistics);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	public PageData<TomCourseLearningRecord> offlineCourseSignStatistics(int pageNum, int pageSize,int courseId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomCourseLearningRecord> page=new PageData<TomCourseLearningRecord>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("courseId", courseId);
		int count=coursesMapper.countSignCourseSection(map);
		if(pageSize==-1){
			pageSize = count;
		}
		
		
		map.put("courseId", courseId);
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomCourseLearningRecord> courseLearningRecords=coursesMapper.selectSignCourseSection(map);
		page.setDate(courseLearningRecords);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 
	 * 功能描述：[公开课统计课程列表下载]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午12:53:09
	 * @param openCourseStatistics
	 * @param fileName
	 * @return
	 */
	public String openCourseStatisticsExcel(List<TomOpenCourseStatistic> openCourseStatistics, String fileName) {
		List<String> headers=new ArrayList<String>();
		headers.add("课程编号");
		headers.add("课程名称");
		headers.add("课程分类");
		headers.add("完成人数");
		headers.add("开放人数");
		headers.add("浏览人数");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < openCourseStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomOpenCourseStatistic openCourseStatistic = openCourseStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(openCourseStatistic.getCourseNumber());
            row.createCell((short) 1).setCellValue(openCourseStatistic.getCourseName());
            row.createCell((short) 2).setCellValue(openCourseStatistic.getCourseType());
            row.createCell((short) 3).setCellValue(openCourseStatistic.getLearningNumber());
            if("N".equals(openCourseStatistic.getOpenCourse())){
            	row.createCell((short) 4).setCellValue(openCourseStatistic.getOpennum());
            }else{
            	row.createCell((short) 4).setCellValue("-");
            }
            row.createCell((short) 5).setCellValue(openCourseStatistic.getViewers());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	//线下课程导出
	public String offlineCourseStatisticsExcel(List<TomOpenCourseStatistic> openCourseStatistics, String fileName) {
		List<String> headers=new ArrayList<String>();
		headers.add("课程编号");
		headers.add("课程名称");
		headers.add("课程分类");
		headers.add("开始时间");
		headers.add("结束时间");
		headers.add("是否必修");
		headers.add("签到人数");
		headers.add("应学人数");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < openCourseStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomOpenCourseStatistic openCourseStatistic = openCourseStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(openCourseStatistic.getCourseNumber());
            row.createCell((short) 1).setCellValue(openCourseStatistic.getCourseName());
            row.createCell((short) 2).setCellValue(openCourseStatistic.getCourseType());
            row.createCell((short) 3).setCellValue(openCourseStatistic.getStartTime());
            row.createCell((short) 4).setCellValue(openCourseStatistic.getEndTime());
            row.createCell((short) 5).setCellValue(openCourseStatistic.getIsRequired());
            row.createCell((short) 6).setCellValue(openCourseStatistic.getCommentCount());
            row.createCell((short) 7).setCellValue(openCourseStatistic.getOpenComment());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	/**
	 * 
	 * 功能描述：[公开课学习记录导出]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 下午1:18:21
	 * @param detailedAttendanceStatistics
	 * @param fileName
	 * @return
	 */
	public String openCourseLearnExcel(List<TomDetailedAttendanceStatistics> detailedAttendanceStatistics,String fileName) {
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("一级部门");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("完成时间");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < detailedAttendanceStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomDetailedAttendanceStatistics detailedAttendanceStatisticss = detailedAttendanceStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(detailedAttendanceStatisticss.getName());
            row.createCell((short) 1).setCellValue(detailedAttendanceStatisticss.getCode());
            row.createCell((short) 2).setCellValue(detailedAttendanceStatisticss.getDeptName());
            row.createCell((short) 3).setCellValue(detailedAttendanceStatisticss.getOneDeptName());
            row.createCell((short) 4).setCellValue(detailedAttendanceStatisticss.getEmail());
            row.createCell((short) 5).setCellValue(detailedAttendanceStatisticss.getPhoneNumber());
            row.createCell((short) 6).setCellValue(detailedAttendanceStatisticss.getSignTime());
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}

	public  PageData<TomEmpCourseDto> selectempidByPage(int pageNum, int pageSize,String code){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomEmpCourseDto> page=new PageData<TomEmpCourseDto>();	
		List<TomEmpCourseDto> list;
		List<TomEmpCourseDto> finallist =new ArrayList<TomEmpCourseDto>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		map.put("code", code);
		map.put("listOrder", "UPDATE_TIME");
		map.put("orderBy", "desc");
		int count=coursesMapper.countempCourseListByPage(map);
		
		if(pageSize==-1){
			pageSize=count;
		}
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		list = coursesMapper.selectempCourseListByPage(map);
		for(TomEmpCourseDto course : list){
			
			map.put("course_id", course.getCourseId());
			List<TomEmpCourseDto> protieslist = coursesMapper.selectempCourseProtice(map);
			if(protieslist.size()>0){
				course.setStartTime(protieslist.get(0).getStartTime());
				course.setEndTime(protieslist.get(0).getEndTime());
				course.setIsRequired(protieslist.get(0).getIsRequired());
			}
			String names = "";
			String[] types = course.getCourseType().split(",");
			for(int i=0;i<types.length;i++){
				if(!"".equals(types[i])){
					TomCourseClassify  classify = courseClassifyMapper.selectByPrimaryKey(Integer.valueOf(types[i]));
					if(i==types.length-1){
						names+=classify.getClassifyName();
					}else{
						names+=classify.getClassifyName()+",";
					}
				}
			}
			course.setCourseTypeName(names);
			if(course.getSumStation()!=null && course.getFinishStation()!=null && course.getSumStation()!=0){
				Double s =course.getFinishStation()/course.getSumStation();
				if(s>1){
					course.setCoursePlan("100%");
				}else{
					course.setCoursePlan(nf.format(s));
				}
			}else{
				course.setCoursePlan("0%");
			}
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[导出学员课程详细统计]
	 *
	 * 创建者：LYM
	 * 创建时间: 2017年1月11日
	 * @param topics
	 * @param
	 * @return
	 */
	public String empCourseStatisticsDetailedExcel(List<TomEmpCourseDto> courselist,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("学习课程");
		headers.add("课程分类");
		headers.add("课程类型");
		headers.add("课程开始时间");
		headers.add("课程结束时间");
		headers.add("标签");
		headers.add("课程是否完成");
		headers.add("完成进度");
		headers.add("课程完成时间");
		headers.add("本课程在线时长");
		headers.add("课程评分");
		headers.add("评论内容");
		headers.add("点赞");
		headers.add("收藏");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        
        for (int i = 0; i < courselist.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomEmpCourseDto detailedAttendanceStatisticss = courselist.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(detailedAttendanceStatisticss.getCourseName());
            row.createCell((short) 1).setCellValue(detailedAttendanceStatisticss.getCourseTypeName());
            if("Y".equals(detailedAttendanceStatisticss.getCourseOnline())){
           	 	row.createCell((short) 2).setCellValue("线上");
            }else{
            	row.createCell((short) 2).setCellValue("线下");
            }
            row.createCell((short) 3).setCellValue(detailedAttendanceStatisticss.getStartTime());
            row.createCell((short) 4).setCellValue(detailedAttendanceStatisticss.getEndTime());
            if("Y".equals(detailedAttendanceStatisticss.getIsRequired())){
            	 row.createCell((short) 5).setCellValue("必修");
            }else{
            	row.createCell((short) 5).setCellValue("非必修");
            }
            if("Y".equals(detailedAttendanceStatisticss.getFinishStatus())){
            	row.createCell((short) 6).setCellValue("完成");
            }else{
        	   row.createCell((short) 6).setCellValue("未完成");
            }
            row.createCell((short) 7).setCellValue(detailedAttendanceStatisticss.getCoursePlan());
            
            row.createCell((short) 8).setCellValue(detailedAttendanceStatisticss.getFinishTime());
            row.createCell((short) 9).setCellValue(detailedAttendanceStatisticss.getInLinetime());
            row.createCell((short) 10).setCellValue(detailedAttendanceStatisticss.getCourseGrade());
            row.createCell((short) 11).setCellValue(detailedAttendanceStatisticss.getCommentContent());
            
            if("Y".equals(detailedAttendanceStatisticss.getThumbStatus())){
            	row.createCell((short) 12).setCellValue("是");
            }else{
        	   row.createCell((short) 12).setCellValue("否");
            }
            if("Y".equals(detailedAttendanceStatisticss.getStatus())){
            	row.createCell((short) 13).setCellValue("是");
            }else{
        	   row.createCell((short) 13).setCellValue("否");
            }
//            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String activitySignTime = format2.format(detailedAttendanceStatisticss.getSignTime());
            
        }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
//        		System.out.println(File.separator);
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}


	/**
	 * 功能描述：[查询应该学习课程名单]
	 * 创建时间：2017-01-10
	 */
	public PageData<TomNeedLearnDto> viewCourseNeedStatistics(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countCourseNeedStatistics(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> needStatistics = coursesMapper.selectCourseNeedStatistics(map);
		List<TomNeedLearnDto> list = new ArrayList<TomNeedLearnDto>();
		for (TomNeedLearnDto tomNeedLearnDto : needStatistics) {
			TomEmp emp=empMapper.selectByPrimaryKey(tomNeedLearnDto.getCode());
			tomNeedLearnDto.setCode(emp.getCode());
			tomNeedLearnDto.setName(emp.getName());
			tomNeedLearnDto.setDeptname(emp.getDeptname());
			tomNeedLearnDto.setJobname(emp.getJobname());
			tomNeedLearnDto.setSecretEmail(emp.getSecretEmail());
			tomNeedLearnDto.setMobile(emp.getMobile());
			if("N".equals(tomNeedLearnDto.getStatusf())){
				tomNeedLearnDto.setStatusf("否");
			}else if("Y".equals(tomNeedLearnDto.getStatusf())) {
				tomNeedLearnDto.setStatusf("是");
			}
			if("N".equals(tomNeedLearnDto.getStatust())){
				tomNeedLearnDto.setStatust("否");
			}else if("Y".equals(tomNeedLearnDto.getStatust())) {
				tomNeedLearnDto.setStatust("是");
			}
			list.add(tomNeedLearnDto);
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
		
	}
	/**
	 * 功能描述：[查询线下课程应该学习名单]
	 * 创建时间：2017-01-10
	 */
	public PageData<TomNeedLearnDto> viewCourseCanlearnStatistics(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countCourseCanlearnStatistics(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> needStatistics = coursesMapper.selectCourseCanleranStatistics(map);
		page.setDate(needStatistics);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
		
	}
	public PageData<TomNeedLearnDto> viewCourseSignStatistics(int pageNum, int pageSize,int courseId,String topDept){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		map.put("topDept", topDept);
		int count = coursesMapper.countCourseSignStatistics(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> needStatistics = coursesMapper.selectCourseSignStatistics(map);
		page.setDate(needStatistics);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
		
	}
	/**
	 * 功能描述：[导出应该学习课程人员名单]
	 * 创建日期：2017-01-11
	 */
	public String downloadViewCourseNeedStatistics(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("是否收藏");
		headers.add("是否点赞");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            row.createCell((short) 6).setCellValue(tom.getStatusf());
            row.createCell((short) 7).setCellValue(tom.getStatust());
            
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	
	/**
	 * 功能描述：[查询点赞人员名单]
	 * 创建时间：2017-01-11
	 */
	public PageData<TomNeedLearnDto> viewCourseThumbUpStatistic(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countCourseThumbUpStatistics(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> courseThumbUpStatistics = coursesMapper.selectCourseThumbUpStatistics(map);
		List<TomNeedLearnDto> list = new ArrayList<TomNeedLearnDto>();
		for (TomNeedLearnDto tomNeedLearnDto : courseThumbUpStatistics) {
			TomEmp emp=empMapper.selectByPrimaryKey(tomNeedLearnDto.getCode());
			tomNeedLearnDto.setCode(emp.getCode());
			tomNeedLearnDto.setName(emp.getName());
			tomNeedLearnDto.setStatus("Y");
			list.add(tomNeedLearnDto);
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 功能描述：[导出点赞人员名单]
	 * 创建时间：2017-01-11
	 */
	public String downloadViewCourseThumbUpStatistic(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("点赞时间");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            row.createCell((short) 6).setCellValue(tom.getCreateTime());
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
		
	}
	/**
	 * 功能描述：[查询课程收藏名单]
	 * 创建时间：2017-01-11
	 */
	public PageData<TomNeedLearnDto> viewFavouriteCourseStatistic(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countFavouriteCourseStatistics(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> favouriteCourseStatistics = coursesMapper.selectFavouriteCourseStatistics(map);
		List<TomNeedLearnDto> list = new ArrayList<TomNeedLearnDto>();
		for (TomNeedLearnDto tomNeedLearnDto : favouriteCourseStatistics) {
			TomEmp emp=empMapper.selectByPrimaryKey(tomNeedLearnDto.getCode());
			tomNeedLearnDto.setCode(emp.getCode());
			tomNeedLearnDto.setName(emp.getName());
			tomNeedLearnDto.setStatus("是");
			list.add(tomNeedLearnDto);
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 功能描述：[导出收藏人员名单]
	 * 创建时间：2017-01-11
	 */
	public String downloadViewFavouriteCourseStatistic(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("收藏时间");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            row.createCell((short) 6).setCellValue(tom.getCreateTime());
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	
	/**
	 * 功能描述：[查询课程评论内容]
	 * 创建时间：2017-01-12
	 */
	public PageData<TomNeedLearnDto> viewCourseComment(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countCourseComment(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> courseComment = coursesMapper.selectCourseComment(map);
		page.setDate(courseComment);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 功能描述：[导出课程评论]
	 * 创建时间：2017-01-12
	 */
	public String downloadViewCourseCommentStatistic(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("评论内容");
		headers.add("评论时间");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            row.createCell((short) 6).setCellValue(tom.getCourseCommentContent());
            row.createCell((short) 7).setCellValue(tom.getCreateTime());
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	/**
	 * 查询课程评分
	 * 创建时间：2017-01-12
	 */
	public PageData<TomNeedLearnDto> viewCourseScore(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countCourseScore(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> courseComment = coursesMapper.selectCourseScore(map);
		page.setDate(courseComment);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 导出课程评分
	 * 创建时间：2017-01-12
	 */
	public String downloadViewCourseScoreStatistic(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("课程评分");
		headers.add("讲师评分");
		headers.add("评分时间");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            row.createCell((short) 6).setCellValue(tom.getScorec());
            row.createCell((short) 7).setCellValue(tom.getScorel());
            row.createCell((short) 8).setCellValue(tom.getCreateTime());
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	/**
	 * 查询开始学习人员名单（章节）
	 * 创建时间：2017-01-12
	 */
	public PageData<TomNeedLearnDto> viewStartLearnSection(int pageNum, int pageSize,int courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<TomNeedLearnDto> page = new PageData<TomNeedLearnDto>();
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		map.put("courseId", courseId);
		int count = coursesMapper.countStartLearnSection(map);
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<TomNeedLearnDto> courseComment = coursesMapper.selectStartLearnSection(map);
		page.setDate(courseComment);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 导出开始学习人员名单
	 * 创建时间：2017-01-12
	 */
	public String downloadViewStartLearnSectionStatistic(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		//headers.add("学完章节");
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            //row.createCell((short) 2).setCellValue(tom.getSectionName());
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
	
	/**
	 * 
	 * 功能描述：[公开课统计]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月18日 上午11:26:10
	 * @param pageNum
	 * @param pageSize
	 * @param courseName
	 * @return
	 * @throws Exception 
	 */
	public PageData<TomOpenCourseStatistic> offlineCourseStatistics(int pageNum, int pageSize,String courseName) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(courseName!=null){
			courseName=courseName.replaceAll("/", "//");
			courseName=courseName.replaceAll("%", "/%");
			courseName=courseName.replaceAll("_", "/_");
		}
		PageData<TomOpenCourseStatistic> page=new PageData<TomOpenCourseStatistic>();
		List<TomOpenCourseStatistic> list=new ArrayList<TomOpenCourseStatistic>();
		TomCourses example=new TomCourses();
		example.setCourseName(courseName);
		example.setCourseOnline("N");
		
		int count=coursesMapper.countByExample(example);
		if(pageSize==-1){
			pageSize = count;
		}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("example", example);
		map.put("listOrder", "UPDATE_TIME");
		map.put("orderBy", "desc");
		List<TomOpenCourseStatistic> courses=coursesMapper.selectOfflineCourse(map);
		for(TomOpenCourseStatistic course:courses){
			List<TomCourseClassify> courseClassifies=courseClassifyMapper.selectByCourseId(course.getCourseId());
			String courseType="";
			for(int i=0;i<courseClassifies.size();i++){
				if(i==0){
					courseType+=courseClassifies.get(i).getClassifyName();
				}else{
					courseType+=","+courseClassifies.get(i).getClassifyName();
				}
			}
			course.setCourseType(courseType);
			if("Y".equals(course.getIsRequired())){
				course.setIsRequired("需要");
			}else{
				course.setIsRequired("不需要");
			}
		}
		page.setDate(courses);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	//导出签到人员/应学习人员
	public String downloadCourseEmpSignStatistics(List<TomNeedLearnDto> tomNeedLearnDto,String fileName){
		List<String> headers=new ArrayList<String>();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("签到时间");
		headers.add("班次");
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        for (int i = 0; i < tomNeedLearnDto.size(); i++) {
        	row = sheet.createRow((int) i + 1);
        	TomNeedLearnDto tom = tomNeedLearnDto.get(i);
        	// 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tom.getName());
            row.createCell((short) 1).setCellValue(tom.getCode());
            row.createCell((short) 2).setCellValue(tom.getDeptname());
            row.createCell((short) 3).setCellValue(tom.getJobname());
            row.createCell((short) 4).setCellValue(tom.getSecretEmail());
            row.createCell((short) 5).setCellValue(tom.getMobile());
            row.createCell((short) 6).setCellValue(sim.format(tom.getSignTime()));
            row.createCell((short) 7).setCellValue(tom.getClassesName());
            
		}
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
        		File file = new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
        		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
        		wb.write(fout);  
        		fout.close();  
        }catch (Exception e){
        	e.printStackTrace();
        }
        return path+"/"+fileName;
	}
}
