package com.chinamobo.ue.reportforms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomProjectClassifyMapper;
import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.reportforms.dao.LearningResourseOrgDeptCourseMapper;
import com.chinamobo.ue.reportforms.dto.EmpCourseCommentDto;
import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptCourseDto;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

/**
 * 描述 [学习资源-组织部门统计报表（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月17日 下午5:48:47
 */
@Service
public class LearningResourseOrgDeptCourseService {

	@Autowired
	private LearningResourseOrgDeptCourseMapper learningResourseOrgDeptCourseMapper;

	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomProjectClassifyMapper tomProjectClassifyMapper;
	
	String filePath=Config.getProperty("file_path");
	
	/**
	 * 功能描述 [条件 分页 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午3:29:28
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param code
	 * @param name
	 * @param courseName
	 * @param courseOnline
	 * @param courseType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public PageData<LearningResourseOrgDeptCourseDto> findPageList(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String courseName,String courseOnline,String sectionClassifyName,String openCourse,String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<LearningResourseOrgDeptCourseDto> page = new PageData<LearningResourseOrgDeptCourseDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("courseName", courseName);
		map.put("courseOnline", courseOnline);
		String[] str = null;
		if(sectionClassifyName!=null && !"".equals(sectionClassifyName)){
			str=sectionClassifyName.split(",");
		}
		map.put("sectionClassifyName", str);
		map.put("openCourse", openCourse);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptCourseDto> list=learningResourseOrgDeptCourseMapper.findPageList(map);
		Integer count=learningResourseOrgDeptCourseMapper.findCount(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		for(LearningResourseOrgDeptCourseDto learningResourseOrgDeptCourseDto:list){
			map.put("courseId", learningResourseOrgDeptCourseDto.getCourseId());
			learningResourseOrgDeptCourseDto.setLearningSectionCount(learningResourseOrgDeptCourseMapper.findLearningSectionCount(map));
		}
		page.setCount(count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	
	
	/**
	 * 功能描述 [条件 查询 导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午4:10:54
	 * @param beginTimeq
	 * @param endTimeq
	 * @param courseNumber
	 * @param courseName
	 * @param code
	 * @param name
	 * @param orgName
	 * @param jobName
	 * @param projectId
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public Response downloadLearningResourseOrgDeptCourseExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String courseName,String courseOnline,String sectionClassifyName,String openCourse,String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name,HttpServletRequest request,
			String column){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("courseName", courseName);
		map.put("courseOnline", courseOnline);
		String[] str = null;
		if(sectionClassifyName!=null && !"".equals(sectionClassifyName)){
			str=sectionClassifyName.split(",");
		}
		map.put("sectionClassifyName", str);
		map.put("openCourse", openCourse);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptCourseDto> list=learningResourseOrgDeptCourseMapper.findList(map);
		
		//写入excel表格
		String fileName = "课程统计.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("资源编号");
		headers.add("资源名称");
		headers.add("资源积分");
		headers.add("资源属性");
		headers.add("资源类别1级");
		headers.add("资源类别2级");
		if(column!=null){
			String[] coul =column.split(",");
			if(coul.length==1){
				if("1".equals(coul[0])){
					//线上课程
					headers.add("章节数量");
					headers.add("资源访问人次");
					headers.add("资源访问人数");
					headers.add("资源开放人数");
					headers.add("资源未学习人数");
					headers.add("资源学习人数");
					headers.add("资源完成学习人数");
					headers.add("资源参与率");
					headers.add("资源平均完成率");
					headers.add("资源总学习总时长");
					headers.add("资源平均学习时长");
					headers.add("资源平均学习进度");
				}
				if("2".equals(coul[0])){
					/*线下课程*/
					headers.add("资源报名人数");
					headers.add("资源分配人数");
					headers.add("班次数量");
					headers.add("签到数量");
					headers.add("平均签到率");
					headers.add("平均报名率");
				}
			}else{
				if("1".equals(coul[0])){
					//线上课程
					headers.add("章节数量");
					headers.add("资源访问人次");
					headers.add("资源访问人数");
					headers.add("资源开放人数");
					headers.add("资源未学习人数");
					headers.add("资源学习人数");
					headers.add("资源完成学习人数");
					headers.add("资源参与率");
					headers.add("资源平均完成率");
					headers.add("资源总学习总时长");
					headers.add("资源平均学习时长");
					headers.add("资源平均学习进度");
				}
				if("2".equals(coul[1])){
					/*线下课程*/
					headers.add("资源报名人数");
					headers.add("资源分配人数");
					headers.add("班次数量");
					headers.add("签到数量");
					headers.add("平均签到率");
					headers.add("平均报名率");
				}
			}
		}
		headers.add("点赞数");
		headers.add("收藏数");
		headers.add("评论数");
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
        for (int i = 0; i < list.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	LearningResourseOrgDeptCourseDto learningResourseOrgDeptCourseDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
        	List<String> dataList= new ArrayList<String>();
        	dataList.add(learningResourseOrgDeptCourseDto.getCourseNumber());
        	dataList.add(learningResourseOrgDeptCourseDto.getCourseName());
            if(learningResourseOrgDeptCourseDto.getEcurrency()!=null){
            	 dataList.add(learningResourseOrgDeptCourseDto.getEcurrency()+"");
            }else{
            	dataList.add("0");
            }
           
            if(learningResourseOrgDeptCourseDto.getOpenCourse()!=null && learningResourseOrgDeptCourseDto.getOpenCourse().equals("Y")){
            	 dataList.add("公开课");
            }else if(learningResourseOrgDeptCourseDto.getOpenCourse()!=null && learningResourseOrgDeptCourseDto.getOpenCourse().equals("N")){
            	dataList.add("非公开课");
            }else{
            	dataList.add("");
            }
            if(learningResourseOrgDeptCourseDto.getCourseOnline()!=null && learningResourseOrgDeptCourseDto.getCourseOnline().equals("Y")){
            	dataList.add("线上");
            }else if(learningResourseOrgDeptCourseDto.getCourseOnline()!=null && learningResourseOrgDeptCourseDto.getCourseOnline().equals("N")){
            	dataList.add("线下");
            }else{
            	dataList.add("");
            }
            dataList.add(learningResourseOrgDeptCourseDto.getSectionClassifyName());
            
            if(column!=null){
    			String[] coul =column.split(",");
    			if(coul.length==1){
    				if("1".equals(coul[0])){
    					 //线上课程
    		            if(learningResourseOrgDeptCourseDto.getSectionCount()!=null){
    		            	dataList.add(learningResourseOrgDeptCourseDto.getSectionCount()+"");
    		            }else{
    		            	dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getViewers()!=null){
    		            	dataList.add(learningResourseOrgDeptCourseDto.getViewers()+"");
    		            }else{
    		            	dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getViewer()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getViewer()+"");
    		            }else{
    		            	 dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getCourseTotalNumber()!=null){
    		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseTotalNumber()+"");
    		            }else{
    		            	dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getCourseNoLearningNumber()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseNoLearningNumber()+"");
    		            }else{
    		            	 dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getCourseLearningNumber()!=null){
    		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseLearningNumber()+"");
    		            }else{
    		            	dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getCourseCompleteLearningNumber()!=null){
    		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseCompleteLearningNumber()+"");
    		            }else{
    		            	dataList.add("0");
    		            }
    		            
    		            if(learningResourseOrgDeptCourseDto.getCoursePartRate()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getCoursePartRate());
    		            }else{
    		            	 dataList.add("");
    		            }
    		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageCompletionRate());
    		            if(learningResourseOrgDeptCourseDto.getCourseTimes()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseTimes()+"");
    		            }else{
    		            	 dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getCourseAverageLearningTimes()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageLearningTimes()+"");
    		            }else{
    		            	 dataList.add("0");
    		            }
    		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageLearningSchedule());
    				}
    				if("2".equals(coul[0])){
    					 /*线下课程*/
    		            if(learningResourseOrgDeptCourseDto.getCourseSignUpNumber()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseSignUpNumber()+"");
    		            }else{
    		            	 dataList.add("0");
    		            }
    		           if(learningResourseOrgDeptCourseDto.getCourseDistributionNumber()!=null){
    		        	   dataList.add(learningResourseOrgDeptCourseDto.getCourseDistributionNumber()+"");
    		           }else{
    		        	   dataList.add("0");
    		           }
    		            if(learningResourseOrgDeptCourseDto.getCourseClassesNumber()!=null){
    		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseClassesNumber()+"");
    		            }else{
    		            	 dataList.add("0");
    		            }
    		            if(learningResourseOrgDeptCourseDto.getCourseSignNumber()!=null){
    		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseSignNumber()+"");
    		            }else{
    		            	dataList.add("0");
    		            }
    		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageSignRate());
    		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageSignUpRate());
    				}
    			}else{
    				if("1".equals(coul[0])){
   					 //线上课程
   		            if(learningResourseOrgDeptCourseDto.getSectionCount()!=null){
   		            	dataList.add(learningResourseOrgDeptCourseDto.getSectionCount()+"");
   		            }else{
   		            	dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getViewers()!=null){
   		            	dataList.add(learningResourseOrgDeptCourseDto.getViewers()+"");
   		            }else{
   		            	dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getViewer()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getViewer()+"");
   		            }else{
   		            	 dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getCourseTotalNumber()!=null){
   		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseTotalNumber()+"");
   		            }else{
   		            	dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getCourseNoLearningNumber()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseNoLearningNumber()+"");
   		            }else{
   		            	 dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getCourseLearningNumber()!=null){
   		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseLearningNumber()+"");
   		            }else{
   		            	dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getCourseCompleteLearningNumber()!=null){
   		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseCompleteLearningNumber()+"");
   		            }else{
   		            	dataList.add("0");
   		            }
   		            
   		            if(learningResourseOrgDeptCourseDto.getCoursePartRate()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getCoursePartRate());
   		            }else{
   		            	 dataList.add("");
   		            }
   		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageCompletionRate());
   		            if(learningResourseOrgDeptCourseDto.getCourseTimes()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseTimes()+"");
   		            }else{
   		            	 dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getCourseAverageLearningTimes()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageLearningTimes()+"");
   		            }else{
   		            	 dataList.add("0");
   		            }
   		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageLearningSchedule());
   				}
   				if("2".equals(coul[1])){
   					 /*线下课程*/
   		            if(learningResourseOrgDeptCourseDto.getCourseSignUpNumber()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseSignUpNumber()+"");
   		            }else{
   		            	 dataList.add("0");
   		            }
   		           if(learningResourseOrgDeptCourseDto.getCourseDistributionNumber()!=null){
   		        	   dataList.add(learningResourseOrgDeptCourseDto.getCourseDistributionNumber()+"");
   		           }else{
   		        	   dataList.add("0");
   		           }
   		            if(learningResourseOrgDeptCourseDto.getCourseClassesNumber()!=null){
   		            	 dataList.add(learningResourseOrgDeptCourseDto.getCourseClassesNumber()+"");
   		            }else{
   		            	 dataList.add("0");
   		            }
   		            if(learningResourseOrgDeptCourseDto.getCourseSignNumber()!=null){
   		            	dataList.add(learningResourseOrgDeptCourseDto.getCourseSignNumber()+"");
   		            }else{
   		            	dataList.add("0");
   		            }
   		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageSignRate());
   		            dataList.add(learningResourseOrgDeptCourseDto.getCourseAverageSignUpRate());
   				}
    			}
            }
           
           
            
            
            if(learningResourseOrgDeptCourseDto.getCourseThumbUpCount()!=null){
            	dataList.add(learningResourseOrgDeptCourseDto.getCourseThumbUpCount()+"");
            }else{
            	dataList.add("0");
            }
            if(learningResourseOrgDeptCourseDto.getCourseFavoriteNumber()!=null){
            	dataList.add(learningResourseOrgDeptCourseDto.getCourseFavoriteNumber()+"");
            }else{
            	dataList.add("0");
            }
            if(learningResourseOrgDeptCourseDto.getCourseCommentCount()!=null){
            	dataList.add(learningResourseOrgDeptCourseDto.getCourseCommentCount()+"");
            }else{
            	dataList.add("0");
            }
            for(int j=0;j<headers.size();j++){
            	row.createCell((short) j).setCellValue(dataList.get(j));  
            }  
        }
        
       
        // 将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        ResponseBuilder response=null;
        try{
    		File file = new File(path);
    		if (!file.isDirectory()) {
    			file.mkdirs();
    		}
    		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
    		wb.write(fout);  
    		fout.close();  
    		
    		File excelFile= new File(path+"/"+fileName);
    		response = Response.ok((Object) excelFile);
    		String downFileName;
    		String userAgent=request.getHeader("USER-AGENT");
    		if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
    			downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    		}else{
    			downFileName = URLEncoder.encode(fileName,"UTF8");
    		}
    		response.header("Content-Type", "application/text");
    		response.header("Content-Disposition",
                    "attachment; filename=\""+downFileName+"\"");
    		//当导出完毕向客户端添加Cookie标识;
			NewCookie activityDeptCookie = new NewCookie("learningResourseOrgDeptCourseOne","ok","/",null,null,1,false);
			response.cookie(activityDeptCookie);
        }catch (Exception e){
        	e.printStackTrace();
        }
		return response.build();
	}
	
	
	
	/**
	 * 功能描述 [分页 条件查询课程评论]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 下午3:00:15
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @return
	 */
	public PageData<LearningResourseOrgDeptCourseDto> findCourseCommentList(int pageNum,int pageSize,String courseId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<LearningResourseOrgDeptCourseDto> page = new PageData<LearningResourseOrgDeptCourseDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("courseId", courseId);
		List<LearningResourseOrgDeptCourseDto> list=learningResourseOrgDeptCourseMapper.findCommentListByCourseId(map);
		Integer count=learningResourseOrgDeptCourseMapper.findCommentListCount(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		
		page.setCount(count);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	
	/**
	 * 功能描述 [课程评论导出 excel]
	 * 创建者 LXT
	 * 创建时间 2017年5月2日 下午1:39:15
	 * @param pageNum
	 * @param pageSize
	 * @param courseId
	 * @param request
	 * @return
	 */
	public Response downloadCourseCommentByCourseIdExcel(int pageNum,int pageSize,String courseId,HttpServletRequest request){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("courseId", courseId);
		List<LearningResourseOrgDeptCourseDto> list=learningResourseOrgDeptCourseMapper.findCommentListByCourseId(map);
		
		//写入excel表格
		String fileName = "学习资源-组织部门统计报表（课程）.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("序号");
		headers.add("姓名");
		headers.add("工号");
		headers.add("部门");
		headers.add("岗位");
		headers.add("邮箱");
		headers.add("电话");
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
        for (int i = 0; i < list.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	LearningResourseOrgDeptCourseDto learningResourseOrgDeptCourseDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(i);  
            row.createCell((short) 1).setCellValue(learningResourseOrgDeptCourseDto.getEmpName());  
            row.createCell((short) 2).setCellValue(learningResourseOrgDeptCourseDto.getEmpCode());
            row.createCell((short) 3).setCellValue(learningResourseOrgDeptCourseDto.getEmpDeptname());
            row.createCell((short) 4).setCellValue(learningResourseOrgDeptCourseDto.getEmpPostname());
            row.createCell((short) 5).setCellValue(learningResourseOrgDeptCourseDto.getEmpSecretEmail());
            row.createCell((short) 6).setCellValue(learningResourseOrgDeptCourseDto.getEmpMobile());
            row.createCell((short) 7).setCellValue(learningResourseOrgDeptCourseDto.getCourseCommentContent());
            if(learningResourseOrgDeptCourseDto.getCommentCreateTime()!=null){
            	row.createCell((short) 8).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptCourseDto.getCommentCreateTime()));
            }else{
            	row.createCell((short) 8).setCellValue("");
            }
        }
        
       
        // 将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        ResponseBuilder response=null;
        try{
    		File file = new File(path);
    		if (!file.isDirectory()) {
    			file.mkdirs();
    		}
    		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
    		wb.write(fout);  
    		fout.close();  
    		
    		File excelFile= new File(path+"/"+fileName);
    		response = Response.ok((Object) excelFile);
    		String downFileName;
    		String userAgent=request.getHeader("USER-AGENT");
    		if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
    			downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    		}else{
    			downFileName = URLEncoder.encode(fileName,"UTF8");
    		}
    		response.header("Content-Type", "application/text");
    		response.header("Content-Disposition",
                    "attachment; filename=\""+downFileName+"\"");
    		//当导出完毕向客户端添加Cookie标识;
			NewCookie activityDeptCookie = new NewCookie("learningResourseOrgDeptCourseTwo","ok","/",null,null,1,false);
			response.cookie(activityDeptCookie);
        }catch (Exception e){
        	e.printStackTrace();
        }
		return response.build();
	}
	
	
	/**
	 * 功能描述 [导出课程点赞员工信息 excel]
	 * 创建者 LXT
	 * 创建时间 2017年5月2日 下午1:41:44
	 * @return
	 */
	public Response downLoadCourseThumbUpExcel(HttpServletRequest request,String courseId,String courseName,String beginTimeq,String endTimeq,
			String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("courseId", courseId);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptCourseDto> list=learningResourseOrgDeptCourseMapper.findThumbUpListByCourseId(map);
		
		//写入excel表格
		String fileName = "课程点赞员工信息.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("序号");
		headers.add("课程名称");
		headers.add("姓名");
		headers.add("工号");
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
        for (int i = 0; i < list.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	LearningResourseOrgDeptCourseDto learningResourseOrgDeptCourseDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(i+1);
            row.createCell((short) 1).setCellValue(courseName);
            row.createCell((short) 2).setCellValue(learningResourseOrgDeptCourseDto.getEmpName());  
            row.createCell((short) 3).setCellValue(learningResourseOrgDeptCourseDto.getEmpCode());
            if(learningResourseOrgDeptCourseDto.getThumbUpCreateTime()!=null){
            	row.createCell((short) 4).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptCourseDto.getThumbUpCreateTime()));
            }else{
            	row.createCell((short) 4).setCellValue("");
            }
        }
        
       
        // 将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        ResponseBuilder response=null;
        try{
    		File file = new File(path);
    		if (!file.isDirectory()) {
    			file.mkdirs();
    		}
    		FileOutputStream fout = new FileOutputStream(path+"/"+fileName);  
    		wb.write(fout);  
    		fout.close();  
    		
    		File excelFile= new File(path+"/"+fileName);
    		response = Response.ok((Object) excelFile);
    		String downFileName;
    		String userAgent=request.getHeader("USER-AGENT");
    		if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
    			downFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    		}else{
    			downFileName = URLEncoder.encode(fileName,"UTF8");
    		}
    		response.header("Content-Type", "application/text");
    		response.header("Content-Disposition",
                    "attachment; filename=\""+downFileName+"\"");
    		//当导出完毕向客户端添加Cookie标识;
			NewCookie activityDeptCookie = new NewCookie("learningResourseOrgDeptCourseThree","ok","/",null,null,1,false);
			response.cookie(activityDeptCookie);
        }catch (Exception e){
        	e.printStackTrace();
        }
		return response.build();
	}
	
	/**
	 * 创建者：Acemon
	 * 功能描述：查询所属组织
	 * 创建时间：2017-03-02
	 */
	public List<TomOrg> findOrgname(){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomOrgMapper.selectOrg();
	} 
	
	/**
	 * 创建者：Acemon
	 * 功能描述：查询一级部门
	 * 创建时间：2017-03-03
	 */
	public List<TomDept> findFirstDeptName(String orgcode){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashMap<Object,Object> map = new HashMap<>();
		map.put("orgcode",orgcode);
		List<TomDept> list = tomDeptMapper.selectFirstDeptName(map);
		return list;
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询二级部门
	 * 创建时间：2017-03-04
	 */
	public List<TomDept> findSecondDeptName(String topcode){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashMap<Object,Object> map = new HashMap<>();
		map.put("topcode", topcode);
		List<TomDept> list = tomDeptMapper.selectSecondDeptName(map);
		return list;
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：查询三级部门
	 * 创建时间：2017-03-06
	 */
	public List<TomDept> findThirdDeptName(String topcode){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashMap<Object,Object> map = new HashMap<>();
		map.put("topcode", topcode);
		List<TomDept> list = tomDeptMapper.selectThirdDeptName(map);
		return list;
	}
	
	/**
	 * 创建者：Acemon
	 * 功能描述：查询所有项目分类
	 * 创建时间：2017-03-06
	 * @return
	 */
	public List<TomProjectClassify> findActivityClassify(){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<TomProjectClassify> list = tomProjectClassifyMapper.selectAllProject();
		return list;
	}
	public List<LearningResourseOrgDeptCourseDto> findSectionClassify(){
		return learningResourseOrgDeptCourseMapper.findSectionClassify();
	}
}
