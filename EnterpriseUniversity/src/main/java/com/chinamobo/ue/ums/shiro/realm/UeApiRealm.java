package com.chinamobo.ue.ums.shiro.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.system.entity.TomUserLog;
import com.chinamobo.ue.system.service.SystemService;
import com.chinamobo.ue.ums.shiro.ApiUserToken;
import com.chinamobo.ue.ums.shiro.ShiroPermission;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.shiro.realm.ApiUserRealm.ApiUser;

public class UeApiRealm extends AuthorizingRealm {


	@Autowired
	private SystemService systemService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object primaryPrincipal = principals.getPrimaryPrincipal();
		if (primaryPrincipal instanceof ApiUser) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			ShiroUser shiroUser = (ShiroUser) primaryPrincipal;
			for (ShiroPermission perm : shiroUser.getShiroPermissions()) {
				info.addRole(perm.getAuthorityurl());
			}
			return info;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		if(arg0 instanceof ApiUserToken){//只验证支持的token
			ApiUserToken apiUserToken = (ApiUserToken) arg0;
			if(apiUserToken.getPassword() == null){
				System.out.println("token为空");
				return null;
			}
			String auth = new String(apiUserToken.getPassword());
//			String account = apiUserToken.getUsername();
			String accountPassword = new String(apiUserToken.getPassword());
			if(StringUtils.isNotBlank(auth)){
				TomUserLog tomUserLog=systemService.getUserByToken(auth);
				if(tomUserLog == null){
					throw new UnknownAccountException();
				}else {
					return new SimpleAuthenticationInfo(new ApiUser(0, tomUserLog.getCode(), tomUserLog.getCode(), tomUserLog.getName(), systemService.apiRole(),
							systemService.apiPermission(),null,accountPassword,null),auth,getName());
				}
				
			}else{
				System.out.println("token为空");
				throw new UnknownAccountException(auth);
			}
		}
		return null;
	}
	
}
