package com.chinamobo.ue.reportforms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import com.chinamobo.ue.reportforms.dao.StuCourseRankingMapper;
import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;
import com.chinamobo.ue.reportforms.dto.OrgActivityLearnDto;
import com.chinamobo.ue.reportforms.dto.StuCourseRankingDto;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

/**
 * 功能描述 [ 排行报表]
 * @作者 TYX
 *	创建时间：2017年3月23日 下午7:17:05
 */
@Service
public class StuCourseRankingService {
	@Autowired
	private StuCourseRankingMapper StuCourseRankingMapper;
	
	String filePath=Config.getProperty("file_path");
	
		
	/**
	 * 功能描述：[学员活动，学时排行 ]
	 * 作者：TYX
	 * 创建时间：2017年3月23日 下午8:38:16
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param orgname
	 * @param deptname
	 * @param depttopname
	 * @param deptthname
	 * @return
	 */
	public PageData<StuCourseRankingDto>stuCourseRankingTimerFrom(int pageNum, int pageSize, String beginTimeq,String endTimeq,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<StuCourseRankingDto> page = new PageData<StuCourseRankingDto>();
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		Integer count=StuCourseRankingMapper.findActivityScoreCount(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<StuCourseRankingDto> list = StuCourseRankingMapper.findActivityScore(map);
		
		page.setCount(count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	
	/**
	 * 功能描述：[导出 学员活动，学时排行 ]
	 * 作者：TYX
	 * 创建时间：2017年4月26日 下午3:08:11
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param orgName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public List<StuCourseRankingDto> stuCourseRankingTimerFromList(int pageNum, int pageSize, String beginTimeq,String endTimeq,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		Integer count=StuCourseRankingMapper.findActivityScoreCount(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<StuCourseRankingDto> list = StuCourseRankingMapper.findActivityScore(map);
		
		return list;
	}
	
	public String stuCourseRankingListStudyTimeExcel(List<StuCourseRankingDto> stuCourseRankingServicesList,String fileName){

		List<String> headers=new ArrayList<String>();
		headers.add("code");
		headers.add("姓名");
		headers.add("学习时长");
		
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
        
        for (int i = 0; i < stuCourseRankingServicesList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	StuCourseRankingDto stuCourseRanking = stuCourseRankingServicesList.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(stuCourseRanking.getCode()); 
        	row.createCell((short) 1).setCellValue(stuCourseRanking.getName()); 
        	if(stuCourseRanking.getTime()!=null){
        		row.createCell((short) 2).setCellValue(new DecimalFormat("#.00").format(stuCourseRanking.getTime()/60));
        	}else{
        		row.createCell((short) 2).setCellValue(0);
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
	
	/**
	 * 功能描述：[学员活动，学分排行 ]
	 * 作者：TYX
	 * 创建时间：2017年3月23日 下午8:39:03
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param orgname
	 * @param deptname
	 * @param depttopname
	 * @param deptthname
	 * @return
	 */
	public PageData<StuCourseRankingDto>stuCourseRankingScoreFrom(int pageNum, int pageSize, String beginTimeq,String endTimeq,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<StuCourseRankingDto> page = new PageData<StuCourseRankingDto>();
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		Integer count=StuCourseRankingMapper.findEmpActivityCount(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<StuCourseRankingDto> list = StuCourseRankingMapper.findEmpActivity(map);
		
		page.setCount(count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	
	/**
	 * 功能描述：[导出 学员活动，学分排行 ]
	 * 作者：TYX
	 * 创建时间：2017年4月27日 上午9:56:41
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param orgName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public List<StuCourseRankingDto> stuCourseRankingScoreFromList(int pageNum, int pageSize, String beginTimeq,String endTimeq,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		Integer count=StuCourseRankingMapper.findEmpActivityCount(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<StuCourseRankingDto> list = StuCourseRankingMapper.findEmpActivity(map);
		
		return list;
	}
	
	public String stuCourseRankingScoreFromListExcel(List<StuCourseRankingDto> stuCourseRankingServicesList,String fileName){

		List<String> headers=new ArrayList<String>();
		headers.add("code");
		headers.add("姓名");
		headers.add("学分");
		
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
        
        for (int i = 0; i < stuCourseRankingServicesList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	StuCourseRankingDto stuCourseRanking = stuCourseRankingServicesList.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(stuCourseRanking.getCode()); 
        	row.createCell((short) 1).setCellValue(stuCourseRanking.getName()); 
        	if(stuCourseRanking.getExchangeNumber()!=null){
        		row.createCell((short) 2).setCellValue(stuCourseRanking.getExchangeNumber());
        	}else{
        		row.createCell((short) 2).setCellValue(0);
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
	
	
	
	/**
	 * 功能描述：[学员活动，课程评分排行 ]
	 * 作者：TYX
	 * 创建时间：2017年3月23日 下午8:39:23
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param orgname
	 * @param deptname
	 * @param depttopname
	 * @param deptthname
	 * @return
	 */
	public PageData<StuCourseRankingDto>stuCourseRankingCourseScoreFrom(int pageNum, int pageSize, String beginTimeq,String endTimeq,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<StuCourseRankingDto> page = new PageData<StuCourseRankingDto>();
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		Integer count=StuCourseRankingMapper.findActivityCourseScoreCount(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<StuCourseRankingDto> list = StuCourseRankingMapper.findActivityCourseScore(map);
		
		page.setCount(count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	
	/**
	 * 功能描述：[导出 学员活动，课程评分排行 ]
	 * 作者：TYX
	 * 创建时间：2017年4月27日 上午10:36:27
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param orgName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public List<StuCourseRankingDto> stuCourseRankingCourseScoreFromList(int pageNum, int pageSize, String beginTimeq,String endTimeq,
			String orgName,String oneDeptName, String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//根据条件筛选活动
		Map<String, Object> map=new HashMap<>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("orgName", orgName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		Integer count=StuCourseRankingMapper.findActivityCourseScoreCount(map);
	
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		List<StuCourseRankingDto> list = StuCourseRankingMapper.findActivityCourseScore(map);
		
		return list;
	}
	
	public String stuCourseRankingCourseScoreFromListExcel(List<StuCourseRankingDto> stuCourseRankingServicesList,String fileName){

		List<String> headers=new ArrayList<String>();
		headers.add("课程名");
		headers.add("评分");
		
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
        
        for (int i = 0; i < stuCourseRankingServicesList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	StuCourseRankingDto stuCourseRanking = stuCourseRankingServicesList.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(stuCourseRanking.getCourseName()); 
        	if(stuCourseRanking.getScore()!=null){
        		row.createCell((short) 1).setCellValue(stuCourseRanking.getScore());
        	}else{
        		row.createCell((short) 1).setCellValue(0);
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
	
}
