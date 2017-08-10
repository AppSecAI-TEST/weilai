package com.chinamobo.ue.ums.shiro;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinamobo.ue.ums.util.Encodes;
import com.chinamobo.ue.ums.util.Servlets;
import com.chinamobo.ue.ums.util.ShiroUtils;


public class ApiUserAuthenticationFilter extends AuthenticatingFilter{

	public static final String DEFAULT_ENCODE_ISO = "ISO-8859-1";
	public static final String DEFAULT_ENCODE_GBK = "gbk";
	public static final String DEFAULT_ENCODE_UTF8 = "utf-8";
	
	public static final String DEFAULT_AUTH_BASIC = "Basic ";
	public static final String DEFAULT_AUTH_HEADER = "Authorization";
	public static final String DEFAULT_AUTH_PARAM = "token";
	public static final String DEFAULT_USER_NAME_PARAM = "userName";
	public static final String DEFAULT_USER_ID_PARAM = "userId";
	public static final String DEFAULT_USER_ROLE_TYPE_PARAM = "userRoleType";
	public static final String DEFAULT_ORGCODE_PARAM = "orgCode";
	public static final String DEFAULT_USER_ROLE_TYPE= "1";
	public static final String DEFAULT_AUTH_USER_SEP = "|";
	public static final String DEFAULT_USER_ID_NAME_SEP = ":";
	
	public static final String INNER_USER_TYPE = "1";
	public static final String API_USER_TYPE = "2";
	
	private String getAuth(ServletRequest request) {
		String auth = WebUtils.getCleanParam(request, DEFAULT_AUTH_PARAM);
		if(StringUtils.isBlank(auth)){
			auth = WebUtils.toHttp(request).getHeader(DEFAULT_AUTH_HEADER);
			if(StringUtils.isNotBlank(auth)){
				if(StringUtils.startsWith(auth, DEFAULT_AUTH_BASIC)){
					auth = StringUtils.trimToEmpty(StringUtils.substringAfter(auth, DEFAULT_AUTH_BASIC));
				}
			}
		}
		return auth;
	}
	
	private String getUserName(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_USER_NAME_PARAM);
	}
	
	private Long getUserId(ServletRequest request) {
		return NumberUtils.toLong(WebUtils.getCleanParam(request, DEFAULT_USER_ID_PARAM), -1);
	}
	
	private String getUserRoleType(ServletRequest request) {
		return WebUtils.getCleanParam(request, DEFAULT_USER_ROLE_TYPE_PARAM);
	}
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse arg1) throws Exception {
		String auth = getAuth(request);
		if(StringUtils.isNotBlank(auth)){
			String userName = getUserName(request);
			if(StringUtils.isNotBlank(userName)){
				userName = new String(userName.getBytes(DEFAULT_ENCODE_ISO), DEFAULT_ENCODE_GBK);
			}
			
			Long userId = getUserId(request);
			
			if(StringUtils.contains(auth, DEFAULT_AUTH_USER_SEP)){
				String authWithUserinfo = auth;
				auth = StringUtils.substringAfter(authWithUserinfo, DEFAULT_AUTH_USER_SEP);
				if(StringUtils.isBlank(userName)){
					String userNameIdBase64Str = StringUtils.substringBefore(authWithUserinfo, DEFAULT_AUTH_USER_SEP);
					String userNameIdStr = Encodes.decodeBase64AsString(userNameIdBase64Str, DEFAULT_ENCODE_GBK);
					if(StringUtils.contains(userNameIdStr, DEFAULT_USER_ID_NAME_SEP)){
						userName = StringUtils.substringAfter(userNameIdStr, DEFAULT_USER_ID_NAME_SEP);
						if(userId == -1){
							userId = NumberUtils.toLong(StringUtils.substringBefore(userNameIdStr, DEFAULT_USER_ID_NAME_SEP));
						}
					}else{
						userName = userNameIdStr;
					}
				}
			}
			String host = Servlets.getHost(request);
			
			String userRoleType = getUserRoleType(request);
			if(StringUtils.isBlank(userRoleType)){
				userRoleType = DEFAULT_USER_ROLE_TYPE;
			}
			
			return new ApiUserToken(host, auth, userName, userId, userRoleType);
		}
		return new ApiUserToken();
	}

	@Override
	protected boolean onAccessDenied(ServletRequest arg0, ServletResponse arg1) throws Exception {
		boolean fa = executeLogin(arg0, arg1);
		if (!fa) {
			HttpServletRequest res=(HttpServletRequest) arg0;
			HttpServletResponse httpResponse = (HttpServletResponse) arg1;
			httpResponse.setStatus(200);
			httpResponse.setCharacterEncoding("UTF-8");
			httpResponse.setContentType("application/json; charset=utf-8");
			PrintWriter out = null;

			try {
				out = httpResponse.getWriter();
				out.append(res.getParameter("callback")+"({\"error_code\":\"401\"})");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}

		}
		return fa;
	}
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		ShiroUser shiroUser = ShiroUtils.getCurrentUser();
		super.issueSuccessRedirect(request, response);
	}
	
}
