package com.chinamobo.ue.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomMessagesEmployeesMapper;
import com.chinamobo.ue.system.dao.TomMessagesMapper;
import com.chinamobo.ue.system.dto.MessageDto;
import com.chinamobo.ue.system.entity.TomEmp;
import com.chinamobo.ue.system.entity.TomMessages;
import com.chinamobo.ue.system.entity.TomMessagesEmployees;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.SendMail;
import com.chinamobo.ue.ums.util.SendMailUtil;
import com.chinamobo.ue.ums.util.ShiroUtils;

import net.sf.json.JSONObject;


@Service
public class MessageServise {
	@Autowired
	private TomMessagesMapper tomMessagesMapper;
	@Autowired
	private TomMessagesEmployeesMapper tomMessagesEmployeesMapper;
	@Autowired
	private TomEmpMapper tomEmpMapper;
	@Autowired
	private SendMessageService sendMessageService;
	ShiroUser user = ShiroUtils.getCurrentUser();
	@Transactional
	public void sendMessage(TomMessages tomMessages) throws Exception {
		Date date = new Date();
		tomMessages.setViewCount(0);
		tomMessages.setCreateTime(date);
		tomMessages.setMessageType("1");
		String mobile = "";
		String email = "";
		if("Y".equals(tomMessages.getSendToPhone())){
			if (null != tomMessages.getEmpIds()) {
				for (String code : tomMessages.getEmpIds()) {
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(code);					
					sendMessageService.sendMessage(tomMessages.getMessageDetails(), emp.getMobile());
					}
				}
		}
		if("Y".equals(tomMessages.getSendToEmail())){
			if (null != tomMessages.getEmpIds()) {
				List<String> list = new ArrayList<String>();
				for (String code : tomMessages.getEmpIds()) {
					TomEmp emp = tomEmpMapper.selectByPrimaryKey(code);
					if(emp.getSecretEmail()!=null)
					list.add(emp.getSecretEmail());					
				}
				if(list.size()>10){
					int part = list.size()/10;//分批数
					for(int i=0;i<part;i++){
						List<String> partList=list.subList(0, 10);
						SendMail sm = SendMailUtil.getMail(partList, "【蔚乐学】任务通知", date, "蔚乐学", tomMessages.getMessageDetails());
						sm.sendMessage();
						list.subList(0, 10).clear();
					}
					if(!list.isEmpty()){
						SendMail sm = SendMailUtil.getMail(list, "【蔚乐学】任务通知", date, "蔚乐学", tomMessages.getMessageDetails());
						sm.sendMessage();
					}
				}else {
					SendMail sm = SendMailUtil.getMail(list, "【蔚乐学】任务通知", date, "蔚乐学", tomMessages.getMessageDetails());
					sm.sendMessage();
				}
				
//				SendMail sm= SendMailUtil.getMail(list,tomMessages.getMessageTitle(),date,"蔚乐学",tomMessages.getMessageDetails() );
//				sm.sendMessage();
				}
		}
		List<String> listApp = new ArrayList<String>();
		listApp.addAll(tomMessages.getEmpIds());
		if("Y".equals(tomMessages.getSendToApp())){
			if (null != tomMessages.getEmpIds()) {		
				 //String[] array = (String[]) tomMessages.getEmpIds().toArray(new String[tomMessages.getEmpIds().size()]);				//集合转成数组
				 String wxMessage = sendMessageService.wxMessage(tomMessages.getEmpIds(), tomMessages.getMessageDetails());
				 JSONObject jsonO = JSONObject.fromObject(wxMessage);
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
						sendMessageService.wxMessage(listApp, tomMessages.getMessageDetails());
					}else{
						throw new Exception("消息发送失败");
					}
				}
		}
		tomMessagesMapper.insertSelective(tomMessages);
		if (null != tomMessages.getEmpIds()) {
			for (String code : tomMessages.getEmpIds()) {			
				TomMessagesEmployees tomMessagesEmployees = new TomMessagesEmployees();
				tomMessagesEmployees.setCreateTime(date);
				tomMessagesEmployees.setEmpCode(code);
				tomMessagesEmployees.setMessageId(tomMessages.getMessageId());
				tomMessagesEmployees.setIsView("N");
				tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
			}
		}
	}
	
	public PageData<TomMessages> findPage(int pageNum, int pageSize, String name) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
		PageData<TomMessages> page = new PageData<TomMessages>();
		// if
		Map<Object, Object> map1 = new HashMap<Object, Object>();

		if (name != null) {
			name = name.replaceAll("/", "//");
			name = name.replaceAll("%", "/%");
			name = name.replaceAll("_", "/_");
		}

		map1.put("name", name);
		int count = tomMessagesMapper.countByExample(map1);

		if (pageSize == -1) {
			pageSize = count;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("startNum", (pageNum - 1) * pageSize);
		map.put("endNum", pageSize);//pageNum * 
		map.put("name", name);

		List<TomMessages> list = tomMessagesMapper.selectByMany(map);
		// 把角色名称查询出来放到roles里
		for (TomMessages tomMessages : list) {
			List<TomMessagesEmployees> selectByAdlinRoles = tomMessagesEmployeesMapper.selectById(tomMessages.getMessageId());
			TomEmp selectByPrimaryKey = tomEmpMapper.selectByPrimaryKey(selectByAdlinRoles.get(0).getEmpCode());
			tomMessages.setDept(selectByPrimaryKey.getDeptname());
			tomMessages.setOrg(selectByPrimaryKey.getOrgname());
		}
		page.setDate(list);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		return page;

	}
	public MessageDto findOne(int messageId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE); 
		TomMessages message = tomMessagesMapper.selectByPrimaryKey(messageId);
		MessageDto messageDto = new MessageDto();
		if(messageId!=0){
		List<Map<String,String>> emp=new ArrayList<Map<String,String>>();
		
		List<TomEmp> emps = tomEmpMapper.selectByMessageId(messageId);
		for(TomEmp tomEmp:emps){
			Map<String,String> map = new HashMap<String,String>();
			map.put("code", tomEmp.getCode());
			map.put("name", tomEmp.getName());
			emp.add(map);
			messageDto.setEmps(emp);
		}	
		}
		messageDto.setMessageDetails(message.getMessageDetails());
		messageDto.setMessageTitle(message.getMessageTitle());		
		messageDto.setSendToApp(message.getSendToApp());
		messageDto.setSendToEmail(message.getSendToEmail());
		messageDto.setSendToPhone(message.getSendToPhone());
		
		return messageDto;
	}
	@Transactional
	public void updateSysMessage(TomMessages tomMessages) {
		tomMessages.setMessageType("0");
		tomMessagesMapper.updateByPrimaryKeySelective(tomMessages);
	}
	@Transactional
	public void  insertSelective(TomMessages activityMessage){
		tomMessagesMapper.insertSelective(activityMessage);
	}
	@Transactional
	public void  insertSelective(TomMessagesEmployees tomMessagesEmployees){
		tomMessagesEmployeesMapper.insertSelective(tomMessagesEmployees);
	}
	@Transactional
	public void updateByPrimaryKeySelective(TomMessages tomMessages){
		tomMessagesMapper.updateByPrimaryKeySelective(tomMessages);
	}
	@Transactional
	public void updateStatus(Map<Object, Object> map){
		tomMessagesEmployeesMapper.updateStatus(map);
	}
	
}
