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
import javax.ws.rs.QueryParam;
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
import com.chinamobo.ue.reportforms.dao.EmpLearningDetailedCourseMapper;
import com.chinamobo.ue.reportforms.dto.EmpCourseCommentDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningDetailedCourseDto;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

/**
 * 描述 [学员报表-学习资源详细（课程）]
 * 创建者 LXT
 * 创建时间 2017年3月22日 下午4:39:15
 */
@Service
public class EmpLearningDetailedCourseService {
	
	@Autowired
	private EmpLearningDetailedCourseMapper empLearningDetailedCourseMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomAdminMapper adminMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomProjectClassifyMapper tomProjectClassifyMapper;
	
	String filePath=Config.getProperty("file_path");
	

	/**
	 * 功能描述 [分页 条件 查询]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:39:18
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param examName
	 * @param offlineExam
	 * @param examType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public PageData<EmpLearningDetailedCourseDto> findPageList(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String examTotalTime,
			String parentClassifyId,String activityName,String needApply,String activityState,String courseName,String code,String name,
			String openCourse,String courseOnline,String sectionClassifyName,
			String orgName,String jobName,String oneDeptName,String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpLearningDetailedCourseDto> page = new PageData<EmpLearningDetailedCourseDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examTotalTime", examTotalTime);
		map.put("parentClassifyId", parentClassifyId);
		map.put("activityName", activityName);
		map.put("needApply", needApply);
		map.put("activityState", activityState);
		map.put("courseName", courseName);
		map.put("code", code);
		map.put("name", name);
		map.put("openCourse", openCourse);
		map.put("courseOnline", courseOnline);
		String[] str = null;
		if(sectionClassifyName!=null && !"".equals(sectionClassifyName)){
			str=sectionClassifyName.split(",");
		}
		map.put("sectionClassifyName", str);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		List<EmpLearningDetailedCourseDto> list=empLearningDetailedCourseMapper.findPageList(map);
		for(EmpLearningDetailedCourseDto empLearningDetailedCourseDto : list){
			//判断是否分配管理员
			int countAdmin = adminMapper.countByCode(empLearningDetailedCourseDto.getCode());
			if(countAdmin<1){
				empLearningDetailedCourseDto.setIsAdmin("-");
			}else{
				empLearningDetailedCourseDto.setIsAdmin("管理员");
			}
		}
		Integer count=empLearningDetailedCourseMapper.findCount(map);
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
	 * 功能描述 [条件 导出excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月21日 下午3:39:33
	 * @param beginTimeq
	 * @param endTimeq
	 * @param examName
	 * @param offlineExam
	 * @param examType
	 * @param orgName
	 * @param jobName
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @return
	 */
	public Response downloadEmpLearningDetailedCourseExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String examTotalTime,
			String parentClassifyId,String activityName,String needApply,String activityState,String courseName,String code,String name,
			String openCourse,String courseOnline,String sectionClassifyName,
			String orgName,String jobName,String oneDeptName,String twoDeptName,String threeDeptName,HttpServletRequest request,
			String column){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examTotalTime", examTotalTime);
		map.put("parentClassifyId", parentClassifyId);
		map.put("activityName", activityName);
		map.put("needApply", needApply);
		map.put("activityState", activityState);
		map.put("courseName", courseName);
		map.put("code", code);
		map.put("name", name);
		map.put("openCourse", openCourse);
		map.put("courseOnline", courseOnline);
		String[] str = null;
		if(sectionClassifyName!=null && !"".equals(sectionClassifyName)){
			str=sectionClassifyName.split(",");
		}
		map.put("sectionClassifyName", str);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		List<EmpLearningDetailedCourseDto> list=empLearningDetailedCourseMapper.findList(map);
		
		//写入excel表格
		String fileName = "学员课程报表.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("姓名");
		headers.add("所属组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("管理角色分配");
		headers.add("邮箱");
		headers.add("课程编号");
		
		headers.add("课程名称");
		headers.add("资源积分");
		headers.add("资源属性");
		headers.add("资源类别1级");
		headers.add("资源类别2级");
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("项目名称");
		headers.add("创建日期");
		headers.add("活动性质");
		
		headers.add("活动报名");
		headers.add("活动报名时间");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
		headers.add("授权日期");
		if(column!=null){
			String[] coul =column.split(",");
			if(coul.length==1){
				if("1".equals(coul[0])){
					//线上课程
					headers.add("首次访问时间");
					headers.add("学习次数");
					headers.add("学习资源时长");
					headers.add("学习时长");
					headers.add("学习进度");
				}
				if("2".equals(coul[0])){
					//headers.add("线下报名");
					headers.add("线下班次名称");
					//headers.add("线下班次时间");
					headers.add("线下班次地点");
					headers.add("线下班次报名时间");
					headers.add("线下班次开始时间");
					headers.add("线下班次结束时间");
					headers.add("班次签到");
				}
				
				if("1".equals(coul[0])){
					headers.add("评论时间");	
					headers.add("课程维度1评分");
					headers.add("课程维度2评分");
					headers.add("课程维度3评分");
					headers.add("课程维度4评分");
					headers.add("课程维度5评分");
					headers.add("课程评论内容");
				}
			}else{
				if("1".equals(coul[0])){
					//线上课程
					headers.add("首次访问时间");
					headers.add("学习次数");
					headers.add("学习资源时长");
					headers.add("学习时长");
					headers.add("学习进度");
				}
				if("2".equals(coul[1])){
					//headers.add("线下报名");
					headers.add("线下班次名称");
					//headers.add("线下班次时间");
					headers.add("线下班次地点");
					headers.add("线下班次报名时间");
					headers.add("线下班次开始时间");
					headers.add("线下班次结束时间");
					headers.add("班次签到");
				}
				
				if("1".equals(coul[0])){
					headers.add("评论时间");	
					headers.add("课程维度1评分");
					headers.add("课程维度2评分");
					headers.add("课程维度3评分");
					headers.add("课程维度4评分");
					headers.add("课程维度5评分");
					headers.add("课程评论内容");
				}
			}
			
		}

		
		
		
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
        for (int i = 0; i < list.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	EmpLearningDetailedCourseDto empLearningDetailedCourseDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
        	List<String> dataList= new ArrayList<String>();
        	dataList.add(empLearningDetailedCourseDto.getCode());
            dataList.add(empLearningDetailedCourseDto.getName()); 
            dataList.add(empLearningDetailedCourseDto.getOrgName()); 
            dataList.add(empLearningDetailedCourseDto.getOneDeptName()); 
            dataList.add(empLearningDetailedCourseDto.getTwoDeptName()); 
            dataList.add(empLearningDetailedCourseDto.getThreeDeptName()); 
          //判断是否分配管理员
			int countAdmin = adminMapper.countByCode(empLearningDetailedCourseDto.getCode());
			if(countAdmin<1){
				dataList.add("");
			}else{
				dataList.add("管理员");
			}
			dataList.add(empLearningDetailedCourseDto.getSecretEmail()); 
			dataList.add(empLearningDetailedCourseDto.getCourseNumber()); 
            
			dataList.add(empLearningDetailedCourseDto.getCourseName()); 
            if(empLearningDetailedCourseDto.getEcurrency()!=null){
            	dataList.add(empLearningDetailedCourseDto.getEcurrency()+""); 
            }else{
            	dataList.add("0"); 
            }
            if(empLearningDetailedCourseDto.getOpenCourse()!=null && "Y".equals(empLearningDetailedCourseDto.getOpenCourse())){
            	dataList.add("公开课"); 
            }else if(empLearningDetailedCourseDto.getOpenCourse()!=null && "N".equals(empLearningDetailedCourseDto.getOpenCourse())){
            	dataList.add("非公开课"); 
            }else{
            	dataList.add(""); 
            }
            if(empLearningDetailedCourseDto.getCourseOnline()!=null && "Y".equals(empLearningDetailedCourseDto.getCourseOnline())){
            	dataList.add("线上"); 
            }else if(empLearningDetailedCourseDto.getCourseOnline()!=null && "N".equals(empLearningDetailedCourseDto.getCourseOnline())){
            	dataList.add("线下");
            }else{
            	dataList.add("");
            }
            
            dataList.add(empLearningDetailedCourseDto.getSectionClassifyName()); 
            dataList.add(empLearningDetailedCourseDto.getActivityNumber()); 
            dataList.add(empLearningDetailedCourseDto.getActivityName()); 
            dataList.add(empLearningDetailedCourseDto.getParentProjectClassifyName()); 
            if(empLearningDetailedCourseDto.getCreateTime()!=null){
            	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getCreateTime())); 
            }else{
            	dataList.add(""); 
            }
            if(empLearningDetailedCourseDto.getNeedApply()!=null && empLearningDetailedCourseDto.getNeedApply().equals("Y")){
            	dataList.add("选修");
            	dataList.add("是"); 
            }else if(empLearningDetailedCourseDto.getNeedApply()!=null &&  empLearningDetailedCourseDto.getNeedApply().toString().equals("N")){
            	dataList.add("必修");
            	dataList.add("否");
            }else{
            	dataList.add("");
            	dataList.add("");
            }
            if(empLearningDetailedCourseDto.getApplicationStartTime()!=null){
            	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getApplicationStartTime()));
            }else{
            	dataList.add("");
            }
            if(empLearningDetailedCourseDto.getActivityStartTime()!=null){
            	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getActivityStartTime())); 
            }else{
            	dataList.add(""); 
            }
            if(empLearningDetailedCourseDto.getActivityEndTime()!=null){
            	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getActivityEndTime()));
            }else{
            	dataList.add("");
            }
            if(empLearningDetailedCourseDto.getUpdateTime()!=null){
            	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getUpdateTime())); 
            }else{
            	dataList.add(""); 
            }
            if(column!=null){
            	String[] coul =column.split(",");
    			if(coul.length==1){
    				if("1".equals(coul[0])){
    	            	dataList.add(empLearningDetailedCourseDto.getFirstVisitTime()); 
    	            	dataList.add(empLearningDetailedCourseDto.getLearningCount()); 
    	                 if(empLearningDetailedCourseDto.getCourseTimes()!=null){
    	                 	 dataList.add(empLearningDetailedCourseDto.getCourseTimes()+""); 
    	                 }else{
    	                 	 dataList.add("0"); 
    	                 }
    	                 dataList.add(empLearningDetailedCourseDto.getLearningCourseTimes()); 
    	                 dataList.add(empLearningDetailedCourseDto.getLearningrate()); 
    	            }
    	           if("2".equals(coul[0])){
    	               dataList.add(empLearningDetailedCourseDto.getClassesName()); 
    	               dataList.add(empLearningDetailedCourseDto.getClassesAddress()); 
    	               dataList.add(empLearningDetailedCourseDto.getClassesSignUpTime()); 
    	               if(empLearningDetailedCourseDto.getClassesBeginTime()!=null){
    	               	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getClassesBeginTime())); 
    	               }else{
    	               	dataList.add(""); 
    	               }
    	               if(empLearningDetailedCourseDto.getClassesEndTime()!=null){
    	               	 dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getClassesEndTime()));
    	               }else{
    	               	 dataList.add("");
    	               }
    	               dataList.add(empLearningDetailedCourseDto.getClassesIsSign()); 
    	           }
    	            if("1".equals(coul[0])){
    	            	 if(empLearningDetailedCourseDto.getCommentDate()!=null){
    	                 	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getCommentDate()));
    	                 }else{
    	                 	dataList.add("");
    	                 }
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC1()); 
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC2()); 
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC3());
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC4());
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC5());
    	                 dataList.add(empLearningDetailedCourseDto.getOneCourseCommentContent()); 
    	            }
    			}else{
    				if("1".equals(coul[0])){
    	            	dataList.add(empLearningDetailedCourseDto.getFirstVisitTime()); 
    	            	dataList.add(empLearningDetailedCourseDto.getLearningCount()); 
    	                 if(empLearningDetailedCourseDto.getCourseTimes()!=null){
    	                 	 dataList.add(empLearningDetailedCourseDto.getCourseTimes()+""); 
    	                 }else{
    	                 	 dataList.add("0"); 
    	                 }
    	                 dataList.add(empLearningDetailedCourseDto.getLearningCourseTimes()); 
    	                 dataList.add(empLearningDetailedCourseDto.getLearningrate()); 
    	            }
    	           if("2".equals(coul[1])){
    	               dataList.add(empLearningDetailedCourseDto.getClassesName()); 
    	               dataList.add(empLearningDetailedCourseDto.getClassesAddress()); 
    	               dataList.add(empLearningDetailedCourseDto.getClassesSignUpTime()); 
    	               if(empLearningDetailedCourseDto.getClassesBeginTime()!=null){
    	               	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getClassesBeginTime())); 
    	               }else{
    	               	dataList.add(""); 
    	               }
    	               if(empLearningDetailedCourseDto.getClassesEndTime()!=null){
    	               	 dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getClassesEndTime()));
    	               }else{
    	               	 dataList.add("");
    	               }
    	               dataList.add(empLearningDetailedCourseDto.getClassesIsSign()); 
    	           }
    	            if("1".equals(coul[0])){
    	            	 if(empLearningDetailedCourseDto.getCommentDate()!=null){
    	                 	dataList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empLearningDetailedCourseDto.getCommentDate()));
    	                 }else{
    	                 	dataList.add("");
    	                 }
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC1()); 
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC2()); 
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC3());
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC4());
    	                 dataList.add(empLearningDetailedCourseDto.getCourseGradeDimensionsC5());
    	                 dataList.add(empLearningDetailedCourseDto.getOneCourseCommentContent()); 
    	            }
    			}
            }
            
            
           
            if(empLearningDetailedCourseDto.getFavoriteStatus()!=null && empLearningDetailedCourseDto.getFavoriteStatus().equals("Y")){
            	 dataList.add("是");
            }
            else{
            	dataList.add("否");
            }
            if(empLearningDetailedCourseDto.getThumbUpStatus()!=null && empLearningDetailedCourseDto.getThumbUpStatus().equals("Y")){
            	dataList.add("是"); 
            }
            else{
            	dataList.add("否"); 
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
			NewCookie activityDeptCookie = new NewCookie("EmpLearningDetailedCourseOne","ok","/",null,null,1,false);
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
	public List<EmpLearningDetailedCourseDto>  findSectionClassify(){
		return empLearningDetailedCourseMapper.findSectionClassify();
	}
}
