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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dto.TomActivityEmpDto;
import com.chinamobo.ue.activity.dto.TomActivityProcessDto;
import com.chinamobo.ue.activity.dto.TomCompletedActDto;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.statistics.entity.TomActivityStatistics;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class ActivityStatisticsService {
	
	@Autowired
	private TomActivityMapper activityMapper;
	
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;
	
	@Autowired
	private TomAdminMapper adminMapper;
	
	@Autowired
	private TomEmpMapper empMapper;
	
	@Autowired
	private TomActivityPropertyMapper tomActivityPropertyMapper;
	
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	
	String filePath=Config.getProperty("file_path");
	
	/**
	 * 
	 * 功能描述：[活动统计分页查询]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月26日
	 * @param pageNum
	 * @param pageSize
	 * @param activityStatistics
	 * @return
	 * @throws Exception 
	 */
	public PageData<TomActivityStatistics> queryActivityStatistics(int pageNum, int pageSize,String activityName,String beginTimeq,String endTimeq) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(activityName!=null){
			activityName=activityName.replaceAll("/", "//");
			activityName=activityName.replaceAll("%", "/%");
			activityName=activityName.replaceAll("_", "/_");
		}
		
		List<TomActivityStatistics> list = new ArrayList<TomActivityStatistics>();
		PageData<TomActivityStatistics> page = new PageData<TomActivityStatistics>();
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date nowDate=new Date();
		String nowStr=format1.format(nowDate);
		
		Map<Object,Object> map=new HashMap<Object, Object>();
		/*if(null==beginTimeq||"".equals(beginTimeq)){
			beginTimeq="1900-01-01"+" 00:00:00";
		}else{
			beginTimeq=beginTimeq+" 00:00:00";
		}*/
		map.put("beginTimeq",beginTimeq);
		
		/*if(null==endTimeq||"".equals(endTimeq)){
			endTimeq=nowStr+" 23:59:59";
		}else{
			endTimeq=endTimeq+" 23:59:59";
		}*/
		map.put("endTimeq", endTimeq);
		map.put("activityName",activityName);
		int count = activityMapper.countByList(map);
		if(pageSize==-1){
			pageSize = count;
		}
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivity> activities = activityMapper.selectListByParam(map);
		for(TomActivity activity : activities){
			TomActivityStatistics activityStatistics = new TomActivityStatistics();
			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
			activityEmpsRelation.setApplyType("E");
			activityEmpsRelation.setActivityId(activity.getActivityId());
			activityStatistics.setActivityId(activity.getActivityId());
			activityStatistics.setActivityName(activity.getActivityName());
			activityStatistics.setNeedApply(activity.getNeedApply());
			activityStatistics.setActivityStartTime(activity.getActivityStartTime());
			activityStatistics.setActivityEndTime(activity.getActivityEndTime());
			activityStatistics.setActivityNumber(activity.getActivityNumber());
			if(activity.getNeedApply().equals("Y")){
				activityStatistics.setNeedApply("是");
				activityStatistics.setOpenNumber(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
				activityEmpsRelation.setApplyStatus("Y");
				activityStatistics.setTotalEnrollment(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
				activityStatistics.setNumberOfParticipants(String.valueOf(activity.getNumberOfParticipants()));
				if(activityStatistics.getNumberOfParticipants().equals("0")){
					activityStatistics.setRegistrationRate("0%");
				}else{
					NumberFormat nf = NumberFormat.getPercentInstance();
		            nf.setMaximumFractionDigits(2);
		            String cf = nf.format(Double.parseDouble(activityStatistics.getTotalEnrollment())/Double.parseDouble(activityStatistics.getNumberOfParticipants()));
					activityStatistics.setRegistrationRate(cf);
				}
			}else{
				activityStatistics.setNeedApply("否");
				activityStatistics.setOpenNumber("-");
				activityStatistics.setTotalEnrollment("-");
				activityStatistics.setNumberOfParticipants("-");
				activityStatistics.setRegistrationRate("-");
			}
			String[] id = activity.getAdmins().split(",");
			String names = "";
			for(int i=1;i<id.length;i++){
				TomAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(id[i]));
				if(admin!=null){
					String name=admin.getName();
					if(i==1){
						names = name;
					}else{
						names = names+","+name;
					}
				}
				
			}
			activityStatistics.setAdmins(names);
			list.add(activityStatistics);
		}
		
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 功能描述：[复选框查询]
	 * 创建者：Acemon
	 * 创建时间：2017-2-13
	 */
	public List<TomActivityStatistics> queryActivityStatisticsByCodes(String[] codes,String[] activityIds){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityStatistics> list = new ArrayList<TomActivityStatistics>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("codes", codes);
		map.put("Ids", activityIds);
		List<TomActivity> lists = activityMapper.selectListByCodes(map);
		for(TomActivity activity : lists){
			TomActivityStatistics activityStatistics = new TomActivityStatistics();
			TomActivityEmpsRelation activityEmpsRelation = new TomActivityEmpsRelation();
			activityEmpsRelation.setApplyType("E");
			activityEmpsRelation.setActivityId(activity.getActivityId());
			activityStatistics.setActivityId(activity.getActivityId());
			activityStatistics.setActivityName(activity.getActivityName());
			activityStatistics.setNeedApply(activity.getNeedApply());
			activityStatistics.setActivityStartTime(activity.getActivityStartTime());
			activityStatistics.setActivityEndTime(activity.getActivityEndTime());
			activityStatistics.setActivityNumber(activity.getActivityNumber());
			if(activity.getNeedApply().equals("Y")){
				activityStatistics.setNeedApply("是");
				activityStatistics.setOpenNumber(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
				activityEmpsRelation.setApplyStatus("Y");
				activityStatistics.setTotalEnrollment(String.valueOf(activityEmpsRelationMapper.countByExample(activityEmpsRelation)));
				activityStatistics.setNumberOfParticipants(String.valueOf(activity.getNumberOfParticipants()));
				if(activityStatistics.getNumberOfParticipants().equals("0")){
					activityStatistics.setRegistrationRate("0%");
				}else{
					NumberFormat nf = NumberFormat.getPercentInstance();
		            nf.setMaximumFractionDigits(2);
		            String cf = nf.format(Double.parseDouble(activityStatistics.getTotalEnrollment())/Double.parseDouble(activityStatistics.getNumberOfParticipants()));
					activityStatistics.setRegistrationRate(cf);
				}
			}else{
				activityStatistics.setNeedApply("否");
				activityStatistics.setOpenNumber("-");
				activityStatistics.setTotalEnrollment("-");
				activityStatistics.setNumberOfParticipants("-");
				activityStatistics.setRegistrationRate("-");
			}
			String[] id = activity.getAdmins().split(",");
			String names = "";
			for(int i=1;i<id.length;i++){
				TomAdmin admin = adminMapper.selectByPrimaryKey(Integer.parseInt(id[i]));
				if(admin!=null){
					String name=admin.getName();
					if(i==1){
						names = name;
					}else{
						names = names+","+name;
					}
				}
			}
			activityStatistics.setAdmins(names);
			list.add(activityStatistics);
		}
		return list;
		
	}

	/**
	 * 
	 * 功能描述：[导出活动统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年5月27日
	 * @param topics
	 * @param
	 * @return
	 */
	public String ActivityStatisticsExcel(List<TomActivityStatistics> activityStatistics,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("是否需要报名");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
		headers.add("开放人数");
		headers.add("报名人数");
		headers.add("限额人数");
		headers.add("报名率");
		
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
        
        for (int i = 0; i < activityStatistics.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityStatistics activityStatisticss = activityStatistics.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(activityStatisticss.getActivityNumber());  
            row.createCell((short) 1).setCellValue(activityStatisticss.getActivityName());  
            row.createCell((short) 2).setCellValue(activityStatisticss.getNeedApply());  
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell((short) 3).setCellValue(format2.format(activityStatisticss.getActivityStartTime()));
            row.createCell((short) 4).setCellValue(format2.format(activityStatisticss.getActivityEndTime()));
            row.createCell((short) 5).setCellValue(activityStatisticss.getOpenNumber());
            row.createCell((short) 6).setCellValue(activityStatisticss.getTotalEnrollment());
            row.createCell((short) 7).setCellValue(activityStatisticss.getNumberOfParticipants());
            row.createCell((short) 8).setCellValue(activityStatisticss.getRegistrationRate());
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
	 * 功能描述：[查看活动统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月1日
	 * @param
	 * @param
	 * @return
	 */
	public PageData<TomActivityProcessDto> seeActivityStatistics(int pageNum, int pageSize, int activityId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityProcessDto> list = new ArrayList<TomActivityProcessDto>();
		PageData<TomActivityProcessDto> page = new PageData<TomActivityProcessDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("activityId",activityId);
		TomActivityProcessDto tomActivityProcessDtoss = new TomActivityProcessDto();
		tomActivityProcessDtoss.setActivityId(activityId);
		int count = activityEmpsRelationMapper.countByProcessExample(tomActivityProcessDtoss);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		/*TomActivityEmpsRelation activityEmpsRelations = new TomActivityEmpsRelation();   totalTask   finishedCourseTask  finishedExamTask
		activityEmpsRelations.setActivityId(activityId);
		activityEmpsRelations.setApplyStatus("Y");*/
		List<TomActivityProcessDto> tomActivityProcessDtos = activityEmpsRelationMapper.selectAllListExample(map);
		for (TomActivityProcessDto tomActivityProcessDto : tomActivityProcessDtos) {
			Map<Object, Object> map1 = new HashMap<Object, Object>();
			TomEmp tomEmp = empMapper.selectByPrimaryKey(tomActivityProcessDto.getCode());
			tomActivityProcessDto.setDeptname(tomEmp.getDeptname());
			tomActivityProcessDto.setMobile(tomEmp.getMobile());
			tomActivityProcessDto.setJobname(tomEmp.getJobname());
			tomActivityProcessDto.setSecretEmail(tomEmp.getSecretEmail());
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(2);
			if (tomActivityProcessDto.getTotalTask() != 0) {
				if (tomActivityProcessDto.getFinishedCourseTask() == 0 && tomActivityProcessDto.getFinishedExamTask() == 0) {
					tomActivityProcessDto.setActivityProcess("0%");
				} else {
					String string = nf.format((Double.parseDouble(String.valueOf(tomActivityProcessDto.getFinishedCourseTask()))
									+ Double.parseDouble(String.valueOf(tomActivityProcessDto.getFinishedExamTask())))
							/ Double.parseDouble(String.valueOf(tomActivityProcessDto.getTotalTask())));
					tomActivityProcessDto.setActivityProcess(string);
				}
			} else {
				tomActivityProcessDto.setActivityProcess("-");
			}
			
		}
		page.setDate(tomActivityProcessDtos);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[导出活动详细统计]
	 *
	 * 创建者：GW
	 * 创建时间: 2016年6月1日
	 * @param topics
	 * @param
	 * @return
	 */
	public String ActivityStatisticsDetailedExcel(List<TomActivityProcessDto> activityEmpsRelation,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("员工编号");
		headers.add("姓名");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("完成进度");
		
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
        
        for (int i = 0; i < activityEmpsRelation.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityProcessDto activityEmpsRelations = activityEmpsRelation.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(activityEmpsRelations.getCode());
            row.createCell((short) 1).setCellValue(activityEmpsRelations.getName());
            row.createCell((short) 2).setCellValue(activityEmpsRelations.getDeptname());
            row.createCell((short) 3).setCellValue(activityEmpsRelations.getJobname());
            row.createCell((short) 4).setCellValue(activityEmpsRelations.getSecretEmail());
            row.createCell((short) 5).setCellValue(activityEmpsRelations.getMobile());
            row.createCell((short) 6).setCellValue(activityEmpsRelations.getActivityProcess());
            /*if(activityEmpsRelations.getApplyStatus().equals("Y")&&activityEmpsRelations.getIsRequired().equals("N")){
            	 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String activityStartTime = format2.format(activityEmpsRelations.getUpdateTime());
                 row.createCell((short) 2).setCellValue(activityStartTime);
            }else{
            	row.createCell((short) 2).setCellValue("-");
            }*/
            
            
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
	 * 功能描述：[查看活动报名人数统计]
	 *
	 * 创建者：Acemon
	 * 创建时间: 2016年12月27日
	 * @param
	 * @param
	 * @return
	 */
	public PageData<TomActivityEmpsRelation> seeActivityApplyStatistics(int pageNum, int pageSize, int activityId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityEmpsRelation> list = new ArrayList<TomActivityEmpsRelation>();
		PageData<TomActivityEmpsRelation> page = new PageData<TomActivityEmpsRelation>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("activityId",activityId);
		TomActivityEmpsRelation activityEmpsRelationss = new TomActivityEmpsRelation();
		activityEmpsRelationss.setActivityId(activityId);
		int count = activityEmpsRelationMapper.countByApplyExample(activityEmpsRelationss);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityEmpsRelation> activityEmpsRelation = activityEmpsRelationMapper.selectListApplyExample(map);
		page.setDate(activityEmpsRelation);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	
	/**
	 * 
	 * 功能描述：[查看活动开放人数统计]
	 *
	 * 创建者：Acemon
	 * 创建时间: 2016年12月27日
	 * @param
	 * @param
	 * @return
	 */
	public PageData<TomActivityEmpDto> seeActivityEmpStatistics(int pageNum, int pageSize, int activityId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityEmpDto> list = new ArrayList<TomActivityEmpDto>();
		PageData<TomActivityEmpDto> page = new PageData<TomActivityEmpDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("activityId",activityId);
		TomActivityEmpsRelation activityEmpsRelationss = new TomActivityEmpsRelation();
		activityEmpsRelationss.setActivityId(activityId);
		int count = activityEmpsRelationMapper.countByExample(activityEmpsRelationss);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityEmpDto> tomActivityEmpDto = activityEmpsRelationMapper.selectListEmpExample(map);
		page.setDate(tomActivityEmpDto);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	
	
	/**
	 * 
	 * 功能描述：[导出活动报名人数详细统计]
	 *
	 * 创建者：Acemon
	 * 创建时间: 2016年12月28日
	 * @param topics
	 * @param
	 * @return
	 */
	public String ActivityApplyStatisticsDetailedExcel(List<TomActivityEmpsRelation> activityEmpsRelation,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("员工编号");
		headers.add("姓名");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
		headers.add("报名时间");
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
        
        for (int i = 0; i < activityEmpsRelation.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityEmpsRelation activityEmpsRelations = activityEmpsRelation.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(activityEmpsRelations.getCode());
            row.createCell((short) 1).setCellValue(activityEmpsRelations.getName());
            row.createCell((short) 2).setCellValue(activityEmpsRelations.getDeptname());
            row.createCell((short) 3).setCellValue(activityEmpsRelations.getJobname());
            row.createCell((short) 4).setCellValue(activityEmpsRelations.getSecretEmail());
            row.createCell((short) 5).setCellValue(activityEmpsRelations.getMobile());
            if(activityEmpsRelations.getApplyStatus().equals("Y")&&activityEmpsRelations.getIsRequired().equals("N")){
            	 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String activityStartTime = format2.format(activityEmpsRelations.getUpdateTime());
                 row.createCell((short) 6).setCellValue(activityStartTime);
            }else{
            	row.createCell((short) 6).setCellValue("-");
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
	 * 
	 * 功能描述：[导出活动开放名单详细统计]
	 *
	 * 创建者：Acemon
	 * 创建时间: 2016年12月28日
	 * @param topics
	 * @param
	 * @return
	 */
	public String ActivityEmpStatisticsDetailedExcel(List<TomActivityEmpDto> tomActivityEmpDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("员工编号");
		headers.add("姓名");
		headers.add("邮箱");
		headers.add("电话号码");
		headers.add("部门");
		headers.add("岗位");
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
        
        for (int i = 0; i < tomActivityEmpDto.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityEmpDto tomActivityEmpDtos = tomActivityEmpDto.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(tomActivityEmpDtos.getCode());
            row.createCell((short) 1).setCellValue(tomActivityEmpDtos.getName());
            row.createCell((short) 2).setCellValue(tomActivityEmpDtos.getSecretEmail());
            row.createCell((short) 3).setCellValue(tomActivityEmpDtos.getOfficephone());
            row.createCell((short) 4).setCellValue(tomActivityEmpDtos.getDeptname());
            row.createCell((short) 5).setCellValue(tomActivityEmpDtos.getPostname());
            /*if(activityEmpsRelations.getApplyStatus().equals("Y")&&activityEmpsRelations.getIsRequired().equals("N")){
            	 DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String activityStartTime = format2.format(activityEmpsRelations.getUpdateTime());
                 row.createCell((short) 2).setCellValue(activityStartTime);
            }else{
            	row.createCell((short) 2).setCellValue("-");
            }*/
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
	public PageData<TomActivityEmpDto> findEmpActive(int pageNum, int pageSize, String code){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityEmpDto> list = new ArrayList<TomActivityEmpDto>();
		PageData<TomActivityEmpDto> page = new PageData<TomActivityEmpDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code",code);
		TomActivityEmpsRelation activityEmpsRelationss = new TomActivityEmpsRelation();
	//	activityEmpsRelationss.setActivityId(activityId);
		int count = activityEmpsRelationMapper.countActiveListByEmp(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomActivityEmpDto> tomActivityEmpDto = activityEmpsRelationMapper.selectActiveListByEmp(map);
		for(TomActivityEmpDto dto : tomActivityEmpDto){
			String[] ids =  dto.getAdmins().split(",");
			map.put("ids",ids);
			List<TomAdmin> admins = adminMapper.selectByPrimaryKeys(map);
			String names = "";
			for(int i=0;i<admins.size();i++){
				if(i==admins.size()-1){
					names=names+admins.get(i).getName();
				}else{
					names=names+admins.get(i).getName()+",";
				}
			}
			dto.setPelCount(names);
		}
		page.setDate(tomActivityEmpDto);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 
	 * 功能描述：[完成活动人员名单]
	 * 创建者：Acemon
	 * 创建时间：2017年5月11日
	 */
	public PageData<EmpLearningActionDto> viewCompletedActivityEmp(int pageNum, int pageSize, int activityId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<String> list = new ArrayList<String>();
		PageData<EmpLearningActionDto> page = new PageData<EmpLearningActionDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("activityId", activityId);
		List<TomActivityEmpsRelation> list2 = activityEmpsRelationMapper.selectActEmpById(activityId);
		for (TomActivityEmpsRelation tomActivityEmpsRelation : list2) {
			int allTask = tomActivityPropertyMapper.countAllTask(activityId);//活动下总任务数
			Map<Object,Object> map1=new HashMap<Object, Object>();
			map1.put("code", tomActivityEmpsRelation.getCode());
			map1.put("activityId", tomActivityEmpsRelation.getActivityId());
			int countActCourse = tomActivityPropertyMapper.countActCourse(map1);//学习线上课程数
			int countActExam = tomActivityPropertyMapper.countActExam(map1);//通过考试数
			int countOfflineCourse = tomActivityPropertyMapper.countOfflineCourse(map1);//学习线下课程数
				if (allTask == countActCourse+ countActExam+countOfflineCourse) {
					list.add(tomActivityEmpsRelation.getCode());
				}
		}
		String[] arr = (String[])list.toArray(new String[list.size()]);//list元素转成数组
		if (arr.length > 0) {
			map.put("codes", arr);
		}
		int count = list.size();
		if (pageSize==-1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);
		List<EmpLearningActionDto> lists = empMapper.selectempByCodes(map);
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 
	 * 功能描述：[导出完成活动人员明细]
	 * 创建者：Acemon
	 * 创建时间：2017年5月11日
	 */
	public String completedActivityEmpExcel(List<EmpLearningActionDto> empLearningActionDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("员工编号");
		headers.add("姓名");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("手机");
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
        
        for (int i = 0; i < empLearningActionDto.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	EmpLearningActionDto activityEmpsRelations = empLearningActionDto.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(activityEmpsRelations.getCode());
            row.createCell((short) 1).setCellValue(activityEmpsRelations.getName());
            row.createCell((short) 2).setCellValue(activityEmpsRelations.getDeptname());
            row.createCell((short) 3).setCellValue(activityEmpsRelations.getJobname());
            row.createCell((short) 4).setCellValue(activityEmpsRelations.getSecretEmail());
            row.createCell((short) 5).setCellValue(activityEmpsRelations.getMobile());
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
	
	//平台统计
	public PageData<TomActivityEmpDto> findpingtai(int pageNum, int pageSize,String beginTime,String endTime){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityEmpDto> list = new ArrayList<TomActivityEmpDto>();
		PageData<TomActivityEmpDto> page = new PageData<TomActivityEmpDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("beginTime",beginTime );
		map.put("endTime",endTime );
		int count = activityEmpsRelationMapper.countpingtaibyDate(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		
		List<TomActivityEmpDto> tomActivityEmpDto = activityEmpsRelationMapper.selectpingtaibyDate(map);
		page.setDate(tomActivityEmpDto);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	public PageData<TomEmp> findpingtaiLearn(int pageNum, int pageSize,String createDate){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//List<TomActivityEmpDto> list = new ArrayList<TomActivityEmpDto>();
		PageData<TomEmp> page = new PageData<TomEmp>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("createDate", createDate);
		int count = activityEmpsRelationMapper.countpingtailearn(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomEmp> tomActivityEmpDto = activityEmpsRelationMapper.selectpingtailearn(map);
		page.setDate(tomActivityEmpDto);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	public PageData<TomEmp> findpingtaiPel(int pageNum, int pageSize,String createDate){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomEmp> list = new ArrayList<TomEmp>();
		PageData<TomEmp> page = new PageData<TomEmp>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("createDate", createDate);
		int count = activityEmpsRelationMapper.countpingtaipel(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomEmp> tomActivityEmpDto = activityEmpsRelationMapper.selectpingtaipel(map);
		page.setDate(tomActivityEmpDto);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	//查询平台在线时长
	public PageData<TomActivityEmpDto> findloginTime(int pageNum, int pageSize,String beginTime,String endTime,String code){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomActivityEmpDto> list = new ArrayList<TomActivityEmpDto>();
		PageData<TomActivityEmpDto> page = new PageData<TomActivityEmpDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("beginTime",beginTime );
		map.put("endTime",endTime );
		map.put("code",code);
		int count = activityEmpsRelationMapper.countLogintimebyDate(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		
		List<TomActivityEmpDto> tomActivityEmpDto = activityEmpsRelationMapper.selectLogintimebyDate(map);
		page.setDate(tomActivityEmpDto);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	public String empActiveStatisticsDetailedExcel(List<TomActivityEmpDto> activityEmpDtoList,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("是否需要报名");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
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
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < activityEmpDtoList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityEmpDto tomActivityEmpDto = activityEmpDtoList.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(tomActivityEmpDto.getAcitvityId());
            row.createCell((short) 1).setCellValue(tomActivityEmpDto.getName());
            if(tomActivityEmpDto.getNeedApply().equals("Y")){
            	row.createCell((short) 2).setCellValue("需要");
            }else{
            	row.createCell((short) 2).setCellValue("不需要");
            }
            row.createCell((short) 3).setCellValue(format2.format(tomActivityEmpDto.getStartTime()));
            row.createCell((short) 4).setCellValue(format2.format(tomActivityEmpDto.getEndTime()));
            row.createCell((short) 5).setCellValue(tomActivityEmpDto.getPelCount());
            
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
	

	public String pingtaiStatisticsDetailedExcel(List<TomActivityEmpDto> activityEmpDtoList,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("日期");
		headers.add("登录人次");
		headers.add("登录人数");
		headers.add("学习人数");
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
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < activityEmpDtoList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityEmpDto tomActivityEmpDto = activityEmpDtoList.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(tomActivityEmpDto.getCreateDate());
            row.createCell((short) 1).setCellValue(tomActivityEmpDto.getLoginCount());
            row.createCell((short) 2).setCellValue(tomActivityEmpDto.getPelCount());
            row.createCell((short) 3).setCellValue(tomActivityEmpDto.getLearnCount());
            
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
	public String pingtaiLearnStatisticsDetailedExcel(List<TomEmp> activityEmpDtoList,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("工号");
		headers.add("姓名");
		headers.add("组织");
		headers.add("一级部门");
		headers.add("二级部门");
		
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
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < activityEmpDtoList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomEmp tomActivityEmpDto = activityEmpDtoList.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(tomActivityEmpDto.getCode());
            row.createCell((short) 1).setCellValue(tomActivityEmpDto.getName());
            row.createCell((short) 2).setCellValue(tomActivityEmpDto.getOrgname());
            row.createCell((short) 3).setCellValue(tomActivityEmpDto.getDepttopname());
            row.createCell((short) 4).setCellValue(tomActivityEmpDto.getDeptname());
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
	public String pingtailoginStatisticsDetailedExcel(List<TomEmp> activityEmpDtoList,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("工号");
		headers.add("姓名");
		headers.add("组织");
		headers.add("一级部门");
		headers.add("二级部门");
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
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < activityEmpDtoList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomEmp tomActivityEmpDto = activityEmpDtoList.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(tomActivityEmpDto.getCode());
            row.createCell((short) 1).setCellValue(tomActivityEmpDto.getName());
            row.createCell((short) 2).setCellValue(tomActivityEmpDto.getOrgname());
            row.createCell((short) 3).setCellValue(tomActivityEmpDto.getDepttopname());
            row.createCell((short) 4).setCellValue(tomActivityEmpDto.getDeptname());
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
	public String pingtaiLoginTimeStatisticsExcel(List<TomActivityEmpDto> activityEmpDtoList,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("日期");
		headers.add("登录时间");
		headers.add("登录时长");
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
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < activityEmpDtoList.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	TomActivityEmpDto tomActivityEmpDto = activityEmpDtoList.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(tomActivityEmpDto.getCreateDate());
            row.createCell((short) 1).setCellValue(tomActivityEmpDto.getMintime());
            row.createCell((short) 2).setCellValue(tomActivityEmpDto.getTimecount());
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
	
