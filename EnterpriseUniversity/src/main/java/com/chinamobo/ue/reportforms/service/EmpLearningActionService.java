package com.chinamobo.ue.reportforms.service;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hwpf.usermodel.Fields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.dao.TomProjectClassifyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomLearnTimeLogMapper;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamAnswerDetailsMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.reportforms.dao.ReportMapper;
import com.chinamobo.ue.reportforms.dto.EmpActivityAnswerDetailDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningTimeDto;
import com.chinamobo.ue.reportforms.dto.ParamterDto;
import com.chinamobo.ue.reportforms.dto.PingtaideptDto;
import com.chinamobo.ue.reportforms.dto.StuLearnResourceDto;
import com.chinamobo.ue.statistics.entity.TomActivityStatistics;
import com.chinamobo.ue.statistics.entity.TomOpenCourseStatistic;
import com.chinamobo.ue.system.dao.TomActiveUserMapper;
import com.chinamobo.ue.system.dao.TomAdminMapper;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomAdmin;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomOrg;
import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.StringUtil;

@Service
public class EmpLearningActionService {
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomAdminMapper adminMapper;
	@Autowired
	private TomProjectClassifyMapper tomProjectClassifyMapper;
	@Autowired
	private TomActivityMapper tomActivityMapper;
	@Autowired
	private TomEbRecordMapper tomEbRecordMapper;
	@Autowired
	private TomLearnTimeLogMapper tomLearnTimeLogMapper;
	@Autowired
	private TomActiveUserMapper tomActiveUserMapper;
	@Autowired
	private TomActivityPropertyMapper tomActivityPropertyMapper;
	@Autowired
	private TomExamAnswerDetailsMapper tomExamAnswerDetailsMapper; 
	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	@Autowired
	private ReportMapper reportMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	String filePath=Config.getProperty("file_path");
	/**
	 * 创建者：Acemon
	 * 功能描述：学员学习行为统计报表
	 * 创建时间：2017-02-28
	 * @throws Exception 
	 */
	public PageData<EmpLearningActionDto> queryEmpLearningAction(int pageNum, int pageSize,String beginTimeq,String endTimeq,
		String name,String code,String threeCode,String twoCode,String onedeptname,String orgname) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpLearningActionDto> page = new PageData<EmpLearningActionDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", name);
		map.put("code", code);
		map.put("threeCode", threeCode);
		map.put("twoCode", twoCode);
		map.put("onedeptname", onedeptname);
		map.put("orgname", orgname);
		int count = tomEmpMapper.countEmpLearningAction(map);
		if (pageSize == -1) {
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);
		List<EmpLearningActionDto> list = tomEmpMapper.selectEmpLearningAction(map);
		Map<Object, Object> map1 = new HashMap<Object, Object>();
			for(EmpLearningActionDto empLearningActionDto : list){
				//判断是否是管理员
				int countAdmin = adminMapper.countByCode(empLearningActionDto.getCode());
				if(countAdmin<1){
					empLearningActionDto.setIsAdmin("-");
				}else{
					empLearningActionDto.setIsAdmin("管理员");
				}
				DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
				DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date nowDate=new Date();
				String nowStr=format1.format(nowDate);
				
				if(null==beginTimeq||"".equals(beginTimeq)){
					beginTimeq="1900-01-01"+" 00:00:00";
				}else{
					beginTimeq=beginTimeq+" 00:00:00";
				}
				map1.put("beginTimeq",format2.parse(beginTimeq));
 				if(null==endTimeq||"".equals(endTimeq)){
					endTimeq=nowStr+" 23:59:59";
				}else{
					endTimeq=endTimeq+" 23:59:59";
				}
				map1.put("endTimeq", format2.parse(endTimeq));
				map1.put("code", empLearningActionDto.getCode());
				//积分数
				int ebByDate = tomEbRecordMapper.countEbByDate(map1);
				empLearningActionDto.seteNumber(ebByDate);
				//登录时长（分钟）
				int loadTime = tomEmpMapper.selectLoadTime(map1);
					empLearningActionDto.setLoadTime(loadTime);
				//学习时长（分钟）
				int learningTime = tomLearnTimeLogMapper.countLearningTime(map1);
				if(learningTime < 60){
					empLearningActionDto.setLearningTime(0);
				}else {
					empLearningActionDto.setLearningTime(learningTime/60);
				}
				//登录次数
				int loadTimes = tomActiveUserMapper.countLoadTimes(map1);//pc
				empLearningActionDto.setSumSignInTimes(loadTimes);
				int loadTimesH5 = tomActiveUserMapper.countLoadTimesH5(map1);
				empLearningActionDto.setSumSignInTimesH5(loadTimesH5);
				//判断123级部门
				if ("".equals(empLearningActionDto.getTwoName()) || empLearningActionDto.getTwoName()==null) {
					empLearningActionDto.setTwoName("-");
				}
				if ("".equals(empLearningActionDto.getThreeName()) || empLearningActionDto.getThreeName()==null) {
					empLearningActionDto.setThreeName("-");
				}
			}
			
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
		
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：学员学习行为统计报表[复选框导出]
	 * 创建时间：2017-03-08
	 * @throws Exception 
	 */
	public List<EmpLearningActionDto> queryLearningActionBycode(String beginTimeq,String endTimeq,
			String name,String code,String deptname,String depttopname,String onedeptname,String orgname,String[] codes) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object,Object> map=new HashMap<Object, Object>();
		map.put("codes", codes);
		map.put("name", name);
		map.put("code", code);
		map.put("deptname", deptname);
		map.put("depttopname", depttopname);
		map.put("onedeptname", onedeptname);
		map.put("orgname", orgname);
		List<EmpLearningActionDto> list = tomEmpMapper.selectLearningActionByCode(map);
		Map<Object, Object> map1 = new HashMap<Object, Object>();
			for(EmpLearningActionDto empLearningActionDto : list){
				//判断是否是管理员
				int countAdmin = adminMapper.countByCode(empLearningActionDto.getCode());
				if(countAdmin<1){
					empLearningActionDto.setIsAdmin("-");
				}else{
					empLearningActionDto.setIsAdmin("管理员");
				}
				DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
				DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date nowDate=new Date();
				String nowStr=format1.format(nowDate);
				
				if(null==beginTimeq||"".equals(beginTimeq)){
					beginTimeq="1900-01-01"+" 00:00:00";
				}else{
					beginTimeq=beginTimeq+" 00:00:00";
				}
				map1.put("beginTimeq",format2.parse(beginTimeq));
 				if(null==endTimeq||"".equals(endTimeq)){
					endTimeq=nowStr+" 23:59:59";
				}else{
					endTimeq=endTimeq+" 23:59:59";
				}
				map1.put("endTimeq", format2.parse(endTimeq));
				map1.put("code", empLearningActionDto.getCode());
				//积分数
				int ebByDate = tomEbRecordMapper.countEbByDate(map1);
				empLearningActionDto.seteNumber(ebByDate);
				//登录时长
				int loadTime = tomEmpMapper.selectLoadTime(map1);
				empLearningActionDto.setLoadTime(loadTime);
				//学习时长
				int learningTime = tomLearnTimeLogMapper.countLearningTime(map1);
				if(learningTime==0 || learningTime<0){
					empLearningActionDto.setLearningTime(0);
				}else if (learningTime>0 && learningTime<60) {
					empLearningActionDto.setLearningTime(1);
				}else if (learningTime==60 || learningTime>60) {
					empLearningActionDto.setLearningTime(learningTime/60);
				}
				//登录次数
				int loadTimes = tomActiveUserMapper.countLoadTimes(map1);
				empLearningActionDto.setSumSignInTimes(loadTimes);
				//判断123级部门
				if ("".equals(empLearningActionDto.getTwoName()) || empLearningActionDto.getTwoName()==null) {
					empLearningActionDto.setTwoName("-");
				}
				if ("".equals(empLearningActionDto.getThreeName()) || empLearningActionDto.getThreeName()==null) {
					empLearningActionDto.setThreeName("-");
				}
			}
		
		return list;
	}
	/**
	 * 创建者：Acemon
	 * 功能描述：导出学员学习行为统计报表
	 * 创建时间：2017-03-08
	 */
	public String LearningActionBycodeExcel(List<EmpLearningActionDto> empLearningActionDto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("姓名");
		headers.add("所属组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("管理角色分配");
		headers.add("邮箱");
		headers.add("学习时长");
		headers.add("获得积分");
		headers.add("pc登录次数");
		headers.add("微信端登录次数");
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
        
        for (int i = 0; i < empLearningActionDto.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	EmpLearningActionDto empLearningActionDtoss = empLearningActionDto.get(i);
        	 // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(empLearningActionDtoss.getCode());  
            row.createCell((short) 1).setCellValue(empLearningActionDtoss.getName());  
            row.createCell((short) 2).setCellValue(empLearningActionDtoss.getOrgname());  
            row.createCell((short) 3).setCellValue(empLearningActionDtoss.getOnedeptname());
            row.createCell((short) 4).setCellValue(empLearningActionDtoss.getTwoName());
            row.createCell((short) 5).setCellValue(empLearningActionDtoss.getThreeName());
            row.createCell((short) 6).setCellValue(empLearningActionDtoss.getIsAdmin());
            row.createCell((short) 7).setCellValue(empLearningActionDtoss.getSecretEmail());
            row.createCell((short) 8).setCellValue(empLearningActionDtoss.getLearningTime());
            row.createCell((short) 9).setCellValue(empLearningActionDtoss.geteNumber());
            row.createCell((short) 10).setCellValue(empLearningActionDtoss.getSumSignInTimes());
            row.createCell((short) 11).setCellValue(empLearningActionDtoss.getSumSignInTimesH5());
            row.createCell((short) 12).setCellValue(empLearningActionDtoss.getLoadTime());
        }
        
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
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
	/**
	 * 创建者：Acemon
	 * 功能描述：根据项目id查找活动
	 * 创建时间：2017-03-06
	 * @return
	 */
	public List<TomActivity> findActivityName(String projectId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashMap<Object,Object> map = new HashMap<>();
		map.put("projectId",projectId);
		List<TomActivity> list = tomActivityMapper.selectByProjectId(map);
		return list;
	}
	/**
	 * 创建者：Acemon
	 *  功能描述：问卷明细
	 * 创建时间：2017-03-11
	 * @return
	 * @throws Exception 
	 */
	public PageData<EmpActivityAnswerDetailDto> findQuestionnaireDetailList(int pageNum, int pageSize,String completedTime,String examName,
			String projectId,String isRequired,String activityName,String activityStatus,
			String name,String code,String threeCode,String twoCode,String onedeptname,String orgname) throws ParseException{
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<EmpActivityAnswerDetailDto> page = new PageData<EmpActivityAnswerDetailDto>();
		Map<Object,Object> map=new HashMap<Object, Object>();
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate=new Date();
		String nowStr=format1.format(nowDate);
		if(null==completedTime||"".equals(completedTime)){
			completedTime=nowStr+" 23:59:59";
		}else{
			completedTime=completedTime+" 23:59:59";
		}
		map.put("completedTime",format2.parse(completedTime));
		map.put("name", name);
		map.put("examName", examName);
		map.put("code", code);
		map.put("threeCode", threeCode);
		map.put("twoCode", twoCode);
		map.put("onedeptname", onedeptname);
		map.put("orgname", orgname);
		map.put("projectId", projectId);
		map.put("isRequired", isRequired);
		map.put("activityName", activityName);
		map.put("activityStatus", activityStatus);
		int count = tomActivityMapper.countEmpAnswer(map);
		if(pageSize==-1){
			pageSize = count;
		}
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);
		List<EmpActivityAnswerDetailDto> list = tomActivityMapper.selectEmpAnswer(map);
		for (EmpActivityAnswerDetailDto empActivityAnswerDetailDto : list) {
			//是否管理员
			int countAdmin = adminMapper.countByCode(empActivityAnswerDetailDto.getCode());
			if(countAdmin<1){
				empActivityAnswerDetailDto.setIsAdmin("-");
			}else{
				empActivityAnswerDetailDto.setIsAdmin("管理员");
			}
			if ("A".equals(empActivityAnswerDetailDto.getExamType())) {
				//判断活动状态
				if (empActivityAnswerDetailDto.getActivityEndTime().getTime()>new Date().getTime()) {
					empActivityAnswerDetailDto.setStatus("Y");
				}else{
					empActivityAnswerDetailDto.setStatus("N");
				}
				//判断项目名称
				if (null != empActivityAnswerDetailDto.getProjectName() && !"".equals(empActivityAnswerDetailDto.getProjectName())) {
					empActivityAnswerDetailDto.setActivityType(empActivityAnswerDetailDto.getProjectName());
				}else{
					empActivityAnswerDetailDto.setProjectName("-");
				}
			}else{
				empActivityAnswerDetailDto.setProjectName("-");
				empActivityAnswerDetailDto.setActivityName("-");
				empActivityAnswerDetailDto.setActivityNumber("-");
				empActivityAnswerDetailDto.setActivityStatus("-");
				empActivityAnswerDetailDto.setStatus("-");
				empActivityAnswerDetailDto.setIsRequired("-");
				empActivityAnswerDetailDto.setNeedApply("-");
				TomExam exam = tomExamMapper.selectByPrimaryKey(empActivityAnswerDetailDto.getExamId());
				empActivityAnswerDetailDto.setCreateTime(exam.getCreateTime());
				empActivityAnswerDetailDto.setActivityStartTime(exam.getBeginTime());
				empActivityAnswerDetailDto.setActivityEndTime(exam.getEndTime());
			}
			
			//判断123级部门
			if ("".equals(empActivityAnswerDetailDto.getTwoName()) || empActivityAnswerDetailDto.getTwoName()==null) {
				empActivityAnswerDetailDto.setTwoName("-");
			}
			if ("".equals(empActivityAnswerDetailDto.getThreeName()) || empActivityAnswerDetailDto.getThreeName()==null) {
				empActivityAnswerDetailDto.setThreeName("-");
			}
			//判断问卷答案
			if ("".equals(empActivityAnswerDetailDto.getTopic()) || null == empActivityAnswerDetailDto.getTopic()) {
				empActivityAnswerDetailDto.setTopic("-");
			}
			if (!"".equals(empActivityAnswerDetailDto.getEmpAnswer()) && null != empActivityAnswerDetailDto.getEmpAnswer()) {
				String options = "";
				for (String answer : empActivityAnswerDetailDto.getEmpAnswer().split(",")) {
					TomTopicOption option=topicOptionMapper.selectByPrimaryKey(Integer.valueOf(answer));
    				if(option!=null){
    					options=options+option.getOptionName();
    				}
				}
				empActivityAnswerDetailDto.setAnswer(options);
			}else if (!"".equals(empActivityAnswerDetailDto.getSubjectiveItemAnswer()) && null != empActivityAnswerDetailDto.getSubjectiveItemAnswer()) {
				empActivityAnswerDetailDto.setAnswer(empActivityAnswerDetailDto.getSubjectiveItemAnswer());
			}else{
				empActivityAnswerDetailDto.setAnswer("-");
			}
			//管理授权名单
			if (!"".equals(empActivityAnswerDetailDto.getAdmins()) && null != empActivityAnswerDetailDto.getAdmins()) {
				String admins = "";
				String[] ids = empActivityAnswerDetailDto.getAdmins().split(",");
				for (int i=1;i<ids.length;i++) {
						TomAdmin tomAdmin = adminMapper.selectByPrimaryKey(Integer.parseInt(ids[i]));
						if (!"".equals(tomAdmin) && null != tomAdmin) {
							if (i == 1) {
								admins = tomAdmin.getName();
							}else{
								admins = admins + "," + tomAdmin.getName();
							}
						}
				}
				empActivityAnswerDetailDto.setAdmins(admins);
			}else{
				empActivityAnswerDetailDto.setAdmins("-");
			}
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
		
	}
	/**
	 * 创建者：Acemon
	 *  功能描述：导出问卷明细
	 * 创建时间：2017-03-23
	 * @return
	 */
	public String QuestionnaireDetailExcel(List<EmpActivityAnswerDetailDto> empActivityAnswerDetailDto, String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("姓名");
		headers.add("组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("管理角色分配");
		headers.add("邮箱");
		headers.add("活动编号");
		headers.add("活动名称");
		headers.add("活动类别");
		headers.add("创建时间");
		headers.add("选修/必修");
		headers.add("活动开始时间");
		headers.add("活动结束时间");
		headers.add("状态");
		headers.add("管理授权");
		headers.add("是否需要报名");
		headers.add("考试名称");
		headers.add("题号");
		headers.add("选项");
		headers.add("答案");
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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < empActivityAnswerDetailDto.size(); i++){
        	row = sheet.createRow((int) i + 1);
        	EmpActivityAnswerDetailDto answerDetail = empActivityAnswerDetailDto.get(i);
        	 // 第四步，创建单元格，并设置值  
        	row.createCell((short) 0).setCellValue(answerDetail.getCode());
            row.createCell((short) 1).setCellValue(answerDetail.getName());
            row.createCell((short) 2).setCellValue(answerDetail.getOrgname());
            row.createCell((short) 3).setCellValue(answerDetail.getOnedeptname());
            row.createCell((short) 4).setCellValue(answerDetail.getTwoName());
            row.createCell((short) 5).setCellValue(answerDetail.getThreeName());
            row.createCell((short) 6).setCellValue(answerDetail.getIsAdmin());
            row.createCell((short) 7).setCellValue(answerDetail.getSecretEmail());
            row.createCell((short) 8).setCellValue(answerDetail.getActivityNumber());
            row.createCell((short) 9).setCellValue(answerDetail.getActivityName());
            row.createCell((short) 10).setCellValue(answerDetail.getProjectName());
            row.createCell((short) 11).setCellValue(format.format(answerDetail.getCreateTime()));
            if (answerDetail.getNeedApply().equals("Y")) {//需要报名
            	answerDetail.setNeedApply("是");
            	answerDetail.setIsRequired("选修");          //选修
			}else if (answerDetail.getNeedApply().equals("N")) {
				answerDetail.setNeedApply("否");
				answerDetail.setIsRequired("必修");
			}
            row.createCell((short) 12).setCellValue(answerDetail.getIsRequired());
            row.createCell((short) 13).setCellValue(format.format(answerDetail.getActivityStartTime()));
            row.createCell((short) 14).setCellValue(format.format(answerDetail.getActivityEndTime()));
            if ("A".equals(answerDetail.getExamType())) {
            	if (answerDetail.getActivityEndTime().getTime()>new Date().getTime()) {
                	answerDetail.setStatus("进行中");
    			}else{
    				answerDetail.setStatus("已结束");
    			}
			}else{
				answerDetail.setStatus("-");
			}
            row.createCell((short) 15).setCellValue(answerDetail.getStatus());
            row.createCell((short) 16).setCellValue(answerDetail.getAdmins());
            row.createCell((short) 17).setCellValue(answerDetail.getNeedApply());
            row.createCell((short) 18).setCellValue(answerDetail.getExamName());
            row.createCell((short) 19).setCellValue(answerDetail.getTopicId());
            row.createCell((short) 20).setCellValue(answerDetail.getTopic());
            row.createCell((short) 21).setCellValue(answerDetail.getAnswer());
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
	
	
	public PingtaideptDto findActityCostList(String startTime,String endTime,String onedeptId,String deptcode,String thirdcode){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<Object,Object> map = new HashMap<Object,Object>();
		PingtaideptDto dto = new PingtaideptDto();
		NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(2);
        
		
		//dto.setEmpall(reportMapper.countAllemp(map));//总用户数
		
		if(null==startTime ||"null".equals(startTime)){
			startTime ="";
		}
		if(null==endTime || "null".equals(endTime)){
			endTime="";
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("onedept",onedeptId );
		map.put("twodept", deptcode);
		map.put("threedept", thirdcode);
		List<List<PingtaideptDto>> list =  reportMapper.selectPingtaiData(map);
		
		dto.setAllcourse(list.get(0).get(0).getAllcourse());
		if(null==list.get(0).get(0).getFinishCourse()){
			dto.setFinishCourse(0);
		}else{
			dto.setFinishCourse(list.get(0).get(0).getFinishCourse());
		}
		if(null==list.get(0).get(0).getAvgfinCourserate()){
			dto.setAvgfinCourserate("0");
		}else{
			dto.setAvgfinCourserate(nf.format(Double.valueOf(list.get(0).get(0).getAvgfinCourserate())));
		}
		if(null==list.get(3).get(0)){
			dto.setLearnAlltime(0l);
		}else{
			dto.setLearnAlltime(list.get(3).get(0).getLearnAlltime());
		}
		if(null==list.get(5).get(0)){
			dto.setAllEb(0);
		}else{
			dto.setAllEb(list.get(5).get(0).getAllEb());
		}
		dto.setEmpall(list.get(1).get(0).getEmpall());
		dto.setLogincount(list.get(2).get(0).getLogincount());
		dto.setH5logincount(list.get(4).get(0).getH5logincount());
		dto.setAllactivity(list.get(6).get(0).getAllactivity());
		if(null==list.get(6).get(0).getFinishActivity()){
			dto.setFinishActivity(0);
		}else{
			dto.setFinishActivity(list.get(6).get(0).getFinishActivity());
		}
		if(null==list.get(6).get(0).getAvgActivefinrate()){
			dto.setAvgActivefinrate("0");
		}else{
			Double rate = Double.valueOf(list.get(6).get(0).getAvgActivefinrate());
			dto.setAvgActivefinrate(nf.format(rate));
		}
		dto.setOfflinlogin(dto.getEmpall() - dto.getLogincount());
		if(dto.getEmpall()>0){
			dto.setOnlinerate(nf.format((double)dto.getLogincount()/dto.getEmpall()));
		}
		if(null!=dto.getLogincount() && dto.getLogincount()>0){
			dto.setAvglearntime(dto.getLearnAlltime()/dto.getLogincount());
			dto.setAvgempEb(dto.getAllEb()/dto.getLogincount());
		}else{
			dto.setAvglearntime(0);
			dto.setAvgempEb(0);
		}
		return dto;
	}
	
	public PageData<StuLearnResourceDto> stuResourceseList(int pageSize,int pageNum,ParamterDto paramter,String columes){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		
		
		
		Map<Object,Object> map = new HashMap<Object,Object>();
		/*NumberFormat nf = NumberFormat.getPercentInstance();
		NumberFormat cFormat=NumberFormat.getNumberInstance();
		cFormat.setMaximumFractionDigits(2);*/
		DecimalFormat nf = new DecimalFormat("##0.00%");
		DecimalFormat cFormat = new DecimalFormat("##0.00");
       // nf.setMaximumFractionDigits(2);
        map.put("paramter", paramter);
		int count  = reportMapper.countEmpResource(map);
		if(pageSize==-1){
			pageSize = count;
		}
		
		//map.put("startNum", pageNum);
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);
		List<StuLearnResourceDto> stulearnes = reportMapper.selectEmpResource(map);
		
		for(StuLearnResourceDto record: stulearnes){
			if(null!=record.getOnlinelearnAlltime()){
				record.setOnlinelearnAlltime(Double.valueOf(cFormat.format(record.getOnlinelearnAlltime())));//.replaceAll(",", "")
			}else{
				record.setOnlinelearnAlltime(0.00);
			}
			
			map.put("userId", record.getUserId());
			if(!"".equals(record.getThreedept()) && null!=record.getThreedept()){
				record.setTwodept(record.getDeptcode());
			}
			/*List<StuLearnResourceDto> sectionlist = reportMapper.selectempSectionDetail(map);
			double jindu =0.0;
			for(StuLearnResourceDto section : sectionlist){
				if(section.getSectionNum()!=0){
					jindu = jindu+section.getPassSectionNum()/section.getSectionNum();
				}
			}
			if(sectionlist.size()>0){
				record.setAvgOnschedule(nf.format(jindu/sectionlist.size()));
			}else{
				record.setAvgOnschedule("0%");
			}
			if(sectionlist.size()>0){
			int  finishNum = reportMapper.selectempCoursefinrate(map);
				record.setAvgOnfinishrate(nf.format((double)finishNum/(double)record.getOnlineResnum()));
			}else{
				record.setAvgOnfinishrate("0%");
			}
			if(record.getOnlineExamnum()>0){
				record.setOnExamfinishrate(nf.format((double)record.getShouldeb()/(double)record.getOnlineExamnum()));
			}else{
				record.setOnExamfinishrate("0%");
			}*/
			StuLearnResourceDto detail = reportMapper.selectEmpResourcedetail(map);
			record.setOnlineResnum(detail.getOnlineResnum());
			record.setOfflineResnum(Integer.valueOf(record.getSumResnum())-detail.getOnlineResnum());
			if(detail.getOnlineResnum()>0){
				record.setAvgOnfinishrate(nf.format((double)detail.getFinishCoursenuma()/detail.getOnlineResnum()));
			}else{
				record.setAvgOnfinishrate("0.00%");
			}
			if(detail.getAvgOnschedule()!=null){
				record.setAvgOnschedule(nf.format(Double.valueOf(detail.getAvgOnschedule())));
			}else{
				record.setAvgOnschedule("0.00%");
			}
			if(null==detail.getOnlineExamnum()){
				record.setOnlineExamnum(0);
			}else{
				record.setOnlineExamnum(detail.getOnlineExamnum());
			}
			
			if(record.getOnlineExamnum()>0){
				record.setOnExamfinishrate(nf.format((double)detail.getShouldeb()/(double)record.getOnlineExamnum()));
			}else{
				record.setOnExamfinishrate("0.00%");
			}
			StuLearnResourceDto applySign =reportMapper.selectApplyandSignNum(map);
			if(applySign.getOfflineResnum()>0){
				record.setOffcourseApplyrate(nf.format((double)applySign.getShouldeb()/(double)applySign.getOfflineResnum()));
				record.setOffcourseSign(nf.format((double)applySign.getOnlineResnum()/(double)applySign.getOfflineResnum()));
			}
			//1.必修活动内线上课程和考试
			map.put("courseId", "1");
			map.put("status", "Y");
			map.put("needApply", "N");
			map.put("courseOnline","Y" );
					 
			StuLearnResourceDto po = reportMapper.selectInActivecoursePo(map);
			if(null!=po){
				if(null != po.getNumbers()){
					record.setAvetempoa(nf.format(po.getNumbers()));
				}else{
					record.setAvetempoa("0.00%");	
				}
				if(null != po.getCourseAlltimea()){
					record.setCourseAlltimea(Double.valueOf(cFormat.format(po.getCourseAlltimea()).replaceAll(",", "")));
				}else{
					record.setCourseAlltimea(0.00);
				}
				record.setCoursenuma(po.getCoursenuma());
				record.setStartCoursenuma(po.getStartCoursenuma());
				record.setNotAtCoursenuma(po.getNotAtCoursenuma());
				record.setFinishCoursenuma(po.getFinishCoursenuma());
				if(null !=po.getAvgCoulearnTimea()){
					record.setAvgCoulearnTimea(cFormat.format(Double.valueOf(po.getAvgCoulearnTimea())));
				}else{
					record.setAvgCoulearnTimea("0.00");
				}
				if(null !=po.getAvgfinishratea()){
					record.setAvgfinishratea(nf.format(Double.valueOf(po.getAvgfinishratea())));
				}else{
					record.setAvgfinishratea("0.00%");
				}
				
			}
			map.put("examId", 1);
			map.put("courseId", null);
			StuLearnResourceDto examres = reportMapper.selectempExamDetail(map);
			record.setNeedExamnuma(examres.getNeedExamnuma());
			record.setFinExamnuma(examres.getFinExamnuma());
			record.setExampassratea(examres.getFinExamnuma().toString());
			record.setUnfinExamnuma(examres.getUnfinExamnuma());
			
			if(null!=examres.getNumbers() && 0.0<examres.getNumbers()){
				record.setAvgExampassratea(nf.format(examres.getNumbers()));
			}else{
				record.setAvgExampassratea("0.00%");
			}
			//2.选修活动内线上课程和考试
			map.put("courseId", 1);
			map.put("examId", null);
			map.put("status", "Y");
			map.put("needApply", "Y");
			map.put("courseOnline","Y" );
			po = reportMapper.selectInActivecoursePo(map);
			if(null==po.getNeedEbb()){
				po.setNeedEbb(0);
			}
			if(null!=po){
				if(null != po.getNumbers()){
					record.setAvetempob(nf.format(po.getNumbers()));
				}else{
					record.setAvetempob("0.00%");	
				}
				if(null != po.getCourseAlltimea()){
					record.setCourseAlltimeb(Double.valueOf(cFormat.format(po.getCourseAlltimea())));
				}else{
					record.setCourseAlltimeb(0.00);
				}
				record.setCoursenumb(po.getCoursenuma());
				record.setStartCoursenumb(po.getStartCoursenuma());
				record.setNotAtCoursenumb(po.getNotAtCoursenuma());
				record.setFinishCoursenumb(po.getFinishCoursenuma());
				if(null !=po.getAvgCoulearnTimea()){
					record.setAvgCoulearnTimeb(cFormat.format(Double.valueOf(po.getAvgCoulearnTimea())));
				}else{
					record.setAvgCoulearnTimeb("0.00");
				}
				if(null !=po.getAvgfinishratea()){
					record.setAvgfinishrateb(nf.format(Double.valueOf(po.getAvgfinishratea())));
				}else{
					record.setAvgfinishrateb("0.00%");
				}
				
			}
			
			map.put("examId", 1);
			map.put("courseId", null);
			StuLearnResourceDto examResb = reportMapper.selectempExamDetail(map);
			if(null ==examResb.getNeedEbb()){
				examResb.setNeedEbb(0);
			}
			record.setNeedExamnumb(examResb.getNeedExamnuma());
			record.setFinExamnumb(examResb.getFinExamnuma());
			record.setExampassrateb(examResb.getFinExamnuma().toString());
			record.setUnfinExamnumb(examResb.getUnfinExamnuma());
			if(null!=examResb.getNumbers() ){
				record.setAvgExampassrateb(nf.format(examResb.getNumbers()));
			}else{
				record.setAvgExampassrateb("0.00%");
			}
			record.setNeedEbb(examResb.getNeedEbb()+po.getNeedEbb());
			record.setGetEbb(Integer.valueOf(record.getEbNumber()));
			
			//3.非活动内线上课程和考试
			map.put("courseId",null);
			map.put("examId", null);
			map.put("status", "Y");
			//map.put("needApply", "Y");
			map.put("courseOnline","Y" );
			po = reportMapper.selectOutActivecoursePo(map);
			if(null ==po.getNeedEbb()){
				po.setNeedEbb(0);
			}
			if(null!=po){
				if(null != po.getNumbers()){
					record.setAvetempoc(nf.format(po.getNumbers()));
				}else{
					record.setAvetempoc("0.00%");	
				}
				if(null != po.getCourseAlltimea()){
					record.setCourseAlltimec(po.getCourseAlltimea());
				}else{
					record.setCourseAlltimec(0.00);
				}
				record.setCoursenumc(po.getCoursenuma());
				record.setStartCoursenumc(po.getStartCoursenuma());
				record.setNotAtCoursenumc(po.getNotAtCoursenuma());
				record.setFinishCoursenumc(po.getFinishCoursenuma());
				if(null !=po.getAvgCoulearnTimea()){
					record.setAvgCoulearnTimec(cFormat.format(Double.valueOf(po.getAvgCoulearnTimea())));
				}else{
					record.setAvgCoulearnTimec("0.00");
				}
				if(null !=po.getAvgfinishratea()){
					record.setAvgfinishratec(nf.format(Double.valueOf(po.getAvgfinishratea())));
				}else{
					record.setAvgfinishratec("0.00%");
				}
				
			}
		
			map.put("examId", 1);
			map.put("courseId", null);
			StuLearnResourceDto examResc = reportMapper.selectempOutExamDetail(map);
			if(null ==examResc.getNeedEbb()){
				examResc.setNeedEbb(0);
			}
			record.setNeedExamnumc(examResc.getNeedExamnuma());
			record.setFinExamnumc(examResc.getFinExamnuma());
			record.setExampassratec(examResc.getFinExamnuma().toString());
			record.setUnfinExamnumc(examResc.getUnfinExamnuma());
			if(null!=examResc.getNumbers() ){
				record.setAvgExampassratec(nf.format(examResc.getNumbers()));
			}else{
				record.setAvgExampassratec("0.00%");
			}
			record.setNeedEbc(examResc.getNeedEbb()+po.getNeedEbb());
			record.setGetEbc(Integer.valueOf(record.getEbNumber()));
			
			//4.必修活动内线下课程
			map.put("courseId", 1);
			map.put("examId", null);
			map.put("status", "Y");
			map.put("needApply", "N");
			map.put("courseOnline","N" );
			
			StuLearnResourceDto courseRes = reportMapper.selectempCourseDetail(map);
			record.setCoursenumd(courseRes.getCoursenumd());
			record.setOpenCoursenumd(courseRes.getOpenCoursenumd());
			record.setApplyCoursenumd(courseRes.getApplyCoursenumd());
			record.setUnappCoursenumd(courseRes.getUnappCoursenumd());
			record.setSignCoursenumd(courseRes.getSignCoursenumd());
			record.setUnsignCoursenumd(courseRes.getUnsignCoursenumd());
			if(null!=courseRes.getAvgaApprated()){
				record.setAvgaApprated(nf.format(Double.valueOf(courseRes.getAvgaApprated())));
			}else{
				record.setAvgaApprated("0.00%");
			}
			if(null!=courseRes.getAvetSignrated()){
				record.setAvetSignrated(nf.format(Double.valueOf(courseRes.getAvetSignrated())));
			}else{
				record.setAvetSignrated("0.00%");
			}
			record.setNeedEbd(courseRes.getNeedEbd());
			record.setGetEbd(Integer.valueOf(record.getEbNumber()));
			//5.选修活动内线下课程
			map.put("courseId", 1);
			map.put("examId", null);
			map.put("status", "Y");
			map.put("needApply", "Y");
			map.put("courseOnline","N" );
			StuLearnResourceDto courseRese = reportMapper.selectempCourseDetail(map);
			record.setCoursenume(courseRese.getCoursenumd());
			record.setOpenCoursenume(courseRese.getOpenCoursenumd());
			record.setApplyCoursenume(courseRese.getApplyCoursenumd());
			record.setUnappCoursenume(courseRese.getUnappCoursenumd());
			record.setSignCoursenume(courseRese.getSignCoursenumd());
			record.setUnsignCoursenume(courseRese.getUnsignCoursenumd());
			if(null!=courseRese.getAvgaApprated()){
				record.setAvgaAppratee(nf.format(Double.valueOf(courseRese.getAvgaApprated())));
			}else{
				record.setAvgaAppratee("0.00%");
			}
			if(null!=courseRese.getAvetSignrated()){
				record.setAvetSignratee(nf.format(Double.valueOf(courseRese.getAvetSignrated())));
			}else{
				record.setAvetSignratee("0.00%");
			}
			record.setNeedEbe(courseRese.getNeedEbd());
			record.setGetEbe(Integer.valueOf(record.getEbNumber()));
		}
		PageData<StuLearnResourceDto> page=new PageData<StuLearnResourceDto>();
		page.setCount(count);
		page.setDate(stulearnes);
		/*if(!"".equals(columes) && !"undefind".equals(columes) && null!=columes){
			String[] fields = columes.split(",");
			List<StuLearnResourceDto> newstulearnes = new ArrayList<StuLearnResourceDto>();
			for(StuLearnResourceDto dto: stulearnes){
				StuLearnResourceDto newdto = new StuLearnResourceDto();
					Class clazz=null;
					clazz = dto.getClass();
					for(int i=0;i<fields.length;i++){
						try {
							Field field =  clazz.getDeclaredField(fields[i]);
							field.setAccessible(true);
							Object obj = field.get(dto);
							field.set(newdto, obj);
							
						} catch (NoSuchFieldException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					newdto.setUserName(dto.getUserName());
					newdto.setOrgName(dto.getOrgName());
					newdto.setOnedept(dto.getOnedept());
					newdto.setTwodept(dto.getTwodept());
					newdto.setThreedept(dto.getThreedept());
					newstulearnes.add(newdto);
					newdto=null;
			}
			page.setDate(newstulearnes);
		}*/
		
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		return page;
	}
	public String stuLearnResourceDetailExcelnew(List<StuLearnResourceDto> StuLearnResourceDto, String fileName,Map<String,String> map){
		String[] headers =  map.keySet().toString().substring(1, map.keySet().toString().indexOf(", 1")).split(",");
		String[] headerTitles =  map.keySet().toString().substring(map.keySet().toString().indexOf("1,")+2, map.keySet().toString().length()-1).split(",");
		// 第一步，创建一个webbook，对应一个Excel文件  
	    HSSFWorkbook wb = new HSSFWorkbook();  
	    // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
	    HSSFSheet sheet = wb.createSheet(fileName);  
	    // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short 
	    HSSFRow rowTitle = sheet.createRow((int) 0); 
	    HSSFRow row = sheet.createRow((int) 1);  
	    // 第四步，创建单元格，并设置值表头 设置表头居中  
	    HSSFCellStyle style = wb.createCellStyle();  
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
	    
	    HSSFCell cell ;
	    int startcell =0;
	    for(int i=0;i<headerTitles.length;i++){
	    	Short celllength= Short.valueOf(headerTitles[i].substring(headerTitles[i].indexOf("_")+1, headerTitles[i].length()));
	    	CellRangeAddress region1 = new CellRangeAddress(0, 0, (short)startcell,(short)startcell+celllength-1); //参数1：起始行 参数2：终止行 参数3：起始列    参数4：终止列 
	    	sheet.addMergedRegion(region1); 
	    	cell= rowTitle.createCell((short) startcell);  
	    	startcell = startcell+celllength;
	        cell.setCellValue(map.get(headerTitles[i].trim()));  
	        cell.setCellStyle(style); 
	    }
	   
	    for(int i=0;i<headers.length;i++){
	    	cell= row.createCell((short) i);  
	        cell.setCellValue(map.get(headers[i].trim()));  
	        cell.setCellStyle(style); 
	    }
	    int k=0;
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	    	List<StuLearnResourceDto> newstulearnes = new ArrayList<StuLearnResourceDto>();
	    	for(StuLearnResourceDto dto: StuLearnResourceDto){
	    		row = sheet.createRow((int) k + 2);
					Class clazz=null;
					clazz = dto.getClass();
					for(int i=0;i<headers.length;i++){
						try {
							Field field =  clazz.getDeclaredField(headers[i].trim());
							field.setAccessible(true);
							Object obj = field.get(dto);
							if(null==obj){
								row.createCell((short) i).setCellValue("-");
							}else{
								row.createCell((short) i).setCellValue(obj.toString());
							}
						} catch (NoSuchFieldException | SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					k++;
			}
	    	 // 第六步，将文件存到指定位置  
		    DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		    String path=filePath+"file/download/"+format1.format(new Date());
		    try{
		//    		System.out.println(File.separator);
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
	public String stuLearnResourceDetailExcel(List<StuLearnResourceDto> StuLearnResourceDto, String fileName,Map<String,String> map){
		List<String> headers=new ArrayList<String>();
		headers.add("用户名");
		headers.add("所属组织");
		headers.add("一级部门");
		headers.add("二级部门");
		headers.add("三级部门");
		headers.add("管理角色分配");
		headers.add("职务");
		headers.add("职级");
		headers.add("邮箱");
		
		headers.add("总学习资源");
		headers.add("线上学习资源");
		headers.add("线下学习资源");
		headers.add("线上考试数");
		headers.add("线上总学习时长");
		headers.add("线上平均学习进度");
		headers.add("线上平均完成率");
		headers.add("线上考试通过率");
		headers.add("线下课程报名率");
		headers.add("线下课程签到率");
		
		headers.add("被分配课程数");
		headers.add("已开始课程数");
		headers.add("未开始课程数");
		headers.add("已完成始课程数");
		headers.add("总学习时长");
		headers.add("平均学习时长");
		headers.add("平均完成率");
		headers.add("平均学习进度");
		headers.add("需参加考试数");
		headers.add("已完成考试数");
		headers.add("未完成考试数");
		headers.add("通过考试数");
		headers.add("平均考试通过率");
		
		headers.add("被分配课程数");
		headers.add("已开始课程数");
		headers.add("未开始课程数");
		headers.add("已完成始课程数");
		headers.add("总学习时长");
		headers.add("平均学习时长");
		headers.add("平均完成率");
		headers.add("平均学习进度");
		headers.add("需参加考试数");
		headers.add("已完成考试数");
		headers.add("未完成考试数");
		headers.add("通过考试数");
		headers.add("平均考试通过率");
		headers.add("需获得积分");
		headers.add("已获得积分");
//		非活动内线上课程和考试
		headers.add("被分配课程数");
		headers.add("已开始课程数");
		headers.add("未开始课程数");
		headers.add("已完成始课程数");
		headers.add("总学习时长");
		headers.add("平均学习时长");
		headers.add("平均完成率");
		headers.add("平均学习进度");
		headers.add("需参加考试数");
		headers.add("已完成考试数");
		headers.add("未完成考试数");
		headers.add("通过考试数");
		headers.add("平均考试通过率");
		headers.add("活动线上考试率");
		headers.add("活动线下课程数");
		headers.add("开放公开课");
		headers.add("需获得积分");
		headers.add("已获得积分");
//		必修活动内线下课程
		headers.add("被分配课程数");
		headers.add("被开放课程数");
		headers.add("已报名课程数");
		headers.add("未报名课程数");
		headers.add("已签到课程数");
		headers.add("平均报名率");
		headers.add("平均签到率");
		headers.add("需获得积分");
		headers.add("已获得积分");
//		选修活动内线下课程		
		headers.add("被分配课程数");
		headers.add("被开放课程数");
		headers.add("已报名课程数");
		headers.add("未报名课程数");
		headers.add("已签到课程数");
		headers.add("平均报名率");
		headers.add("平均签到率");
		headers.add("需获得积分");
		headers.add("已获得积分");
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
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    for (int i = 0; i < StuLearnResourceDto.size(); i++){
	    	row = sheet.createRow((int) i + 1);
	    	StuLearnResourceDto answerDetail = StuLearnResourceDto.get(i);
	    	 // 第四步，创建单元格，并设置值  
	    	row.createCell((short) 0).setCellValue(answerDetail.getUserName());
	        row.createCell((short) 1).setCellValue(answerDetail.getOrgName());
	        row.createCell((short) 2).setCellValue(answerDetail.getOnedept());
	        row.createCell((short) 3).setCellValue(answerDetail.getTwodept());
	        if(answerDetail.getThreedept()!=null && !"".equals(answerDetail.getThreedept())){
	        	row.createCell((short) 4).setCellValue(answerDetail.getThreedept());
	        }else{
	        	row.createCell((short) 4).setCellValue("");
	        }
	        if(answerDetail.getRolename()!=null && !"".equals(answerDetail.getRolename())){
	        	 row.createCell((short) 5).setCellValue(answerDetail.getRolename());
	        }else{
	        	row.createCell((short) 5).setCellValue("");
	        }
	        if(answerDetail.getJob()!=null && !"".equals(answerDetail.getJob())){
	        	row.createCell((short) 6).setCellValue(answerDetail.getJob());
	        }else{
	        	row.createCell((short) 6).setCellValue("");
	        }
	        if(answerDetail.getJobtier()!=null && !"".equals(answerDetail.getJobtier())){
	        	row.createCell((short) 7).setCellValue(answerDetail.getJobtier());
	        }else{
	        	row.createCell((short) 7).setCellValue("");
	        }
	        if(answerDetail.getEmpEmail()!=null && !"".equals(answerDetail.getEmpEmail())){
	        	row.createCell((short) 8).setCellValue(answerDetail.getEmpEmail());
	        }else{
	        	row.createCell((short) 8).setCellValue("");
	        }
	        if(answerDetail.getSumResnum()!=null && !"".equals(answerDetail.getSumResnum())){
	        	row.createCell((short) 9).setCellValue(answerDetail.getSumResnum());
	        }else{
	        	row.createCell((short) 9).setCellValue("");
	        }
	        if(answerDetail.getOnlineResnum()!=null && !"".equals(answerDetail.getOnlineResnum())){
	        	row.createCell((short) 10).setCellValue(answerDetail.getOnlineResnum());
	        }else{
	        	row.createCell((short) 10).setCellValue("");
	        }
	        if(answerDetail.getOfflineResnum()!=null && !"".equals(answerDetail.getOfflineResnum())){
	        	row.createCell((short) 11).setCellValue(answerDetail.getOfflineResnum());
	        }else{
	        	row.createCell((short) 11).setCellValue("");
	        }
	        if(answerDetail.getOnlineExamnum()!=null && !"".equals(answerDetail.getOnlineExamnum())){
	        	row.createCell((short) 12).setCellValue(answerDetail.getOnlineExamnum());
	        }else{
	        	row.createCell((short) 12).setCellValue("");
	        }
	        if(null != answerDetail.getOnlinelearnAlltime() && !"".equals(answerDetail.getOnlinelearnAlltime())){
	        	row.createCell((short) 13).setCellValue(answerDetail.getOnlinelearnAlltime());
	        }else{
	        	row.createCell((short) 13).setCellValue("0");
	        }
	        if(null != answerDetail.getAvgOnschedule() && !"".equals(answerDetail.getAvgOnschedule())){
	        	row.createCell((short) 14).setCellValue(answerDetail.getAvgOnschedule());
	        }else{
	        	row.createCell((short) 14).setCellValue("0%");
	        }
	        if(null != answerDetail.getAvgOnfinishrate() && !"".equals(answerDetail.getAvgOnfinishrate())){
	        	row.createCell((short) 15).setCellValue(answerDetail.getAvgOnfinishrate());
	        }else{
	        	row.createCell((short) 15).setCellValue("0%");
	        }
	        if(null != answerDetail.getOnExamfinishrate() && !"".equals(answerDetail.getOnExamfinishrate())){
	        	 row.createCell((short) 16).setCellValue(answerDetail.getOnExamfinishrate());
	        }else{
	        	row.createCell((short) 16).setCellValue("0%");
	        }
	        if(null != answerDetail.getOffcourseApplyrate() && !"".equals(answerDetail.getOffcourseApplyrate())){
	        	row.createCell((short) 17).setCellValue(answerDetail.getOffcourseApplyrate());
	        }else{
	        	row.createCell((short) 17).setCellValue("0%");
	        }
	        if(null != answerDetail.getOffcourseSign() && !"".equals(answerDetail.getOffcourseSign())){
	        	row.createCell((short) 18).setCellValue(answerDetail.getOffcourseSign());
	        }else{
	        	row.createCell((short) 18).setCellValue("0%");
	        }
	        
	        //必修活动内线上课程和考试
	        if(null != answerDetail.getCoursenuma() && !"".equals(answerDetail.getCoursenuma())){
	        	row.createCell((short) 19).setCellValue(answerDetail.getCoursenuma());
	        }else{
	        	row.createCell((short) 19).setCellValue("0%");
	        }
	        if(null != answerDetail.getStartCoursenuma() && !"".equals(answerDetail.getStartCoursenuma())){
	        	row.createCell((short) 20).setCellValue(answerDetail.getStartCoursenuma());
	        }else{
	        	row.createCell((short) 20).setCellValue("0%");
	        }
	        if(null != answerDetail.getNotAtCoursenuma() && !"".equals(answerDetail.getNotAtCoursenuma())){
	        	row.createCell((short) 21).setCellValue(answerDetail.getNotAtCoursenuma());
	        }else{
	        	row.createCell((short) 21).setCellValue("0%");
	        }
	        if(null != answerDetail.getFinishCoursenuma() && !"".equals(answerDetail.getFinishCoursenuma())){
	        	row.createCell((short) 22).setCellValue(answerDetail.getFinishCoursenuma());
	        }else{
	        	row.createCell((short) 22).setCellValue("0%");
	        }
	        if(null != answerDetail.getCourseAlltimea() && !"".equals(answerDetail.getCourseAlltimea())){
	        	row.createCell((short) 23).setCellValue(answerDetail.getCourseAlltimea());
	        }else{
	        	row.createCell((short) 23).setCellValue("0%");
	        }
	        if(null != answerDetail.getAvgCoulearnTimea() && !"".equals(answerDetail.getAvgCoulearnTimea())){
	        	row.createCell((short) 24).setCellValue(answerDetail.getAvgCoulearnTimea());
	        }else{
	        	row.createCell((short) 24).setCellValue("0");
	        }
	        if(null != answerDetail.getAvgfinishratea() && !"".equals(answerDetail.getAvgfinishratea())){
	        	row.createCell((short) 25).setCellValue(answerDetail.getAvgfinishratea());
	        }else{
	        	row.createCell((short) 25).setCellValue("0%");
	        }
	        if(null != answerDetail.getAvetempoa() && !"".equals(answerDetail.getAvetempoa())){
	        	row.createCell((short) 26).setCellValue(answerDetail.getAvetempoa());
	        }else{
	        	row.createCell((short) 26).setCellValue("0%");
	        }
	        if(null != answerDetail.getNeedExamnuma() && !"".equals(answerDetail.getNeedExamnuma())){
	        	row.createCell((short) 27).setCellValue(answerDetail.getNeedExamnuma());
	        }else{
	        	row.createCell((short) 27).setCellValue("0%");
	        }
	        if(null != answerDetail.getFinExamnuma() && !"".equals(answerDetail.getFinExamnuma())){
	        	row.createCell((short) 28).setCellValue(answerDetail.getFinExamnuma());
	        }else{
	        	row.createCell((short) 28).setCellValue("0%");
	        }
	        if(null != answerDetail.getUnfinExamnuma() && !"".equals(answerDetail.getUnfinExamnuma())){
	        	row.createCell((short) 29).setCellValue(answerDetail.getUnfinExamnuma());
	        }else{
	        	row.createCell((short) 29).setCellValue("0%");
	        }
	        if(null != answerDetail.getExampassratea() && !"".equals(answerDetail.getExampassratea())){
	        	row.createCell((short) 30).setCellValue(answerDetail.getExampassratea());
	        }else{
	        	row.createCell((short) 30).setCellValue("0");
	        }
	        if(null != answerDetail.getAvgExampassratea() && !"".equals(answerDetail.getAvgExampassratea())){
	        	row.createCell((short) 31).setCellValue(answerDetail.getAvgExampassratea());
	        }else{
	        	row.createCell((short) 31).setCellValue("0%");
	        }
	        //选修活动内线上课程和考试
	        if(null != answerDetail.getCoursenumb() && !"".equals(answerDetail.getCoursenumb())){
	        	row.createCell((short) 32).setCellValue(answerDetail.getCoursenumb());
	        }else{
	        	row.createCell((short) 32).setCellValue("0%");
	        }
	        if(null != answerDetail.getStartCoursenumb() && !"".equals(answerDetail.getStartCoursenumb())){
	        	row.createCell((short) 33).setCellValue(answerDetail.getStartCoursenumb());
	        }else{
	        	row.createCell((short) 33).setCellValue("0%");
	        }
	        if(null != answerDetail.getNotAtCoursenumb() && !"".equals(answerDetail.getNotAtCoursenumb())){
	        	row.createCell((short) 34).setCellValue(answerDetail.getNotAtCoursenumb());
	        }else{
	        	row.createCell((short) 34).setCellValue("0%");
	        }
	        if(null != answerDetail.getFinishCoursenumb() && !"".equals(answerDetail.getFinishCoursenumb())){
	        	row.createCell((short) 35).setCellValue(answerDetail.getFinishCoursenumb());
	        }else{
	        	row.createCell((short) 35).setCellValue("0%");
	        }

	        if(null != answerDetail.getCourseAlltimeb() && !"".equals(answerDetail.getCourseAlltimeb())){
	        	row.createCell((short) 36).setCellValue(answerDetail.getCourseAlltimeb());
	        }else{
	        	row.createCell((short) 36).setCellValue("0");
	        }
	        
	        if(null != answerDetail.getAvgCoulearnTimeb() && !"".equals(answerDetail.getAvgCoulearnTimeb())){
	        	row.createCell((short) 37).setCellValue(answerDetail.getAvgCoulearnTimeb());
	        }else{
	        	row.createCell((short) 37).setCellValue("0");
	        }
	        if(null != answerDetail.getAvgfinishrateb() && !"".equals(answerDetail.getAvgfinishrateb())){
	        	row.createCell((short) 38).setCellValue(answerDetail.getAvgfinishrateb());
	        }else{
	        	row.createCell((short) 38).setCellValue("0%");
	        }
	        if(null != answerDetail.getAvetempob() && !"".equals(answerDetail.getAvetempob())){
	        	row.createCell((short) 39).setCellValue(answerDetail.getAvetempob());
	        }else{
	        	row.createCell((short) 39).setCellValue("0%");
	        }
	        if(null != answerDetail.getNeedExamnumb() && !"".equals(answerDetail.getNeedExamnumb())){
	        	row.createCell((short) 40).setCellValue(answerDetail.getNeedExamnumb());
	        }else{
	        	row.createCell((short) 40).setCellValue("0%");
	        }
	        if(null != answerDetail.getFinExamnumb() && !"".equals(answerDetail.getFinExamnumb())){
	        	row.createCell((short) 41).setCellValue(answerDetail.getFinExamnumb());
	        }else{
	        	row.createCell((short) 41).setCellValue("0");
	        }
	        
	        if(null != answerDetail.getUnfinExamnumb() && !"".equals(answerDetail.getUnfinExamnumb())){
	        	row.createCell((short) 42).setCellValue(answerDetail.getUnfinExamnumb());
	        }else{
	        	row.createCell((short) 42).setCellValue("0");
	        }
	        
	        if(null != answerDetail.getExampassrateb() && !"".equals(answerDetail.getExampassrateb())){
	        	row.createCell((short) 43).setCellValue(answerDetail.getExampassrateb());
	        }else{
	        	row.createCell((short) 43).setCellValue("0.00%");
	        }
	       /* row.createCell((short) 44).setCellValue(answerDetail.getNeedEbb());
	        row.createCell((short) 45).setCellValue(answerDetail.getGetEbb());*/
	        if(null!=answerDetail.getNeedEbb()){
	        	row.createCell((short) 44).setCellValue(answerDetail.getNeedEbb());
	        }else{
	        	row.createCell((short) 44).setCellValue(0);
	        }
	        if(null!=answerDetail.getGetEbb()){
	        	row.createCell((short) 45).setCellValue(answerDetail.getGetEbb());
	        }else{
	        	row.createCell((short) 45).setCellValue(0);
	        }
	        
	        //非活动内线上课程和考试
	        if(null != answerDetail.getCoursenumc() && !"".equals(answerDetail.getCoursenumc())){
	        	row.createCell((short) 46).setCellValue(answerDetail.getCoursenumc());
	        }else{
	        	row.createCell((short) 46).setCellValue(0);
	        }
	        if(null != answerDetail.getStartCoursenumc() && !"".equals(answerDetail.getStartCoursenumc())){
	        	row.createCell((short) 47).setCellValue(answerDetail.getStartCoursenumc());
	        }else{
	        	row.createCell((short) 47).setCellValue(0);
	        }
	        if(null != answerDetail.getNotAtCoursenumc() && !"".equals(answerDetail.getNotAtCoursenumc())){
	        	row.createCell((short) 48).setCellValue(answerDetail.getNotAtCoursenumc());
	        }else{
	        	row.createCell((short) 48).setCellValue(0);
	        }
	        if(null != answerDetail.getFinishCoursenumc() && !"".equals(answerDetail.getFinishCoursenumc())){
	        	row.createCell((short) 49).setCellValue(answerDetail.getFinishCoursenumc());
	        }else{
	        	row.createCell((short) 49).setCellValue(0);
	        }
	        if(null != answerDetail.getCourseAlltimec() && !"".equals(answerDetail.getCourseAlltimec())){
	        	row.createCell((short) 50).setCellValue(answerDetail.getCourseAlltimec());
	        }else{
	        	row.createCell((short) 50).setCellValue(0);
	        }
	        if(null != answerDetail.getAvgCoulearnTimec() && !"".equals(answerDetail.getAvgCoulearnTimec())){
	        	row.createCell((short) 51).setCellValue(answerDetail.getAvgCoulearnTimec());
	        }else{
	        	row.createCell((short) 51).setCellValue("0.00%");
	        }
	        if(null != answerDetail.getAvgfinishratec() && !"".equals(answerDetail.getAvgfinishratec())){
	        	row.createCell((short) 52).setCellValue(answerDetail.getAvgfinishratec());
	        }else{
	        	row.createCell((short) 52).setCellValue("0.00%");
	        }
	        if(null != answerDetail.getAvetempoc() && !"".equals(answerDetail.getAvetempoc())){
	        	row.createCell((short) 53).setCellValue(answerDetail.getAvetempoc());
	        }else{
	        	row.createCell((short) 53).setCellValue("0.00%");
	        }
	        if(null != answerDetail.getNeedExamnumc() && !"".equals(answerDetail.getNeedExamnumc())){
	        	row.createCell((short) 54).setCellValue(answerDetail.getNeedExamnumc());
	        }else{
	        	row.createCell((short) 54).setCellValue("0");
	        }
	        if(null != answerDetail.getFinExamnumc() && !"".equals(answerDetail.getFinExamnumc())){
	        	row.createCell((short) 55).setCellValue(answerDetail.getFinExamnumc());
	        }else{
	        	row.createCell((short) 55).setCellValue("0");
	        }
	        if(null != answerDetail.getUnfinExamnumc() && !"".equals(answerDetail.getUnfinExamnumc())){
	        	row.createCell((short) 56).setCellValue(answerDetail.getUnfinExamnumc());
	        }else{
	        	row.createCell((short) 56).setCellValue("0");
	        }
	        if(null != answerDetail.getExampassratec() && !"".equals(answerDetail.getExampassratec())){
	        	row.createCell((short) 57).setCellValue(answerDetail.getExampassratec());
	        }else{
	        	row.createCell((short) 57).setCellValue("0.00%");
	        }
	        if(null != answerDetail.getActiveOnlineExamNum() && !"".equals(answerDetail.getActiveOnlineExamNum())){
	        	row.createCell((short) 58).setCellValue(answerDetail.getActiveOnlineExamNum());
	        }else{
	        	row.createCell((short) 58).setCellValue("0.00");
	        }
	        if(null != answerDetail.getActiveOffcourseNum() && !"".equals(answerDetail.getActiveOffcourseNum())){
	        	row.createCell((short) 59).setCellValue(answerDetail.getActiveOffcourseNum());
	        }else{
	        	row.createCell((short) 59).setCellValue("0.00");
	        }
	        if(null != answerDetail.getUnActiveOpencourse() && !"".equals(answerDetail.getUnActiveOpencourse())){
	        	row.createCell((short) 60).setCellValue(answerDetail.getUnActiveOpencourse());
	        }else{
	        	row.createCell((short) 60).setCellValue("0.00");
	        }
	        if(null!=answerDetail.getNeedEbc()){
	        	row.createCell((short) 61).setCellValue(answerDetail.getNeedEbc());
	        }else{
	        	row.createCell((short) 61).setCellValue(0);
	        }
	        if(null!=answerDetail.getGetEbc()){
	        	row.createCell((short) 62).setCellValue(answerDetail.getGetEbc());
	        }else{
	        	row.createCell((short) 62).setCellValue(0);
	        }
	       // 必修活动内线下课程
	        if(null != answerDetail.getCoursenumd() && !"".equals(answerDetail.getCoursenumd())){
	        	row.createCell((short) 63).setCellValue(answerDetail.getCoursenumd());
	        }else{
	        	row.createCell((short) 63).setCellValue(0);
	        }
	        if(null != answerDetail.getOpenCoursenumd() && !"".equals(answerDetail.getOpenCoursenumd())){
	        	row.createCell((short) 64).setCellValue(answerDetail.getOpenCoursenumd());
	        }else{
	        	row.createCell((short) 64).setCellValue(0);
	        }
	        if(null != answerDetail.getApplyCoursenumd() && !"".equals(answerDetail.getApplyCoursenumd())){
	        	row.createCell((short) 65).setCellValue(answerDetail.getApplyCoursenumd());
	        }else{
	        	row.createCell((short) 65).setCellValue(0);
	        }
	        if(null != answerDetail.getUnappCoursenumd() && !"".equals(answerDetail.getUnappCoursenumd())){
	        	row.createCell((short) 66).setCellValue(answerDetail.getUnappCoursenumd());
	        }else{
	        	row.createCell((short) 66).setCellValue(0);
	        }
	        if(null != answerDetail.getSignCoursenumd() && !"".equals(answerDetail.getSignCoursenumd())){
	        	row.createCell((short) 67).setCellValue(answerDetail.getSignCoursenumd());
	        }else{
	        	row.createCell((short) 67).setCellValue(0);
	        }
	        if(null != answerDetail.getUnsignCoursenumd() && !"".equals(answerDetail.getUnsignCoursenumd())){
	        	row.createCell((short) 68).setCellValue(answerDetail.getUnsignCoursenumd());
	        }else{
	        	row.createCell((short) 68).setCellValue(0);
	        }
	        
	        if(null != answerDetail.getAvgaApprated() && !"".equals(answerDetail.getAvgaApprated())){
	        	row.createCell((short) 69).setCellValue(answerDetail.getAvgaApprated());
	        }else{
	        	row.createCell((short) 69).setCellValue("0.00%");
	        }
	        if(null != answerDetail.getAvetSignrated() && !"".equals(answerDetail.getAvetSignrated())){
	        	row.createCell((short) 70).setCellValue(answerDetail.getAvetSignrated());
	        }else{
	        	row.createCell((short) 70).setCellValue("0.00%");
	        }
	        /*
	        row.createCell((short) 71).setCellValue(answerDetail.getNeedEbd());
	        row.createCell((short) 72).setCellValue(answerDetail.getGetEbd());*/
	        if(null!=answerDetail.getNeedEbd()){
	        	row.createCell((short) 71).setCellValue(answerDetail.getNeedEbd());
	        }else{
	        	row.createCell((short) 71).setCellValue(0);
	        }
	        if(null!=answerDetail.getGetEbd()){
	        	row.createCell((short) 72).setCellValue(answerDetail.getGetEbd());
	        }else{
	        	row.createCell((short) 72).setCellValue(0);
	        }
	     // 选修修活动内线下课程
	        if(null != answerDetail.getCoursenume() && !"".equals(answerDetail.getCoursenume())){
	        	row.createCell((short) 73).setCellValue(answerDetail.getCoursenume());
	        }else{
	        	row.createCell((short) 73).setCellValue("0");
	        }
	        if(null != answerDetail.getOpenCoursenume() && !"".equals(answerDetail.getOpenCoursenume())){
	        	row.createCell((short) 74).setCellValue(answerDetail.getOpenCoursenume());
	        }else{
	        	row.createCell((short) 74).setCellValue("0");
	        }
	        if(null != answerDetail.getApplyCoursenume() && !"".equals(answerDetail.getApplyCoursenume())){
	        	row.createCell((short) 75).setCellValue(answerDetail.getApplyCoursenume());
	        }else{
	        	row.createCell((short) 75).setCellValue("0");
	        }
	        if(null != answerDetail.getUnappCoursenume() && !"".equals(answerDetail.getUnappCoursenume())){
	        	row.createCell((short) 76).setCellValue(answerDetail.getUnappCoursenume());
	        }else{
	        	row.createCell((short) 76).setCellValue("0");
	        }
	        if(null != answerDetail.getSignCoursenume() && !"".equals(answerDetail.getSignCoursenume())){
	        	row.createCell((short) 77).setCellValue(answerDetail.getSignCoursenume());
	        }else{
	        	row.createCell((short) 77).setCellValue("0");
	        }
	        if(null != answerDetail.getUnsignCoursenume() && !"".equals(answerDetail.getUnsignCoursenume())){
	        	row.createCell((short) 78).setCellValue(answerDetail.getUnsignCoursenume());
	        }else{
	        	row.createCell((short) 78).setCellValue("0");
	        }
	        if(null != answerDetail.getAvgaAppratee() && !"".equals(answerDetail.getAvgaAppratee())){
	        	row.createCell((short) 79).setCellValue(answerDetail.getAvgaAppratee());
	        }else{
	        	row.createCell((short) 79).setCellValue("0.00%");
	        }
	        if(null != answerDetail.getAvetSignratee() && !"".equals(answerDetail.getAvetSignratee())){
	        	row.createCell((short) 80).setCellValue(answerDetail.getAvetSignratee());
	        }else{
	        	row.createCell((short) 80).setCellValue("0.00%");
	        }
	        if(null!=answerDetail.getNeedEbe()){
	        	row.createCell((short) 81).setCellValue(answerDetail.getNeedEbe());
	        }else{
	        	row.createCell((short) 81).setCellValue(0.00);
	        }
	        if(null!=answerDetail.getGetEbe()){
	        	row.createCell((short) 82).setCellValue(answerDetail.getGetEbe());
	        }else{
	        	row.createCell((short) 82).setCellValue(0.00);
	        }
	       /* row.createCell((short) 81).setCellValue(answerDetail.getNeedEbe());
	        row.createCell((short) 82).setCellValue(answerDetail.getGetEbe());*/
	       
	        
	    }
	    // 第六步，将文件存到指定位置  
	    DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
	    String path=filePath+"file/download/"+format1.format(new Date());
	    try{
	//    		System.out.println(File.separator);
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
	 * 功能描述：[平台导出]
	 * 创建者：Acemon
	 * 创建时间：2017年3月27日
	 */
	public String downLoadPingtaiExcel(PingtaideptDto dto,String fileName){
		List<String> headers=new ArrayList<String>();
		headers.add("总用户数");
		headers.add("登录人数");
		headers.add("移动端登录人数");
		headers.add("未上线人数");
		headers.add("上线率");
		headers.add("学习总时长");
		headers.add("人均学习时长");
		headers.add("人均获得学分");
		headers.add("总活动数");
		headers.add("完成活动数");
		headers.add("活动平均完成率");
		headers.add("总课程数");
		headers.add("完成课程数");
		headers.add("课程平均完成率");
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
        	 // 第四步，创建单元格，并设置值  
        	row = sheet.createRow((int) 1);
            row.createCell((short) 0).setCellValue(dto.getEmpall());  
            row.createCell((short) 1).setCellValue(dto.getLogincount());  
            row.createCell((short) 2).setCellValue(dto.getH5logincount());  
            row.createCell((short) 3).setCellValue(dto.getOfflinlogin());
            row.createCell((short) 4).setCellValue(dto.getOnlinerate());
            row.createCell((short) 5).setCellValue(dto.getLearnAlltime());
            row.createCell((short) 6).setCellValue(dto.getAvglearntime());
           
            row.createCell((short) 7).setCellValue(dto.getAvgempEb());
            row.createCell((short) 8).setCellValue(dto.getAllactivity());
            row.createCell((short) 9).setCellValue(dto.getFinishActivity());
            if(null==dto.getAvgActivefinrate() || "".equals(dto.getAvgActivefinrate())){
            	row.createCell((short) 10).setCellValue("0.00%");
            }else{
            	row.createCell((short) 10).setCellValue(dto.getAvgActivefinrate());
            }
            if(null==dto.getAllcourse() ||"".equals(dto.getAllcourse())){
            	row.createCell((short) 11).setCellValue(0.00);
            }else{
            	row.createCell((short) 11).setCellValue(dto.getAllcourse());
            }
            row.createCell((short) 12).setCellValue(dto.getFinishCourse());
            if(null==dto.getAvgfinCourserate() || "".equals(dto.getAvgfinCourserate())){
            	row.createCell((short) 13).setCellValue("0.00%");
            }else{
            	row.createCell((short) 13).setCellValue(dto.getAvgfinCourserate());
            }
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        try{
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
	
