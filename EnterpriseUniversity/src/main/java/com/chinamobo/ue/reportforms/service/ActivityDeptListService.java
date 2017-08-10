package com.chinamobo.ue.reportforms.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
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

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomProjectClassifyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.common.entity.DeptNode;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.reportforms.dao.ActivityDeptListMapper;
import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.service.DeptServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;

@Service
public class ActivityDeptListService {
		
	@Autowired
	private TomActivityMapper activityMapper;
	
	@Autowired
	private TomActivityEmpsRelationMapper activityEmpsRelationMapper;
	
	@Autowired
	private TomAdminMapper adminMapper;
	
	@Autowired
	private TomCourseLearningRecordMapper courseLearningRecordMapper;
	
	@Autowired
	private TomCourseSignInRecordsMapper courseSignInRecordsMapper;
	
	@Autowired
	private TomActivityPropertyMapper TomActivityPropertyMapper;
	
	@Autowired
	private TomCoursesMapper coursesMapper;
	
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	
	@Autowired
	private TomProjectClassifyMapper tomProjectClassifyMapper;
	
	@Autowired
	private TomActivityMapper tomActivityMapper;
	
	@Autowired
	private TomDeptMapper deptMapper;
	@Autowired
	private ActivityDeptListReportsService activityDeptListReportsService;
	@Autowired
	private DeptServise deptServise;
	
	String filePath=Config.getProperty("file_path");
	
	/*
	 * 
	 * 查询活动类型
	 */
	public List<TomProjectClassify>findACtivityType( ){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashMap<Object,Object> map = new HashMap<>();
		List<TomProjectClassify> list = tomProjectClassifyMapper.selectAllProject();
		return list;
		
	}
	/*
	 * 
	 * 查询活动名称
	 */
	public List<TomActivity> findActivityName(String projectId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashMap<Object,Object> map = new HashMap<>();
		map.put("projectId",projectId);
		List<TomActivity> list = tomActivityMapper.selectAcyivityName(map);
		return list;
	}
	
	public List<TomDept> searchDept(String code){
		List<TomDept> dept = deptMapper.selectDept2(code);
		List <TomDept>deptt=new ArrayList<TomDept>();
		String name=null;
		for(int i=0;i<dept.size();i++){
			 name=dept.get(i).getName();
			if(dept.get(i).getTopcode()!=null){
				searchDept(dept.get(i).getTopcode());
				deptt.add(dept.get(i));
			}
		}
		return deptt;
	}
	
	
	
	/**
	 * 功能描述：[活动-组织部门 ]
	 * 作者：TYX
	 * 创建时间：2017年4月7日 上午11:44:50
	 * @param pageNum
	 * @param pageSize
	 * @param beginTimeq
	 * @param endTimeq
	 * @param activityType
	 * @param activityName
	 * @param status
	 * @param isRequired
	 * @param orgname
	 * @param deptname
	 * @param depttopname
	 * @param deptthname
	 * @return
	 * @throws Exception
	 */
	@Autowired
	private ActivityDeptListMapper ActivityDeptListMapper;
	public PageData<ActivityDeptListDto> activityDeplistFrom(int pageNum, int pageSize, String beginTimeq,String endTimeq, String activityType, String activityName, String status,
			String isRequired, String orgname,String oneDeptName,String twoDeptName,String threeDeptName) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(activityName!=null){
			activityName=activityName.replaceAll("/", "//");
			activityName=activityName.replaceAll("%", "/%");
			activityName=activityName.replaceAll("_", "/_");
		}
		
		PageData<ActivityDeptListDto> page = new PageData<ActivityDeptListDto>();
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        
        
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
			endTimeq=nowStr+" 23:59:59";
		}else{
			endTimeq=endTimeq+" 23:59:59";
		}
		map.put("endTimeq", format2.parse(endTimeq));
		map.put("activityType", activityType);
		map.put("activityName", activityName);
		map.put("status", status);
		map.put("needApply", isRequired);
		/*map.put("orgname", orgname);
		map.put("deptname", deptname);
		map.put("depttopname", depttopname);
		map.put("deptthname", deptthname);*/
		map.put("orgname", orgname);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		int count = ActivityDeptListMapper.findActivityDeptCount(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<ActivityDeptListDto> list = ActivityDeptListMapper.findActivityDept(map);
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	/**
	 * 
		 * 
		 * 功能描述：[活动-组织部门--优化后]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月17日 下午3:34:52
		 * @param pageNum
		 * @param pageSize
		 * @param beginTimeq
		 * @param endTimeq
		 * @param activityType
		 * @param activityName
		 * @param status
		 * @param isRequired
		 * @param orgname
		 * @param oneDeptName
		 * @param twoDeptName
		 * @param threeDeptName
		 * @return
		 * @throws Exception
	 */
	public PageData<ActivityDeptListDto> activityDeplistFrom2(int pageNum, int pageSize, String beginTimeq,String endTimeq, String activityType, String activityName, String status,
			String isRequired, String orgname,String oneDeptName,String twoDeptName,String threeDeptName) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(activityName!=null){
			activityName=activityName.replaceAll("/", "//");
			activityName=activityName.replaceAll("%", "/%");
			activityName=activityName.replaceAll("_", "/_");
		}
		
		PageData<ActivityDeptListDto> page = new PageData<ActivityDeptListDto>();
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        
        
		Date nowDate=new Date();
		String nowStr=format1.format(nowDate);
		
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("beginTimeq", beginTimeq);
		map.put("endTimeq", endTimeq);
		map.put("activityType", activityType);
		map.put("activityName", activityName);
		map.put("status", status);
		map.put("needApply", isRequired);
		/*map.put("orgname", orgname);
		map.put("deptname", deptname);
		map.put("depttopname", depttopname);
		map.put("deptthname", deptthname);*/
		map.put("orgname", orgname);
		map.put("oneDeptName", oneDeptName);
		map.put("twoDeptName", twoDeptName);
		map.put("threeDeptName", threeDeptName);
		long start = System.currentTimeMillis();
		//DeptNode root = deptServise.getDeptNodeTree();
		int count =  ActivityDeptListMapper.findActivityDeptCount(map);
/*		List<ActivityDeptListDto> activityDeptList = ActivityDeptListMapper.findActivityDeptView(map);*/
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		List<ActivityDeptListDto> list = ActivityDeptListMapper.findActivityDept(map);
		activityDeptListReportsService
		.initOpenNum(list)//1.初始化开放人数
//		.initJoinCourse(ActivityDeptListMapper, map, list)//2.初始化参加课程的人数;
		.initIsLearningNumber(ActivityDeptListMapper, map, list)//3.初始化学习人数(学习中的人数)
//		.initToCourse(ActivityDeptListMapper, map, list)//4.初始化完成人数(课程)
		.initTotalTime(ActivityDeptListMapper, map, list)//5:初始化总时长(课程)
		.initScore(ActivityDeptListMapper, map, list)//6:初始化总成绩
		.initToExam(ActivityDeptListMapper, map, list)//7: 考试通过人数
		.initJoinExam(ActivityDeptListMapper, map, list)//8：考试参加人数
//		.initOpenNumber(ActivityDeptListMapper, map, list)//9：选修开放人数
		.initNumberOfParticipants(ActivityDeptListMapper, map, list)//10：选修报名人数
		.initIntegral(ActivityDeptListMapper, map, list)//11：获得积分(课程+考试)
		.initToBe(ActivityDeptListMapper, map, list)//12:活动学习人数
		.initExamCount(ActivityDeptListMapper, map, list)//13:活动中的课程数
		.initCompletedPersons(ActivityDeptListMapper, map, list);//初始化完成人数
//		System.out.println("总耗时:"+(System.currentTimeMillis()-start)/1000+"秒");
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	public List<ActivityDeptListDto> queryActivityDeptlistServiceByCodes(int pageNum, int pageSize, String beginTimeq,String endTimeq, String activityType, String activityName, String status,
			String isRequired, String orgname,String oneDeptName,String twoDeptName,String threeDeptName) throws Exception {
		PageData<ActivityDeptListDto> page = this.activityDeplistFrom2(1, pageSize, beginTimeq, endTimeq, activityType, activityName, status, isRequired, orgname, oneDeptName, twoDeptName, threeDeptName);
		List<ActivityDeptListDto> list = page.getData();
		return list;
	}
	
	public String ActivityDeptlistExcel(List<ActivityDeptListDto> activityDeplist,String fileName){

		List<String> headers=new ArrayList<String>();
		headers.add("组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("项目名称");
		headers.add("创建时间");
		headers.add("选修/必修");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
		headers.add("状态");
//		headers.add("管理授权");
//		headers.add("是否需要报名");
		headers.add("参加人数");
		headers.add("未学习人数");
		headers.add("学习中人数");
		headers.add("完成人数");
		headers.add("总时长（分）");
		headers.add("活动完成率");
		headers.add("考试平均分");
		headers.add("考试平均通过率");
		headers.add("选修开放人数");
		headers.add("选修报名人数");
		headers.add("选修报名率");
		headers.add("获得积分");
		headers.add("管理授权");
		
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
        
        for (int i = 0; i < activityDeplist.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	ActivityDeptListDto activityStatisticss = activityDeplist.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(activityStatisticss.getOrgname()); 
        	row.createCell((short) 1).setCellValue(activityStatisticss.getOneDeptName());
        	row.createCell((short) 2).setCellValue(activityStatisticss.getTwoDeptName());
        	row.createCell((short) 3).setCellValue(activityStatisticss.getThreeDeptName());
            row.createCell((short) 4).setCellValue(activityStatisticss.getActivityNumber());  
            row.createCell((short) 5).setCellValue(activityStatisticss.getActivityName());  
            row.createCell((short) 6).setCellValue(activityStatisticss.getActivityType());
            DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            row.createCell((short) 7).setCellValue(format2.format(activityStatisticss.getCreateTime()));
            if(activityStatisticss.getIsRequired().equals("Y")){
            	row.createCell((short) 8).setCellValue("选修"); 
            }else{
            	row.createCell((short) 8).setCellValue("必修"); 
            }
            row.createCell((short) 9).setCellValue(format2.format(activityStatisticss.getActivityStartTime()));
            row.createCell((short) 10).setCellValue(format2.format(activityStatisticss.getActivityEndTime()));
            if(activityStatisticss.getStatus().equals("1")){
            	row.createCell((short) 11).setCellValue("进行中");
            }else  if(activityStatisticss.getStatus().equals("2")){
            	row.createCell((short) 11).setCellValue("未开始");
            }else {
            	row.createCell((short) 11).setCellValue("已过期");
            }
            
//            row.createCell((short) 12).setCellValue(activityStatisticss.getAdminsName());
//            row.createCell((short) 13).setCellValue(activityStatisticss.getNeedApply());  
            if(activityStatisticss.getOpenNum()!=null){
            	row.createCell((short) 12).setCellValue(activityStatisticss.getOpenNum()); 
            }
            row.createCell((short) 13).setCellValue(activityStatisticss.getNotLearnNumber());
            if(activityStatisticss.getIsLearningNumber()!=null){
            	row.createCell((short) 14).setCellValue(activityStatisticss.getIsLearningNumber());
            }
            if(activityStatisticss.getTo()!=null ){
            	row.createCell((short) 15).setCellValue(activityStatisticss.getTo());
            }else{
            	row.createCell((short) 15).setCellValue(0);
            }
            row.createCell((short) 16).setCellValue(activityStatisticss.getTotalTime());
            row.createCell((short) 17).setCellValue(activityStatisticss.getCompletedRate());
            row.createCell((short) 18).setCellValue(activityStatisticss.getAverageScore());
            row.createCell((short) 19).setCellValue(activityStatisticss.getAveragePassRate());
            if(activityStatisticss.getOpenNumber()!=null){
            	row.createCell((short) 20).setCellValue(activityStatisticss.getOpenNumber());
            }
            if(activityStatisticss.getNumberOfParticipants()!=null){
            	row.createCell((short) 21).setCellValue(activityStatisticss.getNumberOfParticipants());
            }
            if(activityStatisticss.getNumberOfParticipantsRate()!=null){
            	row.createCell((short) 22).setCellValue(activityStatisticss.getNumberOfParticipantsRate());
            }
            if(activityStatisticss.getIntegral()!=null){
            	row.createCell((short) 23).setCellValue(activityStatisticss.getIntegral());
            }else{
            	row.createCell((short) 23).setCellValue(0);
            }
            row.createCell((short) 24).setCellValue(activityStatisticss.getAdminsName());
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
	
