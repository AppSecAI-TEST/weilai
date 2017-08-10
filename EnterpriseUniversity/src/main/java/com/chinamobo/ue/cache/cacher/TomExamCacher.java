/**
 * Project Name:enterpriseuniversity 
 * File Name:TomExamCacher.java 
 * Package Name:com.chinamobo.ue.api.cacher
 * @author Acemon
 * Date:2017年7月28日上午10:33:26
 */
package com.chinamobo.ue.cache.cacher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobo.ue.api.exam.service.ExamEntity.Topic;
import com.chinamobo.ue.cache.AbstractCacheManager;
import com.chinamobo.ue.cache.CacheConstant;
import com.chinamobo.ue.cache.Cacher;
import com.chinamobo.ue.cache.redis.RedisCacheManager;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamQuestionMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamQuestion;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.utils.JsonMapper;

/**
 * ClassName:TomExamCacher
 * Function:TODO
 * @author Acemon
 * 2017年7月28日
 */
@Service
public class TomExamCacher implements Cacher,CacheConstant{
	
	@Autowired
	private TomTopicMapper topicMapper;

	@Autowired
	private TomTopicOptionMapper topicOptionMapper;

	@Autowired
	private TomExamQuestionMapper examQuestionMapper;
	
	@Autowired
	private TomExamMapper examMapper;
	
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	
	private  JsonMapper mapper = new JsonMapper();
	private RedisCacheManager cacheManager;
	@Override
	public void init() throws Exception {
		
	}
	
	@Override
	public void doCache(AbstractCacheManager cacheManager)throws Exception{
		this.cacheManager = (RedisCacheManager)cacheManager;
		this.doCachePaper();
		this.doCacheQuestion();
		this.doCacheRetakingExam();
		this.doCacheScore();
		this.doCacheTopic();
		this.doCacheTopicOption();
		this.doCacheExam();
	}
	public void doCacheExam()throws Exception{
		List<TomExam> tomExamList = examMapper.selectAll();
		if(tomExamList != null){
			for(TomExam tomExam : tomExamList){
				cacheManager.hset(TOM_EXAM,tomExam.getExamId().toString(),mapper.toJson(tomExam));
			}
		}
	}
	/**
	 * 
		 * 
		 * 功能描述：[缓存题目]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年8月2日 上午11:22:17
		 * @throws Exception
	 */
	public void doCacheTopic()throws Exception{
		List<TomTopic> toPicList = topicMapper.selectAll();
		if(toPicList != null){
			for(TomTopic tomTopic : toPicList){
				cacheManager.hset(TOM_TOPIC,tomTopic.getTopicId().toString(),mapper.toJson(tomTopic));
			}
		}
	}
	/**
	 * 
		 * 
		 * 功能描述：[缓存题目选项]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年8月2日 上午11:21:57
		 * @throws Exception
	 */
	public void doCacheTopicOption()throws Exception{
		List<TomTopicOption> TomTopicOptionList = topicOptionMapper.selectAll();
		if(TomTopicOptionList != null){
			for(TomTopicOption tomTopicOption : TomTopicOptionList){
				String topicId = tomTopicOption.getTopicId().toString();
				List<TomTopicOption> tomTopicOptions = cacheManager.get(TOM_TOPIC_OPTION+"_"+topicId, TomTopicOption.class);
				if(tomTopicOptions == null){
					tomTopicOptions = new ArrayList<TomTopicOption>();
				}
				tomTopicOptions.add(tomTopicOption);
				cacheManager.del(TOM_TOPIC_OPTION+"_"+topicId);
				cacheManager.set(TOM_TOPIC_OPTION+"_"+topicId, tomTopicOptions);
				cacheManager.hset(TOM_TOPIC_OPTION, tomTopicOption.getId().toString(), mapper.toJson(tomTopicOption));
			}
		}
	}
	/**
	 * 
		 * 
		 * 功能描述：[缓存试卷问题]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年8月2日 上午11:22:31
		 * @throws Exception
	 */
	public void doCacheQuestion()throws Exception{
		List<TomExamQuestion> TomExamQuestionList = examQuestionMapper.selectAll();
		if(TomExamQuestionList != null){
			for(TomExamQuestion tomExamQuestion : TomExamQuestionList){
				String examPaperId = tomExamQuestion.getExamPaperId().toString();
				List<TomExamQuestion> tomExamQuestions = cacheManager.get(TOM_EXAM_QUESTION+"_"+examPaperId,TomExamQuestion.class);
				if(tomExamQuestions == null){
					tomExamQuestions = new ArrayList<TomExamQuestion>();
				}
				tomExamQuestions.add(tomExamQuestion);
				cacheManager.del(TOM_EXAM_QUESTION+"_"+examPaperId);
				cacheManager.set(TOM_EXAM_QUESTION+"_"+examPaperId,tomExamQuestions);
				cacheManager.hset(TOM_EXAM_QUESTION, examPaperId+"_"+tomExamQuestion.getQuestionType()+"_"+tomExamQuestion.getQuestionBankId().toString(), mapper.toJson(tomExamQuestion));
			}
		}
	}
	/**
	 * 
		 * 
		 * 功能描述：[缓存试卷]
		 *
		 * 创建者：WChao
		 * 创建时间: 2017年8月2日 上午11:23:02
		 * @throws Exception
	 */
	public void doCachePaper() throws Exception{
		List<TomExamPaper> tomExamPaperList = examPaperMapper.selectAll();
		if(tomExamPaperList != null){
			for(TomExamPaper tomExamPaper : tomExamPaperList){
				cacheManager.hset(TOM_EXAM_PAPER,tomExamPaper.getExamPaperId().toString(),mapper.toJson(tomExamPaper));
			}
		}
	}
	public void doCacheScore()throws Exception{
		List<TomExamScore> tomExamScoreList = examScoreMapper.selectAll();
		if(tomExamScoreList != null){
			for(TomExamScore tomExamScore : tomExamScoreList){
				Integer examId = tomExamScore.getExamId();
				List<TomExamScore> tomExamScores = cacheManager.get(TOM_EXAM_SCORE+"_"+examId,TomExamScore.class);
				if(tomExamScores == null){
					tomExamScores = new ArrayList<TomExamScore>();
				}
				tomExamScores.add(tomExamScore);
				cacheManager.del(TOM_EXAM_SCORE+"_"+examId);
				cacheManager.set(TOM_EXAM_SCORE+"_"+examId,tomExamScores);
				cacheManager.hset(TOM_EXAM_SCORE,tomExamScore.getExamId()+"_"+tomExamScore.getCode(),mapper.toJson(tomExamScore));
			}
		}
	}
	public void doCacheRetakingExam()throws Exception{
		List<TomRetakingExam> tomRetakingExamList = retakingExamMapper.selectAll();
		if(tomRetakingExamList != null){
			for(TomRetakingExam tomRetakingExam : tomRetakingExamList){
				String examId = tomRetakingExam.getExamId().toString();
				List<TomRetakingExam> tomRetakingExams = null;
				tomRetakingExams = cacheManager.get(TOM_RETAKING_EXAM+"_"+examId,TomRetakingExam.class);
				if(tomRetakingExams == null){
					tomRetakingExams = new ArrayList<TomRetakingExam>();
				}
				tomRetakingExams.add(tomRetakingExam);
				cacheManager.del(TOM_RETAKING_EXAM+"_"+examId);
				cacheManager.set(TOM_RETAKING_EXAM+"_"+examId,tomRetakingExams);
				cacheManager.hset(TOM_RETAKING_EXAM, examId+"_"+tomRetakingExam.getSort().toString(), mapper.toJson(tomRetakingExam));
			}
		}
	}
	//读缓存
	public TomTopic getTomTopic(Integer topicId){
		String topicJson = cacheManager.hget(TOM_TOPIC, topicId.toString());
		if (topicJson != null) {
			TomTopic tomTopic = JSONObject.parseObject(topicJson, TomTopic.class);
			return tomTopic;
		}
		return null;
	}
	
	public TomTopicOption getTomTopicOptionById(Integer id){
		/*String topicOptionJson = cacheManager.hget(TOM_TOPIC_OPTION, id.toString());
		if (topicOptionJson != null) {
			TomTopicOption tomTopicOption = JSONObject.parseObject(topicOptionJson, TomTopicOption.class);
			return tomTopicOption;
		}*/
		String topicOptionJson = cacheManager.hget(TOM_TOPIC_OPTION, id.toString());
		if (topicOptionJson != null) {
			TomTopicOption tomTopicOption = JSONObject.parseObject(topicOptionJson, TomTopicOption.class);
			return tomTopicOption;
		}
		return null;
	}
	public List<TomTopicOption> getTomTopicOptionByTopicId(Integer id){
		List<TomTopicOption> tomTopicOptionList = cacheManager.get(TOM_TOPIC_OPTION+"_"+id, TomTopicOption.class);
		List<TomTopicOption> rigthOptionList = null;
		for(TomTopicOption tomTopicOption : tomTopicOptionList){
			if(tomTopicOption.getTopicId().intValue() == id.intValue()){
				if(rigthOptionList == null)
					rigthOptionList = new ArrayList<TomTopicOption>();
				rigthOptionList.add(tomTopicOption);
			}
		}
		return rigthOptionList;
	}
	public List<TomTopicOption> getTomTopicRightOptionList(Integer id){
		List<TomTopicOption> tomTopicOptionList = cacheManager.get(TOM_TOPIC_OPTION+"_"+id, TomTopicOption.class);
		List<TomTopicOption> rigthOptionList = null;
		for(TomTopicOption tomTopicOption : tomTopicOptionList){
			if(tomTopicOption.getTopicId().intValue() == id.intValue() && "Y".equals(tomTopicOption.getRight())){
				if(rigthOptionList == null)
					rigthOptionList = new ArrayList<TomTopicOption>();
				rigthOptionList.add(tomTopicOption);
			}
		}
		return rigthOptionList;
	}
	
	public List<TomExamScore> getByExample(TomExamScore example){
		List<TomExamScore> ExamScoreList = cacheManager.get(TOM_EXAM_SCORE+"_"+example.getExamId(), TomExamScore.class);
		List<TomExamScore> examScoreList = null;
		for (TomExamScore tomExamScore : ExamScoreList) {
			if (example.getExamId().intValue() == tomExamScore.getExamId().intValue() && example.getCode().equals(tomExamScore.getCode())) {
				examScoreList = new ArrayList<TomExamScore>();
				examScoreList.add(tomExamScore);
			}
		}
		return examScoreList;
		
	}
	
	public List<TomExamScore> getListByStateExample(TomExamScore example){
		List<TomExamScore> ExamScoreList = cacheManager.get(TOM_EXAM_SCORE+"_"+example.getExamId(), TomExamScore.class);
		List<TomExamScore> examScoreList = null;
		for (TomExamScore tomExamScore : ExamScoreList) {
			if (example.getExamId().intValue() == tomExamScore.getExamId().intValue() && example.getCode().equals(tomExamScore.getCode()) && example.getGradeState().equals(tomExamScore.getGradeState())) {
				examScoreList = new ArrayList<TomExamScore>();
				examScoreList.add(tomExamScore);
			}
		}
		return examScoreList;
		
	}
	
	public TomExamQuestion getTomExamQuestion(Integer examPaperId,String questionType,Integer questionBankId){
		/*List<TomExamQuestion> list = cacheManager.get(TOM_EXAM_QUESTION+"_"+examPaperId,TomExamQuestion.class);
		for(TomExamQuestion tomExamQuestion : list){
			if(tomExamQuestion.getExamPaperId().intValue() == examPaperId.intValue() && questionType.equals(tomExamQuestion.getQuestionType()) && questionBankId.intValue() == tomExamQuestion.getQuestionBankId().intValue() ){
				return tomExamQuestion;
			}
		}*/
		String examQuestionJson = cacheManager.hget(TOM_EXAM_QUESTION, examPaperId+"_"+questionType+"_"+questionBankId);
		if (examQuestionJson != null) {
			TomExamQuestion tomExamQuestion = JSONObject.parseObject(examQuestionJson, TomExamQuestion.class);
			return tomExamQuestion;
		}
		return null;
	}
	
	public TomExam getTomExam(Integer examId){
		String tomExamJson = cacheManager.hget(TOM_EXAM, examId.toString());
		if (tomExamJson != null) {
			TomExam tomExam = JSONObject.parseObject(tomExamJson, TomExam.class);
			return tomExam;
		}
		return null;
	}
	
	public TomRetakingExam getTomRetakingExam(Integer examId,Integer sort){
		/*List<TomRetakingExam> retakingExamList = cacheManager.get(TOM_RETAKING_EXAM+"_"+examId, TomRetakingExam.class);
		for(TomRetakingExam tomRetakingExam : retakingExamList){
			if(tomRetakingExam.getExamId().intValue() == examId.intValue() && tomRetakingExam.getSort() == sort){
				return tomRetakingExam;
			}
		}*/
		String retakingExamjson = cacheManager.hget(TOM_RETAKING_EXAM, examId.toString()+"_"+sort.toString());
		if (retakingExamjson != null) {
			TomRetakingExam tomRetakingExam = JSONObject.parseObject(retakingExamjson, TomRetakingExam.class);
			return tomRetakingExam;
		}
		return null;
	}
	
	public TomExamPaper getTomExamPaper(Integer examPaperId){
		String examPaperJson = cacheManager.hget(TOM_EXAM_PAPER,examPaperId.toString());
		if (examPaperJson != null) {
			TomExamPaper tomExamPaper = JSONObject.parseObject(examPaperJson, TomExamPaper.class);
			return tomExamPaper;
		}
		return null;
		
	}
	
	public TomExamScore getTomExamScore(Integer examId,String code){
		String examScoreJson = cacheManager.hget(TOM_EXAM_SCORE,examId+"_"+code);
		if (examScoreJson != null) {
			List<TomTopic> topics = JSONArray.parseArray(JSONObject.parseObject(examScoreJson).getString("topics"),TomTopic.class);
			TomExamScore tomExamScore = JSONObject.parseObject(examScoreJson, TomExamScore.class);
			tomExamScore.setTopics(topics);
			return tomExamScore;
		}
		return null;
	}
	
	
	
	public TomTopicMapper getTopicMapper() {
		return topicMapper;
	}

	public TomTopicOptionMapper getTopicOptionMapper() {
		return topicOptionMapper;
	}

	public TomExamMapper getTomExamMapper() {
		return examMapper;
	}
	
	public TomExamPaperMapper getTomExamPaperMapper() {
		return examPaperMapper;
	}
	
	public TomExamQuestionMapper getExamQuestionMapper() {
		return examQuestionMapper;
	}
	public TomExamScoreMapper getTomExamScoreMapper() {
		return examScoreMapper;
	}
}
