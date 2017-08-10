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
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.reportforms.dao.EmpScoreDetaileMapper;
import com.chinamobo.ue.reportforms.dao.OrgActivityLearnMapper;
import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;
import com.chinamobo.ue.reportforms.dto.EmpActivityDetaListDto;
import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptCourseDto;
import com.chinamobo.ue.reportforms.dto.OrgActivityLearnDto;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
@Service
public class OrgActivityLearnService {

	@Autowired
	private OrgActivityLearnMapper orgActivityLearnMapper;
	
	@Autowired
	private TomDeptMapper tomDeptMapper;
	
	
	String filePath=Config.getProperty("file_path");
	
	/**
	 * 功能描述：[查询统计报表-部门-活动学习数据 ]
	 * 作者：TYX
	 * 创建时间：2017年3月20日 下午7:04:00
	 * @return
	 * @throws Exception
	 */
	public PageData<OrgActivityLearnDto> orgActivityLearnListFrom(int pageNum, int pageSize,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        DecimalFormat df = new DecimalFormat("#.##");
        if(orgName!=null){
        	orgName=orgName.replaceAll("/", "//");
        	orgName=orgName.replaceAll("%", "/%");
        	orgName=orgName.replaceAll("_", "/_");
			
		}
		
		PageData<OrgActivityLearnDto> page = new PageData<OrgActivityLearnDto>();
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
//		long start=System.currentTimeMillis();
		Integer count=orgActivityLearnMapper.findListCoun(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		List<OrgActivityLearnDto> list = orgActivityLearnMapper.findList(map);
		for(OrgActivityLearnDto orgActivityLearnDto:list){

			deptCode(orgName,oneDeptName,twoDeptName,threeDeptName,orgActivityLearnDto);
			orgActivityLearnDto.setRequireStudyTime(orgActivityLearnMapper.RStudyTime(map)/60);
			if(orgActivityLearnDto.getRequireStudyTime()!=null && orgActivityLearnDto.getJoinStudyNum()!=null  && orgActivityLearnDto.getJoinStudyNum()!=0){
				orgActivityLearnDto.setRequireAvgStudyTime((double)(orgActivityLearnDto.getRequireStudyTime()/orgActivityLearnDto.getJoinStudyNum())*100/100.0);
			}
			orgActivityLearnDto.setRequireComSection(orgActivityLearnMapper.RComCourseSection(map));
			if(orgActivityLearnDto.getRequireComSection()!=null && orgActivityLearnDto.getRequireTotalSection()!=null && orgActivityLearnDto.getRequireTotalActivity()!=null && orgActivityLearnDto.getRequireComSection()!=null && orgActivityLearnDto.getRequireTotalSection()!=0 && orgActivityLearnDto.getRequireTotalActivity()!=0){
				orgActivityLearnDto.setRequireStudyAvgProcess(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getRequireComSection()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getRequireTotalActivity())*100)+"%");
//				orgActivityLearnDto.setRequireStudyAvgProcess(nf.format((double)orgActivityLearnDto.getRequireComSection()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getRequireTotalActivity())));
			}else{
				orgActivityLearnDto.setRequireStudyAvgProcess("0%");
			}
			orgActivityLearnDto.setRequireAvgScore(Double.parseDouble(new DecimalFormat("#.##").format(orgActivityLearnMapper.RAvgScore(map))));
			orgActivityLearnDto.setRequireCourseENum(orgActivityLearnMapper.RCourseE(map));
			orgActivityLearnDto.setRequirePassExamE(orgActivityLearnMapper.RPassExamE(map));
			if(orgActivityLearnDto.getRequirePassExamE()!=null && orgActivityLearnDto.getRequireCourseENum()!=null && orgActivityLearnDto.getRequireCodeNum()!=null && orgActivityLearnDto.getRequireCodeNum()!=0){
				orgActivityLearnDto.setRequireAvgInter(Double.parseDouble(new DecimalFormat("#.##").format((orgActivityLearnDto.getRequireCourseENum()+orgActivityLearnDto.getRequirePassExamE())/orgActivityLearnDto.getRequireCodeNum())));
			}else{
				orgActivityLearnDto.setRequireAvgInter(0.0);
			}
				
		
			orgActivityLearnDto.setRequireScorePassNum(orgActivityLearnMapper.RPassExam(map));
			orgActivityLearnDto.setRequirescoreNum(orgActivityLearnMapper.RExamTotal(map));
			if(orgActivityLearnDto.getRequireScorePassNum()!=null && orgActivityLearnDto.getRequirescoreNum()!=null && orgActivityLearnDto.getRequirescoreNum()!=0 ){
				orgActivityLearnDto.setRequirePassRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getRequireScorePassNum()/orgActivityLearnDto.getRequirescoreNum()*100)+"%");
//				orgActivityLearnDto.setRequirePassRate(nf.format((double)orgActivityLearnDto.getRequireScorePassNum()/orgActivityLearnDto.getRequirescoreNum()));
			}else{
				orgActivityLearnDto.setRequirePassRate("0%");
			}
			orgActivityLearnDto.setRequireCourseCom(orgActivityLearnMapper.RComCourseNum(map));
			orgActivityLearnDto.setRequireCourseNum(orgActivityLearnMapper.RJoinCourseNum(map));
			if(orgActivityLearnDto.getRequireCourseCom()!=null && orgActivityLearnDto.getRequireTotalSection()!=null && orgActivityLearnDto.getJoinStudyNum()!=null && orgActivityLearnDto.getRequireTotalSection()!=0 && orgActivityLearnDto.getJoinStudyNum()!=0){
				orgActivityLearnDto.setRequireCourseRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getRequireCourseCom()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getJoinStudyNum())*100)+"%");
//				orgActivityLearnDto.setRequireCourseRate(nf.format((double)orgActivityLearnDto.getRequireCourseCom()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getJoinStudyNum())));
			}else{
				orgActivityLearnDto.setRequireCourseRate("0%");
			}
			
			
			orgActivityLearnDto.setOptionStudyTime(orgActivityLearnMapper.CStudyTime(map)/60);
			if(orgActivityLearnDto.getOptionStudyTime()!=null && orgActivityLearnDto.getJoinStudyNum()!=null  && orgActivityLearnDto.getJoinStudyNum()!=0){
				orgActivityLearnDto.setOptionAvgStudyTime((double)(orgActivityLearnDto.getOptionStudyTime()/orgActivityLearnDto.getJoinStudyNum())*100/100.0);
			}
			if(orgActivityLearnDto.getOptionComSection()!=null && orgActivityLearnDto.getOptionTotalSection()!=null && orgActivityLearnDto.getOptionTotalActivity()!=null && orgActivityLearnDto.getOptionComSection()!=null && orgActivityLearnDto.getOptionTotalSection()!=0 && orgActivityLearnDto.getOptionTotalActivity()!=0){
				orgActivityLearnDto.setOptionStudyAvgProcess(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getOptionComSection()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getOptionTotalActivity())*100)+"%");
//				orgActivityLearnDto.setOptionStudyAvgProcess(nf.format((double)orgActivityLearnDto.getOptionComSection()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getOptionTotalActivity())));
			}else{
				orgActivityLearnDto.setOptionStudyAvgProcess("0%");
			}
			orgActivityLearnDto.setOptionComSection(orgActivityLearnMapper.CComCourseSection(map));
			orgActivityLearnDto.setOptionAvgScore(Double.parseDouble(new DecimalFormat("#.##").format(orgActivityLearnMapper.CAvgScore(map))));
			orgActivityLearnDto.setOptionComCouNumE(orgActivityLearnMapper.CCourseE(map));
			orgActivityLearnDto.setOptionPassExamE(orgActivityLearnMapper.CPassExamE(map));
			if(orgActivityLearnDto.getOptionPassExamE()!=null && orgActivityLearnDto.getOptionComCouNumE()!=null && orgActivityLearnDto.getOptionCodeNum()!=null && orgActivityLearnDto.getOptionCodeNum()!=0){
				orgActivityLearnDto.setOptionAvgInter(Double.parseDouble(new DecimalFormat("#.##").format((orgActivityLearnDto.getOptionComCouNumE()+orgActivityLearnDto.getOptionPassExamE())/orgActivityLearnDto.getOptionCodeNum())));
			}else{
				orgActivityLearnDto.setOptionAvgInter(0.0);
			}
			orgActivityLearnDto.setOptionScorePassNum(orgActivityLearnMapper.CPassExam(map));
			orgActivityLearnDto.setOptionscoreNum(orgActivityLearnMapper.CExamTotal(map));
			if(orgActivityLearnDto.getOptionScorePassNum()!=null && orgActivityLearnDto.getOptionscoreNum()!=null && orgActivityLearnDto.getOptionscoreNum()!=0 ){
				orgActivityLearnDto.setOptionPassRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getOptionScorePassNum()/orgActivityLearnDto.getOptionscoreNum()*100)+"%");
//				orgActivityLearnDto.setOptionPassRate(nf.format((double)orgActivityLearnDto.getOptionScorePassNum()/orgActivityLearnDto.getOptionscoreNum()));
			}else{
				orgActivityLearnDto.setOptionPassRate("0%");
			}
			orgActivityLearnDto.setOptionCourseCom(orgActivityLearnMapper.CComCourseNum(map));
			orgActivityLearnDto.setOptionCourseNum(orgActivityLearnMapper.CJoinCourseNum(map));
			if(orgActivityLearnDto.getOptionCourseCom()!=null && orgActivityLearnDto.getOptionTotalSection()!=null && orgActivityLearnDto.getJoinStudyNum()!=null && orgActivityLearnDto.getOptionTotalSection()!=0 && orgActivityLearnDto.getJoinStudyNum()!=0 ){
				orgActivityLearnDto.setOptionCourseRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getOptionCourseCom()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getJoinStudyNum())*100)+"%");
//				orgActivityLearnDto.setOptionCourseRate(nf.format((double)orgActivityLearnDto.getOptionCourseCom()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getJoinStudyNum())));
			}else{
				orgActivityLearnDto.setOptionCourseRate("0%");
			}
		}
//		System.out.println("*****************"+(System.currentTimeMillis()-start)+"毫秒");
		page.setCount(count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	
	public List<OrgActivityLearnDto> queryorgActivityLearnList(int pageNum, int pageSize,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        DecimalFormat df = new DecimalFormat("#.##");
		PageData<OrgActivityLearnDto> page = new PageData<OrgActivityLearnDto>();
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		Integer count=orgActivityLearnMapper.findListCoun(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		List<OrgActivityLearnDto> list = orgActivityLearnMapper.findList(map);
		for(OrgActivityLearnDto orgActivityLearnDto:list){
			deptCode(orgName,oneDeptName,twoDeptName,threeDeptName,orgActivityLearnDto);
			orgActivityLearnDto.setRequireStudyTime(orgActivityLearnMapper.RStudyTime(map)/60);
			if(orgActivityLearnDto.getRequireStudyTime()!=null && orgActivityLearnDto.getJoinStudyNum()!=null  && orgActivityLearnDto.getJoinStudyNum()!=0){
				orgActivityLearnDto.setRequireAvgStudyTime((double)(orgActivityLearnDto.getRequireStudyTime()/orgActivityLearnDto.getJoinStudyNum())*100/100.0);
			}
			orgActivityLearnDto.setRequireComSection(orgActivityLearnMapper.RComCourseSection(map));
			if(orgActivityLearnDto.getRequireComSection()!=null && orgActivityLearnDto.getRequireTotalSection()!=null && orgActivityLearnDto.getRequireTotalActivity()!=null && orgActivityLearnDto.getRequireComSection()!=null && orgActivityLearnDto.getRequireTotalSection()!=0 && orgActivityLearnDto.getRequireTotalActivity()!=0){
				orgActivityLearnDto.setRequireStudyAvgProcess(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getRequireComSection()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getRequireTotalActivity())*100)+"%");
//				orgActivityLearnDto.setRequireStudyAvgProcess(nf.format((double)orgActivityLearnDto.getRequireComSection()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getRequireTotalActivity())));
			}else{
				orgActivityLearnDto.setRequireStudyAvgProcess("0%");
			}
			orgActivityLearnDto.setRequireAvgScore(Double.parseDouble(new DecimalFormat("#.##").format(orgActivityLearnMapper.RAvgScore(map))));
			orgActivityLearnDto.setRequireCourseENum(orgActivityLearnMapper.RCourseE(map));
			orgActivityLearnDto.setRequirePassExamE(orgActivityLearnMapper.RPassExamE(map));
			if(orgActivityLearnDto.getRequirePassExamE()!=null && orgActivityLearnDto.getRequireCourseENum()!=null && orgActivityLearnDto.getRequireCodeNum()!=null && orgActivityLearnDto.getRequireCodeNum()!=0){
				orgActivityLearnDto.setRequireAvgInter(Double.parseDouble(new DecimalFormat("#.##").format((orgActivityLearnDto.getRequireCourseENum()+orgActivityLearnDto.getRequirePassExamE())/orgActivityLearnDto.getRequireCodeNum())));
			}else{
				orgActivityLearnDto.setRequireAvgInter(0.0);
			}
				
		
			orgActivityLearnDto.setRequireScorePassNum(orgActivityLearnMapper.RPassExam(map));
			orgActivityLearnDto.setRequirescoreNum(orgActivityLearnMapper.RExamTotal(map));
			if(orgActivityLearnDto.getRequireScorePassNum()!=null && orgActivityLearnDto.getRequirescoreNum()!=null && orgActivityLearnDto.getRequirescoreNum()!=0 ){
				orgActivityLearnDto.setRequirePassRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getRequireScorePassNum()/orgActivityLearnDto.getRequirescoreNum()*100)+"%");
//				orgActivityLearnDto.setRequirePassRate(nf.format((double)orgActivityLearnDto.getRequireScorePassNum()/orgActivityLearnDto.getRequirescoreNum()));
			}else{
				orgActivityLearnDto.setRequirePassRate("0%");
			}
			orgActivityLearnDto.setRequireCourseCom(orgActivityLearnMapper.RComCourseNum(map));
			orgActivityLearnDto.setRequireCourseNum(orgActivityLearnMapper.RJoinCourseNum(map));
			if(orgActivityLearnDto.getRequireCourseCom()!=null && orgActivityLearnDto.getRequireTotalSection()!=null && orgActivityLearnDto.getJoinStudyNum()!=null && orgActivityLearnDto.getRequireTotalSection()!=0 && orgActivityLearnDto.getJoinStudyNum()!=0){
				orgActivityLearnDto.setRequireCourseRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getRequireCourseCom()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getJoinStudyNum())*100)+"%");
//				orgActivityLearnDto.setRequireCourseRate(nf.format((double)orgActivityLearnDto.getRequireCourseCom()/(orgActivityLearnDto.getRequireTotalSection()*orgActivityLearnDto.getJoinStudyNum())));
			}else{
				orgActivityLearnDto.setRequireCourseRate("0%");
			}
			
			
			orgActivityLearnDto.setOptionStudyTime(orgActivityLearnMapper.CStudyTime(map)/60);
			if(orgActivityLearnDto.getOptionStudyTime()!=null && orgActivityLearnDto.getJoinStudyNum()!=null  && orgActivityLearnDto.getJoinStudyNum()!=0){
				orgActivityLearnDto.setOptionAvgStudyTime((double)(orgActivityLearnDto.getOptionStudyTime()/orgActivityLearnDto.getJoinStudyNum())*100/100.0);
			}
			if(orgActivityLearnDto.getOptionComSection()!=null && orgActivityLearnDto.getOptionTotalSection()!=null && orgActivityLearnDto.getOptionTotalActivity()!=null && orgActivityLearnDto.getOptionComSection()!=null && orgActivityLearnDto.getOptionTotalSection()!=0 && orgActivityLearnDto.getOptionTotalActivity()!=0){
				orgActivityLearnDto.setOptionStudyAvgProcess(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getOptionComSection()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getOptionTotalActivity())*100)+"%");
//				orgActivityLearnDto.setOptionStudyAvgProcess(nf.format((double)orgActivityLearnDto.getOptionComSection()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getOptionTotalActivity())));
			}else{
				orgActivityLearnDto.setOptionStudyAvgProcess("0%");
			}
			orgActivityLearnDto.setOptionComSection(orgActivityLearnMapper.CComCourseSection(map));
			orgActivityLearnDto.setOptionAvgScore(Double.parseDouble(new DecimalFormat("#.##").format(orgActivityLearnMapper.CAvgScore(map))));
			orgActivityLearnDto.setOptionComCouNumE(orgActivityLearnMapper.CCourseE(map));
			orgActivityLearnDto.setOptionPassExamE(orgActivityLearnMapper.CPassExamE(map));
			if(orgActivityLearnDto.getOptionPassExamE()!=null && orgActivityLearnDto.getOptionComCouNumE()!=null && orgActivityLearnDto.getOptionCodeNum()!=null && orgActivityLearnDto.getOptionCodeNum()!=0){
				orgActivityLearnDto.setOptionAvgInter(Double.parseDouble(new DecimalFormat("#.##").format((orgActivityLearnDto.getOptionComCouNumE()+orgActivityLearnDto.getOptionPassExamE())/orgActivityLearnDto.getOptionCodeNum())));
			}else{
				orgActivityLearnDto.setOptionAvgInter(0.0);
			}
			orgActivityLearnDto.setOptionScorePassNum(orgActivityLearnMapper.CPassExam(map));
			orgActivityLearnDto.setOptionscoreNum(orgActivityLearnMapper.CExamTotal(map));
			if(orgActivityLearnDto.getOptionScorePassNum()!=null && orgActivityLearnDto.getOptionscoreNum()!=null && orgActivityLearnDto.getOptionscoreNum()!=0 ){
				orgActivityLearnDto.setOptionPassRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getOptionScorePassNum()/orgActivityLearnDto.getOptionscoreNum()*100)+"%");
//				orgActivityLearnDto.setOptionPassRate(nf.format((double)orgActivityLearnDto.getOptionScorePassNum()/orgActivityLearnDto.getOptionscoreNum()));
			}else{
				orgActivityLearnDto.setOptionPassRate("0%");
			}
			orgActivityLearnDto.setOptionCourseCom(orgActivityLearnMapper.CComCourseNum(map));
			orgActivityLearnDto.setOptionCourseNum(orgActivityLearnMapper.CJoinCourseNum(map));
			if(orgActivityLearnDto.getOptionCourseCom()!=null && orgActivityLearnDto.getOptionTotalSection()!=null && orgActivityLearnDto.getJoinStudyNum()!=null && orgActivityLearnDto.getOptionTotalSection()!=0 && orgActivityLearnDto.getJoinStudyNum()!=0 ){
				orgActivityLearnDto.setOptionCourseRate(new DecimalFormat("##0.00").format((double)orgActivityLearnDto.getOptionCourseCom()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getJoinStudyNum())*100)+"%");
//				orgActivityLearnDto.setOptionCourseRate(nf.format((double)orgActivityLearnDto.getOptionCourseCom()/(orgActivityLearnDto.getOptionTotalSection()*orgActivityLearnDto.getJoinStudyNum())));
			}else{
				orgActivityLearnDto.setOptionCourseRate("0%");
			}
		}
		return list;
	}
	
	public String OrgActivityLearnListExcel(List<OrgActivityLearnDto> orgActivityLearnList,String fileName){

		List<String> headers=new ArrayList<String>();
		headers.add("部门");
		headers.add("人数");
		headers.add("参与学习人数");
		headers.add("参与学习率");
//		必修
		headers.add("统计人数");
		headers.add("平均学习时长");
		headers.add("平均学习进度");
		headers.add("平均成绩");
		headers.add("平均获得学分");
		headers.add("考试及格率");
		headers.add("课程完成率");
//		选修
		headers.add("统计人数");
		headers.add("平均学习时长");
		headers.add("平均学习进度");
		headers.add("平均成绩");
		headers.add("平均获得学分");
		headers.add("考试及格率");
		headers.add("课程完成率");
		
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
        
        for (int i = 0; i < orgActivityLearnList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	OrgActivityLearnDto orgActivityLearnLists= orgActivityLearnList.get(i);
        	 // 第四步，创建单元格，并设置值  
//        必修
        	row.createCell((short) 0).setCellValue(orgActivityLearnLists.getOrgName()); 
        	row.createCell((short) 1).setCellValue(orgActivityLearnLists.getCodeNum());
        	row.createCell((short) 2).setCellValue(orgActivityLearnLists.getJoinStudyNum());
        	row.createCell((short) 3).setCellValue(orgActivityLearnLists.getJoinStudyNumRate());
            row.createCell((short) 4).setCellValue(orgActivityLearnLists.getRequireCodeNum());  
            if(orgActivityLearnLists.getRequireAvgStudyTime()!=null){
            	row.createCell((short) 5).setCellValue(orgActivityLearnLists.getRequireAvgStudyTime());
            }else{
            	row.createCell((short) 5).setCellValue(0.0);
            }
            if(orgActivityLearnLists.getRequireStudyAvgProcess()!=null){
            	row.createCell((short) 6).setCellValue(orgActivityLearnLists.getRequireStudyAvgProcess());
            }else{
            	row.createCell((short) 6).setCellValue("0%");
            }
            if(orgActivityLearnLists.getRequireAvgScore()!=null){
            	row.createCell((short) 7).setCellValue(orgActivityLearnLists.getRequireAvgScore()); 
            }else{
            	row.createCell((short) 7).setCellValue(0.0);
            }
            if(orgActivityLearnLists.getOptionAvgInter()!=null){
            	row.createCell((short) 8).setCellValue(orgActivityLearnLists.getOptionAvgInter());
            }else{
            	row.createCell((short) 8).setCellValue(0.0);
            }
            if(orgActivityLearnLists.getRequirePassRate()!=null){
            	row.createCell((short) 9).setCellValue(orgActivityLearnLists.getRequirePassRate());
            }else{
            	row.createCell((short) 9).setCellValue("0%");
            }
            if(orgActivityLearnLists.getRequireCourseRate()!=null){
            	row.createCell((short) 10).setCellValue(orgActivityLearnLists.getRequireCourseRate());
            }else{
            	row.createCell((short) 10).setCellValue("0%");
            }
//			选修
            row.createCell((short) 11).setCellValue(orgActivityLearnLists.getOptionCodeNum());  
            if(orgActivityLearnLists.getOptionAvgStudyTime()!=null){
            	row.createCell((short) 12).setCellValue(orgActivityLearnLists.getOptionAvgStudyTime());
            }else{
            	row.createCell((short) 12).setCellValue(0.0);
            }
            if(orgActivityLearnLists.getOptionStudyAvgProcess()!=null){
            	row.createCell((short) 13).setCellValue(orgActivityLearnLists.getOptionStudyAvgProcess());
            }else{
            	row.createCell((short) 13).setCellValue("0%");
            }
            if(orgActivityLearnLists.getOptionAvgScore()!=null){
            	row.createCell((short) 14).setCellValue(orgActivityLearnLists.getOptionAvgScore()); 
            }else{
            	row.createCell((short) 14).setCellValue(0.0);
            }
            if(orgActivityLearnLists.getOptionAvgInter()!=null){
            	row.createCell((short) 15).setCellValue(orgActivityLearnLists.getOptionAvgInter());
            }else{
            	row.createCell((short) 15).setCellValue(0.0);
            }
            if(orgActivityLearnLists.getOptionPassRate()!=null){
            	row.createCell((short) 16).setCellValue(orgActivityLearnLists.getOptionPassRate());
            }else{
            	row.createCell((short) 16).setCellValue("0%");
            }
            if(orgActivityLearnLists.getOptionCourseRate()!=null){
            	row.createCell((short) 17).setCellValue(orgActivityLearnLists.getOptionCourseRate());
            }else{
            	row.createCell((short) 17).setCellValue("0%");
            }
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
	public static void main(String[] args) {
		int sum = 0;
		long start = System.currentTimeMillis();
		for(int i=0 ;i<20000000;i++){
			sum += i;
		}
		System.out.println(System.currentTimeMillis()-start);
		System.out.println(sum);
	}
	
	public void deptCode(String orgName,String oneDeptName, String twoDeptName,String threeDeptName,OrgActivityLearnDto orgActivityLearnDto){
		if((orgName==null && oneDeptName==null && twoDeptName==null && threeDeptName==null)){
			orgActivityLearnDto.setOrgName("蔚来汽车");
		}
		else if((orgName==null && oneDeptName!=null && twoDeptName==null && threeDeptName==null )){
			orgActivityLearnDto.setOrgName(tomDeptMapper.selectDept2(oneDeptName).get(0).getName());
		}
		else if((orgName==null && oneDeptName!=null && twoDeptName!=null && threeDeptName==null)){
			orgActivityLearnDto.setOrgName(tomDeptMapper.selectDept2(twoDeptName).get(0).getName());
		}
		else if((orgName==null && oneDeptName!=null && twoDeptName!=null && threeDeptName!=null)){
			orgActivityLearnDto.setOrgName(tomDeptMapper.selectDept2(threeDeptName).get(0).getName());
		}
	}
}
