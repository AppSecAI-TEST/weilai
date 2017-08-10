
package com.chinamobo.ue.system.restful;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.activity.dao.TomActivityEmpsRelationMapper;
import com.chinamobo.ue.activity.dao.TomActivityMapper;
import com.chinamobo.ue.activity.dao.TomActivityPropertyMapper;
import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.activity.entity.TomActivityEmpsRelation;
import com.chinamobo.ue.activity.entity.TomActivityProperty;
import com.chinamobo.ue.course.dao.TomCourseApplyMapper;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCourseLearningRecordMapper;
import com.chinamobo.ue.course.dao.TomCourseSignInRecordsMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.dao.TomOfflineSignMapper;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourseLearningRecord;
import com.chinamobo.ue.course.entity.TomCourseSignInRecords;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.dao.TomExamMapper;
import com.chinamobo.ue.exam.dao.TomExamPaperMapper;
import com.chinamobo.ue.exam.dao.TomExamScoreMapper;
import com.chinamobo.ue.exam.entity.TomExam;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.entity.TomExamPaper;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomInterfLogMapper;
import com.chinamobo.ue.system.dao.TomMessageDetailsMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.dto.TomSendMessage;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.system.entity.WxMessage;
import com.chinamobo.ue.system.service.ContextInitRedisService;
import com.chinamobo.ue.system.service.DeptServise;
import com.chinamobo.ue.system.service.EmpServise;
import com.chinamobo.ue.system.service.SendMessageService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.PathUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/aa")
@Scope("request")
@Component
public class Timer {
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private ContextInitRedisService contextInitForTreeService;
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private TomActivityEmpsRelationMapper tomActivityEmpsRelationMapper;
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private SendMessageService sendMessageService;
	// @Autowired
	// private TimerServise timerServise;
	@Autowired
	private TomExamEmpAllocationMapper tomExamEmpAllocationMapper;
	@Autowired
	private DeptServise deptservise;
	@Autowired
	private EmpServise empservise;
	@Autowired
	private TomExamPaperMapper tomExamPaperMapper;
	@Autowired
	private TomInterfLogMapper tomInterfLogMapper;
	@Autowired
	private TomCoursesMapper tomCoursesMapper;
	@Autowired
	private TomActivityPropertyMapper tomActivityPropertyMapper;
	@Autowired
	private TomCourseEmpRelationMapper tomCourseEmpRelationMapper;
	@Autowired
	private TomCourseLearningRecordMapper tomCourseLearningRecordMapper;
	@Autowired
	private TomCourseSignInRecordsMapper tomCourseSignInRecordsMapper;
	@Autowired
	private TomExamScoreMapper tomExamScoreMapper;
	@Autowired
	private TomExamMapper tomExamMapper;
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Autowired
	private TomActivityMapper tomActivityMapper;
	@Autowired
	private TomOfflineSignMapper tomOfflineSignMapper;
	@Autowired
	private TomCourseApplyMapper courseApplyMapper;

	private static Logger logger = LoggerFactory.getLogger(Timer.class);
	// @GET
	// @Path("/loginpage")
	// public String run() {
	// try{
	// timerServise.add();
	// }catch(Exception e){
	// TomInterfLog interLog = new TomInterfLog();
	// interLog.setCreateTime(new Date());
	// interLog.setLogId(4);
	// interLog.setLogInfo(e.toString());
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	@GET
	@Path("/sendMessage")
	public String sendMessage() throws Exception {
		String messageStatus=Config.getProperty("messageStatus");
		if(!"true".equals(messageStatus)){
			return "";
		}
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String localUrl = Config.getProperty("localUrl");
		List<String> examEndTime = new ArrayList<String>(); // 考试结束前时间
		List<String> downCourseBeginTime = new ArrayList<String>(); // 线下课程开始前时间
		List<String> beforeActTime = new ArrayList<String>(); // 活动开始前时间
		List<String> endActTime = new ArrayList<String>(); // 活动结束前时间
		List<String> beforeCOnlineTime = new ArrayList<String>(); // 线上课程开始前时间
		List<String> endCDownTime = new ArrayList<String>(); // 线下课程结束前时间
		List<String> beforeExamTime = new ArrayList<String>(); // 考试开始前时间
		List<String> endCOnlineTime = new ArrayList<String>(); // 线上课程结束前时间
		List<String> questionEndTime = new ArrayList<String>(); // 问卷结束前时间
		List<String> questionBeforeTime = new ArrayList<String>(); // 问卷开始前时间
		List<String>  lineCourseShiftTime= new ArrayList<String>(); // 线下课程班次开始前时间
		String lineCourseShift = "";//线下课程班次
		String lineCourseShiftEn = "";
		String lineCourseShiftStatus = "";
		String questionEnd = ""; // 问卷结束前
		String questionEndEn = "";
		String questionEndStatus = "";
		String questionBefore = ""; // 问卷开始前
		String questionBeforeEn = "";
		String questionBeforeStatus = "";
		String examEnd = ""; // 考试结束前
		String examEndEn = "";
		String examEndStatus = "";
		String downCourseBegin = ""; // 线下课程开始前
		String downCourseBeginEn = "";
		String downCourseBeginStatus = "";
		String beforeAct = ""; // 活动开始前
		String beforeActEn = "";
		String beforeActStatus = "";
		String beforeCOnline = ""; // 线上课程开始前
		String beforeCOnlineEn = "";
		String beforeCOnlineStatus = "";
		String beforeExam = ""; // 考试开始前
		String beforeExamEn = "";
		String beforeExamStatus = "";
		String endAct = ""; // 活动结束前
		String endActEn = "";
		String endActStatus = "";
		String endCOnline = ""; // 线上课程结束前
		String endCOnlineEn = "";
		String endCOnlineStatus = "";
		String endCDown = ""; // 线下课程结束前
		String endCDownEn = "";
		String endCDownStatus = "";
//		String bxCOnline = ""; // 必修线上课程
//		String bxCOnlineEn = "";
//		String bxCOnlineStatus = "";
//		String xxCOnline = ""; // 修线上课程
//		String xxCOnlineEn = "";
//		String xxCOnlineStatus = "";
//		String cExam = ""; // 课程后的考试
//		String cExamEn = "";
//		String cExamStatus = "";
//		String xxCQuestion = ""; // 选修课程后的问卷调查
//		String xxCQuestionEn = "";
//		String xxCQuestionStatus = "";
		List<TomMessageDetails> selectList = tomMessageDetailsMapper.selectList();
		for (TomMessageDetails message : selectList) {
			if (message.getId() == 57) {
				lineCourseShiftTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 41) {
				examEndTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 42) {
				downCourseBeginTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 43) {
				beforeActTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 44) {
				endActTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 45) {
				beforeCOnlineTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 46) {
				endCDownTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 47) {
				beforeExamTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 48) {
				endCOnlineTime = Arrays.asList(message.getMessage().split(","));
			}
			if(message.getId() == 54){
				questionEndTime = Arrays.asList(message.getMessage().split(","));
			}
			if(message.getId() == 53){
				questionBeforeTime = Arrays.asList(message.getMessage().split(","));
			}
			if (message.getId() == 58) {
				lineCourseShift = message.getMessage();
				lineCourseShiftStatus = message.getStatus();
			}
			if (message.getId() == 59) {
				lineCourseShiftEn = message.getMessage();
			}
			if (message.getId() == 23) {
				examEnd = message.getMessage();
				examEndStatus = message.getStatus();
			}
			if (message.getId() == 24) {
				examEndEn = message.getMessage();
			}
			if (message.getId() == 25) {
				downCourseBegin = message.getMessage();
				downCourseBeginStatus = message.getStatus();
			}
			if (message.getId() == 26) {
				downCourseBeginEn = message.getMessage();
			}
			if (message.getId() == 27) {
				beforeAct = message.getMessage();
				beforeActStatus = message.getStatus();
			}
			if (message.getId() == 28) {
				beforeActEn = message.getMessage();
			}
			if (message.getId() == 29) {
				beforeCOnline = message.getMessage();
				beforeCOnlineStatus = message.getStatus();
			}
			if (message.getId() == 30) {
				beforeCOnlineEn = message.getMessage();
			}
			if (message.getId() == 31) {
				beforeExam = message.getMessage();
				beforeExamStatus = message.getStatus();
			}
			if (message.getId() == 32) {
				beforeExamEn = message.getMessage();
			}
			if (message.getId() == 33) {
				endAct = message.getMessage();
				endActStatus = message.getStatus();
			}
			if (message.getId() == 34) {
				endActEn = message.getMessage();
			}
			if (message.getId() == 35) {
				endCOnline = message.getMessage();
				endCOnlineStatus = message.getStatus();
			}
			if (message.getId() == 36) {
				endCOnlineEn = message.getMessage();
			}
			if (message.getId() == 37) {
				endCDown = message.getMessage();
				endCDownStatus = message.getStatus();
			}
			if (message.getId() == 38) {
				endCDownEn = message.getMessage();
			}
			if(message.getId() == 49){
				questionBefore= message.getMessage();
				questionBeforeStatus=	 message.getStatus();
			}
			if(message.getId() == 50){
				questionBeforeEn=message.getMessage();
			}
			if(message.getId() == 51){
				questionEnd= message.getMessage();
				questionEndStatus=	 message.getStatus();
			}
			if(message.getId() == 52){
				questionEndEn=message.getMessage();
			}
//			if (message.getId() == 9) {
//				bxCOnline = message.getMessage();
//				bxCOnlineStatus = message.getStatus();
//			}
//			if (message.getId() == 10) {
//				bxCOnlineEn = message.getMessage();
//			}
//			if (message.getId() == 13) {
//				xxCOnline = message.getMessage();
//				xxCOnlineStatus = message.getStatus();
//			}
//			if (message.getId() == 14) {
//				xxCOnlineEn = message.getMessage();
//			}
//			if (message.getId() == 15) {
//				cExam = message.getMessage();
//				cExamStatus = message.getStatus();
//			}
//			if (message.getId() == 16) {
//				cExamEn = message.getMessage();
//			}
		}
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		TomMessages message = tomMessagesMapper.selectByPrimaryKey(0);
		
		if ("Y".equals(lineCourseShiftStatus)) { //活动线下课程班次开始前
			List<TomSendMessage> selectTaskStart = tomEmpMapper.selectTaskStart3(lineCourseShiftTime);
			for (TomSendMessage tomSendMessage : selectTaskStart) {
				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
				String selectTaskStart1 = lineCourseShift.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
						.replace("<address>", tomSendMessage.getAddress())
						+ "\r\n"
						+ lineCourseShiftEn.replace("<name>", tomSendMessage.getNameEn())
								.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
								.replace("<endTime>", simple.format(tomSendMessage.getTime()))
								.replace("<addressEn>", tomSendMessage.getAddressEn());
				String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
				+ "views/course/offcourse_learning.html?courseId="+tomSendMessage.getCourseId()+"\">" + Config.getProperty("pcIndex")
				+ "views/course/offcourse_learning.html?courseId=" + tomSendMessage.getCourseId() + "</a>";
				String selectTaskStartApp = Config.getProperty("h5Index") + "views/task/offline_course.html?courseId="+tomSendMessage.getCourseId();
				List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper.selectByActivityId2(tomSendMessage.getId());
				List<String> list3 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("courseId", tomSendMessage.getCourseId());
					map.put("code", tomActivityEmpsRelation.getCode());
					map.put("courseClasses", tomSendMessage.getPreTask());
					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
						int coursed = courseApplyMapper.countEmp(map);
						if(coursed>0){
							list3.add(tomActivityEmpsRelation.getCode());
							TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
							if (emp.getSecretEmail() != null)
								listEmail.add(emp.getSecretEmail());
						}
					}
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
//					sm.sendMessage();
				}
				if(list3.size()>0){
					if (message.getSendToApp().equals("Y")) {
						List<WxMessage> wx = new ArrayList<WxMessage>();
						WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskStartApp,
								localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
						wx.add(setValue);
//						String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
//						// String wxMessage = sendMessageService.wxMessage(list3,
//						// selectTaskStart1);
//						List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
//						if (listApp.size() != 0) {
//							sendMessageService.wxNewsMessage(listApp, wx);
//						}
						List<String> insertList=new ArrayList<String>();
						if(list3.size()>1000){
							int part = list3.size()/1000;//分批数
							for(int i=0;i<part;i++){
								List<String> partList=list3.subList(0, 1000);
								String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
								List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
								if (listApp.size() != 0) {
									sendMessageService.wxNewsMessage(listApp, wx);
									insertList.addAll(listApp);
								}else {
									insertList.addAll(partList);
								}
								list3.subList(0, 1000).clear();
							}
							if(!listEmail.isEmpty()){
								String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
								List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
								if (listApp.size() != 0) {
									sendMessageService.wxNewsMessage(listApp, wx);
									insertList.addAll(listApp);
								}else {
									insertList.addAll(list3);
								}
							}
							list3=insertList;
						}else {
							String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
							List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
							if (listApp.size() != 0) {
								list3=listApp;
								sendMessageService.wxNewsMessage(listApp, wx);
							}
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(lineCourseShift.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
						.replace("<address>", tomSendMessage.getAddress()));
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(lineCourseShiftEn.replace("<name>", tomSendMessage.getNameEn())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
						.replace("<addressEn>", tomSendMessage.getAddressEn()));
				activityMessage.setEmpIds(list3);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				activityMessage.setAppUrl(selectTaskStartApp);
				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
						+ "views/course/course_my.html" + "</a>");
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list3) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
			//单独的线下课程班次开始前
			List<TomSendMessage> selectOfflineStart = tomEmpMapper.selectOfflineStart1(lineCourseShiftTime);
			for (TomSendMessage tomSendMessage : selectOfflineStart) {
				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
				String selectTaskStart1 = lineCourseShift.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
						.replace("<address>", tomSendMessage.getAddress())
						+ "\r\n"
						+ lineCourseShiftEn.replace("<name>", tomSendMessage.getNameEn())
								.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
								.replace("<endTime>", simple.format(tomSendMessage.getTime()))
								.replace("<addressEn>", tomSendMessage.getAddressEn());
				String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
				+ "views/course/offcourse_learning.html?courseId="+tomSendMessage.getCourseId()+"\">" + Config.getProperty("pcIndex")
				+ "views/course/offcourse_learning.html?courseId=" + tomSendMessage.getCourseId() + "</a>";
				String selectTaskStartApp = Config.getProperty("h5Index") + "views/task/offline_course.html?courseId="+tomSendMessage.getCourseId();
				List<TomCourseEmpRelation> codes = tomCourseEmpRelationMapper.selectByCourseId(tomSendMessage.getCourseId());
				List<String> list3 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomCourseEmpRelation tomCourseEmpRelation : codes) {
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("courseId", tomSendMessage.getCourseId());
					map.put("code", tomCourseEmpRelation.getCode());
					map.put("courseClasses", tomSendMessage.getPreTask());
					if ("Y".equals(tomCourseEmpRelation.getStatus())) {
						int coursed = courseApplyMapper.countEmp(map);
						if(coursed>0){
							list3.add(tomCourseEmpRelation.getCode());
							TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomCourseEmpRelation.getCode());
							if (emp.getSecretEmail() != null)
								listEmail.add(emp.getSecretEmail());
						}
					}
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
//					sm.sendMessage();
				}
				if(list3.size()>0){
					if (message.getSendToApp().equals("Y")) {
						List<WxMessage> wx = new ArrayList<WxMessage>();
						WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskStartApp,
								localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
						wx.add(setValue);
						List<String> insertList=new ArrayList<String>();
						if(list3.size()>1000){
							int part = list3.size()/1000;//分批数
							for(int i=0;i<part;i++){
								List<String> partList=list3.subList(0, 1000);
								String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
								List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
								if (listApp.size() != 0) {
									sendMessageService.wxNewsMessage(listApp, wx);
									insertList.addAll(listApp);
								}else {
									insertList.addAll(partList);
								}
								list3.subList(0, 1000).clear();
							}
							if(!listEmail.isEmpty()){
								String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
								List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
								if (listApp.size() != 0) {
									sendMessageService.wxNewsMessage(listApp, wx);
									insertList.addAll(listApp);
								}else {
									insertList.addAll(list3);
								}
							}
							list3=insertList;
						}else {
							String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
							List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
							if (listApp.size() != 0) {
								list3=listApp;
								sendMessageService.wxNewsMessage(listApp, wx);
							}
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(lineCourseShift.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
						.replace("<address>", tomSendMessage.getAddress()));
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(lineCourseShiftEn.replace("<name>", tomSendMessage.getNameEn())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
						.replace("<addressEn>", tomSendMessage.getAddressEn()));
				activityMessage.setEmpIds(list3);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				activityMessage.setAppUrl(selectTaskStartApp);
				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
						+ "views/course/course_my.html" + "</a>");
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list3) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}
		
//		if ("Y".equals(downCourseBeginStatus)) {
//			List<TomSendMessage> selectTaskStart = tomEmpMapper.selectTaskStart(downCourseBeginTime); // 线下课程开始前
//			// 线下课程开始前
//			for (TomSendMessage tomSendMessage : selectTaskStart) {
//				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
//				String selectTaskStart1 = downCourseBegin.replace("<name>", tomSendMessage.getName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
////						.replace("<address>", tomSendMessage.getAddress())
//						+ "\r\n"
//						+ downCourseBeginEn.replace("<name>", tomSendMessage.getNameEn())
//								.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//								.replace("<endTime>", simple.format(tomSendMessage.getTime()));
////								.replace("<address>", tomSendMessage.getAddress());
//				String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>";
//				String selectTaskStartApp = Config.getProperty("h5Index") + "views/user/my-course.html";
//				List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
//						.selectByActivityId2(tomSendMessage.getId());
//				List<String> list3 = new ArrayList<String>();
//				List<String> listEmail = new ArrayList<String>();
//				for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
//					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
//						
//						list3.add(tomActivityEmpsRelation.getCode());
//						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//						if (emp.getSecretEmail() != null)
//							listEmail.add(emp.getSecretEmail());
//					}
//				}
//				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
//					sm.sendMessage();
//				}
//				if (message.getSendToApp().equals("Y")) {
//					List<WxMessage> wx = new ArrayList<WxMessage>();
//					WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskStartApp,
//							localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
//					wx.add(setValue);
//					String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
//					// String wxMessage = sendMessageService.wxMessage(list3,
//					// selectTaskStart1);
//					List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
//					if (listApp.size() != 0) {
//						sendMessageService.wxNewsMessage(listApp, wx);
//					}
//				}
//				TomMessages activityMessage = new TomMessages();
//				activityMessage.setViewCount(0);
//				if ("Y".equals(message.getSendToApp())) {
//					activityMessage.setSendToApp("Y");
//				} else {
//					activityMessage.setSendToApp("N");
//				}
//				if ("Y".equals(message.getSendToEmail())) {
//					activityMessage.setSendToEmail("Y");
//				} else {
//					activityMessage.setSendToEmail("N");
//				}
//				if ("Y".equals(message.getSendToPhone())) {
//					activityMessage.setSendToPhone("Y");
//				} else {
//					activityMessage.setSendToPhone("N");
//				}
//				activityMessage.setMessageTitle(tomSendMessage.getName());
//				activityMessage.setMessageDetails(downCourseBegin.replace("<name>", tomSendMessage.getName()));
//				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
//				activityMessage.setMessageDetailsEn(downCourseBeginEn.replace("<name>", tomSendMessage.getNameEn()));
//				activityMessage.setEmpIds(list3);
//				activityMessage.setMessageType("0");
//				activityMessage.setCreateTime(new Date());
//				activityMessage.setAppUrl(selectTaskStartApp);
//				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>");
//				tomMessagesMapper.insertSelective(activityMessage);
//				for (String code : list3) {
//					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//					tomMessagesEmployees.setCreateTime(new Date());
//					tomMessagesEmployees.setEmpCode(code);
//					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//					tomMessagesEmployees.setIsView("N");
//					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//				}
//			}
//			
//			//单独的线下课程
//			List<TomSendMessage> selectOfflineStart = tomEmpMapper.selectOfflineStart(downCourseBeginTime);
//			for (TomSendMessage tomSendMessage : selectOfflineStart) {
//				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
//				String selectTaskStart1 = downCourseBegin.replace("<name>", tomSendMessage.getName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
////						.replace("<address>", tomSendMessage.getAddress())
//						+ "\r\n"
//						+ downCourseBeginEn.replace("<name>", tomSendMessage.getNameEn())
//								.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//								.replace("<endTime>", simple.format(tomSendMessage.getTime()));
////								.replace("<address>", tomSendMessage.getAddress());
//				String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>";
//				String selectTaskStartApp = Config.getProperty("h5Index") + "views/user/my-course.html";
//				List<TomCourseEmpRelation> codes = tomCourseEmpRelationMapper.selectByCourseId(tomSendMessage.getCourseId());
//				List<String> list3 = new ArrayList<String>();
//				List<String> listEmail = new ArrayList<String>();
//				for (TomCourseEmpRelation tomCourseEmpRelation : codes) {
//					if ("Y".equals(tomCourseEmpRelation.getStatus())) {
//						list3.add(tomCourseEmpRelation.getCode());
//						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomCourseEmpRelation.getCode());
//						if (emp.getSecretEmail() != null)
//							listEmail.add(emp.getSecretEmail());
//					}
//				}
//				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
//					sm.sendMessage();
//				}
//				if (message.getSendToApp().equals("Y")) {
//					List<WxMessage> wx = new ArrayList<WxMessage>();
//					WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskStartApp,
//							localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
//					wx.add(setValue);
//					String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
//					// String wxMessage = sendMessageService.wxMessage(list3,
//					// selectTaskStart1);
//					List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
//					if (listApp.size() != 0) {
//						sendMessageService.wxNewsMessage(listApp, wx);
//					}
//				}
//				TomMessages activityMessage = new TomMessages();
//				activityMessage.setViewCount(0);
//				if ("Y".equals(message.getSendToApp())) {
//					activityMessage.setSendToApp("Y");
//				} else {
//					activityMessage.setSendToApp("N");
//				}
//				if ("Y".equals(message.getSendToEmail())) {
//					activityMessage.setSendToEmail("Y");
//				} else {
//					activityMessage.setSendToEmail("N");
//				}
//				if ("Y".equals(message.getSendToPhone())) {
//					activityMessage.setSendToPhone("Y");
//				} else {
//					activityMessage.setSendToPhone("N");
//				}
//				activityMessage.setMessageTitle(tomSendMessage.getName());
//				activityMessage.setMessageDetails(downCourseBegin.replace("<name>", tomSendMessage.getName()));
//				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
//				activityMessage.setMessageDetailsEn(downCourseBeginEn.replace("<name>", tomSendMessage.getNameEn()));
//				activityMessage.setEmpIds(list3);
//				activityMessage.setMessageType("0");
//				activityMessage.setCreateTime(new Date());
//				activityMessage.setAppUrl(selectTaskStartApp);
//				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>");
//				tomMessagesMapper.insertSelective(activityMessage);
//				for (String code : list3) {
//					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//					tomMessagesEmployees.setCreateTime(new Date());
//					tomMessagesEmployees.setEmpCode(code);
//					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//					tomMessagesEmployees.setIsView("N");
//					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//				}
//			}
//		}

		if ("Y".equals(examEndStatus)) {
			List<TomSendMessage> selectExamEnd = tomEmpMapper.selectExamEnd(examEndTime); // 考试结束前
			for (TomSendMessage tomSendMessage : selectExamEnd) {
				TomExam exam = tomExamMapper.selectByPrimaryKey(tomSendMessage.getId());
				TomExamPaper exampaper1 = tomExamPaperMapper.selectByExamId(exam.getExamId());
				if(!exampaper1.getExamPaperType().equals("3")&&!exampaper1.getExamPaperType().equals("4")){
					if("A".equals(exam.getExamType())){
						TomActivityProperty selectByExamId = tomActivityPropertyMapper.selectByExamId(tomSendMessage.getId());
						if(!"".equals(selectByExamId.getPreTask())){
							continue;
						}
					}
					}else{
						continue;
					}
				String cn =examEnd.replace("<name>", exam.getExamName())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String en =examEndEn.replace("<name>", exam.getExamNameEn())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String appNews = examEnd.replace("<name>", exam.getExamName())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()))
						+ "\r\n"
						+ examEndEn.replace("<name>", exam.getExamNameEn())
								.replace("<beginTime>", simple.format(exam.getBeginTime()))
								.replace("<endTime>", simple.format(exam.getEndTime()));
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "\">"
						+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId=" + tomSendMessage.getId()
						+ "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId();		
				TomExamEmpAllocation examEmp = new TomExamEmpAllocation();
				examEmp.setExamId(tomSendMessage.getId());
				List<TomExamEmpAllocation> selectListByExample = tomExamEmpAllocationMapper
						.selectListByExample(examEmp);
				List<String> list2 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomExamEmpAllocation tomExamEmpAllocation : selectListByExample) {
					list2.add(tomExamEmpAllocation.getCode());
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomExamEmpAllocation.getCode());
					if (emp.getSecretEmail() != null)
						listEmail.add(emp.getSecretEmail());
					// if(message.getSendToPhone().equals("Y")){
					// sendMessageService.sendMessage(selectTaskEnd2,
					// emp.getMobile());
					// }
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<String> list = new ArrayList<String>();
					list.add(tomSendMessage.getCode());
					List<WxMessage> wx = new ArrayList<WxMessage>();
					TomExamPaper exampaper = tomExamPaperMapper.selectByExamId(tomSendMessage.getId());
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list2.size()>1000){
						int part = list2.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list2.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list2.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
							List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list2);
							}
						}
						list2=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
						List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
						if (listApp.size() != 0) {
							list2=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
								+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "</a>");
				activityMessage.setEmpIds(list2);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list2) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

		if ("Y".equals(beforeActStatus)) {
			List<TomSendMessage> selectBeforeAct = tomEmpMapper.selectAct(beforeActTime);// 活动开始前
			// 活动开始前
			for (TomSendMessage tomSendMessage : selectBeforeAct) {
				TomActivity activity = tomActivityMapper.selectByPrimaryKey(tomSendMessage.getId());
				String cn = beforeAct.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String en =beforeActEn.replace("<name>", tomSendMessage.getNameEn())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String appNews = cn						+ "\r\n"						+ en;
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/task_center/train_actcon.html?activityId=" + tomSendMessage.getId() + "\">"
						+ Config.getProperty("pcIndex") + "views/task_center/train_actcon.html?activityId="
						+ tomSendMessage.getId() + "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/activity_detail.html?activityId="
						+ tomSendMessage.getId();
				List<TomActivityEmpsRelation> selectByActivityId = tomActivityEmpsRelationMapper
						.selectByActivityId(tomSendMessage.getId());
				List<String> list2 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomActivityEmpsRelation tomActivityEmpsRelation : selectByActivityId) {
					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
						list2.add(tomActivityEmpsRelation.getCode());
						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
						if (emp.getSecretEmail() != null)
							listEmail.add(emp.getSecretEmail());
					}
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
				}
				if (message.getSendToApp().equals("Y")) {
					List<WxMessage> wx = new ArrayList<WxMessage>();
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+activity.getActPicture());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list2.size()>1000){
						int part = list2.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list2.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list2.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
							List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list2);
							}
						}
						list2=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
						List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
						if (listApp.size() != 0) {
							list2=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/activity_detail.html?activityId="
						+ tomSendMessage.getId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/task_center/train_actcon.html?activityId="
								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
								+ "views/task_center/train_actcon.html?activityId=" + tomSendMessage.getId() + "</a>");
				activityMessage.setEmpIds(list2);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list2) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

//		if ("Y".equals(endActStatus)) {
//			List<TomSendMessage> selectEndAct = tomEmpMapper.selectEndAct(endActTime);// 活动结束前
//			// 活动结束前
//			for (TomSendMessage tomSendMessage : selectEndAct) {
//				TomActivity activity = tomActivityMapper.selectByPrimaryKey(tomSendMessage.getId());
//				String cn =endAct.replace("<name>", tomSendMessage.getName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String en =endActEn.replace("<name>", tomSendMessage.getNameEn())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String appNews = cn
//						+ "\r\n"
//						+ en;
//				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/task_center/train_actcon.html?activityId=" + tomSendMessage.getId() + "\">"
//						+ Config.getProperty("pcIndex") + "views/task_center/train_actcon.html?activityId="
//						+ tomSendMessage.getId() + "</a>";
//				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/activity_detail.html?activityId="
//						+ tomSendMessage.getId();
//				List<TomActivityEmpsRelation> selectByActivityId = tomActivityEmpsRelationMapper
//						.selectByActivityId(tomSendMessage.getId());
//				List<String> list2 = new ArrayList<String>();
//				List<String> listEmail = new ArrayList<String>();
//				for (TomActivityEmpsRelation tomActivityEmpsRelation : selectByActivityId) {
//					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
//						list2.add(tomActivityEmpsRelation.getCode());
//						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//						if (emp.getSecretEmail() != null)
//							listEmail.add(emp.getSecretEmail());
//					}
//				}
//				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
//				}
//				if (message.getSendToApp().equals("Y")) {
//					List<String> list = new ArrayList<String>();
//					list.add(tomSendMessage.getCode());
//					List<WxMessage> wx = new ArrayList<WxMessage>();
//					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
//							localUrl+PathUtil.PROJECT_NAME+activity.getActPicture());
//					wx.add(setValue);
//					String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
//					// String wxMessage = sendMessageService.wxMessage(list2,
//					// selectTaskEnd2);
//					List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
//					if (listApp.size() != 0) {
//						sendMessageService.wxNewsMessage(listApp, wx);
//					}
//				}
//				TomMessages activityMessage = new TomMessages();
//				activityMessage.setViewCount(0);
//				if ("Y".equals(message.getSendToApp())) {
//					activityMessage.setSendToApp("Y");
//				} else {
//					activityMessage.setSendToApp("N");
//				}
//				if ("Y".equals(message.getSendToEmail())) {
//					activityMessage.setSendToEmail("Y");
//				} else {
//					activityMessage.setSendToEmail("N");
//				}
//				if ("Y".equals(message.getSendToPhone())) {
//					activityMessage.setSendToPhone("Y");
//				} else {
//					activityMessage.setSendToPhone("N");
//				}
//				activityMessage.setMessageTitle(tomSendMessage.getName());
//				activityMessage.setMessageDetails(cn);
//				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
//				activityMessage.setMessageDetailsEn(en);
//				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
//						+ tomSendMessage.getId());
//				activityMessage
//						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
//								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
//								+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "</a>");
//				activityMessage.setEmpIds(list2);
//				activityMessage.setMessageType("0");
//				activityMessage.setCreateTime(new Date());
//				tomMessagesMapper.insertSelective(activityMessage);
//				for (String code : list2) {
//					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//					tomMessagesEmployees.setCreateTime(new Date());
//					tomMessagesEmployees.setEmpCode(code);
//					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//					tomMessagesEmployees.setIsView("N");
//					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//				}
//			}
//		}
		
		if ("Y".equals(endActStatus)) {
			List<TomSendMessage> selectEndAct = tomEmpMapper.selectEndAct(endActTime);// 活动结束前
			// 活动结束前
			for (TomSendMessage tomSendMessage : selectEndAct) {
				TomActivity activity = tomActivityMapper.selectByPrimaryKey(tomSendMessage.getId());
				String cn =endAct.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String en =endActEn.replace("<name>", tomSendMessage.getNameEn())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String appNews = cn
						+ "\r\n"
						+ en;
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/task_center/train_actcon.html?activityId=" + tomSendMessage.getId() + "\">"
						+ Config.getProperty("pcIndex") + "views/task_center/train_actcon.html?activityId="
						+ tomSendMessage.getId() + "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/activity_detail.html?activityId="
						+ tomSendMessage.getId();
				List<TomActivityEmpsRelation> selectByActivityId = tomActivityEmpsRelationMapper
						.selectByActivityId(tomSendMessage.getId());
				List<String> list2 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				//查询培训活动任务包配置表
				List<TomActivityProperty> activityPropertyList = tomActivityPropertyMapper.selectByActivityId(tomSendMessage.getId());
				for (TomActivityEmpsRelation tomActivityEmpsRelation : selectByActivityId) {
					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
						//遍历培训活动任务包配置表，得到课程id和考试id，查询该人员是否完成
						for(TomActivityProperty activityProperty : activityPropertyList){
							if(activityProperty.getCourseId()!=null){
								TomCourseLearningRecord courseLearningRecord = new TomCourseLearningRecord();
								courseLearningRecord.setCode(tomActivityEmpsRelation.getCode());
								courseLearningRecord.setCourseId(activityProperty.getCourseId());
								int countByExample = tomCourseLearningRecordMapper.countByExample(courseLearningRecord);
								if(countByExample<1){
									list2.add(tomActivityEmpsRelation.getCode());
									TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
									if (emp.getSecretEmail() != null)
										listEmail.add(emp.getSecretEmail());
									break;
								}
							}else{
								//判断活动结束前的考试成绩是否合格
								Map<Object, Object> map = new HashMap<Object, Object>();
								map.put("code", tomActivityEmpsRelation.getCode());
								map.put("examId", activityProperty.getExamId());
								map.put("activityEndTime", activity.getActivityEndTime());
								TomExamScore examScore = tomExamScoreMapper.selectOne(map);
								if(examScore.getGradeState().equals("N")||examScore.getGradeState().equals("D")){
									list2.add(tomActivityEmpsRelation.getCode());
									TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
									if (emp.getSecretEmail() != null)
										listEmail.add(emp.getSecretEmail());
									break;
								}
							}
						}
					}
				}
				if(listEmail.size()<1){
					return "";
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<String> list = new ArrayList<String>();
					list.add(tomSendMessage.getCode());
					List<WxMessage> wx = new ArrayList<WxMessage>();
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+activity.getActPicture());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list2.size()>1000){
						int part = list2.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list2.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list2.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
							List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list2);
							}
						}
						list2=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
						List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
						if (listApp.size() != 0) {
							list2=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/activity_detail.html?activityId="
						+ tomSendMessage.getId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/task_center/train_actcon.html?activityId="
								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
								+ "views/task_center/train_actcon.html?activityId=" + tomSendMessage.getId() + "</a>");
				activityMessage.setEmpIds(list2);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list2) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

		if ("Y".equals(beforeCOnlineStatus)) {
			List<TomSendMessage> selectBeforeCOnline = tomEmpMapper.selectCOnline(beforeCOnlineTime);// 线上课程开始前时间
			// 线上课程开始前
			for (TomSendMessage tomSendMessage : selectBeforeCOnline) {
				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
				String cn =beforeCOnline.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String en =beforeCOnlineEn.replace("<name>", tomSendMessage.getNameEn())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String selectTaskStart1 = cn+ "\r\n"+en;
				String selectTaskEndBx1 = selectTaskStart1 + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "</a>";
				;
				String selectTaskEndBx2 = Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
						+ tomSendMessage.getCourseId();
				List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
						.selectByActivityId2(tomSendMessage.getId());
				List<String> list3 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
						list3.add(tomActivityEmpsRelation.getCode());
						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
						if (emp.getSecretEmail() != null)
							listEmail.add(emp.getSecretEmail());
					}
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<WxMessage> wx = new ArrayList<WxMessage>();
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskEndBx2,
							localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list3.size()>1000){
						int part = list3.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list3.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list3.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
							List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list3);
							}
						}
						list3=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
						List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
						if (listApp.size() != 0) {
							list3=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setEmpIds(list3);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				activityMessage.setAppUrl(selectTaskEndBx2); 
				activityMessage.setPcUrl( "<a href=\"" + Config.getProperty("pcIndex") 
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "</a>");
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list3) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

//		if ("Y".equals(endCDownStatus)) {
//			List<TomSendMessage> selectEndCDownTime = tomEmpMapper.selectTaskStart2(endCDownTime);// 线下课程结束前时间
//			// 线下课程结束前
//			for (TomSendMessage tomSendMessage : selectEndCDownTime) {
//				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
//				String cn =endCDown.replace("<name>", tomSendMessage.getName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String en =endCDownEn.replace("<name>", tomSendMessage.getNameEn())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String selectTaskStart1 = cn
//						+ "\r\n"
//						+ en;
//				String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>";
//				String selectTaskStartApp = Config.getProperty("h5Index") + "views/user/my-course.html";
//				List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
//						.selectByActivityId2(tomSendMessage.getId());
//				List<String> list3 = new ArrayList<String>();
//				List<String> listEmail = new ArrayList<String>();
//				for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
//					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
//						list3.add(tomActivityEmpsRelation.getCode());
//						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//						if (emp.getSecretEmail() != null)
//							listEmail.add(emp.getSecretEmail());
//					}
//				}
//				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
//					sm.sendMessage();
//				}
//				if (message.getSendToApp().equals("Y")) {
//					List<WxMessage> wx = new ArrayList<WxMessage>();
//					WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskStartApp,
//							localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
//					wx.add(setValue);
//					String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
//					// String wxMessage = sendMessageService.wxMessage(list3,
//					// selectTaskStartApp);
//					List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
//					if (listApp.size() != 0) {
//						sendMessageService.wxNewsMessage(listApp, wx);
//					}
//				}
//				TomMessages activityMessage = new TomMessages();
//				activityMessage.setViewCount(0);
//				if ("Y".equals(message.getSendToApp())) {
//					activityMessage.setSendToApp("Y");
//				} else {
//					activityMessage.setSendToApp("N");
//				}
//				if ("Y".equals(message.getSendToEmail())) {
//					activityMessage.setSendToEmail("Y");
//				} else {
//					activityMessage.setSendToEmail("N");
//				}
//				if ("Y".equals(message.getSendToPhone())) {
//					activityMessage.setSendToPhone("Y");
//				} else {
//					activityMessage.setSendToPhone("N");
//				}
//				activityMessage.setMessageTitle(tomSendMessage.getName());
//				activityMessage.setMessageDetails(cn);
//				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
//				activityMessage.setMessageDetailsEn(en);
//				activityMessage.setEmpIds(list3);
//				activityMessage.setMessageType("0");
//				activityMessage.setCreateTime(new Date());
//				activityMessage.setAppUrl(selectTaskStartApp); 
//				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>");
//				tomMessagesMapper.insertSelective(activityMessage);
//				for (String code : list3) {
//					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//					tomMessagesEmployees.setCreateTime(new Date());
//					tomMessagesEmployees.setEmpCode(code);
//					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//					tomMessagesEmployees.setIsView("N");
//					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//				}
//			}
//			//单独的线下课程
//			List<TomSendMessage> selectOfflineStart = tomEmpMapper.selectOfflineEnd(downCourseBeginTime);
//			for (TomSendMessage tomSendMessage : selectOfflineStart) {
//				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
//				String selectTaskStart1 = downCourseBegin.replace("<name>", tomSendMessage.getName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
////						.replace("<address>", tomSendMessage.getAddress())
//						+ "\r\n"
//						+ downCourseBeginEn.replace("<name>", tomSendMessage.getNameEn())
//								.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//								.replace("<endTime>", simple.format(tomSendMessage.getTime()));
////								.replace("<address>", tomSendMessage.getAddress());
//				String selectTaskStartEmail = selectTaskStart1 + "\r\n  <a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>";
//				String selectTaskStartApp = Config.getProperty("h5Index") + "views/user/my-course.html";
//				List<TomCourseEmpRelation> codes = tomCourseEmpRelationMapper.selectByCourseId(tomSendMessage.getCourseId());
//				List<String> list3 = new ArrayList<String>();
//				List<String> listEmail = new ArrayList<String>();
//				for (TomCourseEmpRelation tomCourseEmpRelation : codes) {
//					if ("Y".equals(tomCourseEmpRelation.getStatus())) {
//						list3.add(tomCourseEmpRelation.getCode());
//						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomCourseEmpRelation.getCode());
//						if (emp.getSecretEmail() != null)
//							listEmail.add(emp.getSecretEmail());
//					}
//				}
//				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskStartEmail);
//					sm.sendMessage();
//				}
//				if (message.getSendToApp().equals("Y")) {
//					List<WxMessage> wx = new ArrayList<WxMessage>();
//					WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskStartApp,
//							localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
//					wx.add(setValue);
//					String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
//					// String wxMessage = sendMessageService.wxMessage(list3,
//					// selectTaskStart1);
//					List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
//					if (listApp.size() != 0) {
//						sendMessageService.wxNewsMessage(listApp, wx);
//					}
//				}
//				TomMessages activityMessage = new TomMessages();
//				activityMessage.setViewCount(0);
//				if ("Y".equals(message.getSendToApp())) {
//					activityMessage.setSendToApp("Y");
//				} else {
//					activityMessage.setSendToApp("N");
//				}
//				if ("Y".equals(message.getSendToEmail())) {
//					activityMessage.setSendToEmail("Y");
//				} else {
//					activityMessage.setSendToEmail("N");
//				}
//				if ("Y".equals(message.getSendToPhone())) {
//					activityMessage.setSendToPhone("Y");
//				} else {
//					activityMessage.setSendToPhone("N");
//				}
//				activityMessage.setMessageTitle(tomSendMessage.getName());
//				activityMessage.setMessageDetails(downCourseBegin.replace("<name>", tomSendMessage.getName()));
//				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
//				activityMessage.setMessageDetailsEn(downCourseBeginEn.replace("<name>", tomSendMessage.getNameEn()));
//				activityMessage.setEmpIds(list3);
//				activityMessage.setMessageType("0");
//				activityMessage.setCreateTime(new Date());
//				activityMessage.setAppUrl(selectTaskStartApp);
//				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_my.html" + "</a>");
//				tomMessagesMapper.insertSelective(activityMessage);
//				for (String code : list3) {
//					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//					tomMessagesEmployees.setCreateTime(new Date());
//					tomMessagesEmployees.setEmpCode(code);
//					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//					tomMessagesEmployees.setIsView("N");
//					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//				}
//			}
//		}

		if ("Y".equals(beforeExamStatus)) {
			List<TomSendMessage> selectBeforeExam = tomEmpMapper.selectExamBegin(beforeExamTime); // 考试开始前
			// 考试开始前
			for (TomSendMessage tomSendMessage : selectBeforeExam) {
				TomExam exam = tomExamMapper.selectByPrimaryKey(tomSendMessage.getId());
				TomExamPaper exampaper1 = tomExamPaperMapper.selectByExamId(exam.getExamId());
				if(!exampaper1.getExamPaperType().equals("3")&&!exampaper1.getExamPaperType().equals("4")){
				if("A".equals(exam.getExamType())){
					TomActivityProperty selectByExamId = tomActivityPropertyMapper.selectByExamId(tomSendMessage.getId());
					if(!"".equals(selectByExamId.getPreTask())){
						continue;
					}
				}
				}else{
					continue;
				}
				String cn =beforeExam.replace("<name>", exam.getExamName())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String en =beforeExamEn.replace("<name>", exam.getExamNameEn())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String appNews = cn+ "\r\n"
						+ en;
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "\">"
						+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId=" + tomSendMessage.getId()
						+ "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId();
				TomExamEmpAllocation examEmp = new TomExamEmpAllocation();
				examEmp.setExamId(tomSendMessage.getId());
				List<TomExamEmpAllocation> selectListByExample = tomExamEmpAllocationMapper
						.selectListByExample(examEmp);
				List<String> list2 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomExamEmpAllocation tomExamEmpAllocation : selectListByExample) {
					list2.add(tomExamEmpAllocation.getCode());
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomExamEmpAllocation.getCode());
					if (emp.getSecretEmail() != null)
						listEmail.add(emp.getSecretEmail());
					// if(message.getSendToPhone().equals("Y")){
					// sendMessageService.sendMessage(selectTaskEnd2,
					// emp.getMobile());
					// }
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<String> list = new ArrayList<String>();
					list.add(tomSendMessage.getCode());
					List<WxMessage> wx = new ArrayList<WxMessage>();
					TomExamPaper exampaper = tomExamPaperMapper.selectByExamId(tomSendMessage.getId());
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list2.size()>1000){
						int part = list2.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list2.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list2.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
							List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list2);
							}
						}
						list2=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
						List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
						if (listApp.size() != 0) {
							list2=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
								+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "</a>");
				activityMessage.setEmpIds(list2);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list2) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

		if ("Y".equals(questionBeforeStatus)) {
			List<TomSendMessage> selectBeforeExam = tomEmpMapper.selectExamBegin(questionBeforeTime); // 考试开始前
			// 问卷开始前
			for (TomSendMessage tomSendMessage : selectBeforeExam) {
				TomExam exam = tomExamMapper.selectByPrimaryKey(tomSendMessage.getId());
				TomExamPaper exampaper1 = tomExamPaperMapper.selectByExamId(exam.getExamId());
				if(exampaper1.getExamPaperType().equals("3")||exampaper1.getExamPaperType().equals("4")){
					if("A".equals(exam.getExamType())){
						TomActivityProperty selectByExamId = tomActivityPropertyMapper.selectByExamId(tomSendMessage.getId());
						if(!"".equals(selectByExamId.getPreTask())){
							continue;
						}
					}
				}else{
					continue;
				}
				String cn =questionBefore.replace("<name>", exam.getExamName())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String en =questionBeforeEn.replace("<name>", exam.getExamNameEn())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String appNews = cn+ "\r\n"
						+ en;
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "\">"
						+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId=" + tomSendMessage.getId()
						+ "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId();
				TomExamEmpAllocation examEmp = new TomExamEmpAllocation();
				examEmp.setExamId(tomSendMessage.getId());
				List<TomExamEmpAllocation> selectListByExample = tomExamEmpAllocationMapper
						.selectListByExample(examEmp);
				List<String> list2 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomExamEmpAllocation tomExamEmpAllocation : selectListByExample) {
					list2.add(tomExamEmpAllocation.getCode());
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomExamEmpAllocation.getCode());
					if (emp.getSecretEmail() != null)
						listEmail.add(emp.getSecretEmail());
					// if(message.getSendToPhone().equals("Y")){
					// sendMessageService.sendMessage(selectTaskEnd2,
					// emp.getMobile());
					// }
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<String> list = new ArrayList<String>();
					list.add(tomSendMessage.getCode());
					List<WxMessage> wx = new ArrayList<WxMessage>();
					TomExamPaper exampaper = tomExamPaperMapper.selectByExamId(tomSendMessage.getId());
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list2.size()>1000){
						int part = list2.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list2.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list2.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
							List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list2);
							}
						}
						list2=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
						List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
						if (listApp.size() != 0) {
							list2=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
								+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "</a>");
				activityMessage.setEmpIds(list2);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list2) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}
		
		if ("Y".equals(questionEndStatus)) {
			List<TomSendMessage> selectquestionEnd = tomEmpMapper.selectExamEnd(questionEndTime); // 问卷结束前
			// 问卷结束前
			for (TomSendMessage tomSendMessage : selectquestionEnd) {
				TomExam exam = tomExamMapper.selectByPrimaryKey(tomSendMessage.getId());
				TomExamPaper exampaper1 = tomExamPaperMapper.selectByExamId(exam.getExamId());
				if(exampaper1.getExamPaperType().equals("3")||exampaper1.getExamPaperType().equals("4")){
					if("A".equals(exam.getExamType())){
						TomActivityProperty selectByExamId = tomActivityPropertyMapper.selectByExamId(tomSendMessage.getId());
						if(!"".equals(selectByExamId.getPreTask())){
							continue;
						}
					}
				}else{
					continue;
				}
				String cn =questionEnd.replace("<name>", exam.getExamName())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String en =questionEndEn.replace("<name>", exam.getExamNameEn())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String appNews = cn+ "\r\n"
						+ en;
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "\">"
						+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId=" + tomSendMessage.getId()
						+ "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId();
				TomExamEmpAllocation examEmp = new TomExamEmpAllocation();
				examEmp.setExamId(tomSendMessage.getId());
				List<TomExamEmpAllocation> selectListByExample = tomExamEmpAllocationMapper
						.selectListByExample(examEmp);
				List<String> list2 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomExamEmpAllocation tomExamEmpAllocation : selectListByExample) {
					list2.add(tomExamEmpAllocation.getCode());
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomExamEmpAllocation.getCode());
					if (emp.getSecretEmail() != null)
						listEmail.add(emp.getSecretEmail());
					// if(message.getSendToPhone().equals("Y")){
					// sendMessageService.sendMessage(selectTaskEnd2,
					// emp.getMobile());
					// }
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<String> list = new ArrayList<String>();
					list.add(tomSendMessage.getCode());
					List<WxMessage> wx = new ArrayList<WxMessage>();
					TomExamPaper exampaper = tomExamPaperMapper.selectByExamId(tomSendMessage.getId());
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list2.size()>1000){
						int part = list2.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list2.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list2.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
							List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list2);
							}
						}
						list2=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list2, wx);
						List<String> listApp = sendMessageService.sendStatus(list2, wxNewsMessage);
						if (listApp.size() != 0) {
							list2=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
								+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
								+ "views/exam/exam_index.html?examId=" + tomSendMessage.getId() + "</a>");
				activityMessage.setEmpIds(list2);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list2) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}
		
		if ("Y".equals(endCOnlineStatus)) {
			List<TomSendMessage> selectEndCOnline = tomEmpMapper.selectCOnlineEnd(endCOnlineTime);// 线上课程结束前时间
			// 线上课程结束前
			for (TomSendMessage tomSendMessage : selectEndCOnline) {
				TomCourses course = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
				String cn = endCOnline.replace("<name>", tomSendMessage.getName())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String en =endCOnlineEn.replace("<name>", tomSendMessage.getNameEn())
						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
				String selectTaskStart1 = cn+ "\r\n"
						+en;
				String selectTaskEndBx1 = selectTaskStart1 + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "</a>";
				;
				String selectTaskEndBx2 = Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
						+ tomSendMessage.getCourseId();
				List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
						.selectByActivityId2(tomSendMessage.getId());
				List<String> list3 = new ArrayList<String>();
				List<String> listEmail = new ArrayList<String>();
				for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
						list3.add(tomActivityEmpsRelation.getCode());
						TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
						if (emp.getSecretEmail() != null)
							listEmail.add(emp.getSecretEmail());
					}
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<WxMessage> wx = new ArrayList<WxMessage>();
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), selectTaskStart1, selectTaskEndBx2,
							localUrl+PathUtil.PROJECT_NAME+course.getCourseImg());
					wx.add(setValue);
					List<String> insertList=new ArrayList<String>();
					if(list3.size()>1000){
						int part = list3.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=list3.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							list3.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
							List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(list3);
							}
						}
						list3=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(list3, wx);
						List<String> listApp = sendMessageService.sendStatus(list3, wxNewsMessage);
						if (listApp.size() != 0) {
							list3=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setEmpIds(list3);
				activityMessage.setMessageType("0");
				activityMessage.setCreateTime(new Date());
				activityMessage.setAppUrl(selectTaskEndBx2); 
				activityMessage.setPcUrl("<a href=\"" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
						+ tomSendMessage.getId() + "</a>");
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : list3) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

//		if ("Y".equals(bxCOnlineStatus)) {
//			List<TomSendMessage> selectCourseNow = tomEmpMapper.selectCourseNow();
//			// 线上课程
//			for (TomSendMessage tomSendMessage : selectCourseNow) {
//				TomCourses tomCourse = tomCoursesMapper.selectByPrimaryKey(tomSendMessage.getCourseId());
//				String cn =bxCOnline.replace("<name>", tomCourse.getCourseName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String en =bxCOnlineEn.replace("<name>", tomCourse.getCourseNameEn())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String appNewsBx =cn+ "\r\n"
//						+ en;
//				String selectTaskEndBx1 = appNewsBx + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
//						+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
//						+ tomSendMessage.getId() + "</a>";
//				;
//				String selectTaskEndBx2 = Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
//						+ tomCourse.getCourseId();
//				String appNewsXx = xxCOnline.replace("<name>", tomCourse.getCourseName())
//						.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//						.replace("<endTime>", simple.format(tomSendMessage.getTime()))
//						+ "\r\n"
//						+ xxCOnlineEn.replace("<name>", tomCourse.getCourseNameEn())
//								.replace("<beginTime>", simple.format(tomSendMessage.getBeginTime()))
//								.replace("<endTime>", simple.format(tomSendMessage.getTime()));
//				String selectTaskEndXx1 = appNewsXx + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
//						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
//						+ tomSendMessage.getId() + "\">" + Config.getProperty("pcIndex")
//						+ "views/course/course_learning.html?courseId=" + tomSendMessage.getCourseId() + "&activityId="
//						+ tomSendMessage.getId() + "</a>";
//				;
//				String selectTaskEndXx2 = Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
//						+ tomCourse.getCourseId();
//				List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
//						.selectByActivityId2(tomSendMessage.getId());
//
//				List<String> listBx = new ArrayList<String>();
//				List<String> listXx = new ArrayList<String>();
//				List<String> listEmailBx = new ArrayList<String>();
//				List<String> listEmailXx = new ArrayList<String>();
//				for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
//					if ("Y".equals(tomActivityEmpsRelation.getApplyStatus())) {
//						if ("Y".equals(tomActivityEmpsRelation.getIsRequired())) {
//							listBx.add(tomActivityEmpsRelation.getCode());
//							TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//							if (emp.getSecretEmail() != null) {
//								listEmailBx.add(emp.getSecretEmail());
//							}
//						} else {
//							listXx.add(tomActivityEmpsRelation.getCode());
//							TomEmp emp = tomEmpMapper.selectByPrimaryKey(tomActivityEmpsRelation.getCode());
//							if (emp.getSecretEmail() != null) {
//								listEmailXx.add(emp.getSecretEmail());
//							}
//						}
//					}
//				}
//				if (message.getSendToEmail().equals("Y")&&(listEmailBx.size()>0||listEmailXx.size()>0)) {
//					SendMail smBx = SendMailUtil.getMail(listEmailBx, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndBx1);
//					SendMail smXx = SendMailUtil.getMail(listEmailXx, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEndXx1);
//					smBx.sendMessage();
//					smXx.sendMessage();
//				}
//				if (message.getSendToApp().equals("Y")) {
//					List<WxMessage> list1 = new ArrayList<WxMessage>();
//					WxMessage setValueExam = new WxMessage(tomCourse.getCourseName(), appNewsBx, selectTaskEndBx2,
//							localUrl+PathUtil.PROJECT_NAME+tomCourse.getCourseImg());
//					list1.add(setValueExam);
//					List<WxMessage> list2 = new ArrayList<WxMessage>();
//					WxMessage setValueExam2 = new WxMessage(tomCourse.getCourseName(), appNewsXx, selectTaskEndXx2,
//							tomCourse.getCourseImg());
//					list2.add(setValueExam2);
//					if (listBx.size() != 0) {
//						String wxNewsMessage = sendMessageService.wxNewsMessage(listBx, list1);
//						List<String> listApp = sendMessageService.sendStatus(listBx, wxNewsMessage);
//						if (listApp.size() != 0) {
//							sendMessageService.wxNewsMessage(listApp, list1);
//						}
//					} else if (listXx.size() != 0) {
//						String wxNewsMessage = sendMessageService.wxNewsMessage(listXx, list2);
//						List<String> listApp = sendMessageService.sendStatus(listXx, wxNewsMessage);
//						if (listApp.size() != 0) {
//							sendMessageService.wxNewsMessage(listApp, list2);
//						}
//					}
//				}
//				if (listBx.size() != 0) {
//					TomMessages activityMessage = new TomMessages();
//					activityMessage.setViewCount(0);
//					if ("Y".equals(message.getSendToApp())) {
//						activityMessage.setSendToApp("Y");
//					} else {
//						activityMessage.setSendToApp("N");
//					}
//					if ("Y".equals(message.getSendToEmail())) {
//						activityMessage.setSendToEmail("Y");
//					} else {
//						activityMessage.setSendToEmail("N");
//					}
//					if ("Y".equals(message.getSendToPhone())) {
//						activityMessage.setSendToPhone("Y");
//					} else {
//						activityMessage.setSendToPhone("N");
//					}
//					activityMessage.setMessageTitle(tomSendMessage.getName());
//					activityMessage.setMessageDetails(cn);
//					activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
//					activityMessage.setMessageDetailsEn(en);
//					activityMessage
//							.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/course/course_learning.html?courseId="
//									+ tomSendMessage.getCourseId() + "&activityId=" + tomSendMessage.getId() + "</a>");
//					activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
//							+ tomCourse.getCourseId());
//					activityMessage.setEmpIds(listBx);
//					activityMessage.setMessageType("3");
//					activityMessage.setCreateTime(new Date());
//					tomMessagesMapper.insertSelective(activityMessage);
//					for (String code : listBx) {
//						TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//						tomMessagesEmployees.setCreateTime(new Date());
//						tomMessagesEmployees.setEmpCode(code);
//						tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//						tomMessagesEmployees.setIsView("N");
//						tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//					}
//				}
//				if (listXx.size() != 0) {
//					TomMessages activityMessage = new TomMessages();
//					activityMessage.setViewCount(0);
//					if ("Y".equals(message.getSendToApp())) {
//						activityMessage.setSendToApp("Y");
//					} else {
//						activityMessage.setSendToApp("N");
//					}
//					if ("Y".equals(message.getSendToEmail())) {
//						activityMessage.setSendToEmail("Y");
//					} else {
//						activityMessage.setSendToEmail("N");
//					}
//					if ("Y".equals(message.getSendToPhone())) {
//						activityMessage.setSendToPhone("Y");
//					} else {
//						activityMessage.setSendToPhone("N");
//					}
//					activityMessage.setMessageTitle(tomSendMessage.getName());
//					activityMessage.setMessageDetails(appNewsXx);
//					activityMessage
//							.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/course/course_learning.html?courseId="
//									+ tomSendMessage.getCourseId() + "&activityId=" + tomSendMessage.getId() + "</a>");
//					activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/course_detail.html?courseId="
//							+ tomCourse.getCourseId());
//					activityMessage.setEmpIds(listXx);
//					activityMessage.setMessageType("3");
//					activityMessage.setCreateTime(new Date());
//					tomMessagesMapper.insertSelective(activityMessage);
//					for (String code : listXx) {
//						TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//						tomMessagesEmployees.setCreateTime(new Date());
//						tomMessagesEmployees.setEmpCode(code);
//						tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//						tomMessagesEmployees.setIsView("N");
//						tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//					}
//				}
//			}
//		}

		if ("Y".equals(beforeExamStatus)) {
			List<TomSendMessage> selectExamNow = tomEmpMapper.selectExamNow();
			// 线上考试开始时推送
			for (TomSendMessage tomSendMessage : selectExamNow) {
				TomExam exam = tomExamMapper.selectByPrimaryKey(tomSendMessage.getExamId());
				TomExamPaper exampaper = tomExamPaperMapper.selectByExamId(exam.getExamId());
				String cn =beforeExam.replace("<name>", exam.getExamName())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String en =beforeExamEn.replace("<name>", exam.getExamNameEn())
						.replace("<beginTime>", simple.format(exam.getBeginTime()))
						.replace("<endTime>", simple.format(exam.getEndTime()));
				String appNews = cn
						+ "\r\n"
						+ en;
				String selectTaskEnd1 = appNews + "\r\n" + "<a href=\"" + Config.getProperty("pcIndex")
						+ "views/exam/exam_index.html?examId=" + exam.getExamId() + "\">"
						+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId=" + exam.getExamId()
						+ "</a>";
				String selectTaskEnd2 = Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ exam.getExamId();
				List<String> listEmp = finishStatus(tomSendMessage);
				List<String> listEmail = new ArrayList<String>();
				for (String code : listEmp) {
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(code);
					if (emp.getSecretEmail() != null)
						listEmail.add(emp.getSecretEmail());
				}
				if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
					
					if(listEmail.size()>10){
						int part = listEmail.size()/10;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmail.subList(0, 10);
							SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
							listEmail.subList(0, 10).clear();
						}
						if(!listEmail.isEmpty()){
							SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
							sm.sendMessage();
						}
					}else {
						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
						sm.sendMessage();
					}
					
//					SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", selectTaskEnd1);
//					sm.sendMessage();
				}
				if (message.getSendToApp().equals("Y")) {
					List<WxMessage> wx = new ArrayList<WxMessage>();
					WxMessage setValue = new WxMessage(tomSendMessage.getName(), appNews, selectTaskEnd2,
							localUrl+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
					wx.add(setValue);
//					String wxNewsMessage = sendMessageService.wxNewsMessage(listEmp, wx);
//					List<String> listApp = sendMessageService.sendStatus(listEmp, wxNewsMessage);
//					if (listApp.size() != 0) {
//						sendMessageService.wxNewsMessage(listApp, wx);
//						// sendMessageService.wxMessage(listEmp,
//						// selectTaskEnd2);
//					}
					List<String> insertList=new ArrayList<String>();
					if(listEmp.size()>1000){
						int part = listEmp.size()/1000;//分批数
						for(int i=0;i<part;i++){
							List<String> partList=listEmp.subList(0, 1000);
							String wxNewsMessage = sendMessageService.wxNewsMessage(partList, wx);
							List<String> listApp = sendMessageService.sendStatus(partList, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(partList);
							}
							listEmp.subList(0, 1000).clear();
						}
						if(!listEmail.isEmpty()){
							String wxNewsMessage = sendMessageService.wxNewsMessage(listEmp, wx);
							List<String> listApp = sendMessageService.sendStatus(listEmp, wxNewsMessage);
							if (listApp.size() != 0) {
								sendMessageService.wxNewsMessage(listApp, wx);
								insertList.addAll(listApp);
							}else {
								insertList.addAll(listEmp);
							}
						}
						listEmp=insertList;
					}else {
						String wxNewsMessage = sendMessageService.wxNewsMessage(listEmp, wx);
						List<String> listApp = sendMessageService.sendStatus(listEmp, wxNewsMessage);
						if (listApp.size() != 0) {
							listEmp=listApp;
							sendMessageService.wxNewsMessage(listApp, wx);
						}
					}
				}
				TomMessages activityMessage = new TomMessages();
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
				activityMessage.setMessageTitle(tomSendMessage.getName());
				activityMessage.setMessageDetails(cn);
				activityMessage.setMessageTitleEn(tomSendMessage.getNameEn());
				activityMessage.setMessageDetailsEn(en);
				activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
						+ tomSendMessage.getExamId());
				activityMessage
						.setPcUrl("<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
								+ tomSendMessage.getExamId() + "\">" + Config.getProperty("pcIndex")
								+ "views/exam/exam_index.html?examId=" + tomSendMessage.getExamId() + "</a>");
				activityMessage.setEmpIds(listEmp);
				activityMessage.setMessageType("4");
				activityMessage.setCreateTime(new Date());
				tomMessagesMapper.insertSelective(activityMessage);
				for (String code : listEmp) {
					TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
					tomMessagesEmployees.setCreateTime(new Date());
					tomMessagesEmployees.setEmpCode(code);
					tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
					tomMessagesEmployees.setIsView("N");
					tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
				}
			}
		}

//		if ("Y".equals(cExamStatus)) {
//			List<TomSendMessage> selectCourseEnd = tomEmpMapper.selectCourseEnd();
//			// 线下课程后的考试
//			for (TomSendMessage tomSendMessage : selectCourseEnd) {
//				List<TomActivityProperty> actPro = tomActivityPropertyMapper.selectByActivityId(tomSendMessage.getId());
//				List<TomActivityProperty> property = new ArrayList<TomActivityProperty>();
//				Integer sort = tomSendMessage.getSort();
//				for (TomActivityProperty tomActivityProperty : actPro) {
//					if (null == tomActivityProperty.getCourseId()) {
//						String preTask = tomActivityProperty.getPreTask();
//						String[] split = preTask.split(",");
//						for (String str : split) {
//							if (str.equals(String.valueOf(sort))) {
//								property.add(tomActivityProperty);
//							}
//						}
//					}
//				}
//				for (TomActivityProperty tomActivityProperty : property) {
//					TomExam exam = tomExamMapper.selectByPrimaryKey(tomActivityProperty.getExamId());
//					TomExamPaper exampaper = tomExamPaperMapper.selectByExamId(exam.getExamId());
//					List<String> listEmp = finishStatusDown(tomActivityProperty);
//					String examApp = cExam.replace("<name>", exam.getExamName())
//							.replace("<beginTime>", simple.format(exam.getBeginTime()))
//							.replace("<endTime>", simple.format(exam.getEndTime()))
//							+ "\r\n"
//							+ cExamEn.replace("<name>", exam.getExamNameEn())
//									.replace("<beginTime>", simple.format(exam.getBeginTime()))
//									.replace("<endTime>", simple.format(exam.getEndTime()));
//					String examNews =  Config.getProperty("h5Index")
//							+ "views/task/exam_examDetail.html?examId=" + tomActivityProperty.getExamId();
//					String exameEmail = examApp + "<a href=\"" + Config.getProperty("pcIndex")
//							+ "views/exam/exam_index.html?examId=" + tomActivityProperty.getExamId() + "\">"
//							+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
//							+ tomSendMessage.getExamId() + "</a>";
//
//					if (null != tomSendMessage.getNeedApply() && "Y".equals(tomSendMessage.getNeedApply())) {
//						if (exampaper.getExamPaperType().equals("3") || exampaper.getExamPaperType().equals("4")) {
//							examApp = xxCQuestion.replace("<name>", tomSendMessage.getName())
//									.replace("<beginTime>", simple.format(exam.getBeginTime()))
//									.replace("<endTime>", simple.format(exam.getEndTime()))
//									+ "\r\n"
//									+ xxCQuestionEn.replace("<name>", tomSendMessage.getNameEn())
//											.replace("<beginTime>", simple.format(exam.getBeginTime()))
//											.replace("<endTime>", simple.format(exam.getEndTime()));
//							examNews = Config.getProperty("h5Index")
//									+ "views/task/exam_examDetail.html?examId=" + tomActivityProperty.getExamId();
//							exameEmail = examApp + "  <a href=\"" + Config.getProperty("pcIndex")
//									+ "views/exam/exam_index.html?examId=" + tomActivityProperty.getExamId() + "\">"
//									+ Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
//									+ tomSendMessage.getExamId() + "</a>";
//						}
//					}
//					List<String> listEmail = new ArrayList<String>();
//					for (String code : listEmp) {
//						TomEmp emp = tomEmpMapper.selectByPrimaryKey(code);
//						if (emp.getSecretEmail() != null)
//							listEmail.add(emp.getSecretEmail());
//					}
//					if (message.getSendToEmail().equals("Y")&&listEmail.size()>0) {
//						SendMail sm = SendMailUtil.getMail(listEmail, "【蔚乐学】任务通知", date, "蔚乐学", exameEmail);
//						sm.sendMessage();
//					}
//					if (message.getSendToApp().equals("Y")) {
//						List<WxMessage> wx = new ArrayList<WxMessage>();
//						WxMessage setValue = new WxMessage(exam.getExamName(), examApp, examNews,
//								localUrl+PathUtil.PROJECT_NAME+exampaper.getExamPaperPicture());
//						wx.add(setValue);
//						String wxNewsMessage = sendMessageService.wxNewsMessage(listEmp, wx);
//						List<String> listApp = sendMessageService.sendStatus(listEmp, wxNewsMessage);
//						if (listApp.size() != 0) {
//							sendMessageService.wxNewsMessage(listApp, wx);
//							// sendMessageService.wxMessage(listEmp,
//							// selectTaskEnd2);
//						}
//						//sendMessageService.wxMessage(listEmp, examNews);
//					}
//					TomMessages activityMessage = new TomMessages();
//					activityMessage.setViewCount(0);
//					if ("Y".equals(message.getSendToApp())) {
//						activityMessage.setSendToApp("Y");
//					} else {
//						activityMessage.setSendToApp("N");
//					}
//					if ("Y".equals(message.getSendToEmail())) {
//						activityMessage.setSendToEmail("Y");
//					} else {
//						activityMessage.setSendToEmail("N");
//					}
//					if ("Y".equals(message.getSendToPhone())) {
//						activityMessage.setSendToPhone("Y");
//					} else {
//						activityMessage.setSendToPhone("N");
//					}
//					activityMessage.setMessageTitle(tomActivityProperty.getExamName());
//					activityMessage.setMessageDetails(examApp);
//					activityMessage.setAppUrl(Config.getProperty("h5Index") + "views/task/exam_examDetail.html?examId="
//							+ tomActivityProperty.getExamId());
//					activityMessage.setPcUrl(
//							"<a href=\"" + Config.getProperty("pcIndex") + "views/exam/exam_index.html?examId="
//									+ tomActivityProperty.getExamId() + "\">" + Config.getProperty("pcIndex")
//									+ "views/exam/exam_index.html?examId=" + tomActivityProperty.getExamId() + "</a>");
//					activityMessage.setEmpIds(listEmp);
//					activityMessage.setMessageType("4");
//					activityMessage.setCreateTime(new Date());
//					tomMessagesMapper.insertSelective(activityMessage);
//					for (String code : listEmp) {
//						TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
//						tomMessagesEmployees.setCreateTime(new Date());
//						tomMessagesEmployees.setEmpCode(code);
//						tomMessagesEmployees.setMessageId(activityMessage.getMessageId());
//						tomMessagesEmployees.setIsView("N");
//						tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
//					}
//				}
//			}
//		}
		return "";
	}

	/**
	 * 
	 * 功能描述：[初始化数据]
	 *
	 * 创建者：cjx 创建时间: 2016年7月18日 下午3:16:51
	 */
	// public void initEhr(){
	// contextInitForTreeService.init();
	// }
	/**
	 * 
	 * 功能描述：[同步部门和员工通讯录]
	 *
	 * 创建者：cjx 创建时间: 2016年11月18日 下午3:16:51
	 */
	/*
	 * @GET
	 * 
	 * @Path("/empSync")
	 */
	public void synodeptAndemp() {
		try {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			deptservise.synchronizationDept();
			empservise.synchronizationEmp("1");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * 功能描述：[线上课程后通过前置任务的考试的人数
	 *
	 * 创建者：cjx 创建时间: 2016年12月12日 上午9:43:52
	 * 
	 * @param tomSendMessage
	 * @return
	 */
	public List<String> finishStatus(TomSendMessage tomSendMessage) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<String> listEmp = new ArrayList<String>();
		List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper.selectByActivityId2(tomSendMessage.getId());
		String preTask = tomSendMessage.getPreTask();
		String[] split = preTask.split(",");
		for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
			String code = tomActivityEmpsRelation.getCode();
			boolean a = false;
			int i = 0;
			for (String str : split) {
				TomActivityProperty example = new TomActivityProperty();
				example.setActivityId(tomSendMessage.getId());
				example.setSort(Integer.parseInt(str));
				TomActivityProperty selectTask = tomActivityPropertyMapper.selectTask(example);

				if (null != selectTask) {
					if (null != selectTask.getCourseId()) {
						TomCourses course = tomCoursesMapper.selectByPrimaryKey(selectTask.getCourseId());
						if ("Y".equals(course.getCourseOnline())) {
							a = true;
							Map<Object, Object> map = new HashMap<Object, Object>();
							map.put("courseId", selectTask.getCourseId());
							map.put("code", code);
							List<TomCourseLearningRecord> selectLearnRecord = tomCourseLearningRecordMapper
									.selectLearnRecord(map);
							if (null != selectLearnRecord) {
								i++;
							}
						} else {
							TomCourseSignInRecords courseSig = new TomCourseSignInRecords();
							courseSig.setCode(code);
							courseSig.setCourseId(selectTask.getCourseId());
							List<TomCourseSignInRecords> selectByExample = tomCourseSignInRecordsMapper
									.selectByExample(courseSig);
							if (null != selectByExample) {
								i++;
							}
						}
					} else {
						TomExamScore scoreExample = new TomExamScore();
						scoreExample.setCode(code);
						scoreExample.setExamId(selectTask.getExamId());
						List<TomExamScore> selectListByExample = tomExamScoreMapper.selectListByExample(scoreExample);
						if (null != selectListByExample) {
							i++;
						}
					}
				}
			}
			if (i == split.length && a == true) {
				listEmp.add(code);
			}
		}
		return listEmp;

	}

	/**
	 * 
	 * 功能描述：[线下课程后通过前置任务的考试的人数
	 *
	 * 创建者：cjx 创建时间: 2016年12月12日 上午9:43:52
	 * 
	 * @param tomSendMessage
	 * @return
	 */
	public List<String> finishStatusDown(TomActivityProperty tomActivityProperty) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<String> listEmp = new ArrayList<String>();
		List<TomActivityEmpsRelation> codes = tomActivityEmpsRelationMapper
				.selectByActivityId2(tomActivityProperty.getActivityId());
		String preTask = tomActivityProperty.getPreTask();
		String[] split = preTask.split(",");
		for (TomActivityEmpsRelation tomActivityEmpsRelation : codes) {
			if("Y".equals(tomActivityEmpsRelation.getApplyStatus())){
				String code = tomActivityEmpsRelation.getCode();
				boolean a = false;
				int i = 0;
				for (String str : split) {
					TomActivityProperty example = new TomActivityProperty();
					example.setActivityId(tomActivityProperty.getActivityId());
					example.setSort(Integer.parseInt(str));
					TomActivityProperty selectTask = tomActivityPropertyMapper.selectTask(example);
	
					if (null != selectTask) {
						if (null != selectTask.getCourseId()) {
							TomCourses course = tomCoursesMapper.selectByPrimaryKey(selectTask.getCourseId());
							if ("Y".equals(course.getCourseOnline())) {
	
								Map<Object, Object> map = new HashMap<Object, Object>();
								map.put("courseId", selectTask.getCourseId());
								map.put("code", code);
								List<TomCourseLearningRecord> selectLearnRecord = tomCourseLearningRecordMapper
										.selectLearnRecord(map);
								if (null != selectLearnRecord) {
									i++;
								}
							} else {
								a = true;
								TomCourseSignInRecords courseSig = new TomCourseSignInRecords();
								courseSig.setCode(code);
								courseSig.setCourseId(selectTask.getCourseId());
								List<TomCourseSignInRecords> selectByExample = tomCourseSignInRecordsMapper
										.selectByExample(courseSig);
								if (null != selectByExample) {
									i++;
								}
							}
						} else {
							TomExamScore scoreExample = new TomExamScore();
							scoreExample.setCode(code);
							scoreExample.setExamId(selectTask.getExamId());
							List<TomExamScore> selectListByExample = tomExamScoreMapper.selectListByExample(scoreExample);
							if (null != selectListByExample) {
								i++;
							}
						}
					}
				}
				if (i == split.length && a == true) {
					listEmp.add(code);
				}
			}
		}
		return listEmp;

	}
}
