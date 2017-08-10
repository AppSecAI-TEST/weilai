package com.chinamobo.ue.exam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomProjectResourceMapper;
import com.chinamobo.ue.activity.entity.TomProjectResource;
import com.chinamobo.ue.cache.CacheConstant;
import com.chinamobo.ue.cache.cacher.TomExamCacher;
import com.chinamobo.ue.cache.redis.RedisCacheManager;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.exam.dao.TomExamAnswerDetailsMapper;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.dao.TomJoinExamRecordMapper;
import com.chinamobo.ue.exam.dao.TomRetakingExamMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamAnswerDetails;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.exam.entity.TomJoinExamRecord;
import com.chinamobo.ue.exam.entity.TomRetakingExam;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomMessageDetailsMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.dto.RollingDto;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.entity.WxMessage;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.MapManager;
import com.chinamobo.ue.utils.PathUtil;
import com.chinamobo.ue.utils.StringUtil;

import net.sf.json.JSONObject;

@Service
public class ExamService {
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Autowired
	private SendMessageService sendMessageService;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomExamMapper examMapper;
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	@Autowired
	private TomProjectResourceMapper tomProjectResourceMapper;
//	@Autowired
//	private ActivityService activityService;
	@Autowired
	private ExamPaperService examPaperService;
	
	@Autowired
	private TomExamScoreMapper examScoreMapper;
	
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	
	@Autowired
	private TomEmpMapper empMapper;
	
	@Autowired
	private TomRetakingExamMapper retakingExamMapper;
	
	@Autowired
	private TomUserInfoMapper userInfoMapper;
	
	@Autowired
	private TomExamPaperMapper examPaperMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private TomEbRecordMapper ebRecordMapper;
	@Autowired
	private TomJoinExamRecordMapper joinExamRecordMapper;
	@Autowired
	private TomExamAnswerDetailsMapper answerDetailsMapper;
	@Autowired
	private TomTopicOptionMapper topicOptionMapper;
	@Autowired
	private TomActivityMapper tomActivityMapper;
	@Autowired
	private RedisCacheManager redisCacheManager;
	
	JsonMapper jsonMapper = new JsonMapper();
	public TomExam findById(int id){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam exam = examMapper.selectByPrimaryKey(id);
		TomProjectResource project =new TomProjectResource();
		project.setResourceId(id);
		project.setType("E");
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(project);
		if(null!=selectByResource){
		exam.setParentProjectClassifyName(selectByResource.getParentProjectClassifyName());
		exam.setParentClassifyId(String.valueOf(selectByResource.getProjectId()));
		}
//		System.out.println(exam.getBeginTime());
		//封装补考
		TomRetakingExam example=new TomRetakingExam();
		example.setExamId(id);
		List<TomRetakingExam> retakingExams = retakingExamMapper.selectListByExample(example);
		List<String> rbeginTime=new ArrayList<String>();
		List<String> rendTime=new ArrayList<String>();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(TomRetakingExam retakingExam:retakingExams){
			rbeginTime.add(format.format(retakingExam.getRetakingExamBeginTime()));
			rendTime.add(format.format(retakingExam.getRetakingExamEndTime()));
		}
		exam.setRbeginTime(rbeginTime);
		exam.setRendTime(rendTime);
		//封装考试人员关联
		List<TomEmp> emps=empMapper.selectByExamId(id);
		List<String> empIds=new ArrayList<String>();
		List<String> empNames=new ArrayList<String>();
		List<String> citynames=new ArrayList<String>();
		List<String> deptnames=new ArrayList<String>();
		for(TomEmp emp:emps){
			empIds.add(emp.getCode());
			empNames.add(emp.getName());
			citynames.add(emp.getCityname());
			deptnames.add(emp.getDeptname());
		}
		exam.setEmpIds(empIds);
		exam.setEmpNames(empNames);
		exam.setCityname(citynames);
		exam.setDeptname(deptnames);
		
		return exam;
	}

	/**
	 * 功能描述：[考试查询]
	 *
	 * 创建者：xjw
	 * 创建时间: 2016年3月4日 下午2:48:34
	 * @param pageNum
	 * @param pageSize
	 * @param examname
	 * @return
	 */
	public PageData<TomExam> findList(int pageNum,int pageSize,String examname){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam example=new TomExam();
		
		if(examname!=null){
			examname=examname.replaceAll("/", "//");
			examname=examname.replaceAll("%", "/%");
			examname=examname.replaceAll("_", "/_");
			
		}
		example.setExamName(examname);
		PageData<TomExam> page=new PageData<TomExam>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("example", example);
		int count =examMapper.countByExample(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		
		List<TomExam> list=examMapper.findList(map);
		for(TomExam exam:list){
			TomExamPaper examPaper =examPaperService.findById(exam.getExamPaperId());
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	/**
	 * 功能描述：[删除考试]
	 *
	 * 创建者：xjw
	 * 创建时间: 2016年3月4日 下午2:27:31
	 * @param id
	 */
	@Transactional
	public void deleteexam(int id){
		examMapper.deleteByPrimaryKey(id);
		//删除初始默认成绩
		TomExamScore scoreExample=new TomExamScore();
		scoreExample.setExamId(id);
		examScoreMapper.deleteByExample(scoreExample);
		//删除员工关联
		TomExamEmpAllocation example=new TomExamEmpAllocation();
		example.setExamId(id);
		examEmpAllocationMapper.deleteByExample(example);
		//删除补考
		retakingExamMapper.deleteByExamId(id);
		//删除项目分类关联
		TomProjectResource record = new TomProjectResource();
		record.setType("E");
		record.setResourceId(id);
		tomProjectResourceMapper.deleteByPrimaryKey(record);
	}
	
	/**
	 * 功能描述：[增加考试]
	 *
	 * 创建者：xjw
	 * 创建时间: 2016年3月4日 下午5:41:04
	 * @param tomExam
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	@Transactional
	public void addexam(TomExam tomExam) throws UnsupportedEncodingException, Exception{
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		String Exam = "";		//单独推送开始
		String ExamEn = "";
		String ExamStatus = "";	
		String Question = "";		//单独推送问卷
		String QuestionEn = "";
		String QuestionStatus = "";	
		 List<TomMessageDetails> selectList = tomMessageDetailsMapper.selectList();
		 for(TomMessageDetails message:selectList){
			 if(message.getId()==19){
				 Exam = message.getMessage();
				 ExamStatus= message.getStatus();
			 }
			 if(message.getId()==20){
				 ExamEn = message.getMessage();
			 }
			 if(message.getId()==21){
				 Question = message.getMessage();
				 QuestionStatus= message.getStatus();
			 }
			 if(message.getId()==22){
				 QuestionEn = message.getMessage();
			 }
		 }
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			tomExam.setBeginTime(format.parse(tomExam.getBeginTimeS()));
			tomExam.setEndTime(format.parse(tomExam.getEndTimeS()));
			tomExam.setExamType("C");
			String examNumber= numberRecordService.getNumber(MapManager.numberType("KS"));
			tomExam.setExamNumber(examNumber);
			tomExam.setCreateTime(new Date());
			tomExam.setUpdateTime(new Date());
			examMapper.insertSelective(tomExam);
			redisCacheManager.hset(CacheConstant.TOM_EXAM, tomExam.getExamId().toString(), jsonMapper.toJson(tomExam));
			TomProjectResource resource = new TomProjectResource();
			resource.setCreateTime(date);
			resource.setCreator(tomExam.getCreator());
			resource.setOperator(tomExam.getLastOperator());
			resource.setProjectId(Integer.parseInt(tomExam.getParentClassifyId()));
			resource.setResourceId(tomExam.getExamId());
			resource.setStatus("Y");
			resource.setType("E");
			resource.setUpdateTime(date);
			resource.setParentProjectClassifyName(tomExam.getParentProjectClassifyName());
			tomProjectResourceMapper.insertSelective(resource);
			//添加补考
			TomRetakingExam retakingExam;
			TomRetakingExam exam=new TomRetakingExam();
			exam.setExamId(tomExam.getExamId());
			exam.setRetakingExamBeginTime(tomExam.getBeginTime());
			exam.setRetakingExamEndTime(tomExam.getEndTime());
			exam.setSort(0);
			retakingExamMapper.insertSelective(exam);
			redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, tomExam.getExamId()+"_"+exam.getSort(), jsonMapper.toJson(exam));
			for(int i=0;i<tomExam.getRbeginTime().size();i++){
				retakingExam=new TomRetakingExam();
				retakingExam.setExamId(tomExam.getExamId());
				retakingExam.setRetakingExamBeginTime(format.parse(tomExam.getRbeginTime().get(i)));
				retakingExam.setRetakingExamEndTime(format.parse(tomExam.getRendTime().get(i)));
				retakingExam.setSort(i+1);
				retakingExamMapper.insertSelective(retakingExam);
				redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, retakingExam.getExamId()+"_"+retakingExam.getSort(), jsonMapper.toJson(retakingExam));
			}
			
			//添加考试员工关联
			TomExamScore examScore;
			TomExamEmpAllocation examEmpAllocation;
			TomExamPaper examPaper=examPaperMapper.selectByExamId(exam.getExamId());
			for(String code:tomExam.getEmpIds()){
				examScore=new TomExamScore();
				examScore.setExamId(tomExam.getExamId());
				examScore.setCode(code);
				examScore.setEmpName(empMapper.selectByPrimaryKey(code).getName());
				examScore.setGradeState("D");
				examScore.setExamCount(0);
				examScore.setTotalPoints(0);
				examScore.setExamTotalTime(0);
				examScore.setCreateTime(tomExam.getBeginTime());
				examScore.setRightNumbe(0);
				examScore.setWrongNumber(examPaper.getTestQuestionCount());
				examScore.setAccuracy(0d);
				examScoreMapper.insertSelective(examScore);
				redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE, examScore.getExamId()+"_"+examScore.getCode(), jsonMapper.toJson(examScore));
				examEmpAllocation=new TomExamEmpAllocation();
				examEmpAllocation.setCode(code);
				examEmpAllocation.setExamId(tomExam.getExamId());
				examEmpAllocation.setCreateTime(new Date());
				examEmpAllocationMapper.insertSelective(examEmpAllocation);
			}
			List<WxMessage> list = new ArrayList<WxMessage>();
			TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
			List<String> listForEmail = new ArrayList<String>();
			String examAppNews ="";
			String examApp ="";
			String examEmail ="";
			if("3".equals(examPaper.getExamPaperType())||"4".equals(examPaper.getExamPaperType())){
				 examAppNews =Question.replace("<name>", tomExam.getExamName()).replace("<beginTime>", format.format(tomExam.getBeginTime())).replace("<endTime>", format.format(tomExam.getEndTime()))
							+"\r\n"+QuestionEn.replace("<name>", tomExam.getExamNameEn()).replace("<beginTime>", format.format(tomExam.getBeginTime())).replace("<endTime>", format.format(tomExam.getEndTime()));
					 examApp =examAppNews+"\r\n"+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomExam.getExamId();
					 examEmail =examAppNews+"\r\n     <a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"</a>";
			}else{
				 examAppNews =Exam.replace("<name>", tomExam.getExamName()).replace("<beginTime>", format.format(tomExam.getBeginTime())).replace("<endTime>", format.format(tomExam.getEndTime()))
							+"\r\n"+ExamEn.replace("<name>", tomExam.getExamNameEn()).replace("<beginTime>", format.format(tomExam.getBeginTime())).replace("<endTime>", format.format(tomExam.getEndTime()));
					 examApp =examAppNews+"\r\n"+Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomExam.getExamId();
					 examEmail =examAppNews+"\r\n     <a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"</a>";
			}
			boolean a = "3".equals(examPaper.getExamPaperType())==true?"4".equals(examPaper.getExamPaperType()):false;
			if(a==true?"Y".equals(QuestionStatus):"Y".equals(ExamStatus)){
			 if ("Y".equals(message.getSendToApp())) {
				TomExamPaper exampaper = examPaperMapper.selectByExamId(tomExam.getExamId());
				List<String> listApp = new ArrayList<String>();
				WxMessage setValue = new WxMessage(tomExam.getExamName(), examAppNews, Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomExam.getExamId(), Config.getProperty("localUrl")+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
				list.add(setValue);
				String wxNewsMessage = sendMessageService.wxNewsMessage(tomExam.getEmpIds(), list);
				//String wxMessage = sendMessageService.wxMessage(tomExam.getEmpIds(), examApp);
				listApp.addAll(tomExam.getEmpIds());
				JSONObject jsonObject = new JSONObject();
				JSONObject jsonO = jsonObject.fromObject(wxNewsMessage);
				String errcode = jsonO.getString("errcode");
				if("0".equals(errcode)){
					
				}else if("60011".equals(errcode)){
					String json="";
					String invaliduser = jsonO.getString("invaliduser");
					String[] split = invaliduser.split("\\|");
					for(String str:split){
						for(int i = 0;i<listApp.size();i++){
							if(str.equalsIgnoreCase(listApp.get(i))){
								listApp.remove(i);
							}
						}
					}
					sendMessageService.wxMessage(listApp, wxNewsMessage);
				}else{
					throw new Exception("消息发送失败");
				}
			}
			if ("Y".equals(message.getSendToEmail())) {
				for(String code:tomExam.getEmpIds()){
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					if(selectByPrimaryKey.getSecretEmail()!=null)
					listForEmail.add(selectByPrimaryKey.getSecretEmail());
				}
				if(listForEmail.size()>0){
					if (message.getSendToEmail().equals("Y")&&listForEmail.size()>0) {
						if(listForEmail.size()>10){
							int part = listForEmail.size()/10;//分批数
							for(int i=0;i<part;i++){
								List<String> partList=listForEmail.subList(0, 10);
								SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", examEmail);
								sm.sendMessage();
								listForEmail.subList(0, 10).clear();
							}
							if(!listForEmail.isEmpty()){
								SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", examEmail);
								sm.sendMessage();
							}
						}else {
							SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", examEmail);
							sm.sendMessage();
						}
					}
					
//					SendMail sm = SendMailUtil.getMail(listForEmail, "【蔚乐学】任务通知", date, "蔚乐学", examEmail);
//					sm.sendMessage();
				}
			}
			if("Y".equals(message.getSendToPhone())){
				for(String code:tomExam.getEmpIds()){
					TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(code);
					sendMessageService.sendMessage(examApp, selectByPrimaryKey.getMobile());
				}
			}
			TomMessages activityMessage = new TomMessages();
			activityMessage.setMessageTitle( tomExam.getExamName());
			activityMessage.setMessageDetails(examAppNews);
			activityMessage.setAppUrl(Config.getProperty("h5Index")+"views/task/exam_examDetail.html?examId="+tomExam.getExamId());
			activityMessage.setPcUrl("<a href=\""+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"\">"+Config.getProperty("pcIndex")+"views/exam/exam_index.html?examId="+tomExam.getExamId()+"</a>");
			activityMessage.setEmpIds(tomExam.getEmpIds());
			activityMessage.setMessageType("0");
			activityMessage.setCreateTime(date);
			activityMessage.setViewCount(0);
			if ("Y".equals(message.getSendToApp())) {
				activityMessage.setSendToApp("Y");
			} else {
				activityMessage.setSendToApp("N");
			}
			if ("Y".equals(message.getSendToEmail())) {
				activityMessage.setSendToEmail("Y");
			} else {
				activityMessage.setSendToEmail("N");
			}
			if ("Y".equals(message.getSendToPhone())) {
				activityMessage.setSendToPhone("Y");
			} else {
				activityMessage.setSendToPhone("N");
			}
			tomMessagesMapper.insertSelective(activityMessage);
			for (String code : tomExam.getEmpIds()) {
				TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
				tomMessagesEmployees.setCreateTime(date);
				tomMessagesEmployees.setEmpCode(code);
				tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
				tomMessagesEmployees.setIsView("N");
				tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
			}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 功能描述：[编辑考试]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年7月13日 下午1:08:50
	 * @param tomExam
	 * @throws Exception
	 */
	@Transactional
	public void updateexam(TomExam tomExam) throws Exception{
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		DateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tomExam.setUpdateTime(new Date());
		tomExam.setBeginTime(format1.parse(tomExam.getBeginTimeS()));
		tomExam.setEndTime(format1.parse(tomExam.getEndTimeS()));
		examMapper.updateByPrimaryKeySelective(tomExam);
		redisCacheManager.hset(CacheConstant.TOM_EXAM, tomExam.getExamId().toString(), jsonMapper.toJson(tomExam));
		
		TomProjectResource resource = new TomProjectResource();
		resource.setCreateTime(new Date());
		resource.setCreator(tomExam.getCreator());
		resource.setOperator(tomExam.getLastOperator());
		resource.setProjectId(Integer.parseInt(tomExam.getParentClassifyId()));
		resource.setResourceId(tomExam.getExamId());
		resource.setStatus("Y");
		resource.setType("E");
		resource.setParentProjectClassifyName(tomExam.getParentProjectClassifyName());
		resource.setUpdateTime(new Date());
		TomProjectResource selectByResource = tomProjectResourceMapper.selectByResource(resource);
		if(null==selectByResource){
			tomProjectResourceMapper.insertSelective(resource);
		}else
		if(!selectByResource.getProjectId().equals(resource.getProjectId())){//判断与原有项目分类是否是一个
			selectByResource.setStatus("N");
			tomProjectResourceMapper.updateByPrimaryKeySelective(selectByResource);
			tomProjectResourceMapper.insertSelective(resource);
		}
		//删除旧补考
		retakingExamMapper.deleteByExamId(tomExam.getExamId());
		//插入新补考
		TomRetakingExam retakingExam;
		
		if(tomExam.getRetakingExamCount()>0){
			TomRetakingExam exam=new TomRetakingExam();
			exam.setExamId(tomExam.getExamId());
			exam.setRetakingExamBeginTime(tomExam.getBeginTime());
			exam.setRetakingExamEndTime(tomExam.getEndTime());
			exam.setSort(0);
			retakingExamMapper.insertSelective(exam);
			redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, exam.getExamId()+"_"+exam.getSort(), jsonMapper.toJson(exam));
			for(int i=0;i<tomExam.getRetakingExamCount();i++){
				retakingExam=new TomRetakingExam();
				retakingExam.setExamId(tomExam.getExamId());
				retakingExam.setRetakingExamBeginTime(format2.parse(tomExam.getRbeginTime().get(i)));
				retakingExam.setRetakingExamEndTime(format2.parse(tomExam.getRendTime().get(i)));
				retakingExam.setSort(i+1);
				retakingExamMapper.insertSelective(retakingExam);
				redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, retakingExam.getExamId()+"_"+retakingExam.getSort(), jsonMapper.toJson(retakingExam));
			}
		}else{
			retakingExam=new TomRetakingExam();
			retakingExam.setExamId(tomExam.getExamId());
			retakingExam.setRetakingExamBeginTime(tomExam.getBeginTime());
			retakingExam.setRetakingExamEndTime(tomExam.getEndTime());
			retakingExam.setSort(0);
			retakingExamMapper.insertSelective(retakingExam);
			redisCacheManager.hset(CacheConstant.TOM_RETAKING_EXAM, retakingExam.getExamId()+"_"+retakingExam.getSort(), jsonMapper.toJson(retakingExam));
		}
		
		//删除旧员工关联
		TomExamEmpAllocation example=new TomExamEmpAllocation();
		example.setExamId(tomExam.getExamId());
		examEmpAllocationMapper.deleteByExample(example);
		//删除旧默认成绩
		TomExamScore examScoreExample=new TomExamScore();
		examScoreExample.setExamId(tomExam.getExamId());
		examScoreExample.setGradeState("D");
		examScoreMapper.deleteByExample(examScoreExample);
		//添加新考试员工关联
		TomExamScore examScore;
		TomExamEmpAllocation examEmpAllocation;
		for(String code:tomExam.getEmpIds()){
			examScore=new TomExamScore();
			examScore.setExamId(tomExam.getExamId());
			examScore.setCode(code);
			examScore.setEmpName(empMapper.selectByPrimaryKey(code).getName());
			examScore.setGradeState("D");
			examScore.setExamCount(0);
			examScore.setTotalPoints(0);
			examScore.setExamTotalTime(0);
			examScore.setCreateTime(tomExam.getBeginTime());
			examScore.setRightNumbe(0);
			examScore.setWrongNumber(0);
			examScore.setAccuracy(0d);
			examScoreMapper.insertSelective(examScore);
			redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE, examScore.getExamId()+"_"+examScore.getCode(), jsonMapper.toJson(examScore));
			examEmpAllocation=new TomExamEmpAllocation();
			examEmpAllocation.setCode(code);
			examEmpAllocation.setExamId(tomExam.getExamId());
			examEmpAllocation.setCreateTime(new Date());
			examEmpAllocationMapper.insertSelective(examEmpAllocation);
		}
	}

	/**
	 * 
	 * 功能描述：[导入成绩]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:49:13
	 * @param examId
	 * @param string
	 */
	public String importResults(int examId, String filePath) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String path=filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		if("xls".equals(path))
			return readXls(filePath,examId);
		else if("xlsx".equals(path))
			return readXlsx(filePath, examId);
		return "模板文件类型不正确。";
	}

	/**
	 * 
	 * 功能描述：[读取xlsx]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:56:56
	 * @param filePath
	 * @param examId
	 */
	@Transactional
	private String readXlsx(String filePath, int examId) throws Exception {
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		TomExam exam=examMapper.selectByPrimaryKey(examId);
		TomExamPaper examPaper=examPaperMapper.selectByExamId(examId);
		List<TomExamScore> examScores=new ArrayList<TomExamScore>();
		
		InputStream is = new FileInputStream(filePath);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow!=null&&xssfRow.getCell(0) != null&&!"".equals(xssfRow.getCell(0))) {
					TomExamScore examScore = new TomExamScore();
					
					XSSFCell examNumber = xssfRow.getCell(0);
					if(!exam.getExamNumber().equals(getValue(examNumber))){
						return "并非此考试的成绩！";
					}
					if(exam.getBeginTime().after(new Date())){
						return "考试并未开始";
					}
					
					XSSFCell empName = xssfRow.getCell(1);
					XSSFCell code = xssfRow.getCell(2);
					XSSFCell gradeState = xssfRow.getCell(3);
					XSSFCell totalPoints = xssfRow.getCell(4);
					XSSFCell examTotalTime = xssfRow.getCell(5);
					XSSFCell rightNumbe = xssfRow.getCell(6);
					XSSFCell wrongNumber = xssfRow.getCell(7);
					examScore.setCode(getValue(code));
					examScore.setExamId(examId);
					examScore.setEmpName(getValue(empName));
					examScore.setGradeState(getValue(gradeState));
					examScore.setTotalPoints((int)Double.parseDouble(getValue(totalPoints)));
					examScore.setExamTotalTime((int)Double.parseDouble(getValue(examTotalTime))*60);
					examScore.setRightNumbe((int)Double.parseDouble(getValue(rightNumbe)));
					examScore.setWrongNumber((int)Double.parseDouble(getValue(wrongNumber)));
					examScore.setAccuracy(Double.parseDouble(getValue(rightNumbe))/(Double.parseDouble(getValue(rightNumbe))+Double.parseDouble(getValue(wrongNumber))));
					
					if(examScore.getRightNumbe()+examScore.getWrongNumber()!=examPaper.getTestQuestionCount()){
						return examScore.getEmpName()+"("+examScore.getCode()+")的题目总数不正确!";
					}else if(examScore.getTotalPoints()>examPaper.getFullMark()||examScore.getTotalPoints()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的分数填写错误!";
					}else if(examScore.getExamTotalTime()>examPaper.getExamTime()*60||examScore.getExamTotalTime()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的考试时长错误!";
					}else if(!"N".equals(examScore.getGradeState())&&!"Y".equals(examScore.getGradeState())){
						return examScore.getEmpName()+"("+examScore.getCode()+")的成绩状态错误!";
					}
					
					TomExamScore scoreExample=new TomExamScore();
					scoreExample.setCode(examScore.getCode());
					scoreExample.setExamId(examId);
					
					List<TomExamScore> selectListByExample = examScoreMapper.selectListByExample(scoreExample);
					if(selectListByExample.size()<=0){
						return examScore.getEmpName()+"("+examScore.getCode()+")不属于本次考试！";
					}else if(selectListByExample.size()==1){
						//判断导入次数
						int remainingCount=examScoreService.getRemainingCount(getValue(code),examId);
						if(remainingCount!=0&&exam.getRetakingExamCount()+1-selectListByExample.get(0).getExamCount()>=remainingCount){
							examScore.setExamCount(selectListByExample.get(0).getExamCount()+1);
							
							TomRetakingExam retakingExample=new TomRetakingExam();
							retakingExample.setExamId(examId);
							retakingExample.setSort(exam.getRetakingExamCount()+1-remainingCount);
							TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
							if (null != retakingExam) {
								if(retakingExam.getRetakingExamBeginTime().before(new Date())){
									if(!selectListByExample.get(0).getGradeState().equals("Y")){
										examScore.setCreateTime(retakingExam.getRetakingExamEndTime());
										examScores.add(examScore);
									}
								}else{
									return "已经完成导入，不能重复导入。";
								}
							}
						}else{
							return "已经完成导入，不能重复导入。";
						}
					}else{
						return examScore.getCode()+"系统数据异常。";
					}
				}
			}
		}
		for(TomExamScore examScore:examScores){
			examScoreMapper.updateSelective(examScore);
			redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE, examScore.getExamId()+"_"+examScore.getCode(), jsonMapper.toJson(examScore));
			//插入考试记录
			TomJoinExamRecord joinExamRecord=new TomJoinExamRecord();
			joinExamRecord.setCode(examScore.getCode());
			joinExamRecord.setExamid(examId);
			joinExamRecord.setCreateTime(examScore.getCreateTime());
			joinExamRecordMapper.insertSelective(joinExamRecord);
			
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(examScore.getCode());
			ebRecord.setUpdateTime(new Date());
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(examScore.getCode());
			if(examScore.getGradeState().equals("N")&&examScoreService.getRemainingCount(examScore.getCode(),examId)<=0){
				if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
				}else{
					userInfo.seteNumber(0);
				}
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				userInfoMapper.updateByCode(userInfo);
				ebRecordMapper.insertSelective(ebRecord);
			}else if(examScore.getGradeState().equals("Y")){
//				Map<Object, Object> map1 = new HashMap<Object, Object>();
//				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//				map1.put("userId", examScore.getCode());
//				map1.put("road", "3");
//				map1.put("date", simple.format(new Date()));
//				List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
//				int ebCount=0;
//				for(TomEbRecord er:ebRList){
//					ebCount+=er.getExchangeNumber();
//				}
//				if(ebCount+examPaper.getPassEb()<=100){
					userInfo.seteNumber(userInfo.geteNumber()+examPaper.getPassEb());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+examPaper.getPassEb());
					ebRecord.setExchangeNumber(examPaper.getPassEb());
					if ("A".equals(exam.getExamType())) {
						ebRecord.setRoad("3");
					}else if ("C".equals(exam.getExamType())) {
						ebRecord.setRoad("14");
					}
					userInfoMapper.updateByCode(userInfo);
					ebRecordMapper.insertSelective(ebRecord);
//				}else{
//					userInfo.seteNumber(userInfo.geteNumber()+100-ebCount);
//					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+100-ebCount);
//					ebRecord.setExchangeNumber(100-ebCount);
//					ebRecord.setRoad("3");
//					userInfoMapper.updateByCode(userInfo);
//					ebRecordMapper.insertSelective(ebRecord);
//				}
			}
		}
		return "导入成功！";
	}

	/**
	 * 
	 * 功能描述：[读取xls]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月25日 上午9:57:08
	 * @param filePath
	 * @param examId
	 */
	@Transactional
	private String readXls(String filePath, int examId) throws Exception {
		TomExamCacher examCahcer = (TomExamCacher)redisCacheManager.getCacher(TomExamCacher.class);
		TomExam exam=examMapper.selectByPrimaryKey(examId);
		TomExamPaper examPaper=examPaperMapper.selectByExamId(examId);
		List<TomExamScore> examScores=new ArrayList<TomExamScore>();
		
		InputStream is = new FileInputStream(filePath);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow!=null&&hssfRow.getCell(0) != null&&!"".equals(hssfRow.getCell(0))) {
					TomExamScore examScore = new TomExamScore();
					
					HSSFCell examNumber = hssfRow.getCell(0);
					if(!exam.getExamNumber().equals(getValue(examNumber))){
						return "并非此考试的成绩!";
					}
					if(exam.getBeginTime().after(new Date())){
						return "考试并未开始";
					}
					
					HSSFCell empName = hssfRow.getCell(1);
					HSSFCell code = hssfRow.getCell(2);
					HSSFCell gradeState = hssfRow.getCell(3);
					HSSFCell totalPoints = hssfRow.getCell(4);
					HSSFCell examTotalTime = hssfRow.getCell(5);
					HSSFCell rightNumbe = hssfRow.getCell(6);
					HSSFCell wrongNumber = hssfRow.getCell(7);
					examScore.setCode(String.valueOf(getValue(code)));
					examScore.setExamId(examId);
					examScore.setEmpName(getValue(empName));
					examScore.setGradeState(getValue(gradeState));
					examScore.setTotalPoints((int)Double.parseDouble(getValue(totalPoints)));
					examScore.setExamTotalTime((int)Double.parseDouble(getValue(examTotalTime))*60);
					examScore.setRightNumbe((int)Double.parseDouble(getValue(rightNumbe)));
					examScore.setWrongNumber((int)Double.parseDouble(getValue(wrongNumber)));
					examScore.setAccuracy(Double.parseDouble(getValue(rightNumbe))/(Double.parseDouble(getValue(rightNumbe))+Double.parseDouble(getValue(wrongNumber))));
					
					if(examScore.getRightNumbe()+examScore.getWrongNumber()!=examPaper.getTestQuestionCount()){
						return examScore.getEmpName()+"("+examScore.getCode()+")的题目总数不正确!";
					}else if(examScore.getTotalPoints()>examPaper.getFullMark()||examScore.getTotalPoints()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的分数填写错误!";
					}else if(examScore.getExamTotalTime()>examPaper.getExamTime()*60||examScore.getExamTotalTime()<0){
						return examScore.getEmpName()+"("+examScore.getCode()+")的考试时长错误!";
					}else if(!"N".equals(examScore.getGradeState())&&!"Y".equals(examScore.getGradeState())){
						return examScore.getEmpName()+"("+examScore.getCode()+")的成绩状态错误!";
					}
					
					TomExamScore scoreExample=new TomExamScore();
					scoreExample.setCode(examScore.getCode());
					scoreExample.setExamId(examId);
					
					//查询员工的现有成绩
					List<TomExamScore> selectListByExample = examScoreMapper.selectListByExample(scoreExample);
					if(selectListByExample.size()<=0){
						//没有则不属于本次考试
						return examScore.getEmpName()+"("+examScore.getCode()+")不属于本次考试！";
					}else if(selectListByExample.size()==1){
						//剩余补考次数
						int remainingCount=examScoreService.getRemainingCount(getValue(code),examId);
						//在还有剩余补考次数的情况下
						if(remainingCount!=0&&exam.getRetakingExamCount()+1-selectListByExample.get(0).getExamCount()>=remainingCount){
							examScore.setExamCount(selectListByExample.get(0).getExamCount()+1);
							
							TomRetakingExam retakingExample=new TomRetakingExam();
							retakingExample.setExamId(examId);
							//sort为下次导入对应的考试
							retakingExample.setSort(exam.getRetakingExamCount()+1-remainingCount);
							TomRetakingExam retakingExam=retakingExamMapper.selectOneByExample(retakingExample);
							//并未到下次导入时间
							if (null != retakingExam) {
								if(retakingExam.getRetakingExamBeginTime().before(new Date())){
									//对于已经通过的成绩不做处理
									if(!selectListByExample.get(0).getGradeState().equals("Y")){
										//将创建时间设置为本次考试的结束时间。（判断一次成绩属于哪次补考是通过此createTime大于开始时间小于等于结束时间）
										examScore.setCreateTime(retakingExam.getRetakingExamEndTime());
										//需要更新的成绩放入list
										examScores.add(examScore);
									}
								}else{
									//throw new EleException("已经完成导入，不能重复导入。");
									return "已经完成导入，不能重复导入。";
								}
							}
							
						}else{
//							throw new EleException("已经完成导入，不能重复导入。");
							return "已经完成导入，不能重复导入。";
						}
					}else{
						//线下考试成绩出现多条异常
						return examScore.getCode()+"系统数据异常。";
					}
				}
			}
		}
		for(TomExamScore examScore:examScores){
			examScoreMapper.updateSelective(examScore);
			redisCacheManager.hset(CacheConstant.TOM_EXAM_SCORE, examScore.getExamId()+"_"+examScore.getCode(), jsonMapper.toJson(examScore));
			//插入考试记录
			TomJoinExamRecord joinExamRecord=new TomJoinExamRecord();
			joinExamRecord.setCode(examScore.getCode());
			joinExamRecord.setExamid(examId);
			joinExamRecord.setCreateTime(examScore.getCreateTime());
			joinExamRecordMapper.insertSelective(joinExamRecord);
			
			TomEbRecord ebRecord=new TomEbRecord();
			ebRecord.setCode(examScore.getCode());
			ebRecord.setUpdateTime(new Date());
			TomUserInfo userInfo=userInfoMapper.selectByPrimaryKey(examScore.getCode());
			if(examScore.getGradeState().equals("N")&&examScoreService.getRemainingCount(examScore.getCode(),examId)<=0){
				if (userInfo.geteNumber()>=examPaper.getNotPassEb()) {
					userInfo.seteNumber(userInfo.geteNumber()- examPaper.getNotPassEb());
				}else{
					userInfo.seteNumber(0);
				}
				ebRecord.setExchangeNumber(-examPaper.getNotPassEb());
				ebRecord.setRoad("9");
				userInfoMapper.updateByCode(userInfo);
				ebRecordMapper.insertSelective(ebRecord);
			}else if(examScore.getGradeState().equals("Y")){
//				Map<Object, Object> map1 = new HashMap<Object, Object>();
//				SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
//				map1.put("userId", examScore.getCode());
//				map1.put("road", "3");
//				map1.put("date", simple.format(new Date()));
//				List<TomEbRecord> ebRList=ebRecordMapper.selectByRode(map1);
//				int ebCount=0;
//				for(TomEbRecord er:ebRList){
//					ebCount+=er.getExchangeNumber();
//				}
//				if(ebCount+examPaper.getPassEb()<=100){
					userInfo.seteNumber(userInfo.geteNumber()+examPaper.getPassEb());
					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+examPaper.getPassEb());
					ebRecord.setExchangeNumber(examPaper.getPassEb());
					if ("A".equals(exam.getExamType())) {
						ebRecord.setRoad("3");
					}else if ("C".equals(exam.getExamType())) {
						ebRecord.setRoad("14");
					}
					userInfoMapper.updateByCode(userInfo);
					ebRecordMapper.insertSelective(ebRecord);
//				}else{
//					userInfo.seteNumber(userInfo.geteNumber()+100-ebCount);
//					userInfo.setAddUpENumber(userInfo.getAddUpENumber()+100-ebCount);
//					ebRecord.setExchangeNumber(100-ebCount);
//					ebRecord.setRoad("3");
//					userInfoMapper.updateByCode(userInfo);
//					ebRecordMapper.insertSelective(ebRecord);
//				}
			}
		}
		return "导入成功！";
	}
	
	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:16
	 * @param xssfRow
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}
	/**
	 * 
	 * 功能描述：[类型处理]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年3月8日 下午4:20:32
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings("static-access")
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
	@Transactional
	public void insertSelective(TomExamEmpAllocation examEmpAllocation){
		examEmpAllocationMapper.insertSelective(examEmpAllocation);
	}
	@Transactional
	public void deleteByExample(TomExamEmpAllocation empAllocationExample){
		examEmpAllocationMapper.deleteByExample(empAllocationExample);
	}

	public PageData<RollingDto> findRollingResource(int pageNum,int pageSize,String examname){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomExam example=new TomExam();
		
		if(examname!=null){
			examname=examname.replaceAll("/", "//");
			examname=examname.replaceAll("%", "/%");
			examname=examname.replaceAll("_", "/_");
			
		}
		example.setExamName(examname);
		PageData<RollingDto> page=new PageData<RollingDto>();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("example", example);
		int count =examMapper.countByExample(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);//pageNum * 
		
		List<TomExam> list=examMapper.findList(map);
		List<RollingDto> rollingList=new ArrayList<RollingDto>();
		for(TomExam exam:list){
			TomExamPaper examPaper =examPaperService.findById(exam.getExamPaperId());
			exam.setTestQuestionCount(examPaper.getTestQuestionCount());
			RollingDto dto=new RollingDto();
			dto.setResourceName(exam.getExamName());
			dto.setResourceType("考试");
			dto.setResourceId(exam.getExamId());
			rollingList.add(dto);
		}
		page.setDate(rollingList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;
	}
	
	public String exportscore(TomExam exam,String codes,String fileName){
		String [] codess=codes.split(",");
		List<String> headers=new ArrayList<String>();
		headers.add("员工编号");
		headers.add("姓名");
		headers.add("题目");
		headers.add("答案");
		headers.add("答题时间");

		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(fileName);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String filePath=Config.getProperty("file_path");
        HSSFCell cell ;
        for(int i=0;i<headers.size();i++){
        	cell= row.createCell((short) i);  
            cell.setCellValue(headers.get(i));  
            cell.setCellStyle(style);  
        }
        int rowNum=1;
        for (int i = 0; i < codess.length; i++){
        	String code=codess[i];
        	String name=empMapper.selectByPrimaryKey(code).getName();
        	TomExamAnswerDetails examAnswerDetailsExample=new TomExamAnswerDetails();
        	examAnswerDetailsExample.setCode(code);
        	examAnswerDetailsExample.setExamId(exam.getExamId());
        	List<TomExamAnswerDetails> answerList=answerDetailsMapper.selectByCode(examAnswerDetailsExample);
        	for(TomExamAnswerDetails tomExamAnswerDetails:answerList){
        		row = sheet.createRow((int) rowNum);
        		row.createCell((short) 0).setCellValue(code);
        		row.createCell((short) 1).setCellValue(name);
        		row.createCell((short) 2).setCellValue(tomExamAnswerDetails.getTopic());
        		if(StringUtil.isNotBlank(tomExamAnswerDetails.getEmpAnswer())){
        			String options="";
        			for(String answer:tomExamAnswerDetails.getEmpAnswer().split(",")){
        				TomTopicOption option=topicOptionMapper.selectByPrimaryKey(Integer.valueOf(answer));
        				if(option!=null){
        					options=options+option.getOptionName();
        				}
        			}
        			row.createCell((short) 3).setCellValue(options);
        		}else if(StringUtil.isNotBlank(tomExamAnswerDetails.getSubjectiveItemAnswer())){
        			row.createCell((short) 3).setCellValue(tomExamAnswerDetails.getSubjectiveItemAnswer());
        		}else {
        			row.createCell((short) 3).setCellValue("");
        		}
        		
        		row.createCell((short) 4).setCellValue(simple.format(tomExamAnswerDetails.getCreateTime()));
        		rowNum++;
        	}
        }
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
