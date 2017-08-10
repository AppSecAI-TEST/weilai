package com.chinamobo.ue.statistics.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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

import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dto.TomEmpCourseDto;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.dto.ExamDto;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.statistics.entity.ExamAnswerDto;
import com.chinamobo.ue.statistics.entity.TomExamEmployeeStatistics;
import com.chinamobo.ue.statistics.entity.TomExamStatistics;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.Global;

@Service
public class ExamStatisticsService {
	
	@Autowired
	private TomExamMapper examMapper;
	
	@Autowired
	private TomTopicOptionMapper optionMapper;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;
	@Autowired
	private TomActivityMapper activityMapper;
	String filePath=Config.getProperty("file_path");
	/**
	 * 功能介绍[考试统计分页查询]
	 * 
	 * 创建者：LG
	 * 创建时间：2016年6月4日 12：00
	 * @param pageNum
	 * @param pageSize
	 * @param TomExamStatistics
	 * @return
	 * @throws Exception 
	 */
	
	
	public PageData<TomExamStatistics> queryExamStatistics(int pageNum, int pageSize,String examName,String beginTimeq,String endTimeq) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(examName!=null){
			examName=examName.replaceAll("/", "//");
			examName=examName.replaceAll("%", "/%");
			examName=examName.replaceAll("_", "/_");
		}
		
		List<TomExamStatistics> lists = new ArrayList<TomExamStatistics>();
		PageData<TomExamStatistics> page = new PageData<TomExamStatistics>();
		
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		//DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date nowDate=new Date();
		String nowStr=format1.format(nowDate);
		
		Map<Object,Object> map=new HashMap<Object, Object>();
		if(null==beginTimeq||"".equals(beginTimeq)){
			beginTimeq="1900-01-01"+" 00:00:00";
		}else{
			beginTimeq=beginTimeq+" 00:00:00";
		}
		map.put("beginTimeq",format2.parse(beginTimeq));
		
		if(null==endTimeq||"".equals(endTimeq)){
//			endTimeq=nowStr+" 23:59:59";
		}else{
			endTimeq=endTimeq+" 23:59:59";
		}
		if(endTimeq!=null){
			map.put("endTimeq", format2.parse(endTimeq));
		}
		
		//Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("examName",examName);
		int count = examMapper.selectBycount(map);
		if(pageSize==-1){
			pageSize = count;
		}
		
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<TomExamStatistics> list = examMapper.selectByList(map);
		
		for(TomExamStatistics exam : list){
			TomExamStatistics examStatistics = new TomExamStatistics();
			examStatistics.setExamId(exam.getExamId());
			examStatistics.setExamName(exam.getExamName());
			examStatistics.setBeginTime(exam.getBeginTime());
			examStatistics.setEndTime(exam.getEndTime());
			examStatistics.setExamType(exam.getExamType());
			examStatistics.setPlanNum(exam.getPlanNum());
			examStatistics.setRealNum(exam.getRealNum());
			examStatistics.setPassNum(exam.getPassNum());
			if(exam.getExamType().equals("A")){
//				TomActivityProperty activityProperty = activityPropertyMapper.selectByExamId(exam.getExamId());
				List<TomActivityProperty> activityPropertyList = activityPropertyMapper.selectByList(exam.getExamId());
				if(activityPropertyList.size()>1){
					for(TomActivityProperty activityProperty : activityPropertyList){
						if(activityProperty.getCreateTime().compareTo(exam.getCreateTime())==0){
							TomActivity activity = activityMapper.selectByPrimaryKey(activityProperty.getActivityId());
							if(activity!=null){
								examStatistics.setActivityName(activity.getActivityName());
							}
						}
					}
				}else if(activityPropertyList.size()==1){
					for(TomActivityProperty activityProperty : activityPropertyList){
						TomActivity activity = activityMapper.selectByPrimaryKey(activityProperty.getActivityId());
						if(activity!=null){
							examStatistics.setActivityName(activity.getActivityName());
						}
					}
				}
//				for(TomActivityProperty activityProperty : activityPropertyList){
//					if(activityProperty!=null){
//						TomActivity activity = activityMapper.selectByPrimaryKey(activityProperty.getActivityId());
//						if(activity!=null){
//							examStatistics.setActivityName(activity.getActivityName());
//						}
//					}
//				}
//				examStatistics.setActivityName(activityMapper.selectByPrimaryKey(activityPropertyMapper.selectByExamId(exam.getExamId()).getActivityId()).getActivityName());
			}
			NumberFormat nf = NumberFormat.getPercentInstance();
			 nf.setMaximumFractionDigits(2);
			 if(exam.getRealNum().equals("0")){
				 examStatistics.setPassRate("0%");			 
			 }else{
				 String passrate=nf.format(Double.parseDouble(exam.getPassNum())/Double.parseDouble(exam.getRealNum()));
				examStatistics.setPassRate(passrate);
			 }
			DecimalFormat df=new DecimalFormat("##.0");
			double value=Double.parseDouble(df.format(exam.getAverageScore()));
			examStatistics.setAverageScore(value);
			lists.add(examStatistics);
		}
		
		page.setDate(lists);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}

	/**
	 * 功能描述：[复选框查询]
	 * 创建人：Acemon
	 * 创建时间：2017-2-13
	 */
	public List<TomExamStatistics> queryExamStatisticsByCode(String[] codes,String[] examIds){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomExamStatistics> list = new ArrayList<TomExamStatistics>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("codes", codes);
		map.put("Ids", examIds);
		List<TomExamStatistics> tomExamList = examMapper.selectByCodes(map);
		for(TomExamStatistics exam : tomExamList){
			TomExamStatistics examStatistics = new TomExamStatistics();
			examStatistics.setExamId(exam.getExamId());
			examStatistics.setExamName(exam.getExamName());
			examStatistics.setBeginTime(exam.getBeginTime());
			examStatistics.setEndTime(exam.getEndTime());
			if(exam.getExamType().equals("A")){
				examStatistics.setExamType("活动考试");
			}else{
				examStatistics.setExamType("独立考试");
			}
			examStatistics.setPlanNum(exam.getPlanNum());
			examStatistics.setRealNum(exam.getRealNum());
			examStatistics.setPassNum(exam.getPassNum());
			if(exam.getExamType().equals("A")){
				examStatistics.setActivityName(activityMapper.selectByPrimaryKey(activityPropertyMapper.selectByExamId(exam.getExamId()).getActivityId()).getActivityName());
			}
			NumberFormat nf = NumberFormat.getPercentInstance();
			 nf.setMaximumFractionDigits(2);
			 if(exam.getRealNum().equals("0")){
				 examStatistics.setPassRate("0%");			 
			 }else{
				 String passrate=nf.format(Double.parseDouble(exam.getPassNum())/Double.parseDouble(exam.getRealNum()));
				examStatistics.setPassRate(passrate);
			 }
			DecimalFormat df=new DecimalFormat("##.0");
			double value=Double.parseDouble(df.format(exam.getAverageScore()));
			examStatistics.setAverageScore(value);
			list.add(examStatistics);
		}
		return list;
		
	}
	/**
	 * 功能介绍：[考试统计导出]
	 * 创建人：LG
	 * 创建时间：2016年6月4日 14：20
	 * @param
	 * @return 
	 */
	public String downloadExamStatisticsExcel(List<TomExamStatistics> examlist,String filename){
		List<String> list=new ArrayList<String>();
		list.add("活动名称");
		list.add("考试名称");
		list.add("考试开始时间");
		list.add("考试结束时间");
		list.add("考试类型");
		list.add("应考人数");
		list.add("实考人数");
		list.add("通过人数");
		list.add("通过率");
		list.add("平均分");
		
		HSSFWorkbook work=new HSSFWorkbook();
		HSSFSheet sheet=work.createSheet(filename);
		
		//添加行
		HSSFRow row=sheet.createRow((int)0);
		//列
		HSSFCellStyle style=work.createCellStyle();
		
		//居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 HSSFCell cell ;
	        for(int i=0;i<list.size();i++){
	        	cell= row.createCell((short) i);  
	            cell.setCellValue(list.get(i));  
	            cell.setCellStyle(style);  
	        }
	        for(int i=0;i<examlist.size();i++){
	        	row=sheet.createRow((int)i+1);
	        	TomExamStatistics exam=examlist.get(i);
	        	row.createCell((int)0).setCellValue(exam.getActivityName());
	        	row.createCell((int)1).setCellValue(exam.getExamName());
	        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        	row.createCell((int) 2).setCellValue(format.format(exam.getBeginTime()));
	        	row.createCell((int) 3).setCellValue(format.format(exam.getEndTime()));
	        	row.createCell((int) 4).setCellValue(exam.getExamType());
	        	row.createCell((int) 5).setCellValue(exam.getPlanNum());
	        	row.createCell((int) 6).setCellValue(exam.getRealNum());
	        	row.createCell((int) 7).setCellValue(exam.getPassNum());
	        	row.createCell((int) 8).setCellValue(exam.getPassRate());
	        	row.createCell((int) 9).setCellValue(exam.getAverageScore());
	        	
	        }
	        
	        DateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String path=Config.getProperty("file_path")+"file/download/"+format.format(new Date());
	        
    		try {

    	        File file=new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
				FileOutputStream out = new FileOutputStream(path+"/"+filename);
				
				work.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
		return path+"/"+filename;
		
	}
	/**
	 * 功能介绍：[考试统计详细信息列表]
	 * 创建人：LG
	 * 创建时间：2016年6月4日 17：00
	 * @param pageNum
	 * @param pageSize
	 * @param TomExamStatistics
	 */
	public PageData<TomExamEmployeeStatistics> queryExamEmployeeStatistics(int pageNum, int pageSize,Integer examId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomExamEmployeeStatistics> list=new ArrayList<TomExamEmployeeStatistics>();
		PageData<TomExamEmployeeStatistics> page=new PageData<TomExamEmployeeStatistics>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		
		map.put("examId", examId);
		
		int count=examMapper.selectEmployeeBycount(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		list=examMapper.selectEmployeeByList(map);
		List<TomExamEmployeeStatistics> lists=new ArrayList<TomExamEmployeeStatistics>();
		for(TomExamEmployeeStatistics exam:list){
			TomExamEmployeeStatistics emp=new TomExamEmployeeStatistics();
			emp.setExamId(exam.getExamId());
			emp.setCode(exam.getCode());
			emp.setEmpName(exam.getEmpName());
			emp.setMobile(exam.getMobile());
			emp.setOnedeptname(exam.getOnedeptname());
			emp.setDeptname(exam.getDeptname());
			emp.setTotalPoints(exam.getTotalPoints());
			emp.setEmail(exam.getEmail());
			if(exam.getGradeState().equals("Y")){
				emp.setGradeState(Global.Yes);
			}else{
				emp.setGradeState(Global.No);
			}
			emp.setExamCount(exam.getExamCount());
			lists.add(emp);
		}
		page.setCount(count);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setDate(lists);
		
		return page;
		
	}
	/**
	 * 功能介绍：[考试统计详细信息导出]
	 * 创建人：LG
	 * 创建时间：2016年6月4日 18：20
	 * @param TomExamEmployeeStatistics
	 * @return 
	 */
	public String downloadExamEmployeeStatisticsExcel(List<TomExamEmployeeStatistics> examlist,String filename){
		List<String> list=new ArrayList<String>();
		list.add("姓名");
		list.add("手机");
		list.add("邮箱");
		list.add("一级部门");
		list.add("当前部门");
		list.add("得分");
		list.add("通过");
		list.add("考试次数");
		
		HSSFWorkbook work=new HSSFWorkbook();
		HSSFSheet sheet=work.createSheet(filename);
		
		//添加行
		HSSFRow row=sheet.createRow((int)0);
		//列
		HSSFCellStyle style=work.createCellStyle();
		
		//居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 HSSFCell cell ;
	        for(int i=0;i<list.size();i++){
	        	cell= row.createCell((short) i);  
	            cell.setCellValue(list.get(i));  
	            cell.setCellStyle(style);  
	        }
	        for(int i=0;i<examlist.size();i++){
	        	row=sheet.createRow((int)i+1);
	        	TomExamEmployeeStatistics exam=examlist.get(i);
	        	row.createCell((int) 0).setCellValue(exam.getEmpName());
	        	row.createCell((int) 1).setCellValue(exam.getMobile());
	        	row.createCell((int) 2).setCellValue(exam.getEmail());
	        	row.createCell((int) 3).setCellValue(exam.getOnedeptname());
	        	row.createCell((int) 4).setCellValue(exam.getDeptname());
	        	row.createCell((int) 5).setCellValue(exam.getTotalPoints());
	        	row.createCell((int) 6).setCellValue(exam.getGradeState());
	        	row.createCell((int) 7).setCellValue(exam.getExamCount());
	        	
	        }
	        
	        DateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String path=Config.getProperty("file_path")+"file/download/"+format.format(new Date());
	        
    		try {

    	        File file=new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
				FileOutputStream out = new FileOutputStream(path+"/"+filename);
				
				work.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
		return path+"/"+filename;
		
	}
	
	/**
	 * 
	 * 功能描述：[考试答题详细报表]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月25日 下午4:15:45
	 * @param examlist
	 * @param filename
	 * @return
	 */
	public String downloadExamAnswerExcel(int examId,String fileName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<String> list=new ArrayList<String>();
		list.add("编号");
		list.add("考试名称");
		list.add("员工编号");
		list.add("姓名");
		list.add("题型");
		list.add("试题内容");
		list.add("所有答案");
		list.add("正确答案");
		list.add("用户答案");
		list.add("是否正确");
		
		List<ExamAnswerDto> answerDtos=examMapper.selectAnswerDto(examId);
		
		HSSFWorkbook work=new HSSFWorkbook();
		HSSFSheet sheet=work.createSheet(fileName);
		
		//添加行
		HSSFRow row=sheet.createRow((int)0);
		//列
		HSSFCellStyle style=work.createCellStyle();
		
		//居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 HSSFCell cell ;
	        for(int i=0;i<list.size();i++){
	        	cell= row.createCell((short) i);  
	            cell.setCellValue(list.get(i));  
	            cell.setCellStyle(style);  
	        }
	        for(int i=0;i<answerDtos.size();i++){
	        	row=sheet.createRow((int)i+1);
	        	ExamAnswerDto answerDto=answerDtos.get(i);
	        	row.createCell((int) 0).setCellValue(i+1);
	        	row.createCell((int) 1).setCellValue(answerDto.getExamName());
	        	row.createCell((int) 2).setCellValue(answerDto.getCode());
	        	row.createCell((int) 3).setCellValue(answerDto.getName());
	        	row.createCell((int) 4).setCellValue(answerDto.getType());
	        	row.createCell((int) 5).setCellValue(answerDto.getTopicName());
	        	//所有答案
	        	List<TomTopicOption> options = optionMapper.selectByTopicId(answerDto.getTopicId());
	        	String rightOption="";
	        	String allOption="";	        	
	        	for(TomTopicOption option:options){
	        		allOption+=","+option.getOptionName();
	        		if(option.getRight().equals("Y")){
	        			rightOption+=","+option.getOptionName();
	        		}
	        	}
	        	
	        	String answer="";
	        	if(answerDto.getAnswer()!=null&&(answerDto.getType().equals("单选")||answerDto.getType().equals("多选")||answerDto.getType().equals("判断"))){
	        		String empOptions[]=answerDto.getAnswer().split(",");
	        		for(String optionId:empOptions){
	        			TomTopicOption option=optionMapper.selectByPrimaryKey(Integer.parseInt(optionId));
	        			answer+=","+option.getOptionName();
	        		}
	        	}else{
	        		answer=answerDto.getAnswer();
	        	}
	        	row.createCell((int) 6).setCellValue(allOption.substring(1));
	        	row.createCell((int) 7).setCellValue(rightOption.substring(1));
	        	if("".equals(answer)||null==answer){
	        		row.createCell((int) 8).setCellValue("");
	        	}else{
	        		row.createCell((int) 8).setCellValue(answer.substring(1));
	        	}
	        	
	        	row.createCell((int) 9).setCellValue(answerDto.getIsRight());
	        }
	        
	        DateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String path=Config.getProperty("file_path")+"file/download/"+format.format(new Date());
	        
    		try {

    	        File file=new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
				FileOutputStream out = new FileOutputStream(path+"/"+fileName);
				
				work.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
		return path+"/"+fileName;
		
	}
	
	
	
	/**
	 * 功能介绍：[实际参加考试统计详细信息列表]
	 * 创建人：LG
	 * 创建时间：2016年6月4日 17：00
	 * @param pageNum
	 * @param pageSize
	 * @param TomExamStatistics
	 */
	public PageData<TomExamEmployeeStatistics> queryExamRealEmployeeStatistics(int pageNum, int pageSize,Integer examId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomExamEmployeeStatistics> list=new ArrayList<TomExamEmployeeStatistics>();
		PageData<TomExamEmployeeStatistics> page=new PageData<TomExamEmployeeStatistics>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		
		map.put("examId", examId);
		
		int count=examMapper.selectRealEmployeeBycount(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		list=examMapper.selectRealEmployeeByList(map);
		List<TomExamEmployeeStatistics> lists=new ArrayList<TomExamEmployeeStatistics>();
		for(TomExamEmployeeStatistics exam:list){
			TomExamEmployeeStatistics emp=new TomExamEmployeeStatistics();
			emp.setExamId(exam.getExamId());
			emp.setCode(exam.getCode());
			emp.setEmpName(exam.getEmpName());
			emp.setMobile(exam.getMobile());
			emp.setOnedeptname(exam.getOnedeptname());
			emp.setDeptname(exam.getDeptname());
			emp.setTotalPoints(exam.getTotalPoints());
			emp.setEmail(exam.getEmail());
			if(exam.getGradeState().equals("Y")){
				emp.setGradeState(Global.Yes);
			}else{
				emp.setGradeState(Global.No);
			}
			emp.setExamCount(exam.getExamCount());
			lists.add(emp);
		}
		page.setCount(count);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setDate(lists);
		
		return page;
		
	}
	
	
	/**
	 * 功能介绍：[考试统计详细信息导出]
	 * 创建人：Acemon
	 * 创建时间：2016年12月29日
	 * @param TomExamEmployeeStatistics
	 * @return 
	 */
	public String downloadExamRealEmployeeStatisticsExcel(List<TomExamEmployeeStatistics> examlist,String filename){
		List<String> list=new ArrayList<String>();
		list.add("姓名");
		list.add("手机");
		list.add("邮箱");
		list.add("一级部门");
		list.add("当前部门");
		list.add("得分");
		list.add("通过");
		list.add("考试次数");
		
		HSSFWorkbook work=new HSSFWorkbook();
		HSSFSheet sheet=work.createSheet(filename);
		
		//添加行
		HSSFRow row=sheet.createRow((int)0);
		//列
		HSSFCellStyle style=work.createCellStyle();
		
		//居中格式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 HSSFCell cell ;
	        for(int i=0;i<list.size();i++){
	        	cell= row.createCell((short) i);  
	            cell.setCellValue(list.get(i));  
	            cell.setCellStyle(style);  
	        }
	        for(int i=0;i<examlist.size();i++){
	        	row=sheet.createRow((int)i+1);
	        	TomExamEmployeeStatistics exam=examlist.get(i);
	        	row.createCell((int) 0).setCellValue(exam.getEmpName());
	        	row.createCell((int) 1).setCellValue(exam.getMobile());
	        	row.createCell((int) 2).setCellValue(exam.getEmail());
	        	row.createCell((int) 3).setCellValue(exam.getOnedeptname());
	        	row.createCell((int) 4).setCellValue(exam.getDeptname());
	        	row.createCell((int) 5).setCellValue(exam.getTotalPoints());
	        	row.createCell((int) 6).setCellValue(exam.getGradeState());
	        	row.createCell((int) 7).setCellValue(exam.getExamCount());
	        	
	        }
	        
	        DateFormat format=new SimpleDateFormat("yyyyMMdd");
	        String path=Config.getProperty("file_path")+"file/download/"+format.format(new Date());
	        
    		try {

    	        File file=new File(path);
        		if (!file.isDirectory()) {
        			file.mkdirs();
        		}
				FileOutputStream out = new FileOutputStream(path+"/"+filename);
				
				work.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        
		return path+"/"+filename;
		
	}
	/**
	 * 
	 * 功能描述：[查看人员所有考试]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:32
	 * @param hssfCell
	 * @return
	 */
	public PageData<ExamDto> findempExam(int pageNum,int pageSize,String code){
		//examMapper
		PageData<ExamDto> page=new PageData<ExamDto>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("code", code);
		int count =examMapper.countempExamlist(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<ExamDto> list = examMapper.selectempExamlistBypage(map);
		
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
	public String empExamStatisticsDetailedExcel(List<ExamDto> examlist,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("考试名称");
		headers.add("分数");
		headers.add("是否通过");
		headers.add("考试开始时间");
		headers.add("考试结束时间");
		
		
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
        
        for (int i = 0; i < examlist.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	ExamDto detailedAttendanceStatisticss = examlist.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(detailedAttendanceStatisticss.getExamName());
            row.createCell((short) 1).setCellValue(detailedAttendanceStatisticss.getTotalpoints());
            if("Y".equals(detailedAttendanceStatisticss.getGradeState())){
            	row.createCell((short) 2).setCellValue("通过");
            }else{
            	row.createCell((short) 2).setCellValue("未通过");
            }
            
            row.createCell((short) 3).setCellValue(detailedAttendanceStatisticss.getStartDate());
            row.createCell((short) 4).setCellValue(detailedAttendanceStatisticss.getEndDate());
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
