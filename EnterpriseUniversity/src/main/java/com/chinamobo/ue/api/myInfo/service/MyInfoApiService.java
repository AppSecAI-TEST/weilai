package com.chinamobo.ue.api.myInfo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.api.myInfo.dto.NewsDto;
import com.chinamobo.ue.api.myInfo.dto.RollingPictureDto;
import com.chinamobo.ue.api.myInfo.dto.UserInfo;
import com.chinamobo.ue.api.result.Result;
import com.chinamobo.ue.api.utils.ErrorCode;
import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.commodity.service.CommodityService;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.course.dao.TomCourseEmpRelationMapper;
import com.chinamobo.ue.course.dao.TomCoursesMapper;
import com.chinamobo.ue.course.entity.TomCourseEmpRelation;
import com.chinamobo.ue.course.entity.TomCourses;
import com.chinamobo.ue.exam.dao.TomExamEmpAllocationMapper;
import com.chinamobo.ue.exam.entity.TomExamEmpAllocation;
import com.chinamobo.ue.exam.service.ExamScoreService;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomLoginAuthMapper;
import com.chinamobo.ue.system.dao.TomNewsEmployeesMapper;
import com.chinamobo.ue.system.dao.TomNewsMapper;
import com.chinamobo.ue.system.dao.TomRollingPictureMapper;
import com.chinamobo.ue.system.dao.TomRollingPictureRecordMapper;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomLoginAuth;
import com.chinamobo.ue.system.entity.TomNews;
import com.chinamobo.ue.system.entity.TomNewsEmployees;
import com.chinamobo.ue.system.entity.TomRollingPicture;
import com.chinamobo.ue.system.entity.TomRollingPictureRecord;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.system.service.NewsServise;
import com.chinamobo.ue.system.service.RollingPictureServise;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.system.service.UserInfoServise;
import com.chinamobo.ue.system.service.UserLogServise;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.JsonMapper;
import com.chinamobo.ue.utils.QRCodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

@Service
public class MyInfoApiService {
	String basePath1 = Config.getProperty("file_path");
	@Autowired
	private TomUserInfoMapper userInfoMapper;

	@Autowired
	private TomEmpMapper empMapper;
	@Autowired
	private TomCoursesMapper coursesMapper;
	@Autowired
	private TomEbRecordMapper tomEbRecordMapper;
	@Autowired
	private TomNewsMapper tomNewsMapper;
	@Autowired
	private TomUserInfoMapper tomUserInfoMapper;	
	@Autowired
	private TomRollingPictureMapper rollingPictureMapper;
	@Autowired
	private TomCourseEmpRelationMapper courseEmpRelationMapper;
	@Autowired
	private TomRollingPictureRecordMapper rollingPictureRecordMapper;
	@Autowired
	private TomNewsEmployeesMapper tomNewsEmployeesMapper;
	@Autowired
	private ExamScoreService examScoreService;
	@Autowired
	private UserInfoServise userInfoServise;
	@Autowired
	private CommodityService commodityService;
	@Autowired
	private RollingPictureServise rollingPictureServise;
	@Autowired
	private TomLoginAuthMapper authMapper;
	@Autowired
	private UserLogServise userLogServise;
	@Autowired
	private NewsServise newsServise;
	@Autowired
	private TomExamEmpAllocationMapper examEmpAllocationMapper;
	
	@Autowired
	private SystemService systemService;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * 功能描述：[获取个人资料]
	 *
	 * 创建者：JCX 创建时间: 2016年3月19日 下午1:42:37
	 * 
	 * @param request
	 * @return
	 */
	public Result eleUserInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//扫描过期考试扣除e币
		DBContextHolder.setDbType(DBContextHolder.MASTER); 
		examScoreService.reSetStatus(request.getParameter("userId"));
		String lang = request.getParameter("lang");		
		UserInfo userInfo = new UserInfo();
		TomUserInfo tomUserInfo = userInfoMapper.selectByPrimaryKey(request.getParameter("userId"));
		TomEmp tomEmp = empMapper.selectByPrimaryKey(request.getParameter("userId"));
		userInfo.setUserId(tomUserInfo.getCode());
//		if("en".equals(lang)){
//			userInfo.setUserName(tomEmp.getNameEn());
//		}else{
			userInfo.setUserName(tomEmp.getName());
//		}
		userInfo.setUserImg(tomUserInfo.getHeadImg());
		userInfo.setTotalTime(String.valueOf(Integer.parseInt(tomUserInfo.getLearningTime())));
		userInfo.setSignInTotal(tomUserInfo.getSumSignInTimes());
		
		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // 今天

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // 昨天

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(tomUserInfo.getLastSignInTime());	
		if (current.get(Calendar.DAY_OF_MONTH) == 1) {
			userInfo.setSignInEbCount(1);
		} else {
			if (current.before(today) && current.after(yesterday)) {
				userInfo.setSignInEbCount(tomUserInfo.getContinuousSignInNumber() + 1);
			} else {
				userInfo.setSignInEbCount(1);
			}
		}
		
		userInfo.seteCoin(tomUserInfo.geteNumber());
		userInfo.setCourseCount(tomUserInfo.getCourseNumber());
		userInfo.setAddress(tomEmp.getCityname());
		userInfo.setUserTel(tomEmp.getMobile());
		userInfo.setAnonymity(tomUserInfo.getAnonymity());
		userInfo.setAddUpENumber(tomUserInfo.getAddUpENumber());
		userInfo.setConsigneeName(tomUserInfo.getName());
		userInfo.setConsigneeAddress(tomUserInfo.getAddress());
		userInfo.setConsigneeTel(tomUserInfo.getPhoneNumber());
		userInfo.setConsigneeGender(tomUserInfo.getSex());
		userInfo.setDeptName(tomEmp.getDeptname());
		int many = userInfoMapper.countByENumber(tomUserInfo.geteNumber());
		userInfo.seteCoinRanking(many+ 1);
		userInfo.setLearnTimeRanking(userInfoMapper.rankByLearnTime(Integer.parseInt(tomUserInfo.getLearningTime()))+1);
		Date date = tomUserInfo.getLastSignInTime();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (format.format(date).equals(format.format(new Date()))) {
			userInfo.setIsSignIn("Y");
		} else {
			userInfo.setIsSignIn("N");
		}
		String json = mapper.writeValueAsString(userInfo);
		return new Result("Y", json, ErrorCode.SUCCESS_CODE, "");
	}

	/**
	 * 
	 * 功能描述：[展示e币历史记录]
	 *
	 * 创建者：cjx 创建时间: 2016年5月30日 下午4:44:21
	 * 
	 * @param request
	 * @return
	 */
	public Result eleERecord(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userId");
		//扫描过期考试扣除e币
		DBContextHolder.setDbType(DBContextHolder.MASTER); 
		examScoreService.reSetStatus(userId);
				
		PageData<TomEbRecord> page = new PageData<TomEbRecord>();
		Map<Object, Object> map = new HashMap<Object, Object>();
			if (request.getParameter("firstIndex") == null) {
				map.put("startNum", 0);
			} else {
				map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
			}
			if (request.getParameter("pageSize") == null) {
				map.put("endNum", 10);
				page.setPageSize(10);
			} else {
				map.put("endNum",  Integer.parseInt(request.getParameter("pageSize")));//(int) map.get("startNum") +
				page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
			}
		map.put("userId", userId);
		int count = tomEbRecordMapper.countByUserId(map);
		List<TomEbRecord> ebRecord = tomEbRecordMapper.selectByUserId(map);
		page.setDate(ebRecord);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");
	}
	
	/**
	 * 
	 * 功能描述：[8.2.E币签到]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年5月31日 下午2:43:56
	 * @param request
	 * @return
	 */
	
	public Result eleRSigin(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.MASTER); 
		String userId = request.getParameter("userId");
		TomUserInfo tomUserInfo = userInfoMapper.selectByPrimaryKey(userId);
		TomEbRecord ebRecord = new TomEbRecord();
		Date date =new Date();
		SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // 今天

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		Calendar yesterday = Calendar.getInstance(); // 昨天

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(tomUserInfo.getLastSignInTime());	
		String eB = "";
		
		String format = simple.format(date);
		String format2 = simple.format(tomUserInfo.getLastSignInTime());
		if(!simple.format(date).equals(simple.format(tomUserInfo.getLastSignInTime()))){
			tomUserInfo.setSumSignInTimes(tomUserInfo.getSumSignInTimes() + 1);
			tomUserInfo.setLastSignInTime(date);
			if (current.before(today) && current.after(yesterday)) {
				if(tomUserInfo.getContinuousSignInNumber()<4){
					eB="2";
					tomUserInfo.seteNumber(tomUserInfo.geteNumber()+2);
					tomUserInfo.setAddUpENumber(tomUserInfo.getAddUpENumber() + 2);
					ebRecord.setExchangeNumber(2);
				}else {
					eB="5";
					tomUserInfo.seteNumber(tomUserInfo.geteNumber()+5);
					tomUserInfo.setAddUpENumber(tomUserInfo.getAddUpENumber() + 5);
					ebRecord.setExchangeNumber(5);
				}
				tomUserInfo.setContinuousSignInNumber(tomUserInfo.getContinuousSignInNumber()+1);
			} else {
				eB="2";
				tomUserInfo.setContinuousSignInNumber(1);
				tomUserInfo.seteNumber(tomUserInfo.geteNumber() + 2);
				tomUserInfo.setAddUpENumber(tomUserInfo.getAddUpENumber() + 2);
				ebRecord.setExchangeNumber(2);
			}
	
			userInfoServise.updateByCodetoAPI(tomUserInfo);
			ebRecord.setCode(userId);
			ebRecord.setRoad("1");
			ebRecord.setUpdateTime(new Date());
			commodityService.insertSelective(ebRecord);
			return new Result("Y", eB, "0", "");
		}else{
			return new Result("N", eB, "0", "请勿重复签到");
		}
	}
	/**
	 * 
	 * 功能描述：[资讯列表]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月1日 上午10:24:38
	 * @param request
	 * @return
	 */
	public Result eleMyNews(HttpServletRequest request,HttpServletResponse response) throws Exception {
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
		String userId = request.getParameter("userId");
		String lang = request.getParameter("lang");
		TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(userId);
		PageData<NewsDto> page = new PageData<NewsDto>();
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("orgCode", selectByPrimaryKey.getPkOrg());
		map.put("code", userId);
		if (request.getParameter("firstIndex") == null) {
			map.put("startNum", 0);
		} else {
			map.put("startNum", Integer.parseInt(request.getParameter("firstIndex")));
		}
		if (request.getParameter("pageSize") == null) {
			map.put("endNum", 10);
			page.setPageSize(10);
		} else {
			map.put("endNum",  Integer.parseInt(request.getParameter("pageSize")));//(int) map.get("startNum") +
			page.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		int count = tomNewsMapper.countByExample(map);                                                                      
		List<NewsDto> list = tomNewsMapper.selectByMany(map);
		for(NewsDto newsDto:list){
			if(null==newsDto.getIsView()||"".equals(newsDto.getIsView())){
				newsDto.setIsView("N");
			}
			if("en".equals(lang)){
				newsDto.setNewsTitle(newsDto.getNewsTitleEn());
				newsDto.setNewsDetails(newsDto.getNewsDetailsEn());
				newsDto.setNewsPicture(newsDto.getNewsPictureEn());
			}
		}
		
		page.setDate(list);
		page.setCount(count);
		JsonMapper jsonMapper = new JsonMapper();
		String pageJson = jsonMapper.toJson(page);
		return new Result("Y", pageJson, "0", "");

	}
	/**
	 * 
	 * 功能描述：[资讯详情]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月1日 下午1:55:40
	 * @param request
	 * @return
	 */
	public Result eleMyNewsDetails(HttpServletRequest request,HttpServletResponse response)  throws Exception{
		Date date = new Date();
		DBContextHolder.setDbType(DBContextHolder.MASTER); 
			String userId = request.getParameter("userId");
			String newsId = request.getParameter("newsId");
			String lang = request.getParameter("lang");
			TomEmp selectByPrimaryKey = empMapper.selectByPrimaryKey(userId);
			Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("orgCode", selectByPrimaryKey.getPkOrg());
				map.put("newsId", newsId);
				map.put("userId", userId);
				TomNewsEmployees selectByMap = tomNewsEmployeesMapper.selectByMap(map);
				if(null==selectByMap){
					TomNewsEmployees tomnews = new TomNewsEmployees();
					tomnews.setCode(userId);
					tomnews.setNewsId(Integer.parseInt(newsId));
					tomnews.setCreateTime(date);
					tomnews.setIsView("Y");
					newsServise.insertSelective(tomnews);
					TomNews selectByPrimaryKey2 = tomNewsMapper.selectByPrimaryKey(Integer.parseInt(newsId));
					TomNews news = new TomNews();
					news.setNewsId(Integer.parseInt(newsId));
					news.setViewCount(selectByPrimaryKey2.getViewCount()+1);
					newsServise.updateByPrimaryKeySelective1(news);
				}
				TomNews tomNews = tomNewsMapper.selectByOne(map);
				if("en".equals(lang)){
					tomNews.setNewsTitle(tomNews.getNewsTitleEn());
					tomNews.setNewsDetails(tomNews.getNewsDetailsEn());
					tomNews.setNewsPicture(tomNews.getNewsPictureEn());
				}
				JsonMapper jsonMapper = new JsonMapper();
				String pageJson = jsonMapper.toJson(tomNews);
			return new Result("Y", pageJson, "0", "");

	}
	
	/**
	 * 
	 * 功能描述：[更改收货地址]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月12日 下午8:41:14
	 * @param json
	 * @return
	 */
	
	public Result eleUpdateMyAddress(String json) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		JSONObject jsonObject=JSONObject.fromObject(json);
		TomUserInfo tomUserInfo = new TomUserInfo();
		tomUserInfo.setAddress(jsonObject.getString("consigneeAddress"));
		tomUserInfo.setName(jsonObject.getString("consigneeName"));
		tomUserInfo.setSex(jsonObject.getString("consigneeGender"));
		if(json.indexOf("anonymity")!=-1){
			tomUserInfo.setAnonymity(jsonObject.getString("anonymity"));
		}
		tomUserInfo.setPhoneNumber(jsonObject.getString("consigneeTel"));
		if(json.indexOf("headImg")!=-1){
			  BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
			  String[] split = jsonObject.getString("headImg").split("base64,");
			  if(!split[0].equals("")){
			        byte[] bytes1 = decoder.decodeBuffer(split[1]);
//			        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);    
//			        BufferedImage bi1 =ImageIO.read(bais); 
			        for (int i = 0; i < bytes1.length; ++i) {  
			        	if (bytes1[i] < 0) {// 调整异常数据  
			        	bytes1[i] += 256;  
			        	}  
			        	}  

			  DateFormat format2 = new SimpleDateFormat("HHmmssSSS");
			  DateFormat format1 = new SimpleDateFormat("yyyyMMdd");

				String baseFolder1 = basePath1 + "file/" + "headImage/" + format1.format(new Date());
		        File dir=new File(baseFolder1);
		        if(!dir.exists()){
		         dir.mkdirs();
		        }
			
		        String fileName=basePath1 + "file/" + "headImage" +"/"+ format1.format(new Date())+"/"+format2.format(new Date()) + RandomUtils.nextInt(100, 999) + ".png";
		       // File w2 = new File(fileName);//可以是jpg,png,gif格式    
		      //  ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动  
		    	OutputStream out = new FileOutputStream(fileName);  
				out.write(bytes1);  
				out.flush();  
				out.close();  
		       String headImg =  StringUtils.replace(StringUtils.substringAfter(fileName, basePath1),"\\", "/");
			tomUserInfo.setHeadImg(headImg);
			  }
		}
		
		tomUserInfo.setCode(jsonObject.getString("userId"));
		userInfoServise.updateByCodetoAPI(tomUserInfo);
		return new Result("Y", "", "0", "");
		
	}
	/**
	 * 
	 * 功能描述：[修改可见]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年6月12日 下午8:42:10
	 * @param request
	 * @param response
	 * @return
	 */
	
	public Result eleUpdateMyAnonymity(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		TomUserInfo tomUserInfo = new TomUserInfo();
		tomUserInfo.setCode(request.getParameter("userId"));
		tomUserInfo.setAnonymity(request.getParameter("anonymity"));		
		userInfoServise.updateByCodetoAPI(tomUserInfo);
		return new Result("Y", "", "0", "");
		
	}
	
	/**
	 * 
	 * 功能描述：[轮播图]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月12日 下午8:28:39
	 * @param request
	 * @param response
	 * @return
	 */
	public Result eleRollingPicture(HttpServletRequest request,HttpServletResponse response) throws Exception{
			DBContextHolder.setDbType(DBContextHolder.SLAVE); 
			String lang = request.getParameter("lang");
			List<TomRollingPicture> rollingPictures = new ArrayList<TomRollingPicture>();
			//查询所有选中的轮播图片；
			List<TomRollingPicture> allCheckedRollingPicture = rollingPictureMapper.selectByRollingPictureByAllChecked();
			List<TomRollingPicture> rollingPicturesEmpList =rollingPictureMapper.selectByRollingPictureByEmpId(request.getParameter("userId"));
			if(allCheckedRollingPicture != null)
				rollingPictures.addAll(allCheckedRollingPicture);
			if(rollingPicturesEmpList != null)
				rollingPictures.addAll(rollingPicturesEmpList);
			List<RollingPictureDto> list=new ArrayList<RollingPictureDto>();
			TomCourseEmpRelation example=new TomCourseEmpRelation();
			example.setCode(request.getParameter("userId"));
			for(TomRollingPicture rollingPicture:rollingPictures){
				RollingPictureDto rollingPictureDto=new RollingPictureDto();
				rollingPictureDto.setId(rollingPicture.getResourceId());
				rollingPictureDto.setPictureId(rollingPicture.getPictureId());
				rollingPictureDto.setIsTop(rollingPicture.getIsTop());
				rollingPictureDto.setPictureCategory(rollingPicture.getPictureCategory());
				if ("en".equals(lang)) {
					rollingPictureDto.setPictureUrl(rollingPicture.getPictureUrlEn());
				}else{
					rollingPictureDto.setPictureUrl(rollingPicture.getPictureUrl());
				}
				
				rollingPictureDto.setResourceName(rollingPicture.getResourceName());
				list.add(rollingPictureDto);
			}
			String json=mapper.writeValueAsString(list);
			return new Result("Y", json, "0", "");
		
	}
	
	/**
	 * 
	 * 功能描述：[轮播图点击记录]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年6月13日 上午10:01:06
	 * @param request
	 * @param response
	 * @return
	 */
	
	public Result eleRollingPictureRecord(HttpServletRequest request,HttpServletResponse response) throws Exception{
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		String lang = request.getParameter("lang");
		TomEmp emp=empMapper.selectByCodeOnetoOne(request.getParameter("userId"));
		TomRollingPictureRecord rollingPictureRecord=new TomRollingPictureRecord();
		rollingPictureRecord.setEmpCode(request.getParameter("userId"));
		rollingPictureRecord.setPictureId(Integer.parseInt(request.getParameter("pictureId")));
		
		if(rollingPictureRecordMapper.countByExample(rollingPictureRecord)==0){
			/*if("en".equals(lang)){
				rollingPictureRecord.setEmpName(emp.getNameEn());
			}else{*/
				rollingPictureRecord.setEmpName(emp.getName());
			/*}*/
			rollingPictureRecord.setCreateTime(new Date());
			rollingPictureServise.insertSelective(rollingPictureRecord);
			
			TomRollingPicture rollingPicture=rollingPictureMapper.selectByPrimaryKey(Integer.parseInt(request.getParameter("pictureId")));
			rollingPicture.setViewCount(rollingPicture.getViewCount()+1);
			rollingPictureMapper.updateByPrimaryKeySelective(rollingPicture);
		}

		return new Result("Y", "", "0", "");
		
	}
	/**
	 * 
	 * 功能描述：[头像上传]
	 *
	 * 创建者：cjx
	 * 创建时间: 2016年7月8日 下午2:44:50
	 * @param json
	 * @return
	 */
	 public Result eleUploadUserimg(String json){
		 try{
		  DBContextHolder.setDbType(DBContextHolder.MASTER);
		  JSONObject jsonObject=JSONObject.fromObject(json);
		  BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
		  String split = jsonObject.getString("userImage");
		  String userId = jsonObject.getString("userId");
		  
		        byte[] bytes1 = decoder.decodeBuffer(split);
		    //    System.out.println(jsonObject.getString("data"));
//		        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);    
//		        BufferedImage bi1 =ImageIO.read(bais); 
		        for (int i = 0; i < bytes1.length; ++i) {  
		        	if (bytes1[i] < 0) {// 调整异常数据  
		        	bytes1[i] += 256;  
		        	}  
		        	}  

		  DateFormat format2 = new SimpleDateFormat("HHmmssSSS");
		  DateFormat format1 = new SimpleDateFormat("yyyyMMdd");

			String baseFolder1 = basePath1 + "file/" + "headImage/" + format1.format(new Date());
	        File dir=new File(baseFolder1);
	        if(!dir.exists()){
	         dir.mkdirs();
	        }
	        String fileName=basePath1 + "file/" + "headImage" +"/"+ format1.format(new Date())+"/"+format2.format(new Date()) + RandomUtils.nextInt(100, 999) + ".png";
//	        File w2 = new File(fileName);//可以是jpg,png,gif格式    
//	        ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动   
	        OutputStream out = new FileOutputStream(fileName);  
			out.write(bytes1);  
			out.flush();  
			out.close();  
		  //  String headImg ="file/" + "headImage" +"/"+ format1.format(new Date())+"/"+format2.format(new Date()) + RandomUtils.nextInt(100, 999) + ".jpg";
	        String headImg =  StringUtils.replace(StringUtils.substringAfter(fileName, basePath1),"\\", "/");
	    	TomUserInfo tomUserInfo = new TomUserInfo();
	    	tomUserInfo.setCode(userId);
	    	tomUserInfo.setHeadImg(headImg);
	    	userInfoServise.updateByCodetoAPI(tomUserInfo);
	    	Map<String,String> map = new HashMap<String,String>();
	    	map.put("headImage", headImg);
	    	JsonMapper jsonMapper = new JsonMapper();
	    	String json2 = jsonMapper.toJson(map);
	        return new Result("Y", json2, "0", "");
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return new Result("N", "", String.valueOf(ErrorCode.SYSTEM_ERROR), "系统繁忙！");
		 }
	 	
	 	/**
	 	 * 
	 	 * 功能描述：[扫码登录二维码获取]
	 	 *
	 	 * 创建者：wjx
	 	 * 创建时间: 2016年12月7日 下午12:02:34
	 	 * @param request
	 	 * @param response
	 	 * @return
	 	 * @throws Exception
	 	 */
	 	
	 	public Result qrLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
	 		DBContextHolder.setDbType(DBContextHolder.MASTER);
	 		String CorpID=Config.getProperty("CorpID");
			String filePath1=Config.getProperty("file_path");
			String userId=request.getParameter("userId");
			TomLoginAuth loginAuth=new TomLoginAuth();
			loginAuth.setUserId(userId);
			loginAuth.setCode(UUID.randomUUID().toString());
			loginAuth.setStatus("N");
			loginAuth.setQrPath("file/tdc/login/"+loginAuth.getCode()+".jpg");
			userLogServise.insert(loginAuth);
			StringBuffer sb=new StringBuffer();
			String redirectUri=Config.getProperty("redirect_uri")+loginAuth.getCode();
			sb.append("https://open.weixin.qq.com/connect/oauth2/authorize?appid=")
			  .append(CorpID)
			  .append("&redirect_uri=")
			  .append(URLEncoder.encode(redirectUri))
			  .append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			QRCodeUtil.courseEncode(sb.toString(),loginAuth.getCode(), filePath1 +"file"+ File.separator + "tdc"+File.separator +"login");
			Map<String,String> map = new HashMap<String,String>();
			map.put("qrPath", loginAuth.getQrPath());
			map.put("authCode", loginAuth.getCode());
			JsonMapper jsonMapper = new JsonMapper();
			return new Result("Y",jsonMapper.toJson(map),"0","");
		}
	 	
	 	public Result qrResult(HttpServletRequest request,HttpServletResponse response){
	 		DBContextHolder.setDbType(DBContextHolder.MASTER);
	 		String code=request.getParameter("authCode");
	 		Map<Object,Object> map = new HashMap<Object,Object>();
	 		map.put("code", code);
	 		TomLoginAuth loginAuth=authMapper.selectByMap(map);
	 		JsonMapper jsonMapper = new JsonMapper();
			return new Result("Y",jsonMapper.toJson(loginAuth),"0","");
	 	}
	 	
	 	public Result loginRecord(HttpServletRequest request,HttpServletResponse response){
	 		DBContextHolder.setDbType(DBContextHolder.MASTER);
	 		String code=request.getParameter("userId");
	 		systemService.updateLoginRecord(code, "h5");
	 		return new Result("Y","","0","");
	 	}
}
