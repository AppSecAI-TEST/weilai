package com.chinamobo.ue.reportforms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
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
import com.chinamobo.ue.reportforms.dao.EmpCourseCommentMapper;
import com.chinamobo.ue.reportforms.dto.EmpCourseCommentDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 创建者：LXT
 * 功能描述：学员报表-课程评论
 * 创建时间：2017-03-09
 *
 */
@Service
public class EmpCourseCommentService {

	@Autowired
	private EmpCourseCommentMapper empCourseCommentMapper;
	@Autowired
	private TomAdminMapper adminMapper;
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
	 * 创建时间 2017年3月13日 下午2:44:11
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageData<EmpCourseCommentDto> findPageList(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String courseNumber,String courseName,String code,String name,String orgName,String jobName,
			String projectId,String oneDeptName,String twoDeptName,String threeDeptName,String commentNull){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpCourseCommentDto> page = new PageData<EmpCourseCommentDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("courseNumber", courseNumber);
		map.put("courseName", courseName);
		map.put("code", code);
		map.put("name", name);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("projectId", projectId);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("commentNull", commentNull);
		List<EmpCourseCommentDto> list=empCourseCommentMapper.findPageList(map);
		for(EmpCourseCommentDto empCourseCommentDto : list){
			//判断是否分配管理员
			int countAdmin = adminMapper.countByCode(empCourseCommentDto.getCode());
		if(countAdmin<1){
			empCourseCommentDto.setIsAdmin("-");
		}else{
			empCourseCommentDto.setIsAdmin("管理员");
		}
		}
		int count=empCourseCommentMapper.findCount(map);
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
	 * 功能描述 [条件 导出excel ]
	 * 创建者 LXT
	 * 创建时间 2017年3月14日 下午7:02:42
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
	public Response downloadEmpCourseCommentExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String courseNumber,String courseName,String code,String name,String orgName,String jobName,
			String projectId,String oneDeptName,String twoDeptName,String threeDeptName,HttpServletRequest request,
			String commentNull){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("courseNumber", courseNumber);
		map.put("courseName", courseName);
		map.put("code", code);
		map.put("name", name);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("projectId", projectId);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("commentNull", commentNull);
		List<EmpCourseCommentDto> list=empCourseCommentMapper.findList(map);
		
		//写入excel表格
		String fileName = "学员报表-课程评论.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("姓名");
		headers.add("所属组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("管理角色分配");
	
		headers.add("邮箱");
		headers.add("资源编号");
		headers.add("课程名称");
		headers.add("评价时间");
		headers.add("课程评分");
		headers.add("课程维度1评分");
		headers.add("课程维度2评分");
		headers.add("课程维度3评分");
		headers.add("课程维度4评分");
		headers.add("课程维度5评分");
		headers.add("讲师维度1评分");
		headers.add("讲师维度2评分");
		headers.add("讲师维度3评分");
		headers.add("讲师维度4评分");
		headers.add("讲师维度5评分");
		headers.add("是否收藏");
		headers.add("是否点赞");
		headers.add("评论内容");
		
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
        	EmpCourseCommentDto empCourseCommentDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(empCourseCommentDto.getCode());  
            row.createCell((short) 1).setCellValue(empCourseCommentDto.getName());  
            row.createCell((short) 2).setCellValue(empCourseCommentDto.getOrgName());  
            row.createCell((short) 3).setCellValue(empCourseCommentDto.getOneDeptName());
            row.createCell((short) 4).setCellValue(empCourseCommentDto.getTwoDeptName());
            row.createCell((short) 5).setCellValue(empCourseCommentDto.getThreeDeptName());
            //判断是否分配管理员
            int countAdmin = adminMapper.countByCode(empCourseCommentDto.getCode());
    		if(countAdmin<1){
    			//empCourseCommentDto.setIsAdmin("-");
    			row.createCell((short) 6).setCellValue("");
    		}else{
    			//empCourseCommentDto.setIsAdmin("管理员");
    			row.createCell((short) 6).setCellValue("管理员");
    		}
            
          
            row.createCell((short) 7).setCellValue(empCourseCommentDto.getSecretEmail());
            row.createCell((short) 8).setCellValue(empCourseCommentDto.getCourseNumber());
            row.createCell((short) 9).setCellValue(empCourseCommentDto.getCourseName());
            if(empCourseCommentDto.getCommentDate()==null || empCourseCommentDto.getCommentDate().equals("")){
            	row.createCell((short) 10).setCellValue("");
            }else{
            	row.createCell((short) 10).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empCourseCommentDto.getCommentDate()));
            }
            if(empCourseCommentDto.getScore()==null){
            	row.createCell((short) 11).setCellValue("");
            }else{
            	row.createCell((short) 11).setCellValue(empCourseCommentDto.getScore());
            }
            
            row.createCell((short) 12).setCellValue(empCourseCommentDto.getCourseGradeDimensionsC1());
            row.createCell((short) 13).setCellValue(empCourseCommentDto.getCourseGradeDimensionsC2());
            row.createCell((short) 14).setCellValue(empCourseCommentDto.getCourseGradeDimensionsC3());
            row.createCell((short) 15).setCellValue(empCourseCommentDto.getCourseGradeDimensionsC4());
            row.createCell((short) 16).setCellValue(empCourseCommentDto.getCourseGradeDimensionsC5());
            row.createCell((short) 17).setCellValue(empCourseCommentDto.getCourseGradeDimensionsL1());
            row.createCell((short) 18).setCellValue(empCourseCommentDto.getCourseGradeDimensionsL2());
            row.createCell((short) 19).setCellValue(empCourseCommentDto.getCourseGradeDimensionsL3());
            row.createCell((short) 20).setCellValue(empCourseCommentDto.getCourseGradeDimensionsL4());
            row.createCell((short) 21).setCellValue(empCourseCommentDto.getCourseGradeDimensionsL5());
            if(empCourseCommentDto.getFavoriteStatus()!=null && empCourseCommentDto.getFavoriteStatus().equals("Y")){
            	 row.createCell((short) 22).setCellValue("是");
            }
//            else if(empCourseCommentDto.getFavoriteStatus()!=null && empCourseCommentDto.getFavoriteStatus().equals("N")){
//            	row.createCell((short) 23).setCellValue("否");
//            }
            else{
            	row.createCell((short) 22).setCellValue("否");
            }
            if(empCourseCommentDto.getThumbUpStatus()!=null && empCourseCommentDto.getThumbUpStatus().equals("Y")){
            	row.createCell((short) 23).setCellValue("是");
            }
//            else if(empCourseCommentDto.getThumbUpStatus()!=null && empCourseCommentDto.getThumbUpStatus().equals("N")){
//           		row.createCell((short) 24).setCellValue("否");
//            }
            else{
           		row.createCell((short) 23).setCellValue("否");
            }
            row.createCell((short) 24).setCellValue(empCourseCommentDto.getOneCourseCommentContent());
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
			NewCookie activityDeptCookie = new NewCookie("EmpCourseCommentOne","ok","/",null,null,1,false);
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
	

}
