/**
 * 
 */
package com.chinamobo.ue.reportforms.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinamobo.ue.reportforms.dao.ActivityDeptListMapper;
import com.chinamobo.ue.reportforms.dto.ActivityDeptListDto;

/**
 * 版本: [1.0]
 * 功能说明: 提供活动组织部门相关服务;
 *
 * 作者: WChao
 * 创建时间: 2017年4月18日 上午8:45:51
 */
@Service
public class ActivityDeptListReportsService {
	public static final String COURSE_ID = "course_id";//课程ID;
	public static final String EXAM_ID = "exam_id";//考试ID;
	/**
	 * 
		 * 
		 * 功能描述：[]
		 * 初始化参数
		 * 创建者：WChao
		 * 创建时间: 2017年4月21日 上午10:06:15
		 * @param activityDeptDto
		 * @param map
	 */
	public void initParam(ActivityDeptListDto activityDeptDto , Map<Object,Object> map){
		map.clear();
		map.put("activityId", activityDeptDto.getActitvtyId());
		if(activityDeptDto.getThreeDeptCode() != null && !"".equals(activityDeptDto.getThreeDeptCode())){
			map.put("threeDeptCode", activityDeptDto.getThreeDeptCode());
		}
		else if(activityDeptDto.getTwoDeptCode() != null && !"".equals(activityDeptDto.getTwoDeptCode())){
			map.put("twoDeptCode", activityDeptDto.getTwoDeptCode());
		}
		else if(activityDeptDto.getOneDeptCode() != null && !"".equals(activityDeptDto.getOneDeptCode())){
			map.put("oneDeptCode", activityDeptDto.getOneDeptCode());
		}
	}
	/**
		 * 
		 * 功能描述：[1.获取开放人数]
		 * 创建者：WChao
		 * 创建时间: 2017年4月17日 下午3:49:23
		 * @param activityDeptList
		 * @return
	**/
	public ActivityDeptListReportsService initOpenNum(List<ActivityDeptListDto> activityDeptDetailList /*, List<ActivityDeptListDto> activityDeptList*/){
		for(ActivityDeptListDto activityDeptDetailView : activityDeptDetailList){
			activityDeptDetailView.setOpenNum(activityDeptDetailView.getCodes().split(",").length);
			if(activityDeptDetailView.getNeedApply().equals("Y")){
				activityDeptDetailView.setOpenNumber(activityDeptDetailView.getOpenNum());
			}else{
				activityDeptDetailView.setOpenNumber(0);
			}
			/*for(ActivityDeptListDto  activityDeptView: activityDeptList){
				if(activityDeptView.getActitvtyId() == activityDeptDetailView.getActitvtyId()){//判断活动ID相等
					//判断三级部门;
					if(activityDeptView.getThreeDeptCode() != null && !"".equals(activityDeptView.getThreeDeptCode())){
						if(activityDeptView.getThreeDeptCode().equals(activityDeptDetailView.getThreeDeptCode())){
							activityDeptDetailView.setOpenNum(activityDeptDetailView.getOpenNum()+1);
						}
						//判断二级部门;
					}else if(activityDeptView.getTwoDeptCode() != null && !"".equals(activityDeptView.getTwoDeptCode())){
						if(activityDeptView.getTwoDeptCode().equals(activityDeptDetailView.getTwoDeptCode())){
							activityDeptDetailView.setOpenNum(activityDeptDetailView.getOpenNum()+1);
						}
						//判断一级部门;
					}else if(activityDeptView.getOneDeptCode() != null && !"".equals(activityDeptView.getOneDeptCode())){
						if(activityDeptView.getOneDeptCode().equals(activityDeptDetailView.getOneDeptCode())){
							activityDeptDetailView.setOpenNum(activityDeptDetailView.getOpenNum()+1);
						}
					}
				}
			}*/
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[2.初始化参加课程的人数]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 上午8:54:21
		 * @param ActivityDeptListMapper
		 * @return
	 */
	public ActivityDeptListReportsService initJoinCourse(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setJoinCourse(ActivityDeptListMapper.findJoinCourseCount(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[3.初始化学习人数(学习中的人数)]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 上午8:54:21
		 * @param ActivityDeptListMapper
		 * @return
	 */
	public ActivityDeptListReportsService initIsLearningNumber(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setIsLearningNumber(ActivityDeptListMapper.findIsLearningNumber(map));
		}
		return this;
	}
	
	/*public ActivityDeptListReportsService initIsLearningNumber(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			
		}
		return this;
	}*/
	/**
	 * 
		 * 
		 * 功能描述：[4：初始化完成人数(课程)]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 上午11:06:15
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initToCourse(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setToCourse(ActivityDeptListMapper.findToCourse(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[5:总时长(课程)]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 上午11:44:53
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initTotalTime(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setTotalTime(Double.parseDouble(new DecimalFormat("#.00").format(ActivityDeptListMapper.findTotalTime(map)/60.0)));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[6:总成绩]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午1:25:23
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initScore(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setScore(ActivityDeptListMapper.findScore(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[7: 考试通过人数]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午1:39:22
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initToExam(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setToExam(ActivityDeptListMapper.findToExam(map));
		}
		return this;
	}
	/**
	 * 
		 * 功能描述：[8：考试参加人数]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午2:03:30
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initJoinExam(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setJoinExam(ActivityDeptListMapper.findJoinExam(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[9：选修开放人数]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午2:08:24
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initOpenNumber(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
//			activityDeptDto.setOpenNumber(ActivityDeptListMapper.findOpenNumber(map));
			if(activityDeptDto.getIsRequired().equals("Y")){
				activityDeptDto.setOpenNumber(activityDeptDto.getOpenNum());
			}else{
				activityDeptDto.setOpenNumber(activityDeptDto.getOpenNum());
			}
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[10：选修报名人数]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午2:08:57
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initNumberOfParticipants(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setNumberOfParticipants(ActivityDeptListMapper.findNumberOfParticipants(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[11：获得积分(课程+考试)]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午2:46:14
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initIntegral(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setIntegral(ActivityDeptListMapper.findCourseEcurrency(map)+ActivityDeptListMapper.findExamPassEB(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[12:活动学习人数]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月18日 下午3:15:38
		 * @param ActivityDeptListMapper
		 * @param map
		 * @param activityDeptDetailList
		 * @return
	 */
	public ActivityDeptListReportsService initToBe(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setToBe(ActivityDeptListMapper.findToBe(map));
		}
		return this;
	}
	
	public ActivityDeptListReportsService initExamCount(ActivityDeptListMapper ActivityDeptListMapper , Map<Object,Object> map ,List<ActivityDeptListDto> activityDeptDetailList){
		for(ActivityDeptListDto activityDeptDto : activityDeptDetailList){
			initParam(activityDeptDto,map);
			activityDeptDto.setExamCount(ActivityDeptListMapper.selectExam(map));
		}
		return this;
	}
	/**
	 * 
		 * 
		 * 功能描述：[13:初始化完成人数(课程+考试)]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月24日 下午3:51:43
		 * @param activityDeptListMapper
		 * @param map
		 * @param list
	 */
	public void initCompletedPersons(ActivityDeptListMapper activityDeptListMapper,Map<Object, Object> map, List<ActivityDeptListDto> list) {
		for(ActivityDeptListDto activityDeptDto : list){
			initParam(activityDeptDto, map);
			List<ActivityDeptListDto> empCourseExamList = activityDeptListMapper.findEmpActivityCourseAndExam(map);
			Map<String,Map<String,List<Integer>>> containsCourseExamInfo = cotainsCourseExamInfo(empCourseExamList);//查询当前人参与的活动课程+考试情况;
			List<Integer> courseId = null;
			List<Integer> examId  = null;
			for(String code : activityDeptDto.getCodes().split(",")){
				String key = activityDeptDto.getActitvtyId()+"_"+code;
				if(containsCourseExamInfo.get(key) != null){
					if(courseId == null)
					courseId = containsCourseExamInfo.get(key).get(COURSE_ID);//课程ID;
					if(examId == null)
					examId = containsCourseExamInfo.get(key).get(EXAM_ID);//考试ID;
				}
				
			}
		    List<ActivityDeptListDto> courseList = null;
		    List<ActivityDeptListDto> examList = null;
		    if(courseId != null){//课程完成信息;
		    	courseList = activityDeptListMapper.findToCourse1(map);//查询课程完成情况;
		    }
		    if(examId != null){//考试完成信息;
		    	examList = activityDeptListMapper.findToExam1(map);//查询考试完成情况;
		    }
		    
		    activityDeptDto.setAveragePassRate(new DecimalFormat("##0.00").format(empExamCompletedNum(activityDeptDto,examList,initActivityExamEmps(containsCourseExamInfo)))+"%");//计算考试平均通过率;
			activityDeptDto.setTo(empCourseExamCompletedNum(activityDeptDto , courseList,examList,empCourseExamList,containsCourseExamInfo));
		}
	}
	/**
	 * 
	 * 功能描述：[初始化活动下考试对应的人]
	 * 作者：WChao
	 * 创建时间：2017年6月28日 上午10:38:07
	 * @param containsCourseExamInfo
	 * @return
	 */
	public Map<String,Map<String,List<String>>> initActivityExamEmps(Map<String,Map<String,List<Integer>>> containsCourseExamInfo){
		Map<String, Map<String,List<String>>> activityExamEmps = new HashMap<String,Map<String,List<String>>>();
		for(String key : containsCourseExamInfo.keySet()){
			List<Integer> exam_ids = containsCourseExamInfo.get(key).get(EXAM_ID);
				if(exam_ids != null){
					for(Integer exam_id : exam_ids){
						String activity_id = key.split("_")[0];
						String code = key.split("_")[1];
						if(activityExamEmps.get(activity_id) == null){
							Map<String,List<String>> exam_emps = new HashMap<String,List<String>>();
							activityExamEmps.put(activity_id,exam_emps );
						}
						List<String> exam_emps = activityExamEmps.get(activity_id).get(exam_id.toString());
						if(exam_emps == null){
							List<String> codes = new ArrayList<String>();
							codes.add(code);
							activityExamEmps.get(activity_id).put(exam_id.toString(), codes);
						}else{
							exam_emps.add(code);
						}
					}
				}
		}
		return activityExamEmps;
	}
	
	public Integer empCourseExamCompletedNum(ActivityDeptListDto activityDeptDto , List<ActivityDeptListDto> courseCompletedList , List<ActivityDeptListDto> examCompletedList , List<ActivityDeptListDto> empCourseExamList ,Map<String,Map<String,List<Integer>>> containsCourseExamInfo){
		int num = 0;
		for(String codeKey : activityDeptDto.getCodes().split(",")){
			String key = activityDeptDto.getActitvtyId()+"_"+codeKey;
			Integer activityId = activityDeptDto.getActitvtyId();
			String code = codeKey;
			List<Integer> courseIds = null;
			List<Integer> examIds  = null;
			if(containsCourseExamInfo.get(key) != null){
				 courseIds = containsCourseExamInfo.get(key).get(COURSE_ID);//课程ID;
				 examIds = containsCourseExamInfo.get(key).get(EXAM_ID);//考试ID;
			}
		    boolean completed = false;
		    boolean courseCompleted = false;
	    	boolean examCompleted = false;
		    if(courseIds != null && examIds != null){
			    	//遍历课程+考试
			    	for(Integer courseId : courseIds){
			    		if(courseCompleted(courseCompletedList,activityId,code,courseId)){
			    			courseCompleted = true;
			    		}else{
			    			courseCompleted =false;
			    			break;
			    		}
			    	}
			    	//遍历考试
			    	for(Integer examId : examIds){
			    		if(examCompleted(examCompletedList,activityId,code,examId)){
			    			examCompleted = true;
			    		}else{
			    			examCompleted =false;
			    			break;
			    		}
			    	}
			    	if( courseCompleted&& examCompleted){
			    		completed = true;
			    	}
		    }else{
		    	if(courseIds != null){
		    		//遍历课程;
		    		for(Integer courseId : courseIds){
			    		if(courseCompleted(courseCompletedList,activityId,code,courseId)){
			    			courseCompleted = true;
			    		}else{
			    			courseCompleted =false;
			    			break;
			    		}
			    	}
		    		if(courseCompleted){
		    			completed = true;
		    		}
			    }
			    if(examIds != null){
			    	for(Integer examId : examIds){
			    		if(examCompleted(examCompletedList,activityId,code,examId)){
			    			examCompleted = true;
			    		}else{
			    			examCompleted =false;
			    			break;
			    		}
			    	}
			    	//遍历考试;
			    	if(examCompleted){
			    		completed = true;
			    	}
			    }
		    }
		  if(completed){
			  num++;
		  }
		}
		return num;
	}
	/**
	 * 
		 * 
		 * 功能描述：[判断该人所属活动是否完成课程]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月25日 上午9:49:31
		 * @param courseCompletedList
		 * @param activityId
		 * @param code
		 * @param courseId
		 * @return
	 */
	public boolean courseCompleted(List<ActivityDeptListDto> courseCompletedList,Integer activityId , String code,Integer courseId){
		if(courseCompletedList != null && courseCompletedList.size() > 0){
			for(ActivityDeptListDto deptDto : courseCompletedList){
//	    		if(activityId == deptDto.getActitvtyId() && code.equals(deptDto.getCode()) && courseId==deptDto.getCourseId()){
//	    			return true;
//	    		}
				if(activityId.equals(deptDto.getActitvtyId()) && code.equals(deptDto.getCode()) && courseId.equals(deptDto.getCourseId())){
	    			return true;
	    		}
	    	}
		}
		return false;
	}
	/**
	 * 
		 * 
		 * 功能描述：[判断该人所属活动是否完成考试]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月25日 上午9:48:52
		 * @param examCompletedList
		 * @param activityId
		 * @param code
		 * @param examId
		 * @return
	 */
	public boolean examCompleted(List<ActivityDeptListDto> examCompletedList,Integer activityId , String code,Integer examId){
		if(examCompletedList != null && examCompletedList.size() > 0){
			for(ActivityDeptListDto deptDto : examCompletedList){
	    		/*if(activityId==deptDto.getActitvtyId() && code.equals(deptDto.getCode()) && examId == deptDto.getExamId()){
	    			return true;
	    		}*/
				if(activityId.equals(deptDto.getActitvtyId()) && code.equals(deptDto.getCode()) && examId.equals(deptDto.getExamId())){
	    			return true;
	    		}
	    	}
		}
		return false;
	}
	/**
	 * 
		 * 
		 * 功能描述：[根据activityId_code组成key，组装该人员参与活动的课程+考试信息]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年4月25日 上午9:47:31
		 * @param empCourseExamList
		 * @return
	 */
	public Map<String,Map<String,List<Integer>>> cotainsCourseExamInfo (List<ActivityDeptListDto> empCourseExamList){
		Map<String,Map<String,List<Integer>>> rerurnMap = new HashMap<String,Map<String,List<Integer>>>();
		for(ActivityDeptListDto deptDto : empCourseExamList){
			String key = deptDto.getActitvtyId()+"_"+deptDto.getCode();
			if(rerurnMap.get(key) == null){
				rerurnMap.put(key, new HashMap<String,List<Integer>>());
			}
			if(deptDto.getCourseId() != null && !"".equals(deptDto.getCourseId())){
				if(rerurnMap.get(key).get(COURSE_ID)==null){
					rerurnMap.get(key).put(COURSE_ID,new ArrayList<Integer>());
				}
				rerurnMap.get(key).get(COURSE_ID).add(deptDto.getCourseId());
			}
			if(deptDto.getExamId() != null && !"".equals(deptDto.getExamId())){
				if(rerurnMap.get(key).get(EXAM_ID)==null){
					rerurnMap.get(key).put(EXAM_ID,new ArrayList<Integer>());
				}
				rerurnMap.get(key).get(EXAM_ID).add(deptDto.getExamId());
//				rerurnMap.get(key).put(EXAM_ID, deptDto.getExamId());
			}
		}
		return rerurnMap;
	}
	
	/*
	 * 
	 * 考试平均通过率
	 */
	public double empExamCompletedNum(ActivityDeptListDto activityDeptDto , List<ActivityDeptListDto> examCompletedList ,  Map<String,Map<String,List<String>>> activityExamEmps){
		double sum = 0.0;
		Map<String,Integer> exam_completed_num = new HashMap<String,Integer>();
		Map<String,Double> exam_completed_percent = new HashMap<String,Double>();
		if(examCompletedList != null && examCompletedList.size() > 0){
		for(ActivityDeptListDto activityDepDto : examCompletedList){
			String activity_id = Integer.toString(activityDepDto.getActitvtyId());
			Map<String,List<String>> exam_emps = activityExamEmps.get(activity_id);
			if(exam_emps != null){
				for(String exm_id : exam_emps.keySet()){
					if(exm_id.equals(Integer.toString(activityDepDto.getExamId()))){
						if(exam_completed_num.get(exm_id) == null){
							exam_completed_num.put(exm_id,1);
						}else{
							exam_completed_num.put(exm_id, exam_completed_num.get(exm_id)+1);
						}
					}
				}
			}
			for(String exam_id : exam_completed_num.keySet()){
				if(activityExamEmps.get(activity_id).get(exam_id) != null){
					Double percent = (double)exam_completed_num.get(exam_id)/(double)activityExamEmps.get(activity_id).get(exam_id).size();
					exam_completed_percent.put(exam_id, percent);
				}
			}
	/*		for(String exam_completed :exam_completed_percent.keySet()){
				sum =sum+exam_completed_percent.get(exam_completed);
			}*/
		}
		for(String exam_completed :exam_completed_percent.keySet()){
			sum =sum+exam_completed_percent.get(exam_completed);
		}
	}
		return sum/exam_completed_percent.size()*100;
	}
}


