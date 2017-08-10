package com.chinamobo.ue.system.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.commodity.dao.TomEbRecordMapper;
import com.chinamobo.ue.commodity.entity.TomEbRecord;
import com.chinamobo.ue.system.dao.TomUserInfoMapper;
import com.chinamobo.ue.system.entity.TomUserInfo;
import com.chinamobo.ue.ums.DBContextHolder;

@Service
public class UserInfoServise {
	@Autowired
	private TomUserInfoMapper tomUserInfoMapper;
	@Autowired
	private TomEbRecordMapper tomEbRecordMapper;

	@Transactional
	public void updateByCode(TomUserInfo tomUserInfo) {
		TomUserInfo userInfo = tomUserInfoMapper.selectByPrimaryKey(tomUserInfo.getCode());
		int eB = tomUserInfo.geteNumber() - userInfo.geteNumber();
		if (tomUserInfo.geteNumber() > userInfo.geteNumber()) {
			if (userInfo.getAddUpENumber() ==null) {
				userInfo.setAddUpENumber(0);
			}
			tomUserInfo.setAddUpENumber(userInfo.getAddUpENumber() + eB);
		}
		TomEbRecord tomEbRecord = new TomEbRecord();
		tomEbRecord.setCode(tomUserInfo.getCode());
		tomEbRecord.setExchangeNumber(eB);
		tomEbRecord.setRoad("12");
		tomEbRecord.setUpdateTime(new Date());
		tomEbRecordMapper.insertSelective(tomEbRecord);
		tomUserInfoMapper.updateByCode(tomUserInfo);
	}
	
	//添加人员
	public void insertUserInfo(TomUserInfo tomUserInfo){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		tomUserInfoMapper.insertSelective(tomUserInfo);
	}
	
	//编辑人员
	public void updateUserInfo(TomUserInfo tomUserInfo){
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		tomUserInfoMapper.updateByCode(tomUserInfo);
	}
	@Transactional
	public void updateByCodetoAPI(TomUserInfo tomUserInfo){
		tomUserInfoMapper.updateByCode(tomUserInfo);
	}
	
}
