package com.chinamobo.ue.ums.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ApiUserToken extends UsernamePasswordToken{
	
	private static final long serialVersionUID = -3362783521812330535L;
	private Long userId;
	private String userRoleType;
	
	public ApiUserToken() {
	}
	
	public ApiUserToken(String host, String auth, String userName, Long userId, String userRoleType) {
		super(userName, auth, true, host);
		this.userId = userId;
		this.userRoleType = userRoleType;
	}
	public ApiUserToken(String userName,String passWord){
		super(userName,passWord);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserRoleType() {
		return userRoleType;
	}

	public void setUserRoleType(String userRoleType) {
		this.userRoleType = userRoleType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
