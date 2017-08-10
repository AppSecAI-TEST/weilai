package com.chinamobo.ue.api.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest=(HttpServletRequest) arg0;
		HttpSession session=httpServletRequest.getSession();
		session.setMaxInactiveInterval(60*60);
		HttpServletResponse response = (HttpServletResponse)arg1;
	        response.setHeader("Cache-Control", "no-cache");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Connection", "keep-alive");
	        response.setDateHeader("expires", -1);
	        arg2.doFilter(arg0, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
