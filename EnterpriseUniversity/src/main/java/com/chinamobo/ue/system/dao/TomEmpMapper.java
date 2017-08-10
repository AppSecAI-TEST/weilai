package com.chinamobo.ue.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.reportforms.dto.EmpActivityDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningActionDto;
import com.chinamobo.ue.reportforms.dto.EmpLearningTimeDto;
import com.chinamobo.ue.system.dto.TomEmpDto;
import com.chinamobo.ue.system.dto.TomSendMessage;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomEmpOne;

public interface TomEmpMapper {
    int deleteByPrimaryKey(String code);

    int insert(TomEmp record);

    void insertSelective(TomEmpOne tomEmpOne);

    TomEmp selectByPrimaryKey(String code);
    
    TomEmpOne selectByPrimaryKeys(String code);

    TomEmp selectByEmail(String email);
    
    void updateByPrimaryKeySelective(TomEmpOne tomEmpOne);

    int updateByPrimaryKey(TomEmp record);
    
    List<TomEmp> selectByMany(Map <Object,Object> map);
    
    List<TomEmp> selectByWxDept(Map <Object,Object> map);
    
    int countByExample(Map <Object,Object> map);

	TomEmp selectByCodeOnetoOne(String code);

	List<TomActivity> selectByEmpActivity(Map<Object,Object> map);
	
 int	CountByEmpActivity (Map<Object,Object> map );

	List<TomEmpDto> selectByEmpExam(Map<Object,Object> map);
	
	int	countByEmpExam (Map<Object,Object> map);
	
	int	countByEmpCourses (Map<Object,Object> map);
	
	List<TomEmpDto> selectByEmpCourses(Map<Object,Object> map);
	
	int insertList(List<TomEmpOne> list);
	TomEmp selectByEmp(Map <Object,Object> map);

	/**
	 * 
	 * 功能描述：[根据课程id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午2:17:44
	 * @param courseId
	 * @return
	 */
	List<TomEmp> selectListByCourseId(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[根据考试id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月7日 下午2:17:56
	 * @param id
	 * @return
	 */
	List<TomEmp> selectByExamId(int id);
	
	  List<TomEmp> selectByAdmin(Map <Object,Object> map);
	    
	    int countByAdmin(Map <Object,Object> map);

		int countByLecture(Map<Object, Object> map1);

		List<TomEmp> selectByLecture(Map<Object, Object> map);
		
		 List<TomEmp> selectEmp(Map <Object,Object> map);
		    
		    int countSelectEmp(Map <Object,Object> map);

			int selectOne(String code);

			List<TomEmpOne> selectAll();
			/**
			 * 
			 * 功能描述：[查找提示报名的活动]
			 *
			 * 创建者：cjx
			 * 创建时间: 2016年6月27日 下午3:48:59
			 * @return
			 */
			 List<TomSendMessage>	selectApplicationStart();
			 
			 List<TomSendMessage>	selectApplicationEnd();
			 
			 List<TomSendMessage>	selectActivityStart();

			List<TomSendMessage> selectActivityEnd();
			
			List<TomSendMessage> selectTaskStart(List<String> list);
			
			List<TomSendMessage> selectTaskStart2(List<String> list);
			
			List<TomSendMessage> selectTaskStart3(List<String> list);

			List<TomSendMessage> selectTaskEnd();
			
			List<TomSendMessage> selectExamBegin();

			List<TomSendMessage> selectExamEnd(List<String> list);
			
			List<TomSendMessage> selectExamBegin(List<String> list);
			
		List<TomEmp>	 selectByMessageId(int messageId);
		
		TomEmp selectCode();
		
		List<TomEmp> selectAllDept();
		
		// 线上课程开始5分钟前推送
		List<TomSendMessage> selectCourseNow();
		//必修线上考试开始5分钟前推送
		List<TomSendMessage> selectExamNow();
		//查找5分钟之内结束的课程
		List<TomSendMessage> selectCourseEnd();
		//活动
		List<TomSendMessage> selectAct( List<String> list);
		//线上课程开始前时间
		List<TomSendMessage> selectCOnline(List<String> list);

		List<TomSendMessage> selectEndAct( List<String> list);

		List<TomSendMessage> selectCOnlineEnd(List<String> list);
    //学员学习行为统计
		List<EmpLearningActionDto> selectEmpLearningAction(Map <Object,Object> map);
		int countEmpLearningAction(Map <Object,Object> map);
		int selectLoadTime(Map <Object,Object> map);
		List<EmpLearningActionDto> selectLearningActionByCode(Map <Object,Object> map);
		List<TomSendMessage> selectOfflineStart(List<String> list);
		
		List<EmpActivityDto> selectEmpActivityDto(Map <Object,Object> map);
		int countEmpActivityDto(Map <Object,Object> map);
		List<TomSendMessage> selectOfflineStart1(List<String> list);
		
		List<TomSendMessage> selectOfflineStart2(List<String> list);
		
		List<TomSendMessage> selectOfflineEnd(List<String> list);
		
		/**
		 * 功能描述 [同步员工表中的二三级部门CODE 在同步完员工和部门之后调用]
		 * 创建者 LXT
		 * 创建时间 2017年4月19日 下午3:52:01
		 * @return
		 */
		void synchronizationEmpDept();
		/**
		 * 功能描述 [同步员工表中的二三级部门NAME 同步完CODE之后执行]
		 * 创建者 LXT
		 * 创建时间 2017年4月26日 上午10:20:40
		 */
		void synchronizationEmpDeptTwo();
		/**
		 * 
		 * 功能描述：[完成活动人员信息]
		 * 创建者：Acemon
		 * 创建时间：2017年5月11日
		 */
		List<EmpLearningActionDto> selectempByCodes(Map <Object,Object> map);
		List<EmpLearningActionDto> selectFinishEmpByCode(Map <Object,Object> map);
		int countByCodes(Map <Object,Object> map);
}