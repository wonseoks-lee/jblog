package com.poscoict.jblog.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.UserVo;



@Controller
@RequestMapping("/{id}")
public class BlogController {
	
	@Autowired
	private HttpSession session; 
	
	@Autowired
	private BlogService blogService;
	
	@RequestMapping("")
	public String blogmain(@PathVariable("id") String id, Model model) {
		BlogVo vo= blogService.viewMain(id);
		model.addAttribute("blogVo", vo);
		return "blog/blog-main";
	}
	
	@RequestMapping("/admin")
	public String adminmain(@PathVariable("id") String id, Model model) {
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		BlogVo blogVo= blogService.viewMain(id);
		model.addAttribute("blogVo", blogVo);
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if(authUser==null||!authUser.getId().equals(id)) {
			return "redirect:/{id}";
		}
		return "blog/blog-main";
	}
	
}
