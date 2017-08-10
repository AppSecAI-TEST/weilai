package com.chinamobo.ue.reportforms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dto.TomCoursesDto;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;
import com.chinamobo.ue.reportforms.dto.EmpActivityDetaListDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.reportforms.dto.LearningDeptExamDto;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class EmpActivityDetaListService {
	
	@Autowired
	private TomActivityMapper tomActivityMapper;
	
	@Autowired
	private TomAdminMapper adminMapper;
	
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;
	
	@Autowired
	private TomActivityMapper activityMapper;
	
	String filePath=Config.getProperty("file_path");
	

	/**
	 * 功能描述：[学员报表-活动详情 ]
	 * 作者：TYX
	 * 创建时间：2017年4月12日 下午5:01:35
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param examTotalTime
	 * @param code
	 * @param name
	 * @param needApply
	 * @param activityName
	 * @param activityId
	 * @param activityIn
	 * @param projectId
	 * @param examName
	 * @param orgcode
	 * @param onedeptcode
	 * @param deptcode2
	 * @param deptcode3
	 * @return
	 * @throws Exception
	 */

	public PageData<EmpActivityDetaListDto> empActivityDetaListFrom1(int pageNum, int pageSize,String beginTimeq, String endTimeq,String examTotalTime, String code,
			String name, String needApply, String activityName, Integer activityId, String activityIn, Integer projectId, String examName,
			String orgcode, String onedeptcode, String deptcode2, String deptcode3) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		 List<EmpActivityDetaListDto> listt = new ArrayList<EmpActivityDetaListDto>();
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		PageData<EmpActivityDetaListDto> page = new PageData<EmpActivityDetaListDto>();
		//根据条件筛选活动
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examTotalTime", examTotalTime);
		map.put("code", code);
		map.put("name", name);
		map.put("needApply", needApply);
		map.put("activityName", activityName);
		map.put("activityId", activityId);
		if(null!=activityIn){
			if(activityIn.equals("0")){
				map.put("activityIn", new Date());
			}else if(activityIn.equals("1")){
				map.put("activityEnd", new Date());
			}else{
				map.put("activityCenter", new Date());
			}
		}
		map.put("projectId", projectId);
		map.put("examName", examName);
		map.put("orgcode", orgcode);
		map.put("onedeptcode", onedeptcode);
		map.put("deptcode2", deptcode2);
		map.put("deptcode3", deptcode3);
		
//		long start=System.currentTimeMillis();
		
		int count = tomActivityMapper.countEmpActivityDeta(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		Double sumlearningprocess;//课程进度和
		List<EmpActivityDetaListDto> list = tomActivityMapper.empActivityDetaList(map);
		for(EmpActivityDetaListDto empActivityDetaListDto:list){
//			long start=System.currentTimeMillis();
			sumlearningprocess=0.0;
			//判断是否分配管理员
			int countAdmin = adminMapper.countByCode(empActivityDetaListDto.getCode());
			if(countAdmin<1){
				empActivityDetaListDto.setIsAdmin("-");
			}else{
				empActivityDetaListDto.setIsAdmin("管理员");
			}
			if(empActivityDetaListDto.getNeedApply()!=null && empActivityDetaListDto.getNeedApply().equals("Y")){
				empActivityDetaListDto.setNeedApply("是");
				empActivityDetaListDto.setIsRequired("选修");
				TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
				activityEmpsRelation.setApplyType("E");
				activityEmpsRelation.setActivityId(empActivityDetaListDto.getActivityId());
				empActivityDetaListDto.setOpenNumber(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			}else{
				empActivityDetaListDto.setNeedApply("否");
				empActivityDetaListDto.setIsRequired("必修");
			}
			if(empActivityDetaListDto.getActivityStartTime()!=null && empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityStartTime().getTime()<new Date().getTime()&&empActivityDetaListDto.getActivityEndTime().getTime()>new Date().getTime()){
				empActivityDetaListDto.setStatus("进行中");
			}
			if(empActivityDetaListDto.getActivityStartTime()!=null && empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityEndTime().getTime()<new Date().getTime()){
				empActivityDetaListDto.setStatus("已过期");
			}
			if(empActivityDetaListDto.getActivityStartTime()!=null && empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityStartTime().getTime()>new Date().getTime()){
				empActivityDetaListDto.setStatus("未开始");
			}
			
			if(empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityStartTime()!=null){
				empActivityDetaListDto.setTotalTime((empActivityDetaListDto.getActivityEndTime().getTime()-empActivityDetaListDto.getActivityStartTime().getTime())/(1000*60));
			}
			map.put("code", empActivityDetaListDto.getCode());
			map.put("activityId", empActivityDetaListDto.getActivityId());
			CountEb(empActivityDetaListDto);
			/*String learnprogress=null;
			empActivityDetaListDto.setCourseLearning(tomActivityMapper.courseLearning(map));
			int courselearning=empActivityDetaListDto.getCourseLearning()+empActivityDetaListDto.getOffilneCompleteCourse();//学完的课程数
			if(tomActivityMapper.activitycourse(map)!=0 && courselearning!=0){
				learnprogress =new DecimalFormat("##0.00").format((double)courselearning/tomActivityMapper.activitycourse(map)*100)+"%";
//				learnprogress=nf.format((double)courselearning/tomActivityMapper.activitycourse(map));
			}else{
				learnprogress="0%";
			}
			empActivityDetaListDto.setLearnProgress(learnprogress);//学习进度*/
			 int courseEmpRelation = tomActivityMapper.countStudyTimer(map);
			 empActivityDetaListDto.setStudyTime(Double.parseDouble(new DecimalFormat("#.00").format(courseEmpRelation/60.0)));
			 List<EmpActivityDetaListDto> list1 = tomActivityMapper.count(map);
			 for(EmpActivityDetaListDto empActivityDetaListDto1:list1){
				 empActivityDetaListDto.setOnlineCourse(empActivityDetaListDto1.getOnlineCourse());
					empActivityDetaListDto.setOfflineCourse(empActivityDetaListDto1.getOfflineCourse());
					empActivityDetaListDto.setOnlineExam(empActivityDetaListDto1.getOnlineExam());
					empActivityDetaListDto.setTotalIntegral(empActivityDetaListDto1.getTotalIntegral());
					empActivityDetaListDto.setOfflineCourseApply(empActivityDetaListDto1.getOfflineCourseApply());//线下课程报名数
					if(empActivityDetaListDto1.getActivityExamCount()!=0 && tomActivityMapper.onlineComplentExam1(map)!=0 ){
						empActivityDetaListDto.setAvgPassRate(new DecimalFormat("##0.00").format((double)tomActivityMapper.onlineComplentExam1(map)/empActivityDetaListDto1.getActivityExamCount()*100)+"%");
//						empActivityDetaListDto.setAvgPassRate(nf.format((double)tomActivityMapper.onlineComplentExam1(map)/empActivityDetaListDto1.getActivityExamCount()));//考试平均通过率
					}else{
						empActivityDetaListDto.setAvgPassRate("0%");
					}
					if(activityMapper.selectAvgScore1(map)!=0 && empActivityDetaListDto1.getActivityExamCount()!=0){
						empActivityDetaListDto.setAverageScore(Double.parseDouble(new DecimalFormat("#.##").format(activityMapper.selectAvgScore1(map)/empActivityDetaListDto1.getActivityExamCount())));
					}else{
						empActivityDetaListDto.setAverageScore(0.0);
					}
			 }
			empActivityDetaListDto.setOnlineCompleteExam(tomActivityMapper.onlineComplentExam(map));
			empActivityDetaListDto.setOpenNumber(tomActivityMapper.ActivityOpenNum(map));
			map.put("startTime", empActivityDetaListDto.getActivityStartTime());
			map.put("endTime", empActivityDetaListDto.getActivityEndTime());
//			int integral=tomActivityMapper.countInteger(map);
//			empActivityDetaListDto.setIntegral(integral);
//			empActivityDetaListDto.setRequireIntegral(tomActivityMapper.applyActivityCourse(map)+tomActivityMapper.applyActivityExam(map));
//			empActivityDetaListDto.setOptionIntegral(tomActivityMapper.NoApplyActivityCourse(map)+tomActivityMapper.NoApplyActivityExam(map));
			
			//活动内课程的平均学习进度
			CourseAvgProgress(empActivityDetaListDto);
			if(empActivityDetaListDto.getOpenNumber()!=0 && empActivityDetaListDto.getOffilneCompleteCourse()!=0){
				empActivityDetaListDto.setOffilneCourseSignRate(new DecimalFormat("##0.00").format((double)empActivityDetaListDto.getOffilneCompleteCourse()/empActivityDetaListDto.getOpenNumber()*100)+"%");
//				empActivityDetaListDto.setOfflineCourseApplyRate(new DecimalFormat("##0.00").format((double)empActivityDetaListDto.getOfflineCourseApply()/empActivityDetaListDto.getOpenNumber()*100)+"%");
//				empActivityDetaListDto.setOffilneCourseSignRate(nf.format((double)empActivityDetaListDto.getOffilneCompleteCourse()/empActivityDetaListDto.getOpenNumber()));
//				empActivityDetaListDto.setOfflineCourseApplyRate(nf.format((double)empActivityDetaListDto.getOfflineCourseApply()/empActivityDetaListDto.getOpenNumber()));
			}else{
				empActivityDetaListDto.setOffilneCourseSignRate("0%");
//				empActivityDetaListDto.setOfflineCourseApplyRate("0%");
			}
			if(empActivityDetaListDto.getOfflineCourseApply()!=0 && empActivityDetaListDto.getOpenNumber()!=0){
				empActivityDetaListDto.setOfflineCourseApplyRate(new DecimalFormat("##0.00").format((double)empActivityDetaListDto.getOfflineCourseApply()/empActivityDetaListDto.getOpenNumber()*100)+"%");
			}else{
				empActivityDetaListDto.setOfflineCourseApplyRate("0%");
			}
			//活动完成时间/首次访问时间
			ComTime(empActivityDetaListDto);
			if(empActivityDetaListDto.getActivityFinishTime()!=null &&empActivityDetaListDto.getActivityEndTime().after(new Date())){
				empActivityDetaListDto.setStatus("已完成");
			}

		}
//		System.out.println("*********"+(System.currentTimeMillis()-start));
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/*
	 * 
	 * [导出 学员报表-活动详情]
	 */
	public List<EmpActivityDetaListDto> queryEmpActivityDeptlist(int pageNum, int pageSize,String beginTimeq, String endTimeq,String examTotalTime, String code,
			String name, String needApply, String activityName, Integer activityId, String activityIn, Integer projectId, String examName,
			String orgcode, String onedeptcode, String deptcode2, String deptcode3){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		//根据条件筛选活动
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examTotalTime", examTotalTime);
		map.put("code", code);
		map.put("name", name);
		map.put("needApply", needApply);
		map.put("activityName", activityName);
		map.put("activityId", activityId);
		if(null!=activityIn){
			if(activityIn.equals("0")){
				map.put("activityIn", new Date());
			}else if(activityIn.equals("1")){
				map.put("activityEnd", new Date());
			}else{
				map.put("activityCenter", new Date());
			}
		}
		map.put("projectId", projectId);
		map.put("examName", examName);
		map.put("orgcode", orgcode);
		map.put("onedeptcode", onedeptcode);
		map.put("deptcode2", deptcode2);
		map.put("deptcode3", deptcode3);
		int count = tomActivityMapper.countEmpActivityDeta(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		Double sumlearningprocess;//课程进度和
		List<EmpActivityDetaListDto> list = tomActivityMapper.empActivityDetaList(map);
		for(EmpActivityDetaListDto empActivityDetaListDto:list){
//			long start=System.currentTimeMillis();
			sumlearningprocess=0.0;
			//判断是否分配管理员
			int countAdmin = adminMapper.countByCode(empActivityDetaListDto.getCode());
			if(countAdmin<1){
				empActivityDetaListDto.setIsAdmin("-");
			}else{
				empActivityDetaListDto.setIsAdmin("管理员");
			}
			if(empActivityDetaListDto.getNeedApply()!=null && empActivityDetaListDto.getNeedApply().equals("Y")){
				empActivityDetaListDto.setNeedApply("是");
				empActivityDetaListDto.setIsRequired("选修");
				TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
				activityEmpsRelation.setApplyType("E");
				activityEmpsRelation.setActivityId(empActivityDetaListDto.getActivityId());
				empActivityDetaListDto.setOpenNumber(activityEmpsRelationMapper.countByExample(activityEmpsRelation));
			}else{
				empActivityDetaListDto.setNeedApply("否");
				empActivityDetaListDto.setIsRequired("必修");
			}
			if(empActivityDetaListDto.getActivityStartTime()!=null && empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityStartTime().getTime()<new Date().getTime()&&empActivityDetaListDto.getActivityEndTime().getTime()>new Date().getTime()){
				empActivityDetaListDto.setStatus("进行中");
			}
			if(empActivityDetaListDto.getActivityStartTime()!=null && empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityEndTime().getTime()<new Date().getTime()){
				empActivityDetaListDto.setStatus("已过期");
			}
			if(empActivityDetaListDto.getActivityStartTime()!=null && empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityStartTime().getTime()>new Date().getTime()){
				empActivityDetaListDto.setStatus("未开始");
			}
			
			if(empActivityDetaListDto.getActivityEndTime()!=null && empActivityDetaListDto.getActivityStartTime()!=null){
				empActivityDetaListDto.setTotalTime((empActivityDetaListDto.getActivityEndTime().getTime()-empActivityDetaListDto.getActivityStartTime().getTime())/(1000*60));
			}
			map.put("code", empActivityDetaListDto.getCode());
			map.put("activityId", empActivityDetaListDto.getActivityId());
			CountEb(empActivityDetaListDto);
			/*String learnprogress=null;
			empActivityDetaListDto.setCourseLearning(tomActivityMapper.courseLearning(map));
			int courselearning=empActivityDetaListDto.getCourseLearning()+empActivityDetaListDto.getOffilneCompleteCourse();//学完的课程数
			if(tomActivityMapper.activitycourse(map)!=0 && courselearning!=0){
				learnprogress=new DecimalFormat("##0.00").format((double)courselearning/tomActivityMapper.activitycourse(map)*100)+"%";
//				learnprogress=nf.format((double)courselearning/tomActivityMapper.activitycourse(map));
			}else{
				learnprogress="0%";
			}
			empActivityDetaListDto.setLearnProgress(learnprogress);//学习进度*/	
		 int courseEmpRelation = tomActivityMapper.countStudyTimer(map);
			 empActivityDetaListDto.setStudyTime(Double.parseDouble(new DecimalFormat("#.00").format(courseEmpRelation/60.0)));
			 List<EmpActivityDetaListDto> list1 = tomActivityMapper.count(map);
			 for(EmpActivityDetaListDto empActivityDetaListDto1:list1){
				 empActivityDetaListDto.setOnlineCourse(empActivityDetaListDto1.getOnlineCourse());
					empActivityDetaListDto.setOfflineCourse(empActivityDetaListDto1.getOfflineCourse());
					empActivityDetaListDto.setOnlineExam(empActivityDetaListDto1.getOnlineExam());
					empActivityDetaListDto.setTotalIntegral(empActivityDetaListDto1.getTotalIntegral());
					empActivityDetaListDto.setOfflineCourseApply(empActivityDetaListDto1.getOfflineCourseApply());//线下课程报名数
					if(empActivityDetaListDto1.getActivityExamCount()!=0){
						empActivityDetaListDto.setAvgPassRate(new DecimalFormat("##0.00").format((double)tomActivityMapper.onlineComplentExam1(map)/empActivityDetaListDto1.getActivityExamCount()*100)+"%");
//						empActivityDetaListDto.setAvgPassRate(nf.format((double)tomActivityMapper.onlineComplentExam1(map)/empActivityDetaListDto1.getActivityExamCount()));//考试平均通过率
//						empActivityDetaListDto.setAverageScore(Double.parseDouble(new DecimalFormat("#.##").format(activityMapper.selectAvgScore1(map)/empActivityDetaListDto1.getActivityExamCount())));
					}else{
						empActivityDetaListDto.setAvgPassRate("0%");
//						empActivityDetaListDto.setAverageScore(0.0);
					}
					if(activityMapper.selectAvgScore1(map)!=0 && empActivityDetaListDto1.getActivityExamCount()!=0){
						empActivityDetaListDto.setAverageScore(Double.parseDouble(new DecimalFormat("#.##").format(activityMapper.selectAvgScore1(map)/empActivityDetaListDto1.getActivityExamCount())));
					}else{
						empActivityDetaListDto.setAverageScore(0.0);
					}
			 }
			empActivityDetaListDto.setOnlineCompleteExam(tomActivityMapper.onlineComplentExam(map));
			empActivityDetaListDto.setOpenNumber(tomActivityMapper.ActivityOpenNum(map));
			map.put("startTime", empActivityDetaListDto.getActivityStartTime());
			map.put("endTime", empActivityDetaListDto.getActivityEndTime());
			//活动内课程的平均学习进度
			CourseAvgProgress(empActivityDetaListDto);
			if(empActivityDetaListDto.getOpenNumber()!=0){
				empActivityDetaListDto.setOffilneCourseSignRate(new DecimalFormat("##0.00").format((double)empActivityDetaListDto.getOffilneCompleteCourse()/empActivityDetaListDto.getOpenNumber()*100)+"%");
//				empActivityDetaListDto.setOfflineCourseApplyRate(new DecimalFormat("##0.00").format((double)empActivityDetaListDto.getOfflineCourseApply()/empActivityDetaListDto.getOpenNumber()*100)+"%");
//				empActivityDetaListDto.setOffilneCourseSignRate(nf.format((double)empActivityDetaListDto.getOffilneCompleteCourse()/empActivityDetaListDto.getOpenNumber()));
//				empActivityDetaListDto.setOfflineCourseApplyRate(nf.format((double)empActivityDetaListDto.getOfflineCourseApply()/empActivityDetaListDto.getOpenNumber()));
			}else{
				empActivityDetaListDto.setOffilneCourseSignRate("0%");
//				empActivityDetaListDto.setOfflineCourseApplyRate("0%");
			}
			if(empActivityDetaListDto.getOfflineCourseApply()!=0 && empActivityDetaListDto.getOpenNumber()!=0){
				empActivityDetaListDto.setOfflineCourseApplyRate(new DecimalFormat("##0.00").format((double)empActivityDetaListDto.getOfflineCourseApply()/empActivityDetaListDto.getOpenNumber()*100)+"%");
			}else{
				empActivityDetaListDto.setOfflineCourseApplyRate("0%");
			}
			//活动完成时间/首次访问时间
			ComTime(empActivityDetaListDto);
			if(empActivityDetaListDto.getActivityFinishTime()!=null &&empActivityDetaListDto.getActivityEndTime().after(new Date())){
				empActivityDetaListDto.setStatus("已完成");
			}
		}
		return list;
	}
	
	public String EmpActivityDeptlistExcel(List<EmpActivityDetaListDto> empActivityDeplist,String fileName){

		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("姓名");
		headers.add("用所属组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("管理员角色分配");
//		headers.add("职务");
		headers.add("邮箱");
		headers.add("活动名称");
		headers.add("项目名称");
		headers.add("活动性质");
		headers.add("活动状态");
		headers.add("活动完成时间");
		headers.add("活动开始日期");
		headers.add("活动结束日期");
		headers.add("授权日期");
		
		headers.add("首次访问时间");
		headers.add("活动所需时长");
		headers.add("活动学习时长");
		headers.add("活动学习进度");
		headers.add("线上课程数");
		headers.add("线下课程数");
		headers.add("线下课程报名数");
		headers.add("线上考试数");
		headers.add("选修活动是否报名");
		headers.add("目标积分");
		headers.add("总或得积分");
		headers.add("必修活动获得积分");
		headers.add("选修活动获得积分");
		headers.add("考试平均成绩");
		headers.add("活动内课程平均学习进度");
		headers.add("活动内线下课程报名率");
		headers.add("活动内线下课程签到率");
		headers.add("活动内完成线上课程数");
		headers.add("活动内完成线下课程数");
		headers.add("活动内完成线上考试数");
		headers.add("活动内考试平均通过率");
		headers.add("管理员");
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
        
        for (int i = 0; i < empActivityDeplist.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	EmpActivityDetaListDto activityStatisticss = empActivityDeplist.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(activityStatisticss.getCode()); 
        	row.createCell((short) 1).setCellValue(activityStatisticss.getName());
        	row.createCell((short) 2).setCellValue(activityStatisticss.getOrgName());
        	row.createCell((short) 3).setCellValue(activityStatisticss.getOneDeptName());
            row.createCell((short) 4).setCellValue(activityStatisticss.getTwoDeptName());  
            row.createCell((short) 5).setCellValue(activityStatisticss.getThreeDeptName());  
            row.createCell((short) 6).setCellValue(activityStatisticss.getIsAdmin());
//            row.createCell((short) 7).setCellValue(activityStatisticss.getJobName());
            row.createCell((short) 7).setCellValue(activityStatisticss.getSecretEmail());
//            row.createCell((short) 9).setCellValue(activityStatisticss.getActivityNumber());
            row.createCell((short) 8).setCellValue(activityStatisticss.getActivityName());
            row.createCell((short) 9).setCellValue(activityStatisticss.getActivityType());
            row.createCell((short) 10).setCellValue(activityStatisticss.getIsRequired());
            row.createCell((short) 11).setCellValue(activityStatisticss.getStatus());
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(activityStatisticss.getActivityFinishTime()!=null){
            	row.createCell((short) 12).setCellValue(format2.format(activityStatisticss.getActivityFinishTime()));
            }
            row.createCell((short) 13).setCellValue(format2.format(activityStatisticss.getActivityStartTime()));
            row.createCell((short) 14).setCellValue(format2.format(activityStatisticss.getActivityEndTime()));
            if(activityStatisticss.getProtocolStartTime()!=null){
            	row.createCell((short) 15).setCellValue(format2.format(activityStatisticss.getProtocolStartTime()));
            }else{
            	row.createCell((short) 15).setCellValue(0);
            }
          
            if(activityStatisticss.getFristJoinTime()!=null){
            	row.createCell((short) 16).setCellValue(format2.format(activityStatisticss.getFristJoinTime()));
            }
            row.createCell((short) 17).setCellValue(activityStatisticss.getTotalTime());
            row.createCell((short) 18).setCellValue(activityStatisticss.getStudyTime());
            row.createCell((short) 19).setCellValue(activityStatisticss.getLearnProgress());
            row.createCell((short) 20).setCellValue(activityStatisticss.getOnlineCourse());
            row.createCell((short) 21).setCellValue(activityStatisticss.getOfflineCourse());
            row.createCell((short) 22).setCellValue(activityStatisticss.getOfflineCourseApply());
            row.createCell((short) 23).setCellValue(activityStatisticss.getOnlineExam());
            row.createCell((short) 24).setCellValue(activityStatisticss.getNeedApply());
            if(activityStatisticss.getTotalIntegral()!=null){
            	row.createCell((short) 25).setCellValue(activityStatisticss.getTotalIntegral());
            }else{
            	row.createCell((short) 25).setCellValue(0);
            }
            row.createCell((short) 26).setCellValue(activityStatisticss.getIntegral());
            row.createCell((short) 27).setCellValue(activityStatisticss.getRequireIntegral());
            row.createCell((short) 28).setCellValue(activityStatisticss.getOptionIntegral());  
            row.createCell((short) 29).setCellValue(activityStatisticss.getAverageScore());   
            row.createCell((short) 30).setCellValue(activityStatisticss.getAverageCourseProgress());
            row.createCell((short) 31).setCellValue(activityStatisticss.getOfflineCourseApplyRate());
            row.createCell((short) 32).setCellValue(activityStatisticss.getOffilneCourseSignRate());
            
            row.createCell((short) 33).setCellValue(activityStatisticss.getOnlineCompleteCourse());
            row.createCell((short) 34).setCellValue(activityStatisticss.getOnlineCompleteCourse());
            row.createCell((short) 35).setCellValue(activityStatisticss.getOnlineCompleteExam());
            row.createCell((short) 36).setCellValue(activityStatisticss.getAvgPassRate());
            row.createCell((short) 37).setCellValue(activityStatisticss.getAdmins());
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
	 * 功能描述：[完成时间]
	 * 作者：TYX
	 * 创建时间：2017年5月3日 下午1:22:04
	 */
	public void ComTime(EmpActivityDetaListDto empActivityDetaListDto){
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("code", empActivityDetaListDto.getCode());
		map.put("activityId", empActivityDetaListDto.getActivityId());
		//活动完成时间
		EmpActivityDetaListDto d=activityMapper.activityFinishTime(map);
		if(d.getTotalNum()!=0 || d.getOffilneCompleteCourse()!=0){
			if(d.getTotalNum()!=(d.getStudyNum()+d.getOffilneCompleteCourse())){
				empActivityDetaListDto.setActivityFinishTime(null );
			}else{
				if(d.getSignFinishTime()!=null && d.getActivityFinishTime()!=null){
					if( d.getActivityFinishTime().after(d.getSignFinishTime())){
						empActivityDetaListDto.setActivityFinishTime(d.getActivityFinishTime());
					}else{
						empActivityDetaListDto.setActivityFinishTime(d.getSignFinishTime());
					}
				}else if(d.getSignFinishTime()!=null){
					empActivityDetaListDto.setActivityFinishTime(d.getSignFinishTime());
				}else{
					empActivityDetaListDto.setActivityFinishTime(d.getActivityFinishTime());
				}
			}
		}
		//首次访问时间
		if(d.getStudyNum()!=0 || d.getOfflineCourse()!=0){
			if(d.getActivityFinishTime1()!=null && d.getSignFinishTime1()!=null){
				if(d.getActivityFinishTime1().after(d.getSignFinishTime1())){
					empActivityDetaListDto.setFristJoinTime(d.getSignFinishTime1());
				}else{
					empActivityDetaListDto.setFristJoinTime(d.getActivityFinishTime1());
				}
			}else if(d.getActivityFinishTime1()!=null){
				empActivityDetaListDto.setFristJoinTime(d.getActivityFinishTime1());
			}else if( d.getSignFinishTime1()!=null){
				empActivityDetaListDto.setFristJoinTime(d.getSignFinishTime1());
			}
		}
		
		//活动学习进度
		String learnprogress=null;
		empActivityDetaListDto.setCourseLearning(tomActivityMapper.courseLearning(map));
		int courselearning=d.getStudyNum()+d.getOffilneCompleteCourse();//学完的课程数
		if(tomActivityMapper.activityCECount(map)!=0 && courselearning!=0){
			learnprogress =new DecimalFormat("##0.00").format((double)courselearning/tomActivityMapper.activityCECount(map)*100)+"%";
//			learnprogress=nf.format((double)courselearning/tomActivityMapper.activitycourse(map));
		}else{
			learnprogress="0%";
		}
		empActivityDetaListDto.setLearnProgress(learnprogress);//学习进度
	}
	
	/**
	 * 功能描述：[线上课程、线下课程完成数、获得EB数 ]
	 * 作者：TYX
	 * 创建时间：2017年5月3日 下午5:44:09
	 * @param empActivityDetaListDto
	 */
	public void CountEb(EmpActivityDetaListDto empActivityDetaListDto){
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("code", empActivityDetaListDto.getCode());
		map.put("activityId", empActivityDetaListDto.getActivityId());
		List<EmpActivityDetaListDto>list=activityMapper.countExamEb(map);
			empActivityDetaListDto.setOnlineCompleteCourse(list.get(0).getNum());
			empActivityDetaListDto.setOffilneCompleteCourse(list.get(1).getNum());
			empActivityDetaListDto.setIntegral(list.get(0).getIntegral()+list.get(1).getIntegral()+list.get(2).getIntegral());
			if(empActivityDetaListDto.getNeedApply().equals("否")){
				empActivityDetaListDto.setRequireIntegral(empActivityDetaListDto.getIntegral());
				empActivityDetaListDto.setOptionIntegral(0);
			}else{
				empActivityDetaListDto.setRequireIntegral(0);
				empActivityDetaListDto.setOptionIntegral(empActivityDetaListDto.getIntegral());
			}
		
	}
	
	/**
	 * 功能描述：[课程平均学习进度]
	 * 作者：TYX
	 * 创建时间：2017年5月12日 上午11:42:51
	 * @param empActivityDetaListDto
	 */
	public void CourseAvgProgress(EmpActivityDetaListDto empActivityDetaListDto){
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("activityId", empActivityDetaListDto.getActivityId());
		map.put("code", empActivityDetaListDto.getCode());
		List<TomActivityProperty>courseIds=activityMapper.getCourseId(map);
		double learnprogresscourse=0.0;
		for(int i=0;i<courseIds.size();i++){
			map.put("courseId", courseIds.get(i).getCourseId());
			if(tomActivityMapper.countSection(map)!=0){
				learnprogresscourse=learnprogresscourse+(double)tomActivityMapper.countActivityCourse(map)/tomActivityMapper.countSection(map);
			
			}
			
		}
//		return learnprogresscourse;
		Double sumlearningprocess;//课程进度和
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
		sumlearningprocess=learnprogresscourse+empActivityDetaListDto.getOffilneCompleteCourse()*1;
		if(tomActivityMapper.activitycourse(map)!=0 && sumlearningprocess!=0){
			empActivityDetaListDto.setAverageCourseProgress(new DecimalFormat("##0.00").format((double)sumlearningprocess/tomActivityMapper.activitycourse(map)*100)+"%");
//			empActivityDetaListDto.setAverageCourseProgress(nf.format((double)sumlearningprocess/tomActivityMapper.activitycourse(map)));
		}else{
			empActivityDetaListDto.setAverageCourseProgress("0%");
		}
	}
	
}
