package com.chinamobo.ue.reportforms.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.reportforms.dao.LearningDeptExamMapper;
import com.chinamobo.ue.reportforms.dto.LearningDeptExamDto;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.ExcelUtil;

@Service
public class LearningDeptExamService {
	@Autowired
	private LearningDeptExamMapper learningDeptExamMapper;
	String filePath=Config.getProperty("file_path");
	
	public Map<String,String> getColumnMap(String queryColumnStr){
		Map<String, String> resultMap = new HashMap<String,String>();
		if(queryColumnStr != null && !"".equals(queryColumnStr)){
			String[] columnArr = queryColumnStr.split("\\|");
			for(int i = 0 ;i<columnArr.length ; i++){
				String value = columnArr[i].split(",")[1];
				resultMap.put("column"+value,value);
			}
		}
		return resultMap;
	}
	
	public PageData<LearningDeptExamDto> learningDeptExamList(int pageNum, int pageSize, String examTotalTime, String code,
			String name, String needApply, Integer activityId, String activityIn, Integer projectId, String examName,
			String orgcode, String onedeptcode, String deptcode2, String deptcode3 ,String queryColumnStr){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		PageData<LearningDeptExamDto> page = new PageData<LearningDeptExamDto>();
		//根据条件筛选活动
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("examTotalTime", examTotalTime);
		map.put("code", code);
		map.put("name", name);
		map.put("needApply", needApply);
		map.put("activityId", activityId);
		if(null!=activityIn){
			if(activityIn.equals("0")){
				map.put("activityIn", new Date());
			}else if(activityIn.equals("1")){
				map.put("activityEnd", new Date());
			}
		}
		map.put("projectId", projectId);
		map.put("examName", examName);
		map.put("orgcode", orgcode);
		map.put("onedeptcode", onedeptcode);
		map.put("deptcode2", deptcode2);
		map.put("deptcode3", deptcode3);
		map.putAll(getColumnMap(queryColumnStr));
		int count = learningDeptExamMapper.learningDeptExamCount(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);
		List<LearningDeptExamDto> learningDeptExamList = learningDeptExamMapper.learningDeptExamList(map);
		for(LearningDeptExamDto learningDeptExam:learningDeptExamList){
			if(learningDeptExam.getNeedApply().equals("N")){
				learningDeptExam.setIsRequired("必修");
				learningDeptExam.setNeedApply("否");
			}else if(learningDeptExam.getNeedApply().equals("Y")){
				learningDeptExam.setIsRequired("选修");
				learningDeptExam.setNeedApply("报名");
			}
		}		
		page.setDate(learningDeptExamList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	public String learningDeptExamExcel(List<LearningDeptExamDto> list, String fileName , String queryColumnStr){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Map<String,String> columnMap = getColumnMap(queryColumnStr);//获取自定义列;
		ExcelUtil excel = new ExcelUtil(fileName);
		Map<Integer,String> headers = new HashMap<Integer,String>();
		headers.put(0, "用户名");
		headers.put(1, "姓名");
		headers.put(2, "所属组织");
		headers.put(3, "一级部门");
		headers.put(4, "二级部门");
		headers.put(5, "三级部门");
		headers.put(6, "邮箱");
		headers.put(7, "管理角色分配");
		if(columnMap.get("column1") != null && !"".equals(columnMap.get("column1"))){
			int currentIndex = headers.size();
			headers.put(currentIndex, "资源名称");
			headers.put(currentIndex+1, "资源编号");
			headers.put(currentIndex+2, "考试总分");
			headers.put(currentIndex+3, "考试及格分");
			headers.put(currentIndex+4, "资源积分");
			headers.put(currentIndex+5, "首次考试成绩");
			headers.put(currentIndex+6, "考试成绩");
			headers.put(currentIndex+7, "是否及格");
			headers.put(currentIndex+8, "考试次数");
			headers.put(currentIndex+9, "完成时间");
			headers.put(currentIndex+10, "获得积分");
		}
		if(columnMap.get("column2") != null && !"".equals(columnMap.get("column2"))){
			int currentIndex = headers.size();
			headers.put(currentIndex, "所属活动名称");
			headers.put(currentIndex+1, "项目名称");
			headers.put(currentIndex+2, "活动编号");
			headers.put(currentIndex+3, "活动创建日期");
			headers.put(currentIndex+4, "必修/选修");
			headers.put(currentIndex+5, "是否报名");
			headers.put(currentIndex+6, "活动报名时间");
			headers.put(currentIndex+7, "活动开始时间");
			headers.put(currentIndex+8, "活动结束时间");
			headers.put(currentIndex+9, "授权日期");
		}
		excel.insertRowData(0, headers);//添加表头;
        Map<Integer,String> datas = new HashMap<Integer,String>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < list.size(); i++){
        	datas.clear();
        	LearningDeptExamDto learningDeptExam = list.get(i);
        	// 第四步，创建单元格，并设置值
        	datas.put(0, learningDeptExam.getCode());
        	datas.put(1, learningDeptExam.getName());
        	datas.put(2, learningDeptExam.getOrgName());
        	datas.put(3, learningDeptExam.getOneDeptName());
        	datas.put(4, learningDeptExam.getTwoDeptName());
        	datas.put(5, learningDeptExam.getThreeDeptName());
        	datas.put(6, learningDeptExam.getSecretEmail());
        	datas.put(7, learningDeptExam.getIsAdmin());
        	if(columnMap.get("column1") != null && !"".equals(columnMap.get("column1"))){
        		int currentIndex = datas.size();
        		datas.put(currentIndex, learningDeptExam.getExamName());
            	datas.put(currentIndex+1, learningDeptExam.getExamNumber());
            	datas.put(currentIndex+2, learningDeptExam.getExamScore()+"");
            	datas.put(currentIndex+3, learningDeptExam.getExaminationScore()+"");
            	datas.put(currentIndex+4, learningDeptExam.getPassEb()+"");
            	datas.put(currentIndex+5, learningDeptExam.getTotalPoints()+"");
            	datas.put(currentIndex+6, learningDeptExam.getTotalPoints()+"");
            	if(learningDeptExam.getGradeState().equals("Y")){
            		datas.put(currentIndex+7, "及格");
            	}else{
            		datas.put(currentIndex+7, "未及格");
            	}
            	datas.put(currentIndex+8, learningDeptExam.getExamCount()+"");
            	datas.put(currentIndex+9, format.format(learningDeptExam.getExamTotalTime()));
            	if(learningDeptExam.getGradeState().equals("Y")){
            		datas.put(currentIndex+10, String.valueOf(learningDeptExam.getPassEb()));
            	}else{
            		datas.put(currentIndex+10, "-"+learningDeptExam.getNotPassEb());
            	}
        	}
        	if(columnMap.get("column2") != null && !"".equals(columnMap.get("column2"))){
        		int currentIndex = datas.size();
	        	datas.put(currentIndex, learningDeptExam.getTheActivityName());
	        	datas.put(currentIndex+1, learningDeptExam.getParentProjectClassifyName());
	        	datas.put(currentIndex+2, learningDeptExam.getActivityNumber());
	        	if(learningDeptExam.getCreateTime()==null){
	        		datas.put(currentIndex+3, "-");
	        	}else{
	        		datas.put(currentIndex+3, format.format(learningDeptExam.getCreateTime()));
	        	}
	        	datas.put(currentIndex+4, learningDeptExam.getIsRequired());
	        	datas.put(currentIndex+5, learningDeptExam.getNeedApply());
	        	if(learningDeptExam.getApplicationStartTime()==null){
	        		datas.put(currentIndex+6, "-");
	        	}else{
	        		datas.put(currentIndex+6,format.format(learningDeptExam.getApplicationStartTime()));
	        	}
	        	if(learningDeptExam.getActivityStartTime()==null){
	        		datas.put(currentIndex+7, "-");
	        	}else{
	        		datas.put(currentIndex+7, format.format(learningDeptExam.getActivityStartTime()));
	        	}
	        	if(learningDeptExam.getActivityEndTime()==null){
	        		datas.put(currentIndex+8, "-");
	        	}else{
	        		datas.put(currentIndex+8, format.format(learningDeptExam.getActivityEndTime()));
	        	}
	        	if(learningDeptExam.getTheActivityName()==null || "-".equals(learningDeptExam.getTheActivityName())){
	        		datas.put(currentIndex+9, format.format(learningDeptExam.getExamCreateTime()));
	        	}else{
	        		datas.put(currentIndex+9, format.format(learningDeptExam.getCreateTime()));
	        	}
        	}
        	excel.insertRowData(i + 1, datas);//添加一行数据;
        }
        
        // 第六步，将文件存到指定位置  
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String path=filePath+"file/download/"+format1.format(new Date());
        excel.save(path);
        return path+"/"+fileName;
	}
}
