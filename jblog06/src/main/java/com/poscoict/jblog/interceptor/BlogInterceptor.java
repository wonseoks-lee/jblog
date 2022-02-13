package com.poscoict.jblog.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.BlogVo;

@SuppressWarnings("deprecation")
public class BlogInterceptor extends HandlerInterceptorAdapter{
	@Autowired
	private BlogService blogService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> user_id = (Map<String, String>) request.getAttribute( HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		// uri에서 pathvariable이 null 일 경우 먼저 처리 해준다.
		if(user_id == null) {
			return true;
		}
		
		// 블로그 아이디접근이 아닌 ~/jblog 까지만 접근했을 시에 ...
		if(user_id.get("blogId") == null) {
			return true;
		}
		
		
		// blogId로 url 접근시, blogVo 객체를 불러오고 해당하는 아이디의 블로그가 존재하지 않을 시, 메인으로 리다이렉트 
		String blogId = user_id.get("blogId");
		BlogVo blogVo = (BlogVo)blogService.viewMain(blogId);
		if(blogVo == null) {
			response.sendRedirect(request.getContextPath());
			return false;
		}
		
		// 정상적인 요청시, blogVo객체를 던짐
		request.setAttribute("blogVo", blogVo);
		return true;
	}
}
