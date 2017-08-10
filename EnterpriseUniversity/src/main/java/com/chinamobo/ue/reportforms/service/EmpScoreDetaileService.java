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
import com.chinamobo.ue.reportforms.dao.EmpScoreDetaileMapper;
import com.chinamobo.ue.reportforms.dto.EmpCourseCommentDto;
import com.chinamobo.ue.reportforms.dto.EmpScoreDetaileDto;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

/**
 * 功能描述  [学员报表-积分 业务]
 * 创建者 LXT
 * 创建时间 2017年3月14日 下午7:03:35
 */
@Service
public class EmpScoreDetaileService {

	@Autowired
	private EmpScoreDetaileMapper empScoreDetaileMapper;
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
	 * 创建时间 2017年3月18日 下午3:01:12
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
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
	public PageData<EmpScoreDetaileDto> findPageList(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String code,String name,String orgName,String jobName,
			String projectId,String oneDeptName,String twoDeptName,String threeDeptName){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpScoreDetaileDto> page = new PageData<EmpScoreDetaileDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("code", code);
		map.put("name", name);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("projectId", projectId);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		List<EmpScoreDetaileDto> list=empScoreDetaileMapper.findPageList(map);
		//取出roads 和 ebs，按“,”分割 存储到对应的积分类型中
		for(EmpScoreDetaileDto empScoreDetaileDto : list){
			//判断是否分配管理员
//			int countAdmin = adminMapper.countByCode(empScoreDetaileDto.getCode());
//			if(countAdmin<1){
//				empScoreDetaileDto.setIsAdmin("-");
//			}else{
//				empScoreDetaileDto.setIsAdmin("管理员");
//			}
			
			if(empScoreDetaileDto.getRoads()!=null && !empScoreDetaileDto.getRoads().trim().equals("") ){
				
			}else{
				continue;
			}
			String[] ssm=empScoreDetaileDto.getRoads().split("\\|");
			String[] roads=ssm[0].split(",");
			String[] ebs=ssm[1].split(",");
			for(int i=0;i<roads.length;i++){
				if(roads[i].equals("1")){
					//平台签到积分 	  1系统签到
					empScoreDetaileDto.setPlatformSignE(ebs[i]);
				}else if(roads[i].equals("2")){
					//在线活动课程积分	 2
					empScoreDetaileDto.setOnLineActivityCourseE(ebs[i]);
				}else if(roads[i].equals("3")){
					//在线活动考试积分 	3
					empScoreDetaileDto.setOnLineActivityExamE(ebs[i]);
				}else if(roads[i].equals("4")){
					//4提交问卷（总结）
					empScoreDetaileDto.setSubmitQuestionnaire(ebs[i]);
				}else if(roads[i].equals("5")){
					//课程评分 	5 课程评分
					empScoreDetaileDto.setCourseEvaluateE(ebs[i]);;
				}else if(roads[i].equals("6")){
					//	6讲师评分
					empScoreDetaileDto.setExamEvaluateE(ebs[i]);
				}else if(roads[i].equals("7")){
					// 	7讨论圈发起话题
					empScoreDetaileDto.setDiscussionTopics(ebs[i]);
				}else if(roads[i].equals("8")){
					//评论点赞积分 	8对评论点赞或评论被他人点赞
					empScoreDetaileDto.setCommentthumbUpE(ebs[i]);
				}else if(roads[i].equals("9")){
					// 	9考试未通过
					empScoreDetaileDto.setExamNotPass(ebs[i]);
				}
//				else if(roads[i].equals("10")){
//					//	登陆积分	10
//					empScoreDetaileDto.setLandingE(ebs[i]);
//				}
				else if(roads[i].equals("11")){
					//线下课程签到积分	11
					empScoreDetaileDto.setOffLineCourseSignE(ebs[i]);
				}
//				else if(roads[i].equals("18")){
//					// 完成活动积分	18
//					empScoreDetaileDto.setCompleteActivityE(ebs[i]);
//				}
				else if(roads[i].equals("13")){
					//在线公开课程积分	13
					empScoreDetaileDto.setOnLineOpenCourseE(ebs[i]);
				}else if(roads[i].equals("14")){
					//在线独立考试积分	14
					empScoreDetaileDto.setOnLineAutocephalyExamE(ebs[i]);
				}else if(roads[i].equals("15")){
					//评论积分	15
					empScoreDetaileDto.setCommentE(ebs[i]);
				}else if(roads[i].equals("16")){
					//学习状态积分 	16
					empScoreDetaileDto.setLearningState(ebs[i]);
				}else if(roads[i].equals("17")){
					//兑换商品		17
					empScoreDetaileDto.setExchangeGoods(ebs[i]);
				}else if(roads[i].equals("12")){
					//12 档案积分
					empScoreDetaileDto.setArchivesE(ebs[i]);
					
				}
			}
			
			
		}
		Integer count=empScoreDetaileMapper.findCount(map);
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
	 * 功能描述 [分页 查询个人积分明细]
	 * 创建者 LXT
	 * 创建时间 2017年3月18日 下午3:00:49
	 * @param beginTimeq
	 * @param endTimeq
	 * @param code
	 * @param name
	 * @param orgName
	 * @param jobName
	 * @param projectId
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @param empCode
	 * @return
	 */
	public PageData<EmpScoreDetaileDto> findOne(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String code,String name,String orgName,String jobName,
			String projectId,String oneDeptName,String twoDeptName,String threeDeptName,
			String empCode){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpScoreDetaileDto> page = new PageData<EmpScoreDetaileDto>();
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("code", code);
		map.put("name", name);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("projectId", projectId);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("empCode", empCode);
		List<EmpScoreDetaileDto> list=empScoreDetaileMapper.findOne(map);
		Integer count=empScoreDetaileMapper.findOneCount(map);
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
	 * 功能描述 [导出条件内的excel]
	 * 创建者 LXT
	 * 创建时间 2017年3月16日 上午9:03:05
	 * @param beginTimeq
	 * @param endTimeq
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
	public Response downloadEmpScoreDetaileExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String code,String name,String orgName,String jobName,
			String projectId,String oneDeptName,String twoDeptName,String threeDeptName,HttpServletRequest request){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("code", code);
		map.put("name", name);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("projectId", projectId);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		List<EmpScoreDetaileDto> list=empScoreDetaileMapper.findList(map);
		
		//写入excel表格
		String fileName = "积分统计.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("姓名");
		headers.add("所属组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("总积分");
		headers.add("完成活动积分");
		headers.add("在线活动课程积分");
		headers.add("在线公开课程积分");
		headers.add("在线活动考试积分");
		headers.add("在线独立考试积分");
		headers.add("评价积分");
		headers.add("评论积分");
		headers.add("登陆积分");
		headers.add("平台签到积分");
		headers.add("线下课程签到积分");
		headers.add("评论点赞积分");
		headers.add("学习状态");
		headers.add("获得积分");
		
		
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
        	EmpScoreDetaileDto empScoreDetaileDto = list.get(i);
        	if(empScoreDetaileDto.getRoads()!=null && !empScoreDetaileDto.getRoads().trim().equals("")){
        		String[] ssm=empScoreDetaileDto.getRoads().split("\\|");
    			String[] roads=ssm[0].split(",");
    			String[] ebs=ssm[1].split(",");
    			
        		//String[] roads=empScoreDetaileDto.getRoads().split(",");
    			//String[] ebs=empScoreDetaileDto.getEbs().split(",");
    			for(int j=0;j<roads.length;j++){
    				if(roads[j].equals("1")){
    					//平台签到积分 	  1系统签到
    					empScoreDetaileDto.setPlatformSignE(ebs[j]);
    				}else if(roads[j].equals("2")){
    					//在线活动课程积分	 2
    					empScoreDetaileDto.setOnLineActivityCourseE(ebs[j]);
    				}else if(roads[j].equals("3")){
    					//在线活动考试积分 	3
    					empScoreDetaileDto.setOnLineActivityExamE(ebs[j]);
    				}else if(roads[j].equals("4")){
    					//4提交问卷（总结）
    					empScoreDetaileDto.setSubmitQuestionnaire(ebs[j]);
    				}else if(roads[j].equals("5")){
    					//课程评分 	5 课程评分
    					empScoreDetaileDto.setCourseEvaluateE(ebs[j]);;
    				}else if(roads[j].equals("6")){
    					//	6讲师评分
    					empScoreDetaileDto.setExamEvaluateE(ebs[j]);
    				}else if(roads[j].equals("7")){
    					// 	7讨论圈发起话题
    					empScoreDetaileDto.setDiscussionTopics(ebs[j]);
    				}else if(roads[j].equals("8")){
    					//评论点赞积分 	8对评论点赞或评论被他人点赞
    					empScoreDetaileDto.setCommentthumbUpE(ebs[j]);
    				}else if(roads[j].equals("9")){
    					// 	9考试未通过
    					empScoreDetaileDto.setExamNotPass(ebs[j]);
    				}
//    				else if(roads[j].equals("10")){
//    					//	登陆积分	10
//    					empScoreDetaileDto.setLandingE(ebs[j]);
//    				}
    				else if(roads[j].equals("11")){
    					//线下课程签到积分	11
    					empScoreDetaileDto.setOffLineCourseSignE(ebs[j]);
    				}
//    				else if(roads[j].equals("18")){
//    					// 完成活动积分	18
//    					empScoreDetaileDto.setCompleteActivityE(ebs[j]);
//    				}
    				else if(roads[j].equals("13")){
    					//在线公开课程积分	13
    					empScoreDetaileDto.setOnLineOpenCourseE(ebs[j]);
    				}else if(roads[j].equals("14")){
    					//在线独立考试积分	14
    					empScoreDetaileDto.setOnLineAutocephalyExamE(ebs[j]);
    				}else if(roads[j].equals("15")){
    					//评论积分	15
    					empScoreDetaileDto.setCommentE(ebs[j]);
    				}else if(roads[j].equals("16")){
    					//学习状态积分 	16
    					empScoreDetaileDto.setLearningState(ebs[j]);
    				}else if(roads[j].equals("17")){
    					//兑换商品		17
    					empScoreDetaileDto.setExchangeGoods(ebs[j]);
    				}else if(roads[j].equals("12")){
    					//12 档案积分
    					empScoreDetaileDto.setArchivesE(ebs[j]);
    				}
    			}
			}
			
			
        	row = sheet.createRow((int) i + 1);
        	
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(empScoreDetaileDto.getCode());  
            row.createCell((short) 1).setCellValue(empScoreDetaileDto.getName());  
            row.createCell((short) 2).setCellValue(empScoreDetaileDto.getOrgName());  
            row.createCell((short) 3).setCellValue(empScoreDetaileDto.getOneDeptName());
            row.createCell((short) 4).setCellValue(empScoreDetaileDto.getTwoDeptName());
            row.createCell((short) 5).setCellValue(empScoreDetaileDto.getThreeDeptName());
            if(empScoreDetaileDto.getEnumber()!=null){
            	row.createCell((short) 6).setCellValue(empScoreDetaileDto.getEnumber());
            }else{
            	row.createCell((short) 6).setCellValue("");
            }
            row.createCell((short) 7).setCellValue(empScoreDetaileDto.getCompleteActivityE());
            row.createCell((short) 8).setCellValue(empScoreDetaileDto.getOnLineActivityCourseE());
            row.createCell((short) 9).setCellValue(empScoreDetaileDto.getOnLineOpenCourseE());
            row.createCell((short) 10).setCellValue(empScoreDetaileDto.getOnLineActivityExamE());
            row.createCell((short) 11).setCellValue(empScoreDetaileDto.getOnLineAutocephalyExamE());
            row.createCell((short) 12).setCellValue(empScoreDetaileDto.getEvaluateE());
            row.createCell((short) 13).setCellValue(empScoreDetaileDto.getCommentE());
            //row.createCell((short) 14).setCellValue(empScoreDetaileDto.getLandingE());
            row.createCell((short) 15).setCellValue(empScoreDetaileDto.getPlatformSignE());
            row.createCell((short) 16).setCellValue(empScoreDetaileDto.getOffLineCourseSignE());
            row.createCell((short) 17).setCellValue(empScoreDetaileDto.getCommentthumbUpE());
            row.createCell((short) 18).setCellValue(empScoreDetaileDto.getLearningState());
            if(empScoreDetaileDto.getAddUpENumber()!=null){
            	row.createCell((short) 19).setCellValue(empScoreDetaileDto.getAddUpENumber());
            }else{
            	 row.createCell((short) 19).setCellValue("");
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
			NewCookie activityDeptCookie = new NewCookie("EmpScoreDetaileOne","ok","/",null,null,1,false);
			response.cookie(activityDeptCookie);
        }catch (Exception e){
        	e.printStackTrace();
        }
		return response.build();
	}
	
	
	
	
	
	/**
	 * 功能描述 [条件 导出个人积分明细]
	 * 创建者 LXT
	 * 创建时间 2017年3月23日 上午11:32:45
	 * @param beginTimeq
	 * @param endTimeq
	 * @param code
	 * @param name
	 * @param orgName
	 * @param jobName
	 * @param projectId
	 * @param oneDeptName
	 * @param twoDeptName
	 * @param threeDeptName
	 * @param empCode
	 * @return
	 */
	public Response downloadEmpScoreDetaileOneExcel(int pageNum,int pageSize,String beginTimeq,String endTimeq,
			String code,String name,String orgName,String jobName,
			String projectId,String oneDeptName,String twoDeptName,String threeDeptName,
			String empCode,HttpServletRequest request){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//封装条件 查询结果集
		Map<String,Object> map = new HashMap<>();
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("code", code);
		map.put("name", name);
		map.put("orgName", orgName);
		map.put("jobName", jobName);
		map.put("projectId", projectId);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		map.put("empCode", empCode);
		List<EmpScoreDetaileDto> list=empScoreDetaileMapper.findOne(map);
		
		//写入excel表格
		String fileName = "学员个人积分明细.xls";
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");	
		headers.add("创建时间");
		headers.add("积分项");
		headers.add("积分");
		
		
		
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
        	EmpScoreDetaileDto empScoreDetaileDto = list.get(i);
        	
        	 // 第四步，创建单元格，并设置值  
    		row.createCell((short) 0).setCellValue(empScoreDetaileDto.getCode());
    		if(empScoreDetaileDto.getGetTime()!=null && !"".equals(empScoreDetaileDto.getGetTime())){
    			 row.createCell((short) 1).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(empScoreDetaileDto.getGetTime()));
    		}else{
    			row.createCell((short) 1).setCellValue("");  
    		}
            
            if(empScoreDetaileDto.getRoad()!=null){
	            if(empScoreDetaileDto.getRoad().equals("1")){
					//平台签到积分 	  1系统签到
	            	empScoreDetaileDto.setRoad("系统签到");
				}else if(empScoreDetaileDto.getRoad().equals("2")){
					//在线活动课程积分	 2
					empScoreDetaileDto.setRoad("在线活动课程");
				}else if(empScoreDetaileDto.getRoad().equals("3")){
					//在线活动考试积分 	3
					empScoreDetaileDto.setRoad("在线活动考试");
				}else if(empScoreDetaileDto.getRoad().equals("4")){
					// 4提交问卷（总结）
					empScoreDetaileDto.setRoad("提交问卷");
				}else if(empScoreDetaileDto.getRoad().equals("5")){
					//5课程评分
					empScoreDetaileDto.setRoad("课程评分");
				}else if(empScoreDetaileDto.getRoad().equals("6")){
					//	6讲师评分
					empScoreDetaileDto.setRoad("讲师评分");
				}else if(empScoreDetaileDto.getRoad().equals("7")){
					// 	7讨论圈发起话题
					empScoreDetaileDto.setRoad("讨论圈发起话题");
				}else if(empScoreDetaileDto.getRoad().equals("8")){
					//评论点赞积分 	8对评论点赞或评论被他人点赞
					empScoreDetaileDto.setRoad("对评论点赞或评论被他人点赞");
				}else if(empScoreDetaileDto.getRoad().equals("9")){
					// 	9考试未通过
					empScoreDetaileDto.setRoad("考试未通过");
				}else if(empScoreDetaileDto.getRoad().equals("10")){
					//	10登陆积分
					empScoreDetaileDto.setRoad("登陆");
				}else if(empScoreDetaileDto.getRoad().equals("11")){
					//11线下课程签到积分
					empScoreDetaileDto.setRoad("线下课程签到");
				}else if(empScoreDetaileDto.getRoad().equals("12")){
					//12 档案积分
					empScoreDetaileDto.setRoad("档案积分");
				}else if(empScoreDetaileDto.getRoad().equals("13")){
					// 13在线公开课积分
					empScoreDetaileDto.setRoad("在线公开课");
				}else if(empScoreDetaileDto.getRoad().equals("14")){
					// 14在线独立考试积分
					empScoreDetaileDto.setRoad("在线独立考试");
				}else if(empScoreDetaileDto.getRoad().equals("15")){
					// 15评论积分
					empScoreDetaileDto.setRoad("评论");
				}else if(empScoreDetaileDto.getRoad().equals("16")){
					// 16学习状态积分
					empScoreDetaileDto.setRoad("学习状态");
				}else if(empScoreDetaileDto.getRoad().equals("17")){
					// 17兑换商品
					empScoreDetaileDto.setRoad("兑换商品");
				}else if(empScoreDetaileDto.getRoad().equals("18")){
					// 18完成活动积分
					empScoreDetaileDto.setRoad("完成活动");
				}
            }else{
            	empScoreDetaileDto.setRoad("");
            }
            row.createCell((short) 2).setCellValue(empScoreDetaileDto.getRoad());
            row.createCell((short) 3).setCellValue(empScoreDetaileDto.getExchangeNumber());  
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
			NewCookie activityDeptCookie = new NewCookie("EmpScoreDetaileTwo","ok","/",null,null,1,false);
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
