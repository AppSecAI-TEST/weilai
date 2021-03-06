package com.chinamobo.ue.exam.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.exam.dto.ExamScoreDto;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.reportforms.dto.LearningDeptExamDto;

public interface TomExamScoreMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_SCORE
     *
     * @mbggenerated
     */
    int insert(TomExamScore record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TOM_EXAM_SCORE
     *
     * @mbggenerated
     */
    int insertSelective(TomExamScore record);

    /**
     * 
     * 功能描述：[分页查询]
     *
     * 创建者：JCX
     * 创建时间: 2016年3月18日 下午5:26:33
     * @param map
     * @return
     */
	List<ExamScoreDto> selectListByPage(Map<Object, Object> map);

	/**
	 * 
	 * 功能描述：[统计考试成绩记录数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月18日 下午5:26:49
	 * @param examId
	 * @return
	 */
	int countByExamId(int examId);

	/**
	 * 
	 * 功能描述：[根据条件查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月22日 下午3:15:44
	 * @param scoreExample
	 * @return
	 */
	List<TomExamScore> selectListByExample(TomExamScore scoreExample);
	
	/**
	 * 
	 * 功能描述：[修改]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月22日 下午3:51:55
	 * @param example
	 * @return
	 */
	int updateSelective(TomExamScore example);

	/**
	 * 
	 * 功能描述：[删除初始的默认成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月1日 上午10:15:02
	 * @param example
	 */
	void deleteByExample(TomExamScore example);
	
	List<TomExamScore> selectbyCode(String empcode);
	
	TomExamScore findByCode(Map map);

	/**
	 * 
	 * 功能描述：[查询最优成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月18日 下午7:32:05
	 * @param scoreExample
	 * @return
	 */
	List<TomExamScore> selectMax(TomExamScore scoreExample);

	/**
	 * 
	 * 功能描述：[计算考试次数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月18日 下午8:39:44
	 * @param scoreExample
	 * @return
	 */
	int countByExample(TomExamScore scoreExample);

	/**
	 * 
	 * 功能描述：[条件查询,忽略未审阅完成的成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月19日 上午10:44:57
	 * @param scoreExample
	 * @return
	 */
	TomExamScore selectOneByExample(TomExamScore scoreExample);
	
	TomExamScore findByCodeExamId(Map map);

	List<TomExamScore> selectByExamId(Integer examId);

	/**
	 * 
	 * 功能描述：[查询模板列表]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月12日 下午5:19:33
	 * @param examId
	 * @return
	 */
	List<TomExamScore> selectExcelListByPage(int examId);

	/**
	 * 
	 * 功能描述：[查询现有成绩，包括未出成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月24日 下午3:52:00
	 * @param scoreExample
	 * @return
	 */
	TomExamScore selectOneLast(TomExamScore scoreExample);

	/**
	 * 
	 * 功能描述：[查询线下考试已导入次数]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月19日 下午4:45:24
	 * @param examId
	 * @return
	 */
	int selectMaxExamCount(int examId);
	
	//查询活动结束前的最优考试信息
	TomExamScore selectOne(Map<Object, Object> map);
	int countPassExam (Map<Object, Object> map);
	
	List<TomExamScore> selectRetakingCount (Map<Object, Object> map);
	TomExamScore selectFirst(TomExamScore scoreExample);
	TomExamScore max(TomExamScore scoreExample);
	TomExamScore examMax(TomExamScore scoreExample);
	
	List<LearningDeptExamDto> learningDeptExamList(Map<Object, Object> map);
	int count(Map<Object, Object> map);
	LearningDeptExamDto learningDeptExam(Map<Object, Object> map);
	
	//根据课程id和人员查考试是否完成
	int selectByExamCode(Map<Object, Object> map);
	//根据考试id和人员查考试是否通过
	int countByCodeE(Map<Object, Object> map);
	/**
	 * 
	 * 功能描述：[查全部数据]
	 * 创建者：Acemon
	 * 创建时间：2017年7月28日
	 */
	List<TomExamScore> selectAll();
}
