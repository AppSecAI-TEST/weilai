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
import com.chinamobo.ue.reportforms.dao.LearningResourseOrgDeptExamMapper;
import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptCourseDto;
import com.chinamobo.ue.reportforms.dto.LearningResourseOrgDeptExamDto;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

/**
 * 描述 [学习资源-组织部门统计报表（线上考试）]
 * 创建者 LXT
 * 创建时间 2017年3月20日 下午7:49:35
 */
@Service
public class LearningResourseOrgDeptExamService {
	
	@Autowired
	private LearningResourseOrgDeptExamMapper learningResourseOrgDeptExamMapper;
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
	 * 创建时间 2017年3月20日 下午8:21:58
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
	public PageData<LearningResourseOrgDeptExamDto> findPageList(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String examName,String offlineExam,String examType,String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<LearningResourseOrgDeptExamDto> page = new PageData<LearningResourseOrgDeptExamDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examName", examName);
		map.put("offlineExam", offlineExam);
		map.put("examType", examType);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptExamDto> list=learningResourseOrgDeptExamMapper.findPageList(map);
		Integer count=learningResourseOrgDeptExamMapper.findCount(map);
		if (pageSize == -1) {
			//测
			pageSize = count;
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
	 * 创建时间 2017年3月20日 下午8:21:28
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
	public Response downloadLearningResourseOrgDeptExamExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String examName,String offlineExam,String examType,String examTotalTime,String orgName,String jobName,
			String oneDeptName,String twoDeptName,String threeDeptName,String code,String name,HttpServletRequest request){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("examName", examName);
		map.put("offlineExam", offlineExam);
		map.put("examType", examType);
		map.put("examTotalTime", examTotalTime);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("code", code);
		map.put("name", name);
		List<LearningResourseOrgDeptExamDto> list=learningResourseOrgDeptExamMapper.findList(map);
		
		//写入excel表格
		String fileName = "考试统计.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("考试编号");
		headers.add("考试名称");
		headers.add("考试积分");
		headers.add("考试属性");
		headers.add("考试类别");
//		headers.add("资源类别2级");
		headers.add("允许考试次数");
		headers.add("总考试次数");
		headers.add("考试人数");
		headers.add("考试时长");
		headers.add("考试及格分");
		headers.add("平均成绩");
		headers.add("平均及格率");
		headers.add("总分");
		
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
        	LearningResourseOrgDeptExamDto learningResourseOrgDeptExamDto = list.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(learningResourseOrgDeptExamDto.getExamNumber());  
            row.createCell((short) 1).setCellValue(learningResourseOrgDeptExamDto.getExamName());  
            if(learningResourseOrgDeptExamDto.getPassEb()!=null){
            	 row.createCell((short) 2).setCellValue(learningResourseOrgDeptExamDto.getPassEb());
            }else{
            	 row.createCell((short) 2).setCellValue(0);
            }
            if(learningResourseOrgDeptExamDto.getOfflineExam()!=null && "1".equals(learningResourseOrgDeptExamDto.getOfflineExam())){
            	 row.createCell((short) 3).setCellValue("线上考试");
            }else{
            	 row.createCell((short) 3).setCellValue("线下考试");
            }
            if(learningResourseOrgDeptExamDto.getExamType()!=null && "A".equals(learningResourseOrgDeptExamDto.getExamType())){
            	row.createCell((short) 4).setCellValue("培训活动考试");
            }else{
            	row.createCell((short) 4).setCellValue("独立考试");
            }
            
//            row.createCell((short) 5).setCellValue(learningResourseOrgDeptExamDto.getExamType2());
            if(learningResourseOrgDeptExamDto.getRetakingExamCount()!=null){
            	row.createCell((short) 5).setCellValue(learningResourseOrgDeptExamDto.getRetakingExamCount());
            }else{
            	row.createCell((short) 5).setCellValue(0);
            }
            if(learningResourseOrgDeptExamDto.getTotalePeopleCount()!=null){
            	row.createCell((short) 6).setCellValue(learningResourseOrgDeptExamDto.getTotalePeopleCount());
            }else{
            	row.createCell((short) 6).setCellValue(0);
            }
            if(learningResourseOrgDeptExamDto.getPeopleCount()!=null){
            	row.createCell((short) 7).setCellValue(learningResourseOrgDeptExamDto.getPeopleCount());
            }else{
            	row.createCell((short) 7).setCellValue(0);
            }
            if(learningResourseOrgDeptExamDto.getExamTime()!=null){
            	row.createCell((short) 8).setCellValue(learningResourseOrgDeptExamDto.getExamTime());
            }else{
            	row.createCell((short) 8).setCellValue(0);
            }
            if(learningResourseOrgDeptExamDto.getPassMark()!=null){
            	row.createCell((short) 9).setCellValue(learningResourseOrgDeptExamDto.getPassMark());
            }else{
            	row.createCell((short) 9).setCellValue(0);
            }
            
            if(learningResourseOrgDeptExamDto.getAvgScore()!=null){
            	 row.createCell((short) 10).setCellValue(learningResourseOrgDeptExamDto.getAvgScore());
            }else{
            	 row.createCell((short) 10).setCellValue(0);
            }
           
            row.createCell((short) 11).setCellValue(learningResourseOrgDeptExamDto.getAvgPassRate());
            if(learningResourseOrgDeptExamDto.getTotalScore()!=null){
            	 row.createCell((short) 12).setCellValue(learningResourseOrgDeptExamDto.getTotalScore());
            }else{
            	 row.createCell((short) 12).setCellValue(0);
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
			NewCookie activityDeptCookie = new NewCookie("learningResourseOrgDeptExamOne","ok","/",null,null,1,false);
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
