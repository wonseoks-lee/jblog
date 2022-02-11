package com.poscoict.jblog.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.security.Auth;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.service.CategoryService;
import com.poscoict.jblog.service.PostService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

// asset을 뺴는 정규표현식 
// .* -> 모든 문자열을 빼겠다.
// (?!aseests) -> assets 을 제외하고 모두 들어온다
@Controller
@RequestMapping("/{blogId:(?!assets)(?!images).*}")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired 
	private PostService postService;
	
	@RequestMapping(value = {"", "/{categoryNo}", "/{categoryNo}/{postNo}"}, method = RequestMethod.GET)
	public String blogMain(
			@PathVariable("blogId") String blogId,
			@PathVariable(required=false) Long categoryNo,
			@PathVariable(required=false) Long postNo,
			Model model) {
		blogService.getBlog(blogId, categoryNo, postNo, model);
		return "blog/blog-main";
	}
	
	@Auth
	@RequestMapping(value = {"/admin","/admin/basic"}, method = RequestMethod.GET)
	public String adminBasic(@PathVariable("blogId") String blogId, Model model) {
		blogService.viewMain(blogId, model);

		return "blog/blog-admin-basic";
	}
	
	@Auth
	@RequestMapping(value = "admin/update", method = RequestMethod.POST)
	public String update(
			@PathVariable("blogId") String blogId,
			@RequestParam(value="logo-file")MultipartFile multipartFile,
			BlogVo blogVo,
			Model model) {
		blogService.updateBlog(blogId, blogVo, multipartFile, model);
		return "redirect:/{blogId}/admin/basic";
	}
	
	@Auth
	@RequestMapping(value="admin/write", method=RequestMethod.GET)
	public String adminWrite(@PathVariable("blogId") String blogId, Model model) {
		blogService.viewMain(blogId, model);
		categoryService.getCategoryList(blogId, model);

		return "blog/blog-admin-write";
	}
	
	@Auth
	@RequestMapping(value="/admin/post/write", method=RequestMethod.POST)
	public String postWrite(@PathVariable("blogId") String blogId, PostVo postVo) {
		postService.write(postVo);
		return "redirect:/{blogId}";
	}
	
	@Auth
	@RequestMapping(value="/admin/category", method=RequestMethod.GET)
	public String adminCategory(@PathVariable("blogId") String blogId, Model model) {
		blogService.viewMain(blogId, model);
		postService.getPostCount(blogId, model);
		
		return "blog/blog-admin-category";
	}
	
	@Auth
	@RequestMapping(value="/admin/category/write", method=RequestMethod.POST)
	public String categoryWrite(@PathVariable("blogId") String blogId, CategoryVo categoryVo) {
		categoryService.addCategory(categoryVo);
		
		return "redirect:/{blogId}/admin/category";
	}
	
	
	@Auth
	@RequestMapping(value="admin/category/delete/{no}", method=RequestMethod.GET)
	public String delete(
			@PathVariable("blogId") String blogId,
			@PathVariable("no") Long no) {
		categoryService.deleteCategory(no);
		return "redirect:/{blogId}/admin/category";
	}
	
}
