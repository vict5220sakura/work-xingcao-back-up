package com.bithaw.ethSignServer.intercepors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bithaw.ethSignServer.common.Common;

@Component 
public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("登陆拦截器执行了");
		Cookie[] cookies = request.getCookies();
		if(cookies == null){
			System.out.println("登录失败!");//跳转到登录页
			if(Common.FIRST_LOGIN_FLAG){
				request.getRequestDispatcher("/firstlogin").forward(request,response);//如果是第一次登录
				return true;
			}
			request.getRequestDispatcher("/login").forward(request,response);//转发到新的页面
			return true;
		}
		for(Cookie cookie : cookies){
			if(cookie == null){
				continue;
			}
			String cookieName = cookie.getName();
			if("c5SignServerToken".equals(cookieName)){
				String token = cookie.getValue();
				boolean checkLogin = Common.checkLogin(token);
				System.out.println(Common.loginToken);
				System.out.println(token);
				if(checkLogin){
					System.out.println("登录成功!");
					return true;
				}
			}
		}
		System.out.println("登录失败!");//跳转到登录页
		if(Common.FIRST_LOGIN_FLAG){
			request.getRequestDispatcher("/firstlogin").forward(request,response);//如果是第一次登录
			return true;
		}
//		response.sendRedirect("/login");//跳转到登录页
		request.getRequestDispatcher("/login").forward(request,response);//转发到新的页面
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
