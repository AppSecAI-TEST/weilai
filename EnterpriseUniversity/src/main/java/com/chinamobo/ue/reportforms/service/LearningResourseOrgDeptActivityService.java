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
import com.chinamobo.ue.reportforms.dao.LearningResourseOrgDeptActivityMapper;
import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptActivityDto;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

/**
 * 描述 [学习资源-组织部门报表统计（活动）]
 * 创建者 LXT
 * 创建时间 2017年3月21日 下午3:36:07
 */
@Service
public class LearningResourseOrgDeptActivityService {
	
	@Autowired
	private LearningResourseOrgDeptActivityMapper learningResourseOrgDeptActivityMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
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
	public PageData<LearningResourseOrgDeptActivityDto> findPageList(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String activityType,String activityName,String needApply,String activityState,String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<LearningResourseOrgDeptActivityDto> page = new PageData<LearningResourseOrgDeptActivityDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("activityType", activityType);
		map.put("activityName", activityName);
		map.put("needApply", needApply);
		map.put("activityState", activityState);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptActivityDto> list=learningResourseOrgDeptActivityMapper.findPageList(map);
		Integer count=learningResourseOrgDeptActivityMapper.findCount(map);
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
	public Response downloadLearningResourseOrgDeptActivityExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String activityType,String activityName,String needApply,String activityState,String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name,HttpServletRequest request){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("activityType", activityType);
		map.put("activityName", activityName);
		map.put("needApply", needApply);
		map.put("activityState", activityState);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptActivityDto> list=learningResourseOrgDeptActivityMapper.findList(map);
		
		//写入excel表格
		String fileName = "活动报表.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("项目名称");
		headers.add("创建日期");
		headers.add("活动性质");
		headers.add("活动报名");
		headers.add("活动报名时间");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
		headers.add("授权日期（修改日期）");
		headers.add("活动积分");
		headers.add("活动获得积分");
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
        for (int i = 0; i < list.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	LearningResourseOrgDeptActivityDto learningResourseOrgDeptActivityDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(learningResourseOrgDeptActivityDto.getActivityNumber());  
            row.createCell((short) 1).setCellValue(learningResourseOrgDeptActivityDto.getActivityName());  
            row.createCell((short) 2).setCellValue(learningResourseOrgDeptActivityDto.getActivityType());
            if(learningResourseOrgDeptActivityDto.getCreateTime()!=null){
            	row.createCell((short) 3).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptActivityDto.getCreateTime()));
            }else{
            	row.createCell((short) 3).setCellValue("");
            }
            if(learningResourseOrgDeptActivityDto.getNeedApply()!=null && "N".equals(learningResourseOrgDeptActivityDto.getNeedApply())){
            	row.createCell((short) 4).setCellValue("必修");
            }else if(learningResourseOrgDeptActivityDto.getNeedApply()!=null && "Y".equals(learningResourseOrgDeptActivityDto.getNeedApply())){
            	row.createCell((short) 4).setCellValue("选修");
            }else{
            	row.createCell((short) 4).setCellValue("");
            }
            if(learningResourseOrgDeptActivityDto.getNeedApply()!=null && "Y".equals(learningResourseOrgDeptActivityDto.getNeedApply())){
            	 row.createCell((short) 5).setCellValue("是");
            }else if(learningResourseOrgDeptActivityDto.getNeedApply()!=null && "N".equals(learningResourseOrgDeptActivityDto.getNeedApply())){
            	 row.createCell((short) 5).setCellValue("否");
            }else{
            	 row.createCell((short) 5).setCellValue("");
            }
           
            if(learningResourseOrgDeptActivityDto.getApplicationStartTime()!=null){
            	row.createCell((short) 6).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptActivityDto.getApplicationStartTime()));
            }else{
            	row.createCell((short) 6).setCellValue("");
            }
            if(learningResourseOrgDeptActivityDto.getActivityStartTime()!=null){
            	row.createCell((short) 7).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptActivityDto.getActivityStartTime()));
            }else{
            	row.createCell((short) 7).setCellValue("");
            }
            if(learningResourseOrgDeptActivityDto.getActivityEndTime()!=null){
            	row.createCell((short) 8).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptActivityDto.getActivityEndTime()));
            }else{
            	row.createCell((short) 8).setCellValue("");
            }
            if(learningResourseOrgDeptActivityDto.getUpdateTime()!=null){
            	row.createCell((short) 9).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningResourseOrgDeptActivityDto.getUpdateTime()));
            }else{
            	row.createCell((short) 9).setCellValue("");
            }
            
            
            row.createCell((short) 10).setCellValue(learningResourseOrgDeptActivityDto.getActivityTotalEb());
            row.createCell((short) 11).setCellValue(learningResourseOrgDeptActivityDto.getActivityEb());
            row.createCell((short) 12).setCellValue(learningResourseOrgDeptActivityDto.getAdminNames());
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
			NewCookie activityDeptCookie = new NewCookie("learningResourseOrgDeptActivityOne","ok","/",null,null,1,false);
			response.cookie(activityDeptCookie);
        }catch (Exception e){
        	e.printStackTrace();
        }
		return response.build();
	}
	
	
	public List<LearningResourseOrgDeptActivityDto> findActivityType(){
		 return learningResourseOrgDeptActivityMapper.findActivityType();
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
}
