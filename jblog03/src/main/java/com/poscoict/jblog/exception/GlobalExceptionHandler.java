package com.poscoict.jblog.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Log LOGGER = LogFactory.getLog(GlobalExceptionHandler.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@ExceptionHandler(Exception.class)
	public String ExceptionHandler(Model model, Exception e) {
		//1. 로깅
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		LOGGER.error(errors.toString());
		
		if(e instanceof HttpRequestMethodNotSupportedException) {
			return request.getContextPath();
		}
		
		model.addAttribute("exception", errors.toString());
		
		//2. 사과페이지 (HTML 응답, 정상 종료)
		return "error/exception";
		
	}
}
