package com.chinamobo.ue.api.exam.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.api.activity.service.ActivityApiService;
import com.chinamobo.ue.api.exam.dto.AnswerResults;
import com.chinamobo.ue.api.exam.dto.EmpAnswer;
import com.chinamobo.ue.api.exam.dto.ExamInfo;
import com.chinamobo.ue.api.queue.ExamRunnable;
import com.chinamobo.ue.api.queue.QueueManager;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.cache.CacheConstant;
import com.chinamobo.ue.cache.cacher.TomExamCacher;
import com.chinamobo.ue.cache.redis.RedisCacheManager;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.commodity.service.CommodityService;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.service.CourseService;
import com.chinamobo.ue.exam.dao.TomExamAnswerDetailsMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomJoinExamRecordMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.dto.MyExam;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomJoinExamRecord;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.exam.service.AnswerDetailsService;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.exam.service.TopicOptionService;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.service.UserInfoServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * 版本: [1.0] 功能说明: 考试接口service
 *
 * 作者: JCX 创建时间: 2016年3月17日 下午2:37:03
 */
@Service
public class ExamApiService {

	@Autowired
	private TomExamMapper examMapper;

	@Autowired
	private TomExamPaperMapper examPaperMapper;

	@Autowired
	private TomTopicMapper topicMapper;

	@Autowired
	private TomTopicOptionMapper topicOptionMapper;

	@Autowired
	private TomExamQuestionMapper examQuestionMapper;

	@Autowired
	private TomExamAnswerDetailsMapper examAnswerDetailsMapper;

	@Autowired
	private TomExamScoreMapper examScoreMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;

	@Autowired
	private TomUserInfoMapper userInfoMapper;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;
	@Autowired
	private ActivityApiService activityApiService;
	@Autowired
	private TomActivityPropertyMapper activityPropertyMapper;// 培训活动配置
	@Autowired
	private TomJoinExamRecordMapper joinExamRecordMapper;
	@Autowired
	private AnswerDetailsService answerDetailsService;
	@Autowired
	private UserInfoServise userInfoServise;
	@Autowired
	private CommodityService commodityService;
	@Autowired
	private TopicOptionService topicOptionService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private RedisCacheManager redisCacheManager;

	ObjectMapper mapper = new ObjectMapper();
	JsonMapper jsonMapper = new JsonMapper();

	/**
	 * 
	 * 功能描述：[展示考试列表]
	 *
	 * 创建者：JCX 创建时间: 2016年3月17日 下午2:37:18
	 * 
	 * @param request
	 * @return
	 */
	public Result eleExamList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		// examScoreService.reSetStatus(request.getParameter("userId"));//扫描所有考试将过期未参加的考试设为不合格
		PageData<TomExam> page = new PageData<TomExam>();
		String lang = request.getParameter("lang");
		String ids = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("code", request.getParameter("userId"));
		map.put("gradeState", "Y");
		List<TomExamScore> examScores = examScoreMapper.selectRetakingCount(map);
		/*
		 * List<TomExamScore>
		 * examScores=examScoreMapper.selectbyCode(request.getParameter("userId"
		 * )); for(TomExamScore examScore:examScores){
		 * if(examScoreService.getRemainingCount(request.getParameter("userId"),
		 * examScore.getExamId())<0){ ids=ids+","+examScore.getExamId(); } }
		 */
		for (int i = 0; i < examScores.size(); i++) {
			if ("D".equals(examScores.get(i).getGradeState())
					&& examScores.get(i).getTotalPoints() - examScores.get(i).getExamTotalTime() == 1) {
				examScores.get(i).setGradeState("P");
				if(examScoreService.getRemainingCount(request.getParameter("userId"),examScores.get(i).getExamId())>0){
					ids = ids +","+ examScores.get(i).getExamId();
				}
			} else if ("N".equals(examScores.get(i).getGradeState())
					&& examScores.get(i).getTotalPoints() == examScores.get(i).getExamCount()) {
				// examScores.remove(examScore);
			} else if ("N".equals(examScores.get(i).getGradeState()) && examScores.get(i).getExamCount() == 0) {

			} else {
				if(examScoreService.getRemainingCount(request.getParameter("userId"),examScores.get(i).getExamId())>0){
					ids = ids +","+ examScores.get(i).getExamId();
				}
			}
		}
		Map<Object, Object> queryMap = new HashMap<Object, Object>();
		TomExam example = new TomExam();
		String keyword = request.getParameter("keyword");
		if (keyword != null) {
			keyword = keyword.replaceAll("/", "//");
			keyword = keyword.replaceAll("%", "/%");
			keyword = keyword.replaceAll("_", "/_");
		}
		if ("en".equals(lang)) {
			example.setExamNameEn(keyword);
		} else if ("cn".equals(lang)) {
			example.setExamName(keyword);
		}
		if(ids.length()>0){
			ids=ids.substring(1);
		}else {
			ids="0";
		}
		
		queryMap.put("lang", lang);
		queryMap.put("example", example);
		queryMap.put("userId", request.getParameter("userId"));
		queryMap.put("ids", ids);
		if (request.getParameter("firstIndex") == null) {
			queryMap.put("startNum", 0);
		} else {
			queryMap.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			queryMap.put("endNum", 10);
			page.setPageSize(10);
		} else {
			queryMap.put("endNum", Integer.parseInt(request.getParameter("pageSize")));// (int)queryMap.get("startNum")+
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}

		int count = examMapper.countByUser(queryMap);
		List<TomExam> list = examMapper.selectByUser(queryMap);
		for (TomExam exam : list) {
			TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
			TomActivityProperty propertyExample = new TomActivityProperty();
			propertyExample.setExamId(exam.getExamId());
			List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(propertyExample);
			exam.setPreStatus(activityApiService.getPreStatus(activityProperties, request.getParameter("userId")));
			exam.setExamPaperPicture(examPaper.getExamPaperPicture());
			/*
			 * if(activityProperties.size()==1){
			 * exam.setActivity(activityProperties.get(0).getActivityId().
			 * toString()); }
			 */
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
			exam.setFullMark(examPaper.getFullMark());
			exam.setPassMark(examPaper.getPassMark());
			exam.setPassEb(examPaper.getPassEb());
			for (TomActivityProperty tomActivityProperty : activityProperties) {
				exam.setActivityId(tomActivityProperty.getActivityId());
			}
			/*exam.setRemainingCount(
					examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));*/
			if ("en".equals(lang)) {
				exam.setExamName(exam.getExamNameEn());
				exam.setExamAddress(exam.getExamAddressEn());
				exam.setExamPaperPicture(examPaper.getExamPaperPictureEn());
			}
			// 判断是否开考
			TomRetakingExam retakingExample = new TomRetakingExam();
			retakingExample.setExamId(exam.getExamId());
			/*if (exam.getRemainingCount() != 0) {
				//retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
				
			} else {
				retakingExample.setSort(exam.getRetakingExamCount());
			}*/
			retakingExample.setSort(exam.getRemainingCount());
			TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
			if (null != retakingExam) {
				exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
				exam.setEndTime(retakingExam.getRetakingExamEndTime());
				exam.setBeginTimeS(String.valueOf(retakingExam.getRetakingExamBeginTime().getTime()));
				exam.setEndTimeS(String.valueOf(retakingExam.getRetakingExamEndTime().getTime()));
			}
			if (exam.getBeginTime().getTime() > new Date().getTime()) {
				if ("en".equals(lang)) {
					exam.setExamState("NOT START");
				} else {
					exam.setExamState("未开始");
				}
			} else if (exam.getEndTime().getTime() < new Date().getTime()) {
				if ("N".equals(exam.getGradeState())) {
					if (examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()) > 0) {
						if ("en".equals(lang)) {
							exam.setExamState("IN PROGRESS");
						} else {
							exam.setExamState("进行中");
						}

					} else if (examScoreService.getRemainingCount(request.getParameter("userId"),
							exam.getExamId()) <= 0) {
						if ("en".equals(lang)) {
							exam.setExamState("not pass");
						} else {
							exam.setExamState("未通过");
						}
					} else {
						if ("en".equals(lang)) {
							exam.setExamState("OVERDUE");
						} else {
							exam.setExamState("已过期");
						}
					}
				} else if ("Y".equals(exam.getGradeState())) {
					if ("en".equals(lang)) {
						exam.setExamState("already passed");
					} else {
						exam.setExamState("已完成");
					}
				} else if ("D".equals(exam.getGradeState())) {
					if ("en".equals(lang)) {
						exam.setExamState("Pending examination");
					} else {
						exam.setExamState("已过期");
					}
				} else if ("P".equals(exam.getGradeState())) {
					if ("en".equals(lang)) {
						exam.setExamState("IN PROGRESS");
					} else {
						exam.setExamState("进行中");
					}
				}
			} else {
				if ("N".equals(exam.getGradeState())) {
					if (examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()) > 0) {
						if ("en".equals(lang)) {
							exam.setExamState("IN PROGRESS");
							exam.setStatus("C");
						} else {
							exam.setExamState("进行中");
							exam.setStatus("C");
						}

					} else if (examScoreService.getRemainingCount(request.getParameter("userId"),
							exam.getExamId()) <= 0) {
						if ("en".equals(lang)) {
							exam.setExamState("not pass");
							exam.setStatus("C");
						} else {
							exam.setExamState("未通过");
							exam.setStatus("C");
						}
					}
				} else if ("Y".equals(exam.getGradeState())) {
					if ("en".equals(lang)) {
						exam.setExamState("already passed");
						exam.setStatus("C");
					} else {
						exam.setExamState("已完成");
						exam.setStatus("C");
					}
				} else if ("D".equals(exam.getGradeState())) {
					if ("en".equals(lang)) {
						exam.setExamState("IN PROGRESS");
						exam.setStatus("A");
					} else {
						exam.setExamState("进行中");
						exam.setStatus("A");
					}
				} else if ("P".equals(exam.getGradeState())) {
					if ("en".equals(lang)) {
						exam.setExamState("IN PROGRESS");
						exam.setStatus("B");
					} else {
						exam.setExamState("进行中");
						exam.setStatus("B");
					}
				}
			}
			/*
			 * TomExamScore scoreExample=new TomExamScore();
			 * scoreExample.setExamId(exam.getExamId());
			 * scoreExample.setCode(request.getParameter("userId"));
			 * TomExamScore
			 * examScore=examScoreMapper.selectOneByExample(scoreExample); int
			 * examCount=examScoreMapper.countByExample(scoreExample);
			 * if(examCount==2){ if ("en".equals(lang)) { exam.setExamState(
			 * "Not reviewed"); }else{ exam.setExamState("进行中"); } }else
			 * if(examCount==1){ if (examScore.getGradeState().endsWith("N")) {
			 * if(examScoreService.getRemainingCount(request.getParameter(
			 * "userId"),exam.getExamId()) > 0){ if ("en".equals(lang)) {
			 * exam.setExamState("Not completed"); }else{
			 * exam.setExamState("进行中"); }
			 * 
			 * }else if(examScoreService.getRemainingCount(request.getParameter(
			 * "userId"),exam.getExamId()) <= 0){ if ("en".equals(lang)) {
			 * exam.setExamState("not pass"); }else{ exam.setExamState("未通过"); }
			 * }else{ if ("en".equals(lang)) { exam.setExamState("not pass");
			 * }else{ exam.setExamState("已过期"); } } } else if
			 * (examScore.getGradeState().endsWith("Y")) { if
			 * ("en".equals(lang)) { exam.setExamState("already passed"); }else{
			 * exam.setExamState("已完成"); } } else if
			 * (examScore.getGradeState().endsWith("D")) { if
			 * ("en".equals(lang)) { exam.setExamState("Pending examination");
			 * }else{ exam.setExamState("未开始"); } } }
			 */

		}
		page.setCount(count);
		page.setDate(list);
		page.setPageNum((int) queryMap.get("startNum") / (int) queryMap.get("endNum") + 1);

		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		pageJson = pageJson.replaceAll(":null", ":\"\"");

		return new Result("Y", pageJson, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[考试详情]
	 *
	 * 创建者：JCX 创建时间: 2016年3月18日 下午3:15:37
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExamDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String lang = request.getParameter("lang");
		TomExam exam = examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId")));
		TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
		exam.setTestQuestionCount(examPaper.getTestQuestionCount());
		exam.setFullMark(examPaper.getFullMark());
		exam.setPassMark(examPaper.getPassMark());
		exam.setExamPaperPicture(examPaper.getExamPaperPicture());
		exam.setPassEb(examPaper.getPassEb());
		exam.setExamTime(exam.getExamTime() * 60);
		exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"),
				Integer.parseInt(request.getParameter("examId"))));
		exam.setShowQualifiedStandard(examPaper.getShowQualifiedStandard());
		// 判断是否开考
		TomRetakingExam retakingExample = new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		if (exam.getRemainingCount() != 0) {
			retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
		} else {
			retakingExample.setSort(exam.getRetakingExamCount());
		}
		TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		if (null != retakingExam) {
			exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
			exam.setEndTime(retakingExam.getRetakingExamEndTime());
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		exam.setBeginTimeS(format.format(exam.getBeginTime()));
		exam.setEndTimeS(format.format(exam.getEndTime()));
		// 计算剩余补考次数
		// TomRetakingExam example=new TomRetakingExam();
		// example.setExamId(Integer.parseInt(request.getParameter("examId")));
		// exam.setRemainingCount(0);
		// List<TomRetakingExam>
		// retakingExams=retakingExamMapper.selectListByExample(example);
		// for(TomRetakingExam retakingExam:retakingExams){
		// if(retakingExam.getSort()!=0&&retakingExam.getRetakingExamEndTime().getTime()>new
		// Date().getTime()){
		// exam.setRemainingCount(exam.getRetakingExamCount()-retakingExam.getSort()+1);
		// break;
		// }
		// }
		if ("en".equals(lang)) {
			exam.setExamName(exam.getExamNameEn());
			exam.setExamAddress(exam.getExamAddressEn());
			exam.setExamPaperPicture(examPaper.getExamPaperPictureEn());
		}
		String json = mapper.writeValueAsString(exam);
		json = json.replaceAll(":null", ":\"\"");

		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[获取试卷]
	 *
	 * 创建者：JCX 创建时间: 2016年3月18日 下午3:15:48
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional
	public Result eleExamTopicList(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ExamInfo examInfo = new ExamInfo();
		TomExam exam = examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId")));
		String lang = request.getParameter("lang");
		// 判断是否开考
		exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));
		TomRetakingExam retakingExample = new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		if (exam.getRemainingCount() != 0) {
			retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
		} else {
			if ("en".equals(lang)) {
				return new Result("N", "", ErrorCode.API_OVERLOAD, "Not in the test time。");
			} else {
				return new Result("N", "", ErrorCode.API_OVERLOAD, "不在考试时间内。");
			}
		}
		TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		if (null != null) {
			Date nowDate = new Date();
			if (nowDate.getTime() < retakingExam.getRetakingExamBeginTime().getTime()
					|| nowDate.getTime() > retakingExam.getRetakingExamEndTime().getTime()) {
				if ("en".equals(lang)) {
					return new Result("N", "", ErrorCode.API_OVERLOAD, "Not in the test time。");
				} else {
					return new Result("N", "", ErrorCode.API_OVERLOAD, "不在考试时间内。");
				}
			}
		}
		TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
		examInfo.setExamTime(examPaper.getExamTime() * 60);
		examInfo.setTestQuestionCount(examPaper.getTestQuestionCount());
		if (examPaper.getExamPaperType().equals("1") || examPaper.getExamPaperType().equals("3")
				|| examPaper.getExamPaperType().equals("5")) {
			List<TomTopic> topics = topicMapper.selectByExamPaperId(examPaper.getExamPaperId());
			for (TomTopic topic : topics) {
				if ("en".equals(lang)) {
					topic.setTopicName(topic.getTopicNameEn());
				}
				List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
				for (TomTopicOption option : topicOptions) {
					if ("en".equals(lang)) {
						option.setOptionName(option.getOptionNameEn());
					}
				}
				if (topicOptions != null) {
					topic.setTopicOptions(topicOptions);
				}
			}
			examInfo.setTopics(topics);
		} else if (examPaper.getExamPaperType().equals("2") || examPaper.getExamPaperType().equals("4")
				|| examPaper.getExamPaperType().equals("6")) {
			List<TomExamQuestion> examQuestions = sort(
					examQuestionMapper.selectByExamPaperId(examPaper.getExamPaperId()));
			List<TomTopic> topics = new ArrayList<TomTopic>();
			for (TomExamQuestion examQuestion : examQuestions) {
				List<TomTopic> topicsPart = randomTopics(examQuestion);
				topics.addAll(topicsPart);
			}
			for (TomTopic topic : topics) {
				if ("en".equals(lang)) {
					topic.setTopicName(topic.getTopicNameEn());
				}
				List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicIdNoStatus(topic.getTopicId());
				for (TomTopicOption option : topicOptions) {
					if ("en".equals(lang)) {
						option.setOptionName(option.getOptionNameEn());
					}
				}
				if (topicOptions != null) {
					topic.setTopicOptions(topicOptions);
				}
			}
			examInfo.setTopics(topics);
		}
		// TomExamAnswerDetails answerDetails;
		// for(TomTopic topic:examInfo.getTopics()){
		// answerDetails=new TomExamAnswerDetails();
		// answerDetails.setCode(request.getParameter("userId"));
		// answerDetails.setExamId(Integer.parseInt(request.getParameter("examId")));
		// answerDetails.setTopicId(topic.getTopicId());
		// answerDetails.setTopic(topic.getTopicName());
		// answerDetails.setScore(0);
		// answerDetails.setCreateTime(new Date());
		// answerDetails.setStatus("Y");
		// answerDetails.setRightState("N");
		// examAnswerDetailsMapper.insertSelective(answerDetails);
		// }
		String json = mapper.writeValueAsString(examInfo);
		json = json.replaceAll(":null", ":\"\"");
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}
	
	/**
	 * 
	 * 功能描述：[提交试卷给非主观题打分（走缓存）]
	 * 创建者：Acemon
	 * 创建时间：2017年7月29日
	 */
	public Result eleExamSubmit(String json) throws Exception {
		System.out.println(redisCacheManager.hget(CacheConstant.TOM_TOPIC_OPTION, "272"));
	    TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		ExamBean examBean = new ExamBean();
		JSONObject jsonObject = JSONObject.parseObject(json);
		int examId = jsonObject.getIntValue("examId");
		String code = jsonObject.getString("userId");
		examBean.setData(jsonObject);
		AnswerResults answerResults = new AnswerResults();
		examBean.setAnswerResults(answerResults);
		answerResults.setExamId(examId);
		answerResults.setUserId(code);
		answerResults.setExamTotalTime(jsonObject.getIntValue("examTotalTime"));
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		// 判断是否开考
		//TomExam exam = examMapper.selectByPrimaryKey(jsonObject.getIntValue("examId"));
		TomExam exam = examCahcer.getTomExam(jsonObject.getIntValue("examId"));
		int remainingCount = examScoreService.getRemainingCount(jsonObject.getString("userId"),
				jsonObject.getIntValue("examId"));
		/*TomRetakingExam retakingExample = new TomRetakingExam();
		retakingExample.setExamId(jsonObject.getIntValue("examId"));
		if (remainingCount != 0) {
			retakingExample.setSort(exam.getRetakingExamCount() + 1 - remainingCount);
		} else {
			return new Result("N", "", ErrorCode.API_OVERLOAD, "不在考试时间内。");
		}
		TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);*/
		if(exam == null){//缓存没有，查询数据库
			exam = examMapper.selectByPrimaryKey(jsonObject.getIntValue("examId"));
		}
		TomRetakingExam retakingExam = examCahcer.getTomRetakingExam(jsonObject.getIntValue("examId"), exam.getRetakingExamCount() + 1 - remainingCount);
		if (null != retakingExam) {
			Date nowDate = new Date();
			if (nowDate.getTime() < retakingExam.getRetakingExamBeginTime().getTime()
					|| nowDate.getTime() > retakingExam.getRetakingExamEndTime().getTime()) {
				return new Result("N", "", ErrorCode.API_OVERLOAD, "不在考试时间内。");
			}
		}
		// AnswerResults answerResults= mapper.readValue(json,
		// AnswerResults.class);
		//TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(examMapper.selectByPrimaryKey(answerResults.getExamId()).getExamPaperId());
		TomExamPaper examPaper = examCahcer.getTomExamPaper(exam.getExamPaperId());

		/*TomExamScore scoreExample = new TomExamScore();
		scoreExample.setExamId(answerResults.getExamId());
		scoreExample.setCode(answerResults.getUserId());
		TomExamScore LastexamScore = examScoreMapper.selectOneByExample(scoreExample);*/
		TomExamScore LastexamScore = examCahcer.getTomExamScore(jsonObject.getIntValue("examId"), jsonObject.getString("userId"));
 
		TomExamScore examScore = new TomExamScore();
		List<TomExamAnswerDetails> list = new ArrayList<TomExamAnswerDetails>();

		List<EmpAnswer> empAnswers = new ArrayList<EmpAnswer>();
		EmpAnswer empAnswer1;
		for (int i = 0; i < jsonObject.getJSONArray("examResult").size(); i++) {
			empAnswer1 = mapper.readValue(jsonObject.getJSONArray("examResult").get(i).toString(), EmpAnswer.class);
			empAnswers.add(empAnswer1);
		}
		answerResults.setEmpAnswers(empAnswers);

		examScore.setExamId(answerResults.getExamId());
		examScore.setCode(answerResults.getUserId());
		examScore.setExamTotalTime(answerResults.getExamTotalTime());
		if(LastexamScore!= null){
			examScore.setEmpName(LastexamScore.getEmpName());
		}
		int totalPoints = 0;
		int rightNumbe = 0;
		int wrongNumber = 0;
		Boolean flag = false;
		List<TomTopic> wrongTopicList = new ArrayList<TomTopic>();
		for (EmpAnswer empAnswer : answerResults.getEmpAnswers()) {
			TomExamAnswerDetails examAnswerDetails = new TomExamAnswerDetails();
			examAnswerDetails.setExamId(answerResults.getExamId());// 封装考试id
			examAnswerDetails.setCode(answerResults.getUserId());// 封装员工id
			examAnswerDetails.setCreateTime(new Date());// 封装创建时间

			TomTopic topic = examCahcer.getTomTopic(empAnswer.getTopicId());

			if (topic.getQuestionType().equals("1") || topic.getQuestionType().equals("5")
					|| topic.getQuestionType().equals("2")) {
				if ("0".equals(empAnswer.getOptionId())) {
					empAnswer.setOptionId("");
				}
				String answers[] = empAnswer.getOptionId().split(",");
				List<TomTopicOption> rightOptions = examCahcer.getTomTopicRightOptionList(topic.getTopicId());
				boolean isRight = true;
				if (empAnswer.getOptionId() != null && !"".equals(empAnswer.getOptionId())
						&& rightOptions.size() == answers.length) {
					for (String s : answers) {
						TomTopicOption topicOption = examCahcer.getTomTopicOptionById(Integer.parseInt(s));
						if (topicOption != null && topicOption.getRight().equals("N")) {
							isRight = false;
						}
					}
				} else {
					isRight = false;
				}
				TomExamQuestion examQuestion = examCahcer.getTomExamQuestion(examPaper.getExamPaperId(), topic.getQuestionType(), topic.getQuestionBankId());
				examAnswerDetails.setEmpAnswer(empAnswer.getOptionId());// 封装非主观题学员答案
				examAnswerDetails.setSubjectiveItemAnswer("");// 封装主观题学员答案

				if (isRight) {
					examAnswerDetails.setScore(examQuestion.getScore());// 封装得分
					examAnswerDetails.setRightState("Y");// 封装正确状态
					totalPoints += examQuestion.getScore();
					rightNumbe += 1;
				} else {
					examAnswerDetails.setScore(0);// 封装得分
					examAnswerDetails.setRightState("N");// 封装正确状态
					wrongNumber += 1;
					wrongTopicList.add(topic);
				}
			} else {
				flag = true;
				examAnswerDetails.setEmpAnswer("");// 封装非主观题学员答案
				examAnswerDetails.setSubjectiveItemAnswer(empAnswer.getOptionId());// 封装主观题学员答案
				examAnswerDetails.setScore(0);
			}
			examAnswerDetails.setTopicId(empAnswer.getTopicId());// 封装题目id
			examAnswerDetails.setTopic(topic.getTopicName());// 封装题目名称
			list.add(examAnswerDetails);
			// examAnswerDetailsMapper.insertSelective(examAnswerDetails);
		}
		examScore.setRightNumbe(rightNumbe);
		examScore.setWrongNumber(wrongNumber);
		examScore.setAccuracy((double) rightNumbe / (double) examPaper.getTestQuestionCount());
		examScore.setTotalPoints(totalPoints);
		examScore.setCreateTime(new Date());
		if (flag) {
			examPaper.setImmediatelyShow("N");
			examScore.setGradeState("P");
		} else {
			if (examScore.getTotalPoints() >= examPaper.getPassMark()) {
				examScore.setGradeState("Y");

			} else {
				examScore.setGradeState("N");
			}
		}
		if(0 != exam.getRetakingExamCount()){
			int retaking = exam.getRetakingExamCount();
			int examCount = LastexamScore.getExamCount() == null?0:LastexamScore.getExamCount();
			examScore.setRemainingExamCount(String.valueOf(retaking-examCount));
		}else{
			examScore.setRemainingExamCount("0");
		}
		int examCount = LastexamScore.getExamCount() + 1;
		LastexamScore.setExamCount(examCount);
		examScore.setExamCount(examCount);
		examScore.setTopics(wrongTopicList);
		examBean.setExamScore(examScore);
		examBean.setExamAnswerDetails(list);
		redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE,examId+"_"+code,jsonMapper.toJson(examScore));//更新缓存;
		ExamRunnable examRunnbale = QueueManager.me().getQueRunnable(ExamRunnable.class);
		examRunnbale.addExamBean(examBean);
		return new Result("Y", mapper.writeValueAsString(examPaper.getImmediatelyShow()), ErrorCode.SUCCESS_CODE, "");
	}
	/**
	 * 
	 * 功能描述：[提交考卷系统并对非主观题打分]
	 *
	 * 创建者：JCX 创建时间: 2016年3月22日 下午1:49:17
	 * 
	 * @param json
	 * @return
	 */

	public Result eleExamSubmitTwo(String json) throws Exception {
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		ExamBean examBean = new ExamBean();
		JSONObject jsonObject = JSONObject.parseObject(json);
		examBean.setData(jsonObject);
		AnswerResults answerResults = new AnswerResults();
		examBean.setAnswerResults(answerResults);
		answerResults.setExamId(jsonObject.getIntValue("examId"));
		answerResults.setUserId(jsonObject.getString("userId"));
		answerResults.setExamTotalTime(jsonObject.getIntValue("examTotalTime"));
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		// 判断是否开考
		TomExam exam = examMapper.selectByPrimaryKey(jsonObject.getIntValue("examId"));
		int remainingCount = examScoreService.getRemainingCount(jsonObject.getString("userId"),
				jsonObject.getIntValue("examId"));
		TomRetakingExam retakingExample = new TomRetakingExam();
		retakingExample.setExamId(jsonObject.getIntValue("examId"));
		if (remainingCount != 0) {
			retakingExample.setSort(exam.getRetakingExamCount() + 1 - remainingCount);
		} else {
			return new Result("N", "", ErrorCode.API_OVERLOAD, "不在考试时间内。");
		}
		TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		if (null != retakingExam) {
			Date nowDate = new Date();
			if (nowDate.getTime() < retakingExam.getRetakingExamBeginTime().getTime()
					|| nowDate.getTime() > retakingExam.getRetakingExamEndTime().getTime()) {
				return new Result("N", "", ErrorCode.API_OVERLOAD, "不在考试时间内。");
			}
		}
		// AnswerResults answerResults= mapper.readValue(json,
		// AnswerResults.class);
		TomExamPaper examPaper = examPaperMapper
				.selectByPrimaryKey(examMapper.selectByPrimaryKey(answerResults.getExamId()).getExamPaperId());

		TomExamScore scoreExample = new TomExamScore();
		scoreExample.setExamId(answerResults.getExamId());
		scoreExample.setCode(answerResults.getUserId());
		TomExamScore LastexamScore = examScoreMapper.selectOneByExample(scoreExample);

		TomExamScore examScore = examCahcer.getTomExamScore(jsonObject.getIntValue("examId"), jsonObject.getString("userId"));
		if (examScore == null) {
			examScore = new TomExamScore();
		}
		List<TomExamAnswerDetails> list = new ArrayList<TomExamAnswerDetails>();

		List<EmpAnswer> empAnswers = new ArrayList<EmpAnswer>();
		EmpAnswer empAnswer1;
		for (int i = 0; i < jsonObject.getJSONArray("examResult").size(); i++) {
			empAnswer1 = mapper.readValue(jsonObject.getJSONArray("examResult").get(i).toString(), EmpAnswer.class);
			empAnswers.add(empAnswer1);
		}
		answerResults.setEmpAnswers(empAnswers);

		examScore.setExamId(answerResults.getExamId());
		examScore.setCode(answerResults.getUserId());
		examScore.setExamTotalTime(answerResults.getExamTotalTime());
		examScore.setEmpName(LastexamScore.getEmpName());
		int totalPoints = 0;
		int rightNumbe = 0;
		int wrongNumber = 0;
		Boolean flag = false;

		for (EmpAnswer empAnswer : answerResults.getEmpAnswers()) {
			TomExamAnswerDetails examAnswerDetails = new TomExamAnswerDetails();
			examAnswerDetails.setExamId(answerResults.getExamId());// 封装考试id
			examAnswerDetails.setCode(answerResults.getUserId());// 封装员工id
			examAnswerDetails.setCreateTime(new Date());// 封装创建时间

			TomTopic topic = topicMapper.selectByPrimaryKey(empAnswer.getTopicId());

			if (topic.getQuestionType().equals("1") || topic.getQuestionType().equals("5")
					|| topic.getQuestionType().equals("2")) {
				if ("0".equals(empAnswer.getOptionId())) {
					empAnswer.setOptionId("");
				}
				String answers[] = empAnswer.getOptionId().split(",");
				List<TomTopicOption> rightOptions = topicOptionMapper.selectRightOption(topic.getTopicId());
				boolean isRight = true;
				if (empAnswer.getOptionId() != null && !"".equals(empAnswer.getOptionId())
						&& rightOptions.size() == answers.length) {
					for (String s : answers) {
						TomTopicOption topicOption = topicOptionMapper.selectByPrimaryKey(Integer.parseInt(s));
						if (topicOption != null && topicOption.getRight().equals("N")) {
							isRight = false;
						}
					}
				} else {
					isRight = false;
				}

				TomExamQuestion example = new TomExamQuestion();
				example.setExamPaperId(examPaper.getExamPaperId());
				example.setQuestionType(topic.getQuestionType());
				example.setQuestionBankId(topic.getQuestionBankId());
				TomExamQuestion examQuestion = examQuestionMapper.selectByExample(example);
				examAnswerDetails.setEmpAnswer(empAnswer.getOptionId());// 封装非主观题学员答案
				examAnswerDetails.setSubjectiveItemAnswer("");// 封装主观题学员答案

				if (isRight) {
					examAnswerDetails.setScore(examQuestion.getScore());// 封装得分
					examAnswerDetails.setRightState("Y");// 封装正确状态
					totalPoints += examQuestion.getScore();
					rightNumbe += 1;
				} else {
					examAnswerDetails.setScore(0);// 封装得分
					examAnswerDetails.setRightState("N");// 封装正确状态
					wrongNumber += 1;
				}
			} else {
				flag = true;
				examAnswerDetails.setEmpAnswer("");// 封装非主观题学员答案
				examAnswerDetails.setSubjectiveItemAnswer(empAnswer.getOptionId());// 封装主观题学员答案
				examAnswerDetails.setScore(0);
			}
			examAnswerDetails.setTopicId(empAnswer.getTopicId());// 封装题目id
			examAnswerDetails.setTopic(topic.getTopicName());// 封装题目名称
			list.add(examAnswerDetails);
			// examAnswerDetailsMapper.insertSelective(examAnswerDetails);
		}

		examScore.setRightNumbe(rightNumbe);
		examScore.setWrongNumber(wrongNumber);
		examScore.setAccuracy((double) rightNumbe / (double) examPaper.getTestQuestionCount());
		examScore.setTotalPoints(totalPoints);
		examScore.setCreateTime(new Date());
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		if (flag) {
			examScore.setGradeState("P");
			examScore.setExamCount(LastexamScore.getExamCount() + 1);
			examScoreService.insertSelective(examScore);
			for (TomExamAnswerDetails examAnswerDetails : list) {
				examAnswerDetails.setStatus("N");
				answerDetailsService.insertSelective(examAnswerDetails);
			}
		} else {
			if (examScore.getTotalPoints() >= examPaper.getPassMark()) {
				examScore.setGradeState("Y");

			} else {
				examScore.setGradeState("N");
			}

			if (LastexamScore.getTotalPoints() > examScore.getTotalPoints()) {
				LastexamScore.setExamCount(LastexamScore.getExamCount() + 1);
				LastexamScore.setCreateTime(examScore.getCreateTime());
				examScoreService.updateSelective(LastexamScore);
			} else if (LastexamScore.getTotalPoints() < examScore.getTotalPoints()) {
				TomExamAnswerDetails answerDetails = new TomExamAnswerDetails();
				answerDetails.setExamId(answerResults.getExamId());
				answerDetails.setCode(answerResults.getUserId());
				answerDetailsService.deleteByExample(answerDetails);
				for (TomExamAnswerDetails examAnswerDetails : list) {
					examAnswerDetails.setStatus("Y");
					answerDetailsService.insertSelective(examAnswerDetails);
				}
				examScore.setExamCount(LastexamScore.getExamCount() + 1);
				examScoreService.updateSelective(examScore);
			} else {
				if (LastexamScore.getExamTotalTime() < examScore.getExamTotalTime()
						&& !LastexamScore.getGradeState().equals("D")) {
					LastexamScore.setExamCount(LastexamScore.getExamCount() + 1);
					LastexamScore.setCreateTime(examScore.getCreateTime());
					examScoreService.updateSelective(LastexamScore);
				} else {
					TomExamAnswerDetails answerDetails = new TomExamAnswerDetails();
					answerDetails.setExamId(answerResults.getExamId());
					answerDetails.setCode(answerResults.getUserId());
					answerDetailsService.deleteByExample(answerDetails);
					for (TomExamAnswerDetails examAnswerDetails : list) {
						examAnswerDetails.setStatus("Y");
						answerDetailsService.insertSelective(examAnswerDetails);
					}
					examScore.setExamCount(LastexamScore.getExamCount() + 1);
					examScoreService.updateSelective(examScore);
				}
			}

			if (examScore.getGradeState().equals("Y")) {
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				// 前置任务消息
				Map<Object, Object> propertyMap = new HashMap<Object, Object>();
				propertyMap.put("code", jsonObject.getString("userId"));
				propertyMap.put("examId", jsonObject.getIntValue("examId"));
				// 根据考试与人员查询正在进行的活动
				List<TomActivityProperty> selectByCourseAndCode = activityPropertyMapper
						.selectByExamAndCodeMessage(propertyMap);
				courseService.preTask(jsonObject.getString("userId"), selectByCourseAndCode);
			}

			// TomExamScore scoreExample1=new TomExamScore();
			// scoreExample1.setCode(answerResults.getUserId());
			// scoreExample1.setExamId(answerResults.getExamId());
			// TomExamScore
			// examScore2=examScoreMapper.selectOneByExample(scoreExample1);
			TomUserInfo userInfo = userInfoMapper.selectByPrimaryKey(answerResults.getUserId());

			TomEbRecord ebRecord = new TomEbRecord();
			ebRecord.setCode(answerResults.getUserId());
			ebRecord.setUpdateTime(new Date());
			if (examScore.getGradeState().equals("N")
					&& examScoreService.getRemainingCount(answerResults.getUserId(), answerResults.getExamId()) <= 1) {
				if (userInfo.geteNumber() >= examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber() - examPaper.getNotPassEb());
				} else {
					userInfo.seteNumber(0);
				}
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				DBContextHolder.setDbType(DBContextHolder.MASTER);
				userInfoServise.updateByCode(userInfo);
				commodityService.insertSelective(ebRecord);

			} else if (examScore.getGradeState().equals("Y")) {
				// Map<Object, Object> map1 = new HashMap<Object, Object>();
				// SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
				// map1.put("userId", jsonObject.getString("userId"));
				// map1.put("road", "3");
				// map1.put("date", simple.format(new Date()));
				// List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
				// int ebCount=0;
				// for(TomEbRecord er:ebRList){
				// ebCount+=er.getExchangeNumber();
				// }
				// if(ebCount+examPaper.getPassEb()<=100){
				userInfo.seteNumber(userInfo.geteNumber() + examPaper.getPassEb());
				if (userInfo.getAddUpENumber() == null) {
					userInfo.setAddUpENumber(0);
				}
				userInfo.setAddUpENumber(userInfo.getAddUpENumber() + examPaper.getPassEb());
				ebRecord.setExchangeNumber(examPaper.getPassEb());
				if ("A".equals(exam.getExamType())) {
					ebRecord.setRoad("3");
				}else if ("C".equals(exam.getExamType())) {
					ebRecord.setRoad("14");
				}
				userInfoServise.updateByCode(userInfo);
				commodityService.insertSelective(ebRecord);
				// }else{
				// userInfo.seteNumber(userInfo.geteNumber()+100-ebCount);
				// userInfo.setAddUpENumber(userInfo.getAddUpENumber()+100-ebCount);
				// ebRecord.setExchangeNumber(100-ebCount);
				// ebRecord.setRoad("3");
				// userInfoMapper.updateByCode(userInfo);
				// ebRecordMapper.insertSelective(ebRecord);
				// }
			}
		}
		if (flag) {
			examPaper.setImmediatelyShow("N");
		}

		// 插入考试记录
		TomJoinExamRecord joinExamRecord = new TomJoinExamRecord();
		joinExamRecord.setCode(jsonObject.getString("userId"));
		joinExamRecord.setExamid(jsonObject.getIntValue("examId"));
		joinExamRecord.setCreateTime(new Date());
		topicOptionService.insertSelective(joinExamRecord);
		return new Result("Y", mapper.writeValueAsString(examPaper.getImmediatelyShow()), ErrorCode.SUCCESS_CODE, "");
	}
	/**
	 * 
	 * 功能描述：[考试结果（走队列）]
	 *
	 * 创建者：JCX 创建时间: 2016年3月31日 下午4:06:58
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExamResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		String lang = request.getParameter("lang");
		ExamRunnable examRunnable = (ExamRunnable)QueueManager.me().getQueRunnable(ExamRunnable.class);
		String examId = request.getParameter("examId");
		String code = request.getParameter("userId");
		TomExam tomExam = examCahcer.getTomExam(Integer.parseInt(examId));
		Integer sort=0;
		if(tomExam == null){
			tomExam = examMapper.selectByPrimaryKey(Integer.parseInt(examId));
		}
		System.out.println(code+"->执行考试["+tomExam.getExamId()+"]成绩查询...");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 ExamBean examBean = examRunnable.getEmpExamInfos().get(code+"_"+examId);
		 TomExamScore tomExamScore = null;
		 if(examBean != null){
			 TomExamPaper tomExamPaper = examCahcer.getTomExamPaper(tomExam.getExamPaperId());
			 //tomExamScore = examBean.getExamScore();
			 tomExamScore = examCahcer.getTomExamScore(Integer.parseInt(examId), code);
			 tomExamScore.setImmediatelyShow(tomExamPaper.getImmediatelyShow());
			 tomExamScore.setShowQualifiedStandard(tomExamPaper.getShowQualifiedStandard());
			 tomExamScore.setPassMark(tomExamPaper.getPassMark());
			 tomExamScore.setShowScore(tomExamPaper.getShowScore());
			 tomExamScore.setRetakingExamCount(tomExamScore.getRemainingExamCount());//前端剩余补考次数;
			 
			 
			 tomExam.setRemainingCount(Integer.parseInt(tomExamScore.getRemainingExamCount()));
		 	 if (tomExam.getRemainingCount() != 0) {
				sort = (tomExam.getRetakingExamCount() + 1 - tomExam.getRemainingCount());
			 } else {
				sort = (tomExam.getRetakingExamCount());
			 }
			 TomRetakingExam tomRetakingExam = examCahcer.getTomRetakingExam(Integer.parseInt(examId), sort);
			 
			 if (null != tomRetakingExam) {
				 tomExamScore.setRetakingExamBeginTime(format.format(tomRetakingExam.getRetakingExamBeginTime()));
			 }
			 List<TomExamAnswerDetails> examAnswerDetails = examBean.getExamAnswerDetails();
			 List<TomTopic> topics = tomExamScore.getTopics();
			 for (TomTopic tomTopic : topics) {
				List<TomTopicOption> tomTopicOptions = examCahcer.getTomTopicOptionByTopicId(tomTopic.getTopicId());
				if ("en".equals(lang)) {
					tomTopic.setTopicName(tomTopic.getTopicNameEn());
					for (TomTopicOption option : tomTopicOptions) {
						option.setOptionName(option.getOptionNameEn());
					}
				}
				tomTopic.setTopicOptions(tomTopicOptions);
				TomExamAnswerDetails examAnswerDetail = null;
				for(TomExamAnswerDetails examAnswerDetailObj : examAnswerDetails){
					if(tomTopic.getTopicId().intValue() == examAnswerDetailObj.getTopicId().intValue()){
						examAnswerDetail = examAnswerDetailObj;
						break;
					}
				}
				if (tomTopic.getQuestionType().equals("3") || tomTopic.getQuestionType().equals("4")) {
					if(examAnswerDetail != null)
					tomTopic.setAnswer(examAnswerDetail.getSubjectiveItemAnswer());
				} else {
					if(examAnswerDetail != null)
					tomTopic.setAnswer(examAnswerDetail.getEmpAnswer());
				}
			}
		 }else{
			 System.out.println( "缓存examBean为NULL，执行数据库查询...");
			 return this.eleExamResultTwo(request, response);
		 }
		String json = mapper.writeValueAsString(tomExamScore);
		json = json.replaceAll(":null", ":\"\"");
		System.out.println(code+"->执行考试["+tomExam.getExamId()+"]成绩查询完毕...");
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}
	/**
	 * 
	 * 功能描述：[考试结果]
	 *
	 * 创建者：JCX 创建时间: 2016年3月31日 下午4:06:58
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleExamResultTwo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		String lang = request.getParameter("lang");
		TomExamScore scoreExample = new TomExamScore();
		scoreExample.setExamId(Integer.parseInt(request.getParameter("examId")));
		scoreExample.setCode(request.getParameter("userId"));
		TomExamScore tomExamScore = examScoreMapper.selectOneByExample(scoreExample);

		TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(
				examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId"))).getExamPaperId());
		tomExamScore.setImmediatelyShow(examPaper.getImmediatelyShow());
		tomExamScore.setShowQualifiedStandard(examPaper.getShowQualifiedStandard());
		// int
		// retakingExamCount=examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId"))).getRetakingExamCount();
		tomExamScore.setPassMark(examPaper.getPassMark());
		tomExamScore.setRetakingExamCount(
				String.valueOf(examScoreService.getRemainingCount(scoreExample.getCode(), scoreExample.getExamId())));
		tomExamScore.setShowScore(examPaper.getShowScore());

		// 判断是否开考
		TomExam exam = examMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("examId")));
		exam.setRemainingCount(examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));
		TomRetakingExam retakingExample = new TomRetakingExam();
		retakingExample.setExamId(exam.getExamId());
		if (exam.getRemainingCount() != 0) {
			retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
		} else {
			retakingExample.setSort(exam.getRetakingExamCount());
		}
		TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
		if (null != retakingExam) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			tomExamScore.setRetakingExamBeginTime(format.format(retakingExam.getRetakingExamBeginTime()));
		}
		TomExamAnswerDetails examAnswerDetails1 = new TomExamAnswerDetails();
		examAnswerDetails1.setCode(request.getParameter("userId"));
		examAnswerDetails1.setExamId(Integer.parseInt(request.getParameter("examId")));
		List<TomExamAnswerDetails> list = examAnswerDetailsMapper.selectByExample(examAnswerDetails1);

		List<TomTopic> topics = new ArrayList<TomTopic>();
		for (TomExamAnswerDetails examAnswerDetails : list) {
			if ("N".equals(examAnswerDetails.getRightState())) {
				TomTopic topic = topicMapper.selectByPrimaryKey(examAnswerDetails.getTopicId());
				List<TomTopicOption> topicOptions = topicOptionMapper.selectByTopicId(topic.getTopicId());
				if ("en".equals(lang)) {
					topic.setTopicName(topic.getTopicNameEn());
					for (TomTopicOption option : topicOptions) {
						option.setOptionName(option.getOptionNameEn());
					}
				}
				topic.setTopicOptions(topicOptions);
				if (topic.getQuestionType().equals("3") || topic.getQuestionType().equals("4")) {
					topic.setAnswer(examAnswerDetails.getSubjectiveItemAnswer());
				} else {
					topic.setAnswer(examAnswerDetails.getEmpAnswer());
				}
				topics.add(topic);
			}
		}

		tomExamScore.setTopics(topics);
		String json = mapper.writeValueAsString(tomExamScore);
		json = json.replaceAll(":null", ":\"\"");
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[展示我的考试]
	 *
	 * 创建者：JCX 创建时间: 2016年3月25日 上午9:19:44
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleMyExam(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		ExamRunnable examRunnable = (ExamRunnable)QueueManager.me().getQueRunnable(ExamRunnable.class);
		List<Integer> expiredExamIds = examScoreService.reSetStatus(request.getParameter("userId"));// 扫描所有考试将过期未参加的考试设为不合格
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String code = request.getParameter("userId");
		String lang = request.getParameter("lang");
		PageData<TomExam> page = new PageData<TomExam>();
		Map<Object, Object> queryMap = new HashMap<Object, Object>();
		queryMap.put("userId", request.getParameter("userId"));
		queryMap.put("lang", lang);
		if (request.getParameter("identifying").equals("P")) {
			queryMap.put("gradeState2", "D");
		} else {
			queryMap.put("gradeState2", request.getParameter("identifying"));
		}
		queryMap.put("gradeState1", request.getParameter("identifying"));

		if (request.getParameter("firstIndex") == null) {
			queryMap.put("startNum", 0);
		} else {
			queryMap.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			queryMap.put("endNum", 10);
			page.setPageSize(10);
		} else {
			queryMap.put("endNum", Integer.parseInt(request.getParameter("pageSize")));// (int)queryMap.get("startNum")+
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}

		int count = examMapper.countMyExam(queryMap);
		List<TomExam> list = examMapper.selectMyExam(queryMap);
		queryMap.put("gradeState1", "Y");
		queryMap.put("gradeState2", "Y");
		int examCount0 = examMapper.countMyExam(queryMap);
		queryMap.put("gradeState1", "N");
		queryMap.put("gradeState2", "N");
		int examCount1 = examMapper.countMyExam(queryMap);
		queryMap.put("gradeState1", "P");
		queryMap.put("gradeState2", "D");
		int examCount2 = examMapper.countMyExam(queryMap);
		for (TomExam exam : list) {
			TomExamPaper examPaper = examPaperMapper.selectByPrimaryKey(exam.getExamPaperId());
			TomExamScore scoreExample = new TomExamScore();
			scoreExample.setExamId(exam.getExamId());
			scoreExample.setCode(code);
			TomExamScore examScore = null;
			int reslutNum = examScoreMapper.countByExample(scoreExample);
			if (reslutNum == 2) {
				scoreExample.setGradeState("P");
				List<TomExamScore> examScoreList = examScoreMapper.selectListByExample(scoreExample);
				if(examScoreList != null && examScoreList.size() > 0)
				examScore = examScoreList.get(0);
			} else if (reslutNum == 1) {
				List<TomExamScore> examScoreList = examScoreMapper.selectListByExample(scoreExample);
				if(examScoreList != null && examScoreList.size() > 0)
				examScore = examScoreList.get(0);
			} else {
				return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR),
						"系统繁忙(userId:" + request.getParameter("userId") + "   examId:" + exam.getExamId() + ")！");
			}
			if(examScore == null){
				 ExamBean examBean = examRunnable.getEmpExamInfos().get(code+"_"+exam.getExamId());
				 if(examBean != null)
				 examScore = examBean.getExamScore();
			}
			TomActivityProperty propertyExample = new TomActivityProperty();
			propertyExample.setExamId(exam.getExamId());
			List<TomActivityProperty> activityProperties = activityPropertyMapper.selectByExample(propertyExample);
			exam.setPreStatus(activityApiService.getPreStatus(activityProperties, request.getParameter("userId")));
			exam.setExamPaperPicture(examPaper.getExamPaperPicture());
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
			exam.setFullMark(examPaper.getFullMark());
			exam.setPassMark(examPaper.getPassMark());
			exam.setPassEb(examPaper.getPassEb());
			exam.setRemainingCount(
					examScoreService.getRemainingCount(request.getParameter("userId"), exam.getExamId()));
			if (activityProperties.size() > 0) {
				exam.setActivityId(activityProperties.get(0).getActivityId());
			}
			// 判断是否开考
			TomRetakingExam retakingExample = new TomRetakingExam();
			retakingExample.setExamId(exam.getExamId());
			if (exam.getRemainingCount() != 0) {
				retakingExample.setSort(exam.getRetakingExamCount() + 1 - exam.getRemainingCount());
			} else {
				retakingExample.setSort(exam.getRetakingExamCount());
			}
			TomRetakingExam retakingExam = retakingExamMapper.selectOneByExample(retakingExample);
			if (null != retakingExam) {
				exam.setBeginTime(retakingExam.getRetakingExamBeginTime());
				exam.setEndTime(retakingExam.getRetakingExamEndTime());
				exam.setBeginTimeS(format.format(retakingExam.getRetakingExamBeginTime()));
				exam.setEndTimeS(format.format(retakingExam.getRetakingExamEndTime()));
			}

			if (examScore != null) {
				// 显示分数或合格标准
				if (examPaper.getShowScore().equals("Y") && !examScore.getGradeState().equals("P")) {
					exam.setTotalPoints(String.valueOf(examScore.getTotalPoints()));
				} else {
					exam.setTotalPoints(examScore.getGradeState());
				}
			}
			
			// 设置状态,判断考试未开始
			if (exam.getBeginTime().after(new Date())) {
				if ("en".equals(lang)) {
					exam.setExamState("NOT START");
				} else {
					exam.setExamState("未开始");
				}
			}
			
			if (examScore != null && examScore.getGradeState().endsWith("N")) {// 未通过
				if (expiredExamIds.size() > 0 && expiredExamIds.contains(examScore.getExamId())) {//已过期
					if ("en".equals(lang)) {
						exam.setExamState("OVERDUE");
					} else {
						exam.setExamState("已过期");
					}
				}else {
					if ("en".equals(lang)) {
						exam.setExamState("Not passed");
					} else {
						exam.setExamState("未通过");
					}
				}
			} else if (examScore != null && examScore.getGradeState().endsWith("P")) {//未审阅也算进行中
				if ("en".equals(lang)) {
					exam.setExamState("IN PROGRESS");
					exam.setExamDetailState("Not reviewed");
				} else {
					exam.setExamState("进行中");
					exam.setExamDetailState("未审阅");
				}
			} else if (examScore != null && examScore.getGradeState().endsWith("Y")) {
				if ("en".equals(lang)) {
					exam.setExamState("COMPLETED");
				} else {
					exam.setExamState("已完成");
				}
			} else if (examScore != null && examScore.getGradeState().endsWith("D")) {
				if (exam.getOfflineExam().equals("2") && exam.getEndTime().getTime() > new Date().getTime()) {//未参加也算进行中
					if ("en".equals(lang)) {
						exam.setExamState("IN PROGRESS");
						exam.setExamDetailState("Did not participate");
					} else {
						exam.setExamState("进行中");
						exam.setExamDetailState("未参加");
					}
				} else if (exam.getOfflineExam().equals("2") && exam.getEndTime().getTime() <= new Date().getTime()) {
					if ("en".equals(lang)) {
						exam.setExamState("IN PROGRESS");
						exam.setExamDetailState("Not reviewed");
					} else {
						exam.setExamState("进行中");
						exam.setExamDetailState("未审阅");
					}
				} else if (exam.getOfflineExam().equals("1") && exam.getEndTime().getTime() > new Date().getTime()) {
					if ("en".equals(lang)) {
						exam.setExamState("IN PROGRESS");
						exam.setExamDetailState("Pending examination");
					} else {
						exam.setExamState("进行中");
						exam.setExamDetailState("待考试");
					}
				}
			}
			if ("en".equals(lang)) {
				exam.setExamName(exam.getExamNameEn());
				exam.setExamAddress(exam.getExamAddressEn());
				exam.setExamPaperPicture(examPaper.getExamPaperPictureEn());
			}
		}
		page.setCount(count);
		page.setDate(list);
		page.setPageNum((int) queryMap.get("startNum") / (int) queryMap.get("endNum") + 1);

		MyExam myExam = new MyExam();
		myExam.setExamCount0(examCount0);
		myExam.setExamCount1(examCount1);
		myExam.setExamCount2(examCount2);
		myExam.setExamList(page);

		String pageJson = mapper.writeValueAsString(myExam);

		pageJson = pageJson.replaceAll(":null", ":\"\"");
		return new Result("Y", pageJson, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[排序]
	 *
	 * 创建者：JCX 创建时间: 2016年3月21日 下午5:28:15
	 * 
	 * @param list
	 * @return
	 */
	public List<TomExamQuestion> sort(List<TomExamQuestion> list) throws Exception {

		TomExamQuestion temp; // 记录临时中间值
		int size = list.size(); // 大小
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (Integer.parseInt(list.get(i).getSort()) > Integer.parseInt(list.get(j).getSort())) {
					temp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, temp);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * 功能描述：[随机抽取题目]
	 *
	 * 创建者：JCX 创建时间: 2016年3月21日 下午6:08:05
	 * 
	 * @param examQuestion
	 * @return
	 */
	public List<TomTopic> randomTopics(TomExamQuestion examQuestion) {
		List<TomTopic> topics = topicMapper.selectByqbIdAndType(examQuestion.getQuestionBankId(),
				examQuestion.getQuestionType());
		List<TomTopic> list = new ArrayList<TomTopic>();
		for (int i = 0; i < examQuestion.getCount(); i++) {
			int index = (int) (Math.random() * topics.size());
			list.add(topics.get(index));
			topics.remove(index);
		}
		return list;
	}

	// /**
	// *
	// * 功能描述：[将过期考试设置为不合格,过期考试扣除e币]
	// *
	// * 创建者：JCX
	// * 创建时间: 2016年5月10日 上午9:16:50
	// * @param userId
	// */
	// @Transactional
	// public void reSetStatus(String userId){
	// Map<Object,Object> queryMap=new HashMap<Object, Object>();
	// queryMap.put("userId",userId);
	// queryMap.put("gradeState1","P");
	// queryMap.put("gradeState2","D");
	// int count=examMapper.countMyExam(queryMap);
	// queryMap.put("startNum",0);
	// queryMap.put("endNum",count);
	//
	// List<TomExam> list=examMapper.selectMyExam(queryMap);
	// for(TomExam exam:list){
	// TomExamPaper examPaper=examPaperMapper.selectByExamId(exam.getExamId());
	//
	// TomExamScore scoreExample=new TomExamScore();
	// scoreExample.setExamId(exam.getExamId());
	// scoreExample.setCode(userId);
	// TomExamScore
	// examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
	// if(examScoreMapper.countByExample(scoreExample)==1){
	//
	// if(exam.getEndTime().getTime()<new
	// Date().getTime()&&examScore.getGradeState().equals("D")){
	// examScore.setGradeState("N");
	// examScore.setWrongNumber(examPaper.getTestQuestionCount());
	// examScoreMapper.updateSelective(examScore);
	// }
	// }
	//
	// TomRetakingExam retakingExample=new TomRetakingExam();
	// retakingExample.setExamId(exam.getExamId());
	// retakingExample.setSort(exam.getRetakingExamCount());
	// TomRetakingExam
	// retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
	// Date nowDate=new Date();
	// if(examScore.getGradeState().equals("N")&&examScore.getCreateTime().getTime()<=retakingExam.getRetakingExamBeginTime().getTime()&&nowDate.getTime()>retakingExam.getRetakingExamEndTime().getTime()){
	// TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(userId);
	//
	// TomEbRecord ebRecord=new TomEbRecord();
	// ebRecord.setCode(userId);
	// ebRecord.setUpdateTime(new Date());
	// ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
	// ebRecord.setRoad("9");
	// userInfo.seteNumber(userInfo.geteNumber()-examPaper.getNotPassEb());
	//
	// userInfoMapper.updateByCode(userInfo);
	// ebRecordMapper.insertSelective(ebRecord);
	// }
	// }
	// }

	// /**
	// *
	// * 功能描述：[计算剩余补考次数]
	// *
	// * 创建者：JCX
	// * 创建时间: 2016年5月10日 上午9:17:41
	// * @param userId
	// * @param examId
	// * @return
	// */
	// public int getRemainingCount(String userId,int examId){
	// TomExam exam=examMapper.selectByPrimaryKey(examId);
	// int count=0;
	// TomExamScore scoreExample=new TomExamScore();
	// scoreExample.setExamId(examId);
	// scoreExample.setCode(userId);
	// TomExamScore examScore;
	// int resultNum=examScoreMapper.countByExample(scoreExample);
	// if(resultNum==2){
	// scoreExample.setGradeState("P");
	// examScore= examScoreMapper.selectListByExample(scoreExample).get(0);
	// }else if(resultNum==1){
	// examScore=examScoreMapper.selectListByExample(scoreExample).get(0);
	// }else{
	// System.out.println("用户id："+userId+" 考试id："+examId+"数据错误！");
	// return count+1;
	// }
	//
	// Map<Object,Object> map=new HashMap<Object, Object>();
	// map.put("examId",examId);
	// map.put("date",examScore.getCreateTime());
	// TomRetakingExam retakingExam=retakingExamMapper.selectByTime(map);
	// if(retakingExam!=null){
	// if(new
	// Date().getTime()<=retakingExam.getRetakingExamEndTime().getTime()){
	// count=exam.getRetakingExamCount()- retakingExam.getSort();
	// }else{
	// TomRetakingExam example=new TomRetakingExam();
	// example.setExamId(examId);
	// List<TomRetakingExam>
	// retakingExams=retakingExamMapper.selectListByExample(example);
	// for(TomRetakingExam retakingExam1:retakingExams){
	// if(retakingExam1.getRetakingExamEndTime().getTime()>=new
	// Date().getTime()){
	// count=exam.getRetakingExamCount()-retakingExam1.getSort()+1;
	// break;
	// }
	// }
	// }
	// }else{
	// TomRetakingExam example=new TomRetakingExam();
	// example.setExamId(examId);
	// List<TomRetakingExam>
	// retakingExams=retakingExamMapper.selectListByExample(example);
	// for(TomRetakingExam retakingExam1:retakingExams){
	// if(retakingExam1.getRetakingExamEndTime().getTime()>=new
	// Date().getTime()){
	// count=exam.getRetakingExamCount()-retakingExam1.getSort()+1;
	// break;
	// }
	// }
	// }
	// return count;
	// }

}